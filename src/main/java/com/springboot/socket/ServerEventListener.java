package com.springboot.socket;

@SuppressWarnings("all")
public interface ServerEventListener {

    void onConnected();

    void onClosed();

}
