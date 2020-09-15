package com.example.livedatademo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.arch.core.util.Function
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.livedatademo.model.CaseViewModel
import kotlinx.android.synthetic.main.activity_case4.*

/**
 * @author wt
 * @date 23:11 2020/9/15
 * @description  数据转换
 **/
class Case4Activity : AppCompatActivity() {
    private var mModel: CaseViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case4)
        mModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory(App.getInstance())
        ).get(CaseViewModel::class.java)



        mModel?.strLiveData?.observe(this, Observer {
            tv1.text = it
        })


    }

    fun btn(view: View){
        mModel?.switchMap(User("LiveData",11111))
    }

    fun btn1(view: View){
        mModel?.userToString(User("LiveData",11111))
    }

}