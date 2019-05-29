<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de configuração">

	<script type="text/javascript" language="Javascript1.1">
		$(document).ready(function() {
			alteraTipoDaForma();
			setTimeout("alteraForma()", 5000);
		});

		function alteraTipoDaForma() {
			ReplaceInnerHTMLFromAjaxResponse(
					'${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_formas?tipoForma='
							+ document.getElementById('tipoForma').value
							+ '&idFormaDoc=' + '${idFormaDoc}', null, document
							.getElementById('comboFormaDiv'), function() {alteraForma()});
		}

		function alteraForma() {
			// console.log('altera-forma', document.getElementById('idFormaDoc').value)
			var especie
			var inpEspecie = document.getElementById('idFormaDoc')
			if (inpEspecie)
				especie = inpEspecie.value
			var modelo = '${idMod}'
			var inpModelo = document.getElementById('idMod')
			if (inpModelo)
				modelo = inpModelo.value
			// if (modelo === undefined)
			// 	modelo = '';

			ReplaceInnerHTMLFromAjaxResponse(
					'${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_modelos?forma='
							+ especie + '&idMod=' + modelo, null, document
							.getElementById('comboModeloDiv'));
		}

		function sbmt() {
			editar_gravar = '${pageContext.request.contextPath}/app/expediente/configuracao/editar';
			editar_gravar.submit();
		}
	</script>

	<body onload="aviso()">

		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">

				<form action="editar_gravar">
					<input type="hidden" name="postback" value="1" /> <input
						type="hidden" name="nmTipoRetorno" value="${nmTipoRetorno}" /> <input
						type="hidden" name="id" value="${id}" />

					<c:set var="dataFim" value="" />

					<h1>Cadastro de configuração</h1>
					<div class="gt-content-box gt-for-table">
						<table class="gt-form-table" width="100%">
							<tr class="header">
								<td colspan="2">Dados da configurção</td>
							</tr>
							<tr>
								<td class="shrink"><b>Tipo de Configuração</b></td>
								<td><c:choose>
										<c:when
											test="${campoFixo && not empty config.cpTipoConfiguracao}">
											<input type="hidden" name="idTpConfiguracao"
												value="${config.cpTipoConfiguracao.idTpConfiguracao}" /> 
							${config.cpTipoConfiguracao.dscTpConfiguracao}
						</c:when>
										<c:otherwise>
											<siga:select name="idTpConfiguracao"
												list="listaTiposConfiguracao" listKey="idTpConfiguracao"
												id="idTpConfiguracao" headerValue="[Indefinido]"
												headerKey="0" listValue="dscTpConfiguracao" theme="simple"
												value="${idTpConfiguracao}" />
										</c:otherwise>
									</c:choose></td>
							</tr>
							<tr>
								<td class="shrink"><b>Situação</b></td>
								<td class="expand"><siga:select name="idSituacao"
										list="listaSituacao" listKey="idSitConfiguracao"
										listValue="dscSitConfiguracao" theme="simple"
										headerValue="[Indefinido]" headerKey="0" value="${idSituacao}" /></td>
							</tr>
							<tr>
								<td class="shrink">Nível de acesso</td>
								<td><siga:select name="idNivelAcesso"
										list="listaNivelAcesso" theme="simple" listKey="idNivelAcesso"
										listValue="nmNivelAcesso" headerValue="[Indefinido]"
										headerKey="0" value="${idNivelAcesso}" /></td>
							</tr>
							<tr>
								<td class="shrink">Perfil</td>
								<td><siga:select name="idPapel"
										list="listaPapel" theme="simple" listKey="idPapel"
										listValue="descPapel" headerValue="[Indefinido]"
										headerKey="0" value="${idPapel}" /></td>
							</tr>
							<tr>
								<td class="shrink">Pessoa</td>
								<td><siga:selecao propriedade="pessoa" tema="simple"
										modulo="siga" /></td>
							</tr>
							<tr>
								<td class="shrink">Lotação</td>
								<td><siga:selecao propriedade="lotacao" tema="simple"
										modulo="siga" /></td>
							</tr>
							<tr>
								<td class="shrink">Tipo lotação</td>
								<td><siga:select name="idTpLotacao"
										list="listaTiposLotacao" theme="simple" listKey="idTpLotacao"
										listValue="dscTpLotacao" headerValue="[Indefinido]"
										headerKey="0" value="${idTpLotacao}" /></td>
							</tr>

							<tr>
								<td class="shrink">Cargo</td>
								<td><siga:selecao propriedade="cargo" tema="simple"
										modulo="siga" /></td>
							</tr>
							<tr>
								<td class="shrink">Função de Confiança</td>
								<td><siga:selecao propriedade="funcao" tema="simple"
										modulo="siga" /></td>
							</tr>
							<tr>
								<td class="shrink">Órgão</td>
								<td><siga:select name="idOrgaoUsu" list="orgaosUsu"
										listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
										headerValue="[Indefinido]" headerKey="0" value="${idOrgaoUsu}" /></td>
							</tr>
							<tr>
								<td class="shrink">Tipo de Movimentação</td>
								<td><c:choose>
										<c:when
											test="${campoFixo && not empty config.exTipoMovimentacao}">
											<input type="hidden" name="idTpMov"
												value="${config.exTipoMovimentacao.idTpMov}" />
							${config.exTipoMovimentacao.descrTipoMovimentacao}
						</c:when>
										<c:otherwise>
											<siga:select name="idTpMov" list="listaTiposMovimentacao"
												listKey="idTpMov" listValue="descrTipoMovimentacao"
												theme="simple" headerValue="[Indefinido]" headerKey="0"
												value="${idTpMov}" />
										</c:otherwise>
									</c:choose></td>
							</tr>
							<tr>
								<td class="shrink">Origem</td>
								<td><siga:select name="idTpDoc" list="listaTiposDocumento"
										listKey="idTpDoc" listValue="descrTipoDocumento"
										theme="simple" headerValue="[Indefinido]" headerKey="0"
										value="${idTpDoc}" /></td>
							</tr>
							<tr>
								<td class="shrink">Espécie</td>
								<td><c:choose>
										<c:when test="${campoFixo && not empty config.exModelo}">
							${config.exModelo.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc} - ${config.exModelo.exFormaDocumento.descrFormaDoc}
						</c:when>
										<c:when
											test="${campoFixo && not empty config.exFormaDocumento}">
							${config.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc} - ${config.exFormaDocumento.descrFormaDoc}
						</c:when>
										<c:otherwise>
											<siga:select name="idTpFormaDoc" list="tiposFormaDoc"
												listKey="idTipoFormaDoc" listValue="descTipoFormaDoc"
												theme="simple" headerKey="0" headerValue="[Indefinido]"
												onchange="javascript:alteraTipoDaForma();" id="tipoForma"
												value="${idTpFormaDoc}" />&nbsp;&nbsp;&nbsp;
										   
					        <div style="display: inline" id="comboFormaDiv"></div>
										</c:otherwise>
									</c:choose></td>
							</tr>
							<!-- Esse timeout no modelo está estranho. Está sendo necessário porque primeiro
      		 precisa ser executado o request ajax referente Ã  FormaDocumento, da qual a lista 
		     de modelos depende. Talvez seria bom tornar síncronos esses dois requests ajax    -->
							<tr>
								<td class="shrink">Modelo</td>
								<td><c:choose>
										<c:when test="${campoFixo && not empty config.exModelo}">
											<input type="hidden" name="idMod"
												value="${config.exModelo.id}" />
							${config.exModelo.descMod}
						</c:when>
										<c:when
											test="${campoFixo && not empty config.exFormaDocumento}">
											<input type="hidden" name="idFormaDoc" value="${idFormaDoc}" />
							${config.exFormaDocumento.descrFormaDoc}
						</c:when>
										<c:otherwise>
											<div style="display: inline" id="comboModeloDiv"></div>
										</c:otherwise>
									</c:choose></td>
							</tr>
							<tr>
								<td class="shrink">Classificação</td>
								<td><siga:selecao propriedade="classificacao" tema="simple"
										modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar" /></td>
							</tr>
							<tr>
								<td class="shrink">Pessoa Objeto</td>
								<td><siga:selecao tipo="pessoa" propriedade="pessoaObjeto" tema="simple"
										modulo="siga" /></td>
							</tr>
							<tr>
								<td class="shrink">Lotação Objeto</td>
								<td><siga:selecao tipo="lotacao" propriedade="lotacaoObjeto" tema="simple"
										modulo="siga" /></td>
							</tr>
							<tr>
								<td class="shrink">Cargo Objeto</td>
								<td><siga:selecao tipo="cargo" propriedade="cargoObjeto" tema="simple"
										modulo="siga" /></td>
							</tr>
							<tr>
								<td class="shrink">Função de Confiança Objeto</td>
								<td><siga:selecao tipo="funcao" propriedade="funcaoObjeto" tema="simple"
										modulo="siga" /></td>
							</tr>
							<tr>
								<td class="shrink">Órgão Objeto</td>
								<td><siga:select name="idOrgaoObjeto" list="orgaosUsu"
										listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
										headerValue="[Indefinido]" headerKey="0"
										value="${idOrgaoObjeto}" /></td>
							</tr>
							<tr>
							</tr>
							<tr class="button">
								<td colspan="2"><input type="submit" value="Ok"
									class="gt-btn-large gt-btn-left" /> <input type="button"
									value="Cancela" onclick="javascript:history.back();"
									class="gt-btn-medium gt-btn-left" /></td>
								<td></td>
							</tr>
						</table>
					</div>
					<br />
			</div>
		</div>
	</body>
</siga:pagina>
