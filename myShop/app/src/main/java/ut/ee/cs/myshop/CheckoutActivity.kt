package ut.ee.cs.myshop

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import ut.ee.cs.myshop.CheckoutActivity
import ut.ee.cs.myshop.adapters.CheckRecyclerViewAdapter
import ut.ee.cs.myshop.entities.ProductObject
import ut.ee.cs.myshop.helpers.MySharedPreference
import ut.ee.cs.myshop.helpers.SimpleDividerItemDecoration
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    private var checkRecyclerView: RecyclerView? = null
    private var subTotal: TextView? = null
    private var mSubTotal = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        title = "Over Cart"
        subTotal = findViewById<View>(R.id.sub_total) as TextView
        checkRecyclerView = findViewById<View>(R.id.checkout_list) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(this@CheckoutActivity)
        checkRecyclerView!!.layoutManager = linearLayoutManager
        checkRecyclerView!!.setHasFixedSize(true)
        checkRecyclerView!!.addItemDecoration(SimpleDividerItemDecoration(this@CheckoutActivity))
        // get content of cart
        val mShared = MySharedPreference(this@CheckoutActivity)
        val builder = GsonBuilder()
        val gson = builder.create()
        val addCartProducts = gson.fromJson(mShared.retrieveProductFromCart(), Array<ProductObject>::class.java)
        val productList = convertObjectArrayToListObject(addCartProducts)
        val mAdapter = CheckRecyclerViewAdapter(this@CheckoutActivity, productList)
        checkRecyclerView!!.adapter = mAdapter
        mSubTotal = getTotalPrice(productList)
        subTotal!!.text = "Subtotal excluding tax and shipping: $mSubTotal $"
        val shoppingButton = (findViewById<View>(R.id.shopping) as Button)
        shoppingButton.setOnClickListener {
            val shoppingIntent = Intent(this@CheckoutActivity, ShoppingActivity::class.java)
            startActivity(shoppingIntent)
        }
        val checkButton = (findViewById<View>(R.id.checkout) as Button)
        checkButton.setOnClickListener {
            val paymentIntent = Intent(this@CheckoutActivity, PayPalCheckoutActivity::class.java)
            paymentIntent.putExtra("TOTAL_PRICE", mSubTotal)
            startActivity(paymentIntent)
        }
    }

    private fun convertObjectArrayToListObject(allProducts: Array<ProductObject>): List<ProductObject> {
        val mProduct: MutableList<ProductObject> = ArrayList()
        Collections.addAll(mProduct, *allProducts)
        return mProduct
    }

    private fun returnQuantityByProductName(productName: String, mProducts: List<ProductObject>): Int {
        var quantityCount = 0
        for (i in mProducts.indices) {
            val pObject = mProducts[i]
            if (pObject.productName.trim { it <= ' ' } == productName.trim { it <= ' ' }) {
                quantityCount++
            }
        }
        return quantityCount
    }

    private fun getTotalPrice(mProducts: List<ProductObject>): Double {
        var totalCost = 0.0
        for (i in mProducts.indices) {
            val pObject = mProducts[i]
            totalCost = totalCost + pObject.productPrice
        }
        return totalCost
    }

    companion object {
        private val TAG = CheckoutActivity::class.java.simpleName
    }
}