<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet" href="/siga/javascript/select2/select2.css"
	type="text/css" media="screen, projection" />
<link rel="stylesheet"
	href="/siga/javascript/select2/select2-bootstrap.css" type="text/css"
	media="screen, projection" />

<siga:pagina titulo="Lista Marcadores">
	<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset == null) {
		offset = 0;
	}
	frm.elements["paramoffset"].value = offset;
	frm.elements["p.offset"].value = offset;
	frm.submit();
}
</script>
	<form name="frm" action="listar" class="form" method="GET">
		<input type="hidden" name="paramoffset" value="0" /> <input
			type="hidden" name="p.offset" value="0" />
		<div class="container-fluid">
			<div class="card bg-light mb-3">
				<div class="card-header">
					<h5>Cadastro de Marcadores</h5>
				</div>
				<div class="card-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group" id="idLotacaoGroup">
								<label for="idLotacaoPesquisa"><fmt:message
										key="usuario.lotacao" /></label> <select
									class="form-control siga-select2" id="idLotacaoPesquisa"
									style="width: 100%" name="idLotacaoPesquisa"
									value="${idLotacaoPesquisa}">
									<c:forEach items="${listaLotacao}" var="item">
										<option value="${item.idLotacao}"
											${item.idLotacao == idLotacaoPesquisa ? 'selected' : ''}>
											<c:if test="${item.descricao ne 'Selecione'}">${item.siglaLotacao} / </c:if>${item.descricao}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group" id="tipoMarcador">
								<label for="idTpMarcador">Tipo de Marcador</label> <select
									class="form-control" id="idTpMarcador" name="idTpMarcador"
									value="${idTpMarcador}">
									<c:forEach items="${listaTipoMarcador}" var="item">
										<option value="${item.idTpMarcador}"
											${item.idTpMarcador == idTpMarcador ? 'selected' : ''}>
											${item.descrTipoMarcador}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group" id="idDescricao">
								<label for="idDescricao">Descri&ccedil;&atilde;o</label> <input
									class="form-control" id="idDescricao" name="idDescricao"
									value="${idDescricao}"> </input>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<button type="submit" class="btn btn-primary">Pesquisar</button>
						</div>
					</div>
				</div>
			</div>

			<!-- main content -->
			<h5>Marcadores cadastrados</h5>
			<table border="0" class="table table-sm table-striped">
				<thead class="${thead_color}">
					<tr>
						<th class="text-left w-10">Tipo</th>
						<th class="text-left w-10">Exibição</th>
						<th class="text-left w-40">Marcador</th>
						<th class="text-left w-20">Exibir para</th>
						<th class="text-left w-10">Aplica&ccedil;&atilde;o</th>
						<th colspan="2" class="text-left w-10">Op&ccedil;&otilde;es</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${listaMarcadores}" var="marcador">
						<tr>
							<td class="text-left w-10">${marcador.cpTipoMarcador.descrTipoMarcador}</td>
							<td class="text-left w-10">Na Data Prevista</td>
							<td class="text-left w-40"><span
								class="badge badge-pill badge-secondary tagmesa btn-xs"
								:style="{color: marcador.cor + ' !important'}"> <i
									class="fas fa-tag" style="color: ${marcador.cor}"></i>
									${marcador.descrMarcador}
							</span> <small class="">${marcador.idTpDataLimite ne CpMarcadorTipoDataEnum.DESATIVADA ? ' - Com Data Limite' : ''}</small>
							</td>
							<td class="text-left w-20"
								title="${marcador.dpLotacaoIni.nomeLotacao}">${marcador.dpLotacaoIni.siglaLotacao}</td>
							<td class="text-left w-20">Em todas as vias</td>
							<td class="text-left w-10">
								<div class="">
									<button type="button" id="btn-excluir"
										class="btn btn-outline-secondary btn-sm p-1 m-1 float-right"
										onclick="excluir(${marcador.id})" title="Excluir">
										<i class="far fa-trash-alt"></i>
									</button>
									<button type="button" id="btn-historico"
										class="btn btn-outline-secondary btn-sm p-1 m-1 float-right"
										onclick="historico(${marcador.hisIdIni})" title="Historico">
										<i class="fas fa-history"></i>
									</button>
									<button type="button" id="btn-editar"
										class="btn btn-outline-secondary btn-sm p-1 m-1 float-right"
										onclick="editar(${marcador.id})" title="Editar">
										<i class="far fa-edit"></i>
									</button>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<div class="gt-table-buttons">
				<button type="button" id="btn-editar" 
					class="btn btn-primary" onclick="$('#editarModal').modal('show');">Incluir
				</button>
			</div>
		</div>
	</form>
	<jsp:include page="edita.jsp" />
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
	$('#editarModal').on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget);
		var titulo = button.data('titulo');
		var modal = $(this);
  		modal.find('.modal-title').text(titulo);
  		$('#id').val('');
	})
	
	function toggleDataLimite(idMarcacao, checado) {
		$("#dataLimite-" + idMarcacao).prop("disabled", !checado);
		if(checado) {
			$("#dataLimite-" + idMarcacao).focus();
		} else {
			$("#dataLimite-" + idMarcacao).val(null);
		}		
	}

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
	
	function editar(id) {
		$.ajax({
			url: "${request.contextPath}/siga/app/marcador/editar",
			type: "GET",
			data: {id}
		})
		.done(function (response, status, jqXHR){
			$('#idMarcador').val(response['id']);
			$('#descricao').val(response['descricao']);
			$('#cor' + response['cor']).prop("checked", true);
			if (response['temDataLimite']) {
				$('#tem-data-limite').prop("checked", true);	
			}
			$('#editarModalLabel').text('Editar Marcador');
			
			$('#editarModal').modal('show');
	    })
		.fail(function (jqXHR, textStatus, errorThrown){
	        console.error(
	            "Ocorreu um erro na edição: "+
	            textStatus, errorThrown
	        );
	    });
	}
</script>
</siga:pagina>
