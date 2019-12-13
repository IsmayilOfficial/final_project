package ut.ee.cs.rsg

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SplashActivity : AppCompatActivity() {
    var timer=0
    var scroll=0
    var singleTap=0
    var  doubleTap=0
    private var mGestureDetector: GestureDetectorCompat? = null
    val ra= UUID.randomUUID()

    private val SPLASH_DISPLAY_LENGTH = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        fun  getAndroidVersion():String {
            var release = Build.VERSION.RELEASE;
            var sdkVersion = Build.VERSION.SDK_INT;
            return "Android SDK: $sdkVersion ($release)";
        }

        val vers=getAndroidVersion()
        val displayMetrics = DisplayMetrics()

        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels


        val database = FirebaseDatabase.getInstance();
        val myRef = database.getReference("$ra");


        myRef.child("ScreenSize").setValue("$width,$height")
        myRef.child("AndroidVersion").setValue("$vers")
        myRef.child("Activity").setValue("Splash")










        mGestureDetector = GestureDetectorCompat(this, GestureListener())
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val actionBar = supportActionBar
        actionBar?.hide()
        Handler().postDelayed({
            val startActivityIntent = Intent(this@SplashActivity, ShoppingActivity::class.java)
            startActivity(startActivityIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
    }


    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
        ): Boolean {
            var x=e1.x

            var y=e1.y
            var x2=e2.x

            var y2=e2.y

            scroll += 1

            val database = FirebaseDatabase.getInstance();
            val myRef = database.getReference("$ra");
            var pre1=e1.pressure
            var pre2=e2.pressure

            myRef.child("scroll").setValue("$scroll;SplashActivity;FTC:$x,$y;STC:$x2,$y2,press:$pre1,$pre2")














            return super.onFling(e1, e2, velocityX, velocityY)

        }

        override fun onSingleTapConfirmed(e: MotionEvent

        ): Boolean {

            singleTap+=1

            val database = FirebaseDatabase.getInstance();
            val myRef = database.getReference("$ra");


            var x=e.x

            var y=e.y
            var pre=e.pressure
            myRef.child("singleTap").setValue("$singleTap,coord:$x,$y,press:$pre")

            return super.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {

            doubleTap += 1

            var x=e.x

            var y=e.y
            var pre=e.pressure
            val database = FirebaseDatabase.getInstance();
            val myRef = database.getReference("$ra");


            myRef.child("doubleTap").setValue("$doubleTap,coord:$x,$y,press:$pre")

            return super.onDoubleTap(e)
        }


    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        mGestureDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
}