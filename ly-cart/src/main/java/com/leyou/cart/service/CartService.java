package com.leyou.cart.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.util.JsonUtils;
import com.leyou.pojo.Sku;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartService {


    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "ly:cart:uid:";


    /**
     * 保存购物车
     * @param cart 购物车数据
     */
    public void saveCart(Cart cart) {
        String key = KEY_PREFIX + cart.getUserId();
        // 获取当前用户购物车信息
        BoundHashOperations<String, Object, Object> userCartData = redisTemplate.boundHashOps(key);

        // 拿出cart中的关键数据
        Integer num = cart.getNum();
        Long skuId = cart.getSkuId();

        // 判断是否存在购物车
        if (userCartData.hasKey(skuId.toString())) {
            // 存在该商品项，增加数量
            String json = userCartData.get(cart.getSkuId()).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + num);
        } else {
            // 不存在该商品项，新增该商品到购物车
            Sku sku = this.goodsClient.querySkuById(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }

        // 将购物车信息存入redis
        userCartData.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    /**
     * 查询当前用户购物车
     *
     * @return List 购物车商品列表
     */
    public List<Cart> queryCartList() {
        // 获取用户信息
        UserInfo userInfo = UserInterceptor.getUserInfo();
        String key = KEY_PREFIX + userInfo.getId();
        // 查询该用户购物车
        if (!redisTemplate.hasKey(key)) {
            throw new LyException(LyExceptionEnum.CURRENT_USER_CART_NOT_EXIST);
        }
        BoundHashOperations<String, Object, Object> userCartData = redisTemplate.boundHashOps(key);
        List<Object> values = userCartData.values();
        if (CollectionUtils.isEmpty(values)) {
            throw new LyException(LyExceptionEnum.CURRENT_USER_CART_NOT_EXIST);
        }
        // 序列化并返回
        return values.stream().map(cart -> JsonUtils.parse(cart.toString(), Cart.class)).collect(Collectors.toList());
    }

    /**
     * 更新购物车中指定商品数量
     *
     * @param skuId 商品ID
     * @param num 数量
     */
    public void updateNum(Long skuId, Integer num) {
        // 获取登录用户
        UserInfo user = UserInterceptor.getUserInfo();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        // 获取购物车
        String json = hashOps.get(skuId.toString()).toString();
        Cart cart = JsonUtils.parse(json, Cart.class);
        cart.setNum(num);
        // 写入购物车
        hashOps.put(skuId.toString(), JsonUtils.serialize(cart));
    }

    /**
     * 删除购物车中的指定商品
     *
     * @param skuId 商品ID
     */
    public void deleteCart(String skuId) {
        // 获取登录用户
        UserInfo user = UserInterceptor.getUserInfo();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        // 删除该hashKey
        hashOps.delete(skuId);
    }

    /**
     * 新增本地购物车商品到登陆用户购物车中
     *
     * @param carts 商品数据
     */
    public void mergeCarts(List<Cart> carts) {
        // 获取当前用户信息
        UserInfo user = UserInterceptor.getUserInfo();
        // 遍历购物车并保存
        carts.forEach(cart -> {
            cart.setUserId(user.getId());
            saveCart(cart);
        });
    }
}
