<template>
  <div class="container-fluid content">
    <template v-if="!aviso">
      <div class="row">
        <div class="col-md-12">
          <h4 class="text-center mt-3 mb-3">Consulta de Intimações/Citações Confirmadas</h4>
        </div>
      </div>

      <div class="pt-3 pb-3 pl-3 pr-3 mb-4" style="background-color: #f7f7f9">
        <div class="row mb-3 justify-content-center">
          <div class="form-groupx col-sm-auto">
            <label for="dataInicial">Data Inicial</label>
            <input id="dataInicial" type="text" class="form-control" placeholder="DD/MM/AAAA" v-model="dataInicial">
          </div>
          <div class="form-groupx col-sm-auto">
            <label for="dataFinal">Data Final</label>
            <input id="dataFinal" type="text" class="form-control" placeholder="DD/MM/AAAA" v-model="dataFinal">
          </div>
          <div class="form-groupx col-md-auto">
            <label for="abrangencia">Abrangência</label>
            <select id="abrangencia" class="form-control" v-model="doGrupo" name="abrangencia">
              <option :value="false">Do Usuário</option>
              <option :value="true">Do Grupo</option>
            </select>
          </div>
          <div class="form-groupx col-sm-auto">
            <label for="dataFinal" class="mb-0">Tipo de Confirmação</label>
            <div>
              <label class="form-check-label"> <input type="checkbox" class="form-check-input" v-model="porConfirmacao"> Manual
              </label>
            </div>
            <div>
              <label class="form-check-label"> <input type="checkbox" class="form-check-input" v-model="porOmissao"> Automática
              </label>
            </div>
          </div>
          <div class="col-sm-auto">
            <label for="pesquisar">&nbsp;</label>
            <button id="pesquisar" class="btn btn-primary btn-block" data-style="expand-left" @click="pesquisar(dataInicial, dataFinal, porConfirmacao, porOmissao, doGrupo)">
              Pesquisar
            </button>
          </div>
        </div>
      </div>

      <!-- RELATÓRIO DIÁRIO -->
      <div class="row" v-if="avisos.length > 0">
        <div class="col col-sm-12">
          <div class="float-right pb-2">
            Filtrar:
            <input type="text" v-model="filtro"></input>
          </div>
          <div class="protocolos-header">
            Intimações/Citações Encontradas
          </div>
          <table class="table table-sm table-striped mb-0 table-protocolo table-responsive">
            <thead class="thead-inverse">
              <tr>
                <th>
                  <a @click="sort('dataconfirmacao')">
                    Data da Confirmação
                    <span v-show="orderByField == 'dataconfirmacao'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
                </th>
                <th>
                  <a @click="sort('usuarioconfirmacao')">
                    Usuário
                    <span v-show="orderByField == 'usuarioconfirmacao'">
                      <span v-show="!reverseSort">&#8679;</span>
                      <span v-show="reverseSort">&#8681;</span>
                    </span>
                  </a>
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
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="r in filtrados">
                <td>
                  <span v-html="r.dataconfirmacaoFormatada"></span>
                </td>
                <td>
                  <span v-html="r.usuarioconfirmacao"></span>
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
                <td align="right">
                  <button type="button" @click="exibirAviso(r)" class="btn btn-sm btn-success d-print-none">Ver</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="col-sm-12" style="padding-top: 1em;">
        <button type="button" @click="imprimir()" id="imprimir" v-if="filtrados.length > 0" class="btn btn-info float-right ml-3 d-print-none">Imprimir</button>
      </div>
    </template>

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
  name: 'aviso-confirmado-lista',

  components: {
    'aviso-detalhe': AvisoDetalhe
  },

  mounted() {
    this.$nextTick(() => {
      if (this.dataInicial && this.dataFinal) this.carregarLista()
    })
  },

  data() {
    return {
      dataInicial: this.$route.params.dataInicial,
      dataFinal: this.$route.params.dataFinal,
      porConfirmacao: this.$route.params.porConfirmacao,
      porOmissao: this.$route.params.porOmissao,
      doGrupo: this.$route.params.doGrupo,
      avisos: [],
      aviso: undefined,
      orderByField: 'dataconfirmacao',
      reverseSort: false,
      filtro: undefined
    }
  },

  computed: {
    filtrados: function() {
      console.log('recalculando filtrados...', this.modified)
      var a = this.avisos
      a = UtilsBL.filtrarPorSubstring(a, this.filtro)

      a.sort((x, y) => {
        if (x[this.orderByField] !== y[this.orderByField]) {
          var r = 0
          if (
            x[this.orderByField] === undefined &&
            y[this.orderByField] !== undefined
          ) {
            r = -1
          } else if (
            x[this.orderByField] !== undefined &&
            y[this.orderByField] === undefined
          ) {
            r = 1
          } else r = x[this.orderByField] < y[this.orderByField] ? -1 : 1
          if (!this.reverseSort) r = -r
          return r
        }
        return 0
      })

      return a
    }
  },

  methods: {
    carregarLista: function() {
      var i
      this.$http
        .get(
          'aviso-confirmado/listar?datainicial=' +
            this.dataInicial +
            '&datafinal=' +
            this.dataFinal +
            '&confirmacao=' +
            this.porConfirmacao +
            '&omissao=' +
            this.porOmissao +
            '&grupo=' +
            this.doGrupo,
          { block: true }
        )
        .then(
          response => {
            for (i = 0; i < response.data.status.length; i++) {
              if (response.data.status[i].errormsg) {
                if (this.errormsg === undefined) this.errormsg = ''
                else this.errormsg += '; '
                this.errormsg +=
                  response.data.status[i].system +
                  ': ' +
                  response.data.status[i].errormsg
              }
            }

            if (response.data.list) {
              this.$set(this, 'avisos', [])
              for (i = 0; i < response.data.list.length; i++) {
                var aviso = response.data.list[i]
                aviso.errormsg = undefined
                aviso.processoFormatado = ProcessoBL.formatarProcesso(
                  aviso.processo
                )
                aviso.dataavisoFormatada = UtilsBL.formatJSDDMMYYYYHHMM(
                  aviso.dataaviso
                )
                aviso.dataconfirmacaoFormatada = UtilsBL.formatJSDDMMYYYYHHMM(
                  aviso.dataconfirmacao
                )
                aviso.datalimiteintimacaoautomaticaFormatada = UtilsBL.formatJSDDMMYYYYHHMM(
                  aviso.datalimiteintimacaoautomatica
                )
                aviso.datarecebimento = aviso.dataconfirmacaoFormatada.replace(
                  '&nbsp;',
                  ' '
                )
                aviso.assuntoNome = CnjAssuntoBL.nome(aviso.assunto)
                aviso.teor = ProcessoBL.formatarTexto(aviso.teor)
                this.avisos.push(aviso)
              }
            }
          },
          error => {
            Bus.$emit('message', 'Erro', error.data.errormsg)
          }
        )
    },

    pesquisar: function(
      dataInicial,
      dataFinal,
      porConfirmacao,
      porOmissao,
      doGrupo
    ) {
      this.$router.push({
        name: 'Lista de Avisos Confirmados',
        params: {
          dataInicial: dataInicial,
          dataFinal: dataFinal,
          porConfirmacao: porConfirmacao,
          porOmissao: porOmissao,
          doGrupo: doGrupo
        }
      })
      this.carregarLista()
    },

    sort: function(field) {
      if (field !== this.orderByField) {
        this.orderByField = field
        this.reverseSort = true
      } else {
        this.reverseSort = !this.reverseSort
      }
    },

    exibirAviso: function(aviso) {
      this.aviso = aviso
    },

    imprimir: function() {
      window.print()
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
