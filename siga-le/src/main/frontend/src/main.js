import Vue from 'vue'
import VueResource from 'vue-resource';
import App from './App.vue'
import './registerServiceWorker'
import router from './router'

import VueClip from 'vue-clip'
import {
  library
} from '@fortawesome/fontawesome-svg-core'
import {
  faShoppingCart,
  faPlus,
  faPlusSquare,
  faMinusSquare,
  faTrashAlt,
  faCheck,
  faArrowLeft,
  faHome,
  faEllipsisH,
  faUser,
  faUserClock,
  faUserCheck,
  faUserTimes,
  faSearchMinus,
  faSearchPlus,
  faUserFriends,
  faEnvelopeOpenText,
  faUserTie,
  faPauseCircle,
  faDollarSign,
  faHandHoldingUsd,
  faServer,
  faCreditCard,
  faCog
} from '@fortawesome/free-solid-svg-icons'
import {
  faWhatsapp
} from '@fortawesome/free-brands-svg-icons'
import {
  FontAwesomeIcon
} from '@fortawesome/vue-fontawesome'

library.add(faShoppingCart)
library.add(faPlus)
library.add(faPlusSquare)
library.add(faMinusSquare)
library.add(faWhatsapp)
library.add(faTrashAlt)
library.add(faCheck)
library.add(faArrowLeft)
library.add(faHome)
library.add(faEllipsisH)
library.add(faUser)
library.add(faUserClock)
library.add(faUserCheck)
library.add(faUserTimes)
library.add(faUserFriends)
library.add(faSearchMinus)
library.add(faSearchPlus)
library.add(faEnvelopeOpenText)
library.add(faUserTie)
library.add(faPauseCircle)
library.add(faDollarSign)
library.add(faHandHoldingUsd)
library.add(faServer)
library.add(faCreditCard)
library.add(faCog)

Vue.component('font-awesome-icon', FontAwesomeIcon)


import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import {
  BAspect,
  BootstrapVue,
  BootstrapVueIcons
} from 'bootstrap-vue'
Vue.use(BootstrapVue)
Vue.use(BootstrapVueIcons)
Vue.component('b-aspect', BAspect)


import VueTheMask from 'vue-the-mask'

import {
  ValidationProvider,
  ValidationObserver,
  extend
} from 'vee-validate';
import {
  required,
} from 'vee-validate/dist/rules';

extend('required', {
  ...required,
  message: "Preenchimento obrigatório"
});

Vue.use(VueTheMask)

Vue.use(VueResource);
Vue.http.options.root = process.env.VUE_APP_API_URL + "/";
console.log(process.env.VUE_APP_API_URL)

import MyInput from './components/MyInput.vue'
import MySelect from './components/MySelect.vue'
import MyPessoa from './components/MyPessoa.vue'
import MyLotacao from './components/MyLotacao.vue'
import MyClassificacao from './components/MyClassificacao.vue'
import MyIFrame from './components/MyIFrame.vue'
Vue.component('MyInput', MyInput);
Vue.component('MySelect', MySelect);
Vue.component('MyPessoa', MyPessoa);
Vue.component('MyLotacao', MyLotacao);
Vue.component('MyClassificacao', MyClassificacao);
Vue.component('MyIframe', MyIFrame);

Vue.component('ValidationProvider', ValidationProvider);
Vue.component('ValidationObserver', ValidationObserver);

import ptBR from 'vee-validate/dist/locale/pt_BR'

import {
  Bus
} from './bl/bus.js'


import Autocomplete from 'v-autocomplete'

// You need a specific loader for CSS files like https://github.com/webpack/css-loader
import 'v-autocomplete/dist/v-autocomplete.css'

Vue.use(Autocomplete)

ptBR.messages.cpf = field => 'CPF ' + field + ' inválido.'
ptBR.messages.cnpj = field => 'CNPJ ' + field + ' inválido.'
ptBR.messages.oab = field => 'OAB ' + field + ' inválido.'

Vue.use(VueResource)
Vue.use(VueClip)

import vSelect from 'vue-select'
Vue.component('v-select', vSelect)

import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
Vue.component('treeselect', Treeselect)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  mounted() {
    Vue.http.interceptors.push(function (request, next) {
      if (request.block) Bus.$emit('block', request.blockmin, request.blockmax)

      // continue to next interceptor
      next(function (response) {
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

var regServiceWorker = function () {
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./service-worker.js', {
      scope: '/siga-le/'
    }).then(() => console.log('Service Worker registered successfully.')).catch(error => console.log('Service Worker registration failed:', error))
  }
}

regServiceWorker();