package com.springboot.netty.common;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class HearbeatHandler extends ChannelInboundHandlerAdapter {

    private static Logger LOG = LoggerFactory.getLogger(HearbeatHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        if(ctx.channel().isActive()){
            String address = ctx.channel().remoteAddress().toString();
            if(evt instanceof IdleStateEvent){
                LOG.debug("client address[{}] Hearbeat start",address);
                IdleState state = ((IdleStateEvent)evt).state();
                //Chancle通道90秒无数据写入强制关闭客户端连接
                if(state == IdleState.READER_IDLE){
                    ctx.channel().close();
                }
            }else{
                super.userEventTriggered(ctx,evt);
            }
        }
    }
}
