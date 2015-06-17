<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
PRORROGAÇÃO DE EXERCÍCIO DA POSSE DE SERVIDOR RECEM EMPOSSADO -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretoraRH" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
	
	 <mod:grupo titulo="DETALHES DA POSSE">
			<mod:texto titulo="Nome do Servidor Empossado" largura="50" var="servidorEmpossado"/>
	<mod:grupo></mod:grupo>		
			<mod:texto titulo="Cargo Empossado" largura="40" var="cargoEmpossado"/>
	 </mod:grupo>
	 <mod:grupo titulo="DETALHES DO REGISTRO DO SERVIDOR">			
			<mod:texto titulo="Número do Ato" var="atoNum"/>
			<mod:data titulo="Data do Ato" var="atoData"/>	
	 <mod:grupo></mod:grupo>
			<mod:data titulo="Data da Publicação no Diario Oficial da União" var="dataPublicacaoDOU"/>			
	 </mod:grupo>
		
	</mod:entrevista>
	
	<mod:documento>
	
		<mod:valor var="texto_requerimento">
		<!-- 
		
			NOTA:
				Não tenho certeza se devemos apontar a recuperacao dos dados 
				do servidor (ou seja funcionario) da base de dados no BD, pois será 
				q a informação desejada ja persiste? Mas ele ele não é um funcionario
				recem empossado?
				
				Para o sistema recuperar informações da base de dados sobre o funcionario,
				neste caso deveriamos saber como funciona os tramites de cadastro na seção,
				pois será q seu cadastro é feito de imediato? - Sera q ele ja
				CONSTA no sistema???? ou após alguns dias? - NÃO SABEMOS, E POR isso 
				entendi	q EMBORA FUNCIONARIO, AINDA NAO CONSTA NO SISTEMA, por isso nao
				seu nome não aparecerá no formulario e consequentemente, ALGUEM DEVERA 
				digitar seu nome em um campo texto na tela.
										
			1)	AS LINHAS DE BAIXO REPRESENTAM 
			AS LINHAS DE CODIGO Q SERIAM USADAS
			SE O FUNCIONARIO JA ESTIVESSE 
			REGISTRADO NO SISTEMA, USE-AS 
			SE TIVER CERTEZA DISSO, POIS O SISTEMA 
			BUSCA A INFORMACAO DAS CLASSES.
												 		
				<B>${doc.subscritor.descricao}</B>, nomeado(a) pelo Ato nº ${atoNum}, de ${atoData}, 
				publicado no Diário Oficial da União - Seção 2 de ${dataPublicacaoDOU}, 
				para o cargo de ${subscritor.cargo.nomeCargo}, tendo, nesta data, 
				assinado o termo de posse,  vem requerer a V.Sª, nos termos do 
				<b>art. 15 da Lei n.º 8.112/90, com a redação dada pela Lei 
				n.º 9.527/97, <b>a prorrogação do exercício pelo 
				prazo de 15 (quinze) dias</b>.
				

			2)	AS LINHAS DE BAIXO REPRESENTAM 
			AS LINHAS DE CODIGO ATUALMENTE USADAS
			POIS REPRESENTAM Q ELE NAO ESTA REGISTRADO E
			NAO HA COMO RECUPERAR DADOS NO SISTEMA
			POR ISSO NAO USAMOS AS CLASSES DOS BD'S.
		-->		
		
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		${servidorEmpossado}, nomeado(a) pelo Ato nº ${atoNum}, de ${atoData}, 
		publicado no Diário Oficial da União - Seção 2 de ${dataPublicacaoDOU}, 
		para o cargo de ${cargoEmpossado}, tendo, nesta data, 
		assinado o termo de posse,  vem requerer a V.Sª, nos termos do 
		art. 15 da Lei n.º 8.112/90, com a redação dada pela Lei 
		n.º 9.527/97, a <b>PRORROGAÇÃO DO EXERCÍCIO</b> pelo 
		prazo de 15 (quinze) dias.
		</p>
		</mod:valor>
	
</mod:documento>
</mod:modelo>
