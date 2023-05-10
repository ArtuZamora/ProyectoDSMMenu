package com.example.proyectocatedramenudsm.adaptadores

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.proyectocatedramenudsm.R
import com.example.proyectocatedramenudsm.modelos.Producto

class AdaptadorProducto(private val context: Activity, var productos: List<Producto>) :
    ArrayAdapter<Producto?>(context, R.layout.item_producto, productos) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val layoutInflater = context.layoutInflater
        var rowview: View? = null
        rowview = view ?: layoutInflater.inflate(R.layout.item_producto, null)
        val ivProducto = rowview!!.findViewById<ImageView>(R.id.imagen_producto)
        val tvNombre = rowview.findViewById<TextView>(R.id.nombre_producto)
        tvNombre.text = productos[position].key
        Glide.with(context)
            .load(productos[position].imagen)
            .into(ivProducto)

        return rowview
    }
}
