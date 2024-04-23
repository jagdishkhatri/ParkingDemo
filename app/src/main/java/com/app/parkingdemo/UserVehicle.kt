package com.app.parkingdemo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserVehicle(var document_id: String = "",var vehicle: Vehicle) : Parcelable