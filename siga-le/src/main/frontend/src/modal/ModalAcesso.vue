<template>
  <div>
    <validation-observer v-slot="{ invalid }">
      <b-modal
        id="cota"
        ref="modal"
        v-model="showModal"
        title="Definir Acesso"
        hide-header-close
        cancel-title="Cancelar"
        ok-title="Prosseguir"
        :ok-disabled="invalid"
        @ok="save"
      >
        <form>
          <div class="row">
            <div class="form-group col col-12">
              <my-select
                label="Acesso"
                id="acesso"
                name="acesso"
                v-model="idAcesso"
                validate="required"
                :disabled="false"
                :list="acessos"
                :edit="true"
                chave="idAcesso"
                descr="nome"
              ></my-select>
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
import UtilsBL from "../bl/utils.js";

export default {
  name: "definir-acesso",

  mounted() {},

  data() {
    return {
      showModal: false,
      errormsg: undefined,
      documentos: undefined,
      idAcesso: undefined,
      acessos: [],
    };
  },

  methods: {
    carregar: function() {
      this.carregando = true;
      var self = this;
      this.$http
        .get(
          "sigaex/api/v1/documentos/" +
            this.documentos[0].codigo +
            "/acessos-disponiveis"
        )
        .then(
          (response) => {
            self.carregando = false;
            self.acessos.length = 0;
            var resp = response.data;
            for (var i = 0; i < resp.list.length; i++) {
              self.acessos.push(
                UtilsBL.applyDefauts(resp.list[i], {
                  id: undefined,
                  nome: undefined,
                })
              );
            }
          },
          (error) => {
            self.carregando = false;
            UtilsBL.errormsg(error, this);
          }
        );
    },

    show: function(documentos, cont) {
      this.showModal = true;
      this.errormsg = undefined;
      this.documentos = documentos;
      this.cont = cont;
      this.carregar();
    },

    cancel: function(e) {
      e.cancel();
    },

    save: function() {
      console.log("acesso");
      if (!this.idAcesso) {
        this.errormsg = "Acesso deve ser informado.";
        return;
      }

      Bus.$emit("definirAcesso", this.documentos, this.cont, {
        idAcesso: this.idAcesso,
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
