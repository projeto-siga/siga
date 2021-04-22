<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-0">Criar Documento</h4>
      </div>
      <div class="col col-sm-12" v-if="errormsg">
        <p class="alert alert-danger"><strong>Erro!</strong> {{ errormsg }}</p>
      </div>
    </div>
    <div class="row pt-5" v-if="warningmsg">
      <div class="col col-sm-12">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> {{ warningmsg }}
        </p>
      </div>
    </div>
    <validation-observer v-slot="{ invalid }">
      <form>
        <div class="row">
          <div class="form-group col col-sm-12">
            <my-select
              label="Modelo"
              id="modelo"
              name="modelo"
              v-model="idModelo"
              validate="required"
              :disabled="false"
              :list="modelos"
              :edit="true"
              chave="idModelo"
              descr="nome"
              @change="carregarModelo()"
            ></my-select>
          </div>
        </div>
        <div class="row">
          <div class="form-group col col-sm-12 col-lg-6">
            <my-pessoa
              v-model="subscritor"
              label="Subscritor"
              name="subscritor"
            />
          </div>
          <div class="form-group col col-sm-4 col-lg-2">
            <validation-provider
              rules="required"
              :immediate="true"
              v-slot="{ errors }"
            >
              <label
                class="control-label"
                for="tipoDestinatario"
                style="width: 100%"
              >
                Destinatário
              </label>
              <select
                class="form-control"
                id="tipoDestinatario"
                v-model="tipoDestinatario"
              >
                <option value="LOTACAO">Lotação</option>
                <option value="PESSOA">Pessoa</option>
              </select>
              <span
                v-if="false"
                v-show="errors.length > 0"
                class="help is-danger"
                >{{ errors[0] }}</span
              >
            </validation-provider>
          </div>
          <div
            class="form-group col col-sm-8 col-lg-4"
            v-if="tipoDestinatario === 'LOTACAO'"
          >
            <my-lotacao
              v-model="lotaDestinatario"
              label="Lotação Destinatária"
            />
          </div>
          <div
            class="form-group col col-sm-8 col-lg-4"
            v-if="tipoDestinatario === 'PESSOA'"
          >
            <my-pessoa
              v-model="destinatario"
              label="Matrícula do Destinatário"
            />
          </div>
          <em
            v-if="errormsg &amp;&amp; errormsg !== ''"
            for="processos"
            class="invalid"
            >{{ errormsg }}</em
          >
        </div>
        <div class="form-group">
          <my-classificacao
            v-model="classificacao"
            label="Classificação Documental"
            name="classificacao"
          />
        </div>
        <div class="form-group">
          <label>Descrição</label>
          <textarea
            v-model="descricao"
            name="exDocumentoDTO.descrDocumento"
            cols="80"
            rows="2"
            id="descrDocumento"
            class="form-control"
          ></textarea>
          <small class="form-text text-muted"
            >Preencher o campo acima com palavras-chave, sempre usando
            substantivos, gênero masculino e singular.</small
          >
        </div>
      </form>
      <form ref="form">
        <documento-entrevista :entrevista="entrevistaTemplate" />
      </form>
      <div style="display: none;" ref="divEntrevista" />
      <b-button
        class="mt-4"
        variant="primary"
        @click.prevent="salvar()"
        accesskey="O"
        ><u>O</u>K</b-button
      >
    </validation-observer>
  </div>
</template>

<script>
import DocumentoEntrevista from "./DocumentoEntrevista";
import UtilsBL from "../bl/utils.js";
import EntrevistaBL from "../bl/entrevista.js";
import { Bus } from "../bl/bus.js";

