function validar() {
	var data = document.getElementsByName('dtDispon')[0].value;
	var i = document.getElementById('descrPublicacao').value.length;
	if (data==null || data=="") {			
		alert("Preencha a data para disponibilização.");
		document.getElementById('dt_dispon').focus();		
	} else {
		if (i<=0) {
			alert('Descrição deve ser preenchida');
			document.getElementById('descrPublicacao').focus();	
		}else	
			frm.submit();
	}
}

function buscaNomeLota(){
	var siglaLota = $('#idLotPublicacao :selected').text();	
		$.ajax({				     				  
			  url:'/siga/lotacao/selecionar.action?sigla=' + siglaLota ,					    					   					 
			  success: function(data) {
				 var parts = data.split(';');					   
		    	$('#nomeLota').html(parts[3]);				    
		 	 }
		});			
}

function aplicarMacro(el) {
	var curPos = document.getElementById("descrPublicacao").selectionStart;
    let x = $("#descrPublicacao").val();
    let texto = "";
    
    switch(el) {
        case "1":
    		texto = "((11VC))";
            break;
        case "2":
        	texto = "((TEXTO))";
          	break;
        case "3":
        	texto = "((VP))";
          	break;
        case "4":
        	texto = ", vago em decorrência do falecimento de ";
          	break;
        case "5":
        	texto = "((TEXTO))((BOLD))";
          	break;
        case "6":
        	texto = "Palácio dos Bandeiraates, \n" +
        		"CAUÊ MACRIS \n" +
        		"((GRIFO))Henrique de Campos Meirelles((CLARO)) \n" +
        		"Secretário da Fazenda e Planejamento \n" +
        		"((GRIFO))Gilberto Kassab((CLARO)) \n" +
        		"Secretário-Chefe da Casa Civil \n" +
        		"((GRIFO))Nelson Baeta Neves Filho((CLARO)) \n" +
        		"Secretário Executivo, Respondendo pelo Expediente da Secretaria de Governo \n" +
        		"Publicado na Secretaria de Governo, aos ";
          	break;
        case "7":
        	texto = "((V))2((P))";
        	break;
        case "8":
        	texto = "((GRIFO))";
        	break;
        case "9":
        	texto = ", vago em decorrência da demissão de ";
        	break;
        case "10":
        	texto = "((VP))DECRETO DO PRESIDENTE DA ASSEMBLÉIA LEGISLATIVA, EM EXERCÍCIO NO CARGO DE GOVERNADOR DO ESTADO, DE ";
        	break;
        case "11":
        	texto = "((VP))DESPACHO DO PRESIDENTE DA ASSEMBLÉIA LEGISLATIVA, EM EXERCÍCIO NO CARGO DE GOVERNADOR DO ESTADO, DE  ";
        	break;
        case "12":
        	texto = "No processo administrativo ";
        	break;
        case "13":
        	texto = "((CLARO))";
        	break;
        case "14":
        	texto = "((BOLD))Decreta((CLARO)):";
        	break;
        case "15":
        	texto = "((EMENTA))";
        	break;
        case "16":
        	texto = "((GRIFO)) ((CLARO))\n" +
        		"Secretário-Adjunto, Respondendo pelo Expediente da Casa Civil";
        	break;
        case "17":
        	texto = "Nomeando((CLARO)), nos termos do inc. I do art. 20, da LC 180-78, o abaixo indicado, para exercer em comissão e em Jornada Completa de Trabalho, o cargo a seguir relacionado da EV-C, a que se referee a LC 1080-2008, do SQC-";
        	break;
        case "18":
        	texto = "14 de Janeiro de 2021";
        	break;
        case "19":
        	texto = ", vago em decorrência da aposentadoria de ";
        	break;
        case "20":
        	texto = "((VP))DECRETO DO VICE-GOVERNADOR, EM EXERCÍCIO NO CARGO DE GOVERNADOR DO ESTADO, DE ";
        	break;
        case "21":
        	texto = "((VP))DESPACHO DO VICE-GOVERNADOR, EM EXERCÍCIO NO CARGO DE GOVERNADOR DO ESTADO, DE ";
        	break;
        case "22":
        	texto = "((TEXTO))RODRIGO GARCIA, Vice-Governador, em Exercício no Cargo de Governador do Estado de São Paulo, ";
        	break;
        case "23":
        	texto = "Palácio dos Bandeirantes,\n" +
        		"RODRIGO GARCIA\n" +
        		"((GRIFO))Henrique de Campos Meirelles((CLARO))\n" +
        		"Secretário da Fazenda e Planejamento\n" +
        		"((GRIFO))Antonio Carlos Rizeque Malufe((CLARO))\n" +
        		"Secretário Executivo, Respondendo pelo Expediente da Casa Civil\n" +
        		"((GRIFO))Nelson Baeta Neves Filho((CLARO))\n" +
        		"Secretário Executivo, Respondendo pelo Expediente da Secretaria de Governo\n" +
        		"Publicado na Secretaria de Governo, aos \n";
        	break;
        case "24":
        	texto = "((TEXTO))JOÃO DORIA, Governador do Estado de São Paulo, ";
        	break;
        case "25":
        	texto = "((BOLD))";
        	break;
        case "26":
        	texto = "((TEXTO))((BOLD))Decreto do Vice-Governador, em Exercício no Cargo de Governador do Estado, de ";
        	break;
        case "27":
        	texto = "Palácio dos Bandeirantes,\n" + 
        		"JOÃO DORIA\n" +
        		"((GRIFO))Rodrigo Garcia((CLARO))\n" +
        		"Secretário de Governo\n" +
        		"((GRIFO))Mauro Ricardo Machado Costa((CLARO))\n" +
        		"Secretário de Projetos, Orçamento e Gestão\n" +
        		"((GRIFO))Henrique de Campos Meirelles((CLARO))\n" +
        		"Secretário da Fazenda e Planejamento\n" +
        		"((GRIFO))Antonio Carlos Rizeque Malufe((CLARO))\n" +
        		"Secretário Executivo, Respondendo pelo Expediente da Casa Civil\n" +
        		"Publicado na Secretaria de Governo, aos ";
        	break;
        case "28":
        	texto = "((RETR, , , /01/2021, MARIANA))";
        	break;
        case "29":
        	texto = "Secretário-Adjunto, Respondendo pelo Expediente, de ";
        	break;
        case "30":
        	texto = "((11VP))";
        	break;
        case "31":
        	texto = ", vago em decorrência da promoção de ";
        	break;
        case "32":
        	texto = "((TEXTO))((BOLD))Decreto do Presidente da Assembléia Legislativa, em Exercício no Cargo de Governador do Estado, de ";
        	break;
        case "33":
        	texto = "((TEXTO))CAUÊ MACRIS, Presidente da Assembléia Legislativa, em Exercício no Cargo de Governador do Estado de São Paulo, ";
        	break;
        case "34":
        	texto = ", vago em decorrência da exoneração de ";
        	break;
    }
    $("#descrPublicacao").val(x.slice(0, curPos) + texto + x.slice(curPos));
    document.querySelector('#macro').value = '0';
}

function selectFunction(){  
    var x=1;   
    for(x=1;x<=34;x++){
	    var option = "<option value='" + x + "'>Macro" + x + "</option>"
	    document.getElementById('macro').innerHTML += option;   
    } 
} 
