<template>
  <div>
    <validation-observer v-slot="{ invalid }">
      <b-modal
        id="cota"
        ref="modal"
        v-model="showModal"
        :title="titulo"
        hide-header-close
        cancel-title="Cancelar"
        ok-title="Prosseguir"
        :ok-disabled="invalid"
        @ok="save"
      >
        <form>
          <input type="hidden" name="sigla" value="${m.sigla}" />
          <div class="form-group">
            <div class="form-group">
              <div class="text-center" v-if="carregando">
                <div
                  class="spinner-grow text-info text-center"
                  role="status"
                ></div>
              </div>
              <div class="form-group" v-if="!carregando">
                <label for="marcador">Marcador</label>
                <select
                  name="marcador"
                  v-model="idMarcador"
                  id="marcador"
                  class="form-control"
                >
                  <optgroup
                    v-for="grupo in listaAgrupada"
                    v-bind:label="grupo.grupo"
                    :key="grupo.grupo"
                  >
                    <option
                      v-for="option in grupo.lista"
                      v-bind:value="option.idMarcador"
                      :key="option.idMarcador"
                      >{{ option.nome }}</option
                    >
                  </optgroup>
                </select>
              </div>
              <div class="form-group" v-if="exibirInteressado">
                <label for="marcador">Interessado</label>
                <select
                  name="interessado"
                  v-model="interessado"
                  id="interessado"
                  class="form-control"
                >
                  <option
                    v-if="marcador.interessado.includes('PESSOA')"
                    value="pessoa"
                    >Pessoa</option
                  >
                  <option
                    v-if="marcador.interessado.includes('LOTACAO')"
                    value="lotacao"
                    >Lotacao</option
                  >
                </select>
              </div>
              <div v-if="exibirLotacao" class="form-group">
                <label for="marcador">Lotacao</label>
                <siga:selecao
                  tema="simple"
                  titulo="Lotação:"
                  propriedade="lotaSubscritor"
                  modulo="siga"
                />
              </div>
              <div v-if="exibirPessoa" class="form-group">
                <label for="marcador">Pessoa</label>
                <siga:selecao
                  tema="simple"
                  titulo="Matrícula:"
                  propriedade="subscritor"
                  modulo="siga"
                />
              </div>
              <div
                v-if="
                  marcador &&
                    (marcador.planejada != 'DESATIVADA' ||
                      marcador.limite !== 'DESATIVADA')
                "
                class="form-group row"
              >
                <div
                  class="col col-12 col-md-6"
                  v-if="marcador && marcador.planejada != 'DESATIVADA'"
                >
                  <label for="planejada">Data de Exibição</label>
                  <input
                    name="planejada"
                    id="planejada"
                    class="form-control campoData"
                    onblur="javascript:verifica_data(this,0);"
                    autocomplete="off"
                  />
                </div>
                <div
                  class="col col-12 col-md-6"
                  v-if="marcador && marcador.limite != 'DESATIVADA'"
                >
                  <label for="limite">Prazo Final</label>
                  <input
                    name="limite"
                    id="limite"
                    class="form-control campoData"
                    onblur="javascript:verifica_data(this,0);"
                    autocomplete="off"
                  />
                </div>
              </div>
              <div
                class="form-group"
                v-if="
                  marcador && marcador.texto && marcador.texto != 'DESATIVADA'
                "
              >
                <label for="texto">Texto</label>
                <input name="texto" id="texto" class="form-control" />
              </div>
            </div>
          </div>
        </form>
      </b-modal>
    </validation-observer>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";
import { Bus } from "../bl/bus.js";

export default {
  name: "marcador",

  mounted: function() {
    this.errormsg = undefined;
  },

  data: function() {
    return {
      interessado: undefined,
      idMarcador: undefined,
      lista: [],
      carregando: false,
      primeiraCarga: true,
      showModal: false,
      errormsg: undefined,
      titulo: undefined,
    };
  },

  computed: {
    listaAgrupada: function() {
      if (!this.lista) return;
      var l = [];
      for (var i = 0; i < this.lista.length; i++) {
        var m = this.lista[i];
        if (l.length == 0 || l[l.length - 1].grupo != m.grupo)
          l.push({
            grupo: m.grupo,
            lista: [],
          });
        l[l.length - 1].lista.push(m);
      }
      return l;
    },
    marcador: function() {
      if (!this.idMarcador) return undefined;
      for (var i = 0; i < this.lista.length; i++) {
        if (this.lista[i].idMarcador == this.idMarcador) return this.lista[i];
      }
      return undefined;
    },
    exibirInteressado: function() {
      if (!this.marcador) return false;
      if (this.marcador.interessado && this.marcador.interessado != "ATENDENTE")
        return true;
      return false;
    },
    exibirPessoa: function() {
      if (!this.marcador || !this.marcador.interessado) return false;
      if (this.marcador.interessado == "PESSOA") return true;
      return this.interessado == "pessoa";
    },
    exibirLotacao: function() {
      if (!this.marcador || !this.marcador.interessado) return false;
      if (this.marcador.interessado == "LOTACAO") return true;
      return this.interessado == "lotacao";
    },
  },

  methods: {
    show: function(documentos, cont, emit, titulo, campo) {
      this.showModal = true;
      this.errormsg = undefined;
      this.documentos = documentos;
      this.mob = null;
      this.cont = cont;
      this.titulo = titulo;
      this.campo = campo;
      this.emit = emit;
      this.carregar();
    },

    carregar: function() {
      this.carregando = true;
      var self = this;
      this.$http
        .get(
          "sigaex/api/v1/documentos/" +
            this.documentos[0].codigo +
            "/marcadores-disponiveis"
        )
        .then(
          (response) => {
            self.carregando = false;
            self.lista.length = 0;
            var resp = response.data;
            for (var i = 0; i < resp.list.length; i++) {
              self.lista.push(self.fixItem(resp.list[i]));
            }
            self.primeiraCarga = false;
          },
          (error) => {
            UtilsBL.errormsg(error, this);
          }
        );
    },

    fixItem: function(item) {
      UtilsBL.applyDefauts(item, {
        idMarcador: undefined,
        nome: undefined,
        odd: undefined,
      });
      return item;
    },

    cancel: function(e) {
      e.cancel();
    },

    save: function() {
      if ((this.idMarcador || "") === "") {
        this.errormsg = "Marcador deve ser informado.";
        return;
      }

      if (this.emit)
        Bus.$emit(this.emit, this.documentos, this.cont, {
          idMarcador: this.idMarcador,
        });
      this.$refs.modal.hide(true);
    },

    validar: function() {
      this.$nextTick(() => this.$validator.validateAll());
    },
  },
};
</script>
