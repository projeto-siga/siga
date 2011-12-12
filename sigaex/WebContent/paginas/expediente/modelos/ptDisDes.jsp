<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo urlBase="/paginas/expediente/modelos/portaria_presidencia.jsp">
	<mod:entrevista>
	         <br>
	        <mod:grupo>
			    <mod:texto titulo="<b>Processo Administrativo</b>" var="proc" largura="15"/>
			    <mod:selecao titulo="<b>Tipo</b>" var="tipproc" opcoes="ADM;PES;Outro" reler="sim" />
		    </mod:grupo>
		    <br>
 			<mod:grupo>
				<mod:pessoa titulo="<b>Nome do Servidor</b>" var="serv" />
			    <br><br>
			    <mod:selecao titulo="<b>Área</b>" var="area"  opcoes="Administrativa;Apoio Especializado;Judiciária" reler="sim" />
			</mod:grupo>
			<br>
			<mod:grupo>
			    <mod:selecao titulo="<b>Nível</b>" var="niv" opcoes="NA;NI;NS" reler="sim" />
				<mod:selecao titulo="<b>Classe</b>" var="classe" opcoes="A;B;C" reler="sim" />
				<mod:selecao titulo="<b>Padrão</b>" var="padr" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32;33;34;35" reler="sim" />
				<mod:selecao titulo="<b>Quadro de Pessoal</b>" var="quadr" opcoes="Seção Judiciária do Estado do Rio de Janeiro;Seção Judiciária do Estado do Rio de Janeiro;TRF 2ª Região" reler="sim" />
			</mod:grupo>
			<br>
			<mod:grupo titulo="Função Comissionada - Dispensa">
			    <mod:grupo>
			      <mod:selecao titulo="Assistente" var="assist"  opcoes="I;II;III;IV;V" reler="sim" />
		          <mod:selecao titulo="FC" var="fdisp"  opcoes="01;02;03;04;05" reler="sim" />
		          <mod:selecao titulo="Acerto Gramatical" var="acgr"  opcoes="do;da" reler="sim" />
		          <mod:texto titulo="Lotação" var="lotp" largura="55"/>
			    </mod:grupo>
		    </mod:grupo>
		    <br>
		    <mod:grupo titulo="Função Comissionada - Designação">
			    <mod:grupo>
			      <mod:selecao titulo="Assistente" var="assistg"  opcoes="I;II;III;IV;V" reler="sim" />
		          <mod:selecao titulo="FC" var="fdesig"  opcoes="01;02;03;04;05" reler="sim" />
		          <mod:selecao titulo="Acerto Gramatical" var="acgra"  opcoes="do;da" reler="sim" />
		          <mod:texto titulo="Lotação" var="lotg" largura="55"/>
			    </mod:grupo>
		    </mod:grupo>
		    <br>
		    <mod:pessoa titulo="<b>Servidor Dispensado<b>" var="disp" />
		    <br><br>
	</mod:entrevista>
	
	<mod:documento>
	<c:set var="servidor" value ="${f:pessoa(requestScope['serv_pessoaSel.id'])}" />
	<c:set var="servdisp" value ="${f:pessoa(requestScope['disp_pessoaSel.id'])}" />
		<mod:valor var="texto_ato">
		
		    <c:set var="servd" value="a servidora" />
		    <c:set var="desig" value="LA" />
		    
		    <c:if test="${servidor.sexo == 'M'}">
		    <c:set var="servd" value="o servidor" />
		    <c:set var="desig" value="LO" />
		    </c:if>
			<br/>
			<p style="TEXT-INDENT: 2cm" align="justify">
			<b><c:choose><c:when test="${doc.subscritor.sexo == 'M'}">O PRESIDENTE</c:when><c:otherwise>A PRESIDENTE</c:otherwise></c:choose> 
			DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO</b>, no uso de suas atribuições, e considerando o que consta nos autos do Processo Administrativo 
			nº <c:choose><c:when test="${tipproc == 'Outro'}">${proc}</c:when><c:otherwise>${proc}-${tipproc}</c:otherwise></c:choose>, <b>RESOLVE</b>:<br><br>
			<b>DISPENSAR</b> ${servd} ${servidor.nomePessoa}, ${servidor.cargo.nomeCargo}, Área ${area}, Nível 
			<c:choose><c:when test="${niv == 'NA'}">Auxiliar</c:when></c:choose>
			<c:choose><c:when test="${niv == 'NI'}">Intermediário</c:when></c:choose>
			<c:choose><c:when test="${niv == 'NS'}">Superior</c:when></c:choose>, Classe ${classe}, Padrão ${padr},
			do Quadro de Pessoal <c:choose><c:when test="${quadr == 'TRF 2ª Região'}"> do </c:when><c:otherwise> da </c:otherwise></c:choose>  
			${quadr}, da função comissionada de Assistente ${assist}, FC-${fdisp}, ${acgr} ${lotp}, e <b>DESIGNÁ-${desig}</b> para exercer a função
			comissionada de Assistente ${assistg}, FC-${fdesig}, ${acgra} ${lotg}, em vaga decorrente da dispensa de
			${servdisp.nomePessoa}.
		</mod:valor>  	
		</mod:documento>
</mod:modelo>