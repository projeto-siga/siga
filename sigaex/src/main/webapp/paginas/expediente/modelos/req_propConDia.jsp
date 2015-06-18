<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
		 
		    <p align="left"><span style="color: red"> <br><b>
			ATENÇÃO: EM CASO DE DÚVIDA NO PREENCHIMENTO, FAVOR OBTER AUXÍLIO NO RAMAL 8559 - SEPADI  (FALAR COM A SERVIDORA ROSA)
			<br><br>
			(os campos marcados com * são de preenchimento obrigatório)</b></span></p>

		</mod:grupo>
		<!-- 
		<mod:grupo>
			<mod:radio titulo="INICIAL" var="periodo" valor="1" reler="sim"
				marcado="Sim" />
			<mod:radio titulo="PRORROGAÇÃO" var="periodo" valor="2" reler="sim" />
		</mod:grupo>
         -->
		<hr style="color: #FFFFFF;" />
		
          <mod:grupo titulo="Proposto pertencente ao quadro do Tribunal Regional Federal da 2ª Região (*)">

			<mod:radio titulo="SIM" var="ppq" valor="1" reler="sim"/>
			<mod:radio titulo="NÃO" var="ppq" valor="2" reler="sim"/>
		</mod:grupo>
		<c:set var="valorppq" value="${ppq}" />
	        <c:if test="${empty valorppq}">
	           <c:set var="valorppq" value="${param['ppq']}" />
	        </c:if>
		
	   
		<c:if test="${valorppq == '1'}">
		<mod:grupo titulo="Proposto (*)">
		    <mod:pessoa titulo="Nome" var="prop" />
			<br>
			<br>

			<mod:texto titulo="C/C n°" var="contac" />
			<mod:texto titulo="Banco" var="banc" />
			<mod:texto titulo="Agência" var="agen" />
			<br>
			<!--
			<mod:texto titulo="Valor Mensal do Auxílio-Alimentação" var="valref" />
			<mod:texto titulo="Valor Mensal do Auxílio-Transporte" var="valtrans" />
		    -->
		</mod:grupo>
		</c:if>
		<c:if test="${valorppq == '2'}">
		<mod:grupo titulo="Proposto (*)">
		<mod:texto titulo="Nome" var="prop" largura="50"/>	
		<mod:texto titulo="CPF (somente algarismos)" var="cpf" />
		<br>
		<mod:texto titulo="Cargo" var="carg" largura="45"/>
			<br>

			<mod:texto titulo="C/C n°" var="contac" />
			<mod:texto titulo="Banco" var="banc" />
			<mod:texto titulo="Agência" var="agen" />
			<br>
			<mod:grupo>
			    <p align="left"><span style="color: red"> <br>Atenção : use ponto ao invés de vírgula nos valores abaixo, para que não ocorra erro</span></p>
            </mod:grupo>
			<mod:texto titulo="Valor Mensal do Auxílio-Alimentação R$" var="valref" />
			<mod:texto titulo="Valor Mensal do Auxílio-Transporte R$" var="valtrans" />
		</mod:grupo>
		 </c:if>
	
	   
		<hr style="color: #FFFFFF;" />
		<mod:grupo>
			<mod:memo titulo="<b>Serviço a ser executado e Local (*)</b>" var="texto_serv" colunas="70"
				linhas="3" />
		</mod:grupo>
		<br>
		<br>
		<mod:grupo titulo="Solicitação de passagem aérea (*)">

			<mod:radio titulo="SIM" var="opc" valor="1" reler="sim" />
			<mod:radio titulo="NÃO" var="opc" valor="2" reler="sim"/>
		</mod:grupo>
		
		<c:set var="valoropc" value="${opc}" />
	        <c:if test="${empty valoropc}">
	           <c:set var="valoropc" value="${param['opc']}" />
	        </c:if>
		
		<c:if test="${valoropc == '1'}">
		   <font color="red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A não devolução dos cartões de embarque no prazo de 05 dias do retorno à sede ensejará a restituição do valor pago a título de diárias (arts. 112 e 114 da Resolução 4/2008/CJF)</font>
		</c:if>
		
		
		
		<hr style="color: #FFFFFF;" />
		<mod:grupo titulo="Uso de Carro Oficial (*)">

			<mod:radio titulo="SIM" var="opcr" valor="1" reler="sim"/>
			<mod:radio titulo="NÃO" var="opcr" valor="2" reler="sim"/>
		</mod:grupo>	
			<c:set var="valoropcr" value="${opcr}" />
	        <c:if test="${empty valoropcr}">
	           <c:set var="valoropcr" value="${param['opcr']}" />
	        </c:if>
		
		<c:if test="${valoropcr == '2' && valoropc == '1'}">
		   <font color="red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;É devida a concessão do adicional de deslocamento previsto no § 2º do art. 107 da Resolução 4/2008/CJF</font>
		</c:if>
			
		
		<hr style="color: #FFFFFF;" />
		<mod:grupo titulo="Itinerários (*)">
			<mod:texto titulo="Origem/Destino" var="dest" largura="40" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></br>
		</mod:grupo>
		<mod:grupo titulo="Período de afastamento (*)">
			<mod:selecao titulo="Dia único" var="diaunic" opcoes="SIM;NÃO"
				reler="sim" />
			<br>
			<c:choose>
				<c:when test="${diaunic=='SIM'}">
					<mod:data titulo="Dia de afastamento" var="perafast" />
				</c:when>
				<c:otherwise>
					<mod:data titulo="Período de afastamento" var="perafast" />
					&nbsp;&nbsp;<mod:data titulo="a" var="perafast2" />
				</c:otherwise>
			</c:choose>
		</mod:grupo>
		
		<hr style="color: #FFFFFF;" />
		<span style="color: red"> <b>OBS.: O afastamento que se
		iniciar a partir da sexta-feira ou que inclua sábado, domingo ou
		feriado deverá ser acompanhado de justificativa, condicionando-se o
		pagamento à aceitação da justificativa pelo ordenador de despesas (art 111,II,§2º Res. 4/2008/CJF). O mesmo se dará quando houver
		necessidade de pernoite na noite anterior e/ou posterior ao evento.</b> </span>


		<mod:grupo>
			<mod:memo titulo="<b>Justificativa</b>" var="just" colunas="60"
				linhas="3" />
		</mod:grupo>



	</mod:entrevista>

	<mod:documento>
