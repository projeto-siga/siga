<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="propriedadeClean"
	value="${fn:replace(param.propriedade,'.','')}" />

<siga:pagina titulo="Busca de Grupo" popup="true">

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset==null) {
		offset=0;
	}
	frm.elements['offset'].value=offset;
	frm.submit();
}
</script>

<c:choose>
	<c:when test="${param.modal != true}">
	    <!-- parteFuncao para fechar window -->
	    <c:set var="parteFuncao" value="opener" />
	</c:when>
	<c:otherwise>
	    <!-- parteFuncao para fechar modal -->
	    <c:set var="parteFuncao" value="parent" />
	</c:otherwise>	
</c:choose>	

<form name="frm" action="buscar" class="form" method="POST">
	<table class="form" width="100%">
		<input type="hidden" name="buscarFechadas" value="${param['buscarFechadas']}" />
		<input type="hidden" name="propriedade" value="${param.propriedade}" />
		<input type="hidden" name="postback" value="1" />
		<input type="hidden" name="offset" value="0" />
		<input type="hidden" name="modal" value="${param['modal']}" />		
		<tr class="header">
			<td align="center" valign="top" colspan="4">Dados do Grupo</td>
		</tr>
		<tr>
			<td class="tdLabel">
				<label class="label" for="buscar_nome">Nome ou Sigla</label>
			</td>
			<td>
				<input type="text" name="nome" id="buscar_nome" value="${nome}" />
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

<table class="table table-sm table-striped">
		<thead class="${thead_color}">
					<tr>	
		<td align="center">Sigla</td>
		<td align="left">Nome</td>
		<td>Fim de Vigência</td>
	</tr>
	</thead>
	<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
		itens="${itens}" var="item">
		<tr class="${evenorodd}">
			<td width="10%" align="center">
				<a href="javascript: ${parteFuncao}.retorna_${propriedadeClean}('${item.id}','${item.sigla}','${item.descricao}');">${item.sigla}</a>
			</td>
			<td width="70%" align="left">${item.descricao}</td>
			<td align="left" width="20%">${item.hisDtFim}</td>
		</tr>
	</siga:paginador>
</table>

</siga:pagina>