package edu.up.isgc.navigation_rlg

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Home : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val myRef = Firebase.database.getReference("peliculas")

    val database = Firebase.database

    lateinit var peliculas: ArrayList<Peliculas>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extras = intent.extras
        auth = Firebase.auth
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                peliculas = ArrayList<Peliculas>()

                val value = snapshot.value
                Log.d("real-time-database", "Value is: " + value)
                snapshot.children.forEach{
                    unit ->
                    var pelicula = Peliculas(unit.child("nombre").value.toString(),
                        unit.child("anio").value.toString(),
                        unit.child("genero").value.toString(),
                        unit.key.toString())
                    peliculas.add(pelicula)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("real-time-database", "Failed to read value.", error.toException())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.Logout){
            auth.signOut()
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}