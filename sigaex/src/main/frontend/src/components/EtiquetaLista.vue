<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Lista de Etiquetas</h4>
      </div>
    </div>

    <div class="row mb-3 d-print-none">
      <div class="col-sm-auto ml-auto">
        <button type="button" @click="criar()" class="btn btn-primary ml-1" title="">
          Nova Etiqueta
        </button>
      </div>
    </div>

    <div class="row" v-if="filtrados.length == 0">
      <div class="col col-sm-12">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhuma etiqueta na lista.
        </p>
      </div>
    </div>

    <div class="row" v-if="filtrados.length > 0">
      <div class="col-sm-12">
        <table class="table table-striped table-sm table-responsive">
          <thead class="thead-inverse">
            <tr>
              <th>Sigla</th>
              <th>Nome</th>
              <th>Descrição</th>
              <th>Modalidade</th>
              <th>Quantidade de Processos</th>
              <th style="text-align: center"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="f in filtrados">
              <td>{{f.sigla}}</td>
              <td>{{f.nome}}</td>
              <td>{{f.descricao}}</td>
              <td>{{f.idestilo}}</td>
              <td>{{f.quantidade}}
                <span v-if="f.errormsg" :class="{red: true}">Erro {{f.errormsg}}
                </span>
              </td>
              <td align="right">
                <a href="" @click.prevent="acrescentar(f)">
                  <span class="fa fa-plus icone-em-linha"></span>
                </a>
                <a v-if="f.quantidade > 0" href="" @click.prevent="subtrair(f)">
                  <span class="fa fa-minus icone-em-linha"></span>
                </a>
                <a v-if="f.quantidade > 0" href="" @click.prevent="editar(f)">
                  <span class="fa fa-pencil icone-em-linha"></span>
                </a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <processo-multiplos ref="processosMultiplos" :show.sync="exibirProcessoMultiplos" @ok="acrescentarProcessosNaLista"></processo-multiplos>
    <etiqueta ref="etiqueta" @ok="acrescentarProcessosNaLista"></etiqueta>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import ProcessoMultiplos from './ProcessoMultiplos'
import Etiqueta from './Etiqueta'
// import { Bus } from '../bl/bus.js'

export default {
  components: {
    ProcessoMultiplos,
    Etiqueta
  },

  mounted () {
    this.errormsg = undefined

    setTimeout(() => {
      this.$http.get('etiqueta/listar', { block: true }).then(
        response => {
          var list = response.data.list
          for (var i = 0; i < list.length; i++) {
            this.lista.push(this.fixItem(list[1]))
          }
          this.carregarQuantidadesSilenciosamente()
        },
        error => {
          this.lista.push({
            id: 1,
            idestilo: 'Grupo',
            sigla: 'GDFN',
            nome: 'Grande Devedor da Fazenda Nacional',
            descricao: 'Devedor com valor superior a R$150M.',
            quantidade: 1968
          })
          this.lista.push({
            id: 2,
            idestilo: 'Pessoal',
            sigla: 'AEFN',
            nome: 'Acompanhamento Especial pela Fazenda Nacional',
            descricao: 'Processos que requerem acompanhamento frequente.',
            quantidade: 152
          })
          UtilsBL.errormsg(error, this)
        }
      )
    })
  },

  data () {
    return {
      filtro: undefined,
      lista: [],
      exibirProcessoMultiplos: false
    }
  },

  computed: {
    filtrados: function () {
      var a = this.lista
      a = UtilsBL.filtrarPorSubstring(a, this.filtro)
      return a
    }
  },

  methods: {
    fixItem: function (item) {
      UtilsBL.applyDefauts(item, {
        id: undefined,
        idestilo: undefined,
        sigla: undefined,
        nome: undefined,
        descricao: undefined,
        quantidade: undefined,
        errormsg: undefined
      })
      return item
    },

    carregarQuantidadesSilenciosamente: function () {
      UtilsBL.quietBatch(this.lista, (item, cont) => {
        this.$http.get('etiqueta/' + item.id + '/contar').then(
          (response) => {
            item.quantidade = response.data.quantidade
            cont()
          },
          (error) => {
            item.errormsg = error.data.errormsg
            cont()
          })
      })
    },

    editar: function () {
      this.$refs.etiqueta.show()
    },

    exibirProcessosMultiplos: function () {
      this.$refs.processosMultiplos.show()
    },

    acrescentarProcessosNaLista: function (arr) {
      if (!arr || arr.length === 0) return
      this.pasta = 'inbox'
      for (var i = 0; i < arr.length; i++) {
        if (arr[i] === '') continue
        var p = this.fixProcesso({
          numero: arr[i],
          inbox: true
        })
        this.processos.push(p)
      }
      this.validarEmLoteSilenciosamente()
    }
  }
}
</script>

<style scoped>
.destaque {
  color: red;
}
</style>
