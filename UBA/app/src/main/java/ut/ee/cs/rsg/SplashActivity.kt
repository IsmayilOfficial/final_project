package ut.ee.cs.rsg

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GestureDetectorCompat
import com.google.firebase.database.FirebaseDatabase
import ut.ee.cs.rsg.authentification.Login
import java.util.*

class SplashActivity : AppCompatActivity() {





    private val SPLASH_DISPLAY_LENGTH = 1000
    override fun onCreate(savedInstanceState: Bundle?) {



        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)











        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val actionBar = supportActionBar
        actionBar?.hide()
        Handler().postDelayed({
            val startActivityIntent = Intent(this, Login::class.java)
            startActivity(startActivityIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
    }



}