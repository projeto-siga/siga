<template>
  <div class="container-fluid content profile">
    <div class="row xd-print-block mt-3 mb-3">
      <div class="col-12">
        <h4 class="text-center mb-0">Dossiê {{ sigla }}</h4>
      </div>
    </div>
    <div class="row justify-content-center mt-3 mb-3">
      <div class="col-12">
        <b-form-radio-group
          class="text-center"
          id="radio-group-2"
          v-model="tipo"
          name="radio-sub-component"
        >
          <b-form-radio value="html">HTML</b-form-radio>
          <b-form-radio value="pdf"
            >PDF -
            <a id="pdflink" accesskey="a" :href="urlAbrirPdf" target="_blank">
              <u>a</u>brir</a
            ></b-form-radio
          >
          <b-form-radio value="pdfSemMarcas"
            >PDF sem Marcas -
            <a
              id="pdflink"
              accesskey="r"
              :href="urlAbrirPdfSemMarcas"
              target="_blank"
            >
              abri<u>r</u></a
            ></b-form-radio
          >
        </b-form-radio-group>
      </div>
    </div>
    <div class="row">
      <div class="col col-12 col-md-4">
        <table class="table table-sm table-striped">
          <thead>
            <td>Documento</td>
            <td style="text-align: center">Origem</td>
            <td style="text-align: right">Pág.</td>
          </thead>
          <tbody>
            <tr
              v-for="i in lista"
              :key="i.paginaInicial"
              @click.prevent="show(i)"
            >
              <td :style="{ 'padding-left': i.nivel + '.4em' }">
                {{ i.descr }}
              </td>
              <td style="text-align: center">{{ i.origem }}</td>
              <td style="text-align: right">{{ i.paginaInicial }}</td>
            </tr>
          </tbody>
        </table>
        <router-link
          class="btn btn-secondary mb-4"
          :to="{ name: 'Documento', params: { numero: numero } }"
          >Voltar</router-link
        >
      </div>

      <div id="right-col" class="col col-12 col-md-8">
        <div
          id="paipainel"
          style="margin: 0px; padding: 0px; border: 0px solid black; clear: both; overflow: hidden;"
        >
          <div ref="html" v-html="html"></div>
          <my-iframe v-if="src" :src="src"></my-iframe>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";
import { Bus } from "../bl/bus.js";
import Acao from "./Acao";

export default {
  name: "dossie",
  mounted() {
    this.$on("filtrar", (texto) => {
      this.filtrarMovimentos(texto);
    });
    this.$nextTick(function() {
      this.carregarDossie(this.$route.params.numero);
    });
  },
  data() {
    return {
      fixed: undefined,
      modified: undefined,
      numero: undefined,
      numeroAtivo: undefined,
      completo: undefined,
      sigla: undefined,
      lista: undefined,
      tipo: "html",
      html: undefined,
      src: undefined,
    };
  },
  watch: {
    "$route.params.numero": function() {
      this.carregarDossie(this.$route.params.numero);
    },
    tipo: function() {
      this.mostrar();
    },
  },
  computed: {
    urlAbrirPdf() {
      return (
        this.$http.options.root +
        "sigaex/app/arquivo/exibir?idVisualizacao=&arquivo=" +
        this.numeroAtivo +
        ".pdf" +
        (this.completo ? "&completo=1" : "")
      );
    },
    urlAbrirPdfSemMarcas() {
      return (
        this.$http.options.root +
        "sigaex/app/arquivo/exibir?idVisualizacao=&arquivo=" +
        this.numeroAtivo +
        ".pdf&semmarcas=1" +
        (this.completo ? "&completo=1" : "")
      );
    },
  },
  methods: {
    carregarDossie: function() {
      this.errormsg = undefined;
      this.numero = this.$route.params.numero;
      this.numeroAtivo = this.numero;
      this.html = undefined;
      this.sigla = this.$route.params.sigla
        ? this.$route.params.sigla
        : this.numero;
      // Validar o número do processo
      Bus.$emit("block", 20);
      this.$http
        .get(
          "sigaex/api/v1/documentos/" +
            UtilsBL.onlyLettersAndNumbers(this.numero) +
            "/dossie"
        )
        .then(
          (response) => {
            Bus.$emit("release");
            this.lista = response.data.list;
            this.lista.push({
              descr: "COMPLETO",
              mobil: this.numero,
              completo: true,
            });
            if (/\dV\d/gm.exec(this.numero) !== null)
              this.lista.push({
                descr: "VOLUMES",
                mobil: this.numero,
                completo: true,
                volumes: true,
              });
            this.show(this.lista[this.lista.length - 1]);
          },
          (error) => {
            Bus.$emit("release");
            UtilsBL.errormsg(error, this);
          }
        );
    },

    mostrar() {
      if (this.completo) this.mostrarCompleto();
      else this.mostrarSimples();
    },

    mostrarSimples: function() {
      var url =
        this.$http.options.root +
        "sigaex/api/v1/documentos/" +
        this.numeroAtivo +
        "/arquivo/produzir?estampa=" +
        (this.tipo !== "pdfSemMarcas") +
        (this.completo ? "&completo=true" : "") +
        "&contenttype=" +
        (this.tipo === "html" ? "text/html" : "application/pdf");

      if (this.tipo === "html") {
        this.src = undefined;
        this.$http.get(url).then(
          (response) => {
            this.html = response.body;
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
      } else {
        this.src = url;
        this.html = undefined;
      }
    },

    mostrarCompleto: function() {
      this.$http
        .get(
          "sigaex/api/v1/documentos/" +
            this.numeroAtivo +
            "/arquivo?estampa=" +
            (this.tipo !== "pdfSemMarcas") +
            (this.completo ? "&completo=true" : "") +
            (this.volumes ? "&volumes=true" : "") +
            "&contenttype=" +
            (this.tipo === "html" ? "text/html" : "application/pdf")
        )
        .then(
          (response) => {
            Bus.$emit(
              "prgAsyncStart",
              "PDF Completo",
              response.data.uuid,
              () => {
                var jwt = response.data.jwt;
                var url =
                  this.$http.options.root +
                  "sigaex/api/v1/download/" +
                  jwt +
                  "/" +
                  this.numero +
                  (this.tipo === "html"
                    ? ".html"
                    : this.tipo === "pdf"
                    ? ".pdf"
                    : ".pdf");

                if (this.tipo === "html") {
                  this.src = undefined;
                  this.$http.get(url).then(
                    (response) => {
                      this.html = response.body;
                    },
                    (error) => {
                      Bus.$emit("message", "Erro", error.data.errormsg);
                    }
                  );
                } else {
                  this.src = url;
                  this.html = undefined;
                }
              }
            );
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },

    show(i) {
      this.numeroAtivo = i.mobil;
      this.completo = !!i.completo;
      this.volumes = !!i.volumes;
      this.mostrar();
    },
  },

  components: {
    acao: Acao,
  },
};
</script>
