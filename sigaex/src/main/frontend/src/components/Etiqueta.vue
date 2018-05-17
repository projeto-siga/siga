<template>
  <div>
    <b-modal ref="etiqueta" id="etiqueta" title="Etiqueta" close-title="Cancelar" ok-title="Salvar Marcador" hide-header-close no-close-on-esc @hide="save">
      <b-form>
        <div class="row">
          <div class="col col-md-2 form-group">
            <label class="control-label" for="sigla" style="width: 100%">Sigla</label>
            <b-form-input type="text" name="sigla" id="sigla" v-model="sigla" class="form-control" :class="{'is-invalid': errors.has('sigla') }" style="width: 100%" v-validate.initial="'required'"></b-form-input>
            <span v-if="false" v-show="errors.has('sigla')" class="help is-danger">{{ errors.first('sigla') }}</span>
          </div>
          <div class="col col-md-6 form-group">
            <label class="control-label" for="nome" style="width: 100%">Nome</label>
            <b-form-input type="text" name="nome" id="nome" v-model="nome" class="form-control" :class="{'is-invalid': errors.has('nome') }" style="width: 100%" autofocus v-validate.initial="'required'"></b-form-input>
            <span v-if="false" v-show="errors.has('nome')" class="help is-danger">{{ errors.first('nome') }}</span>
          </div>
          <div class="col col-md-4">
            <label class="control-label" for="estilo" style="width: 100%">Modalidade</label>
            <b-form-select v-model="estilo" :options="estilosfiltrados" class="mb-3">
            </b-form-select>
          </div>
          <div class="col col-md-12 form-group">
            <label class="control-label" for="descricao" style="width: 100%">Descrição</label>
            <b-form-input type="text" name="descricao" id="descricao" v-model="descricao" class="form-control" :class="{'is-invalid': errors.has('descricao') }" style="width: 100%"></b-form-input>
            <span v-if="false" v-show="errors.has('descricao')" class="help is-danger">{{ errors.first('descricao') }}</span>
          </div>
          <div class="col col-md-12 form-group">
            <label class="control-label" for="processos" style="width: 100%">Processos</label>
            <textarea type="text" name="processos" id="processos" v-model="processos" class="form-control" :class="{'is-invalid': errors.has('processos') }" style="width: 100%"></textarea>
            <span v-if="false" v-show="errors.has('processos')" class="help is-danger">{{ errors.first('processos') }}</span>
          </div>
        </div>
        <em v-if="errormsg &amp;&amp; errormsg !== ''" for="processos" class="invalid">{{errormsg}}</em>
      </b-form>
      <div style="width: 100%" slot="modal-footer">
        <b-btn v-if="editando" variant="outline-danger" @click="remove">
          Remover
        </b-btn>
        <b-btn class="float-right ml-2" variant="primary" @click="$refs.etiqueta.hide(true)" :disabled="errors.any()">
          Gravar
        </b-btn>
        <b-btn class="float-right" variant="secondary" @click="$refs.etiqueta.hide(false)">
          Cancelar
        </b-btn>
      </div>
    </b-modal>
  </div>
</template>

<script>
export default {
  name: 'etiqueta',
  data () {
    return {
      editando: false,
      nome: '',
      sigla: '',
      descricao: '',
      processos: undefined,
      errormsg: undefined,
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

  methods: {
    show: function (id, sigla, nome, descricao, estilo) {
      this.id = id
      this.sigla = sigla
      this.nome = nome
      this.descricao = descricao
      this.editando = !!id
      if (this.editando) {
        this.estilo = estilo
      } else {
        this.estilo = this.$parent.$parent.jwt.origin ? '2' : '4'
      }
      this.$refs.etiqueta.show()
      this.errormsg = undefined
    },

    remove: function () {
      this.$emit('remove')
      this.$refs.edita.hide(false)
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

      this.$emit('ok', {
        id: this.id,
        idestilo: this.estilo,
        sigla: this.sigla,
        nome: this.nome,
        descricao: this.descricao
      })
    }
  }
}
</script>
