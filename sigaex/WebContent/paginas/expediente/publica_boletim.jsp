<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://fckeditor.net/tags-fckeditor" prefix="FCK"%>
<%--<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>--%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Movimentação">

<script language="Javascript1.1" type="text/javascript">
function confirma(){
 return confirm ('Essa movimentação não poderá ser desfeita. Prosseguir?');
}
</script>
	<table width="100%">
		<tr>
			<td><ww:form action="boletim_publicar_gravar"
				namespace="/expediente/mov" cssClass="form" method="GET">
				<input type="hidden" name="postback" value="1" />
				<ww:hidden name="sigla" value="${sigla}"/>

				<h1>Registro de Publica&ccedil;&atilde;o do Boletim Interno -
				${doc.codigo} <!--<c:if
				test="${numVia != null && numVia != 0}">
			- ${numVia}&ordf; Via
			</c:if>--></h1>
				<table class="form">
					<tr class="header">
						<td colspan="2">Dados da Publica&ccedil;&atilde;o</td>
					</tr>
					<ww:textfield name="dtPubl"
						onblur="javascript:verifica_data(this,0);"
						label="Data da Publicação" />
					<tr class="button">
						<td></td>
						<td><input type="submit" value="Ok" onclick="javascript: return confirma();"/> <input type="button"
							value="Cancela" onclick="javascript:history.back();" />
					</tr>
				</table>
			</ww:form></td>
		</tr>
	</table>
</siga:pagina>