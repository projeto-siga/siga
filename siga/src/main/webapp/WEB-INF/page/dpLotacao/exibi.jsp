<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script>
	if (${not empty f:resource('/vizservice.url')}) {
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
<siga:pagina titulo="Dados da Lotação">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Dados da <fmt:message key="usuario.lotacao"/></h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<label>Nome</label>
							<span>${lotacao.nomeLotacao}</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm">
						<div class="form-group">
							<label>Sigla</label>
							<span>${lotacao.sigla}</span>
						</div>
					</div>
				</div>
				<div class="row ${hide_only_GOVSP}">
					<div class="col-sm">
						<div class="form-group">
							<label>Lotação Pai</label>
							<siga:selecionado sigla="${lotacao.lotacaoPai.sigla} - ${lotacao.lotacaoPai.descricaoAmpliada}"
								descricao="${lotacao.lotacaoPai.descricaoAmpliada}"
								lotacaoParam="${lotacao.lotacaoPai.orgaoUsuario.sigla}${lotacao.lotacaoPai.sigla}" />
						</div>
					</div>
				</div>
			</div>
		</div>
		<h3 class="gt-table-head ${hide_only_GOVSP}">Lotações Subordinadas</h3>
		<table border="0" class="table table-sm table-striped  ${hide_only_GOVSP}">
			<thead class="${thead_color}">
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
		<br /> <br />
		<h3 class="gt-table-head"><fmt:message key="tela.lotacao.magistrados.servidores"/></h3>
		<table border="0" class="table table-sm table-striped">
			<thead class="${thead_color}">
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
		<br /> <br />
		<h3 class="gt-table-head ${hide_only_GOVSP}">Organograma</h3>
		<div id="organograma"></div>
	</div>

	<script>
		buildSvg('organograma', '${graph}');
	</script>

</siga:pagina>