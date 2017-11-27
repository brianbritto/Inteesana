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

@IgnoreExtraProperties
data class CentroMedico(val ciudad:String = "",
                        val direccion:String = "",
                        val distrito:String = "",
                        val email:String = "",
                        val fechaAtencion:String = "",
                        val horarioAtencion:String = "",
                        val latitud:Double = 0.0,
                        val longitud:Double = 0.0,
                        val nombre:String = "",
                        val precio:Double = 0.0,
                        val telefono:String = "",
                        val tipo:Int = 0,
                        val consulta:String = "",
                        val descripcion:String = "")