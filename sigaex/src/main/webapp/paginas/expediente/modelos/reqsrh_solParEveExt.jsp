<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<br>
		<span style="color: red"> <b>PREENCHER OBRIGATORIAMENTE O
		CAMPO DESCRIÇÃO COM NOME COMPLETO E ASSUNTO</b></span>
		<br>
		<span style="color: red"> <b>ESTE DOCUMENTO DEVERÁ SER
		ENVIADO À CHEFIA IMEDIATA PARA ANUÊNCIA E, EM SEGUIDA, À DICRE</b></span>
		<br>
		<span style="color:red"> <b>NO CASO DE NECESSIDADE DE CONCESSÃO DE DIÁRIAS, NECESSÁRIO PREENCHER TAMBÉM O DOCUMENTO 
		'PROPOSTA E CONCESSÃO DE DIÁRIAS' NO SIGA</b></span>	
		<br>
		
		<mod:grupo>
			<mod:texto titulo="RAMAL DO REQUERENTE" var="ramal" />
		</mod:grupo>

		<hr style="color: #FFFFFF;" />

		<mod:grupo titulo="Tipo:">
			<mod:radio titulo="Curso" var="tipo" valor="1" reler="sim" />
			<mod:radio titulo="Seminário" var="tipo" valor="2" reler="sim" />
			<mod:radio titulo="Outro" var="tipo" valor="3" reler="sim" />
		</mod:grupo>
        <span style="color: green"> <b>FAVOR ANEXAR DOCUMENTO CONTENDO INFORMAÇÕES SOBRE O CURSO (POR EXEMPLO, IMAGEM DE PÁGINA NA INTERNET)</b></span>
		<hr style="color: #FFFFFF;" />

		<c:set var="valortipo" value="${tipo}" />

		<c:if test="${empty valortipo}">
			<c:set var="valortipo" value="${param['tipo']}" />
		</c:if>
		<c:if test="${valortipo == 3}">
			<mod:grupo titulo="DADOS:">
				<mod:texto titulo="Nome" var="curso" largura="40" />
			</mod:grupo>
		</c:if>


		<c:if test="${valortipo == 1}">
			<mod:grupo titulo="DADOS DO CURSO:">
				<mod:texto titulo="Nome" var="curso" largura="40" />
			</mod:grupo>
		</c:if>


		<c:if test="${valortipo == 2}">
			<mod:grupo titulo="DADOS DO SEMINÁRIO:">
				<mod:texto titulo="Nome" var="curso" largura="40" />
			</mod:grupo>
		</c:if>
		<br>

		<!--  
	         <mod:grupo titulo="DADOS DO CURSO:">
			        <mod:texto titulo="Nome" var="curso" largura="40"/>
	        </mod:grupo> -->


		<mod:grupo>
			<mod:data titulo="Periodo de " var="datiniciocur" obrigatorio="sim" />
			<mod:data titulo="a" var="datfimcur" obrigatorio="sim" />
			<mod:texto titulo="Carga Horária" var="carhor" largura="10" />
			<mod:texto titulo="Valor (R$)" var="valreal" largura="40" />
		</mod:grupo>
		<br>
		<mod:grupo>
			<mod:texto titulo="Local de Realização" var="local" largura="36" />
			<mod:texto titulo="Instrutoria" var="instituto" largura="45" />
		</mod:grupo>
		<hr style="color: #FFFFFF;" />
		<mod:grupo titulo="Diárias/Passagens">
			<mod:radio titulo="Sim" var="diapas" valor="1" marcado="sim" reler="sim" />
			<mod:radio titulo="Não" var="diapas" valor="2" reler="sim" />
		</mod:grupo>

		<hr style="color: #FFFFFF;" />

		<mod:grupo>
			<mod:memo titulo="<b>Justificativa e/ou observação</b>" var="justif"
				colunas="60" linhas="3" />
		</mod:grupo>
		<br>
		
	</mod:entrevista>

	<mod:documento>
		<head>
		<style type="text/css">
