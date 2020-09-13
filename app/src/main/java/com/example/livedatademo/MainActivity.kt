package com.example.livedatademo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //先发送后注册，为什么可以收到消息
    fun case1(view: View) {
        LiveDataBus.get().with1("wt", String::class.java).postValue("Hello LiveData")
        startActivity(Intent(this,Case1Activity::class.java))
    }

    fun case2(view: View) {
        LiveDataBus.get().with("wt", String::class.java).postValue("Hello LiveData")
        startActivity(Intent(this,Case2Activity::class.java))
    }

    fun case3(view: View) {
        //多次注册
        startActivity(Intent(this,Case3Activity::class.java))
    }


}