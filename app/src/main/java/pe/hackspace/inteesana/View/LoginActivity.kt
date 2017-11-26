package pe.hackspace.inteesana.View

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import pe.hackspace.inteesana.R
import pe.hackspace.libertrash.navigate

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_register.setOnClickListener { navigate<RegistroActivity>() }

    }
}
