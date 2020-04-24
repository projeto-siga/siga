<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>

<siga:pagina titulo="Mesa Virtual">
<script type="text/javascript" src="../javascript/vue.min.js"></script>
	<c:set var="siga_mesaCarregaLotacao" scope="session" value="${f:resource('siga.mesa.carrega.lotacao')}" />
	<div id="app" class="container-fluid content" >
		<div id="configMenu" class="mesa-config ml-auto" :class="toggleConfig">
			<button type="button" class="btn-mesa-config btn btn-secondary btn-sm h-100" @click="toggleMenuConfig();">
				<i class="fas fa-cog"></i>
			</button>
			<div class="form-config border-bottom p-3">
				<h6>Configurações da Mesa Virtual</h6>
	            <i><small>Quanto menos informações forem solicitadas, mais rapidamente a mesa carregará.</small></i>
<!-- 		            <div class="form-group my-2 border-bottom"> -->
<!-- 						<div class="form-check"> -->
<!-- 							<input type="checkbox" class="form-check-input" v-model="versaoMesa"> -->
<!-- 							<label class="form-check-label" for="versaoMesa"><small>Utilizar versão antiga da Mesa</small></label> -->
<!-- 						</div>             -->
<!-- 		            </div> -->
	            <div class="form-group my-2 border-bottom">
					<div class="form-check">
						<input type="checkbox" class="form-check-input" id="trazerAnotacoes" v-model="trazerAnotacoes" @click="recarregarMesa();">
						<label class="form-check-label" for="trazerAnotacoes"><small>Trazer anotações nos documentos</small></label>
					</div>            
	            </div>
	            <div class="form-group my-2 border-bottom">
					<div class="form-check">
						<input type="checkbox" class="form-check-input" id="trazerComposto" v-model="trazerComposto" @click="recarregarMesa();">
						<label class="form-check-label" for="trazerComposto">
							<small>Exibir indicador de docto. avulso <i class="far fa-file"></i> ou composto <i class="far fa-copy"></i></small>
						</label>
					</div>            
	            </div>
				<div class="form-group pb-2 mb-1 border-bottom">
					<label for="selQtdPagId"><small>Qtd. de documentos a trazer</small></label>
					<select class="form-control form-control-sm" v-model="selQtdPag">
						  <option value="5">5</option>
						  <option value="10">10</option>
						  <option value="15">15</option>
						  <option value="50">50</option>
						  <option value="100">100</option>
						  <option value="500">500</option>
					</select>
				</div>
				<div class="form-group pt-2">
			    	<button class="btn btn-secondary btn-sm h-60" @click="resetaStorage();">Resetar Mesa</button>
			    	<p><small>Retorna as configurações iniciais da mesa.</small></p>
			    </div>
        	</div>
        </div>	
		<c:if test="${not empty acessoAnteriorData}">
			<div class="row" id="row-bem-vindo">
				<div class="col">
					<p id="bem-vindo" class="alert alert-success mb-3 mb-0">Último
						acesso em ${acessoAnteriorData} no endereço
						${acessoAnteriorMaquina}.</p>
				</div>
			</div>
		</c:if>
		<div id="inicio" class="row">
			<div class="col col-12 col-md-auto mb-2">
				<h3><i class="fa fa-file-alt"></i> Mesa Virtual</h3>
			</div>
			<div class="col col-md-4 my-1 mb-2" style="vertical-align: text-bottom;"> 
				<span class="h-100 d-inline-block my-2" style="vertical-align: text-bottom;">
					<c:if test="${not empty visualizacao}"><b>(Delegante: ${visualizacao.titular.nomePessoa})</b></c:if> 
				</span> 
			</div> 
			<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos') && (cadastrante.orgaoUsuario.isExternoOrgaoUsu eq 0 && lotaTitular.isExternaLotacao eq 0)}">
				<div class="col col-12 col-sm-4 col-md-auto pr-md-0 ml-md-auto mb-2">
					<a href="expediente/doc/editar" class="btn btn-success form-control"> <i class="fas fa-plus-circle mr-1"></i>
						<fmt:message key="documento.novo"/></a>
				</div>
				<div class="col col-12 col-sm-4 col-md-auto mb-2">
					<a href="expediente/doc/listar?primeiraVez=sim" class="btn btn-primary form-control">
						<i class="fas fa-search mr-1"></i><fmt:message key="documento.pesquisar"/> 
					</a>
				</div>
			</c:if>
		</div>
		<div id="rowTopMesa" style="z-index: 1" class="row sticky-top px-3 pt-1 bg-white shadow-sm" 
				v-if="!carregando || (!errormsg &amp;&amp; grupos.length >= 0)">
			<c:if test="${siga_mesaCarregaLotacao && !ehPublicoExterno}">
				<div id="radioBtn" class="btn-group mr-2 mb-1" 
						title="Visualiza somente os documentos do usuário ou da lotação">
					<a class="btn btn-primary btn-sm" v-bind:class="exibeLota ? 'notActive' : 'active'" id="btnUser"  
						accesskey="u" @click="carregarMesaUser('#btnUser');">
						<i class="fas fa-user my-1"></i>
						<span class="d-none d-sm-inline">Usuário</span>
					</a>
					<a class="btn btn-primary btn-sm" v-bind:class="exibeLota ? 'active' : 'notActive'" id="btnLota"  
						accesskey="l" @click="carregarMesaLota('#btnLota');">
						<i class="fas fa-users my-1"></i>
						<span class="d-none d-sm-inline"><fmt:message key='usuario.lotacao'/></span>
					</a>
				</div>
			</c:if>
			<div class="mr-2 mb-1">
				<input id="filtroExibidos" type="text" class="form-control p-1 input-sm" placeholder="Filtrar documentos da mesa" v-model="filtro" ng-model-options="{ debounce: 200 }">
			</div>
			<button type="button" class="btn btn-secondary btn-sm mb-1 mr-2" title="Recarregar Mesa" :class="{disabled: carregando}" @click="recarregarMesa();">
				<i class="fas fa-sync-alt"></i>
			</button>
			<small id="ultima-atualizacao" class="my-auto fadein text-danger">
				Última atualização: {{getLastRefreshTime()}}</small>
		</div>
		
		<div class="row mt-2" v-if="!carregando && errormsg">
			<div class="col col-12">
				<p class="alert alert-danger">
					<strong>{{errormsg}}</strong> 
				</p>
			</div>
		</div>

		<div class="row d-print-none"
			v-if="!carregando &amp;&amp; grupos.length > 0"></div>

		<div class="row mt-3" v-if="carregando">
			<div class="col col-12">
				<p class="alert alert-warning">
					<span class="spinner-border" role="status"></span>
					<strong>Aguarde,</strong> carregando documentos...
				</p>
			</div>
		</div>

		<div class="row mt-3"
			v-if="!carregando &amp;&amp; !errormsg &amp;&amp; grupos.length == 0">
			<div class="col col-12">
				<p class="alert alert-warning">
					<strong>Atenção!</strong> Nenhum documento na mesa.
				</p>
			</div>
		</div>
		<div class="row mt-2" style="min-height: 50vh;">
			<div class="col-sm-12">
				<template v-for="g in filtrados">	
					<div v-if="g.grupoCounterUser > 0 || g.grupoCounterLota > 0" :key="g.grupoOrdem">
						<div class="collapse-header d-inline">
							<h5 :id="['collapse-header-' + g.grupoOrdem]" data-toggle="collapse" :data-target="['#collapsetab-' + g.grupoOrdem]" 
									class="collapse-toggle p-1 table-group table-group-title"
									v-bind:class="{ collapsed: g.grupoCollapsed}" v-bind:aria-expanded="{false: g.grupoCollapsed}"
									@click="collapseGrupo(g.grupoOrdem, g.grupoNome)">
								<i class="h5 mb-0" :class="g.grupoIcone"></i>
								<span class="mr-3">{{g.grupoNome}}</span>
								<small> 
									<span class="badge badge-light btn-sm align-middle" :class="{disabled: exibeLota}">
										<small class="fas fa-user"></small>
										<span class="badge badge-light">{{g.grupoCounterUser}}</span>
									</span>
									<c:if test="${siga_mesaCarregaLotacao && !ehPublicoExterno}">
										<span class="badge badge-light btn-sm align-middle" :class="{disabled: !exibeLota}">
											<small class="fas fa-users"></small>
											<span class="badge badge-light">{{g.grupoCounterLota}}</span>
										</span>
									</c:if>
								</small>
							</h5>
						</div>
						<div :id="['collapsetab-' + g.grupoOrdem]" class="collapse" :key="g.grupoOrdem" v-bind:class="{show: !g.grupoCollapsed }">
							<div class="row" v-if="!carregando && (g.grupoDocs == undefined || g.grupoDocs.length == 0)">
								<div class="col col-12">
									<p class="alert alert-warning alert-dismissible fade show">Não há documentos a exibir para est{{exibeLota? 'a lotação.' : 'e usuário.'}}</p>
								</div>
							</div>
							<table v-else class="text-size-6 text-muted table table-sm table-striped table-hover table-borderless">
								<thead v-if="!carregando">
									<tr class="table-head d-flex">
										<th scope="col" class="col-1 d-none d-md-block">Tempo</th>
										<th scope="col" class="col-9 col-md-2"><fmt:message key = "usuario.mesavirtual.codigo"/></th>
										<th scope="col" class="col-4 d-none d-md-block">Descrição</th>
										<th scope="col" class="col-3 col-md-2">Origem</th>
										<th scope="col" class="col-3 d-none d-md-block"><fmt:message key = "usuario.mesavirtual.etiquetas"/></th>
