<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>			
		<p><b>Quanto ao profissional e aos serviços prestados:</b></p>
		<mod:grupo titulo="Os funcionários estão devidamente identificados (com crachá)? ">
			<mod:radio titulo="Sim." var="comIdentif" valor="1" marcado="Sim"
							reler="ajax" idAjax="comIdentifAjax" />
		
			<mod:grupo largura="7">
				<mod:radio titulo="Não." var="comIdentif" valor="2" reler="ajax"
					idAjax="comIdentifAjax" />
			</mod:grupo>
			<c:set var="comIdentifVal" value="${comIdentif}" />
			<c:if test="${empty comIdentifVal}">
				<c:set var="comIdentifVal" value="${param['comIdentif']}" />
			</c:if>
			<mod:grupo largura="93">
				<mod:grupo depende="comIdentifAjax">
					<c:if test="${comIdentifVal == '2'}">
						<mod:memo titulo="Observações" var="obsComIdentif" linhas="3"
							colunas="60" obrigatorio="Sim" />
						<mod:oculto var="comIdentifNao" valor="não"/>		
					</c:if>					
				</mod:grupo>
			</mod:grupo>			
		</mod:grupo>
		<mod:grupo titulo="O profissional costuma atrasar o início das aulas?">	
			<mod:radio titulo="Não." var="atraso" valor="1" marcado="Sim"
						reler="ajax" idAjax="atrasoAjax" />	
			<mod:grupo largura="7">
				<mod:radio titulo="Sim." var="atraso" valor="2" reler="ajax"
					idAjax="atrasoAjax" />
			</mod:grupo>
			<c:set var="atrasoVal" value="${atraso}" />
			<c:if test="${empty atrasoVal}">
				<c:set var="atrasoVal" value="${param['atraso']}" />
			</c:if>
			<mod:grupo largura="73">
				<mod:grupo depende="atrasoAjax">
					<c:choose>									
						<c:when test="${atrasoVal == '2'}">
							<mod:grupo largura="3">
								Com que frequência?
							</mod:grupo>
							<mod:grupo largura="18">						
									<mod:radio titulo="Sempre." var="frequencia" valor="1" marcado="Sim"/>							
									<mod:radio titulo="Em cerca de 50% das aulas." var="frequencia" valor="2"/>								
									<mod:radio titulo="Em menos de 50% das aulas." var="frequencia" valor="3"/>																				
							</mod:grupo>						
						</c:when>
						<c:otherwise>										
							<mod:grupo largura="20">
								<%-- acertar o mod grupo --%>							
							</mod:grupo>
							<mod:oculto var="atrasoNao" valor="não"/>		
						</c:otherwise>	
					</c:choose>						
				</mod:grupo>
			</mod:grupo>
		</mod:grupo> 
		<mod:grupo titulo="O profissional respeita o tempo de duração das aulas (15 minutos)?">
			<mod:radio titulo="Sim." var="duracao" valor="1" marcado="Sim" reler="ajax" idAjax="duracaoAjax" />
			<mod:radio titulo="Não." var="duracao" valor="2" reler="ajax" idAjax="duracaoAjax" />			
			<mod:radio titulo="Às vezes." var="duracao" valor="3" reler="ajax" idAjax="duracaoAjax" />				
			<c:set var="duracaoVal" value="${duracao}" />				
			<c:if test="${empty duracaoVal}">
				<c:set var="duracaoVal" value="${param['duracao']}" />	
			</c:if>			
			<mod:grupo depende="duracaoAjax"> 
				<c:if test="${duracaoVal == '2' or duracaoVal == '3' }"> 
					<mod:memo titulo="Observações" var="obsDuracao" linhas="2"
						colunas="60" obrigatorio="Sim" />					
					<c:choose>									
						<c:when test="${duracaoVal == '2'}">
							<mod:oculto var="duracaoNao" valor="não" />	
						</c:when>
						<c:otherwise>										
							<mod:oculto var="duracaoNao" valor=", às vezes," />	
						</c:otherwise>	
					</c:choose>						
				</c:if>				
			</mod:grupo>  
			
		</mod:grupo>
		<mod:grupo titulo="O profissional deu esclarecimentos sobre os objetivos de cada exercício durante as aulas?  ">			
			<mod:radio titulo="Sim." var="esclarecimento" valor="1" marcado="Sim" />		
			<mod:radio titulo="Não." var="esclarecimento" valor="2" />				
			<mod:radio titulo="Às vezes, quando perguntado." var="esclarecimento" valor="3" />				
		</mod:grupo>
		<mod:grupo titulo="O profissional costuma dar orientação quanto ao carregamento de pesos e às posturas adotadas 
					no ambiente de trabalho?" >		
			<mod:radio titulo="Sim." var="orientacao" valor="1" marcado="Sim" />		
			<mod:radio titulo="Não." var="orientacao" valor="2" />			
			<mod:radio titulo="Eventualmente." var="orientacao" valor="3" />	
			<mod:radio titulo="Só quando perguntado." var="orientacao" valor="4" />			
		</mod:grupo>	
		<hr color="#FFFFFF" />		
		<p><b>Quanto aos materiais/equipamentos utilizados:</b></p>
		<mod:grupo titulo="O profissional utiliza música para acompanhar as aulas?">			
			<mod:radio titulo="Sempre." var="musica" valor="1" marcado="Sim" />		
			<mod:radio titulo="Frequentemente." var="musica" valor="2" />		
			<mod:radio titulo="Quase nunca." var="musica" valor="3" />		
			<mod:radio titulo="Nunca." var="musica" valor="4" />
		</mod:grupo>		
		<mod:grupo titulo="O profissional utiliza materiais acessórios como bolas, elásticos, mini-bastões, 
					dentre outros, na realização dos exercícios?">			
			<mod:radio titulo="Sempre." var="acessorios" valor="1" marcado="Sim" />		
			<mod:radio titulo="Frequentemente." var="acessorios" valor="2" />			
			<mod:radio titulo="Quase nunca." var="acessorios" valor="3" />		
			<mod:radio titulo="Nunca." var="acessorios" valor="4" />		
		</mod:grupo>	
		<mod:grupo>
			<mod:memo
				titulo="Relate outras ocorrências que considere relevante para a fiscalização
						dos serviços prestados" var="ocorrencias" linhas="2" colunas="80" />
		</mod:grupo>		
		<mod:grupo titulo="FREQUÊNCIA DE FUNCIONÁRIOS">
			<mod:selecao titulo="Índice de Freqüência" var="freqFunc" opcoes="Integral;Parcial" reler="ajax" idAjax="freqFuncAjax" />			
			<mod:grupo depende="freqFuncAjax">
				<c:if test="${freqFunc eq 'Parcial'}">
					<mod:grupo titulo="A empresa avisa, em tempo, quando há ausências ou substituições de profissionais
			 			ou reposição de aulas?" >					
						<mod:radio titulo="Sempre." var="aviso" valor="1" marcado="Sim" />				
						<mod:radio titulo="Frequentemente." var="aviso" valor="2" />				
						<mod:radio titulo="Quase nunca." var="aviso" valor="3" />	
						<mod:radio titulo="Nunca." var="aviso" valor="4" />				
					</mod:grupo>					
					<mod:grupo>
						<mod:numero titulo="Nº de faltas sem reposição" var="numFaltas"
							largura="10" />
					</mod:grupo>
					<mod:grupo>
						<mod:numero
							titulo="Quantidade de minutos em atrasos sem reposição"
							var="quantMinutos" largura="10" />
					</mod:grupo>
					<mod:grupo>
						<mod:memo titulo="Ressalvas" var="ressalvaFreq" linhas="2"
							colunas="80" />
					</mod:grupo>
				</c:if>
			</mod:grupo>
		</mod:grupo>	
	</mod:entrevista>
	<mod:documento>	
		<c:choose>
			<c:when test="${frequencia == '1'}">
				<mod:oculto var="freqAtraso" valor="sempre"/>		
			</c:when>
			<c:when test="${frequencia == '2'}">
				<mod:oculto var="freqAtraso" valor="em cerca de 50% das aulas"/>		
			</c:when>
			<c:when test="${frequencia == '3'}">
				<mod:oculto var="freqAtraso" valor="em menos de 50% das aulas"/>		
			</c:when>						
		</c:choose>										
		<c:choose>
				<c:when test="${esclarecimento == '2'}">
					<mod:oculto var="esclarecimentoNao" valor="não" />	
				</c:when>
				<c:when test="${esclarecimento == '3'}">
					<mod:oculto var="esclarecimentoNao" valor=", às vezes quando perguntado," />	
				</c:when>
		</c:choose>			
		<c:choose>
			<c:when test="${orientacao == '2'}">					
				<mod:oculto var="orientacaoNao" valor="não" />	
			</c:when>
			<c:when test="${orientacao == '3'}">					
				<mod:oculto var="orientacaoNao" valor="eventualmente" />	
			</c:when>
			<c:when test="${orientacao == '4'}">				
				<mod:oculto var="orientacaoNao" valor=", só quando perguntado," />	
			</c:when>
		</c:choose>		
		<c:choose>
			<c:when test="${musica == '1'}">					
				<mod:oculto var="musicaNao" valor="sempre" />	
			</c:when>
			<c:when test="${musica == '2'}">					
				<mod:oculto var="musicaNao" valor="frequentemente" />	
			</c:when>
			<c:when test="${musica == '3'}">					
				<mod:oculto var="musicaNao" valor="quase nunca" />	
			</c:when>
			<c:when test="${musica == '4'}">				
				<mod:oculto var="musicaNao" valor="nunca" />	
			</c:when>
		</c:choose>	
		<c:choose>
			<c:when test="${acessorios == '1'}">					
				<mod:oculto var="acessoriosNao" valor="sempre" />	
			</c:when>
			<c:when test="${acessorios == '2'}">					
				<mod:oculto var="acessoriosNao" valor="frequentemente" />	
			</c:when>
			<c:when test="${acessorios == '3'}">					
				<mod:oculto var="acessoriosNao" valor="quase nunca" />	
			</c:when>
			<c:when test="${acessorios == '4'}">				
				<mod:oculto var="acessoriosNao" valor="nunca" />	
			</c:when>
		</c:choose>	
		<c:choose>
			<c:when test="${aviso == '1'}">					
				<mod:oculto var="avisoNao" valor="sempre" />	
			</c:when>
			<c:when test="${aviso == '2'}">					
				<mod:oculto var="avisoNao" valor="frequentemente" />	
			</c:when>
			<c:when test="${aviso == '3'}">					
				<mod:oculto var="avisoNao" valor="quase nunca" />	
			</c:when>
			<c:when test="${aviso == '4'}">				
				<mod:oculto var="avisoNao" valor="nunca" />	
			</c:when>
		</c:choose>	
		<c:choose>									
			<c:when test="${freqFunc eq 'Integral'}">
				<mod:oculto var="freqFuncTipo" valor="integral" />	
			</c:when>
			<c:otherwise>										
				<mod:oculto var="freqFuncTipo" valor="parcial" />	
			</c:otherwise>	
		</c:choose>			
						
		<table style="float:none; clear:both" width="100%" border="0" align="left" cellspacing="0" cellpadding="5">
			<tr>
				<th><b>Informações referentes ao profissional e aos serviços prestados: </b></th>
			</tr>
			<tr><td>Os funcionários ${comIdentifNao} estão devidamente identificados (com crachá).
				<c:if test="${comIdentif == '2'}">
					<br> obs: ${obsComIdentif}					
				</c:if>
			</td></tr>	
			<tr><td>O profissional ${atrasoNao} costuma atrasar o início das aulas ${freqAtraso}.</td></tr>
			<tr><td>O profissional ${duracaoNao} respeita o tempo de duração das aulas (15 minutos).
				<c:if test="${duracao == '2' or duracao == '3' }">
					<br> obs: ${obsDuracao}.
				</c:if>
			</td></tr>
			<tr><td>O profissional ${esclarecimentoNao} deu esclarecimentos sobre os objetivos de cada exercício durante as aulas.</td></tr>
			<tr><td>O profissional ${orientacaoNao} costuma dar orientação quanto ao carregamento de pesos e às posturas adotadas no ambiente de trabalho.</td></tr>			
		</table>
		<br>
		<table style="float:none; clear:both" width="100%" border="0" align="left" cellspacing="0" cellpadding="5">
			<tr>
				<th><b>Informações referentes aos materiais/equipamentos utilizados: </b></th>
			</tr>
			<tr><td>O profissional ${musicaNao} utiliza música para acompanhar as aulas.</td></tr>
			<tr><td>O profissional ${acessoriosNao} utiliza materiais acessórios como bolas, elásticos, mini-bastões, 
					dentre outros, na realização dos exercícios.</td></tr>
		</table>						
		<c:if test="${not empty ocorrencias}">
			<span>Ocorrências relevantes para a fiscalização dos serviços prestados: ${ocorrencias}</span>
		</c:if>	
		
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		
		<table style="float:none; clear:both" width="100%" border="0" align="left" cellspacing="0" cellpadding="5">
			<tr>
				<th><b>Informações referentes à frequência de funcionários: </b></th>
			</tr>			
			<tr>
				<td>Informo que o(s) funcionário(s) tiveram frequência ${freqFuncTipo} durante o período.</td>
			</tr>
			<c:if test="${freqFunc eq 'Parcial'}">		
				<tr>
					<td>A empresa ${avisoNao} avisa, em tempo, quando há ausências ou substituições de 
						profissionais ou reposição de aulas.</td>
				</tr>
				<tr>
					<c:choose>									
						<c:when test="${not empty numFaltas and numFaltas != '0'}">
							<td>Número de faltas sem reposição: ${numFaltas} </td>					
						</c:when>
						<c:otherwise>										
							<td>Não houve faltas sem reposição.</td>
						</c:otherwise>	
					</c:choose>					
				</tr>
				<tr>
					<c:choose>									
						<c:when test="${not empty quantMinutos and quantMinutos != '0'}">
							<td>Quantidade de minutos em atrasos sem reposição: ${quantMinutos} </td>					
						</c:when>
						<c:otherwise>										
							<td>Não houve minutos em atrasos sem reposição.</td>
						</c:otherwise>	
					</c:choose>					
				</tr>
				<c:if test="${not empty ressalvaFreq}">
					<tr>
						<td>Ressalvas: ${ressalvaFreq} </td>
					</tr>
				</c:if>
			</c:if>
		</table>	
		</body>
		</html>
	</mod:documento>
</mod:modelo>
