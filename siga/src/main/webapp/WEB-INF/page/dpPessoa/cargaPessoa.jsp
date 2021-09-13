<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Cadastro de Pessoa">
	<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />
	<style type="text/css" media="screen">
		.custom-file-label::after{content: 'Escolher arquivo' !important;}
	</style>

	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Carga de Planilha Pessoa</h5>
			</div>
			<div class="card-body">
			<form action="carga" method="post" enctype="multipart/form-data" class="form">
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<b>Observa&ccedil;&otilde;es:</b>
						</div>
					</div>
				</div>					
				<div class="row">
					<div class="col-sm">
						<ul class="list-group">
							<dt>&#149;&#160;Somente esta liberada a carga de planilhas no formato Excel: "XLSX"</dt>
							<dt>&#149;&#160;O conte&uacute;do dos dados contidos na planilha &eacute; de responsabilidade do usu&aacute;rio;</dt>
							<dt>&#149;&#160;Ap&oacute;s realizar a carga, os dados ser&atilde;o inseridos automaticamente na base de dados;</dt>
							<dt>&#149;&#160;O sistema consistir&aacute; a exist&ecirc;ncia de duplicidade dos dados confrontando a planilha como banco de dados;</dt>
							<dt>&#149;&#160;A planilha deve conter os seguintes campos/formatos: (itens destacados são obrigatórios)</dt>
							<dt>&#149;&#160;O campo nome abreviado só deverá ser preenchido caso o usuário opte por usar seu nome de registro civil com abreviação na assinatura dos documentos. Vale considerar que ainda sim no rodapé do documento será exibido seu nome completo</dt>
							
						</ul>
					</div>
				</div>
				<div class="row mt-3">
					<div class="col-5">
						<div class="form-group">
							<ul class="list-group font-weight-bold">
								<li class="list-group-item list-group-item-success"> Sigla do &Oacute;rgão</li>
								<li class="list-group-item list-group-item-success"> Nome do Cargo</li>
								<li class="list-group-item"> Nome da Fun&ccedil;&atilde;o de Confian&ccedil;a</li>
								<li class="list-group-item list-group-item-success"> Sigla da <fmt:message key="usuario.lotacao"/></li>
								<li class="list-group-item list-group-item-success">Nome: m&aacute;ximo de 60 caracteres</li>
								<li class="list-group-item"> Data de nascimento (8 n&uacute;meros dd/mm/aaaa)</li>
								<li class="list-group-item list-group-item-success"> CPF: m&aacute;ximo de 11 caracteres num&eacute;ricos</li>
								<li class="list-group-item list-group-item-success"> E-mail: m&aacute;ximo de 60 caracteres</li>
								<li class="list-group-item"> RG: Incluindo Dígito</li>
                                <li class="list-group-item"> Órgão Expedidor RG: m&aacute;ximo de 50 caracteres</li>
                                <li class="list-group-item"> Sigla UF RG: m&aacute;ximo de 2 caracteres</li>
                                <li class="list-group-item"> Data de Expedição: (8 n&uacute;meros dd/mm/aaaa)</li>
                                <li class="list-group-item"> Nome Abreviado: m&aacute;ximo de 40 caracteres</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-5">
						<div class="form-group">
							<label>Órgão</label>
							<c:choose>
								<c:when test="${empty nmOrgaousu}">
									<select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control  siga-select2">
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
					<div class="col-sm-5">
						<div class="form-group">
						  <label>Planilha para carga</label>
						  <div  class="custom-file">
						 	<siga:uploadArquivo tamanhoMaximo='2' textoCaixa="Selecione o arquivo contendo a planilha com dados" tiposAceitos=".xlsx"/>
						  </div>
						</div>
					</div>
				</div>								
				
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<button type="submit" onclick="javascript: return validaSelecaoAnexo( this.form );" name="sbmt" class="btn btn-primary" >Ok</button>
							<button type="button" onclick="javascript:location.href='/siga/app/pessoa/editar';" class="btn btn-primary" >Cancelar</button>
						</div>
					</div>
				</div>								
			</form>						
			</div>
		</div>	
	</div>
<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
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
<script>
	$('.custom-file-input').on('change', function() { 
	   let fileName = $(this).val().split('\\').pop(); 
	   $(this).next('.custom-file-label').addClass("selected").html('<i class="far fa-file-excel"></i>  ' + fileName); 
	});
</script>
</siga:pagina>