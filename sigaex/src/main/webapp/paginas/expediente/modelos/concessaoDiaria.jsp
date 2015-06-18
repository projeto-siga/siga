<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!--insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_CLASSIFICACAO, ID_FORMA_DOC) values (554,'Pedido de ConcessÃ£o de DiÃ¡ria', 'concessaoDiaria.jsp',null,3);-->

<mod:modelo>
	<mod:entrevista>
		<mod:obrigatorios />
		<mod:grupo>
			<mod:selecao titulo="Tipo da proposta" var="tipoProposta"
				opcoes="Inicial;ProrrogaÃ§Ã£o" reler="sim" />
		</mod:grupo>
		<hr />
		<mod:grupo>
			<mod:grupo titulo="Dados do beneficiÃ¡rio">
				<mod:pessoa titulo="BeneficiÃ¡rio" var="beneficiario"  />
			</mod:grupo>
			<mod:grupo titulo="Dados complementares para o cÃ¡lculo">
				<mod:selecao titulo="Cargo" var="cargoBeneficiario"
					opcoes="ANALISTA JUDICIÃRIO;JUIZ FEDERAL TITULAR;JUIZ FEDERAL SUBSTITUTO;TÃ‰CNICO JUDICIÃRIO;" />
				<mod:selecao titulo="FunÃ§Ã£o" var="funcaoBeneficiario"
					opcoes="SEM FUNÃ‡ÃƒO;ASSISTENTE I;ASSISTENTE II;ASSISTENTE III;ASSISTENTE IV;COORDENADOR;DIRETOR DE SECRETARIA;DIRETOR DE SUBSECRETARIA;SUPERVISOR;" />
			</mod:grupo>

			<mod:grupo>
				<mod:selecao titulo="Executor de Mandados" var="executor"
					opcoes="NÃ£o;Sim" />
			</mod:grupo>
			<mod:grupo>
				<br />
				<mod:texto titulo="Ramal" var="ramal" largura="9" maxcaracteres="9" obrigatorio="Sim"/>
				<mod:texto titulo="Banco" var="banco" largura="10" maxcaracteres="8"
					obrigatorio="Sim" />
				<mod:texto titulo="AgÃªncia" var="agencia" largura="10"
					maxcaracteres="8" obrigatorio="Sim" />
				<mod:texto titulo="Conta" var="conta" largura="10" maxcaracteres="8"
					obrigatorio="Sim" />
			</mod:grupo>
		</mod:grupo>
		<hr />
		<mod:grupo titulo="Dados do serviÃ§o">
			<mod:memo titulo="Local do serviÃ§o (destino)" var="localServico"
				linhas="2" colunas="60" />
			<mod:memo titulo="Tipo do serviÃ§o (descriÃ§Ã£o do evento)"
				var="tipoServico" linhas="2" colunas="60" />
		</mod:grupo>
		<hr />
		<mod:grupo titulo="PerÃ­odo de afastamento">
			<mod:grupo titulo="InÃ­cio">
				<mod:texto titulo="HorÃ¡rio" var="horarioInicioAfastamento"
					largura="10" maxcaracteres="8" obrigatorio="Sim" />
				<mod:data titulo="Data" var="dataInicioAfastamento"
					obrigatorio="Sim" />
			</mod:grupo>
			<mod:grupo titulo="Fim">
				<mod:texto titulo="HorÃ¡rio" var="horarioFimAfastamento" largura="10"
					maxcaracteres="8" obrigatorio="Sim" />
				<mod:data titulo="Data" var="dataFimAfastamento" obrigatorio="Sim" />
			</mod:grupo>
			<mod:grupo titulo="Pernoite">
				<mod:selecao titulo="Necessidade de pernoite" var="pernoite"
					opcoes="Sem Pernoite; Com pernoite anterior ao evento; Com pernoite posterior ao evento; Com pernoite nas noites anterior e posterior ao evento"
					reler="sim" />
			(JUSTIFICAR a necessidade de pernoite em documento a ser anexado no SIGA-DOC e assinado digitalmente)

			</mod:grupo>
		</mod:grupo>
		<hr />
		<mod:grupo>
			<mod:selecao titulo="Categoria da passagem" var="categoria"
				opcoes="NÃ£o hÃ¡;AÃ©rea;RodoviÃ¡ria" reler="sim" />
		</mod:grupo>
		<c:if test="${categoria=='AÃ©rea'}">
			<mod:grupo titulo="Passagem aÃ©rea">
				<mod:grupo titulo="Ida">
					<mod:texto titulo="HorÃ¡rio" var="horarioIda" largura="10"
						maxcaracteres="8" obrigatorio="Sim" />
					<mod:data titulo="Data" var="dataIda" obrigatorio="Sim" />
				</mod:grupo>
				<mod:grupo titulo="Volta">
					<mod:texto titulo="HorÃ¡rio" var="horarioVolta" largura="10"
						maxcaracteres="8" obrigatorio="Sim" />
					<mod:data titulo="Data" var="dataVolta" obrigatorio="Sim" />
				</mod:grupo>
				<mod:grupo titulo="Aeroporto">
					<mod:selecao titulo="Aeroporto" var="aeroporto"
						opcoes="Santos Dumont;GaleÃ£o" reler="sim" />
				</mod:grupo>

				<br />

				<mod:caixaverif var="comprovante"
					titulo="Estou ciente que devo devolver os comprovantes de embarque Ã  SG em 5 dias apÃ³s o retorno da viagem, de acordo com a resoluÃ§Ã£o 89/2009 - CJF, art. 114."
					obrigatorio="Sim" reler="sim" />
			</mod:grupo>
		</c:if>
		<hr />
		<mod:grupo titulo="Carro oficial">

			<mod:selecao titulo="Uso do carro" var="carroOficial"
				opcoes="Sem uso de carro oficial (solicito adicional de deslocamento - art. 103, Res. 89/2009-CJF.);Sem carro oficial;Somente na origem;Somente no destino;Na origem e no destino"
				reler="sim" />


		</mod:grupo>


	</mod:entrevista>
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		</head>
		<body>

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
		<tr bgcolor="#FFFFFF">
			<td align="center"><b>PROPOSTA E CONCESSÃƒO DE DIÃRIAS</b></td>
		<tr>
		<tr bgcolor="#FFFFFF">
			<td align="center"><b>(${tipoProposta})</b></td>
		<tr>
	
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="right"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">PCD N&ordm; ${doc.codigo}</p></td>
						</tr>
						<tr>
							<td align="right"><mod:letra tamanho="11pt"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
		<c:set var="pesBeneficiario"
			value="${f:pessoa(requestScope['beneficiario_pessoaSel.id'])}" />
		<b>BENEFICIÃRIO</b>
		<table border="0" width="100%" bordercolor="#000000" bgcolor="#000000"
			align="" cellpadding="2" cellspacing="1">
			<tr>
				<td bgcolor="#FFFFFF" width="25%"><b>Nome: </b></td>
				<td bgcolor="#FFFFFF">${pesBeneficiario.nomePessoa}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF"><b>MatrÃ­cula: </b>
				${pesBeneficiario.matricula}</td>
				<td bgcolor="#FFFFFF"><b>E-mail: </b>
				${pesBeneficiario.emailPessoa}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="25%"><b>LotaÃ§Ã£o: </b>
				${pesBeneficiario.lotacao.sigla}</td>
				<td bgcolor="#FFFFFF"><b>Ramal: </b> ${ramal}</td>
			</tr>

			<tr>
				<td bgcolor="#FFFFFF"><b>Cargo efetivo: </b></td>
				<td bgcolor="#FFFFFF">${cargoBeneficiario}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF"><b>Cargo/Func. Com.: </b></td>
				<td bgcolor="#FFFFFF">${funcaoBeneficiario}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF"><b>Executor Mandados: </b></td>
				<td bgcolor="#FFFFFF">${executor}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF"><b>CPF </b></td>
				<td bgcolor="#FFFFFF">${f:formatarCPF(pesBeneficiario.cpfPessoa)}</td>
			</tr>
		</table>

		<b>DADOS BANCÃRIOS</b>
		<table border="0" width="100%" bordercolor="#000000" bgcolor="#000000"
			align="" cellpadding="2" cellspacing="1">
			<tr>
				<td bgcolor="#FFFFFF" align="center" width="15%"><b>Banco:
				</b> ${banco}</td>
				<td bgcolor="#FFFFFF" align="center" width="15%"><b>Agencia:
				</b> ${agencia}</td>
				<td bgcolor="#FFFFFF" align="center" width="15%"><b>C/C: </b>
				${conta}</td>
			</tr>
		</table>

		<b>DADOS DO SERVIÃ‡O</b>
		<table border="0" width="100%" bordercolor="#000000" bgcolor="#000000"
			align="" cellpadding="2" cellspacing="1">
			<tr>
				<td bgcolor="#FFFFFF" width="30%"><b>Local do serviÃ§o
				(destino): </b></td>
				<td bgcolor="#FFFFFF">${localServico}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF"><b>Tipo do serviÃ§o (descriÃ§Ã£o do
				evento): </b></td>
				<td bgcolor="#FFFFFF">${tipoServico}</td>
			</tr>
		</table>

		<b>PERÃODO DE AFASTAMENTO</b>
		<table border="0" width="100%" bordercolor="#000000" bgcolor="#000000"
			align="" cellpadding="2" cellspacing="1">
			<tr>
				<td bgcolor="#FFFFFF" width="40%"><b>InÃ­cio do afastamento:
				</b></td>
				<td bgcolor="#FFFFFF">${dataInicioAfastamento} Ã s
				${horarioInicioAfastamento}h</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF"><b>Fim do afastamento: </b></td>
				<td bgcolor="#FFFFFF">${dataFimAfastamento} Ã s
				${horarioFimAfastamento}h</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF"><b>Pernoite: </b></td>
				<td bgcolor="#FFFFFF">${pernoite}</td>
			</tr>
		</table>

		<b>PASSAGEM</b>
		<table border="0" width="100%" bordercolor="#000000" bgcolor="#000000"
			align="" cellpadding="2" cellspacing="1">
			<tr>
				<td bgcolor="#FFFFFF" width="30%"><b>Categoria da passagem:
				</b></td>
				<td bgcolor="#FFFFFF">${categoria}</td>
			</tr>
			<c:if test="${categoria=='AÃ©rea'}">
				<tr>
					<td bgcolor="#FFFFFF"><b>Ida: </b></td>
					<td bgcolor="#FFFFFF">${dataIda} Ã s ${horarioIda}h</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF"><b>Volta: </b></td>
					<td bgcolor="#FFFFFF">${dataVolta} Ã s ${horarioVolta}h</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF"><b>Aeroporto: </b></td>
					<td bgcolor="#FFFFFF">${aeroporto}</td>
				</tr>
			</c:if>
		</table>

		<b>CARRO OFICIAL</b>
		<table border="0" width="100%" bordercolor="#000000" bgcolor="#000000"
			align="" cellpadding="2" cellspacing="1">
			<tr>
				<td bgcolor="#FFFFFF" width="25%"><b>Carro oficial: </b></td>
				<td bgcolor="#FFFFFF">${carroOficial}</td>
			</tr>
		</table>
		<BR />
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


