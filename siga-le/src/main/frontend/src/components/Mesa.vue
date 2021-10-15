<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-0">Mesa Virtual</h4>
      </div>
      <div class="col col-sm-12" v-if="errormsg">
        <p class="alert alert-danger"><strong>Erro!</strong> {{ errormsg }}</p>
      </div>
    </div>

    <div
      class="row d-print-none"
      v-if="lista &amp;&amp; lista.length > 0"
    >
      <div class="col col-12 col-md-auto">
        <div class="input-group mt-3">
          <div class="input-group-prepend">
            <span class="input-group-text" id="basic-addon1"
              ><span class="fa fa-search"></span
            ></span>
          </div>
          <input
            type="text"
            class="form-control"
            placeholder="Filtrar"
            v-model="filtro"
            ng-model-options="{ debounce: 200 }"
          />
        </div>
      </div>
      <div class="col col-auto ml-auto">
        <button
          type="button"
          @click="novoDocumento()"
          class="btn btn-primary mt-3"
          title=""
        >
          <span class="fa fa-sticky-note-o d-none d-md-inline"></span>
          Novo&nbsp;
        </button>
        <button
          v-if="(filtradosEMarcadosEAnotaveis || []).length"
          type="button"
          @click="anotarEmLote()"
          class="btn btn-primary mt-3"
          title=""
        >
          <span class="fa fa-sticky-note-o d-none d-md-inline"></span>
          Anotar&nbsp;
          <span class="badge badge-pill badge-warning">{{
            filtradosEMarcadosEAnotaveis.length
          }}</span>
        </button>
        <button
          v-if="(filtradosEMarcadosEAssinaveis || []).length"
          type="button"
          @click="assinarComSenhaEmLote()"
          class="btn btn-primary mt-3"
          title=""
        >
          <span class="fa fa-shield d-none d-md-inline"></span> Assinar&nbsp;
          <span class="badge badge-pill badge-warning">{{
            filtradosEMarcadosEAssinaveis.length
          }}</span>
        </button>
        <button
          v-if="(filtradosEMarcadosETramitaveis || []).length"
          type="button"
          @click="tramitarEmLote()"
          class="btn btn-primary mt-3"
          title=""
        >
          <span class="fa fa-paper-plane-o d-none d-md-inline"></span>
          Tramitar&nbsp;
          <span class="badge badge-pill badge-warning">{{
            filtradosEMarcadosETramitaveis.length
          }}</span>
        </button>
      </div>
    </div>

    <div class="row mt-3" v-if="carregando &amp;&amp; primeiraCarga">
      <div class="col col-12">
        <p class="alert alert-warning">
          <strong>Aguarde,</strong> carregando documentos...
        </p>
      </div>
    </div>

    <div class="row mt-3" v-if="!carregando &amp;&amp; !primeiraCarga &amp;&amp; filtrados.length == 0">
      <div class="col col-12">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhum documento na mesa.
        </p>
      </div>
    </div>

    <div class="row" v-if="filtrados.length > 0">
      <div class="col-sm-12">
        <table class="table table-sm table-borderless">
          <tbody>
            <template v-for="f in filtrados">
              <tr
                v-if="f.grupoExibir"
                :key="f.sigla + ':grupo1'"
                class="table-group"
              >
                <th colspan="6" class="pt-3 pb-0 pl-0">
                  <h4 class="mb-1">{{ f.grupoNome }}</h4>
                </th>
              </tr>
              <tr
                v-if="f.grupoExibir"
                :key="f.sigla + ':grupo2'"
                class="table-head"
              >
                <th style="text-align: center">
                  <input
                    type="checkbox"
                    id="progress_checkall"
                    name="progress_checkall"
                    v-model="todos[f.grupo]"
                    @change="marcarTodos(f.grupo)"
                  />
                </th>
                <th class="d-none d-md-block">Tempo</th>
                <th>Código</th>
                <th class="d-none d-md-block">Descrição</th>
                <th>Origem</th>
                <th class="d-none d-md-block">Etiquetas</th>
                <th v-show="filtradosTemAlgumErro">Atenção</th>
              </tr>
              <tr v-bind:class="{ odd: f.odd }" :key="f.sigla + ':titulo'">
                <td style="text-align: center">
                  <input
                    type="checkbox"
                    v-model="f.checked"
                    :disabled="f.disabled"
                  />
                </td>
                <td class="d-none d-md-block" :title="f.datahora">
                  {{ f.tempoRelativo }}
                </td>
                <td>
                  <router-link
                    :to="{ name: 'Documento', params: { numero: f.codigo } }"
                    >{{ f.sigla }}</router-link
                  ><span class="d-inline d-md-none"> - {{ f.descr }}</span>
                </td>
                <td class="d-none d-md-block">{{ f.descr }}</td>
                <td>{{ f.origem }}</td>
                <td class="d-none d-md-block" style="padding: 0;">
                  <div class="xrp-label-container">
                    <!-- class="list-unstyled blog-tags" -->
                    <span
                      v-for="m in f.list"
                      :key="f.sigla + ':tag:' + m.marcaId"
                      :title="m.titulo"
                      ><button class="btn btn-default btn-sm xrp-label">
                        <i :class="'fa fa-' + m.icone"></i> {{ m.nome
                        }}<span v-if="m.pessoa &amp;&amp; !m.daPessoa">
                          - {{ m.pessoa }}</span
                        ><span
                          v-if="m.unidade &amp;&amp; (!m.daLotacao || (!m.daPessoa && !m.deOutraPessoa))"
                        >
                          / {{ m.unidade }}</span
                        >
                      </button></span
                    >
                  </div>
                </td>
                <td v-show="filtradosTemAlgumErro" style="color: red">
                  {{ f.errormsg }}
                </td>
              </tr>
              <tr
                v-if="f.grupoEspacar"
                :key="f.sigla + ':grupo3'"
                class="table-group"
              >
                <th colspan="6" class="pb-2 pb-0 pl-0"></th>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
    </div>
    <p
      class="alert alert-success"
      v-if="acessos &amp;&amp; acessos.length >= 1"
    >
      Último acesso em {{ acessos[1].datahora }} no endereço
      {{ acessos[1].ip }}.
    </p>
  </div>
