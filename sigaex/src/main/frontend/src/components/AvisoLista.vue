<template>
  <div class="container-fluid content">
    <div v-if="!aviso">
      <div class="row">
        <div class="col-md-12">
          <h4 class="text-center mt-3 mb-3">Intimações/Citações Pendentes</h4>
        </div>

        <div class="col col-sm-12" v-if="errormsg">
          <p class="alert alert-danger">
            <strong>Erro!</strong> {{errormsg}}
          </p>
        </div>

        <div class="col col-sm-12" v-show="avisos !== undefined &amp;&amp; avisos.length == 0">
          <p class="alert alert-warning">
            <strong>Atenção!</strong> Nenhuma intimação pendente.
          </p>
        </div>
      </div>

      <div class="row mb-3 d-print-none" v-show="avisos &amp;&amp; avisos.length > 0">
        <div class="col-sm-2">
          <button type="button" @click="mostrarOutline()" :class="{'btn btn-block': true, 'btn-info': outlineAtivo, 'btn-outline-info': !outlineAtivo}">Filtro Hierárquico
          </button>
        </div>
        <div class="col-sm-2">
          <div class="input-group">
            <div class="input-group-addon">&#128269;</div>
            <input type="text" class="form-control" placeholder="Filtrar" v-model="filtro" ng-model-options="{ debounce: 200 }">
          </div>
        </div>
        <div class="col-sm-2 ml-sm-auto">
          <div class="btn-group btn-block" role="group">
            <button id="btnGroupDrop1" type="button" class="btn btn-secondary dropdown-toggle btn-block" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Avançado</button>
            <div class="dropdown-menu" aria-labelledby="btnGroupDrop1">
              <router-link class="dropdown-item" v-if="$parent.test.properties['balcaovirtual.env'] !== 'prod'" :to="{name:'Avisos Confirmados Recentemente'}" tag="a" exact>Consultar Confirmados</router-link>
              <a class="dropdown-item" @click="listarProcessos()">Listar Processos Marcados</a>
              <a class="dropdown-item" @click="exportarXML('pendente')">Exportar avisos-pendentes.xml</a>
              <a class="dropdown-item" @click="exportarXML('confirmado')">Exportar avisos-confirmados.xml</a>
            </div>
          </div>
        </div>
        <div class="col-sm-2">
          <button class="btn btn-primary btn-block" data-style="expand-left" @click="confirmarEmLote()">
            Confirmar&nbsp;&nbsp
            <span class="badge badge-pill badge-warning">{{filtradosEMarcados.length}}</span>
          </button>
        </div>
      </div>

      <div class="row" v-show="avisos &amp;&amp; avisos.length > 0">
        <div class="col col-md-4 filtro-outline" v-if="outlineAtivo">
          <div class="card card-outline-info mb-3">
            <div class="card-header">Filtro Hierárquico</div>
            <div class="card-body">
              <ul class="pl-0">
                <li v-if="outline" v-for="l1 in outline">
                  <input type="checkbox" v-model="l1.ativo" @change="toogle(l1)"> {{l1.nome}}
                  <ul>
                    <li v-if="l1" v-for="l2 in l1.item">
                      <input type="checkbox" v-model="l2.ativo" @change="toogle(l2)"> {{l2.nome}}
                      <ul>
                        <li v-if="l2" v-for="l3 in l2.item">
                          <input type="checkbox" v-model="l3.ativo" @change="toogle(l3)"> {{l3.nome}}
                        </li>
                      </ul>
                    </li>
                  </ul>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <div :class="{'col': true, 'col-md-8': outlineAtivo, 'col-md-12': !outlineAtivo}">
          <table class="table table-sm table-striped mb-0 table-protocolo table-responsive">
            <thead class="thead-inverse">
              <tr>
                <th class="d-print-none" style="text-align: center">
                  <input type="checkbox" id="progress_checkall" name="progress_checkall" v-model="todos" @change="marcarTodos()"></input>
                </th>

                <th>
                  <a @click="sort('dataaviso')">
                    Data do Cadastro
                    <span v-show="orderByField == 'dataaviso'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>Prazo</th>
                <th>
                  <a @click="sort('datalimiteintimacaoautomatica')">
                    Data Limite Int. Aut.
                    <span v-show="orderByField == 'datalimiteintimacaoautomatica'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>
                  <a @click="sort('tipo')">
                    Tipo
                    <span v-show="orderByField == 'tipo'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>
                  <a @click="sort('eventointimacao')">
                    Evento
                    <span v-show="orderByField == 'eventointimacao'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>
                  <a @click="sort('motivointimacao')">
                    Motivo
                    <span v-show="orderByField == 'motivointimacao'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>
                  <a @click="sort('processoFormatado')">
                    Processo
                    <span v-show="orderByField == 'processoFormatado'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>
                  <a @click="sort('assuntoNome')">
                    Assunto
                    <span v-show="orderByField == 'assuntoNome'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>
                  <a @click="sort('orgao')">
                    Órgão
                    <span v-show="orderByField == 'orgao'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>
                  <a @click="sort('unidade')">
                    Unidade
                    <span v-show="orderByField == 'unidade'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th v-if="false">
                  <a @click="sort('unidadetipo')">
                    Tipo da Unidade
                    <span v-show="orderByField == 'unidadetipo'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th v-if="false">
                  <a @click="sort('localidade')">
                    Localidade
                    <span v-show="orderByField == 'localidade'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>Status</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="r in filtrados">
                <td class="d-print-none" style="text-align: center">
                  <input type="checkbox" value="true" v-model="r.checked" :disabled="r.disabled" class="chk-assinar"></input>
                </td>
                <td>
                  <span v-html="r.dataavisoFormatada"></span>
                </td>
                <td>{{r.numeroprazo}} {{r.tipoprazo}} {{r.multiplicadorprazo}}</td>
                <td>
                  <span v-html="r.datalimiteintimacaoautomaticaFormatada"></span>
                </td>
                <td>{{r.tipo}}</td>
                <td>{{r.eventointimacao}}</td>
                <td>{{r.motivointimacao}}</td>
                <td>
                  <router-link :to="{name: 'Processo', params: {numero: r.processo}}" target="_blank">{{r.processoFormatado}}</router-link>
                </td>
                <td>{{r.assuntoNome}}</td>
                <td>{{r.orgao}}</td>
                <td :title="r.unidadenome">{{r.unidade}}</td>
                <td v-if="false">{{r.unidadetipo}}</td>
                <td v-if="false">{{r.localidade}}</td>
                <td class="status-td">
                  <span v-if="r.errormsg" class="red" v-html="r.errormsg"></span>
                </td>
                <td align="right">
                  <button type="button" v-if="!r.confirmado" @click="confirmarAviso(r, false)" class="btn btn-sm btn-primary d-print-none">Confirmar</button>
                  <button type="button" v-if="r.confirmado" @click="exibirAviso(r)" class="btn btn-sm btn-success d-print-none">Ver</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="col-sm-12 d-print-none" style="padding-top: 1em;">
          <button type="button" @click="imprimir()" id="imprimir" class="btn btn-info float-right ml-3">Imprimir</button>
        </div>
      </div>
    </div>

    <aviso-detalhe v-if="aviso" :aviso="aviso" @voltar="aviso = undefined"></aviso-detalhe>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import ProcessoBL from '../bl/processo.js'
