package com.example.proyectocatedramenudsm.adaptadores

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.proyectocatedramenudsm.R
import com.example.proyectocatedramenudsm.modelos.Producto

class AdaptadorProducto(private val context: Activity, var productos: List<Producto>) :
    ArrayAdapter<Producto?>(context, R.layout.item_categoria, productos) {
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
        val tvCategoria = rowview!!.findViewById<TextView>(R.id.descripcion_categoria)
//        val tvDescripcion = rowview.findViewById<TextView>(R.id.imagen_categoria)
        tvCategoria.text = "Categoria : " + productos[position].categoria
//        tvDescripcion.text = "Descripcion : " + productos[position].descripcion
        println(tvCategoria)
//        println(tvDescripcion)
        return rowview
    }
}
