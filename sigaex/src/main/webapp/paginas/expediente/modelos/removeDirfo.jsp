<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!-- 
     Modelo : Remove Dirfo
     Data da Criacao : 25/01/2007
     Ultima alteracao : 25/01/2007 
-->

<script>
 function aviso(){
   var mensagem = "Obrigatório o preenchimento de todos os campos";   
   document.getElementById("msg").innerHTML=mensagem;            
   document.getElementById("msg").value=mensagem;
 }
</script>
<!--  
 function trataData(){
    
     alert("entrei aqui ::"); 
     var d1 = new Date()
     alert("data do sistema " + v1); 
 
     var d = document.getElementById("dataPortaria").value; 
     alert("data Digitada " + d); 
     var dia = d.getDay()
     var mes = d.getMonth()
     var ano = d.getFullYear()

     // Para obtermos o nome do dia criamos um Array em que Domingo ocupa a
     // posição 0, segunda ocupa a posição 1, ...
     var nomesDias=new Array("Domingo","Segunda-feira","Terça-feira","Quarta-feira",
	 "Quinta-feira","Sexta-feira","Sábado")
     var nomesMeses=new Array("Janeiro","Fevereiro","Março","Abril","Maio","Junho",
	 "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro")

     var data="Hoje é "+nomesDias[dia]+", "+d.getDate()
     data+=" de "+nomesMeses[mes]+" de "+ano    
     alert (data) ; 
 }

 function pegaTextoFCKeditor() {
   var oEditor = FCKeditorAPI.GetInstance("texto_oficio");
   mensagemCorpo = oEditor.GetXHTML();
   tamanho = mensagemCorpo.length;
   mensagemCorpo = mensagemCorpo.substring(0,42); 
   mens = mensagemCorpo.substring(44,tamanho-4);     
   document.getElementById("mensagemCorpo").innerHTML=mens;   
   document.getElementById("mensagemCorpo").value=mens;
 }
 
 		<mod:grupo titulo="DADOS DA PORTARIA" />
		<mod:grupo>
			<mod:numero titulo="Nº da Portaria" var="numeroPortaria" largura="14"
				maxcaracteres="12" />
			<mod:data titulo="Data da Portaria" var="dataPortaria" />
		</mod:grupo>
-->

<body onload="aviso()">
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DA REMOÇÃO" />
		<font color="red"><b><span id="msg"></span></b></font>
 		<mod:grupo titulo="DADOS DA PORTARIA" />
		<mod:grupo>
			<mod:data titulo="Data da Portaria" var="dataPortaria" />
		</mod:grupo>
		<mod:grupo titulo="DADOS DO SERVIDOR" />
		<mod:grupo>
			<mod:texto titulo="Nome" var="nomeServidor" largura="62"
				maxcaracteres="60" />
			<mod:texto titulo="Matrícula" var="matriculaServidor" largura="12"
				maxcaracteres="10" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Cargo/Especialidade" var="cargoEspecialidade"
				largura="45" maxcaracteres="43" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Área" var="areaServidor" largura="45"
				maxcaracteres="43" />
			<mod:texto titulo="Referência" var="referenciaAreaServidor"
				largura="45" maxcaracteres="43" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Lotação Antiga" var="lotacaoAntiga" largura="45"
				maxcaracteres="43" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Lotação Nova" var="lotacaoNova" largura="45"
				maxcaracteres="43" />
		</mod:grupo>
		<mod:grupo>
			<mod:data titulo="A partir da Data" var="dataRemocao" />
		</mod:grupo>
		<mod:grupo>
			<mod:memo colunas="60" linhas="3"
				titulo="Texto a ser inserido no corpo da Remoção" var="texto_oficio" />
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			@page {
				margin-left: 2cm;
				margin-right: 2cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}			
		</style>
		</head>
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
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight: bold;">PORTARIA N&ordm; ${doc.codigo} DE ${dataPortaria}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		
		<p>&nbsp;</p>
		<!-- INICIO ABERTURA --><p style="TEXT-INDENT: 1.0cm" align="justify">O J U I Z F E D E R
		A L - DIRETOR DO FORO E CORREGEDOR PERMANENTE DOS SERVIÇOS AUXILIARES
		DA JUSTIÇA FEDERAL DE 1º GRAU - SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO, no
		uso de suas atribuições legais e, ${texto_oficio}, RESOLVE:<br><!-- FIM ABERTURA -->
		<br>
		<br>
		REMOVER o(a) servidor(a) ${nomeServidor},<br>
		${cargoEspecialidade}, Área ${areaServidor},
		${referenciaAreaServidor}, matrícula nº <br>
		${matriculaServidor}, da Lotação ${lotacaoAntiga} para ${lotacaoNova}
		a partir de ${dataRemocao}.<br>
		<br>
		<br>
		<p align="center">CUMPRA-SE. REGISTRE-SE. PUBLIQUE-SE.</p>
		<br>
		<br>
		<p align="center">CARLOS GUILHERME FRANCOVICH LUGONES<br>
		Juiz Federal - Diretor do Foro</p>
		<br>
		<br>
		<br>
		</p>
		</body>
		</html>
		<c:import
			url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
	</mod:documento>
</mod:modelo>
</body>
