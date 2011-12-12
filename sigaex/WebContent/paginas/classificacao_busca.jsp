<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>


<c:if
	test="${empty param['postback'] || param['discriminarVias'] eq true}">
	<c:set var="marcado" value="true" />
</c:if>


<siga:pagina titulo="Buscar Classificação Documental" popup="true">

	<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements["p.offset"].value=offset;
	frm.submit();
}



</script>

	<ww:form name="frm" action="buscar" namespace="/classificacao"
		cssClass="form" theme="simple" method="POST">
		<input type="hidden" name="propriedade" value="${param.propriedade}" />
		<input type="hidden" name="postback" value="1" />
		<input type="hidden" name="p.offset" value="0" />

		<table class="form" width="100%">
			<tr class="header">
				<td align="center" valign="top" colspan="4">Parâmetros de
				Pesquisa de Classificação</td>
			</tr>
			<tr>
				<td width="15%">Palavra-chave:</td>
				<td width="85%" colspan="3"><ww:textfield name="nome" size="50" /></td>
			</tr>
			<tr>
				<td width="15%">Assunto:</td>
				<td width="85%"><ww:select name="assunto"
					list="assuntos" listKey="codAssunto"
					listValue="descrClassificacao" headerKey="-1" headerValue="[Todos]"
					onchange="javascript:sbmt(0);" /></td>
			</tr>
			<%-- <tr>
				<td width="15%">Assunto Principal:</td>
				<td width="85%"><ww:select name="assuntoPrincipal"
					list="assuntosPrincipal" listKey="codAssuntoPrincipal"
					listValue="descrClassificacao" headerKey="-1" headerValue="[Todos]"
					onchange="javascript:sbmt(0);" /></td>
			</tr>
			<tr>
				<td width="15%">Assunto Secundário:</td>
				<td width="85%"><ww:select name="assuntoSecundario"
					list="assuntosSecundario" listKey="codAssuntoSecundario"
					listValue="descrClassificacao" headerKey="-1" headerValue="[Todos]"
					onchange="javascript:sbmt(0);" /></td>
			</tr>--%>
			<tr>
				<td width="15%">Classe:</td>
				<td width="85%"><ww:select name="classe" list="classes"
					listKey="codClasse" listValue="descrClassificacao" headerKey="-1"
					headerValue="[Todos]" onchange="javascript:sbmt(0);" /></td>
			</tr>
			<tr>
				<td width="15%">Subclasse:</td>
				<td width="85%"><ww:select name="subclasse" list="subClasses"
					listKey="codSubclasse" listValue="descrClassificacao"
					headerKey="-1" headerValue="[Todos]" onchange="javascript:sbmt(0);" /></td>
			</tr>
			<%--<tr>
				<td></td>
				<td colspan="3"><ww:submit value="Pesquisar" />&nbsp;<ww:checkbox
					name="ultimoNivel" fieldValue="true" onclick="javascript:sbmt(0);" />Permitir
				Classificação Intermediária</td>
			</tr>--%>
			<tr>
				<td></td>
				<td colspan="3"><ww:submit value="Pesquisar" />&nbsp;<ww:checkbox
					name="discriminarVias" id="check" fieldValue="true"
					onclick="javascript: sbmt();" value="${marcado}" />Discriminar
				vias na listagem</td>
			</tr>
		</table>
	</ww:form>
	<table class="list" width="100%">
		<tr class="header">
			<td width="6%" align="center">Código</td>
			<%-- <td width="1%" align="center">Assunto Principal</td>
			<td width="1%" align="center">Assunto Secundário</td>--%>
			<td width="16%" align="center">Assunto</td>
			<td align="center">Descrição</td>
			<c:if test="${marcado}">
				<td width="2%" align="center">Via</td>
				<td width="10%" align="center">Destino</td>
				<td width="6%" align="center">Arq. Corrente</td>
				<td width="6%" align="center">Arq. Intermediário</td>
				<td width="6%" align="center">Destino Final</td>
				<td width="24%" align="center">Observações</td>
			</c:if>
		</tr>

		<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
			itens="${itens}" var="classificacao">
			<c:set var="numVias" value="${classificacao.numVias}" />
			<tr class="${evenorodd}">
				<c:choose>
					<c:when test="${marcado}">
						<c:set var="rowSpan">rowspan="${numVias}"</c:set>
					</c:when>
					<c:otherwise>
						<c:set var="rowSpan" value="" />
					</c:otherwise>
				</c:choose>
				<td width="6%" align="center"${rowSpan}><a
					href="javascript: opener.retorna_${param.propriedade}('${classificacao.id}','${classificacao.sigla}','${classificacao.descricao}');window.close()">${classificacao.sigla}</a></td>
				<%-- <td width="1%" align="left"${rowSpan}>${f:maiusculasEMinusculas(classificacao.descrAssuntoPrincipal)}</td>
				<td width="1%" align="left"${rowSpan}>${f:maiusculasEMinusculas(classificacao.descrAssuntoSecundario)}</td>--%>
				<td width="16%" align="left"${rowSpan}>${f:maiusculasEMinusculas(classificacao.descrAssunto)}</td>
				<td align="left"${rowSpan}>${classificacao.descrClassificacao}</td>
				<c:choose>
					<c:when test="${marcado}">
						<c:forEach var="via" items="${classificacao.exViaSet}"
							varStatus="status">
							<c:if test="${status.index > 0}">
			</tr>
			<tr class="${evenorodd}">
				</c:if>
				<td width="2%" align="center">${via.letraVia}</td>
				<td width="10%">${via.exTipoDestinacao.descrTipoDestinacao}</td>
				<td width="6%">${via.temporalidadeCorrente.descTemporalidade}</td>
				<td width="6%">${via.temporalidadeIntermediario.descTemporalidade}</td>
				<td width="6%">${via.exDestinacaoFinal.descrTipoDestinacao}</td>
				<td width="24%">${via.obs}</td>
			</tr>
			</c:forEach>
			</c:when>
			<c:otherwise>
				</tr>
			</c:otherwise>
			</c:choose>
		</siga:paginador>
	</table>
</siga:pagina>
