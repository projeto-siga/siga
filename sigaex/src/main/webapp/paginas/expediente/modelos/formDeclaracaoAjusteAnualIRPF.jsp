<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>

		<mod:grupo titulo="">
			<mod:selecao titulo="Declaração de ajuste anual de IRPF referente à" var="periodo"
				opcoes="2012/2013;2013/2014;2014/2015;2015/2016;2016/2017;2017/2018;2018/2019;2019/2020" />
		</mod:grupo>
		
		<mod:grupo titulo="Caso não tenha declarado bens à Receita Federal, favor marcar uma das opções abaixo:">	
			<mod:radio titulo="Não se aplica." var="tipoFormulario" valor="1" reler="ajax" idAjax="bensAjax" />
			<mod:radio titulo="Declaro não possuir bens." var="tipoFormulario" valor="2" reler="ajax" idAjax="bensAjax"/>
			<mod:radio titulo="Declaro que possuo os seguintes bens:" var="tipoFormulario" valor="3" reler="ajax" idAjax="bensAjax" />
		</mod:grupo>
		
		<c:set var="valorTipoDeForm" value="${tipoFormulario}" />
		<c:if test="${empty valorTipoDeForm}">
			<c:set var="valorTipoDeForm" value="${param['tipoFormulario']}" />
		</c:if>
		
		
		<mod:grupo  depende="bensAjax">
		  <c:if test="${valorTipoDeForm == 3}">
		  	<mod:memo colunas="80" linhas="6" titulo="Declaração de bens e direitos" var="bens" />
		  </c:if>
		</mod:grupo>  	
		 
						
		<mod:grupo titulo="">
		<br /><b>A Declaração de IRPF e o Recibo de Entrega à Receita Federal deverão ser encaminhados, 
		DIGITALMENTE, em formato PDF, pelo SIGA-DOC, por meio do campo 'anexar arquivo': <br />
		a) no caso de não possuir token, digitalizar a cópia da Declaração e a do respectivo 
		Recibo (assinado e datado);<br />
		b) no caso de possuir certificação (token ou smartcard), anexá-los após gerar imagem PDF
		 por meio do programa da Receita Federal (não é necessário &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;imprimi-los). É obrigatória a assinatura digital dos arquivos.</b>
		</mod:grupo>
		
	</mod:entrevista>
	<mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
		</style>
		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF">
			<tr>
				<td>
					<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
				</td>
			</tr>
		</table>
	
		FIM PRIMEIRO CABECALHO -->
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda2.jsp" />
		FIM CABECALHO -->

			<br />
			<p style="font-family:Arial;font-size:10pt;">Sr.(a) Supervisor(a) da Seção de Cadastro</p>
			<br />
			<p style="text-align: justify; line-height: 150%; font-family: Arial; font-size: 10pt;">${doc.subscritor.descricao}, matrícula nº RJ${doc.subscritor.matricula},
				 ${doc.subscritor.cargo.nomeCargo}, lotado(a) no(a) ${doc.subscritor.lotacao.descricao}, encaminha, 
				 em anexo, o arquivo digital da Declaração de Ajuste Anual do IRPF, referente à 
				 <c:out value="${periodo}"/>, e o respectivo Recibo de Entrega à Receita Federal.
			</p>
			<c:if test="${not empty tipoFormulario and tipoFormulario != 1}">
				<p style="font-family:Arial;font-size:10pt;">Declara, ainda, que 
					<c:choose>
						<c:when test="${tipoFormulario == 2}">não possui bens.</c:when>
						<c:otherwise>possui os seguintes bens: <p style="text-align: justify; line-height: 150%; font-family: Arial; font-size: 10pt;"><siga:fixcrlf><c:out value="${bens}"/></siga:fixcrlf></p></c:otherwise>
					</c:choose>
				</p>
			</c:if>
			<br /><br />
			<p style="font-family: Arial; font-size: 10pt; text-align: center;">${doc.dtExtenso}</p>
			<br />

	<p> </p> <p> </p>
			
			
					<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
					

		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental2.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita2.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>
