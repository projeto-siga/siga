<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="propriedadeClean"
	value="${fn:replace(param.propriedade,'.','')}" />

<siga:pagina titulo="Buscar Pessoa" popup="true">

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements['offset'].value=offset;
	frm.submit();
}
</script>

<form name="frm" action="${request.contextPath}/app/pessoa/buscar" class="form" method="POST">
	<input type="hidden" name="propriedade" value="${param.propriedade}" />
	<input type="hidden" name="postback" value="1" />
	<input type="hidden" name="offset" value="0" />
	<input type="hidden" name="buscarFechadas" value="${param['buscarFechadas']}" />
	<table class="form" width="100%">
		<tr class="header">
			<td align="center" valign="top" colspan="4">Dados do Usuário</td>
		</tr>
		<tr>
			<td>
				<label>Nome ou Matrícula:</label>
			</td>
			<td>
				<input type="text" name="sigla" value="${sigla}" />
			</td>
		</tr>
		<tr>
			<td>
				<siga:selecao titulo="Lotação" urlAcao="buscar" propriedade="lotacao" modulo="siga"/>
			</td>
		</tr>
		<tr>
			<td>
				<label>Órgão:</label>
			</td>
			<td>
				<select name="idOrgaoUsu" value="${idOrgaoUsu}">
					<option value="${item.idOrgaoUsu}" >[Todos]</option>
					<c:forEach items="${orgaosUsu}" var="item">
						<option value="${item.idOrgaoUsu}" ${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
							${item.nmOrgaoUsu}
						</option>  
					</c:forEach>
				</select>	
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div align="right">
					<input type="submit" value="Pesquisar" />
				</div>
			</td>
		</tr>
	</table>
</form>

<br>

<table class="list" width="100%">
	<tr class="header">
		<td align="center">Matrícula</td>
		<td align="left">Nome</td>
		<td align="center">Lotação</td>
		<td align="center">Função</td>
		<td>Fim de Vigência</td>
	</tr>

	<siga:paginador maxItens="10" maxIndices="${empty maxIndices ? 10 : maxIndices}" totalItens="${tamanho}"
		itens="${pessoas}" var="pessoa">
		<tr class="${evenorodd}">
			<td align="center"><a
				href="javascript: opener.retorna_${propriedadeClean}('${pessoa.id}','${pessoa.sigla}','${pessoa.descricao}','${pessoa.funcaoConfianca}');">${pessoa.sigla}</a></td>
			<td align="left">${pessoa.descricao}</td>
			<td align="center">${pessoa.lotacao.sigla}</td>
			<td align="center">${pessoa.funcaoConfianca.nomeFuncao}${buscarFechadas}</td>
			<td align="left">${pessoa.dataFimPessoa}</td>
		</tr>
	</siga:paginador>
</table>

</siga:pagina>
