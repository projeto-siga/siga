<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<siga:pagina titulo="Gerência de Publicação Boletim Interno">
	<script type="text/javascript">
		function hideShowSel(combo){
			var sel1Span = document.getElementById('span'+combo.name.substring(4));
			var sel2Span = document.getElementById('spanLota'+combo.name.substring(4));
			if (combo.selectedIndex==0){
				sel1Span.style.display="";
				sel2Span.style.display="none";
			}else {
				sel1Span.style.display="none";
				sel2Span.style.display="";
			}
		}
		
		<ww:url id="url" action="gerenciar_publicacao_boletim" namespace="/expediente/configuracao">
			<ww:param name="id">${doc.idDoc}</ww:param>
		</ww:url>
		function sbmt(id) {
			var frm = document.getElementById('frm');
			frm.action='<ww:property value="%{url}"/>';
			frm.submit();
		}
		
	</script>
	<h1>Gerenciamento de permissões para solicitação de publicação no BI:</h1>
	<br />
	<ww:form name="frm" action="gerenciar_publicacao_boletim_gravar"
		namespace="/expediente/configuracao" method="POST" theme="simple">
		<ww:hidden name="postback" value="1" />
		<ww:hidden name="gerencia_publicacao" value="sim" />
		<ww:hidden name="idTpMov" value="36" />
		<ww:hidden name="idTpConfiguracao" value="1" />
		<ww:hidden name="" value="" />
		<table class="form">
			<tr class="header">
				<td colspan="2">Incluir Permissão de Publicação</td>
			</tr>
			<tr>
				<td>Forma:</td>
				<td><ww:select value="${idFormaDoc}" name="idFormaDoc" list="listaFormas" listKey="idFormaDoc"
					listValue="descrFormaDoc" headerKey="0" headerValue="[Todas]" onchange="javascript:sbmt();"/></td>
			</tr>
			<tr>
				<td>Modelo:</td>
				<td><ww:select value="${idMod}" name="idMod" list="listaModelosPorForma" listKey="idMod"
					listValue="nmMod" headerKey="0" headerValue="[Todos]" /></td>
			</tr>
			<tr>
				<td>Aplicar a:</td>
				<td><ww:select theme="simple" name="tipoPublicador"
					list="listaTipoPublicador" onchange="javascript:hideShowSel(this);" />
				<c:choose>
					<c:when test="${tipoPublicador == 1}">
						<c:set var="publicadorStyle" value="" />
						<c:set var="lotaPublicadorStyle">display:none</c:set>
					</c:when>
					<c:when test="${tipoPublicador == 2}">
						<c:set var="publicadorStyle">display:none</c:set>
						<c:set var="lotaPublicadorStyle" value="" />
					</c:when>
				</c:choose> <span id="spanPublicador" style="${publicadorStyle}"> <siga:selecao
					propriedade="pessoa" tema="simple" modulo="siga"/> </span> <span
					id="spanLotaPublicador" style="${lotaPublicadorStyle}"> <siga:selecao
					propriedade="lotacao" tema="simple" modulo="siga"/> </span></td>

			</tr>
			<tr>
			<td>Permissão:</td>
			<td><ww:select name="idSituacao" list="listaSituacaoPodeNaoPode"
					listKey="idSitConfiguracao" listValue="dscSitConfiguracao"
					theme="simple" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Incluir"></td>
			</tr>
		</table>
	</ww:form>
	<br />
	<br />
	<c:forEach var="config" items="${itens}">
		<h1>${config[0]}</h1>
		<table class="list">
			<tr class="header">
				<td align="center">Pessoa</td>
				<td align="center">Lotação</td>
				<td align="center">Permissão</td>
				<td align="center">Data de Fim de Vigência</td>
				<td align="center"></td>
				<td></td>
			</tr>
			<c:set var="evenorodd" value="" />

			<c:forEach var="configuracao" items="${config[1]}">
				<tr class="${evenorodd}">
					<c:choose>
						<c:when
							test="${(empty configuracao.dpPessoa) and (empty configuracao.lotacao)}">
							<td colspan="2" align="center">--Todos--</td>
						</c:when>
						<c:otherwise>
							<td><c:if test="${not empty configuracao.dpPessoa}">
						${configuracao.dpPessoa.descricao}
					</c:if></td>
							<td><c:if test="${not empty configuracao.lotacao}">
								<siga:selecionado sigla="${configuracao.lotacao.sigla}"
									descricao="${configuracao.lotacao.descricao}" />
							</c:if></td>
						</c:otherwise>
					</c:choose>

					<td>${configuracao.cpSituacaoConfiguracao.dscSitConfiguracao}</td>
					<td>${configuracao.hisDtFimDDMMYY_HHMMSS}<td>
					<td>
						<c:if test="${configuracao.hisDtFim != null}">
							<ww:url id="url"
								action="excluir_configuracao_publicacao_boletim"
								namespace="/expediente/configuracao">
								<ww:param name="id">${configuracao.idConfiguracao}</ww:param>
							</ww:url> <ww:a href="%{url}">Excluir</ww:a>
						</c:if>
					</td>
				</tr>
				<c:choose>
					<c:when test='${evenorodd == "even"}'>
						<c:set var="evenorodd" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="evenorodd" value="even" />
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</table>
	</c:forEach>
</siga:pagina>
