<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de Lota&ccedil;&atilde;o">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Cadastro de Lota&ccedil;&atilde;o</h2>
			<div class="gt-content-box gt-for-table">
				<form action="carga" method="POST" enctype="multipart/form-data" class="form">
					<input type="hidden" name="postback" value="1" /> <input
						type="hidden" name="sigla" value="${sigla}" />

					<table class="gt-form-table">
						<c:if test="${not empty msg}"><script type="text/javascript">alert('Arquivo processado com sucesso!');</script></c:if>			
						<tr class="header">
							<td colspan="2">Carga de Planilha</td>
						</tr>
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
								  <dd>- Nome: m&aacute;ximo de 100 caracteres alfanum&eacute;ricos (letras e n&uacute;meros)</dd>
								  <dd>- Sigla: m&aacute;ximo de 20 caracteres alfanum&eacute;ricos (letras e n&uacute;meros)</dd>
								  <dd>- Localidade: m&aacute;ximo de 256 caracteres alfanum&eacute;ricos. Somente nome de Munic&iacute;pios existentes no Estado.</dd>
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
								<input type="button" value="Cancela" onclick="javascript:location.href='/siga/app/lotacao/editar';" class="gt-btn-medium gt-btn-left" />
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