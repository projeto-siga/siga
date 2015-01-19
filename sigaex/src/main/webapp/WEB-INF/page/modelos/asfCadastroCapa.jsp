<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Dados Cadastrais">
			<mod:grupo>
				<mod:pessoa titulo="Matrícula" var="servidor" />
			</mod:grupo>
			<mod:grupo largura="42">
				<mod:data titulo="Data de Nascimento" var="dtNascimento" />
			</mod:grupo>
			<mod:grupo largura="58">
				<mod:texto titulo="Tipo sanguíneo e fator RH" largura="5"
					var="tipoSangueRH" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Naturalidade" largura="37" var="naturalidade" />
				<mod:texto titulo="Nacionalidade" largura="37" var="nacionalidade" />
			</mod:grupo>
			<mod:grupo>
				<mod:numero titulo="Número do RG" largura="20" var="numRG"
					maxcaracteres="20" />
				<mod:texto titulo="Órgão Emissor" largura="20" var="orgEmissorRG"
					maxcaracteres="20" />
				<mod:data titulo="Data de Emissão" var="dtEmissaoRG" />
			</mod:grupo>
			<mod:grupo largura="39">
				<mod:texto titulo="Número do CPF" largura="19" var="numCPF"
					maxcaracteres="19" />
			</mod:grupo>
			<mod:grupo largura="61">
				<mod:numero titulo="PIS/PASEP" largura="25" var="pisPasep"
					maxcaracteres="25" />
			</mod:grupo>
			<mod:grupo>
				<mod:numero titulo="Título Eleitoral" largura="30" var="titEleitor"
					maxcaracteres="30" />
				<mod:texto titulo="Zona" largura="3" var="zona" maxcaracteres="3" />
				<mod:texto titulo="Seção" largura="5" var="secao" maxcaracteres="5" />
				<mod:data titulo="Data de Emissão" var="dtEmissaoTit" />
			</mod:grupo>
			<mod:grupo>
				<mod:numero titulo="Certificado de Reservista" largura="20"
					var="certReservista" maxcaracteres="20" />
			</mod:grupo>
			<mod:grupo largura="18">
				<mod:numero titulo="CNH" largura="20" var="cnh"
						maxcaracteres="20" />
			</mod:grupo>
			<mod:grupo largura="33">			 
				<mod:numero titulo="Número do Registro" largura="20"
						var="numRegistro" maxcaracteres="20" />
			</mod:grupo>
			<mod:grupo largura="49">				 
				<mod:texto titulo="Categoria" largura="5" var="categoria"
						maxcaracteres="5" />
			</mod:grupo>			
		</mod:grupo>
		<mod:grupo titulo="Situação Funcional">
			<mod:grupo>
				<mod:selecao titulo="Natureza do Ato" var="naturezaAto"
						opcoes="Nomeação;Remoção;Requisição;Remoção (Lei 11416/06)"
						 reler="ajax" idAjax="naturezaAtoAjax"/>
			</mod:grupo>
			<mod:grupo depende="naturezaAtoAjax">
				<ww:if test="${naturezaAto == 'Requisição' or naturezaAto == 'Remoção' or
							   naturezaAto == 'Remoção (Lei 11416/06)'}">	
					<mod:grupo largura="44">			   				 
						<mod:data titulo="Data da Posse na Origem" var="dtPosse" />
					</mod:grupo>	
					<mod:grupo largura="66">	
						<mod:data titulo="Data do Exercício" var="dtExercicio" />
					</mod:grupo>
					<mod:grupo>		
						<mod:texto titulo="Cargo" largura="36" var="cargo"
						maxcaracteres="36" />				
						&nbsp;<mod:texto titulo="Órgão Origem" largura="43" var="orgaoOrigem"
						maxcaracteres="43" />
					</mod:grupo>						
				</ww:if>
				<ww:else>					
					<mod:grupo largura="44">										   				 
						<mod:data titulo="Data da Posse" var="dtPosse" />						
					</mod:grupo>					
					<mod:grupo largura="66">					
						<mod:data titulo="Data do Exercício" var="dtExercicio" />
					</mod:grupo>	
														
					<mod:grupo>					
						<mod:texto titulo="Cargo" largura="44" var="cargo" 	maxcaracteres="44" />										
					&nbsp;<mod:texto titulo="Padrão" largura="42" var="padrao" maxcaracteres="42" />
					</mod:grupo>
				</ww:else>	
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Veículo de Publicação" largura="15"
							var="veiculo" maxcaracteres="15" />				
				<mod:data titulo="Dt. Publicação" var="publicacao"  />				
				 <mod:texto titulo="Autoridade" largura="25"
							var="autoridade" maxcaracteres="25" />
			</mod:grupo> 		
		</mod:grupo>

	</mod:entrevista>

	<mod:documento>
		<c:if test="${tamanhoLetra =='Normal'}">
			<c:set var="tl" value="11pt" />
		</c:if>
		<c:if test="${tamanhoLetra =='Pequeno'}">
			<c:set var="tl" value="9pt" />
		</c:if>
		<c:if test="${tamanhoLetra =='Grande'}">
			<c:set var="tl" value="13pt" />
		</c:if>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

		<head>
		<style type="text/css">
