package com.springboot.socket;

@SuppressWarnings("all")
public interface ClientEventListener {

    void onConnected();

    void onClosed();

}
