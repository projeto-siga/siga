<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-0">Quadro Quantitativo</h4>
      </div>
    </div>

    <div class="row mt-3">
      <div class="col-12 col-md-6">
        <quadro-tabela
          titulo="Expedientes"
          :lista="listaExpediente"
          :carregando="carregandoExpediente"
          :primeiraCarga="primeiraCarga"
          filtroExpedienteProcesso="Expediente"
        />

        <quadro-tabela
          titulo="Processos Administrativos"
          :lista="listaProcesso"
          :carregando="carregandoProcesso"
          :primeiraCarga="primeiraCarga"
          filtroExpedienteProcesso="Processo"
        />

        <div class="row d-print-none">
          <div class="col col-auto ml-auto">
            <button
              type="button"
              @click="novoDocumento()"
              class="btn btn-primary"
              title=""
            >
              <span class="fa fa-sticky-note-o d-none d-md-inline"></span>
              Criar Documento
            </button>
          </div>
        </div>
      </div>
    </div>
    <p
      class="alert alert-success"
      v-if="acessos &amp;&amp; acessos.length >= 1"
    >
      Último acesso em {{ acessos[1].datahora }} no endereço
      {{ acessos[1].ip }}.
    </p>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";
import QuadroTabela from "./QuadroTabela";

export default {
  components: { quadroTabela: QuadroTabela },

  mounted() {
    this.errormsg = undefined;
    console.log("mesa-mounted");

    setTimeout(() => {
      this.carregarQuadro("Expediente");
      this.carregarQuadro("Processo");
      if (this.$route.params.exibirAcessoAnterior) this.carregarAcessos();
    });
  },

  data() {
    return {
      mesa: undefined,
      filtro: undefined,
      listaExpediente: window.quadroExpediente ? window.quadroExpediente : [],
      listaProcesso: window.quadroProcesso ? window.quadroProcesso : [],
      primeiraCarga: !!window.quadroExpediente || !!window.quadroProcesso,
      todos: {},
      carregandoExpediente: false,
      carregandoProcesso: false,
      acessos: [],
      errormsg: undefined,
    };
  },

  computed: {},

  methods: {
    carregarQuadro: function(tipo) {
      this["carregando" + tipo] = true;
      var erros = {};
      if (this.lista && this.lista.length > 0) {
        for (var i = 0; i < this.lista.length; i++) {
          erros[this.lista[i].codigo] = this.lista[i].errormsg;
        }
      }
      this.$http
        .get("sigaex/api/v1/quadro?filtroExpedienteProcesso=" + tipo, {
          block: true,
        })
        .then(
          (response) => {
            this["carregando" + tipo] = false;
            this["lista" + tipo] = response.data.list.filter(
              (i) =>
                i.marcadorId != 9 &&
                i.marcadorId != 8 &&
                i.marcadorId != 10 &&
                i.marcadorId != 11 &&
                i.marcadorId != 12 &&
                i.marcadorId != 13 &&
                i.marcadorId != 16 &&
                i.marcadorId != 18 &&
                i.marcadorId != 20 &&
                i.marcadorId != 21 &&
                i.marcadorId != 22 &&
                i.marcadorId != 26 &&
                i.marcadorId != 32 &&
                i.marcadorId != 62 &&
                i.marcadorId != 63 &&
                i.marcadorId != 64 &&
                i.marcadorId != 7 &&
                i.marcadorId != 50 &&
                i.marcadorId != 51
            );
            this.primeiraCarga = false;
            window["quadro" + tipo] = this["lista" + tipo];
          },
          (error) => {
            this["carregando" + tipo] = false;
            UtilsBL.errormsg(error, this);
          }
        );
    },

    carregarAcessos: function() {
      this.acessos.length = 0;
      this.$http.get("siga/api/v1/acessos").then(
        (response) => {
          var list = response.data.list;
          for (var i = 0; i < list.length; i++) {
            list[i].datahora = UtilsBL.formatJSDDMMYYYY_AS_HHMM(
              list[i].datahora
            );
            this.acessos.push(list[i]);
          }
        },
        (error) => {
          UtilsBL.errormsg(error, this);
        }
      );
    },

    novoDocumento: function() {
      this.$router.push({ name: "DocumentoNovo" });
    },

    listar: function(item, pessoa) {
      this.$router.push({
        name: "Lista",
        pessoa: !!pessoa,
        lotacao: !pessoa,
      });
    },
  },
};
</script>
