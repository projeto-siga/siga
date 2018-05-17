// The Vue build version to load with the `import` command (runtime-only or
// standalone) has been set in webpack.base.conf with an alias.
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
// import '../static/fps-line-icons/css/fpslineicons.css'
// import 'typicons.font/src/font/typicons.css'
import 'font-awesome/css/font-awesome.min.css'

import 'babel-polyfill'
// import 'jquery'
// import 'tether'
import 'bootstrap'

import Vue from 'vue'
import VueResource from 'vue-resource'
import VueClip from 'vue-clip'
import VeeValidate, { Validator } from 'vee-validate'
import ptBR from 'vee-validate/dist/locale/pt_BR'
import BootstrapVue from 'bootstrap-vue'
import App from './App'
import router from './router'
import { Bus } from './bl/bus.js'
import ValidacaoBL from './bl/validacao.js'
import vSelect from 'vue-select'

VeeValidate.Validator.extend('cpf', {
  getMessage: field => 'CPF ' + field + ' inválido.',
  validate: ValidacaoBL.validarCPF
})

VeeValidate.Validator.extend('cnpj', {
  getMessage: field => 'CNPJ ' + field + ' inválido.',
  validate: ValidacaoBL.validarCNPJ
})

VeeValidate.Validator.extend('oab', {
  getMessage: field => 'OAB ' + field + ' inválido.',
  validate: ValidacaoBL.validarOAB
})

ptBR.messages.cpf = field => 'CPF ' + field + ' inválido.'
ptBR.messages.cnpj = field => 'CNPJ ' + field + ' inválido.'
ptBR.messages.oab = field => 'OAB ' + field + ' inválido.'
Validator.localize('pt_BR', ptBR)

Vue.use(VueResource)
Vue.use(VueClip)
Vue.use(VeeValidate, { locale: 'pt_BR' })
Vue.use(BootstrapVue)

Vue.component('v-select', vSelect)

Vue.config.productionTip = false

console.log(process.env.API_URL)
Vue.http.options.root = process.env.API_URL

/* eslint-disable no-new */
new Vue({
  el: '#app',
  mounted() {
    Vue.http.interceptors.push(function(request, next) {
      if (request.block) Bus.$emit('block', request.blockmin, request.blockmax)

      // continue to next interceptor
      next(function(response) {
        if (request.block) Bus.$emit('release')
        if (response.status === 401) location.reload()
      })
    })
  },
  router,
  template: '<App/>',
  components: {
    App
  }
})
