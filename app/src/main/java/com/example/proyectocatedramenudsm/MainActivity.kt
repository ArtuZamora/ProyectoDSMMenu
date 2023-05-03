package com.example.proyectocatedramenudsm

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectocatedramenudsm.adaptadores.AdaptadorProducto
import com.example.proyectocatedramenudsm.modelos.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    var consultaOrdenada: Query = refProductos.orderByChild("Descripcion")
    var productos: MutableList<Producto>? = null
    var listaProductos: GridView? = null

    private lateinit var cerrarSesionBtn: ImageView
    private lateinit var recordatorioBtn: ImageView
    private lateinit var buscarBtn: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inicializarUI()
        inicializar()

        cerrarSesionBtn.setOnClickListener{
            FirebaseAuth.getInstance().signOut().also {
                val intent = Intent(this, RegistrarseActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        recordatorioBtn.setOnClickListener{
            val intent = Intent(this, RecordatorioActivity::class.java)
            startActivity(intent)
        }
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

    private fun inicializarUI(){
        cerrarSesionBtn = findViewById(R.id.cerrarSesionBtn)
        recordatorioBtn = findViewById(R.id.usuarioBtn)
        buscarBtn = findViewById(R.id.buscarBtn)
    }
    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refProductos: DatabaseReference = database.getReference("Menu")
    }

}