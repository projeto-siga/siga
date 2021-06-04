<template>
  <div>
    <validation-observer v-slot="{ invalid }">
      <b-modal
        id="cota"
        ref="modal"
        v-model="showModal"
        title="Anotar"
        hide-header-close
        cancel-title="Cancelar"
        ok-title="Prosseguir"
        :ok-disabled="invalid"
        @ok="save"
      >
        <form>
          <div class="row">
            <div class="form-group col col-sm-12">
              <validation-provider
                rules="required"
                :immediate="true"
                v-slot="{ errors }"
              >
                <label class="control-label" for="anotacao" style="width: 100%"
                  >Texto da Anotação</label
                >
                <textarea
                  name="anotacao"
                  id="anotacao"
                  v-model="anotacao"
                  maxlength="255"
                  class="form-control"
                  aria-describedby="anotacaoHelp"
                  :class="{ 'is-invalid': errors.length > 0 }"
                  style="width: 100%"
                  :rows="3"
                  autofocus
                ></textarea>
                <span
                  v-if="false"
                  v-show="errors.length > 0"
                  class="help is-danger"
                  >{{ errors[0] }}</span
                >
              </validation-provider>
            </div>
          </div>
        </form>
      </b-modal>
    </validation-observer>
  </div>
</template>

<script>
import { Bus } from '../bl/bus.js'

export default {
  name: 'anotacao',

  mounted() {},

  data() {
    return {
      showModal: false,
      errormsg: undefined,
      documentos: undefined,
      anotacao: undefined,
    }
  },

  methods: {
    show: function(documentos, cont) {
      this.showModal = true
      this.errormsg = undefined
      this.documentos = documentos
      this.anotacao = undefined
      this.cont = cont
    },

    cancel: function(e) {
      e.cancel()
    },

    save: function() {
      if ((this.anotacao || '') === '') {
        this.errormsg = 'Texto da anotação deve ser informado.'
        return
      }
      Bus.$emit('anotar', this.documentos, this.anotacao, this.cont)
      this.$refs.modal.hide(true)
    },

    validar: function() {
      this.$nextTick(() => this.$validator.validateAll())
    },
  },

  components: {},
}
</script>
