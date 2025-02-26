package com.example.appnewsalandalus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appnewsalandalus.databinding.ActivityActualizarContactoBinding
import com.example.appnewsalandalus.models.ContactoModel
import com.example.appnewsalandalus.providers.db.CrudContactos

class ActualizarContactos : AppCompatActivity() {
    private lateinit var binding: ActivityActualizarContactoBinding
    private lateinit var contacto: ContactoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityActualizarContactoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        contacto = intent.getSerializableExtra("CONTACTO") as? ContactoModel
            ?: run {
                Toast.makeText(this, "No se pudo cargar el contacto", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

        cargarDatosContacto()
        setListeners()
    }

    private fun cargarDatosContacto() {
        binding.etNombre.setText(contacto.nombre)
        binding.etApellidos.setText(contacto.apellidos)
        binding.etTelefono.setText(contacto.telefono)
    }

    private fun setListeners() {
        binding.btnGuardar.setOnClickListener { guardarCambios() }
        binding.btnCancelar.setOnClickListener { cancelarEdicion() }
    }

    private fun guardarCambios() {
        val nuevoNombre = binding.etNombre.text.toString().trim()
        val nuevoApellidos = binding.etApellidos.text.toString().trim()
        val nuevoTelefono = binding.etTelefono.text.toString().trim()

        if (nuevoNombre.isEmpty() || nuevoApellidos.isEmpty() || nuevoTelefono.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        contacto = contacto.copy(nombre = nuevoNombre, apellidos = nuevoApellidos, telefono = nuevoTelefono)

        val resultado = CrudContactos().update(contacto)

        if (resultado) {
            Toast.makeText(this, "Contacto actualizado correctamente.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar el contacto.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelarEdicion() {
        Toast.makeText(this, "Edición cancelada.", Toast.LENGTH_SHORT).show()
        finish()
    }
}
