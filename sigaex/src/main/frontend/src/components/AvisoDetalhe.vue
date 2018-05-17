<template>
  <div class="container content profile">
    <div class="row" v-if="errormsg !== undefined">
      <div class="col col-sm-12">
        <p class="alert alert-danger">
          <strong>Erro!</strong> {{errormsg}}
        </p>
      </div>
    </div>

    <div class="row mt-3 mb-3">
      <div class="col-md-12">
        <h4 class="text-center mb-0">{{aviso.tipo}}</h4>
      </div>
    </div>

    <div class="card-deck pb-3">
      <div class="card card-outline-info">
        <div class="card-header">
          Processo:
          <router-link :to="{name: 'Processo', params: {numero: aviso.processo}}" target="_blank">{{aviso.processoFormatado}}</router-link>
          <br>Cadastro:
          <span v-html="aviso.dataavisoFormatada"></span>
          <br>Recebimento: {{aviso.datarecebimento}}
        </div>
        <div class="card-body">
          <p class="card-text" v-html="aviso.teor"></p>
        </div>
      </div>
    </div>

    <div class="row d-print-none">
      <div class="col-sm-12 mt-3">
        <button type="button" @click="imprimir()" id="imprimir" class="btn btn-info float-right">Imprimir</button>
        <button type="button" @click="voltar()" class="btn btn-success">Voltar</button>
      </div>
    </div>

    <div class="row">
      <div class="col col-sm-12">
        <hr class="mt-5 mb-1"></hr>
        <p class="text-center">
          As informações aqui contidas não produzem efeitos legais. Somente a publicação no D.O. tem validade para contagem de prazos.
          <br>Consulta realizada em: {{aviso.datarecebimento}}.
        </p>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'aviso-detalhe',
  props: {
    aviso: {
      type: Object,
      default: function () {
        return {}
      }
    }
  },
  data () {
    return {
      errormsg: undefined
    }
  },
  methods: {
    voltar: function () {
      this.$emit('voltar')
    },

    imprimir: function () {
      window.print()
    }
  }
}
</script>
