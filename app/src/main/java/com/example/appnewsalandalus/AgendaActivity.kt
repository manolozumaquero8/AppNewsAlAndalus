package com.example.appnewsalandalus

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnewsalandalus.adapters.ContactoAdapter
import com.example.appnewsalandalus.providers.db.CrudContactos
import com.example.appnewsalandalus.databinding.ActivityAgendaBinding
import com.example.appnewsalandalus.models.ContactoModel
import com.google.firebase.auth.FirebaseAuth

class AgendaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgendaBinding
    private var lista = mutableListOf<ContactoModel>()
    private lateinit var adapter: ContactoAdapter
    private lateinit var crudContactos: CrudContactos
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setListeners()
        setRecycler()
    }

    private fun setRecycler() {
        val layoutManger=LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManger
        traerRegistros()
        adapter=ContactoAdapter(lista, {position->borrarContacto(position)}, {c->update(c)})
        binding.recyclerView.adapter=adapter
    }

    private fun update(c: ContactoModel){
        val i=Intent(this, AddActivity::class.java).apply {
            putExtra("CONTACTO", c)
        }
        startActivity(intent)
    }

    private fun borrarContacto(p: Int) {
        val id = lista[p].id

        if (crudContactos.borrar(id, userId)) {
            lista.removeAt(p)
            adapter.notifyItemRemoved(p)
            adapter.notifyItemRangeChanged(p, lista.size)
            Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se eliminó ningún registro", Toast.LENGTH_SHORT).show()
        }
    }


    private fun traerRegistros() {
        crudContactos = CrudContactos()
        lista = crudContactos.read(userId)
        if (lista.isNotEmpty()) {
            binding.ivLogo.visibility = View.INVISIBLE
        } else {
            binding.ivLogo.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {
        binding.fabAdd.setOnClickListener{
            startActivity(Intent(this, AddActivity::class.java))
        }
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    override fun onRestart() {
        super.onRestart()
        setRecycler()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_salir -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}