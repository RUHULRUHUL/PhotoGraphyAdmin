package com.bugbd.photographyadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugbd.photographyadmin.databinding.ActivityVideoListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
class VideoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoListBinding
    private lateinit var reference: DatabaseReference
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var videoList: MutableList<Video>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_list)

        initView()
        getVideoList()

    }


    private fun initView() {

        reference = Firebase.database.reference

        videoList = mutableListOf<Video>()

        binding.backPressImg.setOnClickListener {
            onBackPressed()
        }

        binding.FloatBtn.setOnClickListener {
            val intent = Intent(this, VideoUploadActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getVideoList() {

        reference.child(Utils.videos).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    videoList.clear()

                    for (i in snapshot.children) {
                        val video = i.getValue(Video::class.java)
                        if (video != null) {
                            videoList.add(video)
                        }
                    }
                    videoAdapter = VideoAdapter(videoList, this@VideoListActivity)
                    binding.SliderListRV.layoutManager = LinearLayoutManager(
                        this@VideoListActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    binding.SliderListRV.adapter = videoAdapter


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}