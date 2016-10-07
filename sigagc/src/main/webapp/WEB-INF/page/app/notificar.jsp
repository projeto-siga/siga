<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Notificar sobre Tópico de Informação">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>
				<span id="codigoInf">Notificar - ${informacao.sigla}</span>
			</h2>

			<div class="gt-form gt-content-box">
				<form action="${linkTo[AppController].notificarGravar}">
					<input type="hidden" name="informacao.id" value="${informacao.id}" />
					<p>Escolha quem será notificado sobre esse conhecimento</p>
					<br />
					<div class="gt-form-row gt-width-100">
						<label>Tomar Ciência</label>
						<siga:pessoaLotaSelecao2 propriedadePessoa="pessoa"
							propriedadeLotacao="lotacao" propriedadeEmail="email" />
					</div>
					<br />
					<div class="gt-form-row gt-width-100">
						<input type="submit" value="Notificar"
							class="gt-btn-medium gt-btn-left" style="cursor: pointer;" />
						<p class="gt-cancel">
							ou <a
								href="${linkTo[AppController].exibir[informacao.siglaCompacta]}">voltar</a>
						</p>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>