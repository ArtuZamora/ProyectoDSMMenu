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
import android.widget.TextView
import com.bumptech.glide.Glide
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
import org.w3c.dom.Text
import java.io.Serializable

class ProductoDescripcionActivity : AppCompatActivity() {

    private var nombre = ""
    private var categoria = ""
    private var descripcion = ""
    private var imagen = ""
    private var precio = ""

    //var productos: MutableList<Producto>? = null
    //var listaProductos: GridView? = null
    private var ivImagen: ImageView? = null
    private var tvNombre: TextView? = null
    private var tvCategoria: TextView? = null
    private var tvDescripcion: TextView? = null
    private var tvPrecio: TextView?= null

    private lateinit var cerrarSesionBtn: ImageView
    private lateinit var recordatorioBtn: ImageView
    private lateinit var buscarSV: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_descripcion)
        inicializarUI()
        inicializar()

        cerrarSesionBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut().also {
                val intent = Intent(this, RegistrarseActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        recordatorioBtn.setOnClickListener {
            val intent = Intent(this, RecordatorioActivity::class.java)
            startActivity(intent)
        }
    }

    private fun inicializar() {

        ivImagen = findViewById<ImageView>(R.id.imagen_producto2)
        tvNombre = findViewById<TextView>(R.id.nombre_producto)
        tvCategoria = findViewById<TextView>(R.id.categoria_producto)
        tvDescripcion = findViewById<TextView>(R.id.descripcion_producto)
        tvPrecio = findViewById<TextView>(R.id.precio_producto)

        val ivImagen = findViewById<ImageView>(R.id.imagen_producto2)
        val tvNombre = findViewById<TextView>(R.id.nombre_producto)
        val tvCategoria = findViewById<TextView>(R.id.categoria_producto)
        val tvDescripcion = findViewById<TextView>(R.id.descripcion_producto)
        val tvPrecio = findViewById<TextView>(R.id.precio_producto)
// Obtención de datos que envia actividad anterior
        val datos: Bundle? = intent.getExtras()
        Log.i("Trae datos", "${datos}")
        if (datos != null) {
            Log.i("nombreA", "${datos.getString("nombre").toString()}")
            Log.i("imagenA", "${datos.getString("imagen").toString()}")
            Log.i("precioA", "${datos.getString("precio").toString()}")
        }
        if (datos != null) {
            Glide.with(this@ProductoDescripcionActivity)
                .load(intent.getStringExtra("imagen").toString()).into(ivImagen)
        }
        if (datos != null) {
            tvNombre.text = (intent.getStringExtra("nombre").toString())
        }
        if (datos != null) {
            tvCategoria.text = datos.getString("categoria").toString()
        }
        if (datos != null) {
            tvDescripcion.text = datos.getString("descripcion").toString()
        }
        if (datos != null) {
            tvPrecio.text = "$" + datos.getString("precio").toString()
        }

        /*val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            key2 = datos.getString("categoria").toString()
        }
        var refCategorias: DatabaseReference = database.getReference("$key2/Platos")
        var consultaOrdenada: Query = refCategorias.orderByChild("Carne Asada")
        listaProductos!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val intent = Intent(getBaseContext(), ProductoDescripcionActivity::class.java)
                //println("esto mando en categoria: ${categorias!![i].key}")
                intent.putExtra("nombre", productos!![i].key)
                intent.putExtra("categoria", productos!![i].categoria)
                intent.putExtra("descripcion", productos!![i].descripcion)
                intent.putExtra("precio", productos!![i].precio)
                startActivity(intent)
            }
        })*/
        // Cambiarlo refCategorias a consultaOrdenada para ordenar lista
        /*consultaOrdenada.addValueEventListener(object : ValueEventListener {
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
        })*/
    }

    private fun inicializarUI() {
        cerrarSesionBtn = findViewById(R.id.cerrarSesionBtn)
        recordatorioBtn = findViewById(R.id.usuarioBtn)
        //buscarSV = findViewById(R.id.buscarSV)
    }
}