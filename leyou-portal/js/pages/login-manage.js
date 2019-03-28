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
$.fn.drag = function(options) { 
    var x, drag = this, isMove = false, defaults = { 
    }; 
    var options = $.extend(defaults, options); 
    //添加背景，文字，滑块 
    var html = '<div class="drag_bg"></div>' + 
            '<div class="drag_text" onselectstart="return false;" unselectable="on">拖动滑块验证</div>' + 
            '<div class="handler handler_bg"></div>'; 
    this.append(html); 
 
    var handler = drag.find('.handler'); 
    var drag_bg = drag.find('.drag_bg'); 
    var text = drag.find('.drag_text'); 
    var maxWidth = drag.width() - handler.width();  //能滑动的最大间距 
 
    //鼠标按下时候的x轴的位置 
    handler.mousedown(function(e) { 
        isMove = true; 
        x = e.pageX - parseInt(handler.css('left'), 10); 
    }); 
 
    //鼠标指针在上下文移动时，移动距离大于0小于最大间距，滑块x轴位置等于鼠标移动距离 
    $(document).mousemove(function(e) { 
        var _x = e.pageX - x; 
        if (isMove) { 
            if (_x > 0 && _x <= maxWidth) { 
                handler.css({'left': _x}); 
                drag_bg.css({'width': _x}); 
            } else if (_x > maxWidth) {  //鼠标指针移动距离达到最大时清空事件 
                dragOk(); 
            } 
        } 
    }).mouseup(function(e) { 
        isMove = false; 
        var _x = e.pageX - x; 
        if (_x < maxWidth) { //鼠标松开时，如果没有达到最大距离位置，滑块就返回初始位置 
            handler.css({'left': 0}); 
            drag_bg.css({'width': 0}); 
        } 
    }); 
 
    //清空事件 
    function dragOk() { 
        handler.removeClass('handler_bg').addClass('handler_ok_bg'); 
        text.text('验证通过'); 
        drag.css({'color': '#fff'}); 
        handler.unbind('mousedown'); 
        $(document).unbind('mousemove'); 
        $(document).unbind('mouseup'); 
    } 
};