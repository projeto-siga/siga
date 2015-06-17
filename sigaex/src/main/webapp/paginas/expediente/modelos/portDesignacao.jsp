<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo urlBase="/paginas/expediente/modelos/portaria.jsp">
	<mod:entrevista>
		<mod:grupo>
			<mod:texto titulo="Nº da solicitação" var="solicitacao" largura="35" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Nome do responsável" var="responsavel"
				largura="40" />
		</mod:grupo>
		<mod:grupo>
			<mod:pessoa titulo="Servidor" var="servidor" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Observações sobre o servidor" var="servidorObs" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Função" var="funcaoServidor" largura="40" />
		</mod:grupo>
		<mod:grupo>
			<mod:data titulo="A partir de" var="dataInicio" />
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<mod:valor var="texto_portaria">
			<!-- INICIO ABERTURA --><p style="TEXT-INDENT: 2cm" align="justify"><b>O JUIZ FEDERAL
			- DIRETOR DO FORO E CORREGEDOR PERMANENTE DOS SERVIÇOS AUXILIARES DA
			JUSTIÇA FEDERAL DE 1&ordm; GRAU - <c:choose><c:when test="${not empty doc.subscritor.descricao}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when><c:otherwise>SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO</c:otherwise></c:choose></b>, no uso de suas atribuições legais, e tendo em vista o
			disposto no(a) ${solicitacao}, do(a) ${responsavel},
			</p>

			<p style="TEXT-INDENT: 2cm" align="justify"><b>RESOLVE:</b></p><!-- FIM ABERTURA -->

			<!-- INICIO CORPO --><p style="TEXT-INDENT: 2cm" align="justify"><b>DESIGNAR</b> o(a)
			servidor(a) <mod:identificacao
				pessoa="${requestScope['servidor_pessoaSel.id']}"
				nivelHierarquicoMaximoDaLotacao="4" obs="${servidorObs}" negrito="sim" /> para exercer a
			função comissionada de ${funcaoServidor}<c:choose><c:when test="${not empty dataInicio}">, a partir de ${dataInicio}.</c:when><c:otherwise>.</c:otherwise></c:choose>
			</p><!-- FIM CORPO -->
		</mod:valor>
	</mod:documento>
</mod:modelo>

