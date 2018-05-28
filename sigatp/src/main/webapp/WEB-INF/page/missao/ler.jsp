<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>

<siga:pagina titulo="SIGA - Transporte">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Visualizar Miss&atilde;o: ${missao.sequence}</h2>
			<jsp:include page="consultaDados.jsp"></jsp:include>
		</div>
	</div>
</siga:pagina>