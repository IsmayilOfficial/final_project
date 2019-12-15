package ut.ee.cs.rsg.authentification

import android.content.Intent
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

import kotlinx.android.synthetic.main.activity_main.*
import ut.ee.cs.rsg.R

class MainActivity : AppCompatActivity() {










    private var firebaseAuth: FirebaseAuth? = null
    private var fireAuthListener: AuthStateListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        firebaseAuth = FirebaseAuth.getInstance()
        //get current user
        val user = FirebaseAuth.getInstance().currentUser
        fireAuthListener = AuthStateListener { firebaseAuth ->
            val user1 = firebaseAuth.currentUser
            if (user1 == null) { //user not login
                this.startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        old_email.visibility = View.GONE
        new_email.visibility = View.GONE
        password.visibility = View.GONE
        newPassword.visibility = View.GONE
        changeEmail.visibility = View.GONE
        changePass.visibility = View.GONE
        send.visibility = View.GONE
        remove!!.visibility = View.GONE
        if (progressBar != null) {
            progressBar.visibility = View.GONE
        }
        change_email_button.setOnClickListener {
            old_email.visibility = View.GONE
            new_email.visibility = View.VISIBLE
            password.visibility = View.GONE
            newPassword.visibility = View.GONE
            changeEmail.visibility = View.VISIBLE
            changePass.visibility = View.GONE
            send!!.visibility = View.GONE
            remove!!.visibility = View.GONE
        }
        //now change button visible for email changing
        changeEmail!!.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            //chaning email
            val newEmailText = new_email.text.toString().trim { it <= ' ' }
            if (user != null && newEmailText != "") {
                user.updateEmail(newEmailText)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@MainActivity,
                                        "Email address is updated. Please sign in with new email id!", Toast.LENGTH_SHORT).show()
                                firebaseAuth!!.signOut()
                                progressBar!!.visibility = View.GONE
                            } else {
                                Toast.makeText(this@MainActivity, "Failed to update email!", Toast.LENGTH_SHORT).show()
                                progressBar!!.visibility = View.GONE
                            }
                        }
            } else if (newEmailText == "") {
                new_email.error = "Enter email"
                progressBar!!.visibility = View.GONE
            }
        }
        //change button visible for password changing
        change_password_button.setOnClickListener {
            old_email.visibility = View.GONE
            new_email.visibility = View.GONE
            password.visibility = View.GONE
            newPassword.visibility = View.VISIBLE
            changeEmail.visibility = View.GONE
            changePass.visibility = View.VISIBLE
            send.visibility = View.GONE
            remove.visibility = View.GONE
        }
        //changing password
        changePass!!.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            val newPasswordText = newPassword!!.text.toString().trim { it <= ' ' }
            if (user != null && newPasswordText != "") {
                user.updatePassword(newPasswordText)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@MainActivity, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show()
                                firebaseAuth!!.signOut()
                                progressBar!!.visibility = View.GONE
                            } else {
                                Toast.makeText(this@MainActivity, "Failed to update password!", Toast.LENGTH_SHORT).show()
                                progressBar!!.visibility = View.GONE
                            }
                        }
            } else if (newPasswordText == "") {
                newPassword!!.error = "Enter password"
                progressBar!!.visibility = View.GONE
            }
        }
        //reset email button
        sending_pass_reset_button.setOnClickListener {
            old_email.visibility = View.VISIBLE
            new_email.visibility = View.GONE
            password!!.visibility = View.GONE
            newPassword!!.visibility = View.GONE
            changeEmail!!.visibility = View.GONE
            changePass!!.visibility = View.GONE
            send!!.visibility = View.VISIBLE
            remove!!.visibility = View.GONE
        }
        send!!.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            val oldEmailText = old_email.text.toString().trim { it <= ' ' }
            if (oldEmailText != "") {
                firebaseAuth!!.sendPasswordResetEmail(oldEmailText)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@MainActivity, "Reset password email is sent!", Toast.LENGTH_SHORT).show()
                                progressBar!!.visibility = View.GONE
                            } else {
                                Toast.makeText(this@MainActivity, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
                                progressBar!!.visibility = View.GONE
                            }
                        }
            } else {
                old_email.error = "Enter email"
                progressBar!!.visibility = View.GONE
            }
        }
        //deleting some user
        remove_user_button.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            user?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show()
                    this.startActivity(Intent(this, SignupActivity::class.java))
                    finish()
                    progressBar!!.visibility = View.GONE
                } else {
                    Toast.makeText(this, "Failed to delete your account!", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }
        }
        //simple signing out
        sign_out.setOnClickListener { firebaseAuth!!.signOut() }
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener(fireAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (fireAuthListener != null) {
            firebaseAuth!!.removeAuthStateListener(fireAuthListener!!)
        }
    }
}