<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Anexação de Arquivo">
	<div class="container-fluid">
		<div class="">
			<h2>
				<span id="codigoInf">Anexar - ${informacao.sigla}</span>
			</h2>

			<div class="card-sidebar card border-alert bg-white mb-3 mt-3">
				<div class="card-header">Arquivos</div>
				<div class="card-body">
					<ul id="filelist"></ul>
				</div>
			</div>

			<div id="container"></div>
		</div>
		<a id="browse" href="javascript:;" class="btn btn-info">Escolher
			Arquivos</a> <a id="start-upload" href="javascript:;"
			class="btn btn-primary">Enviar</a> <a class="btn btn-secondary"
			href="${linkTo[AppController].exibir(informacao.siglaCompacta)}">Voltar</a>

		<pre id="console"></pre>
	</div>


	<script type="text/javascript"
		src="../../public/javascripts/plupload-3.1.3/js/plupload.full.min.js"></script>
	<script type="text/javascript"
		src="../../public/javascripts/plupload-3.1.3/js/pt_BR.js"></script>

	<script type="text/javascript">
		var uploader = new plupload.Uploader(
				{
					browse_button : 'browse', // this can be an id of a DOM element or the DOM element itself
					url : '${linkTo[AppController].gravarArquivo}?informacao.id=${informacao.id}&origem=anexar',
					filters : {
						max_file_size : '2mb', //-- quando alterar esse valor alterar tb o arquivo pt_BR.js para alterar a msg de erro
						// Specify what files to browse for
						mime_types : [
								{
									title : "Todos os arquivos",
									extensions : "jpg,bmp,png,doc,docx,xls,xlsx,ppt,pptx,pdf,txt"
								},
								{
									title : "Arquivos de imagem",
									extensions : "jpg,bmp,png"
								},
								{
									title : "Arquivos de documento",
									extensions : "doc,docx,xls,xlsx,ppt,pptx,pdf,txt"
								} ]
					}
				});

		uploader.init();

		uploader.bind('FilesAdded', function(up, files) {
			var html = '';
			plupload.each(files, function(file) {
				html += '<li id="' + file.id + '">' + file.name + ' ('
						+ plupload.formatSize(file.size) + ') <b></b></li>';
			});
			document.getElementById('filelist').innerHTML += html;
		});

		uploader
				.bind(
						'UploadProgress',
						function(up, file) {
							document.getElementById(file.id)
									.getElementsByTagName('b')[0].innerHTML = '<span>'
									+ file.percent + "%</span>";
						});

		uploader.bind('Error', function(up, err) {
			document.getElementById('console').innerHTML += "\nError #"
					+ err.code + ": " + err.message;
		});

		uploader
				.bind(
						'UploadComplete',
						function(up, files) {
							/* 			if (up.files.length == $anexados) {
							 up.destroy();
							 */
							location.href = "${linkTo[AppController].exibir(informacao.siglaCompacta)}";

							/* 		} else {
															$("p.gt-notice-box")
																	.remove();
															up
																	.trigger(
																			"Error",
																			{
																				message : "Ocorreu um erro ao tentar anexar os seguintes arquivos: "
																						+ $arquivos
																						+ ". <br />Favor verificar e tentar enviá-los novamente."
																			});
														} */
						});

		document.getElementById('start-upload').onclick = function() {
			uploader.start();
		};

		/* 
		var $anexados = 0;
		var $arquivos = "";
		var $maxFiles = 6;
		$(document)
				.ready(
						function() {
							$("#uploader")
									.plupload(
											{
												// General settings
												runtimes : 'html5,flash,silverlight,html4',
												url : '${linkTo[AppController].gravarArquivo}',
												max_file_count : 0,
												prevent_duplicates : true,
												//chunk_size: '1mb',
												filters : {
													// Maximum file size
													max_file_size : '2mb', //-- quando alterar esse valor alterar tb o arquivo pt_BR.js para alterar a msg de erro
													// Specify what files to browse for
													mime_types : [
															{
																title : "Todos os arquivos",
																extensions : "jpg,bmp,png,doc,docx,xls,xlsx,ppt,pptx,pdf,txt"
															},
															{
																title : "Arquivos de imagem",
																extensions : "jpg,bmp,png"
															},
															{
																title : "Arquivos de documento",
																extensions : "doc,docx,xls,xlsx,ppt,pptx,pdf,txt"
															} ]
												},
												// Rename files by clicking on their titles
												rename : true,
												// Sort files
												sortable : true,
												// Enable ability to drag'n'drop files onto the widget (currently only HTML5 supports that)
												dragdrop : true,
												multipart : true,
												multipart_params : {
													"informacao.id" : "${informacao.id}",
													"origem" : "anexar"
												},
												// Views to activate
												views : {
													list : false,
													thumbs : true, // Show thumbs            
													active : 'thumbs'
												},
												buttons : {
													stop : false
												},
												// Flash settings
												flash_swf_url : '/sigagc/public/javascripts/plupload/Moxie.swf',
												// Silverlight settings
												silverlight_xap_url : '/sigagc/public/javascripts/plupload/Moxie.xap',
												init : {
													FilesAdded : function(up,
															files) {
														if (up.files.length == 1) {
															$('#uploader')
																	.plupload(
																			'notify',
																			'info',
																			"Dica! <br />"
																					+ "Clique duas vezes em cima do nome do arquivo, caso queira alterá-lo.");
															$(
																	"div.plupload_message")
																	.addClass(
																			"dica");
															$(".dica")
																	.delay(
																			10000)
																	.fadeOut(
																			"fast",
																			"linear");
														} else if (up.files.length == $maxFiles) {
															$(
																	"div.plupload_message")
																	.remove();
															$('#uploader')
																	.plupload(
																			'notify',
																			'info',
																			"Aviso: Limite máximo atingido.<br /> Não é possível anexar "
																					+ $maxFiles
																					+ " arquivos de uma única vez.");
															up.disableBrowse();
															$(
																	"#uploader_browse")
																	.addClass(
																			"ui-button-disabled ui-state-disabled");
														}
													},
													FilesRemoved : function(up,
															files) {
														if (up.files.length < $maxFiles) {
															$(
																	"div.plupload_message")
																	.remove();
															$(
																	"#uploader_browse")
																	.removeClass(
																			"ui-button-disabled ui-state-disabled");
															up
																	.disableBrowse(false);
														}
													},
													BeforeUpload : function(up,
															file) {
														up.settings.multipart_params.titulo = file.name;
													},
													FileUploaded : function(up,
															file, info) {
														if (info.response == "success")
															$anexados += 1;
														else {
															if ($arquivos == "")
																$arquivos += file.name;
															else
																$arquivos += ", "
																		+ file.name;
														}
													},
													UploadComplete : function(
															up, files) {
														if (up.files.length == $anexados) {
															up.destroy();
															location.href = "${linkTo[AppController].exibir(informacao.siglaCompacta)}";
														} else {
															$("p.gt-notice-box")
																	.remove();
															up
																	.trigger(
																			"Error",
																			{
																				message : "Ocorreu um erro ao tentar anexar os seguintes arquivos: "
																						+ $arquivos
																						+ ". <br />Favor verificar e tentar enviá-los novamente."
																			});
														}
													}
												}
											});
							$("#uploader_start")
									.click(
											function() {
												$("#uploader")
														.before(
																"<p class='gt-notice-box'>Aguarde, os arquivos estão sendo anexados.<p>");
											});
						});
		 */
	</script>
</siga:pagina>
