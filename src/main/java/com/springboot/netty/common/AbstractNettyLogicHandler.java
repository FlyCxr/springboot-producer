package com.springboot.netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;

public abstract class AbstractNettyLogicHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractNettyLogicHandler.class);

    public AbstractNettyLogicHandler() {
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
            LOG.debug("Receive sourceBytes :" + ByteUtil.format(sourceBytes));
            CommandPayload payload = this.decodePayload(payloadBuffer);
            LOG.debug("Receive payload: {}", payload.toString());
            this.doLogic(payload, ctx.channel());
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

    public abstract void doLogic(CommandPayload var1, Channel var2);

    public abstract CommandPayload decodePayload(ByteBuffer var1) throws Exception;
}
