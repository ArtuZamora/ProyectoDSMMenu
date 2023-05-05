package com.example.proyectocatedramenudsm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class IniciarSesionActivity : AppCompatActivity() {
    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var loginBtn: Button? = null
    private var recoverBtn: TextView? = null
    private var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null

    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_iniciar_sesion)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Iniciar sesión"
        mAuth = FirebaseAuth.getInstance()
        this.checkUser()

        initializeUI()
        loginBtn!!.setOnClickListener{
            loginUserAccount()
        }

        recoverBtn!!.setOnClickListener {
            val intent = Intent(this@IniciarSesionActivity, RecuperarcontraActivity::class.java)
            startActivity(intent)
        }


        passwordTV?.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableWidth = passwordTV?.compoundDrawablesRelative?.get(2)?.bounds?.width()
                if (event.rawX >= passwordTV!!.right - passwordTV?.paddingRight!! - (drawableWidth ?: 0)) {
                    // Si el usuario hizo clic en el icono de visibilidad
                    if (passwordTV?.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        // Si la contraseña está oculta, mostrarla
                        passwordTV?.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        passwordTV?.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.visibility), null)
                    } else {
                        // Si la contraseña está visible, ocultarla
                        passwordTV?.transformationMethod = PasswordTransformationMethod.getInstance()
                        passwordTV?.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.visibility_off), null)
                    }
                    // Mover el cursor al final del texto para que siga visible después de ocultar/mostrar
                    passwordTV?.text?.let { passwordTV?.setSelection(it.length) }
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun loginUserAccount(){

        val email:String
        val psswd:String
        email=emailTV?.getText().toString()
        psswd=passwordTV?.getText().toString()
        if (email.isEmpty() && psswd.isEmpty()) {
            showEmptyFieldError(emailTV, "su correo electrónico")
            showEmptyFieldError(passwordTV, "una contraseña")
        }
        else if (email.isEmpty()) {
            showEmptyFieldError(emailTV, "su correo electrónico")
        } else if (psswd.isEmpty()) {
            showEmptyFieldError(passwordTV, "una contraseña")
        }
        else{
            progressBar!!.visibility= View.VISIBLE
        mAuth?.signInWithEmailAndPassword(email,psswd)?.addOnCompleteListener {
                task ->
            if(task.isSuccessful){
                Toast.makeText(  this, "Inicio de sesión satisfactorio", Toast.LENGTH_LONG).show()

                progressBar?.setVisibility(View.GONE)
                val intent = Intent(this@IniciarSesionActivity, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(  this, "Fallo el inicio de sesión, por favor intenta más tarde",
                    Toast.LENGTH_LONG).show()

                progressBar?.setVisibility(View.GONE)
            }
        }
        }

    }
    fun showEmptyFieldError(field: EditText?, fieldName: String) {
        field?.error = "Ingrese $fieldName"
        field?.requestFocus()
    }
    private fun initializeUI() {
        emailTV = findViewById<EditText>(R.id.etUsuario)
        passwordTV = findViewById<EditText>(R.id.etContra)
        loginBtn = findViewById<Button>(R.id.btnIniciarSesion)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        recoverBtn=findViewById(R.id.recuperarContra)


    }

    override fun onResume(){
        super.onResume()
        mAuth!!.addAuthStateListener(authStateListener)
    }
    override fun onPause(){
        super.onPause()
        mAuth!!.removeAuthStateListener(authStateListener)
    }
    private fun checkUser(){
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if(auth.currentUser != null){
                val intent = Intent(this@IniciarSesionActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
