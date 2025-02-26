package com.example.appnewsalandalus.adapters

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.appnewsalandalus.R
import com.example.appnewsalandalus.databinding.ChatLayoutBinding
import com.example.appnewsalandalus.models.ChatModel
import java.text.SimpleDateFormat
import java.util.Date

class ChatViewHolder(v: View): RecyclerView.ViewHolder(v) {
    private val binding = ChatLayoutBinding.bind(v)

    fun render(item: ChatModel, emailUsuarioLogeado: String) {
        val params = binding.cardViewChat.layoutParams as FrameLayout.LayoutParams

        if (emailUsuarioLogeado == item.email) {
            binding.clChat.setBackgroundColor(binding.tvFecha.context.getColor(R.color.azul_claro))
            params.gravity = Gravity.END
        } else {
            binding.clChat.setBackgroundColor(binding.tvFecha.context.getColor(R.color.ic_launcher_background))
            params.gravity = Gravity.START
        }

        binding.tvEmail.text = item.email
        binding.tvMensaje.text = item.mensaje
        binding.tvFecha.text = fechaFormateada(item.fecha)
    }

    private fun fechaFormateada(fecha: Long): String {
        val date = Date(fecha)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return format.format(date)
    }
}
