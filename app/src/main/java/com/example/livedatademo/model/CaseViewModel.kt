package com.example.livedatademo.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.livedatademo.LiveDataUtils
import com.example.livedatademo.MutableLiveData1

class CaseViewModel:ViewModel() {
    val currentName: MutableLiveData1<String> by lazy {
        MutableLiveData1<String>()
    }

    val test = MutableLiveData<String>()
    fun post(){
        LiveDataUtils.postSetValue(test,"fix数据丢失1")
        LiveDataUtils.postSetValue(test,"fix数据丢失2")
    }




    val demo1: MutableLiveData1<String> by lazy {
        MutableLiveData1<String>()
    }
}