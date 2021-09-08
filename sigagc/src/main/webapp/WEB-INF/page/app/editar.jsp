<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/page/include.jsp"%>
<siga:pagina
	titulo="Edi&ccedil;&atilde;o de T&oacute;pico de Informa&ccedil;&atilde;o">

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

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-8">
				<div class="card bg-light mb-3">
					<div class="card-header">
						<h2>
							<span id="codigoInf">${informacao.sigla}</span>
						</h2>
					</div>

					<div class="card-body">
						<form id="frm" action="${linkTo[AppController].gravar}"
							method="POST" enctype="multipart/form-data">
							<c:if test="${informacao.id != null}">
							</c:if>
							<input type="hidden" id="infoId" name="informacao.id"
								value="${informacao.id}" /> <input type="hidden" name="origem"
								value="${origem}" /> <input type="hidden" id="siglaId"
								name="sigla" value="${informacao.sigla}" />
							<div class="row">
								<div class="col-sm-3 form-group">
									<siga:select label="Tipo" name="tipo.id" list="tiposInformacao"
										value="${tipo.id}" listKey="id" listValue="nome"
										onchange="postback();" />
								</div>
								<c:if
									test="${empty informacao.edicao.id || informacao.acessoPermitido(titular,lotaTitular, informacao.edicao.id)}">
									<div class="col-sm-2">
										<siga:select label="Visualiza&ccedil;&atilde;o"
											name="visualizacao.id" id="informacaoVisualizacao"
											list="acessos" listKey="id" listValue="nome"
											value="${informacao.visualizacao.id}"
											onchange="javascript:ocultaGrupo();" />
									</div>
									<div class="col-sm-2">
										<siga:select label="Edi&ccedil;&atilde;o" name="edicao.id"
											list="acessos" id="informacaoEdicao" listKey="id"
											listValue="nome" value="${informacao.edicao.id}"
											onchange="javascript:ocultaGrupo();" />
									</div>
								</c:if>
								<div class="col-sm-5" id="informacaoGrupo" style="display: none">
										<div class="col-sm-1">
											<label>Grupo</label>
										</div>
										<div class="col-sm-4">
											<siga:selecao2 propriedade="grupo" modulo="siga"
												tipo="gi/perfil" tema="simple" />
										</div>
								</div>
							</div>


							<div class="row">
								<div class="col-sm-12">
									<label>T&iacute;tulo</label>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<input type="text" id="inftitulo" name="inftitulo"
										value="${inftitulo}" class="form-control" />
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12 form-group">
									<label>Texto</label>
									<textarea id="conteudo" name="conteudo" rows="25"
										class="form-control">${conteudo}</textarea>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-12 form-group">
									<label>Classifica&ccedil;&atilde;o</label>
									<c:choose>
										<c:when test="${empty classificacao && !editarClassificacao}">
											<p>Este conhecimento ainda n&atilde;o possui uma
												classifica&ccedil;&atilde;o.</p>
										</c:when>
										<c:otherwise>
											<textarea name="classificacao" class="form-control" rows="3"
												${editarClassificacao ? '' : 'readonly'}>${classificacao}</textarea>
										</c:otherwise>
									</c:choose>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-12">

									<input id="btn-save" type="submit" value="Gravar"
										class="btn btn-primary"/>

										<c:choose>
											<c:when
												test="${not empty informacao && not empty informacao.id}">
										ou <a
													href="${linkTo[AppController].editar(informacao.siglaCompacta)}">cancelar
													altera&ccedil;&otilde;es</a>
										ou <a
													href="${linkTo[AppController].movimentacoes(informacao.siglaCompacta)}">exibir
													movimenta&ccedil;&otilde;es</a>
											</c:when>
											<c:otherwise>
												<a href="${linkTo[AppController].novo}" class="btn btn-link">cancelar
													altera&ccedil;&otilde;es</a>
											</c:otherwise>
										</c:choose>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>

			<div class="col-sm-4">
				<!-- Sidebar Content -->
				<div class="gt-sidebar-content">
					<h5>Informa&ccedil;&otilde;es sobre o Preenchimento</h5>
					<p>O campo "T&iacute;tulo" sempre ser&aacute; acess&iacute;vel,
						independente do controle de acesso selecionado.</p>

					<div id="ajax_arquivo">
						<c:if test="${informacao.contemArquivos}">
							<h5 style="padding-top: 1em">Incluir Imagens ou Arquivos no
								Texto</h5>
							<p>Clique em uma imagem/arquivo abaixo para incluir uma
								refer&ecirc;ncia no texto.</p>
							<c:forEach items="${informacao.movs}" var="m">
								<c:if test="${m.tipo.id == 13 && m.movCanceladora == null}">
									<p>
										<img style="margin-bottom: -4px;"
											src="/siga/css/famfamfam/icons/${m.arq.icon}.png" /> <a
											style="padding-right: 5px;"
											href="javascript: var frm = document.getElementById('frm'); <c:if test="${m.arq.image}">insertImageAtCursor(${m.arq.id},'${m.arq.titulo}');</c:if><c:if test="${not m.arq.image}">insertFileAtCursor(${m.arq.id},'${m.arq.titulo}');</c:if>">${m.arq.titulo}</a>
										[ <img style="margin-bottom: -1px; width: 9px;"
											src="/siga/css/famfamfam/icons/cross.png" /> <span
											class="gt-table-action-list"> <a
											href="javascript:if (confirm('Confirma a remocao deste anexo?')) 
												ReplaceInnerHTMLFromAjaxResponse('../removerAnexo?sigla=${informacao.sigla}&idArq=${m.arq.id}&idMov=${m.id}',
																	null, document.getElementById('ajax_arquivo'));">remover</a></span>
										&nbsp;]
									</p>
								</c:if>
							</c:forEach>
						</c:if>
					</div>
					<h5 style="padding-top: 1em">Inserir
						classifica&ccedil;&atilde;o no Texto</h5>
					<p>
						O conte&uacute;do do campo "Texto" pode receber uma
						marca&ccedil;&atilde;o especial para classifica&ccedil;&atilde;o.
						Clique <a id="marcadores" href="#">aqui</a> para visualizar a
						op&ccedil;&atilde;o dispon&iacute;vel.
					</p>
					<div id="cheatsheet" style="display: none;">
						<table class="side-bar-light-table">
							<tbody>

								<tr>
									<td>#classificacao-conhecimento<br>
									<td class="arrow">&#8594;</td>
									<td><a href="#">#classificacao-conhecimento</a></td>
								</tr>

							</tbody>
						</table>
						<br />
						<p>Não usar caracteres especiais ou espaço</p>
					</div>
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
		CKEDITOR.config.filebrowserUploadMethod = 'form';
//		CKEDITOR.config.removeButtons = 'Image';
		CKEDITOR.config.removeDialogTabs = 'link:advanced;link:upload;image:advanced;image:Link';
		CKEDITOR
				.replace(
						'conteudo',
						{
							filebrowserUploadUrl : '${linkTo[AppController].gravarArquivo}?origem=editar'
									+ '&informacao.id=' + '${informacao.id}',
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
