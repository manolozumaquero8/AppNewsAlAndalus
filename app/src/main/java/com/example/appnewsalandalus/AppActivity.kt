package com.example.appnewsalandalus


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.appnewsalandalus.databinding.ActivityAppBinding
import com.example.appnewsalandalus.fragments.EventosFragment
import com.example.appnewsalandalus.fragments.NoticiasFragment
import com.example.appnewsalandalus.fragments.SensoresFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        setListeners()

        if (savedInstanceState == null) {
            replaceFragment(NoticiasFragment())
        }
    }

    private fun setListeners() {
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_agenda -> {
                    abrirAgenda()
                    true
                }
                R.id.nav_noticias -> {
                    replaceFragment(NoticiasFragment())
                    true
                }
                R.id.nav_sensor -> {
                    replaceFragment(SensoresFragment())
                    true
                }
                R.id.nav_eventos -> {
                    replaceFragment(EventosFragment())
                    true
                }
                R.id.nav_chat -> {
                    abrirChat()
                    true
                }
                R.id.nav_multimedia -> {
                    abrirMultimedia()
                    true
                }
                R.id.item_salir -> {
                    finishAffinity()
                    true
                }
                R.id.item_cerrar -> {
                    auth.signOut()
                    finish()
                    true
                }
                else -> false
            }.also {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }

    private fun abrirChat() {
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
    }

    private fun abrirMultimedia() {
        val intent = Intent(this, MultimediaActivity::class.java)
        startActivity(intent)
    }

    private fun abrirAgenda() {
        val intent = Intent(this, AgendaActivity::class.java)
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fgPrincipal, fragment)
            .commit()
    }
}
