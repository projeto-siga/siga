<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<siga:pagina titulo="Usuario Atualiza">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<form name="frmAtualizaUsuario" method="get" action="${linkTo[UsuarioFormController].atualiza}" enctype="multipart/form-data">
		<br>
		<div class="ui-state-default" style="position: absolute; left: 5%; right: 77%;">Matr&iacute;cula:</div>
		<input type="text" name="objUsuario.matricula_usu" style="position: absolute; left: 25%;" 
		<c:if test="${objUsuario!=null}">
			value="${objUsuario.matricula_usu}"
		</c:if>
		readonly="readonly" /> 
		<br>
		<br>
		<div class="ui-state-default" style="position: absolute; left: 5%; right: 77%;">Sesb (Ex.: T2, RJ ou ES):</div>
		<input type="text" name="objUsuario.sesb_pessoa" style="position: absolute; left: 25%;" 
		<c:if test="${objUsuario!=null}">
			value="${objUsuario.sesb_pessoa}"
		</c:if>
		readonly="readonly" />
		<br>
		<br>
		<div class="ui-state-default" style="position: absolute; left: 5%; right: 77%;">Nome:</div>
		<input type="text" name="objUsuario.nome_usu" style="position: absolute; left: 25%;"
		<c:if test="${objUsuario!=null}">
			value="${objUsuario.nome_usu}"
		</c:if>
		readonly="readonly" /> 
		<br>
		<br>
		<div class="ui-state-default" style="position: absolute; left: 5%; right: 77%;">Forum:</div>
		<select class="ui-state-default" style="position: absolute; left: 25%;" name="paramCodForum">
			<option value="${paramCodForum}" selected="selected">${descricaoForum}</option>
			<c:forEach items="${outrosForuns}" var="outros">
				<option value="${outros.cod_forum}">${outros.descricao_forum}</option>
			</c:forEach>
		</select> <br>
		<br> 
		<input class="ui-button" style="position: absolute; left: 5%;" type="submit" value="Atualiza Forum do Usu&aacute;rio" /> <br>
		<br>
		<br>
		<h4 class="ui-widget" style="position: absolute; left: 5%; color: red;">${mensagem}</h4>
		<br>
		<br>
		<a style="position:absolute;left:5%;bottom:3%;" class="ui-state-hover" href="/sigapp/">In&iacute;cio</a>
	</form>
</siga:pagina>