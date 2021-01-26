<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<form action="${linkTo[SolicitacaoController].darAndamento}" 
		method="post" onsubmit="javascript: return block();"
		enctype="multipart/form-data">
			
	<input type="hidden" name="todoOContexto" value="${todoOContexto}" />
	<input type="hidden" name="ocultas" value="${ocultas}" /> <input
		type="hidden" name="movimentacao.solicitacao.idSolicitacao"
		value="${movimentacao.solicitacao.idSolicitacao}" />

	<c:choose>
		<c:when test="${solicitacao.podeTrocarAtendente(titular, lotaTitular)}">
			<div class="form-group">
				<label>Atendente</label>
				<div id="divAtendente">
					<select name="movimentacao.atendente.id" id="selectActendente" class="form-control">
   						<option value=""></option>
						<c:if test="${atendentes.size() >= 1}">
							<c:forEach items="${atendentes}" var="pessoa">
								<option value="${pessoa.pessoaAtual.idPessoa}" ${movimentacao.atendente.idInicial.equals(pessoa.idInicial) ? 'selected' : ''}>${pessoa.pessoaAtual.descricaoIniciaisMaiusculas}</option>
							</c:forEach>
						</c:if>
						<c:set var="substitutos" value="${solicitacao.substitutos}" />
						<c:if test="${substitutos.size() >= 1}">
							<optgroup label="Substitutos">
								<c:forEach items="${substitutos}" var="pessoaSubst">
									<option value="${pessoaSubst.substituto.pessoaAtual.idPessoa}" ${movimentacao.atendente.idInicial.equals(pessoaSubst.substituto.idInicial) ? 'selected' : ''}>${pessoaSubst.substituto.pessoaAtual.descricaoIniciaisMaiusculas}</option>
								</c:forEach>
							</optgroup>
						</c:if>
					</select>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<input type="hidden" name="movimentacao.atendente.id" value="${movimentacao.solicitacao.atendente.pessoaAtual.idPessoa}" />
		</c:otherwise>
	</c:choose>
					
					
	<div style="display: inline" class="form-group">
		<label>Pr&oacute;ximo Andamento</label>
		<textarea style="width: 100%"
			class="form-control"
			name="movimentacao.descrMovimentacao" id="descrSolicitacao"
			cols="70" rows="4" value="${movimentacao.descrMovimentacao}"></textarea>
	</div>

	<div class="mt-2 form-group">
		<input type="submit" value="Gravar"
			class="btn btn-primary" />
	</div>
</form>