package com.example.appnewsalandalus.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appnewsalandalus.R
import com.example.appnewsalandalus.models.NewsArticulos

class NewsAdapter(
    private var newsList: MutableList<NewsArticulos>,
    private val onDetailClick: (NewsArticulos) -> Unit
) : RecyclerView.Adapter<NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.news_layout, parent, false)
        return NewsViewHolder(v)
    }

    override fun getItemCount() = newsList.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.render(newsItem, onDetailClick)
    }

    fun updateData(newList: List<NewsArticulos>) {
        newsList.clear()
        newsList.addAll(newList)
        notifyDataSetChanged()
    }
}
