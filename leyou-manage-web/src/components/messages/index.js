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
import {Message, MessageBox} from 'element-ui';

const m = {
  info(msg) {
    Message({
      showClose: true,
      message: msg,
      type: 'info'
    });
  },
  error(msg) {
    Message({
      showClose: true,
      message: msg,
      type: 'error'
    });
  },
  success(msg) {
    Message({
      showClose: true,
      message: msg,
      type: 'success'
    });
  },
  warning(msg) {
    Message({
      showClose: true,
      message: msg,
      type: 'warning'
    });
  },
  confirm(msg) {
    return new Promise((resolve, reject) => {
      MessageBox.confirm(msg, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        resolve()
      })
        .catch(() => {
          reject()
        });
    })
  },
  prompt(msg) {
    return new Promise((resolve, reject) => {
      MessageBox.prompt(msg, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({value}) => {
        resolve(value)
      }).catch(() => {
        reject()
      });
    })
  }
}

export default m;
