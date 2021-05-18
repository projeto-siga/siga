<template>
  <div>
    <div class="wrapper">
      <div :class="{ dimmed: loading }">
        <!--=== Header ===-->
        <div class="header d-print-none">
          <!-- Navbar -->
          <b-navbar
            toggleable="lg"
            :class="{
              'navbar-dark bg-success':
                test.properties['siga.ambiente'] === 'desenv',
              'navbar-dark bg-secondary':
                test.properties['siga.ambiente'] === 'homolo',
              'navbar-dark bg-primary':
                test.properties['siga.ambiente'] === 'prod',
            }"
          >
            <b-navbar-brand href="#/mesa">
              <img
                id="logo-header"
                src="./assets/logo-siga-novo-38px.png"
                alt="Siga-Le"
                height="38"
              />
              <img
                class="ml-2"
                id="logo-header2"
                src="./assets/trf2-38px-2.png"
                alt="Logo TRF2"
                height="38"
              />
            </b-navbar-brand>
            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
              <b-navbar-nav>
                <b-nav-item v-if="!(jwt &amp;&amp; jwt.sub)">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Login' }"
                    tag="a"
                    >Login</router-link
                  >
                </b-nav-item>
                <b-nav-item v-if="jwt &amp;&amp; jwt.sub">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Mesa' }"
                    tag="a"
                    >Mesa</router-link
                  >
                </b-nav-item>
                <b-nav-item v-if="jwt &amp;&amp; jwt.sub">
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Quadro' }"
                    tag="a"
                    >Quadro</router-link
                  >
                </b-nav-item>
                <b-nav-item>
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Sugestões' }"
                    tag="a"
                    >Sugestões</router-link
                  >
                </b-nav-item>
                <b-nav-item>
                  <router-link
                    class="nav-link"
                    active-class="active"
                    :to="{ name: 'Sobre' }"
                    tag="a"
                    >Sobre</router-link
                  >
                </b-nav-item>
              </b-navbar-nav>
              <b-navbar-nav class="ml-auto">
                <b-nav-item-dropdown
                  v-if="jwt &amp;&amp; jwt.sub"
                  :text="jwt.sub"
                  right
                >
                  <b-dropdown-item @click="logout">Logout</b-dropdown-item>
                </b-nav-item-dropdown>

                <b-nav-form>
                  <b-form-input
                    v-model="siglaParaPesquisar"
                    size="sm"
                    class="mr-sm-2"
                    placeholder="Sigla"
                  ></b-form-input>
                  <b-button
                    @click="pesquisar"
                    size="sm"
                    class="my-2 my-sm-0"
                    type="submit"
                    >Buscar</b-button
                  >
                </b-nav-form>
              </b-navbar-nav>
            </b-collapse>
          </b-navbar>
          <!-- End Navbar -->
        </div>
        <!--=== End Header ===-->
      </div>
      <div id="app">
        <progressModal ref="progressModal"></progressModal>
        <progressModalAsync ref="progressModalAsync"></progressModalAsync>
        <messageBox ref="messageBox"></messageBox>
        <messageBoxConfirmacao
          ref="messageBoxConfirmacao"
        ></messageBoxConfirmacao>

        <top-progress ref="topProgress" :height="5" color="#000"></top-progress>
        <router-view></router-view>
      </div>
    </div>
    <div class="foot">
      <span :title="test.timestamp">v{{ test.version }}</span>
    </div>
    <assinatura ref="assinatura" title="Assinatura"></assinatura>
    <tramite ref="tramite" title="Trâmite"></tramite>
    <anotacao ref="anotacao" title="Anotação"></anotacao>
    <modal-mob ref="modalMob"></modal-mob>
    <modal-texto ref="modalTexto"></modal-texto>
    <modal-marcador ref="modalMarcador"></modal-marcador>
    <modal-perfil ref="modalPerfil"></modal-perfil>
    <modal-acesso ref="modalAcesso"></modal-acesso>
    <modal-cossignatario ref="modalCossignatario"></modal-cossignatario>
    <modal-anexo ref="modalAnexo"></modal-anexo>
  </div>
</template>

<script>
import Assinatura from "./components/Assinatura";
import Tramite from "./components/Tramite";
import Anotacao from "./components/Anotacao";
import AuthBL from "./bl/auth.js";
import UtilsBL from "./bl/utils.js";
import topProgress from "./components/TopProgress";
import { Bus } from "./bl/bus.js";
import ProgressModal from "./components/ProgressModal";
import ProgressModalAsync from "./components/ProgressModalAsync";
import MessageBox from "./components/MessageBox";
import MessageBoxConfirmacao from "./components/MessageBoxConfirmacao";
import AcaoBL from "./bl/acao.js";
import ModalMob from "./modal/ModalMob.vue";
import ModalTexto from "./modal/ModalTexto.vue";
import ModalMarcador from "./modal/ModalMarcador.vue";
import ModalPerfil from "./modal/ModalPerfil.vue";
import ModalAcesso from "./modal/ModalAcesso.vue";
import ModalCossignatario from "./modal/ModalCossignatario.vue";
import ModalAnexo from "./modal/ModalAnexo.vue";

