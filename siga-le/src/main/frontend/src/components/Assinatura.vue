<template>
  <div>
    <b-modal
      id="cota"
      ref="modal"
      v-model="showModal"
      title="Assinatura com Senha"
      hide-header-close
    >
      <validation-observer v-slot="{ invalid }">
        <form>
          <div class="row">
            <div class="form-group col col-sm-6">
              <my-input
                label="Usuário"
                id="username"
                name="username"
                v-model="username"
                validate="required"
                disabled="true"
              ></my-input>
            </div>
            <div class="form-group col col-sm-6">
              <my-input
                type="password"
                label="Senha"
                id="password"
                name="password"
                v-model="password"
                validate="required"
              ></my-input>
            </div>
          </div>
          <small id="usernameHelp" class="form-text text-muted">
            <strong>Atenção</strong>! Ao clicar em prosseguir, será realizada a
            assinatura com senha. Por favor, tenha certeza que deseja realmente
            assinar antes de clicar em 'Prosseguir'.</small
          >
          <em
            v-if="errormsg &amp;&amp; errormsg !== ''"
            for="processos"
            class="invalid"
            >{{ errormsg }}</em
          >
        </form>
        <div style="width: 100%" slot="modal-footer">
          <b-btn
            class="float-right ml-2"
            variant="primary"
            @click="save"
            :disabled="invalid"
          >
            Prosseguir
          </b-btn>
          <b-btn
            class="float-right"
            variant="secondary"
            @click="$refs.modal.hide(false)"
          >
            Cancelar
          </b-btn>
        </div>
      </validation-observer>
    </b-modal>
  </div>
</template>

<script>
import { Bus } from '../bl/bus.js'

export default {
  name: 'assinatura',

  mounted() {},

  data() {
    return {
      showModal: false,
      errormsg: undefined,
      username: '',
      password: undefined,
      documentos: undefined,
    }
  },

  methods: {
    show: function(documentos, cont) {
      this.showModal = true
      this.errormsg = undefined
      this.documentos = documentos
      this.cont = cont
    },

    cancel: function(e) {
      e.cancel()
    },

    save: function() {
      if ((this.username || '') === '') {
        this.errormsg = 'Matrícula deve ser informado.'
        return
      }

      if ((this.password || '') === '') {
        this.errormsg = 'Senha deve ser informada.'
        return
      }

      Bus.$emit(
        'assinarComSenha',
        this.documentos,
        this.username,
        this.password,
        this.cont
      )
      this.$refs.modal.hide(true)
    },

    validar: function() {
      this.$nextTick(() => this.$validator.validateAll())
    },
  },

  components: {
    //    'pdf-preview': PdfPreview
  },
}
</script>
