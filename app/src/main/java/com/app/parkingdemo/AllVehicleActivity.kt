package com.app.parkingdemo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.app.parkingdemo.databinding.ActivityAllVehicleBinding
import com.app.parkingdemo.databinding.ActivityUserParkingListBinding
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllVehicleActivity : AppCompatActivity() {
    private lateinit var allVehicleAdaoter: AllVehicleAdapter
    private lateinit var binding: ActivityAllVehicleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_vehicle)
        val actionbar = supportActionBar
        actionbar!!.title = "Search"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        getFirestoreData()
        searchData()
    }

    private fun searchData() {
        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (this@AllVehicleActivity::allVehicleAdaoter.isInitialized){
                    allVehicleAdaoter.filter.filter(s.toString())
                }
            }
        })
    }

    private fun getFirestoreData() {
        val db = Firebase.firestore
        if (Constant.isNetwork(applicationContext)) {
                db.collection("All")
                    .addSnapshotListener { documentSnapshot, _ ->
                        if (documentSnapshot != null && documentSnapshot.documents.isNotEmpty()) {
                            val semesterList = ArrayList<UserVehicle>()
                            val documents = documentSnapshot.documents
                            for (value in documents) {
                                val semesters: Vehicle = value.toObject(Vehicle::class.java)!!
                                semesterList.add(UserVehicle(value.id, semesters))
                                Log.e(">>>>>", "onEvent: document id : ${value.id}")
                            }
                            allVehicleAdaoter =
                                AllVehicleAdapter(semesterList, applicationContext)
                            binding.allVehicleRecyclerview.adapter = allVehicleAdaoter
                            binding.allVehicleRecyclerview.isVisible = true
                            binding.tvNothingFound.isVisible = false
                        } else {
                            binding.allVehicleRecyclerview.isVisible = false
                            binding.tvNothingFound.isVisible = true
                        }
                    }
        } else {
            Toast.makeText(applicationContext, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}