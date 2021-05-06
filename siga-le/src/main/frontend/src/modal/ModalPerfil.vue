<template>
  <div>
    <validation-observer v-slot="{ invalid }">
      <b-modal
        id="cota"
        ref="modal"
        v-model="showModal"
        title="Definir Perfil"
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
                label="Perfil"
                id="perfil"
                name="perfil"
                v-model="idPerfil"
                validate="required"
                :disabled="false"
                :list="perfis"
                :edit="true"
                chave="idPerfil"
                descr="nome"
              ></my-select>
            </div>

            <div class="form-group col col-sm-6">
              <validation-provider
                rules="required"
                :immediate="true"
                v-slot="{ errors }"
              >
                <label class="control-label" for="tipo" style="width: 100%"
                  >Responsável</label
                >
                <select class="form-control" id="tipo" v-model="tipo">
                  <option value="lotacao">Lotação</option>
                  <option value="matricula">Pessoa</option>
                </select>
                <span
                  v-if="false"
                  v-show="errors.length > 0"
                  class="help is-danger"
                  >{{ errors[0] }}</span
                >
              </validation-provider>
            </div>
            <div class="form-group col col-sm-6" v-if="tipo === 'lotacao'">
              <validation-provider
                rules="required"
                :immediate="true"
                v-slot="{ errors }"
              >
                <my-lotacao v-model="lotacao" label="Sigla da Lotação" />
                <span
                  v-if="false"
                  v-show="errors.length > 0"
                  class="help is-danger"
                  >{{ errors[0] }}</span
                >
              </validation-provider>
            </div>
            <div class="form-group col col-sm-6" v-if="tipo === 'matricula'">
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
import UtilsBL from "../bl/utils.js";

export default {
  name: "definir-perfil",

  mounted() {},

  data() {
    return {
      showModal: false,
      errormsg: undefined,
      tipo: "lotacao",
      lotacao: null,
      matricula: null,
      documentos: undefined,
      item: "Monica",
      lotacoes: [],
      pessoas: [],
      idPerfil: undefined,
      perfis: [],
      template: ItemTemplate,
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
            "/perfis-disponiveis"
        )
        .then(
          (response) => {
            self.carregando = false;
            self.perfis.length = 0;
            var resp = response.data;
            for (var i = 0; i < resp.list.length; i++) {
              self.perfis.push(
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

    getLabelLotacao: function(item) {
      return item;
    },
    getLabelPessoa: function(item) {
      return item;
    },
    updatePessoas: function(text) {
      // yourGetItemsMethod(text).then((response) => {
      //   this.items = response
      // })
      if (!text || text === "") return;
      this.errormsg = undefined;
      this.$http.get("siga/api/v1/pessoas?texto=" + encodeURI(text)).then(
        (response) => {
          this.pessoas = [];
          var l = response.data.list;
          if (l) {
            for (var i = 0; i < l.length; i++) {
              this.pessoas.push(l[i].sigla + " - " + l[i].nome);
            }
          }
        },
        (error) => {
          UtilsBL.errormsg(error, this);
        }
      );
    },
    updateLotacoes: function(text) {
      // yourGetItemsMethod(text).then((response) => {
      //   this.items = response
      // })
      if (!text || text === "") return;
      this.errormsg = undefined;
      this.$http.get("siga/api/v1/lotacoes?texto=" + encodeURI(text)).then(
        (response) => {
          this.lotacoes = [];
          var l = response.data.list;
          if (l) {
            for (var i = 0; i < l.length; i++) {
              this.lotacoes.push(l[i].sigla + " - " + l[i].nome);
            }
          }
        },
        (error) => {
          UtilsBL.errormsg(error, this);
        }
      );
    },

    show: function(documentos, cont) {
      this.showModal = true;
      this.errormsg = undefined;
      this.documentos = documentos;
      this.matricula = null;
      this.lotacao = null;
      this.cont = cont;
      this.carregar();
    },

    cancel: function(e) {
      e.cancel();
    },

    save: function() {
      if (!this.idPerfil) {
        this.errormsg = "Perfil deve ser informado.";
        return;
      }
      if (this.tipo === "lotacao") {
        this.matricula = undefined;
        if ((this.lotacao || "") === "") {
          this.errormsg = "Lotação deve ser informada.";
          return;
        }
      }
      if (this.tipo === "matricula") {
        this.lotacao = undefined;
        if ((this.matricula || "") === "") {
          this.errormsg = "Matrícula deve ser informada.";
          return;
        }
      }

      var lotacao = this.lotacao;
      if (lotacao) lotacao = lotacao.split(" - ")[0];
      var matricula = this.matricula;
      if (matricula) matricula = matricula.split(" - ")[0];

      Bus.$emit("definirPerfil", this.documentos, this.cont, {
        idPerfil: this.idPerfil,
        lotacao: lotacao,
        matricula: matricula,
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
