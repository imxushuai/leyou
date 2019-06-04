package com.leyou.cart.controller;

import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.enums.LyExceptionEnum;
import com.leyou.common.exception.LyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 新增商品到购物车
     *
     * @param cart 商品数据
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        // 将userid放入cart
        cart.setUserId(UserInterceptor.getUserInfo().getId());
        cartService.saveCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询当前用户购物车
     *
     * @return List 购物车商品列表
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList() {
        List<Cart> carts = cartService.queryCartList();
        if (carts == null || carts.isEmpty()) {
            throw new LyException(LyExceptionEnum.CURRENT_USER_CART_NOT_EXIST);
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 更新购物车中指定商品数量
     *
     * @param skuId 商品ID
     * @param num 数量
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") Long skuId,
                                          @RequestParam("num") Integer num) {
        cartService.updateNum(skuId, num);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除购物车中的指定商品
     *
     * @param skuId 商品ID
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId) {
        cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }

    /**
     * 新增本地购物车商品到登陆用户购物车中
     *
     * @param carts 商品数据
     */
    @PostMapping("/merge")
    public ResponseEntity<Void> addCart(List<Cart> carts) {
        cartService.mergeCarts(carts);
        return ResponseEntity.ok().build();
    }


}
