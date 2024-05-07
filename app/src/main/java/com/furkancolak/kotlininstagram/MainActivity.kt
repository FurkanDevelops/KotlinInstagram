package com.furkancolak.kotlininstagram

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.furkancolak.kotlininstagram.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize Firebase Auth
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if(currentUser!=null){
            val intent = Intent(this@MainActivity,FeedActivity::class.java)
            startActivity(intent)
            finish()
            // burada eskiden giriş yapıldıysa eğer kullanıcıyı giriş ekranına atmıyor
        }

    }
    fun signInClicked(view: View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this@MainActivity,FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this@MainActivity,"Enter email and password",Toast.LENGTH_LONG).show()
        }
    }
    fun signUpClicked(view: View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this@MainActivity,FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this,"Enter email and password",Toast.LENGTH_LONG).show()

        }
    }
}