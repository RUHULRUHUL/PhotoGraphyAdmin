package com.bugbd.photographyadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugbd.photographyadmin.databinding.ActivityCategoryListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CategoryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryListBinding
    private lateinit var reference: DatabaseReference
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryList: MutableList<Category>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_list)
        initView()
        getSliders()
    }

    private fun initView() {

        reference = Firebase.database.reference

        categoryList = mutableListOf<Category>()


        binding.backPressImg.setOnClickListener {
            onBackPressed()
        }

        binding.FloatBtn.setOnClickListener {
            val intent = Intent(this, CategoryCreate::class.java)
            startActivity(intent)
        }

    }

    private fun getSliders() {

        reference.child(Utils.categories)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        categoryList.clear()

                        for (i in snapshot.children) {
                            val category = i.getValue(Category::class.java)
                            if (category != null) {
                                categoryList.add(category)
                            }
                        }
                        categoryAdapter = CategoryAdapter(categoryList, this@CategoryListActivity)
                        binding.categoryRV.layoutManager = LinearLayoutManager(
                            this@CategoryListActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding.categoryRV.adapter = categoryAdapter

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