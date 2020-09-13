package com.example.livedatademo

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.livedatademo.model.CaseViewModel
import kotlinx.android.synthetic.main.activity_case3.*
/**
 * @author wt
 * @date 10:54 2020/9/13
 * @description   注册监听
 **/
class Case3Activity : AppCompatActivity() {
    var mModel:CaseViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case3)
        mModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory(App.getInstance())
        ).get(CaseViewModel::class.java)
        mModel?.currentName?.observe(this, Observer {
            tvName.text = it
        })
        mModel?.currentName?.observeForever(Observer {

        })
        mModel?.currentName?.observeForever {
            tvName.text = it
        }
    }


    fun btn(view: View) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
    fun btn1(view: View) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun btn2(view: View) {
        tvName.text = "Hello word"
    }

    fun liveData(view: View){
        mModel?.currentName?.value = "Hello LiveData"
//        mModel?.currentName?.postValue("Hello LiveData")
    }
}