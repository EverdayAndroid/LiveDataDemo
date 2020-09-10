package com.example.livedatademo

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
/**
  * @date: 2020/4/20
  * @author: wangTao
  * @email: wangtaohandsome@gmail.com
  * @desc:  修复LiveData数据粘性
 */
class LiveDataBus {

    companion object{
        private lateinit var liveDataBus:LiveDataBus
        private var map = HashMap<String,MutableLiveData1<Any>>()
        fun get():LiveDataBus{
            liveDataBus = LiveDataBus()
            return liveDataBus
        }
    }

    fun <T> with(key:String,clz:Class<T>): BusMutableLiveData<T> {
        if(!map.containsKey(key)){
            map[key] = BusMutableLiveData()
        }
        return map[key] as BusMutableLiveData<T>
    }

    fun <T> with1(key:String,clz:Class<T>): MutableLiveData1<T> {
        if(!map.containsKey(key)){
            map[key] = MutableLiveData1()
        }
        return map[key] as MutableLiveData1<T>
    }


    class BusMutableLiveData<T>: MutableLiveData1<T>(){
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            hook(observer)
        }

        private fun hook(observer:Observer<in T>){
            val listDataClass= LiveDataCase::class.java
            Log.e("TAG","listDataClass  ${this::class.java.superclass?.superclass}")
            val mObservers = listDataClass.getDeclaredField("mObservers")
            mObservers.isAccessible = true
            val observers = mObservers.get(this)
            Log.e("TAG","fieldAny   ${observers.toString()}")
            //获取到class类型
            val observersClass = observers::class.java
            //获取到SafeIterableMap类里的get方法，获取到
            val getMethod = observersClass.getDeclaredMethod("get", Any::class.java)
            getMethod.isAccessible = true
//            //执行方法获取
            val objectWrapperEntry = getMethod.invoke(observers, observer)
            var observerWrapper:Any? = null
//            //获取到Map.Entry类型
            if(objectWrapperEntry is Map.Entry<*,*>){
                observerWrapper= objectWrapperEntry.value
            }
            if(observerWrapper == null){
                //todo 提示异常信息
                return
            }
            val wrapperClass = observerWrapper.javaClass.superclass
            Log.e("TAG", "observerWrapperClass   $wrapperClass")
            //获取到mLastVersion
            val fieldLastVersion = wrapperClass?.getDeclaredField("mLastVersion")
            fieldLastVersion?.isAccessible = true
            //获取到mVersion
            val fieldVersion = listDataClass.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            //获取到mVersion值
            val versionValue = fieldVersion.get(this)
            //进行赋值
            fieldLastVersion?.set(observerWrapper,versionValue)
        }
    }
}