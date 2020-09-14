package com.example.livedatademo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.livedatademo.model.CaseViewModel
import kotlinx.android.synthetic.main.activity_case3.*
import kotlinx.android.synthetic.main.case1.*
import kotlinx.android.synthetic.main.case1.tvName

class Case2Activity : AppCompatActivity() {
    var mModel:CaseViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case2)
        mModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory(App.getInstance())
        ).get(CaseViewModel::class.java)

        mModel?.currentName?.observe(this, Observer {
            Log.e("WT","消费$it")
            tvName.text = it
        })

        mModel?.test?.observe(this, Observer {
            Log.e("WT","消费$it")
            tvName.text = it
        })
    }

    //修复数据丢失问题
    fun btn2(view: View) {
        mModel?.post()
    }
    //并发情况下会导致数据丢失
    fun liveData(view: View){
        Thread{
            mModel?.currentName?.postValue("Hello LiveData")
        }.start()
        Thread{
            mModel?.currentName?.postValue("Hello LiveData1")
        }.start()
    }



}