<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<mod:modelo>
	<mod:entrevista>		
		<mod:grupo>
			<mod:caixaverif titulo="Entrevista no Parlatório"
							var="parlatorio" reler="ajax" idAjax="parlatorioAjax" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;				
			<mod:caixaverif titulo="Entrega de Material a Réu Preso"
							var="material" reler="ajax" idAjax="materialAjax" />				
		</mod:grupo>
		<mod:grupo> 
			<mod:lotacao titulo="Autorização: Juiz/Juiza Federal da" var="vara" />
		</mod:grupo>	
		<mod:grupo>
			<mod:texto titulo="Nome do(a) Custodiado(a)" var="custodiado" largura="60"/>			
		</mod:grupo>
		<mod:grupo>
			<mod:data titulo="Data da Entrevista e/ou Entrega de Material" var="data"></mod:data> 
		</mod:grupo>
		<mod:grupo depende="parlatorioAjax">		
			<c:if test="${parlatorio == 'Sim'}">
			    <hr color="#FFFFFF" />				
				<mod:grupo titulo="Dados do campo Entrevista no Parlatório">
					<mod:grupo>					
						<mod:selecao var="numAdvogados" titulo="Número de advogados(as)" reler="ajax"
								idAjax="numAdvogadosAjax" opcoes="0;1;2;3;4;5;6" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<mod:selecao var="numParentes" titulo="Número de parentes" reler="ajax"
								idAjax="numParentesAjax" opcoes="0;1;2;3;4;5;6" />				
					</mod:grupo>	
					<mod:grupo depende="numAdvogadosAjax">
						<c:forEach var="i" begin="1" end="${numAdvogados}">
							<mod:grupo>
								<mod:texto titulo="Nome do(a) Advogado(a)" var="advogado${i}" largura="60"/>
							</mod:grupo>
						</c:forEach>	
					</mod:grupo>
					<mod:grupo depende="numParentesAjax">
						<c:forEach var="i" begin="1" end="${numParentes}">
							<mod:grupo>
								<mod:texto titulo="Nome do(a) Parente" var="parente${i}" largura="60"/>
							</mod:grupo>
						</c:forEach>	
					</mod:grupo>
				</mod:grupo>					
			</c:if>			
		</mod:grupo>
		<mod:grupo depende="materialAjax">		
			<c:if test="${material =='Sim'}">
				<hr color="#FFFFFF" />					
				<mod:grupo titulo="Dados do campo Material a ser Entregue">
					<mod:memo titulo="Relação do Material" var="descMat" colunas="63" />
				</mod:grupo>				
				<mod:grupo>				
					<mod:texto titulo="Nome do(a) Advogado(a)" var="advogadoMat" largura="60"/>
					<b><mod:mensagem texto="Obs: Caso haja Entrevista no Parlatório não preencher este campo"
					vermelho="Nao"></mod:mensagem>	
				</mod:grupo>							
			</c:if>			
		</mod:grupo>		
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
							<td align="right"><font style="font-family:Arial;font-size:11pt;font-weight:bold;">
							SOLICITAÇÃO N&ordm; ${doc.codigo}</font></td>
						</tr>	
						<tr>
							<td align="right"><font style="font-family:Arial;font-size:11pt;font-weight:bold;">
							${doc.dtExtenso}</font></td>
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
		
		<p style="text-align: center;font: bold;"> <b>AUTORIZAÇÃO PARA&nbsp; </b> 	
			<ww:if test="${parlatorio == 'Sim'}">
					<b>ENTREVISTA NO PARLATÓRIO&nbsp;</b>
					<c:if test="${material == 'Sim'}">
						<b>E ENTREGA DE MATERIAL A RÉU PRESO</b>
					</c:if>
			</ww:if>
			<ww:else>			
				<c:if test="${material == 'Sim'}">
					<b>ENTREGA DE MATERIAL A RÉU PRESO</b>					
				</c:if>				 
			</ww:else>			
			
		</p>
		<mod:letra tamanho="9pt">
		<br/>		
		<table  border="1" width="100%" align="left">
			<tr bgcolor="#999999">
				<td><b>AUTORIZAÇÃO PELO JUÍZO COMPETENTE</b> <br /></td>
			</tr>
			<tr>
				<td ><br/>
				<p style="text-align: justify; text-indent: 2cm;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				De ordem do(a) MM. Juíz/Juíza Federal da ${f:lotacao(requestScope['vara_lotacaoSel.id']).descricao}, fica autorizada, em ${data}, a				  					
				<ww:if test="${parlatorio == 'Sim'}">
					Entrevista no Parlatório 
					<ww:if test="${material == 'Sim'}">
						e a Entrega de Material ao(à)
					</ww:if>
					<ww:else>
						do 
					</ww:else>										
				</ww:if>					
				<ww:else>
					<c:if test="${material == 'Sim'}">
							Entrega de Material ao(à) 
					</c:if>								
				</ww:else>
				custodiado(a) abaixo identificado(a)
				<ww:if test="${parlatorio == 'Sim'}">
					, que deverá ser conduzido(a) por Agente 
					da escolta e acompanhado(a) por um Agente de Segurança Judiciária da SJRJ.
				</ww:if>
				<ww:else>
					.
				</ww:else>
				</p>
				<br/> <br/>				
				</td>
			</tr>
			<tr>
				<td><br/>
					Nome do(a) Custodiado(a):  <b>${custodiado}</b>
					<br/><br/>
				</td>
			</tr>	
			<c:if test="${parlatorio != 'Sim' and  material == 'Sim'}">
			<tr>
				<td><br/>
					Nome do(a) Advogado(a):  <b> ${advogadoMat}</b>
					<br/><br/>
				</td>
			</tr>
			</c:if>	
			<tr bgcolor="#999999">			
				<td bgcolor="#999999">
					<ww:if test="${parlatorio == 'Sim'}">
						<b>ENTREVISTA NO PARLATÓRIO</b>
						<tr>
							<td><br>
								<c:forEach var="i" begin="1" end="${numAdvogados}">
									Nome do(a) Advogado(a): ${requestScope[f:concat('advogado',i)]} <br/>
								</c:forEach>
								<c:forEach var="i" begin="1" end="${numParentes}">
									Nome do(a) Parente: ${requestScope[f:concat('parente',i)]} <br/>
								</c:forEach>
								<br/>
							</td>
						</tr>
						<c:if test="${material == 'Sim'}">
							<tr>
								<td>
									<b>MATERIAL A SER ENTREGUE</b> </td>
							</tr>	
							<tr>
								<td><br/>Relação do material: <br/>
									${descMat}<br/><br/></td>
								
							</tr>
						</c:if>						
					</ww:if>								
					<ww:else>
						<c:if test="${material == 'Sim'}">
							<b>MATERIAL A SER ENTREGUE</b>
							<tr>
								<td><br/>Relação do material: <br/>
									${descMat}<br/><br/></td>
								
							</tr>
						</c:if>								
					</ww:else>
				</td>
			</tr>
			<tr>
				<td>			
				<table border="0" cellspacing="0" width="100%">
						<tr>
							<td width="40%">
							<br/>
								Data: ____/_____/_______ <br/> <br/>
					        </td>
							<td width="60%" align="right" valign="bottom">
							<br/>
								________________________________________________ 	
								Assinatura e matrícula do Diretor de Secretaria&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <br/> <br/>							
							</td>
						</tr>					
					</table>
				</td>
			</tr>

		</table>
		<br/>
		<br/>
		
		<c:if test="${material == 'Sim'}">
		<table border="1" width="100%" align="left">
			<tr bgcolor="#999999">
				<td><b>VISTORIA DO MATERIAL PELO NSEG</b> <br /></td>
			</tr>
			<tr> 
				<td>
					<table border="0" cellspacing="0" width="100%">
						<tr>
							<td width="5%" align="right" valign="bottom"> <br/>
								<img src="contextpath/imagens/quadrado.gif">
							</td>
							<td width="95%" align="left" valign="top"> <br/>
							Material vistoriado e entregue ao réu preso, na carceragem, por Agente de Segurança Judiciária da SJRJ.
							</td>
						</tr>
						<tr>
							<td width="5%" align="right" valign="top"> <br/>
								<img src="contextpath/imagens/quadrado.gif">
							</td>
							<td width="95%" align="left" valign="top"> <br/>
							Material vistoriado e retido. Descrição e justificativa: ___________________________________________
							_____________________________________________________________________________________
							_____________________________________________________________________________________	
							_____________________________________________________________________________________
							<br/> <br/>				
							</td>														
						</tr>
					</table>
				</td>	
			</tr>
			<tr>
				<td>
					<table border="0" cellspacing="0" width="100%">
						<tr>
							<td width="40%">
							<br/>
								Data: ____/_____/_______ <br/> <br/>
					        </td>
							<td width="60%" align="right">
							<br/>
								_____________________________________________________ 	
								Assinatura e matrícula do Agente de Segurança Judiciária&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <br/> <br/>							
							</td>
						</tr>	
						<tr>
							<td align="right" valign="top">						
							ddd
						</tr>						
					</table>
				</td>
			</tr>			
		</table>
		</c:if>
		</mod:letra>	
			
		<br/> <br/>	<br/>				
		
		
		
		
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
