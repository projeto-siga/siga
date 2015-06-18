<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
CONSIGNAÇÃO DE ALUGUEL EM FOLHA DE PAGAMENTO  -->

<c:set var="esconderTexto" value="sim" scope="request" />
<c:set var="para" value="diretorForo" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
	
		<mod:grupo titulo="QUANTO A CONSIGNAÇÃO NA FOLHA DE PAGAMENTO">
		
			<mod:selecao titulo="Mês inicial do desconto"
			var="mes" opcoes="Janeiro;Fevereiro;Março;Abril;
			Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro" />
		</mod:grupo>
		

		<mod:grupo>
			<mod:oculto var="valor" valor="1,25" />
			<%--<mod:monetario titulo="Custos do processamento (R$)" var="valor" largura="10" extensoNum="sim" formataNum="sim" /><!--  1,25 era o valor em 2006-->--%>
		</mod:grupo>

	</mod:entrevista>
	
	<mod:documento>
		<mod:valor var="texto_requerimento">	
			<p style="TEXT-INDENT: 2cm" align="justify">
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, ${doc.subscritor.padraoReferenciaInvertido}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, vem requerer a Vossa Excelência <B> CONSIGNAÇÃO EM FOLHA DE PAGAMENTO</B> do valor correspondente à 
			locação do imóvel residencial, prevista na Resolução n.º 4/2008 do Conselho da Justiça Federal, conforme contrato em anexo.
			</p>
			<p style="TEXT-INDENT: 2cm" align="justify">
			Declara, ainda, estar ciente de que, nos termos do art. 137, II, da referida 
			Resolução, para cada consignação realizada 
			será cobrado do <i>consignatário</i>, a título de reposição de custos de processamento de dados de 
			consignações facultativas, o valor de <c:if test="${not empty valor}"> R$ ${valor} (${f:reaisPorExtenso(valor)}) </c:if>. 
			</p>
			<p style="TEXT-INDENT: 2cm" align="justify"> 
			Requer, outrossim, que o valor seja descontado <B>a partir do mês de ${mes},</B> 
			ou no mês subseqüente, caso não seja possível efetuá-lo no mês indicado, 
			devido ao cumprimento das datas de encerramento da folha de pagamento.
		</p>
		</mod:valor>
		<mod:valor var="texto_requerimento4">
			<br><br>			
			<p style="TEXT-INDENT: 2cm" align="justify"><b><i>Anexar:</i></b></p>
			<table border="0" bgcolor="#FFFFFF" cellspacing="2" width="100%">
				<tr>
					<td width="15%"></td>
					<td width="85%"><p style="TEXT-INDENT: 0cm" align="justify">- <i>cópias autenticadas do RG e CPF do Consignatário;</i></p></td>
				</tr>
				<tr>
					<td width="15%"></td>
					<td width="85%"><p style="TEXT-INDENT: 0cm;" align="justify">
						- <i>cópia autenticada ou conferida com o original do contrato de locação (que deverá conter cláusula expressa de que a Administração não intervém como fiadora ou garantidora do cumprimento de quaisquer obrigações contratuais dele decorrentes).
						</i></p>
					</td>
				</tr>	
			</table>
		</mod:valor>	
</mod:documento>
</mod:modelo>
