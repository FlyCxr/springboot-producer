package com.springboot.netty.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
@SuppressWarnings("all")
@Component
public class HearbeatTrigger extends ChannelInboundHandlerAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(HearbeatTrigger.class);

    public static Map<Channel,String> nettyChannelMap = new HashMap();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LOGGER.debug("heartBeatCheck Start");
        //某种场景下，比如保持心跳，每30秒记录未心跳标记到缓存中，90秒之后remove连接
        if(false){
            nettyChannelMap.remove(ctx.channel());
            ctx.channel().close();
        }
        LOGGER.debug("hearBeatCheck end");
    }
}
