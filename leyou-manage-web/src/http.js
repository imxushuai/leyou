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
