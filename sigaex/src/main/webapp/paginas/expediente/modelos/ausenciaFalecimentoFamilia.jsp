<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
REQUERIMENTO PARA AUSÊNCIA AO SERVIÇO EM RAZÃO DE FALECIMENTO EM FAMILIA 
A SABER: falecimento do cônjuge, companheiro, pais, madrasta ou padrasto, 
filhos, enteados, menor sob guarda ou tutela e irmãos. -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>

		<mod:grupo titulo="DETALHES DO FALECIDO">
			<mod:texto titulo="Nome do Parente Falecido" largura="60" var="nomeParenteFalecido"/>
		</mod:grupo>
		
		<mod:grupo>	
			<mod:selecao titulo="Grau de parentesco" var="grauParentesco"
				opcoes="Pai;Mãe;Padrasto;Madrasta;irmão;irmã;Filho;Filha;
				Enteado;Enteada;Cônjugue;Companheiro;Companheira;Menor sob Guarda ou Tutela" />
		</mod:grupo>
	</mod:entrevista>
	
	<mod:documento>		
		<mod:valor var="texto_requerimento">
			<p style="TEXT-INDENT: 2cm" align="justify">
				${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Senhoria <b>AUSÊNCIA AO SERVIÇO EM RAZÃO DO FALECIMENTO </b>
				de ${nomeParenteFalecido}, ${grauParentesco}, com base na alínea 
				 b, inciso III, art. 97 da Lei 8.112/90.
			</p>
		</mod:valor>	
</mod:documento>
</mod:modelo>
