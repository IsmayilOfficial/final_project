package ut.ee.cs.rsg

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import ut.ee.cs.myshop.adapters.ShopRVA
import ut.ee.cs.rsg.authentification.Profile
import ut.ee.cs.rsg.entities.ProductObject
import ut.ee.cs.rsg.helpers.SpacesItemDecoration
import java.util.*


class ShoppingActivity : AppCompatActivity() {

    var scroll=0
    var singleTap=0
    var  doubleTap=0
    private var mGestureDetector: GestureDetectorCompat? = null
    val ra: UUID = UUID.randomUUID()


    private var shoppingRecyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_shopping)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        shoppingRecyclerView = findViewById<View>(R.id.product_list) as RecyclerView
        val mGrid = GridLayoutManager(this@ShoppingActivity, 2)
        shoppingRecyclerView!!.layoutManager = mGrid
        shoppingRecyclerView!!.setHasFixedSize(true)
        shoppingRecyclerView!!.addItemDecoration(SpacesItemDecoration(2, 12, false))
        val shopAdapter = ShopRVA(this@ShoppingActivity, allProductsOnSale)
        shoppingRecyclerView!!.adapter = shopAdapter
        Handler().postDelayed({





            val intent = Intent(this, SRUNDS::class.java)


            startActivity(intent)
        },10000)


        Thread {

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




        }.start()
        when {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    !== PackageManager.PERMISSION_GRANTED
            -> {
                ActivityCompat.requestPermissions( this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1 )






            }
        }
        mGestureDetector = GestureDetectorCompat(this, GestureListener())

    }

    private val allProductsOnSale: List<ProductObject>
        private get() {
            val products: MutableList<ProductObject> = ArrayList()
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 1020.0, 16, "Black"))
            products.add(ProductObject(1, "Acer", R.drawable.acer1, "ACER ASPIRE 3 A314-32-C2VP 14\" LAPTOP OXIDANT RED (N4000, 4GB, 500GB, INTEL, W10)", 102.0, 16 , "Black"))
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 1200.0, 14, "White"))
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 1200.0, 16, "Dark Blue"))
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 1020.0, 16, "Spotted Green"))
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 1020.0, 16, "Multi-color"))
            products.add(ProductObject(1, "Acer", R.drawable.acer1, "ACER ASPIRE 3 A314-32-C2VP 14\" LAPTOP OXIDANT RED (N4000, 4GB, 500GB, INTEL, W10)", 200.0, 16, "Black"))
            products.add(ProductObject(1, "Acer", R.drawable.acer1, "ACER ASPIRE 3 A314-32-C2VP 14\" LAPTOP OXIDANT RED (N4000, 4GB, 500GB, INTEL, W10)", 200.0, 15, "Black"))
            products.add(ProductObject(1, "Acer", R.drawable.acer1, "ACER ASPIRE 3 A314-32-C2VP 14\" LAPTOP OXIDANT RED (N4000, 4GB, 500GB, INTEL, W10)", 200.0, 14, "Black"))
            products.add(ProductObject(1, "Acer", R.drawable.acer1, "ACER ASPIRE 3 A314-32-C2VP 14\" LAPTOP OXIDANT RED (N4000, 4GB, 500GB, INTEL, W10)", 200.0, 16, "Black"))
            return products
        }

    companion object {
        private val TAG = ShoppingActivity::class.java.simpleName
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_secondary,menu);

        return true
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId;

        when (id) {
            R.id.add_dark -> {

                val startActivityIntent = Intent(this, ut.ee.cs.rsg.theme::class.java)
                startActivity(startActivityIntent)
                finish()

                return true}
            R.id.add_action -> {
                val startActivityIntent = Intent(this, Profile::class.java)
                startActivity(startActivityIntent)
                finish()


                return true
            }
            else -> {

                MyApplication.instance?.clearApplicationData();



                return true
            }
        }

        }


    }

