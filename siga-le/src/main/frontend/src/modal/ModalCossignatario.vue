<template>
  <div>
    <validation-observer v-slot="{ invalid }">
      <b-modal
        id="cota"
        ref="modal"
        v-model="showModal"
        title="Incluir Cossignatario"
        hide-header-close
        cancel-title="Cancelar"
        ok-title="Prosseguir"
        :ok-disabled="invalid"
        @ok="save"
      >
        <form>
          <div class="row">
            <div class="form-group col col-12">
              <validation-provider
                rules="required"
                :immediate="true"
                v-slot="{ errors }"
              >
                <my-pessoa v-model="matricula" label="Sigla da Pessoa" />
                <span
                  v-if="false"
                  v-show="errors.length > 0"
                  class="help is-danger"
                  >{{ errors[0] }}</span
                >
              </validation-provider>
            </div>

            <div class="form-group col col-12">
              <b-form-checkbox
                id="personalizar"
                name="personalizar"
                v-model="personalizar"
              >
                Personalizar campos da assinatura
              </b-form-checkbox>
            </div>
            <div class="form-group col col-12" v-if="personalizar">
              <my-input
                label="Função"
                id="funcao"
                name="funcao"
                v-model="funcao"
                :edit="true"
              ></my-input>
            </div>
            <div class="form-group col col-12" v-if="personalizar">
              <my-input
                label="Lotação"
                id="lotacao"
                name="lotacao"
                v-model="lotacao"
                :edit="true"
              ></my-input>
            </div>
          </div>
          <em
            v-if="errormsg &amp;&amp; errormsg !== ''"
            for="processos"
            class="invalid"
            >{{ errormsg }}</em
          >
        </form>
      </b-modal>
    </validation-observer>
  </div>
</template>

<script>
import { Bus } from "../bl/bus.js";
import ItemTemplate from "../components/ItemTemplate.vue";

export default {
  name: "definir-perfil",

  mounted() {},

  data() {
    return {
      showModal: false,
      errormsg: undefined,
      matricula: null,
      personalizar: false,
      funcao: undefined,
      lotacao: undefined,
      localidade: undefined,
      documentos: undefined,
      template: ItemTemplate,
    };
  },

  methods: {
    show: function(documentos, cont) {
      this.showModal = true;
      this.errormsg = undefined;
      this.documentos = documentos;
      this.matricula = null;
      this.funcao = undefined;
      this.lotacao = undefined;
      this.localidade = undefined;
      this.cont = cont;
      this.carregar();
    },

    cancel: function(e) {
      e.cancel();
    },

    save: function() {
      if ((this.matricula || "") === "") {
        this.errormsg = "Matrícula deve ser informada.";
        return;
      }

      var matricula = this.matricula;
      if (matricula) matricula = matricula.split(" - ")[0];

      Bus.$emit("incluirCossignatario", this.documentos, this.cont, {
        matricula: matricula,
        funcao: this.funcao,
        lotacao: this.lotacao,
        lotcalidade: this.localidade,
      });
      this.$refs.modal.hide(true);
    },

    validar: function() {
      this.$nextTick(() => this.$validator.validateAll());
    },
  },

  components: {},
};
</script>
