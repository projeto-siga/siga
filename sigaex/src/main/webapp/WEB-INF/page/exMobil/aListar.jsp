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
	function alteraTipoDaForma() {
		ReplaceInnerHTMLFromAjaxResponse(
				'${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_formas?tipoForma='
						+ document.getElementById('tipoForma').value
						+ '&idFormaDoc=' + '${idFormaDoc}', null, document
						.getElementById('comboFormaDiv'))
	}

	function alteraForma() {
		ReplaceInnerHTMLFromAjaxResponse(
				'${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_modelos?forma='
						+ document.getElementById('idFormaDoc').value
						+ '&idMod='	+ '${idMod}', null, document
						.getElementById('comboModeloDiv'))
	}

	function sbmtAction(id, action) {
		var frm = document.getElementById(id);
		frm.action = action;
		frm.submit();
		return;
	}

	function sbmt(offset) {
		if (offset == null) {
			offset = 0;
		}
		listar["paramoffset"].value = offset;
		listar["p.offset"].value = offset;
		listar.submit();
	}

	function montaDescricao(id, via, descrDoc) {
		var popW = 700;
		var popH = 500;
		var winleft = (screen.width - popW) / 2;
		var winUp = (screen.height - popH) / 2;
		var winProp = '\'width=' + popW + ',height=' + popH + ',left='
				+ winleft + ',top=' + winUp + ',scrollbars=yes,resizable\'';
		var url = '\'<c:url value="/app/expediente/doc/exibir"/>?popup=true&id='
				+ id + '&via=' + via + '\'';

		var onclick = ' onclick="javascript:window.open(' + url
				+ ',\'documento\',' + winProp + ')"';
		var href = ' href="javascript:void(0)"';

		var a = '<a'+href+onclick+'>' + descrDoc + '</a>';
		return a;
	}

	function alteraOrigem() {
		var objSelecionado = document.getElementById('idTpDoc');

		switch (parseInt(objSelecionado.value)) {
		case 0:
			document.getElementById('trNumOrigDoc').style.display = 'none';
			document.getElementById('trNumDocSistAntigo').style.display = 'none';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';

			document.getElementById('idFormaDoc').value = '0';
			break;
		case 1: // Interno Produzido
			document.getElementById('trNumOrigDoc').style.display = 'none';
			document.getElementById('trNumDocSistAntigo').style.display = 'none';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';

			document.getElementById('idFormaDoc').value = '0';
			break;
		case 2: // Interno Folha de Rosto
			document.getElementById('trNumOrigDoc').style.display = '';
			document.getElementById('trNumDocSistAntigo').style.display = '';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';

			document.getElementById('idFormaDoc').value = '0';
			break;
		case 3: // Externo Folha de Rosto
			document.getElementById('trNumOrigDoc').style.display = '';
			document.getElementById('trNumDocSistAntigo').style.display = '';
			document.getElementById('trOrgExterno').style.display = '';
			document.getElementById('trTipo').style.display = 'none';

			document.getElementById('idFormaDoc').value = '5';
			break;
		case 4: // Externo Capturado
			document.getElementById('trNumOrigDoc').style.display = '';
			document.getElementById('trNumDocSistAntigo').style.display = 'none';
			document.getElementById('trOrgExterno').style.display = '';
			document.getElementById('trTipo').style.display = 'none';

			document.getElementById('idFormaDoc').value = '0';
			break;
		case 5: // Interno Capturado
			document.getElementById('trNumOrigDoc').style.display = 'none';
			document.getElementById('trNumDocSistAntigo').style.display = 'none';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';

			document.getElementById('idFormaDoc').value = '0';
			break;
		}
	}

	function alteraAtendente() {
		var objSelecionado = document.getElementById('ultMovTipoResp');

		switch (parseInt(objSelecionado.value)) {
		case 1:
			document.getElementById('divUltMovResp').style.display = '';
			document.getElementById('divUltMovLotaResp').style.display = 'none';
			break;
		case 2:
			document.getElementById('divUltMovResp').style.display = 'none';
			document.getElementById('divUltMovLotaResp').style.display = '';
			break;
		}
	}

	function alteraCadastranteDocumento() {
		var objSelecionado = document.getElementById('tipoCadastrante');

		switch (parseInt(objSelecionado.value)) {
		case 1:
			document.getElementById('divCadastrante').style.display = '';
			document.getElementById('divLotaCadastrante').style.display = 'none';
			break;
		case 2:
			document.getElementById('divCadastrante').style.display = 'none';
			document.getElementById('divLotaCadastrante').style.display = '';
			break;
		}
	}

	function alteraDestinatarioDocumento() {
		var objSelecionado = document.getElementById('tipoDestinatario');

		switch (parseInt(objSelecionado.value)) {
		case 1:
			document.getElementById('divDestinatario').style.display = '';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		case 2:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = '';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		case 3:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = '';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		case 4:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = '';
			break;
		}
	}

	function limpaCampos() {

		var ultMovTipoResp = document.getElementById('ultMovTipoResp');

		switch (parseInt(ultMovTipoResp.value)) {
		case 1:
			document.getElementById('formulario_ultMovLotaRespSel_id').value = '';
			document.getElementById('formulario_ultMovLotaRespSel_descricao').value = '';
			document.getElementById('formulario_ultMovLotaRespSel_buscar').value = '';
			document.getElementById('formulario_ultMovLotaRespSel_sigla').value = '';
			document.getElementById('ultMovLotaRespSelSpan').innerHTML = '';
			break;
		case 2:
			document.getElementById('formulario_ultMovRespSel_id').value = '';
			document.getElementById('formulario_ultMovRespSel_descricao').value = '';
			document.getElementById('formulario_ultMovRespSel_buscar').value = '';
			document.getElementById('formulario_ultMovRespSel_sigla').value = '';
			document.getElementById('ultMovRespSelSpan').innerHTML = '';
			break;
		}

		var tipoCadastrante = document.getElementById('tipoCadastrante');

		switch (parseInt(tipoCadastrante.value)) {
		case 1:
			document.getElementById('formulario_lotaCadastranteSel_id').value = '';
			document.getElementById('formulario_lotaCadastranteSel_descricao').value = '';
			document.getElementById('formulario_lotaCadastranteSel_buscar').value = '';
			document.getElementById('formulario_lotaCadastranteSel_sigla').value = '';
			document.getElementById('lotaCadastranteSelSpan').innerHTML = '';
			break;
		case 2:
			document.getElementById('formulario_cadastranteSel_id').value = '';
			document.getElementById('formulario_cadastranteSel_descricao').value = '';
			document.getElementById('formulario_cadastranteSel_buscar').value = '';
			document.getElementById('formulario_cadastranteSel_sigla').value = '';
			document.getElementById('cadastranteSelSpan').innerHTML = '';
			break;
		}

		var tipoDestinatario = document.getElementById('tipoDestinatario');

		switch (parseInt(tipoDestinatario.value)) {
		case 1:
			document.getElementById('formulario_lotacaoDestinatarioSel_id').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqlotacaoDestinatarioSel').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_sigla').value = '';
			document.getElementById('lotacaoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_id').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqorgaoExternoDestinatarioSel').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_sigla').value = '';
			document.getElementById('orgaoExternoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('nmDestinatario').value = '';

			break;
		case 2:
			document.getElementById('formulario_destinatarioSel_id').value = '';
			document.getElementById('formulario_destinatarioSel_descricao').value = '';
			document.getElementById('formulario_destinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqdestinatarioSel').value = '';
			document.getElementById('formulario_destinatarioSel_sigla').value = '';
			document.getElementById('destinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_id').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqorgaoExternoDestinatarioSel').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_sigla').value = '';
			document.getElementById('orgaoExternoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('nmDestinatario').value = '';

			break;
		case 3:
			document.getElementById('formulario_destinatarioSel_id').value = '';
			document.getElementById('formulario_destinatarioSel_descricao').value = '';
			document.getElementById('formulario_destinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqdestinatarioSel').value = '';
			document.getElementById('formulario_destinatarioSel_sigla').value = '';
			document.getElementById('destinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_id').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqlotacaoDestinatarioSel').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_sigla').value = '';
			document.getElementById('lotacaoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('nmDestinatario').value = '';

			break;
		case 4:
			document.getElementById('formulario_destinatarioSel_id').value = '';
			document.getElementById('formulario_destinatarioSel_descricao').value = '';
			document.getElementById('formulario_destinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqdestinatarioSel').value = '';
			document.getElementById('formulario_destinatarioSel_sigla').value = '';
			document.getElementById('destinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_id').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqlotacaoDestinatarioSel').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_sigla').value = '';
			document.getElementById('lotacaoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_id').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqorgaoExternoDestinatarioSel').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_sigla').value = '';
			document.getElementById('orgaoExternoDestinatarioSelSpan').innerHTML = '';

			break;
		}

		var listar_idTpDoc = document.getElementById('idTpDoc');

		switch (parseInt(listar_idTpDoc.value)) {
		case 0:
			document.getElementById('numExtDoc').value = '';

			document.getElementById('formulario_cpOrgaoSel_id').value = '';
			document.getElementById('formulario_cpOrgaoSel_descricao').value = '';
			document.getElementById('formulario_cpOrgaoSel_buscar').value = '';
			document.getElementById('formulario_cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			document.getElementById('numAntigoDoc').value = '';

			break;
		case 1:
			document.getElementById('numExtDoc').value = '';
			document.getElementById('formulario_cpOrgaoSel_id').value = '';
			document.getElementById('formulario_cpOrgaoSel_descricao').value = '';
			document.getElementById('formulario_cpOrgaoSel_buscar').value = '';
			document.getElementById('formulario_cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			document.getElementById('numAntigoDoc').value = '';

			break;
		case 2:
			document.getElementById('formulario_cpOrgaoSel_id').value = '';
			document.getElementById('formulario_cpOrgaoSel_descricao').value = '';
			document.getElementById('formulario_cpOrgaoSel_buscar').value = '';
			document.getElementById('formulario_cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';

			break;
		case 3:
			document.getElementById('idFormaDoc').value = '5';

			break;
		}

		return true;
	}
</script>

<siga:pagina titulo="Lista de Expedientes" popup="${popup}">
	<div class="container-fluid content mb-3">
		<c:if
			test="${((empty primeiraVez) or (primeiraVez != 'sim')) and ((empty apenasRefresh) or (apenasRefresh != 1))}">
			<c:if test="${not empty tamanho and tamanho > 0}">
				<h2 class="mt-3"><fmt:message key="documento.encontrados"/></h2>
				<c:choose>
					<c:when test="${siga_cliente == 'GOVSP'}">
						<jsp:include page="./listaSP.jsp"/>
					</c:when>
					<c:otherwise>
						<jsp:include page="./lista.jsp"/>
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${empty tamanho or tamanho == 0}">
				<h2 class="mt-3"><fmt:message key="documento.encontrados"/></h2>
				<p class="gt-notice-box">A pesquisa não retornou resultados.</p>
			</c:if>
		</c:if>		
		
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Pesquisar Documentos</h5>
			</div>
			<div class="card-body">
				<form id="listar" name="listar"
					onsubmit="javascript: return limpaCampos()" action="listar"
					method="get" class="form100">
					<input type="hidden" name="popup" value="${popup}" /> <input
						type="hidden" name="propriedade" value="${propriedade}" /> <input
						type="hidden" name="postback" value="1" /> <input type="hidden"
						name="apenasRefresh" value="0" /> <input type="hidden"
						name="paramoffset" value="0" /> <input type="hidden"
						name="p.offset" value="0" />

					<c:if test="${siga_cliente == 'GOVSP'}">
						<div class="form-row">
							<div class="form-group col-md-6">
								<label for="classificacao"><fmt:message key="documento.descricao"/></label> <input
									class="form-control" type="text" name="descrDocumento"
									value="${descrDocumento}" size="80" />
							</div>
						</div>
					</c:if>

					<div class="form-row">
						<div class="form-group col-md-6">
							<label for="ultMovIdEstadoDoc">Situação</label> <select
								class="form-control" id="ultMovIdEstadoDoc"
								name="ultMovIdEstadoDoc">
								<option value="0">[Todos]</option>
								<c:forEach items="${estados}" var="item">
									<option value="${item.idMarcador}"
										${item.idMarcador == ultMovIdEstadoDoc ? 'selected' : ''}>
										${item.descrMarcador}</option>
								</c:forEach>
							</select>
						</div>

						<div class="form-group col-md-2">
							<label for="ultMovTipoResp"><fmt:message key="usuario.pessoa"/>/<fmt:message key="usuario.lotacao"/></label> <select
								class="form-control" id="ultMovTipoResp" name="ultMovTipoResp"
								onchange="javascript:alteraAtendente();">
								<c:forEach items="${listaTipoResp}" var="item">
									<option value="${item.key}"
										${item.key == ultMovTipoResp ? 'selected' : ''}>
										${item.value}</option>
								</c:forEach>
							</select>
						</div>
						<c:if test="${ultMovTipoResp == 1}">
							<div id="divUltMovResp" style="display:"
								class="form-group col-md-4">
								<label for="ultMovTipoResp"><fmt:message key="tela.pesquisa.pessoa"/></label>
								<siga:selecao propriedade="ultMovResp" tema="simple"
									paramList="buscarFechadas=true" modulo="siga" />
							</div>
							<div id="divUltMovLotaResp" style="display: none"
								class="form-group col-md-4">
								<label for="ultMovTipoResp"><fmt:message key="usuario.lotacao"/></label>
								<siga:selecao propriedade="ultMovLotaResp" tema="simple"
									paramList="buscarFechadas=true" modulo="siga" />
							</div>
						</c:if>
						<c:if test="${ultMovTipoResp == 2}">
							<div id="divUltMovResp" style="display: none"
								class="form-group col-md-4">
								<label for="ultMovTipoResp"><fmt:message key="tela.pesquisa.pessoa"/></label>
								<siga:selecao propriedade="ultMovResp" tema="simple"
									paramList="buscarFechadas=true" modulo="siga" />
							</div>
							<div id="divUltMovLotaResp" style="display:"
								class="form-group col-md-4">
								<label for="ultMovTipoResp"><fmt:message key="usuario.lotacao"/></label>
								<siga:selecao propriedade="ultMovLotaResp" tema="simple"
									paramList="buscarFechadas=true" modulo="siga" />
							</div>
						</c:if>
					</div>

					<div class="form-row">
						<div class="form-group col-md-3">
							<label for="orgaoUsu">Órgão</label> <select class="form-control"
								id="orgaoUsu" name="orgaoUsu">
								<option value="0">[Todos]</option>
								<c:forEach items="${orgaosUsu}" var="item">
									<option value="${item.idOrgaoUsu}"
										${item.idOrgaoUsu == orgaoUsu ? 'selected' : ''}>
										${item.nmOrgaoUsu}</option>
								</c:forEach>
							</select>
						</div>
						<c:if test="${siga_cliente != 'GOVSP'}">
							<div class="form-group col-md-3">
								<label for="idTpDoc">Origem</label> <select class="form-control"
									id="idTpDoc" name="idTpDoc"
									onchange="javascript:alteraOrigem();">
								<option value="0">[Todos]</option>
								<c:forEach items="${tiposDocumento}" var="item">
									<option value="${item.idTpDoc}"
										${item.idTpDoc == idTpDoc ? 'selected' : ''}>
										${item.descrTipoDocumento}</option>
								</c:forEach>
								</select>
							</div>
						</c:if>
						<div class="form-group col-md-3">
							<label for="dtDocString">Data Inicial</label> <input
								class="form-control" type="text" name="dtDocString"
								id="dtDocString" value="${dtDocString}"
								onblur="javascript:verifica_data(this,0);" />
						</div>
						<div class="form-group col-md-3">
							<label for="dtDocFinalString">Data Final</label> <input
								class="form-control" type="text" name="dtDocFinalString"
								id="dtDocFinalString" value="${dtDocFinalString}"
								onblur="javascript:verifica_data(this,0);" />
						</div>
					</div>
					
					<div id="trTipo" style="display:${idTpDoc == 3 ? 'none' : ''}"
						class="form-row">
						<div class="form-group col-md-3 ${hide_only_GOVSP}">
							<label for="tipoForma">Tipo da Espécie</label> <select
								class="form-control" id="tipoForma" name="idTipoFormaDoc"
								onchange="javascript:alteraTipoDaForma();">
								<option value="0">[Todos]</option>
								<c:forEach items="${tiposFormaDoc}" var="item">
									<option value="${item.idTipoFormaDoc}"
										${item.idTipoFormaDoc == idTipoFormaDoc ? 'selected' : ''}>
										${item.descTipoFormaDoc}</option>
								</c:forEach>
							</select>
						</div>

						<div class="form-group col-md-3">
							<div style="display: inline" id="comboFormaDiv"></div>
							<script type="text/javascript">
									alteraTipoDaForma();
								</script>
						</div>

						<div class="form-group col-md-6">
							<div style="display: inline" id="comboModeloDiv"></div>
							<script type="text/javascript">
							setTimeout("alteraForma()", 2000);
								</script>
						</div>
					</div>

					<div class="form-row">
						<div class="form-group col-md-3">
							<label for="anoEmissaoString">Ano de Emissão</label> <select
								class="form-control" id="anoEmissaoString"
								name="anoEmissaoString">
								<option value="0">[Todos]</option>
								<c:forEach items="${listaAnos}" var="item">
									<option value="${item}"
										${item == anoEmissaoString ? 'selected' : ''}>${item}</option>
								</c:forEach>
							</select>
						</div>
						<c:if test="${siga_cliente == 'GOVSP'}">
							<div class="form-group col-md-3">
								<label for="numExpediente">Número</label>
							    <input type="text" size="7" name="numExpediente" value="${numExpediente}" maxlength="6" class="form-control" />
							</div>
						</c:if>
					</div>
					<div class="form-row">
						<div class="form-group col-md-3" id="trNumOrigDoc"
							style="display:${idTpDoc == 2 || idTpDoc == 3 ? '' : 'none'}">
							<label for="numExtDoc">Nº Original do Documento</label> <input
								class="form-control" type="text" name="numExtDoc" size="16"
								id="numExtDoc" value="${numExtDoc}" />
						</div>

						<div class="form-group col-md-6" id="trOrgExterno"
							style="display:${idTpDoc == 3 ? '' : 'none'}">
							<label for="cpOrgao">Órgão Externo</label>
							<siga:selecao propriedade="cpOrgao" modulo="siga"
								titulo="Órgão Externo" tema="simple" />
						</div>

						<div class="form-group col-md-3" id="trNumDocSistAntigo"
							style="display:${idTpDoc == 3 ? '' : 'none'}">
							<label for="numAntigoDoc">Nº do Documento no Sistema
								Antigo</label> <input class="form-control" type="text"
								name="numAntigoDoc" value="${numAntigoDoc}" size="16" value=""
								id="numAntigoDoc" />
						</div>

						<div class="form-group col-md-3" id="trSubscritorExt"
							style="display:${idTpDoc == 3 or idTpDoc == 4 ? '' : 'none'}">
							<label for="nmSubscritorExt"><fmt:message key="documento.subscritor.antigo"/></label> <input
								class="form-control" type="text" label="Subscritor"
								name="nmSubscritorExt" value="${nmSubscritorExt}" size="80" />
						</div>

						<div class="form-group col-md-6" id="trSubscritor"
							style="display:${idTpDoc != 3 and idTpDoc != 4 ? '' : 'none'}">
							<label for="subscritor"><fmt:message key="documento.subscritor"/></label>
							<siga:selecao titulo="Subscritor:" propriedade="subscritor"
								paramList="buscarFechadas=true" modulo="siga" tema="simple" />
						</div>
					</div>

					<div class="form-row">
						<div class="form-group col-md-2">
							<label for="tipoCadastrante"><fmt:message key="documento.cadastrante"/></label> <select
								class="form-control" id="tipoCadastrante" name="tipoCadastrante"
								onchange="javascript:alteraCadastranteDocumento();">
								<c:forEach items="${listaTipoResp}" var="item">
									<option value="${item.key}"
										${item.key == tipoCadastrante ? 'selected' : ''}>
										${item.value}</option>
								</c:forEach>
							</select>
						</div>
						<c:if test="${tipoCadastrante == 1}">
							<div id="divCadastrante" style="display:"
								class="form-group col-md-4">
								<label for="ultMovTipoResp"><fmt:message key="tela.pesquisa.pessoa"/></label>
								<siga:selecao propriedade="cadastrante" tema="simple"
									paramList="buscarFechadas=true" modulo="siga" />
							</div>
							<div id="divLotaCadastrante" style="display: none"
								class="form-group col-md-4">
								<label for="ultMovTipoResp"><fmt:message key="usuario.lotacao"/></label>
								<siga:selecao propriedade="lotaCadastrante" tema="simple"
									paramList="buscarFechadas=true" modulo="siga" />
							</div>
						</c:if>
						<c:if test="${tipoCadastrante == 2}">
							<div id="divCadastrante" style="display: none"
								class="form-group col-md-4">
								<label for="ultMovTipoResp"><fmt:message key="tela.pesquisa.pessoa"/></label>
								<siga:selecao propriedade="cadastrante" tema="simple"
									paramList="buscarFechadas=true" modulo="siga" />
							</div>
							<div id="divLotaCadastrante" style="display:"
								class="form-group col-md-4">
								<label for="ultMovTipoResp"><fmt:message key="usuario.lotacao"/></label>
								<siga:selecao propriedade="lotaCadastrante" tema="simple"
									paramList="buscarFechadas=true" modulo="siga" />
							</div>
						</c:if>
						<div class="form-group col-md-2">
							<label for="tipoDestinatario"><fmt:message key="documento.destinatario"/></label> <select
								class="form-control" id="tipoDestinatario"
								name="tipoDestinatario"
								onchange="javascript:alteraDestinatarioDocumento();">
								<c:forEach items="${listaTipoDest}" var="item">
									<option value="${item.key}"
										${item.key == tipoDestinatario ? 'selected' : ''}>
										${item.value}</option>
								</c:forEach>
							</select>
						</div>
						<div id="divDestinatario"
							style="display:${tipoDestinatario == 1 ? '':'none'}"
							class="form-group col-md-4">
							<label for="destinatario"><fmt:message key="tela.pesquisa.pessoa"/></label>
							<siga:selecao propriedade="destinatario" tema="simple"
								paramList="buscarFechadas=true" modulo="siga" />
						</div>
						<div id="divLotaDestinatario"
							style="display: ${tipoDestinatario == 2 ? '':'none'}"
							class="form-group col-md-4">
							<label for="lotacaoDestinatario"><fmt:message key="usuario.lotacao"/></label>
							<siga:selecao propriedade="lotacaoDestinatario" tema="simple"
								paramList="buscarFechadas=true" modulo="siga" />
						</div>
						<div id="divOrgaoExternoDestinatario"
							style="display: ${tipoDestinatario == 3 ? '':'none'}"
							class="form-group col-md-4">
							<label for="orgaoExternoDestinatario">Órgão Externo</label>
							<siga:selecao propriedade="orgaoExternoDestinatario"
								tema="simple" modulo="siga" />
						</div>
						<div id="divNmDestinatario"
							style="display: ${tipoDestinatario == 4 ? '':'none'}"
							class="form-group col-md-4">
							<label for="nmDestinatario">Campo Livre</label> <input
								class="form-control" type="text" name="nmDestinatario"
								id="nmDestinatario" value="${nmDestinatario}" size="80" />
						</div>
					</div>

					<c:if test="${siga_cliente != 'GOVSP'}">
						<div class="form-row">
							<div class="form-group col-md-6">
								<label for="classificacao"><fmt:message key="documento.descricao"/></label> <input
									class="form-control" type="text" name="descrDocumento"
									value="${descrDocumento}" size="80" />
							</div>
						</div>
					</c:if>
					
					${f:obterExtensaoBuscaTextualbs4(lotaTitular.orgaoUsuario, fullText)} 

					<div class="form-row">
						<div class="form-group col-md-6">
							<label for="classificacao"><fmt:message key="tela.pesquisa.classificacao"/></label>
							<siga:selecao propriedade="classificacao" modulo="sigaex" tema="simple"
								urlAcao="buscar" urlSelecionar="selecionar" />

								
						</div>

						<div class="form-group col-md-3">
							<label for="ordem"><fmt:message key="tela.pesquisa.ordenacao"/></label> <select class="form-control"
								id="ordem" name="ordem" onchange="javascript:sbmt();">
								<c:forEach items="${listaOrdem}" var="item">
									<option value="${item.key}"
										${item.key == ordem ? 'selected' : ''}>${item.value}</option>
								</c:forEach>
							</select>
						</div>

						<div class="form-group col-md-3">
							<label for="visualizacao"><fmt:message key="tela.pesquisa.visualizacao"/></label> <select
								class="form-control" id="visualizacao" name="visualizacao"
								onchange="javascript:sbmt();">
								<c:forEach items="${listaVisualizacao}" var="item">
									<option value="${item.key}"
										${item.key == visualizacao ? 'selected' : ''}>
										${item.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<input type="submit" value="Buscar" class="btn btn-primary"/>
					<c:if
						test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;FE:Ferramentas;LD:Listar Documentos')}">
						<siga:monobotao inputType="button"
							onclique="sbmtAction('listar', '/sigaex/app/ferramentas/doc/listar');"
							value="Administrar Documentos" cssClass="btn btn-primary" />
					</c:if>
					<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-primary" />
				</form>
			</div>
		</div>
	</div>
	<script>
		alteraOrigem();
	</script>
	<c:if test="${siga_cliente == 'GOVSP'}">
		<script>
		$(document).ready(function() {
			alteraTipoDaForma()
		});
		</script>
	</c:if>
</siga:pagina>