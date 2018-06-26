package com.springboot.service.impl;

import com.springboot.service.WebUploadService;
import com.springboot.util.StringUtil;
import com.springboot.util.WebUploadFileUtil;
import com.springboot.vo.WebUploadVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("all")
@Service("webUploadService")
public class WebUploadServiceImpl implements WebUploadService {

    @Value("${upload.path}")
    private String UPLOAD_PATH;

    @Value("${upload.delimiter}")
    private String Delimiter;

    @Override
    public Object check(WebUploadVo vo) {
        Long chunk = StringUtil.isBlank(vo.getChunk()) ? 0L : Long.valueOf(vo.getChunk());
        String fileName = vo.getFileName();
        Long fileSize = StringUtil.isBlank(vo.getFileSize()) ? 0L :Long.valueOf(vo.getFileSize());
        String fileMd5 = vo.getFileMd5();
        if (chunk == 0) { // 未分片校验
            String destfilePath = UPLOAD_PATH  + File.separator + fileName;
            File destFile = new File(destfilePath);
            if (destFile.exists() && destFile.length() == fileSize) {
                return WebUploadVo.success("文件已存在，跳过", 1);
            } else {
                return WebUploadVo.success("文件不存在", 0);
            }
        } else {// 分片校验
            String destFileDir = UPLOAD_PATH + File.separator + fileMd5;
            String destFileName = chunk + Delimiter + fileName;
            String destFilePath = destFileDir + File.separator + destFileName;
            File destFile = new File(destFilePath);
            if (destFile.exists() && destFile.length() == fileSize) {
                return WebUploadVo.success("分片已存在，跳过", 1);
            } else {
                return WebUploadVo.success("分片不存在", 0);
            }
        }
    }

    @Override
    public Object chunkUpload(File file,WebUploadVo vo) {
        String fileName = vo.getName();
        Long chunk = StringUtil.isBlank(vo.getChunk()) ? 0 :Long.valueOf(vo.getChunk());
        Long fileSize = StringUtil.isBlank(vo.getFileSize()) ? 0L :Long.valueOf(vo.getFileSize());
        Long chunks = StringUtil.isBlank(vo.getChunks()) ? 0L :Long.valueOf(vo.getChunks());
        Long chunkSize = StringUtil.isBlank(vo.getChunkSize()) ? 0L :Long.valueOf(vo.getChunkSize());
        String fileMd5 = vo.getFileMd5();
        // 分片目录创建
        String chunkDirPath = UPLOAD_PATH  + File.separator + fileMd5;
        File chunkDir = new File(chunkDirPath);
        if (!chunkDir.exists()) {
            chunkDir.mkdirs();
        }
        // 分片文件上传
        String chunkFileName = chunk + Delimiter + fileName;
        String chunkFilePath = chunkDir + File.separator + chunkFileName;
        File chunkFile = new File(chunkFilePath);
        try {
            WebUploadFileUtil.copy(new FileInputStream(file), new FileOutputStream(chunkFile));
        } catch (Exception e) {
            return WebUploadVo.fail("分片上传出错", 1);
        }
        // 合并分片
        long seek = chunkSize * chunk;
        String destFilePath = UPLOAD_PATH + File.separator + fileName;
        File destFile = new File(destFilePath);
        if (chunkFile.length() > 0) {
            try {
                WebUploadFileUtil.randomAccessFile(chunkFile, destFile, seek);
            } catch (IOException e) {
                return WebUploadVo.fail("分片合并失败", 1);
            }
        }
        if (chunk == chunks - 1) {
            // 删除分片文件夹
            WebUploadFileUtil.deleteDirectory(chunkDirPath);

            return WebUploadVo.success("上传成功", 1);
        } else {
            return WebUploadVo.fail("上传中...", 1);
        }
    }

    @Override
    public Object unChunkUpload(File file, WebUploadVo vo) {
        // 文件上传
        File destFile = new File(UPLOAD_PATH  + File.separator + vo.getName());
        if (file != null) {
            // 上传目录
            File fileDir = new File(UPLOAD_PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            if (destFile.exists()) {
                destFile.delete();
            }
            try {
                WebUploadFileUtil.copy(new FileInputStream(file), new FileOutputStream(destFile));
                return WebUploadVo.success("上传成功", 0);
            } catch (Exception e) {
                return WebUploadVo.fail("文件上传出错", 0);
            }
        }
        return WebUploadVo.fail("上传失败", 0);
    }
}
