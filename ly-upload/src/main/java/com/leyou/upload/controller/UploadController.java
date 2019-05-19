/**
 * Copyright © 2019-Now imxushuai
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
package com.leyou.upload.controller;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.LyUploadConfigProperty;
import com.leyou.upload.constants.FileTypeConstants;
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

        return uploadService.uploadFile(file, FileTypeConstants.IMAGE);
    }
}
