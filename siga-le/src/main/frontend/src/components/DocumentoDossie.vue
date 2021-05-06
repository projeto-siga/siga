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
            <tr v-for="i in lista" :key="i.paginaInicial">
              <td :style="{'padding-left': i.nivel + '.4em'}">{{ i.descr }}</td>
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

    reler: function() {
      console.log("relendo");
      this.$http
        .get("sigaex/api/v1/documentos/" + this.numero, { block: true })
        .then(
          (response) => {
            this.atualizarDocumento(response.data);
          },
          (error) => {
            UtilsBL.errormsg(error, this);
          }
        );
    },

    getMarcadores: function() {
      // Carregar os marcadores da classe
      this.$http
        .get(
          "classe/" + this.proc.dadosBasicos.classeProcessual + "/marcadores"
        )
        .then(
          (response) => {
            if (!response.data.list) return;
            for (var i = 0; i < response.data.list.length; i++) {
              this.marcadores.push(response.data.list[i].texto);
            }
          },
          (error) => {
            if (error.data.errormsg === "disabled") {
              this.marcasativas = false;
              return;
            }
            UtilsBL.errormsg(error, this);
          }
        );
    },
    getMarcas: function() {
      // Carregar os marcadores da classe
      this.$http
        .get("processo/" + this.numero + "/marcas?orgao=" + this.orgao)
        .then(
          (response) => {
            // if (!response.data.list) return
            for (var i = 0; i < response.data.list.length; i++) {
              var marca = response.data.list[i];
              for (var j = 0; j < this.fixed.movdoc.length; j++) {
                var movdoc = this.fixed.movdoc[j];
                if (movdoc.doc && movdoc.doc.idDocumento === marca.idpeca) {
                  movdoc.marca.push({
                    idmarca: marca.idmarca,
                    texto: marca.texto,
                    idestilo: marca.idestilo,
                    paginicial: marca.paginicial,
                    pagfinal: marca.pagfinal,
                  });
                }
              }
            }
          },
          (error) => {
            if (error.data.errormsg === "disabled") {
              this.marcasativas = false;
              return;
            }
            UtilsBL.errormsg(error, this);
          }
        );
    },
    mostrarDadosComplementares: function(ativo) {
      this.$parent.$emit("setting", "mostrarDadosComplementares", ativo);
    },
    mostrarProcessosRelacionados: function(ativo) {
      this.$parent.$emit("setting", "mostrarProcessosRelacionados", ativo);
    },
    mostrarPeca: function(idDocumento, disposition) {
      this.$http
        .get(
          "processo/" +
            this.numero +
            "/peca/" +
            idDocumento +
            "/pdf?orgao=" +
            this.orgao
        )
        .then(
          (response) => {
            var jwt = response.data.jwt;
            var url =
              this.$http.options.root +
              "/download/" +
              jwt +
              "/" +
              this.numero +
              "-peca-" +
              idDocumento +
              ".pdf";
            if (disposition) window.location = url + "?disposition=attachment";
            else window.open(url);
            UtilsBL.logEvento("consulta-processual", "mostrar pdf peça");
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },
    mostrarCompleto: function() {
      this.$http
        .get(
          "sigaex/api/v1/documentos/" +
            this.numero +
            "/arquivo?estampa=true&completo=true"
        )
        .then(
          (response) => {
            Bus.$emit(
              "prgAsyncStart",
              "PDF Completo",
              response.data.uuid,
              () => {
                var jwt = response.data.jwt;
                window.open(
                  this.$http.options.root +
                    "sigaex/api/v1//download/" +
                    jwt +
                    "/" +
                    this.numero +
                    ".pdf"
                );
              }
            );
            UtilsBL.logEvento(
              "consulta-processual",
              "mostrar pdf completo",
              "individual"
            );
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },
    filtrarMovimentos: function(texto) {
      this.$parent.$emit("setting", "filtrarMovimentos", texto !== undefined);
      var f = this.filtro;
      if (texto) {
        if (
          texto.length > 0 &&
          texto.substring(0, 1) === "#" &&
          f &&
          f.length > 0 &&
          f.substring(0, 1) === "#"
        ) {
          this.filtro = f + " " + texto;
          return;
        }
      }
      this.filtro = texto;
      this.$nextTick(() => this.$refs.filtro.focus());
    },
    mostrarPartes: function(ativo) {
      this.$parent.$emit("setting", "mostrarPartes", ativo);
    },
    imprimir: function() {
      window.print();
    },
    formatDDMMYYYHHMM: function(s) {
      if (s === undefined) {
        return;
      }
      var r =
        s.substring(6, 8) +
        "/" +
        s.substring(4, 6) +
        "/" +
        s.substring(0, 4) +
        " " +
        s.substring(8, 10) +
        ":" +
        s.substring(10, 12);
      r = r.replace(" ", "&nbsp;");
      return r;
    },

    exibirProcessoPecaDetalhes: function(movdoc, marca) {
      this.currentMovDoc = movdoc;
      this.currentMarca = marca;
      this.$refs.processoPecaDetalhes.show(
        marca,
        this.marcadores,
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaInicial
          : undefined,
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaFinal
          : undefined
      );
    },

    cotar: function() {
      this.$refs.Assinatura.show();
    },

    cotaEnviada: function(msg) {
      Bus.$emit("message", "Sucesso", "Cota enviada com sucesso. " + msg);
    },

    cotaNaoEnviada: function(msg, texto) {
      Bus.$emit(
        "message",
        "Erro",
        'Não foi possível enviar a cota "' +
          texto +
          '". Ocorreu o erro: "' +
          msg +
          '"'
      );
    },

    salvarProcessoPecaDetalhes: function(marca) {
      if (!this.currentMovDoc) return;

      var movdoc = this.currentMovDoc;
      var inicial =
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaInicial
          : undefined;
      var final =
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaFinal
          : undefined;
      if (inicial === marca.paginicial && final === marca.pagfinal) {
        marca.paginicial = undefined;
        marca.pagfinal = undefined;
      }

      var data = {
        idclasse: this.proc.dadosBasicos.classeProcessual,
        idmarca: this.currentMarca ? this.currentMarca.idmarca : undefined,
        texto: marca.texto,
        idestilo: marca.idestilo,
        paginicial: marca.paginicial,
        pagfinal: marca.pagfinal,
      };

      this.$http
        .post(
          "processo/" +
            this.numero +
            "/peca/" +
            this.currentMovDoc.doc.idDocumento +
            "/marca?orgao=" +
            this.orgao,
          data,
          { block: true }
        )
        .then(
          (response) => {
            if (this.currentMarca) {
              var index = this.currentMovDoc.marca.indexOf(this.currentMarca);
              UtilsBL.overrideProperties(marca, response.data.marca);
              UtilsBL.overrideProperties(
                this.currentMovDoc.marca[index],
                marca
              );
            } else {
              UtilsBL.overrideProperties(marca, response.data.marca);
              this.currentMovDoc.marca.push(marca);
            }
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },

    excluirProcessoPecaDetalhes: function() {
      if (!this.currentMovDoc || !this.currentMarca) return;

      this.$http
        .delete("marca/" + this.currentMarca.idmarca, { block: true })
        .then(
          () => {
            var index = this.currentMovDoc.marca.indexOf(this.currentMarca);
            if (index > -1) this.currentMovDoc.marca.splice(index, 1);
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },

    favoritar: function(favorito) {
      this.errormsg = undefined;
      this.$http
        .post(
          "processo/" + this.numero + "/sinalizar",
          { favorito: favorito },
          { block: true }
        )
        .then(
          (response) => {
            var d = response.data;
            this.favorito = d.processo.favorito;
          },
          (error) => {
            this.warningmsg = error.data.errormsg;
          }
        );
    },

    mostrarNotas: function(show) {
      this.$parent.$emit("setting", "mostrarNotas", show);

      this.$nextTick(() => {
        this.$refs.notaUnidade.focus();
        this.notasAlteradas();
      });
    },

    notasAlteradas: function() {
      if (this.notaUnidade !== undefined && this.notaUnidade.trim() === "") {
        this.notaUnidade = undefined;
      }
      if (this.notaPessoal !== undefined && this.notaPessoal.trim() === "") {
        this.notaPessoal = undefined;
      }
      this.$refs.notaUnidade.style.height = "5px";
      this.$refs.notaPessoal.style.height = "5px";
      var h = Math.max(
        this.$refs.notaUnidade.scrollHeight,
        this.$refs.notaPessoal.scrollHeight
      );
      this.$refs.notaUnidade.style.height = h + "px";
      this.$refs.notaPessoal.style.height = h + "px";
    },
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
