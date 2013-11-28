<script language="javascript">;
var gConfiguracao;
var gCertificadoB64;
var gUtil;
var gAssinatura;
var gAtributoAssinavelDataHora
var gSigner;

var gCopia;
var gNome;
var gUrlDocumento;
var process = {
	    steps: [],
	    index: 0,
	    reset: function() {
	    	this.steps=[]; this.index = 0; 
		    },
	    push: function(x) {
		    this.steps.push(x);
		    },
		run: function() {
			this.dialogo = $('<div id="dialog-ad" title="Assinatura Digital"><p id="vbslog">Iniciando...</p><div id="progressbar-ad"></div></div>').dialog({
				       title: "Assinatura Digital",
				       width: '50%',
				       height: 'auto',
				       resizable: false,
				       autoOpen: true,
				       position: { my: "center top+25%", at: "center top", of: window },
				       modal: true,
				       closeText: "hide" 
			       });
		    this.progressbar = $('#progressbar-ad').progressbar();
		    this.nextStep();
			},
		finalize: function() {
			this.dialogo.dialog('destroy');
			},
	    nextStep: function(){
		    if (typeof this.steps[this.index] == 'string')
			    eval(this.steps[this.index++]);
		    else {
		        var ret = this.steps[this.index++]();
		        if ((typeof ret == 'string') && ret != "OK") {
			        this.finalize();
		           	alert(ret, 0, "Não foi possível completar a operação");
		     		return;
		        }
		    }

		    this.progressbar.progressbar("value", 100 * (this.index / this.steps.length));
	        
	        if (this.index != this.steps.length) {
	            var me = this;
	            window.setTimeout(function(){
	                me.nextStep();
	            }, 100);
	        } else {
		        this.finalize();
		    }
	    }
	};

function assinar(){
 var Assinatura;
 var Configuracao;
 
 try {
	Configuracao = new ActiveXObject("CAPICOM.Settings");
	Configuracao.EnablePromptForCertificateUI = true;
	Assinatura = new ActiveXObject("CAPICOM.SignedData");
	Util = new ActiveXObject("CAPICOM.Utilities");
 } catch(err) {
	  return Erro(err);
 }
 
 try {
	 Assinatura.Content = Util.Base64Decode(frm.conteudo_b64.value);
	 frm.assinaturaB64.value = Assinatura.Sign(undefined, true, 0);
 } catch(err) {
	  return Erro(err);
 }
 try {
	 var Assinante;
	 Assinante = Assinatura.Signers(1).Certificate.SubjectName;
	 Assinante = Assinante.split("CN=")[1];
	 Assinante = Assinante.split(",")[0];
	 frm.assinante.value = Assinante;
	 frm.conteudo_b64.value = "";
 } catch(err) {
	  return Erro(err);
 }
 frm.Submit();
}

function Erro(err) {
  alert("Ocorreu um erro durante o processo de assinatura: " + err.message);
}

function InicializarCapicom(){
	var Desprezar;
	try {
		gConfiguracao = new ActiveXObject("CAPICOM.Settings");
			gConfiguracao.EnablePromptForCertificateUI = true;
		gAssinatura = new ActiveXObject("CAPICOM.SignedData");
		gUtil = new ActiveXObject("CAPICOM.Utilities");
	} catch(err) {
		return Erro(err);
	}
	
	//Infelizmente não é possível armazenar o objeto Signer e utilizá-lo na próxima chamada para Sign.;
	//Renato: desabilitado temporariamente para não interferir com os testes de desempenho.;
	gAssinatura.Content = "Produz assinatura apenas para identificar o certificado a ser utilizado.";
	var nothing;
	gSigner = new ActiveXObject("CAPICOM.Signer");
	Desprezar = gAssinatura.Sign(gSigner, 1, 0);
	gCertificadoB64 = gAssinatura.Signers(1).Certificate.Export(0)
	return "OK";
}

function AssinarDocumento(conteudo){
	var ret = {};
	try { 
		gAssinatura.Content = conteudo;
		ret.assinaturaB64 = gAssinatura.Sign(gSigner, true, 0);
		var assinante = gAssinatura.Signers(1).Certificate.SubjectName;
		assinante = assinante.split("CN=")[1];
		assinante = assinante.split(",")[0];
		ret.assinante = assinante;
	} catch(err) {
		Erro(err);
		ret.status = err.message;
		return ret;
	}
	ret.status = "OK"
	return ret;
}

