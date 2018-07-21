package com.springboot.socket.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PuffEncoder extends MessageToMessageEncoder {

    private static final Logger LOG = LoggerFactory.getLogger(PuffEncoder.class);

    public PuffEncoder() {
    }

    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        out.add((byte[])msg);
    }

}
