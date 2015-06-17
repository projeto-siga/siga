<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo>
	<mod:entrevista>
	    <span style="color:red"> <b>PREENCHER OBRIGATORIAMENTE O CAMPO DESCRIÇÃO COM NOME COMPLETO E ASSUNTO</b></span><br>
		<span style="color: red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À CHEFIA IMEDIATA PARA ANUÊNCIA E, EM SEGUIDA, À DICAP/SECADS</b></span>
		<br>
		<br>

		<mod:grupo titulo="">
			<mod:grupo>
				<mod:selecao var="ilustrissimo" titulo="VOCATIVO"
					opcoes="ILUSTRÍSSIMA SENHORA DIRETORA;ILUSTRÍSSIMO SENHOR DIRETOR"
					reler="sim" />
			</mod:grupo>
			<br>
		</mod:grupo>

		<mod:grupo titulo="">
			<br />

			<mod:texto titulo="RAMAL" var="ramal" />
			<mod:selecao titulo="Acerto Gramatical da Lotação" var="acgr"  opcoes="no;na" reler="sim" />
			<br /></br>
		</mod:grupo>


		<mod:grupo titulo="Tipo de Solicitação:">
			<mod:radio titulo="Marcação de Férias" var="tipo" valor="1"
				reler="sim" />
			<mod:radio titulo="Alteração de Período" var="tipo" valor="2"
				reler="sim" />
		</mod:grupo>


		<mod:selecao var="periodoaquisitivo" titulo="Período Aquisitivo"
			opcoes="2008/2009;2009/2010;2010/2011;2011/2012;2012/2013;2013/2014;2014/2015;2015/2016;2016/2017;2017/2018;2018/2019;2019/2020"
			reler="sim" />
		</br>
		<br>

		<mod:grupo titulo="Período de Férias:">
			<mod:radio titulo="Integral" var="periodo" valor="1" reler="sim" />
			<mod:radio titulo="Parcelado" var="periodo" valor="2" reler="sim" />
		</mod:grupo>
		<c:set var="valorperiodo" value="${periodo}" />
		<c:if test="${empty valorperiodo}">
			<c:set var="valorperiodo" value="${param['periodo']}" />
		</c:if>

		<c:set var="valtipo" value="${tipo}" />
		<c:if test="${empty valtipo}">
			<c:set var="valtipo" value="${param['tipo']}" />
		</c:if>

		<c:if test="${valorperiodo == 1}">
			<mod:grupo titulo=" Período de Férias">
				<mod:data titulo="De" var="datinicio" obrigatorio="sim" />
				<mod:data titulo="a" var="datfim" obrigatorio="sim" />
				<c:if test="${valtipo == 2}">
		                    &nbsp;&nbsp;
		            	    <mod:data titulo="para" var="datiniv"
						obrigatorio="sim" />
					<mod:data titulo="a" var="datfimv" obrigatorio="sim" />
				</c:if>
			</mod:grupo>
		</c:if>

		<c:if test="${valorperiodo == 2}">
			<hr style="color: #FFFFFF;" />
			<mod:selecao var="contadorinterv" titulo="Quantidade de Parcelas"
				opcoes="1;2;3" reler="sim" />
			<br />
			<br>
			<mod:grupo titulo=" Períodos de Férias ">
			</mod:grupo>
			<mod:grupo depende="contDependAjax">

				<c:if test="${valorperiodo == 2}">
					<c:forEach var="i" begin="1" end="${contadorinterv}">
					
					<mod:grupo titulo=" Período nº ${i}">
							<mod:data titulo="De" var="datinicio${i}" obrigatorio="sim" />
							<mod:data titulo="a" var="datfim${i}" obrigatorio="sim" />
							<c:if test="${valtipo == 2}">
		            	                       &nbsp;&nbsp;
		            	                       <mod:data titulo="para"
									var="datiniv${i}" obrigatorio="sim" />
								<mod:data titulo="a" var="datfimv${i}" obrigatorio="sim" />
							</c:if>
						</mod:grupo>
					</c:forEach>
				</c:if>

			</mod:grupo>
		</c:if>
		<br>
		<br>

		<mod:grupo titulo="Antecipação da Gratificação Natalina: ">
			<mod:radio var="antgratnat" titulo="Sim" valor="Sim" reler="sim" />
			<mod:radio var="antgratnat" titulo="Não" valor="Não" reler="sim" />
		</mod:grupo>
		<br>
		<br>
		<mod:grupo titulo="Antecipação do Salário do mês das Férias: ">
			<mod:radio var="antsalfer" titulo="Sim" valor="Sim" reler="sim" />
			<mod:radio var="antsalfer" titulo="Não" valor="Não" reler="sim" />
		</mod:grupo>
		<mod:radio titulo="<b>Declaro ter ciência de que poderei NÃO receber antecipadamente o adicional e a remuneração de férias, em caso de requerimento</b>" marcado="Sim" reler="nao" />
        <b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;enviado fora do prazo previsto na Portaria nº 113/2005-Pres do TRF-2ª Região</b>
		<br>
	</mod:entrevista>

	<mod:documento>
		<head>
		<style type="text/css">
