package com.example.proyectocatedramenudsm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Filterable
import android.widget.GridView
import android.widget.ImageView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectocatedramenudsm.adaptadores.AdaptadorCategoria
import com.example.proyectocatedramenudsm.adaptadores.AdaptadorProducto
import com.example.proyectocatedramenudsm.modelos.Categoria
import com.example.proyectocatedramenudsm.modelos.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class MainActivity : AppCompatActivity() {

    var consultaOrdenada: Query = refCategorias.orderByKey()
    var categorias: MutableList<Categoria>? = null
    var listaCategorias: GridView? = null

    private lateinit var cerrarSesionBtn: ImageView
    private lateinit var recordatorioBtn: ImageView
    private lateinit var buscarSV: SearchView


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
        listaCategorias = findViewById<GridView>(R.id.ListaCategorias)

        categorias = ArrayList<Categoria>()

        // Cambiarlo refCategorias a consultaOrdenada para ordenar lista
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Procedimiento que se ejecuta cuando hubo algun cambio
                // en la base de datos
                // Se actualiza la coleccion de personas
                categorias!!.removeAll(categorias!!)
                for (dato in dataSnapshot.getChildren()) {
                    val categoria: Categoria? = dato.getValue(Categoria::class.java)
                    Log.i("Platos", "${categoria!!.platos}")

                    categoria?.key(dato.key)
                    if (categoria != null) {
                        categorias!!.add(categoria)
                    }
                }
                val adapter = AdaptadorCategoria(
                    this@MainActivity,
                    categorias as ArrayList<Categoria>
                )
                listaCategorias!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        buscarSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (listaCategorias?.adapter is Filterable) {
                    (listaCategorias!!.adapter as Filterable).filter.filter(newText)
                }
                return true
            }
        })
    }

    private fun inicializarUI(){
        cerrarSesionBtn = findViewById(R.id.cerrarSesionBtn)
        recordatorioBtn = findViewById(R.id.usuarioBtn)
        buscarSV = findViewById(R.id.buscarSV)
    }
    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refCategorias: DatabaseReference = database.getReference("")
    }

}