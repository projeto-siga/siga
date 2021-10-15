<%@ include file="/WEB-INF/page/include.jsp"%>
<siga:pagina titulo="${pi.sigla}">


	<div class="container-fluid content" id="page">

		<div class="row">
			<div class="col">
				<h2 class="">Diagrama ${pd.sigla}: ${pd.nome}</h2>
				<!-- lista de Ações -->
				<siga:links>
					<c:forEach var="acao" items="${pd.getAcoes(titular, lotaTitular)}">
						<siga:link icon="${acao.icone}" title="${acao.nomeNbsp}"
							pre="${acao.pre}" pos="${acao.pos}"
							url="${pageContext.request.contextPath}${acao.url}"
							popup="${acao.popup}" confirm="${acao.msgConfirmacao}"
							classe="${acao.classe}" estilo="line-height: 160% !important"
							atalho="${true}" modal="${acao.modal}"
							explicacao="${acao.explicacao}" post="${acao.post}"
							test="${acao.pode}" />
					</c:forEach>
				</siga:links>
			</div>
		</div>
		<div class="row mt-2">
			<div class="col col-sm-12 col-md-8">
				<c:if test="${not empty dot}">
					<div class="card bg-light mb-3 bg-white p-3">
						<div id="output" class="graph-svg"
							style="border: 0px; padding: 0px; text-align: center;"></div>
					</div>
				</c:if>
			</div>
			<div class="col col-sm-12 col-md-4">
				<div class="card-sidebar card bg-light mb-3">
					<div class="card-header">Dados da Definição de Procedimento</div>
					<div class="card-body">
						<p>
							<b>Tipo de Principal:</b> ${pd.tipoDePrincipal.descr}
						</p>
						<c:if test="${pd.tipoDePrincipal != 'NENHUM'}">
							<p>
								<b>Vínculo com Principal:</b>
								${pd.tipoDeVinculoComPrincipal.descr}
							</p>
						</c:if>
						<p>
							<b>Descrição:</b> ${pd.descr}
						</p>
						<p>
							<b>Acesso para Editar:</b> ${pd.acessoDeEdicao.descr}
						</p>
						<p>
							<b>Acesso para Iniciar:</b> ${pd.acessoDeInicializacao.descr}
						</p>
						<p>
							<b>Responsavel:</b> ${pd.responsavel}
						</p>
						<p>
							<b>Lotação Responsável:</b> ${pd.lotaResponsavel}
						</p>
						<p>
							<b>Cadastrante:</b> ${pd.hisIdcIni.dpPessoa.pessoaAtual.sigla}
							(${pd.hisIdcIni.dpPessoa.pessoaAtual.lotacao.sigla})
						</p>
						<p>
							<b>Última atualização:</b>
							<fmt:formatDate value="${pd.hisDtIni}"
								pattern="dd/MM/yyyy HH:mm:ss" />
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		if (${not empty f:resource('/vizservice.url')}) {
		} else if (window.Worker) {
			window.VizWorker = new Worker("/siga/javascript/viz.js");
			window.VizWorker.onmessage = function(oEvent) {
				document.getElementById(oEvent.data.id).innerHTML = oEvent.data.svg;
				$(document).ready(function() {
					updateContainer();
				});
			};
		} else {
			document
					.write("<script src='/siga/javascript/viz.js' language='JavaScript1.1' type='text/javascript'>"
							+ "<"+"/script>");
		}
	</script>

	<script>
		function buildSvg(id, input, cont) {
			if (${not empty f:resource('/vizservice.url')}) {
			    input = input.replace(/fontsize=\d+/gm, "");
			    $.ajax({
				    url: "/siga/public/app/graphviz/svg",
				    data: input,
				    type: 'POST',
				    processData: false,
				    contentType: 'text/vnd.graphviz',
				    contents: window.String,
				    success: function(data) {
					    data = data.replace(/width="\d+pt" height="\d+pt"/gm, "");
					    data = data.replace(/fill="white"/gm, 'fill="none"');
					    data = data.replace(/stroke="white"/gm, 'stroke="none"');
					    $(data).width("100%");
				        $("#" + id).html(data);
				        updateContainer();
				    }
				});
			} else if (window.VizWorker) {
				document.getElementById(id).innerHTML = "Aguarde...";
				window.VizWorker.postMessage({
					id : id,
					graph : input
				});
			} else {
				var result = Viz(input, "svg", "dot");
				document.getElementById(id).innerHTML = result;
				cont();
			}
		}

		function smallmap() {
			var input = 'digraph G { graph[size="100,100"]; ${dot} }';
			buildSvg('output', input, updateContainer);
		}

		function showBig() {
			document.getElementById('page').style.display = 'none';
			document.getElementById('svg').style.display = 'block';
			bigmap();
		}

		function hideBig() {
			document.getElementById('page').style.display = 'block';
			document.getElementById('svg').style.display = 'none';
			updateContainer();
		}

		smallmap();

		$(document).ready(function() {
			updateContainer();
			$(window).resize(function() {
				updateContainer();
			});
		});
		function updateContainer() {
			var smallwidth = $('#output').width();
			var smallheight = $('#output').height();
			var smallsvg = $('#output :first-child').first();
			var smallviewbox = smallsvg.attr('viewBox');
			
			if (smallheight > smallwidth * 120/100)
				smallheight = smallwidth * 120/100;

			if (smallsvg && smallsvg[0] && smallsvg[0].viewBox && smallsvg[0].viewBox.baseVal){

				console.log('updated')

				var baseVal = smallsvg[0].viewBox.baseVal;
				var width = smallwidth;
				var height = smallwidth * baseVal.height / baseVal.width;
				if (height > smallheight) {
					width = width * smallheight / height;
					height = smallheight;
				}
				smallsvg.attr('width', width);
				smallsvg.attr('height', height);
			} else if (typeof smallviewbox != 'undefined') {
				var a = smallviewbox.split(' ');
				
				// set attrs and 'resume' force 
				smallsvg.attr('width', smallwidth);
				smallsvg.attr('height', smallwidth * a[3] / a[2]);
			} 

		}
	</script>
</siga:pagina>