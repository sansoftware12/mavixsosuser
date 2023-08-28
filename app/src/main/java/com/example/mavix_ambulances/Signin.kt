package com.example.mavix_ambulances

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Signin : AppCompatActivity() {
    private lateinit var etemail: EditText
    private lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var etPhoneNumber: EditText // Added EditText for phone number
    private lateinit var btnSignUp: Button
    private lateinit var login: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        etemail = findViewById(R.id.inputEmail)
        etConfPass = findViewById(R.id.confirm_password)
        etPass = findViewById(R.id.inputPassword)
        etPhoneNumber = findViewById(R.id.inputPhoneNumber) // Initialize EditText for phone number
        btnSignUp = findViewById(R.id.btnsignin)
        login = findViewById(R.id.btnLogin)

        auth = Firebase.auth

        btnSignUp.setOnClickListener {
            signUpUser()
        }

        login.setOnClickListener {
            val intent = Intent(this, login_page::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser() {
        val email = etemail.text.toString()
        val pass = etPass.text.toString()
        val confirmPassword = etConfPass.text.toString()
        val phoneNumber = etPhoneNumber.text.toString() // Get the phone number from EditText

        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank() || phoneNumber.isBlank()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign-up successful
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    // Save the phone number to Firebase Realtime Database
                    val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)
                    val userMap = hashMapOf(
                        "email" to email,
                        "phoneNumber" to phoneNumber
                    )
                    databaseReference.setValue(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show()
                            // Add further logic here, such as navigating to the main activity
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                // Sign-up failed
                Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}