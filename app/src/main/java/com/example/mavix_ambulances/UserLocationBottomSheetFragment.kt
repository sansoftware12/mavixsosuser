package com.example.mavix_ambulances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserLocationBottomSheetFragment : BottomSheetDialogFragment() {

     companion object {
        private const val ARG_LOCATION = "location"
        private const val ARG_DISTANCE = "distance"
        private const val ARG_PHONE_NUMBER = "phone_number"

        fun newInstance(location: String, distance: String, phoneNumber: String): UserLocationBottomSheetFragment {
            val fragment = UserLocationBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_LOCATION, location)
            args.putString(ARG_DISTANCE, distance)
            args.putString(ARG_PHONE_NUMBER, phoneNumber)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var sendSOSButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_location_bottom_sheet, container, false)

        val textLocation: TextView = view.findViewById(R.id.textLocation)
        val textDistance: TextView = view.findViewById(R.id.textDistance)
        val textPhoneNumber: TextView = view.findViewById(R.id.driver_phone)
        sendSOSButton = view.findViewById(R.id.button)

        arguments?.let {
            val location = it.getString(ARG_LOCATION)
            val distance = it.getString(ARG_DISTANCE)
            val phoneNumber = it.getString(ARG_PHONE_NUMBER)

            textLocation.text = location
            textDistance.text = distance
            textPhoneNumber.text = "Driver's Phone: $phoneNumber"
        }

        // Add click listener to the "Send SOS" button
        sendSOSButton.setOnClickListener {
            // Call the function to send SOS to the driver here
            // For example, you can use an interface to communicate with the activity and then send SOS to the driver
            // For simplicity, I'll just show a toast message here
            showToast("SOS Sent to Driver")
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}