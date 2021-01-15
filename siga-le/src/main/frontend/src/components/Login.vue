<template>
  <div class="container content pt-5">
    <div class="row justify-content-center">
      <div class="col col-sm-12 col-md-6">
        <div class="jumbotron d-block mx-auto">
          <h2 class="text-center pb-3">Autenticação</h2>
          <p v-if="errormsg" class="alert alert-danger" role="alert">
            {{ errormsg }}
          </p>
          <form role="form">
            <div class="form-group">
              <label for="username">Usuário</label>
              <input
                type="text"
                class="form-control"
                v-model="user.username"
                id="username"
                placeholder="Username"
                autocorrect="off"
                autocapitalize="none"
              />
            </div>
            <div class="form-group">
              <label for="password">Senha</label>
              <input
                type="password"
                class="form-control"
                id="password"
                v-model="user.password"
                placeholder="Password"
              />
            </div>
            <div class="row pt-3">
              <div class="col">
                <button
                  class="btn btn-primary d-block mx-auto"
                  @click.prevent="login()"
                >
                  Enviar
                </button>
              </div>
            </div>
          </form>
          <!-- <a href="signup()">Click here to Signup</a> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'

export default {
  name: 'login',
  data() {
    return {
      errormsg: undefined,
      user: {},
    }
  },
  methods: {
    login: function() {
      var token = this.user.username + ':' + this.user.password
      var hash = btoa(token)
      var auth = 'Basic ' + hash
      this.$http
        .post(
          'siga/api/v1/autenticar',
          {},
          { headers: { Authorization: auth }, block: true }
        )
        .then(
          (response) => {
            this.$parent.$emit('updateLogged', response.data.token)
          },
          (error) => UtilsBL.errormsg(error, this)
        )
    },
  },
}
</script>

<style scoped></style>
