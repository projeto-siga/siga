<template>
  <div>
    <label v-if="label" :for="name">{{label}}</label>
    <div v-if="!edit">{{value}}</div>
    <input type="text" v-if="edit" :id="name" class="form-control" 
      :placeholder="placeholder" v-mask="mask != undefined ? mask : ''"
      :disabled="disabled"
      v-bind:value="value" v-on:input="$emit('input', $event.target.value)" v-on:change="$emit('change', $event.target.value)" 
      :name="name" :class="{ 'is-invalid': error }">
    <div v-if="error" class="invalid-feedback">{{error}}</div>
  </div>
</template>

<script>
import AwesomeMask from 'awesome-mask'

export default {
  name: 'my-input',
  props: ['value', 'label', 'name', 'placeholder', 'mask', 'edit', 'error', 'disabled'],
  data() {
    return {}
  },
  directives: {
    mask: AwesomeMask
  }
}
</script>
