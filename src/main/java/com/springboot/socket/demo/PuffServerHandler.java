package com.springboot.socket.demo;

import com.springboot.socket.netty.AbstractNettyServerHandler;
import com.springboot.socket.netty.ServerMessageSender;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public class PuffServerHandler extends AbstractNettyServerHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PuffServerHandler.class);

    @Autowired
    private ServerMessageSender serverMessageSender;

    @Override
    public void doLogic(byte[] data, Channel channel) {
        String s = new String(data);
        LOG.info("接收到消息：" + s);
        try {
            serverMessageSender.send("返回消息呵呵".getBytes(), channel);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
    }

    @Override
    public byte[] decodePayload(ByteBuffer byteBuffer) throws Exception {
        return byteBuffer.array();
    }
}