<!-- 											<th v-show="gruposTemAlgumErro">Atenção</th> -->
									</tr>
								</thead>
								<tbody>
									<template v-for="f in g.grupoDocs">
										<tr class="d-flex">
											<td class="col-1 d-none d-md-block" 
												:title="f.datahoraDDMMYYYHHMM">{{f.tempoRelativo}}</td>
											<td class="col-9 col-md-2">
												<c:if test="${siga_cliente == 'GOVSP'}">
													<span v-if="trazerComposto">
														<span v-if="f.tipoDoc == 'Avulso'"><i class="far fa-file text-secondary" title="Documento Avulso"></i></span>
														<span v-else><i class="far fa-copy" title="Documento Composto"></i></span>
													</span>
												</c:if>
												<c:choose>
													<c:when test="${not empty idVisualizacao && idVisualizacao != 0}">
														<a :href="'expediente/doc/visualizar?sigla=' + f.codigo + '&idVisualizacao='+${idVisualizacao}">{{f.sigla}}</a>
													</c:when>
													<c:otherwise>
														<a :href="'expediente/doc/exibir?sigla=' + f.codigo">{{f.sigla}}</a>
													</c:otherwise>
												</c:choose>
												<span class="d-inline d-md-none"> - {{f.descr}}</span>
											</td>
											<td class="col-4 d-none d-md-block">
												<span class="text-break" :title='f.descr' v-if="f.descr.length<60">{{f.descr}}</span>
												<span class="text-break" :title='f.descr' v-else>{{ f.descr.substring(0,60)+"..." }}</span>												
											</td>
											<td class="col-3 col-md-2">
												<c:if test="${siga_cliente == 'GOVSP'}">
													<span v-if="f.dataDevolucao == 'ocultar'"></span>
													<span v-if="f.dataDevolucao == 'alerta'"><i class="fa fa-exclamation-triangle text-warning"></i></span>
													<span v-if="f.dataDevolucao == 'atrasado'"><i class="fa fa-exclamation-triangle text-danger"></i></span>
												</c:if>
												{{f.origem}}
											</td>
											<td class="col-3 d-none d-md-block" style="padding: 0;">
												<span v-if="f.anotacao != null">
													<a tabindex="0" class="anotacao fas fa-sticky-note text-warning popover-dismiss ml-1" role="button" 
															data-toggle="popover" data-trigger="hover focus" :data-content="f.anotacao"></a>
												</span>
												<span class="xrp-label-container">
													<span v-for="m in f.list" :title="m.titulo">
														<span class="text-size-8 badge badge-pill badge-secondary tagmesa m-1 btn-xs">												
