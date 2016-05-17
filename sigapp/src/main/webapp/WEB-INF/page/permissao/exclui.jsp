<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Permissao Exclui">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" /> 
	
	<title>Permiss&atilde;o Exclui</title>
	
	 <table class="ui-tabs"  align="center" style="font-size:100%;">
	 <tr bgcolor="Silver">
	  <th>&nbsp; Nome &nbsp; </th>
	  <th>&nbsp; Matricula &nbsp; </th>
	  <th>&nbsp; Forum &nbsp;</th>
	  <th>&nbsp; Sesb &nbsp; </th>
	  <th></th>
	 </tr>
	
	<c:forEach items="${listPermitidos}" var="usu">	  	 
		<tr class="ui-button-icon-only"
			<c:if test="${!b}"> bgcolor="#dddddd" </c:if>
		/> 
		<c:set var="b" value="${!b}" /> 
		<td>&nbsp; ${usu.nome_usu}</td>
		<td>&nbsp; ${usu.matricula_usu}</td>
		<td>&nbsp; ${usu.forumFk.descricao_forum}</td>
		<td>&nbsp; ${usu.sesb_pessoa}</td>
		<td>&nbsp;
			<form name="frm_exclui_permissao" method="get" action="${linkTo[PermissaoController].exclui}" enctype="multipart/form-data">
				<img  src="/siga/css/famfamfam/icons/delete.png"/>
				<input type="hidden" name="matricula_proibida" value="${usu.matricula_usu}" /> &nbsp;
				<input type="hidden" name="sesb_proibida" value="${usu.sesb_pessoa}" /> &nbsp;
				<input type="submit" value="Exclui"/>
			</form>
		</td>
	  </tr>
	</c:forEach>
	 </table>
	 <br/><br/>
	 <div style="position:relaive;left:50%;"> <h4 class="ui-widget" style="color:red;"> ${mensagem}</h4></div>
	<a style="position: absolute;left:5%;" class="ui-state-hover" href="${linkTo[PermissaoController].exclui}">Continuar Excluindo</a>
	<a style="position:absolute;left:15%;" class="ui-state-hover" href="/sigapp/">Voltar</a>
</siga:pagina>