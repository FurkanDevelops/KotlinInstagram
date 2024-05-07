package com.furkancolak.kotlininstagram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.furkancolak.kotlininstagram.databinding.RecyclerRowBinding
import com.furkancolak.kotlininstagram.model.Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(private val postList: ArrayList<Post>):RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>(){
            // yukarıda yazan private val postList gösterilecek datalar
    class PostHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root) {
        // binding den sonra gelen recyclerView için yapılan layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        // binding ile bağlama işlemi yapılmalı
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerEmailText.text = postList.get(position).email
        holder.binding.recyclerCommentText.text = postList.get(position).comment
        Picasso.get().load(postList.get(position).downloadUrl).into(holder.binding.recyclerImageView)

    }
}