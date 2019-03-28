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
    // 鼠标经过增加边框
            $(".seckill-item").hover(function () {
                $(this).css("border-color","#b1191a");
            },function(){
                $(this).css("border-color","transparent");
            })
//    $.ajax ({
//        type:"GET",
//        url:"../data/list-data.json",
//        dataType:"json",
//        cache:false,
//        success:function(data){
//            $.each(data, function (index, list) {
//             $("#seckill").append(
//                 "<li class='seckill-item'><div class='pic' ><img src='" + list["img"] + "' alt=''></div><div class='intro'><span>" + list["desc"] + "</span></div>"
//                 + "<div class='price'><b class='sec-price'>￥" + list["n-price"] + "</b><b class='ever-price'>￥" + list["o-price"] + "</b></div>"
//                 + "<div class='num'><div>已售" + list["saled"] + "</div><div class='progress'>"
//                 + "<div class='sui-progress progress-danger'><span style='width: 70%;' class='bar'></span></div>"
//                 + "</div>"
//                 + "<div>剩余<b class='owned'>" + list["leaved"] + "</b>件</div>"
//                 + "</div>"
//                 + "<a class='sui-btn btn-block btn-buy' href='seckill-item.html' target='_blank'>立即抢购</a>"
//                 + "</li >"
//             );
            
//         })
//        }
//    })

})
//go to top
$(document).ready(function ($) {
    var offset = 300,
        offset_opacity = 1200,
        scroll_top_duration = 700,
        $back_to_top = $('.cd-top');

    $(window).scroll(function () {
        ($(this).scrollTop() > offset) ? $back_to_top.addClass('cd-is-visible') : $back_to_top.removeClass('cd-is-visible cd-fade-out');
        if ($(this).scrollTop() > offset_opacity) {
            $back_to_top.addClass('cd-fade-out');
        }
    });
    $back_to_top.on('click', function (event) {
        event.preventDefault();
        $('body,html').animate({
            scrollTop: 0,
        }, scroll_top_duration
        );
    });

});