@page {
	margin-left: 1cm;
	margin-right: 1cm;
	margin-top: 1cm;
	margin-bottom: 1cm;
}
</style>
		</head>
		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="7pt"></c:set>
		</c:if>

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr><br>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:12,5pt;">SOLICITAÇÃO DE CONCESSÃO / ALTERAÇÃO DE FÉRIAS</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<c:choose>
			<c:when test="${ doc.subscritor.sexo == 'M'}">
				<c:set var="lotc" value="lotado"></c:set>
			</c:when>
			<c:otherwise>
				<c:set var="lotc" value="lotada"></c:set>
			</c:otherwise>
		</c:choose>
		<c:set var="conalter"
			value="<b>Concessão de Férias</b> , nos termos dos arts. 77 e 78 da Lei 8.112/1990"></c:set>
		<c:if test="${tipo == 2}">
			<c:set var="conalter" value="<b>alteração de data de suas férias</b>"></c:set>
		</c:if>
		<c:set var="opt" value="${f:classNivPadr(doc.subscritor.padraoReferencia)}"/>
		
		<p style="text-align: center"><b> ${ilustrissimo} DA SECRETARIA DE RECURSOS HUMANOS</b></p>
		<br />
		<br />	

		${doc.subscritor.descricao}, matrícula ${doc.subscritor.matricula}, ${doc.subscritor.cargo.nomeCargo}, ${opt},
		do Quadro de Pessoal do Tribunal Regional Federal da 2ª Região, ${lotc} ${acgr} ${doc.subscritor.lotacao.descricao}, ramal ${ramal}, solicita a V.Sª ${conalter}, conforme descrito a seguir.
		<br>
		<br>
		<br>
		Período Aquisitivo &nbsp;&nbsp;&nbsp;:
		${periodoaquisitivo}&nbsp;&nbsp;&nbsp;
		<br>
		<br>
		<c:if test="${periodo == 1}">
			Período de Férias &nbsp;&nbsp; :
		         <c:choose>
				<c:when test="${tipo == 1}"> ${datinicio} a ${datfim}<br>
				</c:when>
				<c:otherwise> De ${datinicio} a ${datfim} para  ${datiniv} a ${datfimv}</c:otherwise>
			</c:choose>
		</c:if>

		<c:if test="${periodo == 2}">
			Períodos de Férias&nbsp;&nbsp;&nbsp;:
			
			      <c:if test="${tipo == 1}">
				<c:forEach var="i" begin="1" end="${contadorinterv}">
					<c:set var="dt1" value="${requestScope[(f:concat('datinicio',i))]}" />
					<c:set var="dt2" value="${requestScope[(f:concat('datfim',i))]}" />
			                 ${dt1} a ${dt2}&nbsp;&nbsp;&nbsp;
				</c:forEach>
			</c:if>

			<c:if test="${tipo == 2}">
				<c:forEach var="i" begin="1" end="${contadorinterv}">
					<br>
					<c:set var="dt1" value="${requestScope[(f:concat('datinicio',i))]}" />
					<c:set var="dt2" value="${requestScope[(f:concat('datfim',i))]}" />
					<c:set var="dt1n" value="${requestScope[(f:concat('datiniv',i))]}" />
					<c:set var="dt2n" value="${requestScope[(f:concat('datfimv',i))]}" />
			                 De ${dt1} a ${dt2} para ${dt1n} a ${dt2n}
		     
			            </c:forEach>
			</c:if>
		</c:if>
		</p>
		Antecipação da Gratificação Natalina : ${antgratnat}
		<br />
		Antecipação do Salário do mês de férias : ${antsalfer}


		<br>
		<br>
		<p style="font-family:Arial;font-size:9pt">
		<b>Declaro ter ciência de que poderei não receber antecipadamente o adicional e a remuneração de férias, em caso de requerimento enviado fora do prazo 
		previsto na Portaria nº 113/2005-Pres do TRF-2ª Região.</b>
		</p>
		<br><br>



		<!-- INICIO FECHO -->
		<p align="center">${doc.dtExtenso}</p>
		<!-- FIM FECHO -->

		<!-- INICIO ASSINATURA -->
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<!-- FIM ASSINATURA -->

		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->

		</body>
	</mod:documento>
</mod:modelo>