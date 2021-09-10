<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="propriedadeClean"
	value="${fn:replace(param.propriedade,'.','')}" />

<siga:pagina titulo="Buscar Função" popup="true">
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />

<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset == null) {
		offset = 0;
	}
	frm.elements["paramoffset"].value = offset;
	frm.elements["p.offset"].value = offset;
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

<form name="frm" action="${request.contextPath}/app/funcao/buscar"  cssClass="form">
	<input type="hidden" name="propriedade" value="${param.propriedade}" />
	<input type="hidden" name="postback" value="1" />
	<input type="hidden" name="paramoffset" value="0" />
	<input type="hidden" name="p.offset" value="0" />
	<input type="hidden" name="modal" value="${param['modal']}" />	

	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
			<h5>Dados da Função de Confiança</h5>
		</div>
		<div class="card-body">
			<div class="row">
				<div class="col-sm">
					<div class="form-group">
						<label>Nome</label> 
						<input type="text" value="${nome}" name="nome" class="form-control"/>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm">
					<div class="form-group">
						<label>Órgão</label>
						<select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control  siga-select2">
							<c:forEach items="${orgaosUsu}" var="item">
								<option value="${item.idOrgaoUsu}" ${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
									${item.nmOrgaoUsu}
								</option>  
							</c:forEach>
						</select>	
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm">
					<input type="submit" value="Pesquisar" class="btn btn-primary"/>
				</div>
			</div>
		</div>
	</div>
</form>

<br>

<table  class="table table-sm table-striped">
	<thead class="${thead_color}">
		<tr class="header">
			<th align="center">Sigla</th>
			<th align="left">Nome</th>
		</tr>
	</thead>
	<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
		itens="${itens}" var="item">
		<tr class="${evenorodd}">
			<td width="10%" align="center"><a
				href="javascript: ${parteFuncao}.retorna_${propriedadeClean}('${item.id}','${item.sigla}','${item.descricao}');">${item.sigla}</a></td>
			<td width="90%" align="left"><a
				href="javascript: ${parteFuncao}.retorna_${propriedadeClean}('${item.id}','${item.sigla}','${item.descricao}');">${item.descricao}</a></td>
		</tr>
	</siga:paginador>
</table>

<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
</siga:pagina>
		