<!-- 														<button	class="btn btn-default btn-sm xrp-label"> -->
															<i :class="m.icone"></i> {{m.nome}}<span
																v-if="m.pessoa &amp;&amp; !m.daPessoa"> -
																{{m.pessoa}}/ </span><span
																v-if="m.unidade &amp;&amp; (!m.daLotacao || (!m.daPessoa && !m.deOutraPessoa))">
																{{m.unidade}}</span>
														</span>
													</span>
												</span>
											</td>
<!-- 												<td v-show="gruposTemAlgumErro" style="color: red">{{f.errormsg}}</td> -->
										</tr>
									</template>
								</tbody>
							</table>
							<div class="row">
								<div class="col col-md-9 mb-2">
									<div class="text-center" v-if="carregando">
										<div class="spinner-grow text-info text-center" role="status"></div>
									</div>
								</div>
								<div class="col-6 col-md-3 mb-2">
									<div class="float-right d-flex">
										<button	type="button" class="btn btn-primary btn-sm"
												v-if="g.grupoDocs != undefined && g.grupoCounterAtivo > g.grupoDocs.length" 
												:class="{disabled: carregando}" @click="pageDownGrupo(g.grupoNome);">
											Mais<i v-if="!carregando" class="ml-2 fas fa-chevron-circle-down"></i>
											<div v-if="carregando" class="spinner-border" role="status"></div>
										</button>
										<button type="button" class="ml-2 btn btn-light btn-sm"
											v-if="g.grupoDocs != undefined && g.grupoCounterAtivo > 0" 
											@click="fecharGrupo(g.grupoOrdem, g.grupoNome);">
											<i class="fas fa-chevron-circle-up text-secondary"></i>
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</template>
			</div>
		</div>
		<p class="alert alert-success"
			v-if="acessos &amp;&amp; acessos.length >= 1">Último acesso em
			{{acessos[1].datahora}} no endereço {{acessos[1].ip}}.</p>
		<div class="scroll-arrows">
			<p>
				<a class="btn btn-light btn-circle shadow-sm" href="#inicio"> 
					<i class="fas fa-chevron-up h6"></i>
				</a>
			</p>
			<p>
				<a class="btn btn-light btn-circle shadow-sm" href="#final"> 
					<i class="fas fa-chevron-down h6"></i>
				</a>
			</p>
		</div>
		<div id="final"></div>		
	</div>
