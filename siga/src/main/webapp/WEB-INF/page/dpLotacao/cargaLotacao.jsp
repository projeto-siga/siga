<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de Lota&ccedil;&atilde;o">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Carga de Planilha Unidade</h5>
			</div>
			<div class="card-body">
			<form action="carga" method="post" enctype="multipart/form-data" class="form">
				<input type="hidden" name="postback" value="1" /> 
				<input type="hidden" name="sigla" value="${sigla}" />				
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<b>Observa&ccedil;&otilde;es:</b>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<dl>
								<dt>&#149;&#160;Somente esta liberada a carga de planilhas no formato Excel: "XLSX"</dt>
								<dt>&#149;&#160;O conte&uacute;do dos dados contidos na planilha &eacute; de responsabilidade do usu&aacute;rio;</dt>
								<dt>&#149;&#160;Ap&oacute;s realizar a carga, os dados ser&atilde;o inseridos automaticamente na base de dados;</dt>
								<dt>&#149;&#160;O sistema consistir&aacute; a exist&ecirc;ncia de duplicidade dos dados confrontando a planilha como banco de dados;</dt>
								<dt>&#149;&#160;A planilha deve conter os seguintes campos/formatos:</dt>
								<dd>- Nome: m&aacute;ximo de 100 caracteres alfanum&eacute;ricos, hífen , vírgula e ponto (letras, n&uacute;meros, "-", "," e ".")</dd>
								<dd>- Sigla: m&aacute;ximo de 20 caracteres alfanum&eacute;ricos, barra e hífen (letras, n&uacute;meros, "/" e "-")</dd>
								<dd>- UF: sigla do Estado, por exemplo: SP.</dd>
								<dd>- Localidade: m&aacute;ximo de 256 caracteres alfanum&eacute;ricos (Nome do município).</dd>
								<dd>- Sigla Lotação PAI: m&aacute;ximo de 20 caracteres alfanum&eacute;ricos, barra e hífen (letras, n&uacute;meros, "/" e "-")</dd>
								<dd>  (A Sigla da Lotaç&atilde;o pai define o nível de hierarquia, indicando a qual unidade tal cadastro ir&aacute; pertencer)</dd>
								<dd>- Unidade Externa: SIM ou N&Atilde;O (caso a coluna n&atilde;o seja informada ser&aacute; considerado como N&Atilde;O)</dd>																
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
					<div class="col-sm">
						<div class="form-group">
							<button type="submit" onclick="javascript: return validaSelecaoAnexo( this.form );" name="sbmt" class="btn btn-primary" >Ok</button>
							<button type="button" onclick="javascript:location.href='/siga/app/lotacao/editar';" class="btn btn-primary" >Cancelar</button>
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