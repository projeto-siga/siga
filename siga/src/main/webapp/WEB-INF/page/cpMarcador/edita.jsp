<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<link rel="stylesheet" href="/siga/css/selectpicker/bootstrap-select.min.css" type="text/css" media="screen, projection"/>

<siga:pagina titulo="Forma">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Dados do Marcador</h5>
			</div>
			<div class="card-body">
				<form name="frmCriaMarcador"
					action="/siga/app/marcador/gravar"
					method="post">
					<div class="modal-body">
						<input type="hidden" id="idMarcador" name="id" value="${marcador.id}" />
						<div class="form-group">
							<div class="form-group">
								<div class="form-group row">
									<div class="col col-12">
										<div class="form-group">
											<label for="descricao">Nome</label> <input name="descricao"
												id="descricao" class="form-control" value="${marcador.descrMarcador}"/>
										</div>
									</div>
									<div class="col col-12">
										<div class="form-group">
											<label for="descrDetalhada">Descrição</label> <input
												name="descrDetalhada" id="descrDetalhada"
												class="form-control" value="${marcador.descrDetalhada}" />
										</div>
									</div>
									<div class="col col-6">
										<div class="form-group" id="tipoMarcador">
											<label for="idTpMarcador">Tipo de Marcador</label> <select
												class="form-control" id="idTpMarcador" name="idTpMarcador"
												value="${idTpMarcador}">
												<c:forEach items="${listaTipoMarcador}" var="item">
													<option value="${item.idTpMarcador}"
														${item.idTpMarcador eq marcador.cpTipoMarcador.idTpMarcador ? 'selected' : ''}>
														${item.descrTipoMarcador}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col col-6">
										<div class="form-group">
											<label for="idTpAplicacao">Aplicação de Marcador</label> <select
												class="form-control" id="idTpAplicacao" name="idTpAplicacao"
												value="${idTpAplicacao}">
												<c:forEach items="${listaTipoAplicacao}" var="item">
													<option value="${item.id}"
														${item == marcador.idTpAplicacao ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col col-6">
										<div class="form-group">
											<label for="idCor">Cor</label> <select
												class="form-control" id="idCor" name="idCor"
												value="${idCor.id}">
												<c:forEach items="${listaCores}" var="item">
													<option value="${item.id}"
														style="background-color: ${item.descricao}"
														${item.id == marcador.idCor.id ? 'selected' : ''}>
													</option>
												</c:forEach>
											</select>
										</div>
									</div>

									<div class="col col-6">
										<div class="form-group">
											<label for="idIcone">Ícone</label> <select
												class="form-control selectpicker" id="idIcone" name="idIcone"
												value="${idIcone.id}" data-show-content="true">
												<c:forEach items="${listaIcones}" var="item">
													<option value="${item.id}"
														${item.id == marcador.idIcone.id ? 'selected' : ''} 
														data-icon="${item.codigoFontAwesome}">  ${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>

								</div>
								<div class="form-group row">
									<div class="col">
										<div class="form-group">
											<label for="idTpDataPlanejada">Data Planejada</label> <select
												class="form-control" id="idTpDataPlanejada"
												name="idTpDataPlanejada" value="${idTpDataPlanejada}">
												<c:forEach items="${listaTipoDataPlanejada}" var="item">
													<option value="${item.id}"
														${item == marcador.idTpDataPlanejada ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col">
										<div class="form-group">
											<label for="idTpDataLimite">Data Limite - ${item }</label> <select
												class="form-control" id="idTpDataLimite"
												name="idTpDataLimite" value="${idTpDataLimite}">
												<c:forEach items="${listaTipoDataLimite}" var="item">
													<option value="${item.id}"
														${item == marcador.idTpDataLimite ? 'selected' : ''}>
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
														${item == marcador.idTpExibicao ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>

									<div class="col">
										<div class="form-group">
											<label for="idTpTexto">Justificativa</label> <select
												class="form-control" id="idTpTexto"
												name="idTpTexto" value="${idTpTexto}">
												<c:forEach items="${listaTipoJustificativa}" var="item">
													<option value="${item.id}"
														${item == marcador.idTpTexto ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>

									<div class="col">
										<div class="form-group">
											<label for="idTpInteressado">Interessado</label> <select
												class="form-control" id="idTpInteressado"
												name="idTpInteressado" value="${idTpInteressado}">
												<c:forEach items="${listaTipoInteressado}" var="item">
													<option value="${item.id}"
														${item == marcador.idTpInteressado ? 'selected' : ''}>
														${item.descricao}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="form-group">
						<input type="submit" type="button" class="btn btn-primary  btn-salvar" value="OK">
						<a href="/siga/app/marcador/listar" class="btn btn-cancel">Cancela</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
<script type="text/javascript" src="/siga/javascript/selectpicker/bootstrap-select.min.js"></script>