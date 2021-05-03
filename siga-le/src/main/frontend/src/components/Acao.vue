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
      var s = UtilsBL.slugify(this.acao.nome.replace("_", "")).replace(
        "-",
        "_"
      );
      if (s.startsWith("desfazer_")) s = "desfazer";
      return s;
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

    emitir(operacao, params, callback) {
      Bus.$emit(
        operacao,
        [{ codigo: this.$parent.numero, sigla: this.$parent.doc.sigla }],
        callback ? callback : this.$parent.reler,
        params
      );
    },

    criar_via() {
      this.emitir("criarVia", undefined, (result) => {
        this.$router.push({
          name: "Documento",
          params: { numero: result.data.sigla.replace(/[^a-z0-9]/gi, "") },
        });
      });
    },

    finalizar() {
      this.emitir("finalizar", undefined, (result) => {
        this.$router.push({
          name: "Documento",
          params: { numero: result.data.sigla.replace(/[^a-z0-9]/gi, "") },
        });
      });
    },

    assinar() {
      this.emitir("assinarComSenha");
    },

    autenticar() {
      this.emitir("autenticarComSenha");
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
      this.$parent.mostrarCompleto();
    },

    juntar: function() {
      this.emitir("juntarModal");
    },

    vincular: function() {
      this.emitir("vincularModal");
    },

    apensar: function() {
      this.emitir("apensarModal");
    },

    desapensar: function() {
      this.emitir("desapensar");
    },

    cancelar: function() {
      if (this.acao.acao === "tornarDocumentoSemEfeito") {
        this.emitir("tornarSemEfeitoModal");
      }
    },

    incluir_copia: function() {
      this.emitir("incluirCopiaModal");
    },

    desfazer: function() {
      this.emitir("cancelarMovimentacao", { idMov: "-" });
    },

    definir_marcador: function() {
      this.emitir("definirMarcadorModal");
    },

    definir_perfil: function() {
      this.emitir("definirPerfilModal");
    },

    redefinir_acesso: function() {
      this.emitir("definirAcessoModal");
    },
  },
};
</script>
