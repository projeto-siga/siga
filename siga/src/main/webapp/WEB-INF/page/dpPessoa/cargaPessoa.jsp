<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Cadastro de Pessoa">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Carga de Planilha Pessoa</h5>
			</div>
			<div class="card-body">
			<form action="carga" method="POST" enctype="multipart/form-data" class="form">
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<b>Observa&ccedil;&otilde;es:</b>
						</div>
					</div>
				</div>
				<c:if test="${not empty msg}"><script type="text/javascript">mensagemAlerta('Arquivo processado com sucesso!');</script></c:if>
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<dl>
								<dt>&#149;&#160;Somente esta liberada a carga de planilhas no formato Excel: "XLSX"</dt>
								<dt>&#149;&#160;O conte&uacute;do dos dados contidos na planilha &eacute; de responsabilidade do usu&aacute;rio;</dt>
								<dt>&#149;&#160;Ap&oacute;s realizar a carga, os dados ser&atilde;o inseridos automaticamente na base de dados;</dt>
								<dt>&#149;&#160;O sistema consistir&aacute; a exist&ecirc;ncia de duplicidade dos dados confrontando a planilha como banco de dados;</dt>
								<dt>&#149;&#160;A planilha deve conter os seguintes campos/formatos:</dt>
								<dd>- Sigla do &Oacute;rgão;</dd>
								<dd>- Nome do Cargo;</dd>
								<dd>- Nome da Fun&ccedil;&atilde;o de Confian&ccedil;a;</dd>
								<dd>- Nome da <fmt:message key="usuario.lotacao"/>;</dd>
								<dd>- Nome: m&aacute;ximo de 60 caracteres;</dd>
								<dd>- Data de nascimento (8 n&uacute;meros dd/mm/aaaa);</dd>
								<dd>- CPF: m&aacute;ximo de 11 caracteres num&eacute;ricos;</dd>
								<dd>- E-mail: m&aacute;ximo de 60 caracteres</dd>
							</dl>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<label>&Oacute;rg&atilde;o</label>
							<c:choose>
								<c:when test="${empty nmOrgaousu}">
									<select name="idOrgaoUsu" value="${idOrgaoUsu}">
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
					<div class="col-sm-3">
						<div class="form-group">
							<b>Selecione o arquivo contendo a planilha com dados:&nbsp;&nbsp;&nbsp;</b>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<input type="file" name="arquivo" accept=".xlsx" class="form-control-file"/>				
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
			<!-- Modal -->
			<div class="modal fade" id="alertaModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog" role="document">
			    	<div class="modal-content">
			      		<div class="modal-header">
					        <h5 class="modal-title" id="alertaModalLabel">Alerta</h5>
					        <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
					          <span aria-hidden="true">&times;</span>
					    	</button>
					    </div>
				      	<div class="modal-body">
				        	<p class="mensagem-Modal"></p>
				      	</div>
						<div class="modal-footer">
						  <button type="button" class="btn btn-primary" data-dismiss="modal">Fechar</button>
						</div>
			    	</div>
			  	</div>
			</div>				
			<!--Fim Modal -->
			
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
		result = false;
	}
	return result;
}

function mensagemAlerta(mensagem) {
	$('#alertaModal').find('.mensagem-Modal').text(mensagem);
	$('#alertaModal').modal();
}
	
</script>
</siga:pagina>