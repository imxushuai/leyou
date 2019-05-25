package com.leyou.user.service;

import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.util.LeyouConstants;
import com.leyou.common.util.NumberUtils;
import com.leyou.pojo.User;
import com.leyou.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.leyou.util.UserConstants.USER_DATA_PHONE;
import static com.leyou.util.UserConstants.USER_DATA_USERNAME;

@Slf4j
@Service
public class UserService {

    private static final String REDIS_PREFIX_SMS = "sms:verify:code:phone:";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 校验用户数据
     *
     * @param data 数据
     * @param type 数据类型。1：用户名 2：手机
     * @return 返回true表示该信息已被使用，否则返回false
     */
    public Boolean checkUserData(String data, String type) {
        User param = new User();
        switch (type) {
            case USER_DATA_USERNAME:
                param.setUsername(data);
                break;
            case USER_DATA_PHONE:
                param.setPhone(data);
                break;
            default:
                throw new LyException(LyExceptionEnum.NOT_SUPPORT_DATA_TYPE);
        }

        return userMapper.select(param).size() == 0;
    }

    /**
     * 用户注册
     *
     * @param user 用户数据
     * @param code 短信验证码
     * @return 是否注册成功
     */
    public void register(User user, String code) {
        user.setCreated(new Date());
        // 判断验证码
        String key = REDIS_PREFIX_SMS + user.getPhone();
        String veriyCode = redisTemplate.opsForValue().get(key);
        if (!code.equals(veriyCode)) {
            throw new LyException(LyExceptionEnum.VERIFY_CODE_NOT_EQUALS);
        }
        // 生成盐，我这里直接使用uuid作为盐
        String salt = UUID.randomUUID().toString().replace("-", "");
        user.setSalt(salt);
        // 生成密码
        String password = DigestUtils.md5Hex(user.getPassword()) + salt;
        user.setPassword(password);

        // 保存用户
        if (userMapper.insert(user) != 1) {
            // 保存失败
            throw new LyException(LyExceptionEnum.REGISTER_FAILURE);
        }
        // 注册成功，清除短信验证码
        redisTemplate.delete(key);
    }

    /**
     * 发送短信验证码
     *
     * @param phone 手机号码
     */
    public void sendVerifyCode(String phone) {
        // 生成验证码
        String verifyCode = NumberUtils.generateCode(6);
        try {
            // 发送短信验证码
            Map<String, String> msg = new HashMap<>();
            msg.put("phoneNumber", phone);
            msg.put("code", verifyCode);
            amqpTemplate.convertAndSend(LeyouConstants.EXCHANGE_SMS, LeyouConstants.ROUTING_KEY_VERIFY_CODE_SMS, msg);

            // 保存验证码到redis
            redisTemplate.opsForValue().set(REDIS_PREFIX_SMS + phone, verifyCode, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("发送验证码失败, phone = [{}] verifyCode = [{}]", phone, verifyCode, e);
            throw new LyException(LyExceptionEnum.SEND_VERIFY_CODE_FAILURE);
        }
    }

    /**
     * 按用户名和密码查询用户
     *
     * @param username 用户名
     * @param password 密码
     * @return User 用户信息
     */
    public User queryUserByUsernameAndPassword(String username, String password) {
        // 使用username查询用户
        User param = new User();
        param.setUsername(username);
        User user = userMapper.selectOne(param);

        // 用户名不存在
        if (user == null) {
            throw new LyException(LyExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
        }

        // 密码错误
        if (!(DigestUtils.md5Hex(password) + user.getSalt()).equals(user.getPassword())) {
            throw new LyException(LyExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
        }

        return user;
    }
}