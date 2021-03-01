<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<link rel="stylesheet"
	href="/siga/css/selectpicker/bootstrap-select.min.css" type="text/css"
	media="screen, projection" />

<siga:pagina titulo="Editar Marcador">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Dados do Marcador</h5>
			</div>
			<div class="card-body">
				<form name="frmCriaMarcador" action="/siga/app/marcador/gravar"
					method="post">
					<input type="hidden" id="idMarcador" name="id"
						value="${marcador.id}" />
					<div class="form-group row">
						<div class="col col-12 col-md-6 col-lg-3">
							<div class="form-group">
								<label for="descricao">Nome</label> <input name="descricao"
									id="descricao" class="form-control"
									value="${marcador.descrMarcador}" />
							</div>
						</div>
						<div class="col col-12 col-md-6 col-lg-3">
							<div id="idIconeGroup" class="form-group">
								<label for="idIcone">Ícone</label> <select
									class="form-control selectpicker border-light" id="idIcone"
									name="idIcone" value="${idIcone}" data-show-content="true"
									data-style="btn-outline-secondary">
									<c:forEach items="${listaIcones}" var="item">
										<option value="${item.name()}"
											${item eq marcador.idIcone ? 'selected' : ''}
											data-icon="${item.codigoFontAwesome}">
											${item.descricao}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col col-12 col-md-6 col-lg-3">
							<div class="form-group justify-content-end">
								<label for="idIcone">Cor</label>
								<button id="btnEscolherCor" type="button"
									class="form-control w-100" data-toggle="modal"
									data-target="#modalCor">
									<span id="idCorAmostra"
										style="color: ${(empty marcador.idCor.descricao)? listaCores[0].descricao : marcador.idCor.descricao} !important">
										&#11044; </span> Escolha a cor do ícone
								</button>
								<div id="modalCor" class="modal fade" tabindex="-1"
									role="dialog" aria-labelledby="Cor" aria-hidden="true">
									<div class="modal-dialog modal-sm">
										<div class="modal-content">
											<div class="modal-header">
												<h6 class="modal-title">Escolha a cor do ícone</h6>
											</div>
											<div class="form-group pb-3">
												<c:forEach items="${listaCores}" var="item">
													<label class="radio-image"> <input type="radio"
														id="idCor${item.id}" name="idCor"
														${item eq marcador.idCor 
																|| (empty marcador and item.id == 1) ? 'checked="checked"' : ''}
														onclick="mudaCorAmostra('#${item.descricao}');"
														value="${item.name()}"> <span class="checkmark"
														style="background-color: ${item.descricao}"></span>
													</label>
												</c:forEach>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col col-12 col-md-6 col-lg-3">
							<div class="form-group" id="grupo">
								<label for="idGrupo">Grupo</label> <select
									class="form-control" id="idGrupo" name="idGrupo"
									value="${idGrupo}">
									<c:forEach items="${listaGrupos}" var="item">
										<option value="${item.name()}"
											${(item eq marcador.idGrupo) or (empty marcador and item eq 'OUTROS') ? 'selected' : ''}>
											${item.nome}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col col-12">
							<div class="form-group">
								<label for="descrDetalhada">Descrição</label> <input
									name="descrDetalhada" id="descrDetalhada" class="form-control"
									value="${marcador.descrDetalhada}" />
							</div>
						</div>
						<div class="col col-12">
							<div class="form-group" id="finalidade">
								<label for="idFinalidade">Finalidade do Marcador</label> <select
									class="form-control" id="idFinalidade" name="idFinalidade"
									value="${idFinalidade}">
									<c:forEach items="${listaFinalidade}" var="item">
										<option value="${item.name()}"
											${item eq marcador.idFinalidade ? 'selected' : ''}>
											${item.nome} - ${item.descricao}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<input type="submit" type="button"
							class="btn btn-primary  btn-salvar" value="OK"> <a
							href="/siga/app/marcador/listar" class="btn btn-cancel btn-light">Cancela</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>
<script type="text/javascript"
	src="/siga/javascript/selectpicker/bootstrap-select.min.js"></script>
<script>
	function mudaCorAmostra(cor) {
		$("#idCorAmostra").css("color", cor);
		$('#modalCor').modal('hide');
	}
</script>