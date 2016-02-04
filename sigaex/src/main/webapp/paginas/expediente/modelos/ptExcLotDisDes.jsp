<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo urlBase="/paginas/expediente/modelos/portaria_presidencia.jsp">
	<mod:entrevista>
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
				<mod:selecao titulo="<b>Padrão</b>" var="padr" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15" reler="sim" />
				<mod:selecao titulo="<b>Quadro de Pessoal</b>" var="quadr" opcoes="Seção Judiciária do Estado do Espírito Santo;Seção Judiciária do Estado do Rio de Janeiro;TRF 2ª Região" reler="sim" />
			</mod:grupo>
			<br>
			<c:if test="${quadr != 'TRF 2ª Região'}">
			    <mod:selecao titulo="<b>À disposição do TRF</b>" var="dispo" opcoes="Sim;Não" reler="sim" />
			    <br><br>
			</c:if>
			<mod:grupo titulo="Função Comissionada Dispensada & Exclusão de Lotação">
			    <mod:grupo>
			      <mod:selecao titulo="Assistente" var="assist"  opcoes="I;II;III;IV;V" reler="sim" />
		          <mod:selecao titulo="Acerto Gramatical" var="acgr"  opcoes="do;da" reler="sim" />
		          <mod:texto titulo="Lotação" var="lotp" largura="55"/>
			    </mod:grupo>
		    </mod:grupo>
		    <br>
		    <mod:grupo titulo="Função Comissionada Designada & Nova Lotação">
			    <mod:grupo>
			      <mod:selecao titulo="Assistente" var="assistg"  opcoes="I;II;III;IV;V" reler="sim" />
		          <mod:selecao titulo="Acerto Gramatical" var="acgra"  opcoes="no;na" reler="sim" />
		          <mod:texto titulo="Lotação" var="lotg" largura="55"/>
		          <mod:data titulo="Data Inical" var="dtini" />
			    </mod:grupo>
		    </mod:grupo>
		    <br>
		    <mod:pessoa titulo="<b>Servidor Dispensado<b>" var="disp" />
		    <br><br>
	</mod:entrevista>
	
	<mod:documento>
	<c:set var="servidor" value ="${f:pessoa(requestScope['serv_pessoaSel.id'])}" />
	<c:set var="servdisp" value ="${f:pessoa(requestScope['disp_pessoaSel.id'])}" />
	
	<c:if test="${assist == 'I'}">
		    <c:set var="fdisp" value="01" />
	</c:if>
	<c:if test="${assist == 'II'}">
		    <c:set var="fdisp" value="02" />
	</c:if>
	<c:if test="${assist == 'III'}">
		    <c:set var="fdisp" value="03" />
	</c:if>
	<c:if test="${assist == 'IV'}">
		    <c:set var="fdisp" value="04" />
	</c:if>
	<c:if test="${assist == 'V'}">
		    <c:set var="fdisp" value="05" />
	</c:if>
	
	<c:if test="${assistg == 'I'}">
		    <c:set var="fdesig" value="01" />
	</c:if>
	<c:if test="${assistg == 'II'}">
		    <c:set var="fdesig" value="02" />
	</c:if>
	<c:if test="${assistg == 'III'}">
		    <c:set var="fdesig" value="03" />
	</c:if>
	<c:if test="${assistg == 'IV'}">
		    <c:set var="fdesig" value="04" />
	</c:if>
	<c:if test="${assistg == 'V'}">
		    <c:set var="fdesig" value="05" />
	</c:if>
	
		<mod:valor var="texto_ptp">
		
		    <c:set var="acgrav" value="do" />
		    <c:if test="${acgra == 'na'}">
		        <c:set var="acgrav" value="da" />
		    </c:if>
		    
		    <c:set var="servd" value="a servidora" />
		    <c:set var="desig" value="LA" />
		    <c:set var="refr" value="a referida servidora" />
		    <c:if test="${servidor.sexo == 'M'}">
		        <c:set var="servd" value="o servidor" />
		        <c:set var="desig" value="LO" />
		        <c:set var="refr" value="o referido servidor" />
		    </c:if>
			<br/>
			<p style="TEXT-INDENT: 2cm" align="justify">
			<b><c:choose><c:when test="${doc.subscritor.sexo == 'M'}">O PRESIDENTE</c:when><c:otherwise>A PRESIDENTE</c:otherwise></c:choose> 
			DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO</b>, no uso de suas atribuições, <b>RESOLVE</b>:<br><br>
			I - <b>EXCLUIR</b> ${servd} ${servidor.nomePessoa}, ${servidor.cargo.nomeCargo}, Área ${area}, Nível 
			<c:choose><c:when test="${niv == 'NA'}">Auxiliar</c:when></c:choose>
			<c:choose><c:when test="${niv == 'NI'}">Intermediário</c:when></c:choose>
			<c:choose><c:when test="${niv == 'NS'}">Superior</c:when></c:choose>, Classe ${classe}, Padrão ${padr},
			do Quadro de Pessoal <c:choose><c:when test="${quadr == 'TRF 2ª Região'}"> do </c:when><c:otherwise> da </c:otherwise></c:choose>  
			${quadr}, <c:choose><c:when test="${dispo == 'Sim'}">, ora à disposição deste Tribunal,</c:when></c:choose>  da lotação ${acgr} ${lotp} e <b>LOTÁ-${desig}</b> ${acgra} ${lotg}, a partir de ${dtini}.
			<br><br>
			II - <b>DISPENSAR</b> ${refr} da função comissionada de Assistente ${assist}, FC-${fdisp}, ${acgr} ${lotp}, e 
			<b>DESIGNÁ-${desig}</b> para exercer a função comissionada de Assistente ${assistg}, FC-${fdesig}, ${acgrav} ${lotg}, em vaga decorrente da dispensa de
			${servdisp.nomePessoa}.
		</mod:valor>  	
		</mod:documento>
</mod:modelo>