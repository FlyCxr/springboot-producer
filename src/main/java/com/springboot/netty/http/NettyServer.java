package com.springboot.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class NettyServer {

    public static void main(String[] args) throws Exception {
        startHttp(8090);
//        startHttps(8091);
    }

    public static void startHttp(Integer httpPort) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        bootstrap.group(eventExecutors)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("initChannel ch:" + ch);
                        ch.pipeline()
                          .addLast("decoder", new HttpRequestDecoder())
                          .addLast("encoder", new HttpResponseEncoder())
                          .addLast("aggregator", new HttpObjectAggregator(512 * 1024))//512kb
                          .addLast("handler", new NettyHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128) // determining the number of connections queued
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        bootstrap.bind(httpPort).sync();
    }

    public static void startHttps(Integer httpsPort) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        bootstrap.group(eventExecutors)
                .channel(NioServerSocketChannel.class)
                .childHandler(new SSLChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 128) // determining the number of connections queued
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        bootstrap.bind(httpsPort).sync();
    }
}
