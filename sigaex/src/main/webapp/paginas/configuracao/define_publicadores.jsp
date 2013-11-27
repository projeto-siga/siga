<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<siga:pagina titulo="Definição de Publicadores">
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
	</script>
	<h1>Definição de lotações e servidores permitidos para publicação direta
	no DJE:</h1>
	<br />
	<ww:form name="frm" action="definir_publicadores_gravar"
		namespace="/expediente/configuracao" method="POST" theme="simple">
		<ww:hidden name="postback" value="1" />
		<ww:hidden name="define_publicadores" value="sim" />
		<ww:hidden name="idTpMov" value="32" />
		<ww:hidden name="idTpConfiguracao" value="1" />
		<ww:hidden name="idSituacao" value="1" />
		<ww:hidden name="" value="" />
		<table class="form">
			<tr class="header">
				<td colspan="2">Incluir Publicador</td>
			</tr>
			<tr>
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
				<td><input type="submit" value="Incluir"></td>
			</tr>
		</table>
	</ww:form>
	<br />
	<br />
	<table class="list">
		<tr class="header">
			<td align="center">Pessoa</td>
			<td align="center">Lotação</td>
			<td></td>
		</tr>
		<c:set var="evenorodd" value="" />
		<c:set var="tamanho" value="0" />

		<c:forEach var="configuracao" items="${publicadores}">
			<tr class="${evenorodd}">
				<c:choose>
					<c:when
						test="${empty configuracao.dpPessoa && empty configuracao.lotacao}">
						<td colspan="2" align="center">--Todos--</td>
					</c:when>
					<c:otherwise>
						<td><c:if test="${not empty configuracao.dpPessoa}">
							<siga:selecionado sigla="${configuracao.dpPessoa.iniciais}"
								descricao="${configuracao.dpPessoa.descricao}" />
						</c:if></td>
						<td><c:if test="${not empty configuracao.lotacao}">
							<siga:selecionado sigla="${configuracao.lotacao.sigla}"
								descricao="${configuracao.lotacao.descricao}" />
						</c:if></td>
					</c:otherwise>
				</c:choose>
				<td><ww:url id="url" action="excluir_publicador"
					namespace="/expediente/configuracao">
					<ww:param name="id">${configuracao.idConfiguracao}</ww:param>
				</ww:url> <ww:a href="%{url}">Excluir</ww:a></td>
			</tr>
			<c:choose>
				<c:when test='${evenorodd == "even"}'>
					<c:set var="evenorodd" value="odd" />
				</c:when>
				<c:otherwise>
					<c:set var="evenorodd" value="even" />
				</c:otherwise>
			</c:choose>
			<c:set var="tamanho" value="${tamanho + 1 }" />
		</c:forEach>
		<tr class="footer">
			<td colspan="8">Total Listado: ${tamanho}</td>
		</tr>
	</table>
</siga:pagina>
