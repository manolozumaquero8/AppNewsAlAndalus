package com.example.appnewsalandalus.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsResponse(
    @SerializedName("articles") val articulos: List<NewsArticulos>
) : Serializable

data class NewsArticulos(
    @SerializedName("title") val titulo: String,
    @SerializedName("description") val descripcion: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String,
) : Serializable








