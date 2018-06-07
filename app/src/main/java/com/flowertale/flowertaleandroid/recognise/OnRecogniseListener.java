package com.flowertale.flowertaleandroid.recognise;

public interface OnRecogniseListener<T> {
    void onSuccess(T result);

    void onFailure();
}
