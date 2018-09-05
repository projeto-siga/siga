<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<script src="/sigatp/public/javascripts/jquery/jquery-1.6.4.min.js" type="text/javascript"></script>
		<script src="/sigatp/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
	
		<style type="text/css">
			@media screen {
				#header {
					line-height: 10px;
					height: 90px;
				}
				
				.conteudos {
					line-height: 25px;
				}
			}
			
			@media print {
				#header {
					line-height: 12px;
					height: 50%;
					width:90%;
				}
				
				.conteudos {
					line-height: 15px;
					width:90%;
				}
			}

			@media screen, print {
				body { 
					font-family: Arial, Helvetica, sans-serif; 
				}
				
				th {
					text-align: right;
					vertical-align: top;
				}
				
				img {
					float: left;
					padding: 10px;
				}	
				
				#header {
					vertical-align: middle;
					border: 1px solid;
					padding: 10px;
				}
	
				.destaque {
					background-color: #9CF;
				}
				
				.destaque2 {
					background-color: #990;
				}
				
				.conteudos {
					border-top-style: solid;
					border-right-style: solid;
					border-bottom-style: solid;
					border-left-style: solid;
					border: 1px solid #666;	
					padding: 10px;
				}
			}
		</style>
		
		<script type="text/javascript">
			$(document).ready(function() {
				var valor = Array(52).join("_").concat("<br/>");

				$(".linhasPreencher").each(function() {
					$(this).html(Array(4).join(valor));
				});
			});
		</script>
	</head>
	<body>
		<div id="header">
			<img width="80px" alt="" src="data:image/png;base64,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" />
			<h2>PODER JUDICIÁRIO</h2>
			<h2>JUSTIÇA FEDERAL DA 2ª REGIÃO</h2>
		</div>
		<br/>
		<div>
			<div class="conteudos"> 
				<c:if test="${missao != null}">
					<table>
				    	<tr>
				    		<th>Missão Nº:</th><td>${missao.sequence}</td>
				    	</tr>
				    	<tr>	
					    	<th>Data:</th><td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${missao.dataHora.time}" /></td>
					    </tr>
						<tr>    
							<th>Veículo:</th><td>${missao.veiculo.placa}</td>
						</tr>
				    	<tr>	
					    	<th>Condutor:</th><td>${missao.condutor.nome}</td>
					    </tr>
					    <tr class="destaque">	
					    	<th>Saída:</th><td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${missao.dataHoraSaida.time}" /></td>
					    </tr>
					    <tr class="destaque">	
					    	<th>Odômetro:</th><td>____________</td>
					    </tr>
					    <tr class="destaque">	
					    	<th>Estepe:</th><td>Sim / Não</td>
					    </tr>
					    <tr class="destaque">
					    	<th>Avarias Aparentes:</th><td>Sim / Não<td></td>
					    </tr>
					    <tr class="destaque">
					    	<th>Limpeza:</th><td>Sim / Não</td>
					    </tr>
					    <tr class="destaque">
					    	<th>Combustível:</th><td>Sim / Não</td>
					    </tr>
					    <tr class="destaque">	
					    	<th>Triângulos:</th><td>Sim / Não</td>
					    </tr>
					    <tr class="destaque">
					    	<th>Extintor:</th><td>Sim / Não</td>
					    </tr>
					    <tr class="destaque">
					    	<th>Ferramentas:</th><td>Sim / Não</td>
					    </tr>
					    <tr class="destaque">
					    	<th>Licença:</th><td>Sim / Não</td>
					    </tr>
					    <tr class="destaque">
					    	<th>Cartão do Seguro:</th><td>Sim / Não</td>
					    </tr>
					    <tr class="destaque">
					    	<th>Cartão de Abast.:</th><td>Sim / Não</td>
					    </tr>
					    <tr class="destaque">
					    	<th>Cartão de Saída:</th><td>Sim / Não</td>
					    </tr>
						<tr class="destaque2">	
					    	<th>Retorno:</th><td>___/___/______ ___:___</td>
					    </tr>
					    <tr class="destaque2">	
					    	<th>Odômetro:</th><td>____________</td>
					    </tr>
					    <tr class="destaque2">
					    	<th>Combustível:</th><td>____________</td>
					    </tr>
					    <tr class="destaque2">
					    	<th>Avarias Aparentes:</th><td>Sim / Não</td>
					    </tr>
					    <tr>	
					    	<th>Distância/Tempo:</th><td>______Km / ___:___</td>
					    </tr>
					    <tr>	
					    	<th>Consumo:</th><td>______litros</td>
					    </tr>
					    <tr>	
					    	<th>Ocorrências:</th>
					    	<td class="linhasPreencher">
					    	</td>
					    </tr>
					    <tr>	
					    	<th>Itinerário Completo:</th>
					    	<td class="linhasPreencher">
					    	</td>
					    </tr>
					</table>   
				</c:if>
			</div>
			<br />
			<c:forEach items="${missao.requisicoesTransporte}" var="requisicao">
				<div class="conteudos">
					<table>
				    	<tr>
				    		<th>Requisição Nº:</th><td>${requisicao.buscarSequence()}</td>
				    	</tr>
				    	<tr>	
					    	<th>Tipo:</th><td>${requisicao.tipoRequisicao.toString()}</td>
					    </tr>
				    	<tr>	
					    	<th>Data:</th><td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${missao.dataHora.time}" /></td>
					    </tr>
				    	<tr>	
					    	<th>Saída Prevista:</th><td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${missao.dataHoraSaida.time}" /></td>
					    </tr>
					    <tr>    
							<th>Retorno Previsto:</th><td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${missao.dataHoraRetorno.time}" /></td>
						</tr>
						<tr>    
							<th>Passageiros:</th><td>${requisicao.passageiros}</td>
						</tr>
						<tr>    
							<th>Finalidade:</th><td>${requisicao.finalidade}</td>
						</tr>
						<tr>    
							<th>Itinerarios:</th><td>${requisicao.itinerarios}</td>
						</tr>
					</table>
				</div>
				<br />
			</c:forEach>
		</div>
		<div id="footer">
		</div>
	</body>
</html>
