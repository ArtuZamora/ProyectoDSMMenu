package com.example.proyectocatedramenudsm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class IniciarSesionActivity : AppCompatActivity() {
    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var loginBtn: Button? = null
    private var backBtn: Button? = null
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

        mAuth = FirebaseAuth.getInstance()
        this.checkUser()

        initializeUI()
        loginBtn!!.setOnClickListener{
            loginUserAccount()
        }
        backBtn?.setOnClickListener {
            val intent = Intent(this@IniciarSesionActivity, RegistrarseActivity::class.java)
            startActivity(intent)
        }
        recoverBtn!!.setOnClickListener {
            val intent = Intent(this@IniciarSesionActivity, RecuperarcontraActivity::class.java)
            startActivity(intent)
        }

    }
    private fun loginUserAccount(){
        progressBar?.setVisibility(View.VISIBLE)
        val email:String
        val psswd:String
        email=emailTV?.getText().toString()
        psswd=passwordTV?.getText().toString()
        if(TextUtils.isEmpty(email)){
            emailTV?.error = "Ingrese su correo electrónico"
            emailTV?.requestFocus()

            return
        }
        if(TextUtils.isEmpty(psswd)){
            passwordTV?.error = "Ingrese una contraseña"

            passwordTV?.requestFocus()
            return
        }
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
    private fun initializeUI() {
        emailTV = findViewById<EditText>(R.id.etUsuario)
        passwordTV = findViewById<EditText>(R.id.etContra)
        loginBtn = findViewById<Button>(R.id.btnIniciarSesion)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        recoverBtn=findViewById(R.id.recuperarContra)
        backBtn=findViewById(R.id.backButton)

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
