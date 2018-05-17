<template>
  <div>
    <div class="wrapper">
      <div :class="{dimmed: loading}">
        <!--=== Header ===-->
        <div class="header d-print-none">
          <!-- Navbar -->
          <nav class="navbar navbar navbar-expand-lg navbar-light" :class="{'navbar-dark bg-success': test.properties['balcaovirtual.env'] === 'desenv', 'navbar-dark bg-secondary': test.properties['balcaovirtual.env'] === 'homolo', 'navbar-dark bg-primary': true}">
            <a class="navbar-brand pt-0 pb-0" href="#">
              <img id="logo-header" src="./assets/balcao-virtual-38px.png" alt="Logo Siga-Doc" height="38"></img>
            </a>

            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
              <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                  <router-link class="nav-link" active-class="active" :to="{name:'Consulta Simples'}" tag="a" exact>Painel</router-link>
                </li>
                <li class="nav-item">
                  <router-link class="nav-link" active-class="active" :to="{name:'Lista de Processos'}" tag="a" exact>Pesquisar</router-link>
                </li>
                <li class="nav-item">
                  <router-link class="nav-link" active-class="active" :to="{name:'Processo'}" tag="a" exact>Documento</router-link>
                </li>
                <li class="nav-item">
                  <router-link class="nav-link" active-class="active" :to="{name:'Sugestões'}" tag="a">Sugestões</router-link>
                </li>
                <li class="nav-item">
                  <router-link class="nav-link" active-class="active" :to="{name:'Sobre'}" tag="a">Sobre</router-link>
                </li>
              </ul>
              <ul class="navbar-nav navbar-right" v-if="jwt &amp;&amp; jwt.username">
                <li class="nav-item dropdown">
                  <a class="nav-link dropdown-toggle" href="" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span>{{jwt.username}}</span>
                  </a>
                  <div class="dropdown-menu logout" aria-labelledby="navbarDropdownMenuLink">
                    <a class="dropdown-item" @click="logout">Logout</a>
                  </div>
                </li>
              </ul>
            </div>
          </nav>
          <!-- End Navbar -->
        </div>
        <!--=== End Header ===-->
      </div>
      <div id="app">
        <progressModal ref="progressModal"></progressModal>
        <messageBox ref="messageBox"></messageBox>

        <top-progress ref="topProgress" :height="5" color="#000"></top-progress>
        <router-view></router-view>
      </div>
    </div>
    <div class="foot">
      <span :title="test.timestamp">v{{test.version}}</span>
    </div>
  </div>
</template>

<script>
import AuthBL from './bl/auth.js'
import UtilsBL from './bl/utils.js'
import topProgress from './components/TopProgress'
import { Bus } from './bl/bus.js'
import ProgressModal from './components/ProgressModal'
import MessageBox from './components/MessageBox'

export default {
  name: 'app',
  mounted() {
    UtilsBL.overrideProperties(
      this.settings,
      JSON.parse(localStorage.getItem('bv-settings')) || {}
    )
    this.$router.beforeEach((to, from, next) => {
      next()
      if (to.meta && to.meta.title) {
        document.title = to.meta.title(to)
      } else {
        document.title = 'Siga-Doc - ' + to.name
      }
    })

    Bus.$on('block', (min, max) => {
      if (this.blockCounter === 0) {
        this.$nextTick(function() {
          if (this.blockCounter > 0) {
            this.$refs.topProgress.start(min, max)
          }
        }, 200)
        this.loading = true
      }
      this.blockCounter++
    })

    Bus.$on('release', () => {
      this.blockCounter--
      if (this.blockCounter === 0) {
        this.loading = false
        this.$nextTick(function() {
          if (this.blockCounter === 0) {
            this.$refs.topProgress.done()
          }
        }, 200)
      }
    })

    this.$on('updateLogged', token => {
      if (token) {
        AuthBL.setIdToken(token)
        this.jwt = AuthBL.decodeToken(token)
        // $rootScope.updateLogged();
        // $state.go('consulta-processual');
        this.$router.push({ name: 'Consulta Simples' })
      }
    })

    this.$on('setting', (key, value) => {
      this.settings[key] = value
      var json = JSON.stringify(this.settings)
      localStorage.setItem('bv-settings', json)
    })

    var prg = this.$refs.progressModal

    Bus.$on('prgStart', (title, total, callbackNext, callbackEnd) => {
      prg.start(title, total, callbackNext, callbackEnd)
    })

    Bus.$on('prgCaption', caption => {
      prg.caption = caption
    })

    Bus.$on('prgNext', () => {
      prg.next()
    })

    Bus.$on('message', (title, message) => {
      this.$refs.messageBox.show(title, message)
    })

    this.token = AuthBL.getIdToken()
    if (this.token && AuthBL.isTokenExpired(this.token)) this.token = undefined
    if (this.token) {
      AuthBL.setIdToken(this.token)
      this.jwt = AuthBL.decodeToken(this.token)
    } else {
      this.$router.push({ name: 'Login' })
    }
    this.$nextTick(function() {
      this.$http.get('test?skip=all').then(
        response => {
          this.test = response.data

          if (
            this.test.properties['balcaovirtual.wootric.token'] &&
            this.test.properties['balcaovirtual.wootric.token'] !==
              '[undefined]' &&
            this.jwt
          ) {
            // This loads the Wootric survey
            // window.wootric_survey_immediately = true
            window.wootricSettings = {
              email: this.jwt.username,
              account_token: this.test.properties['balcaovirtual.wootric.token']
            }
            window.wootric('run')
          }
        },
        error => UtilsBL.errormsg(error, this)
      )
    })
  },
  data() {
    return {
      blockCounter: 0,
      loading: false,
      test: { properties: {} },
      errormsg: undefined,
      settings: {
        timeline: undefined,
        timelineIncompatible: undefined,
        filtrarMovimentos: undefined,
        mostrarNotas: undefined,
        mostrarPartes: undefined,
        mostrarDadosComplementares: undefined,
        mostrarProcessosRelacionados: undefined
      },
      token: undefined,
      jwt: {}
    }
  },
  methods: {
    isTokenValid: function() {
      return this.token && !AuthBL.isTokenExpired(this.token)
    },
    logout: function() {
      AuthBL.logout()
      this.jwt = {}
      this.$router.push({ name: 'Login' })
    }
  },
  components: {
    topProgress,
    progressModal: ProgressModal,
    messageBox: MessageBox
  }
}
</script>

