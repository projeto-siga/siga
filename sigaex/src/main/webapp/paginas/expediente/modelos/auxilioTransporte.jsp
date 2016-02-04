<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<mod:modelo>	
	<mod:entrevista>
		<mod:grupo>
			<mod:selecao titulo="Finalidade" var="finalidade" opcoes="Inclusão;Atualização;Reinclusão" />
		</mod:grupo>			
		<mod:grupo titulo="DADOS DO SERVIDOR">

			<mod:grupo>
				<mod:data var="dataexercicio" titulo="Data de exercício" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto var="endereco" titulo="Endereço residencial:" largura="60" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto var="bairro" titulo="Bairro:" largura="30" />
				<mod:texto var="cidade" titulo="Cidade:" largura="20" />
				<mod:texto var="cep" titulo="CEP:" largura="20" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto var="telefone" titulo="Telefone:" />
				<mod:texto var="ramal" titulo="Ramal:" largura="10" />
			</mod:grupo>
			<mod:grupo titulo="Em caso de atualização, especificar o motivo:">
				<mod:caixaverif var="mudaendereco" titulo="Mudança de endereço" />
				<mod:caixaverif var="aumentotarifa" titulo="Aumento de tarifa" />
			</mod:grupo>
			<mod:grupo>
				<mod:caixaverif var="mudanca" titulo="Mudança de lotação" />
				<mod:caixaverif var="outro" titulo="Outro:" reler="ajax" idAjax="outroAjax" />
				<mod:grupo depende="outroAjax"> 
					<c:if test="${outro=='Sim'}">
						<mod:texto var="outroTexto" titulo="" largura="40" />
					</c:if>
				</mod:grupo>
			</mod:grupo>
		</mod:grupo>


		<mod:grupo titulo="PERCURSO DO SERVIDOR">
			<mod:selecao var="quantidadePercursoIda" titulo="Quantidade de percursos de ida" opcoes="1;2;3;4;5;6;7;8" reler="ajax" idAjax="quantidadePercursoIdaAjax" />
			
			<mod:grupo depende="quantidadePercursoIdaAjax">				
			<mod:grupo titulo="Percurso de ida (RESIDÊNCIA-TRABALHO)">
				<c:forEach var="i" begin="1" end="${quantidadePercursoIda}">
					<mod:grupo largura="20">
						<mod:selecao opcoes="Barca;Metrô;Ônibus;Trem" var="tipoIda${i}" titulo="Tipo de Transporte" />
					</mod:grupo>
					<mod:grupo largura="20">
						<mod:texto titulo="Linha" var="linhaIda${i}" />
					</mod:grupo>
					<mod:grupo largura="20">
						<mod:texto titulo="Empresa" var="empresaIda${i}" />
					</mod:grupo>
					<mod:grupo largura="10">
						<mod:selecao titulo="Bilhetes" var="bilhetesIda${i}" opcoes="Sim;Não" />
					</mod:grupo>
					<mod:grupo largura="20">						
						<mod:monetario maxcaracteres="10" var="valorIda${i}" titulo="Valor" formataNum="sim" relertab="sim" reler="sim" />
					</mod:grupo>
					<mod:grupo largura="10">
						<mod:texto titulo="Percurso do Servidor" var="percursoIda${i}" />
					</mod:grupo>
				</c:forEach>
			</mod:grupo>
			</mod:grupo>
			
			<mod:selecao var="quantidadePercursoVolta" titulo="Quantidade de percursos de volta" opcoes="1;2;3;4;5;6;7;8" reler="ajax" idAjax="quantidadePercursoVoltaAjax" />
			<mod:grupo depende="quantidadePercursoVoltaAjax" titulo="Percurso de volta (TRABALHO-RESIDÊNCIA)">
				<c:forEach var="i" begin="1" end="${quantidadePercursoVolta}">
					<mod:grupo largura="20">
						<mod:selecao titulo="Tipo de Transporte" opcoes="Barca;Metrô;Ônibus;Trem" var="tipoVolta${i}" />
					</mod:grupo>
					<mod:grupo largura="20">
						<mod:texto titulo="Linha" var="linhaVolta${i}" />
					</mod:grupo>
					<mod:grupo largura="20">
						<mod:texto titulo="Empresa" var="empresaVolta${i}" />
					</mod:grupo>
					<mod:grupo largura="10">
						<mod:selecao titulo="Bilhetes" var="bilhetesVolta${i}" opcoes="Sim;Não" />
					</mod:grupo>
					<mod:grupo largura="20">						
						<mod:monetario titulo="Valor" maxcaracteres="10" var="valorVolta${i}" formataNum="sim" relertab="sim" reler="sim" />
					</mod:grupo>
					<mod:grupo largura="10">
						<mod:texto titulo="Percurso do Servidor" var="percursoVolta${i}" />
					</mod:grupo>
				</c:forEach>
			</mod:grupo>
		</mod:grupo>
		
	</mod:entrevista>

	<mod:documento>
	
		<c:set var="tl" value="7pt" />	
	
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
		<p align="center" style="font-family:Arial;font-size:11pt;font-weight:bold;">REQUERIMENTO DE AUXÍLIO-TRANSPORTE</p>
		<mod:letra tamanho="${tl}">	
		<p> Finalidade: <c:if test="${finalidade=='Inclusão'}">Inclusão</c:if><c:if test="${finalidade=='Atualização'}">Atualização</c:if><c:if test="${finalidade=='Reinclusão'}">Reinclusão</c:if></p>		
		<table width="100%">
		<tr><td align="center">DADOS DO SERVIDOR</td></tr>
		</table>
		<br>
		<table width="100%">
			<tr>
				<td width="70%">
				Nome: ${doc.subscritor.descricao} 
				</td>
				<td>
				Matricula: ${doc.subscritor.sigla }	
				</td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td width="25%">
				Data de exercício: ${dataexercicio} 
				</td>
				<td width="65%">
				Lotação: ${doc.subscritor.lotacao.descricao }	
				</td>
				<td width="10%">
				Ramal: ${ramal}	
				</td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td width="82%">
				Cargo: ${doc.subscritor.cargo.descricao} 
				</td>
				<td width="18%">
				Classe/Padrão: ${doc.subscritor.padraoReferenciaInvertido}	
				</td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td width="100%">
				Endereço: ${endereco} 
				</td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td width="40%">
				Bairro: ${bairro} 
				</td>
				<td width="40%">
				Cidade: ${cidade }	
				</td>
				<td>
				CEP: ${cep}	
				</td>
			</tr>
		</table>
		<table width="100%">
			<tr>
				<td width="100%">
				Telefone: ${telefone} 
				</td>
			</tr>
		</table>
        <br>
		<c:if test="${mudaendereco=='Sim' or aumentotarifa=='Sim' or mudanca=='Sim' or outro=='Sim'}">
		<table width="100%">
			<tr>
				<td width="100%">
				Em caso de atualização, especificar o motivo:</br>
				<c:if test="${mudaendereco=='Sim'}">
				- Mudança de endereço</c:if></br>
				<c:if test="${aumentotarifa=='Sim'}">
				- Aumento de tarifa</c:if></br>
				<c:if test="${mudanca=='Sim'}">
				- Mudança de lotação</c:if></br>
				<c:if test="${outro=='Sim'}">
				- Outros: ${outroTexto}</c:if></br>
				</td>
			</tr>
		</table>
		</c:if>
		<br>
		<table width="100%">
		<tr><td align="center">PERCURSO DO SERVIDOR</td></tr>
		
		</table>
		<br>
		<table width="100%">
		<tr><td align="center">PERCURSO DE IDA (RESIDÊNCIA - TRABALHO)</td></tr>
		</table>
		<table width="100%">
		<tr>
		<td width="20%" align="center">TIPO DE TRANSPORTE</td><td width="42%" align="center">SOMENTE PARA ÔNIBUS<br>Linha &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Empresa &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bilhetes </td><td width="10%">VALOR</td><td width="28%">PERCURSO DO SERVIDOR</td>
		</tr>
		</table>
		
		</table>
		<c:forEach var="i" begin="1" end="${quantidadePercursoIda}">
			<table width="100%">
				<tr>
					<td width="20%">${requestScope[f:concat('tipoIda',i)]}</td><td width="14%" align="center">${requestScope[f:concat('linhaIda',i)]}</td><td width="14%" align="center">${requestScope[f:concat('empresaIda',i)]}</td><td width="14%">${requestScope[f:concat('bilhetesIda',i)]}</td><td width="10%">${requestScope[f:concat('valorIda',i)]}</td><td width="28%">${requestScope[f:concat('percursoIda',i)]}</td>	
			</table>
		</c:forEach>
		
		<br>
		<table width="100%">
		<tr><td align="center">PERCURSO DE VOLTA (TRABALHO - RESIDÊNCIA)</td></tr>
		</table>
		<table width="100%">
		<tr>
		<td width="20%" align="center">TIPO DE TRANSPORTE</td><td width="42%" align="center">SOMENTE PARA ÔNIBUS<br>Linha &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Empresa &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bilhetes </td><td width="10%">VALOR</td><td width="28%">PERCURSO DO SERVIDOR</td>
		</tr>
		</table>
		
		<c:forEach var="i" begin="1" end="${quantidadePercursoVolta}">
			<table width="100%">
				<tr>
					<td width="20%">${requestScope[f:concat('tipoVolta',i)]}</td><td width="14%" align="center">${requestScope[f:concat('linhaVolta',i)]}</td><td width="14%" align="center">${requestScope[f:concat('empresaVolta',i)]}</td><td width="14%">${requestScope[f:concat('bilhetesVolta',i)]}</td><td width="10%">${requestScope[f:concat('valorVolta',i)]}</td><td width="28%">${requestScope[f:concat('percursoVolta',i)]}</td>	
			</table>
		</c:forEach>
		<br>
		<p align="justify"> Declaro ter conhecimento de que constitui falta grave a falsidade desta declaração. Comprometo-me, ainda, a utilizar transportes sem caracteristicas especiais e/ou seletivas, exceto quando não houver outro meio de transporte para o percurso realizado, responsabilizando-me a manter atualizados meus dados cadastrais para efeito do benefício, comunicando, incontinenti, por meio de Requerimento de Atualização, quaisquer alterações que possam implicar mudança do percurso declarado, sob pena de perda do benefício</p>
	
	<p align="center">${doc.dtExtenso} </p>
	<p align="center">Assinatura do requerente: _________________________________________________________</p>
	<table width="100%">
	<tr><td align="center"><p>ANEXAR XEROX DO COMPROVANTE DE RESIDÊNCIA EM NOME DO PRÓPRIO<br/>NOS CASOS DE INCLUSÃO OU ATUALIZAÇÃO POR MOTIVO DE MUDANÇA DE ENDEREÇO<BR/>(Encaminhar o formulário para a Seção de Benefícios - Av. Rio Branco, 243 - 10º andar - anex I</td></tr>
	</table>
	<table width="100%">
		<tr>
			<td width="60"valign="top">PARA USO DA SEÇÃO DE BENEFÍCIOS<br>Recebido por:_______ em: __/__/__<br>Cadastrado por:______ em:__/__/__<br>Obs: _____________________________________________________<br>_________________________________________________________<br><br></td><td width="40%" valign="top" align="right">[&nbsp;&nbsp;] Registro do servidor &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; [&nbsp;&nbsp;] Ítens de Transporte<br>[&nbsp;&nbsp;] Avulsos: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; mês1:______&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;nº de dias:_____<br/>mês2:_____  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  nº de dias:_____<br/>mês3:_____  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  nº de dias:_____<br/>Total:_____<br><br></td>
		
	</table>
	</mod:letra>
	</body>
	</html>
	</mod:documento>
</mod:modelo>
	
