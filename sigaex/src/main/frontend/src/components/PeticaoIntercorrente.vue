<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Petição Intercorrente</h4>
      </div>
    </div>

    <div class="row pb-4" v-if="editando &amp;&amp; !mostrarQuantidadePorData">
      <div class="col-md-12">
        <vue-clip :options="vueclipOptions" :on-added-file="addedFileProxy" :on-complete="completeProxy">
          <template slot="clip-uploader-action">
            <div>
              <div class="dz-message drop-box">
                Arraste suas petições intercorrentes e solte elas nesta área, ou clique aqui para selecioná-las.
                <br>Use arquivos PDF com tamanho máximo de 5MB.
              </div>
            </div>
          </template>

          <template slot="clip-uploader-body" scope="props">
            <div class="col-md-12 mt-3" v-if="hasErrorMessages(props.files)">
              <div class="alert alert-danger mb-0">
                <strong>Arquivos inválidos!</strong> Não foi possível aceitar os seguintes arquivos:
                <ul class="mb-0">
                  <li v-for="ifile in props.files" v-if="ifile.errorMessage">{{ifile.name}} ({{ifile.errorMessage}})</li>
                </ul>
              </div>
            </div>
          </template>

        </vue-clip>
      </div>
    </div>

    <div class="row" v-if="arquivos.length > 0 &amp;&amp; !mostrarQuantidadePorData">
      <div class="col-sm-12" style="padding-top: 1em;">
        <table class="table table-peticao table-responsive">
          <thead class="thead-inverse">
            <tr>
              <th>Processo</th>
              <th>Tipo</th>
              <th>Segredo</th>
              <th>Arquivo</th>
              <th>Status</th>
              <th style="text-align: center" v-if="editando"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="f in arquivos" :class="{odd: f.odd}">
              <td v-show="!f.anexo" :rowspan="f.rowspan">
                <div v-if="editando" class="input-group">
                  <input type="text" class="form-control" style="min-width: 16em;" placeholder="Número do Processo" v-model="f.processo" @input="alterarArquivo(f)" :disabled="f.bloq || f.protocolado">
                  <button type="button" v-show="f.processo === undefined || f.processo === ''" @click="exibirProcessosMultiplos(f)" class="btn btn-sm btn-outline-primary ml-1" title="Inserir este PDF em múltiplos processos">&#x2795;</button>
                </div>
                <span class="unbreakable" v-if="!editando">
                  <router-link :to="{name: 'Processo', params: {numero: f.processo}}" target="_blank">{{f.processo}}</router-link>
                </span>
              </td>

              <td v-show="!f.anexo" :rowspan="f.rowspan">
                <select style="min-width: 8em;" v-if="editando" class="form-control mr-sm-2" v-model="f.tipo" :disabled="f.protocolado" @change="selecionarTipo(f, f.tipo)">
                  <option v-for="tipo in filtrarTipos(f.orgao)" :value="tipo.orgao+'-'+tipo.id">{{tipo.descricao}}</option>
                  <option disabled hidden selected value="">[Selecionar]</option>
                </select>
                <span v-if="!editando">{{f.tipodescr}}</span>
              </td>

              <td v-show="!f.anexo" :rowspan="f.rowspan">
                <select style="min-width: 4em;" v-if="editando" class="form-control mr-sm-2" v-model="f.segredo" :disabled="f.protocolado" @change="selecionarSegredo(f, f.segredo)">
                  <option v-for="segredo in segredos" :value="segredo.codigo">{{segredo.nome}}</option>
                  <option disabled hidden selected value="">[Selecionar]</option>
                </select>
                <span v-if="!editando">{{f.segredo ? 'Sim' : 'Não'}}</span>
              </td>

              <td>
                <a @click="view(doc)">
                  <a :href="$http.options.root + '/arquivo-temporario/' + f.id" target="_blank">{{f.nome}}</span>
                  </a>
                </a>
              </td>

              <td class="status-td" :rowspan="f.protocolado ? f.rowspan : 1" v-show="f.protocolado ? !f.anexo : true">
                <span v-show="f.file.progress &amp;&amp; f.file.progress < 100">{{f.file.progress.toFixed(1)}}%</span>
                <span :class="{green: f.protocolado}" v-show="f.file.progress === 100 &amp;&amp; !f.errormsg">{{f.status}}</span>
                <span v-show="f.errormsg" :class="{red: true}">{{f.errormsg}}</span>
                <span v-show="f.$error">{{f.$error}} {{f.$errorParam}}</span>
              </td>

              <td align="center" v-if="editando">
                <button type="button" @click="removerArquivo(f)" class="btn btn-sm btn-outline-danger">&#x274C;</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="row" v-if="!mostrarQuantidadePorData">
      <div class="col-sm-12">
        <button type="button" @click="carregarProtocoladosRecentemente()" id="protocoladosRecentemente" class="btn btn-secondary d-print-none mr-3 mt-3">Consultar Protocolos
        </button>
        <button type="button" @click="limpar()" id="limpar" v-if="!editando" class="btn btn-secondary d-print-none mt-3">Enviar Outras Petições</button>
        <button type="button" @click="imprimirArquivosComErro()" v-if="arquivosComErro.length > 0" class="btn btn-info float-right ml-3 d-print-none mt-3">
          Imprimir Arquivos com Erro&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{arquivosComErro.length}}</span>
        </button>
        <button type="button" @click="imprimir()" id="imprimir" v-if="!editando" class="btn btn-info float-right ml-3 d-print-none mt-3">Imprimir</button>
        <button type="button" @click="peticionar()" id="prosseguir" v-if="arquivos.length > 0 && (editando || arquivosAProtocolar)" :disabled="!isAllValid()" class="btn btn-primary float-right d-print-none mt-3">
          Protocolar&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{arquivosAProtocolar}}</span>
        </button>
      </div>
    </div>

    <!-- QUANTIDADE POR DATA -->
    <div class="row" v-if="mostrarQuantidadePorData &amp;&amp; dataDeProtocolo === undefined">
      <div class="col col-12" v-if="quantidadePorData.length > 0">
        <div class="protocolos-header">Petição(ões) Intercorrente(s) Protocolada(s) Recentemente</div>
        <table class="table table-striped mb-0 table-responsive">
          <thead class="thead-inverse">
            <tr>
              <th>Data</th>
              <th style="text-align: right;">Quantidade</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in quantidadePorData">
              <td>
                <span v-html="p.dataFormatada"></span>
              </td>
              <td style="text-align: right;">
                <a href="" @click.prevent="carregarResumo(p.data)">{{p.quantidade}}</a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="col col-sm-12" v-if="quantidadePorData.length == 0">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhuma petição intercorrente protocolada nos últimos 7 dias.
        </p>
      </div>
      <div class="col-sm-12" style="padding-top: 1em;">
        <button type="button" @click="voltarParaEdicao()" v-if="quantidadePorData !== undefined" class="btn btn-success d-print-none">Voltar</button>
      </div>
    </div>

    <!-- RELATÓRIO DIÁRIO -->
    <div class="row" v-if="dataDeProtocolo">
      <div class="col col-sm-12">
        <div class="protocolos-header">
          Petições Intercorrentes Protocoladas em
          <span v-html="dataDeProtocolo"></span>
        </div>
        <div class="d-print-none float-right pb-2">
          Filtrar:
          <input type="text" v-model="filtroProtocolo"></input>
        </div>
        <table class="table table-striped mb-0 table-protocolo table-responsive">
          <thead class="thead-inverse">
            <tr>
              <th>Processo</th>
              <th>Classe</th>
              <th>Data/Hora</th>
              <th>Protocolo</th>
              <th>Órgão</th>
              <th>Unidade</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="r in resumoPorDataFiltrado">
              <td>
                <router-link :to="{name: 'Processo', params: {numero: r.processo}}" target="_blank">{{r.processo}}</router-link>
              </td>
              <td>{{r.classe}}</td>
              <td>
                <span v-html="r.dataprotocoloFormatada"></span>
              </td>
              <td>{{r.protocolo}}</td>
              <td>{{r.orgao}}</td>
              <td>{{r.unidade}}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="col-sm-12" style="padding-top: 1em;">
        <button type="button" @click="voltarParaQuantidade()" v-if="dataDeProtocolo !== undefined" class="btn btn-success d-print-none">Voltar</button>
        <button type="button" @click="imprimir()" id="imprimir" v-if="resumoPorData.length > 0" class="btn btn-info float-right ml-3 d-print-none">Imprimir</button>
      </div>
    </div>
    <processo-multiplos ref="processosMultiplos" @ok="multiplosProcessos"></processo-multiplos>
  </div>
