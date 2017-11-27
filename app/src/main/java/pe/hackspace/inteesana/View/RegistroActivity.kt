package pe.hackspace.inteesana.View

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registro.*
import pe.hackspace.inteesana.Entity.Usuario
import pe.hackspace.inteesana.R
import pe.hackspace.libertrash.toast


class RegistroActivity : AppCompatActivity() {

    val mAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance()  }
    val currentUser by lazy { mAuth.currentUser }
    val mDatabase by lazy { FirebaseDatabase.getInstance().reference }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        initSpinnerCiudad()
        btn_terminos.setOnClickListener { createDialogTerminos() }
        btn_registrar.setOnClickListener { registerUsuario() }

    }

    fun registerUsuario(){
        if (!validateForm()){return}

        val nombres = edt_nombres.text.toString()
        val apellidos = edt_apellidos.text.toString()
        val ciudad = spn_ciudad.selectedItem.toString()
        val direccion = edt_direccion.text.toString()
        val email = edt_email.text.toString()
        val password = edt_password.text.toString()

        val usuario = Usuario(nombres,apellidos,ciudad,direccion,email)

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        if (user != null){
                            mDatabase.child("Usuario").child(user.uid).setValue(usuario)
                            toast("Usuario registrado con éxito.")
                            finish()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(FragmentActivity.TAG, "createUserWithEmail:failure", task.exception)
                        toast("Authentication failed.")
                    }
                }

    }

    fun validateForm(): Boolean {
        var valid = true

        val nombres = edt_nombres.text.toString()
        if (TextUtils.isEmpty(nombres)) {
            edt_nombres.error = "Required."
            valid = false
        } else {
            edt_nombres.error = null
        }

        val apellidos = edt_apellidos.getText().toString()
        if (TextUtils.isEmpty(apellidos)) {
            edt_apellidos.setError("Required.")
            valid = false
        } else {
            edt_apellidos.setError(null)
        }

        /*val ciudad = edt_ciudad.getText().toString()
        if (TextUtils.isEmpty(ciudad)) {
            edt_ciudad.setError("Required.")
            valid = false
        } else {
            edt_apellidos.setError(null)
        }*/

        val direccion = edt_direccion.text.toString()
        if (TextUtils.isEmpty(direccion)) {
            edt_direccion.setError("Required.")
            valid = false
        } else {
            edt_direccion.setError(null)
        }

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

        val check = cbx_terminos.isChecked
        if (!check){
            cbx_terminos.error = "Required."
            valid = false
        } else {
            cbx_terminos.error = null
        }

        return valid
    }

    fun initSpinnerCiudad(){
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.ciudades_array, android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_ciudad.adapter = adapter
    }

    fun createDialogTerminos(){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_terminos, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}
