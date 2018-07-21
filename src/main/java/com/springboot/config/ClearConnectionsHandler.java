package com.springboot.config;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@SuppressWarnings("all")
@Component
public class ClearConnectionsHandler extends Thread implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearConnectionsHandler.class);

    @Value("${http.maxIdleTime}")
    private String clearTime;

    @Autowired
    private HttpClientConnectionManager httpClientConnectionManager;

    private volatile boolean shutdown;

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(Integer.valueOf(clearTime));
                    // 关闭失效的连接
                    httpClientConnectionManager.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
        LOGGER.info("ClearConnectionsHandler start run！！！");
    }
}
