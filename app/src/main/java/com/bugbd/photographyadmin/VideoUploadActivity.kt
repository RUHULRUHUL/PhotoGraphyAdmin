package com.bugbd.photographyadmin

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bugbd.photographyadmin.databinding.ActivityVideoUploadBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VideoUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoUploadBinding

    private lateinit var categoryList: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var storageRef: StorageReference
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    var indexNumber: Int = 0

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_upload)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_upload)

        initView()
        setAutoCompleteTXT()
        clickEvent()
    }

    private fun setAutoCompleteTXT() {

        reference.child(Utils.categories).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    categoryList.clear()
                    for (i in snapshot.children) {
                        val category = i.getValue(Category::class.java)
                        if (category != null) {
                            categoryList.add(category.categoryTitle.toString())
                            Log.d(
                                "VideoUpload",
                                "upload video " + category.categoryTitle.toString()
                            )

                        }

                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        adapter = ArrayAdapter(this, R.layout.dropdown_item, categoryList)
        binding.autoCompleteTxt.setAdapter(adapter)


    }

    private fun clickEvent() {

        reference.child(Utils.videos)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        indexNumber = (snapshot.childrenCount.toInt())

                        categoryList.clear()
                        for (i in snapshot.children) {

                            val category = i.getValue(Category::class.java)
                            if (category != null) {
                                category.categoryTitle?.let { categoryList.add(it) }
                            }
                        }

                        Toast.makeText(
                            this@VideoUploadActivity,
                            "Video   Exits item $indexNumber",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        binding.UploadBtn.setOnClickListener {


            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Please Wait for video save")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()


            var title: String = binding.videoTitleET.text.toString()
            var description: String = binding.descriptionET.text.toString()
            var videoId: String = binding.VideoIdET.text.toString()
            var categoryName = binding.autoCompleteTxt.text.toString()


            if (title.isEmpty()) {
                binding.titleContainer.helperText = "Enter Title"
            } else if (categoryName.isEmpty()) {
                Toast.makeText(this, "select category ", Toast.LENGTH_SHORT).show()
            } else if (description.isEmpty()) {
                Toast.makeText(this, "Enter video description", Toast.LENGTH_SHORT).show()
            } else if (videoId.isEmpty()) {
                binding.videoIdContainer.helperText = "Enter Video Id "
            } else {
                val video = Video(
                    title,
                    categoryName,
                    description,
                    videoId,
                )

                reference.child(Utils.videos).child((indexNumber + 1).toString())
                    .setValue(video)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this,
                                "Slider Upload Success",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            progressDialog.dismiss()
                        }
                    }


            }


        }
    }

    private fun initView() {
        // creating a storage reference
        storageRef = FirebaseStorage.getInstance().reference
        database = Firebase.database
        reference = Firebase.database.reference



        categoryList = mutableListOf()
    }
}