package com.furkancolak.kotlininstagram

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.furkancolak.kotlininstagram.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import java.io.IOException

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            try {
                val inputSteram = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputSteram)
                val drawable = BitmapDrawable(resources, bitmap)
                binding.addRecipePhoto.setImageDrawable(drawable)
            }catch (e: IOException){
                e.printStackTrace()

            }
            }
        else {

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.addRecipePhoto.setOnClickListener{
            photoPicker()
        }

    }
    fun upload(view: View){

    }

    private fun photoPicker(){
        pickMedia.launch("image/*")
    }
}

