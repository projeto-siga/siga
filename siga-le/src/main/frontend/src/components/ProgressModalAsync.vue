<template>
  <div>
    <b-modal
      id="progressModal"
      v-model="showModal"
      :title="title"
      no-close-on-backdrop
      ok-title="Cancelar"
      ok-only
      hide-header-close
      no-close-on-esc
    >
      <p class="mb-1">{{ progressbarCaption }}</p>
      <div class="progress">
        <div
          id="progressbar-ad"
          class="progress-bar"
          role="progressbar"
          aria-valuenow="{progressbarWidth}"
          aria-valuemin="0"
          aria-valuemax="100"
          :style="{ width: progressbarWidth + '%' }"
        >
          <span class="sr-only"></span>
        </div>
      </div>
      <div v-if="bytes">
        <span class="float-right mt-1">{{ bytes }}</span>
      </div>
    </b-modal>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";

export default {
  name: "progressBarAsync",

  data() {
    return {
      showModal: false,
      i: 0,
      title: undefined,
      caption: undefined,
      callbackEnd: function() {},
      progressbarWidth: 0,
      progressbarCaption: undefined,
      bytes: undefined
    };
  },

  methods: {
    start: function(title, key, callbackEnd) {
      this.title = title;
      this.key = key;
      this.callbackEnd = callbackEnd;

      this.showModal = true;
      this.refresh();
    },

    cancel: function() {
      this.showModal = false;
    },

    refresh: function() {
      if (!this.showModal) return;
      this.$http.get("sigaex/api/v1/status/" + this.key).then(
        response => {
          var r = response.data;
          this.progressbarCaption = r.mensagem;
          this.progressbarWidth = 100 * (r.indice / r.contador);
          this.bytes = r.bytes ? UtilsBL.formatBytes(r.bytes) : undefined;
          if (r.indice === r.contador) {
            this.showModal = false;
            this.progressbarWidth = 0;
            this.callbackEnd(this.i);
          } else {
            setTimeout(() => {
              this.refresh();
            }, 500);
          }
        },
        error => {
          this.errormsg =
            error.data.errormsg || "Erro obtendo informações de status";
        }
      );
    }
  }
};
</script>
