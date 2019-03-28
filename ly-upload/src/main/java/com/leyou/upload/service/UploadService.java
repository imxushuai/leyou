/**
 * Copyright © 2019-2019 imxushuai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.LyUploadConfigProperty;
import com.leyou.upload.constans.FileTypeConstans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
@Slf4j
public class UploadService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private LyUploadConfigProperty lyUploadConfigProperty;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件url
     */
    public String uploadFile(MultipartFile file, String fileType) {
        String url = null;
        switch (fileType) {
            case FileTypeConstans.IMAGE:
                url = uploadImage(file);
                break;
            default:
                throw new LyException(LyExceptionEnum.FILE_TYPE_ERROR);
        }
        return url;
    }

    /**
     * 图片上传
     *
     * @param file 图片
     * @return 图片url
     */
    private String uploadImage(MultipartFile file) {
        try {
            // 校验图片内容，防止修改后缀名恶意上传
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null || image.getWidth() == 0 || image.getHeight() == 0) {
               throw new LyException(LyExceptionEnum.FILE_TYPE_ERROR);
            }
            // 获取文件后缀名
            String filename = file.getOriginalFilename();
            String suffix = filename.substring(filename.indexOf(".") + 1);

            // 上传文件
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), suffix, null);
            // 返回文件路径
            return lyUploadConfigProperty.getImageServer() + storePath.getFullPath();
        } catch (IOException e) {
            log.error("读取文件内容发生IO异常.  e = {}", e);
            throw new LyException(LyExceptionEnum.READ_FILE_FAILURE);
        }
    }
}
