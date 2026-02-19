package edu.up.isgc.navigation_rlg

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
    }
    fun login(view: View){
        val user_mail = findViewById<EditText>(R.id.email).text.toString()
        val user_pass = findViewById<EditText>(R.id.password).text.toString()

        if(!user_mail.isBlank() && !user_pass.isBlank()){
            auth.signInWithEmailAndPassword(user_mail,user_pass).addOnCompleteListener{task ->
                if(task.isSuccessful){
                    Toast.makeText(this,"Succesful LogIn", Toast.LENGTH_LONG).show()

                    startActivity(Intent(this, HomeLogin::class.java).putExtra("email",user_mail))
                    finish()
                }else{
                    Toast.makeText(this,task.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        } else{
            Toast.makeText(this, "Fill in all required inputs", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart(){
        super.onStart()
        val usuarioActual = Firebase.auth.currentUser

        if (usuarioActual != null){
            val user_mail = usuarioActual?.email.toString()
            Toast.makeText(this,"User already autenticated", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomeLogin::class.java).putExtra("email",user_mail))
            finish()
        }
    }
}