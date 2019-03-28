/*
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
//列表数据加载
$(function () {
    $.getJSON("../data/list-data.json", function (data) {
        $.each(data, function (index, list) {
            $("#goods-list").append(
                "<li class='yui3-u-1-4'><div class='list-wrap' ><div class='p-img'><img src='" + list["img"] + "' alt=''></div><div class='price'><strong><em>¥</em> <i>" + list["n-price"] + "</i></strong></div>"
                + "<div class='attr'><em>" + list["desc"] + "</em></div><div class='cu'><em><span>促</span>"+ list["cu"] +"</em></div>"
                + "<div class='operate'><a href='success-cart.html' target='blank' class='sui-btn btn-bordered btn-danger'>加入购物车</a>"          
                + "<a href='javascript:void(0);' class='sui-btn btn-bordered'>对比</a>"
                + "<a href='javascript:void(0);' class='sui-btn btn-bordered'>降价通知</a>"
                + "</div></div></li >"
            );
           
        })
    })
})

