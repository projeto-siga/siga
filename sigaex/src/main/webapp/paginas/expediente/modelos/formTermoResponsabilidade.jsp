<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>

		<br />
		<mod:mensagem titulo="OBS" texto="O cadastramento de servidores no sistema INFOJUD-e-CAC 
		deve ser feito pelo próprio magistrado. O Termo de Responsabilidade de servidor deve ser arquivado 
		na própria vara, para controle dos acessos concedidos." vermelho="Sim" />
		<br /><br />
		
	</mod:entrevista>
	<mod:documento>
	<c:set var="tl" value="8pt" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
@body {
	margin-top: 6cm;
	margin-bottom: 0.5cm;
}
@first-page-body {
	margin-top: 6cm;
	margin-bottom: 0.5cm; 
}
		</style>
		</head>
		<body>
		<!-- FOP -->
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina2.jsp" />
		</td></tr><tr><td></tr></td>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<br/><br/>
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">INFOJUD-E-CAC - SECRETARIA DA RECEITA FEDERAL
							</td>
						</tr>
						<tr><td><br /></td></tr>
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">	
							TERMO DE RESPONSABILIDADE</p></td>
						</tr>
						<tr>
							<td align="right">
								<p><b>Formulário N&ordm; ${doc.codigo}</b></p><br/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda2.jsp" />
		FIM CABECALHO -->
		<br />
		
		<mod:letra tamanho="${tl}">
			<p align="justify" style="line-height: 150%">Eu, ${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, lotado no(a) ${doc.subscritor.lotacao.descricao}, matrícula nº RJ${doc.subscritor.matricula},
			cadastrado no CPF/MF sob o nº ${f:formatarCPF(doc.subscritor.cpfPessoa)} comprometo-me a manter sigilo sobre os dados cadastrais a que tenha acesso ou
			conhecimento, em razão do Convênio INFOJUD, celebrado entre a Secretaria de Receita Federal (SRF) e o Tribunal Regional Federal da 2ª Região, em 18/12/2006,
			visando simplificar e agilizar o atendimento de requisição de informação protegida por sigilo fiscal. Do mesmo modo, estou ciente do que preceitua o Decreto
			N. 4.553 de 27/12/2002 (salvaguarda de dados, informações, documentos e materiais sigilosos). Comprometo-me a utilizar os dados a que tiver acesso exclusivamente
			dentro das atribuições de minha responsabilidade.<br />
			E por estar de acordo com o presente Termo, assino-o na presença das testemunhas abaixo nomeadas.
			</p>
			<br /><br /><br />

			<p align="center">${doc.dtExtenso}</p>
			<br /><br />
		
		</mod:letra>

	<p> </p> <p> </p>
			
			
					<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
					

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
