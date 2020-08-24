<%@ tag body-content="scriptless"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="tamanhoMaximo"%>
<%@ attribute name="textoCaixa"%>
<%@ attribute name="tiposAceitos"%>

<style type="text/css" media="screen">
	.custom-file-label::after{content: 'Escolher arquivo' !important;}
</style>

<input type="file" name="arquivo" accept="${tiposAceitos }" class="custom-file-input" id="arquivo"  onchange="testTamanho();">
<label  class="custom-file-label text-truncate" for="arquivo" data-browse="Escolha o Arquivo">
	<c:if test="${fn:contains(tiposAceitos, 'xls')}"><i class="far fa-file-excel"></i></c:if>
	${textoCaixa }
</label>

<script type="text/javascript">
	function testTamanho() {
		var tamanhoArquivo = parseInt(document.getElementById("arquivo").files[0].size);
	    if(tamanhoArquivo > ${tamanhoMaximo}*1024*1024){
	    	sigaModal.alerta("Tamanho do arquivo excede o permitido (${tamanhoMaximo} MB)!");
	        document.getElementById("arquivo").value = "";
	    }
	}	
</script>
<script>
	$('.custom-file-input').on('change', function() { 
	   let fileName = $(this).val().split('\\').pop(); 
	   $(this).next('.custom-file-label').addClass("selected").html('<i class="far fa-file-excel"></i>  ' + fileName); 
	});
</script>