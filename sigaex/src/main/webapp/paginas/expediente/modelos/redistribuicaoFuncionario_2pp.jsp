<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
	
		<%--<mod:grupo titulo="DETALHES DO FUNCIONÁRIO">
				<mod:texto titulo="Classe" var="classe"/>
				<mod:texto titulo="Padrão" var="padrao" />
		</mod:grupo>--%>
		
		<mod:grupo titulo="DETALHES DA REDISTRIBUIÇÃO">
				<FONT COLOR="BLUE"><B>
				<mod:memo colunas="60" linhas="4"  titulo="Motivo" var="motivo"/>
				</FONT></B>
		</mod:grupo>
				
	</mod:entrevista>
		
	<mod:documento>
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head></head>
	<body>
		
		<c:import url="/paginas/expediente/modelos/inc_tit_juizfedDirForo.jsp" />
		
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
		classe ${doc.subscritor.padraoReferenciaInvertido}, 
		lotado(a) no(a) ${doc.subscritor.lotacao.descricao},
	    vem requerer a Vossa Excelência, que se digne encaminhar 
	    o requerimento de <b>REDISTRIBUIÇÃO</b>, em anexo, ao E. Tribunal Regional 
	    Federal da 2ª Região.
	    </p>
	    
	    
		<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
				
		<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
		
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		
		<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
	
		<c:import url="/paginas/expediente/modelos/inc_tit_presidTrf2aRegi.jsp" />


		<p style="TEXT-INDENT: 2cm" align="justify">
		${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, 
		classe ${doc.subscritor.padraoReferenciaInvertido}, 
		lotado(a) no(a) ${doc.subscritor.lotacao.descricao},
		vem requerer a Vossa Excelência, nos termos do art. 37 da Lei n.º 8.112/90, 
		c/c a redação dada pela Lei n.º 9.527/97, <B>REDISTRIBUIÇÃO</B>, 
		pelos motivos expostos a seguir:
		</p>
		<p style="TEXT-INDENT: 2cm" align="justify">
		<i>${motivo}</i>
		</p>
		
		<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />
					
		<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
		
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
			

	</body>
	</html>
</mod:documento>
</mod:modelo>
