package com.example.appnewsalandalus


import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.example.appnewsalandalus.databinding.ActivityMultimediaBinding

class MultimediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMultimediaBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMultimediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupVideoPlayer()
        setListeners()
    }

    private fun setupVideoPlayer() {
        val videoPath = "android.resource://" + packageName + "/" + R.raw.video
        val uri = Uri.parse(videoPath)

        binding.videoView.setVideoURI(uri)
        binding.videoView.setMediaController(MediaController(this))
        binding.videoView.requestFocus()
    }

    private fun setListeners() {
        binding.btnPlayVideo.setOnClickListener {
            binding.videoView.start()
        }

        binding.btnPlayAudio.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.audio)
                mediaPlayer?.start()
            } else {
                mediaPlayer?.start()
            }
        }

        binding.btnStopAudio.setOnClickListener {
            mediaPlayer?.pause()
            mediaPlayer?.seekTo(0)
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
}
