<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3"><span v-if="!editando">Confirmação de Recebimento de</span> Petição Inicial</h4>
      </div>
    </div>

    <div class="row" v-if="errormsg">
      <div class="col col-sm-12">
        <p class="alert alert-danger">
          <strong>Erro!</strong> {{errormsg}}
        </p>
      </div>
    </div>

    <div class="row" v-if="warningmsg">
      <div class="col col-sm-12">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> {{warningmsg}}
        </p>
      </div>
    </div>

    <div class="row" v-if="alerta &amp;&amp; !successmsg">
      <div class="col col-sm-12"><p class="alert alert-warning" v-html="alerta"></p>
      </div>
    </div>

    <template v-if="!editando">
      <div class="row">
        <div class="col col-sm-12">
          <p class="alert alert-success">
            <strong>Petição:</strong> {{protocolo}}<br/>
            <strong>Processo:</strong> {{numeroFormatado}}<br/>
            <strong>Data de Entrada:</strong> <span v-html="dataDeProtocolo"></span>
          </p>
        </div>
      </div>
    </template>

    <template>
      <div class="row pb-4" v-show="arquivos.length == 0">
        <div class="col-md-12">
          <vue-clip ref="clip" :options="vueclipOptions" :on-added-file="addedFileProxy" :on-complete="completeProxy">
            <template slot="clip-uploader-action">
              <div>
                <div class="dz-message drop-box">
                  Arraste os arquivos que compoem a petição inicial e solte eles nesta área, ou clique aqui para selecioná-los.<br />Use o nome dos PDFs para ordenar as peças, por exemplo: 01-Termo.pdf, 02-Identidade.pdf, etc.
                  <br />Cada PDF está limitado a 5MB.
                </div>
              </div>
            </template>

            <template slot="clip-uploader-body" scope="props">
              <div class="col-md-12 mt-3" v-if="hasErrorMessages(props.files)">
                <div class="alert alert-danger mb-0">
                  <strong>Arquivos inválidos!</strong> Não foi possível aceitar os seguintes arquivos:
                  <ul class="mb-0">
                    <li v-for="ifile in props.files" v-if="ifile.errorMessage">{{ifile.name}} ({{ifile.errorMessage}})</li>
                  </ul>
                </div>
              </div>
            </template>
          </vue-clip>
          <div v-if="arquivos.length == 0" class="invalid-feedback" style="display: block">É necessário fornecer os PDFs para prosseguir</div>
        </div>
      </div>

      <div class="row" v-if="arquivos.length > 0">
        <div class="col-sm-6">
          <h5 class="mt-3 mb-2">Arquivos</h5>
        </div>
        <div v-if="editando" class="col-sm-6" style="height: 100%">
          <button type="button" @click="adicionarArquivo()" class="btn btn-info btn-sm mt-2 float-right">Adicionar Arquivo
          </button>
        </div>
      </div>

      <div class="row" v-if="arquivos.length > 0">
        <div class="col-sm-12 col-md-12">
          <table class="table table-peticao table-responsive table-full-width mb-0">
            <thead class="thead-inverse">
              <tr>
                <th>Tipo</th>
                <th>Descrição</th>
                <th>Arquivo</th>
                <th>Status</th>
                <th v-if="editando" style="text-align: center"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(f, index) in arquivos" :class="{odd: f.odd}">
                <td v-if="!editando">{{localizar(f.tipo, tipospeca)}}</td>
                <td v-if="editando">
                  <select class="form-control mr-sm-2" v-model="f.tipo" :disabled="f.protocolado" @change="selecionarTipo(f, f.tipo)" :name="'tipopeca[' + index +']'" :class="{ 'is-invalid': errors.has('tipopeca[' + index +']') }" v-validate="'required'">
                    <option v-for="tipo in tipospeca" :value="tipo.id">{{tipo.nome}}</option>
                    <option disabled hidden selected value=""> [Selecionar]</option>
                  </select>
                </td>

                <td>
                  <span v-if="!editando">{{f.nome}}</span>
                  <div v-if="editando" class="input-group">
                    <input type="text" class="form-control" style="min-width: 16em;" placeholder="Descrição" v-model="f.nome" @input="alterarArquivo(f)" :disabled="f.bloq || f.protocolado">
                  </div>
                </td>

                <td>
                  <a @click="view(doc)">
                    <a :href="'api/v1/arquivo-temporario/' + f.id" target="_blank">{{f.nome}}</span>
                    </a>
                  </a>
                </td>

                <td class="status-td" :rowspan="f.protocolado ? f.rowspan : 1" v-show="f.protocolado ? !f.anexo : true">
                  <span v-show="f.file.progress &amp;&amp; f.file.progress < 100">{{f.file.progress.toFixed(1)}}%</span>
                  <span :class="{green: f.protocolado}" v-show="f.file.progress === 100 &amp;&amp; !f.errormsg">{{f.status}}</span>
                  <span v-show="f.errormsg" :class="{red: true}">{{f.errormsg}}</span>
                  <span v-show="f.$error">{{f.$error}} {{f.$errorParam}}</span>
                </td>

                <td v-if="editando" align="right">
                  <a href="" @click.prevent="removerArquivo(f)">
                    <span class="fa fa-remove icone-em-linha"></span>
                  </a>
                </td>
              </tr>
            </tbody>
          </table>
          <div v-if="faltaTeorDaPeticao" class="invalid-feedback" style="display: block">É necessário informar o teor da petição</div>
        </div>
      </div>

      <div class="row mt-3">
        <div class="col-md-12">
          <h5 class="mt-3 mb-2">Dados Básicos</h5>
        </div>
      </div>
      <div class="pt-3 pb-3 pl-3 pr-3" style="background-color: rgb(233, 236, 239)">
        <div class="row">
          <div class="form-group col-md-2">
            <my-select name="orgao" label="Órgão" v-model="orgao" :list="orgaos" @change="selecionarOrgao" :edit="editando" v-validate="'required'" :error="errors.first('orgao')"></my-select>
          </div>
          <div class="form-group col-md-3">
            <my-select :disabled="localidades.length == 0" name="localidade" label="Localidade" v-model="localidade" :list="localidades" @change="selecionarLocalidade" :edit="editando" v-validate="'required'" :error="errors.first('localidade')"></my-select>
          </div>
          <div class="form-group col-md-2">
            <my-select :disabled="especialidades.length == 0" name="especialidade" label="Especialidade" v-model="especialidade" :list="especialidades" @change="selecionarEspecialidade" :edit="editando" v-validate="'required'" :error="errors.first('especialidade')"></my-select>
          </div>
          <div class="form-group col-md-3">
            <my-select :disabled="classes.length == 0" name="classe" label="Classe" v-model="classe" :list="classes" @change="selecionarClasse" :edit="editando" v-validate="'required'" :error="errors.first('classe')"></my-select>
          </div>

          <div class="form-group col-md-2">
            <my-input :disabled="!classe" name="valorcausa" label="Valor da Causa (R$)" v-model="valorcausa" :edit="editando" placeholder="0,00" mask="money" v-validate="valordacausaobrigatorio ? 'required|min:5|max:14' : ''" :error="errors.first('valorcausa')"></my-input>
          </div>

          <div class="form-group col-md-6" v-if="ef">
            <label for="cda">CDA</label>
            <div v-if="!editando">{{cda}}</div>
            <input v-if="editando" type="text" class="form-control" :class="{ 'is-invalid': errors.has('cda') }" v-model="cda" name="cda" placeholder="" />
            <small id="cdaHelp" class="form-text text-muted">Se houver mais de uma, separar com vírgulas.</small>
          </div>

          <div class="form-group col-md-6" v-if="ef">
            <label for="pa">Processo Administrativo</label>
            <div v-if="!editando">{{pa}}</div>
            <input v-if="editando" type="text" class="form-control" :class="{ 'is-invalid': errors.has('pa') }" v-model="pa" name="pa" placeholder="" />
            <small id="paHelp" class="form-text text-muted">Se houver mais de um, separar com vírgulas.</small>
          </div>

          <div class="form-check col-md-3">
            <label class="form-check-label"> <input :disabled="!editando" type="checkbox" class="form-check-input" v-model="nivelsigilo"> Segredo de Justiça
            </label>
          </div>

          <div class="form-check col-md-3">
            <label class="form-check-label"> <input :disabled="!editando" type="checkbox" class="form-check-input" v-model="justicagratuita"> Justiça Gratuita
            </label>
          </div>
          <div class="form-check col-md-3">
            <label class="form-check-label"> <input :disabled="!editando" type="checkbox" class="form-check-input" v-model="tutelaantecipada"> Tutela Liminar/Antecipada
            </label>
          </div>
          <div class="form-check col-md-3">
            <label class="form-check-label"> <input :disabled="!editando" type="checkbox" class="form-check-input" v-model="prioridadeidoso"> Prioridade de Idoso
            </label>
          </div>
        </div>
      </div>

      <div class="row mt-3">
        <div class="col col-auto">
          <h5 class="mt-3 mb-2">Partes</h5>
        </div>
        <div v-if="editando" class="col col-auto ml-auto">
          <button type="button" @click="adicionarParte()" class="btn btn-info btn-sm mt-2">Adicionar Parte</button>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-12">
          <table class="table table-peticao table-responsive table-full-width">
            <thead class="thead-inverse" @click="validar()">
              <tr>
                <th>Polo</th>
                <th>Tipo</th>
                <th>Documento</th>
                <th>Nome</th>
                <th v-if="editando" style="text-align: center"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(p, index) in partes">
                <td v-if="!editando">{{localizar(p.polo, polos)}}</td>
                <td v-if="editando">
                  <select class="form-control mr-sm-2" v-model="p.polo" @change="selecionarPolo(p, p.polo)">
                    <option disabled hidden selected value="">[Selecionar]</option>
                    <option v-for="polo in polos" :value="polo.id">{{poloNome(polo.nome)}}</option>
                  </select>
                </td>
                <td v-if="!editando">{{localizar(p.tipopessoa, tipospessoa)}}</td>
                <td v-if="editando">
                  <select class="form-control mr-sm-2" v-model="p.tipopessoa" @change="selecionarTipoPessoa(p, p.tipopessoa)">
                    <option disabled hidden selected value="">[Selecionar]</option>
                    <option v-for="tipopessoa in tipospessoa" :value="tipopessoa.id">{{tipopessoa.nome}}</option>
                  </select>
                </td>

                <td v-if="!editando">{{p.documento}}</td>
                <td v-if="editando" :colspan="p.tipopessoa == '3' ? 2 : 1">
                  <my-input v-if="p.tipopessoa == '1'" :disabled="!orgao" :name="'documento[' + index +']'" v-model="p.documento" :edit="editando" placeholder="CPF" mask="999.999.999-99" @change="alterouCpf(p)" v-validate="'required|cpf'" :error="errors.first('documento[' + index +']')"></my-input>

                  <input type="text" :disabled="!orgao" class="form-control mr-sm-2" :class="{ 'is-invalid': errors.has('documento[' + index +']') }" v-model="p.documento" :name="'documento[' + index +']'" placeholder="CNPJ" v-if="p.tipopessoa == '2'" v-validate="'required|cnpj'" v-mask="'99.999.999/9999-99'" @change="alterouCnpj(p)" />

                  <select :disabled="!orgao" class="form-control mr-sm-2" :class="{ 'is-invalid': errors.has('documento[' + index +']') }" v-model="p.documento" :name="'documento[' + index +']'" placeholder="Entidade" v-if="p.tipopessoa == '3'" v-validate="'required'" @change="alterouEntidade(p)">
                    <option disabled selected hidden :value="undefined">[Selecionar]</option>
                    <option v-for="l in entidadesFiltradas" :value="l.documento">{{l.nome}}</option>
                  </select>

                  <input type="text" :disabled="!orgao" class="form-control mr-sm-2" :class="{ 'is-invalid': errors.has('documento[' + index +']') }" v-model="p.documento" :name="'documento[' + index +']'" placeholder="OAB" v-if="p.tipopessoa == '4'" v-validate="'required|oab'" v-mask="'AA999999'" @change="alterouOab(p)" v-on:blur="fixOab(p)"/>
                </td>

                <td v-if="!editando">{{p.nome}}</td>
                <td v-if="editando &amp;&amp; p.tipopessoa !== '3'">
                  <my-input :disabled="!orgao" :name="'nome[' + index +']'" v-model="p.nome" :edit="editando" placeholder="Nome Completo" v-validate="'required'" :error="errors.first('nome[' + index +']')"></my-input>
                </td>

                <td v-if="editando" align="right">
                  <a href="" @click.prevent="removerParte(p)">
                    <span class="fa fa-remove icone-em-linha"></span>
                  </a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="row align-items-center d-print-none">
        <div v-if="editando" class="col-sm-12">
          <button type="button " @click="peticionar()" id="prosseguir" :disabled="arquivos.length==0 || faltaTeorDaPeticao || !isAllValid() || errors.any()" class="btn btn-primary float-right d-print-none">Protocolar</button>
        </div>

        <template v-if="!editando">
          <div class="col-sm-auto">
            <button type="button " @click="limpar()" id="voltar" class="btn btn-secondary d-print-none">Enviar Outra Petição</button>
          </div>
          <div class="col-sm-auto">
            <div>
              <label class="form-check-label pl-0">
                <input type="checkbox" v-model="manterCampos">
                Manter preenchimento anterior
              </label>
            </div>
          </div>
          <div class="col-sm-auto ml-auto">
            <button type="button" @click="imprimir()" id="imprimir" class="btn btn-info float-right ml-3">Imprimir</button>
          </div>
        </template>
      </div>
    </template>
  </div>
