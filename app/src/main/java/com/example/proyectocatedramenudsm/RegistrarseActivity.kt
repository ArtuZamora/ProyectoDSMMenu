package com.example.proyectocatedramenudsm

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.firebase.auth.GoogleAuthProvider

class RegistrarseActivity : AppCompatActivity() {
    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var passwordTVR: EditText? = null
    private var signup: Button? = null
    private var signinScreen: Button? = null
    private var progressBar: ProgressBar? = null
    private var googlebtn: MaterialButton? = null
    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private var mAuth: FirebaseAuth? = null
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_registrarse)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        title = "Registrarse"
        mAuth= FirebaseAuth.getInstance()
        initializeUI()


        signup!!.setOnClickListener { registerNewUser() }

        signinScreen!!.setOnClickListener {  val intent = Intent(this@RegistrarseActivity, IniciarSesionActivity::class.java)
            startActivity(intent)

        }
        googlebtn!!.setOnClickListener{

            registerGoogle()
        }
        //ocultar y mostrar contra para passwordTV

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
        //
        //ocultar y mostrar contra para passwordTVR
        passwordTVR?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0)
        passwordTVR?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                passwordTVR?.let {
                    val touchableArea = it.width - it.compoundPaddingEnd
                    if (event.rawX >= touchableArea) {
                        if (it.transformationMethod == PasswordTransformationMethod.getInstance()) {
                            // Si la contraseña está oculta, mostrarla
                            passwordTVR?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility, 0)
                            it.transformationMethod = HideReturnsTransformationMethod.getInstance()

                        } else {
                            // Si la contraseña está visible, ocultarla
                            it.transformationMethod = PasswordTransformationMethod.getInstance()
                            passwordTVR?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0)
                        }
                        // Mover el cursor al final del texto para que siga visible después de ocultar/mostrar
                        it.setSelection(it.text.length)
                        return@setOnTouchListener true
                    }
                }


            }
            false
        }
    }

    private fun registerGoogle(){


            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth?.currentUser
                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerNewUser(){

        val email:String
        val psswd:String
        val psswdR:String
        email=emailTV!!.text.toString()
        psswd=passwordTV!!.text.toString()
        psswdR=passwordTVR!!.text.toString()

        if (email.isEmpty() && psswd.isEmpty() && psswdR.isEmpty()) {
            showEmptyFieldError(emailTV, "su correo electrónico")
            showEmptyFieldError(passwordTV, "una contraseña")
            showEmptyFieldError(passwordTVR, "el valor de repetir contraseña")
        } else if (email.isEmpty() && psswd.isEmpty()) {
            showEmptyFieldError(emailTV, "su correo electrónico")
            showEmptyFieldError(passwordTV, "una contraseña")
        } else if (email.isEmpty() && psswdR.isEmpty()) {
            showEmptyFieldError(emailTV, "su correo electrónico")
            showEmptyFieldError(passwordTVR, "el valor de repetir contraseña")
        } else if (psswd.isEmpty() && psswdR.isEmpty()) {
            showEmptyFieldError(passwordTV, "una contraseña")
            showEmptyFieldError(passwordTVR, "el valor de repetir contraseña")
        } else if (email.isEmpty()) {
            showEmptyFieldError(emailTV, "su correo electrónico")
        } else if (psswd.isEmpty()) {
            showEmptyFieldError(passwordTV, "una contraseña")
        } else if (psswdR.isEmpty()) {
            showEmptyFieldError(passwordTVR, "el valor de repetir contraseña")
        } else if (psswd != psswdR) {
            passwordTVR?.error = "Las contraseñas no coinciden"
            passwordTV?.error = "Las contraseñas no coinciden"
            passwordTVR?.requestFocus()
            passwordTV?.requestFocus()
        } else {

            progressBar!!.visibility= View.VISIBLE
            mAuth!!.createUserWithEmailAndPassword(email, psswd).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    Toast.makeText(this, "Registro satisfactorio", Toast.LENGTH_LONG).show()
                    Log.d(ContentValues.TAG, "User locale updated successfully")
                    progressBar!!.setVisibility(View.GONE)
                    val intent = Intent(this@RegistrarseActivity, IniciarSesionActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "El registro falló, intente más tarde", Toast.LENGTH_LONG)
                        .show()
                    Log.e(ContentValues.TAG, "Error updating user locale: ${task.exception}")
                    progressBar!!.setVisibility(View.GONE)


                }
            }
        }
    }
    fun showEmptyFieldError(field: EditText?, fieldName: String) {
        field?.error = "Ingrese $fieldName"
        field?.requestFocus()
    }
    private fun initializeUI() {
        emailTV = findViewById(R.id.etUsuarioR)
        passwordTV = findViewById(R.id.etContraR)
        passwordTVR = findViewById(R.id.etRepetirContraR)
        signup = findViewById(R.id.RegistrarseButton)
        signinScreen=findViewById(R.id.IniciarSesionScreen)
        googlebtn =findViewById(R.id.googlebtn)
        progressBar = findViewById(R.id.progressBar)
    }


}