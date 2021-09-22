<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina
	titulo="Notificar sobre T&oacute;pico de Informa&ccedil;&atilde;o">

	<div class="container-fluid">
		<div class="">
			<h2>
				<span id="codigoInf">Notificar - ${informacao.sigla}</span>
			</h2>

			<div class="">
				<form action="${linkTo[AppController].notificarGravar}">
					<input type="hidden" name="informacao.id" value="${informacao.id}" />
					<p>Escolha quem ser&aacute; notificado sobre esse conhecimento</p>
					<div class="row">
						<div class="col-sm-12">
							<siga:pessoaLotaSelecao2 propriedadePessoa="pessoa"
								labelPessoaLotacao="Tomar Ciência" propriedadeLotacao="lotacao"
								propriedadeEmail="email" />
						</div>
					</div>
					<br />
					<div class="">
						<input type="submit" value="Notificar" class="btn btn-primary" />
						ou <a
							href="${linkTo[AppController].exibir(informacao.siglaCompacta)}">voltar</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>