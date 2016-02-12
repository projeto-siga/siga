<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:lotacao titulo="Unidade Organizacional" var="lotacao"/>
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Mês de competência" var="mes"
				opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro" />
		</mod:grupo>

		<mod:grupo titulo="Dados dos Analistas">
			<mod:selecao titulo="Total de Analistas"
				var="totalAnalistas" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20" reler="sim" />
			<br/>
			<c:forEach var="i" begin="1" end="${totalAnalistas}">
				<mod:grupo>
					<mod:pessoa titulo="Matrícula" var="analista${i}"/>
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Quantidade de dias" var="qtd${i}" largura="5" />
				</mod:grupo>
				<mod:grupo>
					<mod:memo titulo="OBSERVAÇÕES (licenças, plantões e outros)" var="obs${i}" colunas="80" linhas="3"/>
				</mod:grupo>
				<br />
			</c:forEach>	
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
		</td></tr><tr><td></tr></td>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<br/><br/>
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">ATESTADO DE SERVIÇOS EXTERNOS <br /><br />N&ordm; ${doc.codigo}</p></td>
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

		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
			  <td bgcolor="#FFFFFF"><strong>UNIDADE ORGANIZACIONAL:</strong>&nbsp;${requestScope['lotacao_lotacaoSel.descricao']}</td>
				<td bgcolor="#FFFFFF"><strong>M&Ecirc;S DE COMPET&Ecirc;NCIA:</strong>&nbsp;${mes}</td>
			</tr>
		</table>

			<br/>
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
          <tr>
            <td bgcolor="#FFFFFF"> <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Em  cumprimento &agrave; Resolu&ccedil;&atilde;o N.&ordm; 358, de 29/3/2004, do Conselho da Justi&ccedil;a  Federal- CJF, e do artigo 15 da Lei 9289, de 4/7/1996, ATESTO, para os  devidos fins, que os   <strong>Analistas Judici&aacute;rios/Execu&ccedil;&atilde;o de Mandados</strong>, abaixo relacionados, realizaram servi&ccedil;os externos no m&ecirc;s acima indicado.</p></td>
          </tr>
        </table>
		<br/>
		<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
			<tr>
			  <td width="17%" bgcolor="#FFFFFF" align="center"><strong>MATR&Iacute;CULA</strong></td>
			  <td width="40%" bgcolor="#FFFFFF" align="center"><strong>NOME</strong></td>
				<td width="18%" bgcolor="#FFFFFF" align="center"><p><strong>QUANTIDADE DE DIAS</strong></p></td>
				<td width="25%" bgcolor="#FFFFFF" align="center"><p><strong>OBSERVA&Ccedil;&Otilde;ES &nbsp;</strong></p>
			    <p><strong>(licen&ccedil;as, plant&otilde;es e outros)</strong></p></td>
			</tr>
			<c:forEach var="i" begin="1" end="${totalAnalistas}">
				<tr>
				  <td width="17%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat(f:concat('analista',i),'_pessoaSel.sigla')]}</td>
				  <td width="40%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat(f:concat('analista',i),'_pessoaSel.descricao')]}</td>
					<td width="18%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('qtd',i)]} dia(s)</td>
					<td width="25%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('obs',i)]}</td>
				</tr>
			</c:forEach>
		</table>
		<br />
		

					<p align="center">${doc.dtExtenso}</p>
					<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
					<br /><br />


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
