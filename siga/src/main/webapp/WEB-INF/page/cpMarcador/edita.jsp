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
	
	<div class="modal fade" id="editarModal" tabindex="-1" role="dialog"
		aria-labelledby="editarModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="editarModalLabel"></h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<form name="frmCriaMarcador"
					action="${request.contextPath}/siga/app/marcador/gravar"
					method="post">
					<div class="modal-body">
						<input type="hidden" id="idMarcador" name="id" value="" />
						<div class="form-group">
							<div class="form-group">
								<div class="form-group row">
									<div class="col">
										<label for="descricao">Nome</label> <input name="descricao"
											id="descricao" class="form-control" />
									</div>
								</div>
								<div class="form-group row">
									<div class="col">
										<label for="descrDetalhada">Descrição</label> <input name="descrDetalhada"
											id="descrDetalhada" class="form-control" />
									</div>
								</div>
								<div class="form-group row">
									<div class="col">
										<div class="form-group" id="tipoMarcador">
											<label for="idTpMarcador">Tipo de Marcador</label> <select
												class="form-control" id="idTpMarcador" name="idTpMarcador"
												value="${idTpMarcador}">
												<c:forEach items="${listaTipoMarcador}" var="item">
													<option value="${item.idTpMarcador}"
														${item == idTpMarcador ? 'selected' : ''}>
														${item.descrTipoMarcador}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col">
										<div class="form-group">
											<label for="idTpAplicacao">Aplicação de Marcador</label> <select
												class="form-control" id="idTpAplicacao" name="idTpAplicacao"
												value="${idTpAplicacao}">
												<c:forEach items="${listaTipoAplicacao}" var="item">
													<option value="${item.id}"
														${item.id == idTpAplicacao ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
								<div class="form-group row">
									<label for="cor" class="col-form-label">Cor</label>
									<div class="card pt-2">
										<div class="imageRadio">
											<c:set var="count" value="0" scope="page" />
											<c:forEach var="item" items="${listaCores}">
												<input type="radio" id="cor${item}" name="cor"
													value="${item}" />
												<label for="cor${item}"><i class="fas fa-tag"
													style="color: #${item}"></i></label>
												<c:set var="count" value="${count + 1}" scope="page" />
												<c:if test="${count == 8}">
													<br />
													<c:set var="count" value="0" scope="page" />
												</c:if>
											</c:forEach>
										</div>
									</div>
								</div>
								<div class="form-group row">
									<div class="col">
										<label for="icone">Ícone</label> <select name="icone"
											id="icone" class="form-control">
											<option value="1">Pessoa</option>
											<option value="2">Etiqueta</option>
											<option value="3">Bomba</option>
										</select>
									</div>
								</div>
								<div class="form-group row">
									<div class="col">
										<div class="form-group">
											<label for="idTpDataPlanejada">Data Planejada</label> <select
												class="form-control" id="idTpDataPlanejada" name="idTpDataPlanejada"
												value="${idTpDataPlanejada}">
												<c:forEach items="${listaTipoDataPlanejada}" var="item">
													<option value="${item.id}"
														${item.id == idTpDataPlanejada ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col">
										<div class="form-group">
											<label for="idTpDataLimite">Data Limite - ${item }</label> <select
												class="form-control" id="idTpDataLimite" name="idTpDataLimite"
												value="${idTpDataLimite}">
												<c:forEach items="${listaTipoDataLimite}" var="item">
													<option value="${item.id}"
														${item.id == idTpDataLimite ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
								<div class="form-group row">
									<div class="col">
										<div class="form-group">
											<label for="idTpExibicao">Opções de Exibição</label> <select
												class="form-control" id="idTpExibicao" name="idTpExibicao"
												value="${idTpExibicao}">
												<c:forEach items="${listaTipoExibicao}" var="item">
													<option value="${item.id}"
														${item.id == idTpExibicao ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									
									<div class="col">
										<div class="form-group">
											<label for="idTpJustificativa">Justificativa</label> <select
												class="form-control" id="idTpJustificativa" name="idTpJustificativa"
												value="${idTpJustificativa}">
												<c:forEach items="${listaTipoJustificativa}" var="item">
													<option value="${item.id}"
														${item.id == idTpJustificativa ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
	
								<label for="observacoes">Opções de Definição do
									Interessado</label>
								<div class="form-group row">
									<div class="col">
										<div class="form-group">
											<label for="idTpInteressado">Interessado</label> <select
												class="form-control" id="idTpInteressado" name="idTpInteressado"
												value="${idTpInteressado}">
												<c:forEach items="${listaTipoInteressado}" var="item">
													<option value="${item.id}"
														${item.id == idTpInteressado ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">Cancelar</button>
						<input type="submit" class="btn btn-primary" value="Salvar" />
					</div>
				</form>
			</div>
		</div>
	</div>
