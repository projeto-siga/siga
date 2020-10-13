import Vue from 'vue'
import Router from 'vue-router'
import Documento from '@/components/Documento'
import Mesa from '@/components/Mesa'
import Login from '@/components/Login'
import Sugestoes from '@/components/Sugestoes'
import Sobre from '@/components/Sobre'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    }, {
      path: '/documento/:numero',
      name: 'Documento',
      component: Documento,
      meta: {
        title: route => {
          return 'Documento ' + route.params.numero + '..'
        }
      }
    }, {
      path: '/sugestoes',
      name: 'Sugestões',
      component: Sugestoes
    }, {
      path: '/sobre',
      name: 'Sobre',
      component: Sobre
    }, {
      path: '/',
      name: 'Mesa',
      component: Mesa
    }, {
      path: '*',
      redirect: '/'
    }
  ]
})
export default router
