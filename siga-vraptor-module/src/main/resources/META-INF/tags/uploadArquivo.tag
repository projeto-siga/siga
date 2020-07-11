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

<!-- Modal -->
<div class="modal fade" id="alertaM" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
    	<div class="modal-content">
      		<div class="modal-header">
		        <h5 class="modal-title" id="alertaModalLabel">Alerta</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
		          <span aria-hidden="true">&times;</span>
		    	</button>
		    </div>
	      	<div class="modal-body">
	        	<p class="mensagem-Modal"></p>
	      	</div>
			<div class="modal-footer">
			  <button type="button" class="btn btn-primary" data-dismiss="modal">Fechar</button>
			</div>
    	</div>
  	</div>
</div>				
<!--Fim Modal -->

<script type="text/javascript">
	function testTamanho() {
		var tamanhoArquivo = parseInt(document.getElementById("arquivo").files[0].size);
	    if(tamanhoArquivo > ${tamanhoMaximo}*1024*1024){
	    	mensagem("TAMANHO DO ARQUIVO EXCEDE O PERMITIDO (${tamanhoMaximo} MB)!");
	        document.getElementById("arquivo").value = "";
	    }
	}
	function mensagem(mensagem) {
		$('#alertaM').find('.mensagem-Modal').text(mensagem);
		$('#alertaM').modal();
	}
</script>
<script>
	$('.custom-file-input').on('change', function() { 
	   let fileName = $(this).val().split('\\').pop(); 
	   $(this).next('.custom-file-label').addClass("selected").html('<i class="far fa-file-excel"></i>  ' + fileName); 
	});
</script>