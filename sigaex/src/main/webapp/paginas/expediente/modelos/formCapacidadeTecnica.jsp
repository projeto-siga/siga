<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>


<mod:modelo>
	<mod:entrevista>
              	<mod:grupo>
			<mod:selecao titulo="<b>Órgão Jurisdicional</b>" var="org" opcoes="SJRJ;SJES;TRF2" reler="sim" />
		</mod:grupo>
			<br>
		<mod:grupo>
			<mod:numero titulo="Processo" var="numProcesso" largura="25" />		
		</mod:grupo>
		<mod:grupo titulo="Dados da Empresa">
			<mod:grupo largura="50">
				<mod:texto titulo="Nome" var="nomeEmpresa" largura="60" />
			</mod:grupo>
			<mod:grupo largura="55">
				<mod:texto titulo="CNPJ/MF nº" var="cnpj" largura="16" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Endereço" var="enderecoEmpresa" largura="70" />
			</mod:grupo>
		</mod:grupo>
		<mod:grupo titulo="Dados da Contratação">			
 			<mod:grupo>
				<mod:caixaverif titulo="Executou os serviços"
							var="servicos" reler="ajax" idAjax="servicosAjax" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;				
				<mod:caixaverif titulo="Forneceu os materiais"
							var="materiais" reler="ajax" idAjax="materiaisAjax" />				
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Objeto" var="objeto" largura="60"
					maxcaracteres="60" />
			</mod:grupo>
			<mod:grupo>
				<mod:grupo largura="40">
					<mod:texto titulo="Ata de Registro de Preços (se couber)" var="ata"
					largura="10" />
				</mod:grupo>	
				<mod:grupo largura="60">
					<mod:texto titulo="Número do Contrato/Nota de Empenho"
					var="contrato" largura="10" />
				</mod:grupo>	
			</mod:grupo>
			<mod:grupo>
				<mod:grupo largura="40">
					<mod:data titulo="Data de Vigência" var="dataIni" />
					<mod:data titulo="a" var="dataFim" />
				</mod:grupo>				
					<mod:grupo largura="60">
						<mod:monetario titulo="Valor do Contrato" var="valContrato" />
					</mod:grupo>
			</mod:grupo>
			<mod:grupo>	
				<mod:grupo largura="40">
					<mod:data titulo="Data de Recebimento Provisório"
						var="dataProvisorio" />
				</mod:grupo>
				<mod:grupo largura="60">
					<mod:data titulo="Data de Recebimento Definitivo"
						var="dataDefinitivo" />
				</mod:grupo>
			</mod:grupo>
			<mod:grupo depende="servicosAjax">
				<c:if test="${servicos == 'Sim'}">
					<mod:texto titulo="Local da prestação dos serviços" var="local"
						largura="50" />
				</c:if>			
			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Profissionais responsáveis técnicos (se couber)"
					var="numProfissionais" opcoes="0;1;2;3" reler="ajax" idAjax="profissionaisAjax"/>
			</mod:grupo>
			<mod:grupo depende="profissionaisAjax">
				<c:if test="${numProfissionais != 0}">
					<c:forEach var="i" begin="1" end="${numProfissionais}">
						<mod:grupo>
							<mod:texto titulo="Profissional ${i}" var="profissional${i}" largura="40" />
						</mod:grupo>
					</c:forEach>
				</c:if>
			</mod:grupo>			
		</mod:grupo>
		<mod:grupo titulo="Descrição da Contratação">
			<mod:selecao titulo="Número de itens a serem incluídos"
				var="numItens" opcoes="0;1;2;3;4;5;6;7;8;9;10" reler="ajax" idAjax="numItensAjax" />
			<mod:grupo depende="numItensAjax">
				<c:if test="${numItens != 0}">						
					<c:forEach var="i" begin="1" end="${numItens}">
						<mod:grupo>
							<b>${i})</b>
							<mod:texto titulo="Especificação" var="especificacao${i}" largura="40" />
							<mod:texto titulo="Unidade" var="unidade${i}" />
							<mod:texto titulo="Quantidade" var="quantidade${i}" largura="4" />
						</mod:grupo> 
						<mod:grupo>
							&nbsp;&nbsp;&nbsp;
							<mod:selecao titulo="Número de subitens a serem incluídos"
							var="numSubItens${i}" opcoes="0;1;2;3;4;5;6;7;8;9;10"
							reler="ajax" idAjax="numSubItensAjax${i}" />
						</mod:grupo>	
						<mod:grupo depende="numSubItensAjax${i}">
							<c:if test="${requestScope[f:concat('numSubItens',i)] != 0}">						
								<c:forEach var="j" begin="1" end="${requestScope[f:concat('numSubItens',i)]}">								
									 <mod:grupo>
									 	&nbsp;&nbsp;&nbsp;
										<b>${i}.${j})</b>
										<mod:texto titulo="Especificação" var="especificacao${i}${j}"
											largura="40" />
										<mod:texto titulo="Unidade" var="unidade${i}${j}" />
										<mod:texto titulo="Quantidade" var="quantidade${i}${j}" largura="4" />
									</mod:grupo> 
									<mod:grupo>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<mod:selecao titulo="Número de sub subitens a serem incluídos"
											var="numSubItens${i}${j}" opcoes="0;1;2;3;4;5;6;7;8;9;10"
											reler="ajax" idAjax="numSubItensAjax${i}${j}" />
										<mod:grupo depende="numSubItensAjax${i}${j}">
											<c:if test="${requestScope[f:concat(f:concat('numSubItens',i),j)] != 0}">
												<c:forEach var="k" begin="1" end="${requestScope[f:concat(f:concat('numSubItens',i),j)]}">													
													<mod:grupo>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<b>${i}.${j}.${k})</b>
														<mod:texto titulo="Especificação"
															var="especificacao${i}${j}${k}" largura="40" />
														<mod:texto titulo="Unidade" var="unidade${i}${j}${k}" />
														<mod:texto titulo="Quantidade" var="quantidade${i}${j}${k}" largura="4" />
													</mod:grupo> 
												</c:forEach>
											</c:if>
										</mod:grupo>
									</mod:grupo>
								</c:forEach>
							</c:if>
						</mod:grupo>			
						<hr color="#FFFFFF" />
					</c:forEach>				
				</c:if>
			</mod:grupo>
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
                <c:set var="orgaojudic" value="A JUSTIÇA FEDERAL DE 1º GRAU DO ESTADO DO RIO DE JANEIRO, estabelecida na" />
                <c:set var="endereco" value="Av. Rio Branco, 243 - Anexo I - Centro - Rio de Janeiro, inscrita no CNPJ/MF sob o nº " />
                <c:set var="cpj" value="05.424.540/0001-16" />
                
                <c:if test="${org == 'SJES'}">
		    <c:set var="orgaojudic" value="A JUSTIÇA FEDERAL DE 1º GRAU DO ESTADO DO ESPÍRITO SANTO, estabelecida na" />
                    <c:set var="endereco" value="Av. Marechal Mascarenhas de Moraes, 1.877 - Vitória - ES, inscrita no CNPJ/MF sob o nº" />
                    <c:set var="cpj" value="05.424.467/00001-82" />
	        </c:if>
                <c:if test="${org == 'TRF2'}">
		    <c:set var="orgaojudic" value="O TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO, estabelecido na" />
                    <c:set var="endereco" value="Rua Acre, 80 - Centro - Rio de Janeiro - RJ, inscrito no CNPJ/MF sob o nº" />
                    <c:set var="cpj" value="32.243.347/0001-51" />
	        </c:if>

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
		
		<p style="text-align: center; font: bold;"><b>ATESTADO DE CAPACIDADE TÉCNICA </b></p>
		
		<p style="text-align: justify;">${orgaojudic} ${endereco} ${cpj}, ATESTA, para os devidos fins de direito, que a empresa 
		${nomeEmpresa}, estabelecida na ${enderecoEmpresa}, inscrita no CNPJ/MF sob o nº ${cnpj},
		<ww:if test="${servicos == 'Sim'}">
			executou os serviços
			<c:if test="${materiais == 'Sim'}">
				e forneceu os materiais 
			</c:if>	
		</ww:if>
		<ww:else>
			<c:if test="${materiais == 'Sim'}">
				forneceu os materiais 
			</c:if>
		</ww:else> 
		abaixo relacionados: </p>

		<br/>
		
		<table border="1" width="100%" align="left" cellpadding="5">
			<ww:if test="${not empty ata}">	
				<tr bgcolor="#999999">
						<td colspan="3"><b>DADOS DA CONTRATAÇÃO</b></td>
				</tr>
				<tr>
					<td colspan="3" cels>Objeto: ${objeto} </td>
				</tr>			
				<tr>		
					<td> Ata de Registro de preços: ${ata} </td> 													 
					<td> Contrato/Nota de Empenho: ${contrato} </td>
					<td> Vigência: ${dataIni} à ${dataFim} </td>				
				</tr>				
				<tr>
					<td>Data do Recebimento Provisório: ${dataProvisorio} </td>
					<td>Data do Recebimento Definitivo: ${dataDefinitivo} </td>
					<td>Valor do Contrato:  ${valContrato} </td>
				</tr>
			</ww:if>				
			<ww:else>
				<tr bgcolor="#999999">
						<td colspan="3"><b>DADOS DA CONTRATAÇÃO</b></td>
				</tr>
				<tr>
					<td colspan="3">Objeto: ${objeto} </td>
				</tr>			
				<tr>																	 
					<td colspan="2"> Contrato/Nota de Empenho: ${contrato} </td>
					<td> Vigência: ${dataIni} à ${dataFim} </td>				
				</tr>				
				<tr>
					<td width="30%">Data do Recebimento Provisório: ${dataProvisorio} </td>
					<td width="30%">Data do Recebimento Definitivo: ${dataDefinitivo} </td>
					<td width="40%">Valor do Contrato:  ${valContrato} </td>
				</tr>				
			</ww:else>	
		</table>
		<c:if test="${servicos == 'Sim'}">
			<table border="1" width="100%" align="left" cellpadding="5">	
				<tr>
					<td>Local da prestação dos serviços: ${local} </td>
				</tr>	
			</table>			
		</c:if>			
		<c:if test="${numProfissionais != 0}">
			<table border="1" width="100%" align="left" cellpadding="5">	
				<tr>
					<td> Profissionais responsáveis técnicos: <br>
						<c:forEach var="i" begin="1" end="${numProfissionais}">
							${requestScope[f:concat('profissional',i)]} <br>
						</c:forEach>
					</td>
				</tr>	
			</table> 	
		</c:if>	
			 
		
		<br>		
		<table border="1" width="100%" cellpadding="5">
			<tr bgcolor="#999999">
				<td colspan="4"><b>DESCRIÇÃO DA CONTRATAÇÃO </b></td>
			</tr>
			<tr>
				<td width="10%" align="left"><b>Item</b></td>
				<td width="40%" align="center"><b>Especificacao</b></td>
				<td width="40%" align="center"><b>Unidade</b></td>
				<td width="10%" align="center"><b>Qtde</b></td>
			</tr>
			<c:if test="${numItens != 0}">						
				<c:forEach var="i" begin="1" end="${numItens}">
					<tr>
						<td width="10%"><b>${i}</b></td>
						<td width="40%">${requestScope[f:concat('especificacao',i)]}</td>
						<td width="40%">${requestScope[f:concat('unidade',i)]}</td>
						<td width="10%" align="center">${requestScope[f:concat('quantidade',i)]}</td>
					</tr>												
					<c:if test="${requestScope[f:concat('numSubItens',i)] != 0}">						
						<c:forEach var="j" begin="1" end="${requestScope[f:concat('numSubItens',i)]}">	
							<tr>
								<td width="10%"><b>${i}.${j}</b></td>
								<td width="40%">${requestScope[f:concat(f:concat('especificacao',i),j)]}</td>
								<td width="40%">${requestScope[f:concat(f:concat('unidade',i),j)]}</td>
								<td width="10%" align="center">${requestScope[f:concat(f:concat('quantidade',i),j)]}</td>
							</tr>								
							<c:if test="${requestScope[f:concat(f:concat('numSubItens',i),j)] != 0}">
								<c:forEach var="k" begin="1" end="${requestScope[f:concat(f:concat('numSubItens',i),j)]}">
									<tr>
										<td width="10%"><b>${i}.${j}.${k}</b></td>
										<td width="40%">${requestScope[f:concat(f:concat(f:concat('especificacao',i),j),k)]}</td>
										<td width="40%">${requestScope[f:concat(f:concat(f:concat('unidade',i),j),k)]}</td>
										<td width="10%" align="center">${requestScope[f:concat(f:concat(f:concat('quantidade',i),j),k)]}</td>
									</tr>									
								</c:forEach>
							</c:if>							
						</c:forEach>
					</c:if>		
				</c:forEach>
			</c:if>					
		</table>	
			
		<br />
		<p style="font-family: Arial; font-size: 10pt; text-align: left;">${doc.dtExtenso}</p>

		
		
		<br />
		<br />


		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />

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
