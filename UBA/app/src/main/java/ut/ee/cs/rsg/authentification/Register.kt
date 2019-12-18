package ut.ee.cs.rsg.authentification

import android.content.Intent
import android.os.Bundle

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import ut.ee.cs.rsg.R
import ut.ee.cs.rsg.ShoppingActivity

class Register : AppCompatActivity() {

    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ButterKnife.bind(this)

        firebaseAuth = FirebaseAuth.getInstance()
        reset_button.setOnClickListener { this.startActivity(Intent(this, Reset::class.java)) }
        sign_in_button.setOnClickListener { finish() }
        sign_up_button.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val userEmail = email.text.toString().trim { it <= ' ' }
        val userPassword = password.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(userEmail)) {
            showToast("Enter email address!")
            return
        }
        if (TextUtils.isEmpty(userPassword)) {
            showToast("Enter Password!")
            return
        }
        if (userPassword.length < 6) {
            showToast("Password too short, enter minimum 6 characters")
            return
        }
        progress_bar.visibility = View.VISIBLE
        firebaseAuth!!.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this) { task ->
                    Log.d(TAG, "New user registration: " + task.isSuccessful)
                    if (!task.isSuccessful) {
                        showToast("Authentication failed. " + task.exception)
                    } else {
                        this.startActivity(Intent(this, ShoppingActivity::class.java))
                        finish()
                    }
                }
    }

    override fun onResume() {
        super.onResume()
        progress_bar.visibility = View.GONE
    }

    fun showToast(toastText: String?) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "SignupActivity"
    }
}