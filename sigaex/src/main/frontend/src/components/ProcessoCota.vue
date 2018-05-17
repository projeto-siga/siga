<template>
  <div>
    <b-modal id="cota" ref="modal" v-model="showModal" title="Cota nos Autos" hide-header-close>
      <form>
        <div class="row">
          <div class="form-group col col-sm-12">
            <label class="control-label" for="texto" style="width: 100%">Texto</label>
            <b-form-input type="text" list="lst_cotas" name="texto" id="texto" v-model="texto" class="form-control" aria-describedby="cotaHelp" :class="{'is-invalid': errors.has('texto') }" style="width: 100%" autofocus v-validate.initial="'required'"></b-form-input>
            <datalist id="lst_cotas">
              <option v-for="t in textos">{{t}}</option>
            </datalist>
            <span v-if="false" v-show="errors.has('texto')" class="help is-danger">{{ errors.first('texto') }}</span>
          </div>
          <div class="form-group col col-sm-6">
            <label class="control-label" for="cargo" style="width: 100%">Cargo</label>
            <b-form-input type="text" name="cargo" id="cargo" v-model="cargo" class="form-control" :class="{'is-invalid': errors.has('cargo') }" autofocus v-validate.initial="'required'"></b-form-input>
            <span v-if="false" v-show="errors.has('cargo')" class="help is-danger">{{ errors.first('cargo') }}</span>
          </div>
          <div class="form-group col col-sm-6">
            <label class="control-label" for="empresa" style="width: 100%">Órgão</label>
            <b-form-input type="text" name="empresa" id="empresa" v-model="empresa" class="form-control" :class="{'is-invalid': errors.has('empresa') }" autofocus v-validate.initial="'required'"></b-form-input>
            <span v-if="false" v-show="errors.has('empresa')" class="help is-danger">{{ errors.first('empresa') }}</span>
          </div>
        </div>
        <small id="cotaHelp" class="form-text text-muted">
          <strong>Atenção</strong>! Ao clicar em prosseguir, o texto acima será convertido em um PDF e instantaneamente protocolado na forma de uma Petição Intercorrente. Por favor, verifique o texto antes de clicar em 'Prosseguir'.</small>
        <em v-if="errormsg &amp;&amp; errormsg !== ''" for="processos" class="invalid">{{errormsg}}</em>
      </form>
      <div style="width: 100%" slot="modal-footer">
        <b-btn variant="outline-warning" @click="preview" :disabled="errors.any()">
          Prever Cota
        </b-btn>
        <b-btn class="float-right ml-2" variant="primary" @click="save" :disabled="errors.any()">
          Prosseguir
        </b-btn>
        <b-btn class="float-right" variant="secondary" @click="$refs.modal.hide(false)">
          Cancelar
        </b-btn>
      </div>
    </b-modal>
    <pdf-preview ref="pdfPreview" title="Pré-visualização da Cota" :src="previewUrl"></pdf-preview>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import PdfPreview from './PdfPreviewModal'

var textos = [
  'Ciente, nada a opor.',
  'Devolvo os autos tendo em vista que a União não é parte.',
  'A União requer o ingresso no feito e vista dos autos após as Informações da Autoridade Coatora.',
  'A União requer o arquivamento dos autos, nos termos do art. 40 da LEF e da Portaria PGFN nº 396/2016 (RDCC).',
  'A União informa que a(s) Inscrição(ões) demanda(s) permanecem parceladas.'
]

var defaultCargoEmpresa = {
  'pgfn.gov.br': {
    cargo: 'Procurador da Fazenda Nacional',
    empresa: 'PRFN-2ªR'
  }
}

export default {
  name: 'processo-cota',

  props: ['processo', 'orgao', 'unidade'],

  mounted () {
    var company = this.$parent.$parent.jwt.company
    this.defs = company ? defaultCargoEmpresa[company] : undefined
    this.cargo = this.$parent.$parent.settings.cotaCargo
    this.empresa = this.$parent.$parent.settings.cotaEmpresa
    if (company && this.defs && !(this.cargo && this.empresa)) {
      this.cargo = this.defs.cargo
      this.empresa = this.defs.empresa
      this.validar()
    }
    console.log('Empresa: ', this.empresa, ', Cargo: ', this.cargo)
  },

  data () {
    return {
      defs: undefined,
      textos: textos,
      showModal: false,
      errormsg: undefined,
      texto: '',
      cargo: undefined,
      empresa: undefined,
      previewUrl: undefined
    }
  },

  methods: {
    show: function () {
      this.showModal = true
      this.errormsg = undefined
    },

    cancel: function (e) {
      e.cancel()
    },

    save: function () {
      if ((this.texto || '') === '') {
        this.errormsg = 'Texto deve ser informado.'
        return
      }

      this.$http.post('processo/' + this.processo + '/cota/enviar', {
        orgao: this.orgao,
        nivelsigilo: '0',
        texto: this.texto,
        cargo: this.cargo,
        empresa: this.empresa,
        unidade: this.unidade
      }, { block: true }).then(response => {
        UtilsBL.logEvento('peticionamento', 'enviar', 'cota')
        this.$emit('ok', response.data.status)
      }, error => {
        this.$emit('erro', error.data.errormsg, this.texto)
      })

      this.$refs.modal.hide(true)
    },

    preview: function () {
      this.$http.post('processo/' + this.processo + '/cota/previsao-pdf', {
        orgao: this.orgao,
        nivelsigilo: '0',
        texto: this.texto,
        cargo: this.cargo,
        empresa: this.empresa,
        unidade: this.unidade
      }, { block: true }).then(response => {
        this.previewUrl = this.$http.options.root + '/download/' + response.data.jwt + '/cota.pdf'
        this.$refs.pdfPreview.show(true)
      }, error => UtilsBL.errormsg(error, this))
    },

    validar: function () {
      this.$nextTick(() => this.$validator.validateAll())
    }
  },

  components: {
    'pdf-preview': PdfPreview
  }
}
</script>
