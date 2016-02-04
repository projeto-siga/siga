<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
	<mod:grupo titulo="1 - Dados do beneficiário">
			<mod:pessoa var="nome" titulo="Nome"/>
		<mod:grupo>
			<mod:lotacao var="lotacao" titulo="Lotação"/>
		</mod:grupo>
		<mod:grupo>
			<mod:texto var="ramal" titulo="Ramal" largura="5"/>
		</mod:grupo>
	</mod:grupo>
	<mod:grupo titulo="2 - Dados do profissional referenciado">
			<mod:texto var="nomemed" titulo="Nome" largura="50"/> 
			<mod:selecao titulo="Tipo de espécialidade" var="psi" opcoes="Psiquiatra;Psicoterapeuta" />
		<mod:grupo>
			<mod:texto var="crmcrp" titulo="CRM / CRP" largura="10"/>
			<mod:texto var="cpf" titulo="CPF" largura="15"/>
		</mod:grupo>
		
		<mod:grupo>
			<mod:texto var="endereco" titulo="Endereço" largura="50"/>
		</mod:grupo>
		<mod:grupo>
			<mod:texto var="telefone" titulo="Telefone" largura="13"/>
		</mod:grupo>
	</mod:grupo>
	
	</mod:entrevista>
	<mod:documento>	
		<c:set var="tl" value="6pt" />		
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			@page {
				margin-left: 3cm;
				margin-right: 3cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
		</style>
		</head>
	<body>
	<mod:letra tamanho="${tl}">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="center" width="100%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">INCLUSÃO NO PROGRAMA DE APOIO À PSIQUIATRIA E PSICOLOGIA (PAPSI)</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br>       
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="100%">
					1- DADOS DO BENEFICIÁRIO</td>
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="60%">Nome: ${requestScope['nome_pessoaSel.descricao']}</td> 
					<td bgcolor="#FFFFFF" width="40%">Matrícula: ${requestScope['nome_pessoaSel.sigla']}</td>
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="100%">Cargo: ${f:cargoPessoa(requestScope['nome_pessoaSel.id']) }</td>
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="60%">Lotação: ${requestScope['lotacao_lotacaoSel.descricao']}</td>
					<td bgcolor="#FFFFFF" width="40%">Ramal: ${ramal }</td>
				</tr>
			</table>
	 		<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="100%">2 - DECLARAÇÃO DO SERVIDOR<br></td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" width="100%" align="justify"><br><font size='0.1'>a) Declaro estar ciente dos</font> termos da IN--23-09, de 19/10/2004, que dispõe sobre o programa de Apoio à Psiquiatria e Psicologia, oferecido pelo Tribunal Regional Federal da 2ª Região e Seções Judiciárias Jurisdicionadas, a fim de possibilitar um tratamento adequado às necessidades dos servidores<br><br>
																	   b) Declaro ter ciência de que o reembolso será parcial, podendo ser acumulado com reembolso do Plano de Saúde Externo, e que o valor reembolsado será fixado pela presidência do Tribunal, equivalente , no máximo a 50% (cinqünta por cento) do respectivo valor de consulta/sessão, sempre condicionada a existência de dotação orçamentária  para o Tribunal e Seções Judiciárias.<br><br>
					                                                   c) Declaro ter ciência, para fins de ressarcimento, que deverei obter do profissional referenciado o recibo mensal de pagamento das consultas, emitido em nome do servidor, no qual deverá constar: <b><u>Nome completo do profissional, seu número de CPF, número de inscrição no CRM ou CRP(conforme o caso), numero de sessões realizadas no mês, (com as respectivas datas em que foram realizadas) e valor total do recibo</u></b>.<br><br>
					                                                   d) Tenho ciência que deverei apresentar a cópia do recibo na Seção de Benefícios, até o 5º dia do mês subseqüente ao da realização das consultas/sessões, mediante apresentação do original para fins de conferência pelo Setor.<br><br>
					                                                   e) Tenho ciêcia que, em caso de férias, licenças ou afastamentos autorizados, terei o prazo de até 30 dias, após o retorno, para a entrega do recibo e que na hipótese de entrega do recibo fora do prazo estabelecido, deverei submeter o pedido à Administração, mediante justificativa por escrito e manifestação do Serviço Médico ou Psicológico.<br><br>
					                                                   f) Declaro ter ciência de que a duração do benefício será estipulada em dois anos; findo os quais o Serviço Médico/Psicológico  podera prorrogá-lo, no máximo, mais 2 (dois) anos.<br><br>
					                                                   g) Declaro, ainda, ter ciência de que ao não apresentar os recibos na SEBEN/SRH, no período de 2 meses, e não tenha interrompido o meu tratamento, apresentarei uma justificativa por escrito, para a CAMS/SRH, esclarecendo os motivos, pois a partir do terceiro mês consecutivo serei, automaticamente, excluído do Programa por desistência.<br><br>
					                                                   h) Declaro ter ciência de que terei o prazo de 15 dias, a partir da data do encaminhamento, para realizar minha primeira consulta psiquiátrica/sessão de psicoterapia.<br>                                                 
         																																																																				
         			</td>
         			</tr>
         			</table>
         			<table width="100%" border="0" cellpadding="2" cellspacing="1" align="center">
         			<tr>
         			<td>
         			<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp"/>
         			</td>         		
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="100%">3 - DADOS DO PROFISSIONAL REFERENCIADO</td>
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="50%">Nome: ${nomemed}</td>
					<td bgcolor="#FFFFFF" width="50%"><c:if test="${psi eq 'Psiquiatra'}">PSIQUIATRA</c:if>
					<c:if test="${psi eq 'Psicoterapeuta'}">PSICOTERAPEUTA</c:if></td>			
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" width="50%">CRM / CRP: ${crmcrp}</td>
					<td bgcolor="#FFFFFF" width="50%">CPF: ${cpf}</td>			
				</tr>
				</table>
				<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="100%">Endereço: ${endereco}</td>			
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" width="100%">Telefone: ${telefone}</td>			
				</tr>
			</table>
			<table width="100%" bordercolor="#FFFFFF" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="100%">4 - SETOR DE PSICOLOGIA/PSIQUIATRIA</td>
				</tr>
			</table>
			<table width="100%" border="1" cellpadding="8" cellspacing="1" bgcolor="#000000">
				<tr>
					<td bgcolor="#FFFFFF" width="100%"  align="justify">O servidor _________________________________________________ está autorizado a fazer inscrição no benefício PAPSI.</td>
				</tr>
			</table>
			
			<table width="100%" bordercolor="#FFFFFF" cellpadding="2" cellspacing="1">	
				<tr>
					<td align="right">
					${doc.dtExtenso}<br><br>
					______________________________________<br>
									(Assinatura Psiquiatra/Psicólogo)<br><br>
					<table width="40%" align="left">		
						<tr>
							<td>5 - SEÇÃO DE BENEFÍCIOS</td>
						</tr>
						<tr>
							<td>
							<br/>
								Incluído por:__________em:____/____/____ 
							</td>												
						</tr>
					</table>
					</td>
				</tr>			
			</table>
			
						
				
				
				<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />				
	</mod:letra>
	</body>
	</html>
	</mod:documento>
</mod:modelo>

