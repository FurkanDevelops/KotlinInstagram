package com.furkancolak.kotlininstagram.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkancolak.kotlininstagram.R
import com.furkancolak.kotlininstagram.adapter.FeedRecyclerAdapter
import com.furkancolak.kotlininstagram.databinding.ActivityFeedBinding
import com.furkancolak.kotlininstagram.model.Post
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var feedAdapter: FeedRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        // çıkış yapılınca firebase de de çıkış yapsın diye yaptık
        db = Firebase.firestore
        postArrayList = ArrayList<Post>()
        getData()
        binding.recylerView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedRecyclerAdapter(postArrayList)
        binding.recylerView.adapter = feedAdapter
    }

    private fun getData(){
        // firebaseden verileri alır
        // collection içine firebase de nasılsa o şekilde yazman lazım
        db.collection("Post").addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value!=null){
                    // değişken boş mu
                    if(!value.isEmpty){
                    // girilen değer boş değilse
                        val documents = value.documents
                        postArrayList.clear()
                        for(documents in documents){
                            // as demek gelen değer string olacak demek
                            // document.gets içinde olan firestora içinde olanla olmalı
                            val comment = documents.get("comment") as String
                            val userEmail = documents.get("userEmail") as String
                            val downloadUrl = documents.get("downloadUrl") as String
                            println(comment)
                            val post = Post(userEmail,comment,downloadUrl)
                            postArrayList.add(post)
                        }
                        // güncelleme
                        feedAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // bağlama islemi
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.insta_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.add_post){
            val intent = Intent(this@FeedActivity, UploadActivity::class.java)
            startActivity(intent)
        }
        else if(item.itemId == R.id.signout){
            auth.signOut()// sunucudan direkt çıkış yapar
            val intent = Intent(this@FeedActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}