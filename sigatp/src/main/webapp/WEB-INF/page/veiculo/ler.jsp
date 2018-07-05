<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="SIGA-Transporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Visualizar Ve&iacute;culo ${veiculo.placa}</h2>
			<input type="hidden" id="lersomente">
			<jsp:include page="form.jsp"></jsp:include>
		</div>
	</div>
</siga:pagina>