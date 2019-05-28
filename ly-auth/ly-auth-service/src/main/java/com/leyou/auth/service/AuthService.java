package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private JwtProperties properties;

    @Autowired
    private UserClient userClient;

    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return String 用户凭证
     */
    public String login(String username, String password) {
        // 登陆
        User user;
        try {
            user = userClient.queryUserByUsernameAndPassword(username, password);
        } catch (Exception e) {
            log.info("[授权中心] 调用UserClient失败, 用户名=[{}]", username, e);
            throw new LyException(LyExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
        }
        // 生成token
        return JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), properties.getPrivateKey(), properties.getExpire());
    }
}
