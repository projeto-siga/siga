<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Iniciar Procedimento">
	<div class="container-fluid content">
		<h2>Iniciar Procedimento ${pd.sigla}: ${pd.nome}</h2>
		<form method="POST" action="${linkTo[WfAppController].iniciar(pd.id)}">
			<fieldset title="Dados Básicos">
				<div class="row">
					<div class="col col-12">
						<div class="form-group">
							<label>Documento</label>
							<siga:escolha id="tipoDePrincipal" var="tipoDePrincipal"
								singleLine="${true}">
								<siga:opcao id='DOC' texto="Documento">
									<siga:selecao tema='simple' titulo="Documento Principal"
										propriedade="documentoRef" urlAcao="expediente/buscar"
										urlSelecionar="expediente/selecionar" modulo="sigaex" />
								</siga:opcao>
							</siga:escolha>
						</div>
					</div>
				</div>
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF;INI:Iniciar;PRIM:Especificar Primeira Tarefa')}">
					<div class="row">
						<div class="col col-12 col-md-6">
							<div class="form-group">
								<div class="form-group">
									<label for="tdId">Primeira Tarefa</label> <select id="tdId"
										name="tdId" class="form-control">
										<option value="">[Início]</option>
										<c:forEach var="td" items="${pd.definicaoDeTarefa}">
											<option value="${td.id}">${td.nome}</option>
										</c:forEach>
									</select> <small class="form-text text-muted float-right" id="Qtd">Selecione
										a tarefa na qual vc deseja que o procedimento inicie</small>
								</div>
							</div>
						</div>
					</div>
				</c:if>
			</fieldset>
			<div class="row">
				<div class="col col-12">
					<div class="form-group mb-0">
						<button class="btn btn-primary" type="submit">Ok</button>
						<button onclick="javascript:history.back();"
							class="btn btn-light ml-3">Cancelar</button>
					</div>
				</div>
			</div>
		</form>
	</div>
</siga:pagina>
