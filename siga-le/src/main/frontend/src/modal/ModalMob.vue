<template>
  <div>
    <validation-observer v-slot="{ invalid }">
      <b-modal
        id="cota"
        ref="modal"
        v-model="showModal"
        :title="titulo"
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
                <label class="control-label" for="mob" style="width: 100%">{{
                  campo
                }}</label>
                <v-autocomplete
                  :items="mobs"
                  name="mob"
                  id="mob"
                  v-model="mob"
                  :get-label="getLabelMob"
                  :component-item="template"
                  @update-items="updateMobs"
                  input-class="form-control"
                ></v-autocomplete>
                <span
                  v-if="false"
                  v-show="errors.length > 0"
                  class="help is-danger"
                  >{{ errors[0] }}</span
                >
              </validation-provider>
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
import ItemTemplate from "../components/ItemTemplate";

export default {
  name: "juntar",

  mounted() {},

  data() {
    return {
      showModal: false,
      errormsg: undefined,
      documento: null,
      mob: undefined,
      mobs: [],
      cont: undefined,
      titulo: undefined,
      campo: undefined,
      emit: undefined,
      template: ItemTemplate,
    };
  },

  methods: {
    getLabelMob: function(item) {
      return item;
    },
    updateMobs: function(text) {
      if (!text || text === "") return;
      this.errormsg = undefined;
      this.$http.get("sigaex/api/v1/documentos/" + encodeURI(text)).then(
        (response) => {
          this.mobs = [];
          // var l = response.data.list;
          // if (l) {
          //   for (var i = 0; i < l.length; i++) {
          //     this.mobs.push(l[i].sigla + " - " + l[i].nome);
          //   }
          // }
          var d = response.data;
          if (d.sigla) {
            var sigla = d.sigla;
            if (d.mobs && d.mobs.length > 0 && d.mobs[0].sigla)
              sigla = d.mobs[0].sigla;
            this.mobs.push(sigla + " - " + d.descrDocumento);
          }
        },
        (error) => {
          UtilsBL.errormsg(error, this);
        }
      );
    },

    show: function(documentos, cont, emit, titulo, campo) {
      this.showModal = true;
      this.errormsg = undefined;
      this.documentos = documentos;
      this.mob = null;
      this.cont = cont;
      this.titulo = titulo;
      this.campo = campo;
      this.emit = emit;
    },

    cancel: function(e) {
      e.cancel();
    },

    save: function() {
      if ((this.mob || "") === "") {
        this.errormsg = "Documento deve ser informado.";
        return;
      }

      var mob = this.mob;
      if (mob) mob = mob.split(" - ")[0];

      if (this.emit)
        Bus.$emit(this.emit, this.documentos, this.cont, { mob: mob });
      this.$refs.modal.hide(true);
    },

    validar: function() {
      this.$nextTick(() => this.$validator.validateAll());
    },
  },
};
</script>
