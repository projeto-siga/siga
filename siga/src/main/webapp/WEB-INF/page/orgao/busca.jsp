<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="propriedadeClean"
	value="${fn:replace(param.propriedade,'.','')}" />

<siga:pagina titulo="Busca de Órgão Externo" popup="true">

	<script type="text/javascript" language="Javascript1.1">
		function sbmt(offset) {
			if (offset == null) {
				offset = 0;
			}
			frm.elements['offset'].value = offset;
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

	<div class="container-fluid">
		<form name="frm" action="${request.contextPath}/app/orgao/buscar"
			cssClass="form" method="POST">
			<input type="hidden" name="propriedade" value="${param.propriedade}" />
			<input type="hidden" name="postback" value="1" /> <input
				type="hidden" name="offset" value="0" />

			<div class="card bg-light mb-3">
				<div class="card-header">
					<h5>Dados do Órgão Externo</h5>
				</div>
				<div class="card-body">
					<div class="row">
						<div class="col-sm-4">
							<label>Nome ou Sigla: </label>
						</div>
						<div class="col-sm-5">
							<input type="text" name="sigla" value="${sigla}"
								class="form-control" />
						</div>
						<div class="col-sm-2">
							<input type="submit" value="Pesquisar" class="btn btn-primary" />
						</div>
					</div>

				</div>
			</div>
		</form>

		<br>


		<table border="0" class="table table-sm table-striped">
			<thead class="thead-dark">
				<th align="center">Sigla</th>
				<th align="left">Nome</th>
			</thead>
			<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}"
				itens="${itens}" var="item">
				<tr class="${evenorodd}">
					<td width="10%" align="center"><a
						href="javascript: ${parteFuncao}.retorna_${propriedadeClean}('${item.id}','${item.sigla}','${item.descricao}');">${item.sigla}</a></td>
					<td width="90%" align="left">${item.descricao}</td>
				</tr>
			</siga:paginador>
		</table>
	</div>
</siga:pagina>