</template>

<script>
import AuthBL from '../bl/auth.js'
import ProcessoBL from '../bl/processo.js'
import UtilsBL from '../bl/utils.js'
import CnjClasseBL from '../bl/cnj-classe.js'
import { Bus } from '../bl/bus.js'
import AwesomeMask from 'awesome-mask'
import ValidacaoBL from '../bl/validacao.js'
import MySelect from './MySelect'
import MyInput from './MyInput'

const polos = [{
  id: 1,
  nome: 'Ativo'
}, {
  id: 2,
  nome: 'Passivo'
}]

const partes = [{ polo: 1, tipopessoa: '1', documento: undefined, nome: undefined }, { polo: 2, tipopessoa: '1', documento: undefined, nome: undefined }]

const tipospeca = [{
  id: 1,
  nome: 'Teor da Petição'
}, {
  id: 2,
  nome: 'Cópia de CPF/CNPJ'
}, {
  id: 3,
  nome: 'Comprovante de Residência'
}, {
  id: 4,
  nome: 'Outros Documentos'
}, {
  id: 5,
  nome: 'Outros Documentos Sigilosos'
}, {
  id: 11,
  nome: 'Inquérito/Procedimento Criminal'
}]

const tipospessoa = [{
  id: '1',
  nome: 'Pessoa Física'
}, {
  id: '2',
  nome: 'Pessoa Jurídica'
}, {
  id: '3',
  nome: 'Entidade'
}, {
  id: '4',
  nome: 'Advogado'
}]

