package com.app.parkingdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.app.parkingdemo.databinding.ActivityAddEditVehicleBinding
import com.app.parkingdemo.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.content.SharedPreferences
import android.content.Intent
import android.text.InputType
import android.util.Log
import androidx.core.widget.addTextChangedListener

import com.google.firebase.firestore.DocumentReference

import com.google.android.gms.tasks.OnSuccessListener

class AddEditVehicleActivity : AppCompatActivity() {
    private var isEdit: Boolean = false
    private lateinit var userVehicle: UserVehicle
    private lateinit var binding: ActivityAddEditVehicleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_vehicle)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Add Details"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        intent.extras?.let {
            isEdit = true
            userVehicle = it.getParcelable("vehicle")!!
            actionbar.title = "Edit Details"
            setData(userVehicle)
            Log.e(">>>>>>", "onCreate: house no :  ${userVehicle.vehicle.house_no}" )
        }
        initUI()
        clickListener()
        setTextWatchers()
    }

    private fun initUI() {
        binding.rtoEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        binding.serialCodeEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    }

    private fun setTextWatchers() {
        binding.mobileEditText.addTextChangedListener {
            binding.mobileLayout.error = ""
        }
        binding.firstEditText.addTextChangedListener {
            binding.firstLayout.error = ""
        }
        binding.lastEditText.addTextChangedListener {
            binding.lastLayout.error = ""
        }
        binding.houseEditText.addTextChangedListener {
            binding.houseLayout.error = ""
        }
        binding.rtoEditText.addTextChangedListener {
            binding.rtoLayout.error = ""
        }
        binding.rtoCodeEditText.addTextChangedListener {
            binding.rtoCodeLayout.error = ""
        }
        binding.serialCodeEditText.addTextChangedListener {
            binding.serialCodeLayout.error = ""
        }
        binding.vehicleNoEditText.addTextChangedListener {
            binding.vehicleNoLayout.error = ""
        }
    }

    private fun clickListener() {
        binding.containedButton.setOnClickListener {
            if (binding.firstEditText.text.toString().trim().isEmpty()){
                binding.firstLayout.error = "please enter first name"
            }else if (binding.lastEditText.text.toString().trim().isEmpty()){
                binding.lastLayout.error = "please enter last name"
            }else if (binding.houseEditText.text.toString().isEmpty()){
                binding.houseLayout.error = "please enter house number"
            }else if (binding.mobileEditText.text.toString().length != 10){
                binding.mobileLayout.error = "please enter 10 digits"
            }else if (binding.rtoEditText.text.toString().length != 2){
                binding.rtoLayout.error = "please enter 2 characters like GJ"
            }else if (binding.rtoCodeEditText.text.toString().length != 2){
                binding.rtoCodeLayout.error = "please enter 2 digit like 01"
            }else if (binding.serialCodeEditText.text.toString().length != 2){
                binding.serialCodeLayout.error = "please enter 2 characters like GJ"
            }else if (binding.vehicleNoEditText.text.toString().length != 4){
                binding.vehicleNoLayout.error = "please enter 4 characters like 0001"
            }else{
                if (Constant.isNetwork(applicationContext)) {
                    val dialog = Constant.progressDialog(this@AddEditVehicleActivity, "Loading")
                    dialog.show()
                    val db = Firebase.firestore
                    val prefs = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE)
                    val mobile_no = prefs.getString("mobile_no", "").toString()
                    val vehicle = Vehicle()
                    vehicle.first_name = binding.firstEditText.text.toString().trim()
                    vehicle.last_name = binding.lastEditText.text.toString().trim()
                    vehicle.house_no = binding.houseEditText.text.toString()
                    vehicle.mobile_no = binding.mobileEditText.text.toString()
                    vehicle.vehicle_rto = binding.rtoEditText.text.toString()
                    vehicle.vehicle_rto_code = binding.rtoCodeEditText.text.toString()
                    vehicle.vehicle_serial_code= binding.serialCodeEditText.text.toString()
                    vehicle.vehicle_no = binding.vehicleNoEditText.text.toString()
                    if (isEdit) {
                        // edit code
                        db.collection("Users")
                            .document(mobile_no)
                            .collection("Vehicles").document(userVehicle.document_id).set(vehicle)
                        db.collection("All").document(userVehicle.document_id).set(vehicle)
                        dialog.dismiss()
                        finish()
                    } else {
                        // add code
                        db.collection("Users")
                            .document(mobile_no)
                            .collection("Vehicles")
                            .add(vehicle).addOnSuccessListener { documentReference ->
                                val id = documentReference.id
                                db.collection("All").document(id).set(vehicle)
                            }
                        dialog.dismiss()
                        finish()
                    }
                }else {
                    Toast.makeText(applicationContext, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setData(userVehicle: UserVehicle) {
        binding.firstEditText.setText(userVehicle.vehicle.first_name)
        binding.lastEditText.setText(userVehicle.vehicle.last_name)
        binding.houseEditText.setText(userVehicle.vehicle.house_no)
        binding.mobileEditText.setText(userVehicle.vehicle.mobile_no)

        binding.rtoEditText.setText(userVehicle.vehicle.vehicle_rto)
        binding.rtoCodeEditText.setText(userVehicle.vehicle.vehicle_rto_code)
        binding.serialCodeEditText.setText(userVehicle.vehicle.vehicle_serial_code)
        binding.vehicleNoEditText.setText(userVehicle.vehicle.vehicle_no)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}