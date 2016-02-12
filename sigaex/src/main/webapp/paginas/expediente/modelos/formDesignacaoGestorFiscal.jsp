<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>


<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:numero titulo="Processo" var="numProcesso" largura="25" />
		</mod:grupo>
		<mod:grupo titulo="Dados dos Servidores Designados">
			<mod:grupo>
				<mod:pessoa titulo="1) Nome do Gestor (Titular)" var="gestorTitular"
					obrigatorio="Sim" />
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="2) Nome do Gestor (Suplente)"
					var="gestorSuplente" obrigatorio="Sim" />
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="3) Nome do Fiscal Técnico (Titular)"
					var="fiscalTecTitular" />
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="4) Nome do Fiscal Técnico (Suplente)"
					var="fiscalTecSuplente" />
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="5) Nome do Fiscal Administrativo (Titular)"
					var="fiscalAdmTitular" />
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="6) Nome do Fiscal Administrativo (Suplente)"
					var="fiscalAdmSuplente" />
			</mod:grupo>
			<mod:grupo>
				<mod:memo titulo="Outros Fiscais da capital" var="outrosFiscais"
					colunas="80" linhas="2" />
			</mod:grupo>

		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Incluir Fiscais de Subseção Judiciária?"
				var="inclFiscais" opcoes="Não;Sim" reler="ajax" idAjax="fiscaisAjax" />
		</mod:grupo>
		<mod:grupo depende="fiscaisAjax">
			<c:if test="${inclFiscais == 'Sim'}">
				<c:set var="UFCadastrante"
					value="${doc.cadastrante.orgaoUsuario.ufOrgaoUsu}" />
				<c:set var="localidades" value="${f:consultarPorUF(UFCadastrante)}" />
				<c:if test="${not empty localidades}">
					<table border="0" cellspacing="05%" width="90%">
						<c:set var="i" value="${0}" />
						<c:forEach var="localid" items="${localidades}">
							<c:set var="i" value="${i+1}" />
							<mod:oculto var="nomeLocalid${i}" valor="${localid.nmLocalidade}" />
							<tr>
								<td>${i}) ${localid.nmLocalidade}</td>
							</tr>
							<tr>
								<td width="20%" align="left">Nome do Fiscal Titular:</td>
								<td width="80%" align="left"><mod:pessoa titulo=""
									var="fiscalTitular${i}" /></td>
							</tr>
							<tr>
								<td align="left">Nome(s) do(s) Fiscal(is) Suplente(s):</td>
								<td align="left"><mod:pessoa titulo=""
									var="fiscalSuplente${i}" /></td>
							</tr>
							<tr>
								<td></td>
								<td align="left"><mod:pessoa titulo=""
									var="fiscalSuplente2${i}" /></td>
							</tr>
							<tr>
								<td colspan="2">
								<hr color="#FFFFFF" />
								</td>
							</tr>
						</c:forEach>
						<mod:oculto var="contadorDeLocalidades" valor="${i}" />
					</table>
				</c:if>
				<mod:mensagem titulo="Obs."
					texto="Neste caso, o gestor do contrato deverá cientificar cada fiscal/suplente de subseção designado" />
			</c:if>
		</mod:grupo>
	</mod:entrevista>

	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr><br /><br /><br /></tr>
						<tr>
							<td align="right"><font style="font-family:Arial;font-size:11pt;font-weight:bold;">
							${doc.codigo}</font></td>
						</tr>											
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<br />

		<p>Ref. Processo : ${numProcesso}</p>

		<p style="text-align: center; font: bold;"><b>DESIGNAÇÃO DE
		GESTOR/FISCAL DE CONTRATO </b></p>

		<p>Sr(a) Diretor(a) da Subsecretaria,</p>

		<p>Solicito, nos termos do art. 67 da Lei nº 8.666/93, a
		designação dos servidores abaixo relacionados para a gerência e
		fiscalização do presente ajuste:</p>
		<br />

		<table border="1" width="100%" align="left" cellpadding="5">
			<tr bgcolor="#999999">
				<td colspan="2"><b>GESTOR</b></td>
				<c:if test="${!doc.eletronico}">
					<td width="10%">ciência</td>
				</c:if>
			</tr>
			<tr>
				<td width="69%">Titular:
				${requestScope['gestorTitular_pessoaSel.descricao']}</td>
				<td width="21%">Matrícula:
				${f:pessoa(requestScope['gestorTitular_pessoaSel.id']).matricula}</td>
				<c:if test="${!doc.eletronico}">
					<td></td>
				</c:if>
			</tr>
			<tr>
				<td>Suplente:
				${requestScope['gestorSuplente_pessoaSel.descricao']}</td>
				<td>Matrícula:
				${f:pessoa(requestScope['gestorSuplente_pessoaSel.id']).matricula}</td>
				<c:if test="${!doc.eletronico}">
					<td></td>
				</c:if>
			</tr>
			<c:if test="${not empty requestScope['fiscalTecTitular_pessoaSel.id']}">
				<tr bgcolor="#999999">
					<td colspan="2"><b>FISCAL TÉCNICO</b></td>
					<c:if test="${!doc.eletronico}">
						<td width="10%">ciência</td>
					</c:if>
				</tr>
				<tr>
					<td width="69%">Titular:
					${requestScope['fiscalTecTitular_pessoaSel.descricao']}</td>
					<td width="21%">Matrícula:
					${f:pessoa(requestScope['fiscalTecTitular_pessoaSel.id']).matricula}</td>
					<c:if test="${!doc.eletronico}">
						<td></td>
					</c:if>
				</tr>
				<tr>
					<td>Suplente:
					${requestScope['fiscalTecSuplente_pessoaSel.descricao']}</td>
					<td>Matrícula:
					${f:pessoa(requestScope['fiscalTecSuplente_pessoaSel.id']).matricula}</td>
					<c:if test="${!doc.eletronico}">
						<td></td>
					</c:if>
				</tr>
			</c:if>
			<c:if test="${not empty requestScope['fiscalAdmTitular_pessoaSel.id']}">
				<tr bgcolor="#999999">
					<td colspan="2"><b>FISCAL ADMINISTRATIVO</b></td>
					<c:if test="${!doc.eletronico}">
						<td width="10%">ciência</td>
					</c:if>
				</tr>
				<tr>
					<td width="69%">Titular:
					${requestScope['fiscalAdmTitular_pessoaSel.descricao']}</td>
					<td width="21%">Matrícula:
					${f:pessoa(requestScope['fiscalAdmTitular_pessoaSel.id']).matricula}</td>
					<c:if test="${!doc.eletronico}">
						<td></td>
					</c:if>
				</tr>
				<tr>
					<td>Suplente:
					${requestScope['fiscalAdmSuplente_pessoaSel.descricao']}</td>
					<td>Matrícula:
					${f:pessoa(requestScope['fiscalAdmSuplente_pessoaSel.id']).matricula}</td>
					<c:if test="${!doc.eletronico}">
						<td></td>
					</c:if>
				</tr>
			</c:if>
			<c:if test="${not empty outrosFiscais}">
				<tr bgcolor="#999999">
					<td colspan="2"><b>OUTROS FISCAIS DA CAPITAL</b></td>
					<c:if test="${!doc.eletronico}">
						<td width="10%">ciência</td>
					</c:if>
				<tr>
					<td colspan="2">${outrosFiscais}</td>
					<c:if test="${!doc.eletronico}">
						<td></td>
					</c:if>
				</tr>
			</c:if>
		</table>

		<ww:if test="${inclFiscais == 'Sim'}">
