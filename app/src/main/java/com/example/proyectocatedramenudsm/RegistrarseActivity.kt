package com.example.proyectocatedramenudsm

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
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

        mAuth= FirebaseAuth.getInstance()
        initializeUI()


        signup!!.setOnClickListener { registerNewUser() }
        signinScreen!!.setOnClickListener {  val intent = Intent(this@RegistrarseActivity, IniciarSesionActivity::class.java)
            startActivity(intent)

        }
        googlebtn!!.setOnClickListener{

            registerGoogle()
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
        progressBar!!.visibility= View.VISIBLE
        val email:String
        val psswd:String
        val psswdR:String
        email=emailTV!!.text.toString()
        psswd=passwordTV!!.text.toString()
        psswdR=passwordTVR!!.text.toString()
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
        if(TextUtils.isEmpty(psswdR)){
            passwordTVR?.error = "Ingrese el valor de repetir contraseña"
            passwordTVR?.requestFocus()


            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, psswd).addOnCompleteListener {
                task ->
            if(task.isSuccessful){
                Toast.makeText(  this, "Registro satisfactorio", Toast.LENGTH_LONG).show()
                Log.d(ContentValues.TAG, "User locale updated successfully")
                progressBar!!.setVisibility(View.GONE)
                val intent = Intent(this@RegistrarseActivity, IniciarSesionActivity::class.java)
                startActivity(intent)

            }
            else{
                Toast.makeText(  this, "El registro falló, intente más tarde", Toast.LENGTH_LONG).show()
                Log.e(ContentValues.TAG, "Error updating user locale: ${task.exception}")
                progressBar!!.setVisibility(View.GONE)


            }
        }

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