<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Associa&ccedil;&atilde;o de Atributo">

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
		<c:choose>
		<c:when test="${associacao?.idConfiguracao}">
		<h2 class="gt-form-head">AssociaÃ§Ã£o NÂº ${associacao?.hisIdIni}</h2>
		</c:when> <c:otherwise>
		<h2 class="gt-form-head">Cadastro de AssociaÃ§Ã£o</h2>
		</c:otherwise>
		</c:choose>

		<div class="gt-form gt-content-box">
			<form action="@Application.gravarAssociacao()" enctype="multipart/form-data">
			<c:if test="associacao?.idConfiguracao"> <input type="hidden"
				name="associacao.idConfiguracao"
				value="${associacao.idConfiguracao}"> </c:if>

			<div class="gt-form-row gt-width-66">
				<label>Item</label> <sigasr:selecao tipo="item"
				nome="associacao.itemConfiguracaoUnitario"
				value="${associacao.itemConfiguracaoUnitario?.atual}" />
			</div>

			<div class="gt-form-row gt-width-66">
				<label>ServiÃ§o</label> <sigasr:selecao tipo="acao"
				nome="associacao.acaoUnitaria" value="${associacao.acaoUnitaria?.atual}" />
			</div>
			<div class="gt-form-row gt-width-66">
				<label>Tipo de atributo</label> <sigasr:select name="associacao.atributo"
				items="${SrAtributo.listar(false)}" labelProperty="nomeAtributo"
				value="${associacao.atributo?.atual?.idAtributo}" valueProperty="idAtributo">
				</sigasr:select>
				
			</div>
			<div class="gt-form-row">
				<label><siga:checkbox name="associacao.atributoObrigatorio"
					value="${associacao.atributoObrigatorio}" /> ObrigatÃ³rio</label>
			</div>
			<div class="gt-form-row">
				<c:if test="${!associacao.hisDtFim}">
					<input type="submit" value="Gravar"
						class="gt-btn-medium gt-btn-left" />
					<a href="@{Application.listarAssociacao}" class="gt-btn-medium gt-btn-left">Cancelar</a>
				</c:if>
			</div>
			</form>
		</div>
	</div>
</div>

