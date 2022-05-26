package com.kd23243.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener(){
            val Intent = Intent(this, SignUp::class.java)
            startActivity(Intent)
        }

        btnLogIn.setOnClickListener(){
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            login(email,password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for login in user

                    val intent = Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Login,"user does not exist", Toast.LENGTH_SHORT).show()

                }
            }
    }
}