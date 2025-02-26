package com.example.appnewsalandalus


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appnewsalandalus.databinding.ActivityDetalleNoticiaBinding
import com.example.appnewsalandalus.models.NewsArticulos

class DetalleNoticiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleNoticiaBinding
    private lateinit var noticia: NewsArticulos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetalleNoticiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recuperarNoticia()
        inicializarWebView()
        setListener()
    }

    private fun recuperarNoticia() {
        val datos = intent.extras
        noticia = datos?.getSerializable("NOTICIA") as NewsArticulos
        binding.webView.loadUrl(noticia.url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun inicializarWebView() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = android.view.View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = android.view.View.GONE
            }
        }

        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.settings.javaScriptEnabled = true
    }

    private fun setListener() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
}
