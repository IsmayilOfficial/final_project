package ut.ee.cs.myshop

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ut.ee.cs.myshop.ProductActivity
import ut.ee.cs.myshop.entities.ProductObject
import ut.ee.cs.myshop.helpers.MySharedPreference
import java.util.*

class ProductActivity : AppCompatActivity() {
    private var productSize: TextView? = null
    private var productColor: TextView? = null
    private var productPrice: TextView? = null
    private var productDescription: TextView? = null
    private var productImage: ImageView? = null
    private lateinit var gson: Gson
    private var cartProductNumber = 0
    private var sharedPreference: MySharedPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        sharedPreference = MySharedPreference(this@ProductActivity)
        productImage = findViewById<View>(R.id.full_product_image) as ImageView
        productSize = findViewById<View>(R.id.product_size) as TextView
        productColor = findViewById<View>(R.id.product_color) as TextView
        productPrice = findViewById<View>(R.id.product_price) as TextView
        productDescription = findViewById<View>(R.id.product_description) as TextView
        val builder = GsonBuilder()
        gson = builder.create()
        val productInStringFormat = intent.extras!!.getString("PRODUCT")
        val singleProduct = gson.fromJson(productInStringFormat, ProductObject::class.java)
        if (singleProduct != null) {
            title = singleProduct.productName
            productImage!!.setImageResource(singleProduct.productImage)
            productSize!!.text = "Size: " + singleProduct.productSize.toString()
            productColor!!.text = "Color: " + singleProduct.productColor
            productPrice!!.text = "Price: " + singleProduct.productPrice.toInt().toString() + " $"
            productDescription!!.text = Html.fromHtml("<strong>Product Description</strong><br/>" + singleProduct.productDescription)
        }
        val addToCartButton = (findViewById<View>(R.id.add_to_cart) as Button)
        addToCartButton.setOnClickListener {
            //increase product count
            val productsFromCart = sharedPreference!!.retrieveProductFromCart()
            cartProductNumber = if (productsFromCart == "") {
                val cartProductList: MutableList<ProductObject?> = ArrayList()
                cartProductList.add(singleProduct)
                val cartValue = gson.toJson(cartProductList)
                sharedPreference!!.addProductToTheCart(cartValue)
                cartProductList.size
            } else {
                val productsInCart = sharedPreference!!.retrieveProductFromCart()
                val storedProducts = gson.fromJson(productsInCart, Array<ProductObject>::class.java)
                val allNewProduct = convertObjectArrayToListObject(storedProducts)
                allNewProduct.add(singleProduct)
                val addAndStoreNewProduct = gson.toJson(allNewProduct)
                sharedPreference!!.addProductToTheCart(addAndStoreNewProduct)
                allNewProduct.size
            }
            sharedPreference!!.addProductCount(cartProductNumber)
            invalidateCart()
        }
    }

    private fun convertObjectArrayToListObject(allProducts: Array<ProductObject>): MutableList<ProductObject?> {
        val mProduct: MutableList<ProductObject?> = ArrayList()
        Collections.addAll(mProduct, *allProducts)
        return mProduct
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu.findItem(R.id.action_shop)
        val mCount = sharedPreference!!.retrieveProductCount()
        menuItem.icon = buildCounterDrawable(mCount, R.drawable.cart)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_shop) {
            val checkoutIntent = Intent(this@ProductActivity, CheckoutActivity::class.java)
            startActivity(checkoutIntent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buildCounterDrawable(count: Int, backgroundImageId: Int): Drawable {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.shopping_layout, null)
        view.setBackgroundResource(backgroundImageId)
        if (count == 0) {
            val counterTextPanel = view.findViewById<View>(R.id.counterValuePanel)
            counterTextPanel.visibility = View.GONE
        } else {
            val textView = view.findViewById<View>(R.id.count) as TextView
            textView.text = "" + count
        }
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.isDrawingCacheEnabled = true
        view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return BitmapDrawable(resources, bitmap)
    }

    private fun invalidateCart() {
        invalidateOptionsMenu()
    }

    companion object {
        private val TAG = ProductActivity::class.java.simpleName
    }
}