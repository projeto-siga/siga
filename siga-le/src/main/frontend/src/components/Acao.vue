<template>
  <button
    type="button"
    @click="clique()"
    class="btn btn-sm d-print-none mr-2 mb-2"
    :class="{ 'btn-primary': !!metodo, 'btn-light': !metodo }"
  >
    <img
      :src="
        $http.options.root + '/siga/css/famfamfam/icons/' + acao.icone + '.png'
      "
      width="16px"
      height="16px"
      class="mr-1 mb-1"
      title=""
    />
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

    numero() {
      return UtilsBL.onlyLettersAndNumbers(this.$parent.numero);
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
          "Não existe suporte para a ação '" + this.slug + "'"
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

    voltarParaDocumento() {
      this.$router.push({
        name: "Documento",
        params: {
          numero: UtilsBL.onlyLettersAndNumbers(this.$parent.mob.sigla),
        },
      });
    },

    irParaDocumento(result) {
      this.$router.push({
        name: "Documento",
        params: { numero: UtilsBL.onlyLettersAndNumbers(result.data.sigla) },
      });
    },

    assinar_anexo() {
      this.emitir(
        "assinarComSenhaMovimentacao",
        {
          idMov: this.$parent.mov.idMov,
        },
        this.voltarParaDocumento
      );
    },

    autenticar_anexo() {
      this.emitir(
        "autenticarComSenhaMovimentacao",
        {
          idMov: this.$parent.mov.idMov,
        },
        this.voltarParaDocumento
      );
    },

    excluir_anexo() {
      this.emitir(
        "excluirMovimentacao",
        {
          idMov: this.$parent.mov.idMov,
        },
        this.voltarParaDocumento
      );
    },

    cancelar_anexo() {
      this.emitir(
        "cancelarMovimentacao",
        {
          idMov: this.$parent.mov.idMov,
        },
        this.voltarParaDocumento
      );
    },

    criar_via() {
      this.emitir("criarVia", undefined, this.irParaDocumento);
    },

    editar() {
      this.$router.push({
        name: "DocumentoEditar",
        params: { numero: this.numero },
      });
    },

    incluir_documento() {
      this.$router.push({
        name: "DocumentoNovo",
        params: { siglaMobilPai: this.$parent.mob.sigla },
      });
    },

    autuar() {
      this.$router.push({
        name: "DocumentoNovo",
        params: { siglaMobilFilho: this.$parent.mob.sigla },
      });
    },

    ver_dossie() {
      this.$router.push({
        name: "DocumentoDossie",
        params: {
          numero: UtilsBL.onlyLettersAndNumbers(this.$parent.mob.sigla),
          sigla: this.$parent.mob.sigla,
        },
      });
    },

    excluir() {
      this.emitir("excluir", undefined, () => {
        this.$router.push({
          name: "Home",
        });
      });
    },

    finalizar() {
      this.emitir("finalizar", undefined, this.irParaDocumento);
    },

    duplicar() {
      this.emitir("duplicar", undefined, this.irParaDocumento);
    },

    refazer() {
      this.emitir("refazer", undefined, this.irParaDocumento);
    },

    receber() {
      this.emitir("receber");
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

    desentranhar: function() {
      this.emitir("desentranharModal");
    },

    anexar: function() {
      this.emitir("anexarModal");
    },

    incluir_cossignatario: function() {
      this.emitir("incluirCossignatarioModal");
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
