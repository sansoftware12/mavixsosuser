package com.example.mavix_ambulances

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.Driver

class DriverAdapter(private val driversList: ArrayList<com.example.mavix_ambulances.Driver>) :
    RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val driverName: TextView = itemView.findViewById(R.id.driverName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_driver, parent, false)
        return DriverViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val currentDriver = driversList[position]
        holder.driverName.text = currentDriver.email
    }

    override fun getItemCount(): Int {
        return driversList.size
    }
}