export default {
  name: "documento-editar",
  mounted() {
    var self = this;

    this.$on("filtrar", (texto) => {
      this.filtrarMovimentos(texto);
    });
    this.$nextTick(function() {
      this.carregarModelos();
    });

    window.sbmt = function() {
      self.processarEntrevista();
    };
  },
  data() {
    return {
      tipoDestinatario: "LOTACAO",
      modelos: [],
      modelo: undefined,
      idModelo: undefined,
      entrevista: undefined,
      entrevistaTemplate: undefined,
      subscritor: undefined,
      destinatario: undefined,
      lotaDestinatario: undefined,
      classificacao: undefined,
      descricao: undefined,
      nivelacesso: "PUBLICO",
    };
  },
  watch: {
    "$route.params.numero": function() {
      this.carregarDocumento(this.$route.params.numero);
    },
  },
  computed: {
    siglaSubscritor() {
      if (!this.subscritor) return undefined;
      return this.subscritor.split(" - ")[0];
    },
    siglaDestinatario() {
      if (!this.destinatario || this.tipoDestinatario !== "PESSOA")
        return undefined;
      return this.destinatario.split(" - ")[0];
    },
    siglaLotaDestinatario() {
      if (!this.lotaDestinatario || this.tipoDestinatario !== "LOTACAO")
        return undefined;
      return this.lotaDestinatario.split(" - ")[0];
    },
    siglaClassificacao() {
      if (!this.classificacao) return undefined;
      return this.classificacao.split(" - ")[0];
    },
  },
  methods: {
    executar: function(mov, acao) {
      if (acao.acao === "exibir") {
        this.$router.push({
          name: "Documento",
          params: { numero: acao.params.sigla.replace(/[^a-z0-9]/gi, "") },
        });
      }
    },

    carregarModelos: function() {
      this.errormsg = undefined;
      Bus.$emit("block", 20);
      this.$http.get("sigaex/api/v1/modelos").then(
        (response) => {
          Bus.$emit("release");
          this.modelos = response.data.list;
        },
        (error) => {
          Bus.$emit("release");
          UtilsBL.errormsg(error, this);
        }
      );
    },

    carregarModelo: function() {
      this.errormsg = undefined;
      Bus.$emit("block", 20);
      this.$http.get("sigaex/api/v1/modelos/" + this.idModelo).then(
        (response) => {
          Bus.$emit("release");
          this.modelo = response.data;
        },
        (error) => {
          Bus.$emit("release");
          UtilsBL.errormsg(error, this);
        }
      );
      this.processarEntrevista();
    },

    processarEntrevista: function() {
      this.errormsg = undefined;
      var formParams = EntrevistaBL.encodeFormParams(
        EntrevistaBL.getFormResults(this.$refs.form)
      );
      // console.log(formParams);
      // console.log(s);
      this.$http
        .post(
          "sigaex/api/v1/modelos/" + this.idModelo + "/processar-entrevista",
          { entrevista: formParams },
          { block: true }
        )
        .then(
          (response) => {
            this.entrevista = EntrevistaBL.fix(response.body);
            this.$refs.divEntrevista.innerHTML = this.entrevista;
            this.entrevistaTemplate = this.$refs.divEntrevista.innerHTML;
          },
          (error) => {
            UtilsBL.errormsg(error, this);
          }
        );
    },

    salvar: function() {
      this.errormsg = undefined;
      var formParams = EntrevistaBL.encodeFormParams(
        EntrevistaBL.getFormResults(this.$refs.form)
      );
      // console.log(formParams);
      // console.log(s);
      this.$http
        .post(
          "sigaex/api/v1/documentos",
          {
            modelo: this.idModelo,
            subscritor: this.siglaSubscritor,
            eletronico: true,
            descricaotipodoc: "Interno Produzido",
            classificacao: this.classificacao,
            pessoadestinatario: this.siglaDestinatario,
            lotadestinatario: this.siglaLotaDestinatario,
            descricaodocumento: this.descricao,
            nivelacesso: this.nivelacesso,
            entrevista: formParams,
          },
          { block: true }
        )
        .then(
          (response) => {
            this.$router.replace({
              name: "Documento",
              params: { numero: response.data.sigladoc },
            });
          },
          (error) => {
            UtilsBL.errormsg(error, this);
          }
        );
    },
  },

  components: { DocumentoEntrevista },
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped></style>
