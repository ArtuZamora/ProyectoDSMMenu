package com.example.proyectocatedramenudsm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectocatedramenudsm.adaptadores.AdaptadorProducto
import com.example.proyectocatedramenudsm.modelos.Producto
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    var consultaOrdenada: Query = refProductos.orderByChild("Descripcion")
    var productos: MutableList<Producto>? = null
    var listaProductos: GridView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializar()

    }

    private fun inicializar() {
        listaProductos = findViewById<GridView>(R.id.ListaProductos)

        productos = ArrayList<Producto>()

        // Cambiarlo refProductos a consultaOrdenada para ordenar lista
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Procedimiento que se ejecuta cuando hubo algun cambio
                // en la base de datos
                // Se actualiza la coleccion de personas
                productos!!.removeAll(productos!!)
                for (dato in dataSnapshot.getChildren()) {
                    val producto: Producto? = dato.getValue(Producto::class.java)
                    producto?.key(dato.key)
                    if (producto != null) {
                        productos!!.add(producto)
                    }
                }
                val adapter = AdaptadorProducto(
                    this@MainActivity,
                    productos as ArrayList<Producto>
                )
                listaProductos!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refProductos: DatabaseReference = database.getReference("Menu")
    }

}