</template>

<script>
import { Bus } from "../bl/bus.js";
import UtilsBL from "../bl/utils.js";
// import { Bus } from '../bl/bus.js'

export default {
  components: {},

  mounted() {
    this.errormsg = undefined;
    console.log("mesa-mounted");

    setTimeout(() => {
      this.carregarMesa();
      if (this.$route.params.exibirAcessoAnterior) this.carregarAcessos();
    });
  },

  data() {
    return {
      mesa: undefined,
      filtro: undefined,
      lista: window.listaDaMesa ? window.listaDaMesa : [],
      primeiraCarga: window.listaDaMesa ? false : true,
      todos: {},
      carregando: false,
      acessos: [],
      errormsg: undefined,
    };
  },

  computed: {
    filtrados: function() {
      var a = this.lista;
      var grupo;
      var odd = false;
      a = UtilsBL.filtrarPorSubstring(
        a,
        this.filtro,
        "grupoNome,tempoRelativo,sigla,codigo,descr,origem,situacao,errormsg,list.nome".split(
          ","
        )
      );
      a = a.filter(function(item) {
        return item.grupo !== "NENHUM";
      });
      for (var i = 0; i < a.length; i++) {
        a[i].grupoExibir = a[i].grupo !== grupo;
        grupo = a[i].grupo;
        if (a[i].grupoExibir) odd = false;
        if (a[i].grupoExibir && i > 0) a[i - 1].grupoEspacar = true;
        odd = !odd;
        a[i].odd = odd;
      }
      return a;
    },

    filtradosTemAlgumErro: function() {
      if (!this.filtrados || this.filtrados.length === 0) return false;
      for (var i = 0; i < this.filtrados.length; i++) {
        if (this.filtrados[i].errormsg) return true;
      }
      return false;
    },

    filtradosEAnotaveis: function() {
      return this.filtrados.filter((item) => item.podeAnotar);
    },

    filtradosEAssinaveis: function() {
      return this.filtrados.filter((item) => item.podeAssinarEmLote);
    },

    filtradosETramitaveis: function() {
      return this.filtrados.filter((item) => item.podeTramitar);
    },

    filtradosEMarcadosEAnotaveis: function() {
      return this.filtradosEAnotaveis.filter(function(item) {
        return item.checked;
      });
    },

    filtradosEMarcadosEAssinaveis: function() {
      return this.filtradosEAssinaveis.filter(function(item) {
        return item.checked;
      });
    },

    filtradosEMarcadosETramitaveis: function() {
      return this.filtradosETramitaveis.filter(function(item) {
        return item.checked;
      });
    },
  },

  methods: {
    carregarMesa: function() {
      this.carregando = true;
      var erros = {};
      if (this.lista && this.lista.length > 0) {
        for (var i = 0; i < this.lista.length; i++) {
          erros[this.lista[i].codigo] = this.lista[i].errormsg;
        }
      }
      this.$http.get("sigaex/api/v1/mesa", { block: true }).then(
        (response) => {
          this.carregando = false;
          this.lista.length = 0;
          var list = response.data.list;
          for (var i = 0; i < list.length; i++) {
            list[i].errormsg = erros[list[i].codigo];
            this.lista.push(this.fixItem(list[i]));
          }
          this.primeiraCarga = false;
          window.listaDaMesa = this.lista;
        },
        (error) => {
          this.carregando = false;
          UtilsBL.errormsg(error, this);
        }
      );
    },

    carregarAcessos: function() {
      this.acessos.length = 0;
      this.$http.get("siga/api/v1/acessos").then(
        (response) => {
          var list = response.data.list;
          for (var i = 0; i < list.length; i++) {
            list[i].datahora = UtilsBL.formatJSDDMMYYYY_AS_HHMM(
              list[i].datahora
            );
            this.acessos.push(list[i]);
          }
        },
        (error) => {
          UtilsBL.errormsg(error, this);
        }
      );
    },

    fixItem: function(item) {
      UtilsBL.applyDefauts(item, {
        checked: false,
        disabled: false,
        grupo: undefined,
        grupoNome: undefined,
        grupoExibir: undefined,
        grupoEspacar: undefined,
        datahora: undefined,
        datahoraFormatada: undefined,
        sigla: undefined,
        codigo: undefined,
        descr: undefined,
        origem: undefined,
        situacao: undefined,
        errormsg: undefined,
        odd: undefined,
      });
      if (item.datahora !== undefined) {
        item.datahoraFormatada = UtilsBL.formatJSDDMMYYYYHHMM(item.datahora);
      }
      return item;
    },

    novoDocumento: function() {
      this.$router.push({ name: "DocumentoNovo" });
    },

    assinarComSenhaEmLote: function() {
      var a = this.filtradosEMarcadosEAssinaveis;
      // Bus.$emit('iniciarAssinaturaComSenha', a, this.carregarMesa)
      Bus.$emit("assinarComSenha", a, undefined, undefined, this.carregarMesa);
    },

    anotarEmLote: function() {
      var a = this.filtradosEMarcadosEAnotaveis;
      Bus.$emit("iniciarAnotacao", a);
    },

    tramitarEmLote: function() {
      var a = this.filtradosEMarcadosETramitaveis;
      Bus.$emit("iniciarTramite", a, this.carregarMesa);
    },

    marcarTodos: function(grupo) {
      var docs = this.filtrados;
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i];
        if (doc.grupo === grupo) doc.checked = this.todos[grupo];
      }
    },

    mostrarDocumento: function(item, disposition) {
      var form = document.createElement("form");
      form.action =
        this.$parent.test.properties["siga-le.assijus.endpoint"] +
        "/api/v1/view" +
        (disposition === "attachment" ? "?disposition=attachment" : "");
      form.method = "POST";
      form.target = "_blank";
      form.style.display = "none";

      var cpf = document.createElement("input");
      cpf.type = "text";
      cpf.name = "cpf";
      cpf.value = this.$parent.jwt.cpf;

      var system = document.createElement("input");
      system.type = "text";
      system.name = "system";
      system.value = item.docsystem;

      var docid = document.createElement("input");
      docid.type = "text";
      docid.name = "id";
      docid.value = item.docid;

      var docsecret = document.createElement("input");
      docsecret.type = "text";
      docsecret.name = "secret";
      docsecret.value = item.docsecret;

      var submit = document.createElement("input");
      submit.type = "submit";
      submit.id = "submitView";

      form.appendChild(cpf);
      form.appendChild(system);
      form.appendChild(docid);
      form.appendChild(docsecret);
      form.appendChild(submit);
      document.body.appendChild(form);

      /* global $ */
      $("#submitView").click();

      document.body.removeChild(form);
    },

    criarAssinavel: function(item) {
      return {
        id: item.docid,
        system: item.docsystem,
        code: item.codigo,
        descr: item.docdescr,
        kind: item.dockind,
        origin: "Balcão Virtual",
      };
    },

    assinarDocumento: function(item) {
      this.chamarAssijus([this.criarAssinavel(item)]);
    },

    assinarDocumentos: function() {
      var list = [];
      for (var i = 0; i < this.filtradosEMarcadosEAssinaveis.length; i++) {
        list.push(this.criarAssinavel(this.filtradosEMarcadosEAssinaveis[i]));
      }
      if (list.length > 0) this.chamarAssijus(list);
    },

    chamarAssijus: function(list) {
      var json = JSON.stringify({ list: list });
      this.$http
        .post(
          this.$parent.test.properties["siga-le.assijus.endpoint"] +
            "/api/v1/store",
          { payload: json },
          { block: true }
        )
        .then(
          (response) => {
            var callback = window.location.href + "";
            console.log(callback);
            window.location.href =
              this.$parent.test.properties["siga-le.assijus.endpoint"] +
              "/?endpointautostart=true&endpointlistkey=" +
              response.data.key +
              "&endpointcallback=" +
              encodeURI(callback).replace("#", "__hashsign__");
          },
          (error) => UtilsBL.errormsg(error, this)
        );
    },

    editar: function() {
      this.$refs.etiqueta.show();
    },

    exibirProcessosMultiplos: function() {
      this.$refs.processosMultiplos.show();
    },

    acrescentarProcessosNaLista: function(arr) {
      if (!arr || arr.length === 0) return;
      this.pasta = "inbox";
      for (var i = 0; i < arr.length; i++) {
        if (arr[i] === "") continue;
        var p = this.fixProcesso({
          numero: arr[i],
          inbox: true,
        });
        this.processos.push(p);
      }
      this.validarEmLoteSilenciosamente();
    },
  },
};
</script>

<style scoped>
.destaque {
  color: red;
}

.td-middle {
  vertical-align: middle;
}

table .table-group th {
  border-top: 0;
}

table .table-head th {
  border-top: 0;
}

.odd {
  background-color: rgba(0, 0, 0, 0.05);
}

.xrp-label-container {
  margin-top: 4px;
  margin-bottom: 0px;
}

.xrp-label {
  font-size: 13px;
  margin-bottom: 4px;
  margin-right: 8px;
  line-height: 1.1;
  padding-left: 7px;
  padding-right: 7px;
  border-radius: 0px;
  border: 1px solid #ccc;
}
</style>
