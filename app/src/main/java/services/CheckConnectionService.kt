package services

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

class CheckConnectionService() {

    companion object {
        fun checkConnection(application: Application): Boolean {
            val cm =
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo ?: return false
            return activeNetwork.isConnectedOrConnecting
        }
    }

}