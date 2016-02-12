<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="DADOS DOS MAGISTRADOS/SERVIDORES BENEFICIÁRIO">
			<mod:grupo>
				<mod:texto titulo="Quadro de pessoal" var="quadro" />
				<mod:texto titulo="Ramal" var="ramal" largura="15"/>
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Plano de Saúde a que está vinculado" var ="plano"/>
			</mod:grupo>
		</mod:grupo>
		
		
		<mod:grupo titulo="DADOS DOS DEPENDENTES(S)">
			<mod:grupo>
				<mod:texto titulo="Nome" var="dep" largura="50"/>
			</mod:grupo>
			<mod:grupo>
			<mod:texto titulo="Parentesco" var="paren" largura="20"/>
				
			</mod:grupo>
			<mod:grupo>
				<mod:data titulo="data de nascimento" var="dtNasc"/>
				<mod:selecao titulo="Estado Civil" var="estado" opcoes="solteiro(a);casado(a);viúvo(a);outros" reler="não" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Nº Identidade" var="iden" largura="20"/>
				<mod:texto titulo="Orgão Expedidor" var ="OrgExp"/>
				<mod:data titulo="Data de Expedição" var="dtExp"/>
			</mod:grupo>
			<mod:grupo>
				<mod:data titulo="data" var="dt"/>
				<mod:texto titulo="relaçao de documentos de dependencia" var="rel"/>
			</mod:grupo>
		</mod:grupo>
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
				<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPaginaTRF.jsp" />
		</td></tr>
		
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr> 
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;"><br/><br/>
						DADOS CADASTRAIS PARA O AUX&Iacute;LIO-SA&Uacute;DE<br /><br /></td>
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
		<table border="1" cellpadding="5" width="96%"style="font-size:9" >
			<tr>
    			<th colspan="2" align="left" style="font-size:12"><b>1-DADOS DO MAGISTRADOS/SERVIDOR BENEFICIÁRIO(TITULAR)</b></th>
  			</tr>
  			<tr >
  				<td>
  				Nome: ${doc.subscritor.descricao}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; matricula:${doc.subscritor.matricula}<br/>
				Cargo: ${doc.subscritor.cargo.descricao} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Quadro de pessoal:${quadro}<br/>
				Lotação: ${doc.subscritor.lotacao.descricao}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ramal:${ramal}<br/>
				Plano de Sa&uacute;de a que est&aacute; vinculado: ${plano}
				
  				</td>
  			</tr>
  		</table>
  		<br/>
		<table border="1" cellpadding="5" width="96%"style="font-size:9" >
	  		<tr>
  				<td colspan="2" align="left" style="font-size:12"><b>2-DADOS DOS DEPENDENTE(S)</b></td>
  			</tr>
  			<tr>
  				<td>
  					Nome: ${dep}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Parentesco: ${paren} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  					Estado Civil: ${estado} <br/>
  					Data de nascimento: ${dtNasc} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  					Nº Identidade:${ iden} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  					&Oacute;rg&atilde;o Expedidor: ${OrgExp} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  					Data de expedi&ccedil;&atilde;o: ${dtExp }<br/>
  				</td>
  			</tr>
		</table>
		
		<br/>
		
		<table border="1" cellpadding="5" width="96%"style="font-size:9">
			<tr>
				<th colspan="2" align="left" style="font-size:12"><b>3-DECLARA&Ccedil;&Atilde;O </b></th>
			</tr>
			<tr >
				<td >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Declaro estar ciente dos termos do capitulo IV da resoluçao nª 2 de 2008 do Conselho da Justiça
					Federal, que regulamenta a assistencia à saude prevista no art. 2320 da lei nº 8.112, de 1990, 
					com a redação dada pela lei 11.032, de 2006, e de que  o repesctivo auxilio sera pago mediante
					reembolso.<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Declaro, ainda que os beneficios acima relacionados não recebem  auxilio semelhante, nem participar de outro 
					progrmaa de assisaitencia saude, custeado pelos cofres publicos, ainda que eme partes.<br/>
					<b>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Declaro, por fim, estar ciente de que a documentoção sobre a operadora/contrato do contrato do plano de saude
					sera apresentada apos a regulamentação da matéria pelo TRF - 2ª Região.
					</b> 
				</td>
			</tr>
		</table>
		<br>
		<table border="1" cellpadding="5" width="96%"style="font-size:9">
			<tr>
				<th colspan="2" align="left" style="font-size:12"><b>4- ANEXOS </b></th>
			</tr>
			<tr >
				<td >
					Em anexo seguem os documentos que comprovam a situação de dependencia: ${rel}<br/>
				</td>
			</tr>
			<tr align="right">
				<td>Rio de Janeiro: ${dt}, &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" /></td>
			</tr>
		</table>
		
		<br/>
		<table border="1" cellpadding="5" width="96%"style="font-size:9">
			<tr>
				<th colspan="2" align="left" style="font-size:12"><b>5- OBSERVAÇÕES </b></th>
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