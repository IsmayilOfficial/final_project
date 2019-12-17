package ut.ee.cs.rsg.helpers

import android.content.Context
import android.content.SharedPreferences
import ut.ee.cs.rsg.helpers.Constants

class MySharedPreference(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
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

}