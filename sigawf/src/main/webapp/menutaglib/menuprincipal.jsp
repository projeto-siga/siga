<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="l"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Menu</title>



<!--|**START IMENUS**|imenus0,inline-->
<!--[if IE]><style type="text/css">.imcm .imea span{position:absolute;}.imcm .imclear,.imclear{display:none;}.imcm{zoom:1;} .imcm li{curosr:hand;} .imcm ul{zoom:1}.imcm a{zoom:1;}</style><![endif]-->
<!--[if gte IE 7]><style type="text/css">.imcm .imsubc{background-image:url(ie_css_fix);}</style><![endif]-->

<!--|**START IMENUS**|imenus1,inline-->
<!--[if IE]><style type="text/css">.imcm .imea span{position:absolute;}.imcm .imclear,.imclear{display:none;}.imcm{zoom:1;} .imcm li{curosr:hand;} .imcm ul{zoom:1}.imcm a{zoom:1;}</style><![endif]-->
<!--[if gte IE 7]><style type="text/css">.imcm .imsubc{background-image:url(ie_css_fix);}</style><![endif]-->

<link href="${pageContext.request.contextPath}/sigalibs/menu.css"
	rel="stylesheet" type="text/css" />
</head>

<body style="padding: 0px 0px 0px 0px; margin: 0px 0px 0px 0px;">
<c:import url="/sigalibs/cabecalho.jsp"></c:import>
<div style="width: 100%; background-color: #254189">
<div
	style="z-index:999999;padding: 0px 0px 0px 0px;
margin: 0px 0px 0px 0px;float: left">
<div class="imrcmain0 imgl"
	style="width:186px;z-index:999999;position:relative;float: left">
