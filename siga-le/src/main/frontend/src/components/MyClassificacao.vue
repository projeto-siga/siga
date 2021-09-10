<template>
  <validation-provider rules="required" :immediate="true" v-slot="{ errors }">
    <label class="control-label" for="matricula" style="width: 100%">{{
      label
    }}</label>
    <v-autocomplete
      :name="name"
      v-bind:value="value"
      v-on:input="$emit('input', $event)"
      v-on:change="$emit('change', $event)"
      :items="classificacoes"
      :get-label="getLabelClassificacao"
      :component-item="template"
      @update-items="updateClassificacoes"
      input-class="form-control"
    ></v-autocomplete>
    <span v-if="false" v-show="errors.length > 0" class="help is-danger">{{
      errors[0]
    }}</span>
  </validation-provider>
</template>

<script>
import UtilsBL from "../bl/utils.js";
import ItemTemplate from "./ItemTemplate.vue";

export default {
  name: "my-classificacao",
  props: ["value", "name", "label"],
  data() {
    return {
      classificacao: undefined,
      classificacoes: [],
      template: ItemTemplate,
    };
  },
  methods: {
    getLabelClassificacao: function(item) {
      return item;
    },
    updateClassificacoes: function(text) {
      // yourGetItemsMethod(text).then((response) => {
      //   this.items = response
      // })
      if (!text || text === "") return;
      this.errormsg = undefined;
      this.$http
        .get("sigaex/api/v1/classificacoes?texto=" + encodeURI(text))
        .then(
          (response) => {
            this.classificacoes = [];
            var l = response.data.list;
            if (l) {
              for (var i = 0; i < l.length; i++) {
                this.classificacoes.push(l[i].sigla + " - " + l[i].nome);
              }
            }
          },
          (error) => {
            UtilsBL.errormsg(error, this);
          }
        );
    },
  },
};
</script>
