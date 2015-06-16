<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/solicitacao.jsp">

	<mod:entrevista>
		<mod:grupo>
			<mod:pessoa titulo="Substituto" var="substituto" />
		</mod:grupo>
		<mod:grupo>
			<mod:pessoa titulo="Titular" var="titular" />
		</mod:grupo>
		<mod:grupo titulo="Período Solicitado">
			<mod:data titulo="De" var="dataInicio" />
			<mod:data titulo="a" var="dataFim" />
		</mod:grupo>
		<mod:grupo>
			<mod:selecao reler="sim" titulo="Por motivo de" var="motivo" opcoes="[SELECIONE];AUSÊNCIA EM RAZÃO DE FALECIMENTO DE FAMILIAR;AUSÊNCIA AO SERVIÇO POR MOTIVO DE CASAMENTO;AFASTAMENTO AUTORIZADO PARA DOAÇÃO DE SANGUE;COMPENSAÇÃO DOS DIAS TRABALHADOS NO RECESSO JUDICIÁRIO;COMPENSAÇÃO DOS DIAS TRABALHADOS NAS ELEIÇÕES;LICENÇA PRÊMIO;FÉRIAS REGULAMENTARES;LICENÇA PARA TRATAMENTO DA PRÓPRIA SAÚDE;LICENÇA GESTANTE;LICENÇA PARA CAPACITAÇÃO;LICENÇA POR MOTIVO DE DOENÇA EM PESSOA DA FAMILIA;LICENÇA PATERNIDADE;LICENÇA ADOTANTE;PARTICIPAÇÃO EM AÇÕES DE CAPACITAÇÃO;TITULAR SUBSTITUI OUTRO;OUTROS"/>
		</mod:grupo>
		<c:if test="${motivo == 'OUTROS'}">
			<mod:texto var="outrosMotivos" titulo="Por motivo de" largura="60"/>
		</c:if>
	</mod:entrevista>
	<mod:documento>						
	
		<mod:valor var="texto_solicitacao"><p style="TEXT-INDENT: 2cm" align="justify">Solicito as providências necessárias para que o(a) servidor(a) <mod:identificacao pessoa="${requestScope['substituto_pessoaSel.id']}" nivelHierarquicoMaximoDaLotacao="4"/>
			<b>substitua</b>
			o(a) servidor(a) 
			 <mod:identificacao pessoa="${requestScope['titular_pessoaSel.id']}" funcao="sim" negrito="sim" nivelHierarquicoMaximoDaLotacao="4"/>
			<c:choose>
				<c:when test="${(dataInicio == dataFim) or (empty dataFim)}">
					no dia <b>${dataInicio}</b>,
				</c:when>
					<c:otherwise>
					no período de <b>${dataInicio}</b> a <b>${dataFim}</b>,
					</c:otherwise>
			</c:choose>
			por motivo de <c:choose><c:when test="${motivo != 'OUTROS'}"><b>${motivo}</b></c:when><c:otherwise><b>${outrosMotivos}</b></c:otherwise></c:choose>.
			</p>
		</mod:valor>
	</mod:documento>
</mod:modelo>

