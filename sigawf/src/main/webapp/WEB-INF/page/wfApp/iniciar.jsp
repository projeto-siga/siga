<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Iniciar Procedimento">
	<div class="container-fluid content">
		<h2>Iniciar Procedimento ${pd.sigla} - ${pd.nome}</h2>
		<form method="POST" action="${linkTo[WfAppController].iniciar(pd.id)}">
			<fieldset title="Dados Básicos">
				<div class="row">
					<div class="col-12">
						<div class="form-group">
							<label>Documento</label>
							<siga:escolha id="tipoDePrincipal" var="tipoDePrincipal">
								<siga:opcao id='DOC' texto="Documento">
									<siga:selecao tema='simple' titulo="Documento Principal"
										propriedade="documentoRef" urlAcao="expediente/buscar"
										urlSelecionar="expediente/selecionar" modulo="sigaex" />
								</siga:opcao>
							</siga:escolha>
						</div>
					</div>
				</div>
			</fieldset>
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group mb-0">
						<button class="btn btn-primary" type="submit">Ok</button>
						<button onclick="javascript:history.back();" class="btn btn-light ml-3">Cancelar</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</siga:pagina>
