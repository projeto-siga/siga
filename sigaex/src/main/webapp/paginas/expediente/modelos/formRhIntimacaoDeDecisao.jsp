<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>

<mod:modelo>

	<mod:entrevista>
		</br>
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
				<c:if test="${tipoDeIntimacao == 'Afastado do serviço'}">
					<mod:texto titulo="Endereço Residencial" var="endereco" largura="50" />
				</c:if>
			</mod:grupo>
			</br>
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
							<td align="left"><p style="font-family:Arial;font-size:11pt;font-weight:bold;" >Av.Rio Branco 243 - Anexo I - 9º andar - Cep: 22240-009</p></td>
						</tr>
						<tr>
							<td align="center">
								<br/>
								<br/>
								<p style="font-family:Arial;font-size:11pt;font-weight:bold;" >INTIMAÇÃO DA DECISÃO</p>
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
			<b>INTIMAÇÃO DE</b> ${f:pessoa(requestScope['matricula_pessoaSel.id']).nomePessoa}
			<c:if test="${tipoDeIntimacao == 'Afastado do serviço'}">
				<br/>
				<b>ENDEREÇO RESIDENCIAL:</b> ${endereco}
			</c:if>
		
		<p style="TEXT-INDENT: 2cm" align="justify">
			Tendo em vista o disposto nos arts. 6º, parágrafo único, e 7º da Resolução nº 68 de 27/07/2009 do 
			Conselho de Justiça Federal (CJF), INTIMO V.Exa. para, querendo, interpor RECURSO, nos autos do 
			Processo Administrativo em epígrafe, que versa sobre a devolução de valores indevidamente pagos, 
			no prazo máximo de 15 (quinze) dias, a contar do recebimento desta.
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
			Esta Intimação assinada deverá ser devolvida, com urgência, a esta Unidade, que, após o prazo 
			acima previsto, prosseguirá com a tramitação do processo, independentemente de sua manifestação.
		</p>
		<br />
		<br />
		<p style="TEXT-INDENT: 2cm" align="justify">
			EXPEDIDO no Município do Rio de Janeiro, em	${doc.dtExtensoSemLocalidade}, pela Subsecretaria de Gestão de Pessoas.
		</p>
		<br />
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

