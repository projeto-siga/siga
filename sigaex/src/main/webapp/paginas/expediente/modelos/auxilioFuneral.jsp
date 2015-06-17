<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
REQUERIMENTO PARA INDENIZAÇÃO A AUXILIO-FUNERAL
DE SERVIDOR PÚBLICO -->
<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		
			<mod:grupo titulo="DETALHES DO FUNCIONARIO FALECIDO">
				<mod:texto titulo="Nome do Servidor Falecido" largura="50" var="ServidorFalecido"/>
			</mod:grupo>
			
			<mod:grupo titulo="DETALHES DO REQUERENTE BENEFICIADO">
				<mod:texto titulo="Nome do Beneficiado" largura="50" var="NomeBeneficiado"/>
			</mod:grupo>
			<mod:grupo>	
				<mod:texto titulo="Nacionalidade" var="Nacionalidade"/>
			</mod:grupo>
			<mod:grupo>	
				<mod:texto titulo="Identidade" var="Identidade" />
				<mod:data titulo="Data de Expedição" var="DataExpedicaoIdentidade" />
				<mod:texto titulo="CPF" var="Cpf"/>
			</mod:grupo>	
			<mod:grupo titulo="DETALHES DE DEPÓSITO DO BENEFÍCIO">	
				<mod:texto titulo="Banco" var="Banco"/>
			    <mod:texto titulo="Agencia" var="Agencia" />
				<mod:texto titulo="Conta Corrente" var="ContaCorrente" />
			</mod:grupo>
	
		</mod:entrevista>
	
	<mod:documento>
		<mod:valor var="texto_requerimento">
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		${NomeBeneficiado}, de Nacionalidade ${Nacionalidade}, identidade nº ${Identidade}, 
		expedida em ${DataExpedicaoIdentidade}, CPF nº ${Cpf}, 
		Conta-Corrente nº ${ContaCorrente}, Agência nº ${Agencia} e Banco ${Banco},
		
		vem requerer a Vossa Senhoria, com base no art.
	    226 e parágrafos da Lei 8.112/90, <b>AUXÍLIO-FUNERAL</b> em virtude do 
	    falecimento do(a) servidor(a) ${ServidorFalecido}.
	    </p>
	    <br>
	    <p style="TEXT-INDENT: 2cm" align="justify">
        Na oportunidade, anexa ao presente comprovante das despesas realizadas.
 		</p>
 		</mod:valor> 		
		
	</mod:documento>
</mod:modelo>
