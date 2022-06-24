<template>
  <div class="hello">
    <div class="container-fluid content profile">
      <div class="row pt-5" v-if="errormsg">
        <div class="col col-sm-12">
          <p class="alert alert-danger">
            <strong>Erro!</strong> {{ errormsg }}
          </p>
        </div>
      </div>
      <div class="row pt-5" v-if="warningmsg">
        <div class="col col-sm-12">
          <p class="alert alert-warning">
            <strong>Atenção!</strong> {{ warningmsg }}
          </p>
        </div>
      </div>
      <div v-if="!errormsg &amp;&amp; doc">
        <div class="row xd-print-block mt-3 mb-3">
          <div class="col-md-12">
            <h4 class="text-center mb-0">
              {{ doc.forma }} {{ doc.mobs[0].sigla }}
            </h4>
          </div>
          <div class="col-md-12">
            <h6
              class="text-center mb-0 mt-2"
              v-html="doc.mobs[0].marcadoresEmHtml"
            ></h6>
          </div>
        </div>
        <div class="row no-gutters mt-2">
          <!--
                    <div class="col col-auto mr-1" v-if="!$parent.settings.filtrarMovimentos">
                      <button type="button" @click="filtrarMovimentos('')" class="btn btn-secondary d-print-none">
                        <span class="fa fa-filter"></span>
                        Filtrar Movimentos
                      </button>
                    </div>
                    <div class="col col-auto mr-1 mb-3" v-if="filtro !== '#marca'">
                      <button type="button" @click="filtrarMovimentos('#marca')" class="btn btn-secondary d-print-none">
                        <span class="fa fa-filter"></span>
                        Filtrar Marcas
                      </button>
                    </div>
                    -->
        </div>
        <template v-if="doc">
          <div class="row">
            <div class="col col-12 col-lg-8">
              <div
                class="d-print-none"
                v-if="errormsg === undefined &amp;&amp; doc.conteudoBlobHtmlString"
              >
                <div class="card-deck">
                  <div class="card card-consulta-processual mb-3">
                    <div class="card-body">
                      <p class="card-text">
                        <span v-html="doc.conteudoBlobHtmlString"></span>
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <my-iframe
                v-if="!doc.conteudoBlobHtmlString"
                :src="pdfSource"
              ></my-iframe>
              <table
                v-if="filteredMovs && filteredMovs.length"
                class="table table-sm table-striped"
              >
                <thead class="table-dark">
                  <tr>
                    <th>Tempo</th>
                    <th>Lotação</th>
                    <th>Evento</th>
                    <th>Descrição</th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-bind:class="[mov.classe, mov.disabled]"
                    v-for="mov in filteredMovs"
                    :key="mov.idMov"
                  >
                    <td>{{ mov.tempoRelativo }}</td>
                    <td>{{ mov.lotaCadastranteSigla }}</td>
                    <td>{{ mov.exTipoMovimentacaoSigla }}</td>
                    <td style="padding: 5px 5px; word-break: break-all;">
                      {{ mov.descricao }}
                      <span v-if="mov.idTpMov != 2">{{ mov.complemento }}</span>
                      <span
                        v-if="mov.descricao &amp;&amp; mov.acoes &amp;&amp; mov.acoes.length !== 0"
                        >|</span
                      >
                      <span v-for="acao in mov.acoes" :key="acao.nome"
                        >{{ acao.pre }}
                        <a
                          v-if="acao.acao"
                          href=""
                          @click.prevent="executar(mov, acao)"
                          >{{ acao.nome }}</a
                        ><span v-if="!acao.acao">{{ acao.nome }}</span>
                        {{ acao.pos }}</span
                      >
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="col col-12 col-lg-4">
              <h4>Ações</h4>
              <span v-for="acao in filteredAcoes" :key="acao.nome">
                <acao :acao="acao" />
              </span>

              <!-- PENDENCIAS -->
              <div class="card text-white bg-danger mb-3" v-if="mob.pendencias">
                <div class="card-header">Pendências</div>
                <div class="card-body">
                  <div v-if="mob.pendenciasDeAnexacao">
                    <p>
                      <b style="color: rgb(195, 0, 0)">Anexos Pendentes:</b>
                    </p>
                    <ul>
                      <li
                        v-for="anexoPendente in mob.pendenciasDeAnexacao"
                        :key="anexoPendente.idMov"
                      >
                        {{ anexoPendente.descricao }}
                      </li>
                    </ul>
                  </div>
                  <div v-if="mob.anexosNaoAssinados">
                    <p>
                      <b style="color: rgb(195, 0, 0)">Anexos não assinados:</b>
                    </p>
                    <ul>
                      <li
                        v-for="naoAssinado in mob.anexosNaoAssinados"
                        :key="naoAssinado.idMov"
                      >
                        {{ naoAssinado.descricao }}
                      </li>
                    </ul>
                  </div>
                  <div v-if="mob.despachosNaoAssinados">
                    <p>
                      <b style="color: rgb(195, 0, 0)"
                        >Despachos não assinados:</b
                      >
                    </p>
                    <ul>
                      <li
                        v-for="naoAssinado in mob.despachosNaoAssinados"
                        :key="naoAssinado.idMov"
                      >
                        {{ naoAssinado.descricao }}
                      </li>
                    </ul>
                  </div>
                  <div v-if="mob.expedientesJuntadosNaoAssinados">
                    <p>
                      <b style="color: rgb(195, 0, 0)"
                        >Expedientes juntados não assinados:</b
                      >
                    </p>
                    <ul></ul>
                  </div>
                  <div v-if="mob.expedientesFilhosNaoJuntados">
                    <p>
                      <b style="color: rgb(195, 0, 0)"
                        >Expedientes não juntados:</b
                      >
                    </p>
                    <ul>
                      <li
                        v-for="naoJuntado in mob.expedientesFilhosNaoJuntados"
                        :key="naoJuntado.idMov"
                      >
                        {{ naoJuntado.sigla }}
                      </li>
                    </ul>
                  </div>
                  <div v-if="mob.pendenciasDeColaboracao">
                    <p>
                      <b style="color: rgb(195, 0, 0)"
                        >Pendências de Colaboração:</b
                      >
                    </p>
                    <ul>
                      <li
                        v-for="colaboracaoPendente in mob.pendenciasDeColaboracao"
                        :key="colaboracaoPendente.idMov"
                      >
                        {{ colaboracaoPendente.descricao }}
                      </li>
                    </ul>
                  </div>
                </div>
              </div>

              <!-- RELACAO -->
              <div class="card bg-light mb-3" v-show="relacao">
                <div class="card-header">Documentos Relacionados</div>
                <div class="card-body" v-html="relacao"></div>
              </div>

              <!-- TRAMITACAO -->
              <div class="card bg-light mb-3" v-show="tramitacao">
                <div class="card-header">Tramitação</div>
                <div class="card-body" v-html="tramitacao"></div>
              </div>

              <!-- DETALHES -->
              <div class="card bg-light">
                <div class="card-header">
                  Documento {{ doc.exTipoDocumentoDescricao }}
                </div>
                <div class="card-body" style="font-size: 70%;">
                  <p><b>Suporte:</b> {{ doc.fisicoOuEletronico }}</p>
                  <p>
                    <b>Data:</b> {{ doc.dtDocDDMMYY }}
                    <span v-if="doc.originalData"
                      ><b>original:</b> {{ doc.originalData }}</span
                    >
                  </p>
                  <p v-if="doc.originalNumero">
                    <b>Número original:</b> {{ doc.originalNumero }}
                  </p>
                  <p><b>De:</b> {{ doc.subscritorString }}</p>
                  <p><b>Para:</b> {{ doc.destinatarioString }}</p>
                  <p>
                    <b>Cadastrante:</b> {{ doc.cadastranteString }}
                    {{ doc.lotaCadastranteString }}
                  </p>
                  <p><b>Espécie:</b> {{ doc.forma }}</p>
                  <p><b>Modelo:</b> {{ doc.modelo }}</p>
                  <p id="descricao">
                    <b>Descrição:</b> {{ doc.descrDocumento }}
                  </p>
                  <p>
                    <b>Classificação:</b>
                    {{ doc.classificacaoDescricaoCompleta }}
                  </p>
                  <div v-if="doc.dadosComplementares">
                    {{ doc.dadosComplementares }}
                  </div>
                  <div v-if="false &amp;&amp; doc.cossignatarios">
                    <h6>Cossignatários</h6>
                    <ul>
                      <li
                        v-for="cossig in doc.cossignatarios"
                        :key="cossig.idMov"
                      >
                        {{ cossig.key.subscritor.nomePessoa }}
                      </li>
                    </ul>
                  </div>
                  <!--
                              <c:if test="{{not empty doc.doc.perfis}}">
                                <div class="gt-sidebar-content" style="padding-top: 10px">
                                  <h3>Perfis</h3>
                                  <c:forEach var="perfil" items="{{doc.doc.perfis}}">
                                    <p style="margin-bottom: 3px;">
                                      <b>{{perfil.key.descPapel}}:</b>
                                    </p>
                                    <ul>
                                      <c:forEach var="pessoaOuLota" items="{{perfil.value}}">
                                        <li><c:catch var="exception">{{pessoaOuLota.nomePessoa}}</c:catch>
                                          <c:if test="{{not empty exception}}">{{pessoaOuLota.nomeLotacao}}</c:if>
                                        </li>
                                      </c:forEach>
                                    </ul>
                                  </c:forEach>
                                </div>
                              </c:if>
                              -->
                  <div>
                    <h6>Nível de Acesso</h6>
                    <div>
                      <b>{{ doc.nmNivelAcesso }}</b>
                      <div v-if="doc.listaDeAcessos">
                        <div v-if="doc.listaDeAcessos.length == 1">
                          {{
                            doc.listaDeAcessos[0] == "PUBLICO"
                              ? "(Público)"
                              : acesso.sigla
                          }}
                        </div>
                        <div v-if="doc.listaDeAcessos.length > 1">
                          <ul>
                            <li
                              v-for="acesso in doc.listaDeAcessos"
                              :key="acesso"
                            >
                              {{ acesso.sigla }}
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";
import { Bus } from "../bl/bus.js";
import Acao from "./Acao";

