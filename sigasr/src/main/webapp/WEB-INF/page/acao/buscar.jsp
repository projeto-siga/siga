<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../popupHeader.jsp"></jsp:include>
<!-- ja existe em main.jsp -->
<!-- <script src="//code.jquery.com/jquery-1.11.0.min.js"></script> -->
<jsp:include page="../main.jsp"></jsp:include>


<head><title>Pesquisa de Ações</title></head>

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
		<div class="gt-content-box gt-for-table">
			<form action="${linkTo[AcaoController].buscar }" enctype="multipart/form-data">
				<input type="hidden" name="popup" value="true" />
				<table class="gt-form-table">
					<tr class="header">
						<td align="center" valign="top" colspan="4">Dados da a&ccedil;&atilde;o</td>
					</tr>
					<tr>
						<td>Código:</td>
						<td><input type="text" name="siglaAcao" value="${filtro.siglaAcao}" />
						</td>
					</tr>
					<tr>
						<td>Título</td>
						<td>
							<input type="text" name="tituloAcao" value="${filtro.tituloAcao}" />
							<input type="hidden" name="nome" value="${nome}" />
							<input type="hidden" name="propriedade" value="${propriedade}" />
						</td>
					</tr>
					<tr>
						<input type="hidden" name="pessoa" value="${pessoa}" />
						<input type="hidden" name="item" value="${item}" />
						<td><input type="submit" class="gt-btn-small gt-btn-left" value="Pesquisar" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<br />
	
	<div class="gt-content-box gt-for-table">
		<table class="gt-table">
			<tr>
				<th>Código
				</td>
				<th>Descri&ccedil;&atilde;o
				</td>
			</tr>
			<c:forEach items="${items}" var="acao">
				<tr>
					<td><a href="javascript:parent.retorna_${param.propriedade}('${acao.id}','${acao.sigla}','${acao.descricao}');window.close()">${acao.sigla}</a></td>
					<td style="padding-left: ${acao.nivel*13}px;">${acao.descricao}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>