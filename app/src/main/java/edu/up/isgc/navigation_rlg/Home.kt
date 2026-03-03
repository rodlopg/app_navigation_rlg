package edu.up.isgc.navigation_rlg

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    private fun llenaLista(){
        val adaptador = PeliAdapter(this, peliculas)
        val lista = findViewById<ListView>(R.id.lista)
        lista.adapter = adaptador
        Log.d("real-time-database", "Items in list: ${peliculas.size}")
        Log.d("real-time-database", "ListView adapter count: ${lista.adapter.count}")
    }

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
        val agregaPeliculas = findViewById<FloatingActionButton>(R.id.addMovies)

        agregaPeliculas.setOnClickListener{
            val pelicula = PeliAgrega("nombre", "genero", "anio")
            myRef.push().setValue(pelicula).addOnCompleteListener{
                task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Pelicula Agregada", Toast.LENGTH_LONG).show()
                }
            }
        }

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
                val lista = findViewById<ListView>(R.id.lista)
                lista.setOnItemClickListener{ adapterView, view, i, l ->
                    Toast.makeText(this@Home, peliculas[i].nombre.toString(), Toast.LENGTH_SHORT).show()
                }
                llenaLista()
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