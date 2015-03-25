<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Buscar A&cedil;&atilde;o">
<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
		<div class="gt-content-box gt-for-table">
			<form action="@{Application.buscarAcao}" enctype="multipart/form-data">
				<input type="hidden" name="popup" value="true" />
				<table class="gt-form-table">
					<tr class="header">
						<td align="center" valign="top" colspan="4">Dados da ação</td>
					</tr>
					<tr>
						<td>Código:</td>
						<td><input type="text" name="filtro.siglaAcao"
							value="${filtro?.siglaAcao}" />
						</td>
					</tr>
					<tr>
						<td>Título</td>
						<td><input type="text" name="filtro.tituloAcao"
							value="${filtro?.tituloAcao}" />  <input type="hidden" name="nome"
							value="${nome}" />
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
				<th>Descrição
				</td>
			</tr>
			<c:forEach var="acao" items="${itens}">
			<tr>
				<td><a href="javascript:opener.retorna_acao${nome}('${acao.id}','${acao.sigla}','${acao.descricao}');window.close()">${acao.sigla}</a>
				</td>
				<td style="padding-left: ${acao.nivel*13}px;">${acao.descricao}</td>
			</tr>
			</c:forEach>
		</table>
	</div>
</div>
</siga:pagina>