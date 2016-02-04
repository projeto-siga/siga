<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo>
	<mod:entrevista>
		<br>
		<span style="color:red"> <b>PREENCHER OBRIGATORIAMENTE O CAMPO DESCRIÇÃO COM NOME COMPLETO E ASSUNTO</b></span><br>
		<span style="color:red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À SRH</b></span>
		<br/><br/>
	<mod:grupo titulo=" ">	
		
	  	 <mod:grupo>
				<mod:selecao var="ilustrissimo"
				titulo="VOCATIVO"
				opcoes="ILUSTRÍSSIMA SENHORA DIRETORA;ILUSTRÍSSIMO SENHOR DIRETOR"	
				reler="sim" />
		</mod:grupo>
		<br>
	</mod:grupo>
		<mod:grupo titulo="DADOS DO REQUERENTE:"> 
		        <mod:texto titulo="RAMAL DO REQUERENTE" var="ramal"/></mod> <br><br>
		        <mod:selecao titulo="Acerto Gramatical da Lotação" var="acgr"  opcoes="no;na" reler="sim" />
		</mod:grupo>
		<hr style="color: #FFFFFF;" />
		<mod:selecao var="contadorDeCurso"
				titulo="Quantidade de Cursos"
				opcoes="1;2;3;4;5"
				reler="sim"  /><br/>
		<mod:grupo depende="contDependAjax">
				<c:forEach var="i" begin="1" end="${contadorDeCurso}">
					<mod:grupo>
						<mod:texto titulo="Curso nº ${i}" var="nomecurso${i}" largura="30" 
						maxcaracteres="50" obrigatorio="Sim"/>
					</mod:grupo>
        <!-- 			<mod:grupo>
						<mod:texto var="instituicao${i}" largura="30" maxcaracteres="50"
							obrigatorio="Sim" titulo="Instituição"/>
						</mod:grupo>
					<mod:grupo>
						<mod:selecao var="ano${i}" titulo="Ano" 
						opcoes=" ;2008;2009;2010;2011;2012;2013;2014;2015;2016;2017;2018;2019;2020"
						reler="sim" idAjax="anoAjax"/>
						<mod:texto titulo="Horas" var="hora${i}"
							obrigatorio="Não" />
					</mod:grupo> -->
					<hr style="color: #FFFFFF;" />
				</c:forEach>
		</mod:grupo>	
		
		
		<mod:grupo titulo="Finalidade(s):">	
		     <mod:grupo>
				<mod:caixaverif titulo="Concessão de Adicional de Qualificação" var="concadicqual" reler="sim" />
		   	 </mod:grupo>
		     
		     <mod:grupo>
				<mod:caixaverif titulo="Carga horária para gerente, conforme previsto na Lei nº 11.416/2006" var="cargahorgerent"
					reler="sim" />
		     </mod:grupo>
		   
           	<mod:grupo>
             	<mod:caixaverif titulo="Outros" var="outros" reler="sim"/>
				<mod:texto titulo="" var="descricaoutros" largura="50" /></mod>
			</mod:grupo>
    	</mod:grupo>
		
	
			
			<!-- <b>DATA DE EXPEDIÇÃO:</b>
				<mod:selecao var="dia"
				titulo="DIA"
				opcoes="01;02;03;04;05;06;07;08;09;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31"
				reler="sim" idAjax="diaAjax" />
				
				<mod:selecao var="mes"
				titulo="MÊS"
				opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro"
				reler="sim" idAjax="mesAjax" />
				
				<mod:selecao var="ano"
				titulo="ANO"
				opcoes="2011;2012;2013;2014;2015;2016;2017;2018;2019;2020"
				reler="sim" idAjax="anoAjax" />-->
						
	</mod:entrevista>
	
<mod:documento>			
			<head>
		<style type="text/css">
@page {
	margin-left: 1cm;
	margin-right: 1cm;
	margin-top: 1cm;
	margin-bottom: 1cm;
}
      