export default {
  name: "app",
  components: {
    topProgress,
    progressModal: ProgressModal,
    messageBox: MessageBox,
    messageBoxConfirmacao: MessageBoxConfirmacao,
    assinatura: Assinatura,
    tramite: Tramite,
    anotacao: Anotacao,
    modalMob: ModalMob,
    modalTexto: ModalTexto,
    modalMarcador: ModalMarcador,
    modalPerfil: ModalPerfil,
    modalAcesso: ModalAcesso,
    modalCossignatario: ModalCossignatario,
    modalAnexo: ModalAnexo,
    progressModalAsync: ProgressModalAsync,
  },
  mounted() {
    UtilsBL.overrideProperties(
      this.settings,
      JSON.parse(localStorage.getItem("bv-settings")) || {}
    );
    this.$router.beforeEach((to, from, next) => {
      next();
      if (to.meta && to.meta.title) {
        document.title = to.meta.title(to);
      } else {
        document.title = "SIGA-Le - " + to.name;
      }
    });

    AcaoBL.registrar(this.$refs);

    Bus.$on("block", (min, max) => {
      if (this.blockCounter === 0) {
        this.$nextTick(function() {
          if (this.blockCounter > 0 && this.$refs.topProgress) {
            this.$refs.topProgress.start(min, max);
          }
        }, 200);
        this.loading = true;
      }
      this.blockCounter++;
    });

    Bus.$on("release", () => {
      this.blockCounter--;
      if (this.blockCounter === 0) {
        this.loading = false;
        this.$nextTick(function() {
          if (this.blockCounter === 0 && this.$refs.topProgress) {
            this.$refs.topProgress.done();
          }
        }, 200);
      }
    });

    this.$on("updateLogged", (token) => {
      if (token) {
        AuthBL.setIdToken(token);
        this.jwt = AuthBL.decodeToken(token);
        // $rootScope.updateLogged();
        // $state.go('consulta-processual');
        this.$router.push({
          name: "Mesa",
          params: { exibirAcessoAnterior: true },
        });
      }
    });

    this.$on("setting", (key, value) => {
      this.settings[key] = value;
      var json = JSON.stringify(this.settings);
      localStorage.setItem("bv-settings", json);
    });

    var prg = this.$refs.progressModal;

    Bus.$on("prgStart", (title, total, callbackNext, callbackEnd) => {
      prg.start(title, total, callbackNext, callbackEnd);
    });

    Bus.$on("prgCaption", (caption) => {
      prg.caption = caption;
    });

    Bus.$on("prgNext", (result) => {
      prg.next(result);
    });

    Bus.$on("message", (title, message) => {
      this.$refs.messageBox.show(title, message);
    });

    Bus.$on("confirmar", (title, message, callback) => {
      this.$refs.messageBoxConfirmacao.show(title, message, callback);
    });

    Bus.$on("iniciarAssinaturaComSenha", (documentos, cont) => {
      this.$refs.assinatura.show(documentos, cont);
    });

    Bus.$on("iniciarTramite", (documentos, cont) => {
      this.$refs.tramite.show(documentos, cont);
    });

    Bus.$on("iniciarAnotacao", (documentos, cont) => {
      this.$refs.anotacao.show(documentos, cont);
    });

    Bus.$on("tramitar", (documentos, lotacao, matricula, cont) => {
      this.tramitarEmLote(documentos, lotacao, matricula, cont);
    });

    Bus.$on("anotar", (documentos, anotacao, cont) => {
      this.anotarEmLote(documentos, anotacao, cont);
    });

    var prgAsync = this.$refs.progressModalAsync;

    Bus.$on("prgAsyncStart", (title, key, callbackEnd) => {
      prgAsync.start(title, key, callbackEnd);
    });

    this.token = AuthBL.getIdToken();
    if (this.token && AuthBL.isTokenExpired(this.token)) this.token = undefined;
    if (this.token) {
      AuthBL.setIdToken(this.token);
      this.jwt = AuthBL.decodeToken(this.token);
    } else {
      this.$router.push({ name: "Login" });
    }
    this.$nextTick(function() {
      this.$http.get("sigaex/api/v1/test?skip=all").then(
        (response) => {
          var re = /\[default: (.*)\]/gm;
          var subst = "$1";
          this.test = response.data;
          if (this.test.properties) {
            for (var k in this.test.properties) {
              if (k in this.test.properties)
                if (this.test.properties[k] == "[undefined]")
                  this.test.properties[k] = undefined;
                else
                  this.test.properties[k] = this.test.properties[k].replace(
                    re,
                    subst
                  );
            }
          }

          if (
            this.test.properties["siga-le.wootric.token"] &&
            this.test.properties["siga-le.wootric.token"] !== "[undefined]" &&
            this.jwt
          ) {
            // This loads the Wootric survey
            // window.wootric_survey_immediately = true
            window.wootricSettings = {
              email: this.jwt.sub,
              account_token: this.test.properties["siga-le.wootric.token"],
            };
            window.wootric("run");
          }
        },
        (error) => UtilsBL.errormsg(error, this)
      );
    });
  },
  data() {
    return {
      blockCounter: 0,
      loading: false,
      test: { properties: {} },
      errormsg: undefined,
      siglaParaPesquisar: undefined,
      settings: {
        timeline: undefined,
        timelineIncompatible: undefined,
        filtrarMovimentos: undefined,
        mostrarNotas: undefined,
        mostrarPartes: undefined,
        mostrarDadosComplementares: undefined,
        mostrarProcessosRelacionados: undefined,
      },
      token: undefined,
      jwt: {},
    };
  },
  methods: {
    isTokenValid: function() {
      return this.token && !AuthBL.isTokenExpired(this.token);
    },

    logout: function() {
      delete window.listaDaMesa;
      AuthBL.logout();
      this.jwt = {};
      this.$router.push({ name: "Login" });
    },

    pesquisar: function() {
      var pesq = (this.siglaParaPesquisar || "").replace(/[^a-z0-9]/gi, "");
      this.$http
        .get("sigaex/api/v1/documentos/" + pesq + "/pesquisar-sigla", {
          block: true,
        })
        .then(
          (response) => {
            if (response.data.codigo) {
              this.$router.push({
                name: "Documento",
                params: { numero: response.data.codigo },
              });
            }
          },
          (error) => UtilsBL.errormsg(error, this)
        );
      this.siglaParaPesquisar = undefined;
    },

    tramitar: function(d, lotacao, matricula, lote) {
      this.errormsg = undefined;
      Bus.$emit("prgCaption", "Tramitando " + d.sigla);

      this.$http
        .post(
          "sigaex/api/v1/documentos/" + d.codigo + "/tramitar",
          {
            lotacao: lotacao,
            matricula: matricula,
          },
          { block: !lote }
        )
        .then(
          () => {
            d.errormsg = undefined;
            UtilsBL.logEvento("tramite em lote", "tramitado");
            Bus.$emit("prgNext");
          },
          (error) => {
            if (lote) d.errormsg = error.data.errormsg;
            else Bus.$emit("message", "Erro", error.data.errormsg);
            Bus.$emit("prgNext");
          }
        );
    },

    tramitarEmLote: function(documentos, lotacao, matricula, cont) {
      Bus.$emit(
        "prgStart",
        "Tramitando",
        documentos.length,
        (i) =>
          this.tramitar(
            documentos[i],
            lotacao,
            matricula,
            documentos.length !== 1
          ),
        cont
      );
    },

    anotarEmLote: function(documentos, anotacao, cont) {
      Bus.$emit(
        "prgStart",
        "Anotando",
        documentos.length,
        (i) => this.anotar(documentos[i], anotacao, documentos.length !== 1),
        cont
      );
    },

    anotar: function(d, anotacao, lote) {
      this.errormsg = undefined;
      Bus.$emit("prgCaption", "Anotando " + d.sigla);

      this.$http
        .post(
          "sigaex/api/v1/documentos/" + d.codigo + "/anotar",
          {
            anotacao: anotacao,
          },
          { block: !lote }
        )
        .then(
          () => {
            d.errormsg = undefined;
            UtilsBL.logEvento("anotacao em lote", "anotado");
            Bus.$emit("prgNext");
          },
          (error) => {
            if (lote) d.errormsg = error.data.errormsg;
            else Bus.$emit("message", "Erro", error.data.errormsg);
            Bus.$emit("prgNext");
          }
        );
    },
  },
};
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
  content: " ";
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

