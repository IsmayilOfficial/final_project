package ut.ee.cs.myshop.network

import android.app.Application
import com.android.volley.RequestQueue

class CustomApplication : Application() {
    var volleyRequestQueue: RequestQueue? = null
        private set

    override fun onCreate() {
        super.onCreate()
        volleyRequestQueue = VolleySingleton.Companion.getInstance(applicationContext)?.requestQueue
    }

}