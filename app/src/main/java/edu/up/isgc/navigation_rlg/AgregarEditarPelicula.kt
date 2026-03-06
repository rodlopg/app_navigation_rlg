package edu.up.isgc.navigation_rlg

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database

class AgregarEditarPelicula : AppCompatActivity() {

    val myRef = Firebase.database.getReference("peliculas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_editar_pelicula)

        val nombre = findViewById<EditText>(R.id.nombre)
        val genero = findViewById<EditText>(R.id.genero)
        val anio = findViewById<EditText>(R.id.anio)
        val guardar = findViewById<Button>(R.id.guardar)

        val id = intent.getStringExtra("id")

        if (id != null) {
            nombre.setText(intent.getStringExtra("nombre"))
            genero.setText(intent.getStringExtra("genero"))
            anio.setText(intent.getStringExtra("anio"))
        }

        guardar.setOnClickListener {

            val peli = PeliAgrega(
                nombre.text.toString(),
                genero.text.toString(),
                anio.text.toString()
            )

            if (id == null) {
                myRef.push().setValue(peli)
            } else {
                myRef.child(id).setValue(peli)
            }

            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}