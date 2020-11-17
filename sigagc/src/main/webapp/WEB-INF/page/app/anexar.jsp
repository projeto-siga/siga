<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Anexação de Arquivo">
    <link rel="stylesheet" href="../../public/stylesheets/jquery-ui-1.10.4.custom/jquery-ui-1.10.4.custom.css" type="text/css" />
    <link rel="stylesheet" href="../../public/stylesheets/plupload/jquery.ui.plupload.css" type="text/css" />
    
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>
				<span id="codigoInf">Anexar - ${informacao.sigla}</span>
			</h2>
	
			<div id="uploader">
	            <p>Sistema de Upload encontra-se com problema. Favor reportar erro para a equipe responsável.</p>
	        </div> 
	
	        <div class="gt-form-row gt-width-100">
	            <br /><br /><br />    
	            <p class="gt-cancel">
	                <a href="${linkTo[AppController].exibir(informacao.siglaCompacta)}">voltar</a>
	            </p>
	        </div>
		</div>
	</div>
	
	<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
	<script type="text/javascript" src="../../public/javascripts/plupload/plupload.full.min.js"></script>
	<script type="text/javascript" src="../../public/javascripts/plupload/jquery.ui.plupload.min.js"></script>
	<script type="text/javascript" src="../../public/javascripts/plupload/pt_BR.js"></script>
	
	<script type="text/javascript">
	   var $anexados = 0;
	   var $arquivos = "";
	   var $maxFiles = 6;
	   $(document).ready(function () {
	       $("#uploader").plupload({
	           // General settings
	           runtimes : 'html5,flash,silverlight,html4',
	           url: '${linkTo[AppController].gravarArquivo}', 
	           max_file_count: 0,
	           prevent_duplicates: true,
	           //chunk_size: '1mb',
	           filters : {
	               // Maximum file size
	               max_file_size: '2mb', //-- quando alterar esse valor alterar tb o arquivo pt_BR.js para alterar a msg de erro
	               // Specify what files to browse for
	               mime_types: [
	                            {title: "Todos os arquivos", extensions : "jpg,bmp,png,doc,docx,xls,xlsx,ppt,pptx,pdf,txt"},
	                            {title: "Arquivos de imagem", extensions : "jpg,bmp,png"},
	                            {title: "Arquivos de documento", extensions:"doc,docx,xls,xlsx,ppt,pptx,pdf,txt"}
	               ]
	           },
	           // Rename files by clicking on their titles
	           rename: true,
	           // Sort files
	           sortable: true,
	           // Enable ability to drag'n'drop files onto the widget (currently only HTML5 supports that)
	           dragdrop: true,
	           multipart: true,
	           multipart_params: {
	               "informacao.id": "${informacao.id}",
	               "origem": "anexar"
	           },
	           // Views to activate
	           views: {            
	               list: false,            
	               thumbs: true, // Show thumbs            
	               active: 'thumbs'        
	           },
	           buttons : {
	               stop: false
	           },
	           // Flash settings
	           flash_swf_url : '/sigagc/public/javascripts/plupload/Moxie.swf',
	           // Silverlight settings
	           silverlight_xap_url : '/sigagc/public/javascripts/plupload/Moxie.xap',
	           init : {
	               FilesAdded: function(up, files) {
	                   if(up.files.length == 1) {
	                       $('#uploader').plupload('notify', 'info', "Dica! <br />" + 
	                                               "Clique duas vezes em cima do nome do arquivo, caso queira alterá-lo.");
	                       $("div.plupload_message").addClass("dica");
	                       $(".dica").delay(10000).fadeOut("fast", "linear");        
	                   }
	                   else if(up.files.length == $maxFiles) {
	                       $("div.plupload_message").remove();
	                       $('#uploader').plupload('notify', 'info', "Aviso: Limite máximo atingido.<br /> Não é possível anexar "
	                               + $maxFiles + " arquivos de uma única vez.");
	                       up.disableBrowse();
	                       $("#uploader_browse").addClass("ui-button-disabled ui-state-disabled");
	                   }
	               },
	               FilesRemoved: function(up, files) {
	                   if(up.files.length < $maxFiles) {
	                       $("div.plupload_message").remove();
	                       $("#uploader_browse").removeClass("ui-button-disabled ui-state-disabled");
	                       up.disableBrowse(false);
	                   }
	               },
	               BeforeUpload: function(up, file) {
	                   up.settings.multipart_params.titulo = file.name;
	               },
	               FileUploaded: function(up, file, info) {
	                   if(info.response == "success") 
	                       $anexados += 1;
	                   else {
	                       if($arquivos == "")
	                           $arquivos += file.name;
	                       else
	                           $arquivos += ", " + file.name;
	                   }
	               },
	               UploadComplete: function(up, files) {
	                   if(up.files.length == $anexados) {
	                       up.destroy();
	                       location.href = "${linkTo[AppController].exibir(informacao.siglaCompacta)}";
	                   }
	                   else {
	                       $("p.gt-notice-box").remove();
	                       up.trigger("Error", {message: "Ocorreu um erro ao tentar anexar os seguintes arquivos: " 
	                                                   + $arquivos + ". <br />Favor verificar e tentar enviá-los novamente."});
	                   }
	               }
	           }
	       });
	       $("#uploader_start").click(function() {
	           $("#uploader").before("<p class='gt-notice-box'>Aguarde, os arquivos estão sendo anexados.<p>");
	       });
	   });
	</script>
</siga:pagina>
