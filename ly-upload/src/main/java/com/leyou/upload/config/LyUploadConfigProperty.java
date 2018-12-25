package com.leyou.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "ly.upload")
public class LyUploadConfigProperty {
    private List<String> imageTypes = new ArrayList<>();
    private String imageServer;
}
