<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<jsp:useBean id="now" class="java.util.Date" />

<siga:pagina titulo="Lista Marcadores">
	<form name="frm" action="listar" class="form" method="GET">
		<input type="hidden" name="paramoffset" value="0" /> <input
			type="hidden" name="p.offset" value="0" />
		<div class="container-fluid">
			<div class="card bg-light mb-3">
				<div class="card-header">
					<h5>Cadastro de Marcadores</h5>
				</div>
				<div class="card-body">
					<!-- main content -->
					<table border="0" class="table table-sm table-striped">
						<thead class="${thead_color}">
							<tr>
								<th class="text-left w-10">Categoria</th>
								<th class="text-left w-35">Marcador</th>
								<th class="text-left w-10">Tipo</th>
								<th class="text-left w-10">Finalidade</th>
								<th class="text-left w-10">Grupo</th>
								<th colspan="2" class="text-right w-15">Op&ccedil;&otilde;es</th>
							</tr>
						</thead>

						<tbody>
							<c:forEach items="${listaMarcadores}" var="marcador">
								<tr>
									<fmt:formatDate var="dtIni" value="${marcador.hisDtIni}" type='date' pattern="dd/MM/yyyy" />
									<td class="${marcador.hisDtIni gt now? 'disabled':''} text-left w-10">${marcador.idFinalidade.grupo.nome}</td>
									<td class="${marcador.hisDtIni gt now? 'disabled':''} text-left w-30"><span
										class="badge badge-pill badge-secondary tagmesa btn-xs"
										title="${marcador.descrDetalhada}"> <i
											class="${marcador.idIcone.codigoFontAwesome}"
											style="color: ${marcador.idCor.descricao}"></i>
											${marcador.descrMarcador}
										</span>
										${marcador.hisDtIni gt now? '<br /><small>(Será ativado em '.concat(dtIni).concat(')</small>'):''}
									</td>
									<td class="${marcador.hisDtIni gt now? 'disabled':''} text-left w-10">${marcador.idFinalidade.nome}</td>
									<td class="${marcador.hisDtIni gt now? 'disabled':''} text-left w-10">${marcador.idFinalidade.descricao}</td>
									<td class="${marcador.hisDtIni gt now? 'disabled':''} text-left w-10">${marcador.idGrupo.nome}</td>
									<td class="text-left w-10">
										<div class="">
											<button type="button" id="btn-excluir"
												class="enabled btn btn-outline-secondary btn-sm p-1 m-1 float-right"
												onclick="excluir(${marcador.id})" title="Excluir">
												<i class="far fa-trash-alt"></i>
											</button>
											<a type="button" id="btn-historico"
												class="enabled btn btn-outline-secondary btn-sm p-1 m-1 float-right"
												href="/siga/app/marcador/historico?id=${marcador.hisIdIni}"
												title="Histórico"><i class="fas fa-history"></i> </a> <a
												type="button" id="btn-editar"
												class="enabled btn btn-outline-secondary btn-sm p-1 m-1 float-right"
												href="/siga/app/marcador/editar?id=${marcador.id}"
												title="Editar"><i class="far fa-edit"></i> </a>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

					<div class="gt-table-buttons">
						<a id="btn-editar" class="btn btn-primary"
							href="/siga/app/marcador/editar">Incluir</a> <input type="button"
							value="Voltar" onclick="javascript:history.back();"
							class="btn btn-cancel ml-2" />
					</div>
				</div>
			</div>
		</div>
	</form>

	<script type="text/javascript"
		src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript"
		src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>

	<script type="text/javascript">
	function excluir(id) {
		$.ajax({
			url: "${request.contextPath}/siga/app/marcador/excluir",
			type: "POST",
			data: {id}
		})
		.done(function (response, status, jqXHR){
			document.location.reload(true);
		})
		.fail(function (jqXHR, textStatus, errorThrown){
	        console.error(
	            "Ocorreu um erro na exclusão: "+
	            textStatus, errorThrown
	        );
			document.getElementById("mensagemCabec").textContent = textStatus;
			document.getElementById("mensagemCabec").classList.add("alert-danger");
			document.getElementById("mensagemCabec").classList.add("fade-close")
			document.getElementById("mensagemCabecId").classList.remove("d-none");
	    });
		
	}
	</script>
</siga:pagina>
