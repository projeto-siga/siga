/*
 * mascara.js - utilitário para aplicar máscara a uma caixa de texto
 * 
  Para utilizar este componente faça o seguinte:
  
  1) Inclua o javascript na página
  2) Defina os campos que terão máscara
  3) Defina os hidden com valores da máscara
  4) Implemente a interface IUsaMascara na action
  5) (Opcional)Se quiser que a máscara seja preenchida com zeros, coloque o seguinte "onblur" no input
   
  			<input type="text" onblur="javascript:aplicarMascara(this)" />
  			
  
  Exemplo:
  
	<!-- mascara.js -->
	
  		<script src="/siga/javascript/mascara.js"></script>
  
  		<script type="text/javascript">
 				var elementosComMascara = ['#codificacao','inputText2'];		
  		</script>

  		<input id="mask_in" type="hidden" value="${mascaraEntrada}"/>
  		<input id="mask_out" type="hidden" value="${mascaraSaida}">
  		<input id="mask_js" type="hidden" value="${mascaraJavascript}">

	<!-- mascara.js -->
	
	Implementação de IUsaMascara
	
	public class XXX implements IUsaMascara {
	
	...
	
		public String getMascaraEntrada(){
			return MascaraUtil.getInstance().getMascaraEntrada();
		}
		
		public String getMascaraSaida(){
			return MascaraUtil.getInstance().getMascaraSaida();
		}
		
		public String getMascaraJavascript(){
			return SigaExProperties.getExClassificacaoMascaraJavascript();
		}
		
	...
		
	}
 *
 * 
 * */



function aplicarMascara(input){
		
			var mask_in = getMascaraIn();
			var mask_out = getMascaraOut();

			encontrado = input.value.match(mask_in)[0];
			if (encontrado==null || encontrado == ''){
				// window.alert("Valor inválido!");
				input.value="";
				return;
			}
			gruposRegEx = mask_in.exec(input.value);
			gruposRegEx[0] = mask_out;
			
			for(i=0;i<gruposRegEx.length;i++){
				if(gruposRegEx[i]==null){
					gruposRegEx[i]=0;	
				}
			}

			input.value = mdgw.format.apply(null,gruposRegEx);
			
		}

		function getMascaraIn(){
			return new RegExp($("#mask_in").val());
		}

		function getMascaraOut(){
			return $("#mask_out").val();
		}
		
		function getMascaraJs(){
			return $("#mask_js").val();
		}
		
		$.getScript("/siga/javascript/format4js.js", function(){
			
		});


		//carrega a máscara
		$.getScript("/siga/javascript/jquery.maskedinput.min.js", function(){
				jQuery(function($){
					for(e in elementosComMascara){
					   $(elementosComMascara[e]).mask(getMascaraJs());
					   
					   $(elementosComMascara[e]).blur(function(){
						   aplicarMascara(this);
					   });
					}
				});
		});
		
		

