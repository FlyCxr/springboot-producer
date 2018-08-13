package com.springboot.netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.nio.ByteBuffer;

@Service
@SuppressWarnings("all")
public class PuffHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PuffHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf && ((ByteBuf)msg).isReadable()) {
            ByteBuf byteBuf = (ByteBuf)msg;
            if (byteBuf.readableBytes() <= 0) {
                return;
            }
            ByteBuffer payloadBuffer = byteBuf.nioBuffer();
            byte[] sourceBytes = new byte[payloadBuffer.remaining()];
            payloadBuffer.get(sourceBytes);
            payloadBuffer.flip();
            LOGGER.debug("Receive byte msg : {}",sourceBytes);
            this.doLogic(sourceBytes, ctx.channel());
        }
    }

    /**
     * 业务处理模块方法
     * @param sourceBytes
     * @param channel
     */
    public void doLogic(byte[] sourceBytes, Channel channel) {
        //TODO 将byte[] 转码解析成自定义对象
        LOGGER.debug("Receive payload: {}", "将byte[] 转码解析成自定义对象");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
