package com.springboot.socket.netty;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

@SuppressWarnings("all")
@Component
public class ServerMessageSender {

    public ServerMessageSender() {
    }

    public static void send(byte[] payload, Channel channel) {
        channel.writeAndFlush(payload);
    }
}
