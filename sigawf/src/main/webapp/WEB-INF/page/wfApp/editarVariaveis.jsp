<%@ include file="/WEB-INF/page/include.jsp"%>
<siga:pagina titulo="${pi.sigla}">
	<div class="container-fluid content" id="page">
		<div class="row mt-2">
			<div class="col col-12">
				<h2 class="mt-3">Editar Vari&aacute;veis do Procedimento ${pi.sigla}</h2>
				<div class="card bg-light mb-3 mt-3">
					<div class="card-body bg-light text-black">
						<form method="POST"
							action="${linkTo[WfAppController].salvarVariaveis(piId, null, null)}">
							<%@ include file="inc-form.jsp"%>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</siga:pagina>