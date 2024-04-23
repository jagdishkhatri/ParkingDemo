package com.app.parkingdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.app.parkingdemo.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import android.content.SharedPreferences
import com.rbddevs.splashy.Splashy


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        clickListener()
        setTextWatchers()
    }

    private fun setTextWatchers() {
        binding.mobileEditText.addTextChangedListener {
            binding.mobileLayout.error = ""
        }
        binding.passcodeEditText.addTextChangedListener {
            binding.passcodeLayout.error = ""
        }
    }

    private fun clickListener() {
        binding.containedButton.setOnClickListener {
            if (binding.mobileEditText.text.toString().length != 10){
                binding.mobileLayout.error = "please enter 10 digits"
            }else if (binding.passcodeEditText.text.toString().length != 4){
                binding.passcodeLayout.error = "please add 4 digit passcode"
            }else{
                val db = Firebase.firestore
                if (Constant.isNetwork(applicationContext)) {
                    val dialog = Constant.progressDialog(this@MainActivity, "Loading")
                    dialog.show()
                    db.collection("Users")
                        .document(binding.mobileEditText.text.toString())
                        .get().addOnCompleteListener {
                            if (it.isSuccessful) {
                                val document: DocumentSnapshot? = it.result
                                if (document?.exists() == true) {
                                    Log.d(">>>>>>", "Document exists!")
                                    val user = document.toObject(User::class.java)
                                    dialog.dismiss()
                                    if (user?.passcode == binding.passcodeEditText.text.toString()){
                                        // next screen
                                        val editor = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE).edit()
                                        editor.putString("mobile_no", binding.mobileEditText.text.toString())
                                        editor.apply()

                                        val intent = Intent(this,UserParkingListActivity::class.java)
                                        intent.putExtra("mobile_no",binding.mobileEditText.text.toString())
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        binding.passcodeLayout.error = "passcode wrong"
                                    }
                                } else {
                                    val user = User()
                                    user.mobile_no = binding.mobileEditText.text.toString()
                                    user.passcode = binding.passcodeEditText.text.toString()
                                    Log.d(">>>>>", "Document does not exist!")
                                    db.collection("Users")
                                        .document(binding.mobileEditText.text.toString())
                                        .set(user)
                                    // next screen
                                    dialog.dismiss()
                                    val editor = getSharedPreferences(Constant.MY_PREFS_NAME, MODE_PRIVATE).edit()
                                    editor.putString("mobile_no", binding.mobileEditText.text.toString())
                                    editor.apply()

                                    val intent = Intent(this,UserParkingListActivity::class.java)
                                    intent.putExtra("mobile_no",binding.mobileEditText.text.toString())
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                }else {
                    Toast.makeText(applicationContext, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}