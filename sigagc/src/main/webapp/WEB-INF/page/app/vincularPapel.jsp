<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina
	titulo="Definir Perfil Sobre T&oacute;pico de Informa&ccedil;&atilde;o">

	<div class="container-fluid">
		<div class="gt-content clearfix">
			<h2>
				<span id="codigoInf">Definir Perfil - ${informacao.sigla}</span>
			</h2>

			<div class="form mt-3">
				<form action="${linkTo[AppController].vincularPapelGravar}">
					<input type="hidden" name="informacao.id" value="${informacao.id}" />
					<div class="gt-form-row gt-width-100">
						<label>Perfil</label> <select name="papel.id">
							<c:forEach items="${listaPapel}" var="item">
								<option value="${item.idPapel}">${item.descPapel}</option>
							</c:forEach>
						</select>
					</div>
					<div class="gt-form-row gt-width-100">
						<label>Vincular a</label>
						<siga:pessoaLotaFuncCargoSelecao nomeSelLotacao="lotacao"
							nomeSelPessoa="pessoa" nomeSelGrupo="grupo" />
					</div>
					<br />
					<div class="gt-form-row gt-width-100">
						<input type="submit" value="Gravar" class="btn btn-primary" /> ou
						<a
							href="${linkTo[AppController].exibir(informacao.siglaCompacta)}">voltar</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>