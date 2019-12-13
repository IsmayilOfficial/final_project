package ut.ee.cs.rsg

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.GsonBuilder
import com.paypal.android.sdk.payments.*
import org.json.JSONException
import ut.ee.cs.rsg.jsonEntityObjects.PaymentResponseObject
import ut.ee.cs.rsg.jsonEntityObjects.ServerObject
import ut.ee.cs.rsg.network.GsonRequest
import ut.ee.cs.rsg.network.VolleySingleton
import java.math.BigDecimal
import java.util.*

class PayPalCheckoutActivity : AppCompatActivity() {
    private var totalCostPrice = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_pal_checkout)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        title = getString(R.string.pay_with_paypal)
        totalCostPrice = intent.extras!!.getDouble("TOTAL_PRICE")
        Log.d(TAG, "Price $totalCostPrice")
        val intent = Intent(this, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        startService(intent)
        val payPalButton = (findViewById<View>(R.id.pay_pal_button) as Button)
        payPalButton.setOnClickListener { initializePayPalPayment() }
    }

    private fun initializePayPalPayment() {
        val payment = PayPalPayment(BigDecimal(totalCostPrice.toString()), "USD", "Female Cloths", PayPalPayment.PAYMENT_INTENT_SALE)
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val confirm: PaymentConfirmation = data!!.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4))
                    val jsonPaymentResponse = confirm.toJSONObject().toString(4)
                    val builder = GsonBuilder()
                    val gson = builder.create()
                    val responseObject = gson.fromJson(jsonPaymentResponse, PaymentResponseObject::class.java)
                    if (responseObject != null) {
                        val paymentId = responseObject.response.id
                        val paymentState = responseObject.response.state
                        Log.d(TAG, "Log payment id and state $paymentId $paymentState")
                        //send to your server for verification.
                        sendPaymentVerificationToServer(paymentId, paymentState)
                    }
                } catch (e: JSONException) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e)
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.")
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.")
        }
    }

    private fun sendPaymentVerificationToServer(id: String?, state: String?) {
        val params: MutableMap<String, String> = HashMap()
        params["PAYMENT_ID"] = id!!
        params["PAYMENT_STATE"] = state!!
        val serverRequest = GsonRequest(
                Request.Method.POST,
                SERVER_PATH,
                ServerObject::class.java,
                params,
                createRequestSuccessListener(),
                createRequestErrorListener())
        serverRequest.retryPolicy = DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        VolleySingleton.Companion.getInstance(this@PayPalCheckoutActivity)!!.addToRequestQueue(serverRequest)
    }

    private fun createRequestSuccessListener(): Response.Listener<ServerObject> {
        return Response.Listener<ServerObject> { response ->
            try {
                Log.d(TAG, "Json Response " + response.success)
                if (!TextUtils.isEmpty(response.success)) {
                    Toast.makeText(this@PayPalCheckoutActivity, getString(R.string.successful_payment), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@PayPalCheckoutActivity, getString(R.string.server_error), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createRequestErrorListener(): Response.ErrorListener {
        return Response.ErrorListener { error -> error.printStackTrace() }
    }

    public override fun onDestroy() {
        stopService(Intent(this, PayPalService::class.java))
        super.onDestroy()
    }

    companion object {
        private val TAG = PayPalCheckoutActivity::class.java.simpleName
        private const val MY_SOCKET_TIMEOUT_MS = 5000
        private const val SERVER_PATH = "Path_to_Server_To_Store_Token"
        private val config = PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .clientId("ENTER PAY PAL CLIENT ID")
    }
}