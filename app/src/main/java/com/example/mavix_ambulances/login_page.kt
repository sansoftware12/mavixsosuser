package com.example.mavix_ambulances

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class login_page : AppCompatActivity() {
    lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    lateinit var btnLogin: Button
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in)
        btnLogin = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.inputEmail)
        etPass = findViewById(R.id.inputPassword)

        auth= FirebaseAuth.getInstance()
        btnLogin.setOnClickListener {
            login()
            val intent = Intent(this,Dashboard::class.java)
            startActivity(intent)
        }
    }
    private fun login(){
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
        }
    }
}