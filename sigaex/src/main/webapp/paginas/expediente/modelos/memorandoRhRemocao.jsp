<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/memorando.jsp">
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
		<mod:grupo>
			<b> <mod:mensagem titulo="Atenção"
				texto="preencha o destinatário com SELOT e, após finalizar, transfira para a SELOT." />
			</b>
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<mod:valor var="texto_memorando"><p style="TEXT-INDENT: 2cm" align="justify">Solicito a <b>remoção</b>
			do(a) servidor(a)<mod:identificacao pessoa="${requestScope['servidor_pessoaSel.id']}" negrito="sim" nivelHierarquicoMaximoDaLotacao="4" />
			para a <b>${f:lotacao(requestScope['lotacao_lotacaoSel.id']).descricao}</b>,
			a partir de <b>${dataInicio}</b>.</p>
			</p>	
		</mod:valor>
		</mod:documento>
</mod:modelo>

