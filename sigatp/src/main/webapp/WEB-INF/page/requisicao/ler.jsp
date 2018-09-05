<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>

<siga:pagina titulo="Siga - Transporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Visualizar Requisi&ccedil;&atilde;o ${requisicaoTransporte.buscarSequence()}</h2>
			<input type="hidden" id="lersomente">
			<jsp:include page="form.jsp"></jsp:include>
		</div>
	</div>
</siga:pagina>