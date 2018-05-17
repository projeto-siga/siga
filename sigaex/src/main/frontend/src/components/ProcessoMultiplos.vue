<template>
  <div>
    <b-modal id="processosMultiplos" v-model="showModal" title="Múltiplos Processos" close-title="Cancelar" ok-title="Prosseguir" hide-header-close @hide="save">
      <form>
        <label class="control-label" for="processos" style="width: 100%">Números dos Processos </label>
        <textarea class="form-control" style="width: 100%" id="processos" aria-describedby="processosHelp" v-model="processos" placeholder="" rows="10" autofocus></textarea>
        <small id="processosHelp" class="form-text text-muted">Informe os números dos processos separados por vírgula ou quebra de linha. Os números podem incluir pontos e traços ou podem ser representados apenas por seus algarismos.</small>
        <em v-if="errormsg &amp;&amp; errormsg !== ''" for="processos" class="invalid">{{errormsg}}</em>
      </form>
    </b-modal>
  </div>
</template>

<script>
import ProcessoBL from '../bl/processo.js'

export default {
  name: 'processo-multiplos',

  data () {
    return {
      showModal: false,
      errormsg: undefined,
      processos: ''
    }
  },

  methods: {
    show: function () {
      this.showModal = true
      this.errormsg = undefined
    },

    save: function (e) {
      if (e.isOK === undefined) e.cancel()
      if (!e.isOK) return

      var reProc = /^(\d{7})-?(\d{2})\.?(\d{4})\.?(4)\.?(02)\.?(\d{4})\/?-?(\d{2})?$/
      var reSep = /(?:\s+|\s*(?:,|;)\s*)/

      if ((this.processos || '') === '') {
        this.errormsg = 'Números de processos devem ser informados.'
        e.cancel()
      }

      var arr = this.processos.split(reSep)
      for (var i = 0; i < arr.length; i++) {
        if (arr[i] === '') continue
        var m = reProc.exec(arr[i])
        if (!m) {
          this.errormsg = 'Número de processo inválido: \'' + arr[i] + '\''
          e.cancel()
          return
        }
        arr[i] = ProcessoBL.formatarProcesso(arr[i])
      }

      this.$emit('ok', arr)
    }
  }
}
</script>