const alertas = {
  'jfrj': '<strong>Atenção</strong>: O serviço de petição inicial pela internet não deve ser utilizado para o ajuizamento de TURMA RECURSAL e plantão judiciário. Nestes casos, as petições devem ser protocoladas por meio físico, junto a Seção de Distribuição da TURMA RECURSAL ou no JUÍZO DE PLANTÃO, conforme o caso.' +
  '<br><br>Nos casos de demandas destinadas aos Juizados Especiais Federais, sendo o autor domiciliado nos Municípios de Seropédica ou Itaguaí, ou ainda, nos bairros de Santa Cruz, Paciência, Sepetiba, Campo Grande, Cosmos, Guaratiba, Barra de Guaratiba, Pedra de Guaratiba, Inhoaíba, Santíssimo, Senador Camará, ou Senador Vasconcelos, selecione a localidade CAMPO GRANDE.'
}

export default {
  name: 'peticao-inicial',

  mounted () {
    this.$nextTick(() => {
      this.carregarOrgaos()
      this.carregarEntidades()
    })
  },

  data () {
    return {
      // remover
      aorgao2: undefined,
      files: [],
      invalidFiles: [],

      polos: polos,
      partes: JSON.parse(JSON.stringify(partes)),
      tipospeca: tipospeca,
      tipospessoa: tipospessoa,

      alertas: alertas,

      orgao: undefined,
      localidade: undefined,
      especialidade: undefined,
      classe: undefined,
      valorcausa: undefined,

      cda: undefined,
      pa: undefined,

      nivelsigilo: false,
      justicagratuita: false,
      tutelaantecipada: false,
      prioridadeidoso: false,

      entidades: [],
      orgaos: [],
      localidades: [],
      especialidades: [],
      classes: [],

      tipos: [],
      protocolo: undefined,
      dataDeProtocolo: undefined,
      numero: undefined,
      editando: true,

      resumoPorData: [],
      filtroProtocolo: undefined,
      mostrarQuantidadePorData: undefined,
      quantidadePorData: [],
      arquivos: [],
      arquivoCorrente: undefined,
      errormsg: undefined,
      warningmsg: undefined,
      successmsg: undefined,
      manterCampos: false,
      vueclipOptions: {
        url: this.$http.options.root + '/../upload',
        headers: {
          Authorization: AuthBL.getIdToken()
        },
        maxFilesize: {
          limit: 5,
          message: '{{ filesize }} tamanho é maior que {{ maxFilesize }}'
        },
        acceptedFiles: {
          extensions: ['application/pdf'],
          message: 'Só é permitido o envio de PDFs'
        }
      }
    }
  },

  computed: {
    entidadesFiltradas: function () {
      if (!this.entidades || !this.orgao) return []
      var org = this.orgao.toUpperCase()
      var a = this.entidades.filter((item) => {
        return item.orgao === org
      })
      return a
    },

    ef: function () {
      if (!this.classe) return false
      var c = this.classe.split('|')[0]
      // Substituir pelo uso de um parâmetro de retorno referente à classe escolhida
      return c === '1116' || c === '203' || c === '99'
    },

    alerta: function () {
      return this.alertas[this.orgao]
    },

    valordacausaobrigatorio: function() {
      if (!this.classes || !this.classe) return false
      for (var i = 0; i < this.classes.length; i++) {
        if (this.classe === this.classes[i].id) return this.classes[i].valordacausaobrigatorio
      }
      return false
    },

    faltaTeorDaPeticao: function() {
      if (this.arquivos.length === 0) return true
      for (var i = 0; i < this.arquivos.length; i++) {
        if (this.arquivos[i].tipo === 1) return false
      }
      return true
    }
  },

  methods: {
    //
    // Selects
    //
    carregar: function (url, items, item) {
      this.$http.get(url, { block: true }).then(response => {
        this[items].length = 0
        this[item] = undefined
        for (var i = 0; i < response.data.list.length; i++) this[items].push(response.data.list[i])
        // if (items === 'classes') {
        // for (i = 0; i < this.classes.length; i++) this.classes[i].nome = CnjClasseBL.nomeCompleto(this.classes[i].id)
        // }
        this[items].sort(function (a, b) {
          if (a.nome !== b.nome) return a.nome < b.nome ? -1 : 1
          return 0
        })
        // if (!this[item]) this[item] = response.data.list[0].id

        // Desabilitando especialidades criminais enquanto não temos a possibilidade de informar dados de inquérito
        if (items === 'especialidades') {
          for (i = 0; i < this.especialidades.length; i++) {
            if (this.especialidades[i].nome.toUpperCase() === 'CRIMINAL') {
              this.especialidades.splice(i, 1)
              return
            }
          }
        }
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    },

    carregarEntidades: function () {
      this.carregar('config/entidades', 'entidades', 'entidade')
    },

    carregarOrgaos: function () {
      this.carregar('config/orgaos', 'orgaos', 'orgao')
    },

    selecionarOrgao: function () {
      this.localidade = undefined
      this.localidades.length = 0
      this.especialidade = undefined
      this.especialidades.length = 0
      this.classe = undefined
      this.classes.length = 0
      this.carregarLocalidades()
    },

    carregarLocalidades: function () {
      this.carregar('config/localidades?orgao=' + this.orgao, 'localidades', 'localidade')
    },

    selecionarLocalidade: function () {
      this.especialidade = undefined
      this.especialidades.length = 0
      this.classe = undefined
      this.classes.length = 0
      this.carregarEspecialidades()
    },

    carregarEspecialidades: function () {
      this.carregar('config/localidade/' + this.localidade + '/especialidades?orgao=' + this.orgao, 'especialidades', 'especialidade')
    },

    selecionarEspecialidade: function () {
      this.classe = undefined
      this.classes.length = 0
      this.carregarClasses()
    },

    selecionarClasse: function () {
      this.$nextTick(() => {
        this.validar()
      })
    },

    carregarClasses: function () {
      this.carregar('config/localidade/' + this.localidade + '/especialidade/' + this.especialidade + '/classes?orgao=' + this.orgao, 'classes', 'classe')
    },

    //
    // Arquivos
    //
    addedFileProxy: function (file) {
      this.arquivos.push({
        file: file,
        nome: file.name,
        bloq: false,
        protocolado: undefined,
        status: undefined,
        validando: undefined,
        valido: undefined,
        orgao: undefined,
        errormsg: undefined,
        tipo: undefined,
        tipodescr: undefined,
        segredo: undefined
      })
    },

    completeProxy: function (file, status, xhr) {
      var arq = this.arquivo(file)
      if (file.status === 'error') {
        arq.errormsg = file.errorMessage
        return
      }
      var json = JSON.parse(xhr.response)
      arq.size = json.size
      arq.id = json.id
      this.validarArquivo(arq)
      this.organizarArquivos()
    },

    hasErrorMessages: function (arr) {
      if (!arr) return false
      for (var i = 0; i < arr.length; i++) {
        if (arr[i].errorMessage) return true
      }
      return false
    },

    validarArquivo: function (arq) {
      var a = arq
      if (a.processo) {
        a.status = 'Validando...'
        a.validando = true
        a.valido = false
        this.$http.get('processo/' + ProcessoBL.somenteNumeros(a.processo) + '/validar', { block: true }).then(response => {
          var d = response.data
          a.status = d.unidade + '/' + d.orgao
          a.orgao = d.orgao
          a.validando = false
          a.valido = true
        }, error => {
          a.validando = false
          a.valido = false
          a.errormsg = error.data.errormsg
        })
      }
    },

    alterarArquivo: function (arq) {
      this.validarArquivo(arq)
      this.organizarArquivos()
    },

    adicionarArquivo: function () {
      this.$refs.clip.$el.firstChild.click()
    },

    removerArquivo: function (arq) {
      for (var i = 0; i < this.arquivos.length; i++) {
        if (arq === this.arquivos[i]) this.arquivos.splice(i, 1)
      }
      this.organizarArquivos()
    },

    selecionarTipo: function (arq, tipo) {
      this.organizarArquivos()
    },

    selecionarSegredo: function (arq, segredo) {
      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        if (a !== arq && a.segredo === undefined) a.segredo = segredo
      }
    },

    organizarArquivos: function () {
      this.arquivos.sort(function (a, b) {
        if (a.tipo && !b.tipo) return -1
        if (!a.tipo && b.tipo) return 1
        if (a.tipo !== b.tipo) return a.tipo < b.tipo ? -1 : 1
        if (a.nome !== b.nome) { return a.nome.replace('.pdf', '') < b.nome.replace('.pdf', '') ? -1 : 1 }
        return 0
      })

      var arq = { odd: false }

      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        a.odd = !arq.odd
        arq = a
      }
    },

    arquivo: function (file) {
      for (var j = 0; j < this.arquivos.length; j++) {
        if (this.arquivos[j].file === file) return this.arquivos[j]
      }
    },

    isAllValid: function () {
      for (var j = 0; j < this.arquivos.length; j++) {
        if (this.arquivos[j].file.status !== 'success') return false
        if (!this.arquivos[j].tipo) return false
      }
      return true
    },

    //
    // Partes
    //

    adicionarParte: function () {
      this.partes.push({ polo: 2, tipopessoa: 1, documento: undefined, nome: undefined })
      this.validar()
    },

    removerParte: function (parte) {
      this.partes.splice(this.partes.indexOf(parte), 1)
    },

    selecionarPolo: function (parte, polo) {
      this.ordenarPartes()
      this.validar()
    },

    poloNome: function(nome) {
      if (this.classe) {
        var c = this.classe.split('|')[0]
        var n
        if (nome === 'Ativo') {
          n = CnjClasseBL.poloAtivo(c)
        } else {
          n = CnjClasseBL.poloPassivo(c)
        }
        if (n) return n
      }
      return nome
    },

    selecionarTipoPessoa: function (parte, tipoPessoa) {
      this.ordenarPartes()
      this.validar()
    },

    validar: function () {
      this.$nextTick(() => this.$validator.validateAll())
    },

    ordenarPartes: function () {
      this.partes.sort((a, b) => {
        if (a.polo && !b.polo) return -1
        if (!a.polo && b.polo) return 1
        if (a.polo !== b.polo) return a.polo < b.polo ? -1 : 1
        if (a.tipopessoa && !b.tipopessoa) return -1
        if (!a.tipopessoa && b.tipopessoa) return 1
        if (a.tipopessoa !== b.tipopessoa) return a.tipopessoa < b.tipopessoa ? -1 : 1
        return 0
      })
    },

    alterouCpf: function(parte) {
      var cpf = ProcessoBL.somenteNumeros(parte.documento)
      if (!cpf || !ValidacaoBL.validarCPF(cpf)) {
        parte.nome = undefined
        this.validar()
        return
      }
      this.$http.get('config/pessoa-fisica/' + cpf + '?orgao=' + this.orgao, { block: true }).then(response => {
        parte.nome = response.data.nome
        this.validar()
      }, error => UtilsBL.errormsg(error, this))
    },

    alterouCnpj: function(parte) {
      var cnpj = ProcessoBL.somenteNumeros(parte.documento)
      if (!cnpj || !ValidacaoBL.validarCNPJ(cnpj)) {
        parte.nome = undefined
        this.validar()
        return
      }
      this.$http.get('config/pessoa-juridica/' + cnpj + '?orgao=' + this.orgao, { block: true }).then(response => {
        parte.nome = response.data.nome
        this.validar()
      }, error => UtilsBL.errormsg(error, this))
    },

    alterouOab: function(parte) {
      var oab = parte.documento

      if (oab) {
        oab = oab.toUpperCase()
        parte.documento = oab
      }

      if (!oab || !ValidacaoBL.validarOAB(oab)) {
        parte.nome = undefined
        this.validar()
        return
      }
      this.$http.get('config/advogado/' + oab + '?orgao=' + this.orgao, { block: true }).then(response => {
        parte.nome = response.data.nome
        window.setTimeout(() => this.validar(), 100)
      }, error => UtilsBL.errormsg(error, this))
    },

    fixOab: function(parte) {
      var oab = parte.documento
      if (!oab) return
      if (oab.length < 6) return
      while (oab.length < 8) oab = oab.substring(0, 2) + '0' + oab.substring(2)
      this.$set(parte, 'documento', oab)
      this.alterouOab(parte)
      window.setTimeout(() => this.validar(), 100)
    },

    alterouEntidade: function(parte) {
      parte.nome = undefined
      for (var i = 0; i < this.entidadesFiltradas.length; i++) {
        var e = this.entidadesFiltradas[i]
        if (e.documento === parte.documento) {
          parte.nome = e.nome
        }
      }
      this.validar()
    },

    //
    // Peticionar
    //
    peticionar: function () {
      this.errormsg = undefined
      this.$validator.validateAll().then((result) => {
        if (!result) {
          this.errormsg = 'Por favor, verifique o preenchimento dos campos marcados em vermelho e repita a operação.'
          return
        } else {
          this.peticionarEnviar()
        }
      })
    },

    peticionarEnviar: function() {
      var pdfs, classificacoes, i
      for (i = 0; i < this.arquivos.length; i++) {
        if (pdfs) pdfs += ','
        else pdfs = ''
        pdfs += this.arquivos[i].id

        if (classificacoes) classificacoes += ','
        else classificacoes = ''
        classificacoes += this.arquivos[i].tipo
      }

      this.$http.post('peticao-inicial/protocolar', {
        orgao: this.orgao,
        localidade: this.localidade,
        especialidade: this.especialidade,
        classe: this.classe,
        valorcausa: this.valorcausa,
        cdas: this.ef ? this.cda : undefined,
        pas: this.ef ? this.pa : undefined,
        nivelsigilo: this.nivelsigilo ? 1 : 0,
        justicagratuita: this.justicagratuita,
        tutelaantecipada: this.tutelaantecipada,
        prioridadeidoso: this.prioridadeidoso,
        partes: JSON.stringify(this.partes),
        pdfs: pdfs,
        classificacoes: classificacoes,
        nomepoloativo: this.poloNome('Ativo'),
        nomepolopassivo: this.poloNome('Passivo')
      }, { block: true }).then(response => {
        this.successmsg = response.data.status
        this.protocolo = response.data.protocolo
        this.dataDeProtocolo = UtilsBL.formatJSDDMMYYYYHHMM(response.data.data)
        this.numero = response.data.numero
        this.numeroFormatado = ProcessoBL.formatarProcesso(this.numero)
        this.editando = false
        UtilsBL.logEvento('peticionamento', 'enviar', 'petição inicial')
      }, error => UtilsBL.errormsg(error, this))
    },

    limpar: function () {
      this.successmsg = undefined
      this.editando = true
      this.protocolo = undefined
      this.dataDeProtocolo = undefined
      this.numero = undefined
      this.numeroFormatado = undefined
      if (!this.manterCampos) {
        this.arquivos.length = 0
        this.orgao = undefined
        this.localidade = undefined
        this.especialidade = undefined
        this.classe = undefined

        this.nivelsigilo = false
        this.justicagratuita = false
        this.tutelaantecipada = false
        this.prioridadeidoso = false

        this.valorcausa = undefined
        this.cda = undefined
        this.pa = undefined
        this.$set(this, 'partes', JSON.parse(JSON.stringify(partes)))
      }
      this.ordenarPartes()
      this.validar()
    },

    localizar: function (id, array) {
      for (var i = 0; i < array.length; i++) {
        if (array[i].id === id) return array[i].nome
      }
      return ''
    },

    imprimir: function () {
      window.print()
    }
  },

  components: {
    MySelect,
    MyInput
  },

  directives: {
    'mask': AwesomeMask
  }
}
</script>

