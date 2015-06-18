<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!-- 
Este modelo trata da
Designação do Servidor
Ultima atualização 12/03/2007
 -->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DA DESIGNAÇÃO">
			<mod:grupo>
				<mod:funcao titulo="Função" var="funcaoComissionada" />
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

		<!-- INICIO ABERTURA --><p style="TEXT-INDENT: 2cm" align="justify">O JUIZ FEDERAL -
		DIRETOR DO FORO E CORREGEDOR PERMANENTE DOS SERVIÇOS AUXILIARES DA
		JUSTIÇA - SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO, no uso de suas
		atribuições legais, ${textoPortaria }, RESOLVE:</p><!-- FIM ABERTURA --> <br>
		<br>
		<br>
	
		<p style="MARGIN-LEFT: 2cm" align="justify">DESIGNAR o(a) servidor(a) <b>${doc.subscritor.descricao}</b>,
		<b>${doc.subscritor.cargo.nomeCargo}</b>, <b>${doc.subscritor.padraoReferenciaInvertido}</b>, matrícula n&ordm;
		<b>${doc.subscritor.sigla}</b>, lotado no(a) <b>${doc.subscritor.lotacao.descricao}</b>, para exercer a função
		comissionada de <b>${requestScope['funcaoComissionada_funcaoSel.descricao']}</b> (FC <b>${requestScope['funcaoComissionada_funcaoSel.sigla']}</b>), a partir da publicação desta portaria.</p>
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

		<table align="center" cellpadding=5>

			<tr>

				<td valign="bottom">

				<table align="center" border="1" cellpadding="1" cellspacing="1">
					<tr>
						<td><font size="1">Publicada no Boletim Interno "on
						line" de ___/__/___, nos termos do art.15, § 4º, da Lei nº 9527 de
						10/12/97, publicada no D.O.U. de 11/12/97 c/c com o art. 7º da
						Resolução nº 284/2002-CJF.</font> <br>
						<br>
						<br>

						_____________________Assinatura</td>
					</tr>
				</table>

				</td>

				<td valign="bottom">

				<table border=1>

					<tr>

						<td align="center" width="60%"
							style="font-family:Arial;font-size:8pt;text-decoration:italic;"
							bgcolor="#FFFFFF">Classif. documental</td>

						<td align="center" width="40%"
							style="font-family:Arial;font-size:8pt;" bgcolor="#FFFFFF">${doc.exClassificacao.sigla}</td>

					</tr>

				</table>

				</td>

			</tr>

		</table>
		</html>
	</mod:documento>
</mod:modelo>
