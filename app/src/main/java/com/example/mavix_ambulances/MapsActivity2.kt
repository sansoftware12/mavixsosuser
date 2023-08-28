package com.example.mavix_ambulances

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 123
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var driversReference: DatabaseReference
    private var driverMarkers: HashMap<String, Marker?> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps2)

        firebaseDatabase = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (isLocationPermissionGranted()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun getCurrentLocation() {
        if (isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.lastLocation
                .addOnCompleteListener(this, OnCompleteListener<Location> { task ->
                    if (task.isSuccessful && task.result != null) {
                        val location = task.result
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            val locationData = hashMapOf(
                                "latitude" to latitude,
                                "longitude" to longitude
                            )

                            val databaseReference = firebaseDatabase.reference.child("user_locations")
                            databaseReference.child(userId).setValue(locationData)
                                .addOnSuccessListener {
                                    showToast("Location data sent to Firebase")
                                }
                                .addOnFailureListener { e ->
                                    showToast("Error sending location data: ${e.message}")
                                }
                        }

                        val markerOptions = MarkerOptions()
                            .position(LatLng(latitude, longitude))
                            .title("My Location")
                        mMap.addMarker(markerOptions)
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 12f)
                        mMap.moveCamera(cameraUpdate)

                        // Display nearest driver marker
                        displayNearestDriverMarker(location)
                    } else {
                        showToast("Error getting location")
                    }
                })
        }
    }

    private fun displayNearestDriverMarker(userLocation: Location) {
        driversReference = firebaseDatabase.reference.child("driver_locations")
        driversReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var nearestDriver: Marker? = null
                var nearestDriverDistance = Float.MAX_VALUE
                var nearestDriverPhoneNumber: String? = null // Variable to hold the driver's phone number

                for (driverSnapshot in dataSnapshot.children) {
                    val driverId = driverSnapshot.key
                    val latitude = driverSnapshot.child("latitude").getValue(Double::class.java)
                    val longitude = driverSnapshot.child("longitude").getValue(Double::class.java)
                    val phoneNumber = driverSnapshot.child("phoneNumber").getValue(String::class.java) // Get the driver's phone number

                    if (latitude != null && longitude != null) {
                        val driverLocation = Location("")
                        driverLocation.latitude = latitude
                        driverLocation.longitude = longitude

                        val distance = userLocation.distanceTo(driverLocation)
                        if (distance < nearestDriverDistance) {
                            nearestDriverDistance = distance
                            nearestDriverPhoneNumber = phoneNumber // Update the nearest driver's phone number
                            if (nearestDriver != null) {
                                nearestDriver.remove()
                            }
                            nearestDriver = mMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(latitude, longitude))
                                    .title("Nearest Driver")
                                    .snippet("Distance: ${String.format("%.2f", distance)} meters")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            )
                        }
                    }
                }

                if (nearestDriver != null) {
                    val nearestDriverPosition = nearestDriver.position
                    showDriverLocation(nearestDriverPosition, nearestDriverDistance, nearestDriverPhoneNumber) // Pass the phone number to the function
                    driverMarkers["nearestDriver"] = nearestDriver
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToast("Error retrieving driver locations")
            }
        })
    }

    private fun showDriverLocation(location: LatLng, distance: Float, phoneNumber: String?) {
        driverMarkers["nearestDriver"]?.remove()

        driverMarkers["nearestDriver"] = mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("Nearest Driver")
                .snippet("Distance: ${String.format("%.2f", distance / 1000)} km")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        // Pass the phone number to the UserLocationBottomSheetFragment
        val userLocationBottomSheetFragment = UserLocationBottomSheetFragment.newInstance(
            "Driver Location",
            "Distance: ${String.format("%.2f", distance / 1000)} km",
            phoneNumber ?: "098766554" // Pass the phone number or an empty string if it's null
        )

        userLocationBottomSheetFragment.show(supportFragmentManager, userLocationBottomSheetFragment.tag)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                showToast("Location permission denied")
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove driver markers from the map
        for (marker in driverMarkers.values) {
            marker?.remove()
        }
        driverMarkers.clear()
    }
}