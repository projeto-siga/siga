<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>	
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/memorando.jsp">
	<mod:entrevista>
		<mod:grupo>
			<mod:radio titulo="Unidade/Assistente que <b>remeterá</b> processos eletrônicos." 
						var="escolha" 	valor="1" reler="sim"  marcado="Sim"/>
			<mod:radio titulo="Unidade/Assistente que <b>receberá</b> processos eletrônicos." 
						var="escolha" valor="2"  reler="sim" gerarHidden="Não"/>
		</mod:grupo>		
		<c:set var="valorEscolha" value="${escolha}" />	
		<c:if test="${empty valorEscolha}">			
			<c:set var="valorEscolha" value="${param['escolha']}" />
		</c:if>		
		
		<mod:grupo>
			<mod:numero titulo="Ordem de Serviço" var="numODF" largura="5"/>
			<mod:numero titulo="Ano" var="anoODF" largura="4" maxcaracteres="4"/>			
		</mod:grupo>		
		<c:choose>			
			<c:when test="${empty valorEscolha or valorEscolha == '1'}">
				<mod:grupo>				
					<mod:numero titulo="Quantidade de processos a serem remetidos" var="qtdProcessos" largura="10" />
				</mod:grupo>
				<mod:oculto var="titUnid" valor="Unidade/Assistente que elaborará os cálculos" />										 
			</c:when>
			<c:when test="${valorEscolha == '2'}">		
				<mod:grupo>				
					<mod:numero titulo="Quantidade de processos a serem recebidos e calculados" var="qtdProcessos" largura="10" />
				</mod:grupo>
				<mod:oculto var="titUnid" valor="Unidade/Assistente que remeterá os processos" />							 
			</c:when>
		</c:choose>
		 
		<mod:grupo>			   
			<mod:selecao titulo="${titUnid}" var="unidade"
						opcoes="Seção de Contadoria de Niterói;
							Seção de Contadoria de São João de Meriti;
							Seção de Contadoria de Volta Redonda;
							Seção de Contadoria de São Gonçalo;
							Assistente IV - Contadoria de Duque de Caxias;
							Assistente IV - Contadoria de Nova Iguaçu;
							Assistente IV - Contadoria de Campos;
							Assistente IV - Contadoria de Nova Friburgo;
							Assistente IV - Contadoria de Petrópolis;
							Assistente IV - Contadoria de Resende;
							Assistente III - Contadoria de Angra dos Reis;
							Assistente III - Contadoria de Barra do Piraí;
							Assistente III - Contadoria de Itaboraí;
							Assistente III - Contadoria de Itaperuna;
							Assistente III - Contadoria de Macaé;
							Assistente III - Contadoria de Magé;
							Assistente III - Contadoria de São Pedro D'Aldeia;
							Assistente III - Contadoria de Teresópolis;
							Assistente III - Contadoria de Três Rios;
							SECEL - Sede;
							SECLJ - Sede;
							SECPC - Sede;
							SECPJ - Sede;
							SECPV - Sede" />
		</mod:grupo>
		<mod:grupo>				
				<mod:selecao titulo="Mês" var="mes" 
 						opcoes="Janeiro;Fevereiro;Março;Abril;Maio;Junho;Julho;Agosto;Setembro;Outubro;Novembro;Dezembro"/>
							
		</mod:grupo>		
	</mod:entrevista>
	<mod:documento>		
	<mod:valor var="texto_memorando">
	Ref.: Central de Cálculo Judicial - RJ-ODF-${anoODF}/${numODF}
	<br><br>		
	<c:choose>			
			<c:when test="${empty escolha or escolha == '1'}">
				<p style="align:left;TEXT-INDENT:2cm">Comunico sobre a <b>remessa</b> de processos eletrônicos, 
				conforme informações abaixo: </p>
				<p style="align:left;TEXT-INDENT:2cm">a)	Unidade/Assistente destinatária(o): ${unidade}</p>
				<p style="align:left;TEXT-INDENT:2cm">b)	quantidade de processos: ${qtdProcessos}</p>
				<p style="align:left;TEXT-INDENT:2cm">c)	mês de referência: ${mes}</p>
				<p style="align:left;TEXT-INDENT:2cm">A remessa deverá ser feita <b>diretamente</b> à Unidade/Assistente destinatária(o), 
				no prazo máximo de <b>5 dias a contar da data do recebimento deste memorando</b>.</p>		
																 
			</c:when>
			<c:when test="${escolha == '2'}">	
				<p style="align:left;TEXT-INDENT:2cm">Comunico sobre o <b>recebimento</b> de processos eletrônicos 
				para a elaboração de cálculos, conforme informações abaixo: </p>
				<p style="align:left;TEXT-INDENT:2cm">a)	Unidade/Assistente que remeterá os processos: ${unidade}</p>
				<p style="align:left;TEXT-INDENT:2cm">b)	quantidade de processos: ${qtdProcessos}</p>
				<p style="align:left;TEXT-INDENT:2cm">c)	mês de referência: ${mes}</p>
				<p style="align:left;TEXT-INDENT:2cm">A devolução dos processos (com os cálculos elaborados) deverá 
				ser feita <b>diretamente ao juízo de origem</b>, no prazo máximo de <b>30 dias a contar da data do 
				recebimento dos processos</b>.</p>								 
			</c:when>
		</c:choose>	
		<br><br><br>	
	</mod:valor>		
	</mod:documento>
</mod:modelo>