<div class="imcm imde" id="imouter0" style="float:left">
<ul id="imenus0">
	<li class="imatm" style="width:186px;"><a class="" href="#"> <span
		class="imea imeam"><span></span></span>SIGA</a>
	<div class="imsc">
	<div class="imsubc" style="width:186px;top:0px;left:0px;">
	<ul style="">
		<li><ww:url id="url" action="principal" namespace="/" /> <ww:a
			href="%{url}">Página Inicial</ww:a></li>
		<li><a href="#"> <span class="imea imeas"><span></span></span>Administração</a>
		<div class="imsc">
		<div class="imsubc" style="width:155px;top:-23px;left:139px;">
		<ul style="">
			<li><a href="/autenticador/usuario.jf?acao=aListar&exibir=troca">Trocar
			senha</a></li>
			<li><ww:url id="url" action="substituir"
				namespace="/substituicao" /><ww:a href="%{url}">Entrar como substituto</ww:a></li>
			<c:if
				test="${(not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao) ||(not empty titular && titular.idPessoa!=cadastrante.idPessoa)}">
				<li><ww:url id="url" action="finalizar"
					namespace="/substituicao" /> <ww:a href="%{url}">
					Finalizar substituição de 
					<c:choose>
						<c:when
							test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}">${titular.nomePessoa}</c:when>
						<c:otherwise>${lotaTitular.sigla}</c:otherwise>
					</c:choose>
				</ww:a></li>
			</c:if>
			<li><ww:url id="url" action="listar" namespace="/substituicao" /><ww:a
				href="%{url}">Gerenciar possíveis substitutos</ww:a></li>
		</ul>
		</div>
		</div>
		</li>
		<li><a target="_blank"
			href="/wiki/Wiki.jsp?page=${l:removeAcento(titulo)}">Ajuda</a></li>
		<li><ww:url id="url" action="logoff" namespace="/" /> <ww:a
			href="%{url}">Logoff</ww:a></li>
		<%--
								<li><a href="#">
									<span class="imea imeas"><span></span></span>Gest&atilde;o Documental</a>
									<div class="imsc">
										<div class="imsubc" style="width:155px;top:-23px;left:139px;">
											<ul style="">
												<li><a href="#">Expedientes</a></li>
												<li><a href="#">Malotes</a></li>
												<li><a href="#">Processos Administrativos</a></li>
												<li><a href="#">Correspondencia</a></li>
												<li><a href="#">Boletim Interno</a></li>
												<li><a href="#">Arquivamento</a></li>
											</ul>
										</div>
									</div>
								</li>
								<li><a href="#">
									<span class="imea imeas"><span></span></span>Gest&atilde;o do Trabalho</a>
									<div class="imsc">
										<div class="imsubc" style="width:155px;top:-23px;left:139px;">
											<ul style="">
												<li><a href="#">Servi&ccedil;os</a></li>
												<li><a href="#">Projetos</a></li>
												<li><a href="#">Manuten&ccedil;&otilde;es Preventivas</a></li>
												<li><a href="#">Reprografia</a></li>
												<li><a href="#">Liga&ccedil;&otilde;es Telef&ocirc;nicas</a></li>
												<li><a href="#">Ve&iacute;culos</a></li>
											</ul>
										</div>
									</div>
								</li>
								<li><a href="#">
									<span class="imea imeas"><span></span></span>Recursos Humanos</a>
									<div class="imsc">
										<div class="imsubc" style="width:155px;top:-23px;left:139px;">
											<ul style="">
												<li><a href="/SigaRH/Cadastro.action">Cadastro</a></li>
												<li><a href="/SigaRH/beneficios/index.jsp">Benef&iacute;cios</a></li>
												<li><a href="#">Treinamento</a></li>
												<li><a href="#">&Oacute;rg&atilde;os Externos</a></li>
												<li><a href="#">Avalia&ccedil;&atilde;o de Desempenho</a></li>
											</ul>
										</div>
									</div>
								</li>
								<li><a href="#">
									<span class="imea imeas"><span></span></span>Gest&atilde;o Patrimonial</a>
									<div class="imsc">
										<div class="imsubc" style="width:155px;top:-23px;left:139px;">
											<ul style="">
												<li><a href="#">Aquisi&ccedil;&otilde;es</a></li>
												<li><a href="#">Contratos</a></li>
												<li><a href="#">Patrim&ocirc;nio</a></li>
												<li><a href="#">Biblioteca</a></li>
												<li><a href="#">Estoque Local</a></li>
											</ul>
										</div>
									</div>
								</li>
								<li><a href="#">
									<span class="imea imeas"><span></span></span>Gest&atilde;o Institucional</a>
									<div class="imsc">
										<div class="imsubc" style="width:155px;top:-23px;left:139px;">
											<ul style="">
												<li><a href="#">Conhecimento</a></li>
												<li><a href="#">Apoio Gerencial</a></li>
											</ul>
										</div>
									</div>
								</li>
								<li><a href="#">
									<span class="imea imeas"><span></span></span>Administra&ccedil;&atilde;o</a>
									<div class="imsc">
										<div class="imsubc" style="width:155px;top:-23px;left:139px;">
											<ul style="">
												<li><a href="#">Acesso</a></li>
												<li><a href="#">Prefer&ecirc;ncias Pessoais</a></li>
												<li><a href="#">Colabora&ccedil;&atilde;o</a></li>
												<li><a href="#">Qualidade</a></li>
												<li><a href="#">Tabelas Institucionais</a></li>
												<li><a href="#">Manuten&ccedil;&atilde;o do Sistema</a></li>
												<li><a href="#">Ger&ecirc;ncia de Mensagens</a></li>
												<li><a href="#">Cadastro da Institui&ccedil;&atilde;o</a></li>
												<li><a href="#">Apoio ao Usu&aacute;rio</a></li>
											</ul>
										</div>
									</div>
								</li>
--%>
	</ul>
	</div>
	</div>
	</li>
</ul>
</div>
</div>
<div class="imclear"></div>
</div>

<c:if test="${not empty menu}">
	<c:import url="/paginas/menus/${menu}.jsp"></c:import>
</c:if>

<div class="imclear"></div>
</div>

<c:if test="${not empty barranav}">
	<c:if test="${barranav!='nao'}">
		<c:import url="/sigalibs/barranav.jsp"></c:import>
	</c:if>
</c:if>

<c:import url="/sigalibs/staticjs.jsp"></c:import>

</body>

</html>
