<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!--
Modelo de Designação SRH
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

		<!-- INICIO CABECALHO

			<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />

		FIM CABECALHO -->
		<br>
		<br>

		<!-- INICIO ABERTURA --><p style="TEXT-INDENT: 2cm" align="justify">A DIRETORIA DA
		SUBSECRETARIA DE GESTÃO DE PESSOAS DA JUSTIÇA FEDERAL - SEÇÃO
		JUDICIÁRIA DO RIO DE JANEIRO, usando a competência que lhe foi
		delegada pela portaria nº 011 - GDF, de 26 de março de 2003,
		${textoPortaria}, RESOLVE:</p><!-- FIM ABERTURA -->
		<br>
		<br>
		<br>

		<p style="MARGIN-LEFT: 2cm" align="justify">DESIGNAR o(a)
		servidor(a) <b>${doc.subscritor.descricao}</b>,<b>${doc.subscritor.cargo.nomeCargo}</b>,
		<b>${doc.subscritor.padraoReferenciaInvertido}</b>, matrícula n&ordm; <b>${doc.subscritor.sigla}</b>,
		para exercer a função comissionada de <b>${param['funcaoComissionada_funcaoSel.descricao']}</b>
		(FC <b>${param['funcaoComissionada_funcaoSel.sigla']}</b>) da <b>${doc.subscritor.lotacao.descricao
		}</b>, a partir da publicação desta portaria.</p>
		<br>
		<br>
		
		<p style="TEXT-INDENT: 2 cm" align="center">CUMPRA-SE.
		REGISTRE-SE. PUBLIQUE-SE</p>
		
		<br>
		<br>
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		<!-- 
		<p style="TEXT-INDENT: 2 cm" align="center"><b>REGINA HELENA MOREIRA FARIA</b> <br>
		<b>Diretora de Subsecretaria de Gestão de Pessoas</b></p>
		-->
		<br>
		<br>
		<br>
		</body>



		<table border=0 align="center" valign="bottom" cellpadding=5>

			<tr>

				<td valign="bottom">

				<table border=1>

					<tr>

						<td><font size=1>Publicada no boletim interno online<br>
						de ___/___/__ , nos termos do art 15,§ 4º, da<br>
						lei nº 9527 de 10/12/97 c/c com o art 7º da resolução nº<br>
						284/2002-CJF.</font><br>

						</td>

					</tr>

					<tr>

						<td align="center" width="2" colspan="2"><font size="1">
						_______________________________<br>
						Assinatura </font></td>




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


