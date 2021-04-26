<template>
  <div>
    <button
      type="button"
      @click="tramitar()"
      class="btn btn-secondary d-print-none"
    >
      <span class="fa fa-paper-plane-o"></span>
      {{ nome }}
    </button>
  </div>
</template>

<script>
export default {
  props: {
    acao: { required: true },
  },
  computed: {
    nome() {
      return this.acao.nome.replace("_", "");
    },
  },
};
</script>
