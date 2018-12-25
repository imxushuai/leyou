package com.leyou;

import com.leyou.upload.config.LyUploadConfigProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(LyUploadConfigProperty.class)
public class LyUpload {
    public static void main(String[] args) {
        SpringApplication.run(LyUpload.class, args);
    }
}
