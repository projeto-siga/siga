<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Intimações/Citações Confirmadas Recentemente</h4>
      </div>

      <div class="col col-sm-12" v-if="errormsg">
        <p class="alert alert-danger">
          <strong>Erro!</strong> {{errormsg}}
        </p>
      </div>
    </div>

    <!-- QUANTIDADE POR DATA -->
    <div class="row">
      <div class="col col-12" v-if="quantidadePorData.length > 0">
        <table class="table table-striped mb-0 table-responsive">
          <thead class="thead-inverse">
            <tr>
              <th rowspan="2">Data</th>
              <th style="text-align: center;" colspan="2">Quantidade do Usuário</th>
              <th style="text-align: center;" colspan="2">Quantidade do Grupo</th>
            </tr>
            <tr>
              <th style="text-align: center;">Confirmação Manual</th>
              <th style="text-align: center;">Confirmação Automática</th>
              <th style="text-align: center;">Confirmação Manual</th>
              <th style="text-align: center;">Confirmação Automática</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in quantidadePorData">
              <td>
                <span v-html="p.data"></span>
              </td>
              <td style="text-align: center;">
                <a href="" @click.prevent="listar(p.data, true, false, false)">{{p.quantidadedousuarioporconfirmacao}}</a>
              </td>
              <td style="text-align: center;">
                <a href="" @click.prevent="listar(p.data, false, true, false)">{{p.quantidadedousuarioporomissao}}</a>
              </td>
              <td style="text-align: center;">
                <a href="" @click.prevent="listar(p.data, true, false, true)">{{p.quantidadedogrupoporconfirmacao}}</a>
              </td>
              <td style="text-align: center;">
                <a href="" @click.prevent="listar(p.data, false, true, true)">{{p.quantidadedogrupoporomissao}}</a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="col col-sm-12" v-if="!errormsg &amp;&amp; quantidadePorData.length == 0">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhuma intimação/citação confirmada nos últimos 7 dias.
        </p>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'

export default {
  name: 'aviso-confirmado-recentes',

  mounted() {
    this.$nextTick(() => {
      this.$http.get('aviso-confirmado/contar', { block: true }).then(
        response => {
          for (var i = 0; i < response.data.list.length; i++) {
            var qd = response.data.list[i]
            // qd.dataFormatada = UtilsBL.formatDDMMYYYY(qd.data)
            this.quantidadePorData.push(qd)
          }
        },
        error => UtilsBL.errormsg(error, this)
      )
    })
  },

  data() {
    return {
      quantidadePorData: [],
      errormsg: undefined
    }
  },

  methods: {
    listar: function(data, porConfirmacao, porOmissao, doGrupo) {
      this.$router.push({
        name: 'Lista de Avisos Confirmados',
        params: {
          dataInicial: data,
          dataFinal: data,
          porConfirmacao: porConfirmacao,
          porOmissao: porOmissao,
          doGrupo: doGrupo
        }
      })
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
