<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Iniciar Procedimento">
	<div class="container-fluid content">
		<h2>Iniciar Procedimento ${pd.sigla} - ${pd.nome}</h2>
		<form method="POST" action="${linkTo[WfAppController].iniciar(pd.id)}">
			<fieldset title="Dados Básicos">
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<label>Principal</label> <input type="text" name="principal"
								size="80" class="form-control" />
						</div>
					</div>
				</div>
			</fieldset>
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group mb-0">
						<button class="btn btn-primary" type="submit">Ok</button>
						<button onclick="javascript:history.back();" class="btn btn-light">Cancelar</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</siga:pagina>
