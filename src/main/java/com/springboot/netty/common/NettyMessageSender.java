package com.springboot.netty.common;

import io.netty.channel.Channel;

public class NettyMessageSender {
    public static void send(CommandPayload payload, Channel channel) {
        channel.writeAndFlush(payload);
    }

    public static void send(byte[] payload, Channel channel) {
        channel.writeAndFlush(payload);
    }
}
