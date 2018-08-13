package com.springboot.netty.server;

import com.springboot.exception.BusinessException;
import com.springboot.netty.common.HearbeatTrigger;
import com.springboot.netty.common.PuffEncoder;
import com.springboot.netty.common.PuffHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@Component
public class PuffServer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PuffServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    @Autowired
    private HearbeatTrigger puffTrigger;

    @Autowired
    private PuffHandler puffHandler;

    @Autowired
    private PuffEncoder puffEncoder;

    /**
     * 启动NettyServer
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }

    public void start() {
        LOGGER.info("Start server ...");
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        ((ServerBootstrap)((ServerBootstrap)bootstrap
                .group(this.bossGroup, this.workerGroup)
                .channel(NioServerSocketChannel.class))
                .option(ChannelOption.SO_BACKLOG, Integer.valueOf(100)))
                .childHandler(this.getChannelInitializer());
        try {
            ChannelFuture f = bootstrap.bind(this.port).sync();
            LOGGER.info("Start server succeed!");
        } catch (InterruptedException var3) {
            throw new BusinessException("Server start error!", var3);
        }
    }

    /**
     * 停止NettyServer
     */
    @PreDestroy
    public void stop() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    /**
     * 自定义通道
     * @return
     */
    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                ch.pipeline().addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                ch.pipeline().addLast(puffTrigger);
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
                                        LOGGER.debug("start time:"+startTime);
                                    }else {
                                        endTime = new Date();
                                        LOGGER.debug("end time:"+endTime);
                                    }
                                    //当拆包时间，超过60秒就断开链接
                                    if(startTime != null && endTime != null && endTime.getTime()-startTime.getTime() > 60*1000){
                                        LOGGER.debug("超时！！！！ 相差时间："+ (endTime.getTime()-startTime.getTime()));
                                        startTime = null;
                                        //ctx.channel().close();
                                        throw new BusinessException("read data timeout...");
                                    }
                                }
                                return obj;
                            }
                        },
                        puffEncoder,
                        puffHandler
                );
            }
        };
    }

    private Integer port;

    private Integer timeOut;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public PuffServer() {
    }

    public PuffServer(Integer port, Integer timeOut) {
        this.port = port;
        this.timeOut = timeOut;
    }

    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public void setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

}
