<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!--INCLUSÃO DO MODELO: insert into SIGA.EX_MODELO (ID_MOD, NM_MOD, NM_ARQ_MOD, ID_CLASSIFICACAO, ID_FORMA_DOC) values (560,'Cálculo de Pedido de Concessão de Diária (PCD)', 'calculoPCD.jsp',null,3);-->
<!--CONFIGURAÇÃO PARA SOLICITAR PUBLICAÇÃO NO BI: insert into ex_configuracao (id_configuracao, id_tp_mov,id_mod,id_sit_configuracao,id_tp_configuracao ) values (1325,36,560,1,1) -->
<c:set var="ID_MODELO_PAI" value="554" />
<mod:modelo>
	<mod:entrevista>
		<c:choose>
			<c:when test="${doc.exMobilPai.doc.exModelo.idMod == ID_MODELO_PAI}">
				<mod:obrigatorios />
				<mod:grupo>
					<mod:monetario var="descontoSalario" formataNum="sim"
						extensoNum="sim" titulo="Desconto Salario (WEmul)" reler="sim" />
				</mod:grupo>
				<mod:grupo>
					<mod:monetario var="valorConcedido" formataNum="sim"
						extensoNum="sim" titulo="Valor Concedido (WEmul)" reler="sim" />
				</mod:grupo>
			</c:when>
			<c:otherwise>
		O PAI DESTE DOCUMENTO DEVE SER UM PEDIDO DE CONCESSÃO DE DÁRIAS
	</c:otherwise>
		</c:choose>
	</mod:entrevista>
	<mod:documento>
		<c:choose>
			<c:when test="${doc.exMobilPai.doc.exModelo.idMod == ID_MODELO_PAI}">
				<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
				<head>
				</head>
				<body>
				<!-- Utiliza a classe CalculoPCD para efetuar os cálculos -->
				<jsp:useBean id="calculo"
					class="br.gov.jfrj.siga.ex.util.CalculoPCD" scope="request" />
				<jsp:setProperty name="calculo" property="cargo"
					value="${doc.exMobilPai.doc.form['cargoBeneficiario']}" />
				<jsp:setProperty name="calculo" property="funcao"
					value="${doc.exMobilPai.doc.form['funcaoBeneficiario']}" />
				<jsp:setProperty name="calculo" property="beneficiario"
					value="${f:pessoa(doc.exMobilPai.doc.form['beneficiario_pessoaSel.id'])}" />
				<jsp:setProperty name="calculo" property="dataInicio"
					value="${doc.exMobilPai.doc.form['dataInicioAfastamento']}" />
				<jsp:setProperty name="calculo" property="dataFim"
					value="${doc.exMobilPai.doc.form['dataFimAfastamento']}" />
				<c:choose>
					<c:when
						test="${doc.exMobilPai.doc.form['carroOficial'] == 'Sem uso de carro oficial (solicito adicional de deslocamento - art. 103, Res. 89/2009-CJF.)'}">
						<jsp:setProperty name="calculo" property="solicitaAuxTransporte"
							value="true" />
						<jsp:setProperty name="calculo" property="carroOficial"
							value="false" />
					</c:when>
					<c:when
						test="${doc.exMobilPai.doc.form['carroOficial'] == 'Sem carro oficial'}">
						<jsp:setProperty name="calculo" property="solicitaAuxTransporte"
							value="false" />
						<jsp:setProperty name="calculo" property="carroOficial"
							value="false" />
					</c:when>
					<c:when
						test="${doc.exMobilPai.doc.form['carroOficial'] == 'Somente na origem'}">
						<jsp:setProperty name="calculo" property="solicitaAuxTransporte"
							value="false" />
						<jsp:setProperty name="calculo" property="carroOficial"
							value="true" />
					</c:when>
					<c:when
						test="${doc.exMobilPai.doc.form['carroOficial'] == 'Somente no destino'}">
						<jsp:setProperty name="calculo" property="solicitaAuxTransporte"
							value="false" />
						<jsp:setProperty name="calculo" property="carroOficial"
							value="true" />
					</c:when>
					<c:when
						test="${doc.exMobilPai.doc.form['carroOficial'] == 'Na origem e no destino'}">
						<jsp:setProperty name="calculo" property="solicitaAuxTransporte"
							value="false" />
						<jsp:setProperty name="calculo" property="carroOficial"
							value="true" />
					</c:when>
				</c:choose>

				<c:choose>
					<c:when test="${pernoite == 'Com pernoite anterior ao evento'}">
						<jsp:setProperty name="calculo" property="pernoite" value="1" />
					</c:when>
					<c:when test="${pernoite == 'Com pernoite posterior ao evento'}">
						<jsp:setProperty name="calculo" property="pernoite" value="2" />
					</c:when>
					<c:when
						test="${pernoite == 'Com pernoite nas noites anterior e posterior ao evento'}">
						<jsp:setProperty name="calculo" property="pernoite" value="3" />
					</c:when>
				</c:choose>


				<c:if test='${descontoSalario == ""}'>
					<c:set var="descontoSalario" value="0,00" />
				</c:if>
				<c:if test='${valorConcedido == ""}'>
					<c:set var="valorConcedido" value="0,00" />
				</c:if>

				<c:set var="descontoSalario"
					value="${f:replace(descontoSalario,'.','')}" />
				<c:set var="valorConcedido"
					value="${f:replace(valorConcedido,'.','')}" />

				<jsp:setProperty name="calculo" property="descontoSalario"
					value="${f:replace(descontoSalario,',','.')}" />
				<jsp:setProperty name="calculo" property="valorConcedido"
					value="${f:replace(valorConcedido,',','.')}" />
				<c:choose>
					<c:when test="${doc.exMobilPai.doc.form['executor'] == 'Sim'}">
						<jsp:setProperty name="calculo" property="executorMandado"
							value="true" />
					</c:when>
					<c:when test="${doc.exMobilPai.doc.form['executor'] == 'Não'}">
						<jsp:setProperty name="calculo" property="executorMandado"
							value="false" />
					</c:when>
				</c:choose>

				<!-- FIM DO USO DO JAVA BEAN -->

				<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
		<tr bgcolor="#FFFFFF">
			<td align="center"><b>CÁLCULO DE DIÁRIAS</b></td>
		<tr>
	
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="right"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">CALC N&ordm; ${doc.codigo}</p></td>
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

				<br />
				<b>PCD</b>

				<table width="100%">
					<tr>
						<td><b>Número:</b></td>
						<td>${doc.exMobilPai.doc.sigla}</td>
					</tr>
				</table>
				<c:set var="pesProponente"
					value="${f:pessoa(doc.exMobilPai.doc.titular.id)}" />
				<b>PROPONENTE</b>
				<table border="0" width="100%" bordercolor="#000000"
					bgcolor="#000000" align="" cellpadding="2" cellspacing="1">
					<tr>
						<td bgcolor="#FFFFFF" width="30%"><b>Nome: </b></td>
						<td bgcolor="#FFFFFF">${pesProponente.nomePessoa}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" width="30%"><b>Matrícula: </b></td>
						<td bgcolor="#FFFFFF">${pesProponente.matricula}</td>
					</tr>
				</table>
				<c:set var="pesBeneficiario"
					value="${f:pessoa(doc.exMobilPai.doc.form['beneficiario_pessoaSel.id'])}" />
				<b>BENEFICIÁRIO</b>
				<table border="0" width="100%" bordercolor="#000000"
					bgcolor="#000000" align="" cellpadding="2" cellspacing="1">
					<tr>
						<td bgcolor="#FFFFFF" width="30%"><b>Nome: </b></td>
						<td bgcolor="#FFFFFF">${pesBeneficiario.nomePessoa}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" width="30%"><b>Matrícula: </b></td>
						<td bgcolor="#FFFFFF">${pesBeneficiario.matricula}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" width="30%"><b>Cargo: </b></td>
						<td bgcolor="#FFFFFF">${doc.exMobilPai.doc.form['cargoBeneficiario']}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" width="30%"><b>Função: </b></td>
						<td bgcolor="#FFFFFF">${doc.exMobilPai.doc.form['funcaoBeneficiario']}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" width="30%"><b>Executor de
						mandados: </b></td>
						<td bgcolor="#FFFFFF">${doc.exMobilPai.doc.form['executor']}</td>
					</tr>
				</table>
				<b>CÁLCULO</b>
				<table border="0" width="100%" bordercolor="#000000"
					bgcolor="#000000" align="" cellpadding="2" cellspacing="1">
					<tr>
						<td bgcolor="#AAAAAA" align="center" width="35%"><b>Tipo</b></td>
						<td bgcolor="#AAAAAA" align="center" width="20%"><b>Valor
						Unitário</b></td>
						<td bgcolor="#AAAAAA" align="center" width="20%"><b>Nº
						Diárias/Dias</b></td>
						<td bgcolor="#AAAAAA" align="center" width="25%"><b>Total
						Parcial</b></td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" align="center"><b>Diária (R$)</b></td>
						<td bgcolor="#FFFFFF" align="right">
						${f:floatParaMonetario(calculo.valorDiaria)}</td>
						<td bgcolor="#FFFFFF" align="center">${f:replace(calculo.numeroDiarias,".",",")}</td>
						<td bgcolor="#FFFFFF" align="right">${f:floatParaMonetario(calculo.calculoDiarias)}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" align="center"><b>Adicional
						Deslocamento (R$)</b></td>
						<td bgcolor="#FFFFFF" align="right">${f:floatParaMonetario(calculo.valorDeslocamento)}</td>
						<td bgcolor="#FFFFFF" align="center">${calculo.numeroDeslocamentos}</td>
						<td bgcolor="#FFFFFF" align="right">${f:floatParaMonetario(calculo.calculoDeslocamento)}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" align="center"><b>Auxílio-Alimentação(R$)</b></td>
						<td bgcolor="#FFFFFF" align="right">${f:floatParaMonetario(calculo.valorAuxilioAlimentacaoDiario)}</td>
						<td bgcolor="#FFFFFF" align="center">${f:replace(calculo.numeroDiariasDesconto,".0","")}</td>
						<td bgcolor="#FFFFFF" align="right">${f:floatParaMonetario(calculo.calculoDescontoAlimentacao)}</td>
						</tr> <tr> <td bgcolor="#FFFFFF"
						align="center"><b>Auxílio-Transporte (R$)</b></td> <td
						bgcolor="#FFFFFF"
						align="right">${f:floatParaMonetario(calculo.valorAuxilioTransporteDiario)}</td>
						<td bgcolor="#FFFFFF" align="center">${f:replace(calculo.numeroDiariasDesconto,".0","")}</td>
						<td bgcolor="#FFFFFF" align="right">${f:floatParaMonetario(calculo.calculoDescontoTransporte)}</td>
						</tr> </table> <b>DADOS COMPLEMENTARES</b> <table border="0"
						width="100%"> <tr> <td bgcolor="#FFFFFF" align="right"
						width="75%"><b>Desconto Salario (WEmul) (R$)</b></td> <td
						bgcolor="#FFFFFF"
						align="right">${f:floatParaMonetario(f:replace(descontoSalario,',','.'))}</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF" align="right" width="75%"><b>Valor
						Concedido (WEmul) (R$)</b></td>
						<td bgcolor="#FFFFFF" align="right">${f:floatParaMonetario(f:replace(valorConcedido,',','.'))}</td>
					</tr>
				</table>
				<br />
				<hr />

				<table border="0" width="100%">
					<tr>
						<td bgcolor="#FFFFFF" align="right" width="75%"><b>TOTAL
						A PAGAR (R$)</b></td>
						<td bgcolor="#FFFFFF" align="right">${f:floatParaMonetario(calculo.calculoValorLiquido)}</td>
					</tr>
				</table>
				<br />
				<br />

				<!-- O COMENTÁRIO ABAIXO REFERE-SE ÀS TAGS DE PUBLICAÇÃO NO BOLETIM INTERNO. NÃO REMOVA-O!! -->

				<!-- INICIO NUMERO ${doc.codigo} FIM NUMERO -->
				<!-- INICIO CORPO 
		<div style="text-align: left;">		
		BENEFICIÁRIO:${pesBeneficiario.nomePessoa} - 
		${doc.exMobilPai.doc.form['cargoBeneficiario']} - ${doc.exMobilPai.doc.form['funcaoBeneficiario']}<br/>
		ENTIDADE/LOCAL: ${doc.exMobilPai.doc.form['localServico']}<br/>
		PERÍODO:${doc.exMobilPai.doc.form['dataInicioAfastamento']} a ${doc.exMobilPai.doc.form['dataFimAfastamento']}<br/>
		TRANSPORTE:${doc.exMobilPai.doc.form['categoria']}<br/>
		VALOR DA DIÁRIA:${f:floatParaMonetario(calculo.valorDiaria)}<br/>
		Nº DE DIÁRIA(S): ${f:replace(calculo.numeroDiarias,".",",")} + ${calculo.numeroDeslocamentos} ADICIONAL(IS) DE 25% - ART. 103, RES. 89/2009-CJF<br/>
		VALOR PAGO: ${f:floatParaMonetario(calculo.calculoValorLiquido)}<br/>
		OBJETIVO DO DESLOCAMENTO: ${doc.exMobilPai.doc.form['tipoServico']}<br/>
		PROPONENTE:${pesProponente.nomePessoa} - ${pesProponente.cargo.nomeCargo} - ${pesProponente.funcaoConfianca.nomeFuncao} <br/>
		CARRO OFICIAL:${doc.exMobilPai.doc.form['carroOficial']}

		</div>
		FIM CORPO -->
				<!-- INICIO FECHO -->
				<c:import
					url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
				<!-- FIM FECHO -->
				<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

				<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
				</body>

				</html>
			</c:when>
			<c:otherwise>
		O PAI DESTE DOCUMENTO DEVE SER UM PEDIDO DE CONCESSÃO DE DÁRIAS
	</c:otherwise>
		</c:choose>
	</mod:documento>
</mod:modelo>


