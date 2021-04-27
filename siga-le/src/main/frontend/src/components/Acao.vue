<template>
  <button
    type="button"
    @click="clique()"
    class="btn btn-sm d-print-none mr-2 mb-2"
    :class="{ 'btn-primary': !!metodo, 'btn-light': !metodo }"
  >
    <span class="fa fa-paper-plane-o"></span>
    {{ nome }}
  </button>
</template>

<script>
import { Bus } from "../bl/bus.js";
import UtilsBL from "../bl/utils.js";

export default {
  props: {
    acao: { required: true },
  },
  computed: {
    nome() {
      return this.acao.nome.replace("_", "");
    },
    slug() {
      return UtilsBL.slugify(this.acao.nome.replace("_", "")).replace("-", "_");
    },
    metodo() {
      return this[this.slug];
    },
  },
  methods: {
    clique() {
      if (this.acao.msgConfirmacao) {
        Bus.$emit("confirmar", "Confirmação", this.acao.msgConfirmacao, () => {
          this.executar();
        });
      } else this.executar();
    },
    executar() {
      if (this.metodo) this.metodo();
      else
        Bus.$emit(
          "message",
          "Erro",
          "Não existe suporte para a ação " + this.slug
        );
    },

    emitir(operacao) {
      Bus.$emit(
        operacao,
        [{ codigo: this.$parent.numero, sigla: this.$parent.doc.sigla }],
        this.$parent.reler
      );
    },

    assinar() {
      Bus.$emit(
        "assinarComSenha",
        [{ codigo: this.$parent.numero, sigla: this.$parent.doc.sigla }],
        undefined,
        undefined,
        this.$parent.reler
      );
    },

    anotar: function() {
      this.emitir("iniciarAnotacao");
    },

    arq_corrente: function() {
      this.emitir("arquivarCorrente");
    },

    desarq_corrente: function() {
      this.emitir("desarquivarCorrente");
    },

    sobrestar: function() {
      this.emitir("sobrestar");
    },

    desobrestar: function() {
      this.emitir("dessobrestar");
    },

    tramitar: function() {
      this.emitir("iniciarTramite");
    },

    ver_impressao: function() {
      this.$parent.$parent.mostrarCompleto();
    },
  },
};
</script>
