package com.bugbd.photographyadmin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.bugbd.photographyadmin.databinding.ActivityUploadSliderBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UploadSliderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadSliderBinding

    private lateinit var storageRef: StorageReference
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private var imageUri: Uri? = null
    var indexNumber: Int = 0

    private lateinit var progressDialog: ProgressDialog


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_slider)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_slider)

        requestPermissions()

        // creating a storage reference
        storageRef = FirebaseStorage.getInstance().reference
        database = Firebase.database
        reference = Firebase.database.reference


        binding.SliderImgMC.setOnClickListener {
            choseGallery()
        }


        reference.child(Utils.sliders)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        indexNumber = (snapshot.childrenCount.toInt())
                        Toast.makeText(
                            this@UploadSliderActivity,
                            "Database  Exits item $indexNumber",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        binding.UploadBtn.setOnClickListener {

            var title: String = binding.SliderNameET.text.toString()
            var link: String = binding.EnterBrowserLinkET.text.toString()
            var videoId: String = binding.VideoIdET.text.toString()



            if (title.isEmpty()) {
                binding.titleContainer.helperText = "Enter Title"
            } else if (imageUri == null) {
                Toast.makeText(this, "Add Slider Image ", Toast.LENGTH_SHORT).show()
            } else if (videoId.isEmpty()) {
                binding.videoIdContainer.helperText = "Enter Video Id "
            } else {
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Please Wait for Image Upload")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()


                val sd = getFileName(applicationContext, imageUri!!)
                val uploadTask = storageRef.child("file/$sd").putFile(imageUri!!)
                uploadTask.addOnSuccessListener {

                    storageRef.child("file/$sd").downloadUrl.addOnSuccessListener { imageUri ->

                        Log.d("FileUrl", "Url: $imageUri ")
                        if (imageUri != null) {

                            val slider = Slider(
                                title,
                                link,
                                videoId,
                                imageUri.toString()
                            )

                            reference.child(Utils.sliders).child((indexNumber + 1).toString())
                                .setValue(slider)
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

                        } else {
                            progressDialog.dismiss()
                        }
                    }.addOnFailureListener {
                        progressDialog.dismiss()
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Log.e("Firebase", "Image Upload fail")
                }


            }


        }

    }


    private fun choseGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        imagePickerActivityResult.launch(galleryIntent)
    }

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                imageUri = result.data?.data

                if (imageUri != null) {
                    Glide.with(this@UploadSliderActivity)
                        .load(imageUri)
                        .into(binding.SliderImg)
                }
            }
        }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }


    private fun requestPermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Toast.makeText(
                            applicationContext,
                            "All the permissions are granted..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show()
            }
            .onSameThread().check()
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@UploadSliderActivity)

        builder.setTitle("Need Permissions")

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }
}