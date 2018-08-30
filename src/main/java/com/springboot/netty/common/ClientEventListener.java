package com.springboot.netty.common;

public interface ClientEventListener {
    void onConnected();

    void onClosed();
}
