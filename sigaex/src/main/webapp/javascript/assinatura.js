var gCertificadoB64;
var gUtil;
var gAssinatura;
var gAtributoAssinavelDataHora
var gSigner;
var gTecnologia;
var gPolitica;
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
			window.scrollTo(0, 0);
			this.dialogo = $('<div id="dialog-ad" title="Assinatura Digital"><p id="vbslog">Iniciando...</p><div id="progressbar-ad"></div></div>').dialog({
				title: "Assinatura Digital (" + gTecnologia + ")",
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

function Erro(err) {
	alert("Ocorreu um erro durante o processo de assinatura: " + err.message);
	return "Ocorreu um erro durante o processo de assinatura: " + err.message;
}

var ittruSignAx;

function TestarAssinaturaDigital() {
	try {
		if (ittruSignAx == undefined) {
			ittruSignAx = new ActiveXObject("ittru");
			gTecnologia = "Ittru ActiveX";
		}
	} catch(Err) {
		gPolitica = false;
		if (window.navigator.userAgent.indexOf("MSIE ") > 0 || window.navigator.userAgent.indexOf(" rv:11.0") > 0) {
			return TestCAPICOM();
		}
	}
}

function InicializarAssinaturaDigital() {
	if (gTecnologia == 'Ittru ActiveX') {
		try
		{
			gCertificadoB64 = ittruSignAx.getCertificate('Assinatura Digital', 'Escolha o certificado que será utilizado na assinatura.', 'ICP-Brasil', '');
			return "OK";

		}
		catch(Err)
		{
			return Err.description;
		}
	} else {
		return InicializarCapicom();
	}
}

function AssinarDigitalmente(conteudo) {
	if (gTecnologia == 'Ittru ActiveX') {
		var ret = {};
		try {
//			alert(conteudo);
//			gAssinatura.Content = gUtil.Base64Decode(conteudo)
			if (gPolitica) {
				if (ittruSignAx.getKeySize() >= 2048) {
					ret.assinaturaB64 = ittruSignAx.sign("sha256", conteudo);
				} else {
					ret.assinaturaB64 = ittruSignAx.sign("sha1", conteudo);
				}
			} else {
				ret.assinaturaB64 = ittruSignAx.sign("PKCS7", conteudo);
			}
//			alert(ret.assinaturaB64);
			ret.assinante = ittruSignAx.getSubject();

			var re = /CN=([^,]+),/gi;
			var m;
			if ((m = re.exec(ret.assinante)) != null) {
				ret.assinante = m[1];
			}
			ret.status = "OK"
				return ret;
		} catch(err) {
			Erro(err);
			ret.status = err.message;
			return ret;
		}
	} else {
		return AssinarDocumento(conteudo);
	}
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

	gSigner = new ActiveXObject("CAPICOM.Signer");

	if ($("#acessoCertificadoDigital").val() === "true"){
		var oStore = new ActiveXObject("CAPICOM.Store");
		oStore.Open(2, "My", 0);

		var oCertificates = oStore.Certificates.Find(2, "ICP-Brasil", true);

		if (oCertificates.Count > 1) {
			oCertificates = oCertificates.Select(
					"Seleção de Certificado Digital",
					"Escolha o certificado digital que deseja utilizar para assinar ou autenticar:", false);
		}

		if (oCertificates.Count == 0) {
			return "Nenhum certificado digital disponível.";
		}

		gSigner.Certificate = oCertificates.Item(1);

		if (gPolitica) {
			gCertificadoB64 = gSigner.Certificate.Export(0);
		}
		if (gPolitica) {
			if (typeof gCertificadoB64 == 'undefined') {
				alert("Atenção: Produzindo assinatura apenas para obter o certificado.");
				gAssinatura.Content = "Produz assinatura apenas para identificar o certificado a ser utilizado.";
				var nothing;
				gSigner = new ActiveXObject("CAPICOM.Signer");
				Desprezar = gAssinatura.Sign(gSigner, 1, 0);
				gCertificadoB64 = gAssinatura.Signers(1).Certificate.Export(0);
			}
		}
	}
	return "OK";
}

function Conteudo(url){
//	alert(url);

	//objHTTP = new ActiveXObject("MSXML2.XMLHTTP");
	//objHTTP.open("GET", url, false);
	//objHTTP.send();

	//if(objHTTP.Status == 200){
	var Conteudo, Inicio, Fim, Texto, err;
	//alert("OK, enviado");
	//Conteudo = objHTTP.responseText;

	//Conteudo = objHTTP.responseText;
	if (gTecnologia == 'Ittru ActiveX') {
		$.ajax({
			type:		"GET",
			url: 		url,
			dataType:   "text",
			accepts:	{text: "text/vnd.siga.b64encoded"},
			success: 	function(data, textStatus, XMLHttpRequest){
				//alert(data);
				//alert(XMLHttpRequest.getResponseHeader('Atributo-Assinavel-Data-Hora'));
				gAtributoAssinavelDataHora = XMLHttpRequest.getResponseHeader('Atributo-Assinavel-Data-Hora');
				Conteudo = data;
			},
			error:     function(jqXHR, textStatus, errorThrown) {
				err = textStatus + ": " + errorThrown;
			},
			async:   	false,
			cache:		false
		});

	} else  {
		Conteudo = VBConteudo(url);
	}

	if (err != undefined)
		return err;

//	alert(Conteudo);
//	alert(gVBAtributoAssinavelDataHora);
//	alert(gVBConteudoArray);

//	alert(Conteudo);

	if (Conteudo.indexOf("gt-error-page-hd") != -1) {
		Inicio = Conteudo.indexOf("<h3>") + 4;
		Fim = Conteudo.indexOf("</h3>",Inicio);
		Texto = Conteudo.substr(Inicio, Fim - Inicio);
		return "Não foi possível obter o conteúdo do documento a ser assinado: " + Texto;
	}

	if (gTecnologia == 'Capicom') {
		Conteudo = gVBConteudoArray;
		//gAtributoAssinavelDataHora = objHTTP.getResponseHeader("Atributo-Assinavel-Data-Hora");
		gAtributoAssinavelDataHora = gVBAtributoAssinavelDataHora;
	}

	//alert(Conteudo);
	return Conteudo;
	//}
	return "Não foi possível obter o conteúdo do documento a ser assinado.";
}

function AssinarDocumento(conteudo){
	var ret = {};
	try {
//		alert(conteudo);
//		gAssinatura.Content = gUtil.Base64Decode(conteudo)
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
	ret.status = "OK";
	return ret;
}

function AssinarDocumentos(Copia, oElm){
	TestarAssinaturaDigital();
	process.reset();

//	oElm = document.getElementById(Id);
//	oElm.innerHTML = Caption;
	if(Copia == "true"){
		Copia = "true";
		// alert("Iniciando conferência")
		process.push(function(){Log("Iniciando conferência")});
	}else{
		Copia = "false";
		// alert("Iniciando assinatura")
		process.push(function(){Log("Iniciando assinatura")});
	}

	process.push(InicializarAssinaturaDigital);

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

	if (!location.origin){
		location.origin = location.protocol + "//" + location.host;
	}

	oUrlBase = location.origin;
	if(oUrlBase == null){
		alert("element urlbase does not exist");
		return;
	}
	oUrlPath = document.getElementById("urlpath");
	if(oUrlPath == null){
		alert("element urlpath does not exist");
		return;
	}

	//oUrlPost.value = removerPortaSeNaoForLocalhost(oUrlPost.value);
	//oUrlNext.value = removerPortaSeNaoForLocalhost(oUrlNext.value);

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
				b = oChk.checked;
			}

			process.push("Copia=" + Copia + ";");
			if(b){
				process.push("gNome='" + oNome.value + "'; gUrlDocumento = '" + oUrlBase + oUrlPath.value + oUrl.value + "&semmarcas=1'; if (gPolitica){gUrlDocumento = gUrlDocumento + '&certificadoB64=' + encodeURIComponent(gCertificadoB64);};");
				process.push(function(){Log(gNome + ": Buscando no servidor...")});
				process.push(function(){gDocumento = Conteudo(gUrlDocumento); if (typeof gDocumento == "string" && gDocumento.indexOf("Não") == 0) return gDocumento;});

				var ret;
				process.push(function(){Log(gNome + ": Assinando...")});
				process.push(function(){gRet = AssinarDigitalmente(gDocumento)});
				process.push(function(){Log(gNome + ": Gravando assinatura de " + gRet.assinante)});

				process.push(function(){
					// Nato: Acrescentei um tramitar = false para garantir que assinatura em lote não provoque trâmite. O ideal é remover esse JS e a página antiga de assinatura em lote.
					var DadosDoPost = "tramitar=false&sigla=" + encodeURIComponent(gNome) + "&copia=" + Copia + "&assinaturaB64=" + encodeURIComponent(gRet.assinaturaB64) + "&assinante=" + encodeURIComponent(gRet.assinante);
					if (gPolitica){
						DadosDoPost = DadosDoPost + "&certificadoB64=" + encodeURIComponent(gCertificadoB64);
						DadosDoPost = DadosDoPost + "&atributoAssinavelDataHora=" + gAtributoAssinavelDataHora;
					}

					//alert("oNome: " + oNome.value);
					var aNome = gNome.split(":");
					if(aNome.length == 2){
						//alert("id: " + aNome(1));
						DadosDoPost = "id=" + aNome[1] + "&" + DadosDoPost;
					}

					if (gRet.assinante == null || gRet.assinante == 'undefined'){
						return "O assinante não foi definido";
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

function AssinarDocumentosSenha(Copia, oElm){
	process.reset();
	gTecnologia = "Com Senha";

//	oElm = document.getElementById(Id);
//	oElm.innerHTML = Caption;
	if(Copia == "true"){
		Copia = "true";
		// alert("Iniciando conferência")
		process.push(function(){Log("Iniciando conferência")});
	}else{
		Copia = "false";
		// alert("Iniciando assinatura")
		process.push(function(){Log("Iniciando assinatura")});
	}

	var oUrlPost, oNextUrl, oUrlBase, oUrlPath, oNome, oUrl, oChk;

	oUrlPost = document.getElementById("jspServerSenha");
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
				b = oChk.checked;
			}

			process.push("Copia=" + Copia + ";");
			if(b){
				var ret;
				process.push("gNome='" + oNome.value + "'; gUrlDocumento = '" + oUrlBase.value + oUrlPath.value + oUrl.value + "&semmarcas=1';");
				process.push(function(){Log(gNome + ": Assinando...")});
				process.push(function(){Log(gNome + ": Gravando assinatura de " + $("#nomeUsuarioSubscritor").val())});

				process.push(function(){
					var DadosDoPost = "tramitar=false&sigla=" + encodeURIComponent(gNome) + "&copia=" + Copia + "&nomeUsuarioSubscritor=" + encodeURIComponent($("#nomeUsuarioSubscritor").val()) + "&senhaUsuarioSubscritor=" + encodeURIComponent($("#senhaUsuarioSubscritor").val());

					//alert("oNome: " + oNome.value);
					var aNome = gNome.split(":");
					if(aNome.length == 2){
						//alert("id: " + aNome(1));
						DadosDoPost = "id=" + aNome[1] + "&" + DadosDoPost;
					}

					Status = GravarAssinaturaSenha(oUrlPost.value, DadosDoPost);
					return Status;
				});
			}
		}
	}

	process.push(function(){Log("Concluído, redirecionando...");});
	process.push(function(){location.href = oUrlNext.value;});
	process.run();
}


function GravarAssinatura(url, datatosend) {
	objHTTP = new ActiveXObject("MSXML2.XMLHTTP");
	objHTTP.Open("POST", url, false);
	objHTTP.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	objHTTP.send(datatosend);

	if(objHTTP.Status == 200){
		var Conteudo, Inicio, Fim, Texto;
		//alert("OK, enviado");
		Conteudo = objHTTP.responseText;

		//Edson: adicionei a segunda sentença ao if abaixo porque, no caso da assinatura externa, a página
		//de retorno é esta mesma, que obviamente já tem o texto gt-error-page-hd (na linha abaixo) sem
		//significar que seja uma página de erro
		if (Conteudo.indexOf("gt-error-page-hd") != -1 && Conteudo.indexOf("function GravarAssinatura") < 0) {
			Inicio = Conteudo.indexOf("<h3>") + 4;
			Fim = Conteudo.indexOf("</h3>",Inicio);
			Texto = Conteudo.substr(Inicio, Fim - Inicio);
			return Texto;
		}
		if (Conteudo.indexOf("ERRO -", 0) === 0) {
			Texto = Conteudo.substring(7);
			return Texto;
		}
	}
	return "OK";
}


function GravarAssinaturaSenha(url, datatosend) {
	retorno = "OK";
	$.ajax({
		url: url,
		type: "POST",
		data: datatosend,
		async:  false,
		statusCode: {
			200: function(Conteudo) {
				if (Conteudo.indexOf("gt-error-page-hd") != -1 && Conteudo.indexOf("function GravarAssinatura") < 0) {
					Inicio = Conteudo.indexOf("<h3>") + 4;
					Fim = Conteudo.indexOf("</h3>",Inicio);
					retorno = Conteudo.substr(Inicio, Fim - Inicio);
					return retorno;
				}
				if (Conteudo.indexOf("ERRO -", 0) === 0) {
					retorno = Conteudo.substring(7);
					return retorno;
				}
			}}
	});

	return retorno;
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
//Window.clearInterval intID
//}

function TestCAPICOM() {
	try {
		oTest = new ActiveXObject("CAPICOM.Settings");
		gTecnologia = "Capicom";
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

//see doc on http://msdn.microsoft.com/en-us/library/ms535874(VS.85).aspx
function getXMLHttpRequest()
{
	if (window.XMLHttpRequest) {
		return new window.XMLHttpRequest;
	}
	else {
		try {
			return new ActiveXObject("MSXML2.XMLHTTP");
		}
		catch(ex) {
			return null;
		}
	}
}

if((window.navigator.userAgent.indexOf("MSIE ") > 0 || window.navigator.userAgent.indexOf(" rv:11.0") > 0) && !/opera/i.test(navigator.userAgent)) {
	var VBConteudo_Script =
		'<!-- VBConteudo -->\r\n'+
		'<script type="text/vbscript">\r\n'+
		'Dim gVBConteudoArray, gVBAtributoAssinavelDataHora\r\n'+
		'Function VBConteudo(url)\r\n'+
		'	Set objHTTP = CreateObject("MSXML2.XMLHTTP")\r\n'+
		'	objHTTP.open "GET", url, False\r\n'+
		'	objHTTP.send\r\n'+
		'	If objHTTP.Status = 200 Then\r\n'+
		'	    gVBAtributoAssinavelDataHora = objHTTP.getResponseHeader("Atributo-Assinavel-Data-Hora")\r\n'+
		'	    VBConteudo = objHTTP.responseText\r\n'+
		'	    gVBConteudoArray = objHTTP.responseBody\r\n'+
		'	End If\r\n'+
		'End Function\r\n'+
		'\<\/script>\r\n';

	// inject VBScript
	document.write(VBConteudo_Script);
}


/*
if((window.navigator.userAgent.indexOf("MSIE ") > 0 || window.navigator.userAgent.indexOf(" rv:11.0") > 0) && !/opera/i.test(navigator.userAgent)) {
    var IEBinaryToArray_ByteStr_Script =
    "<!-- IEBinaryToArray_ByteStr -->\r\n"+
    "<script type='text/vbscript'>\r\n"+
    "Function IEBinaryToArray_ByteStr(Binary)\r\n"+
    "   IEBinaryToArray_ByteStr = CStr(Binary)\r\n"+
    "End Function\r\n"+
    "Function IEBinaryToArray_ByteStr_Last(Binary)\r\n"+
    "   Dim lastIndex\r\n"+
    "   lastIndex = LenB(Binary)\r\n"+
    "   if lastIndex mod 2 Then\r\n"+
    "       IEBinaryToArray_ByteStr_Last = Chr( AscB( MidB( Binary, lastIndex, 1 ) ) )\r\n"+
    "   Else\r\n"+
    "       IEBinaryToArray_ByteStr_Last = "+'""'+"\r\n"+
    "   End If\r\n"+
    "End Function\r\n"+
    "\<\/script>\r\n";

    // inject VBScript
    document.write(IEBinaryToArray_ByteStr_Script);
}

// this fn is invoked if IE
function IeBinFileReaderImpl(fileURL){
    this.req = getXMLHttpRequest();
    this.req.open("GET", fileURL, true);
    this.req.setRequestHeader("Accept-Charset", "x-user-defined");
    this.req.send();

    // my helper to convert from responseBody to a "responseText" like thing
    var convertResponseBodyToText = function (binary) {
        var byteMapping = {};
        for ( var i = 0; i < 256; i++ ) {
            for ( var j = 0; j < 256; j++ ) {
                byteMapping[ String.fromCharCode( i + j * 256 ) ] =
                    String.fromCharCode(i) + String.fromCharCode(j);
            }
        }
        // call into VBScript utility fns
        var rawBytes = IEBinaryToArray_ByteStr(binary);
        var lastChr = IEBinaryToArray_ByteStr_Last(binary);
        return rawBytes.replace(/[\s\S]/g,
                                function( match ) { return byteMapping[match]; }) + lastChr;
    };

	if(this.req.Status == 200){
		// this doesn't work
		//fileContents = that.req.responseBody.toArray();

		// this doesn't work
		//fileContents = new VBArray(that.req.responseBody).toArray();

		// this works...
		var fileContents = convertResponseBodyToText(that.req.responseBody);

		fileSize = fileContents.length-1;
		if(that.fileSize < 0) throwException(_exception.FileLoadFailed);
		that.readByteAt = function(i){
		    return fileContents.charCodeAt(i) & 0xff;
		};
    };
}

function Conteudo2(url){
	alert(url);
	objHTTP = new ActiveXObject("MSXML2.XMLHTTP");
	objHTTP.open("GET", url, false);
	objHTTP.send();

    // my helper to convert from responseBody to a "responseText" like thing
    var convertResponseBodyToText = function (binary) {
        var byteMapping = {};
        for ( var i = 0; i < 256; i++ ) {
            for ( var j = 0; j < 256; j++ ) {
                byteMapping[ String.fromCharCode( i + j * 256 ) ] =
                    String.fromCharCode(i) + String.fromCharCode(j);
            }
        }
        // call into VBScript utility fns
        var rawBytes = IEBinaryToArray_ByteStr(binary);
        var lastChr = IEBinaryToArray_ByteStr_Last(binary);
        return rawBytes.replace(/[\s\S]/g,
                                function( match ) { return byteMapping[match]; }) + lastChr;
    };

	if(objHTTP.Status == 200){
		var Conteudo, Inicio, Fim, Texto;
		//alert("OK, enviado");

		// this doesn't work
		//Conteudo = objHTTP.responseText;

		// this doesn't work
		//Conteudo = that.req.responseBody.toArray();

		// this doesn't work
		//Conteudo = new VBArray(that.req.responseBody).toArray();

		// this works...
		Conteudo = convertResponseBodyToText(objHTTP.responseBody);

		//fileSize = Conteudo.length-1;
		//if(that.fileSize < 0) throwException(_exception.FileLoadFailed);
		//that.readByteAt = function(i){
		    //return fileContents.charCodeAt(i) & 0xff;
		//};

        if (Conteudo.indexOf("gt-error-page-hd") != -1) {
			Inicio = Conteudo.indexOf("<h3>") + 4;
			Fim = Conteudo.indexOf("</h3>",Inicio);
			Texto = Conteudo.substr(Inicio, Fim - Inicio);
			return "Não foi possível obter o conteúdo do documento a ser assinado: " + Texto;
        }

		gAtributoAssinavelDataHora = objHTTP.getResponseHeader("Atributo-Assinavel-Data-Hora");
	  	return Conteudo;
	}
	return "Não foi possível obter o conteúdo do documento a ser assinado.";
}



// this fn is invoked if non IE
function NormalBinFileReaderImpl(fileURL){
    this.req = new XMLHttpRequest();
    this.req.open('GET', fileURL, true);
    this.req.onreadystatechange = function(aEvt) {
        if (that.req.readyState == 4) {
            if(that.req.status == 200){
                var fileContents = that.req.responseText;
                fileSize = fileContents.length;

                that.readByteAt = function(i){
                    return fileContents.charCodeAt(i) & 0xff;
                }
                if (typeof callback == "function"){ callback(that);}
            }
            else
                throwException(_exception.FileLoadFailed);
        }
    };
    //XHR binary charset opt by Marcus Granado 2006 [http://mgran.blogspot.com]
    this.req.overrideMimeType('text/plain; charset=x-user-defined');
    this.req.send(null);
}
 */

/**
 *
 *  Base64 encode / decode
 *  http://www.webtoolkit.info/
 *
 **/
var Base64 = {

//		private property
		_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

//		public method for encoding
		encode : function (input) {
			var output = "";
			var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
			var i = 0;

			input = Base64._utf8_encode(input);

			while (i < input.length) {

				chr1 = input.charCodeAt(i++);
				chr2 = input.charCodeAt(i++);
				chr3 = input.charCodeAt(i++);

				enc1 = chr1 >> 2;
				enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
				enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
				enc4 = chr3 & 63;

				if (isNaN(chr2)) {
					enc3 = enc4 = 64;
				} else if (isNaN(chr3)) {
					enc4 = 64;
				}

				output = output +
				this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
				this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);

			}

			return output;
		},

//		public method for decoding
		decode : function (input) {
			var output = "";
			var chr1, chr2, chr3;
			var enc1, enc2, enc3, enc4;
			var i = 0;

			input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

			while (i < input.length) {

				enc1 = this._keyStr.indexOf(input.charAt(i++));
				enc2 = this._keyStr.indexOf(input.charAt(i++));
				enc3 = this._keyStr.indexOf(input.charAt(i++));
				enc4 = this._keyStr.indexOf(input.charAt(i++));

				chr1 = (enc1 << 2) | (enc2 >> 4);
				chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
				chr3 = ((enc3 & 3) << 6) | enc4;

				output = output + String.fromCharCode(chr1);

				if (enc3 != 64) {
					output = output + String.fromCharCode(chr2);
				}
				if (enc4 != 64) {
					output = output + String.fromCharCode(chr3);
				}

			}

			output = Base64._utf8_decode(output);

			return output;

		},

//		private method for UTF-8 encoding
		_utf8_encode : function (string) {
			string = string.replace(/\r\n/g,"\n");
			var utftext = "";

			for (var n = 0; n < string.length; n++) {

				var c = string.charCodeAt(n);

				if (c < 128) {
					utftext += String.fromCharCode(c);
				}
				else if((c > 127) && (c < 2048)) {
					utftext += String.fromCharCode((c >> 6) | 192);
					utftext += String.fromCharCode((c & 63) | 128);
				}
				else {
					utftext += String.fromCharCode((c >> 12) | 224);
					utftext += String.fromCharCode(((c >> 6) & 63) | 128);
					utftext += String.fromCharCode((c & 63) | 128);
				}

			}

			return utftext;
		},

//		private method for UTF-8 decoding
		_utf8_decode : function (utftext) {
			var string = "";
			var i = 0;
			var c = c1 = c2 = 0;

			while ( i < utftext.length ) {

				c = utftext.charCodeAt(i);

				if (c < 128) {
					string += String.fromCharCode(c);
					i++;
				}
				else if((c > 191) && (c < 224)) {
					c2 = utftext.charCodeAt(i+1);
					string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
					i += 2;
				}
				else {
					c2 = utftext.charCodeAt(i+1);
					c3 = utftext.charCodeAt(i+2);
					string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
					i += 3;
				}

			}

			return string;
		}

}

$(function() {

	gPolitica = ($("#gPolitica").val() === "true");

});
