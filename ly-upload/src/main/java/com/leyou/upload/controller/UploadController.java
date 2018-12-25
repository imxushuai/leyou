package com.leyou.upload.controller;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.LyUploadConfigProperty;
import com.leyou.upload.constans.FileTypeConstans;
import com.leyou.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private LyUploadConfigProperty lyUploadConfigProperty;

    @PostMapping("/image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        // 判断图片类型
        if (!lyUploadConfigProperty.getImageTypes().contains(file.getContentType())) {
            throw new LyException(LyExceptionEnum.FILE_TYPE_ERROR);
        }

        return uploadService.uploadFile(file, FileTypeConstans.IMAGE);
    }
}