</siga:pagina>

<script type="text/javascript" language="Javascript1.1">
	setTimeout(function() {
		$('#bem-vindo').fadeTo(1000, 0, function() {
			$('#row-bem-vindo').slideUp(1000);
		});
		$("#ultima-atualizacao").removeClass("text-danger");
		$("#ultima-atualizacao").addClass("text-secondary");
	}, 5000);
	 
	var appMesa = new Vue({
		  el: "#app",

		  mounted: function() {
			this.errormsg = undefined;
	      	var self = this
			self.exibeLota = (getParmUser('exibeLota') === 'true');
	      	self.trazerAnotacoes = (getParmUser('trazerAnotacoes') == null ? self.trazerAnotacoes : getParmUser('trazerAnotacoes'));
	      	self.trazerComposto = (getParmUser('trazerComposto') == null ? self.trazerComposto : getParmUser('trazerComposto'));
  	        self.carregarMesa();
		  },
		  data: function() {
		    return {
		      mesa: undefined,
		      filtro: undefined,
		      resp: undefined,
		      grupos: [],
		      todos: {},
		      carregando: false,
		      primeiraCarga: true,
		      exibeLota: undefined,
		      acessos: [],
		      errormsg: undefined,
		      toggleConfig: '',
		      selQtd: undefined,
		      selQtdPag: 15,
		      trazerAnotacoes: true,
		      trazerComposto: false,
		    };
		  },

		  computed: {
		    filtrados: function() {
		      var grps = JSON.parse(JSON.stringify(this.grupos));
		      for (g in grps) {
		    	  if (grps[g].grupoDocs != undefined) {
			    	  var a = grps[g].grupoDocs;
	    			  var grupo;
				      var odd = false;
				      a = this.filtrarPorSubstring(
				        a,
				        this.filtro,
				        "grupoNome,tempoRelativo,sigla,codigo,descr,origem,situacao,errormsg,list.nome,dataDevolucao".split(
				          ","
				        )
				      );
				      a = a.filter(function(item) {
				        return item.grupo !== "NENHUM";
				      });
				      grps[g].grupoDocs = a;
		    	  }
		      } 
		      this.$nextTick(function () {		      
	        	$('.popover-dismiss').popover({
	        		html: true,
	        		animation: false,
	        		trigger: 'manual'
	        	}).on("mouseenter", function () {
	                var _this = this;
	                $(this).popover("show");
	                $(".popover").on("mouseleave", function () {
	                    $(_this).popover('hide');
	                });
	            }).on("mouseleave", function () {
	                var _this = this;
	                setTimeout(function () {
	                    if (!$(".popover:hover").length) {
	                        $(_this).popover("hide");
	                    }
	                }, 300);
		      	})
		      });
		      return grps;
		    },

		    filtradosTemAlgumErro: function() {
		      if (!this.filtrados || this.filtrados.length === 0) return false;
		      for (var i = 0; i < this.filtrados.length; i++) {
		        if (this.filtrados[i].errormsg) return true;
		      }
		      return false;
		    }
		  },
		  watch: {
			selQtdPag: function(qtdPag) {
				var parms = JSON.parse(getParmUser('gruposMesa'));
				if (parms != null) {
					for (var g in parms) {
			        	setValueGrupo(g, 'qtdPag', qtdPag);
			        	if (parms[g].collapsed == false) {
			        		setValueGrupo(g, 'qtd', qtdPag);
			        	}
					}
				}
				this.recarregarMesa();
			},
			trazerAnotacoes: function() {
				setParmUser('trazerAnotacoes', this.trazerAnotacoes);
			},
			trazerComposto: function() {
				setParmUser('trazerComposto', this.trazerComposto);
			},
		  },		  
			  
		  methods: {
		    carregarMesa: function(grpNome, qtdPagina) {
		      var self = this
		      var timeout = Math.abs(new Date() - 
		    		  new Date(sessionStorage.getItem('timeout' + getUser())));
		      if (timeout < 120000 && grpNome == null) {
				carregaFromJson(sessionStorage.getItem('mesa' + getUser()), self);
				
				return;
		      }
		      if (this.carregando || timeout < 120000) 
		    	  return;
		      this.carregando = true;
		      var erros = {};
		      if (this.grupos && this.grupos.length > 0) {
		        for (var i = 0; i < this.grupos.length; i++) {
		          erros[this.grupos[i].codigo] = this.grupos[i].errormsg;
		        }
		      }
		  	  var parmGrupos = JSON.parse(getParmUser('gruposMesa'));
		  	  var parms = {};
		  	  if (grpNome != null) {
		    	  parms[grpNome] = {'grupoOrdem' : parmGrupos[grpNome].ordem, 
		    			'grupoQtd' : parseInt(parmGrupos[grpNome].qtd) + qtdPagina, 
		    			'grupoQtdPag' : parmGrupos[grpNome].qtdPag, 'grupoCollapsed' : parmGrupos[grpNome].collapsed }; 
		          setValueGrupo(grpNome, 'qtd', parseInt(parmGrupos[grpNome].qtd) + qtdPagina);
		      } else {
		    	  var i=0;
				  for (p in parmGrupos) {
		    	    parms[p] = {'grupoOrdem' : parmGrupos[p].ordem,	'grupoQtd' : parseInt(parmGrupos[p].qtd), 
			    		'grupoQtdPag' : parmGrupos[p].qtdPag, 'grupoCollapsed' : parmGrupos[p].collapsed }; 
				  }
		      }
		      $.ajax({ 
		          type: 'GET', 
		          url: 'mesa2.json', 
			          data: {parms: JSON.stringify(parms),
		        	  	 exibeLotacao: getParmUser('exibeLota'),
		        	  	 trazerAnotacoes: getParmUser('trazerAnotacoes'),
		        	  	 trazerComposto: getParmUser('trazerComposto'),
		        	  	 idVisualizacao: ${idVisualizacao}
		        	  }, 
		          complete: function (response, status, request) {   
		        	  cType = response.getResponseHeader('Content-Type');
		        	  self.errormsg = undefined;
			          if (cType && cType.indexOf('text/plain') !== -1 && response.status == 200) {
			        	  self.errormsg = response.responseText;
				          self.carregando = false;
				      } else {
			        	  if (response.status > 300 && cType.indexOf('text/html') !== -1) {
			                  window.location.href = response.redirect;
						  } else {		  	
							  carregaFromJson(response.responseText, self);
							  sessionStorage.setItem(
									  'timeout' + getUser(), new Date());
						  }
				      }
		          }, 
		          failure: function (response, status) {           
		  	          self.carregando = false; 
		  	          self.showError(response.responseText, self); 
		          }
		          
		      }) 
		    },
		    resetaStorage: function() {
		    	sessionStorage.removeItem('timeout' + getUser());
		    	sessionStorage.removeItem('mesa' + getUser());
		    	localStorage.removeItem('gruposMesa' + getUser());
		    	setParmUser('trazerAnotacoes', true);
		    	this.trazerAnotacoes=true;
		    	setParmUser('trazerComposto', true);
		    	this.trazerComposto=false;
		    	localStorage.removeItem('exibeLota' + getUser());
		    	this.carregarMesa();
    			this.selQtdPag = 15;
		    },
		    recarregarMesa: function() {
		    	this.grupos = [];
		    	sessionStorage.removeItem('timeout' + getUser());
		    	this.carregarMesa();
		    },
		    carregarMesaUser: function() {
		    	setParmUser('exibeLota', false);
		    	this.exibeLota = false;
		    	this.recarregarMesa();
		    },
		    carregarMesaLota: function() {
		    	setParmUser('exibeLota', true)
		    	this.exibeLota = true;
		    	this.recarregarMesa();
		    },
		    setQtdPag: function(grupoNome, event) {
		  	    var parmGrupos = JSON.parse(getParmUser('gruposMesa'));
	    		setValueGrupo(grupoNome, 'qtdPag', parseInt(event.target.value));
		    },
		    pageDownGrupo: function(grupoNome) {
		  	    var parmGrupos = JSON.parse(getParmUser('gruposMesa'));
	    		if (parmGrupos[grupoNome].qtd === 0) {
	    			setValueGrupo(grupoNome, 'qtd', parmGrupos[grupoNome].qtdPag)
	    		} else {
		  	    	setValueGrupo(grupoNome, 'qtd', parseInt(parmGrupos[grupoNome].qtd));
	    		}
		    	sessionStorage.removeItem('timeout' + getUser());
	    		this.carregarMesa(grupoNome, parseInt(parmGrupos[grupoNome].qtdPag));
		    },
		    collapseGrupo: function(grupoOrdem, grupoNome) {
		    	// Abre ou fecha o grupo, salvando info no local/session storage e refletindo no vue
		  	    var parmGrupos = JSON.parse(getParmUser('gruposMesa'));
		    	var collapsibleElemHeader = document.getElementById('collapse-header-' + grupoOrdem);
		  	    if (collapsibleElemHeader.classList.contains('collapsed')) {
		    		setValueGrupoSessionStorage(grupoNome,'grupoCollapsed', false);
		    		setValueGrupo(grupoNome, 'collapsed', false);
		    		if (parmGrupos[grupoNome].qtd === 0) {
		    			setValueGrupo(grupoNome, 'qtd', parmGrupos[grupoNome].qtdPag)
		    		} else {
			  	    	setValueGrupo(grupoNome, 'qtd', parseInt(parmGrupos[grupoNome].qtd));
		    		}
		    		if ($('#collapsetab-' + grupoOrdem + ' tr').length < 2) {
		    	    	sessionStorage.removeItem('timeout' + getUser());
			    		this.carregarMesa(grupoNome, 0);
		    		}
		    	} else {
		    		setValueGrupoSessionStorage(grupoNome,'grupoCollapsed', true);
		    		setValueGrupo(grupoNome, 'collapsed', true);
		    	}
		    },
		    fecharGrupo: function(grupoOrdem, grupoNome) {
		    	$('#collapsetab-' + grupoOrdem).collapse('hide');
	    		setValueGrupo(grupoNome, 'collapsed', true);
    			setValueGrupoVue(grupoNome,'grupoCollapsed', true);
		    },
		    getLastRefreshTime: function() {
		    	var dt = new Date(sessionStorage.getItem('timeout' + getUser()));
		    	return ("0" + dt.getDate()).slice (-2) + "/" + ("0" + (dt.getMonth() + 1)).slice (-2) + " " 
		    		+ ("0" + dt.getHours()).slice (-2) + ":" + ("0" + dt.getMinutes()).slice (-2);
		    },
		    toggleMenuConfig: function() {
		    	if (this.toggleConfig === 'show-config') {
		    		this.toggleConfig = 'hide-config';
		    	} else {
		    		this.toggleConfig = 'show-config'; 
		    	}
		    },
		    fixItem: function(item) {
		      this.applyDefauts(item, {
		        checked: false,
		        disabled: false,
		        grupo: undefined,
		        grupoNome: undefined,
		        datahora: undefined,
		        datahoraFormatada: undefined,
		        sigla: undefined,
		        codigo: undefined,
		        descr: undefined,
		        origem: undefined,
		        situacao: undefined,
		        errormsg: undefined,
		        odd: undefined
		      });
		      if (item.datahora !== undefined) {
		        item.datahoraFormatada = this.formatJSDDMMYYYYHHMM(item.datahora);
		      }
		      return item;
		    },

		    // De UtilsBL
		    applyDefauts: function(obj, defaults) {
		      for (var k in defaults) {
		        if (!defaults.hasOwnProperty(k)) continue;
		        if (obj.hasOwnProperty(k)) continue;
		        obj[k] = defaults[k];
		      }
		    },

		    showError: function(error, component) {
		      component.errormsg = undefined;
		      try {
		      	component.errormsg = error.data.errormsg;
		      } catch (e) {}
		      if (
		        component.errormsg === undefined &&
		        error.statusText &&
		        error.statusText !== ""
		      ) {
		        component.errormsg = error.statusText;
		      }
		      if (component.errormsg === undefined) {
		        component.errormsg = "Erro desconhecido!";
		      }
		    },

		    // Filtra itens por uma string qualquer. Se desejar restringir o filtro apenas a algumas das propriedades,
		    // informar o parâmetro propriedades na forma: ['propriedade','outraPropriedade','lista.propriedade']
		    filtrarPorSubstring: function(a, s, propriedades) {
		      var re = new RegExp(s, "i");
		      return a.filter(function filterItem(item, idx, arr, context) {
		        for (var key in item) {
		          if (!item.hasOwnProperty(key)) continue;
		          var prop = context;
		          prop = context === undefined ? key : context + "." + key;
		          if (Array.isArray(item[key])) {
		            for (var i = 0; i < item[key].length; i++) {
		              if (filterItem(item[key][i], i, item[key], prop)) return true;
		            }
		          }
		          if (propriedades && propriedades.indexOf(prop) === -1) continue;
		          if (typeof item[key] === "string" && re.test(item[key])) {
		            return true;
		          }
		          if (
		            typeof item[key] === "object" &&
		            filterItem(item[key], 0, arr, prop)
		          ) {
		            return true;
		          }
		        }
		        return false;
		      });
		    },

		    formatJSDDMMYYYYHHMM: function(s) {
		      if (!s) return;
		      var r =
		        s.substring(8, 10) +
		        "/" +
		        s.substring(5, 7) +
		        "/" +
		        s.substring(0, 4) +
		        "&nbsp;" +
		        s.substring(11, 13) +
		        ":" +
		        s.substring(14, 16);
		      return r;
		    },

		    formatJSDDMMYYYY_AS_HHMM: function (s) {
		        if (s === undefined) return

		        var r = this.formatJSDDMMYYYYHHMM(s)
		        r = r.replace('&nbsp;', ' às ')
		        return r
		      },

		  }
		});

