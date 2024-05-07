package com.furkancolak.kotlininstagram

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.furkancolak.kotlininstagram.databinding.ActivityFeedBinding
import com.furkancolak.kotlininstagram.databinding.ActivityUploadBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth : FirebaseAuth
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // bağlama islemi
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.insta_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId ==R.id.add_post){
            val intent = Intent(this@FeedActivity,UploadActivity::class.java)
            startActivity(intent)
        }
        else if(item.itemId ==R.id.signout){
            auth.signOut()// sunucudan direkt çıkış yapar
            val intent = Intent(this@FeedActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}