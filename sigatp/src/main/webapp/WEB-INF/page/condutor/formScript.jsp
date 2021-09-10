<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	$(document).ready(function() {
		$('#imgArquivo').css('display','none');

		$('#exibirImagem').click(function() {
			var url = '';
			
			if($('#imgArquivo').css('display') == 'block') {
				if ($('#situacaoImagem').val() == "nobanco") {
						url = "${linkTo[CondutorController].exibirImagem(condutor.id)}";
				}
				else if ($('#situacaoImagem').val() == "imagemnova") {
					url = document.getElementById("imgArquivo").src;
				}

				var newwin = window.open(url,'miniwin','toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=0,width=800,height=800,top=100,left=100');
				//newwin.document.close();
			}
		});
		
		$('#arquivo').change(function(evt) {
			var arquivo; 
			if($('#arquivo').val() != "") {
	        	if ( $.browser.msie ) {
		        	$('#imgArquivo').attr('src',this.value);
	                $('#exibirImagem').attr('disabled',false);
				}
				else { //chrome, mozilla
					  var f = evt.target.files[0];
				      var reader = new FileReader();
				      reader.onload = (function(theFile) {
				       	return function(e) {
					        	$('#imgArquivo').attr('src',e.target.result);
					        };
				      })(f);
				      reader.readAsDataURL(f);
						  $('#exibirImagem').attr('disabled',false);
				}

				if ($('#imgArquivo').css('display') == 'none') {
					$('#imgArquivo').css('display','block');
					$('#excluirImagem').attr('disabled',false);
					$('#situacaoImagem').val('imagemnova');
				}
			}
		});

		if ($('#imgArquivo').css('display') == 'none') {
			$('#excluirImagem').attr('disabled',true);
		}

		else if($('#imgArquivo').css('display') == 'block') {
			$('#excluirImagem').attr('disabled',false);
		}
		});

	function removerArquivo() {
		$('#imgArquivo').removeAttr('src');
		$('#excluirImagem').attr('disabled',true);
		$('#imgArquivo').css('display','none');

		if ( $.browser.msie ) 
			$("#arquivo").replaceWith($("#arquivo").clone(true));
		else //chrome, mozilla
			$('#arquivo').val("");
	
		$('#situacaoImagem').val('semimagem');
	    $('#exibirImagem').attr('disabled',true);
 	}

	$(window).load(function() {
		carregarDadosDpPessoa();
		if(${condutor.id} > 0) {
			var exibirImgArquivo = "${linkTo[CondutorController].exibirImagem(condutor.id)}";
		  	    $.get(
		  	    		exibirImgArquivo, 
			           function(carregouImagem) {
	    					if ("${condutor.getConteudoimagemblob()}" != "") {   
	    						$('#imgArquivo').css('display','block');
	    						$('#excluirImagem').attr('disabled',false);
	    						$('#exibirImagem').attr('disabled',false);
	    						$('#situacaoImagem').val('nobanco');
	    					}
	    					else {
	    						$('#imgArquivo').css('display','none');
	        					$('#excluirImagem').attr('disabled',true);
	        					$('#exibirImagem').attr('disabled',true);
	    						$('#situacaoImagem').val('semimagem');
	    					}
	    			   }
		  	);
		}
	});
</script>