package ut.ee.cs.rsg

import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_activityx.*
import java.io.File
import java.io.IOException
import java.util.*
import android.os.CountDownTimer
import android.util.DisplayMetrics


class SRUNDS : AppCompatActivity() {
var timer=0
    var scroll=0
    var singleTap=0
    var  doubleTap=0
    private var mGestureDetector: GestureDetectorCompat? = null
    val ra= UUID.randomUUID()

    private var mStorageRef: StorageReference? = null







    private var mRecorder: MediaRecorder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        actionBar?.hide()

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_activityx)


        var countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                timer += 1

            }
            // override object functions here, do it quicker by setting cursor on object, then type alt + enter ; implement members
        }
        countDownTimer.start()


        mFileName = Environment.getExternalStorageDirectory().absolutePath
        mFileName += "/AudioRecording.mp3"





        mRecorder = MediaRecorder()
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

        mRecorder!!.setOutputFile(mFileName)
        try {
            mRecorder!!.prepare()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "prepare() failed")
        }

        mRecorder!!.start()


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


        val firebaseData= myRef

        firebaseData.child("ScreenSize").setValue("$width,$height")
        firebaseData.child("AndroidVersion").setValue("$vers")










        mGestureDetector = GestureDetectorCompat(this, GestureListener())



        image_you_ch!!.setOnClickListener {

            mRecorder!!.stop()
            mRecorder!!.release()
            mRecorder = null




            val database = FirebaseDatabase.getInstance();
            val myRef = database.getReference("$ra");


            myRef.child("closed").setValue("closed:$timer")


            val file = Uri.fromFile(File("/storage/emulated/0/AudioRecording.mp3"))
            mStorageRef = FirebaseStorage.getInstance().reference
            Log.i("path", mFileName!!)

            val ra = UUID.randomUUID().toString()
            val riversRef = mStorageRef!!.child("audio/$ra.mp3")




            riversRef.putFile(file)
                    .addOnSuccessListener {
                        // Get a URL to the uploaded content
                    }
                    .addOnFailureListener {
                        // Handle unsuccessful uploads
                        // ...
                    }

                        finish()



        }




    }


    companion object {
        private val LOG_TAG = "AudioRecording"
        private var mFileName: String? = null

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

            myRef.child("scroll").setValue("$scroll;FTC:$x,$y;STC:$x2,$y2,press:$pre1,$pre2")














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
