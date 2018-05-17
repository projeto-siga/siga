<template>
  <div>
    <b-modal id="pdfpreview" ref="modal" :title="title" size="lg" hide-footer>
      <iframe :src="src" style="width: 100%; height: 35em;"></iframe>
    </b-modal>
  </div>
</template>

<script>
export default {
  name: 'pdf-preview',

  props: ['title', 'src'],

  data () {
    return {}
  },

  methods: {
    show: function () {
      this.$refs.modal.show(true)
      this.errormsg = undefined
    },

    hide: function () {
      this.$refs.modal.hide(true)
    }
  }
}
</script>
