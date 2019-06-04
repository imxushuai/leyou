package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Value("${ly.jwt.cookieName}")
    private String cookieName;

    @Autowired
    private JwtProperties properties;

    @Autowired
    private AuthService authService;

    /**
     * 登陆并保存用户凭证到cookie
     *
     * @param username 用户名
     * @param password 密码
     * @param request  request
     * @param response response
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletRequest request, HttpServletResponse response) {
        // 获取token
        String token = authService.login(username, password);
        // 将token写入cookie
        CookieUtils.setCookie(request, response, cookieName, token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 校验当前客户端是否为登录状态并返回登录信息
     *
     * @param token 用户凭证
     * @return UserInfo 用户信息
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> login(@CookieValue("LY_TOKEN") String token,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        if (StringUtils.isBlank(token)) {
            throw new LyException(LyExceptionEnum.UNAUTHORIZED);
        }
        try {
            // 解析token
            UserInfo userInfo = JwtUtils.getUserInfo(properties.getPublicKey(), token);

            // 刷新token
            token = JwtUtils.generateToken(userInfo, this.properties.getPrivateKey(), this.properties.getExpire());
            // 将token写入cookie
            CookieUtils.setCookie(request, response, cookieName, token);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            throw new LyException(LyExceptionEnum.UNAUTHORIZED);
        }
    }
}
