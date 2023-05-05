package com.example.proyectocatedramenudsm.modelos

import com.google.firebase.database.PropertyName

class Categoria {
    fun key(key: String?) {
        this.key = key
    }
    var key: String? = null
    @PropertyName("Imagen")
    var imagen: String? = null
    @PropertyName("Platos")
    var platos: Map<String, Producto>? = null

    constructor() {}
    constructor(key: String?, imagen: String?, platos: Map<String, Producto>?) {
        this.key = key
        this.imagen = imagen
        this.platos = platos
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "imagen" to imagen,
            "platos" to platos,
            "key" to key,
        )
    }

}


