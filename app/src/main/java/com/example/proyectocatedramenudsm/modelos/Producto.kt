package com.example.proyectocatedramenudsm.modelos

import com.google.firebase.database.PropertyName

class Producto {
    fun key(key: String?) {
    }

    @PropertyName("Categoria")
    var categoria: String? = null
    @PropertyName("Descripcion")
    var descripcion: String? = null
    var key: String? = null
    var pro: MutableMap<String, Boolean> = HashMap()

    constructor() {}
    constructor(categoria: String?, descripcion: String?) {
        this.categoria = categoria
        this.descripcion = descripcion
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "categoria" to categoria,
            "descripcion" to descripcion,
            "key" to key,
            "pro" to pro
        )
    }

}


