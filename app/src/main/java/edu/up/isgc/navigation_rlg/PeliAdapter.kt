package edu.up.isgc.navigation_rlg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PeliAdapter(
    private val listaPeliculas: ArrayList<Peliculas>,
    private val onEditar: (Peliculas) -> Unit,
    private val onEliminar: (Peliculas) -> Unit
) : RecyclerView.Adapter<PeliAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.itemNombre)
        val anio: TextView = view.findViewById(R.id.itemAnio)
        val genero: TextView = view.findViewById(R.id.itemGenero)

        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelicula = listaPeliculas[position]

        holder.nombre.text = pelicula.nombre
        holder.anio.text = pelicula.anio
        holder.genero.text = pelicula.genero

        holder.btnEditar.setOnClickListener {
            onEditar(pelicula)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminar(pelicula)
        }
    }

    override fun getItemCount(): Int = listaPeliculas.size
}