package pe.hackspace.inteesana.View

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_centro_medico_detalle.*
import pe.hackspace.inteesana.Entity.CentroMedico
import pe.hackspace.inteesana.R
import pe.hackspace.inteesana.Session.Prefs
import pe.hackspace.libertrash.navigate
import pe.hackspace.libertrash.toast




class CentroMedicoDetalleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centro_medico_detalle)

        val centro = Prefs.centroMedico
        initUI(centro!!)
        btn_localizar.setOnClickListener { navigate<MapsActivity>() }
        btn_llamar.setOnClickListener { llamar(centro.telefono) }
        btn_mensaje.setOnClickListener { enviarMensaje(centro.telefono) }
    }

    fun initUI(c:CentroMedico){
        Glide.with(this).load(c.imageUrl).into(imv_foto)
        txt_nombre.text = c.nombre
        txt_descripcion.text = c.descripcion
        txt_consulta.text = c.consulta
        txt_precio.text = "${c.precio}"
        txt_horario.text = "${c.fechaAtencion} ${c.horarioAtencion}"
        txt_direccion.text = "${c.direccion}, ${c.distrito}"
    }

    @SuppressLint("MissingPermission")
    fun llamar(telefono : String){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefono))

        try {
            startActivity(intent)
        } catch (ex: android.content.ActivityNotFoundException) {
            toast("Could not find an activity to place the call.")
        }

    }

    fun enviarMensaje(telefono : String){
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:" + Uri.encode(telefono))
        try {
            startActivity(intent)
        } catch (ex: android.content.ActivityNotFoundException) {
            toast("Could not find an activity to place the SMS.")
        }
    }
}