div.foot {
  position: absolute;
  right: 0;
  bottom: 0;
  left: 0;
}

div.foot span {
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

.card-body label {
  text-transform: uppercase;
  color: #777;
  margin-bottom: 0px;
  font-size: 80%;
}

.text-white .card-body a {
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
  content: " ";
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

div.sobre p {
  font-size: 120%;
}

div.sobre ol li {
  font-size: 120%;
  padding-bottom: 12pt;
}

table.table-peticao tbody td {
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

.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

h1 {
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

.v-autocomplete .v-autocomplete-list {
  width: 100%;
  text-align: left;
  border: none;
  border-top: none;
  max-height: 400px;
  overflow-y: auto;
  border-bottom: 1px solid #157977;
}

.v-autocomplete .v-autocomplete-list .v-autocomplete-list-item {
  cursor: pointer;
  background-color: #fff;
  padding: 10px;
  border-bottom: 1px solid #157977;
  border-left: 1px solid #157977;
  border-right: 1px solid #157977;
}

.v-autocomplete .v-autocomplete-list .v-autocomplete-list-item:last-child {
  border-bottom: none;
}

.v-autocomplete .v-autocomplete-list .v-autocomplete-list-item:hover {
  background-color: #eee;
}

.v-autocomplete .v-autocomplete-list .v-autocomplete-list-item abbr {
  opacity: 0.8;
  font-size: 0.8em;
  display: block;
  font-family: sans-serif;
}
div.v-autocomplete-selected input.form-control {
  background-color: #f2fff2;
}
</style>
