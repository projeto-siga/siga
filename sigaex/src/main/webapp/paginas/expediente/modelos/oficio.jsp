<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<c:if test="${empty esconderDest}">
			<mod:grupo>
				<mod:grupo>
					<mod:selecao titulo="Tipo de Autoridade" var="tipoAutoridade"
						opcoes="[Nenhum];Auditor da Justiça Militar;Bispo e Arcebispo;Cardeal;Chefe de Gabinete Civil;Chefe de Gabinete Militar da Presidência da República;Consultor-Geral da República;Corregedor do Tribunal Regional Federal;Dirigente administrativo e Procurador;Embaixador;Governador de Estado e do Distrito Federal;Juiz Federal;Juiz em geral;Membro do Congresso Nacional;Membro do Supremo Tribunal Federal;Membro do Tribunal Superior;Membro do Tribunal de Contas da União;Membro do Tribunal Regional Federal;Membro do Tribunal de Justiça;Membro da Assembléia Legislativa;Ministro de Estado;Monsenhor, Cônego;Prefeito;Presidente da República;Presidente do Supremo Tribunal Federal;Presidente do Tribunal Superior;Presidente do Tribunal Regional Federal;Presidente do Tribunal de Justiça;Presidente da Assembléia Legislativa;Presidente do Tribunal de Contas da União;Procurador-Geral da República;Procurador-Geral junto ao Tribunal;Secretário de Estado do Governo Estadual;Reitor de Universidade;Vice-Presidente da República;Oficial General das Forças Armadas;[Outros]"
						reler="sim" />

					<c:choose>
						<c:when
							test="${f:verificaGenero(requestScope['tipoAutoridade'])=='F'}">
							<mod:selecao titulo="Gênero da Autoridade" var="genero"
								opcoes="M;F" reler="sim" />
						</c:when>
						<c:otherwise>
							<c:set var="genero"
								value="${f:verificaGenero(requestScope['tipoAutoridade'])}" />
						</c:otherwise>
					</c:choose>

					<c:set var="tratamento"
						value="${f:tratamento(requestScope['tipoAutoridade'],genero)}" />

					<c:if test="${not empty tratamento}">
						<mod:grupo>
							<mod:mensagem titulo="Forma de tratamento"
								texto="${tratamento.formaDeTratamento} (${tratamento.abreviatura})" />
						</mod:grupo>
					</c:if>

				</mod:grupo>

				<c:if test="${not empty tratamento}">
					<mod:grupo>
						<mod:caixaverif titulo="Especificar manualmente o vocativo"
							var="especificarVocativo" reler="sim" />
					</mod:grupo>
				</c:if>

				<c:choose>
					<c:when test="${especificarVocativo == 'Sim' or empty tratamento}">
						<mod:grupo>
							<mod:texto titulo="Vocativo" var="vocativo" largura="45" />
						</mod:grupo>
					</c:when>
					<c:otherwise>
						<mod:oculto var="vocativo" valor="${tratamento.vocativo}" />
					</c:otherwise>
				</c:choose>



				<mod:grupo titulo="Texto a ser inserido no corpo do ofício">
					<mod:grupo>
						<mod:editor titulo="" var="texto_oficio" />
					</mod:grupo>
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Fecho (de acordo com o vocativo)" var="fecho"
						largura="60" />
				</mod:grupo>
			</mod:grupo>

			<mod:grupo titulo="Dados do destinatário">

				<!-- Tratamento -->
				<c:if test="${not empty tratamento}">
					<mod:grupo>
						<mod:caixaverif
							titulo="Especificar manualmente a forma de endereçamento"
							var="especificarEnderecamento" reler="sim" />
					</mod:grupo>
				</c:if>
				<c:choose>
					<c:when
						test="${especificarEnderecamento == 'Sim' or empty tratamento}">
						<mod:grupo>
							<mod:texto titulo="Forma de endereçamento"
								var="enderecamento_dest" largura="45" />
						</mod:grupo>
					</c:when>
					<c:otherwise>
						<mod:oculto var="enderecamento_dest"
							valor="${tratamento.formaDeEnderecamento}" />
					</c:otherwise>
				</c:choose>

				<!-- Nome -->
				<c:if test="${not empty doc.destinatario.descricao}">
					<mod:grupo>
						<mod:caixaverif
							titulo="Especificar manualmente o nome do destinatário"
							var="especificarNome" reler="sim" />
					</mod:grupo>
				</c:if>
				<c:choose>
					<c:when
						test="${especificarNome == 'Sim' or empty doc.destinatario.descricao}">
						<mod:grupo>
							<mod:texto titulo="Nome" var="nome_dest" largura="60" />
						</mod:grupo>
					</c:when>
					<c:otherwise>
						<mod:oculto var="nome_dest" valor="${doc.destinatario.descricao}" />
					</c:otherwise>
				</c:choose>

				<!-- Cargo -->
				<c:if test="${not empty doc.destinatario.cargo.nomeCargo}">
					<mod:grupo>
						<mod:caixaverif
							titulo="Especificar manualmente o cargo do destinatário"
							var="especificarCargo" reler="sim" />
					</mod:grupo>
				</c:if>
				<c:choose>
					<c:when
						test="${especificarCargo == 'Sim' or empty doc.destinatario.cargo.nomeCargo}">
						<mod:grupo>
							<mod:texto titulo="Cargo" var="cargo_dest" largura="60" />
						</mod:grupo>
					</c:when>
					<c:otherwise>
						<mod:oculto var="cargo_dest"
							valor="${doc.destinatario.cargo.nomeCargo}" />
					</c:otherwise>
				</c:choose>

				<!-- Órgão -->
				<c:choose>
					<c:when test="${not empty doc.lotaDestinatario.descricao}">
						<c:set var="orgaoAux" value="${doc.lotaDestinatario.descricao}" />
					</c:when>
					<c:otherwise>
						<c:set var="orgaoAux"
							value="${orgaoExternoDestinatarioSel.descricao}" />
					</c:otherwise>
				</c:choose>
				<c:if test="${not empty orgaoAux}">
					<mod:grupo>
						<mod:caixaverif
							titulo="Especificar manualmente o órgão do destinatário"
							var="especificarOrgao" reler="sim" />
					</mod:grupo>
				</c:if>
				<c:choose>
					<c:when test="${especificarOrgao == 'Sim' or empty orgaoAux}">
						<mod:grupo>
							<mod:texto titulo="Órgão" var="orgao_dest" largura="60" />
						</mod:grupo>
					</c:when>
					<c:otherwise>
						<mod:oculto var="orgao_dest" valor="${orgaoAux}" />
					</c:otherwise>
				</c:choose>

			</mod:grupo>

			<mod:grupo>
				<mod:memo titulo="Endereço" var="endereco_dest" linhas="4"
					colunas="60" />
			</mod:grupo>
			<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra"
				opcoes="Normal;Pequeno;Grande" />
		</c:if>
	</mod:entrevista>


	<mod:documento>
		<c:choose>
			<c:when test="${tamanhoLetra=='Pequeno'}">
				<c:set var="tl" value="9pt" />
			</c:when>
			<c:when test="${tamanhoLetra=='Grande'}">
				<c:set var="tl" value="13pt" />
			</c:when>
			<c:otherwise>
				<c:set var="tl" value="11pt" />
			</c:otherwise>
		</c:choose>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 3cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" width="40%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">OF&Iacute;CIO N&ordm; ${doc.codigo}</p></td>
							<td align="right" width="60%"><mod:letra tamanho="${tl}"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
		<c:if
			test="${(not f:contains(fecho,'.')) && (not f:contains(fecho,','))}">
			<c:set var="virgula" value="," />
		</c:if>
		<mod:letra tamanho="${tl}">
			<div style="font-family: Arial; font-size: 10pt;">
			<p>&nbsp;</p>
			<c:if test="${empty vocativo}">
			<p align="left" style="TEXT-INDENT: 2cm">${vocativo}</p>
			</c:if>
			<p align="left" style="TEXT-INDENT: 2cm">${vocativo},</p>
			<p><span style="font-size: ${tl}"> ${texto_oficio} </span> <c:if
				test="${not empty fecho}">
				<span style="font-size: ${tl}">
				<center>${fecho}${virgula}</center>
				</span>
			</c:if></p>
			</div>
			<c:import
				url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		</mod:letra>

		<!-- INICIO PRIMEIRO RODAPE
		<table width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
			<tr>
				<td align="left">
					<table align="left" width="100%" border="0" cellpadding="2" cellspacing="0">
						<tr><td><mod:letra tamanho="${tl}"><p>${enderecamento_dest}</p></mod:letra></td></tr>
						<tr><td><mod:letra tamanho="${tl}"><p>${nome_dest}</p></mod:letra></td></tr>
						<c:if test="${not empty cargo_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p>${cargo_dest}</p></mod:letra></td></tr>
						</c:if>
						<c:if test="${not empty orgao_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p>${orgao_dest}</p></mod:letra></td></tr>
						</c:if>
						<c:if test="${not empty endereco_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p><siga:fixcrlf>${endereco_dest}</siga:fixcrlf></p></mod:letra></td></tr>
						</c:if>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
				</td>
			</tr>
		</table>
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>

