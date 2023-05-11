package com.example.proyectocatedramenudsm.adaptadores

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.proyectocatedramenudsm.R
import com.example.proyectocatedramenudsm.modelos.Categoria
import com.example.proyectocatedramenudsm.modelos.Producto

class AdaptadorProducto(private val context: Activity, var productos: List<Producto>) :
    ArrayAdapter<Producto?>(context, R.layout.item_producto, productos), Filterable {

    private var productosFiltrados: List<Producto> = productos
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val layoutInflater = context.layoutInflater
        var rowview: View? = null
        rowview = view ?: layoutInflater.inflate(R.layout.item_producto, null)
        val ivProducto = rowview!!.findViewById<ImageView>(R.id.imagen_producto)
        val tvNombre = rowview.findViewById<TextView>(R.id.nombre_producto)
        tvNombre.text = productosFiltrados[position].key
        Glide.with(context)
            .load(productosFiltrados[position].imagen)
            .into(ivProducto)

        return rowview
    }


    override fun getCount(): Int {
        return productosFiltrados.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filtro = constraint.toString().toLowerCase()
                val resultadosFiltrados = mutableListOf<Producto>()

                for (producto in productos) {
                    if (producto.key?.toLowerCase()!!.contains(filtro)) {
                        resultadosFiltrados.add(producto)
                    }
                }

                val resultados = FilterResults()
                resultados.values = resultadosFiltrados
                return resultados
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                productosFiltrados = results?.values as List<Producto>
                notifyDataSetChanged()
            }
        }
    }
}
