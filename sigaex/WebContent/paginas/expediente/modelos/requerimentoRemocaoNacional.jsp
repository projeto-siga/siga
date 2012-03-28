<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:selecao titulo="Ano da Remoção" opcoes="2011;2012;2013;2014;2015;2016;2017;2018"
			var="anoRemocao" />
			&nbsp;&nbsp;&nbsp;
			<mod:data  var="dataEdital" titulo="Data de publicação do Edital" obrigatorio="Sim"/>
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Área do cargo" opcoes="administrativa;judiciária"
			var="area" /> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<mod:texto obrigatorio="Sim" var="email" titulo="E-mail"
			largura="30" maxcaracteres="50" />
		</mod:grupo>	
		<mod:grupo>
			<mod:texto var="qtdFilhos" titulo="Quantidade de Filhos"
			largura="3" maxcaracteres="2" />
		</mod:grupo>
		<mod:grupo>	
			<mod:mensagem vermelho="Sim"
			texto="ATENÇÃO: Transferir para Seção de Apoio da Subsecretaria de Gestão de Pessoas 
			após finalização."></mod:mensagem>
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

@
body {
	margin-top: 6cm;
	margin-bottom: 0.5cm;
}

@
first-page-body {
	margin-top: 6cm;
	margin-bottom: 0.5cm;
}
</style>
		</head>
		<body>
		<!-- FOP -->
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPaginaSemSJRJ-JF.jsp" />
		</td></tr><tr><td></tr></td>
		</table>
	
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda2.jsp" />
		FIM CABECALHO -->
		<br />

		<br />
		<p align="justify" style="line-height: 150%">Excelentíssimo Senhor
		Diretor do Foro</p>
		<br />
		<br />
		<p align="justify" style="line-height: 200%">Eu,
		${doc.subscritor.descricao}, ocupante do cargo efetivo de ${doc.subscritor.cargo.nomeCargo},
		área ${area}, do quadro pessoal da Seção Judiciária do Rio de Janeiro, matrícula nº
		RJ${doc.subscritor.matricula}, venho requerer a V. Exa. a inscrição no
		Concurso Nacional de Remoção a Pedido Mediante Permuta ${anoRemocao}, com base
		no art. 20 da Lei nº 11.416/2006 c/c a Lei nº 8.112/1990, e nos termos
		da Portaria Conjunta nº 3/2007, da Resolução 3/2008 alterada pela
		Resolução nº 66/2009 do Conselho da Justiça Federal e declaro que
		concordo com os termos do Edital do CJF publicado em ${dataEdital}.</p>
		<br />
		<br />
		<p>Nestes termos,</p>
		<p>Peço deferimento.</p>
		<br />
		<br />

		<p align="center">${doc.dtExtenso}</p>
		<br />
		<br />
		<br />
		<br />

		<p></p>
		<p></p>


		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />

		<p>Informações adicionais:</p>
		<p>CPF: ${f:formatarCPF(doc.subscritor.cpfPessoa)}</p>
		<p>E-mail: ${email}</p>
		<c:if test="${empty qtdFilhos}">
			<c:set var="qtdFilhos" value="0" />
		</c:if>
		<p>Quantidade de filhos: ${qtdFilhos}</p>

		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental2.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita2.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>
