package com.trivia.quiz.utils

import android.content.Context
import android.media.MediaPlayer
import com.trivia.quiz.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MusicClass @Inject constructor(@ApplicationContext val context: Context){

    fun startTouchMusic(){
        val music: MediaPlayer = MediaPlayer.create(context, R.raw.touch)
        music.start()
    }

    fun startSuccessBeep(){
        val music: MediaPlayer = MediaPlayer.create(context, R.raw.touchbeep)
        music.start()
    }

    fun startFailedBeep(){
        val music: MediaPlayer = MediaPlayer.create(context, R.raw.failed)
        music.start()
    }


}