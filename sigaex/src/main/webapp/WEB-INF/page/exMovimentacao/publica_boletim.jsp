<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>--%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Movimentação">

<script language="Javascript1.1" type="text/javascript">
function confirma(){
 return confirm ('Essa movimentação não poderá ser desfeita. Prosseguir?');
}
</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Registro de Publica&ccedil;&atilde;o do Boletim Interno -
				${doc.codigo}
			</h2>

			<div class="gt-content-box gt-for-table">
				<form action="boletim_publicar_gravar" namespace="/expediente/mov" cssClass="form" method="get">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
	
					<table class="gt-form-table">
						<colgroup>
						<col style="width:30%;"/>
						<col style="width:70%;"/>
						</colgroup>
						<tr class="header">
							<td colspan="2">Dados da Publica&ccedil;&atilde;o</td>
						</tr>
						<tr class="tdLabel">
							<td>
								<label for="boletim_publicar_gravar_dtPubl" class="label">Data da Publicação:</label>
							</td>
							<td>
								<input type="text" name="dtPubl" onblur="javascript:verifica_data(this,0);" label="Data da Publicação" value="${dtPubl}"/>
							</td>
						</tr>
						
						<tr class="button">
							<td colspan="2">
								<input type="submit" value="Ok" onclick="javascript: return confirma();" class="gt-btn-medium gt-btn-left"/> 
								<input type="button" value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>			
</siga:pagina>