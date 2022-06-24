<template>
  <validation-provider rules="required" :immediate="true" v-slot="{ errors }">
    <label class="control-label" for="matricula" style="width: 100%">{{
      label
    }}</label>
    <v-autocomplete
      :name="name"
      v-bind:value="definedValue"
      v-on:input="$emit('input', $event)"
      v-on:change="$emit('change', $event)"
      :items="pessoas"
      :get-label="getLabelPessoa"
      :component-item="template"
      @update-items="updatePessoas"
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
  name: "my-pessoa",
  props: ["value", "name", "label"],
  data() {
    return {
      pessoa: undefined,
      pessoas: [],
      template: ItemTemplate,
    };
  },
  computed: {
    definedValue() {
      if (this.value) return this.value;
      return "";
    },
  },
  methods: {
    getLabelPessoa: function(item) {
      return item;
    },
    updatePessoas: function(text) {
      // yourGetItemsMethod(text).then((response) => {
      //   this.items = response
      // })
      if (!text || text === "") return;
      this.errormsg = undefined;
      this.$http.get("siga/api/v1/pessoas?texto=" + encodeURI(text)).then(
        (response) => {
          this.pessoas = [];
          var l = response.data.list;
          if (l) {
            for (var i = 0; i < l.length; i++) {
              this.pessoas.push(l[i].sigla + " - " + l[i].nome);
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