<style scoped>
.protocolos-header {
  font-size: 150%;
  padding-bottom: 0.5rem;
}

.unbreakable {
  white-space: nowrap;
  word-break: keep-all;
  hyphens: none;
}

TABLE.table-peticao TBODY TD {
  vertical-align: top;
}

@media print {
  .table-peticao {
    font-size: 10pt;
  }
  .table-protocolo {
    font-size: 8pt;
  }
  .table-full-width {
      width: 100%;
  }
  .col-md-1, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6, .col-md-7, .col-md-8, .col-md-9, .col-md-10, .col-md-11, .col-md-12 {
      float: left;
  }
  .col-md-12 {
      width: 100%;
  }
  .col-md-11 {
      width: 91.66666667%;
  }
  .col-md-10 {
      width: 83.33333333%;
  }
  .col-md-9 {
      width: 75%;
  }
  .col-md-8 {
      width: 66.66666667%;
  }
  .col-md-7 {
      width: 58.33333333%;
  }
  .col-md-6 {
      width: 50%;
  }
  .col-md-5 {
      width: 41.66666667%;
  }
  .col-md-4 {
      width: 33.33333333%;
  }
  .col-md-3 {
      width: 25%;
  }
  .col-md-2 {
      width: 16.66666667%;
  }
  .col-md-1 {
      width: 8.33333333%;
  }
}
</style>