@page {
	margin-left: 0.2cm;
	margin-right: 0.2cm;
	margin-top: 0.2cm;
	margin-bottom: 0.2cm;
}
</style>
		</head>
		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="7pt"></c:set>
		</c:if>

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#ffffff"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		 <tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:10pt;">SOLICITAÇÃO DE PARTICIPAÇÃO EM EVENTOS EXTERNOS</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" /> 
		FIM CABECALHO -->

		<c:if test="${doc.subscritor.sexo == 'M'}">
			<c:set var="lotc" value="lotado"></c:set>
			<c:set var="benef1" value="Beneficiário"></c:set>
			<c:set var="benef2" value="beneficiário"></c:set>
		</c:if>
		<c:if test="${doc.subscritor.sexo == 'F'}">
			<c:set var="lotc" value="lotada"></c:set>
			<c:set var="benef1" value="Beneficiária"></c:set>
			<c:set var="benef2" value="beneficiária"></c:set>
		</c:if>
		
	    <c:set var="sim" value="X" ></c:set>
	    <c:set var="nao" value="&nbsp;&nbsp;" ></c:set>
	    <c:if test="${diapas == '2'}">
			<c:set var="sim" value="&nbsp;&nbsp;" ></c:set>
	        <c:set var="nao" value="X" ></c:set>
		</c:if>
	    <br></br>
	
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
		<tr><td>
		
		<table width="100%" border="1" cellpadding="2" cellspacing="1">
			<tr>
				<td width="100%" bgcolor="#000000" align="center"><b>DADOS
				DO SOLICITANTE</b></td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
               	<td width="100%" bgcolor="#FFFFFF" align="left"><b>Nome :</b>&nbsp;${doc.subscritor.descricao}</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
               	<td width="100%" bgcolor="#FFFFFF" align="left">  </td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">
			<tr>
				<td width="80%" bgcolor="#FFFFFF" border="0" align="left"><b>Cargo ou função :</b>&nbsp;${doc.subscritor.cargo.nomeCargo}</td>
				<td width="20%" bgcolor="#FFFFFF" border="0" align="left"><b>Matrícula :</b>&nbsp;${doc.subscritor.matricula}</td>
            </tr>
        </table>
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
               	<td width="100%" bgcolor="#FFFFFF" align="left">  </td>
			</tr>
		</table>
        <table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">
			<tr>
				<td width="80%" bgcolor="#FFFFFF" border="0" align="left"><b>Lotação :</b>&nbsp;${doc.subscritor.lotacao.descricao}</td>
				<td width="20%" bgcolor="#FFFFFF" border="0" align="left"><b>Ramal :</b>&nbsp;${ramal}</td>
        </tr>
        </table>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
               	<td width="100%" bgcolor="#FFFFFF" align="left">  </td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
     			<td width="100%" bgcolor="#FFFFFF" align="left"><b>Curso/Seminário/Outros : </b>&nbsp;${curso}</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
               	<td width="100%" bgcolor="#FFFFFF" align="left">  </td>
			</tr>
		</table>
    	<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">
			<tr>
				<td width="50%" bgcolor="#FFFFFF" align="left"><b>Período :</b>&nbsp;de ${datiniciocur}  a ${datfimcur}</td>
				<td width="30%" bgcolor="#FFFFFF" align="left"><b>Carga horária : </b>&nbsp;${carhor}</td>
				<td width="20%" bgcolor="#FFFFFF" align="left"><b>Valor : R$ </b>&nbsp;${valreal}</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
               	<td width="100%" bgcolor="#FFFFFF" align="left">  </td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">
			<tr>
				<td width="32.5%" bgcolor="#FFFFFF" align="left"><b>Diárias/Passagens :</td>
				<td width="19%" bgcolor="#FFFFFF" align="left">[${sim}] SIM</td>
				<td width="48.5%" bgcolor="#FFFFFF" align="left">[${nao}] NÃO</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
               	<td width="100%" bgcolor="#FFFFFF" align="left">  </td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#FFFFFF">
	    	<tr>
				<td width="100%" bgcolor="#FFFFFF" align="left"><b>Justificativa e/ou observação : </b>&nbsp;${justif}</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="1" cellspacing="0">
			<tr>
               	<td width="100%" bgcolor="#FFFFFF" align="left">  </td>
			</tr>
		</table>
		 	</td></tr>
		</table>
		
	    <br></br><br>
				
		<!-- INICIO FECHO -->
		<p align="center">${doc.dtExtenso}</p>
		<!-- FIM FECHO -->
		<br>
		<!-- INICIO ASSINATURA -->
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<!-- FIM ASSINATURA -->
		<br>
		/${doc.cadastrante.siglaPessoa}
		</br>
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
