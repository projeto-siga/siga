<template>
  <div class="card card-consulta-processual mb-3" style="background-color: #f8ff99">
    <div class="card-header">
      <strong>{{titulo}}</strong>
      <span class="close-button d-print-none icone-em-linha" aria-hidden="true" @click="esconderNotas()">
        <i class="fa fa-close"></i>
      </span>
      <span v-if="state === 'changed'" class="pull-right chars-left icone-em-linha">{{limit - count}}/{{limit}} restantes</span>
      <span v-if="errormsg === undefined &amp;&amp; (state === undefined || state === 'processing')" class="pull-right processing icone-em-linha">
        <i class="fa fa-refresh fa-spin fa-fw"></i>
      </span>
    </div>
    <div class="card-body">
      <div class="alert alert-danger" role="alert" v-if="errormsg">{{errormsg}}</div>
      <textarea :disabled="state === undefined || errormsg !== undefined" ref="textarea" v-model="textoAtual" @input="notaAlterada"></textarea>
    </div>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import _ from 'underscore'

export default {
  name: 'processo-nota',
  props: ['titulo', 'pessoal', 'processo', 'orgao'],
  mounted () {
    this.carregarNota()
  },
  data () {
    return {
      state: undefined,
      id: undefined,
      dataalteracao: undefined,
      textoAtual: undefined,
      textoAnterior: undefined,
      textoDebonced: undefined,
      errormsg: undefined,
      limit: 2000
    }
  },

  watch: {
    count: function () {
      if (this.count > this.limit) {
        this.textoAtual = UtilsBL.clipUTF8Length(this.textoAtual, this.limit)
        this.notaAlterada()
      }
    }
  },

  computed: {
    count: function () {
      return this.textoAtual !== undefined ? UtilsBL.getUTF8Length(this.textoAtual) : 0
    }
  },

  methods: {
    carregarNota: function () {
      if (this.state !== undefined && this.state !== 'ready') {
        setTimeout(this.carregarNota, 120000)
        return
      }
      this.$http.get('processo/' + this.processo + '/nota?orgao=' + this.orgao).then(
        response => {
          for (var i = 0; i < response.data.list.length; i++) {
            var n = response.data.list[i]
            if (n.pessoal !== this.pessoal) continue
            UtilsBL.overrideProperties(n, response.data.list[i])
            this.id = n.idnota
            this.dataalteracao = n.dataalteracao
            this.textoAnterior = n.texto
            this.textoAtual = n.texto
          }
          this.errormsg = undefined
          if (this.state === undefined) this.state = 'ready'
          this.$nextTick(() => this.$emit('input', this.textoAtual))
          setTimeout(this.carregarNota, 120000)
        },
        error => UtilsBL.errormsg(error, this))
    },

    esconderNotas: function () {
      this.$parent.$parent.$parent.$emit('setting', 'mostrarNotas', false)
    },

    atualizar: function (nota) {
      if (nota === undefined) nota = { idnota: undefined, texto: undefined, dataalteracao: undefined, pessoal: this.pessoal }
      if (this.textoAtual === nota.texto) this.state = 'ready'
      else {
        this.state = 'changed'
        this.refletirAlteracao()
      }

      this.dataalteracao = nota.dataalteracao
      this.id = nota.idnota
    },

    refletirAlteracao: _.debounce(function () {
      if (this.textoAtual) {
        if (this.id) this.putNota(this.textoAtual)
        else this.postNota(this.textoAtual)
      } else {
        if (this.id) this.deleteNota()
      }
    }, 2000),

    notaAlterada: function () {
      if (this.textoAtual !== undefined && this.textoAtual.trim() === '') this.textoAtual = undefined
      if (this.state !== 'processing') {
        this.state = 'changed'
        this.refletirAlteracao()
      } else {
        this.refletirAlteracao.clear()
      }
      this.$emit('input', this.textoAtual)
    },

    postNota: function (texto) {
      this.state = 'processing'
      this.$http.post('processo/' + this.processo + '/nota?orgao=' + this.orgao, {
        texto: texto,
        pessoal: this.pessoal
      }).then(
        response => {
          this.atualizar(response.data.nota)
        },
        error => UtilsBL.errormsg(error, this))
    },

    putNota: function (texto) {
      this.state = 'processing'
      this.$http.put('processo/' + this.processo + '/nota/' + this.id + '?orgao=' + this.orgao, {
        texto: texto,
        pessoal: this.pessoal,
        dataalteracao: this.dataalteracao
      }).then(
        response => {
          this.atualizar(response.data.nota)
        },
        error => UtilsBL.errormsg(error, this))
    },

    deleteNota: function () {
      this.state = 'processing'
      this.$http.delete('processo/' + this.processo + '/nota/' + this.id + '?orgao=' + this.orgao, {}).then(
        response => {
          this.atualizar()
        },
        error => UtilsBL.errormsg(error, this))
    }
  }
}
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
textarea {
  border: none;
  background: none;
  width: 100%;
  resize: none;
  overflow: hidden;
  min-height: 50px;
}

.chars-left {
  margin-right: 1em;
}

.processing {
  margin-right: 1em;
}
</style>
