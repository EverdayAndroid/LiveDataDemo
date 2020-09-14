package com.example.livedatademo

import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Volatile
    private var mData = 0
    private val index =
        AtomicInteger(0)
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//        val atLeast =
//            com.example.livedatademo.Test.RESUMED.isAtLeast(com.example.livedatademo.Test.STARTED)
//        println(atLeast)
//        val map = SafeIterableMap<String,String>()
//        val putIfAbsent = map.putIfAbsent("wt", "hello")?:"123"
//        println(putIfAbsent)
//        val putIfAbsent1 = map.putIfAbsent("wt", "hello")
//        println(putIfAbsent1)
//        var index = 0
//        do {
//            println("============")
//            index = 1
//        }while (index == 0)


        for (index in 0 .. 3){
            Thread{
//                mData += 1
                add()
            }.start()
        }
        while (Thread.activeCount() > 1){
            Thread.yield()
        }
        println(mData)
    }
    @Synchronized
    private fun add(){
        mData++
//        index.getAndIncrement()
//        index.set(andIncrement)
    }
}