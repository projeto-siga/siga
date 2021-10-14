<%@ include file="/WEB-INF/page/include.jsp"%>
<siga:pagina titulo="${pi.sigla}">


	<div class="container-fluid content" id="page">

		<div class="row mt-2">
			<div class="col col-sm-12 col-md-8">
				<h2 class="mt-3">Procedimento ${pi.sigla}</h2>

				<!-- lista de Ações -->
				<siga:links>
					<c:forEach var="acao" items="${pi.getAcoes(titular, lotaTitular)}">
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

				<c:if test="${pi.pausado}">
					<div class="card bg-info mb-3 mt-3">
						<div class="card-header text-white">
							<c:if
								test="${pi.tipoDePrincipal eq 'DOCUMENTO' and not empty pi.principal}">
								<a
									href="/sigaex/app/expediente/doc/exibir?sigla=${pi.principal}"
									style="color: white; text-decoration: underline;">${pi.principal}</a> -
							</c:if>

							${pi.definicaoDeProcedimento.nome} -
							${pi.definicaoDeTarefaCorrente.nome}
						</div>
						<div class="card-body bg-light text-black">
							<form method="POST"
								action="${linkTo[WfAppController].continuar(piId, null, null, null)}">
								<%@ include file="inc-form.jsp"%>
							</form>
						</div>
					</div>
				</c:if>


				<!-- Conhecimento -->
				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
					<div class="card-deck">
						<span id="gc-ancora"></span> <span id="gc"></span>
					</div>
				</c:if>

				<!-- Movimentações -->
				<c:if test="${not empty movs}">
					<div class="gt-content-box gt-for-table"
						style="margin-bottom: 25px; margin-top: 10px;">
						<script type="text/javascript">
							var css = "<style>TABLE.mov TR.despacho { background-color: rgb(240, 255, 240);}";
							css += "TABLE.mov TR.juntada,TR.desentranhamento { background-color: rgb(229, 240, 255);}";
							css += "TABLE.mov TR.anotacao { background-color: rgb(255, 255, 255);}";
							css += "TABLE.mov TR.anexacao { background-color: rgb(255, 255, 215);}";
							css += "TABLE.mov TR.encerramento_volume { background-color: rgb(255, 218, 218);}</style>";
							$(css).appendTo("head");
						</script>
						<table class="table table-sm table-responsive-sm table-striped">
							<thead>
								<tr>
									<th align="center">Tempo</th>
									<th>Lotação</th>
									<th>Evento</th>
									<th>Descrição</th>
									<th></th>
								</tr>
							</thead>
							<c:forEach var="mov" items="${movs}">
								<tr class="${mov.classeVO}">
									<td class="align-top" title="${mov.dtIniVO}">${mov.tempoRelativoVO}</td>
									<td class="align-top"
										title="${mov.hisIdcIni.dpPessoa.descricao} - ${mov.hisIdcIni.dpPessoa.lotacao.descricao}">${mov.lotaTitular.sigla}</td>
									<td class="align-top">${mov.evento}</td>
									<td class="align-top" style="word-break: break-all;"><span
										class="align-top">${mov.descricaoEvento}</span></td>

									<td class="align-top" style="word-break: break-all;"><span
										class="align-top"> <siga:links buttons="${false}"
												inline="${true}" separator="${false}">
												<c:forEach var="acao" items="${mov.acoes}">
													<siga:link title="${acao.nomeNbsp}" pre="${acao.pre}"
														pos="${acao.pos}"
														url="${pageContext.request.contextPath}${acao.url}"
														test="${true}" popup="${acao.popup}"
														confirm="${acao.msgConfirmacao}" classe="${acao.classe}"
														explicacao="${acao.explicacao}" post="${acao.post}" />
												</c:forEach>
											</siga:links>
									</span></td>

								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
			</div>

			<div class="col col-sm-12 col-md-4">
				<c:if test="${not empty dot}">
					<div class="card-sidebar card bg-light mb-3">
						<div class="card-header">Diagrama</div>
						<div class="card-body bg-white p-2">
							<div id="output" class="graph-svg"
								style="border: 0px; padding: 0px; text-align: center;"></div>
						</div>
					</div>
					<!-- Fim mapa tramitação -->
				</c:if>

				<c:if test="${not empty pi.variaveis}">
					<div class="card-sidebar card bg-light mb-3">
						<div class="card-header">Variáveis</div>
						<div class="card-body">
							<c:forEach var="v" items="${pi.variaveis}">
								<p>
									<b>${v.nome}:</b> ${v.valorAsString}
								</p>
							</c:forEach>
						</div>
					</div>
				</c:if>

				<div class="card-sidebar card bg-light mb-3">
					<div class="card-header">Dados do Procedimento</div>
					<div class="card-body">
						<c:if test="${not empty pi.principal}">
							<p>
								<b>Principal:</b>
								<c:if test="${pi.tipoDePrincipal eq 'DOCUMENTO'}">
									<a
										href="/sigaex/app/expediente/doc/exibir?sigla=${pi.principal}">${pi.principal}</a>
								</c:if>
								<c:if test="${pi.tipoDePrincipal != 'DOCUMENTO'}">
								${pi.principal}
								</c:if>
							</p>
							<p>
								<b>Tipo do principal:</b> ${pi.tipoDePrincipal.descr}
							</p>
						</c:if>
						<p>
							<b>Diagrama:</b> <a
								href="/sigawf/app/diagrama/exibir?id=${pi.definicaoDeProcedimento.id}">${pi.definicaoDeProcedimento.nome}</a>
							(${pi.definicaoDeProcedimento.sigla})
						</p>
						<p>
							<b>Prioridade:</b> ${pi.prioridade.descr}
						</p>
						<p>
							<b>Cadastrante:</b> ${pi.hisIdcIni.dpPessoa.sigla}
							(${pi.hisIdcIni.dpPessoa.lotacao.sigla})
						</p>
						<p>
							<b>Titular:</b> ${pi.titular} (${pi.lotaTitular})
						</p>
						<c:if test="${not empty pi.eventoData}">
							<p>
								<b>Tarefa:</b> ${pi.definicaoDeTarefaCorrente.nome}
							</p>
							<p>
								<b>Início da tarefa:</b> ${f:espera(pi.eventoData)}
							</p>
						</c:if>
						<p>
							<b>Início do procedimento:</b> ${f:espera(pi.hisDtIni)}
						</p>
						<p>
							<b>Término do procedimento:</b> ${f:espera(pi.hisDtFim)}
						</p>
					</div>
				</div>

				<c:if test="${not empty pi.eventoData}">
					<div class="card-sidebar card bg-light mb-3">
						<div class="card-header">Dados da Tarefa</div>
						<div class="card-body">
							<p>
								<b>Tarefa:</b> ${pi.definicaoDeTarefaCorrente.nome}
							</p>
							<p>
								<b>Início da tarefa:</b> ${f:espera(pi.eventoData)}
							</p>
						</div>
					</div>
				</c:if>

				<div class="gt-bd clearfix" style="display: none" id="svg">
					<div class="gt-content clearfix">
						<div id="desc_editar">
							<h3>Mapa do Procedimento</h3>
							<div style="display: none" id="input2">digraph G {
								graph[size="100,100", rankdir="LR"]; ${dot} }</div>

							<div class="gt-form gt-content-box" style="padding-bottom: 15px;"
								id="output2"></div>
						</div>
					</div>
					<a href="javascript: hideBig();" class="gt-btn-large gt-btn-left">Voltar</a>

				</div>

				<c:if
					test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GC')}">
					<c:url var="url" value="/../sigagc/app/knowledgeInplace">
						<c:param name="tags">${pi.ancora}</c:param>
						<c:param name="msgvazio">Ainda não existe uma descrição de como esta tarefa deve ser executada. Por favor, clique <a
								href="$1">aqui</a> para contribuir.</c:param>
						<c:param name="titulo">${pi.definicaoDeProcedimento.nome} - ${pi.currentTaskDefinition.nome}</c:param>
						<c:param name="ts">${currentTimeMillis}</c:param>
					</c:url>
					<script type="text/javascript">
					$.ajax({
		                type: "GET",
		                url: "${url}",
		                cache: false,
		                success: function(response) {
		                    $('#gc-ancora').replaceWith(response);
		                }
		            });
					</script>

					<c:url var="url" value="/../sigagc/app/knowledgeSidebar">
						<c:param name="tags">@workflow</c:param>
						<c:forEach var="tag" items="${pi.tags}">
							<c:param name="tags">${tag}</c:param>
						</c:forEach>
						<c:param name="ts">${currentTimeMillis}</c:param>
					</c:url>
					<script type="text/javascript">
					$.ajax({
		                type: "GET",
		                url: "${url}",
		                cache: false,
		                success: function(response) {
		                    $('#gc').replaceWith(response);
		                }
		            });
					</script>
				</c:if>

				<%@ include file="anotar.jsp"%>

				<%@ include file="redirecionar.jsp"%>

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

		function bigmap() {
			var input = 'digraph G { graph[size="100,100"]; ${dot} }';
			buildSvg('output2', input, updateContainer);
		}

		function smallmap() {
			var input = 'digraph G { graph[size="3,3"]; ${dot} }';
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
			
			if (smallheight > smallwidth * 4/4)
				smallheight = smallwidth * 4/4;

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

			var bigwidth = $('#output2').width();
			var bigsvg = $('#output2 :first-child').first();
			var bigviewbox = bigsvg.attr('viewBox');

			if (typeof bigviewbox != 'undefined') {
				var a = bigviewbox.split(' ');

				// set attrs and 'resume' force 
				bigsvg.attr('width', bigwidth);
				bigsvg.attr('height', bigwidth * a[3] / a[2]);
			}
		}
	</script>
</siga:pagina>