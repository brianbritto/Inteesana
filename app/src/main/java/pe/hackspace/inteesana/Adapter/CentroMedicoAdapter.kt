package pe.hackspace.inteesana.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_centro_medico.view.*
import pe.hackspace.inteesana.Entity.CentroMedico
import pe.hackspace.inteesana.R
import pe.hackspace.libertrash.inflate

/**
 * Created by brianbritto on 08/11/17.
 */
class CentroMedicoAdapter (var centros: ArrayList<CentroMedico>) : RecyclerView.Adapter<CentroMedicoAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ViewHolder(parent?.inflate(R.layout.item_centro_medico))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(centros[position])

    override fun getItemCount() = centros.size


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bind(centro: CentroMedico) {
            itemView?.let{
                with(itemView) {
                    Glide.with(context).load(centro.imageUrl).into(imv_foto)
                    txt_nombre.text = centro.nombre
                    txt_consulta.text = centro.consulta
                    txt_precio.text = centro.precio.toString()
                    txt_direccion.text = centro.direccion
                }
            }
        }
    }
}