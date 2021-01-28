<template>
  <div>
    <b-modal id="messageBox" v-model="showModal" :title="title" ok-only ok-title="OK" @hide="save">
      <p>{{message}}</p>
    </b-modal>
  </div>
</template>

<script>
export default {
  name: 'message-box',

  data () {
    return {
      showModal: false,
      title: undefined,
      message: undefined
    }
  },

  methods: {
    show: function (title, message) {
      this.title = title
      this.message = message
      this.showModal = true
    },

    save: function () {
    }
  }
}
</script>
