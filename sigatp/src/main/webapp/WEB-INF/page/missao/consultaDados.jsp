<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="tptags" uri="/WEB-INF/tpTags.tld"%>

<script src="/sigatp/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<jsp:include page="../tags/calendario.jsp" />
<sigatp:erros />
<sigatp:decimal />

<c:if test="${mostrarMenu}">
	<jsp:include page="menuMissao.jsp"></jsp:include>
</c:if>

<br />
<input type="hidden" id="missaoId" name="missao" value="${missao.id}" />

<h3>Informa&ccedil;&otilde;es B&aacute;sicas</h3>
<div id ="infbasicas" class="gt-content-box gt-for-table">
 	<table id="htmlgridInformacoesBasicas" class="gt-table" >
 		<tr>
        	<th width="14%">Estado</span></th>
        	<td>${missao.estadoMissao}</td>
        </tr>
        <c:if test="${missao.estadoMissao.equals('FINALIZADA')}">
	        <tr>
	        	<th width="14%">Dist&acirc;ncia Percorrida</span></th>
	        	<td>${missao.distanciaPercorridaEmKm}</td>
	        </tr>
	        <tr>
	        	<th width="14%">Tempo</th>
	        	<td>${missao.tempoBruto}</td>
	        </tr>
	        <tr>
	        	<th width="14%">Consumo (l)</th>
	        	<td>${missao.consumoEmLitros}</td>
	        </tr>
        </c:if>
	</table>
</div>
<br>

<h3> Requisi&ccedil;&atilde;o(&otilde;es)</h3>
<div id ="gridRequisicoes" class="gt-content-box gt-for-table">
 	<table id="htmlgridRequisicoes" class="gt-table" >
    	<thead>
	    	<tr style="font-weight: bold;">
	    		<th>Sa&iacute;da prevista</th>
	    		<th>Retorno previsto</th>
	    		<th>Dados da Requisi&ccedil;&atilde;o</th>
	    		<th width="12%"></th>
			</tr>
		</thead>
		<tbody id="tbody">
			<c:forEach items="${missao.requisicoesTransporte}" var="requisicaoTransporte">
            	<tr id="row_${requisicaoTransporte.id}">
	   	    		<input type="hidden" name='requisicoesSelecionadas' readonly="readonly" value="${requisicaoTransporte.id}" class="requisicoes" />
	   	   			<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${requisicaoTransporte.dataHoraSaidaPrevista.time}" /></td>
    				<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${requisicaoTransporte.dataHoraRetornoPrevisto.time}" /></td>
	   	    		<td>
	   	    			<tptags:link texto="${requisicaoTransporte.descricaoCompleta}"
	   	    						 parteTextoLink="${requisicaoTransporte.buscarSequence()}"
	   	    						 comando="${linkTo[RequisicaoController].buscarPelaSequence(popUp,sequence)}?popUp=true&sequence=${requisicaoTransporte.buscarSequence()}">
	   	    			</tptags:link>
					</td>
	   	    		<td width="12%" >
	   	    			<c:if test="${missao.estadoMissao.equals('FINALIZADA')}">
	   	    				${null != requisicaoTransporte.getUltimoEstadoNestaMissao(missao.id) ? requisicaoTransporte.getUltimoEstadoNestaMissao(missao.id).descricao : ''}
	   	    			</c:if>
	   	    		</td>
				</tr>
			</c:forEach>
		</tbody>
     </table>
</div>
<br />

