<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:lotacao titulo="Unidade Gestora" var="lotacao" />
		</mod:grupo>
		<mod:texto titulo="Nº Processo" var="numProcesso" largura="10" />
		<mod:selecao titulo="Nº de Faturas a informar" var="qtdFaturas"
			opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20"
			reler="ajax" idAjax="qtdFaturaAjax" />
		<hr>
		<mod:grupo depende="qtdFaturaAjax">
			<c:forEach var="i" begin="1" end="${qtdFaturas}">
				<mod:grupo>
					<mod:texto titulo="Nº da Fatura ou Nota de Empenho*" var="numFatura${i}" largura="15"
						maxcaracteres="15" />
				</mod:grupo>
				<mod:grupo>
				*Quando o contrato tiver sido formalizado por Nota de Empenho, este campo deve ser preenchido apenas com o número da N.E.
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Período de referência" var="mes${i}"
						largura="50" />
				</mod:grupo>
				<mod:grupo>
					<mod:monetario titulo="Valor da Fatura" var="valorFatura${i}"
						formataNum="sim" extensoNum="sim" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Nome da Empresa" var="nomeEmpresa${i}"
						largura="55" />
				</mod:grupo>
				<mod:grupo>
				<mod:texto titulo="Descrição Contrato" var="descContrato${i}"
						largura="50"/>
				</mod:grupo>
				<mod:grupo>
				<mod:memo titulo="Observação" var="obs${i}" linhas="3" colunas="80" />
	  <!-- <mod:texto titulo="Observação" var="obs${i}" largura="80"/> -->
		</mod:grupo>
				
				<mod:grupo>
					<mod:selecao titulo="Atesto que os serviços foram prestados"
						var="fatura${i}"
						opcoes="conforme o contrato.;com as seguintes ressalvas"
						reler="sim" />
					<mod:grupo>
						<c:if
							test="${requestScope[f:concat('fatura',i)] == 'com as seguintes ressalvas'}">
							<mod:grupo titulo="Descrição das ressalvas">
								<mod:grupo>
									<mod:editor titulo="" var="descRessalvas${i}" />
								</mod:grupo>
							</mod:grupo>
						</c:if>
					</mod:grupo>
				</mod:grupo>
				<hr>
				    
			</c:forEach>
		</mod:grupo>
		
		<mod:grupo>
			<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra"
				opcoes="Normal;Pequeno;Grande" />
		</mod:grupo>
		<c:if
			test="${(requestScope[f:concat('fatura',i)] == 'com as seguintes ressalvas') or (requestScope[f:concat('fatura',i)] == 'conforme discriminados na Fatura em referência')}">
			<b> <mod:mensagem titulo="Atenção"
				texto="Antes de despachar este documento ao setor competente, anexe o arquivo referente à fatura informada." />
			</b>
		</c:if>
		
	</mod:entrevista>
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}">
			<c:set var="tl" value="11pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}">
			<c:set var="tl" value="9pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Grande'}">'
			<c:set var="tl" value="13pt" />
		</c:if>
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
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" width="40%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">${doc.codigo}</p></td>
							<td align="right" width="60%"><mod:letra tamanho="${tl}"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<mod:letra tamanho="${tl}">

			<mod:letra tamanho="${tl}">
				<p align="center">CONTRATOS - FORMULÁRIO DE ATESTO DE SERVIÇOS</p>
			</mod:letra>
			<br>
			<br>

			<p  >Unidade Gestora: <b>${requestScope['lotacao_lotacaoSel.descricao']}</b>
			</p>
			<p  >Nº Processo: <b>${numProcesso}</b>
			</p>
			<br>
			<hr>
			<c:forEach var="i" begin="1" end="${qtdFaturas}">
				<p  >Nº da Fatura: <b>${requestScope[f:concat('numFatura',i)]}</b>
				</p>
				<p  >Período de Ref. <b>${requestScope[f:concat('mes',i)]}</b>
				</p>
				<p  >Valor da Fatura: <b>${requestScope[f:concat('valorFatura',i)]}</b> &nbsp;&nbsp;
				<b>(${requestScope[f:concat(f:concat('valorFatura',i),'vrextenso')]})</b>
				</p>                      
				
				<p  >Nome da Empresa: <b>${requestScope[f:concat('nomeEmpresa',i)]}</b>
				</p>
				<p  >Descrição Contrato:
				<b>${requestScope[f:concat('descContrato',i)]}</b></p>
				<c:if test="${not empty requestScope[f:concat ('obs',i)]}">
					<p  >Observação:<b>
					${requestScope[f:concat('obs',i)]}</b></p>
				</c:if>
				<p>Atesto que os
				serviços foram prestados <c:if
					test="${requestScope[f:concat('fatura',i)] == 'conforme o contrato.'}">
		 	conforme o contrato.
		 </c:if> <c:if
					test="${requestScope[f:concat('fatura',i)] == 'com as seguintes ressalvas'}">
			com as seguintes ressalvas: 
		 <span style="font-size: ${tl};"> <b>${requestScope[f:concat('descRessalvas',i)]}</b>
					</span>
				</c:if></p>
				<br>
				<br>
				<hr>
			</c:forEach>
			<br>	
			<br>
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		</mod:letra>
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />

		</body>
		</html>
	</mod:documento>
</mod:modelo>
