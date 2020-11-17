<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Solicitar Revisão de Tópico de Informação">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>
				<span id="codigoInf">Solicitar Revisão - ${informacao.sigla}</span>
			</h2>

			<div class="gt-form gt-content-box">
				<form action="${linkTo[AppController].solicitarRevisaoGravar}">
					<input type="hidden" name="informacao.id" value="${informacao.id}" />

					<p>Escolha a Pessoa ou a Lotação que irá fazer a revisão desse
						conhecimento</p>
					<br />
					<div class="gt-form-row gt-width-100">
						<label>Revisar</label>
						<siga:pessoaLotaSelecao2 propriedadePessoa="pessoa"
							propriedadeLotacao="lotacao" />
					</div>
					<br />
					<div class="gt-form-row gt-width-100">
						<input type="submit" value="Solicitar"
							class="gt-btn-medium gt-btn-left" style="cursor: pointer;" />
						<p class="gt-cancel">
							ou <a
								href="${linkTo[AppController].exibir(informacao.siglaCompacta)}">voltar</a>
						</p>
					</div>
				</form>
			</div>
		</div>
	</div>

</siga:pagina>