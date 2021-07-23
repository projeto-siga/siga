<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"
	import="br.gov.jfrj.siga.wf.model.enm.WfTipoDeVariavel, br.gov.jfrj.siga.wf.model.enm.WfTipoDeAcessoDeVariavel"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%
pageContext.setAttribute("tipoDeVariavel", WfTipoDeVariavel.values());
pageContext.setAttribute("tipoDeAcesso", WfTipoDeAcessoDeVariavel.values());
%>


<siga:pagina titulo="Diagrama" incluirJs="/sigawf/js/diagrama.js">

	<link rel="stylesheet" href="/siga/fontawesome/css/all.css"></link>

	<!-- CSS Customization -->
	<link rel="stylesheet" href="/sigawf/css/diagrama.css"></link>

	<link rel="stylesheet"
		href="/siga/javascript/angucomplete-alt/angucomplete-alt.css"></link>

	<script src="/siga/javascript/jquery/3.3.1/jquery.min.js"
		type="text/javascript"></script>
	<script src="/siga/bootstrap/js/bootstrap.bundle.min.js?v=4.1.1"
		type="text/javascript"></script>


	<script src="/siga/javascript/angularjs/1.8.2/angular.min.js"></script>

	<script src="/siga/javascript/angucomplete-alt/angucomplete-alt.js"></script>

	<div class="container-fluid content" ng-app="app" ng-controller="ctrl">
		<h2>
			<c:choose>
				<c:when test="${param.duplicar}">Duplicar</c:when>
				<c:otherwise>Editar</c:otherwise>
			</c:choose>
			Diagrama ${pd.sigla}
		</h2>

		<input type="hidden" name="postback" value="1" />
		<fieldset title="Dados Básicos">
			<div class="row">
				<div class="col col-md-4">
					<div class="form-group">
						<label>Nome</label> <input type="text" name="pd.nome"
							ng-model="data.workflow.nome" size="80" class="form-control" />
					</div>
				</div>
				<div class="col col-md-8">
					<div class="form-group">
						<label>Descrição</label> <input type="text" name="descricao"
							ng-model="data.workflow.descricao" size="80" class="form-control" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col col-md-3">
					<div class="form-group" id="tipoMarcador">
						<label for="idAcessoDeEdicao">Acesso para Editar</label> <select
							ng-model="data.workflow.acessoDeEdicao"
							ng-options="item.id as item.descr for item in acessosDeEdicao"
							class="form-control"></select>
					</div>
				</div>
				<div class="col col-md-3">
					<div class="form-group" id="tipoMarcador">
						<label for="idAcessoDeEdicao">Acesso para Iniciar</label> <select
							ng-model="data.workflow.acessoDeInicializacao"
							ng-options="item.id as item.descr for item in acessosDeInicializacao"
							class="form-control"></select>
					</div>
				</div>
				<div class="col col-md-3">
					<div class="form-group" id="lotaResponsavel">
						<label for="lotaResponsavel" title="" class="label w-100">Lotação
							Resp.
							<div minlength="1" selected-object="selectedObject"
								focus-first="true" text-searching="Pesquisando Unidades..."
								initial-value="data.workflow.lotaResponsavel.originalObject"
								title-field="firstLine" description-field="secondLine"
								input-class="form-control form-control-small"
								remote-url-data-field="list" pause="200"
								text-no-results="Não encontrei nenhuma Unidade."
								match-class="highlight"
								selected-object-data="{context: data.workflow, variable: 'lotaResponsavel', full:false}"
								remote-url="/siga/app/lotacao/buscar-json/" angucomplete-alt
								name="lotaResponsavel" placeholder="Pesquisar Unidade"
								id="lotaResponsavel" class="angucomplete-ctrl p-0"
								template-url="/siga/javascript/angucomplete-alt/custom-template.html"></div>
						</label>
					</div>
				</div>
				<div class="col col-md-3">
					<div class="form-group" id="responsavel">
						<label for="responsavel" title="" class="label w-100">Pessoa
							Resp.
							<div minlength="1" selected-object="selectedObject"
								focus-first="true" text-searching="Pesquisando Pessoas..."
								initial-value="data.workflow.responsavel.originalObject"
								title-field="firstLine" description-field="secondLine"
								input-class="form-control form-control-small"
								remote-url-data-field="list" pause="200"
								text-no-results="Não encontrei nenhuma Pessoa."
								match-class="highlight"
								selected-object-data="{context: data.workflow, variable: 'responsavel', full:false}"
								remote-url="/siga/app/pessoa/buscar-json/" angucomplete-alt
								name="responsavel" placeholder="Pesquisar Pessoa"
								id="responsavel" class="angucomplete-ctrl p-0"
								template-url="/siga/javascript/angucomplete-alt/custom-template.html"></div>
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col col-md-3">
					<div class="form-group">
						<label for="tipoDePrincipal">Tipo de Principal</label> <select
							ng-model="data.workflow.tipoDePrincipal"
							ng-options="item.id as item.descr for item in tiposDePrincipal"
							class="form-control"></select>
					</div>
				</div>
				<div class="col col-md-3"
					ng-show="data.workflow.tipoDePrincipal != 'NENHUM'">
					<div class="form-group">
						<label for="tipoDeVinculoComPrincipal">Vínculo com o
							Principal</label> <select
							ng-model="data.workflow.tipoDeVinculoComPrincipal"
							ng-options="item.id as item.descr for item in tiposDeVinculoComPrincipal"
							class="form-control"></select>
					</div>
				</div>
			</div>
		</fieldset>

		<fieldset title="Tarefas">
			<div class="row">
				<div class="col col-12 col-md-8">
					<header class="form-group juia">
					<div class="row align-items-center">
						<div class="col col-auto">Tarefas</div>
						<div class="col col-auto ml-auto">
							<button
								ng-click="data.workflow.tarefa = (data.workflow.tarefa || []);data.workflow.tarefa.push({}); $event.stopPropagation();"
								style="margin-left: 1em;" class="juia btn btn-sm btn-secondary">
								<i class="fa fa-plus"></i>&nbsp;Tarefa
							</button>
						</div>
					</div>
					</header>
					<div ng-repeat="tarefaItem in data.workflow.tarefa" class="row">
						<div class="col col-auto">
							<section> <label class="label" for="ddm">&nbsp;
								<div id="ddm" role="group" class="form-controlx btn-group"
									style="padding: 0 !important; display: block;">
									<div role="group" class="btn-group dropright">
										<button data-toggle="dropdown" aria-expanded="false"
											aria-haspopup="true" id="dropBtn"
											class="btn btn-secondary dropdown-toggle">{{$index+1}}</button>
										<div aria-labelledby="dropBtn" class="dropdown-menu pt-0 pb-0">
											<button
												ng-click="data.workflow.tarefa.splice($index + 1, 0, {});"
												class="btn btn-link p-2" ng-disabled>
												<i class="fa fa-fa fa-plus"></i>
											</button>
											<button
												ng-click="data.workflow.tarefa[$index] = data.workflow.tarefa.splice($index - 1, 1, data.workflow.tarefa[$index])[0]"
												class="btn btn-link p-2" ng-disabled="$index === 0">
												<i class="fa fa-fa fa-arrow-up"></i>
											</button>
											<button
												ng-click="data.workflow.tarefa[$index] = data.workflow.tarefa.splice($index + 1, 1, data.workflow.tarefa[$index])[0]"
												class="btn btn-link p-2"
												ng-disabled="$index === data.workflow.tarefa.length - 1">
												<i class="fa fa-fa fa-arrow-down"></i>
											</button>
											<button ng-click="data.workflow.tarefa.splice($index, 1);"
												class="btn btn-link p-2" ng-disabled>
												<i class="fa fa-fa fa-trash"></i>
											</button>
										</div>
									</div>
								</div>
							</label> </section>
						</div>
						<div class="col">
							<div class="row">
								<section class="col col-12 col-md-2 col-lg-2 form-group">
								<label for="tipo" title="" class="label">Tipo<i
									title="Preenchimento obrigatório"
									class="label-clue fa fa-asterisk"></i><select
									ng-model="tarefaItem.tipo" ng-required="true"
									class="form-control"><option value="FORMULARIO">Formulário</option>
										<option value="DECISAO">Decisão</option>
										<option value="EMAIL">E-mail</option>
										<option value="EXECUTAR">Executar</option>
										<optgroup label="{{getPrincipalNome()}}"
											ng-if="data.workflow.tipoDePrincipal != 'NENHUM'">
											<option value="AGUARDAR_ASSINATURA_PRINCIPAL"
												ng-if="data.workflow.tipoDePrincipal == 'DOCUMENTO'">
												Aguardar Assinatura</option>
											<option value="TRAMITAR_PRINCIPAL"
												ng-if="data.workflow.tipoDePrincipal == 'DOCUMENTO'">
												Tramitar</option>
											<option value="ARQUIVAR_PRINCIPAL"
												ng-if="data.workflow.tipoDePrincipal == 'DOCUMENTO'">
												Arquivar</option>
											<option value="INCLUIR_DOCUMENTO"
												ng-if="data.workflow.tipoDePrincipal == 'DOCUMENTO'">
												Incluir Documento</option>
										</optgroup>
								</select></label> <i></i> </section>
								<section class="col col-12 col-md-2 col-lg-2 form-group">
								<label for="titulo" title="" class="label">Título<i
									title="Preenchimento obrigatório"
									class="label-clue fa fa-asterisk"></i><input
									ng-model="tarefaItem.titulo" name="titulo" ng-required="true"
									id="titulo" type="text" class="form-control"></label> </section>
								<section
									ng-show="tarefaItem.tipo != 'FORMULARIO' && tarefaItem.tipo != 'DECISAO'"
									class="col col-12 col-md-2 col-lg-2 form-group"> <label
									for="depois" title="" class="label">Depois<select
									ng-selected="desvioItem.tarefa" ng-model="tarefaItem.depois"
									ng-init=""
									ng-options="item.id as item.nome disable when item.id === tarefaItem.id for item in tarefas"
									class="form-control"><option value="">[Próxima]</option></select></label>
								<i></i> </section>
								<section
									ng-show="tarefaItem.tipo == 'FORMULARIO' || tarefaItem.tipo == 'INCLUIR_DOCUMENTO' || tarefaItem.tipo == 'EMAIL' || tarefaItem.tipo == 'TRAMITAR_PRINCIPAL'"
									class="col col-12 col-md-2 col-lg-2 form-group"> <label
									for="tipoResponsavel" title="" class="label">Tipo Resp.<i
									title="Preenchimento obrigatório"
									class="label-clue fa fa-asterisk"></i><select
									ng-model="tarefaItem.tipoResponsavel" ng-required="true"
									class="form-control">
										<option value="RESPONSAVEL">Tabelado</option>
										<option value="LOTACAO">Lotação</option>
										<option value="PESSOA">Pessoa</option>
										<optgroup label="Procedimento">
											<option value="PROCEDIMENTO_TITULAR">Titular</option>
											<option value="PROCEDIMENTO_LOTA_TITULAR">Lotação do
												Titular</option>
										</optgroup>
										<optgroup label="{{getPrincipalNome()}}"
											ng-if="data.workflow.tipoDePrincipal != 'NENHUM'">
											<option value="PRINCIPAL_CADASTRANTE">Cadastrante</option>
											<option value="PRINCIPAL_LOTA_CADASTRANTE">Lotação
												do Cadastrante</option>
											<option value="PRINCIPAL_TITULAR">Titular</option>
											<option value="PRINCIPAL_LOTA_TITULAR">Lotação do
												Titular</option>
											<option value="PRINCIPAL_SUBSCRITOR">Subscritor</option>
											<option value="PRINCIPAL_LOTA_SUBSCRITOR">Lotação do
												Subscritor</option>
											<option value="PRINCIPAL_DESTINATARIO">Destinatário</option>
											<option value="PRINCIPAL_LOTA_DESTINATARIO">Lotação
												do Destinatário</option>
											<option value="PRINCIPAL_GESTOR">Gestor</option>
											<option value="PRINCIPAL_LOTA_GESTOR">Lotação do
												Gestor</option>
											<option value="PRINCIPAL_FISCAL_TECNICO">Fiscal
												Técnico</option>
											<option value="PRINCIPAL_LOTA_FISCAL_TECNICO">
												Lotação do Fiscal Técnico</option>
											<option value="PRINCIPAL_FISCAL_ADMINISTRATIVO">
												Fiscal Administrativo</option>
											<option value="PRINCIPAL_LOTA_FISCAL_ADMINISTRATIVO">
												Lotação do Fiscal Administrativo</option>
											<option value="PRINCIPAL_INTERESSADO">Interessado</option>
											<option value="PRINCIPAL_LOTA_INTERESSADO">Lotação
												do Interessado</option>
											<option value="PRINCIPAL_AUTORIZADOR">Interessado</option>
											<option value="PRINCIPAL_LOTA_AUTORIZADOR">Lotação
												do Autorizador</option>
											<option value="PRINCIPAL_REVISOR">Interessado</option>
											<option value="PRINCIPAL_LOTA_REVISOR">Lotação do
												Revisor</option>
											<option value="PRINCIPAL_LIQUIDANTE">Interessado</option>
											<option value="PRINCIPAL_LOTA_LIQUIDANTE">Lotação do
												Liquidante</option>
										</optgroup>
								</select>
								</label> <i></i> </section>
								<section
									ng-show="(tarefaItem.tipo == 'FORMULARIO' || tarefaItem.tipo == 'INCLUIR_DOCUMENTO' || tarefaItem.tipo == 'EMAIL' || tarefaItem.tipo == 'TRAMITAR_PRINCIPAL') && tarefaItem.tipoResponsavel == 'LOTACAO'"
									class="col col-12 col-md-3 col-lg-3 form-group"> <label
									for="refUnidadeResponsavel" title="" class="label">Lotação
									Resp.
									<div minlength="1" selected-object="selectedObject"
										focus-first="true" text-searching="Pesquisando Unidades..."
										initial-value="tarefaItem.refUnidadeResponsavel.originalObject"
										title-field="firstLine" description-field="secondLine"
										input-class="form-control form-control-small"
										remote-url-data-field="list" pause="200"
										text-no-results="Não encontrei nenhuma Unidade."
										match-class="highlight"
										selected-object-data="{context:tarefaItem, variable: 'refUnidadeResponsavel', full:false}"
										remote-url="/siga/app/lotacao/buscar-json/" angucomplete-alt
										name="refUnidadeResponsavel" placeholder="Pesquisar Unidade"
										id="refUnidadeResponsavel" class="angucomplete-ctrl p-0"
										template-url="/siga/javascript/angucomplete-alt/custom-template.html"></div>
								</label> </section>
								<section
									ng-show="(tarefaItem.tipo == 'FORMULARIO' || tarefaItem.tipo == 'INCLUIR_DOCUMENTO' || tarefaItem.tipo == 'EMAIL' || tarefaItem.tipo == 'TRAMITAR_PRINCIPAL') && tarefaItem.tipoResponsavel == 'PESSOA'"
									class="col col-12 col-md-3 col-lg-3 form-group"> <label
									for="refPessoaResponsavel" title="" class="label">Pessoa
									Resp.
									<div minlength="1" selected-object="selectedObject"
										focus-first="true" text-searching="Pesquisando Pessoas..."
										initial-value="tarefaItem.refPessoaResponsavel.originalObject"
										title-field="firstLine" description-field="secondLine"
										input-class="form-control form-control-small"
										remote-url-data-field="list" pause="200"
										text-no-results="Não encontrei nenhuma Pessoa."
										match-class="highlight"
										selected-object-data="{context:tarefaItem, variable: 'refPessoaResponsavel', full:false}"
										remote-url="/siga/app/pessoa/buscar-json/" angucomplete-alt
										name="refPessoaResponsavel" placeholder="Pesquisar Pessoa"
										id="refPessoaResponsavel" class="angucomplete-ctrl p-0"
										template-url="/siga/javascript/angucomplete-alt/custom-template.html"></div>
								</label> </section>
								<section
									ng-show="(tarefaItem.tipo == 'FORMULARIO' || tarefaItem.tipo == 'INCLUIR_DOCUMENTO' || tarefaItem.tipo == 'EMAIL' || tarefaItem.tipo == 'TRAMITAR_PRINCIPAL') && tarefaItem.tipoResponsavel == 'RESPONSAVEL'"
									class="col col-12 col-md-3 col-lg-3 form-group"> <label
									for="refResponsavel" title="" class="label">Responsável
									<select ng-model="tarefaItem.refResponsavel"
									ng-options="item.hisIdIni as item.nome for item in responsaveis"
									class="form-control"></select>
								</label> </section>
								<section ng-show="tarefaItem.tipo == 'INCLUIR_DOCUMENTO'"
									class="col col-12 col-md-3 col-lg-3 form-group"> <label
									for="ref" title="" class="label">Modelo
									<div minlength="1" selected-object="selectedObject"
										focus-first="true"
										text-searching="Pesquisando Modelo Documental..."
										initial-value="tarefaItem.ref.originalObject"
										title-field="firstLine" description-field="secondLine"
										input-class="form-control form-control-small"
										remote-url-data-field="list" pause="200"
										text-no-results="Não encontrei nenhuma Tipologia Documental."
										match-class="highlight"
										selected-object-data="{context:tarefaItem, variable: 'ref', full:false}"
										remote-url="/sigaex/app/modelo/buscar-json-para-incluir/"
										angucomplete-alt name="ref"
										placeholder="Pesquisar Tipologia Documental" id="ref"
										class="angucomplete-ctrl"
										template-url="/siga/javascript/angucomplete-alt/custom-template.html"></div>
								</label> </section>
								<fieldset ng-show="tarefaItem.tipo == 'FORMULARIO'"
									title="Variáveis" class="col col-12">
									<header class="form-group juia">
									<div class="row align-items-center">
										<div class="col col-auto">Variáveis</div>
										<div class="col col-auto ml-auto">
											<button
												ng-click="tarefaItem.variavel = (tarefaItem.variavel || []);tarefaItem.variavel.push({});"
												style="margin-left: 1em;"
												class="juia btn btn-sm btn-secondary">
												<i class="fa fa-plus"></i>&nbsp;Variável
											</button>
										</div>
									</div>
									</header>
									<div ng-repeat="variavelItem in tarefaItem.variavel"
										class="row">
										<div class="col col-auto">
											<section> <label title="" class="label">&nbsp;</label>
											<label class="input float-right"><div role="group"
													class="btn-group">
													<div role="group" class="btn-group dropright">
														<button data-toggle="dropdown" aria-expanded="false"
															aria-haspopup="true" id="dropBtn"
															class="btn btn-secondary dropdown-toggle">{{$index+1}}</button>
														<div aria-labelledby="dropBtn"
															class="dropdown-menu pt-0 pb-0">
															<button
																ng-click="tarefaItem.variavel.splice($index + 1, 0, {});"
																class="btn btn-link p-2" ng-disabled>
																<i class="fa fa-fa fa-plus"></i>
															</button>
															<button
																ng-click="tarefaItem.variavel[$index] = tarefaItem.variavel.splice($index - 1, 1, tarefaItem.variavel[$index])[0]"
																class="btn btn-link p-2" ng-disabled="$index === 0">
																<i class="fa fa-fa fa-arrow-up"></i>
															</button>
															<button
																ng-click="tarefaItem.variavel[$index] = tarefaItem.variavel.splice($index + 1, 1, tarefaItem.variavel[$index])[0]"
																class="btn btn-link p-2"
																ng-disabled="$index === tarefaItem.variavel.length - 1">
																<i class="fa fa-fa fa-arrow-down"></i>
															</button>
															<button ng-click="tarefaItem.variavel.splice($index, 1);"
																class="btn btn-link p-2" ng-disabled>
																<i class="fa fa-fa fa-trash"></i>
															</button>
														</div>
													</div>
												</div></label> </section>
										</div>
										<div class="col">
											<div class="row">
												<section class="col col-12 col-md-3 form-group"> <label
													for="titulo" title="" class="label">Título<i
													title="Preenchimento obrigatório"
													class="label-clue fa fa-asterisk"></i><input
													ng-model="variavelItem.titulo" name="titulo"
													ng-required="true" id="titulo" type="text"
													class="form-control"></label> </section>
												<section class="col col-12 col-md-3 form-group"> <label
													for="identificador" title="" class="label">Identificador<i
													title="Preenchimento obrigatório"
													class="label-clue fa fa-asterisk"></i><input
													ng-model="variavelItem.identificador" name="identificador"
													ng-required="true" id="identificador" type="text"
													class="form-control"></label> </section>
												<section class="col col-12 col-md-3 form-group"> <label
													for="tipo" title="" class="label">Tipo<i
													title="Preenchimento obrigatório"
													class="label-clue fa fa-asterisk"></i><select
													ng-model="variavelItem.tipo" ng-required="true"
													class="form-control">
														<c:forEach items="${tipoDeVariavel}" var="val">
															<option value="${val.name()}">${val.descr}</option>
														</c:forEach>
												</select></label> <i></i> </section>
												<section class="col col-12 col-md-3 form-group"> <label
													for="tipoDeEdicao" title="" class="label">Edição<i
													title="Preenchimento obrigatório"
													class="label-clue fa fa-asterisk"></i><select
													ng-model="variavelItem.tipoDeEdicao" ng-required="true"
													class="form-control">
														<c:forEach items="${tipoDeAcesso}" var="val">
															<option value="${val.name()}">${val.descr}</option>
														</c:forEach>
												</select></label> <i></i> </section>
											</div>
										</div>
									</div>
								</fieldset>
								<fieldset
									ng-show="tarefaItem.tipo == 'FORMULARIO' || tarefaItem.tipo == 'DECISAO'"
									title="Desvios" class="col col-12">
									<header class="form-group juia">
									<div class="row align-items-center">
										<div class="col col-auto">Desvios</div>
										<div class="col col-auto ml-auto">
											<button
												ng-click="tarefaItem.desvio = (tarefaItem.desvio || []);tarefaItem.desvio.push({});"
												style="margin-left: 1em;"
												class="juia btn btn-sm btn-secondary">
												<i class="fa fa-plus"></i>&nbsp;Desvio
											</button>
										</div>
									</div>
									</header>
									<div ng-repeat="desvioItem in tarefaItem.desvio" class="row">
										<div class="col col-auto">
											<section> <label title="" class="label">&nbsp;</label>
											<label class="input float-right"><div role="group"
													class="btn-group">
													<div role="group" class="btn-group dropright">
														<button data-toggle="dropdown" aria-expanded="false"
															aria-haspopup="true" id="dropBtn"
															class="btn btn-secondary dropdown-toggle">{{$index+1}}</button>
														<div aria-labelledby="dropBtn"
															class="dropdown-menu pt-0 pb-0">
															<button
																ng-click="tarefaItem.desvio.splice($index + 1, 0, {});"
																class="btn btn-link p-2" ng-disabled>
																<i class="fa fa-fa fa-plus"></i>
															</button>
															<button
																ng-click="tarefaItem.desvio[$index] = tarefaItem.desvio.splice($index - 1, 1, tarefaItem.desvio[$index])[0]"
																class="btn btn-link p-2" ng-disabled="$index === 0">
																<i class="fa fa-fa fa-arrow-up"></i>
															</button>
															<button
																ng-click="tarefaItem.desvio[$index] = tarefaItem.desvio.splice($index + 1, 1, tarefaItem.desvio[$index])[0]"
																class="btn btn-link p-2"
																ng-disabled="$index === tarefaItem.desvio.length - 1">
																<i class="fa fa-fa fa-arrow-down"></i>
															</button>
															<button ng-click="tarefaItem.desvio.splice($index, 1);"
																class="btn btn-link p-2" ng-disabled>
																<i class="fa fa-fa fa-trash"></i>
															</button>
														</div>
													</div>
												</div></label> </section>
										</div>
										<div class="col">
											<div class="row">
												<section class="col col-12 col-md-4 form-group"> <label
													for="titulo" title="" class="label">Título<i
													title="Preenchimento obrigatório"
													class="label-clue fa fa-asterisk"></i><input
													ng-model="desvioItem.titulo" name="titulo"
													ng-required="true" id="titulo" type="text"
													class="form-control"></label> </section>
												<section class="col col-12 col-md-4 form-group"> <label
													for="tarefa" title="" class="label">Tarefa<select
													ng-selected="desvioItem.tarefa"
													ng-model="desvioItem.tarefa" ng-init=""
													ng-options="item.id as item.nome disable when item.id === tarefaItem.id for item in tarefas"
													class="form-control"><option value="">[Nenhum]</option></select></label>
												<i></i> </section>
												<section class="col col-12 col-md-4 form-group"> <label
													for="condicao" title="" class="label">Condição<input
													ng-model="desvioItem.condicao" name="condicao"
													id="condicao" type="text" class="form-control"></label> </section>
											</div>
										</div>
									</div>
								</fieldset>
								<section ng-show="tarefaItem.tipo == 'EMAIL'"
									class="col col-12 col-md-3 col-lg-3 form-group"> <label
									for="assunto" title="" class="label">Assunto<i
									title="Preenchimento obrigatório"
									class="label-clue fa fa-asterisk"></i><input
									ng-model="tarefaItem.assunto" name="assunto" ng-required="true"
									id="assunto" type="text" class="form-control"></label> </section>
								<section
									ng-show="tarefaItem.tipo == 'EMAIL' || tarefaItem.tipo == 'EXECUTAR'"
									class="col col-12 form-group"> <label for="texto"
									title="" class="mb-0">Texto<i
									title="Preenchimento obrigatório"
									class="label-clue fa fa-asterisk"></i></label> <textarea
									ng-model="tarefaItem.conteudo" name="texto" ng-required="true"
									id="conteudo" class="form-control"></textarea> </section>
							</div>
						</div>
					</div>
				</div>
				<div
					ng-show="data.workflow.tarefa &amp;&amp; data.workflow.tarefa.length > 0"
					class="col col-12 col-md-4">
					<header class="juia form-group">
					<div class="row align-items-center"></div>
					<div class="col col-auto">Workflow</div>
					</header>
					<div class="graph-container-tall border border-light rounded">
						<div id="graph-workflow" class="graph-svg"></div>
					</div>
				</div>
			</div>
		</fieldset>

		<div class="row">
			<div class="col-sm-12">
				<div class="form-group mb-0">
					<button class="btn btn-primary" ng-click="gravar()">Ok</button>
					<button value="Desativar" class="btn btn-primary"
						ng-click="desativar()">Desativar</button>
					<button onclick="javascript:history.back();"
						class="btn btn-primary">Cancelar</button>
				</div>
			</div>
		</div>
	</div>

	<script>
		$(document).ready(function() {
			updateContainer();
			$(window).resize(function() {
				updateContainer();
			});
		});

		function updateContainer() {
			var smallwidth = $('#graph-workflow').width();
			var smallheight = $('#graph-workflow').height();
			var smallsvg = $('#graph-workflow :first-child').first();
			var smallviewbox = smallsvg.attr('viewBox');

			if (smallheight > smallwidth * 120 / 100)
				smallheight = smallwidth * 120 / 100;

			if (smallsvg && smallsvg[0] && smallsvg[0].viewBox
					&& smallsvg[0].viewBox.baseVal) {

				console.log('updated')

				var baseVal = smallsvg[0].viewBox.baseVal;
				var width = smallwidth;
				var height = smallwidth * baseVal.height / baseVal.width;
				if (height > smallheight) {
					width = width * smallheight / height;
					height = smallheight;
				}
				smallsvg.attr('width', width);
				smallsvg.attr('height', height);
			} else if (typeof smallviewbox != 'undefined') {
				var a = smallviewbox.split(' ');

				// set attrs and 'resume' force 
				smallsvg.attr('width', smallwidth);
				smallsvg.attr('height', smallwidth * a[3] / a[2]);
			}

		}
	</script>
</siga:pagina>