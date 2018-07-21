package com.springboot.socket.demo;

import com.springboot.exception.BusinessException;
import com.springboot.socket.netty.AbstractNettyServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@SuppressWarnings("all")
@Component
public class PuffServer extends AbstractNettyServer {

    private static final Logger logger = LoggerFactory.getLogger(PuffServer.class);

    @Autowired
    private PuffServerHandler puffServerHandler;

    @Override
    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(
                        //允许接收100K的数据
                        new LengthFieldBasedFrameDecoder(102400, 2, 2, -4, 0,true){
                            Date startTime,endTime = null;
                            @Override
                            protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
                                Object obj = super.decode(ctx, in);
                                if (obj == null) {
                                    if (startTime == null) {
                                        startTime = new Date();
                                        logger.debug("start time:"+startTime);
                                    }else {
                                        endTime = new Date();
                                        logger.debug("end time:"+endTime);
                                    }

                                    //当拆包时间，超过60秒就断开链接
                                    if(startTime != null && endTime != null
                                            && endTime.getTime()-startTime.getTime() > 60*1000){
                                        logger.debug("超时！！！！ 相差时间："+ (endTime.getTime()-startTime.getTime()));
                                        startTime = null;
//                                        ctx.channel().close();
                                        throw new BusinessException("read data timeout...");
                                    }
                                }
                                return obj;
                            }
                        },
                        new PuffEncoder(),
                        new IdleStateHandler(0, 0, timeout),
                        puffServerHandler
                );
            }
        };
    }

    @PostConstruct
    @Override
    public void start() {
        super.start();
    }

    @PreDestroy
    @Override
    public void stop() {
        super.stop();
    }
}
