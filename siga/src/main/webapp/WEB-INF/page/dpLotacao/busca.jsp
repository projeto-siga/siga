<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="propriedadeClean"
	value="${fn:replace(param.propriedade,'.','')}" />

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


<siga:pagina titulo="Busca de Órgão Integrado" popup="true">
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />

	<script type="text/javascript" language="Javascript1.1">
		
		function limpar() {
			$("#alertFiltros").hide(); 
			$('#sigla').val("");
			$('#idOrgaoUsu').val(${titular.orgaoUsuario.idOrgaoUsu}).change();
			$('#containerResult').remove();
			$('#alertNaoEncontrado').remove();
			$('#sigla').focus();		
		}
		
		function valida() {
			if (($('#sigla').val().trim() !== "" )){
				return true;
			} else {
				return false;
			}
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

			$("#alertFiltros").hide();
			if ($('#sigla').val().trim() === "" ) {
				$('#sigla').focus();
			}
				
		});
	</script>

	<!-- main content -->
	<div class="container-fluid">
		<div id="alertFiltros" class="alert alert-warning" role="alert" style="display: none;">
		  Obrigatório informar o <strong>Nome ou Sigla</strong> para realizar a pesquisa.
		</div>
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da <fmt:message key="usuario.lotacao"/></h5>
			</div>
			<div class="card-body">
				<form name="frm" id="frm" action="${request.contextPath}/app/lotacao/buscar" class="form" method="POST">
					<input type="hidden" name="buscarFechadas" value="${param['buscarFechadas']}" /> 
					<input type="hidden" name="buscarTodosOrgaos" value="true" /> 
					<input type="hidden" name="propriedade" value="${param.propriedade}" /> 
					<input type="hidden" name="postback" value="1" /> 
					<input type="hidden" name="paramoffset" value="0" />
					<input type="hidden" name="p.offset" value="0" />
					<input type="hidden" name="modal" value="${param['modal']}" />
					<input type="hidden" name="campoOrgaoDesabilitado" value="${param['campoOrgaoDesabilitado']}" />			
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label for="sigla">Nome ou Sigla</label>
								<input type="text" name="sigla" id="sigla" value="${sigla}" class="form-control" />
								<small class="text-muted">Obrigatório que seja informado </small>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm">
							<div class="form-group">
								<label for="idOrgaoUsu">Órgão</label>
								<select name="idOrgaoUsu" id="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control  siga-select2" >
										<option value="${item.idOrgaoUsu}">[Todos]</option>
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
					<div class="row pt-1">
						<div class="col-12 text-center">
							<div class="form-group ">
								<button type="submit" class="btn btn-primary" id="idSubmit">Pesquisar</button> 
								<button type="button" class="btn btn-secondary" onclick="limpar();" >Limpar</button> 
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<br />
		<c:if test="${tamanho gt 0}">
			<div id="containerResult">
				<table class="table table-sm table-striped">
					<thead class="${thead_color}">
						<tr>
							<th align="center">Sigla</th>
							<th align="left">Nome</th>
							<th>Fim de Vigência</th>
						</tr>
					</thead>
					<siga:paginador maxItens="10" maxIndices="10" totalItens="${tamanho}" itens="${itens}" var="item">
						<tr class="${evenorodd}">
							<td width="20%" align="left"><a
								href="javascript: ${parteFuncao}.retorna_${propriedadeClean}('${item.id}','${item.siglaCompletaFormatada}','${fn:replace(item.descricao,'\'','&#8217;')}');">${item.siglaCompletaFormatada}</a></td>
							<td width="60%" align="left">${item.descricao}</td>
							<td align="left" width="20%">${item.dataFimLotacao}</td>
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
	</div>
	
<script type="text/javascript" language="Javascript1.1">
	
	if (${param.campoOrgaoDesabilitado == true}) {
		$('#idOrgaoUsu').prop('disabled', true);
	
		$('#frm').on('submit', function() {
		    $('#idOrgaoUsu').prop('disabled', false);
		});
	}
</script>
<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>	
<script type="text/javascript" src="/siga/javascript/select2/select2-dropdownPosition.js"></script>
</siga:pagina>