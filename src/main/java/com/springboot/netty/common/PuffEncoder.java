package com.springboot.netty.common;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PuffEncoder extends MessageToMessageEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PuffEncoder.class);

    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        //自定义协议转码
        if (msg != null && msg instanceof Object) {
            Object ab = msg;
            out.add(Unpooled.wrappedBuffer(this.encode(msg)));
        } else if (msg != null && msg instanceof byte[]) {
            out.add(Unpooled.wrappedBuffer((byte[])((byte[])msg)));
        } else {
            LOGGER.error("Payload {} encode error!", msg.getClass());
        }
    }

    /**
     * TODO 自定义对象编码器
     * @param msg
     * @return
     */
    public byte[] encode(Object msg) {
        return new byte[100];
    }
}
