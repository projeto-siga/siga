<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<script type="text/javascript">
	var urlSalvar = '<c:out value="${linkTo[EscalaDeTrabalhoController].salvar}" />';
	var urlFinalizar = '<c:out value="${linkTo[EscalaDeTrabalhoController].finalizar}" />';
	var urlCancelar = '<c:out value="${linkTo[CondutorController].listar}" />';
</script>

<form id="formEscalasDeTrabalho" method="post" onsubmit="return false;" enctype="multipart/form-data">
	<div class="gt-content-box gt-for-table">
		<h3>&nbsp;&nbsp;Escala de Trabalho Vigente</h3>
		<input type="hidden" name="escalaDeTrabalho.dataVigenciaInicio" value='<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${escala.dataVigenciaInicio.time}"/>' />
		<input type="hidden" name="escalaDeTrabalho" value="${escala.id}" />
		<table id="htmlgridDiasDeTrabalho" class="gt-table">
			<tbody id="tbody">
				<c:forEach items="${escala.diasDeTrabalho}" var="diaDeTrabalho">
					<tr>
						<th width="15%" class="obrigatorio">Dia In&iacute;cio / Fim </th>
						<td>
							<select name="diaEntrada" >
								<c:forEach items="${diaSemana.getValues()}" var="dia">
									<option value="${dia}" ${dia == diaDeTrabalho.getDiaEntrada() ? 'selected' : ''} ${dia == diaDeTrabalho.getDiaEntrada() ? 'class="selecionado"' : ''}>${dia.nomeAbreviado}</option>
								</c:forEach>
							</select>
							<input type="text" name="horaEntrada" value='<fmt:formatDate pattern="HH:mm" value="${diaDeTrabalho.getHoraEntrada().time}"/>' size="8" class="hora selecionado" />
							at&eacute;
							<select name="diaSaida">
								<c:forEach items="${diaSemana.getValues()}" var="dia">
									<option value="${dia}" ${dia == diaDeTrabalho.getDiaEntrada() ? 'selected' : ''} ${dia == diaDeTrabalho.getDiaEntrada() ? 'class="selecionado"' : ''}>${dia.nomeAbreviado}</option>
								</c:forEach>
							</select>
							<input type="text" name="horaSaida" value='<fmt:formatDate pattern="HH:mm" value="${diaDeTrabalho.horaSaida.time}"/>' size="8" class="hora selecionado" />
							<input type="hidden" name="id" class="selecionado" value="${diaDeTrabalho.id}" />
						</td>
						<td width="8%"><a class="linkExcluir" name="linkExcluirSelecionados" onclick="escalas.apagaLinha(this)" style="display: inline" href="#"><fmt:message key="views.botoes.excluir" /></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div id="btngridDiasDeTrabalho" class="gt-table-buttons">
			<input type="button" id="btn-Incluir-DiasDeTrabalho" value='<fmt:message key="views.botoes.incluirNovoDia" />' class="gt-btn-medium gt-btn-left btnSelecao" />
		</div>
		<input type="hidden" name="escalaDeTrabalho.condutor.id" value="${escala.condutor.id}" />
	</div>
	<br />
	<span style="color: red; font-weight: bolder; font-size: smaller;"><fmt:message key="views.erro.preenchimentoObrigatorio" /></span>
	<div class="gt-table-buttons">
		<input type="button" id="salvar" value='<fmt:message key="views.botoes.salvar" />' class="gt-btn-medium gt-btn-left" />
		<input type="button" id="finalizar" value='<fmt:message key="views.botoes.finalizar" />' class="gt-btn-medium gt-btn-left" />
		<input type="button" id="cancelar" value='<fmt:message key="views.botoes.cancelar" />' class="gt-btn-medium gt-btn-left" />
	</div>
</form>

<div id="rowDiasDeTrabalho" style="display: none">
	<select name="diaEntrada" class="naoSelecionado">
		<c:forEach items="${diaSemana.getValues()}" var="dia">
			<option value="${dia}" ${dia == "SEGUNDA" ? 'selected' : ''}>${dia.nomeAbreviado}</option>
		</c:forEach>
	</select>
	<input type="text" name="horaEntrada" value="11:00" size="8" class="hora naoSelecionado" /> 
	at&eacute;
	<select name="diaSaida" class="naoSelecionado">
		<c:forEach items="${diaSemana.getValues()}" var="dia">
			<option value="${dia}" ${dia == "SEGUNDA" ? 'selected' : ''}>${dia.nomeAbreviado}</option>
		</c:forEach>
	</select>
	<input type="text" name="horaSaida" value="19:00" size="8" class="hora naoSelecionado" />
	<input type="hidden" name="id" class="naoSelecionado" value="${escala.id}" />
</div>

<jsp:include page="escalas.jsp"/>
<jsp:include page="../tags/calendario.jsp"/>
