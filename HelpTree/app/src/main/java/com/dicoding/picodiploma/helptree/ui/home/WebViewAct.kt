package com.dicoding.picodiploma.helptree.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.helptree.data.News
import com.dicoding.picodiploma.helptree.databinding.ActivityWebViewBinding

class WebViewAct : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    companion object {
        const val EXTRA_ID = "id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val nonton = intent.getParcelableExtra<News>(EXTRA_ID)

        Glide.with(this)
            .load(nonton?.photo)
            .into(binding.imgPoto)
        binding.title.text= nonton?.name
        binding.url.text= nonton?.path
        binding.describe.text=nonton?.desc
    }

}
