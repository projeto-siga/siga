<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Informações">

	<div class="container-fluid">
		<h3>Estatísticas - ${lotacao.descricaoIniciaisMaiusculas}</h3>
		<br />
		<div class="row">
			<div class="col col-md-6">
				<h2>Conhecimentos Mais Recentes</h2>
				<c:if test="${not empty listaMaisRecentes}">
					<div>
						<table class="table table-sm">
							<thead class="thead-dark">
								<tr>
									<th width="45%">Título</th>
									<th width="20%">Data</th>
									<th width="20%">Autor</th>
									<th width="15%">Lotação</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="i" items="${listaMaisRecentes}">
									<tr class="gt-td">
										<td width="45%"><a
											href="${linkTo[AppController].exibir(i.siglaCompacta)}">${i.arq.titulo}
										</a></td>
										<td width="20%">${i.dtElaboracaoFim}</td>
										<td width="20%">${i.autor.primeiroNomeEIniciais}</td>
										<td width="15%">${i.lotacao.sigla}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
				<c:if test="${empty listaMaisRecentes}">
					<p class="alert alert-warning">Nenhum conhecimento recente.</p>
				</c:if>
				<br />

				<h2>Conhecimentos Mais Acessados</h2>
				<c:if test="${not empty listaMaisVisitados}">
					<div>
						<table class="table table-sm">
							<thead class="thead-dark">
								<tr>
									<th width="45%">Título</th>
									<th width="20%">Data</th>
									<th width="20%">Autor</th>
									<th width="15%">Lotação</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="i" items="${listaMaisVisitados}">
									<tr class="gt-td">
										<td width="45%"><a
											href="${linkTo[AppController].exibir(i.siglaCompacta)}">${i.arq.titulo}
										</a></td>
										<td width="20%">${i.dtElaboracaoFim}</td>
										<td width="20%">${i.autor.primeiroNomeEIniciais}</td>
										<td width="15%">${i.lotacao.sigla}</td>
									</tr>
								</c:forEach>
						</table>
					</div>
				</c:if>
				<c:if test="${empty listaMaisVisitados}">
					<p class="alert alert-warning">Nenhum conhecimento visitado.</p>
				</c:if>
				<br />
				<h2>Principais Classificações</h2>
				<c:if test="${cloud.size() > 0}">
					<div class="gt-content-box gt-tag-cloud">
						<c:forEach items="${cloud.tags()}" var="tag">
							<a href="${tag.link}" style="font-size: ${tag.weight}%;">${tag.name}</a>
						</c:forEach>
					</div>
				</c:if>
				<c:if test="${cloud.size() == 0}">
					<p class="alert alert-warning">Nenhuma classificação
						cadastrada.</p>
				</c:if>
			</div>
			<div class="col col-md-6">
				<h2>Principais Autores</h2>
				<c:if test="${not empty listaPrincipaisAutores}">
					<div>
						<table class="table table-sm">
							<thead class="thead-dark">
								<tr>
									<th width="60%">Autor</th>
									<th width="15%">Lotação</th>
									<th style="width: 25%; text-align: right;">Conhecimentos</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${listaPrincipaisAutores}" var="p">
									<tr>

										<td style="width: 65%;">${p[0]}</td>
										<td style="width: 15%;">${p[2]}</td>
										<td style="width: 20%; text-align: right;"><a
											href="${linkTo[AppController].listar}?filtro.lotacao.id=${p[3]}&filtro.autor.id=${p[1]}&filtro.situacao.idMarcador=36&filtro.pesquisa=true">${p[4]}</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
				<c:if test="${empty listaPrincipaisAutores}">
					<p class="alert alert-warning">Nenhum autor cadastrou
						conhecimentos nessa lotação.</p>
				</c:if>
				<br />

				<h2>Evolução</h2>
				<div id="evolution"></div>
				<p id="dicaGrafico" class="alert alert-primary">
					Clique nas barras <span style="color: #B00000;"> vermelhas</span>
					para saber quais conhecimentos fazem parte dessa estatística no mês
					selecionado
				</p>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#dicaGrafico").hide();
		$("#evolution").mouseenter(function(){
			$("#dicaGrafico").show()
		}).mouseleave(function(){
			$("#dicaGrafico").hide();
		});
	});
	google.load('visualization', '1', {
		packages : [ 'corechart', 'gauge' ]
	});
	google.setOnLoadCallback(drawEvolution);
	
	function drawEvolution() {
		// Some raw data (not necessarily accurate)
		var data = google.visualization.arrayToDataTable([${evolucao}]);
	
		// Create and draw the visualization.
		var grafico = new google.visualization.ColumnChart(document.getElementById('evolution'));
		grafico.draw(data, {
			legend : {position: 'top', alignment : 'center'},
			width : "100%",
			height : "100%"
		});
		google.visualization.events.addListener(grafico,"onmouseover", mudarCursorMao);
		google.visualization.events.addListener(grafico,"onmouseout", mudarCursorDefault);
		google.visualization.events.addListener(grafico, "select", function(){
			var selecao = grafico.getSelection()[0];
			if (selecao.column == 2){
				var mesAno = data.getValue(selecao.row,0).split("/");
				var numeroMes = converteMes(mesAno[0]);
				location.href = "${linkTo[AppController].listar}?filtro.situacao.idMarcador=" + "36" +
								"&filtro.dtIni=" + encodeURIComponent(primeiroDiaMes(mesAno[1],numeroMes)) + 
								"&filtro.dtFim=" + encodeURIComponent(ultimoDiaMes(mesAno[1],numeroMes)) +
								"&filtro.lotacao.id=" + ${lotacao.id} + 
								"&filtro.pesquisa=true&estatistica=" + data.getValue(selecao.row,selecao.column);
			}
			else
				grafico.setSelection([{'row': null, 'column': null}]);
		});
	}

	function mudarCursorMao(e) {
		if(e.column == 2) {
			$(document).ready(function(){
				$("#evolution").css("cursor","pointer");
			});
		}
	}
	
	function mudarCursorDefault() {
		$(document).ready(function(){
			$("#evolution").css("cursor","default");
		});
	}	
	/* 
	* recebe abreviação do mês e retorna o numero referente a esse mês.
	* parametro = Nov
	* retorno 	= 11   
	*/	
	function converteMes(mes) {
		var numeroMes = new Array();
		numeroMes[1] = "Jan";
		numeroMes[2] = "Fev";
		numeroMes[3] = "Mar";
		numeroMes[4] = "Abr";
		numeroMes[5] = "Mai";
		numeroMes[6] = "Jun";
		numeroMes[7] = "Jul";
		numeroMes[8] = "Ago";
		numeroMes[9] = "Set";
		numeroMes[10] = "Out";
		numeroMes[11] = "Nov";
		numeroMes[12] = "Dez";
		return numeroMes.indexOf(mes);
	}	
	/*
	* recebe ano e mês, e retorna o primeiro dia do mês desse ano
	* no formato dd/mm/yyyy
	*/
	function primeiroDiaMes(ano, mes) {
		var anoCompleto = "20" + ano;
		var data = new Date(anoCompleto, mes-1,1); 
		return [data.getDate(),data.getMonth()+1,data.getFullYear()].join("/");
	}
	/*
	* recebe ano e mês, e retorna o último dia do mês desse ano
	* no formato dd/mm/yyyy
	*/
	function ultimoDiaMes(ano, mes) {
		var anoCompleto = "20" + ano;
		var data = new Date(anoCompleto, mes,0);
		return [data.getDate(),data.getMonth()+1,data.getFullYear()].join("/");
	}	

</script>
</siga:pagina>