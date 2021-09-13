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
						<th rowspan="3" align="right">Número</th>
						<th colspan="3" align="center">Documento</th>
						<th colspan="4" align="center">Situação</th>
						<th rowspan="3">Tipo</th>
						<th rowspan="3"><fmt:message key="documento.modelo2"/></th>
						<th rowspan="3"><fmt:message key="documento.descricao"/></th>
						<c:if test="${visualizacao == 1}">
							<th rowspan="3">Última Anotação</th>
						</c:if>
						<c:if test="${visualizacao == 3}">
							<c:forEach var="campo" items="${campos.keySet()}">
								<th rowspan="3" align="left">${campos.get(campo)}</th>
							</c:forEach>
						</c:if>
					</tr>
					<tr>
						<th rowspan="2" align="center"><fmt:message key="documento.data.assinatura"/></th>
						<th colspan="2" align="center"><fmt:message key="documento.subscritor"/></th>
						<th rowspan="2" align="center">Data</th>
						<th colspan="2" align="center">Atendente</th>
						<th rowspan="2" align="center">Situação</th>
					</tr>
					<tr>
						<th align="center"><fmt:message key="usuario.lotacao"/></th>
						<th align="center"><fmt:message key="usuario.pessoa"/></th>
						<th align="center"><fmt:message key="usuario.lotacao"/></th>
						<th align="center"><fmt:message key="usuario.pessoa"/></th>
					</tr>

				</thead>

				<siga:paginador maxItens="${itemPagina}" maxIndices="10"
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
						<td width="11.5%" align="right"><c:choose>
								<c:when test='${popup!="true"}'>
									<c:choose>
										<c:when test="${podeAcessar eq true}">
											<a
												href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento[1].sigla}">
												${documento[1].codigo} </a>
										</c:when>
										<c:otherwise> 
										${documento[1].codigo}
									</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<a
										href="javascript:opener.retorna_${propriedade}('${documento[1].id}','${documento[1].sigla}','${f:selDescricaoConfidencial(documento[1], lotaTitular, titular)}');">
										${documento[1].codigo} </a>
								</c:otherwise>
							</c:choose></td>
						<c:if test="${documento[1].numSequencia != 0}">
							<td width="5%" align="center">
								${documento[0].dtDocDDMMYY}</td>
							<td width="4%" align="center"><siga:selecionado
									sigla="${documento[0].lotaSubscritor.sigla}"
									descricao="${documento[0].lotaSubscritor.descricao}"
									lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgaoUsu}${documento[0].lotaSubscritor.sigla}" />
							</td>
							<td width="4%" align="center"><siga:selecionado
									sigla="${documento[0].subscritor.iniciais}"
									descricao="${documento[0].subscritor.descricao}"
									pessoaParam="${documento[0].subscritor.sigla}" /></td>
							<td width="5%" align="center">
								${documento[2].dtIniMarcaDDMMYYYY}</td>
							<td width="4%" align="center"><siga:selecionado
									sigla="${documento[2].dpLotacaoIni.lotacaoAtual.sigla}"
									descricao="${documento[2].dpLotacaoIni.lotacaoAtual.descricao}"
									lotacaoParam="${documento[2].dpLotacaoIni.orgaoUsuario.siglaOrgaoUsu}${documento[2].dpLotacaoIni.sigla}" />
							</td>
							<td width="4%" align="center"><siga:selecionado
									sigla="${documento[2].dpPessoaIni.iniciais}"
									descricao="${documento[2].dpPessoaIni.descricao}"
									pessoaParam="${documento[2].dpPessoaIni.sigla}" />
							</td>


							<td width="10.5%" align="center">
								${documento[2].cpMarcador.descrMarcador}</td>
						</c:if>
						<c:if test="${documento[1].numSequencia == 0}">
							<td width="5%" align="center">
								${documento[0].dtDocDDMMYY}</td>

							<td width="4%" align="center"><siga:selecionado
									sigla="${documento[0].lotaSubscritor.sigla}"
									descricao="${documento[0].lotaSubscritor.descricao}"
									lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgao}${documento[0].lotaSubscritor.sigla}" />
							</td>
							<td width="4%" align="center"><siga:selecionado
									sigla="${documento[0].subscritor.iniciais}"
									descricao="${documento[0].subscritor.descricao}"
									pessoaParam="${documento[0].subscritor.sigla}" /></td>


							<td width="5%" align="center">tag1</td>
							<td width="4%" align="center"></td>
							<td width="4%" align="center"></td>
							<td width="10.5%" align="center">tag4</td>
						</c:if>
						<td width="6%">${documento[0].descrFormaDoc}</td>

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
									${f:descricaoSePuderAcessar(documento[0], titular, lotaTitular)}
								</td>
								<c:if test="${visualizacao == 1}">
									<td class="${estilo}" width="38%">
										${f:anotacaoConfidencial(documento[1], titular,lotaTitular)}
									</td>
								</c:if>

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
				</tbody>
			</table>
		</div>
	</c:otherwise>
</c:choose>