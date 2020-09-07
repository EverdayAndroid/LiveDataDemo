package com.example.livedatademo.model

import androidx.lifecycle.ViewModel
import com.example.livedatademo.MutableLiveData1

class CaseViewModel:ViewModel() {
    val currentName: MutableLiveData1<String> by lazy {
        MutableLiveData1<String>()
    }
}