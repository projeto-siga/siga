<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
LICENÇA PRÊMIO FRUIÇÃO -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DO PERÍODO">
				<mod:selecao titulo="Mês/Meses a ser(em) usufruído(s), na licença"
				var="mesFruicao" opcoes="1;2;3" reler="nao" />		
				<mod:data titulo="Data de Inicio da Fruição" var="dataInicioFruicao" />
		</mod:grupo>
		
	</mod:entrevista>
	
	<mod:documento>
		<mod:valor var="texto_requerimento">	
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Senhoria a <b>FRUIÇÃO DE 
		
		<c:if test="${mesFruicao =='1'}">	
			   1 (UM) MÊS
		</c:if>
		
		<c:if test="${mesFruicao =='2'}">	
			   2 (DOIS) MESES
		</c:if>

		<c:if test="${mesFruicao =='3'}">	
			   3 (TRÊS) MESES
		</c:if>
								
		DA LICENÇA-PRÊMIO</b> por assiduidade a que faz jus, de acordo com o 
		art. 87 da Lei n.º 8.112/90, em sua redação original, 
		a partir de ${dataInicioFruicao}.
		<p>
		<p style="TEXT-INDENT: 2cm" align="justify">
			Declara, ainda, estar ciente de que o art. 86º da Resolução n.º 5/2008 
			do Conselho da Justiça Federal prevê que <I>durante o período de 
			licença será devida ao servidor <B>apenas a remuneração do cargo efetivo</B>, 
			ainda que investido em função gratificada ou em cargo comissionado</I>.
		</mod:valor>
		<mod:valor var="texto_requerimento4">
		<p style="TEXT-INDENT: 2cm" align="left">
		<b>De acordo.</b>
		</p>
		<p style="TEXT-INDENT: 2cm">               
        <b>_______________________________________________</b>
        </p>
        <p style="TEXT-INDENT: 3cm" align="left">
		<b>Local e Data</b>
		</p>
		</br>
		</br>
		<p style="TEXT-INDENT: 2cm" align="left">
		<b>____________________________________________________________</b>
		</p>
		<p style="TEXT-INDENT: 2cm" align="left">
		<b>Assinatura e Matrícula do Superior Hierárquico</b>
		</p>
		</mod:valor>	
	
</mod:documento>
</mod:modelo>
