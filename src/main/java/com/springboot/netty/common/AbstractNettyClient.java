package com.springboot.netty.common;

import com.springboot.exception.BusinessException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("all")
public abstract class AbstractNettyClient implements NettyClient{

    private static Logger LOG = LoggerFactory.getLogger(AbstractNettyClient.class);
    protected String ip;
    protected int port;
    protected Channel channel;
    protected int timeout;
    protected Bootstrap bootstrap;
    protected volatile boolean isConnected;
    protected volatile boolean isClosed;
    protected int connectNum;
    protected int connectMaxNum;
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
    private NioEventLoopGroup workerGroup;

    public AbstractNettyClient() {
    }

    public AbstractNettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void start() {
        this.connect();
    }

    @Override
    public void stop() {
        this.workerGroup.shutdownGracefully();
    }

    public void connect(String ip, int port, final ClientEventListener listener) {
        try {
            this.workerGroup = new NioEventLoopGroup();
            this.bootstrap = new Bootstrap();
            ((Bootstrap)((Bootstrap)((Bootstrap)this.bootstrap.group(this.workerGroup)).channel(NioSocketChannel.class)).remoteAddress(ip, port).option(ChannelOption.SO_KEEPALIVE, true)).handler(this.getChannelInitializer());
            this.channel = this.bootstrap.connect().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        AbstractNettyClient.LOG.debug("Client connect succeed!");
                        AbstractNettyClient.this.isConnected = true;
                        AbstractNettyClient.this.connectNum = 0;
                        if (listener != null) {
                            listener.onConnected();
                        }
                    }

                }
            }).sync().channel();
            this.channel.closeFuture().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    AbstractNettyClient.this.isConnected = false;
                    AbstractNettyClient.LOG.debug("Client connection closed!");
                    if (listener != null) {
                        listener.onClosed();
                    }

                }
            }).sync();
        } catch (Exception e) {
            throw new BusinessException("Client connect error!", e);
        } finally {
            this.isConnected = false;
            this.workerGroup.shutdownGracefully();
            this.reConnect();
        }

    }

    public void connect() {
        this.connect((ClientEventListener)null);
    }

    public void connect(final ClientEventListener listener) {
        this.executor.execute(new Runnable() {
            public void run() {
                AbstractNettyClient.this.connect(AbstractNettyClient.this.ip, AbstractNettyClient.this.port, listener);
            }
        });
    }

    public void reConnect() {
        if (!this.isClosed && !this.isConnected && this.connectNum++ < this.connectMaxNum) {
            LOG.debug("Client re connect num: {}", this.connectNum);

            try {
                Thread.sleep(2000L);
                this.connect();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

    }

    public abstract ChannelInitializer<SocketChannel> getChannelInitializer();

    public void send(final CommandPayload payload) {
        if (this.isConnected) {
            this.executor.execute(new Runnable() {
                public void run() {
                    NettyMessageSender.send(payload, AbstractNettyClient.this.channel);
                }
            });
        }

    }

    public void send(final byte[] payload) {
        this.executor.execute(new Runnable() {
            public void run() {
                NettyMessageSender.send(payload, AbstractNettyClient.this.channel);
            }
        });
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getConnectMaxNum() {
        return this.connectMaxNum;
    }

    public void setConnectMaxNum(int connectMaxNum) {
        this.connectMaxNum = connectMaxNum;
    }

    public boolean isClose() {
        return this.isClosed;
    }

    public boolean isConnect() {
        return this.isConnected;
    }

    public void setClosed(boolean closed) {
        this.isClosed = closed;
    }

    public void close() {
        if (!this.isClosed && this.channel != null && this.channel.isOpen()) {
            this.isClosed = true;
        }
    }

}
