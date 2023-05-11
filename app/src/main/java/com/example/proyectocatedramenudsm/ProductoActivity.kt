package com.example.proyectocatedramenudsm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Filterable
import android.widget.GridView
import android.widget.ImageView
import android.widget.SearchView
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
import java.io.Serializable

class ProductoActivity : AppCompatActivity() {

    private var key2 = ""
    var productos: MutableList<Producto>? = null
    var listaProductos: GridView? = null

    private lateinit var cerrarSesionBtn: ImageView
    private lateinit var recordatorioBtn: ImageView
    private lateinit var buscarSV: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)
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

        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            key2 = datos.getString("categoria").toString()
        }
        var refCategorias: DatabaseReference = database.getReference("$key2/Platos")
        var consultaOrdenada: Query = refCategorias.orderByChild("Carne Asada")
        listaProductos!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val intent = Intent(getBaseContext(), ProductoDescripcionActivity::class.java)
                //println("esto mando en categoria: ${categorias!![i].key}")
                Log.i("nombre", "${productos!![i].key}")
                intent.putExtra("nombre", productos!![i].key)
                Log.i("categoria", "${productos!![i].categoria}")
                intent.putExtra("categoria", productos!![i].categoria)
                intent.putExtra("descripcion", productos!![i].descripcion)
                Log.i("imagen", "${productos!![i].imagen}")
                intent.putExtra("imagen", productos!![i].imagen)
                Log.i("precio", "${productos!![i].precio}")
                intent.putExtra("precio", productos!![i].precio.toString())
                startActivity(intent)
            }
        })
        // Cambiarlo refCategorias a consultaOrdenada para ordenar lista
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Procedimiento que se ejecuta cuando hubo algun cambio
                // en la base de datos
                // Se actualiza la coleccion de personas
                productos!!.removeAll(productos!!)
                for (dato in dataSnapshot.getChildren()) {
                    println("esto trae dato "+dato.getValue(Producto::class.java))
                    val producto: Producto? = dato.getValue(Producto::class.java)
                    println("Aqui están los platos del activity: $producto")
                    Log.i("Platos", "$producto")

                    producto?.key(dato.key)
                    if (producto != null) {
                        productos!!.add(producto)
                    }
                }
                val adapter = AdaptadorProducto(
                    this@ProductoActivity,
                    productos as ArrayList<Producto>
                )
                listaProductos!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        buscarSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (listaProductos?.adapter is Filterable) {
                    (listaProductos!!.adapter as Filterable).filter.filter(newText)
                }
                return true
            }
        })
    }

    private fun inicializarUI(){
        cerrarSesionBtn = findViewById(R.id.cerrarSesionBtn)
        recordatorioBtn = findViewById(R.id.usuarioBtn)
        buscarSV = findViewById(R.id.buscarSV)
        findViewById<ImageView>(R.id.atrasBtn).setOnClickListener{
            onBackPressed();
        }
    }
    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    }

}