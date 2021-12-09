<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

	<style type="text/css">
/* Quando se usa a classe 'disabled' TODOS os eventos de mouse são 
desabilitados, inclusive a exibição do title. Por isso fez-se necessário 
sobrescrever esse comportamento para poder mostrar o title.
 */
td.tramitacoes.fa-fw>a.disabled {
	cursor: auto; /* mantém o cursor do mouse como o padrão. */
	pointer-events: inherit;
}
</style>
<script type="text/javascript" language="Javascript1.1">
	function csv(id, action) {
		var frm = document.getElementById(id);
		frm.method = "POST";
		document.getElementById("exportar").disabled = true;
		sbmtAction(id, action);
		frm.action = 'listar';
		frm.method = "GET";
	}
</script>
<div id="inicio" class="row mb-3">
	<div class="col">		
		<button type="button" class="btn btn-outline-success" id="exportar" title="Exportar para CSV"	onclick="javascript:csv('listar', '/sigaex/app/expediente/doc/exportarCsv');"><i class="fa fa-file-csv"></i> Exportar</button>	
	</div>
</div>
<c:choose>
	<c:when test="${visualizacao == 2 or visualizacao == 4}">
		<div id="pivot" style="margin: 30px;"></div>


		<link rel="stylesheet" type="text/css"
			href="/siga/javascript/pivottable/pivot.css" />
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script type="text/javascript"
			src="/siga/javascript/pivottable/pivot.min.js"></script>
		<script type="text/javascript"
			src="/siga/javascript/pivottable/gchart_renderers.js"></script>
		<script>
			var data = [ 
				<c:forEach var="documento" items="${itens}">
					{
						"data": '<c:out value="${documento[0].dtDocDDMMYYYY}" />',
						"situação": '<c:out value="${documento[2].cpMarcador.descrMarcador}" />',
						"situação data": '<c:out value="${documento[0].dtDocDDMMYYYY}" />',
						"órgão": '<c:out value="${documento[0].orgaoUsuario.sigla}" />',
						"tipo": '<c:out value="${documento[0].exFormaDocumento.exTipoFormaDoc.descricao}" />',
						"espécie": '<c:out value="${documento[0].exFormaDocumento.descricao}" />',
						"modelo": '<c:out value="${documento[0].exModelo.nmMod}" />',
						"classificacao": '<c:out value="${documento[0].exClassificacao.sigla}" />',
						"origem": '<c:out value="${documento[0].exTipoDocumento.sigla}" />',
						"sigla": '<c:out value="${documento[1].codigo}" />',
						"subscritor sigla": '<c:out value="${documento[0].subscritor.sigla}" />',
						"subscritor nome": '<c:out value="${documento[0].subscritor.descricao}" />',
						"subscritor lotação sigla": '<c:out value="${documento[0].lotaSubscritor.sigla}" />',
						"subscritor lotação nome": '<c:out value="${documento[0].lotaSubscritor.descricao}" />',
						"atendente sigla": '<c:out value="${documento[2].dpPessoaIni.sigla}" />',
						"atendente nome": '<c:out value="${documento[2].dpPessoaIni.descricao}" />',
						"atendente lotação sigla": '<c:out value="${documento[2].dpLotacaoIni.lotacaoAtual.sigla}" />',
						"atendente lotação nome": '<c:out value="${documento[2].dpLotacaoIni.lotacaoAtual.descricao}" />';
						<c:if test="${visualizacao == 4}">
							<c:forEach var="campo" items="${campos.keySet()}">
								,"${campos.get(campo)}": '<c:out value="${documento[0].getFormConfidencial(titular, lotaTitular)[campo]}" />'
							</c:forEach>
						</c:if>
					},
				</c:forEach>								
			];

			 google.load("visualization", "1", {packages:["corechart", "charteditor"]});
	            $(function(){
	                var derivers = $.pivotUtilities.derivers;
	                var renderers = $.extend($.pivotUtilities.renderers, 
	                    $.pivotUtilities.gchart_renderers);

	        		var conf;
	        		try {
	        			conf = JSON.parse(localStorage.getItem('siga-doc-pivot'));
	        		} catch (e) {
		        	}
	        		if (conf == null) {
		        		conf = {
		        			rows : [],
		        			cols : []
		        		};
	        		}

                    $("#pivot").pivotUI(data, {
                        renderers: renderers,
                        cols: conf.cols, rows: conf.rows,
                        derivedAttributes : {
            				"ano" : function(record) {
            					return record.data.substring(6, 10);
            				},
            				"mês" : function(record) {
            					return record.data.substring(3, 5);
            				},
            				"dia" : function(record) {
            					return record.data.substring(0, 2);
            				},
            				"situação ano" : function(record) {
            					return record["situação data"].substring(6, 10);
            				},
            				"situação mês" : function(record) {
            					return record["situação data"].substring(3, 5);
            				},
            				"situação dia" : function(record) {
            					return record["situação data"].substring(0, 2);
            				}
            			},
            			hiddenAttributes : [ "data", "situação data" ],
            			onRefresh : function(config) {
            				var config_copy = JSON.parse(JSON.stringify(config));
            				// delete some values which are functions
            				delete config_copy["aggregators"];
            				delete config_copy["renderers"];
            				delete config_copy["derivedAttributes"];
            				// delete some bulky default values
            				delete config_copy["rendererOptions"];
            				delete config_copy["localeStrings"];
            				localStorage.setItem('siga-doc-pivot', JSON.stringify(config_copy));
            			}
                    });
	             });
			</script>
	</c:when>
	<c:otherwise>
		<div class="gt-content-box gt-for-table">
			<table class="gt-table table table-sm table-hover">
				<thead class="${thead_color}">
					<tr>
						<th rowspan="2" align="right">Número</th>
						<th colspan="3" align="center"><fmt:message key="documento.subscritor"/></th>
						<th colspan="4" align="center">Responsável pela situação atual</th>
						<th rowspan="2"><fmt:message key="documento.modelo2"/></th>
						<th rowspan="2"><fmt:message key="documento.descricao"/></th>
						<c:if test="${visualizacao == 1}">
							<th rowspan="2">Última Anotação</th>
						</c:if>
						<c:if test="${visualizacao == 3}">
							<c:forEach var="campo" items="${campos.keySet()}">
								<th rowspan="2" align="left">${campos.get(campo)}</th>
							</c:forEach>
						</c:if>
						<th id="colHistTramitacao" rowspan="2"></th>
					</tr>
					<tr>
						<th rowspan="1" align="center">Unidade</th>
						<th colspan="1" align="center">Matrícula</th>
						<th rowspan="1" align="center">Data</th>
						<th rowspan="1" align="center">Unidade</th>
						<th colspan="1" align="center">Matrícula</th>
						<th rowspan="1" align="center">Data</th>
						<th rowspan="1" align="center">Situação</th>
					</tr>
				</thead>

				<siga:paginador maxItens="${itemPagina}" maxIndices="0"
					totalItens="${tamanho}" itens="${itens}" var="documento">
					<c:choose>
						<c:when test="${documento[0].eletronico}">
							<c:set var="exibedoc" value="even" />
						</c:when>
						<c:otherwise>
							<c:set var="exibedoc" value="fisicoeven" />
						</c:otherwise>
					</c:choose>

					<tr class="${exibedoc}">
						<c:set var="podeAcessar"
							value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular, documento[1])}" />
						<c:set var="podeAcessar" value="true" />
						<td width="11.5%" align="right">
							<c:choose>
								<c:when test='${popup!="true"}'>
									<c:choose>
										<c:when test="${podeAcessar eq true}">
											<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento[1].sigla}&linkVolta=history.back();">
												${documento[1].codigo} 
											</a>
										</c:when>
										<c:otherwise> 
											${documento[1].codigo}
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<a href="javascript:opener.retorna_${propriedade}('${documento[1].id}','${documento[1].sigla}','${f:selDescricaoConfidencial(documento[1], lotaTitular, titular)}');">
										${documento[1].codigo} 
									</a>
								</c:otherwise>
							</c:choose>
						</td>
						<c:if test="${documento[1].numSequencia != 0}">
							<!-- resposavel assinatura -->
							<td width="4%" align="center">
								<siga:selecionado
									sigla="${documento[0].lotaSubscritor.sigla}"
									descricao="${documento[0].lotaSubscritor.descricao}"
									lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgaoUsu}${documento[0].lotaSubscritor.sigla}" />
							</td>
							<td width="4%" align="center">
								<siga:selecionado
									sigla="${documento[0].subscritor.iniciais}"
									descricao="${documento[0].subscritor.descricao}"
									pessoaParam="${documento[0].subscritor.sigla}" />
							</td>
							<td width="5%" align="center">
								${documento[0].dtDocDDMMYY}
							</td>
							<td width="4%" align="center">
								<siga:selecionado
									sigla="${documento[2].dpLotacaoIni.lotacaoAtual.sigla}"
									descricao="${documento[2].dpLotacaoIni.lotacaoAtual.descricao}"
									lotacaoParam="${documento[2].dpLotacaoIni.orgaoUsuario.siglaOrgaoUsu}${documento[2].dpLotacaoIni.sigla}" />
							</td>
							<td width="4%" align="center">
								<siga:selecionado
									sigla="${documento[2].dpPessoaIni.iniciais}"
									descricao="${documento[2].dpPessoaIni.descricao}"
									pessoaParam="${documento[2].dpPessoaIni.sigla}" />
							</td>
							<td width="10.5%" align="center">
								${documento[2].dtIniMarcaDDMMYYYY}
							</td>
							<td width="10.5%" align="center">
								${fn:substring(documento[2],0,fn:indexOf(documento[2],'['))}
							</td>
						</c:if>
						<c:if test="${documento[1].numSequencia == 0}">
							<td width="4%" align="center">
								<siga:selecionado
									sigla="${documento[0].lotaSubscritor.sigla}"
									descricao="${documento[0].lotaSubscritor.descricao}"
									lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgao}${documento[0].lotaSubscritor.sigla}" />
							</td>
							<td width="4%" align="center">
								<siga:selecionado
									sigla="${documento[0].subscritor.iniciais}"
									descricao="${documento[0].subscritor.descricao}"
									pessoaParam="${documento[0].subscritor.sigla}" />
							</td>
							<td width="5%" align="center">tag1</td>
							<td width="4%" align="center"></td>
							<td width="4%" align="center"></td>
							<td width="4%" align="center"></td>
							<td width="10.5%" align="center">tag4</td>
						</c:if>
						
						
						<td width="6%">${documento[0].nmMod}</td>

						<c:set var="acessivel" value="" />
						<c:set var="acessivel"
							value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular,documento[1])}" />
						<c:set var="acessivel" value="true" />
						<c:choose>
							<c:when test="${acessivel eq true}">
								<c:set var="estilo" value="" />
								<c:if
									test="${f:mostraDescricaoConfidencial(documento[0], titular, lotaTitular) eq true}">
									<c:set var="estilo" value="confidencial" />
								</c:if>
								<td class="${estilo}" width="38%">
									<c:choose>
										<c:when test="${siga_cliente == 'GOVSP'}">
											${documento[0].descrDocumento }
										</c:when>
										<c:otherwise>
											${f:descricaoSePuderAcessar(documento[0], titular, lotaTitular)}
										</c:otherwise>
									</c:choose>
								</td>
								<c:if test="${visualizacao == 1}">
									<td class="${estilo}" width="38%">
										${f:anotacaoConfidencial(documento[1], titular,lotaTitular)}
									</td>
								</c:if>
								<td class="tramitacoes fa-fw" style="min-width: 120px;">
									<c:choose>
										<c:when test="${not empty documento[1].getMovimentacoesPorTipo(ExTipoDeMovimentacao.TRANSFERENCIA, false)}">
											<%-- Tem Tramitação? --%>
											<c:set var="link"
												value="${pageContext.request.contextPath}/app/expediente/doc/exibirMovimentacoesTramitacao?idMobil=${documento[1].idMobil}&docCancelado=false" />
											<c:set var="title" value="Ver Histórico de Tramitação" />
											<c:set var="classDisabled" value="" />
										</c:when>
										<c:when test="${(documento[1].exTipoMobil.idTipoMobil == 1) and (documento[2].cpMarcador.idMarcador == 32)}">
											<%-- É a via principal? Ela foi cancelada (sem efeito)? --%>
											<c:set var="docTemTramitacoes" value="${false }" />
											<%-- Verifica se algumas das movimentações do documento tem movimentação. --%>
											<c:forEach var="mobil" items="${documento[0].exMobilSet}">
												<c:if test="${not empty mobil.getMovimentacoesPorTipo(ExTipoDeMovimentacao.TRANSFERENCIA, false) }">
													<c:set var="docTemTramitacoes" value="${true}" />
												</c:if>
											</c:forEach>
	
											<c:choose>
												<c:when test="${docTemTramitacoes}">
													<c:set var="link"
														value="${pageContext.request.contextPath}/app/expediente/doc/exibirMovimentacoesTramitacao?idMobil=${documento[1].idMobil}&docCancelado=true" />
													<c:set var="title" value="Ver Histórico de Tramitação" />
													<c:set var="classDisabled" value="" />
												</c:when>
												<c:otherwise>
													<c:set var="link" value="javascript:void(0)" />
													<c:set var="title" value="Não tem Histórico de Tramitação"/>
													<c:set var="classDisabled" value="disabled"/>
												</c:otherwise>
											</c:choose>

										</c:when>
										<c:otherwise>
											<c:set var="link" value="javascript:void(0)" />
											<c:set var="title" value="Não tem Histórico de Tramitação"/>
											<c:set var="classDisabled" value="disabled"/>
										</c:otherwise>
									</c:choose>

									<a class="fa fa-search btn btn-default btn-sm xrp-label ${classDisabled}"
										title="${title}" href="${link}">
									</a>
								</td>

							</c:when>
							<c:otherwise>
								<a
									href="javascript:opener.retorna_${propriedade}('${documento[1].id}','${documento[1].sigla}','${f:selDescricaoConfidencial(documento[1], lotaTitular, titular)}');">
									${documento[1].codigo} </a>
							</c:otherwise>
						</c:choose>
						</td>
						<c:if test="${visualizacao == 3}">
							<c:forEach var="campo" items="${campos.keySet()}">
								<td>${documento[0].getFormConfidencial(titular, lotaTitular)[campo]}</td>
							</c:forEach>
						</c:if>
					</tr>
				</siga:paginador>
				<c:if test="${currentPageNumber > 1}">
					<button type="button" class="btn btn-primary btn-sm active mr-1 mb-3" 
						onclick="javascript:sigaSpinner.mostrar();sbmt(${(currentPageNumber - 2) * itemPagina});">Anterior</button>
				</c:if>						
				<c:if test="${itemPagina lt tamanho}">
					<button type="button" class="btn btn-primary btn-sm active mr-1 mb-3"
						onclick="javascript:sigaSpinner.mostrar();sbmt(${currentPageNumber * itemPagina});">Próxima</button>
				</c:if>
				<c:if test="${currentPageNumber ne 0}">
					<button type="button" class="btn btn-primary btn-sm active mr-1 mb-3"
						onclick="javascript:sigaSpinner.mostrar();sbmt(${0});">Voltar para o início</button>
				</c:if>
				</tbody>
			</table>
		</div>
		<div id="final" class="col">
			<div class="d-inline position-fixed fixed-bottom" style="left:auto">
				<div class="float-right mr-3 opacity-80">
					<p>
						<a class="btn btn-light btn-circle" href="#inicio"> 
							<i class="fas fa-chevron-up h6"></i>
						</a>
					</p>
					<p>
						<a class="btn btn-light btn-circle" href="#final"> 
							<i class="fas fa-chevron-down h6"></i>
						</a>
					</p>
				</div>
			</div>				
		</div>		
	</c:otherwise>
</c:choose>