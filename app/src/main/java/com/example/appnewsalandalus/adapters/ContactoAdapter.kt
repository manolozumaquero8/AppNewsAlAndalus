package com.example.appnewsalandalus.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appnewsalandalus.R
import com.example.appnewsalandalus.models.ContactoModel

class ContactoAdapter(
    var lista: MutableList<ContactoModel>,
    private val borrarContacto: (Int) -> Unit,
    private val updateContacto: (ContactoModel) -> Unit
) : RecyclerView.Adapter<ContactoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contacto_layout, parent, false)
        return ContactoViewHolder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        holder.render(lista[position], borrarContacto)
    }
}
