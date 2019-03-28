/*
 * Copyright © 2019-2019 imxushuai
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
/**
 * Created by hans on 2016/10/31.
 * 购物车数据
 */
var cartModel = {

    // 加入购物车商品
    add : function (data, success) {
        czHttp.getJSON('../data/success.json', data, function (responseData) {
            if(responseData.isok){
                success(responseData);
            }
        });
    },

    // 删除购物车商品
    remove : function (data, success) {
        czHttp.getJSON('../data/success.json', data, function (responseData) {
            if(responseData.isok){
                success(responseData);
            }
        });
    },

    // 修改商品数量
    changeNumber : function (data, success) {
        czHttp.getJSON('../data/success.json', data, function (responseData) {
            if(responseData.isok){
                success(responseData);
            }
        });
    },

    // 购物车统计
    subtotal : function (success) {
        czHttp.getJSON('../data/orders.json', data, function (responseData) {
            if(responseData.isok){
                success(responseData);
            }
        });
    },

    // 购物车列表
    list : function (success) {

        czHttp.getJSON('../data/orders.json', {}, function(responseData){
            success(responseData);
        });
    }
};