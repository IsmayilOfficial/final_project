package ut.ee.cs.myshop

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ut.ee.cs.myshop.ShoppingActivity
import ut.ee.cs.myshop.adapters.ShopRecyclerViewAdapter
import ut.ee.cs.myshop.entities.ProductObject
import ut.ee.cs.myshop.helpers.SpacesItemDecoration
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
    }

    private val allProductsOnSale: List<ProductObject>
        private get() {
            val products: MutableList<ProductObject> = ArrayList()
            products.add(ProductObject(1, "Sleek Black Top", R.drawable.productonesmall, "Beautiful sleek black top for casual outfit and evening walk", 20.0, 38, "Black"))
            products.add(ProductObject(1, "Flare Black Gown", R.drawable.producttwo, "Beautiful sleek black top for casual outfit and evening walk", 20.0, 38, "Black"))
            products.add(ProductObject(1, "Flare White Blouse", R.drawable.productthree, "Beautiful sleek black top for casual outfit and evening walk", 20.0, 38, "White"))
            products.add(ProductObject(1, "Blue Swed Gown", R.drawable.productfour, "Beautiful sleek black top for casual outfit and evening walk", 20.0, 38, "Dark Blue"))
            products.add(ProductObject(1, "Spotted Gown", R.drawable.productfive, "Beautiful sleek black top for casual outfit and evening walk", 20.0, 38, "Spotted Green"))
            products.add(ProductObject(1, "Flare Wax Gown", R.drawable.productsix, "Beautiful sleek black top for casual outfit and evening walk", 20.0, 38, "Multi-color"))
            return products
        }

    companion object {
        private val TAG = ShoppingActivity::class.java.simpleName
    }
}