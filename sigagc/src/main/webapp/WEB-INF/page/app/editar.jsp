<%@ include file="/WEB-INF/page/include.jsp"%>
<siga:pagina titulo="Edição de Tópico de Informação">

	<script>
		function postback() {
			var frm = document.getElementById('frm');
			frm.action = '@{Application.editar}';
			frm.submit();
		}
		function ocultaGrupo() {
			if ($("#informacaoVisualizacao").val() == 7
					|| $("#informacaoEdicao").val() == 7) {
				$("#informacaoGrupo").show();
			} else {
				$("#informacaoGrupo").hide();
				$("#informacaogrupo").val("");
				$("#informacaogrupo_sigla").val("");
				$("#informacaogrupo_descricao").val("");
				$("#informacaogrupoSpan").text("");
			}
		}
		$(document).ready(function() {
			ocultaGrupo();
			//$("#informacaoGrupo").left($("#informacaoVisualizacao").left());
		});
	</script>

	<div class="gt-bd gt-cols clearfix">
		<div class="gt-content clearfix">
			<h2>
				<span id="codigoInf">${informacao.sigla}</span>
			</h2>

			<div class="gt-form gt-content-box">
				<form id="frm" action="${linkTo[AppController].gravar}"
					method="POST" enctype="multipart/form-data">
					<c:if test="${informacao.id != 0}">
					</c:if>
					<input type="hidden" id="infoId" name="informacao.id"
						value="${informacao.id}" /> <input type="hidden" name="origem"
						value="${origem}" /> <input type="hidden" id="siglaId"
						name="sigla" value="${informacao.sigla}" />
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-25" style="margin-right: 2em">
							<siga:select label="Tipo" name="tipo.id" list="tiposInformacao"
								listKey="id" listValue="nome" onchange="postback();" />
						</div>
						<c:if
							test="${empty informacao.edicao.id || informacao.acessoPermitido(titular,lotaTitular, informacao.edicao.id)}">
							<div class="gt-left-col gt-width-25" style="padding-left: 2em">
								<siga:select label="Visualizacão" name="visualizacao.id"
									list="acessos" listKey="id" listValue="nome"
									onchange="javascript:ocultaGrupo();" />
							</div>
							<div class="gt-left-col gt-width-25" style="padding-left: 2em">
								<siga:select label="Edicão" name="edicao.id" list="acessos"
									listKey="id" listValue="nome"
									onchange="javascript:ocultaGrupo();" />
							</div>
						</c:if>
					</div>
					<div class="gt-form-row gt-width-100" id="informacaoGrupo"
						style="display: none">
						<label>Grupo</label>
						<div class="gt-left-col gt-width-25" style="padding-left: 2em">
							<siga:selecao2 propriedade="informacao.grupo" modulo="siga"
								tipo="perfil" tema="simple" />
						</div>
					</div>

					<div class="gt-form-row gt-width-100">
						<label>Título</label> <input type="text" id="inftitulo"
							name="inftitulo" value="${inftitulo}" class="gt-form-text" />
					</div>

					<div class="gt-form-row gt-width-100">
						<label>Texto</label>
						<textarea id="conteudo" name="conteudo" cols="80" rows="25"
							class="gt-form-textarea">${conteudo}</textarea>
					</div>

					<div class="gt-form-row gt-width-100">
						<label>Classificação</label>
						<c:if test="${not empty classificacao}">
							<c:if test="${not editarClassificacao}">
								<c:set var="somenteLeitura" value="readonly" />
							</c:if>
							<textarea name="classificacao" class="gt-form-text"
								${somenteLeitura}>${classificacao}</textarea>
						</c:if>
						<c:if test="${empty classificacao}">
							<p>Esse conhecimento ainda não possui uma classificação</p>
						</c:if>
					</div>

					<div class="gt-form-row gt-width-100">

						<input id="btn-save" type="submit" value="Gravar"
							class="gt-btn-medium gt-btn-left" style="cursor: pointer;" />

						<p class="gt-cancel">
							<c:choose>
								<c:when
									test="not empty informacao && not empty informacao.id && informacao.id != 0}">
								ou <a href="${linkTo[AppController].}editar[informacao.sigla]}">cancelar
										alterações</a>
								ou <a
										href="${linkTo[AppController].movimentacoes[informacao.sigla]}">exibir
										movimentações</a>
								</c:when>
								<c:otherwise>
									<a href="${linkTo[AppController].editar}">cancelar
										alterações</a>
								</c:otherwise>
							</c:choose>
						</p>
					</div>
				</form>
			</div>
		</div>
		<div class="gt-sidebar">
			<!-- Sidebar Content -->
			<div class="gt-sidebar-content">
				<h3>Informações sobre o Preenchimento</h3>
				<p>O campo "Título" sempre será acessível, independente do
					controle de acesso selecionado.</p>

				<div id="ajax_arquivo">
					<c:if test="${informacao.contemArquivos}">
						<h3 style="padding-top: 1em">Incluir Imagens ou Arquivos no
							Texto</h3>
						<p>Clique em uma imagem/arquivo abaixo para incluir uma
							referência no texto.</p>
						<c:forEach items="${informacao.mov}" var="m">
							<c:if
								test="${m.tipo.id == models.GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO && m.movCanceladora == null}">
								<p>
									<img style="margin-bottom: -4px;"
										src="/siga/css/famfamfam/icons/${m.arq.icon}.png" /> <a
										style="padding-right: 5px;"
										href="javascript: var frm = document.getElementById('frm'); <c:if test="${m.arq.image}">insertImageAtCursor(${m.arq.id},'${m.arq.titulo}');</c:if><c:if test="${not m.arq.image}">insertFileAtCursor(${m.arq.id},'${m.arq.titulo}');</c:if>">${m.arq.titulo}</a>
									[ <img style="margin-bottom: -1px; width: 9px;"
										src="/siga/css/famfamfam/icons/cross.png" /> <span
										class="gt-table-action-list"> <a
										href="javascript:if (confirm('Confirma a remoção deste anexo?')) 
											ReplaceInnerHTMLFromAjaxResponse('removerAnexo?sigla=${informacao.sigla}&idArq=${m.arq.id}&idMov=${m.id}',
																null, document.getElementById('ajax_arquivo'));">remover</a></span>
									&nbsp;]
								</p>
							</c:if>
						</c:forEach>
					</c:if>
				</div>
				<h3 style="padding-top: 1em">Inserir classificação no Texto</h3>
				<p>
					O conteúdo do campo "Texto" pode receber uma marcação especial para
					classificação. Clique <a id="marcadores" href="#">aqui</a> para
					visualizar a opção disponível.
				</p>
				<div id="cheatsheet" style="display: none;">
					<table class="side-bar-light-table">
						<tbody>
							<!-- <tr>
							<td>//itálico//</td>
							<td class="arrow">→</td>
							<td><em>itálico</em></td>
						</tr>
						<tr>
							<td>**negrito**</td>
							<td class="arrow">→</td>
							<td><strong>negrito</strong></td>
						</tr>
						<tr>
							<td>&&highlight&&</td>
							<td class="arrow">→</td>
							<td><mark>highlight</mark></td>
						</tr>
						<tr>
							<td>* Lista não numerada<br>* Segundo item<br>**
								Sub item</td>
							<td class="arrow">→</td>
							<td>• Lista não numerada<br>• Segundo item<br>..•
								Sub item</td>
						</tr>
						<tr>
							<td># Lista numerada<br># Segundo item<br>## Sub
								item</td>
							<td class="arrow">→</td>
							<td>1. Lista numerada<br>2. Segundo item<br>2.1
								Sub item</td>
						</tr> -->
							<tr>
								<td>#classificacao-conhecimento<br>
								<td class="arrow">→</td>
								<td><a href="#">#classificacao-conhecimento</a></td>
							</tr>
							<!-- 
					<tr>
						<td>Link para [[outro conhecimento]]</td>
						<td class="arrow">→</td>
						<td>Link to <a
							href="http://www.wikicreole.org/attach/CheatSheet/URL">wiki
								page</a>
						</td>
					</tr>
 -->
							<!-- 	<tr>
							<td>[[URL|nome do link]]</td>
							<td class="arrow">→</td>
							<td><a
								href="http://www.wikicreole.org/attach/CheatSheet/URL">nome
									do link</a></td>
						</tr>

						<tr>
							<td>== Título grande<br>=== Título médio<br>====
								Título pequeno</td>
							<td class="arrow">→</td>
							<td><span style="font-size: 120%; font-weight: bold;">
									Título grande</span><br> <span
								style="font-size: 110%; font-weight: bold;">Título médio</span><br>
								<span style="font-size: 100%; font-weight: bold;">Título
									pequeno</span></td>
						</tr>

						<tr>
							<td>Não<br> quebrar a linha!<br> <br>Use uma
								linha vazia</td>
							<td class="arrow">→</td>
							<td>Não quebrar a linha!<br> <br>Use uma linha
								vazia</td>
						</tr>
						<tr>
							<td>Quebra de linha\\forçada<br></td>
							<td class="arrow">→</td>
							<td>Quebra de linha<br>forçada</td>
						</tr>
						<tr>
							<td>Linha horizontal:<br>----</td>
							<td class="arrow">→</td>
							<td>Linha horizontal:
								<hr></td>
						</tr>
						<tr>
							<td>{{imagem.jpg|título}}</td>
							<td class="arrow">→</td>
							<td>Imagem com título</td>
						</tr>
						<tr>
							<td>|cabeçalho|cabeçalho|<br>|coluna|coluna|<br>|coluna|coluna|</td>
							<td class="arrow">→</td>
							<td>Tabela</td>
						</tr>
						<tr>
							<td>{{{<br>== [[não|formatar]]<br />}}}</td>
							<td class="arrow">→</td>
							<td>== [[não|formatar]]:</td>
						</tr> -->
						</tbody>
					</table>
					<br />
					<p>Não usar caracteres especiais ou espaço</p>
				</div>
			</div>
		</div>
	</div>

	<script src="/ckeditor/ckeditor/ckeditor.js"></script>
	<script type="text/javascript">
		function postback() {
			var frm = document.getElementById('frm');
			frm.action = '${linkTo[AppController].editar}';
			frm.submit();
		}

		//CKEDITOR.config.autoGrow_onStartup = true;
		//CKEDITOR.config.autoGrow_bottomSpace = 50;
		//CKEDITOR.config.autoGrow_maxHeight = 400;
		CKEDITOR.config.removePlugins = 'elementspath';
		CKEDITOR.config.image_previewText = ' ';
		CKEDITOR.config.height = 270;
		CKEDITOR.config.removeDialogTabs = 'link:advanced;link:upload;image:advanced;image:Link';
		CKEDITOR
				.replace(
						'conteudo',
						{
							filebrowserUploadUrl : '${linkTo[AppController].gravarArquivo}?origem=editar',
							toolbar : [
									{
										name : 'clipboard',
										groups : [ 'clipboard', 'undo' ],
										items : [ 'Cut', 'Copy', 'Paste',
												'PasteText', 'PasteFromWord',
												'-', 'Undo', 'Redo' ]
									},
									{
										name : 'editing',
										groups : [ 'find', 'selection' ],
										items : [ 'Find', 'Replace', '-',
												'SelectAll' ]
									},
									{
										name : 'links',
										items : [ 'Link', 'Unlink', 'Anchor' ]
									},
									{
										name : 'document',
										groups : [ 'mode', 'document',
												'doctools' ],
										items : [ 'Maximize', '-', 'Source' ]
									},
									{
										name : 'basicstyles',
										groups : [ 'basicstyles', 'cleanup' ],
										items : [ 'Bold', 'Italic',
												'Underline', 'Strike',
												'Subscript', 'Superscript',
												'-', 'RemoveFormat' ]
									},
									{
										name : 'paragraph',
										groups : [ 'list', 'indent', 'blocks',
												'align', 'bidi' ],
										items : [ 'NumberedList',
												'BulletedList', '-', 'Outdent',
												'Indent', '-', 'Blockquote',
												'-', 'JustifyLeft',
												'JustifyCenter',
												'JustifyRight', 'JustifyBlock' ]
									},
									{
										name : 'insert',
										items : [ 'Image', 'Table', 'Smiley',
												'SpecialChar' ]
									}, {
										name : 'styles',
										items : [ 'Styles', 'Format' ]
									}, {
										name : 'colors',
										items : [ 'TextColor', 'BGColor' ]
									} ]

						});
		//$(".cke_botom").hide();
	</script>
	<script type="text/javascript">
		function insertFileAtCursor(id, nome) {
			var tag = '<a href="/sigagc/app/baixar?id=' + id + '">' + nome
					+ '</a>';
			CKEDITOR.instances.conteudo.insertHtml(tag);
		}
		function insertImageAtCursor(id, nome) {
			var tag = '<img alt="' + nome + '" src="/sigagc/app/baixar?id='
					+ id + '" style="width: 200px;" title="' + nome + '" />';
			CKEDITOR.instances.conteudo.insertHtml(tag);
		}
		function postback() {
			var frm = document.getElementById('frm');
			frm.action = '${linkTo[AppController].editar}';
			frm.submit();
		}
		$(document).ready(function() {
			$("textarea[readonly]").css({
				"background-color" : "transparent",
				"border" : "none",
				"overflow" : "auto",
				"height" : "50px",
				"resize" : "none"
			});
			$("#marcadores").click(function() {
				$("#cheatsheet").show();
			});
		});
	</script>
</siga:pagina>
