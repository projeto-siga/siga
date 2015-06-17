<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de SOLICITAÇÃO DE ABONO DE FALTAS
	POR MOTIVO DE DOENÇA sem vinculo com ADM Pública - Ultima Alteração 09/04/2008 
 -->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DA LICENÇA">
			<mod:selecao titulo="Atestado em anexo de" var="tipoAtestado"
				opcoes="médico da SJRJ;médico externo" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Endereço" largura="85" maxcaracteres="80"
				var="endereco" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Tel Residencial" largura="15" maxcaracteres="13"
				var="telResidencial" />
			<mod:texto titulo="Celular" largura="15" maxcaracteres="13"
				var="telCelular" />
			<mod:texto titulo="Ramal" largura="10" maxcaracteres="10"
				var="ramalLotacao" />
		</mod:grupo>
		<mod:grupo titulo="Período Solicitado">
			<mod:data titulo="De" var="dataInicio${i}" />
			<mod:data titulo="a" var="dataFim${i}" />
		</mod:grupo>
	</mod:entrevista>

	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 2cm;
	margin-right: 3cm;
	margin-top: 1cm;
	margin-bottom: 1cm;
}
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr><tr><td></tr></td>
		</table>
	
		FIM PRIMEIRO CABECALHO -->

		<table width="100%" border="0" cellpadding="2" cellspacing="0"
			bgcolor="#ffffff">
			<tr>
				<td>
				<p style="font-size: 12pt; text-align: left; font-weight: bold"><b>SOLICITAÇÃO
				DE ABONO DE FALTAS</b></p>
				</td>
			</tr>
			<tr>
				<td>
				<p style="font-size: 12pt; text-align: left"><b>POR MOTIVO
				DE DOENÇA</b></p>
				</td>
			</tr>
			<tr>
				<td>
				<p style="font-size: 9pt; text-align: left"></p>
				</td>
			</tr>
			<tr>
				<td>
				<p style="font-size: 9pt; text-align: left"><b>SERVIDOR
				OCUPANTE EXCLUSIVAMENTE DE CARGO EM</b></p>
				</td>
			</tr>
			<tr>
				<td>
				<p style="font-size: 9pt; text-align: left"><b>COMISSÃO /
				SEM VÍNCULO COM A ADMINISTRAÇÃO PÚBLICA</b></p>
				</td>
			</tr>
			<tr>
				<td>
				<p style="font-size: 11pt; text-align: left"><b> Atestado em
				anexo:&nbsp;&nbsp;&nbsp;&nbsp; <c:choose>
					<c:when test="${tipoAtestado eq 'médico da SJRJ'}">
				[x] de médico da SJRJ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[&nbsp;&nbsp;] de médico externo
			</c:when>
					<c:otherwise>
				[&nbsp;&nbsp;] de médico da SJRJ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[x] de médico externo
			</c:otherwise>
				</c:choose></p>
				</td>
			</tr>
		</table>
		<br />
		<table width="100%" border="1" cellpadding="3"
			style="border: 1px solid black">
			<tr><td colspan="2" style="background-color: #C0C0C0"><b>SERVIDOR</b></td></tr>
			<tr>
				<td width="70%">Nome: <b>${doc.subscritor.descricao}</b></td>
				<td width="30%">Matrícula: <b>${doc.subscritor.matricula}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3"
			style="border: 1px solid black">
			<tr>
				<td width="30%">Lotação: <b>${doc.subscritor.lotacao.descricao}</b></td>
				<td width="70%">Cargo: <b>${doc.subscritor.cargo.nomeCargo}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3"
			style="border: 1px solid black">
			<tr>
				<td>Endereço: <b>${endereco}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3"
			style="border: 1px solid black">
			<tr>
				<td>Tel Residencial: <b>${telResidencial}</b></td>
				<td>Tel Celular: <b>${telCelular}</b></td>
				<td>Ramal da Lotação: <b>${ramalLotacao}</b></td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="3"
			style="border: 1px solid black">
			<tr>
				<td width="40%" align="center">Período Solicitado: a <b>${dataInicio}</b>
				De <b>${dataFim}</b></td>
			</tr>
		</table>
		<table width="100%" border="1">
			<tr>
				<td width="50%" valign="top">Local e Data:<br />
				${doc.dtExtenso}<br />
				<br />
				<br />
				<br />
				</td>
				<td width="50%" valign="top">Assinatura do(a) Servidor(a)<br />
				<br />
				<br />
				<br />
				</td>
			</tr>
		</table>
		<table width="100%" cellpadding="3" border="1">
			<tr>
				<td style="background-color: #C0C0C0"><b> OBSERVAÇÕES </b></td>
			</tr>
			<tr>
				<td>
				<p align="justify" style="font-size: 8pt; line-height: 90%;">Deverá
				ser anexado ao presente formulário atestado médico dentro de
				envelope lacrado, com identificação de "confidencial", e encaminhado
				à SESAU/SRH ou às SEAPOs no prazo de 3 (três) dias úteis apartir do
				1º dia do afastamento.</p><br /><br />
				<p align="justify" style="font-size: 8pt; line-height: 90%;">Somente
				serão aceitos formulários com assinatura do servidor e de seu
				superior hierárquico, com carimbo de identificação.
				</p><br /><br />
				<p align="justify" style="font-size: 8pt; line-height: 90%;"><b>O
				atestado firmado por médico externo</b>, de forma legível, sem rasuras e
				em receituário adequado, deverá conter: nome completo do servidor;
				diagnóstico definitivo ou provável, codificado (CID-10) ou por
				extenso; período de afastamento recomendado e nome completo do
				médico; assinatura, carimbo e nº de registro do CRM.
				</p><br /><br />
				<p align="justify" style="font-size: 8pt; line-height: 90%;">Caso
				sejam concedidos ao servidor <b>licenças ou afastamentos durante
				o período de férias</b>, estas serão suspensas e o período remanescente
				será remarcado pela SRH, indicando-se no 1º dia imediatamente
				posterior ao termino da licença ou afastamento.
				</p><br /><br />
				<p align="justify" style="font-size: 8pt; line-height: 90%;">As
				faltas por motivo de doença poderão ser abonadas pela SJRJ até o
				prazo de 15 dias (ininterruptos). Ultrapassado esse prazo, o
				servidor será encaminhado à perícia médica da Previdência Social
				(Base legal: arts. 59 e 60, §§ 3º e 4º da Lei nº 8.213/91).
				</p><br /><br />
				<p align="justify" style="font-size: 8pt; line-height: 90%;">Se
				o servidor retornar a atividade no 16º dia e, dentro de 60 dias
				desse retorno, precisar se afastar de novo, em virtude da mesma
				doença, não caberá abono de faltas, mas, sim, auxílio doença. (Base
				legal: art. 75 §§ 4º e 5º Decreto 3048/99).
				</p><br /><br />
				<p align="justify" style="font-size: 8pt; line-height: 90%;">Se
				retornar antes de completar o prazo máximo para o abono de faltas
				(15 dias) e, dentro de 60 dias desse retorno, precisar se afastar de
				novo, em virtude da mesma doença, caberá o abono das faltas
				restantes até o período máximo de 15 dias. A partir do dia seguinte
				que completar esse período (15 dias), somente caberá auxílio-doença.
				(Base legal: art. 75 §§ 4º e 5º Decreto 3048/99).
				</p>
				</td>
			</tr>
			<tr>
				<td style="background-color: #C0C0C0"><b>CHEFIA</b></td>
			</tr>
		</table>
		<table width="100%" border="1">
			<tr>
				<td valign="top">
				<p align="left" style="font-size: 8pt;"><b>Observações da
				chefia para a perícia médica</b><br />
				(preenchimento obrigatório, mesmo que seja com "nada a declarar").<br />
				<br />
				OBS:<br />
				<br />
				<br />
				<br />
				<br />
				<br />
				<br />
				<br />
				<br />
				<br />
				<br />
				</p>
				</td>
				<td align="center" valign="top">
				<p style="font-size: 8pt;">Último dia de trabalho do servidor:<br />
				<br />
				<br />
				_______/_______/_______<br />
				Carimbo e assinatura da chefia<br />
				<br />
				<br />
				<br />
				<br />
				<br />
				<br />
				<br />
				</p>
				</td>
			</tr>
		</table>
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
	</mod:documento>
</mod:modelo>
