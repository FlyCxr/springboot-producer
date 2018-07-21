package com.springboot.socket;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
@Component
public class Common {

    public static Map<String, Channel> channelMaps = new ConcurrentHashMap();

    public Common() {
    }
}
