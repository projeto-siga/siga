<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Dependentes">
			<mod:selecao titulo="Dependentes a incluir" var="dependentes" opcoes="1;2;3;4;5" reler="ajax" idAjax="dependAjax"/>
				<mod:grupo depende="dependAjax">
					<c:forEach var="i" begin="1" end="${dependentes}">
						<mod:grupo>
							<mod:texto titulo="${i}) Nome:" largura="40" var="nome${i}" />
							<mod:selecao titulo="Parentesco:" var="grauparentesco${i}" opcoes="Filha;Filho;Enteada;Enteado" />
							<mod:data titulo="Data Nascimento:" var="dataNasc${i}" />
						</mod:grupo>
					</c:forEach>
				</mod:grupo>
		</mod:grupo>
		<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande"/>
	</mod:entrevista>
	
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}"><c:set var="tl" value="7pt"/></c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}"><c:set var="tl" value="6pt"/></c:if>
		<c:if test="${tamanhoLetra=='Grande'}"><c:set var="tl" value="8pt"/></c:if>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head></head>
		<body>
		<mod:letra tamanho="${tl}">
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		
		<h3><b>RECADASTRAMENTO - AUXÍLIO PRÉ-ESCOLAR</b></h3>

		<h4><p><b>1 - DADOS DO MAGISTRADO/SERVIDOR BENEFICIÁRIO</b><p/></h4>
		<c:import url="/paginas/expediente/modelos/subscritor.jsp" />
		
		<h4><p><b>2 - DEPENDENTES</b><p/></h4>
		<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" width="50%"><p>NOME</p></td>
				<td bgcolor="#FFFFFF" width="25%"><p>GRAU DE PARENTESCO</p></td>
				<td bgcolor="#FFFFFF" width="25%"><p>DATA DE NASCIMENTO</p></td>
			</tr>
			<c:forEach var="i" begin="1" end="${dependentes}">
				<tr>
					<td bgcolor="#FFFFFF"><p>${requestScope[f:concat('nome',i)]}</p></td>
					<td bgcolor="#FFFFFF"><p>${requestScope[f:concat('grauparentesco',i)]}</p></td>
					<td bgcolor="#FFFFFF"><p>${requestScope[f:concat('dataNasc',i)]}</p></td>
				</tr>
			</c:forEach>
		</table>

		<h4><p><b>3 - DECLARAÇÃO</b></h4><br/>
			Declaro estar ciente dos termos da Resolução CJF n&ordm; 588, de 29/11/2007, que dispõe sobre o Auxílio Pré-Escolar, e de que o respectivo reembolso será realizado em 
		Folha de Pagamento, conforme apresentado abaixo:<br/>
		
			- O auxílio pré-escolar será devido a partir do mês em que for feita a inscrição do dependente, não sendo pagos valores relativos a meses anteriores;<br/>
			- O auxílio pré-escolar será pago a cada criança na faixa etária compreendida desde o nascimento até o mês de completar 6 anos de idade inclusive; ou quando ingressar no ensino fundamental;<br/>
			- O beneficiário é responsável por comunicar a administração qualquer situação que cause perda do benefício, pelas hipóteses do art. 14 da referida Resolução;<br/>
			- Declaro, ainda, que o menor(es) acima relacionado(s) não está(ão) cadastrado(s) como dependente(s), deste benefício, ou programa similar em outro órgão ou entidade da Administração Pública direta ou indireta.
		<p/>
		<p>
			Declaro, ainda, que preencho os requisitos previstos pelo art. 5&deg; da Resolução n&ordm; 588, de 29/11/2007.
		</p>
		<p>${doc.dtExtenso}
		</p>
		<p>
		Assinatura:_______________________________________________________________
		<p/>
		<br><br>
		<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF">
					<p>
						Tratando-se de dependentes excepcionais será considerada, como limite para o atendimento, a idade mental correspondente a 6 (seis) anos de idade, comprovada mediante laudo médico, homologado pela área competente do Órgão.
					</p>
					<br/><br/>
					<p>
						Servidores requisitados ou cedidos ou que exerçam mais de um cargo, devem apresentar declaração fornecida pelo outro órgão de que não usufruem benefício semelhante, informando o valor da remuneração percebida naquele órgão.
					</p>
				</td>
			</tr>
		</table>
		</mod:letra>
		</body>
		<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp"/>
		</html>
	</mod:documento>
</mod:modelo>
