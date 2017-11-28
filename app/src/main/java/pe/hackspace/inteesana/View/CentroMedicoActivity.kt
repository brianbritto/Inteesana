package pe.hackspace.inteesana.View

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_centro_medico.*
import pe.hackspace.inteesana.Adapter.CentroMedicoAdapter
import pe.hackspace.inteesana.R
import pe.hackspace.inteesana.Session.Prefs

class CentroMedicoActivity : AppCompatActivity() {

    var centroAdapter : CentroMedicoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centro_medico)

        rcv_listaCentroMedico.layoutManager = LinearLayoutManager(applicationContext)
        rcv_listaCentroMedico.setHasFixedSize(true)
        rcv_listaCentroMedico.itemAnimator = DefaultItemAnimator()
        centroAdapter = CentroMedicoAdapter(Prefs.centroMedico_list)
        rcv_listaCentroMedico.adapter = centroAdapter

    }
}
