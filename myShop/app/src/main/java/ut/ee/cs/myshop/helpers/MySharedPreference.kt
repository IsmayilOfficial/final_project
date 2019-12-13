package ut.ee.cs.myshop.helpers

import android.content.Context
import android.content.SharedPreferences

class MySharedPreference(private val context: Context) {
    private val prefs: SharedPreferences
    fun addProductToTheCart(product: String?) {
        val edits = prefs.edit()
        edits.putString(Constants.PRODUCT_ID, product)
        edits.apply()
    }

    fun retrieveProductFromCart(): String? {
        return prefs.getString(Constants.PRODUCT_ID, "")
    }

    fun addProductCount(productCount: Int) {
        val edits = prefs.edit()
        edits.putInt(Constants.PRODUCT_COUNT, productCount)
        edits.apply()
    }

    fun retrieveProductCount(): Int {
        return prefs.getInt(Constants.PRODUCT_COUNT, 0)
    }

    init {
        prefs = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
    }
}