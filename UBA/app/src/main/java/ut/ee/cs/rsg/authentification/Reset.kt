package ut.ee.cs.rsg.authentification

import android.os.Bundle

import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_reset.*
import ut.ee.cs.rsg.R

class Reset : AppCompatActivity() {



    private var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        ButterKnife.bind(this)
        firebaseAuth = FirebaseAuth.getInstance()
        btn_back.setOnClickListener { finish() }
        btn_reset_password.setOnClickListener(View.OnClickListener {
            val userEmail = email.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(userEmail)) {
                Toast.makeText(this, "Enter your register email id", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            progressBar.visibility = View.VISIBLE
            //reset password you will get a mail
            firebaseAuth!!.sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
                        }
                        progressBar.visibility = View.GONE
                    }
        })
    }
}