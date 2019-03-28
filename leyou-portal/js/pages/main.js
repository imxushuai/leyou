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
$(function () {

  'use strict';

  var $distpicker = $('#distpicker');

  $distpicker.distpicker({
    province: '福建省',
    city: '厦门市',
    district: '思明区'
  });

  $('#reset').click(function () {
    $distpicker.distpicker('reset');
  });

  $('#reset-deep').click(function () {
    $distpicker.distpicker('reset', true);
  });

  $('#destroy').click(function () {
    $distpicker.distpicker('destroy');
  });

  $('#distpicker1').distpicker();

  $('#distpicker2').distpicker({
    province: '---- 所在省 ----',
    city: '---- 所在市 ----',
    district: '---- 所在区 ----'
  });

  $('#distpicker3').distpicker({
    province: '浙江省',
    city: '杭州市',
    district: '西湖区'
  });

  $('#distpicker4').distpicker({
    placeholder: false
  });

  $('#distpicker5').distpicker({
    autoSelect: false
  });

});
