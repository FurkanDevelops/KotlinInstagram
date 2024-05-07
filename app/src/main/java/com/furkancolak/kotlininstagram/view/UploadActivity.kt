package com.furkancolak.kotlininstagram.view

import android.os.Bundle
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.furkancolak.kotlininstagram.R
import com.furkancolak.kotlininstagram.databinding.ActivityUploadBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID

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
    private lateinit var auth : FirebaseAuth // Kullanıcı bilgileri
    private lateinit var firestore: FirebaseFirestore // Toplu depolama
    private lateinit var storage: FirebaseStorage // İmage depolama
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

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage =  Firebase.storage

        binding.addRecipePhoto.setOnClickListener{
            photoPicker()
        }
        // firebase e yükleme işlemi
        binding.button3.setOnClickListener{
            // yükleme kısmı
            val uuid = UUID.randomUUID()// uydurma rakam veriyor her yapınca resimler farklı isimle kaydedilsin diye
            val imageName = "$uuid.jpg" // isimlendirme
            val reference = storage.reference // en baştaki storage referansını al
            val imageReference = reference.child("images/${imageName}") // images klasörüne gir
            if(binding.addRecipePhoto.drawable != null){
                val bitmap = (binding.addRecipePhoto.drawable as BitmapDrawable).bitmap
                val uri = getImageUri(this, bitmap)
                imageReference.putFile(uri).addOnSuccessListener {
                    // download url -> firestore
                    val uploadPictureReference = storage.reference.child("images").child(imageName)
                    uploadPictureReference.downloadUrl.addOnSuccessListener {
                        val downloadUrl = it.toString()

                        val postMap = hashMapOf<String,Any>()
                        postMap.put("downloadUrl",downloadUrl)
                        //  firebase de nasıl gözeküceğei anahtar kelime , diğeri kaydedilen
                        postMap.put("userEmail",auth.currentUser!!.email!!)
                        postMap.put("comment",binding.commendText.text.toString())
                        postMap.put("date",Timestamp.now())// güncel zaman ne ise onu alır

                        firestore.collection("Post").add(postMap).addOnSuccessListener{
                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }

                    }.addOnFailureListener {
                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun photoPicker(){
        pickMedia.launch("image/*")
    }
    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

}

