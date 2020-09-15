package com.example.livedatademo.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.livedatademo.LiveDataUtils
import com.example.livedatademo.MutableLiveData1
import com.example.livedatademo.User

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



    val userLiveData = MutableLiveData<User>()
    val strLiveData = MutableLiveData<String>()

    fun userToString(user: User){
        userLiveData.value = user
        val map = Transformations.map(userLiveData) {
            "${it.name} ====${it.age}"
        }
        strLiveData.value = map.value
    }
    fun switchMap(user: User){
//        userLiveData.value = user
//        val map = Transformations.switchMap(userLiveData) {
//            User("aaaa",1111)
//        }
//        strLiveData.value = map.value
    }
}