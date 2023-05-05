package com.example.proyectocatedramenudsm.adaptadores

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.proyectocatedramenudsm.R
import com.example.proyectocatedramenudsm.modelos.Categoria

class AdaptadorCategoria(private val context: Activity, var categorias: List<Categoria>) :
    ArrayAdapter<Categoria?>(context, R.layout.item_categoria, categorias) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        // Método invocado tantas veces como elementos tenga la coleccion personas
        // para formar a cada item que se visualizara en la lista personalizada
        val layoutInflater = context.layoutInflater
        var rowview: View? = null
        // optimizando las diversas llamadas que se realizan a este método
        // pues a partir de la segunda llamada el objeto view ya viene formado
        // y no sera necesario hacer el proceso de "inflado" que conlleva tiempo y
        // desgaste de bateria del dispositivo
        rowview = view ?: layoutInflater.inflate(R.layout.item_categoria, null)
        val ivCategoria = rowview!!.findViewById<ImageView>(R.id.imagen_categoria)
        val tvDescripcion = rowview.findViewById<TextView>(R.id.descripcion_categoria)
        tvDescripcion.text = categorias[position].key
        Glide.with(context)
            .load(categorias[position].imagen)
            .into(ivCategoria)

        return rowview
    }
}
