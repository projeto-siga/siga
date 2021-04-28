<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<script>
	function sbmt(id, action) {
		var frm = document.getElementById(id);
		frm.action = action;
		frm.submit();
		return;
	}

	
	function enviar(idServico, idSituacao) {
		if (!IsRunningAjaxRequest()) {
			ReplaceInnerHTMLFromAjaxResponse(
					'gravar?idServico='+idServico+'&idSituacao='+idSituacao
						+ '&servicoPai=${servicoPai}'
						+ '<c:if test="${idAbrangencia == 4}">&perfilSel.id=${perfilSel.id}</c:if>'
						+ '<c:if test="${idAbrangencia == 3}">&pessoaSel.id=${pessoaSel.id}</c:if>'
						+ '<c:if test="${idAbrangencia == 2}">&lotacaoSel.id=${lotacaoSel.id}</c:if>'
						+ '<c:if test="${idAbrangencia == 1}">&idOrgaoUsuSel=${idOrgaoUsuSel}</c:if>',
					null, document.getElementById('SPAN-' + idServico));
		}
	}
	
</script>

<siga:pagina titulo="Atribuição de Permissões">
	<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />
	
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Selecione a abrangência</h5>
			</div>
			<div class="card-body">	
			<form id="listar" name="listar" action="listar" method="get" class="form100">
				<input type="hidden" name="servicoPai" value="${servicoPai}" />
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<label for="idAbrangencia">Abrangência</label>
							<siga:escolha id='idAbrangencia' var='idAbrangencia' classSelect="form-control" singleLine="${true}">
								<siga:opcao id='4' texto="Perfil">
									<siga:selecao tema='simple' titulo="Perfil:" propriedade="perfil" modulo="siga"/>
								</siga:opcao>
								
								<siga:opcao id='1' texto="Órgão usuário">											
									<select name="idOrgaoUsuSel" class="form-control  siga-select2">
										<c:forEach items="${orgaosUsu}" var="item">
											<option value="${item.idOrgaoUsu}" ${item.idOrgaoUsu == idOrgaoUsuSel ? 'selected' : ''}>
												${item.nmOrgaoUsu}
											</option>  
										</c:forEach>
									</select>		
								</siga:opcao>
								<siga:opcao id='2' texto="Lotação">
									<siga:selecao tema='simple' titulo="Lotação:" propriedade="lotacao" modulo="siga" />
								</siga:opcao>
								
								<siga:opcao id='3' texto="Matrícula">
									<siga:selecao tema='simple' titulo="Matrícula:" propriedade="pessoa" modulo="siga" />
								</siga:opcao>
							</siga:escolha>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<button type="submit" class="btn btn-primary">Pesquisar</button>
					</div>
				</div>
			</form>
			</div>			
		</div>	
	

		<c:if test="${not empty itensHTML}">
			<h3 class="gt-table-head">Permissões</h3>
			<table class="table table-sm table-striped">
				<colgroup>
					<col width="15%" />
				</colgroup>
				<c:if test="${idAbrangencia == 1}">
					<thead class="${thead_color}">
						<tr>
							<th>Órgão usuário:</th>
							<th>${nomeOrgaoUsuSel}</th>
						</tr>
					</thead>
				</c:if>
				<c:if test="${idAbrangencia == 2}">
					<thead class="${thead_color}">
						<tr>
							<td>Lotação:</td>
							<td>${lotacaoSel.descricao}</td>
						</tr>
						<tr >
							<td>Sigla:</td>
							<td>${lotacaoSel.sigla}</td>
						</tr>
					</thead>
				</c:if>
				<c:if test="${idAbrangencia == 3}">
					<thead class="${thead_color}">
						<tr>
							<td>Pessoa:</td>
							<td>${pessoaSel.descricao}</td>
						</tr>
					</thead>
	
					<tr>
						<td>Matrícula:</td>
						<td>${pessoaSel.sigla}</td>
					</tr>
				</c:if>
				<c:if test="${idAbrangencia == 4}">
					<tr>
						<td>Perfil:</td>
						<td>${perfilSel.descricao}</td>
					</tr>
					<tr>
						<td>Sigla:</td>
						<td>${perfilSel.sigla}</td>
					</tr>
				</c:if>
				<tr>
					<td colspan="2">${itensHTML}</td>
				</tr>
			</table>
		</c:if>
	</div>
	<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>	
</siga:pagina>