</template>

<script>
import AuthBL from '../bl/auth.js'
import ProcessoBL from '../bl/processo.js'
import UtilsBL from '../bl/utils.js'
import ProcessoMultiplos from './ProcessoMultiplos'
import { Bus } from '../bl/bus.js'

const reProc = /^(\d{7})-?(\d{2})\.?(\d{4})\.?(4)\.?(02)\.?(\d{4})\/?-?(\d{2})?$/

export default {
  name: 'peticao-intercorrente',
  components: {
    ProcessoMultiplos
  },

  mounted () {
    this.$nextTick(() => {
      this.$http.get('config/peticao-intercorrente/tipos', { block: true }).then(response => {
        for (var i = 0; i < response.data.list.length; i++) this.tipos.push(response.data.list[i])
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    })
  },

  data () {
    return {
      // remover
      files: [],
      invalidFiles: [],

      editando: true,
      tipos: [],
      dataDeProtocolo: undefined,
      resumoPorData: [],
      filtroProtocolo: undefined,
      mostrarQuantidadePorData: undefined,
      quantidadePorData: [],
      arquivos: [],
      arquivosAProtocolar: undefined,
      arquivoCorrente: undefined,
      segredos: [{
        codigo: 0,
        nome: 'Não'
      }, {
        codigo: 1,
        nome: 'Sim'
      }],
      vueclipOptions: {
        url: this.$http.options.root + '/../upload',
        headers: {
          Authorization: AuthBL.getIdToken()
        },
        maxFilesize: {
          limit: 5,
          message: '{{ filesize }} tamanho é maior que {{ maxFilesize }}'
        },
        acceptedFiles: {
          extensions: ['application/pdf'],
          message: 'Só é permitido o envio de PDFs'
        }
      }
    }
  },

  computed: {
    resumoPorDataFiltrado: function () {
      console.log('recalculando filtrados...', this.modified)
      var a = this.resumoPorData
      a = UtilsBL.filtrarPorSubstring(a, this.filtroProtocolo)
      return a
    },

    arquivosComErro: function () {
      var a = []
      for (var i = 0; i < this.arquivos.length; i++) {
        if (this.arquivos[i].errormsg) a.push(this.arquivos[i])
      }
      return a
    }
  },

  methods: {
    addedFileProxy: function (file) {
      var proc = ProcessoBL.formatarProcesso(file.name)

      this.arquivos.push({
        file: file,
        nome: file.name,
        processo: proc,
        bloq: !!proc,
        protocolado: undefined,
        status: undefined,
        validando: undefined,
        valido: undefined,
        orgao: undefined,
        errormsg: undefined,
        tipo: undefined,
        tipodescr: undefined,
        segredo: undefined
      })
    },

    completeProxy: function (file, status, xhr) {
      var json = JSON.parse(xhr.response)
      var arq = this.arquivo(file)
      arq.size = json.size
      arq.id = json.id
      this.validarArquivo(arq)
      this.organizarArquivos()
    },

    hasErrorMessages: function (arr) {
      if (!arr) return false
      for (var i = 0; i < arr.length; i++) {
        if (arr[i].errorMessage) return true
      }
      return false
    },

    filtrarTipos: function (orgao) {
      return this.tipos.filter(function (item) {
        return item.orgao === orgao
      })
    },

    carregarResumo: function (data) {
      this.dataDeProtocolo = UtilsBL.formatDDMMYYYY(data)
      this.$http.get('peticao-intercorrente/listar?data=' + data, { block: true }).then(response => {
        this.resumoPorData = response.data.list
        for (var i = 0; i < this.resumoPorData.length; i++) {
          var r = this.resumoPorData[i]
          r.dataprotocoloFormatada = UtilsBL.formatJSDDMMYYYYHHMM(r.dataprotocolo)
        }
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    },

    voltarParaQuantidade: function () {
      this.dataDeProtocolo = undefined
      this.resumoPorData = undefined
    },

    carregarProtocoladosRecentemente: function () {
      this.$http.get('peticao-intercorrente/contar', { block: true }).then(response => {
        this.mostrarQuantidadePorData = true
        for (var i = 0; i < response.data.list.length; i++) {
          var qd = response.data.list[i]
          qd.dataFormatada = UtilsBL.formatDDMMYYYY(qd.data)
          this.quantidadePorData.push(qd)
        }
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    },

    voltarParaEdicao: function () {
      this.mostrarQuantidadePorData = undefined
      this.quantidadePorData.length = 0
    },

    validarArquivo: function (arq) {
      var a = arq
      if (a.processo) {
        a.status = 'Validando...'
        a.validando = true
        a.valido = false
        this.$http.get('processo/' + ProcessoBL.somenteNumeros(a.processo) + '/validar', { block: true }).then(response => {
          var d = response.data
          a.status = d.unidade + '/' + d.orgao
          a.orgao = d.orgao
          a.validando = false
          a.valido = true
        }, error => {
          a.validando = false
          a.valido = false
          a.errormsg = error.data.errormsg
        })
      }
    },

    enviarPeticao: function (item) {
      Bus.$emit('prgCaption', 'Enviando ' + item.arq.processo)
      this.$http.post('processo/' + ProcessoBL.somenteNumeros(item.arq.processo) + '/peticionar', {
        orgao: item.arq.orgao,
        tipopeticao: item.arq.tipo,
        nivelsigilo: item.arq.segredo,
        pdfs: item.pdfs
      }).then(response => {
        for (var i = item.index; i <= item.indexFinal; i++) {
          this.arquivos[i].status = response.data.status
          this.arquivos[i].protocolado = true
        }
        UtilsBL.logEvento('peticionamento', 'enviar', 'petição intercorrente')
        Bus.$emit('prgNext')
      }, error => {
        for (var i = item.index; i <= item.indexFinal; i++) {
          this.arquivos[i].errormsg = error.data.errormsg
        }
        Bus.$emit('prgNext')
      })
    },

    // Falta criar callback de retorno
    peticionar: function () {
      this.editando = false
      var lote = []

      for (var i = 0; i < this.arquivos.length; i++) {
        var arq = this.arquivos[i]
        if (arq.protocolado) continue
        arq.errormsg = undefined
        arq.protocolado = undefined
        var r = {
          arq: arq,
          index: i,
          indexFinal: i,
          pdfs: arq.id
        }
        lote.push(r)
        while (i < this.arquivos.length - 1) {
          if (!this.arquivos[i + 1].anexo) break
          i++
          r.indexFinal = i
          r.pdfs += ',' + this.arquivos[i].id
        }
      }
      var func = this.enviarPeticao
      Bus.$emit('prgStart', 'Protocolando Petições Intercorrentes', lote.length, function (i) { func(lote[i]) }, this.organizarArquivos)
    },

    exibirAviso: function (aviso) {
      this.aviso = aviso
    },

    marcarTodos: function () {
      var docs = this.filtrados
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i]
        if (!doc.disabled) doc.checked = this.todos
      }
    },

    printPdf: function (url) {
      var iframe = this._printIframe
      if (!this._printIframe) {
        iframe = this._printIframe = document.createElement('iframe')
        document.body.appendChild(iframe)

        iframe.style.display = 'none'
        iframe.onload = function () {
          setTimeout(function () {
            iframe.focus()
            iframe.contentWindow.print()
          }, 1)
        }
      }

      iframe.src = url
    },

    imprimirArquivo: function (arquivo, lote) {
      console.log('vai imprimir', url)
      this.errormsg = undefined
      if (lote) Bus.$emit('prgCaption', 'Imprimindo ' + arquivo.nome)

      var url = this.$http.options.root + '/arquivo-temporario/' + arquivo.id
      console.log(url)

      var iframe = document.createElement('iframe')
      document.body.appendChild(iframe)
      iframe.style.width = '0px'
      iframe.style.height = '0px'
      iframe.onload = function () {
        console.log('carregou')
        setTimeout(function () {
          console.log('imprimindo...')
          iframe.focus()
          iframe.contentWindow.print()
          setTimeout(function () {
            if (lote) Bus.$emit('prgNext')
            setTimeout(function () {
              console.log('vai fechar...')
              iframe.remove()
              console.log('fechou')
            }, 60000)
          }, 2000)
        }, 1)
      }
      iframe.src = url

      // setTimeout(function () {
      //   var w = window.open(url, '_blank')
      //   window.focus()
      //   setTimeout(function () {
      //     console.log('carregou')
      //     setTimeout(function () {
      //       console.log('imprimindo...')
      //       w.print()
      //       setTimeout(function () {
      //         console.log('vai fechar')
      //         w.close()
      //         UtilsBL.logEvento('peticao-inicial', 'imprimir')
      //         if (lote) Bus.$emit('prgNext')
      //       }, 100)
      //     }, 500)
      //   }, 5000)
      // }, 500)
    },

    imprimirArquivosComErro: function () {
      var a = this.arquivosComErro
      Bus.$emit('prgStart', 'Imprimindo Arquivos', a.length, (i) => this.imprimirArquivo(a[i], true))
    },

    limpar: function () {
      this.editando = true
      this.arquivos.length = 0
    },

    imprimir: function () {
      window.print()
    },

    exibirProcessosMultiplos: function (arq) {
      this.$refs.processosMultiplos.show()
      this.arquivoCorrente = arq
    },

    multiplosProcessos: function (arr) {
      var arq = this.arquivoCorrente
      var i
      if (!arr || arr.length === 0) return
      arq.processo = ProcessoBL.formatarProcesso(ProcessoBL.somenteNumeros(arr[0]))
      for (i = 1; i < arr.length; i++) {
        var newArq = {
          file: arq.file,
          nome: arq.nome,
          processo: ProcessoBL.formatarProcesso(ProcessoBL.somenteNumeros(arr[i])),
          bloq: arq.bloq,
          perc: arq.perc,
          size: arq.size,
          id: arq.id,
          status: undefined
        }
        this.arquivos.push(newArq)
      }
      this.organizarArquivos()
      for (i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        if (a.file === arq.file) {
          a.status === undefined
          this.validarArquivo(a)
        }
      }
    },

    alterarArquivo: function (arq) {
      var m = reProc.exec(arq.processo)
      if (!m) {
        arq.status = 'Número de processo inválido'
        this.organizarArquivos()
        return
      }
      arq.processo = ProcessoBL.formatarProcesso(ProcessoBL.somenteNumeros(arq.processo))
      this.validarArquivo(arq)
      this.organizarArquivos()
    },

    removerArquivo: function (arq) {
      for (var i = 0; i < this.arquivos.length; i++) {
        if (arq === this.arquivos[i]) this.arquivos.splice(i, 1)
      }
      this.organizarArquivos()
    },

    selecionarTipo: function (arq, tipo) {
      arq.tipodescr = this.descricaoTipoPorCodigo(tipo)
      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        if (a !== arq && !a.tipo) {
          a.tipo = tipo
          a.tipodescr = arq.tipodescr
        }
      }
    },

    descricaoTipoPorCodigo: function (tipo) {
      for (var i = 0; i < this.tipos.length; i++) {
        if ((this.tipos[i].orgao + '-' + this.tipos[i].id) === tipo) {
          return this.tipos[i].descricao
        }
      }
      return tipo
    },

    selecionarSegredo: function (arq, segredo) {
      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        if (a !== arq && a.segredo === undefined) a.segredo = segredo
      }
    },

    organizarArquivos: function () {
      this.arquivosAProtocolar = 0
      this.arquivos.sort(function (a, b) {
        if (a.processo && !b.processo) return -1
        if (!a.processo && b.processo) return 1
        if (a.processo !== b.processo) return a.processo < b.processo ? -1 : 1
        // if (a.bloq !== b.bloq)
        // return a.bloq ? -1 : 1
        if (a.nome !== b.nome) { return a.nome.replace('.pdf', '') < b.nome.replace('.pdf', '') ? -1 : 1 }
        return 0
      })

      var arq = { odd: false }

      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        a.anexo = a.processo !== undefined && a.processo === arq.processo
        if (a.anexo) {
          arq.rowspan = arq.rowspan + 1
          a.tipo = arq.tipo
          a.segredo = arq.segredo
          a.odd = arq.odd
        } else {
          a.rowspan = 1
          a.odd = !arq.odd
          arq = a
        }
        if (a.protocolado !== true && !a.anexo) this.arquivosAProtocolar++
      }
    },

    arquivo: function (file) {
      for (var j = 0; j < this.arquivos.length; j++) {
        if (this.arquivos[j].file === file) return this.arquivos[j]
      }
    },

    isAllValid: function () {
      for (var j = 0; j < this.arquivos.length; j++) {
        if (this.arquivos[j].file.status !== 'success') return false
        if (!this.arquivos[j].processo) return false
        if (!this.arquivos[j].tipo) return false
        if (this.arquivos[j].segredo === undefined) return false
      }
      return true
    }
  }
}
</script>

<style scoped>
.protocolos-header {
  font-size: 150%;
  padding-bottom: 0.5rem;
}

.unbreakable {
  white-space: nowrap;
  word-break: keep-all;
  hyphens: none;
}

@media print {
  .table-peticao {
    font-size: 10pt;
  }
  .table-protocolo {
    font-size: 8pt;
  }
}
</style>
