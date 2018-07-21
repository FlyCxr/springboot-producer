package com.springboot.socket.netty;

import com.springboot.exception.BusinessException;
import com.springboot.socket.Client;
import com.springboot.socket.ClientEventListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class AbstractNettyClient implements Client {
    private static Logger LOG = LoggerFactory.getLogger(AbstractNettyClient.class);
    protected String ip = "127.0.0.1";
    protected int port = 8888;
    protected Channel channel;
    protected int timeout = 10;
    protected Bootstrap bootstrap;
    protected volatile boolean isConnected;
    protected volatile boolean isClosed;
    protected int connectNum;
    protected int connectMaxNum;
    private ExecutorService executor = Executors.newFixedThreadPool(8);
    private NioEventLoopGroup workerGroup;

    public AbstractNettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public AbstractNettyClient() {
    }

    public void connect(String ip, int port, final ClientEventListener listener) {
        try {
            this.workerGroup = new NioEventLoopGroup();
            this.bootstrap = new Bootstrap();
            ((Bootstrap)((Bootstrap)((Bootstrap)this.bootstrap.group(this.workerGroup)).channel(NioSocketChannel.class)).remoteAddress(ip, port).option(ChannelOption.SO_KEEPALIVE, true)).handler(this.getChannelInitializer());
            this.channel = this.bootstrap.connect().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        LOG.info("Client connect succeed!");
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
                    LOG.info("Client connection closed!");
                    if (listener != null) {
                        listener.onClosed();
                    }

                }
            }).sync();
        } catch (Exception var8) {
            throw new BusinessException("Client connect error!", var8);
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

    public void reConnect() {
        if (!this.isClosed && !this.isConnected && this.connectNum++ < this.connectMaxNum) {
            LOG.debug("Client re connect num: {}", this.connectNum);

            try {
                Thread.sleep(2000L);
                this.connect();
            } catch (Exception var2) {
                LOG.error(var2.getMessage(), var2);
            }
        }

    }

    public abstract ChannelInitializer<SocketChannel> getChannelInitializer();

    public void send(final byte[] payload) {
        this.executor.execute(new Runnable() {
            public void run() {
                ClientMessageSender.send(payload, AbstractNettyClient.this.channel);
            }
        });
    }

    public void start() {
        this.connect();
    }

    public void stop() {
        this.workerGroup.shutdownGracefully();
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
}
