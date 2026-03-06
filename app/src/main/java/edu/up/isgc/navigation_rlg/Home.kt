package edu.up.isgc.navigation_rlg

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    lateinit var peliculas: ArrayList<Peliculas>


    private fun llenaLista() {
        val lista = findViewById<RecyclerView>(R.id.lista)
        lista.layoutManager = LinearLayoutManager(this)

        val adaptador = PeliAdapter(
            peliculas,
            onEditar = { pelicula ->
                val intent = Intent(this, AgregarEditarPelicula::class.java)
                intent.putExtra("id", pelicula.id)
                intent.putExtra("nombre", pelicula.nombre)
                intent.putExtra("anio", pelicula.anio)
                intent.putExtra("genero", pelicula.genero)
                startActivity(intent)
            },
            onEliminar = { pelicula ->
                myRef.child(pelicula.id!!).removeValue()
            }
        )

        lista.adapter = adaptador
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

        auth = Firebase.auth

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val agregaPeliculas = findViewById<FloatingActionButton>(R.id.addMovies)

        agregaPeliculas.setOnClickListener {
            val intent = Intent(this, AgregarEditarPelicula::class.java)
            startActivity(intent)
        }

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                peliculas = ArrayList()

                snapshot.children.forEach { unit ->
                    val pelicula = Peliculas(
                        unit.child("nombre").value.toString(),
                        unit.child("anio").value.toString(),
                        unit.child("genero").value.toString(),
                        unit.key.toString()
                    )
                    peliculas.add(pelicula)
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Logout) {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}