<style>
.close-button {
  float: right;
}

.icone-em-linha {
  color: black;
  opacity: 0.2;
}

.icone-em-linha:hover {
  opacity: 1;
}

.dimmed {
  position: relative;
}

.dimmed:before {
  content: ' ';
  z-index: 10;
  display: block;
  position: absolute;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.5);
}

.highlight {
  background-color: #ffff00 !important;
}

.highlightable {
  transition-property: background-color;
  transition-duration: 1s;
  transition-timing-function: easy-out;
  transition-delay: 0s;
}

html {
  height: 100%;
  box-sizing: border-box;
}

*,
*:before,
*:after {
  box-sizing: inherit;
}

body {
  position: relative;
  margin: 0;
  padding-bottom: 6rem;
  min-height: 100%;
}

div.dropdown-menu.logout {
  right: 0px;
  left: auto;
  min-width: 0px;
}

DIV.foot {
  position: absolute;
  right: 0;
  bottom: 0;
  left: 0;
}

DIV.foot SPAN {
  font-size: 80%;
  color: #ccc;
  border-top: 1px solid #ccc;
  float: right;
  margin-right: 0.5em;
  margin-bottom: 0.3em;
}

.red {
  color: red;
}

.green {
  color: green;
}

.odd {
  background-color: rgba(0, 0, 0, 0.05);
}

h1::first-letter {
  margin-top: 0;
}

h2::first-letter {
  margin-top: 0;
}

h3::first-letter {
  margin-top: 0;
}

h4::first-letter {
  margin-top: 0;
}

h5::first-letter {
  margin-top: 0;
}

h6::first-letter {
  margin-top: 0;
}

.text-success-dark {
  color: #5cb85c;
}

@media print {
  body {
    font-size: 70%;
    margin: 0;
    padding: 0 !important;
    min-width: 768px;
  }
  .container {
    width: auto;
    min-width: 750px;
  }
}

.break {
  margin-bottom: 0.6rem;
}

.card-body LABEL {
  text-transform: uppercase;
  color: #777;
  margin-bottom: 0px;
  font-size: 80%;
}

.text-white .card-body A {
  color: #fff;
}

.dropdown-submenu {
  position: relative;
}

.dropdown-submenu > .dropdown-menu {
  top: 0;
  left: 100%;
  margin-top: -6px;
  margin-left: -1px;
  -webkit-border-radius: 0 6px 6px 6px;
  -moz-border-radius: 0 6px 6px;
  border-radius: 0 6px 6px 6px;
}

.dropdown-submenu:hover > .dropdown-menu {
  display: block;
}

.dropdown-submenu > a:after {
  display: block;
  content: ' ';
  float: right;
  width: 0;
  height: 0;
  border-color: transparent;
  border-style: solid;
  border-width: 5px 0 5px 5px;
  border-left-color: #ccc;
  margin-top: 5px;
  margin-right: -10px;
}

.dropdown-submenu:hover > a:after {
  border-left-color: #fff;
}

.dropdown-submenu.pull-left {
  float: none;
}

.dropdown-submenu.pull-left > .dropdown-menu {
  left: -100%;
  margin-left: 10px;
  -webkit-border-radius: 6px 0 6px 6px;
  -moz-border-radius: 6px 0 6px 6px;
  border-radius: 6px 0 6px 6px;
}

DIV.sobre p {
  font-size: 120%;
}

DIV.sobre ol li {
  font-size: 120%;
  padding-bottom: 12pt;
}

TABLE.table-peticao TBODY TD {
  vertical-align: middle;
}

.drop-box {
  background: #f8f8f8;
  border: 5px dashed #ddd;
  width: 100#;
  text-align: center;
  padding: 1em;
}

.dragover {
  border: 5px dashed blue;
}

body1 {
  background-color: #eee !important;
}

.form-signin {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}

.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}

.form-signin .checkbox {
  font-weight: normal;
}

.form-signin .form-control {
  position: relative;
  height: auto;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  padding: 10px;
  font-size: 16px;
}

.form-signin .form-control:focus {
  z-index: 2;
}

.form-signin input[type='email'] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-signin input[type='password'] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

H1 {
  text-align: center;
}

.metas {
  text-align: center;
  width: 100%;
}

.transparent {
  filter: alpha(opacity=0);
  opacity: 0;
  -moz-opacity: 0;
  -webkit-opacity: 0;
}

div.metas {
  text-align: center;
  display: inline-block;
}

div.metas table {
  width: auto;
  margin: 0 auto !important;
}

em.invalid {
  display: block;
  margin-top: 6px;
  padding: 0 1px;
  font-style: normal;
  font-size: 12px;
  line-height: 15px;
  color: #ee9393;
}
</style>
