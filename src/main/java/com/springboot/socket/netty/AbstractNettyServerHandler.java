package com.springboot.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;

@SuppressWarnings("all")
public abstract class AbstractNettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractNettyServerHandler.class);

    public AbstractNettyServerHandler() {
    }

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
            this.doLogic(sourceBytes, ctx.channel());
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    public abstract void doLogic(byte[] var1, Channel var2);

    public abstract byte[] decodePayload(ByteBuffer var1) throws Exception;
}
