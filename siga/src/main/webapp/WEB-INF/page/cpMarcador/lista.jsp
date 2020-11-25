<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista Marcadores">
	<form name="frm" action="listar" class="form" method="GET">
		<input type="hidden" name="paramoffset" value="0" /> <input
			type="hidden" name="p.offset" value="0" />
		<div class="container-fluid">
			<div class="card-header">
				<h5>Cadastro de Marcadores</h5> 
			</div>

			<!-- main content -->
			<table border="0" class="table table-sm table-striped">
				<thead class="${thead_color}">
					<tr>
						<th class="text-left w-10">Tipo</th>
						<th class="text-left w-20">Marcador</th>
						<th class="text-left w-5">Aplica&ccedil;&atilde;o</th>
						<th class="text-left w-5">Exibição</th>
						<th class="text-left w-5">Texto</th>
						<th class="text-left w-5">Interessado</th>
						<th colspan="2" class="text-left w-10">Op&ccedil;&otilde;es</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${listaMarcadores}" var="marcador">
						<tr>
							<td class="text-left w-10">${marcador.cpTipoMarcador.descricao}</td>
							<td class="text-left w-20"><span
								class="badge badge-pill badge-secondary tagmesa btn-xs"
								:style="{color: marcador.idCor.descricao + ' !important'}"> <i
									class="${marcador.idIcone.codigoFontAwesome}" style="color: ${marcador.idCor.descricao}"></i>
									${marcador.descrMarcador}
								</span>
							</td>
							<td class="text-left w-5">${marcador.idTpAplicacao.descricao}</td>
							<td class="text-left w-5">${marcador.idTpExibicao.descricao}</td>
							<td class="text-left w-5">${marcador.idTpTexto.descricao}</td>
							<td class="text-left w-5">${marcador.idTpInteressado.descricao}</td>
							<td class="text-left w-10">
								<div class="">
									<a type="button" id="btn-excluir"
										class="btn btn-outline-secondary btn-sm p-1 m-1 float-right"
										href="/siga/app/marcador/excluir?id=${marcador.id}"
										title="Excluir"><i class="far fa-trash-alt"></i>
									</a>
									<a type="button" id="btn-historico"
										class="btn btn-outline-secondary btn-sm p-1 m-1 float-right"
										href="/siga/app/marcador/historico?id=${marcador.hisIdIni}"
										title="Histórico"><i class="fas fa-history"></i>
									</a>
									<a type="button" id="btn-editar"
										class="btn btn-outline-secondary btn-sm p-1 m-1 float-right"
										href="/siga/app/marcador/editar?id=${marcador.id}"
										title="Editar"><i class="far fa-edit"></i>
									</a>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<div class="gt-table-buttons">
				<a id="btn-editar" class="btn btn-primary" href="/siga/app/marcador/editar">Incluir</a>
				<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
			</div>
		</div>
	</form>
	<div class="modal fade" id="historicoModal" tabindex="-1" role="dialog"
		aria-labelledby="historicoModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="historicoModalLabel">Histórico de
						Alterações</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<table class="table table-striped">
						<thead>
							<tr>
								<th scope="col">Validade Inicial</th>
								<th scope="col">Validade Final</th>
								<th scope="col">Responsável pela alteração</th>
								<th scope="col">Marcador</th>
								<th scope="col">Tem Data Limite?</th>
							</tr>
						</thead>
						<tbody id="historicoBody">
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Fechar</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript"
		src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>

	<script type="text/javascript">
	function excluir(id) {
		$.ajax({
			url: "${request.contextPath}/siga/app/marcador/excluir",
			type: "GET",
			data: {id}
		})
		.done(function (response, status, jqXHR){
		    if ('referrer' in document) {
		        window.location = document.referrer;
		    } else {
		        window.history.back();
		    }
		})
		.fail(function (jqXHR, textStatus, errorThrown){
	        console.error(
	            "Ocorreu um erro na exclusão: "+
	            textStatus, errorThrown
	        );
	    });
		
	}
	
	function historico(id) {
		$('#historicoModal').modal('show');
		$('#historicoBody').html("<tr><td colspan='6'><div class='text-center m-2'><div class='spinner-border text-primary' role='status'>"
				  + "<span class='sr-only'>Carregando...</span></div></div></td></tr>");
		$.ajax({
			url: "${request.contextPath}/siga/app/marcador/historico",
			type: "GET",
			data: {id}
		})
		.done(function (response, status, jqXHR){
			var table = "";
			for (var item in response) {
		        m = response[item];
				table += "<tr><td>" + m.hisDtIni
		        		+ "</td><td>" + m.hisDtFim
		        		+ "</td><td title='" + m.nomeResponsavel + "'>"  + m.responsavel 
		        		+ "</td><td><i class='fas fa-tag' style='color: #" + m.cor + "''></i>  " 
		        		+ m.descricao
		        		+ "</td><td>" + (m.temDataLimite == 1? 'Sim' : 'Não')
		        		+ "</td></tr>";
			}
			$('#historicoBody').html(table);
	    })
		.fail(function (jqXHR, textStatus, errorThrown){
	        console.error(
	            "Ocorreu um erro ao listar o histórico do marcador: "+
	            textStatus, errorThrown
	        );
	    });
		
	}
</script>
</siga:pagina>
