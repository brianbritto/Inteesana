package pe.hackspace.inteesana.View

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import pe.hackspace.inteesana.Entity.CentroMedico
import pe.hackspace.inteesana.R
import pe.hackspace.inteesana.Session.Prefs
import pe.hackspace.libertrash.navigate
import pe.hackspace.libertrash.toast


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val mDatabase by lazy{ FirebaseDatabase.getInstance().getReference()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        btn_buscar.setOnClickListener{
            var tipo = -1
            when(group_radio.checkedRadioButtonId){
                radio_dolencia.id -> tipo = 0
                radio_enfermedad.id -> tipo = 1
            }
            val consulta = edt_consulta.text.toString()
            buscarCentroMedico(tipo,consulta)

        }

    }

    fun buscarCentroMedico(tipo:Int, consulta:String){
        if (!validateForm()) return

        val query : Query = mDatabase.child("CentroMedico")
                                     .orderByChild("precio")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Prefs.centroMedico_list.clear()

                for (postSnapshot in dataSnapshot.children) {
                    val cm : CentroMedico = postSnapshot.getValue(CentroMedico::class.java) as CentroMedico
                    // Log.i("cm -> ",cm.consulta+" = "+cm.tipo)
                    // Log.i("tipo -> ",""+tipo + " = " + consulta)
                    if (cm.tipo==tipo && cm.consulta == consulta)
                    {
                        // Log.i("cm -> ",cm.nombre+": "+cm.precio)
                        Prefs.centroMedico_list.add(cm)
                    }
                }

                if (!Prefs.centroMedico_list.isEmpty()){
                    navigate<CentroMedicoActivity>()
                } else {
                    toast("No existen ofertas para esta consulta. Inténtelo más tarde.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                // Log.w(FragmentActivity.TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    fun validateForm() : Boolean{
        var valid = true

        val radio = group_radio.checkedRadioButtonId
        if (radio == -1){
            // radio_dolencia.error = "Required."
            // radio_enfermedad.error = "Required."
            txt_seleccionar.error = "Required."
            valid = false
        } else {
            // radio_dolencia.error = null
            // radio_enfermedad.error = null
            txt_seleccionar.error = null
        }

        val consulta = edt_consulta.text.toString()
        if (TextUtils.isEmpty(consulta)) {
            edt_consulta.setError("Required.")
            valid = false
        } else {

            edt_consulta.setError(null)
        }

        return valid
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {

            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
