package com.example.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.activity.hook.HookHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun start(view:View){
        Log.e("TAG", ""+Build.VERSION.SDK_INT);
        HookHelper.hookIActivityManager()
        HookHelper.hookHandler()


        startActivity(Intent(this,TargetActivity::class.java))
    }
}
