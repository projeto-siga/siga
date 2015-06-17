<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
REQUERIMENTO PARA AUXILIO-NATALIDADE -->


<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">	
	<mod:entrevista>
	
		<!--  inicio da entrevista -->
		<mod:grupo titulo="Quem está requerendo o Auxílio?">
			<mod:selecao titulo="Requerente" var="requerente" opcoes="Pai;Mãe"
				reler="ajax" idAjax="requerenteAjax" />
				
			<!--  CONDIÇÃO DE SE EH PAI OU MAE -->
			<mod:grupo depende="requerenteAjax">
			<c:if test="${requerente == 'Pai'}">
				<mod:selecao titulo="A mãe é Servidora Pública?" var="seraServidora"
					opcoes="---;Sim;Não" reler="ajax" idAjax="seraServidoraAjax"/>				
				
				<mod:grupo depende="seraServidoraAjax">
				<c:if test="${seraServidora == 'Não'}">
					<mod:grupo titulo="DADOS DA MÃE">
						<mod:texto titulo="Nome Completo" largura="50" var="nomeMae" />
					</mod:grupo>
				</c:if>
				</mod:grupo>
			</c:if>
			</mod:grupo>
		</mod:grupo>



		<!-- CODIÇÃO PARA REALIZAR O REQUERIMENTO COM OU SEM DECLARAÇÃO ou nem realiza-lo -->


		<!--  SE O SISTEMA PERMITIR O BENEFICIO -->
		
		<mod:grupo titulo="DETALHES DO(S) RECÉM-NASCIDO(S)">
				<mod:selecao titulo="Número de Recém-Nascidos"
					var="quantidadeNascidos" opcoes="1;2;3;4;5;6" reler="ajax" idAjax="quantidadeNascidosAjax" />
				<mod:grupo depende="quantidadeNascidosAjax">
				<c:forEach var="i" begin="1" end="${quantidadeNascidos}">
					<mod:grupo>
						<mod:texto titulo="${i}) Nome" largura="40"
							var="nomeRecemNasc${i}" />
						<mod:data titulo="Data de Nascimento" var="dataNasc${i}" />
					</mod:grupo>
				</c:forEach>
				</mod:grupo>
		</mod:grupo>		
		<!-- and NomeMae != null -->
		<!--  SE O SISTEMA NÃO PERMITIR O BENIFICIO	-->
		<c:if test="${(requerente == 'Pai' and seraServidora == 'Sim')}">
			<FONT COLOR="red"><b>
			<P>Este Auxílio Natalidade não poderá ser concedido, pelos
			seguintes motivos: <br>
			Segundo o <b>Art. 196 § 2º</b> da Lei <b>nº 8.112/90 </B>, que reza:
			<br>
			<I> "O auxílio será pago ao cônjuge ou companheiro servidor
			público, quando a parturiente não for servidora".
			</I>
			<br>
			Desta forma o beneficio só poderá ser concedido a apenas a servidora<br>
			mãe, e este benefício não poderá ser duplicado.</P>
			</b></FONT>
			<b></b>
		</c:if>
	</mod:entrevista>

	<mod:documento>

		
		
		<mod:valor var="texto_requerimento">
		<P><!--  REQUERIMENTO SE BENEFICIO NÃO FOR CONCEDIDO IMPRIMIRA MENSAGEM ALERTA -->
		<c:if test="${(requerente == 'Pai' and seraServidora == 'Sim')}">
				<TABLE width="470" border="1" cellspacing="0" cellpadding="0">
					<TR bgcolor="#000000">
						<TD>
						<DIV align="center"><FONT face="Arial, Helvetica, sans-serif"
							size="2"><b><FONT size="4" color="#FFFFFF">ATEN&Ccedil;&Atilde;O</FONT></b></FONT></DIV>
						</TD>
					</TR>
					<TR>
						<TD>
						<P align="justify"><FONT size="3" face="Arial, Helvetica, sans-serif">Este
						Auxílio Natalidade não poderá ser concedido, pelos seguintes
						motivos: Segundo o <b>Art. 196 § 2º</b> da Lei <b>nº 8.112/90 </b>,
						que reza:<br>
						</FONT></P>
						<P align="justify"><FONT size="3" face="Arial, Helvetica, sans-serif"><I>"O
						auxílio será pago ao cônjuge ou companheiro servidor público,<br>
						quando a parturiente não for servidora".</I><br>
						Desta forma o beneficio só poderá ser concedido a apenas a
						servidora<br>
						mãe, e este benefício não poderá ser duplicado. </FONT></P>
						</TD>
					</TR>
					<TR bgcolor="#999999">
						<TD>&nbsp;</TD>
					</TR>
				</TABLE>
				</P>
		</c:if>
		
		<c:if test="${(requerente == 'Pai' and seraServidora == 'Não') or requerente == 'Mãe'}">
			<p style="TEXT-INDENT: 2cm" align="justify">
				${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, vem requerer a Vossa Senhoria concessão do <b>AUXÍLIO-NATALIDADE</b>, a que faz jus, de acordo com o art. 196 e parágrafos 
				da Lei 8.112/90 e Resolução nº 2/2008 do Conselho da Justiça Federal, conforme documentação em anexo, em virtude do nascimento
				
							
			<c:if test="${quantidadeNascidos =='1'}"> 
				de 
				${requestScope[f:concat('nomeRecemNasc',1)]}, em ${requestScope[f:concat('dataNasc',1)]}.			
				
			</c:if>

			
			<c:if test="${quantidadeNascidos >'1'}">
				dos seguintes nascituros:
				</p>
				<br>
				<ul>
					<c:forEach var="i" begin="1" end="${quantidadeNascidos}">
							<li><p>
							${requestScope[f:concat('nomeRecemNasc',i)]}, em ${requestScope[f:concat('dataNasc',i)]}.
							</p></li>
					</c:forEach>
					</ul>
			</c:if>		
		
		</c:if>
		
		</mod:valor>
		<!--  REQUERIMENTO  CONCEDIDO -->
		<%--<mod:valor var="texto_requerimento2">
		<c:if test="${(requerente == 'Pai' and seraServidora == 'Não') or requerente == 'Mãe'}">
	
			<c:import url="/paginas/expediente/modelos/inc_tit_SraDiretoraSubsecretariaRH.jsp" />
			
			
				<p style="TEXT-INDENT: 2cm" align="justify">
				${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferencia}, vem requerer a Vossa Senhoria, a concessão do <b>AUXÍLIO-NATALIDADE</b>, a que faz jus, de acordo com o art. 196 e parágrafos 
				da Lei 8.112/90 e Resolução nº 106/93, alterada pela Resolução nº 290/2002, ambas 
				do Conselho da Justiça Federal, conforme documentação em anexo, em virtude do nascimento
				
							
			<c:if test="${quantidadeNascidos =='1'}"> 
				de <b><i>	
				${requestScope[f:concat('nomeRecemNasc',1)]}, em ${requestScope[f:concat('dataNasc',1)]};				
				</i></b>
			</c:if>

			
			<c:if test="${quantidadeNascidos >'1'}">
				dos seguintes nascituros:
				</p>
				<br>
				<ul>
					<c:forEach var="i" begin="1" end="${quantidadeNascidos}">
							<li><p><b><i>
							${requestScope[f:concat('nomeRecemNasc',i)]}, em ${requestScope[f:concat('dataNasc',i)]};
							</i></b></p></li>
					</c:forEach>
					</ul>
			</c:if>
			<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
		
			<p align="center">${doc.dtExtenso}</p>
			<br>
			<br>
			<c:import
				url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
			<c:import
				url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		
			
		</c:if>
		</mod:valor>--%>

			<!--  EMITE A DECLARAÇÃO, quando a mãe nao é servidora mas o pai SIM -->
		
		<mod:valor var="texto_requerimento2">
			<c:if test="${requerente == 'Pai' and seraServidora == 'Não'}">
				<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
				<c:import url="/paginas/expediente/modelos/inc_tit_declaracao.jsp" />
					
					<p style="TEXT-INDENT: 2cm" align="justify">
					Declaro, atendendo ao disposto no § 2º do art. 196 da Lei n.º
						8.112/90, 
					para fins de concessão de auxílio-natalidade, que
					${nomeMae}</b> não é servidora pública.
					</p>
					<br>
					<br>
					<br>
					<p align="center">${doc.dtExtenso}</p>
					<c:import
						url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
					<c:import
						url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		  </c:if>
		</mod:valor>
		
	</mod:documento>
</mod:modelo>
