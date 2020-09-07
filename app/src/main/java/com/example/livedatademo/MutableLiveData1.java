package com.example.livedatademo;

public class MutableLiveData1<T>  extends LiveDataCase<T> {
    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }
}