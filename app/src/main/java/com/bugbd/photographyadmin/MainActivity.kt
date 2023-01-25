package com.bugbd.photographyadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bugbd.photographyadmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        clickEvent()
    }

    private fun clickEvent() {
        binding.SlidersMC.setOnClickListener {
            startActivity(Intent(this, SliderActivity::class.java))

        }

        binding.CategoryMC.setOnClickListener {
            startActivity(Intent(this, CategoryListActivity::class.java))

        }
        binding.VideoMC.setOnClickListener {
            startActivity(Intent(this, VideoListActivity::class.java))

        }
        binding.PhotographyMC.setOnClickListener {
            startActivity(Intent(this, PhotographyPhotoPoseActivity::class.java))

        }
        binding.PhotographyMC.setOnClickListener {
            startActivity(Intent(this, PhotographyPhotoPoseActivity::class.java))

        }



    }
}