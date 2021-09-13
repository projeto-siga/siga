<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/"%>

<siga:pagina titulo="Transportes">

	<sigatp:toggleTexto totalCaracteres="50" />
	<input type="hidden" id="txtToggleTexto">

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${veiculo.dadosParaExibicao}</h2>
			<h3>Relat&oacute;rios Di&aacute;rios</h3>
			<jsp:include page="../veiculo/menu.jsp" />
			
			<c:choose>
				<c:when test="${relatoriosDiarios.size() > 0}">
					<div class="gt-content-box gt-for-table">
					 	<table id="htmlgrid" class="gt-table tablesorter">
					    	<thead>
						    	<tr style="font-weight: bold;">
						    		<th width="1%" class="noSort"></th>
			   						<th width="1%" class="noSort"></th>
						    	    <th>Data</th>
							   		<th>Od&ocirc;metro (Km)</th>
							   		<th>N&iacute;vel de Combust&iacute;vel</th>
							   		<th>Equip. Obrig.</th>
							   		<th>Cart&otilde;es</th>
							   		<th width="25%">Observa&ccedil;&atilde;o</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${relatoriosDiarios}" var="relatorioDiario">
									<tr>
							    		<td>
											<sigatp:formatarColuna operacao="editar" href="${linkTo[RelatorioDiarioController].editar(relatorioDiario.id)}" titulo="relat&oacute;rio di&aacute;rio" />
							    		</td>
							    		<td>
											<sigatp:formatarColuna operacao="excluir" href="${linkTo[RelatorioDiarioController].excluir(relatorioDiario.id)}" 
											onclick="javascript:return confirm('Tem certeza de que deseja excluir este Relat&oacute;rio Di&aacute;rio?');" titulo="relat&oacute;rio di&aacute;rio" /> 
							    		</td>
							    	    <td><fmt:formatDate value="${relatorioDiario.data.time}" pattern="dd/MM/yyyy"/></td>
							    		<td><c:out value="${relatorioDiario.odometroEmKm}"/></td>
							    		<td><c:out value="${relatorioDiario.nivelDeCombustivel.descricao}"/></td>
							    		<td><c:out value="${relatorioDiario.equipamentoObrigatorio.descricao}"/></td>
							    		<td><c:out value="${relatorioDiario.cartoes.descricao}"/></td>
							    		<td>
										    <div style="display: none;" class="toggleTexto">
								    			<c:out value="${relatorioDiario.observacao}" />
							    			</div>
							    			<jsp:include page="../tags/observacao.jsp" />
							    		</td>
									 </tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<sigatp:paginacao />    
				</c:when>
				<c:otherwise>
					<br/><h3>N&atilde;o existem relat&oacute;rios di&aacute;rios cadastrados para este ve&iacute;culo.</h3>
				</c:otherwise>
			</c:choose>
		
			<div class="gt-table-buttons">
				<a href="${linkTo[RelatorioDiarioController].incluir(veiculo.id)}" class="gt-btn-medium gt-btn-left"><fmt:message key="views.botoes.incluir"/></a>
			</div>
		</div>
	</div>
</siga:pagina>