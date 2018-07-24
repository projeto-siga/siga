<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>


<siga:pagina titulo="Dados da Lotação">
	<script>
		if (${not empty f:resource('graphviz.url')}) {
		} else if (window.Worker) {
			window.VizWorker = new Worker("/siga/javascript/viz.js");
			window.VizWorker.onmessage = function(oEvent) {
				document.getElementById(oEvent.data.id).innerHTML = oEvent.data.svg;
				$(document).ready(function() {
				});
			};
		} else {
			document
					.write("<script src='/siga/javascript/viz.js' language='JavaScript1.1' type='text/javascript'>"
							+ "<"+"/script>");
		}

		function buildSvg(id, input, cont) {
			if (${not empty f:resource('graphviz.url')}) {
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
					    $(data).width("100%");
				        $("#" + id).html(data);
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
				if (cont) 
					cont();
			}
		}

		function pageHeight() {
			return window.innerHeight != null ? window.innerHeight
					: document.documentElement
							&& document.documentElement.clientHeight ? document.documentElement.clientHeight
							: document.body != null ? document.body.clientHeight
									: null;
		}

		function resize() {
			var ifr = document.getElementById('painel');

			ifr.height = pageHeight() - 300;
			console.log("resize foi chamado!");
		}
	</script>
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2 class="gt-table-head">Dados da Lotação</h2>
		</div>
		<div class="gt-form gt-content-box">
			<p>
				<b>Nome: ${lotacao.nomeLotacao} </b>
			</p>
			<p>
				<b>Sigla:</b> ${lotacao.sigla}
			</p>
			<p>
				<b>Lotação Pai:</b>
				<siga:selecionado
					sigla="${lotacao.lotacaoPai.sigla} - ${lotacao.lotacaoPai.descricaoAmpliada}"
					descricao="${lotacao.lotacaoPai.descricaoAmpliada}"
					lotacaoParam="${lotacao.lotacaoPai.orgaoUsuario.sigla}${lotacao.lotacaoPai.sigla}" />
			</p>
		</div>

		<div class="gt-content clearfix">
			<h2 class="gt-table-head">Lotações Subordinadas</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>
							<th align="right">Sigla</th>
							<th align="right">Nome</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="lotacaoSubordinada"
							items="${lotacao.dpLotacaoSubordinadosSet}">
							<c:if test="${empty lotacaoSubordinada.dataFimLotacao}">
								<tr>
									<td><siga:selecionado sigla="${lotacaoSubordinada.sigla}"
											descricao="${lotacaoSubordinada.descricaoAmpliada}"
											lotacaoParam="${lotacaoSubordinada.orgaoUsuario.sigla}${lotacaoSubordinada.sigla}" />
									</td>
									<td>${lotacaoSubordinada.nomeLotacao}</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<br /> <br />
		<div class="gt-content clearfix">
			<h2 class="gt-table-head">Magistrados/ Servidores</h2>
			<div class="gt-content-box gt-for-table">
				<table border="0" class="gt-table">
					<thead>
						<tr>
							<th align="right">Nome</th>
							<th align="right">Matricula</th>
							<!-- <th align="right">Lotacao</th> -->
							<th align="right">Cargo</th>
							<th align="right">Função</th>
							<th align="right">Email</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach var="pessoa" items="${lotacao.dpPessoaLotadosSet}">
							<tr>
								<td>${pessoa.nomePessoa}</td>
								<td><siga:selecionado sigla="${pessoa.sigla}"
										descricao="${pessoa.descricao} - ${pessoa.sigla}"
										pessoaParam="${pessoa.sigla}" /></td>
								<%-- <td><siga:selecionado 
									sigla="${pessoa.lotacao.sigla}"
									descricao="${pessoa.lotacao.descricaoAmpliada}"
									lotacaoParam="${pessoa.lotacao.orgaoUsuario.sigla}${pessoa.lotacao.sigla}" /></td> --%>
								<td>${pessoa.cargo.descricao}</td>
								<td>${pessoa.funcaoConfianca.descricao}</td>
								<td>${pessoa.emailPessoa}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<br /> <br />
		<div class="gt-content clearfix">
			<h2 class="gt-table-head">Organograma</h2>
			<div class="gt-content-box gt-for-table">
				<div id="organograma"></div>
			</div>
		</div>
	</div>
	<script>
		buildSvg('organograma', '${graph}');
	</script>
</siga:pagina>