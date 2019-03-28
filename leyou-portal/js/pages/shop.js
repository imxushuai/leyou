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
    // $.getJSON("../data/shoplist.json",function (data) {
    //     $.each(data,function (index,type) {
    //         $("#listall").append(
    //             "<li>"+ type["name"] + "</li>"
    //         );
    //     })
    // });
    // $.getJSON("../data/shoplist-data.json", function (data) {
    //     $.each(data, function (index, list) {
    //         $("#goods-list").append(
    //             "<li class='yui3-u-1-4'><div class='list-wrap' ><div class='p-img'><img src='" + list["img"] + "' alt=''></div><div class='price'><strong><em>¥</em> <i>" + list["n-price"] + "</i></strong></div>"
    //             + "<div class='attr'><em>" + list["desc"] + "</em></div><div class='cu'><em><span>促</span>" + list["cu"] + "</em></div>"
    //             + "<div class='operate'><a href='success-cart.html' target='blank' class='sui-btn btn-bordered btn-danger'>加入购物车</a>"
    //             + "<a href='javascript:void(0);' class='sui-btn btn-bordered'>对比</a>"
    //             + "<a href='javascript:void(0);' class='sui-btn btn-bordered'>关注</a>"
    //             + "</div></div></li >"
    //         );

    //     })
    // });
    var lileg = $(".sui-nav").children().length;
    if (lileg < 8) {
        $("#li-1").css({"display":"none"});
    }
})

$(document).ready(function () {
    //    nav-li hover e
    var num;
    $('.sui-nav>li[id]').hover(function () {
        /*图标向上旋转*/
        $(this).children().removeClass().addClass('hover-up');
        /*下拉框出现*/
        var Obj = $(this).attr('id');
        num = Obj.substring(3, Obj.length);
        $('#box-' + num).slideDown(300);
    }, function () {
        /*图标向下旋转*/
        $(this).children().removeClass().addClass('hover-down');
        /*下拉框消失*/
        $('#box-' + num).hide();
    });
    // hidden-box hover e
    $('.hidden-box').hover(function () {
        /*保持图标向上*/
        $('#li-' + num).children().removeClass().addClass('hover-up');
        $(this).show();
    }, function () {
        $(this).slideUp(200);
        $('#li-' + num).children().removeClass().addClass('hover-down');
    });
});

$(function () {
    var navH = $("#headnav-fixed").offset().top; //获取到顶部的距离
    // 滚动条事件
    $(window).scroll(function () {
        var scroH = $(this).scrollTop(); //获取滚动条滑动距离
        if (scroH >= navH) {
            $("#headnav-fixed").css({ "position": "fixed", "top": 0,"width":"inherit" ,"border-bottom":"1px solid #B1191A"});
        } else if (scroH < navH) {
            $("#headnav-fixed").css({ "position": "static","border-bottom":0});
        }
    })
})



