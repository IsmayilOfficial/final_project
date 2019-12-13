package ut.ee.cs.myshop

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.identity.intents.Address.AddressOptions
import com.google.android.gms.wallet.*
import com.google.android.gms.wallet.Wallet.WalletOptions
import com.google.android.gms.wallet.fragment.*
import ut.ee.cs.myshop.PaymentsActivity
import ut.ee.cs.myshop.helpers.Constants

class PaymentsActivity : AppCompatActivity(), ConnectionCallbacks, OnConnectionFailedListener {
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mWalletFragment: SupportWalletFragment? = null
    private lateinit var maskedWallet: MaskedWallet
    private val cartTotal = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        title = "Payment Page"
        val totalPrice = intent.extras!!.getDouble("TOTAL_PRICE")
        val options = AddressOptions(WalletConstants.THEME_LIGHT)
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .setAccountName("Inducesmile")
                .addApi(Wallet.API, WalletOptions.Builder()
                        .setEnvironment(Constants.WALLET_ENVIRONMENT)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .build())
                .build()
        mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOption)
        initializeWalletFragment()
        Wallet.Payments.isReadyToPay(mGoogleApiClient).setResultCallback { booleanResult ->
            if (booleanResult.status.isSuccess) {
                if (booleanResult.value) { // Show Android Pay buttons
                    initializeWalletFragment()
                } else { // Hide Android Pay buttons, show a message that Android Pay
// cannot be used yet, and display a traditional checkout button
                }
            } else { // Error making isReadyToPay call
                Log.e(TAG, "isReadyToPay:" + booleanResult.status)
            }
        }
    }

    private fun useStripePaymentGatewayForProcessing(): PaymentMethodTokenizationParameters {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.PAYMENT_GATEWAY)
                .addParameter("gateway", "stripe")
                .addParameter("stripe:publishableKey", Constants.PUBLISHABLE_KEY)
                .addParameter("stripe:version", Constants.VERSION)
                .build()
    }

    private val walletFragmentOption: WalletFragmentOptions
        private get() {
            val walletFragmentStyle = WalletFragmentStyle()
                    .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
                    .setBuyButtonAppearance(WalletFragmentStyle.BuyButtonAppearance.ANDROID_PAY_DARK)
                    .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT)
            return WalletFragmentOptions.newBuilder()
                    .setEnvironment(Constants.WALLET_ENVIRONMENT)
                    .setFragmentStyle(walletFragmentStyle)
                    .setTheme(WalletConstants.THEME_LIGHT)
                    .setMode(WalletFragmentMode.BUY_BUTTON)
                    .build()
        }

    private fun initializeWalletFragment() {
        val startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(sendMaskedWalletRequest())
                .setMaskedWalletRequestCode(REQUEST_CODE_MASKED_WALLET)
                .setAccountName("Inducesmile")
        mWalletFragment!!.initialize(startParamsBuilder.build())
        // add Wallet fragment to the UI
        supportFragmentManager.beginTransaction().replace(R.id.wallet_button_holder, mWalletFragment!!).commit()
    }

    private fun sendMaskedWalletRequest(): MaskedWalletRequest {
        return MaskedWalletRequest.newBuilder()
                .setMerchantName(Constants.MERCHANT_NAME)
                .setPhoneNumberRequired(true)
                .setShippingAddressRequired(true)
                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
                .setEstimatedTotalPrice(cartTotal.toString()) // Create a Cart with the current line items. Provide all the information
// available up to this point with estimates for shipping and tax included.
                .setCart(Cart.newBuilder()
                        .setCurrencyCode(Constants.CURRENCY_CODE_USD)
                        .setTotalPrice(cartTotal.toString()) //.setLineItems()
                        .build())
                .setPaymentMethodTokenizationParameters(useStripePaymentGatewayForProcessing())
                .build()
    }

    private fun makeFullWalletRequest() {
        Wallet.Payments.loadFullWallet(mGoogleApiClient, generateFullWalletRequest(), FULL_WALLET_REQUEST_CODE)
    }

    private fun generateFullWalletRequest(): FullWalletRequest {
        return FullWalletRequest.newBuilder()
                .setGoogleTransactionId(maskedWallet!!.googleTransactionId)
                .setCart(Cart.newBuilder()
                        .setCurrencyCode(Constants.CURRENCY_CODE_USD)
                        .setTotalPrice(cartTotal.toString()) //.setLineItems(lineItems)
                        .build())
                .build()
    }

    override fun onConnected(bundle: Bundle?) {}
    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    public override fun onStart() {
        super.onStart()
        mGoogleApiClient!!.connect()
    }

    public override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnecting || mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var errorCode = -1
        if (data != null) {
            errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1)
        }
        when (requestCode) {
            REQUEST_CODE_MASKED_WALLET -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(TAG, "Testing for output")
                    if (data != null) {
                        maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET)
                        // call to get the Google transaction id
                        val googleTransactionId = maskedWallet.getGoogleTransactionId()
                        detailedMaskedWalletInformation(maskedWallet)
                    }
                }
                Activity.RESULT_CANCELED -> {
                }
                else -> handleError(errorCode)
            }
            WalletConstants.RESULT_ERROR -> handleError(errorCode)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun handleError(errorCode: Int) {
        when (errorCode) {
            WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED -> Toast.makeText(this@PaymentsActivity, "Error with your payment", Toast.LENGTH_LONG).show()
            WalletConstants.ERROR_CODE_INVALID_PARAMETERS, WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE, WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR, WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR, WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE, WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION, WalletConstants.ERROR_CODE_UNKNOWN -> {
            }
            else -> {
            }
        }
    }

    private fun detailedMaskedWalletInformation(maskedWallet: MaskedWallet?) {}

    companion object {
        private val TAG = PaymentsActivity::class.java.simpleName
        private const val REQUEST_CODE_MASKED_WALLET = 1000
        const val FULL_WALLET_REQUEST_CODE = 889
    }
}