<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Diagrama" incluirJs="/sigawf/js/diagrama.js">

	<link rel="stylesheet"
		href="https://use.fontawesome.com/releases/v5.5.0/css/all.css"
		integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU"
		crossorigin="anonymous"></link>

	<!-- CSS Customization -->
	<link rel="stylesheet" href="/sigawf/css/diagrama.css"></link>
	<link rel="stylesheet"
		href="/sigawf/js/angucomplete-alt/angucomplete-alt.css"></link>

	<!-- JS Global Compulsory -->
	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"
		integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
		integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
		integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
		crossorigin="anonymous"></script>

	<script
		src="https://ajax.googleapis.com/ajax/libs/angularjs/1.7.9/angular.min.js"></script>

	<script src="/sigawf/js/angucomplete-alt/angucomplete-alt.js"></script>

	<div class="container-fluid content" ng-app="app" ng-controller="ctrl">
		<h2>Diagrama ${pd.sigla}</h2>

		<input type="hidden" name="postback" value="1" />
		<fieldset title="Dados Básicos">
			<div class="row">
				<div class="col-sm-4">
					<div class="form-group">
						<label>Nome</label> <input type="text" name="pd.nome"
							ng-model="data.workflow.nome" size="80" class="form-control" />
					</div>
				</div>
				<div class="col-sm-8">
					<div class="form-group">
						<label>Descrição</label> <input type="text" name="descricao"
							ng-model="data.workflow.descricao" size="80" class="form-control" />
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
											<button ng-click="data.workflow.tarefa.splice($index, 1);"
												class="btn btn-link" ng-disabled>
												<i class="fa fa-fa fa-trash"></i>
											</button>
											<button
												ng-click="data.workflow.tarefa[$index] = data.workflow.tarefa.splice($index - 1, 1, data.workflow.tarefa[$index])[0]"
												class="btn btn-link" ng-disabled="$index === 0">
												<i class="fa fa-fa fa-arrow-up"></i>
											</button>
											<button
												ng-click="data.workflow.tarefa[$index] = data.workflow.tarefa.splice($index + 1, 1, data.workflow.tarefa[$index])[0]"
												class="btn btn-link"
												ng-disabled="$index === data.workflow.tarefa.length - 1">
												<i class="fa fa-fa fa-arrow-down"></i>
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
									class="form-control"><option
											value="AGUARDAR_ASSINATURA_PRINCIPAL">Aguardar
											Assinatura</option>
										<option value="ENVIAR_PRINCIPAL">Enviar</option>
										<option value="ARQUIVAR_PRINCIPAL">Arquivar</option>
										<option value="INCLUIR_DOCUMENTO">Incluir Documento</option>
										<option value="FORMULARIO">Formulário</option>
										<option value="DECISAO">Decisão</option>
										<option value="EMAIL">E-mail</option>
										<option value="EXECUTAR">Executar</option></select></label> <i></i> </section>
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
									ng-show="tarefaItem.tipo == 'FORMULARIO' || tarefaItem.tipo == 'INCLUIR_DOCUMENTO' || tarefaItem.tipo == 'EMAIL' || tarefaItem.tipo == 'ENVIAR_PRINCIPAL'"
									class="col col-12 col-md-2 col-lg-2 form-group"> <label
									for="tipoResponsavel" title="" class="label">Tipo Resp.<i
									title="Preenchimento obrigatório"
									class="label-clue fa fa-asterisk"></i><select
									ng-model="tarefaItem.tipoResponsavel" ng-required="true"
									class="form-control"><option value="CADASTRANTE">Cadastrante</option>
										<option value="LOTA_CADASTRANTE">Lotação do
											Cadastrante</option>
										<option value="TITULAR">Titular</option>
										<option value="LOTA_TITULAR">Lotação do Titular</option>
										<option value="SUBSCRITOR">Subscritor</option>
										<option value="LOTA_SUBSCRITOR">Lotação do Subscritor</option>
										<option value="DESTINATARIO">Destinatário</option>
										<option value="LOTA_DESTINATARIO">Lotação do
											Destinatário</option> 
										<option value="RESPONSAVEL">Cadastrado</option>
										<option value="LOTACAO">Lotação</option>
										<option value="PESSOA">Pessoa</option></select>
								</label> <i></i> </section>
								<section
									ng-show="(tarefaItem.tipo == 'FORMULARIO' || tarefaItem.tipo == 'INCLUIR_DOCUMENTO' || tarefaItem.tipo == 'EMAIL' || tarefaItem.tipo == 'ENVIAR_PRINCIPAL') && tarefaItem.tipoResponsavel == 'LOTACAO'"
									class="col col-12 col-md-3 col-lg-3 form-group"> <label
									for="refUnidadeResponsavel" title="" class="label">Lotação
									Resp. Resp.
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
										template-url="/sigawf/js/angucomplete-alt/custom-template.html"></div>
								</label> </section>
								<section
									ng-show="(tarefaItem.tipo == 'FORMULARIO' || tarefaItem.tipo == 'INCLUIR_DOCUMENTO' || tarefaItem.tipo == 'EMAIL' || tarefaItem.tipo == 'ENVIAR_PRINCIPAL') && tarefaItem.tipoResponsavel == 'PESSOA'"
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
										template-url="/sigawf/js/angucomplete-alt/custom-template.html"></div>
								</label> </section>
								<section
									ng-show="(tarefaItem.tipo == 'FORMULARIO' || tarefaItem.tipo == 'INCLUIR_DOCUMENTO' || tarefaItem.tipo == 'EMAIL' || tarefaItem.tipo == 'ENVIAR_PRINCIPAL') && tarefaItem.tipoResponsavel == 'RESPONSAVEL'"
									class="col col-12 col-md-3 col-lg-3 form-group"> <label
									for="refResponsavel" title="" class="label">Responsável
									<select ng-model="tarefaItem.refResponsavel"
									ng-options="item.hisIdIni as item.nome for item in responsaveis"
									class="form-control"></select>
								</label> </section>
								<section ng-show="tarefaItem.tipo == 'INCLUIR_DOCUMENTO'"
									class="col col-12 col-md-3 col-lg-3 form-group"> <label
									for="refTipologia" title="" class="label">Tipologia
									<div minlength="1" selected-object="selectedObject"
										focus-first="true"
										text-searching="Pesquisando Tipologia Documental..."
										initial-value="tarefaItem.refTipologia.originalObject"
										title-field="firstLine" description-field="secondLine"
										input-class="form-control form-control-small"
										remote-url-data-field="list" pause="200"
										text-no-results="Não encontrei nenhuma Tipologia Documental."
										match-class="highlight"
										selected-object-data="{context:tarefaItem, variable: 'refTipologia', full:false}"
										remote-url="app/ecm-tipologia/buscar/" angucomplete-alt
										name="refTipologia"
										placeholder="Pesquisar Tipologia Documental" id="refTipologia"
										class="form-control angucomplete-ctrl"
										template-url="/sigawf/js/angucomplete-alt/custom-template.html"></div>
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
															<button ng-click="tarefaItem.variavel.splice($index, 1);"
																class="btn btn-link" ng-disabled>
																<i class="fa fa-fa fa-trash"></i>
															</button>
															<button
																ng-click="tarefaItem.variavel[$index] = tarefaItem.variavel.splice($index - 1, 1, tarefaItem.variavel[$index])[0]"
																class="btn btn-link" ng-disabled="$index === 0">
																<i class="fa fa-fa fa-arrow-up"></i>
															</button>
															<button
																ng-click="tarefaItem.variavel[$index] = tarefaItem.variavel.splice($index + 1, 1, tarefaItem.variavel[$index])[0]"
																class="btn btn-link"
																ng-disabled="$index === tarefaItem.variavel.length - 1">
																<i class="fa fa-fa fa-arrow-down"></i>
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
													class="form-control"><option value="STRING">String</option>
														<option value="DATE">Data</option>
														<option value="BOOLEAN">Booleana</option></select></label> <i></i> </section>
												<section class="col col-12 col-md-3 form-group"> <label
													for="tipoDeEdicao" title="" class="label">Edição<i
													title="Preenchimento obrigatório"
													class="label-clue fa fa-asterisk"></i><select
													ng-model="variavelItem.tipoDeEdicao" ng-required="true"
													class="form-control"><option value="READ_WRITE">Leitura
															e Escrita</option>
														<option value="READ_ONLY">Apenas Leitura</option></select></label> <i></i>
												</section>
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
															<button ng-click="tarefaItem.desvio.splice($index, 1);"
																class="btn btn-link" ng-disabled>
																<i class="fa fa-fa fa-trash"></i>
															</button>
															<button
																ng-click="tarefaItem.desvio[$index] = tarefaItem.desvio.splice($index - 1, 1, tarefaItem.desvio[$index])[0]"
																class="btn btn-link" ng-disabled="$index === 0">
																<i class="fa fa-fa fa-arrow-up"></i>
															</button>
															<button
																ng-click="tarefaItem.desvio[$index] = tarefaItem.desvio.splice($index + 1, 1, tarefaItem.desvio[$index])[0]"
																class="btn btn-link"
																ng-disabled="$index === tarefaItem.desvio.length - 1">
																<i class="fa fa-fa fa-arrow-down"></i>
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
								<section ng-show="tarefaItem.tipo == 'EMAIL'"
									class="col col-12 form-group"> <label for="texto"
									title="" class="label">Texto<i
									title="Preenchimento obrigatório"
									class="label-clue fa fa-asterisk"></i> <textarea
										ng-model="tarefaItem.texto" name="texto" ng-required="true"
										id="texto" class="form-control"></textarea></label> </section>
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

</siga:pagina>