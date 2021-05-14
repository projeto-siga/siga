<template>
  <div ref="myIFrame" id="paipainel" v-bind:style="{ height: height + 'px' }">
    <iframe
      style="visibility: visible; margin: 0px; padding: 0px; min-height: 20em; display: block;"
      name="painel"
      id="painel"
      :src="src"
      align="right"
      width="100%"
      frameborder="0"
      scrolling="auto"
      height="100%"
    ></iframe>
  </div>
</template>

<script>
export default {
  name: "my-iframe",
  props: ["src"],
  data() {
    return {
      top: 0,
      window: {
        width: 0,
        height: 0,
      },
    };
  },
  created() {
    window.addEventListener("resize", this.handleResize);
    this.$nextTick(this.handleResize);
  },
  destroyed() {
    window.removeEventListener("resize", this.handleResize);
  },
  computed: {
    height() {
      return this.window.height - this.top;
    },
  },
  methods: {
    handleResize() {
      this.top = this.$refs.myIFrame.getBoundingClientRect().top;
      this.window.width = window.innerWidth;
      this.window.height = window.innerHeight;
    },
  },
};
</script>
