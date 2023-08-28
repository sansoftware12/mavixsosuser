package com.example.mavix_ambulances

import java.sql.Connection
import java.sql.Driver
import java.sql.DriverPropertyInfo
import java.util.Properties

data class Driver(
    val driverId: String,
    val email: String,  // Add this field for the driver's email
    val latitude: Double,
    val longitude: Double
)
