package com.example.proyectocatedramenudsm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth



class RecuperarcontraActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var btnEnviarCorreo: Button? = null
    private var emailTV: EditText? = null
    private var progressBar: ProgressBar? = null
    private var backBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_recuperarcontra)

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        initializeUI()

        backBtn?.setOnClickListener {
            val intent = Intent(this@RecuperarcontraActivity, IniciarSesionActivity::class.java)
            startActivity(intent)
        }
        btnEnviarCorreo?.setOnClickListener {
            val email = emailTV?.text.toString().trim()

            if (email.isEmpty()) {
                emailTV?.error = "Ingrese su correo electrónico"
                emailTV?.requestFocus()
                return@setOnClickListener
            }
            val firebaseAuth = mAuth
            firebaseAuth?.sendPasswordResetEmail(email)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Se ha enviado un correo electrónico para restablecer su contraseña", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al enviar el correo electrónico de restablecimiento de contraseña", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
    private fun initializeUI() {
        emailTV=findViewById(R.id.etUsuarioC)
        btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo)
        progressBar = findViewById(R.id.progressBar)
        backBtn=findViewById(R.id.backButton)
    }
}