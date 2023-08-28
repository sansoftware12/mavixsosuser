package com.example.mavix_ambulances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.*
import com.example.mavix_ambulances.Driver


class DriverListFragment : BottomSheetDialogFragment() {

    private lateinit var driverRecyclerView: RecyclerView
    private lateinit var driverAdapter: DriverAdapter
    private val driversList: ArrayList<Driver> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_driver_list, container, false)

        driverRecyclerView = view.findViewById(R.id.driverRecyclerView)
        driverRecyclerView.layoutManager = LinearLayoutManager(context)
        driverAdapter = DriverAdapter(driversList)
        driverRecyclerView.adapter = driverAdapter

        fetchDriverListFromDatabase()

        return view
    }

    private fun fetchDriverListFromDatabase() {
        val driversReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("drivers")

        driversReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                driversList.clear()
                for (driverSnapshot in dataSnapshot.children) {
                    val driverId = driverSnapshot.key.toString()
                    val email = driverSnapshot.child("email").value.toString() // Explicitly cast to non-null String
                    val latitude = driverSnapshot.child("latitude").value as Double
                    val longitude = driverSnapshot.child("longitude").value as Double

                    val driver = Driver(driverId, email, latitude, longitude)
                    driversList.add(driver)
                }
                driverAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if needed
            }
        })
    }


}