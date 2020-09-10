package com.example.livedatademo

import androidx.lifecycle.MutableLiveData
import java.util.*

class LiveDateBus private constructor() {
    private val bus: MutableMap<String, MutableLiveData<Any>>

    @Synchronized
    fun <T> with(key: String, clz: Class<T>?): MutableLiveData<T>{
        if (!bus.containsKey(key)) {
            bus[key] = MutableLiveData()
        }
        return bus[key] as MutableLiveData<T>
    }

    companion object {
        val instance = LiveDateBus()
    }

    init {
        bus = HashMap()
    }
}