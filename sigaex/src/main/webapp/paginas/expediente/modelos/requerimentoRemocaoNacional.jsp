<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:selecao titulo="Ano da Remoção" opcoes="2015;2016;2017;2018;2019"
			var="anoRemocao" />
			&nbsp;&nbsp;&nbsp;
			<mod:texto var="numEdital" titulo="Número do Edital"
			largura="22" obrigatorio="Sim"/>
			&nbsp;&nbsp;&nbsp;
			<mod:data  var="dataEdital" titulo="Data de publicação do Edital" obrigatorio="Sim"/>
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Área do cargo" opcoes="apoio especializado;administrativa;judiciária"
			var="area" /> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<mod:texto obrigatorio="Sim" var="email" titulo="E-mail"
			largura="50" maxcaracteres="50" />
		</mod:grupo>	
		<mod:grupo>
			<mod:texto var="qtdFilhos" titulo="Quantidade de Filhos"
			largura="3" maxcaracteres="2" />
		</mod:grupo>
		<mod:grupo>	
			<mod:mensagem vermelho="Sim"
			texto="ATENÇÃO: Transferir para Subsecretaria de Gestão de Pessoas 
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


		<p align="justify" style="line-height: 150%">Excelentíssimo Senhor
		Diretor do Foro</p>


		<p align="justify" style="line-height: 200%">Eu,
		${doc.subscritor.descricao}, ocupante do cargo efetivo de ${doc.subscritor.cargo.nomeCargo},
		área ${area}, do quadro pessoal da Seção Judiciária do Rio de Janeiro, matrícula nº
		RJ${doc.subscritor.matricula}, venho requerer a V. Exa. a inscrição no
		Concurso Nacional de Remoção ${anoRemocao},com base no art. 20 da Lei n. 11.416/2006 c/c a Lei n.
8.112/1990, no Anexo IV da Portaria Conjunta n. 3, de 31 de maio de 2007, e no art. 27, § 3º,
da Resolução n. 3, de 10 de março de 2008, do Conselho da Justiça Federal e declaro que
concordo com os termos do EDITAL n. ${numEdital}, de ${dataEdital}.</p>
		
		<br />
		<p>Nestes termos,</p>
		<p>Peço deferimento.</p>

		<p align="center">${doc.dtExtenso}</p>
		<br />

		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />

		<p>Informações adicionais:</p>
		<p>CPF: ${f:formatarCPF(doc.subscritor.cpfPessoa)} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;E-mail: ${email}</p>
		<c:if test="${empty qtdFilhos}">
			<c:set var="qtdFilhos" value="0" />
		</c:if>
		<p>Quantidade de filhos: ${qtdFilhos}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Data de nascimento: <fmt:formatDate pattern="dd/MM/yyyy" value="${doc.subscritor.dataNascimento}" /></p>

		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental2.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita2.jsp" />
		FIM RODAPE -->
		<p align="center">Termo de ciência e concordância:</p>
&nbsp;&nbsp;&nbsp;Afirmo que estou ciente do prazo para desistência previsto no edital e assumo a
responsabilidade pela participação no certame.<br/>
&nbsp;&nbsp;&nbsp;Afirmo que estou ciente que posso ser removido para qualquer das opções
escolhidas e concordo com as regras previstas no edital do concurso Nacional de
Remoção de ${anoRemocao}.<br/>
&nbsp;&nbsp;&nbsp;Afirmo que estou ciente de que, se contemplado com a remoção, deverei cancelar as
férias deferidas pelo órgão de origem para remarcá-las diretamente no órgão de
destino.<br/>
&nbsp;&nbsp;&nbsp;Concordo em receber e-mail dos candidatos inscritos no SINAR.
		</body>
		</html>
	</mod:documento>
</mod:modelo>
