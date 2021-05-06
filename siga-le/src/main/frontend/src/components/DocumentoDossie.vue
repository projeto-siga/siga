<template>
  <div class="container-fluid content profile">
    <div class="row xd-print-block mt-3 mb-3">
      <div class="col-md-12">
        <h4 class="text-center mb-0">Dossiê {{ sigla }}</h4>
      </div>
    </div>
    <div class="mt-3 mb-3">
      <a
        id="visualizar-movimentacoes"
        class="once btn btn-sm btn-info text-white link-tag"
        accesskey="m"
        href="/sigaex/app/expediente/doc/exibir?sigla=TRF2-PES-201901238-V01"
        title=""
        ><img
          src="/siga/css/famfamfam/icons/application_view_list.png"
          class="mr-1 mb-1"
          title=""
        />Visualizar&nbsp;<u>M</u>ovimentações</a
      >

      <span class="pl-2"></span>
      <button
        type="button"
        class="link-btn btn btn-secondary btn-sm align-center"
        id="TelaCheia"
        data-toggle="button"
        aria-pressed="false"
        autocomplete="off"
        accesskey="t"
        onclick="javascript: telaCheia(this);"
      >
        <u>T</u>ela Cheia
      </button>
      <span class="pl-2"></span>
      <div class="d-inline-block align-center mb-2 mt-2">
        <img
          src="/siga/css/famfamfam/icons/wrench.png"
          class="mr-1 mb-1"
          title=""
        />Preferência:

        <span class="pl-2"></span>
        <span style="white-space: nowrap;">
          <input
            type="radio"
            id="radioHTML"
            name="formato"
            value="html"
            accesskey="h"
            checked="checked"
            onclick="exibir(htmlAtual,pdfAtual,'');"
          />
          <u>H</u>TML&nbsp;
        </span>
        <span class="pl-2"></span>
        <span style="white-space: nowrap;">
          <input
            type="radio"
            id="radioPDF"
            name="formato"
            value="pdf"
            accesskey="p"
            onclick="exibir(htmlAtual,pdfAtual,'');"
          />
          <u>P</u>DF -
          <a
            id="pdflink"
            accesskey="a"
            href="/sigaex/app/arquivo/exibir?idVisualizacao=&amp;arquivo=TRF2PES201901238V01.pdf&amp;completo=1"
          >
            <u>a</u>brir</a
          >
        </span>
        <span class="pl-2"></span>
        <span style="white-space: nowrap;">
          <input
            type="radio"
            id="radioPDFSemMarcas"
            name="formato"
            accesskey="s"
            value="pdfsemmarcas"
            onclick="exibir(htmlAtual,pdfAtual,'semmarcas/');"
          />
          PDF <u>s</u>em marcas -
          <a
            id="pdfsemmarcaslink"
            accesskey="b"
            href="/sigaex/app/arquivo/exibir?idVisualizacao=&amp;arquivo=TRF2PES201901238V01.pdf&amp;completo=1&amp;semmarcas=1"
          >
            a<u>b</u>rir</a
          >
        </span>
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
      sigla: undefined,
      lista: undefined,
      orgao: undefined,
      perfil: undefined,
      gui: {},
      filtro: undefined,
      errormsg: undefined,
      warningmsg: undefined,
      partes: false,
      dadosComplementares: false,
      doc: undefined,
      mob: undefined,
      marcadores: [],
      marcasativas: true,
      notas: false,
      tramitacao: undefined,
    };
  },
  watch: {
    "$route.params.numero": function() {
      this.carregarDossie(this.$route.params.numero);
    },
  },
  computed: {},
  methods: {
    carregarDossie: function() {
      this.errormsg = undefined;
      this.numero = this.$route.params.numero;
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
          },
          (error) => {
            Bus.$emit("release");
            UtilsBL.errormsg(error, this);
          }
        );
    },

    // show(i) {
    //   "http://localhost:8080/sigaex/app/arquivo/exibir?idVisualizacao=&arquivo=ZZRHU202100001V01.html&completo=1";
    // },
  },

  components: {
    acao: Acao,
  },
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
.marca-ref:hover,
.marca-ref:link,
.marca-ref:visited,
.marca-ref:active {
  color: black;
}

.inquebravel {
  white-space: nowrap;
}

.marca {
  padding-left: 0rem;
  padding-right: 0rem;
  margin-right: 0.5rem;
  margin-bottom: 0;
  margin-top: 0rem;
}

.marca-yellow {
  background-color: yellow;
}

.marca-blue {
  background-color: #41f1f4;
}

.marca-green {
  background-color: #00ff00;
}

.marca-pink {
  background-color: #faf;
}

.red {
  color: red;
}

.protocolado {
  color: green;
}

.odd {
  background-color: rgba(0, 0, 0, 0.05);
}

.card-consulta-processual div p b {
  color: #fff;
}

.card-consulta-processual div p {
  margin-bottom: 0.5rem;
}

.card-consulta-processual div i {
  line-height: 3rem;
  height: 3rem;
  color: #fff;
  float: right;
  font-size: 4rem;
  margin: 0rem -0.5rem 0rem 0rem;
}

.card-text-descr {
  margin-bottom: 0;
}

textarea {
  border: none;
  background: none;
  width: 100%;
  resize: none;
  overflow: hidden;
  min-height: 50px;
}

table.mov tr.despachox {
  background-color: rgb(240, 255, 240);
}

table.mov tr.juntadax,
tr.desentranhamentox {
  background-color: rgb(229, 240, 255);
}

table.mov tr.anotacaox {
  background-color: rgb(255, 255, 255);
}

table.mov tr.anexacaox {
  background-color: rgb(255, 255, 215);
}

table.mov tr.encerramento_volumex {
  background-color: rgb(255, 218, 218);
}

.card-body p {
  margin-bottom: 0.2em;
}

.card-body div {
  margin-top: 1em;
}

.card-body div h6 {
  margin-bottom: 0.2em;
}
</style>
