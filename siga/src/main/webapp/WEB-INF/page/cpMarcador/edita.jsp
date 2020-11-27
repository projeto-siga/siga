<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<link rel="stylesheet" href="/siga/css/selectpicker/bootstrap-select.min.css" type="text/css" media="screen, projection"/>

<siga:pagina titulo="Editar Marcador">
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
					<input type="hidden" id="idMarcador" name="id" value="${marcador.id}" />
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
										<option value="${item.name()}"
											${item eq marcador.cpTipoMarcador ? 'selected' : ''}>
											${item.descricao}</option>
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
										<option value="${item.name()}"
											${item eq marcador.idTpAplicacao ? 'selected' : ''}>
											${item.descricao}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col col-6">
							<div id="idIconeGroup" class="form-group">
								<label for="idIcone">Ícone</label> <select
									class="form-control selectpicker border-light" id="idIcone" name="idIcone"
									value="${idIcone}" data-show-content="true" data-style="btn-outline-secondary">
									<c:forEach items="${listaIcones}" var="item">
										<option value="${item.name()}"  
											${item eq marcador.idIcone ? 'selected' : ''} 
											data-icon="${item.codigoFontAwesome}">  
											${item.descricao}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col col-6">
							<div class="form-group justify-content-end">
								<button id="btnEscolherCor" type="button" class="btn btn-sm btn-outline-secondary border-light mt-4" 
										data-toggle="modal" data-target="#modalCor">
									<span id="idCorAmostra" class="btn btn-sm btn-circle" 
										style="background-color: ${(empty marcador.idCor.descricao)? listaCores[0].descricao : marcador.idCor.descricao} !important">
										</span><div class="float-right mt-1 ml-2">Escolha a cor do ícone</div></button>
								<div id="modalCor" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="Cor" aria-hidden="true">
									<div class="modal-dialog modal-sm">
										<div class="modal-content">
											<div class="modal-header">
												<h6 class="modal-title">Escolha a cor do ícone</h6>
											</div>
											<div class="form-group pb-3">		
												<c:forEach items="${listaCores}" var="item">
													<label class="radio-image">
														<input type="radio" id="idCor${item.id}" name="idCor"
															${item eq marcador.idCor 
																|| (empty marcador and item.id == 1) ? 'checked="checked"' : ''}
															onclick="mudaCorAmostra('#${item.descricao}');"
															value="${item.name()}">
															<span class="checkmark" style="background-color: ${item.descricao}"></span>
													</label>
												</c:forEach>
											</div>
										</div>
									</div>
								</div>
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
										<option value="${item.name()}"
											${item eq marcador.idTpDataPlanejada ? 'selected' : ''}>
											${item.descricao}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col">
							<div class="form-group">
								<label for="idTpDataLimite">Data Limite</label> <select
									class="form-control" id="idTpDataLimite"
									name="idTpDataLimite" value="${idTpDataLimite}">
									<c:forEach items="${listaTipoDataLimite}" var="item">
										<option value="${item.name()}"
											${item eq marcador.idTpDataLimite ? 'selected' : ''}>
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
										<option value="${item.name()}"
											${item eq marcador.idTpExibicao ? 'selected' : ''}>
											${item.descricao}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="col">
							<div class="form-group">
								<label for="idTpTexto">Texto</label> <select
									class="form-control" id="idTpTexto"
									name="idTpTexto" value="${idTpTexto}">
									<c:forEach items="${listaTipoTexto}" var="item">
										<option value="${item.name()}"
											${item eq marcador.idTpTexto ? 'selected' : ''}>
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
										<option value="${item.name()}"
											${item eq marcador.idTpInteressado ? 'selected' : ''}>
											${item.descricao}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<input type="submit" type="button" class="btn btn-primary  btn-salvar" value="OK">
						<a href="/siga/app/marcador/listar" class="btn btn-cancel btn-light">Cancela</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
<script type="text/javascript" src="/siga/javascript/selectpicker/bootstrap-select.min.js"></script>
<script>
	function mudaCorAmostra(cor) {
		$("#idCorAmostra").css("background-color", cor);	
		$('#modalCor').modal('hide');
	}
</script>