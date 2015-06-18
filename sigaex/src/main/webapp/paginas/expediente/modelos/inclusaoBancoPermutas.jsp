<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--<c:set var="textoPrimeiroRodape" scope="request">
<span style="text-align: justify;">A desistência da inclusão no Banco de Permutas deverá ser comunicada à Subsecretaria de Gestão de Pessoas por meio de requerimento.</span>
</c:set>--%>
<script language="javascript">
var newwin = null;
</script>
<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		<mod:grupo titulo="DETALHES DO SERVIDOR">
			<mod:grupo><mod:texto titulo="Formação (informar o grau de escolaridade e o curso, conforme registrado nos assentamentos funcionais)" var="formacao" largura="60"/></mod:grupo>
			<mod:grupo><mod:data titulo="Data do Exercício" var="dataExercicio" /></mod:grupo>
			<%--<mod:grupo><mod:texto titulo="Lotação Atual" var="lotaAtual" valor="${doc.subscritor.lotacao.nomeLotacao}" largura="60"/></mod:grupo>--%>
			<mod:grupo><mod:texto titulo="Lotação Desejada" var="lotaDesejada" largura="60"/></mod:grupo>
			<br/>
			<c:set var="conteudo11">
			O JUIZ FEDERAL - DIRETOR DO FORO E CORREGEDOR PERMANENTE DOS SERVIÇOS AUXILIARES DA JUSTIÇA FEDERAL - SEÇÃO JUDICIÁRIA DO RIO DE JANEIRO, no uso de suas atribuições legais e com o objetivo de tornar o processo de lotação e remoção mais célere, RESOLVE: <br/><br/>Estabelecer os seguintes critérios a serem observados pelos servidores interessados em integrar o Banco de Permutas da Seção Judiciária do Rio de Janeiro:<br/><br/> 
			</c:set>
			<c:set var="conteudo12">
			I- a inscrição do servidor implica a sua disponibilidade imediata para a realização da permuta pretendida. Caso o servidor não concorde em realizá-la à época em que surgir a possibilidade de atendimento, sua inscrição no Banco será excluída, automaticamente. Não haverá óbice para que o servidor faça uma nova inscrição, posteriormente.<br/><br/>
			</c:set>
			<c:set var="conteudo13">
			II- na hipótese de o servidor se inscrever para mais de uma unidade de lotação, quando houver o atendimento de uma delas, automaticamente, será excluído do Banco de Permutas, sem prejuízo para que proceda a uma nova inscrição.<br/><br/>
			</c:set>
			<c:set var="conteudo14">
			III- a movimentação dos servidores inscritos no Banco de Permutas dar-seá de acordo com a ordem cronológica dos pedidos na Subsecretaria de Recursos Humanos.<br/><br/>			
			</c:set>
			<c:set var="conteudo15">
			IV- Esta Portaria entra em vigor na data de sua publicação.<br/><br/>
			</c:set>
			<c:set var="conteudo2">
			Art. 12. Servidores ocupantes de cargos efetivos com especialidade somente poderão ser lotados em unidades organizacionais diretamente ligadas às atribuições dos cargos, exceto na hipótese de designação para função comissionada de chefia (FC-04 ou superior) ou para cargo em comissão de Direção ou Assessoramento.<br/>
			</c:set>			
			
			<c:set var="textoCiencia">
			<b>Estou ciente da 
			<span onmouseover="this.style.cursor='hand';" 
			onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste',null,'height=450,width=550,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${conteudo11}'); newwin.document.write('${conteudo12}'); newwin.document.write('${conteudo13}'); newwin.document.write('${conteudo14}'); newwin.document.write('${conteudo15}');"><u>Portaria nº 36/2008</u></span>, de 26/06/08, 
			do 
			<span onmouseover="this.style.cursor='hand';" onclick="javascript: if (newwin!=null) newwin.close(); newwin = window.open('teste2',null,'height=160,width=400,status=no,toolbar=no,menubar=no,location=no'); newwin.document.write('${conteudo2}');"><u>caput do art. 12 da portaria nº 50/2008</u></span>, 
			de 29/09/08 e de que a desistência da inclusão no Banco de Permutas deverá ser comunicada à Subsecretaria de Recursos Humanos por meio de requerimento.</b>
			</c:set>
			<mod:obrigatorios />
			<br/>
			<mod:caixaverif titulo="${textoCiencia}" var="ciente" obrigatorio="Sim"/>
		</mod:grupo>
	</mod:entrevista>

	<mod:documento>
		<mod:valor var="texto_requerimento">
			<p style="TEXT-INDENT: 2cm" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo},
			matricula ${doc.subscritor.matricula}, do Quadro de Pessoal Permanente 
			da Justiça Federal de Primeiro Grau no Rio de Janeiro, 
			com exercício a partir de ${dataExercicio}, lotado no(a) ${doc.subscritor.lotacao.descricao}, 
			com nível de escolaridade ${formacao}, vem requerer a Vossa Senhoria
			inclusão no Banco de Permutas, a fim de ser lotado(a) no(a) ${lotaDesejada}.</p>
			<p style="TEXT-INDENT: 2cm" align="justify">
			Está ciente da Portaria nº 36/2008, de 26/06/08, 
			do caput do art. 12 da Portaria nº 50/2008, de 29/08/08 e de que 
			a desistência da inclusão no Banco de Permutas deverá ser comunicada à 
			Subsecretaria de Recursos Humanos por meio de requerimento.
			</p>
		</mod:valor>
	</mod:documento>
</mod:modelo>
