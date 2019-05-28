package com.leyou.api;

import com.leyou.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {

    /**
     * 按用户名和密码查询用户
     *
     * @param username 用户名
     * @param password 密码
     * @return User 用户信息
     */
    @GetMapping("/query")
    User queryUserByUsernameAndPassword(@RequestParam("username") String username,
                                        @RequestParam("password") String password);

}
