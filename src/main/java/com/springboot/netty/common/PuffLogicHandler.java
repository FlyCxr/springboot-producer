package com.springboot.netty.common;


import io.netty.channel.Channel;

import java.nio.ByteBuffer;

@SuppressWarnings("all")
public class PuffLogicHandler extends AbstractNettyLogicHandler {

    @Override
    public void doLogic(CommandPayload var1, Channel var2) {

    }

    @Override
    public CommandPayload decodePayload(ByteBuffer var1) throws Exception {
        return null;
    }
}
