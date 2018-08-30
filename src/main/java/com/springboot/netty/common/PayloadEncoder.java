package com.springboot.netty.common;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@SuppressWarnings("all")
public class PayloadEncoder extends MessageToMessageEncoder {

    private static final Logger LOG = LoggerFactory.getLogger(PayloadEncoder.class);

    public PayloadEncoder() {

    }

    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        if (msg != null && msg instanceof CommandPayload) {
            CommandPayload ab = (CommandPayload)msg;
            out.add(Unpooled.wrappedBuffer(ab.encode()));
        } else if (msg != null && msg instanceof byte[]) {
            out.add(Unpooled.wrappedBuffer((byte[])((byte[])msg)));
        } else {
            LOG.error("Payload {} encode error!", msg.getClass());
        }
    }
}
