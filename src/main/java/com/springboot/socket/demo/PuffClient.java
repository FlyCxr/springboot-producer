package com.springboot.socket.demo;

import com.springboot.socket.netty.AbstractNettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;

@SuppressWarnings("all")
@Component
public class PuffClient extends AbstractNettyClient{

    private static Logger LOG = LoggerFactory.getLogger(PuffClient.class);

    @Override
    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(
                        new PuffEncoder(){
                            @Override
                            protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
                                out.add((byte[])msg);
                            }
                        },
                        new IdleStateHandler(0, 0, timeout),
                        new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                sendHeartbeat();
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                if(msg != null && msg instanceof ByteBuf){
                                    ByteBuf buffer = (ByteBuf)msg;
                                    byte[] bytes = buffer.array();
                                    String s = new String(bytes);
                                    LOG.info("服务端响应消息："+s);
                                }
                            }

                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                sendHeartbeat();
                            }
                        }
                );
            }
        };
    }

    @PostConstruct
    @Override
    public void connect() {
        super.connect();
    }

    private void sendHeartbeat() {
        send("心跳来了".getBytes());
    }
}