</style>
		</head>
		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="7pt"></c:set>
		</c:if>
			
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#ffffff"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		   <tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr><br><br>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">CADASTRAMENTO DE CURSOS EXTERNOS</p></td>
						</tr>
					</table>
				</td>
			</tr>
			</table>
		FIM PRIMEIRO CABECALHO -->
		
       
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" /> 
		FIM CABECALHO -->
				
		
		<c:set var="lotc" value="lotado"></c:set>
		<c:if test="${doc.subscritor.sexo == 'F'}">
			<c:set var="lotc" value="lotada"></c:set>
		</c:if>
		 <c:set var="opt" value="${f:classNivPadr(doc.subscritor.padraoReferencia)}"/>
         <c:set var="singul" value="seja averbada a conclusão do seguinte curso"></c:set>
         <c:set var="singcur" value="Curso Realizado:"></c:set>
         <c:set var="singdip" value="diploma / certificado"></c:set>
       
        <c:if test="${contadorDeCurso > '1'}">
           <c:set var="singul" value="sejam averbadas as conclusões dos seguintes cursos"></c:set>
           <c:set var="singcur" value="Cursos Realizados:"></c:set>
           <c:set var="singdip" value="diplomas / certificados"></c:set>
        </c:if>
        <c:set var="finali" value="a seguinte finalidade"></c:set>
        <c:if test="${(concadicqual == 'Sim' and outros == 'Sim') or (concadicqual == 'Sim' and cargahorgerent == 'Sim') or (cargahorgerent == 'Sim' and outros == 'Sim')}">
          <c:set var="finali" value="as seguintes finalidades"></c:set>
        </c:if>
		<p style="text-align: center;font-weight:bold;font-size:11pt;"><br><b> ${ilustrissimo} DA SECRETARIA DE RECURSOS HUMANOS</b></center></p>
			<br><br>		
			${doc.subscritor.descricao}, matrícula ${doc.subscritor.matricula}, ${doc.subscritor.cargo.nomeCargo}, ${opt},
			do Quadro de Pessoal do Tribunal Regional Federal da 2ª Região, ${lotc} ${acgr} ${doc.subscritor.lotacao.descricao}, ramal ${ramal}, requer a V.Sª que ${singul} em seus assentamentos funcionais:<br/>
			 <br />
			<b>${singcur}</b><br>
					
			<c:if test="${contadorDeCurso > '0'}">
			<c:forEach var="i" begin="1" end="${contadorDeCurso}">
				<c:set var="cur" value="${requestScope[(f:concat('nomecurso',i))]}" />
				<u>${cur}</u><br>		    
			 </c:forEach>
		     </c:if>
			<br> conforme ${singdip} em anexo, com ${finali} :
		<br>
		<br>	
			
		
		
		<c:choose>
					<c:when test="${concadicqual == 'Sim'}">[X]</c:when>
					<c:otherwise>[&nbsp;&nbsp;&nbsp;]</c:otherwise>
		</c:choose>
		 Concessão de Adicional de Qualificação. <br>
		<c:choose>
					<c:when test="${cargahorgerent == 'Sim'}">[X] </c:when>
					<c:otherwise>[&nbsp;&nbsp;&nbsp;]</c:otherwise>
		</c:choose>
		Carga horária para gerente, conforme previsto na Lei nº. 11.416/2006.<br>	
		<c:choose>
					<c:when test="${outros== 'Sim'}">[X] ${descricaoutros}</c:when>
					<c:otherwise>[&nbsp;&nbsp;&nbsp;] Outros<c:set var="descricaoutros" value="empty"/></c:otherwise>
		</c:choose>
		<br><br>
		
		
		 		 		
		Termos em que <br/>
		Espera deferimento.
		
		<!-- INICIO FECHO -->
		<p align="center">
		${doc.dtExtenso}
		</p><br>
		<!-- FIM FECHO -->
		
		<!-- INICIO ASSINATURA -->
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<!-- FIM ASSINATURA -->
				
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
		</td>
			</tr>
		</table>
		</body>
	</mod:documento>
</mod:modelo>