function AssinarDocumentos(Copia, oElm){
	TestCAPICOM();
	process.reset();

//   	oElm = document.getElementById(Id);
//    oElm.innerHTML = Caption;
    if(Copia == "true"){
  		Copia = "true";
		// alert("Iniciando conferência")
     	process.push(function(){Log("Iniciando conferência")});
 	}else{
  		Copia = "false";
		// alert("Iniciando assinatura")
     	process.push(function(){Log("Iniciando assinatura")});
    }

	process.push(InicializarCapicom);
    
    var oUrlPost, oNextUrl, oUrlBase, oUrlPath, oNome, oUrl, oChk;

	oUrlPost = document.getElementById("jspserver");
	if(oUrlPost == null){
        alert("element jspserver does not exist");
        return;
    }
    oUrlNext = document.getElementById("nexturl");
    if(oUrlNext == null){
        alert("element nexturl does not exist");
        return;
    }
    oUrlBase = document.getElementById("urlbase");
    if(oUrlBase == null){
        alert("element urlbase does not exist");
        return;
    }
    oUrlPath = document.getElementById("urlpath");
    if(oUrlPath == null){
        alert("element urlpath does not exist");
        return;
    }

    var Codigo;
    var NodeList = document.getElementsByTagName("INPUT");
    for (var i=0,len=NodeList.length; i<len; i++) {
        var Elem = NodeList[i];
       	if(Elem.name.substr(0,7) == "pdfchk_"){
		    Codigo = Elem.name.substr(7);
		    //alert(Codigo)
		
		    var oNome = document.getElementsByName("pdfchk_" + Codigo)[0];
		    if(oNome == null){
		        alert("element pdfchk_" + Codigo + " does ! exist");
		        return;
		    }
		    oUrl = document.getElementsByName("urlchk_" + Codigo)[0];
		    oChk = document.getElementsByName("chk_" + Codigo)[0];
		
			var b;
		    if(oChk == null){
		        b = true;
		    }else{
		        b = oChk.Checked;
		    } 
		
      		process.push("Copia=" + Copia + ";");
		    if(b){
				process.push("gNome='" + oNome.value + "'; gUrlDocumento = '" + oUrlBase.value + oUrlPath.value + oUrl.value + "&semmarcas=1'; if (typeof gCertificadoB64 != 'undefined'){gUrlDocumento = gUrlDocumento + '&certificadoB64=' + encodeURIComponent(gCertificadoB64);};");
	      		process.push(function(){Log(gNome + ": Buscando no servidor...")});
	      		process.push(function(){gDocumento = Conteudo(gUrlDocumento)});
	
	            var ret;
	      		process.push(function(){Log(gNome + ": Assinando...")});
	            process.push(function(){gRet = AssinarDocumento(gDocumento)});
	      		process.push(function(){Log(gNome + ": Gravando assinatura de " + gRet.assinante)});
	      		
	            process.push(function(){
		            var DadosDoPost = "sigla=" + encodeURIComponent(oNome.value) + "&copia=" + Copia + "&assinaturaB64=" + encodeURIComponent(gRet.assinaturaB64) + "&assinante=" + encodeURIComponent(gRet.assinante);
		            if (typeof gCertificadoB64 != 'undefined'){
		                 DadosDoPost = DadosDoPost + "&certificadoB64=" + encodeURIComponent(gCertificadoB64);
		                 DadosDoPost = DadosDoPost + "&atributoAssinavelDataHora=" + gAtributoAssinavelDataHora;
		            }
		
					//alert("oNome: " + oNome.value);
					var aNome = oNome.value.split(":");
					if(aNome.length == 1){
						//alert("id: " + aNome(1));
						DadosDoPost = "id=" + aNome[1] + "&" + DadosDoPost;
					}
			
			        Status = GravarAssinatura(oUrlPost.value, DadosDoPost);
			        return Status;
	    		});
		    }
       	}
	}
    process.push(function(){Log("Concluído, redirecionando...");});
    process.push(function(){location.href = oUrlNext.value;});
	process.run();
}

function Conteudo(url){
	//alert(url);
	objHTTP = new ActiveXObject("MSXML2.XMLHTTP");
	objHTTP.open("GET", url, false);
	objHTTP.send();
	
	if(objHTTP.Status == 200){
		gAtributoAssinavelDataHora = objHTTP.getResponseHeader("Atributo-Assinavel-Data-Hora");
	  	return objHTTP.responseBody;
	}
	return Erro("Não foi possível obter o conteúdo do documento a ser assinado.")
}

function GravarAssinatura(url, datatosend) {
	objHTTP = new ActiveXObject("MSXML2.XMLHTTP");
	objHTTP.Open("POST", url, false);
	objHTTP.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	objHTTP.send(datatosend); 
	
	GravarAssinatura = "Erro inespecífico.";
	if(objHTTP.Status == 200){
		var Conteudo, Inicio, Fim, Texto;
		//alert("OK, enviado");
		Conteudo = objHTTP.responseText;
		
        if (Conteudo.indexOf("gt-error-page-hd") != -1) {
			Inicio = Conteudo.indexOf("<h3>") + 4;
			Fim = Conteudo.indexOf("</h3>",Inicio);
			Texto = Conteudo.substr(Inicio, Fim - Inicio);
			return Texto;
        }
 	}
	return "OK";
}

//var intID 'a global variable

function Log(Text){
    var oLog;
   	oLog = document.getElementById("vbslog");
    if (oLog != null) {
     	oLog.innerHTML = Text;
  		//intID = window.setInterval("clearIntvl", 1000, "vbscript") ;
    }
}


//function clearIntvl {
//    Window.clearInterval intID 
//}

function TestCAPICOM() {
	try {
	oTest = new ActiveXObject("CAPICOM.Settings");
	} catch(err) {
		var oMissing = document.getElementById("capicom-missing");
        if (oMissing != null) {
            oMissing.style.display = "block";
        }
       	var oDiv = document.getElementById("capicom-div");
        if (oDiv != null) {
            oDiv.style.display = "none";
        }
	}
}


</script>