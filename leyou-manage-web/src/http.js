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
import Vue from 'vue'
import axios from 'axios'
import config from './config'

axios.defaults.baseURL = config.api;
axios.defaults.timeout = 2000;

axios.interceptors.request.use(function (config) {
  return config;
});

axios.interceptors.response.use(
  response => {

  const res = response.status
  if (res !== 200) {
  return Promise.reject('error')
} else {
  return response
}
},
error => {
  console.log('err' + error.response) // for debug
  console.log('err' + error.message)
  return Promise.reject(error.response)
}
)

Vue.prototype.$http = axios;
