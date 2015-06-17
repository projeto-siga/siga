<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:selecao titulo="Local" var="local" reler="sim"
				opcoes="Selecione;Passeio;Av. México;Av. Barão de Teffé;Almirante Barroso" />

			<c:choose>
				<c:when test="${local eq 'Av. México'}">
					<b><mod:mensagem texto="Somente para Juizes Federais"
						vermelho="Nao"></mod:mensagem>	
					<mod:oculto var="observacao" valor=" "/>								
				</c:when>
				<c:when test="${local eq 'Passeio'}">
					<b><mod:mensagem
						texto="Somente para Diretores de Secretaria/ Subsecretaria e Coordenadores."
						vermelho="Nao"></mod:mensagem>
					<mod:oculto var="observacao" valor=" "/>							
				</c:when>
				<c:when test="${local eq 'Av. Barão de Teffé'}">
					<b><mod:mensagem
						texto="Somente para Juízes Federais e Diretores de Secretaria/ Subsecretaria."
						vermelho="Nao"></mod:mensagem> </b>
					<mod:oculto var="observacao" valor=" "/>
				</c:when>
				<c:when test="${local eq 'Almirante Barroso'}">
					<b><mod:mensagem
						texto="Somente para Diretores."
						vermelho="Nao"></mod:mensagem> </b>
					<mod:oculto var="observacao" valor=" "/>
				</c:when>
			</c:choose>
        
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Marca/Modelo" var="marcaModelo" largura="50"
				obrigatorio="Sim" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Ano" var="ano" largura="4" obrigatorio="Sim" />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <mod:texto titulo="Cor" var="cor" largura="10" obrigatorio="Sim" />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
		    <mod:texto titulo="Placa" var="placa" largura="10"
				obrigatorio="Sim" />
		</mod:grupo>
		<mod:grupo>
			<b><mod:mensagem texto="${observacao}" vermelho="Nao"></mod:mensagem>
		</mod:grupo>
		<mod:grupo>
			<mod:mensagem titulo="Atenção"
				texto="Preencha o destinatário com DSEG e, após finalizar, transfira para a DSEG.
				       Anexar o CRLV, de acordo com a portaria n° RJ-PDG-2012/5 de 14 de março de 2012." vermelho="Sim" />
		</mod:grupo>	
	</mod:entrevista>

	<mod:documento>
		<c:set var="tl" value="10pt" />
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
		<table width="100%" border="0"  bgcolor="#FFFFFF">
			<tr><td>
			<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
			</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/><br/>
					<table width="100%" border="0" >						
						<tr>
						<td align="right"><br/><br/><b>SOLICITA&Ccedil;&Atilde;O Nº ${doc.codigo}</b>
						</td>
						</tr>
						<tr>
							<td align="right">${doc.dtExtenso}<br/><br/></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->

		<br/>
		<table border="1" width="100%">
			<tr>
				<td align="center"><mod:letra tamanho="${tl}">
					<p style="font-family: Arial; font-weight: bold">SOLICITAÇÃO DE
					CARTÃO DE ESTACIONAMENTO - ${fn:toUpperCase(local)}</p>
				</mod:letra></td>
			</tr>
		</table>
		<br />
		<br />
		<table border="1" width="100%">
			<tr bgcolor="#999999">
				<td><b>DADOS DO SOLICITANTE</b></td>
			</tr>
			<tr>
				<td> 
					<table border="0" cellspacing="0" width="100%"> 
						<tr>
							<td>
								Nome: ${f:maiusculasEMinusculas(doc.subscritor.descricao)}
							</td>
						</tr>
						<tr>
							<td>
								Cargo/Função: ${f:maiusculasEMinusculas(doc.subscritor.cargo.nomeCargo)} &nbsp; ${f:maiusculasEMinusculas(doc.subscritor.funcaoConfianca.nomeFuncao)}
							</td>
						</tr>
						<tr>
							<td>
								Lotação: ${doc.subscritor.lotacao.descricao}
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br />
		<table border="1" width="100%">
			<tr bgcolor="#999999">
				<td><b>DADOS DO VEÍCULO</b></td>
			</tr>
			<tr>
				<td>
					<table border="0" cellspacing="0" width="100%">
						<tr>
							<td>
								Marca/Modelo: ${marcaModelo}
							</td>
						</tr> 
						<tr>
							<td>
								Ano: ${ano}
							</td>
						</tr> 
						<tr>
							<td>
								Cor: ${cor}
							</td>
						</tr> 
						<tr>
							<td>
								Placa: ${placa}
							</td>
						</tr> 
					</table>				
				</td>
			</tr>

		</table>
		<br/>	
		<b>${observacao}</b>
		
		<br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/>



		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->


		</body>
		</html>
	</mod:documento>
</mod:modelo>