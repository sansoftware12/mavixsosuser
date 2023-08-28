package com.example.mavix_ambulances

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }
    fun imageClicked(view: View) {


        val imageId = view.id

        if (imageId == R.id.bus) {
            // Handle click for the "Sprained ankle" image
            // Show relevant first aid measures

            // For example, you can display a dialog with the first aid measures
            AlertDialog.Builder(this)
                .setTitle("Sprained Ankle")
                .setMessage(
                    "First aid measures for a sprained ankle:\n\n" +
                            "- Rest and elevate the injured ankle.\n" +
                            "- Apply ice to reduce swelling.\n" +
                            "- Compress the ankle with a bandage.\n" +
                            "- Seek medical attention if necessary."
                )
                .setPositiveButton("OK", null)
                .show()
        } else if (imageId == R.id.imageView) {
            val intent = Intent(this, MapsActivity2::class.java)
            startActivity(intent)

        } else if (imageId == R.id.bus1) {
            AlertDialog.Builder(this)
                .setTitle("Sprained Ankle")
                .setMessage(
                    "First aid measures for a sprained ankle:\n\n" +
                            "- Rest and elevate the injured ankle.\n" +
                            "- Apply ice to reduce swelling.\n" +
                            "- Compress the ankle with a bandage.\n" +
                            "- Seek medical attention if necessary."
                )
                .setPositiveButton("OK", null)
                .show()

        } else if (imageId == R.id.imageView3) {
            AlertDialog.Builder(this)
                .setTitle("Burn")
                .setMessage(
                    "First aid measures for a Burn:\n\n" +
                            "Cool the burn: For first-degree and some second-degree burns, you can cool the area by running cool (not cold) water over it.\n" +
                            "\n" +
                            "Protect the burn: Once the burn is cooled, you can cover it loosely with a sterile, non-stick dressing .\n" +
                            "\n" +
                            "Manage pain and swelling: Over-the-counter pain relievers, such as acetaminophen or ibuprofen, can help alleviate pain and reduce swelling."
                )
                .setPositiveButton("OK", null)
                .show()

            // Add similar conditions for other images as per your requirements
        }else if (imageId == R.id.bus1) {
            AlertDialog.Builder(this)
                .setTitle("CPR")
                .setMessage(
                    "First aid measures for a CPR:\n\n" +
                            "Cool the burn: For first-degree and some second-degree burns, you can cool the area by running cool (not cold) water over it.\n" +
                            "\n" +
                            "Protect the burn: Once the burn is cooled, you can cover it loosely with a sterile, non-stick dressing .\n" +
                            "\n" +
                            "Manage pain and swelling: Over-the-counter pain relievers, such as acetaminophen or ibuprofen, can help alleviate pain and reduce swelling."
                )
                .setPositiveButton("OK", null)
                .show()

            // Add similar conditions for other images as per your requirements
        }else if (imageId == R.id.bus1) {
            AlertDialog.Builder(this)
                .setTitle("CPR")
                .setMessage(
                    "First aid measures for a CPR:\n\n" +
                            "Cool the burn: For first-degree and some second-degree burns, you can cool the area by running cool (not cold) water over it.\n" +
                            "\n" +
                            "Protect the burn: Once the burn is cooled, you can cover it loosely with a sterile, non-stick dressing .\n" +
                            "\n" +
                            "Manage pain and swelling: Over-the-counter pain relievers, such as acetaminophen or ibuprofen, can help alleviate pain and reduce swelling."
                )
                .setPositiveButton("OK", null)
                .show()

            // Add similar conditions for other images as per your requirements
        }
    }



    }



