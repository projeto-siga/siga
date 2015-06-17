<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!-- 
     Modelo : Remocao Dirfo
     Ultima alteracao : 12/03/2007 
-->
<!-- 
<script>
 function aviso(){
   var mensagem = "Obrigatório o preenchimento de todos os campos";   
   document.getElementById("msg").innerHTML=mensagem;            
   document.getElementById("msg").value=mensagem;
 }
</script>

<body onload="aviso()">
-->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DA REMOÇÃO" />
		<mod:grupo>
			<mod:lotacao titulo="Lotação Nova" var="lotacaoNova" />
		</mod:grupo>
		<mod:grupo>
			<mod:data titulo="Data Remoção" var="dataRemocao" />
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
		JUSTIÇA FEDERAL DE 1º GRAU - SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO, no
		uso de suas atribuições legais e, ${textoPortaria}, RESOLVE:</p><!-- FIM ABERTURA -->
		<br>
		<br>

		<p style="MARGIN-LEFT: 2cm" align="justify">REMOVER o(a)
		servidor(a) <b>${doc.subscritor.descricao}</b>, <b>${doc.subscritor.cargo.nomeCargo}</b>,
		<b>${doc.subscritor.padraoReferenciaInvertido}</b>, matrícula n&ordm; <b>${doc.subscritor.sigla}</b>,
		da Lotação <b>${doc.subscritor.lotacao.descricao}</b>
		para <b>${requestScope['lotacaoNova_lotacaoSel.descricao']}</b>, a
		partir de <b>${dataRemocao}</b>.</p>
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
