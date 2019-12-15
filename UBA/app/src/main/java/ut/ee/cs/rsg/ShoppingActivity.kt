package ut.ee.cs.rsg

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ut.ee.cs.myshop.adapters.ShopRecyclerViewAdapter
import ut.ee.cs.rsg.entities.ProductObject
import ut.ee.cs.rsg.helpers.SpacesItemDecoration
import java.util.*

class ShoppingActivity : AppCompatActivity() {
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
        val shopAdapter = ShopRecyclerViewAdapter(this@ShoppingActivity, allProductsOnSale)
        shoppingRecyclerView!!.adapter = shopAdapter
        Handler().postDelayed({



            val intent = Intent(this, SRUNDS::class.java)


            startActivity(intent)
        },10000)

    }

    private val allProductsOnSale: List<ProductObject>
        private get() {
            val products: MutableList<ProductObject> = ArrayList()
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 20.0, 38, "Black"))
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 20.0, 38, "Black"))
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 20.0, 38, "White"))
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 20.0, 38, "Dark Blue"))
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 20.0, 38, "Spotted Green"))
            products.add(ProductObject(1, "MacBook Pro Retina 15", R.drawable.macbook2019, "MacBook Pro Retina 15 Inch 256 GB Flash (SSD) 2.4 Ghz 8 GB (1600 Mhz) - B grade - Refurbished", 20.0, 38, "Multi-color"))
            return products
        }

    companion object {
        private val TAG = ShoppingActivity::class.java.simpleName
    }
}