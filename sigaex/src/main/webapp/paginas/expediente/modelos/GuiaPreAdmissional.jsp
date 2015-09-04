<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<mod:modelo>
	<mod:entrevista>

		<mod:grupo>
			<mod:selecao titulo="Candidato / Requisitado" opcoes="Candidato;Requisitado" var="tipo" reler="sim"/>
		</mod:grupo>

		<mod:grupo>
			<mod:texto titulo="Nome do(a) candidato(a)" var="nome" largura="50" obrigatorio="Sim"/>
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Sexo" opcoes="Masculino;Feminino" var="sexo"/>
		</mod:grupo>


<c:if test="${tipo == 'Candidato'}">
		<mod:grupo>
			<mod:texto titulo="Cargo no qual deverá tomar posse" var="cargo" largura="30"/>
		</mod:grupo>
				<mod:grupo>
			<mod:texto titulo="Área referente ao cargo" var="area" largura="30" obrigatorio="Sim"/>
		</mod:grupo>
				<mod:grupo>
			<mod:data titulo="Data em que se exaure o prazo para a posse do(a) candidato(a)" var="data" obrigatorio="Sim"/>
		</mod:grupo>
</c:if>

</mod:entrevista>

	<mod:documento>
		<c:set var="tl" value="10pt" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 3cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0"  bgcolor="#FFFFFF">
			<tr><td>
			<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
			</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/><br/>
					<table width="100%" border="0" >
						<tr>
						<c:choose>									
							<c:when test="${tipo == 'Requisitado'}">
								<td align="center"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold" >GUIA PR&Eacute;-ADMISSIONAL/REQUISITADO</p></mod:letra></td>			   				
							</c:when>
							<c:otherwise>										
				    			 <td align="center"><mod:letra tamanho="${tl}"><p style="font-family:Arial;font-weight:bold" >GUIA PR&Eacute;-ADMISSIONAL</p></mod:letra></td>	
							</c:otherwise>	
						</c:choose>							
						</tr>
						<tr>
						<td align="right"><br/><br/><br/><b>SOLICITA&Ccedil;&Atilde;O Nº ${doc.codigo}</b>
						</td>
						</tr>
						<tr>
							<td align="right">${doc.dtExtenso}<br/><br/><br/></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->


		<mod:letra tamanho="${tl}">

			<p style="TEXT-INDENT: 2cm" align="justify">
			Sr.(a) Supervisor(a) da Seção de Serviços de Saúde<br/><br/>
			
			Solicito a V. Sª que ${nome}, do sexo ${sexo}, seja
submetido(a) a Exame de Aptidão Física/Sanidade Mental para

 <c:if	test="${tipo eq 'Candidato'}">

efeito de posse no cargo de ${cargo}, Área ${area}. Informo que o prazo para a posse se exaure em ${data}.
			
			</c:if>
			
			 <c:if	test="${tipo eq 'Requisitado'}">
			 o exercício de função comissionada/cargo em comissão.
			 </c:if>
			<br/><br/>
			Atenciosamente,
			</p>
			
						

			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		</mod:letra>




		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->


		</body>
		</html>
	</mod:documento>
</mod:modelo>