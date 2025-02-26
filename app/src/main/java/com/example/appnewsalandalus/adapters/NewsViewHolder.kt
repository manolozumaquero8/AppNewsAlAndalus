package com.example.appnewsalandalus.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.appnewsalandalus.databinding.NewsLayoutBinding
import com.example.appnewsalandalus.models.NewsArticulos
import com.squareup.picasso.Picasso

class NewsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val binding = NewsLayoutBinding.bind(v)

    fun render(news: NewsArticulos, onDetailClick: (NewsArticulos) -> Unit) {
        binding.tvTitle.text = news.titulo
        binding.tvDescription.text = news.descripcion
        news.urlToImage.let {Picasso.get().load(it).into(binding.ivImage)}
        itemView.setOnClickListener {
            onDetailClick(news)
        }
    }
}









