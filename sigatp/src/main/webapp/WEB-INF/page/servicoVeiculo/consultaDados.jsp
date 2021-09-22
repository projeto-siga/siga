<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib prefix="tptags" uri="/WEB-INF/tpTags.tld"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<sigatp:decimal/>
<jsp:include page="../tags/calendario.jsp" />

<script src="/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>


<c:if test="${mostrarMenu}">
	<jsp:include page="menuServicoVeiculo.jsp" />
</c:if>
<br />

		<input type="hidden" id="servicoId" name="servico.id" value="${servico.id}" />

	<h3>Informa&ccedil;&otilde;es B&aacute;sicas</h3>
	<sigatp:erros />
	<div id ="infbasicas" class="gt-content-box gt-for-table">
	 	<table id="htmlgridInformacoesBasicas" class="gt-table" >
	 		<tr>
	        	<th width="14%">Estado</span></th>
	        	<td>${servico.situacaoServico}</td>
	        </tr>

		    <c:if test="${servico.situacaoServico.equals('INICIADO') || servico.situacaoServico.equals('REALIZADO')}">
		 		<tr>
		        	<th width="14%">Data Hora In&iacute;cio</span></th>
		        	<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${servico.dataHoraInicio.time}" /></td>
		        </tr>
		    </c:if>

		    <c:if test="${servico.situacaoServico.equals('REALIZADO')}">
		 		<tr>
		        	<th width="14%">Data Hora Fim</span></th>
		        	<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${servico.dataHoraFim.time}" /></td>
		        </tr>
		    </c:if>

      		<tr>
	        	<th width="14%">Tipo de Servi&ccedil;o</span></th>
	        	<td>${servico.tiposDeServico}</td>
	        </tr>

      		<tr>
	        	<th width="14%">Ve&iacute;culo</span></th>
	        	<td>
				<tptags:link texto="${servico.veiculo.dadosParaExibicao}"
  				             parteTextoLink="${servico.veiculo.dadosParaExibicao}"
  			                 comando="${linkTo[VeiculoController].buscarPeloId(servico.veiculo.id)}">
  				</tptags:link>
    			</td>
	        </tr>

 		    <c:if test="${servico.requisicaoTransporte != null}">
	            <tr>
		        	<th width="14%">Requisi&ccedil;&atilde;o Transporte</span></th>
					<td>
					<tptags:link texto="${servico.requisicaoTransporte.buscarSequence()}"
  	 					         parteTextoLink="${servico.requisicaoTransporte.buscarSequence()}"
  						         comando="${linkTo[RequisicaoController].buscarPelaSequence(popUp,sequence)}?popUp=true&sequence=${requisicaoTransporte.buscarSequence()}">
										   	    				
  					</tptags:link>
					</td>
				</tr>
			</c:if>

		    <c:if test="${servico.situacaoServico.equals('CANCELADO')}">
			    <tr>
			    	<th width="14%">Motivo do Cancelamento</span></th>
			    	<td>${servico != null ? servico.motivoCancelamento : ""}</td>
			    </tr>
		    </c:if>

		</table>
	</div>
	<br>