@page {
	margin-left: 1.5 cm;
	margin-right: 2 cm;
	margin-top: 0.5cm;
	margin-bottom: 2 cm;
}

.tfonte {
	font-size: 16;
}
</style>
		</head>

		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="11pt"></c:set>
		</c:if>

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<table border="0" align="center" bgcolor="#ffffff" width="100%">
						<tr>
							<td width="20%" bgcolor="#787878"></td>
							<td width="20%" bgcolor="#787878"></td>
						</tr>
					</table>
				</td>
			<tr>
			<tr>
				<td>
					<br><br/><c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
					
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td cellpadding="5">
					&nbsp;
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<%--      <!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		<br/><br/>
		FIM CABECALHO --> --%>

		<br />
		<%-- Não serão necessários os campos Apenso e volume abaixo, por isto estão comentados  --%>
		<table align="center" width="60%" border="1" cellspacing="1">
			<tr>
				<td width="30%"  align="center"><br />
				<b>ASSENTAMENTO FUNCIONAL Nº</b><br />
				<br />
				</td>
				<%-- 		<td width="15%" bgcolor="#FFFFFF" align="center"><b>Apenso</b></td> 
				<td width="15%" bgcolor="#FFFFFF" align="center"><b>Volume</b></td> --%>
			</tr>

			<tr>
				<td align="center"><br />
				${doc.codigo}<br />
				<br />
				</td>
			</tr>
		</table>

		<c:if test="${doc.exTipoDocumento.idTpDoc == 2}">
			<br>
			<table align="center" width="60%" border="1" cellspacing="1">
				<tr>
					<td width="30%" align="center"><br />
					<b>Nº no Sistema Antigo</b><br />
					<br />
					</td>
					<%-- 		<td width="15%" align="center"><b>Apenso</b></td> 
				<td width="15%"  align="center"><b>Volume</b></td> --%>
				</tr>

				<tr>
					<td align="center"><br />
					${doc.numAntigoDoc}<br />
					<br />
					</td>
				</tr>

			</table>
		</c:if>

		<br>
		<br>

		<table align="center" width="60%" border="1" cellspacing="1">
			<tr>
				<td width="50%" align="center"><br />
				<b>Data da abertura</b><br />
				<br />
				</td>
				<td align="center">${doc.dtDocDDMMYYYY}</td>
			</tr>
		</table>

		<br>
		<br>

		<table align="center" width="85%" border="1" cellpadding=4>
			<tr>
				<td align="center"><b>SERVIDOR</b></td>
			</tr>
			<tr>
				<td align="center">
				${f:pessoa(requestScope['servidor_pessoaSel.id']).nomePessoa}</td>
			</tr>
		</table>

		<br>
		<br>

		<table align="center" width="60%" border="1" cellpadding="4">
			<tr>
				<td width="50%" align="center"><b>Matrícula</b>
				</td>
				<td align="center">${f:pessoa(requestScope['servidor_pessoaSel.id']).matricula}</td>
			</tr>
		</table>
		<br>
		<br>

		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<br />
		<br />

		<table width="100%" border="0" bgcolor="#FFFFFF">
			<tr bgcolor="#FFFFFF">
				<td align="left" valign="bottom" width="15%"><img
					src="contextpath/imagens/brasao2.png" width="65" height="65" /></td>
				<td align="left" width="1%"></td>
				<td width="37%">
				<table align="left" width="100%">
					<tr>
						<td width="100%" align="left">
						<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${f:resource('siga.ex.modelos.cabecalho.titulo')}</p>
						</td>
					</tr>
					<c:if test="${not empty f:resource('siga.ex.modelos.cabecalho.subtitulo')}">
						<tr>
							<td width="100%" align="left">
							<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${f:resource('siga.ex.modelos.cabecalho.subtitulo')}L</p>
							</td>
						</tr>
					</c:if>
					<tr>
						<td width="100%" align="left">
						<p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
						<c:choose>
							<c:when test="${empty mov}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when>
							<c:otherwise>${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:otherwise>
						</c:choose></p>
						</td>
					</tr>
				</table>
				</td>
				<td width="57%" align="center">
				<p align="center" style="font: bold; font-size: 10pt"><b>ASSENTAMENTO
				FUNCIONAL&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b><br />
				<font size="2">(CONTRACAPA) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></p>
				</td>
			</tr>
		</table>
		<br />
		<%-- Não serão necessários os campos Apenso e volume abaixo, por isto estão comentados  --%>

		<table Width="100%" cellpadding="6">

			<tr>
				<td colspan="4"><b>DADOS CADASTRAIS</b></td>

			</tr>
			<tr>
				<td colspan="3"><b
					style="font-family: Arial Narrow; font-size: 7pt;">NOME: </b>
				&nbsp;${f:pessoa(requestScope['servidor_pessoaSel.id']).nomePessoa}
				</td>
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">MATRÍCULA:</b>
				&nbsp;${f:pessoa(requestScope['servidor_pessoaSel.id']).matricula}</td>
			</tr>
			<tr>
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">DATA DE
				NASCIMENTO:</b> &nbsp;${dtNascimento}</td>
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">TIPO
				SANGUÍNEO E FATOR RH:</b> &nbsp;${tipoSangueRH}</td>
			</tr>
			<tr>
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">NATURALIDADE:</b>
				&nbsp;${naturalidade}</td>
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">NACIONALIDADE:</b>
				&nbsp;${nacionalidade}</td>
			</tr>
			<tr>
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">RG:</b>
				&nbsp;${numRG}</td>
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">ÓRGÃO
				EMISSOR:</b> &nbsp;${orgEmissorRG}</td>
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">DATA
				DE EMISSÃO:</b> &nbsp;${dtEmissaoRG}</td>
			</tr>
			<tr>
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">CPF:</b>
				&nbsp;${f:formatarCPF(numCPF)}</td>
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">PIS/PASEP:</b>
				&nbsp;${pisPasep}</td>
			</tr>
			<tr>
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">TÍTULO
				ELEITORAL:</b> &nbsp;${titEleitor}</td>
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">ZONA:</b>
				&nbsp;${zona}</td>
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">SEÇÃO:</b>
				&nbsp;${secao}</td>
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">DATA
				DE EMISSÃO:</b> &nbsp;${dtEmissaoTit}</td>
			</tr>
			<c:if test="${not empty cnh}">
				<tr>
					<td><b style="font-family: Arial Narrow; font-size: 7pt;">CNH:</b>
					&nbsp;${cnh}</td>
					<td colspan="2"><b
						style="font-family: Arial Narrow; font-size: 7pt;">Nº DO
					REGISTRO:</b> &nbsp;${numRegistro}</td>
					<td><b style="font-family: Arial Narrow; font-size: 7pt;">CATEGORIA</b>
					&nbsp;${categoria}</td>
				</tr>
			</c:if>
			<c:if test="${not empty certReservista}">
				<tr>
					<td colspan="4"><b
						style="font-family: Arial Narrow; font-size: 7pt;">CERTIFICADO
					DE RESERVISTA:</b> &nbsp;${certReservista}</td>
				</tr>
			</c:if>
			<tr>
				<td colspan="4"><b>PROVIMENTO INICIAL</b></td>
			</tr>
			<tr>
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">NATUREZA DO
				ATO:</b> &nbsp;${naturezaAto}</td>
				<ww:if test="${naturezaAto == 'Requisição' or naturezaAto == 'Remoção' or
							   naturezaAto == 'Remoção (Lei 11416/06)'}">
					<td><b style="font-family: Arial Narrow; font-size: 7pt;">DATA DA POSSE
					ORIGEM:</b> &nbsp;${dtPosse}</td>
				</ww:if>	
				<ww:else>						   
					<td><b style="font-family: Arial Narrow; font-size: 7pt;">DATA
					DA POSSE:</b> &nbsp;${dtPosse}</td>
				</ww:else>	
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">DATA
				EXERCÍCIO:</b> &nbsp;${dtExercicio}</td>
			</tr>
			<tr>
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">CARGO:</b>
				&nbsp;${cargo}</td>
				<ww:if test="${naturezaAto == 'Requisição' or naturezaAto == 'Remoção' or
							   naturezaAto == 'Remoção (Lei 11416/06)'}">
					<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">ÓRGÃO ORIGEM:</b>
				&nbsp;${orgaoOrigem}</td>	
				</ww:if>
				<ww:else>				
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">PADRÃO:</b>
				&nbsp;${padrao}</td>
				</ww:else>
			</tr>
			<tr>		
				<td colspan="2"><b
					style="font-family: Arial Narrow; font-size: 7pt;">AUTORIDADE:</b>
				&nbsp;${autoridade}	
				</td>
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">VEÍCULO DE PUBLICAÇÃO:</b>
				&nbsp;${veiculo}</td>
				<td><b style="font-family: Arial Narrow; font-size: 7pt;">DATA DE PUBLICAÇÃO:</b>
				&nbsp;${publicacao}</td>				
				
			</tr>			
		</table>
			

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