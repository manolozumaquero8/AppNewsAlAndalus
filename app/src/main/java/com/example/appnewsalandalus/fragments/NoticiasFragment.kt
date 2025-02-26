package com.example.appnewsalandalus.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnewsalandalus.DetalleNoticiaActivity
import com.example.appnewsalandalus.R
import com.example.appnewsalandalus.adapters.NewsAdapter
import com.example.appnewsalandalus.databinding.FragmentNoticiasBinding
import com.example.appnewsalandalus.models.NewsArticulos
import com.example.appnewsalandalus.providers.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticiasFragment : Fragment() {

    private lateinit var binding: FragmentNoticiasBinding
    private val listaNoticias = mutableListOf<NewsArticulos>()
    private val adapter = NewsAdapter(mutableListOf()) { noticia ->
        abrirNoticia(noticia)
    }
    private var apiKey = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoticiasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiKey = getString(R.string.news_api_key)
        setRecycler()
        cargarNoticias()
    }

    private fun setRecycler() {
        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.adapter = adapter
    }

    private fun cargarNoticias() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvMensaje.visibility = View.GONE
        binding.recView.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiClient.getEverything("tecnología", "es", getString(R.string.news_api_key))
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE

                    if (response.isSuccessful && response.body() != null) {
                        val noticias = response.body()?.articulos ?: emptyList()
                        if (noticias.isNotEmpty()) {
                            listaNoticias.clear()
                            listaNoticias.addAll(noticias)
                            adapter.updateData(listaNoticias)
                            binding.recView.visibility = View.VISIBLE
                        } else {
                            binding.tvMensaje.text = "No hay noticias disponibles"
                            binding.tvMensaje.visibility = View.VISIBLE
                        }
                    } else {
                        binding.tvMensaje.text = "Error ${response.code()}: ${response.message()}"
                        binding.tvMensaje.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.tvMensaje.text = "Error: ${e.message}"
                    binding.tvMensaje.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun abrirNoticia(noticia: NewsArticulos) {
        val intent = Intent(requireContext(), DetalleNoticiaActivity::class.java)
        intent.putExtra("NOTICIA", noticia)
        startActivity(intent)
    }

}
