import Vue from 'vue'
import Router from 'vue-router'
import Anexo from '@/components/Anexo'
import Documento from '@/components/Documento'
import DocumentoEditar from '@/components/DocumentoEditar'
import DocumentoDossie from '@/components/DocumentoDossie'
import Mesa from '@/components/Mesa'
import Quadro from '@/components/Quadro'
import Login from '@/components/Login'
import Sugestoes from '@/components/Sugestoes'
import Sobre from '@/components/Sobre'
import Lista from '@/components/Lista'

Vue.use(Router)

const router = new Router({
  routes: [{
    path: '/login',
    name: 'Login',
    component: Login
  }, {
    path: '/documento/novo',
    name: 'DocumentoNovo',
    component: DocumentoEditar,
    meta: {
      title: () => {
        return 'Novo Documento'
      }
    }
  }, {
    path: '/documento/:numero/dossie',
    name: 'DocumentoDossie',
    component: DocumentoDossie,
    meta: {
      title: route => {
        return 'Dossiê ' + route.params.numero + '..'
      }
    }
  }, {
    path: '/documento/:numero/editar',
    name: 'DocumentoEditar',
    component: DocumentoEditar,
    meta: {
      title: route => {
        return 'Editando Documento ' + route.params.numero + '..'
      }
    }
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
    path: '/documento/:numero/anexo/:id',
    name: 'Anexo',
    component: Anexo,
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
    path: '/mesa',
    name: 'Mesa',
    component: Mesa
  }, {
    path: '/quadro',
    name: 'Quadro',
    component: Quadro
  }, {
    path: '/lista/:idMarcador/:filtroPessoaLotacao/:filtroExpedienteProcesso',
    name: 'Lista',
    component: Lista
  }, {
    path: '*',
    redirect: '/quadro'
  }]
})
export default router