export default {
  name: "documento",
  mounted() {
    this.$on("filtrar", (texto) => {
      this.filtrarMovimentos(texto);
    });
    this.$nextTick(function() {
      this.carregarDocumento(this.$route.params.numero);
    });
  },
  data() {
    return {
      fixed: undefined,
      modified: undefined,
      numero: undefined,
      orgao: undefined,
      perfil: undefined,
      gui: {},
      filtro: undefined,
      errormsg: undefined,
      warningmsg: undefined,
      partes: false,
      dadosComplementares: false,
      doc: undefined,
      mob: undefined,
      marcadores: [],
      marcasativas: true,
      notas: false,
      tramitacao: undefined,
      relacao: undefined,
    };
  },
  watch: {
    "$route.params.numero": function() {
      this.carregarDocumento(this.$route.params.numero);
    },
  },
  computed: {
    filteredMovs() {
      if (!this.mob || !this.mob.movs || this.mob.movs.length == 0)
        return undefined;
      return this.mob.movs.filter((m) => m.idTpMov != 14 && !m.cancelada);
    },
    filteredAcoes() {
      if (!this.mob) return undefined;
      var acoes =
        this.doc.mobs.length > 1
          ? this.mob.acoes.concat(this.doc.mobs[1].acoes)
          : this.mob.acoes;
      acoes = acoes.sort((a, b) =>
        a.nome > b.nome ? 1 : b.nome > a.nome ? -1 : 0
      );
      return acoes.filter((m) => m.pode);
    },
    pdfSource() {
      if (!this.doc || this.doc.conteudoBlobHtmlString) return undefined;
      return (
        this.$http.options.root +
        "sigaex/api/v1/documentos/" +
        this.numero +
        "/arquivo/produzir?contenttype=application/pdf"
      );
    },
  },
  methods: {
    executar: function(mov, acao) {
      if (
        mov.descrTipoMovimentacao === "Anexação" &&
        (acao.nome === "Assinar/Autenticar" || acao.nome === "Excluir")
      ) {
        this.$router.push({
          name: "Anexo",
          params: { numero: this.numero, id: mov.idMov },
        });
      } else if (
        mov.descrTipoMovimentacao.includes("Inclusão de Cossignatário") &&
        acao.nome === "Excluir"
      ) {
        Bus.$emit(
          "excluirMovimentacao",
          [{ codigo: this.numero, sigla: this.doc.sigla }],
          this.carregarDocumento,
          {
            idMov: mov.idMov,
          }
        );
        this.emitir();
      } else if (acao.acao === "exibir") {
        this.$router.push({
          name: "Documento",
          params: { numero: acao.params.sigla.replace(/[^a-z0-9]/gi, "") },
        });
      }
    },

    carregarDocumento: function() {
      this.errormsg = undefined;
      this.numero = this.$route.params.numero;
      // Validar o número do processo
      Bus.$emit("block", 20);
      this.$http.get("sigaex/api/v1/documentos/" + this.numero).then(
        (response) => {
          Bus.$emit("release");
          this.atualizarDocumento(response.data);
        },
        (error) => {
          Bus.$emit("release");
          UtilsBL.errormsg(error, this);
        }
      );
    },

    atualizarDocumento: async function(data) {
      this.doc = data;
      this.mob = this.doc.mobs[0];
      if (!this.mob.isGeral)
        this.numero = this.mob.sigla.replace(/[^a-zA-Z0-9]/gi, "");
      if (this.$parent.test.properties["vizservice.url"]) {
        this.tramitacao = await this.computeGraph(this.doc.vizTramitacao);
        if (this.doc.vizRelacaoDocs && this.doc.vizRelacaoDocs.length > 200)
          this.relacao = await this.computeGraph(this.doc.vizRelacaoDocs);
      }
    },

    async computeGraph(dot) {
      if (!dot) return;
      var response = await this.$http.post(
        (location.port === "8081" ? "http://localhost:8080/" : "") +
          "siga/public/app/graphviz/svg",
        'digraph G { graph[tooltip="Tramitação"] ' + dot + "}",
        {
          headers: { "Content-Type": "text/vnd.graphviz" },
        }
      );
      if (!response || !response.data) return;
      var result = response.data.replace(
        /width="\d+pt" height="\d+pt"/gm,
        'style="left:0; top:0; width:100%; height:12em; display:block; margin: auto;"'
      );
      result = result.replace(/<polygon fill="white".+?\/>/gm, "");
      return result;
    },

    reler: function() {
      console.log("relendo");
      this.$http
        .get("sigaex/api/v1/documentos/" + this.numero, { block: true })
        .then(
          (response) => {
            this.atualizarDocumento(response.data);
          },
          (error) => {
            UtilsBL.errormsg(error, this);
          }
        );
    },

    getMarcadores: function() {
      // Carregar os marcadores da classe
      this.$http
        .get(
          "classe/" + this.proc.dadosBasicos.classeProcessual + "/marcadores"
        )
        .then(
          (response) => {
            if (!response.data.list) return;
            for (var i = 0; i < response.data.list.length; i++) {
              this.marcadores.push(response.data.list[i].texto);
            }
          },
          (error) => {
            if (error.data.errormsg === "disabled") {
              this.marcasativas = false;
              return;
            }
            UtilsBL.errormsg(error, this);
          }
        );
    },
    getMarcas: function() {
      // Carregar os marcadores da classe
      this.$http
        .get("processo/" + this.numero + "/marcas?orgao=" + this.orgao)
        .then(
          (response) => {
            // if (!response.data.list) return
            for (var i = 0; i < response.data.list.length; i++) {
              var marca = response.data.list[i];
              for (var j = 0; j < this.fixed.movdoc.length; j++) {
                var movdoc = this.fixed.movdoc[j];
                if (movdoc.doc && movdoc.doc.idDocumento === marca.idpeca) {
                  movdoc.marca.push({
                    idmarca: marca.idmarca,
                    texto: marca.texto,
                    idestilo: marca.idestilo,
                    paginicial: marca.paginicial,
                    pagfinal: marca.pagfinal,
                  });
                }
              }
            }
          },
          (error) => {
            if (error.data.errormsg === "disabled") {
              this.marcasativas = false;
              return;
            }
            UtilsBL.errormsg(error, this);
          }
        );
    },
    mostrarDadosComplementares: function(ativo) {
      this.$parent.$emit("setting", "mostrarDadosComplementares", ativo);
    },
    mostrarProcessosRelacionados: function(ativo) {
      this.$parent.$emit("setting", "mostrarProcessosRelacionados", ativo);
    },
    mostrarPeca: function(idDocumento, disposition) {
      this.$http
        .get(
          "processo/" +
            this.numero +
            "/peca/" +
            idDocumento +
            "/pdf?orgao=" +
            this.orgao
        )
        .then(
          (response) => {
            var jwt = response.data.jwt;
            var url =
              this.$http.options.root +
              "/download/" +
              jwt +
              "/" +
              this.numero +
              "-peca-" +
              idDocumento +
              ".pdf";
            if (disposition) window.location = url + "?disposition=attachment";
            else window.open(url);
            UtilsBL.logEvento("consulta-processual", "mostrar pdf peça");
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },
    mostrarCompleto: function() {
      this.$http
        .get(
          "sigaex/api/v1/documentos/" +
            this.numero +
            "/arquivo?estampa=true&completo=true"
        )
        .then(
          (response) => {
            Bus.$emit(
              "prgAsyncStart",
              "PDF Completo",
              response.data.uuid,
              () => {
                var jwt = response.data.jwt;
                window.open(
                  this.$http.options.root +
                    "sigaex/api/v1//download/" +
                    jwt +
                    "/" +
                    this.numero +
                    ".pdf"
                );
              }
            );
            UtilsBL.logEvento(
              "consulta-processual",
              "mostrar pdf completo",
              "individual"
            );
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },
    filtrarMovimentos: function(texto) {
      this.$parent.$emit("setting", "filtrarMovimentos", texto !== undefined);
      var f = this.filtro;
      if (texto) {
        if (
          texto.length > 0 &&
          texto.substring(0, 1) === "#" &&
          f &&
          f.length > 0 &&
          f.substring(0, 1) === "#"
        ) {
          this.filtro = f + " " + texto;
          return;
        }
      }
      this.filtro = texto;
      this.$nextTick(() => this.$refs.filtro.focus());
    },
    mostrarPartes: function(ativo) {
      this.$parent.$emit("setting", "mostrarPartes", ativo);
    },
    imprimir: function() {
      window.print();
    },
    formatDDMMYYYHHMM: function(s) {
      if (s === undefined) {
        return;
      }
      var r =
        s.substring(6, 8) +
        "/" +
        s.substring(4, 6) +
        "/" +
        s.substring(0, 4) +
        " " +
        s.substring(8, 10) +
        ":" +
        s.substring(10, 12);
      r = r.replace(" ", "&nbsp;");
      return r;
    },

    exibirProcessoPecaDetalhes: function(movdoc, marca) {
      this.currentMovDoc = movdoc;
      this.currentMarca = marca;
      this.$refs.processoPecaDetalhes.show(
        marca,
        this.marcadores,
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaInicial
          : undefined,
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaFinal
          : undefined
      );
    },

    cotar: function() {
      this.$refs.Assinatura.show();
    },

    cotaEnviada: function(msg) {
      Bus.$emit("message", "Sucesso", "Cota enviada com sucesso. " + msg);
    },

    cotaNaoEnviada: function(msg, texto) {
      Bus.$emit(
        "message",
        "Erro",
        'Não foi possível enviar a cota "' +
          texto +
          '". Ocorreu o erro: "' +
          msg +
          '"'
      );
    },

    salvarProcessoPecaDetalhes: function(marca) {
      if (!this.currentMovDoc) return;

      var movdoc = this.currentMovDoc;
      var inicial =
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaInicial
          : undefined;
      var final =
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaFinal
          : undefined;
      if (inicial === marca.paginicial && final === marca.pagfinal) {
        marca.paginicial = undefined;
        marca.pagfinal = undefined;
      }

      var data = {
        idclasse: this.proc.dadosBasicos.classeProcessual,
        idmarca: this.currentMarca ? this.currentMarca.idmarca : undefined,
        texto: marca.texto,
        idestilo: marca.idestilo,
        paginicial: marca.paginicial,
        pagfinal: marca.pagfinal,
      };

      this.$http
        .post(
          "processo/" +
            this.numero +
            "/peca/" +
            this.currentMovDoc.doc.idDocumento +
            "/marca?orgao=" +
            this.orgao,
          data,
          { block: true }
        )
        .then(
          (response) => {
            if (this.currentMarca) {
              var index = this.currentMovDoc.marca.indexOf(this.currentMarca);
              UtilsBL.overrideProperties(marca, response.data.marca);
              UtilsBL.overrideProperties(
                this.currentMovDoc.marca[index],
                marca
              );
            } else {
              UtilsBL.overrideProperties(marca, response.data.marca);
              this.currentMovDoc.marca.push(marca);
            }
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },

    excluirProcessoPecaDetalhes: function() {
      if (!this.currentMovDoc || !this.currentMarca) return;

      this.$http
        .delete("marca/" + this.currentMarca.idmarca, { block: true })
        .then(
          () => {
            var index = this.currentMovDoc.marca.indexOf(this.currentMarca);
            if (index > -1) this.currentMovDoc.marca.splice(index, 1);
          },
          (error) => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },

    favoritar: function(favorito) {
      this.errormsg = undefined;
      this.$http
        .post(
          "processo/" + this.numero + "/sinalizar",
          { favorito: favorito },
          { block: true }
        )
        .then(
          (response) => {
            var d = response.data;
            this.favorito = d.processo.favorito;
          },
          (error) => {
            this.warningmsg = error.data.errormsg;
          }
        );
    },

    mostrarNotas: function(show) {
      this.$parent.$emit("setting", "mostrarNotas", show);

      this.$nextTick(() => {
        this.$refs.notaUnidade.focus();
        this.notasAlteradas();
      });
    },

    notasAlteradas: function() {
      if (this.notaUnidade !== undefined && this.notaUnidade.trim() === "") {
        this.notaUnidade = undefined;
      }
      if (this.notaPessoal !== undefined && this.notaPessoal.trim() === "") {
        this.notaPessoal = undefined;
      }
      this.$refs.notaUnidade.style.height = "5px";
      this.$refs.notaPessoal.style.height = "5px";
      var h = Math.max(
        this.$refs.notaUnidade.scrollHeight,
        this.$refs.notaPessoal.scrollHeight
      );
      this.$refs.notaUnidade.style.height = h + "px";
      this.$refs.notaPessoal.style.height = h + "px";
    },
  },

  components: {
    acao: Acao,
  },
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
.marca-ref:hover,
.marca-ref:link,
.marca-ref:visited,
.marca-ref:active {
  color: black;
}

.inquebravel {
  white-space: nowrap;
}

.marca {
  padding-left: 0rem;
  padding-right: 0rem;
  margin-right: 0.5rem;
  margin-bottom: 0;
  margin-top: 0rem;
}

.marca-yellow {
  background-color: yellow;
}

.marca-blue {
  background-color: #41f1f4;
}

.marca-green {
  background-color: #00ff00;
}

.marca-pink {
  background-color: #faf;
}

.red {
  color: red;
}

.protocolado {
  color: green;
}

.odd {
  background-color: rgba(0, 0, 0, 0.05);
}

.card-consulta-processual div p b {
  color: #fff;
}

.card-consulta-processual div p {
  margin-bottom: 0.5rem;
}

.card-consulta-processual div i {
  line-height: 3rem;
  height: 3rem;
  color: #fff;
  float: right;
  font-size: 4rem;
  margin: 0rem -0.5rem 0rem 0rem;
}

.card-text-descr {
  margin-bottom: 0;
}

textarea {
  border: none;
  background: none;
  width: 100%;
  resize: none;
  overflow: hidden;
  min-height: 50px;
}

table.mov tr.despachox {
  background-color: rgb(240, 255, 240);
}

table.mov tr.juntadax,
tr.desentranhamentox {
  background-color: rgb(229, 240, 255);
}

table.mov tr.anotacaox {
  background-color: rgb(255, 255, 255);
}

table.mov tr.anexacaox {
  background-color: rgb(255, 255, 215);
}

table.mov tr.encerramento_volumex {
  background-color: rgb(255, 218, 218);
}

.card-body p {
  margin-bottom: 0.2em;
}

.card-body div {
  margin-top: 1em;
}

.card-body div h6 {
  margin-bottom: 0.2em;
}
</style>
