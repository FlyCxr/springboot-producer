package com.springboot.netty.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.exception.BusinessException;
import java.nio.ByteBuffer;

@SuppressWarnings("all")
public abstract class CommandPayload<T extends CommonCodeer> implements CommonCodeer {

    protected String type;

    protected T body;

    protected String vin;

    protected String sn;

    @JsonIgnore
    protected byte[] payloadBytesCache;

    public CommandPayload() {
    }

    public abstract String toReadableHexString();

    public void decodeBody(ByteBuffer byteBuffer, Class<T> bodyClass) {
        try {
            this.body = bodyClass.newInstance();
            this.body.decode(byteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cache(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        byteBuffer.flip();
        this.payloadBytesCache = bytes;
    }

    public void cache(byte[] bytes) {
        this.payloadBytesCache = bytes;
    }

    public T getBody() {
        return this.body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String toHexString() {
        return ByteUtil.format(this.encode());
    }

    public String getVin() {
        return this.vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public byte[] getPayloadBytesCache() {
        return payloadBytesCache;
    }

    public void setPayloadBytesCache(byte[] payloadBytesCache) {
        this.payloadBytesCache = payloadBytesCache;
    }

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Parse json error!", e);
        }
    }
}