<head>
		<style type="text/css">
@page {
	margin-left: 0.5cm;
	margin-right: 0.5cm;
	margin-top: 1cm;
	margin-bottom: 1cm;
}
</style>
		</head>
		<c:set var="pessoa_propon"
			value="${f:pessoa(requestScope['propo_pessoaSel.id'])}" />
		<c:set var="pessoa_prop"
			value="${f:pessoa(requestScope['prop_pessoaSel.id'])}" />

		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="11pt"></c:set>
		</c:if>

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		
		<tr><td>&nbsp;</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:11pt;"><br/>PROPOSTA E CONCESSÃO DE DIÁRIAS N&ordm; ${doc.codigo} DE ${doc.dtExtensoMaiusculasSemLocalidade}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<br>
		<c:set var="cpf" value="${f:formatarCPF(cpf)}"></c:set>
		<c:set var="alm" value="${f:floatParaMonetario(valref)}"></c:set>
        <c:set var="trp" value="${f:floatParaMonetario(valtrans)}"></c:set>
		<c:if test="${ppq == '1'}">
           <c:set var="prop" value="${f:maiusculasEMinusculas(pessoa_prop.nomePessoa)}"></c:set>
           <c:set var="cpf" value="${f:formatarCPF(pessoa_prop.cpfPessoa)}"></c:set>
           <c:set var="alm" value=""></c:set>
           <c:set var="trp" value=""></c:set>
           <c:set var="carg" value="${f:maiusculasEMinusculas(pessoa_prop.cargo.descricao)}"></c:set>
           <c:set var="func" value="${pessoa_prop.funcaoConfianca.nomeFuncao}"></c:set>
           <c:set var="matr" value="${pessoa_prop.sesbPessoa}${pessoa_prop.matricula}"></c:set>
        </c:if>
        <c:set var="observ" value="Observação"></c:set>
        <c:if test="${opc == '1' || opcr == '2'}">
           <c:set var="observ" value="Observações"></c:set>
        </c:if>
       <!--
        <b>
        <table border="0"  bgcolor="#FFFFFF" cellpadding="1" width="51%" align="right" >
	  		   <tr>
  				<td width="40%" style="font-family:Arial;font-size:8.5pt;font-weight:normal" bgcolor="#FFFFFF" border="0" align="left"><b>(&nbsp;&nbsp;)&nbsp;INICIAL</b></td>
				<td width="60%" style="font-family:Arial;font-size:8.5pt;font-weight:normal" bgcolor="#FFFFFF" border="0" align="left"><b>(&nbsp;&nbsp;) Julgamento de Causas</b></td>
  			   </tr>
  		</table>
        <table width="100%" border="0" cellpadding="1" cellspacing="0" align="right">
			<tr>
               	<td bgcolor="#FFFFFF" align="left">  </td>
			</tr>
		</table>
        <table border="0"  bgcolor="#FFFFFF" cellpadding="1" width="51%" align="right" >
	  		   <tr>
  				<td width="40%" style="font-family:Arial;font-size:8.5pt;font-weight:normal" bgcolor="#FFFFFF" border="0" align="left"><b>(&nbsp;&nbsp;)&nbsp;PRORROGAÇÃO</b></td>
				<td width="60%" style="font-family:Arial;font-size:8.5pt;font-weight:normal" bgcolor="#FFFFFF" border="0" align="left"><b>(&nbsp;&nbsp;) Capacitação de Recursos Humanos</b></td>
  			   </tr>
  		</table>
  		</b>
  		<br>
  		</br>
          -->
		<!-- INICIO DA TABELA PROPONENTE -->
		<table width="100%" border="1" cellpadding="2" cellspacing="1">
			<tr>
				<td>
				<table width="100%" border="1" cellpadding="2" cellspacing="1">
					<tr>

						<td>
						<table width="100%" border="1" cellpadding="2" cellspacing="1">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>1-PROPONENTE</b>
								</td>
							</tr>
						</table>


						<table width="100%" border="0" cellpadding="3" cellspacing="0">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Nome&nbsp;
								&nbsp;&nbsp;:</b>&nbsp;${f:maiusculasEMinusculas(doc.subscritor.descricao)}</td>
							</tr>
						</table>


						<table width="100%" border="0" cellpadding="3" cellspacing="0">
							<tr><c:choose>
								<c:when test="${doc.subscritor.matricula > 9999}">
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Função
								:</b>&nbsp;${doc.subscritor.funcaoConfianca.nomeFuncao}</td>
								</c:when>
								<c:otherwise>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Cargo&nbsp;&nbsp;&nbsp;
								:</b>&nbsp;${f:maiusculasEMinusculas(doc.subscritor.cargo.descricao)}</td>
								</c:otherwise>
					            </c:choose>
							</tr>
						</table>
				</table>
				<table width="100%" border="1" cellpadding="2" cellspacing="1">
					<tr>
						<td>
						<table width="100%" border="1" cellpadding="2" cellspacing="1">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>2-PROPOSTO</b>
								</td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td width="75%" bgcolor="#FFFFFF" align="left"><b>Nome :
								</b>&nbsp;${prop}</td>
								
								<td width="25%" bgcolor="#FFFFFF" align="left"><b>CPF :</b> &nbsp;${cpf}</td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td width="75%" bgcolor="#FFFFFF" align="left"><b>Cargo
								</b>&nbsp;:&nbsp;${carg}&nbsp;&nbsp;&nbsp;&nbsp;<c:choose>
									<c:when test="${ppq == 1}"><b>Função
								</b>&nbsp;:&nbsp;${func}</td>
								<td width="25%" bgcolor="#FFFFFF" align="left"><b>Matrícula :
								</b>&nbsp;&nbsp;${matr}</c:when></c:choose></td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td width="40%" bgcolor="#FFFFFF" align="left"><b>C/C
								Nº : </b>&nbsp;${contac}</td>
								<td width="35%" bgcolor="#FFFFFF" align="left"><b>Banco :
								</b>&nbsp;${banc}</td>
								<td width="25%" bgcolor="#FFFFFF" align="left"><b>Agência :
								</b>&nbsp;${agen}</td>
							</tr>
						</table>
						<c:choose>
						<c:when test="${ppq == 2}">
						<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Valor
								Mensal do Auxílio-Alimentação : </b>&nbsp;R$&nbsp;${alm}</td>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Valor
								Mensal do Auxílio-Transporte : </b>&nbsp;R$&nbsp;${trp}
								</td>

							</tr>
						</table>
						</c:when></c:choose>
				</table>

				<table width="100%" border="1" cellpadding="2" cellspacing="1">
					<tr>
						<td>

						<table width="100%" border="1" cellpadding="2" cellspacing="1">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Serviço a ser executado e Local:</b></td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td width="100%" bgcolor="#FFFFFF">${texto_serv}</td>


							</tr>
						</table>
				</table>
				<table width="100%" border="1" cellpadding="2" cellspacing="1">
					<tr>

						<td>
						<table width="100%" border="1" cellpadding="2" cellspacing="1">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Solicitação
								de Passagem Aérea </b></td>

							</tr>
						</table>
						<table width="100%" border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td width="50%" bgcolor="#FFFFFF"><c:choose>
									<c:when test="${opc==1}">&nbsp;(X)&nbsp;SIM&nbsp;(&nbsp;&nbsp;)&nbsp;NÃO</c:when>
									<c:otherwise>&nbsp;(&nbsp;&nbsp;)&nbsp;SIM&nbsp;(X)&nbsp;NÃO</c:otherwise>
								</c:choose></td>



							</tr>
						</table>
				</table>
				<table width="100%" border="1" cellpadding="2" cellspacing="1">
					<tr>

						<td>
						<table width="100%" border="1" cellpadding="2" cellspacing="1">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Uso
								de Carro Oficial</b></td>

							</tr>
						</table>
						<table width="100%" border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td width="50%" bgcolor="#FFFFFF"><c:choose>
									<c:when test="${opcr==1}">&nbsp;(X)&nbsp;SIM&nbsp;(&nbsp;&nbsp;)&nbsp;NÃO</c:when>
									<c:otherwise>&nbsp;(&nbsp;&nbsp;)&nbsp;SIM&nbsp;(X)&nbsp;NÃO</c:otherwise>
								</c:choose></td>
							</tr>
						</table>
			     </table>
			     
			     <!--  -->
			     <table width="100%" border="1" cellpadding="2" cellspacing="1">
					  <tr>
                      <td>
						<table width="100%" border="1" cellpadding="2" cellspacing="1">
						   <tr>
							<td width="100%" bgcolor="#FFFFFF" align="left"><b>Itinerários</b></td>
                           </tr>
				        </table>
				        <table width="100%" border="0" cellpadding="2" cellspacing="0">
					       <tr>
						     <td width="65%" bgcolor="#FFFFFF"><b>Origem/Destino</b></td>
                             <td width="35%" bgcolor="#FFFFFF"><b><c:choose>
					            <c:when test="${diaunic=='SIM'}">Dia de Afastamento</c:when>
					            <c:otherwise>Período de Afastamento</c:otherwise>
					            </c:choose></b></td>
                           </tr>
				        </table>
				        <table width="100%" border="0" cellpadding="2" cellspacing="0">
					         <tr>
					         <td width="65%" bgcolor="#FFFFFF">${dest}</td>
					         <td width="35%" bgcolor="#FFFFFF"><c:choose>
					            <c:when test="${diaunic=='SIM'}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${perafast}</c:when>
					            <c:otherwise>${perafast}&nbsp;a&nbsp;${perafast2}</c:otherwise>
					            </c:choose>
					         </td>
					         </tr>
        		        </table>
			     </table>
			     
			     <!--  -->
			     <table width="100%" border="1" cellpadding="2" cellspacing="1">
					<tr>
						<td>

						<table width="100%" border="1" cellpadding="2" cellspacing="1">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Justificativa</b></td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td width="100%" bgcolor="#FFFFFF">${just}</td>
							</tr>
						</table>
				</table>
			     
			     
			    <table width="100%" border="1" cellpadding="2" cellspacing="1">
					<tr>
						<td>

						<table width="100%" border="1" cellpadding="2" cellspacing="1">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>${observ}</b></td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td width="100%" bgcolor="#FFFFFF">O afastamento que se
		iniciar a partir da sexta-feira ou que inclua sábado, domingo ou
		feriado deverá ser acompanhado de justificativa, condicionando-se o
		pagamento à aceitação da justificativa pelo ordenador de despesas (art 111,II,§2º Res. 4/2008/CJF). O mesmo se dará quando houver
		necessidade de pernoite na noite anterior e/ou posterior ao evento.</td>
							</tr>
						</table>
						
						<c:if test="${opc == '1'}">
						
						<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td width="100%" bgcolor="#FFFFFF">A não devolução dos cartões de embarque no prazo de 05 dias do retorno à sede ensejará a restituição do valor pago a título de diárias (arts. 112 e 114 da Resolução 4/2008/CJF).</td>
							</tr>
						</table>
						</c:if>
						
						<c:if test="${opcr == '2' && opc == '1'}">
						
						<table width="100%" border="0" cellpadding="5" cellspacing="0">
							<tr>
								<td width="100%" bgcolor="#FFFFFF">É devida a concessão do adicional de deslocamento previsto no § 2º do art. 107 da Resolução 4/2008/CJF.</td>
							</tr>
						</table>
						
						</c:if>
			    	</table> 
			    </table>     
			     
			
			     
			 
		    
		

		<!-- FIM DA TABELA PROPONENTE-->


       <!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
				
		<!-- INICIO FECHO -->
		<p align="center">${doc.dtExtenso}</p>
	    <!-- FIM FECHO -->
	    
	    
	    
		<!-- INICIO ASSINATURA -->
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<!-- FIM ASSINATURA -->
		
		<br>/${doc.cadastrante.siglaPessoa}</br> 
		<!-- 
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
		<br>
		<table width="100%" border="1" cellpadding="2" cellspacing="1">
			<tr>
				<td>
				
				<table width="100%" border="1" cellpadding="2" cellspacing="1">
					<tr>

						<td>
						<table width="100%" border="1" cellpadding="2" cellspacing="1">
							<tr>
								<td width="100%" bgcolor="#FFFFFF" align="left"><b>Cálculo
								das Diárias</b></td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td width="27.5%" bgcolor="#FFFFFF"><b>Número Total de
								Diárias</b></td>
                                <td width="27.5%" bgcolor="#FFFFFF" align="left"><b>:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;___________________</td>
								<td width="16%" bgcolor="#FFFFFF"><b>Vale-Refeição</b></td>
                                <td width="29%" bgcolor="#FFFFFF" align="left"><b>:</b>&nbsp;R$&nbsp;_____________________</td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td width="27.5%" bgcolor="#FFFFFF" align="left"><b>Valor Unitário da Diária</td>
								<td width="27.5%" bgcolor="#FFFFFF" align="left"><b>:</b>&nbsp;R$&nbsp;___________________</td>
								<td width="16%" bgcolor="#FFFFFF"><b>Vale-Transporte</b></td>
								<td width="29%" bgcolor="#FFFFFF" align="left"><b>:</b>&nbsp;R$&nbsp;_____________________</td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td width="27.5%" bgcolor="#FFFFFF" align="left"><b>Auxílio</b>&nbsp;(art. 107 §2º Res.CJF)</b></td>
								<td width="27.5%" bgcolor="#FFFFFF" align="left"><b>:</b>&nbsp;R$&nbsp;___________________</td>
								<td width="16%" bgcolor="#FFFFFF" align="left"><b>Total Desconto</b></td>
                                <td width="29%" bgcolor="#FFFFFF" align="left"><b>:</b>&nbsp;R$&nbsp;_____________________</td>
							</tr>
						</table>
						<table width="100%" border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td width="27.5%" bgcolor="#FFFFFF" align="left"><b>Valor Bruto</b></td>
								<td width="27.5%" bgcolor="#FFFFFF" align="left"><b>:</b>&nbsp;R$&nbsp;___________________</td>
								<td width="16%" bgcolor="#FFFFFF" align="left"><b>Valor Líquido</b></td>
                                <td width="29%" bgcolor="#FFFFFF" align="left"><b>:</b>&nbsp;R$&nbsp;_____________________</td>
							</tr>
						</table>
						<table width="100%" border="1" cellpadding="2" cellspacing="1">
							<tr>

								<td>
								<table width="100%" border="1" cellpadding="2" cellspacing="1">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"><b>3-Concessão</b>
										</td>

									</tr>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="50%" bgcolor="#FFFFFF">(&nbsp;&nbsp;)&nbsp;Concedo
										as diárias.Pague-se.</td>
										<td width="50%" bgcolor="#FFFFFF">(&nbsp;&nbsp;)&nbsp;Requisite(m)-se
										a(s) passagem(ens)</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left">Em
										&nbsp;____ /____ /_________.</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="center">__________________________________________________________</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="center">Assinatura
										da Autoridade Competente</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="1" cellpadding="2" cellspacing="1">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"><b>4-Pagamento/Recebimento</b>
										</td>

									</tr>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left">Paga a
										importância
										de&nbsp;&nbsp;_________________&nbsp;(&nbsp;___________________________________________________</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left">________________________________________________________________&nbsp;),</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="60%" bgcolor="#FFFFFF" align="left">através da
										Ordem Bancária nº.&nbsp;&nbsp;___________________________,</td>
										<td width="40%" bgcolor="#FFFFFF" align="left">&nbsp;de&nbsp;____
										/____ /_________.</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="center">__________________________________________________________</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="center">Assinatura
										do Resp. Setor Financeiro</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="1" cellpadding="2" cellspacing="1">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"><b>5-Publicação</b>
										</td>

									</tr>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left">O
										presidente documento está de acordo com as normas
										regulamentares pertinentes e será publicado no Boletim:</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="50%" bgcolor="#FFFFFF" align="left">Concessão:</td>

										<td width="50%" bgcolor="#FFFFFF" align="left">Pagamento:</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="50%" bgcolor="#FFFFFF" align="left">Nº.&nbsp;______________
										&nbsp;,&nbsp;&nbsp;de&nbsp;____ /____ /_______.</td>

										<td width="50%" bgcolor="#FFFFFF" align="left">Nº.&nbsp;_______________
										&nbsp;,&nbsp;&nbsp;de&nbsp;____ /____ /_______.</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="center">__________________________________________________________</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="center">Assinatura
										do Responsável</td>
								</table>
								<table width="100%" border="0" cellpadding="2" cellspacing="0">
									<tr>
										<td width="100%" bgcolor="#FFFFFF" align="left"></td>
								</table>
						</table>
				</table>
		</table>
         -->

		
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
		</body>
		
	</mod:documento>
</mod:modelo>