<%--			<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" /> --%>
			<br> <br> <br>
			<table border="1" width="100%" align="left" style="float:none; clear:both;">
				<tr>
					<th bgcolor="#999999" colspan="2"><b>FISCAL DE SUBSEÇÃO
					JUDICIÁRIA</b></th>
				</tr>
				<c:forEach var="i" begin="1" end="${contadorDeLocalidades}">
					<c:if
						test="${not empty requestScope[f:concat(f:concat('fiscalTitular',i),'_pessoaSel.id')]}">
						<tr>
							<td width="25%">${requestScope[f:concat('nomeLocalid',i)]}</td>
							<td width="75%">
							<table border="0" cellspacing="0" width="100%">
								<tr>
									<td width="73%" align="left">Titular:
									${f:maiusculasEMinusculas(requestScope[f:concat(f:concat('fiscalTitular',i),'_pessoaSel.descricao')])}</td>
									<td width="27%" align="left">Matrícula:${f:pessoa(requestScope[f:concat(f:concat('fiscalTitular',i),'_pessoaSel.id')]).matricula}</td>
								</tr>
								<tr>
									<td align="left">Suplente(s):
									${f:maiusculasEMinusculas(requestScope[f:concat(f:concat('fiscalSuplente',i),'_pessoaSel.descricao')])}</td>
									<td align="left">Matrícula:${f:pessoa(requestScope[f:concat(f:concat('fiscalSuplente',i),'_pessoaSel.id')]).matricula}</td>
								</tr>
								<c:if test="${not empty requestScope[f:concat(f:concat('fiscalSuplente2',i),'_pessoaSel.id')]}">
									<tr>
										<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;												
										${f:maiusculasEMinusculas(requestScope[f:concat(f:concat('fiscalSuplente2',i),'_pessoaSel.descricao')])}</td>
										<td align="left">Matrícula:${f:pessoa(requestScope[f:concat(f:concat('fiscalSuplente2',i),'_pessoaSel.id')]).matricula}</td>
									</tr>
								</c:if>
							</table>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</table>
			<b>OBS.: Neste caso, o gestor do contrato deverá cientificar cada
			fiscal/suplente de subseção designado.</b>
		Nesses termos, aguardo deferimento.
			<br>
			<br>
		</ww:if>
		<ww:else>
			<br>
			<br>
			<br>			
			Nesses termos, aguardo deferimento.
		</ww:else>


		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />

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
