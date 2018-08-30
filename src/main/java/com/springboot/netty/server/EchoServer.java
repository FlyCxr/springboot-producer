package com.springboot.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import java.net.InetSocketAddress;

/**
 * Netty服务端  在当有客户端连接到服务端打印客户端的IP和端口
 *
 * 当接收到客户端数据时原样返回客户端
 */
@SuppressWarnings("all")
public class EchoServer {

    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public EchoServer(Integer port) {
        this.port = port;
    }

    public EchoServer() {
    }

    public static void main(String[] args) throws Exception{
        new EchoServer(8090).start();
    }

    /**
     * 启动NettyServer
     */
    private void start() throws Exception{
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup  group  = new NioEventLoopGroup();
        try {
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)//指定NIO传输
                    .localAddress(new InetSocketAddress(port))//使用指定的端口设置套接字地址
                    .childHandler(new ChannelInitializer<SocketChannel>() {//添加一个EchoServerHandler到子Channel的ChannelPipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    @ChannelHandler.Sharable//标示一个ChannelHandler 可以被多个Channel安全地共享
    class EchoServerHandler extends ChannelInboundHandlerAdapter {

        //在与客户端的连接建立之后被调用
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println("Client " + ctx.channel().remoteAddress() + " connected");
            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
            ctx.writeAndFlush(in);
        }

        //在ByteBuf字节容器为空时关闭与客户端的连接
//        @Override
//        public void channelReadComplete(ChannelHandlerContext ctx) {
//            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener((ChannelFutureListener.CLOSE));
//        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        //在与客户端的断连之后被调用
        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            System.out.println("Client " + ctx.channel().remoteAddress() + " disconnected");
            ctx.fireChannelActive();
        }
    }
}
