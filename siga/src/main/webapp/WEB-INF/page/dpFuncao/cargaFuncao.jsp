<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de Lota&ccedil;&atilde;o">

	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Carga de Planilha Fun&ccedil;&atilde;o de Confian&ccedil;a</h5></div>
			<div class="card-body">
				<form action="carga" method="post" enctype="multipart/form-data" class="form">								
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label><b>Observa&ccedil;&otilde;es:</b></label>
								<nav id="navbar-exemplo2" class="navbar navbar-light bg-light">
									<ul class="nav nav-pills">
								    	<li class="nav-item"><b>&#149;&#160;Somente esta liberada a carga de planilhas no formato Excel: "XLSX";</b></li>
								    </ul>
								    <ul class="nav nav-pills">
								    	<li class="nav-item"><b>&#149;&#160;O conte&uacute;do dos dados contidos na planilha &eacute; de responsabilidade do usu&aacute;rio;</b></li>
								    </ul>
								    <ul class="nav nav-pills">
								    	<li class="nav-item"><b>&#149;&#160;Ap&oacute;s realizar a carga, os dados ser&atilde;o inseridos automaticamente na base de dados;</b></li>
								    </ul>
								    <ul class="nav nav-pills">
								    	<li class="nav-item"><b>&#149;&#160;O sistema consistir&aacute; a exist&ecirc;ncia de duplicidade dos dados confrontando a planilha como banco de dados;</b></li>
								    </ul>
								    <ul class="nav nav-pills">
								    	<li class="nav-item"><b>&#149;&#160;A planilha deve conter os seguintes campos/formatos:</b></li>
								    </ul>
								    <nav id="navbar-exemplo2" class="navbar navbar-light bg-light">
								    <ul class="nav nav-pills">
								    	<li class="nav-item">- Nome: m&aacute;ximo de 100 caracteres alfanum&eacute;ricos e ponto (letras, n&uacute;meros e ".")</li>
								    </ul>
								    </nav>
								</nav>
							</div>
						</div>
					</div>	
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">		
								<label>&Oacute;rg&atilde;o:</label>
								<c:choose>
									<c:when test="${empty nmOrgaousu}">
										<select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control">
											<c:forEach items="${orgaosUsu}" var="item">
												<option value="${item.idOrgaoUsu}"
													${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
													${item.nmOrgaoUsu}</option>
											</c:forEach>
										</select>
									</c:when>
									<c:otherwise>
										${nmOrgaousu}
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Selecione o arquivo contendo a planilha com dados</label>
								<div  class="custom-file">
									<siga:uploadArquivo tamanhoMaximo='2' textoCaixa="Selecione o arquivo contendo a planilha com dados"  tiposAceitos=".xlsx"/>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
					
								<input type="submit" value="Ok"	class="btn btn-primary" onclick="javascript: return validaSelecaoAnexo( this.form );" name="sbmt"/>
								<input type="button" value="Cancelar" class="btn btn-primary" onclick="javascript:location.href='/siga/app/funcao/editar';"/>
							</div>
						</div>
					</div>
				</form>										
			</div>
		</div>		
	</div>
	<script>
		/**
		 * Valida se o anexo foi selecionado ao clicar em OK
		 */
		function validaSelecaoAnexo(form) {
			var result = true;
			var arquivo = form.arquivo;
			if (arquivo == null || arquivo.value == '') {
				sigaModal.alerta('Selecione um arquivo');
				result = false;
			}
			return result;
		}		
	</script>
</siga:pagina>