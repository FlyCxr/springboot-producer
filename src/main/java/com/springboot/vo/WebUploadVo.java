package com.springboot.vo;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class WebUploadVo {

    private String type;

    private String fileName;

    private String fileMd5;

    private String chunk;

    private String fileSize;

    private String formData;

    private String name;

    private String chunks;

    private String chunkSize;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getChunk() {
        return chunk;
    }

    public void setChunk(String chunk) {
        this.chunk = chunk;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChunks() {
        return chunks;
    }

    public void setChunks(String chunks) {
        this.chunks = chunks;
    }

    public String getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(String chunkSize) {
        this.chunkSize = chunkSize;
    }

    public static Map<String, Object> success(String msg) {
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("rtn", 0);
        value.put("msg", msg);
        return value;
    }

    public static Map<String, Object> success(String msg, Object obj) {
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("rtn", 0);
        value.put("msg", msg);
        value.put("obj", obj);
        return value;
    }

    public static Map<String, Object> fail(String msg) {
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("rtn", 1);
        value.put("msg", msg);
        return value;
    }

    public static Map<String, Object> fail(String msg, Object obj) {
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("rtn", 1);
        value.put("msg", msg);
        value.put("obj", obj);
        return value;
    }
}
