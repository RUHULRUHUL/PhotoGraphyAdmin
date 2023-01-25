package com.bugbd.photographyadmin

import android.content.Intent
import android.media.MediaDrm
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugbd.photographyadmin.databinding.ActivitySliderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SliderActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySliderBinding

    private lateinit var reference: DatabaseReference
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var sliderList: MutableList<Slider>

    private val logDebug: String = "SliderActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_slider)

        initView()
        getSliders()
    }

    private fun initView() {

        reference = Firebase.database.reference

        sliderList = mutableListOf<Slider>()


        binding.backPressImg.setOnClickListener {
            onBackPressed()
        }

        binding.FloatBtn.setOnClickListener {
            val intent = Intent(this, UploadSliderActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getSliders() {

        reference.child(Utils.sliders)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        sliderList.clear()

                        for (sliderSnap in snapshot.children) {
                            val slider = sliderSnap.getValue(Slider::class.java)
                            if (slider != null) {
                                Log.d(logDebug, "item: - " + slider.title)
                                Log.d(logDebug, "item: - " + slider.link)
                                Log.d(logDebug, "item: - " + slider.imageUri)
                                Log.d(logDebug, "item: - " + slider.videoId)
                                sliderList.add(slider)
                            }
                        }
                        sliderAdapter = SliderAdapter(sliderList, this@SliderActivity)
                        binding.SliderListRV.layoutManager = LinearLayoutManager(
                            this@SliderActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding.SliderListRV.adapter = sliderAdapter


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