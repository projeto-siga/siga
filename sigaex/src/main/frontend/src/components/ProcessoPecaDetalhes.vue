<template>
  <div>
    <b-modal ref="processoPecaDetalhes" id="processoPecaDetalhes" v-model="showModal" title="Marcar Páginas" close-title="Cancelar" ok-title="Salvar Marcador" hide-header-close no-close-on-esc @hide="save">
      <b-form>
        <div class="row">
          <div class="col col-md-8 form-group">
            <label class="control-label" for="texto" style="width: 100%">Marcador</label>
            <b-form-input type="text" list="lst_userIdTypes" name="texto" id="texto" v-model="texto" class="form-control" :class="{'is-invalid': errors.has('texto') }" style="width: 100%" autofocus v-on:keyup.enter="$refs.processoPecaDetalhes.hide(true)" v-validate.initial="'required'"></b-form-input>
            <datalist id="lst_userIdTypes">
              <option v-for="m in marcadores">{{m}}</option>
            </datalist>
            <span v-if="false" v-show="errors.has('texto')" class="help is-danger">{{ errors.first('texto') }}</span>
          </div>
          <div class="col col-md-4">
            <label class="control-label" for="estilo" style="width: 100%">Modalidade</label>
            <b-form-select v-model="estilo" :options="estilosfiltrados" class="mb-3">
            </b-form-select>
          </div>
        </div>
        <input class="mt-3 mb-3" type="checkbox" v-model="intervalo"> Intervalo de Páginas </input>
        <div class="row" v-show="intervalo">
          <div class="col form-group">
            <label class="control-label" for="paginicial" style="width: 100%">Página Inicial</label>
            <b-form-input type="text" name="paginicial" id="paginicial" v-model="paginicial" class="form-control" :class="{'is-invalid': errors.has('paginicial') }" :placeholder="pagmin" style="width: 100%" v-validate.initial="'between:' + pagmin + ',' + pagmax + '|required'" @change="validar()"></b-form-input>
            <span v-if="false" v-show="errors.has('paginicial')" class="help is-danger">{{ errors.first('paginicial') }}</span>
          </div>
          <div class="col form-group">
            <label class="control-label" for="pagfinal" style="width: 100%">Página Final</label>
            <b-form-input type="text" name="pagfinal" id="pagfinal" v-model="pagfinal" class="form-control" :class="{'is-invalid': errors.has('pagfinal') }" :placeholder="pagmax" style="width: 100%" v-validate.initial="'between:' + paginicial + ',' + pagmax + '|required'" @change="validar()"></b-form-input>
            <span v-if="false" v-show="errors.has('pagfinal')" class="help is-danger">{{ errors.first('pagfinal') }}</span>
          </div>
        </div>
        <em v-if="errormsg &amp;&amp; errormsg !== ''" for="processos" class="invalid">{{errormsg}}</em>
      </b-form>
      <div style="width: 100%" slot="modal-footer">
        <b-btn v-if="editando" variant="outline-danger" @click="remove">
          Remover
        </b-btn>
        <b-btn class="float-right ml-2" variant="primary" @click="$refs.processoPecaDetalhes.hide(true)" :disabled="errors.any()">
          Gravar
        </b-btn>
        <b-btn class="float-right" variant="secondary" @click="$refs.processoPecaDetalhes.hide(false)">
          Cancelar
        </b-btn>
      </div>
    </b-modal>
  </div>
</template>

<script>
export default {
  name: 'processo-peca-detalhes',
  data () {
    return {
      editando: false,
      texto: '',
      marcadores: [],
      intervalo: false,
      showModal: false,
      errormsg: undefined,
      paginicial: undefined,
      pagfinal: undefined,
      pagmin: undefined,
      pagmax: undefined,
      estilo: undefined,
      estilos: [
        {
          text: 'Pessoal (int)',
          value: '1',
          interno: true
        }, {
          text: 'Unidade (int)',
          value: '2',
          interno: true
        },
        {
          text: 'Pessoal (ext)',
          value: '3',
          interno: false
        }, {
          text: 'Grupo (ext)',
          value: '4',
          interno: false
        }]
    }
  },

  computed: {
    estilosfiltrados: function () {
      var interno = !!this.$parent.$parent.jwt.origin
      return this.estilos.filter((i) => i.interno === interno)
    }
  },

  watch: {
    intervalo: function (v) {
      if (v) return
      this.paginicial = this.pagmin
      this.pagfinal = this.pagmax
    }
  },

  methods: {
    show: function (marca, marcadores, pagmin, pagmax) {
      this.marcadores = marcadores
      this.pagmin = pagmin
      this.pagmax = pagmax
      this.paginicial = undefined
      this.pagfinal = undefined
      if (marca) {
        this.texto = marca.texto
        this.estilo = marca.idestilo
        this.paginicial = marca.paginicial
        this.pagfinal = marca.pagfinal
        this.editando = true
      } else {
        this.texto = undefined
        this.editando = false
        this.estilo = this.$parent.$parent.jwt.origin ? '2' : '4'
      }
      if (this.paginicial === undefined) this.paginicial = this.pagmin
      if (this.pagfinal === undefined) this.pagfinal = this.pagmax
      this.intervalo = this.paginicial !== this.pagmin || this.pagfinal !== this.pagmax
      this.showModal = true
      this.errormsg = undefined
    },

    remove: function () {
      this.$emit('remove')
      this.$refs.processoPecaDetalhes.hide(false)
    },

    validar: function () {
      this.$nextTick(() => this.$validator.validateAll())
    },

    save: function (e) {
      // Close on Esc
      if (e.isOK === undefined) e.cancel()

      // Close on cancel
      if (!e.isOK) return

      this.$validator.validateAll().then((result) => {
        if (!result) {
          e.cancel()
          return
        }
      })

      if ((this.texto || '') === '') {
        this.errormsg = 'Texto do marcador deve ser informado.'
        e.cancel()
        return
      }

      this.$emit('ok', {
        texto: this.texto,
        idestilo: this.estilo,
        intervalo: this.intervalo,
        paginicial: this.paginicial,
        pagfinal: this.pagfinal
      })
    }
  }
}
</script>
