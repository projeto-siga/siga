<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<div
	style="position: relative; z-index: 999998; padding: 0px 0px 0px 0px; margin: 0px 0px 0px 0px; float: left;">
<div class="imrcmain1 imgl"
	style="width: 100%; z-index: 999998; position: relative; float: left;">
<div class="imcm imde" id="imouter1" style="float: left">
<ul id="imenus1">

	<li class="imatm" style="width: 146px;"><a href="#"> <span
		class="imea imeam"><span></span></span>Documentos</a>
	<div class="imsc">
	<div class="imsubc" style="width: 146px; top: 0px; left: 0px;">
	<ul style="">
		<li><ww:url id="url" action="editar" namespace="/expediente/doc" />
		<ww:a href="%{url}">Novo</ww:a></li>
		<li><ww:url id="url" action="listar" namespace="/expediente/doc">
			<ww:param name="primeiraVez">sim</ww:param>
		</ww:url> <ww:a href="%{url}">Pesquisar</ww:a></li>
		<li><ww:url id="url" action="full_search"
			namespace="/expediente/doc">
		</ww:url> <ww:a href="%{url}">Pesquisar por texto</ww:a></li>

		<li><ww:url id="url" action="transferir_lote"
			namespace="/expediente/mov" /> <siga:monolink href="%{url}"
			texto="Transferir em lote" /></li>
		<li><ww:url id="url" action="receber_lote"
			namespace="/expediente/mov" /> <siga:monolink href="%{url}"
			texto="Receber em lote" /></li>
		<li><ww:url id="url" action="arquivar_lote"
			namespace="/expediente/mov" /> <siga:monolink href="%{url}"
			texto="Arquivar em lote" /></li>
		<li><ww:url id="url" action="anotar_lote"
			namespace="/expediente/mov" /> <siga:monolink href="%{url}"
			texto="Anotar em lote" /></li>
		<c:catch>
			<c:if
				test="${f:testaCompetencia('atenderPedidoPublicacao',titular,lotaTitular,null)}">
				<li><ww:url id="url" action="atender_pedido_publicacao"
					namespace="/expediente/mov" /><ww:a href="%{url}">Gerenciar Publicação DJE</ww:a></li>
			</c:if>
		</c:catch>
		<%--<c:catch>
			<c:if
				test="${f:testaCompetencia('definirPublicadoresPorConfiguracao',titular,lotaTitular,null)}">
				<li><ww:url id="url" action="definir_publicadores"
					namespace="/expediente/configuracao" /><ww:a href="%{url}">Definir Publicadores DJE</ww:a></li>
			</c:if>
		</c:catch>--%>
		<c:catch>
			<c:if
				test="${f:testaCompetencia('gerenciarPublicacaoBoletimPorConfiguracao',titular,lotaTitular,null)}">
				<li><ww:url id="url" action="gerenciar_publicacao_boletim"
					namespace="/expediente/configuracao" /><ww:a href="%{url}">Definir Publicadores Boletim</ww:a></li>
			</c:if>
		</c:catch>
	</ul>
	</div>
	</div>
	</li>

	<li class="imatm" style="width: 146px;"><a href="#"> <span
		class="imea imeam"><span></span></span>Ferramentas</a>
	<div class="imsc">
	<div class="imsubc" style="width: 146px; top: 0px; left: 0px;">
	<ul style="">
		<li><ww:url id="url" action="listar"
			namespace="/modelo" /> <ww:a href="%{url}">Cadastro de modelos</ww:a></li>
		<li><ww:url id="url" action="listar"
			namespace="/despacho/tipodespacho" /> <ww:a href="%{url}">Cadastro de tipos de despacho</ww:a></li>
	</ul>
	</div>
	</div>
	</li>

	<li class="imatm" style="width: 176px;"><a href="#"> <span
		class="imea imeam"><span></span></span>Relatórios</a>
	<div class="imsc">
	<div class="imsubc" style="width: 176px; top: 0px; left: 0px;">
	<ul style="">
		<li><ww:url id="url" action="relRelatorios"
			namespace="/expediente/rel">
			<ww:param name="nomeArquivoRel">relFormularios.jsp</ww:param>
		</ww:url> <ww:a href="%{url}">Relação de formulários</ww:a></li>

		<%-- Substituído pelo pelo "relConsultaDocEntreDatas"
	<li><ww:url id="url" action="relRelatorios"
			namespace="/expediente/rel">
			<ww:param name="nomeArquivoRel">relExpedientes.jsp</ww:param>
		</ww:url> <ww:a href="%{url}">Relatório de Expedientes</ww:a></li>  --%>


		<li><ww:url id="url" action="relRelatorios"
			namespace="/expediente/rel">
			<ww:param name="nomeArquivoRel">relConsultaDocEntreDatas.jsp</ww:param>
		</ww:url> <ww:a href="%{url}">Relação de documentos entre datas</ww:a></li>

		<!-- 
		<li><ww:url id="url" action="relRelatorios"
			namespace="/expediente/rel">
			<ww:param name="nomeArquivoRel">relModelos.jsp</ww:param>
		</ww:url> <ww:a href="%{url}">Relatório de Modelos</ww:a></li>
-->
		<li><ww:url id="url" action="relRelatorios"
			namespace="/expediente/rel">
			<ww:param name="nomeArquivoRel">relDocumentosSubordinados.jsp</ww:param>
		</ww:url> <ww:a href="%{url}">Relatório de Documentos em Setores Subordinados</ww:a></li>



	</ul>
	</div>
	</div>
	</li>

	<%--
	<li class="imatm" style="width:146px;"><a class="" href="#"> <span
		class="imea imeam"><span></span></span>Ferramentas</a>
	<div class="imsc">
	<div class="imsubc" style="width:146px;top:;left:;">
	<ul style="">
		<li><a href="#">Relat&oacute;rio A</a></li>
		<li><a href="#"> <span class="imea imeas"><span></span></span>Relat&oacute;rio
		B</a>
		<div class="imsc">
		<div class="imsubc" style="width:146px;top:-18px;left:130px;">
		<ul style="">
			<li><a href="#">Tipo 1</a></li>
			<li><a href="#">Tipo 2</a></li>
			<li><a href="#">Tipo 3</a></li>
		</ul>
		</div>
		</div>
		</li>
		<li><a href="#">Relat&oacute;rio C</a></li>
	</ul>
	</div>
	</div>
	</li>

	<li class="imatm" style="width:146px;"><a class="" href="#"> <span
		class="imea imeam"><span></span></span>Relat&oacute;rios</a>
	<div class="imsc">
	<div class="imsubc" style="width:146px;top:;left:;">
	<ul style="">
		<li><a href="#">Relat&oacute;rio A</a></li>
		<li><a href="#"> <span class="imea imeas"><span></span></span>Relat&oacute;rio
		B</a>
		<div class="imsc">
		<div class="imsubc" style="width:146px;top:-18px;left:130px;">
		<ul style="">
			<li><a href="#">Tipo 1</a></li>
			<li><a href="#">Tipo 2</a></li>
			<li><a href="#">Tipo 3</a></li>
		</ul>
		</div>
		</div>
		</li>
		<li><a href="#">Relat&oacute;rio C</a></li>
	</ul>
	</div>
	</div>
	</li>
	
--%>

</ul>
</div>
</div>
</div>
