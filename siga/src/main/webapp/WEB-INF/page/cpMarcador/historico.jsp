<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Forma">
	<c:set var="grupoDefault" scope="session" value="${f:resource('/siga.marcadores.grupo.default')}" />
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Histórico do Marcador</h5>
			</div>
			<div class="card-body">
				<table class="table table-striped">
					<thead class="thead-dark">
						<tr>
							<th scope="col">Validade Inicial</th>
							<th scope="col">Validade Final</th>
							<th scope="col">Responsável pela alteração</th>
							<th scope="col">Marcador</th>
							<th scope="col">Tipo Marcador</th>
							<th scope="col">Finalidade</th>
							<c:if test="${empty grupoDefault}">
								<th scope="col">Grupo ${grupoDefault}</th>
							</c:if>
							<th scope="col">Ativo</th>
						</tr>
					</thead>
					<tbody id="historicoBody">
						<c:forEach items="${listaHistorico}" var="marcador">
							<tr>
								<td>${marcador.hisDtIni}</td>
								<td>${marcador.hisDtFim}</td>
								<td title="${marcador.hisIdcIni.dpPessoa.nomePessoa}">${marcador.hisIdcIni.nmLoginIdentidade}</td>
								<td title="${marcador.descrDetalhada}"><i class='${marcador.idIcone.codigoFontAwesome}' style='color: #${marcador.idCor.descricao}'>
									</i> ${marcador.descrMarcador}</td>
								<td>${marcador.idFinalidade.idTpMarcador.descricao}</td>
								<td>${marcador.idFinalidade.descricao}</td>
								<c:if test="${empty grupoDefault}">
									<td>${marcador.idGrupo.nome}</td>
								</c:if>
								<td>${marcador.hisAtivo eq 1? 'Sim' : 'Não'}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="form-group">
					<a href="/siga/app/marcador/listar" class="btn btn-cancel btn-primary">Voltar</a>
				</div>
			</div>
		</div>
	</div>
</siga:pagina>
