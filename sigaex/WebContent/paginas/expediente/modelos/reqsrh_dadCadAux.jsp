<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
	<br>
	    <br>
		<span style="color:red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À DIMED</b></span>
		<br/><br/>
		<mod:grupo titulo="DADOS DO MAGISTRADO/SERVIDOR BENEFICIÁRIO TITULAR">
			<mod:grupo>
				
				<mod:texto titulo="Ramal" var="ramal" largura="15"/>
		
				<mod:texto titulo="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Plano de Saúde a que está vinculado" var ="plano"/>
				
			</mod:grupo>
		</mod:grupo>
		<br>
		<hr style="color: #FFFFFF;" />
		<mod:selecao var="contdep"
				titulo="Quantidade de Dependentes"
				opcoes="1;2;3;4;5"
				reler="sim"  /><br/>
		<mod:grupo depende="contDependAjax">
				<c:forEach var="i" begin="1" end="${contdep}">
		           <mod:grupo titulo="Dependente nº ${i}">
			           <mod:grupo>
				           <mod:texto titulo="Nome" var="nom${i}" largura="60"/>
		               </mod:grupo>
			           <mod:texto titulo="Parentesco" var="paren${i}" largura="20"/>
				
			           <mod:grupo>
				           <mod:data titulo="Data de Nascimento" var="dtNasc${i}"/>
			               <mod:selecao titulo="Estado Civil" var="estado${i}" opcoes="Solteiro;Solteira;Casado;Casada;Viúvo;Viúva;Outros" reler="não" />
			           </mod:grupo>
			           <mod:grupo>
				           <mod:texto titulo="Nº Identidade" var="iden${i}" largura="20" obrigatorio="Nao"/>
				           <mod:texto titulo="Orgão Expedidor" var ="OrgExp${i}" obrigatorio="Nao"/>
				           <mod:data titulo="Data de Expedição" var="dtExp${i}" obrigatorio="Nao"/>
			           </mod:grupo>
			       </mod:grupo> 
			       <hr style="color: #FFFFFF;" />
				</c:forEach>    
		</mod:grupo>								
			
	        <br>	
	        
	        <hr style="color: #FFFFFF;" />
		<mod:selecao var="contdoc"
				titulo="Quantidade de Documentos Comprobatórios"
				opcoes="1;2;3;4;5"
				reler="sim"  /><br/>
		<mod:grupo depende="contDependAjax">
				<c:forEach var="i" begin="1" end="${contdoc}">
		              <mod:grupo>
				          <mod:texto titulo="Documento nº ${i}" var="dcmt${i}" largura="60"/>
		               </mod:grupo>
	            </c:forEach>    
	    </mod:grupo>        
	        
			<mod:grupo>			
			<mod:caixaverif titulo="Declaro estar ciente dos termos do capitulo IV da resoluçao nª 2 de 2008 do Conselho da Justiça
					Federal, que regulamenta a assistência à saude prevista no art. 2320 da lei nº &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.112, de 1990, 
					com a redação dada pela lei 11.032, de 2006, e de que  o respectivo auxilio será pago mediante
					reembolso.<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Declaro, ainda que os beneficiários acima relacionados não recebem  auxílio semelhante, nem participam de outro 
					programa de assistência saúde, custeado pelos cofres públicos, &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ainda que em partes.<br><br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Declaro, por fim, estar ciente de que a documentação sobre a operadora/contrato do contrato do plano de saude
					será apresentada após a regulamentação da matéria pelo TRF - &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2ª Região." var="decl" marcado="Sim" obrigatorio="Sim" reler="Nao"/>
			</mod:grupo>
			<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<mod:obrigatorios />
			<br><br>
			
			<mod:grupo titulo="Resolução 02/2008/CJF:<br/><br>
					Art. 42. Só fará jus ao ressarcimento o
					beneficiário que não receber auxílio semelhante e nem participar de outro 
					programa de assistência à saúde de servidor, custeado pelos cofres públicos, 
					ainda que em parte.<br/><br>
					Art. 43. São beneficiários do auxílio:<br/>
					I - na qualidade de titulares: <br/>
 					a)     magistrados e servidores ativos e inativos, incluídos os cedidos e ocupantes apenas de cargo comissionado no Conselho e órgãos da Justiça Federal de primeiro e segundo graus;
					b)     pensionistas estatutários.<br/>
					II - na qualidade de dependente do titular:<br/>
					a)     o cônjuge, o companheiro ou companheira de união estável;<br/>
					b)     a pessoa desquitada, separada judicialmente ou divorciada, que perceba pensão alimentícia;<br/>
					c)     os filhos e enteados, solteiros, até 21 (vinte e um) anos de idade ou, se inválidos, enquanto durar a invalidez;<br/>
					d)     os filhos e enteados, entre 21 (vinte e um) e 24 (vinte e quatro) anos de idade, dependentes econômicos do magistrado ou servidor e estudantes de curso regular reconhecido pelo Ministério da  Educação;<br/>
					e)     o menor sob guarda ou tutela concedida por decisão judicial.<br/><br>
					Art. 45. São documentos indispensáveis para inscrição:<br>
					I - cópia autenticada do contrato celebrado entre o beneficiário titular e a operadora de planos de saúde ou o original seguido de cópia, a ser conferida pelo servidor responsável;<br/>
					II - comprovante de que a operadora de planos de saúde contratada pelo servidor está regular e autorizada pela Agência Nacional de Saúde (ANS);<br>
					III - declaração para fins de cumprimento do art. 42 desta Resolução;<br>
					IV - documentos oficiais que comprovem a situação de  dependência, (...)"/>
			
		 	
		<br>	
	</mod:entrevista>
	<mod:documento>
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
			<style type="text/css">
			@page {
				margin-left: 1cm;
				margin-right: 0.5cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
			</style>
		</head>
		<body>
		<c:set var="pessoa" value ="${f:pessoa(requestScope['pessoa_pessoaSel.id'])}" />
		<c:set var="vinc" value="vinculado"/>
		<c:if test="${doc.subscritor.sexo == 'F'}">
			<c:set var="vinc" value="vinculada"></c:set>
		</c:if>
				<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr> 
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;"><br/>
						DADOS CADASTRAIS PARA O AUX&Iacute;LIO-SA&Uacute;DE<br/></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
<br/>
		<table border="1" cellpadding="5" width="96%"style="font-size:19" >
			<tr>
    			<th colspan="2" align="left" style="font-size:10"><b>1-DADOS DO MAGISTRADOS/SERVIDOR BENEFICIÁRIO(TITULAR)</b></th>
  			</tr>
  			<tr >
  				<td>
  				<p style="font-family:Arial;font-size:9pt">
  				Nome: ${doc.subscritor.descricao}&nbsp;_________&nbsp; Matricula: ${doc.subscritor.matricula}<br/>
				Cargo: ${doc.subscritor.cargo.descricao} &nbsp;_________&nbsp;Quadro de Pessoal:&nbsp;TRF<br/>
				Lotação: ${doc.subscritor.lotacao.descricao}  &nbsp;_________&nbsp;Ramal: ${ramal}<br/>
				Plano de Sa&uacute;de a que est&aacute; ${vinc}: ${plano}
				</p>
				</td>
  			</tr>
  		</table>
  		<br/>
		<table border="1" cellpadding="5" width="96%"style="font-size:10" >
	  		<tr>
  				<td colspan="2" align="left" style="font-size:10"><b>2-DADOS DOS DEPENDENTES</b></td>
  			</tr>
  			<c:forEach var="i" begin="1" end="${contdep}">
  			   <c:set var="nomm" value="${requestScope[(f:concat('nom',i))]}" />
	 		   <c:set var="parenn" value="${requestScope[(f:concat('paren',i))]}" />
	 		   <c:set var="estd" value="${requestScope[(f:concat('estado',i))]}" />
	 	       <c:set var="dtNascc" value="${requestScope[(f:concat('dtNasc',i))]}" />
	 	       <c:set var="idenn" value="${requestScope[(f:concat('iden',i))]}" />
	 		   <c:set var="OrgExpp" value="${requestScope[(f:concat('OrgExp',i))]}" />
	 		   <c:set var="dtExpp" value="${requestScope[(f:concat('dtExp',i))]}" />
	 		  
  			<tr>
  				<td>
  				    <p style="font-family:Arial;font-size:9pt">
  					Nome: ${nomm}  <br> Parentesco: ${parenn} &nbsp;_____&nbsp;
  					Estado Civil: ${estd} &nbsp;_____&nbsp;
  					Data de Nascimento: ${dtNascc} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <br>
  					Nº Identidade: ${idenn} &nbsp;_____&nbsp; 
  					&Oacute;rg&atilde;o Expedidor: ${OrgExpp} &nbsp;_____&nbsp;
  					Data de Expedi&ccedil;&atilde;o: ${dtExpp}<br/>
  					</p>
  				</td>
  			</tr>
  			</c:forEach>
		</table>
		
		<br/>
		
		<table border="1" cellpadding="5" width="96%"style="font-size:10">
			<tr>
				<th colspan="2" align="left" style="font-size:9"><b>3-DECLARA&Ccedil;&Atilde;O </b></th>
			</tr>
			<tr >
				<td >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Declaro estar ciente dos termos do capitulo IV da resolução nª 2 de 2008 do Conselho da Justiça
					Federal, que regulamenta a assistência à saude prevista no art. 2320 da lei nº 8.112, de 1990, 
					com a redação dada pela lei 11.032, de 2006, e de que  o respectivo auxílio será pago mediante
					reembolso.<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Declaro, ainda que os beneficiados acima relacionados não recebem  auxílio semelhante, nem participam de outro 
					programa de assistência saúde, custeado pelos cofres públicos, ainda que em partes.<br/>
					<b>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Declaro, por fim, estar ciente de que a documentação sobre a operadora/contrato do contrato do plano de saúde
					será apresentada após a regulamentação da matéria pelo TRF - 2ª Região.
					</b> 
				</td>
			</tr>
		</table>
		<br>
		<table border="1" cellpadding="5" width="96%"style="font-size:9">
			<tr>
				<th colspan="2" align="left" style="font-size:10"><b>4- ANEXOS </b></th>
			</tr>
			<tr >
				<td >
					Em anexo seguem os documentos que comprovam a situação de dependência: ${rel}<br/>
					<c:if test="${contdoc > '0'}">
		        	   <c:forEach var="i" begin="1" end="${contdoc}">
				          <c:set var="docs" value="${requestScope[(f:concat('dcmt',i))]}" />
				          ${i})- <u>${docs}</u><br>		    
			           </c:forEach>
		            </c:if>
				</td>
			</tr>
			<tr align="center">
				<td>${doc.dtExtenso}<br><br><br><c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" /></td>
			</tr>
		</table>
		
		<br><br><br>
		<table border="1" cellpadding="5" width="96%"style="font-size:9">
			<tr>
				<th colspan="2" align="left" style="font-size:9"><b>5- OBSERVAÇÕES </b></th>
			</tr>
			<tr >
				<td >
					Res. 02/2008/CJF:<br/> 
					"Art. 42. Só fará jus ao ressarcimento o
					beneficiário que não receber auxílio semelhante e nem participar de outro 
					programa de assistência à saúde de servidor, custeado pelos cofres públicos, 
					ainda que em parte.<br/>
					Art. 43. São beneficiários do auxílio:<br/>
					I - na qualidade de titulares: <br/>
 					a)     magistrados e servidores ativos e inativos, incluídos os cedidos e ocupantes apenas de cargo comissionado no Conselho e órgãos da Justiça Federal de primeiro e segundo graus;
					b)     pensionistas estatutários.<br/>
					II - na qualidade de dependente do titular:<br/>
					a)     o cônjuge, o companheiro ou companheira de união estável;<br/>
					b)     a pessoa desquitada, separada judicialmente ou divorciada, que perceba pensão alimentícia;<br/>
					c)     os filhos e enteados, solteiros, até 21 (vinte e um) anos de idade ou, se inválidos, enquanto durar a invalidez;<br/>
					d)     os filhos e enteados, entre 21 (vinte e um) e 24 (vinte e quatro) anos de idade, dependentes econômicos do magistrado ou servidor e estudantes de curso regular reconhecido pelo Ministério da  Educação;<br/>
					e)     o menor sob guarda ou tutela concedida por decisão judicial.<br/>
					Art. 45. São documentos indispensáveis para inscrição:
					I - cópia autenticada do contrato celebrado entre o beneficiário titular e a operadora de planos de saúde ou o original seguido de cópia, a ser conferida pelo servidor responsável;<br/>
					II - comprovante de que a operadora de planos de saúde contratada pelo servidor está regular e autorizada pela Agência Nacional de Saúde (ANS);
					III - declaração para fins de cumprimento do art. 42 desta Resolução;
					IV - documentos oficiais que comprovem a situação de  dependência, (...)"
				</td>
			</tr>
		</table>

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