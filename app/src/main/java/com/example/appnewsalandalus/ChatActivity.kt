package com.example.appnewsalandalus


import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnewsalandalus.adapters.ChatAdapter
import com.example.appnewsalandalus.databinding.ActivityChatBinding
import com.example.appnewsalandalus.models.ChatModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    var emailUsuarioLogeado = ""

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    private var listadoChats = mutableListOf<ChatModel>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        emailUsuarioLogeado = auth.currentUser?.email.toString()

        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)

        databaseRef = FirebaseDatabase.getInstance().getReference("chat")

        setRecycler()
        setListeners()

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvChats.layoutManager = layoutManager
        adapter = ChatAdapter(listadoChats, emailUsuarioLogeado)
        binding.rvChats.adapter = adapter
    }


    private fun setListeners() {
        binding.btnSend.setOnClickListener {
            enviar()
        }

        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listadoChats.clear()
                for(nodo in snapshot.children) {
                    val chatNodo = nodo.getValue(ChatModel::class.java)
                    if(chatNodo != null) {
                        listadoChats.add(chatNodo)
                    }
                }
                listadoChats.sortBy { it.fecha }
                adapter.updateAdapter(listadoChats)
                binding.rvChats.scrollToPosition(listadoChats.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Error al recuperar los chats", Toast.LENGTH_SHORT).show()
            }

        })

        binding.etMensaje.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                enviar()
                ocultarTeclado()
                true
            } else {
                false
            }
        }
    }

    private fun enviar() {
        val texto = binding.etMensaje.text.toString().trim()
        if(texto.isEmpty()) return
        val fecha = System.currentTimeMillis()
        val mensaje = ChatModel(emailUsuarioLogeado, texto, fecha)
        val key = fecha.toString()
        databaseRef.child(key).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseRef.child(key).setValue(mensaje)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {
                        Toast.makeText(this@ChatActivity, "No se pudo guardar el mensaje", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        binding.etMensaje.setText("")
    }

    private fun ocultarTeclado() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus?: View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
        }
}

