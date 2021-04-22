<template>
  <validation-provider rules="required" :immediate="true" v-slot="{ errors }">
    <label class="control-label" for="lotacao" style="width: 100%">{{
      label
    }}</label>
    <v-autocomplete
      :name="name"
      v-bind:value="value"
      v-on:input="$emit('input', $event)"
      v-on:change="$emit('change', $event)"
      :items="lotacoes"
      :get-label="getLabelLotacao"
      :component-item="template"
      @update-items="updateLotacoes"
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
  name: "my-lotacao",
  props: ["value", "label", "name"],
  data() {
    return {
      lotacoes: [],
      template: ItemTemplate,
    };
  },
  methods: {
    getLabelLotacao: function(item) {
      return item;
    },
    updateLotacoes: function(text) {
      // yourGetItemsMethod(text).then((response) => {
      //   this.items = response
      // })
      if (!text || text === "") return;
      this.errormsg = undefined;
      this.$http.get("siga/api/v1/lotacoes?texto=" + encodeURI(text)).then(
        (response) => {
          this.lotacoes = [];
          var l = response.data.list;
          if (l) {
            for (var i = 0; i < l.length; i++) {
              this.lotacoes.push(l[i].sigla + " - " + l[i].nome);
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
