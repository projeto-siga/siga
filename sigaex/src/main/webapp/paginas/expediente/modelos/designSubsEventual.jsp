<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!-- 
Este modelo trata da
Designação de Substituição Eventual - Dirfo
Ultima atualização 09/03/2007
 -->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DA SUBSTITUIÇÃO">
			<mod:grupo>
				<mod:data titulo="Data início" var="dataInicio" />
				<mod:data titulo="Data fim" var="dataFim" />
			</mod:grupo>
		</mod:grupo>	
		<mod:grupo>
			<font color="black"><b> <mod:memo colunas="60" linhas="3"
				titulo="Texto a ser inserido no corpo da portaria"
				var="textoPortaria" /> </b></font>
		</mod:grupo>
	</mod:entrevista>

	<mod:documento>

		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

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
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight: bold;">PORTARIA N&ordm; ${doc.codigo} de ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<br>
		<br>
		
		<!-- INICIO ABERTURA --><p style="TEXT-INDENT: 2 cm" align="justify">O JUIZ FEDERAL -
		DIRETOR DO FORO E CORREGEDOR PERMANENTE DOS SERVIÇOS AUXILIARES DA
		JUSTIÇA FEDERAL DE 1º GRAU - SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO, no
		uso de suas atribuições legais e, <b>${textoPortaria }</b>, RESOLVE:</p><!-- FIM ABERTURA --><br>
		<br>
		<br>

		<p style="MARGIN-LEFT: 2cm" align="justify">DESIGNAR o(a) servidor(a) <b>${doc.titular.descricao}</b>,
		<b>${doc.titular.cargo.nomeCargo}</b>, <b>${doc.titular.lotacao.descricao}</b>,
		<b>${doc.titular.padraoReferenciaInvertido}</b>, matrícula N&ordm; <b>${doc.titular.sigla}</b>,
		para atuar como substituto(a) eventual do Diretor de Secretaria
		(CJ 03) da <b>${doc.subscritor.lotacao.nomeLotacao}</b>, em seus afastamentos, impedimentos legais ou
		regulamentares e na vacância do cargo, no período de <b>${dataInicio }</b> a <b>${dataFim }</b>.</p> <br>
		<br>
		<p style="TEXT-INDENT: 2cm" align="center">CUMPRA-SE.
		REGISTRE-SE. PUBLIQUE-SE</p>

		<br>
		<br>
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		<!-- 
		<p style="TEXT-INDENT: 2 cm" align="center"><b>CARLOS
		GUILHERME FRANCOVICH LUGONES</b> <br>
		<b>Juiz Federal - Diretor do Foro</b></p>
		-->
		<br>
		<br>
		<br>
		</body>
		</html>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
	</mod:documento>
</mod:modelo>