<h3>Sa&iacute;da</h3>
<div id ="infSaida" class="gt-content-box gt-for-table">
 	<table id="htmlgridSaida" class="gt-table" >
	    <tr>
	       	<th  width="14%">Data/Hora</th>
	       	<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${missao.dataHoraSaida.time}" /></td>

	       	<th  width="14%">Ve&iacute;culo</th>
			<td>${null != missao.veiculo ? missao.veiculo.dadosParaExibicao : ''}</td>

	       	<th  width="14%" >Condutor</th>
			<td colspan="3">${null != missao.condutor ? missao.condutor.dadosParaExibicao : ''}</td>
		</tr>
		<c:if test="${missao.estadoMissao.equals('INICIADA') || missao.estadoMissao.equals('FINALIZADA')}">
			<tr>
	        	<th width="14%">Od&ocirc;metro</th>
	        	<td>${missao.odometroSaidaEmKm}</td>

	        	<th width="14%">Estepe</th>
				<td align="left" style="padding: 7px 10px;">${missao.estepe}</td>

	        	<th width="14%">Avarias Aparentes</th>
				<td align="left" style="padding: 7px 10px;">${missao.avariasAparentesSaida}</td>

				<th width="14%">Limpeza</th>
				<td align="left" style="padding: 7px 10px;">${missao.limpeza}</td>
	        </tr>
	        <tr>
	        	<th width="14%">N&iacute;vel Combust&iacute;vel</th>
				<td align="left" style="padding: 7px 10px;">${missao.nivelCombustivelSaida}</td>

	        	<th width="14%">Tri&acirc;ngulo</th>
				<td align="left" style="padding: 7px 10px;">${missao.triangulos}</td>

	        	<th width="14%">Extintor</th>
				<td align="left" style="padding: 7px 10px;">${missao.extintor}</td>

				<th width="14%">Ferramentas</th>
				<td align="left" style="padding: 7px 10px;">${missao.ferramentas}</td>

	        </tr>
	        <tr>
	        	<th width="14%">Licen&ccedil;a</th>
				<td align="left" style="padding: 7px 10px;">${missao.licenca}</td>

	        	<th width="14%">Cart&atilde;o Seguro</th>
				<td align="left" style="padding: 7px 10px;">${missao.cartaoSeguro}</td>

	        	<th width="14%">Cart&atilde;o Abastecimento</th>
				<td align="left" style="padding: 7px 10px;">${missao.cartaoAbastecimento}</td>

	        	<th width="14%">Cart&atilde;o Sa&iacute;da</th>
				<td align="left" style="padding: 7px 10px;">${missao.cartaoSaida}</td>
	        </tr>
		</c:if>
	</table>
</div>
<br>

<c:if test="${missao.estadoMissao.equals('CANCELADA')}">
	<div id ="infCancelamento" class="gt-content-box gt-for-table">
		<table class="gt-table" >
			<tr>
				<th width="15%">Justificativa</th>
				<td>${missao.justificativa}</td>
			</tr>
		</table>
	</div>
</c:if>

<c:if test="${missao.estadoMissao.equals('FINALIZADA')}">
	<h3>Retorno</h3>
	<div id ="infRetorno" class="gt-content-box gt-for-table">
	 	<table id="htmlgridRetorno" class="gt-table" >
		    <tr>
	        	<th width="14%">Data/Hora</th>
	        	<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${missao.dataHoraRetorno.time}" /></td>
	        	<th colspan ="6"/>
	        </tr>
	        <tr>
	        	<th width="14%">Od&ocirc;metro</th>
	        	<td>${missao.odometroRetornoEmKm}</td>

	        	<th width="14%">Combust&iacute;vel</th>
				<td align="left" style="padding: 7px 10px;">${missao.nivelCombustivelRetorno}</td>

	        	<th width="14%">Avarias Aparente</th>
				<td align="left" style="padding: 7px 10px;">${missao.avariasAparentesRetorno}</td>

				<th width="14%"></th>
				<td></td>

	        	<th>Ocorr&ecirc;ncias</th>
	        	<td colspan="7" style="font-family: monospace; white-space: pre;">${missao.ocorrencias}</td>

	        	<th>Itiner&aacute;rio Completo</th>
	        	<td colspan="7" style="font-family: monospace; white-space: pre;">${missao.itinerarioCompleto}</td>
			</tr>
		</table>
	</div>
</c:if>