<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- este modelo trata de
FORMULÁRIO DE EXCLUSÃO DE USUÁRIO-->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:selecao titulo="Excluir Usuário" opcoes="Magistrado;Servidor"
				var="tipoUsuario" reler="sim" />
		</mod:grupo>

		<!-- doc.subscritor.matricula -->

		<mod:grupo titulo="Identificação do Usuário">
			<mod:pessoa titulo="Matrícula do Usuário" var="usuario" />
			<br />
			<mod:texto titulo="E-mail Institucional (alias)" var="email"
				largura="30" maxcaracteres="30" obrigatorio="Sim" />@jfrj.jus.br <br />
		</mod:grupo>
		<mod:grupo titulo="Telefone Funcional:">
			<mod:texto titulo="Código de área" var="codArea" largura="2"
				maxcaracteres="2" obrigatorio="Sim" />
			<mod:texto titulo="Número" var="telefone" maxcaracteres="10"
				largura="10" obrigatorio="Sim" />
		</mod:grupo>

		<c:if test="${tipoUsuario == 'Magistrado'}">
			<mod:grupo>
				<mod:mensagem titulo="Observação"
					texto="exclusão é necessária somente se a remoção se der para outra seccional ou Tribunal.
				O subscritor do formulário de exclusão deve ser o próprio magistrado. Caso o magistrado já tenha 
				sido removido, o formulário de solicitação de exclusão poderá ter como subscritor o Diretor de Secretaria."
					vermelho="Sim" />
			</mod:grupo>
		</c:if>

		<c:if test="${tipoUsuario == 'Servidor'}">
			<mod:grupo>
				<mod:mensagem titulo="Observação"
					texto="O subscritor do formulário de exclusão de servidor será o Magistrado da vara."
					vermelho="Sim" />
			</mod:grupo>
		</c:if>
	</mod:entrevista>

	<mod:documento>
		<c:set var="tl" value="9pt" />
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
		<table width="100%" border="0" bgcolor="#FFFFFF">
			<tr>
				<td>
				<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="center">
								<p style="font-family:Arial;font-size:11pt;font-weight:bold;">FORMUL&Aacute;RIO DE EXCLUS&Atilde;O DE USU&Aacute;RIO <br />RENAJUD WEB</p>
							</td>
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
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<br />
		<mod:letra tamanho="${tl}">


			<table bgcolor="#000000" border="1" cellpadding="5" align="center">
				<tr>
					<td bgcolor="#FFFFFF" colspan="3" align="left"><b> 1 - SOLICITAÇÃO DE
					EXCLUSÃO DE <c:if test="${tipoUsuario == 'Magistrado'}">
    MAGISTRADO 
    </c:if> <c:if test="${tipoUsuario == 'Servidor'}">
    SERVIDOR
    </c:if> </b></td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" colspan="3" align="left"><b>1.1 - IDENTIFICAÇÃO DO
					USUÁRIO </b></td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" colspan="3" align="left"><b>NOME COMPLETO</b><br />
					${f:pessoa(requestScope['usuario_pessoaSel.id']).nomePessoa}</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" width="40%" align="left"><b>MATR&Iacute;CULA</b><br />
					RJ${f:pessoa(requestScope['usuario_pessoaSel.id']).matricula}</td>
					<td bgcolor="#FFFFFF" width="60%" colspan="2" align="left"><b>CPF</b><br />
					${f:formatarCPF(f:pessoa(requestScope['usuario_pessoaSel.id']).cpfPessoa)}</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" align="left"><b>LOTA&Ccedil;&Atilde;O</b><br />
					${f:pessoa(requestScope['usuario_pessoaSel.id']).lotacao.descricao}</td>
					<td bgcolor="#FFFFFF" align="left" valign="top"><b>E-MAIL INSTITUCIONAL</b><br />
					${email}@jfrj.jus.br</td>
					<td bgcolor="#FFFFFF" align="left" valign="top"><b>TELEFONE FUNCIONAL</b><br />
					(${codArea}) ${telefone}</td>
				</tr>

				<tr>
					<td bgcolor="#FFFFFF" align="left"><b>LOCAL/DATA</b><br />
					${doc.dtExtenso}</td>
					<td bgcolor="#FFFFFF" colspan="2" align="center"><br />
					<br />

					<c:if test="${tipoUsuario == 'Magistrado'}">
						____________________________________________
						<br />Assinatura do Solicitante<br />
						${doc.subscritor.descricao}
					</c:if>
					<c:if test="${tipoUsuario == 'Servidor'}">
						____________________________________________
						<br />Assinatura do Juiz Solicitante<br />
						${doc.subscritor.descricao}
					</c:if>
					</td>
				</tr>
			</table>


		</mod:letra>
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