function atualizaGrupos (grupos) {
	var parms = JSON.parse(getParmUser('gruposMesa'));
	if (parms == null) {
		for (var g in grupos) {
			setGrupo(grupos[g].grupoNome, grupos[g].grupoOrdem, grupos[g].grupoQtd, grupos[g].grupoQtdPag, grupos[g].grupoCollapsed)
		}
	}
	for (var i in parms) {
		if (parms[i].qtdPag > 0) {
			var grpSelect = document.getElementById('selQtdItens-' + parms[i].ordem);
			if (grpSelect != null)
				grpSelect.value = parms[i].qtdPag;
		}
	}
}
function cloneArray(arr) {
	var newArray = [];
	for (var i = 0; i < arr.length; i++) {
	  if (arr[i].isArray) {
	    cloneArray(arr[i])
	  } else {
	    newArray.push(arr[i])
	  }
	}
	return newArray;
}
function setGrupo(grupoNome, ordem, qtd, qtdPag, collapsed) {
	// Seta um parametro com dados de selecao do grupo no JSON de parametros e armazena na local storage. 
	// Se a quantidade for "" ou false, deleta o parametro
	var parms = JSON.parse(getParmUser('gruposMesa'));
	if (parms == null) { 
		parms = {"key" : ""};
		parms[grupoNome] = {'ordem' : ordem, 'qtd' : qtd, 'qtdPag' : qtdPag, 'collapsed' : collapsed };
		delete parms["key"];
	} else {
		if (qtd === "" || qtd === false) {
			delete parms [grupoNome];
		} else {
			parms[grupoNome] = {'ordem' : ordem, 'qtd' : qtd, 'qtdPag' : qtdPag, 'collapsed' : collapsed };
		}
	}
	setParmUser('gruposMesa', JSON.stringify(parms));
}
function setValueGrupo(grupoNome, key, value) {
	var parms = JSON.parse(getParmUser('gruposMesa'));
	parms[grupoNome] [key] = value;
	setParmUser('gruposMesa', JSON.stringify(parms));
}
function setValueGrupoVue(grupoNome, key, value) {
    for (var g in appMesa.grupos) {
        if (appMesa.grupos[g].grupoNome == grupoNome) 
        	Vue.set(appMesa.grupos[g], key, value);
    }
}
function setValueGrupoSessionStorage(grupoNome, key, value) {
	var grp = JSON.parse(sessionStorage.getItem('mesa' + getUser()));
    for (var g in grp) {
      if (grp[g].grupoNome == grupoNome) {
    	grp [g] [key] = value;  
      }
    }
    sessionStorage.setItem(
			  'mesa' + getUser(), JSON.stringify(grp));
}
function setParmUser(nomeParm, value) {
	window.localStorage.setItem(nomeParm + getUser(), value)	
}
function getParmUser(nomeParm) {
	return window.localStorage.getItem(nomeParm + getUser())	
}
function slideDown (id) { 
	setInterval(function(){
    	document.getElementById("#" + id).slideDown();
	}, 20);
}
function carregaFromJson (json, appMesa) {
	var grp = JSON.parse(json);
	if (grp.length > 1) {
	  appMesa.grupos = grp;  
    } else {
      for (var g in appMesa.grupos) {
        if (appMesa.grupos[g].grupoOrdem === grp[0].grupoOrdem) {
      		Vue.set(appMesa.grupos, g, grp[0]);
      		if (appMesa.grupos[g].grupoDocs == undefined)
      			appMesa.grupos[g].grupoDocs = [];
	        var q = Math.round((appMesa.grupos[g].grupoDocs.length / appMesa.grupos[g].grupoQtdPag) - 0.51)  
						* appMesa.grupos[g].grupoQtdPag + appMesa.grupos[g].grupoQtdPag;
			if (q > 0)
	        	setValueGrupo(appMesa.grupos[g].grupoNome, 'qtd', q);
      	}
      }
    }
    sessionStorage.setItem(
			  'mesa' + getUser(), JSON.stringify(appMesa.grupos));
    atualizaGrupos(appMesa.grupos);
    appMesa.primeiraCarga = false;
	appMesa.carregando = false;
}
function getUser () {
	return document.getElementById('cadastrante').title + ${idVisualizacao == 0 ? '""' : idVisualizacao };
}

</script>