<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		
		<!------------------------ 1 - DADOS DO SERVIDOR ------------------------>
		
		<mod:grupo>
			<mod:grupo titulo="1. DADOS DO SERVIDOR">
				<mod:grupo>
					<mod:pessoa titulo="Matrícula" obrigatorio="Sim" var="matServidor" reler="sim" />
				</mod:grupo>
				<mod:grupo>
					<c:if test="${not empty requestScope['matServidor_pessoaSel.id']}">
						<mod:oculto var="cargoOculto" valor="${f:cargoPessoa(requestScope['matServidor_pessoaSel.id'])}" />					
						Cargo: ${cargoOculto}
					</c:if>				
				</mod:grupo>
				<mod:grupo largura="21">
					<mod:texto titulo="Quadro de Pessoal"
						valor="SJRJ" obrigatorio="Sim" var="quaPessoal" largura="4" maxcaracteres="5" />
				</mod:grupo>
				<mod:grupo largura="79">
					<mod:data titulo="Data Inicial de Afastamento" obrigatorio="Sim" var="datIniAfastamento" />
				</mod:grupo>
				<mod:memo titulo="Histórico" obrigatorio="Sim" var="historico" colunas="80" linhas="2" />
			</mod:grupo>
		</mod:grupo>

		<!------------------------ 2 - AVALIAÇÃO ------------------------>

		<hr>

		<mod:grupo>
			<mod:grupo titulo="2. AVALIAÇÃO">

				<mod:grupo titulo="Cabe readaptação?">
					<mod:grupo largura="7">
						<mod:radio titulo="Sim" var="readaptacao" valor="1" marcado="Sim"/>
					</mod:grupo>
					<mod:grupo largura="93">
						<mod:radio titulo="Não" var="readaptacao" valor="2"/>
					</mod:grupo>
				</mod:grupo>

				<mod:grupo titulo="O(A) servidor(a) encontra-se inválido(a) permanentemente nos termos
					do art. 186, inciso I, da Lei nº 8.112/90?">
					<mod:grupo largura="7">
						<mod:radio titulo="Sim" var="invalido" valor="1" marcado="Sim"/>
					</mod:grupo>
					<mod:grupo largura="93">
						<mod:radio titulo="Não" var="invalido" valor="2"/>
					</mod:grupo>
				</mod:grupo>
				
				<mod:grupo titulo="Enquadra-se a doença dentre aquelas 
					específicas no art. 186, §1º da Lei 8.112/90?">
					<mod:grupo largura="7">
						<mod:radio titulo="Sim" var="doeEspecifica" valor="1"
							reler="ajax" idAjax="doeEspecificaAjax"/>
					</mod:grupo>
					<mod:grupo largura="93">
						<mod:radio titulo="Não" var="doeEspecifica" valor="2"
							reler="ajax" idAjax="doeEspecificaAjax" marcado="Sim"/>
					</mod:grupo>
					<c:set var="valDoeEspecifica" value="${doeEspecifica}"/>
					<c:if test="${empty valDoeEspecifica}">
						<c:set var="valDoeEspecifica" value="${param['doeEspecifica']}"/>
					</c:if>
					<mod:grupo depende="doeEspecificaAjax">
						<c:if test="${valDoeEspecifica == '1'}">					
							<mod:grupo largura="50">
								<mod:texto titulo="<b>Especificar o nome da doença</b>" obrigatorio="Sim" var="nomDoeEspecifica" largura="60" maxcaracteres="60"/>
							</mod:grupo>
							<mod:grupo largura="50"></mod:grupo>
						</c:if>
					</mod:grupo>
				</mod:grupo>
				
				<mod:grupo titulo="Enquadra-se a doença como acidente em serviço?">
					<mod:grupo largura="7">
						<mod:radio titulo="Sim" var="doeAcidente" valor="1"
							reler="ajax" idAjax="doeAcidenteAjax"/>
					</mod:grupo>
					<mod:grupo largura="93">
						<mod:radio titulo="Não" var="doeAcidente" valor="2"
							reler="ajax" idAjax="doeAcidenteAjax" marcado="Sim"/>
					</mod:grupo>
					<c:set var="valDoeAcidente" value="${doeAcidente}"/>
					<c:if test="${empty valDoeAcidente}">
						<c:set var="valDoeAcidente" value="${param['doeAcidente']}"/>
					</c:if>
		
					<mod:grupo depende="doeAcidenteAjax">
						<c:if test="${valDoeAcidente == '1'}">					
							<mod:grupo largura="50">
									<mod:texto titulo="<b>Especificar o nome da doença</b>" obrigatorio="Sim" var="nomDoeAcidente" largura="60" maxcaracteres="60"/>
							</mod:grupo>
							<mod:grupo largura="50">
									<mod:texto titulo="<b>Informar o nº do PA que reconheceu a situação</b>" obrigatorio="Sim" var="numPAAcidente" largura="42" maxcaracteres="42"/>
							</mod:grupo>
						</c:if>
					</mod:grupo>
				</mod:grupo>

				<mod:grupo titulo="Enquadra-se a doença como moléstia profissional?">
					<mod:grupo largura="7">
						<mod:radio titulo="Sim" var="doeMolestia" valor="1"
							reler="ajax" idAjax="doeMolestiaAjax"/>
					</mod:grupo>
					<mod:grupo largura="93">
						<mod:radio titulo="Não" var="doeMolestia" valor="2"
							reler="ajax" idAjax="doeMolestiaAjax" marcado="Sim"/>
					</mod:grupo>
					<c:set var="valDoeMolestia" value="${doeMolestia}"/>
					<c:if test="${empty valDoeMolestia}">
						<c:set var="valDoeMolestia" value="${param['doeMolestia']}" />
					</c:if>

					<mod:grupo depende="doeMolestiaAjax">
						<c:if test="${valDoeMolestia == '1'}">					
							<mod:grupo  largura="50">
									<mod:texto titulo="<b>Especificar o nome da doença</b>" obrigatorio="Sim" var="nomDoeMolestia" largura="60" maxcaracteres="60"/>
							</mod:grupo>
							<mod:grupo  largura="50">
									<mod:texto titulo="<b>Informar	o nº do PA que reconheceu a situação</b>" obrigatorio="Sim" var="numPAMolestia" largura="42" maxcaracteres="42"/>
							</mod:grupo>
						</c:if>
					</mod:grupo>
				</mod:grupo>

				<mod:grupo titulo="Caso constatada alienação mental no exame, 
					sugere curatela para o recebimento de proventos?">
					<mod:grupo largura="7">
						<mod:radio titulo="Sim" var="aliMental" valor="1" marcado="Sim"/>
					</mod:grupo>
					<mod:grupo largura="93">
						<mod:radio titulo="Não" var="aliMental" valor="2"/>
					</mod:grupo>
				</mod:grupo>

				<mod:grupo titulo="Foi constatada, no exame"/>
					<mod:caixaverif titulo="doença para fins de isenção 
						de imposto de renda, nos termos da Lei nº 7.713/1988, com a redação atual,
						art. 30 da Lei nº 9.250/1995 e IN nº 15/2001/SRF?"
						var="doeInsencaoIR" />
				</mod:grupo>
					
				<mod:grupo titulo="Foi constatada, no exame"/>
					<mod:caixaverif titulo="doença considerada incapacitante 
						nos termos do art. 40, §21 (desconto para o plano de seguridade do
						servidor público - PSS), da Constituição Federal?"
						var="doeIncapacitante" />
				</mod:grupo>

		<!------------------------ 3 - REAVALIAÇÃO ------------------------> 

		<hr>

		<mod:grupo>
			<mod:grupo titulo="3. REAVALIAÇÃO">
				<mod:grupo titulo="Cabe reavaliação?">
					<mod:grupo largura="7">
						<mod:radio titulo="Sim" var="reavaliacao" valor="1"
							reler="ajax" idAjax="reavaliacaoAjax"/>
					</mod:grupo>
					<mod:grupo largura="93">
						<mod:radio titulo="Não" var="reavaliacao" valor="2" marcado="Sim" 
							reler="ajax" idAjax="reavaliacaoAjax"/>
					</mod:grupo>
				</mod:grupo>
				<c:set var="valReavaliacao" value="${reavaliacao}"/>
					<c:if test="${empty valReavaliacao}">
						<c:set var="valReavaliacao" value="${param['reavaliacao']}" />
					</c:if>
						<mod:grupo depende="reavaliacaoAjax">
							<c:if test="${valReavaliacao == '1'}">					
								<mod:grupo>
										<mod:texto titulo="<b>Especificar a finalidade</b>" obrigatorio="Sim" var="nomFinalidade" largura="106" maxcaracteres="106"/>
								</mod:grupo>
								<mod:grupo titulo="Período para reavaliar:">
									<mod:data titulo="De" obrigatorio="Sim" var="nomFinalidadeI"/>
									<mod:data titulo="Até" obrigatorio="Sim" var="nomFinalidadeF"/>
								</mod:grupo>
							</c:if>
						</mod:grupo>
			</mod:grupo>
		</mod:grupo>

	</mod:entrevista>

	<mod:documento>
		<c:set var="tl" value="10pt" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>

		
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF">
			<tr>
				<td>
					<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
				</td>
			</tr>
		</table>
	
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda2.jsp" />
		FIM CABECALHO -->

		<br />
		<table width="100%" border="0" cellpadding="0">
			<tr>
				<td align="center"><b>ATA MÉDICO PERICIAL DE APOSENTADORIA</b></td>
			</tr>			
		</table>
		<br/>

		<table cellpadding="4" width="100%" border="1">
		   	<tr>
				<td colspan="8"><b>1 - DADOS DO SERVIDOR</b></td>
			</tr>
			<tr>
				<td colspan="6">
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Nome:</b>&nbsp;&nbsp;&nbsp;
						${requestScope['matServidor_pessoaSel.descricao']}
					</p>
				</td>
				<td colspan="2">
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Matrícula:</b>&nbsp;&nbsp;&nbsp;
						${requestScope['matServidor_pessoaSel.sigla']}
					</p>
				</td>
			</tr>
			<tr>
				<td colspan="8">
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Cargo:</b>&nbsp;&nbsp;&nbsp;
						${cargoOculto}
					</p>
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Quadro de Pessoal:</b>&nbsp;&nbsp;&nbsp; 
						${quaPessoal}
					</p>
				</td>
				<td colspan="4">
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Data Inicial do Afastamento:</b>&nbsp;&nbsp;&nbsp; 
						${datIniAfastamento}
					</p>
				</td>
			</tr>
			<tr>
				<td colspan="8">
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Histórico:</b>&nbsp;&nbsp;&nbsp; 
						${historico}
					</p>
				</td>
			</tr>
		</table>

		<br />
		
		<table cellpadding="4" width="100%" border="1">
			<tr>
				<td><b>2 - AVALIAÇÃO</b></td>
			</tr>
			<tr>
				<td>
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Cabe readaptação?</b>&nbsp;&nbsp;&nbsp;
						<c:choose> 
  							<c:when test="${readaptacao == '2'}">
    							Não.
  							</c:when>
  							<c:otherwise>
    							Sim.
  							</c:otherwise>
						</c:choose>
					</p>
				</td>
			</tr>
			<tr>
				<td>
					<p style="font-family: Arial; font-size: 8pt;">
						<b>O(A) servidor(a) encontra-se inválido(a) permanentemente nos termos do art. 186, inciso I, da Lei nº 8.112/90?</b>&nbsp;&nbsp;&nbsp; 
						<c:choose> 
  							<c:when test="${invalido == '2'}">
    							Não.
  							</c:when>
  							<c:otherwise>
    							Sim.
  							</c:otherwise>
						</c:choose>
					</p>
				</td>
			</tr>
			<tr>
				<td>
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Enquadra-se a doença dentre aquelas específicas no art. 186, §1º da Lei 8.112/90?</b>&nbsp;&nbsp;&nbsp;
						<c:choose> 
  							<c:when test="${doeEspecifica == '2'}">
    							Não.
  							</c:when>
  							<c:otherwise>
								Sim.<br/>
								<b>Nome da doença:</b>&nbsp;&nbsp;&nbsp;${nomDoeEspecifica}
  							</c:otherwise>
						</c:choose>
					</p>
				</td>
			</tr>
			<tr>
				<td>
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Enquadra-se a doença como acidente em serviço?</b>&nbsp;&nbsp;&nbsp; 
						<c:choose> 
  							<c:when test="${doeAcidente == '2'}">
    							Não.
  							</c:when>
  							<c:otherwise>
								Sim.<br/>
								<b>Nome da doença:</b>&nbsp;&nbsp;&nbsp;${nomDoeAcidente}&nbsp;&nbsp;&nbsp;<b>Nº do PA:</b>&nbsp;&nbsp;&nbsp;${numPAAcidente}
  							</c:otherwise>
						</c:choose>
					</p>
				</td>
			</tr>
			<tr>
				<td>
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Enquadra-se a doença como moléstia profissional?</b>&nbsp;&nbsp;&nbsp;
						<c:choose> 
  							<c:when test="${doeMolestia == '2'}">
    							Não.
  							</c:when>
  							<c:otherwise>
								Sim.<br/>
								<b>Nome da doença:</b>&nbsp;&nbsp;&nbsp;${nomDoeMolestia}&nbsp;&nbsp;&nbsp;<b>Nº do PA:</b>&nbsp;&nbsp;&nbsp;${numPAMolestia}
  							</c:otherwise>
						</c:choose>
					</p>
				</td>
			</tr>
			<tr>
				<td>
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Caso constatada alienação mental no exame, sugere curatela para o recebimento de proventos?</b>&nbsp;&nbsp;&nbsp; 
						<c:choose> 
  							<c:when test="${aliMental == '2'}">
    							Não.
  							</c:when>
  							<c:otherwise>
								Sim.
  							</c:otherwise>
						</c:choose>
					</p>
				</td>
			</tr>
			<tr>
				<td>
					<p style="font-family: Arial; font-size: 8pt;">
						<b>Foi constatada, no exame, doença para fins de isenção no imposto de renda, nos termos da Lei nº 7.713/1988, com a redação atual, art. 30 da Lei nº 9.250/1995 e IN nº 15/2001/SRF?:</b>&nbsp;&nbsp;&nbsp; 
						<c:choose> 
  							<c:when test="${doeInsencaoIR == 'Nao'}">
    							Não.
  							</c:when>
  							<c:otherwise>
								Sim.
  							</c:otherwise>
						</c:choose>
					</p>
				</td>
			</tr>
			<tr>
				<td>
					<p style="font-family: Arial; font-size: 8pt;"><b>Foi constatada, no exame, doença considerada incapacitante nos termos do art. 40, §21 (desconto para o plano de seguridade do servidor público - PSS), da Constituição Federal?</b>&nbsp;&nbsp;&nbsp; 
						<c:choose> 
  							<c:when test="${doeIncapacitante == 'Nao'}">
    							Não.
  							</c:when>
  							<c:otherwise>
								Sim.
  							</c:otherwise>
						</c:choose>
					</p>
				</td>
			</tr>
		</table>

		<br />

		<table cellpadding="4" width="100%" border="1" >
			<tr>
				<td><b>3 - REAVALIAÇÃO</b></td>
			</tr>
				<c:choose> 
  					<c:when test="${reavaliacao == '2'}">
						<tr>
							<td>
								<p style="font-family: Arial; font-size: 8pt;">
									<b>Cabe reavaliação?</b>&nbsp;&nbsp;&nbsp;Não.
								</p>
							</td>
						</tr>
  					</c:when>
  					<c:otherwise>
						<tr>
							<td>
								<p style="font-family: Arial; font-size: 8pt;">
									<b>Cabe reavaliação?</b>&nbsp;&nbsp;&nbsp;Sim.&nbsp;&nbsp;&nbsp;<b>Finalidade:</b>&nbsp;&nbsp;&nbsp;${nomFinalidade}
								</p>
							</td>
						</tr>
						<tr>
							<td>
								<p style="font-family: Arial; font-size: 8pt;">
									<b>Período</b>&nbsp;&nbsp;&nbsp;<b>De:</b>&nbsp;&nbsp;&nbsp;${nomFinalidadeI}&nbsp;&nbsp;&nbsp;<b>Até:</b>&nbsp;&nbsp;&nbsp;${nomFinalidadeF}
								</p>
							</td>
						</tr>				
  					</c:otherwise>
				</c:choose>
		</table>
	
	 
	<c:import
			url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
			
	<table width="80%" border="0" cellpadding="0">
		<tr>
			<td> Ciente em _____/_____/_______ </td>
			<td>_____________________________________________________ <br>
	${requestScope['matServidor_pessoaSel.descricao']} - ${requestScope['matServidor_pessoaSel.sigla']} </td>
		</tr>	
	</table>
	

	<!-- INICIO PRIMEIRO RODAPE
			<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
			FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
			<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
			FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>
