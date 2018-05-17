<template>
  <div class="container content pt-5">
    <div class="row">
      <div class="col-md-12">
        <div class="headline pb-1">
          <h2>Sugestões</h2>
        </div>
        <p>Precisamos ouvir sugestões e críticas para que possamos evoluir. Para enviá-las, basta preencher o formulário abaixo e clicar em "Enviar mensagem".</p>
        <br>
        <form class="css-form" role="form">
          <div class="row">
            <div class="col-lg-6">
              <div :class="{ 'form-group': true, 'has-danger': errors.has('nome') }">
                <label for="nome">Nome</label>
                <input type="text" class="form-control" v-model="sugestao.nome" id="nome" name="nome" placeholder="" v-validate.initial="'required'">
              </div>
            </div>
            <div class="col-lg-6">
              <div :class="{ 'form-group': true, 'has-danger': errors.has('email') }">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="" v-model="sugestao.email" v-validate.initial="'required|email'">
              </div>
            </div>
          </div>
  
          <div class="row">
            <div class="col-lg-12">
              <div :class="{ 'form-group': true, 'has-danger': errors.has('mensagem') }">
                <label for="nome">Mensagem</label>
                <textarea rows="4" class="form-control" v-model="sugestao.mensagem" id="nome" name="mensagem" placeholder="" v-validate.initial="'required'">
                </textarea>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="row">
              <div class="col-lg-12">
                <button class="btn btn-primary float-right" :disabled="errors.any()" @click.prevent="sugerir()">Enviar mensagem
                </button>
              </div>
            </div>
          </div>
        </form>
      </div>
      <!--/row-->
    </div>
  </div>
</template>

<script>
import { Bus } from '../bl/bus.js'

export default {
  name: 'sugestao',

  data () {
    return {
      sugestao: {
        nome: undefined,
        email: undefined,
        mensagem: undefined
      }
    }
  },

  methods: {
    sugerir: function () {
      this.$http.post('sugestao', this.sugestao).then(response => {
        Bus.$emit('message', 'Sucesso', 'Sua mensagem foi enviada. Muito obrigado!')
        this.sugestao.nome = undefined
        this.sugestao.email = undefined
        this.sugestao.mensagem = undefined
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    }
  }
}
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>

</style>
