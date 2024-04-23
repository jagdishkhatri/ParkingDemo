package com.app.parkingdemo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView

class Constant {
    companion object{
        val MY_PREFS_NAME = "com.app.parkingdemo_pref"
        fun progressDialog(context: Context, title: String): Dialog {
            val dialog = Dialog(context,R.style.AppCompatLoderDialogStyle)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val view = LayoutInflater.from(context).inflate(R.layout.layout_progress, null)
            val tv_progress = view.findViewById<TextView>(R.id.tv_progress)
            tv_progress.text = title
            dialog.setContentView(view)
            dialog.setCancelable(false)
            val width = context.resources.displayMetrics.widthPixels
            val lp = dialog.window!!.attributes
            lp.width = (width / 1.1).toInt()
            dialog.window!!.attributes = lp
            return dialog
        }
        fun isNetwork(context: Context): Boolean {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT > 22) {
                val networkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                if (networkCapabilities != null && networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_INTERNET
                    )
                    && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                ) {
                    // if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    //     return true
                    // } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    //     return true
                    // }
                    return true
                }
                return false
            } else {
                return isNetworkOlderSDK(cm)
            }
        }
        @Suppress("DEPRECATION")
        private fun isNetworkOlderSDK(cm: ConnectivityManager): Boolean {
            val activeNetwork: android.net.NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            //                val isWiFi: Boolean = activeNetwork?.type == ConnectivityManager.TYPE_WIFI
            //                val isMob: Boolean = activeNetwork?.type == ConnectivityManager.TYPE_MOBILE
            if (isConnected) {
                return true
            }
            return false
        }
    }
}