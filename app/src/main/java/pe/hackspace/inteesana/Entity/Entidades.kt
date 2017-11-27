package pe.hackspace.inteesana.Entity

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by brianbritto on 26/11/17.
 */

@IgnoreExtraProperties
data class Usuario(val nombres:String = "",
                   val apellidos:String = "",
                   val ciudad:String = "",
                   val direccion:String = "",
                   val email:String = "")