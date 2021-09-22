<%@ include file="/WEB-INF/page/include.jsp"%>

<c:set var="moreHead">
	<style>
ol.categoria {
	list-style-type: upper-roman;
}

ol.categoria li {
	padding-top: 10px;
}

ol.categoria ol.item li {
	padding-top: 0px;
}
</style>
</c:set>

<siga:pagina titulo="Pontos de Entrada" meta="${meta}">

	<div class="container-fluid">
		<div class="gt-content">
			<h2>Pontos de Entrada</h2>
			<!-- Dados do documento -->
			<div class="gt-content-box" style="padding: 10px;">
				<ol class="categoria">
					<c:forEach var="i" items="${map.keySet()}">
						<li>${i}</li>
						<ol class="item">
							<c:forEach var="lv" items="${map.get(i)}">
								<li><a
									href="/sigagc/app/exibirPontoDeEntrada?sigla=${lv.value}">${lv.itemLabel}</a></li>
							</c:forEach>
						</ol>
					</c:forEach>
				</ol>
			</div>
		</div>

	</div>
</siga:pagina>