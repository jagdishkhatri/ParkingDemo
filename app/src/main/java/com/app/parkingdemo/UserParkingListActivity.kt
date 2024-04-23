package com.app.parkingdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import com.app.parkingdemo.databinding.ActivityUserParkingListBinding
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible


class UserParkingListActivity : AppCompatActivity() {
    private var mobile_no: String? = null
    private lateinit var binding: ActivityUserParkingListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_parking_list)
        intent.extras?.let {
            mobile_no = it.getString("mobile_no")
            Log.e(">>>>>", "onCreate: $mobile_no")
        }

        val actionbar = supportActionBar
        actionbar!!.title = "Welcome $mobile_no"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        getFirestoreData()
        binding.add.setOnClickListener {
            val intent = Intent(this,AddEditVehicleActivity::class.java)
            startActivity(intent)
        }
        binding.search.setOnClickListener {
            val intent = Intent(this,AllVehicleActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getFirestoreData() {
        val db = Firebase.firestore
        if (Constant.isNetwork(applicationContext)) {
            mobile_no?.let {
                db.collection("Users")
                    .document(it).collection("Vehicles")
                    .addSnapshotListener(object : EventListener<QuerySnapshot?>,
                        UserVehicleAdapter.onClickListener {
                        override fun onEvent(
                            @Nullable documentSnapshot: QuerySnapshot?,
                            @Nullable e: FirebaseFirestoreException?
                        ) {
                            if (documentSnapshot != null && documentSnapshot.documents.isNotEmpty()) {
                                val semesterList = ArrayList<UserVehicle>()
                                val documents = documentSnapshot.documents
                                for (value in documents) {
                                    val semesters: Vehicle = value.toObject(Vehicle::class.java)!!
                                    semesterList.add(UserVehicle(value.id,semesters))
                                    Log.e(">>>>>", "onEvent: document id : ${value.id}" )
                                }
                                val userVehicleAdaoter = UserVehicleAdapter(semesterList, applicationContext,this)
                                binding.userVehicleRecyclerview.adapter = userVehicleAdaoter
                                binding.userVehicleRecyclerview.isVisible = true
                                binding.tvNothingFound.isVisible = false
                            }else{
                                binding.userVehicleRecyclerview.isVisible = false
                                binding.tvNothingFound.isVisible = true
                            }
                        }

                        override fun mobileClicked(userVehicle: UserVehicle) {
                            Log.e(">>>>>>>>>", "mobileClicked: ${userVehicle.vehicle.mobile_no}" )
                            val intentDial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${userVehicle.vehicle.mobile_no}"))
                            startActivity(intentDial)
                        }

                        override fun editClicked(userVehicle: UserVehicle) {
                            Log.e(">>>>>>>>>", "editClicked: ${userVehicle.vehicle.mobile_no}" )
                            val intent = Intent(this@UserParkingListActivity,AddEditVehicleActivity::class.java)
                            intent.putExtra("vehicle",userVehicle)
                            startActivity(intent)
                        }

                        override fun deleteClicked(userVehicle: UserVehicle) {
                            Log.e(">>>>>>>>>", "deleteClicked: ${userVehicle.vehicle.mobile_no}" )
                            deleteDialog(userVehicle)
                        }
                    })
            }
        }else {
            Toast.makeText(applicationContext, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteDialog(userVehicle: UserVehicle) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure want to delete?")
        builder.setIcon(R.drawable.ic_delete)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            val db = Firebase.firestore
            val prefs = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE)
            val mobile_no = prefs.getString("mobile_no", "").toString()
            db.collection("Users")
                .document(mobile_no)
                .collection("Vehicles").document(userVehicle.document_id).delete()
            db.collection("All").document(userVehicle.document_id).delete()
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}