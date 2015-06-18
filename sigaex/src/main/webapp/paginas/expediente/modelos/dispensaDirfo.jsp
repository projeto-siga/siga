<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- 
Este modelo trata da Dispensa do Servidor
Ultima atualização 12/03/2007
 -->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DA DISPENSA">
			<mod:grupo>
				<mod:funcao titulo="Função" var="funcaoComissionada" />
			</mod:grupo>
			<mod:grupo>
				<mod:data titulo="Data Dispensa" var="dataDispensa" />
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
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br />
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight: bold;">PORTARIA N&ordm; ${doc.codigo} DE ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<br>
		<br>

		<!-- INICIO ABERTURA --><p style="TEXT-INDENT: 2cm" align="justify">O JUIZ FEDERAL -
		DIRETOR DO FORO E CORREGEDOR PERMANENTE DOS SERVIÇOS AUXILIARES DA
		JUSTIÇA FEDERAL DE 1º GRAU - SEÇAO JUDICIÁRIA DO RIO DE JANEIRO, no uso
		de suas atribuiçoes legais e, ${textoPortaria}, RESOLVE:</p><!-- FIM ABERTURA -->
		<br>
		<br>
		
		<p style="MARGIN-LEFT: 2cm" align="justify">DISPENSAR o(a)
		servidor(a) <b>${doc.subscritor.descricao}</b>, <b>${doc.subscritor.cargo.nomeCargo}</b>,
		<b>${doc.subscritor.padraoReferenciaInvertido}</b>, matrícula n&ordm; <b>${doc.subscritor.sigla}</b>,
		da função comissionada de <b>${requestScope['funcaoComissionada_funcaoSel.descricao']}</b>, da <b>${doc.subscritor.lotacao.descricao}</b>,
		a partir de <b>${dataDispensa}</b>.</p>
		<br>
		<br>
		
		<p style="TEXT-INDENT: 2 cm" align="center">CUMPRA-SE.
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


