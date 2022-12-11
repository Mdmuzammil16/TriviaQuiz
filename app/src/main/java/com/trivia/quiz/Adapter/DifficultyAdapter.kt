package com.trivia.quiz.Adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trivia.quiz.InterFaces.DifficultyInterface
import com.trivia.quiz.Models.DifficultyModel
import com.trivia.quiz.R
import com.trivia.quiz.databinding.DifficultyitemBinding
import com.trivia.quiz.utils.MusicClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class DifficultyAdapter constructor(val arrayList: ArrayList<DifficultyModel>,
                                val difficultyInterface: DifficultyInterface,val activity: Activity
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
        val scale = activity.resources.displayMetrics.density


        holder.binding!!.frontCard.cameraDistance = 8000 * scale
        holder.binding!!.cardBack.cameraDistance = 8000 * scale

      val   front_animation = AnimatorInflater.loadAnimator(activity, R.animator.front_anim) as AnimatorSet
      val  back_animation = AnimatorInflater.loadAnimator(activity, R.animator.back_anim) as AnimatorSet

        holder.binding!!.difficultyIv.setImageResource(arrayList[position].image)
        holder.binding!!.difficultyTv.text = arrayList[position].title
        holder.binding!!.difficultyLevelTv.text = "Level Increment: ${arrayList[position].maxLevel}"
        holder.binding!!.questionCountTv.text = "Questions: ${arrayList[position].questionsCount}"


        holder.binding!!.factsTv.text = arrayList[position].facts
        var isFront = true
        holder.binding!!.startBtn.setOnClickListener {
            if (!isFront){
                difficultyInterface.getDifficulty(arrayList[position])

            }

        }



        holder.binding!!.root.setOnClickListener {


            if (isFront){
                front_animation.setTarget(holder.binding!!.frontCard);
                back_animation.setTarget(holder.binding!!.cardBack);
                front_animation.start()
                back_animation.start()
                isFront = false
            }else{
                front_animation.setTarget(holder.binding!!.cardBack);
                back_animation.setTarget(holder.binding!!.frontCard);
                back_animation.start()
                front_animation.start()
                isFront = true

            }
            holder.binding!!.root.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                holder.binding!!.root.isEnabled = true
                holder.binding!!.startBtn.isEnabled = true
            },1000)

        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(difficultyitemBinding: DifficultyitemBinding):
        RecyclerView.ViewHolder(difficultyitemBinding.root) {

            var binding: DifficultyitemBinding? = null
        init {
            binding = difficultyitemBinding
        }

    }
}