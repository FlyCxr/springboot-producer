package com.springboot.netty.common;

import com.springboot.exception.BusinessException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class AbstractNettyServer implements NettyServer{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNettyServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    protected int port;

    protected int timeout;

    public AbstractNettyServer() {

    }

    public AbstractNettyServer(int port, int timeout) {
        this.port = port;
        this.timeout = timeout;
    }

    protected abstract ChannelInitializer<SocketChannel> getChannelInitializer();

    @Override
    public void start() {
        LOGGER.debug("Start server ...");
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        ((ServerBootstrap)((ServerBootstrap)bootstrap.group(this.bossGroup, this.workerGroup).channel(NioServerSocketChannel.class)).option(ChannelOption.SO_BACKLOG, Integer.valueOf(100))).childHandler(this.getChannelInitializer());
        try {
            ChannelFuture f = bootstrap.bind(this.port).sync();
            LOGGER.debug("Start server succeed!");
        } catch (InterruptedException e) {
            throw new BusinessException("Server start error!", e);
        }
    }

    @Override
    public void stop() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
