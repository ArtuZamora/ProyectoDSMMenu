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

class AdaptadorCategoria(private val context: Activity, var categorias: List<Categoria>) :
    ArrayAdapter<Categoria?>(context, R.layout.item_categoria, categorias), Filterable {

    private var categoriasFiltradas: List<Categoria> = categorias

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val layoutInflater = context.layoutInflater
        var rowview: View? = null
        rowview = view ?: layoutInflater.inflate(R.layout.item_categoria, null)
        val ivCategoria = rowview!!.findViewById<ImageView>(R.id.imagen_categoria)
        val tvDescripcion = rowview.findViewById<TextView>(R.id.descripcion_categoria)
        tvDescripcion.text = categoriasFiltradas[position].key
        Glide.with(context)
            .load(categoriasFiltradas[position].imagen)
            .into(ivCategoria)

        return rowview
    }

    override fun getCount(): Int {
        return categoriasFiltradas.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filtro = constraint.toString().toLowerCase()
                val resultadosFiltrados = mutableListOf<Categoria>()

                for (categoria in categorias) {
                    if (categoria.key?.toLowerCase()!!.contains(filtro)) {
                        resultadosFiltrados.add(categoria)
                    }
                }

                val resultados = FilterResults()
                resultados.values = resultadosFiltrados
                return resultados
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categoriasFiltradas = results?.values as List<Categoria>
                notifyDataSetChanged()
            }
        }
    }
}

