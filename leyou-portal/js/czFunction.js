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
;/*!components/js-modules/function/czConfig.js*/
/**
 * Created by hans on 2016/11/1.
 * 配置项目
 */
var czConfig = {
    basePath : "",// 网站根
    apiServer : ""// api服务器
};
;/*!components/js-modules/function/czHttp.js*/
var czHttp = {

    // 获取url传递参数
    getQueryString : function (paramName) {
        var searchString = window.location.search.substring(1),
            i, val, params = searchString.split("&");

        for (i=0;i<params.length;i++) {
            val = params[i].split("=");
            if (val[0] == paramName) {
                return val[1];
            }
        }
        return null;
    },

    // 获取 json 对象
    getJSON : function (url, data, callback) {
        $.getJSON(url, data, function(json){
            callback(json);
        });
    },

    // 获取 http get 对象
    get : function (url, data, callback) {
        $.get(url, data, function(json){
            callback(json);
        });
    },
    get : function (url, callback) {
        $.get(url, function(json){
            callback(json);
        });
    },

    // 获取 http post 对象
    post : function (url, data, callback) {
        $.post(url, data, function(json){
            callback(json);
        });
    },
    post : function (url, callback) {
        $.post(url, function(json){
            callback(json);
        });
    }

};
;/*!components/js-modules/function/czString.js*/
// 字符串格式化
String.prototype.format = function() {
    var args = arguments;
    return this.replace(/\{(\d+)\}/g,
        function(m,i){
            return args[i];
        });
};
String.format = function() {
    if( arguments.length == 0 )
        return null;

    var str = arguments[0];
    for(var i=1;i<arguments.length;i++) {
        var re = new RegExp('\\{' + (i-1) + '\\}','gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
};

// 判断是否为数字
String.isNumber = function() {
    if( arguments.length == 0 )
        return false;

    var str = arguments[0];
    if (str!=null && str!="")
    {
        return !isNaN(str);
    }
    return false;
};

// 字符串转json
String.toJson = function() {
    if( arguments.length == 0 )
        return null;

    var str = arguments[0];
    return (new Function("return " + str))();
};