package com.trivia.quiz.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trivia.quiz.InterFaces.DifficultyInterface
import com.trivia.quiz.Models.DifficultyModel
import com.trivia.quiz.databinding.DifficultyitemBinding

class DifficultyAdapter constructor(val arrayList: ArrayList<DifficultyModel>,
                                val difficultyInterface: DifficultyInterface
) : RecyclerView.Adapter<DifficultyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DifficultyAdapter.ViewHolder {
        val root = DifficultyitemBinding.inflate(LayoutInflater.from(parent.context),
        parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: DifficultyAdapter.ViewHolder, position: Int) {

        holder.binding!!.difficultyIv.setImageResource(arrayList[position].image)
        holder.binding!!.difficultyTv.text = arrayList[position].title
        holder.binding!!.difficultyLevelTv.text = "max Level increment upto ${arrayList[position].maxLevel}"
        holder.binding!!.questionCountTv.text = "Questions: ${arrayList[position].questionsCount}"

        holder.binding!!.root.setOnClickListener {
            difficultyInterface.getDifficulty(arrayList[position])
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(val difficultyitemBinding: DifficultyitemBinding):
        RecyclerView.ViewHolder(difficultyitemBinding.root) {

            var binding: DifficultyitemBinding? = null
        init {
            binding = difficultyitemBinding!!
        }

    }
}