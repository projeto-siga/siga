<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
.
<mod:modelo>
	
	<mod:entrevista>
		<br/>
		<mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Tipo de Intimação" var="tipoDeIntimacao" 
					opcoes="No ambiente de trabalho;Afastado do serviço" reler='sim'/>
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Processo Administrativo Nº" var="numProcesso" largura="20" />
			</mod:grupo>
			<mod:grupo>
				<mod:pessoa titulo="Matrícula do Servidor/Juiz Federal" var="matricula" reler="sim"
					buscarFechadas="true" />
			</mod:grupo>
			<mod:grupo>
				<mod:memo titulo="Motivo da Notificação" var="motivoNotificacao" linhas="4" colunas="60" />
			</mod:grupo>
			<mod:grupo>
				<c:if test="${tipoDeIntimacao == 'Afastado do serviço'}">
					<mod:texto titulo="Endereço Residencial" var="endereco" largura="50" />
				</c:if>
			</mod:grupo>
			<br/>
		</mod:grupo>
	</mod:entrevista>

	<mod:documento>
	
	<!-- INICIO PRIMEIRO CABECALHO
		
		<table width="100%" border="0"  bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<br/><br/>
					<table width="100%" border="0" >
						<tr>
							<td align="left"><p style="font-family:Arial;font-size:11pt;font-weight:bold;" >SUBSECRETARIA DE GESTÃO DE PESSOAS (SGP)</p></td>
						</tr>
						<tr>
							<td align="left"><p style="font-family:Arial;font-size:11pt;font-weight:bold;" >Rua Dom Gerardo, 46 - 9º andar  - CEP 20.090-030</p></td>
						</tr>
						<tr>
							<td align="center">
								<br/>
								<br/>
								<p style="font-family:Arial;font-size:11pt;font-weight:bold;" >NOTIFICAÇÃO DE DÉBITO</p>
							</td>
						</tr>
				<tr><td></td>
				</tr>
				<tr><td></td>
				</tr>
				<tr><td></td>
				</tr>
					</table>
				</td>
			</tr>
		</table>
		
		FIM PRIMEIRO CABECALHO -->
		
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
		<br />
		
			PROCESSO ADMINISTRATIVO Nº: ${numProcesso}
			<br/>
			<b>NOTIFICAÇÃO DE</b> ${f:pessoa(requestScope['matricula_pessoaSel.id']).nomePessoa}
			<c:if test="${tipoDeIntimacao == 'Afastado do serviço'}">
				<br/>
				<b>ENDEREÇO RESIDENCIAL:</b> ${endereco}
			</c:if>
		
		<p style="TEXT-INDENT: 2cm" align="justify">
			Tendo em vista o disposto no art. 2º da Resolução nº 68 de 27/07/2009 do Conselho da Justiça 
			Federal (CJF), NOTIFICO V.Sa. sobre a abertura, nesta Subsecretaria de Gestão de Pessoas, do 
			Processo Administrativo em epígrafe, cujo objeto é a devolução ao erário de valores apurados, 
			em decorrência de ${motivoNotificacao}.
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
			Ressalto que as informações pertinentes ao Processo Administrativo encontram-se anexadas a esta 
			Notificação, a fim de dar-lhe ciência de seu conteúdo, possibilitando-lhe a manifestação, inclusive 
			com a apresentação de provas, no prazo máximo de 10 (dez) dias, contados do recebimento desta. 
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
			O débito em questão poderá ser objeto de parcelamento, nos termos do art. 46 da Lei nº 8.112/90, caso seja requerido por V.Sa.
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
			Esta Notificação assinada deverá ser devolvida, com urgência, a esta Unidade, que, após o prazo 
			acima previsto, prosseguirá com a tramitação do processo, independentemente de sua manifestação.
		</p>
		<p style="font-family: Arial; font-size: 11pt;"
		align="center">
			Rio de Janeiro, ${doc.dtExtensoSemLocalidade}
		</p>
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<br/>
		<p style="font-family: Arial; font-size: 11pt; font-weight: bold;"
		align="center">
			${f:pessoa(requestScope['matricula_pessoaSel.id']).nomePessoa} - ${f:pessoa(requestScope['matricula_pessoaSel.id']).matricula}
		</p>
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
	</mod:documento>
</mod:modelo>

