package com.tvolution.tvtransformer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {

    val playPauseLiveData : MutableLiveData<Boolean> = MutableLiveData()

    fun pauseVideo(){
        playPauseLiveData.value = false
    }

    fun playVideo(){
        playPauseLiveData.value = true
    }


}