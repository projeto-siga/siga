<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="propriedadeClean"
	value="${fn:replace(param.propriedade,'.','')}" />

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

<siga:pagina titulo="Busca de Órgão Integrado" popup="true">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da <fmt:message key="usuario.lotacao"/></h5>
			</div>
			<div class="card-body">
			<form name="frm" action="${request.contextPath}/app/lotacao/buscar" class="form" method="POST">
				<input type="hidden" name="buscarFechadas" value="${param['buscarFechadas']}" /> 
				<input type="hidden" name="propriedade" value="${param.propriedade}" /> 
				<input type="hidden" name="postback" value="1" /> 
				<input type="hidden" name="paramoffset" value="0" />
				<input type="hidden" name="p.offset" value="0" />
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<label for="sigla">Nome ou Sigla</label>
							<input type="text" name="sigla" value="${sigla}" class="form-control" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<label for="idOrgaoUsu">Órgão</label>
							<select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control" >
									<option value="${item.idOrgaoUsu}" >[Todos]</option>
									<c:forEach items="${orgaosUsu}" var="item">
										<option value="${item.idOrgaoUsu}"
											${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
											${item.nmOrgaoUsu}
										</option>
									</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<button type="submit" class="btn btn-primary" >Pesquisar</button> 
						</div>
					</div>
				</div>
			</form>
			</div>
		</div>
		<br />

		<table class="table table-sm table-striped">
			<thead class="thead-dark">
				<tr>
					<th align="center">Sigla</th>
					<th align="left">Nome</th>
					<th>Fim de Vigência</th>
				</tr>
			</thead>
			<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}" itens="${itens}" var="item">
				<tr class="${evenorodd}">
					<td width="10%" align="center"><a
						href="javascript: opener.retorna_${propriedadeClean}('${item.id}','${item.sigla}','${item.descricao}');">${item.sigla}</a></td>
					<td width="70%" align="left">${item.descricao}</td>
					<td align="left" width="20%">${item.dataFimLotacao}</td>
				</tr>
			</siga:paginador>
		</table>
	</div>
</siga:pagina>