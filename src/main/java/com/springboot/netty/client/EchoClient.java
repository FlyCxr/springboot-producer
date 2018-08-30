package com.springboot.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import java.net.InetSocketAddress;

/**
 * Netty客户端，当在连接到服务端后，服务端返回Netty rocks!到客户端
 *
 * 客户端传输任何数据到服务端，服务端都原样返回给客户端
 */
@SuppressWarnings("all")
public class EchoClient {

    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public EchoClient() {
    }

    public static void main(String[] args) throws Exception{
        new EchoClient("127.0.0.1",8090).start();
    }

    private void start() throws Exception{
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group  = new NioEventLoopGroup();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new  InetSocketAddress(host, port))
                    .handler((new ChannelInitializer<SocketChannel>() {//添加一个EchoClientHandler到子Channel的ChannelPipeline
                        @Override
                        protected void  initChannel (SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    }));
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    @ChannelHandler.Sharable//标示一个ChannelHandler 可以被多个Channel安全地共享
    class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

        //当从服务器接收到一条消息时被调用
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
            System.out.println("Client received: " + byteBuf.toString(CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

        //在与服务端的连接建立之后被调用
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println("server " + ctx.channel().remoteAddress() + " connected");
            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        }

        //在与服务端的连接中断之后被调用
        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            System.out.println("server " + ctx.channel().remoteAddress() + " disconnected");
            ctx.fireChannelActive();
        }
    }
}
