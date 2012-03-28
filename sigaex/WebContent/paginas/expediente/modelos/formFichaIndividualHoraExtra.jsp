<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%> 
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
		    <mod:pessoa titulo="Matrícula" var="servidor" />
		</mod:grupo>
		<mod:grupo> 
			<mod:lotacao titulo="Unidade Organizacional" var="unidade" />
		</mod:grupo>	
		<mod:grupo>
			<mod:selecao titulo="Mês" var="mes" 
			opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro"/> &nbsp;
			<mod:texto titulo="Ano" var="ano" maxcaracteres="4" largura="4"/> &nbsp;
			<mod:selecao titulo="Número de dias incluídos" 	var="numDiasHoraExtra"
			  opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31"
			  reler="ajax" idAjax="diasAjax" />
		</mod:grupo>    		
		<mod:grupo depende="diasAjax">	
			<c:forEach var="i" begin="1" end="${numDiasHoraExtra}">
				<b>${i}º dia:</b>
				<mod:grupo>	
					<mod:selecao titulo="Dia do mês"  var="diaMes${i}" 
			         	opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31" />
			        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 						
					<mod:selecao titulo="Dia da semana" var="diaSemana${i}"
						opcoes="Domingo;Segunda-feira;Terça-feira;Quarta-feira;Quinta-feira;Sexta-feira;Sábado"/>
				</mod:grupo>
				<mod:grupo>
					<mod:hora titulo="Início" var="inicio1${i}" /> h  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
					<mod:hora titulo="Término" var="termino1${i}" /> h 
				</mod:grupo>
				<mod:grupo>
					<mod:hora titulo="Início" var="inicio2${i}" /> h  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
					<mod:hora titulo="Término" var="termino2${i}" /> h 						
				</mod:grupo>	
				<mod:grupo>
					<mod:texto titulo="Total de Horas Extras no dia" var="totHrs${i}" largura="10"/>
				</mod:grupo>									
			</c:forEach>
		</mod:grupo>
		<br/>
		<mod:grupo>
			<mod:texto titulo="Total de Horas Extraordinárias em Dias Úteis e Sábados" var="totHrsDiasUteis" largura="10" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Total de Horas Extraordinárias em Domingos e Feriados" var="totHrsDomingos" largura="10" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Total de Horas Extraordinárias" var="totHrsFinal" largura="10"/>
		</mod:grupo>
		<br>
		<b><mod:mensagem texto="Obs: O subscritor da ficha é o próprio servidor. Após a finalização do documento, 
		 o superior hierárquico e o titular da unidade <br> deverão ser cadastrados como cossignatários exatamente
		  nessa ordem." vermelho="não">
		</mod:mensagem></b>	
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
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr><br /><br /><br /></tr>
						<tr>
							<td align="center"><font style="font-family:Arial;font-size:11pt;font-weight:bold;">
							FICHA INDIVIDUAL DE FREQUENCIA DE SERVIÇO EXTRAORDINÁRIO</font></td>
						</tr>	
						<tr>
							<td align="center"><font style="font-family:Arial;font-size:11pt;">
							(Art.49 da Resolução n&ordm; 4/2008 - CJF)</font></td>
						</tr>											
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		
		&nbsp;<br/> <%-- Solução by Edson --%>
		<b>Unidade Organizacional:</b> ${requestScope['unidade_lotacaoSel.sigla']}-${requestScope['unidade_lotacaoSel.descricao']}<br/>
		<b>Mês/Ano:</b>  ${mes}/${ano} <br/>
		<b>Nome:</b>  ${requestScope['servidor_pessoaSel.descricao']}<br/>
		<b>Matrícula:</b>  ${requestScope['servidor_pessoaSel.sigla']} <br/> <br/>
		 

		<table width="100%" align="center" border="1" cellpadding="2"
			cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#999999" width="05%" align="center">Dia</td>
				<td bgcolor="#999999" width="15%" align="center">Dia da Semana</td>
				<td bgcolor="#999999" width="10%" align="center">Início</td>
				<td bgcolor="#999999" width="10%" align="center">Término</td>
				<td bgcolor="#999999" width="10%" align="center">Início</td>
				<td bgcolor="#999999" width="10%" align="center">Término</td>
				<td bgcolor="#999999" width="10%" align="center">Horas Extras</td>				
			
			</tr>
		</table>
		<c:forEach var="i" begin="1" end="${numDiasHoraExtra}">		    
    	    <table width="100%" align="center" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" width="05%" align="center">${requestScope[f:concat('diaMes',i)]}</td>
				<td bgcolor="#FFFFFF" width="15%" align="center">${requestScope[f:concat('diaSemana',i)]}</td>
			    <td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('inicio1',i)]} h</td>			    
				<td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('termino1',i)]} h</td>
				<td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('inicio2',i)]} h</td>			    
				<td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('termino2',i)]} h</td>
				<td bgcolor="#FFFFFF" width="10%" align="center">${requestScope[f:concat('totHrs',i)]} h</td>				
				 
			</tr>
		</table> 
		</c:forEach>
		<br/>
		<table width="100%" align="center" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" width="30%" align="left"><b>Total de Horas Extraordinárias em Dias Úteis e Sábados:</b> &nbsp; ${totHrsDiasUteis}</td>			
		    </tr>
		    <tr>
				<td bgcolor="#FFFFFF" width="30%" align="left"><b>Total de Horas Extraordinárias em Domingos e Feriados:</b> &nbsp; ${totHrsDomingos}</td>		
		    </tr>
		    <tr>
				<td bgcolor="#FFFFFF" width="30%" align="left"><b>Total de Horas Extraordinárias:</b> &nbsp; ${totHrsFinal}</td>		
		    </tr>		    
		</table>		
	    <br/> <br/> 
	    
	    <p style="text-align: center;">${doc.dtExtenso}</p> 
	    
		
	  
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		



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
