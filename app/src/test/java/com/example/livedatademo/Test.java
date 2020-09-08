package com.example.livedatademo;

import androidx.annotation.NonNull;

public enum  Test {
    DESTROYED,


    INITIALIZED,


    CREATED,


    STARTED,


    RESUMED;


    public boolean isAtLeast(@NonNull Test state) {
        return compareTo(state) >= 0;
    }
}
