package com.trivia.quiz.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.trivia.quiz.Models.CategoryModel
import com.trivia.quiz.R
import com.trivia.quiz.databinding.CategoryitemBinding
import com.trivia.quiz.utils.MusicClass

class CategoryAdapter constructor(val arrayList: ArrayList<CategoryModel>,
val musicClass: MusicClass) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val root = CategoryitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {

       holder.mybinding!!.catIv.setImageResource(arrayList[position].image)
        holder.mybinding!!.catTitleTv.text = arrayList[position].title
        holder.mybinding!!.catDescTv.text = arrayList[position].desc

        holder.mybinding!!.root.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("category", arrayList[position].value)

            Navigation.findNavController(it).navigate(
            R.id.action_categoryFragment2_to_difficultyFragment,
            bundle)
            musicClass.startTouchMusic()
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

   inner class ViewHolder(val binding: CategoryitemBinding): RecyclerView.ViewHolder(binding.root) {

       var mybinding: CategoryitemBinding? = null
       init {
           this.mybinding = binding
       }
    }
}