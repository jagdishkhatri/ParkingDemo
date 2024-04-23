package com.app.parkingdemo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Vehicle(var mobile_no : String = "",
              var house_no : String = "",
              var first_name : String = "",
              var last_name : String = "",
              var vehicle_rto : String = "",
              var vehicle_rto_code : String = "",
              var vehicle_serial_code : String = "",
              var vehicle_no : String = "") : Parcelable