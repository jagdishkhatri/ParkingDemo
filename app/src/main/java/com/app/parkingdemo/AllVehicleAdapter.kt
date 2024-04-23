package com.app.parkingdemo


import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList
import android.text.style.UnderlineSpan

import android.text.SpannableString




open class AllVehicleAdapter(
    private var items: ArrayList<UserVehicle>,
    private val applicationContext: Context
) :
    RecyclerView.Adapter<AllVehicleAdapter.NewViewHolder>(),Filterable {
    var filterItems : MutableList<UserVehicle> = items
    class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFirstName: TextView = itemView.findViewById(R.id.tvFirstName)
        var tvLastName: TextView = itemView.findViewById(R.id.tvLastName)
        var tvHouseNo: TextView = itemView.findViewById(R.id.tvHouseNo)
        var tvMobileNo: TextView = itemView.findViewById(R.id.tvMobileNo)
        var tvVehicleNo: TextView = itemView.findViewById(R.id.tvVehicleNo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.child_all_vehicle, parent, false)
        return NewViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val vehicle = filterItems[position].vehicle
        val vehicleNo = vehicle.vehicle_rto+vehicle.vehicle_rto_code+vehicle.vehicle_serial_code+vehicle.vehicle_no
        holder.tvFirstName.text = String.format(applicationContext.getString(R.string.first_name),vehicle.first_name)
        holder.tvLastName.text = String.format(applicationContext.getString(R.string.last_name),vehicle.last_name)
        holder.tvHouseNo.text = String.format(applicationContext.getString(R.string.house_no),vehicle.house_no)
        holder.tvVehicleNo.text = String.format(applicationContext.getString(R.string.vehicle_no),vehicleNo)
        val content = SpannableString(vehicle.mobile_no)
        content.setSpan(UnderlineSpan(), 0, vehicle.mobile_no.length, 0)
        holder.tvMobileNo.text =content
        holder.tvMobileNo.setOnClickListener {
            val intentDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${vehicle.mobile_no}"))
            intentDial.flags = FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intentDial)
        }
    }

    override fun getItemCount(): Int {
        return filterItems.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filterItems = items
                } else {
                    val filteredList: MutableList<UserVehicle> = ArrayList()
                    for (row in items) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.vehicle.vehicle_no.contains(charString.lowercase(Locale.getDefault())) ) {
                            filteredList.add(row)
                        }
                    }
                    filterItems = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filterItems
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                filterItems = filterResults.values as ArrayList<UserVehicle>
                notifyDataSetChanged()
            }
        }
    }
}