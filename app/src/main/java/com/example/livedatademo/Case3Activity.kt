package com.example.livedatademo

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.livedatademo.model.CaseViewModel
import kotlinx.android.synthetic.main.activity_case3.*
import kotlinx.android.synthetic.main.activity_case3.tvName
import kotlinx.android.synthetic.main.case1.*

/**
 * @author wt
 * @date 10:54 2020/9/13
 * @description   注册监听
 **/
class Case3Activity : AppCompatActivity() {
    private var mModel:CaseViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case3)
        mModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory(App.getInstance())
        ).get(CaseViewModel::class.java)

    }

    //多次注册
    fun btnClick(view: View) {
        mModel?.currentName?.observe(this, Observer {
            Log.e("WT","消费$it")
            tvName.text = it
        })
        mModel?.currentName?.value = "Hello LiveDta"
    }

    private val observer = Observer<String> {
        Log.e("WT","消费$it")
        tvName.text = it
    }
    //fix修复多次注册
    fun btnClick1(view: View) {
        mModel?.demo1?.observe(this, observer)
        mModel?.demo1?.value = "Hello LiveDta"
    }
}