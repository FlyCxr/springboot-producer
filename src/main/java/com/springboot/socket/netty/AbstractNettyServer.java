package com.springboot.socket.netty;

import com.springboot.exception.BusinessException;
import com.springboot.socket.Server;
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
public abstract class AbstractNettyServer implements Server {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractNettyServer.class);
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    protected int port = 8888;
    protected int timeout = 6000;

    public AbstractNettyServer() {
    }

    public void start() {
        LOG.info("Start NettyServer ...");
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        ((ServerBootstrap)((ServerBootstrap)bootstrap.group(this.bossGroup, this.workerGroup).channel(NioServerSocketChannel.class)).option(ChannelOption.SO_BACKLOG, Integer.valueOf(100))).childHandler(this.getChannelInitializer());
        try {
            ChannelFuture f = bootstrap.bind(this.port).sync();
            LOG.info("Start NettyServer succeed!");
        } catch (InterruptedException var3) {
            throw new BusinessException("NettyServer Start error!", var3);
        }
    }

    public abstract ChannelInitializer<SocketChannel> getChannelInitializer();

    public void stop() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
