<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo>
	<mod:entrevista>
	<br/>
		<span style="color:red"> <b>PREENCHER OBRIGATORIAMENTE O CAMPO DESCRIÇÃO COM NOME COMPLETO E ASSUNTO</b></span><br>
		<span style="color:red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À SRH</b></span>		
		<br/>
		<br/>
		<mod:grupo titulo=" ">	
		
	  	 <mod:grupo>
				<mod:selecao var="excel"
				titulo="VOCATIVO"
				opcoes="EXCELENTÍSSIMA SENHORA DESEMBARGADORA FEDERAL; EXCELENTÍSSIMO SENHOR DESEMBARGADOR FEDERAL"	
				reler="sim" />
		</mod:grupo>
		<br>	
	</mod:grupo>
			<mod:grupo titulo=""> 
		        <mod:texto titulo="RAMAL DO REQUERENTE" var="ramal"/></mod> <br><br>
		        <mod:selecao titulo="Acerto Gramatical da Lotação" var="acgr"  opcoes="no;na" reler="sim" />
			</mod:grupo>
			 <br/> 
			<mod:grupo titulo="">
				<mod:data titulo="Data de posse no outro órgão" var="dtinicio"/>
			</mod:grupo>
			<br/>
		<mod:grupo titulo="Documentos em anexo (marcados com * são imprescindíveis para instrução do processo administrativo, consoante Resolução nº 148, de 26/05/95, do Conselho da Justiça Federal)">	
			<span style="color:red"> <b>*</b></span>	
			<mod:caixaverif titulo="Copia do termo de posse em outro cargo público;"
							var="termposs" reler="sim"/><br/>
			<span style="color:red"> <b>*</b></span>
			<mod:caixaverif titulo="Declaração de bens atualizada até a data da exoneração, com os respectivos valores;"
							var="declarabens" reler="sim"/><br/>
			<span style="color:red"> <b>*</b></span>
			<mod:caixaverif titulo="Cópia da declaração de Imposto de Renda ou Declaração de Isenção de apresentação da mesma;"
							var="copiadeclara" reler="sim" /><br/>
			<span style="color:red"> <b>*</b></span>
			<mod:caixaverif titulo="Cópia do CPF;"
							var="copiacpf" reler="sim"/><br/>
			<span style="color:red"> <b>*</b></span>
			<mod:caixaverif titulo="Carteira Funcional;"
							var="cartfunc" reler="sim" /><br/>
							<span style="color:red"> <b>*</b></span>	
			<mod:caixaverif titulo="Instrumento de Certificação Digital (SmartCard ou similar)"
							var="certdigi" reler="sim" /><br/>
			&nbsp;&nbsp;
			<mod:caixaverif titulo="Crachá Funcional;"
							var="crachafunc" reler="sim" /><br/>
			&nbsp;&nbsp;
			<mod:caixaverif titulo="Carteira(s) do Plano de Saúde do Titular e dependentes <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(se a exoneração acontecer antes do término do mês, 
									a devolução da(s)carteira(s) deverá ocorrer no início do mês subsequente, diretamente na DIMED)"
							var="cartplano" reler="sim"/><br/>
			 &nbsp;&nbsp;
			 <mod:selecao titulo="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Quantidade de dependentes" var="dependente" opcoes=" ;1 (um) dependente;2 (dois) dependentes;3 (três) dependentes;4 (quatro) dependentes;5 (cinco) dependentes;5 (cinco) dependentes;6 (seis) dependentes;7 (sete) dependentes;8 (oito) dependentes;9 (nove) dependentes;10 (dez) dependentes" reler="sim"/><br/>
			
			
		     <mod:grupo>
		        &nbsp;
			    <mod:caixaverif titulo="Outros" var="outros" reler="sim" />
			    <c:if test="${ outros == 'Sim'}">
			    <mod:texto titulo="- &nbsp;Especificar" var="outrostext" largura="40" />
		    	</c:if>
		     </mod:grupo>
			<!--  
			 <mod:grupo>
		        &nbsp;
             	<mod:caixaverif titulo="Outros" var="outros" reler="sim"/>
				<mod:texto titulo="" var="descricaoutros" largura="50" /></mod>
		    </mod:grupo> -->
		</mod:grupo>
			
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
		<br>	
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#ffffff"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" /><br>
		</td></tr>
				<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr><br>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:8pt;">SOLICITAÇÃO DE VACÂNCIA</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
       
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
		 <br>	
		<c:set var="lotc" value="lotado"></c:set>
		<c:if test="${doc.subscritor.sexo == 'F'}">
			<c:set var="lotc" value="lotada"></c:set>
		</c:if>
		<c:set var="opt" value="${f:classNivPadr(doc.subscritor.padraoReferencia)}"/>	
            <p style="font-family:Arial;"><center><b>${excel} PRESIDENTE DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO</b></center></p>
			
			<p style="font-family:Arial;font-size:9pt">
			${doc.subscritor.descricao}, matrícula ${doc.subscritor.matricula}, ${doc.subscritor.cargo.nomeCargo}, ${opt},
			do Quadro de Pessoal do Tribunal Regional Federal da 2ª Região, ${lotc} ${acgr} ${doc.subscritor.lotacao.descricao}, ramal ${ramal}, do Quadro de Pessoal deste Tribunal, vem requerer a Vossa Excelência  
			<b>vacância</b> do cargo público que ora ocupa, com base no art. 33, VIII, da Lei nº 8.112/90, tendo em vista a posse em outro cargo público inacumulável, 
			a partir do dia ${dtinicio}.<br/><br>
			Seguem anexos os seguintes documentos: <br>
			</p>
			<p style="font-family:Arial;font-size:8pt">
			*&nbsp;
			<c:if test="${ termposs == 'Sim'}">
			    [X] Copia do termo de posse em outro cargo público.
			    <br>	
			</c:if>
			<c:if test="${ termposs == 'Nao'}">
			    [&nbsp;&nbsp;] Copia do termo de posse em outro cargo público.
			    <br>	
			</c:if>
			*&nbsp;
			<c:if test="${ declarabens == 'Sim'}">
			    [X] Declaração de bens atualizada até a data da exoneração, com os respectivos valores.
			    <br>	
			</c:if>
			<c:if test="${ declarabens == 'Nao'}">
			    [&nbsp;&nbsp;] Declaração de bens atualizada até a data da exoneração, com os respectivos valores.
			    <br>	
			</c:if>
			*&nbsp;
			<c:if test="${ copiadeclara == 'Sim'}">
			    [X] Cópia da declaração de Imposto de Renda ou Declaração de Isenção de apresentação da mesma.
			    <br>		
			</c:if>
			<c:if test="${ copiadeclara == 'Nao'}">
			    [&nbsp;&nbsp;] Cópia da declaração de Imposto de Renda ou Declaração de Isenção de apresentação da mesma.
			    <br>		
			</c:if>
			*&nbsp;
			<c:if test="${ copiacpf == 'Sim'}">
			    [X] Cópia do CPF.
			    <br>		
			</c:if>
			<c:if test="${ copiacpf == 'Nao'}">
			    [&nbsp;&nbsp;] Cópia do CPF.
			    <br>		
			</c:if>
			*&nbsp;
			<c:if test="${ cartfunc == 'Sim'}">
			    [X] Carteira Funcional.
			    <br>		
			</c:if>
			<c:if test="${ cartfunc == 'Nao'}">
			    [&nbsp;&nbsp;] Carteira Funcional.
			    <br>		
			</c:if>
			*&nbsp;
			<c:if test="${ certdigi == 'Sim'}">
			    [X] Instrumento de Certificação Digital.
			    <br>		
			</c:if>
			<c:if test="${ certdigi == 'Nao'}">
			    [&nbsp;&nbsp;] Instrumento de Certificação Digital.
			    <br>		
			</c:if>
			&nbsp;&nbsp;&nbsp;
			<c:if test="${ crachafunc == 'Sim'}">
			    [X] Crachá Funcional.
			    <br>		
			</c:if>
			<c:if test="${ crachafunc == 'Nao'}">
			    [&nbsp;&nbsp;] Crachá Funcional.
			    <br>		
			</c:if>
			&nbsp;&nbsp;&nbsp;
			
			<c:choose>
					<c:when test="${cartplano == 'Sim' }"> 
						<c:if test="${dependente == ' ' }">
			    			[X] Carteira do Plano de Saúde.
						</c:if>	
						<c:if test="${dependente != ' ' }">
			    			[X] Carteiras do Plano de Saúde do Titular e de ${dependente}.
						</c:if>
					</c:when>
					<c:otherwise>
						<c:if test="${ cartplano == 'Nao'}">
			    			[&nbsp;&nbsp;] Carteira(s) do Plano de Saúde do Titular e Dependentes<br>
						</c:if>
					</c:otherwise>
			</c:choose><br>
			&nbsp;&nbsp;&nbsp;
			<c:if test="${ outros == 'Sim'}">
			    [X] ${outrostext}
			</c:if>
			
			</p>
			 
			 <br/>		
			Termos em que <br/>
			Espera deferimento.<p>
			
		<p align="center">
		
		<!-- INICIO FECHO -->
		 ${doc.dtExtenso}
		<!-- FIM FECHO -->
				
		</p>
		
		
		<!-- import java.util.Date; Date hoje = new Date();  -->
		
		<!-- INICIO ASSINATURA -->
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<!-- FIM ASSINATURA -->
		
		<p style="font-family:Arial;font-size:9pt">
		 * Documentos <b>imprescindíveis</b> para instrução do processo administrativo, consoante Resolução nº 148, de 26/05/95, <br>
		 &nbsp;&nbsp;do Conselho da Justiça Federal.
		</p>
		<br>
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
	
		</body>
	</mod:documento>
</mod:modelo>