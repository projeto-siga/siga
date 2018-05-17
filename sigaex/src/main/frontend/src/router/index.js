import Vue from 'vue'
import Router from 'vue-router'
import ConsultaSimples from '@/components/ConsultaSimples'
import Processo from '@/components/Processo'
import ProcessoLista from '@/components/ProcessoLista'
import PeticaoInicial from '@/components/PeticaoInicial'
import PeticaoIntercorrente from '@/components/PeticaoIntercorrente'
import AvisoLista from '@/components/AvisoLista'
import AvisoConfirmadoRecentes from '@/components/AvisoConfirmadoRecentes'
import AvisoConfirmadoLista from '@/components/AvisoConfirmadoLista'
import EtiquetaLista from '@/components/EtiquetaLista'
import Mesa from '@/components/Mesa'
import Login from '@/components/Login'
import Sugestoes from '@/components/Sugestoes'
import Sobre from '@/components/Sobre'
import ProcessoBL from '../bl/processo.js'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/',
      name: 'Consulta Simples',
      component: ConsultaSimples
    },
    {
      path: '/documento/:numero',
      name: 'Documento',
      component: Processo,
      meta: {
        title: route => {
          return (
            'Documento ' +
            ProcessoBL.formatarProcesso(
              ProcessoBL.somenteNumeros(route.params.numero)
            ) +
            '..'
          )
        }
      }
    },
    {
      path: '/processo-lista',
      name: 'Lista de Processos',
      component: ProcessoLista
    },
    {
      path: '/etiqueta-lista',
      name: 'Lista de Etiquetas',
      component: EtiquetaLista
    },
    {
      path: '/peticao-inicial',
      name: 'Petição Inicial',
      component: PeticaoInicial
    },
    {
      path: '/peticao-intercorrente',
      name: 'Petição Intercorrente',
      component: PeticaoIntercorrente
    },
    {
      path: '/aviso-lista',
      name: 'Lista de Avisos',
      component: AvisoLista
    },
    {
      path: '/mesa',
      name: 'Mesa',
      component: Mesa
    },
    {
      path: '/aviso-confirmado-recentes',
      name: 'Avisos Confirmados Recentemente',
      component: AvisoConfirmadoRecentes
    },
    {
      path:
        '/aviso-confirmado-lista/:dataInicial/:dataFinal/:porConfirmacao/:porOmissao/:doGrupo',
      name: 'Lista de Avisos Confirmados',
      component: AvisoConfirmadoLista
    },
    {
      path: '/sugestoes',
      name: 'Sugestões',
      component: Sugestoes
    },
    {
      path: '/sobre',
      name: 'Sobre',
      component: Sobre
    }
  ]
})
