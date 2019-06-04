/**
 * Copyright © 2019-Now imxushuai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LyExceptionEnum {

    PARAM_CANNOT_BE_NULL(400, "参数不能为空"),
    PARAM_INVALID(400, "传递的参数无效"),
    FILE_TYPE_ERROR(400, "不支持的文件格式"),
    CATEGORY_LIST_NOT_FOUND(404, "未查询到分类列表"),
    BRAND_LIST_NOT_FOUND(404, "未查询到品牌列表"),
    SPU_LIST_NOT_FOUND(404, "未查询到商品列表"),
    SPU_NOT_FOUND(404, "未查询到商品列表"),
    BRAND_NOT_FOUND(404, "该品牌不存在"),
    GOODS_NOT_FOUND(404, "该商品不存在"),
    ORDER_NOT_FOUND(404, "查询订单不存在"),
    SKU_LIST_NOT_FOUND(404, "未查询到SKU列表"),
    SKU_NOT_FOUND(404, "未查询到SKU信息"),
    CATEGORY_NOT_FOUND(404, "该分类不存在"),
    PARENT_CATEGORY_NOT_FOUND(404, "该父分类不存在"),
    GOODS_STOCK_NOT_FOUND(404, "未查询到该商品的库存信息"),
    GOODS_DETAIL_NOT_FOUND(404, "未查询到商品详情"),
    CURRENT_USER_CART_NOT_EXIST(404, "当前用户购物车空空如也"),
    RECEIVER_ADDRESS_NOT_FOUND(404, "未查询到收件人地址"),
    SAVE_FAILURE(500, "保存失败"),
    WX_PAY_SIGN_INVALID(500, "微信支付签名无效"),
    WX_PAY_NOTIFY_PARAM_ERROR(500, "微信支付回调错误"),
    DELETE_FAILURE(500, "删除失败"),
    ORDER_STATUS_EXCEPTION(500, "订单状态异常"),
    CREATE_PAY_URL_ERROR(500, "创建支付链接错误"),
    CATEGORY_DELETE_INVALID(500, "该分类下含有子分类，请先删除其子分类"),
    READ_FILE_FAILURE(500, "读取文件内容失败"),
    NOT_SUPPORT_DATA_TYPE(400, "不支持校验该类型的数据"),
    VERIFY_CODE_NOT_EQUALS(400, "验证码错误"),
    REGISTER_FAILURE(500, "注册失败，服务器内部错误"),
    SEND_VERIFY_CODE_FAILURE(500, "验证码发送失败"),
    USERNAME_EXISTED(400, "注册失败, 用户名已存在"),
    PHONE_EXISTED(400, "注册失败, 手机号已被注册"),
    INVALID_USERNAME_OR_PASSWORD(400, "用户名或密码错误"),
    UNAUTHORIZED(401, "未授权"),
    ;
    private int code;
    private String message;
}
