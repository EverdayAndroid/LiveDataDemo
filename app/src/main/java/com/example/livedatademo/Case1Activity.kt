package com.example.livedatademo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.livedatademo.model.CaseViewModel
import kotlinx.android.synthetic.main.case1.*

class Case1Activity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.case1)
        val viewModelProvider = ViewModelProvider(
            ViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory(App.getInstance())
        )
        val mModel = viewModelProvider.get(CaseViewModel::class.java)
        mModel.currentName.value = "LiveData_Case1"
        mModel.currentName.observe(this, Observer {
            tvName.text = it
        })
//        mModel.currentName.postValue("LiveData")

    }
}