package com.leyou.auth.utils;

import com.leyou.auth.entity.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtUtilsTest {

    /**
     * 公钥和私钥保存位置
     */
    private static final String pubKeyPath = "c:\\key\\rsa.pub";
    private static final String priKeyPath = "c:\\key\\rsa.pri";

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * 初始化公钥和私钥
     *
     * @throws Exception
     */
    @Before
    public void testGetRsa() throws Exception {
        publicKey = RsaUtils.getPublicKey(pubKeyPath);
        privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 生成公钥和私钥
     *
     * @throws Exception
     */
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "imxushuai");
    }

    /**
     * 生成token
     *
     * @throws Exception
     */
    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "xushuai"), privateKey, 5);
        System.out.println("token = " + token);
    }

    /**
     * 解析token
     */
    @Test
    public void testParseToken() {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoieHVzaHVhaSIsImV4cCI6MTU1ODk2NTMwOX0.RTOt2PrhteC4DOdaB2TGVBroHI5ZRg9YxyqRradjP6wak5vWvDXQPJOA9WtfqQAj6oIkG0CwKvmFt74d4LW8fETWTDKEBBik3_HF0vDX5vAZk4LTGM91nE0nOepRSBawTs_zwn0O_UKQwcw3ufLgeV3_Q4uoZDfqWsR4IsLrgDw";

        // 使用公钥解析token
        UserInfo user = JwtUtils.getUserInfo(publicKey, token);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }

}