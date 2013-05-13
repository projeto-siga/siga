		function aplicarMascara(input){
		
			var mask_in = getMascaraIn();
			var mask_out = getMascaraOut();

			encontrado = input.value.match(mask_in)[0];
			if (encontrado==null || encontrado == ''){
				window.alert("Codificação inválida!");
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
			/*
			var pos = input.selectionStart;
			input.value = mdgw.format.apply(null,gruposRegEx);
			
			var charAtual = document.getElementById('texto').value.charAt(pos);
			if (charAtual!="" && !isDigito(charAtual)){
				while(!isDigito(charAtual)){
					pos++;
					charAtual = document.getElementById('texto').value.charAt(pos);
				}
			}
			input.selectionStart = pos;
			*/
			input.value = mdgw.format.apply(null,gruposRegEx);
			//document.getElementById('textoFormatado').value = mdgw.format.apply(null,gruposRegEx);
		}

		function getMascaraIn(){
			/*mask = $("#mask_in").val();
			mask.replaceAll('\\','\');
			return mask;
			*/
			return new RegExp($("#mask_in").val());
		}

		function getMascaraOut(){
			return $("#mask_out").val();
		}
		/*
		function isDigito(digito){
			regExDigito = /[0-9,a-z,A-Z]/;
			return regExDigito.exec(digito)!=null
		}
		*/
