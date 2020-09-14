package com.example.livedatademo

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData


object LiveDataUtils {
    private var handler: Handler? = null
    fun <T> setValue(mld: MutableLiveData<T>?, d: T) {
        if (mld == null) {
            return
        }
        if (Thread.currentThread() === Looper.getMainLooper().thread) {
            mld.setValue(d)
        } else {
            postSetValue(mld, d)
        }
    }

    fun <T> postSetValue(mld: MutableLiveData<T>, d: T) {
        if (handler == null) {
            handler = Handler(Looper.getMainLooper())
        }

        handler?.post(SetValueRunnable.create(mld, d))
    }

    class  SetValueRunnable<T>():Runnable{
        var liveData: MutableLiveData<T>? = null
        var obj:T? = null
        constructor(mld: MutableLiveData<T>,obj:T):this(){
            this.liveData = mld
            this.obj = obj
        }

        override fun run() {
            liveData?.value = obj
        }
        companion object{
            fun <T> create(mld: MutableLiveData<T>,obj:T):SetValueRunnable<T>{
                return SetValueRunnable(mld,obj)
            }
        }
    }

//    private class SetValueRunnable<T> private constructor(
//        private val liveData: MutableLiveData<T>,
//        private val data: T
//    ) :
//        Runnable {
//        override fun run() {
//            liveData.value = data
//        }
//
//        companion object {
//            fun <T> create(liveData: MutableLiveData<T>, data: T): SetValueRunnable<T> {
//                return SetValueRunnable(liveData, data)
//            }
//        }
//
//    }
}