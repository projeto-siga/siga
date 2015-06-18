<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
CONSIGNAÇÃO DE ALUGUEL EM FOLHA DE PAGAMENTO - CANCELAMENTO -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
	
		<mod:selecao titulo="Mês de Cancelamento"
		var="mes" opcoes="janeiro;fevereiro;março;abril;
		maio;junho;julho;agosto;setembro;outubro;novembro;dezembro" />

	</mod:entrevista>
	
	<mod:documento>
	
			<mod:valor var="texto_requerimento">		
			<p style="TEXT-INDENT: 2cm" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Senhoria o 
			<B>CANCELAMENTO DA CONSIGNAÇÃO EM FOLHA DE PAGAMENTO</B> 
			do valor correspondente à locação do imóvel residencial, 
			prevista na Resolução n.º 4/2008 do Conselho 
			da Justiça Federal, <B>a partir do mês de ${mes}</B>.</p>
			</mod:valor>
			<mod:valor var="texto_requerimento4">
			<br><br>			
			<p align="center" >Ciente.</p><br>
			<p align="center" >________________________________________________________</p>
			<p align="center" >Assinatura do Locador/Representante Legal</p>
			<p align="center" >(Reconhecer firma em caso de pessoa física)</p>
			</mod:valor>
			
			
</mod:documento>
</mod:modelo>