import CnjAssuntoBL from '../bl/cnj-assunto.js'
import { Bus } from '../bl/bus.js'
import AvisoDetalhe from './AvisoDetalhe'

export default {
  name: 'processo',
  components: {
    'aviso-detalhe': AvisoDetalhe
  },
  mounted () {
    this.$on('filtrar', (texto) => { this.filtrarMovimentos(texto) })

    // Carragar a lista de avisos pendentes
    this.$nextTick(function () {
      var i

      this.$http.get('aviso/listar', { block: true }).then(response => {
        for (i = 0; i < response.data.status.length; i++) {
          if (response.data.status[i].errormsg) {
            if (this.errormsg === undefined) this.errormsg = ''
            else this.errormsg += '; '
            this.errormsg += response.data.status[i].system + ': ' + response.data.status[i].errormsg
          }
        }

        if (response.data.list) {
          this.$set(this, 'avisos', [])
          for (i = 0; i < response.data.list.length; i++) {
            var aviso = response.data.list[i]
            aviso.errormsg = undefined
            aviso.checked = true
            aviso.disabled = false
            aviso.processoFormatado = ProcessoBL.formatarProcesso(aviso.processo)
            aviso.dataavisoFormatada = UtilsBL.formatJSDDMMYYYYHHMM(aviso.dataaviso)
            aviso.datalimiteintimacaoautomaticaFormatada = UtilsBL.formatJSDDMMYYYYHHMM(aviso.datalimiteintimacaoautomatica)
            aviso.assuntoNome = CnjAssuntoBL.nome(aviso.assunto)
            this.avisos.push(aviso)
          }

          this.montarOutline(this.avisos)
        }
        UtilsBL.logEvento('aviso', 'listar avisos')
      }, error => UtilsBL.errormsg(error, this))
    })
  },
  data () {
    return {
      outlineMap: {},
      orderByField: 'dataaviso',
      reverseSort: false,
      avisos: undefined,
      todos: true,
      outlineAtivo: false,
      filtro: undefined,
      tipoRepresentante: {
        A: 'Advogado',
        E: 'Escritório de Advocacia',
        M: 'Ministério Público',
        D: 'Defensor Público',
        P: 'Advogado Público'
      },
      errormsg: undefined,
      aviso: undefined
    }
  },
  computed: {
    filtrados: function () {
      console.log('recalculando filtrados...', this.modified)
      if (this.avisos === undefined) return []
      var a = this.avisos
      var outmap = this.outlineMap
      a = a.filter(item => outmap[item.filtro].ativo)
      a = UtilsBL.filtrarPorSubstring(a, this.filtro)

      a.sort((x, y) => {
        if (x[this.orderByField] !== y[this.orderByField]) {
          var r = 0
          if (x[this.orderByField] === undefined && y[this.orderByField] !== undefined) r = -1
          else if (x[this.orderByField] !== undefined && y[this.orderByField] === undefined) r = 1
          else r = x[this.orderByField] < y[this.orderByField] ? -1 : 1
          if (!this.reverseSort) r = -r
          return r
        }
        return 0
      })

      return a
    },

    filtradosEMarcados: function () {
      return this.filtrados.filter(function (item) {
        return item.checked
      })
    }
  },
  methods: {
    sort: function (field) {
      if (field !== this.orderByField) {
        this.orderByField = field
        this.reverseSort = true
      } else {
        this.reverseSort = !this.reverseSort
      }
    },

    montarOutline: function (arr) {
      var o, i
      var outline = []
      var map = {}
      for (i = 0; i < arr.length; i++) {
        var a = arr[i]
        var ko = a.orgao
        if (!map[ko]) {
          o = {
            nome: a.orgao,
            filtro: ko,
            ativo: true
          }
          outline.push(o)
          map[ko] = o
        }

        var kl = ko + ';' + a.localidade
        if (!map[kl]) {
          o = {
            nome: a.localidade,
            filtro: kl,
            ativo: true
          }
          if (!map[ko].item) map[ko].item = []
          map[ko].item.push(o)
          map[kl] = o
        }

        var kt = kl + ';' + a.unidadetipo
        if (!map[kt]) {
          o = {
            nome: a.unidadetipo,
            filtro: kt,
            ativo: true
          }
          if (!map[kl].item) map[kl].item = []
          map[kl].item.push(o)
          map[kt] = o
        }
        a.filtro = kt
      }
      this.outlineMap = map
      this.outline = outline

      for (i = 0; i < this.outline.length; i++) {
        this.outlineInitPai(this.outline[i])
        this.outline[i].ativo = true
        this.toogle(this.outline[i])
      }
    },

    outlineInitPai: function (outline) {
      if (!outline.item) return
      for (var i = 0; i < outline.item.length; i++) {
        outline.item[i].pai = outline
        this.outlineInitPai(outline.item[i])
      }
    },

    toogle: function (outline) {
      if (outline.ativo) {
        this.tooglePai(outline, outline.ativo)
      } else {
        this.toogleUncheckParentIfEmpty(outline)
      }
      if (!outline.item) return
      for (var i = 0; i < outline.item.length; i++) {
        outline.item[i].ativo = outline.ativo
        this.toogle(outline.item[i])
      }
      this.modified = new Date()
    },

    tooglePai: function (outline, ativo) {
      if (!outline.pai) return
      outline.pai.ativo = ativo
      this.tooglePai(outline.pai, ativo)
    },

    toogleUncheckParentIfEmpty: function (outline) {
      if (!outline.pai) return
      for (var i = 0; i < outline.pai.item.length; i++) {
        if (outline.pai.item[i].ativo) return
      }
      outline.pai.ativo = false
      this.toogleUncheckParentIfEmpty(outline.pai)
    },

    mostrarOutline: function () {
      this.outlineAtivo = !this.outlineAtivo
      if (!this.outlineAtivo) {
        for (var property in this.outlineMap) {
          if (this.outlineMap.hasOwnProperty(property)) {
            this.outlineMap[property].ativo = true
          }
        }
      }
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

    confirmarAviso: function (aviso, lote) {
      this.errormsg = undefined
      if (lote) Bus.$emit('prgCaption', 'Confirmando ' + aviso.processo)

      this.$http.post('processo/' + aviso.processo + '/aviso/' + aviso.idaviso + '/receber?orgao=' + aviso.orgao, {}, { block: !lote }).then(response => {
        var d = response.data
        aviso.teor = ProcessoBL.formatarTexto(d.teor)
        aviso.datarecebimento = d.datarecebimento
        aviso.confirmado = true
        aviso.checked = false
        aviso.disabled = true
        aviso.errormsg = undefined
        if (!lote) this.$set(this, 'aviso', aviso)
        UtilsBL.logEvento('aviso', 'confirmar', 'singular')
        if (lote) Bus.$emit('prgNext')
      }, error => {
        aviso.errormsg = error.data.errormsg
        if (lote) Bus.$emit('prgNext')
      })
    },

    confirmarEmLote: function () {
      var a = this.filtradosEMarcados
      Bus.$emit('prgStart', 'Confirmando Intimações/Citações', a.length, (i) => this.confirmarAviso(a[i], true))
    },

    listarProcessos: function () {
      var avisos = this.filtradosEMarcados
      var map = {}
      var processos = []
      for (var i = 0; i < avisos.length; i++) {
        if (map.hasOwnProperty(avisos[i].processo)) continue
        map[avisos[i].processo] = true
        processos.push({
          numero: ProcessoBL.formatarProcesso(avisos[i].processo),
          orgao: avisos[i].orgao,
          unidade: avisos[i].unidade
        })
      }
      this.$router.push({ name: 'Lista de Processos', params: { processos: processos } })
    },

    imprimir: function () {
      window.print()
    },

    exportarXML: function (tipo) {
      this.$http.get('aviso-' + tipo + '/xml').then(response => {
        var jwt = response.data.jwt
        window.open(this.$http.options.root + '/download/' + jwt + '/' + this.$parent.jwt.username + '-avisos-' + tipo + 's.pdf')
        UtilsBL.logEvento('aviso', 'mostrar avisos-' + tipo + 's.xml')
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    }
  }
}
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
.red {
  color: red
}

.protocolado {
  color: green
}

.odd {
  background-color: rgba(0, 0, 0, .05)
}

.card-consulta-processual DIV P B {
  color: #fff
}

.card-consulta-processual DIV P {
  margin-bottom: 0.5rem
}

.card-consulta-processual DIV I {
  line-height: 3rem;
  height: 3rem;
  color: #fff;
  float: right;
  font-size: 4rem;
  margin: 0rem -0.5rem 0rem 0rem
}

.card-text-descr {
  margin-bottom: 0
}
</style>
