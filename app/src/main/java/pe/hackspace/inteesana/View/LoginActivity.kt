package pe.hackspace.inteesana.View

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import pe.hackspace.inteesana.R
import pe.hackspace.libertrash.navigate
import pe.hackspace.libertrash.toast


class LoginActivity : AppCompatActivity() {

    val mAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance()  }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        /*val currentUser = mAuth.currentUser
        if (currentUser!=null) navigate<MainActivity>()*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_register.setOnClickListener { navigate<RegistroActivity>() }
        btn_login.setOnClickListener {
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()
            login(email,password)
        }
    }

    fun login(email:String, password:String){
        if (!validateForm()){return}

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        toast("Bienvenido a Inteesana.")
                        navigate<MainActivity>()
                    } else {
                        toast("Email y/o password incorrecto.")
                    }
                }
    }

    fun validateForm(): Boolean {
        var valid = true

        val email = edt_email.text.toString()
        if (TextUtils.isEmpty(email)) {
            edt_email.setError("Required.")
            valid = false
        } else {
            edt_email.setError(null)
        }

        val password = edt_password.getText().toString()
        if (TextUtils.isEmpty(password)) {
            edt_password.setError("Required.")
            valid = false
        } else {
            edt_password.setError(null)
        }

        return valid
    }
}
