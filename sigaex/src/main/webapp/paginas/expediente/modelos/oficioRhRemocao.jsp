<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import
	url="/paginas/expediente/modelos/rj_inc_dad_juizFedDirForo.jsp" />

<mod:modelo urlBase="/paginas/expediente/modelos/oficio.jsp">
	<mod:entrevista>
		<mod:grupo>
			<mod:pessoa titulo="Servidor" var="servidor" />
		</mod:grupo>
		<mod:grupo>
			<mod:lotacao titulo="Próxima lotação" var="lotacao" />
		</mod:grupo>
		<mod:grupo>
			<mod:data titulo="A partir de" var="dataInicio" />
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<mod:valor var="texto_oficio">
			<p style="TEXT-INDENT: 2cm" align="justify">Solicito a Vossa
			Excelência a <b>remoção</b> do(a) servidor(a)
			<mod:identificacao pessoa="${requestScope['servidor_pessoaSel.id']}" />
			para a <b>${requestScope['lotacao_lotacaoSel.descricao']}</b>,
			a partir de <b>${dataInicio}</b>.</p>
		</mod:valor>
	</mod:documento>
</mod:modelo>

