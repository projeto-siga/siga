<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Mesa Virtual</h4>
      </div>
      <div class="col col-sm-12" v-if="errormsg">
        <p class="alert alert-danger">
          <strong>Erro!</strong> {{errormsg}}
        </p>
      </div>
    </div>

    <div class="row mb-3 d-print-none">
      <div class="col-sm-auto ml-1">
        <div class="input-group">
          <div class="input-group-addon">
            <span class="fa fa-map-marker"></span>
          </div>
          <select id="mesa" class="form-control" v-model="mesa" @change="selecionarMesa" name="mesa">
            <option disabled selected hidden :value="undefined">[Selecionar]</option>
            <option v-for="i in mesas" :value="i">{{i.nome}}</option>
          </select>
        </div>
      </div>
      <div class="col-sm-auto ml-1">
        <div class="input-group">
          <div class="input-group-addon">
            <span class="fa fa-search"></span>
          </div>
          <input type="text" class="form-control" placeholder="Filtrar" v-model="filtro" ng-model-options="{ debounce: 200 }">
        </div>
      </div>
      <div class="col-sm-auto ml-auto">
        <button type="button" @click="assinarDocumentos()" class="btn btn-primary ml-1" title="">
          <span class="fa fa-certificate"></span> Assinar&nbsp;&nbsp
          <span class="badge badge-pill badge-warning">{{filtradosEMarcadosEAssinaveis.length}}</span>
        </button>
      </div>
    </div>

    <div class="row" v-if="filtrados.length == 0">
      <div class="col col-sm-12">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhuma documento na mesa.
        </p>
      </div>
    </div>

    <div class="row" v-if="filtrados.length > 0">
      <div class="col-sm-12">
        <table class="table table-striped table-sm table-responsive">
          <thead class="thead-inverse">
            <tr>
              <th style="text-align: center">
                <input type="checkbox" id="progress_checkall" name="progress_checkall" v-model="todos" @change="marcarTodos()"></input>
              </th>
              <th>Documento</th>
              <th>Processo</th>
              <th>Motivo</th>
              <th>Origem</th>
              <th>Data/Hora</th>
              <th>Situação</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="f in filtrados">
              <td style="text-align: center">
                <input type="checkbox" v-model="f.checked" :disabled="f.disabled"></input>
              </td>
              <td>
                <span v-if="!f.docid">{{f.documento}}</span>

                <a href="" v-if="f.docid" @click.prevent="mostrarDocumento(f)">{{f.docdescr}}</a>

                <a href="" v-if="f.docid" @click.prevent="mostrarDocumento(f, 'attachment')">
                  <span class="fa fa-download icone-em-linha"></span>
                </a>

                <a href="" v-if="f.docid" @click.prevent="assinarDocumento(f)">
                  <span class="fa fa-certificate icone-em-linha" title="Assinar Digitalmente"></span>
                </a>
              </td>
              <td class="td-middle" v-if="f.rows" :rowspan="f.rows">
                <span class="unbreakable">
                  <router-link :to="{name: 'Processo', params: {numero: f.processo}}" target="_blank">{{f.processoFormatado}}</router-link>
                </span>
              </td>
              <td class="td-middle" v-if="f.rows" :rowspan="f.rows">{{f.motivo}}</td>
              <td class="td-middle" v-if="f.rows" :rowspan="f.rows">{{f.responsavel}}</td>
              <td class="td-middle" v-if="f.rows" :rowspan="f.rows" v-html="f.dataentradaFormatada"></td>
              <td class="td-middle" v-if="f.rows" :rowspan="f.rows">{{f.situacao}}
                <span v-if="f.errormsg" :class="{red: true}">Erro {{f.errormsg}}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import ProcessoBL from '../bl/processo.js'
// import { Bus } from '../bl/bus.js'

