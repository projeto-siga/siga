<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="table-responsive" id="etapas">
	<script>
		function clica(obj, force){
			var jObj = $(obj);

			//Se clicou no + geral...
			if (obj.id == 'sinal' ){
				$("a[id^=sinal]").not("#sinal").each(function(o){
					clica(this, jObj.html());
				})
			}
			
			if ((!force && jObj.html() == '+') || force == '+'){
				jObj.parent().parent().next().show();
				jObj.html('-');
			} else {
				jObj.parent().parent().next().hide();
				jObj.html('+');
			}
		}
	</script>
	<table border="0" width="100%" class="table">
		<thead>
			<tr>
				<th><a href="" onclick="clica(this); return false;" style="text-decoration: none;" id="sinal">+</a></th>
				<th>Etapa</th>
				<c:if test="${todoOContexto}">
					<th>Solicita&ccedil;&atilde;o</th>
				</c:if>
				<th>Equipe</th>
				<th>Início</th>
				<th>Fim</th>
				<th>Decorrido</th>
				<th>Restante</th>
			</tr>
		</thead>

		<tbody>
			<c:choose>
				<c:when test="${not empty etapas}">
					<c:forEach items="${etapas}" var="etapa">
						<tr>
							<td><a href="" onclick="clica(this); return false;" style="text-decoration: none;" id="sinal${etapa.id}">+</a></td>
							<td>${etapa.descricao}</td>
							<c:if test="${todoOContexto}">
								<td>
								     <c:if test="${etapa.solicitacao.filha}">
									     <a style="color: #365b6d;" 
									        href="${linkTo[SolicitacaoController].exibir(etapa.solicitacao.siglaCompacta)}" 
									        style="text-decoration: none">
											${etapa.solicitacao.numSequenciaString} </a>
								     </c:if>
								</td>
							</c:if>
							<td>${not empty etapa.lotaResponsavel ? etapa.lotaResponsavel : ''}</td>
							<td>${etapa.inicioString}</td>
							<td>${etapa.fimString}</td>
							<td class="crono resumido decorrido ${etapa.ativo ? 'ligado' : 'desligado'}">
								<nobr><span class="descrValor"></span><span class="valor">${etapa.decorridoEmSegundos}</span></nobr>
							</td>
							<td class="crono resumido restante ${etapa.ativo ? 'ligado' : 'desligado'}">
								<nobr><span class="descrValor"></span><span class="valor">${etapa.restanteEmSegundos}</span></nobr>
							</td>
						</tr>
						<tr style="display: none">
							<c:set var="haDetalhes" value="false" />
							<td></td>
							<td></td>
							<td colspan="6">
								<c:if test="${not empty etapa.paramAcordo}">
									<c:set var="haDetalhes" value="true" />
									<p><b>Acordo:</b> ${etapa.paramAcordo.acordo.nomeAcordo}</p>
								</c:if>
								<c:if test="${not empty etapa.item}">
									<c:set var="haDetalhes" value="true" />
									<p><b>Item de Configuração:</b> ${etapa.item.sigla} - ${etapa.item.descricao}</p>
								</c:if>
								<c:if test="${not empty etapa.acao}">
									<c:set var="haDetalhes" value="true" />
									<p><b>Ação:</b> ${etapa.acao.sigla} - ${etapa.acao.descricao}</p>
								</c:if>
								<c:if test="${not empty etapa.prioridade}">
									<c:set var="haDetalhes" value="true" />
									<p><b>Prioridade Técnica:</b> ${etapa.prioridade.descPrioridade}</p>
								</c:if>
							</td>
							<c:if test="${not haDetalhes}">
								<script>$("#sinal${etapa.id}").remove();</script>
							</c:if>
								
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td align="center" colspan="10">N&atilde;o h&aacute;
							atendimentos nesse modo de
							exibi&ccedil;&atilde;o</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
</div>
<script>
	function exibirMovs(){
		$("#movs").show();
		$("#maisDetalhes").show();
		$("#todoOContexto").show();
		$("#etapas").hide();
	}
	function exibirEtapas(){
		$("#etapas").show();
		$("#movs").hide();
		$("#maisDetalhes").hide();
		$("#todoOContexto").show();
	}
	$("#radioMovs").trigger("click");
</script>