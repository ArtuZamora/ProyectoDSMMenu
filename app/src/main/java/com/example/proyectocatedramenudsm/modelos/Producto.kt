package com.example.proyectocatedramenudsm.modelos

import com.google.firebase.database.PropertyName

class Producto {
    fun key(key: String?) {
        this.key = key
    }
    @PropertyName("Categoria")
    var categoria: String? = null
    @PropertyName("Descripcion")
    var descripcion: String? = null
    @PropertyName("Imagen")
    var imagen: String? = null
    @PropertyName("Precio")
    var precio: Double? = null
    var key: String? = null

    constructor() {}
    constructor(categoria: String?, descripcion: String?, imagen: String?, precio: Double?) {
        this.categoria = categoria
        this.descripcion = descripcion
        this.imagen = imagen
        this.precio = precio
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "categoria" to categoria,
            "descripcion" to descripcion,
            "imagen" to imagen,
            "precio" to precio,
            "key" to key,
        )
    }

}


