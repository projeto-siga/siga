<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de Pessoa">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Cadastro de Pessoa</h2>
			<div class="gt-content-box gt-for-table">
				<form action="carga" method="POST" enctype="multipart/form-data" class="form">
					
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Carga de Planilha</td>
						</tr>
						
						<c:if test="${not empty msg}"><script type="text/javascript">alert('Arquivo processado com sucesso!');</script></c:if>
						<tr><td colspan="2"></td></tr>
						<tr>
							<td  colspan="2"><b>Observa&ccedil;&otilde;es:</b></td>
						</tr>
						<tr>
							<td  colspan="2">
								<dl>
								  <dt>&#149;&#160;Somente esta liberada a carga de planilhas no formato Excel: "XLSX"</dt>
								  <dt>&#149;&#160;O conte&uacute;do dos dados contidos na planilha &eacute; de responsabilidade do usu&aacute;rio;</dt>
								  <dt>&#149;&#160;Ap&oacute;s realizar a carga, os dados ser&atilde;o inseridos automaticamente na base de dados;</dt>
								  <dt>&#149;&#160;O sistema consistir&aacute; a exist&ecirc;ncia de duplicidade dos dados confrontando a planilha como banco de dados;</dt>
								  <dt>&#149;&#160;A planilha deve conter os seguintes campos/formatos:</dt>
								  <dd>- Sigla do &Oacute;rgão;</dd>
								  <dd>- Nome do Cargo;</dd>
								  <dd>- Nome da Fun&ccedil;&atilde;o de Confian&ccedil;a;</dd>
								  <dd>- Nome da Lota&ccedil;&atilde;o;</dd>
								  <dd>- Nome: m&aacute;ximo de 60 caracteres;</dd>
								  <dd>- Data de nascimento (8 n&uacute;meros dd/mm/aaaa);</dd>
								  <dd>- CPF: m&aacute;ximo de 11 caracteres num&eacute;ricos;</dd>
								  <dd>- E-mail: m&aacute;ximo de 60 caracteres</dd>
								</dl>
							</td>
						</tr>
						<tr>
							<td style="width:100px;"><label>&Oacute;rg&atilde;o:</label></td>
							<td>
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
							</td>
						</tr>
						<tr>
							<td colspan="2"><label>Selecione o arquivo contendo a planilha com dados:&nbsp;&nbsp;&nbsp;</label><input type="file" name="arquivo" accept=".xlsx" /></td>
							
						</tr>
						<tr>
							<td colspan="2">
								<input type="submit" value="Ok"	class="gt-btn-medium gt-btn-left" onclick="javascript: return validaSelecaoAnexo( this.form );" name="sbmt"/>
								<input type="button" value="Cancela" onclick="javascript:location.href='/siga/app/pessoa/editar';" class="gt-btn-medium gt-btn-left" />
							</td>
						</tr>
					</table>
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
				result = false;
			}
			return result;
		}
	</script>
</siga:pagina>