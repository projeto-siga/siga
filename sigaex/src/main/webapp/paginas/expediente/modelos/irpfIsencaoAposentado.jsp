<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de ISENÇÃO DO IMPOSTO DE RENDA PARA APOSENTADO   -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>	
		<mod:grupo titulo="DETALHES DO APOSENTADO">
				<mod:texto titulo="Nome do Servidor Inativo" var="nomeServidorInativo" largura="60" />
				<mod:grupo>
					<mod:texto titulo="Endereço" var="endereco" largura="70" />
				</mod:grupo>
				<mod:grupo>
				<mod:texto titulo="Telefone (1)" var="tel1" largura="15" maxcaracteres="11"/>
				<mod:texto titulo="Telefone (2)" var="tel2" largura="15" maxcaracteres="11"/>
				</mod:grupo>
		</mod:grupo>		
	</mod:entrevista>
			
	<mod:documento>
	
		<mod:valor var="texto_requerimento">	
		<p style="TEXT-INDENT: 2cm" align="justify">
		<b>${nomeServidorInativo}</b>, servidor(a) Inativo(a) desta <c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose>, residente na <b>${endereco}</b>, 
				
		<c:if test="${tel1!='' && tel2!=''}">
		telefone(s) para contato  
		</c:if>
		
		<c:if test="${tel1!='' && tel2==''}">
		telefone como contato 
		</c:if>

		<c:if test="${tel1!=''}">
		<b>${tel1}</b>, 
		</c:if>

		<c:if test="${tel2!=''}">
		e <b>${tel2}</b>, 
		</c:if>
		
		vem requerer a Vossa Excelência <b>ISENÇÃO DE IMPOSTO 
		DE RENDA</b>, com base nas Leis n&ordm; 7.713/88 e 9.250/95, 
		regulamentadas pelo Decreto n&ordm; 3.000/99 e conforme disposto 
		na Instrução Normativa SRF n&ordm; 15/2001, bem como a aplicação 
		da <b>FORMA DIFERENCIADA DE CONTRIBUIÇÃO PREVIDENCIÁRIA</b>, prevista no 
		art. 40, § 21, da Constituição Federal, incluído pela Emenda Constitucional 
		nº 47/2005, em face da patologia que lhe acometeu.
		</p><br>
						
		</mod:valor>		
	
</mod:documento>
</mod:modelo>
