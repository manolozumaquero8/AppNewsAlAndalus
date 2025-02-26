package com.example.appnewsalandalus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appnewsalandalus.providers.db.CrudContactos
import com.example.appnewsalandalus.databinding.ActivityAddBinding
import com.example.appnewsalandalus.models.ContactoModel
import com.google.firebase.auth.FirebaseAuth

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var userId: String
    private var nombre = ""
    private var telefono = ""
    private var apellidos = ""
    private var id = -1
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recogerContacto()
        setListeners()

        if (isUpdate) {
            binding.etTitle2.text = "Editar Contacto"
            binding.btn2Enviar.text = "EDITAR"
        }
    }

    private fun recogerContacto() {
        val datos = intent.extras
        if (datos != null) {
            val c = datos.getSerializable("CONTACTO") as ContactoModel
            isUpdate = true
            nombre = c.nombre
            apellidos = c.apellidos
            telefono = c.telefono
            id = c.id
            pintarDatos()
        }
    }

    private fun pintarDatos() {
        binding.etNombre.setText(nombre)
        binding.etApellidos.setText(apellidos)
        binding.etTelefono.setText(telefono)
    }

    private fun setListeners() {
        binding.btnCancelar.setOnClickListener {
            finish()
        }
        binding.btn2Reset.setOnClickListener {
            limpiar()
        }
        binding.btn2Enviar.setOnClickListener {
            guardarRegistro()
        }
    }

    private fun guardarRegistro() {
        if (datosCorrectos()) {
            val c = ContactoModel(id, nombre, apellidos, telefono, userId)

            if (!isUpdate) {
                if (CrudContactos().create(c, userId) != -1L) {
                    Toast.makeText(
                        this,
                        "Se ha añadido un registro a la agenda",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    binding.etTelefono.error = "¡Teléfono duplicado!"
                }
            } else {
                if (CrudContactos().update(c)) {
                    Toast.makeText(this, "Registro Editado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    binding.etTelefono.error = "¡Teléfono duplicado!"
                }
            }
        }
    }


    private fun datosCorrectos(): Boolean {
        nombre = binding.etNombre.text.toString().trim()
        apellidos = binding.etApellidos.text.toString().trim()
        telefono = binding.etTelefono.text.toString().trim()

        if (nombre.length < 3) {
            binding.etNombre.error = "El campo nombre debe tener al menos 3 caracteres"
            return false
        }
        if (apellidos.length < 5) {
            binding.etApellidos.error = "El campo apellidos debe tener al menos 5 caracteres"
            return false
        }
        if (!telefono.matches(Regex("^\\d{9}$"))) {
            binding.etTelefono.error = "Debes introducir un número de teléfono válido"
            return false
        }
        return true
    }

    private fun limpiar() {
        binding.etNombre.setText("")
        binding.etApellidos.setText("")
        binding.etTelefono.setText("")
    }
}

