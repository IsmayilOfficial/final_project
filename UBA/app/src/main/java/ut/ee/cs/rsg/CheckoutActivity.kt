package ut.ee.cs.rsg

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.check_layout.*
import ut.ee.cs.rsg.adapters.CheckRVA
import ut.ee.cs.rsg.entities.ProductObject
import ut.ee.cs.rsg.helpers.MySharedPreference
import ut.ee.cs.rsg.helpers.SimpleDividerItemDecoration
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    private var checkRecyclerView: RecyclerView? = null

    private var mSubTotal = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        title = "Over Cart"

        checkRecyclerView = findViewById<View>(R.id.checkout_list) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        checkRecyclerView!!.layoutManager = linearLayoutManager
        checkRecyclerView!!.setHasFixedSize(true)
        checkRecyclerView!!.addItemDecoration(SimpleDividerItemDecoration(this))

        val mShared = MySharedPreference(this)
        val builder = GsonBuilder()
        val gson = builder.create()
        var addCartProducts = gson.fromJson(mShared.retrieveProductFromCart(),Array<ProductObject>::class.java)
        var productList = convertObjectArrayToListObject(addCartProducts)
        val mAdapter = CheckRVA(this, productList)
        checkRecyclerView!!.adapter = mAdapter
        mSubTotal = getTotalPrice(productList)




        shopping.setOnClickListener {
            val shoppingIntent = Intent(this, ShoppingActivity::class.java)
            startActivity(shoppingIntent)
        }



        val checkButton = (findViewById<View>(R.id.checkout) as Button)
        checkButton.setOnClickListener {
            val paymentIntent = Intent(this, CheckoutActivityGooglePay::class.java)
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
            totalCost += pObject.productPrice
        }
        return totalCost
    }


    companion object {
        private val TAG = CheckoutActivity::class.java.simpleName
    }
}