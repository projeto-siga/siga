<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>

<link rel="stylesheet" href="/siga/bootstrap/4.6.0/css/bootstrap.min.css" type="text/css" media="screen, projection" />
<siga:pagina titulo="Mesa Virtual" incluirBS="false">
	
	<script type="text/javascript" src="../javascript/vue.min.js"></script>
	
	<style>
		.toast {
			background-color: rgba(255,255,255,.95) !important;
		}
		
		.toast-body {
			background-color: rgba(248,249,250,.95) !important;
		}		
	</style>
	
	<c:set var="siga_mesaCarregaLotacao" scope="session" value="${f:resource('/siga.mesa.carrega.lotacao')}" />
	<div id="app" class="container-fluid content" >
		<div id="configMenu" class="mesa-config ml-auto" :class="toggleConfig">
			<c:if test="${!ehPublicoExterno}">
				<button type="button" class="btn-mesa-config btn btn-secondary btn-sm h-100" @click="toggleMenuConfig();">
					<i class="fas fa-cog"></i>
				</button>
			</c:if>
			<div class="form-config border-bottom p-2">
				<h6>Configurações da Mesa Virtual</h6>
	            <i><small>Quanto menos informações forem solicitadas, mais rapidamente a mesa carregará.</small></i>
	            <div class="form-group my-1 border-bottom">
					<div class="form-check">
						<input type="checkbox" class="form-check-input" id="trazerAnotacoes" v-model="trazerAnotacoes" :disabled="carregando">
						<label class="form-check-label" for="trazerAnotacoes"><small>Trazer anotações nos docs.</small></label>
					</div>            
	            </div>
	            <div class="form-group my-1 border-bottom">
					<div class="form-check">
						<input type="checkbox" class="form-check-input" id="trazerComposto" v-model="trazerComposto" :disabled="carregando">
						<label class="form-check-label" for="trazerComposto">
							<small>Exibir indicador de doc. avulso <i class="far fa-file"></i> ou composto <i class="far fa-copy"></i></small>
						</label>
					</div>            
	            </div>
	            <div class="form-group my-1 border-bottom">
					<div class="form-check">
						<input type="checkbox" class="form-check-input" id="trazerCancelados" v-model="trazerCancelados" :disabled="carregando">
						<label class="form-check-label" for="trazerCancelados">
							<small>Exibir vias canceladas</small>
						</label>
					</div>            
	            </div>
	            <div class="form-group my-1 border-bottom">
					<div class="form-check">
						<input type="checkbox" class="form-check-input" id="trazerArquivados" v-model="trazerArquivados" :disabled="carregando">
						<label class="form-check-label" for="trazerArquivados">
							<small>Exibir pasta Aguardando Ação de Temporalidade</small>
						</label>
					</div>            
	            </div>
	            <div class="form-group my-1 border-bottom">
					<div class="form-check">
						<input type="checkbox" class="form-check-input" id="ordemCrescenteData" v-model="ordemCrescenteData" :disabled="carregando">
						<label class="form-check-label" for="ordemCrescenteData">
							<small>Exibir em ordem crescente</small>
						</label>
					</div>            
	            </div>
	            <div class="form-group my-1 border-bottom">
					<div class="form-check">
						<input type="checkbox" class="form-check-input" id="usuarioPosse" v-model="usuarioPosse" :disabled="carregando">
						<label class="form-check-label" for="usuarioPosse">
							<small>Exibir <fmt:message key="documento.atendente"/> do Documento</small>
						</label>
					</div>            
	            </div>
	            <div class="form-group my-1 border-bottom">
					<div class="form-check">
						<input type="checkbox" class="form-check-input" id="dtDMA" v-model="dtDMA" :disabled="carregando">
						<label class="form-check-label" for="dtDMA">
							<small>Exibir datas em dia, mês e ano</small>
						</label>
					</div>            
	            </div>
				<div class="form-group pb-2 mb-1 border-bottom">
					<label for="selQtdPagId"><small>Qtd. de documentos a trazer</small></label>
					<select class="form-control form-control-sm p-0" v-model="selQtdPag" :class="{disabled: carregando}">
						  <option value="5">5</option>
						  <option value="10">10</option>
						  <option value="15">15</option>
						  <option value="50">50</option>
						  <option value="100">100</option>
						  <option value="500">500</option>
					</select>
				</div>
				<div class="form-group pt-2">
			    	<button class="btn btn-secondary btn-sm py-0" @click="resetaStorage();" :class="{disabled: carregando}">Limpar configurações</button>
			    	<p><small>Todas as configurações selecionadas serão desmarcadas.</small></p>
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
		<div aria-live="polite" aria-atomic="true" style="position: relative; min-height: 200px;">
			<div id="inicio" class="row">
				<div class="col col-12 col-md-auto mb-2">
					<h3><i class="fa fa-file-alt"></i> Mesa Virtual</h3>
				</div>
				<div class="col col-md-4 my-1 mb-2" style="vertical-align: text-bottom;"> 
					<span class="h-100 d-inline-block my-2" style="vertical-align: text-bottom;">
						<c:if test="${not empty visualizacao}"><b>(Delegante: ${visualizacao.titular.nomePessoa})</b></c:if> 
					</span> 
				</div> 
				<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC:Módulo de Documentos')}">
					<c:if test="${!ehPublicoExterno || (siga_cliente == 'GOVSP' && ehPublicoExterno && podeNovoDocumento)}">
						<div class="col col-12 col-sm-4 col-md-auto ml-md-auto mb-2">
							<a href="expediente/doc/editar" class="btn btn-success form-control"> <i class="fas fa-plus-circle mr-1"></i>
								<fmt:message key="documento.novo"/></a>
						</div>
					</c:if>
					<c:if test="${!ehPublicoExterno}">
						<div class="col col-12 col-sm-4 col-md-auto pl-md-0 mb-2">
							<a href="expediente/doc/listar?primeiraVez=sim" class="btn btn-primary form-control">
								<i class="fas fa-search mr-1"></i><fmt:message key="documento.pesquisar"/> 
							</a>
						</div>
					</c:if>
				</c:if>
			</div>
			<div id="rowTopMesa" style="z-index: 1" class="row sticky-top px-3 pt-1 bg-white shadow-sm" 
					v-if="!carregando || (!errormsg &amp;&amp; grupos.length >= 0)">
				<c:if test="${siga_mesaCarregaLotacao && !ehPublicoExterno}">
					<c:set var="varLotacaoUnidade"><fmt:message key='usuario.lotacao'/></c:set>
					<div id="radioBtn" class="btn-group mr-2 mb-1">
						<a class="btn btn-primary btn-sm" v-bind:class="exibeLota ? 'notActive' : 'active'" id="btnUser"  
							accesskey="u" @click="carregarMesaUser('#btnUser');" 
							title="Visualiza somente os documentos do Usuário">
							<i class="fas fa-user my-1"></i>
							<span class="d-none d-sm-inline">Usuário</span>
						</a>
						<a class="btn btn-primary btn-sm" v-bind:class="exibeLota ? 'active' : 'notActive'" id="btnLota"  
							accesskey="l" @click="carregarMesaLota('#btnLota');"
							title="Visualiza somente os documentos da ${varLotacaoUnidade}">
							<i class="fas fa-users my-1"></i>
							<span class="d-none d-sm-inline"><fmt:message key='usuario.lotacao'/></span>
						</a>
					</div>
				</c:if>
				<div class="mr-2 mb-1">
					<input id="filtroExibidos" type="text" class="form-control p-1 input-sm" placeholder="Filtrar docs. da mesa" v-model="filtro" ng-model-options="{ debounce: 200 }">
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
						<div v-if="!g.hide && (g.grupoCounterUser > 0 || g.grupoCounterLota > 0)" :key="g.grupoOrdem">	
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
								<table v-else class="text-muted table table-sm table-striped table-hover table-borderless">
									<thead v-if="!carregando">
										<tr class="table-head d-flex">
											<th scope="col" class="col-1 d-none d-md-block">Tempo</th>
											<th scope="col" class="col-md-2"
													v-bind:class="usuarioPosse ? 'col-8' : 'col-9'">
												<fmt:message key="usuario.mesavirtual.codigo"/></th>
											<th scope="col" class="d-none d-md-block"
											 	v-bind:class="usuarioPosse ? 'col-3' : 'col-4'">Descrição</th>
											<th scope="col" class="col-md-1"
												v-bind:class="usuarioPosse ? 'col-2' : 'col-3'">Origem</th>
											<th v-if="usuarioPosse" scope="col" class="col-md-1 col-2"><fmt:message key="documento.atendente"/></th>
											<th scope="col" class="col-3 d-none d-md-block"><fmt:message key = "usuario.mesavirtual.etiquetas"/></th>
	<!-- 											<th v-show="gruposTemAlgumErro">Atenção</th> -->
										</tr>
									</thead>
									<tbody>
										<template v-for="f in g.grupoDocs">
											<tr class="d-flex">
												<td class="col-1 d-none d-md-block" 
													:title="f.datahoraDDMMYYYHHMM"><small>{{dtDMA? formatJSDDMMYYYY(f.datahoraDDMMYYYHHMM) 
														: f.tempoRelativo}}</small></td>
												<td class="col-md-2"
														v-bind:class="usuarioPosse ? 'col-8' : 'col-9'">
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
												<td class="d-none d-md-block" v-bind:class="usuarioPosse ? 'col-3' : 'col-4'">
													<span class="text-break" :title='processDescription(f.descr)' >{{ processDescription(f.descr, 60) }}</span>
												</td>
												<td class="col-md-1"
														v-bind:class="usuarioPosse ? 'col-2' : 'col-3'">
													<small>
														<c:if test="${siga_cliente == 'GOVSP'}">
															<span v-if="f.dataDevolucao == 'ocultar'"></span>
															<span v-if="f.dataDevolucao == 'alerta'"><i class="fa fa-exclamation-triangle text-warning"></i></span>
															<span v-if="f.dataDevolucao == 'atrasado'"><i class="fa fa-exclamation-triangle text-danger"></i></span>
														</c:if>
														{{f.origem}}
													</small>
												</td>
												<td v-if="usuarioPosse" class="col-md-1 col-2">
													<small>{{f.lotaPosse}} / {{f.nomePessoaPosse}}</small>
												</td>												
												<td class="col-3 d-none d-md-block p-1">
													<span v-if="f.anotacao != null">
														<a tabindex="0" class="anotacao fas fa-sticky-note text-warning popover-dismiss ml-2" role="button" 
																data-toggle="popover" data-trigger="hover focus" :data-content="f.anotacao"></a>
													</span>
													<span class="xrp-label-container">
														<span v-for="m in f.list">
															<a class="popover-dismiss" role="button" 
																data-toggle="popover" data-trigger="hover focus" :title="m.titulo" data-html="true" :data-pessoa="m.pessoa" :data-lotacao="m.lotacao">
																<span class="text-size-8 badge badge-pill badge-secondary tagmesa m-1 btn-xs" v-bind:style="{color: '#' + m.cor + ' !important'}">												
																	<i :class="m.icone" :style="{color: m.cor}"></i> {{m.nome}}<span v-if="m.ref1">, planejada: {{dtDMA? m.ref1DDMMYYYY : m.ref1}}</span><span v-if="m.ref2">, limite: {{dtDMA? m.ref2DDMMYYYY : m.ref2}}</span>
																</span>
															</a>
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
		<div id="toastContainer" style="position: fixed; top: 135px; right: 0;z-index: 999999;"></div>
	</div>
	<script type="text/javascript">
		const ID_VISUALIZACAO = ${idVisualizacao};
		$( document ).ready(function() {
			initPopovers();
		});
	</script>
	<script type="text/javascript" src="/siga/javascript/mesa2.js?v=1614289085"></script>
</siga:pagina>
<script src="/siga/bootstrap/4.6.0/js/bootstrap.bundle.min.js" type="text/javascript"></script>
