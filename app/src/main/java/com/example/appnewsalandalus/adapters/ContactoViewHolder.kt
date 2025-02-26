package com.example.appnewsalandalus.adapters

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.appnewsalandalus.ActualizarContactos
import com.example.appnewsalandalus.databinding.ContactoLayoutBinding
import com.example.appnewsalandalus.models.ContactoModel

class ContactoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val binding = ContactoLayoutBinding.bind(v)

    fun render(
        c: ContactoModel,
        borrarContacto: (Int) -> Unit,
    ) {
        binding.tvNombre.text = c.nombre
        binding.tvTelefono.text = c.telefono
        binding.tvApellidos.text = c.apellidos

        binding.btnBorrar.setOnClickListener {
            borrarContacto(adapterPosition)
        }
        binding.btnUpdate.setOnClickListener {
            val intent = Intent(itemView.context, ActualizarContactos::class.java)
            intent.putExtra("CONTACTO", c)
            itemView.context.startActivity(intent)
        }
    }
}



