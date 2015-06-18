<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>	
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
DECLARAÇÃO DE MARGEM DE REFINANCIAMENRO-COMPRA DE DÍVIDA -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="sepag" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		<mod:obrigatorios />
		<mod:grupo titulo="">
			<mod:texto obrigatorio="Sim" titulo="Instituição Financeira" var="instituicaoFinanceiro"/>
			<mod:texto titulo="Agência" var="agencia" obrigatorio="Sim"/>
			<mod:memo colunas="80" linhas="5" 
				titulo="Observações" 
				var="obs" />			
		</mod:grupo>
				
	</mod:entrevista>
	<mod:documento>		
		<mod:valor var="texto_requerimento"><p style="TEXT-INDENT: 2cm">		
		<br>${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
		${doc.subscritor.padraoReferenciaInvertido}, matrícula ${doc.subscritor.sigla}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao },  
		vem requerer a Vossa Senhoria declaração de margem para fins de consignação de empréstimo na seguinte Instituição Financeira: ${instituicaoFinanceiro}, agência ${agencia}.
		</p>
		<p style="TEXT-INDENT: 2cm">${obs}</p>
		</mod:valor>
	</mod:documento>
</mod:modelo>
