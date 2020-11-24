<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../popupHeader.jsp"></jsp:include>
<jsp:include page="../main.jsp"></jsp:include>


<head><title>Pesquisa de Atributos</title></head>

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
		<div class="gt-content-box gt-for-table">
			<form action="${linkTo[AtributoController].buscar}" enctype="multipart/form-data">
				<table class="gt-form-table">
					<tr class="header">
						<td align="center" valign="top" colspan="4">Dados do atributo</td>
					</tr>
					<tr>
						<td style="width: 5%;">Nome:</td>
						<td><input type="text" name="nomeAtributo" value="${filtro.nomeAtributo}" /></td>
					</tr>
					<tr>
						<input type="hidden" name="propriedade" value="${propriedade}" />
						<input type="hidden" name="popup" value="true" />
						<td><input type="submit" class="gt-btn-medium gt-btn-left" value="Pesquisar" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<br />
	
	<div class="gt-content-box gt-for-table">
		<table class="gt-table">
			<tr>
				<th>Nome
				</td>
				<th>Descri&ccedil;&atilde;o
				</td>
			</tr>
			<c:forEach items="${atributos}" var="atributo">
				<tr>
					<td><a href="javascript:parent.retorna_${param.propriedade}('${atributo.idAtributo}','${atributo.sigla}','${atributo.descricao}');window.close()">${atributo.nomeAtributo}</a></td>
					<td>${atributo.descrAtributo}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>