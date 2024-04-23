package com.app.parkingdemo


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserVehicleAdapter(
    private var items: ArrayList<UserVehicle>,
    private val applicationContext: Context,
    private val listener: onClickListener
) :
    RecyclerView.Adapter<UserVehicleAdapter.NewViewHolder>() {
    class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFirstName: TextView = itemView.findViewById(R.id.tvFirstName)
        var tvLastName: TextView = itemView.findViewById(R.id.tvLastName)
        var tvHouseNo: TextView = itemView.findViewById(R.id.tvHouseNo)
        var tvMobileNo: TextView = itemView.findViewById(R.id.tvMobileNo)
        var tvVehicleNo: TextView = itemView.findViewById(R.id.tvVehicleNo)
        var editButton: Button = itemView.findViewById(R.id.editButton)
        var deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.child_user_vehicle, parent, false)
        return NewViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val vehicle = items[position].vehicle
        val vehicleNo = vehicle.vehicle_rto+vehicle.vehicle_rto_code+vehicle.vehicle_serial_code+vehicle.vehicle_no
        holder.tvFirstName.text = String.format(applicationContext.getString(R.string.first_name),vehicle.first_name)
        holder.tvLastName.text = String.format(applicationContext.getString(R.string.last_name),vehicle.last_name)
        holder.tvHouseNo.text = String.format(applicationContext.getString(R.string.house_no),vehicle.house_no)
        holder.tvVehicleNo.text = String.format(applicationContext.getString(R.string.vehicle_no),vehicleNo)
        holder.tvMobileNo.text = vehicle.mobile_no
        holder.editButton.setOnClickListener {
            listener.editClicked(items[position])
        }
        holder.deleteButton.setOnClickListener {
            listener.deleteClicked(items[position])
        }
        holder.tvMobileNo.setOnClickListener {
            listener.mobileClicked(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface onClickListener {
        fun mobileClicked(userVehicle: UserVehicle)
        fun editClicked(userVehicle: UserVehicle)
        fun deleteClicked(userVehicle: UserVehicle)
    }

}