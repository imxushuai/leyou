package com.leyou.user.controller;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.util.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 校验用户数据是否已存在
     *
     * @param data 数据
     * @param type 数据类型。1：用户名 2：手机
     * @return 返回true表示该信息已被使用，否则返回false
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data") String data,
                                             @PathVariable(value = "type") String type) {
        Boolean result = userService.checkUserData(data, type);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 发送短信验证码
     *
     * @param phone 手机号码
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendVerifyCode(String phone) {
        userService.sendVerifyCode(phone);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 用户注册
     *
     * @param user 用户数据
     * @param code 短信验证码
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user,
                                         BindingResult bindingResult,
                                         @RequestParam("code") String code) {
        // 数据校验
        if (bindingResult.hasErrors()) {// 打印错误信息
            bindingResult.getAllErrors().forEach(error -> {
                throw new LyException(LyExceptionEnum.valueOf(error.getDefaultMessage()));
            });
        }
        // 账号以及手机校验，防止非正规调用
        Boolean username_unique = checkData(user.getUsername(), UserConstants.USER_DATA_USERNAME).getBody();
        Boolean phone_unique = checkData(user.getUsername(), UserConstants.USER_DATA_PHONE).getBody();
        if (username_unique == null || username_unique) {
            throw new LyException(LyExceptionEnum.USERNAME_EXISTED);
        }
        if (phone_unique == null || phone_unique) {
            throw new LyException(LyExceptionEnum.PHONE_EXISTED);
        }
        // 注册
        userService.register(user, code);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 按用户名和密码查询用户
     *
     * @param username 用户名
     * @param password 密码
     * @return User 用户信息
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(@RequestParam("username") String username,
                                                               @RequestParam("password") String password) {
        return ResponseEntity.ok(userService.queryUserByUsernameAndPassword(username, password));
    }


}

