package com.springboot.netty.common;

import java.nio.ByteBuffer;

@SuppressWarnings("all")
public interface CommonCodeer {

    void decode(ByteBuffer byteBuffer);

    byte[] encode();

}
