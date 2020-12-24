package com.example.demo.frontend.builder;

public interface Builder<T,Y> {
    T encode(Y uiPatient);
    Y decode(T uiPatient);
}
