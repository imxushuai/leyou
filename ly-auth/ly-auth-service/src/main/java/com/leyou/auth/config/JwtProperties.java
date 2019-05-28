package com.leyou.auth.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;


@Data
@Slf4j
@ConfigurationProperties("ly.jwt")
public class JwtProperties {

    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private int expire;

    private PublicKey publicKey; // 公钥
    private PrivateKey privateKey; // 私钥

    @PostConstruct
    public void init() {
        try {
            checkPubAndPri();
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
            privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥私钥失败", e);
            throw new RuntimeException();
        }
    }

    /**
     * 检查公钥与私钥文件是否存在
     */
    private void checkPubAndPri() throws Exception {
        if (!Paths.get(pubKeyPath).toFile().exists() || !Paths.get(priKeyPath).toFile().exists()) {// 公钥或私钥不存在
            // 重新创建公私钥文件
            RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
        }
    }

}
