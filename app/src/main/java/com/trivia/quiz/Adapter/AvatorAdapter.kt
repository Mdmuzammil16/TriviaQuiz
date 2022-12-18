package com.trivia.quiz.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trivia.quiz.InterFaces.AvatorInterface
import com.trivia.quiz.databinding.AvatoritemBinding

class AvatorAdapter constructor(val list: List<Int>, val avatorInterface: AvatorInterface): RecyclerView.Adapter<AvatorAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  ViewHolder {
        val view = AvatoritemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:  ViewHolder, position: Int) {
        holder.binding.avatorIv.setImageResource(list[position])
        holder.binding.root.setOnClickListener {
            avatorInterface.getAvatorImage(list[position])
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }


    class ViewHolder(val binding: AvatoritemBinding) : RecyclerView.ViewHolder(binding.root)
}