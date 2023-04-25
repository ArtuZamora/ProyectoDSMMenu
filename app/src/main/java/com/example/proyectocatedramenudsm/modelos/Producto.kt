package com.example.proyectocatedramenudsm.modelos

class Producto {
    fun key(key: String?) {
    }

    var categoria: String? = null
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