export default {
  components: {},

  mounted() {
    this.errormsg = undefined

    setTimeout(() => {
      this.carregarMesas()
    })
  },

  data() {
    return {
      mesa: undefined,
      mesas: [],
      filtro: undefined,
      lista: [],
      todos: true,
      errormsg: undefined
    }
  },

  computed: {
    filtrados: function() {
      var a = this.lista
      a = UtilsBL.filtrarPorSubstring(a, this.filtro)
      var procnum, procline
      for (var i = 0; i < a.length; i++) {
        if (procnum !== a[i].processo) {
          procnum = a[i].processo
          procline = i
          a[i].rows = 1
        } else {
          a[procline].rows++
          a[i].rows = 0
        }
      }
      return a
    },

    filtradosEMarcados: function() {
      return this.filtrados.filter(function(item) {
        return item.checked
      })
    },

    filtradosEMarcadosEAssinaveis: function() {
      return this.filtrados.filter(function(item) {
        return item.docid
      })
    }
  },

  methods: {
    carregarMesas: function() {
      this.$http.get('mesa', { block: true }).then(
        response => {
          var list = response.data.list
          for (var i = 0; i < list.length; i++) {
            var m = list[i]
            this.mesas.push({ id: m.id, nome: m.nome })
          }
          if (this.mesas.length > 0) {
            this.mesa = this.mesas[0]
            this.selecionarMesa()
          }
        },
        error => UtilsBL.errormsg(error, this)
      )
    },

    selecionarMesa: function() {
      console.log('mesa', this.mesa)
      this.$http.get('mesa/' + this.mesa.id, { block: true }).then(
        response => {
          this.lista.length = 0
          var list = response.data.list
          for (var i = 0; i < list.length; i++) {
            this.lista.push(this.fixItem(list[i]))
          }
        },
        error => UtilsBL.errormsg(error, this)
      )
    },

    fixItem: function(item) {
      UtilsBL.applyDefauts(item, {
        rows: 1,
        checked: true,
        disabled: false,
        dataentrada: undefined,
        dataentradaFormatada: undefined,
        doccode: undefined,
        documento: undefined,
        processo: undefined,
        processoFormatado: undefined,
        motivo: undefined,
        situacao: undefined,
        responsavel: undefined,
        errormsg: undefined
      })
      if (item.processo !== undefined) {
        item.processoFormatado = ProcessoBL.formatarProcesso(item.processo)
      }
      if (item.dataentrada !== undefined) {
        item.dataentradaFormatada = UtilsBL.formatJSDDMMYYYYHHMM(
          item.dataentrada
        )
      }
      return item
    },

    marcarTodos: function() {
      var docs = this.filtrados
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i]
        doc.checked = this.todos
      }
    },

    mostrarDocumento: function(item, disposition) {
      var form = document.createElement('form')
      form.action =
        this.$parent.test.properties['balcaovirtual.assijus.endpoint'] +
        '/api/v1/view' +
        (disposition === 'attachment' ? '?disposition=attachment' : '')
      form.method = 'POST'
      form.target = '_blank'
      form.style.display = 'none'

      var cpf = document.createElement('input')
      cpf.type = 'text'
      cpf.name = 'cpf'
      cpf.value = this.$parent.jwt.cpf

      var system = document.createElement('input')
      system.type = 'text'
      system.name = 'system'
      system.value = item.docsystem

      var docid = document.createElement('input')
      docid.type = 'text'
      docid.name = 'id'
      docid.value = item.docid

      var docsecret = document.createElement('input')
      docsecret.type = 'text'
      docsecret.name = 'secret'
      docsecret.value = item.docsecret

      var submit = document.createElement('input')
      submit.type = 'submit'
      submit.id = 'submitView'

      form.appendChild(cpf)
      form.appendChild(system)
      form.appendChild(docid)
      form.appendChild(docsecret)
      form.appendChild(submit)
      document.body.appendChild(form)

      /* global $ */
      $('#submitView').click()

      document.body.removeChild(form)
    },

    criarAssinavel: function(item) {
      return {
        id: item.docid,
        system: item.docsystem,
        code: item.doccode,
        descr: item.docdescr,
        kind: item.dockind,
        origin: 'Balcão Virtual'
      }
    },

    assinarDocumento: function(item) {
      this.chamarAssijus([this.criarAssinavel(item)])
    },

    assinarDocumentos: function() {
      var list = []
      for (var i = 0; i < this.filtradosEMarcadosEAssinaveis.length; i++) {
        list.push(this.criarAssinavel(this.filtradosEMarcadosEAssinaveis[i]))
      }
      if (list.length > 0) this.chamarAssijus(list)
    },

    chamarAssijus: function(list) {
      var json = JSON.stringify({ list: list })
      this.$http
        .post(
          this.$parent.test.properties['balcaovirtual.assijus.endpoint'] +
            '/api/v1/store',
          { payload: json },
          { block: true }
        )
        .then(
          response => {
            var callback = window.location.href + ''
            console.log(callback)
            window.location.href =
              this.$parent.test.properties['balcaovirtual.assijus.endpoint'] +
              '/?endpointautostart=true&endpointlistkey=' +
              response.data.key +
              '&endpointcallback=' +
              encodeURI(callback).replace('#', '__hashsign__')
          },
          error => UtilsBL.errormsg(error, this)
        )
    },

    editar: function() {
      this.$refs.etiqueta.show()
    },

    exibirProcessosMultiplos: function() {
      this.$refs.processosMultiplos.show()
    },

    acrescentarProcessosNaLista: function(arr) {
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

.td-middle {
  vertical-align: middle;
}
</style>
