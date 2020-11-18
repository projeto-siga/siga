<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<style type="text/css"> </style>
<script type="text/javascript">
function printpage()
{
   window.print();
}
</script>

<div class="container-fluid">
	<a style="text-decoration: none" href="javascript:printpage();">
	Imprimir</a>	
	<div style="width: 45%; float: right;">
	Atendimento:
	<script language="javascript">
	var date = new Date();
	var d  = date.getDate();
	var day = (d < 10) ? '0' + d : d;
	var m = date.getMonth() + 1;
	var month = (m < 10) ? '0' + m : m;
	var yy = date.getYear();
	var year = (yy < 1000) ? yy + 1900 : yy;
	var hours = date.getHours()
	var minutes = date.getMinutes()
	var seconds = date.getSeconds()
	if (minutes < 10){
	minutes = "0" + minutes
	}
	if (seconds < 10){
		seconds = "0" + seconds
	}
	document.write(day + "/" + month + "/" + year + " " + hours + ":" + minutes + ":" + seconds);
    </script>
	<p>
	<!-- Previs&atilde;o: -->
	</div>
	<div style="clear:both;"></div>
	<br/><br/>
	<h2 style="font-size: 161.6%; font-weight: bold; color: #0E2029; text-align:center;">TERMO DE ATENDIMENTO</h2>
		<div style="width: 45%; float: left;">
		<p>
			<font style="font-size: 11pt; font-weight: bold; color: #0E2029; padding:0px;">
			${solicitacao.codigo}
			</font>
		</p>
		<p>
			<font style="font-size: 11pt; font-weight: bold; text-align:left; ">
			Solicitante:&nbsp;
			</font>
			<font style="font-size: 11pt;">
			${solicitacao.solicitante.descricaoIniciaisMaiusculas}
			</font>
			<p>
			<font style="font-size: 11pt; font-weight: bold; text-align:left; ">
			Lota&ccedil;&atilde;o:&nbsp;
			</font>
			${solicitacao.lotaSolicitante.siglaLotacao} 
				<c:if test="${not empty solicitacao.local}">
				(${solicitacao.local.nomeComplexo})
				</c:if>
			</p>
			
			
		<c:if test="${not empty solicitacao.telPrincipal}">
			<p>
				<font style="font-size: 11pt; font-weight: bold; text-align: left;">
					Telefone:&nbsp; </font> <font style="font-size: 11pt;">
					${solicitacao.telPrincipal} 
				</font>
			</p>
		</c:if>
		<c:if test="${not empty solicitacao.endereco}">
			<p>
				<font style="font-size: 11pt; font-weight: bold; text-align: left;">
					Endereco&nbsp; </font> <font style="font-size: 11pt;">
					${solicitacao.endereco} 
				</font>
			</p>
		</c:if>
		</font>
			</p>
			<p>
			<font style="font-size: 11pt; font-weight: bold; text-align:left; ">
			Atendente:&nbsp;
			</font>
			<font style="font-size: 11pt;">
			${solicitacao.lotaAtendente.siglaLotacao} 
			</font>
			</p>
		</div>
		
		<div style="clear:both;text-align:left;">
		<p>
			<font style="font-size: 11pt; font-weight: bold; text-align:left; ">
			Item de Configura&ccedil;&atilde;o:&nbsp;
			</font>
			<font style="font-size: 11pt;">
			${solicitacao.itemAtual.siglaItemConfiguracao} - ${solicitacao.itemAtual.descricao}
			</font>
			</p>
			<p>
			<b>A&ccedil;&atilde;o:</b>
			${solicitacao.acaoAtual.siglaAcao} - ${solicitacao.acaoAtual.descricao}
			</p>
		</div>
		
		<div style="clear:both;">
			<h3 style="font-size: 161.6%; font-weight: bold; color: #0E2029; text-align:center;">DADOS DA SOLICITA&Ccedil;&Atilde;O</h3>

			<div style="clear:both;text-align:left;">
			<c:forEach items="${atributos}" var="att">
				<p>
					<font style="font-size: 11pt; font-weight: bold; text-align:left; ">${att.atributo.nomeAtributo}: &nbsp;</font>
					<font style="font-size: 11pt;">${att.valorAtributoSolicitacao}</font>
				</p>
			</c:forEach>
			</div>
			
			<p> 
			<font style="font-size: 11pt; font-weight: bold; text-align:left; ">
			DESCRI&Ccedil;&Atilde;O:
			</font>
			<font style="font-size: 11pt;">
			${solicitacao.descrSolicitacao}
			</font>
		</p>
		</div>
		
		<div style="width:100%;">
		<p> 
			<font style="font-size: 11pt; font-weight: bold; text-align:left; ">
			ANDAMENTO/SOLU&Ccedil;&Atilde;O:&nbsp;
			</font>
			<p style="text-align: left;">
			________________________________________________________________________________________________________________
			<br/><br/>
			________________________________________________________________________________________________________________
			<br/><br/>
			________________________________________________________________________________________________________________
			
			</p>
		</p>
		</div>		
		<p></p>
		<div style="font-size: 11pt; font-weight: bold; text-align:center; ">
		ACEITA&Ccedil;&Atilde;O DO USU&Aacute;RIO
		<p></p>
		<font style="font-weight: normal;">
		DECLARO ESTAR CIENTE E DE ACORDO COM AS CONDI&Ccedil;&Otilde;ES DE ATENDIMENTO E QUE OS SERVI&Ccedil;OS DA PRESENTE SOLICITA&Ccedil;&Atilde;O FORAM 
		EFETIVAMENTE TESTADOS TENDO SIDO DADOS COMO ACEITOS POR MIM NESTA DATA. 
		</font>
		</div>
		
		<div style="text-align:center;">
			<p> 
			<font style="font-size: 11pt; font-weight: bold; text-align:center; ">
			&nbsp;
			Data:&nbsp;____&nbsp;/&nbsp;____&nbsp;/&nbsp;________&nbsp;&nbsp;&nbsp;____&nbsp;:&nbsp;____<br/><br/><br/>
			Assinatura:&nbsp;_______________________________________________________ 
			</font>
			</p>
		</div>
		<p></p>
		<br/><br/>
		<div style="font-size: 11pt; font-weight: bold; text-align:center; ">
			<div>
			<p style="font-weight: normal; text-align: left;">
		 	NOME DO T&Eacute;CNICO:
		 	</p>
		 	</div>
		 	<br/><br/>
			<div style="width: 45%; float: left; font-weight: normal; text-align:left;">
		   	VISTO DO T&Eacute;CNICO:
		   	</div>				
		</div>
		
</div>
</html>


	