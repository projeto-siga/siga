<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%> 
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>		
		<mod:grupo>
			<mod:pessoa titulo="Titular do Benefício" var="titular" />
		</mod:grupo>
		<mod:grupo>
		<mod:selecao titulo="Mês" var="mes" 
			opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro"/>
		&nbsp;<b><mod:mensagem
				texto="O envio dos recibos (do titular e/ou dependentes) deverá ser feito, necessariamente,
				no mesmo formulário."
				vermelho="Nao"></mod:mensagem> </b>						
		</mod:grupo>	
		<mod:grupo>
			<mod:selecao titulo="Encaminhar recibo(s) de dependentes" 	var="dependentes"
			  opcoes="Não;Sim"
			  reler="ajax" idAjax="dependentesAjax" />		
		</mod:grupo>
		<mod:grupo depende="dependentesAjax">			
			<c:if test="${dependentes == 'Sim'}">
				<b><mod:mensagem
						texto="Confira se o comprovante de pagamento contém os valores discriminados por dependente."
						vermelho="Nao"></mod:mensagem> </b>
				<mod:grupo>		
					<mod:selecao titulo="Número de dependentes"  var="numDependentes"
			  			opcoes="1;2;3;4;5;6;7;8;9;10"  reler="ajax" idAjax="numDepAjax" />
			  	</mod:grupo>	
			  	<mod:grupo depende="numDepAjax">	
			  		<c:forEach var="i" begin="1" end="${numDependentes}">
			  			<mod:grupo>
			  				<mod:texto titulo="Nome do Dependente" var="nomeDep${i}" maxcaracteres="50" largura="50"/>
			  			</mod:grupo>
			  		</c:forEach>
			  	</mod:grupo>			
			</c:if>			
		</mod:grupo>
		<b><mod:mensagem
				texto="Obs: Antes de finalizar o documento digitar o seguinte texto no campo Descrição: 
				Auxílio Saúde + (mês escolhido) + (nome e matrícula do titular do benefício)"
				vermelho="Nao"></mod:mensagem> </b>	
		
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
		
		<table width="100%"  border="0" bgcolor="#FFFFFF">
	          <tr bgcolor="#FFFFFF">
		         <td align="left" valign="bottom" width="15%"><img src="contextpath/imagens/brasao2.png" width="65" height="65" /></td>
		         <td align="left" width="1%"></td>
		         <td width="37%">
		            <table align="left" width="100%">
			          <tr>
				          <td width="100%" align="left">
				          <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${f:resource('modelos.cabecalho.titulo')}</p>
				          </td>
			          </tr>
			          <c:if test="${not empty f:resource('modelos.cabecalho.subtitulo')}">
				          <tr>
					          <td width="100%" align="left">
					          <p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${f:resource('modelos.cabecalho.subtitulo')}</p>
					          </td>
				          </tr>
			          </c:if>
			          <tr>
			             <td width="100%" align="left">
				         <p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
				         <c:choose>
					     <c:when test="${empty mov}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when>
					     <c:otherwise>${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:otherwise>
				         </c:choose></p>
				         </td>
			          </tr>
		            </table>
		         </td>		         		           
		         <td width="57%" align="center">		         		  
		         	<p align="right" style=" font: bold; font-size: 10pt">		         	
		          	<b>ENCAMINHAMENTO MENSAL DE RECIBO PARA <br> CRÉDITO DO AUXÍLIO-SAÚDE</b></p>		          		          	 	
		         </td>         		
	          </tr>
           </table>
		

		
		&nbsp;<br/> <%-- Solução by Edson --%>
		<br/><br/>
		<b>Titular do Benefício:</b>  ${requestScope['titular_pessoaSel.descricao']}<br/>
		<b>Matrícula:</b>  ${f:pessoa(requestScope['titular_pessoaSel.id']).matricula} <br/>
		<b>Mês de competência:</b>&nbsp;${mes} 
				
		<c:if test="${dependentes == 'Sim'}">
			<table width="80%"  border="0" cellspacing="0" align="left">				
					<c:forEach var="i" begin="1" end="${numDependentes}">
						<tr>
							<c:choose>									
								<c:when test="${i == 1}">
									<td width="17"><b>Dependentes:</b></td>
									<td width="63" align="left"> ${requestScope[f:concat('nomeDep',i)]}</td>
								</c:when>
								<c:otherwise>										
									<td width="17"></td>
									<td width="63" align="left"> ${requestScope[f:concat('nomeDep',i)]}</td>
								</c:otherwise>	
							</c:choose>							
						</tr>				
					</c:forEach>				
			</table>			
		</c:if>		
		<br/><br><br/><br>
		Encaminho, em anexo, o(s) recibo(s) de pagamento referente(s) ao plano de saúde 
		com o objetivo de assegurar a regular percepção do benefício Auxílio-Saúde para o Titular e/ou 
		para o(s) dependente(s).
		<br><br/><br><br/><br>
		
		
		Atenciosamente,    
		
	  
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
