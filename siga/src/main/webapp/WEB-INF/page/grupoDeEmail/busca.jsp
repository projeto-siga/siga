<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="propriedadeClean"
	value="${fn:replace(param.propriedade,'.','')}" />

<siga:pagina titulo="Buscar Grupo" popup="true">
	<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />

	<script type="text/javascript" language="Javascript1.1">
		
		function limpar() {
			$("#alertFiltros").hide(); 
			$('#nome').val("");
			$('#idOrgaoUsu').val(${titular.orgaoUsuario.idOrgaoUsu}).change();
			$('#containerResult').remove();
			$('#alertNaoEncontrado').remove();
			$('#nome').focus();		
		}
		
		function valida() {
		    return true;
		}
		
		function invalid(event) {
			$("#alertFiltros").show();
			$('#containerResult').remove();
			$('#alertNaoEncontrado').remove();
		}
		
		function submit(event) {
	
			if (!valida()) {
			  invalid(event);
			  event.preventDefault();
			}
		}
		
		function sbmt(offset) {
			if (offset == null) {
				offset = 0;
			}
			frm.elements["paramoffset"].value = offset;
			frm.elements["p.offset"].value = offset;
			frm.submit();
		}
		
		$( document ).ready(function() {
			$("#alertFiltros").hide();
			frm.onsubmit = submit;

			if ($('#nome').val().trim() === "" ) {
				$('#nome').focus();
			}
				
		});
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
		<input type="hidden" name="buscarFechadas" value="${param['buscarFechadas']}" />
		<input type="hidden" name="propriedade" value="${param.propriedade}" />
		<input type="hidden" name="postback" value="1" />
		<input type="hidden" name="paramoffset" value="0" />
		<input type="hidden" name="p.offset" value="0" />
		<input type="hidden" name="modal" value="${param['modal']}" />

	<div class="container-fluid">
		<div id="alertFiltros" class="alert alert-warning" role="alert" style="display: none;">
		  Obrigatório informar o <strong>Nome</strong> para realizar a pesquisa.
		</div>
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados do Grupo</h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<label>Nome</label></td>
							<input type="text" name="nome" id="nome" value="${nome}" class="form-control" />
							<small class="text-muted">Obrigatório que seja informado </small>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<label>Órgão</label>
							<select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control  siga-select2">
									<c:forEach items="${orgaosUsu}" var="item">
										<option value="${item.idOrgaoUsu}"
											${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
											${item.nmOrgaoUsu}</option>
									</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="row pt-1">
					<div class="col-12 text-center">
						<div class="form-group ">
							<button type="submit" class="btn btn-primary" id="idSubmit">Pesquisar</button> 
							<button type="button" class="btn btn-secondary" onclick="limpar();" >Limpar</button> 
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
<br>
<c:if test="${tamanho gt 0}">
	<div id="containerResult">
		<table  class="table table-sm table-striped">
			<thead class="${thead_color}">
				<th align="center">Sigla</th>
				<th align="left">Nome</th>
			</thead>
			<siga:paginador maxItens="10" maxIndices="40" totalItens="${tamanho}"
				itens="${itens}" var="item">
				<tr class="${evenorodd}">
					<td width="10%" align="center"><a
						href="javascript: ${parteFuncao}.retorna_${propriedadeClean}('${item.id}','${item.sigla}','${item.descricao}');">${item.sigla}</a></td>
					<td width="90%" align="left"><a
						href="javascript: ${parteFuncao}.retorna_${propriedadeClean}('${item.id}','${item.sigla}','${item.descricao}');">${item.descricao}</a></td>
				</tr>
			</siga:paginador>
		</table>
	</div>
</c:if>
<c:if test="${tamanho eq 0}">
	<div id="alertNaoEncontrado" class="alert alert-danger text-center leas" role="alert">
		<h5>Não foi possível encontrar resultados</h5>
		<p>
			Verifique as informações fornecidas
		</p>
	</div>
</c:if>

<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/select2-dropdownPosition.js"></script>
</siga:pagina>
