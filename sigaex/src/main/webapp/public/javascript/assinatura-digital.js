var gConfiguracao;
var gCertificadoB64;
var gUtil;
var gAssinatura;
var gAtributoAssinavelDataHora
var gSigner;
var gTecnologia;
var gCertAlias;
var gStore = "0";
var gPIN;
var gPolitica = false;
var gCopia;
var gNome;
var gUrlDocumento;
var gUrlPost;
var gOperacoes;
var gLogin;
var gPassword;

var gAssinando = false;

var ittruSignAx;
var ittruSignApplet;

var provider;

// Testa os recursos disponíveis e seleciona o melhor provider
//

function selecionarProvider() {
	for (i = 0; i < providers.length; i++) {
		if (providers[i].testar()) {
			return providers[i];
		}
	}
	Erro({
		message : "Nenhum método de assinatura está disponível."
	});
}

function TestarAssinaturaDigital() {
	// console.log("Testando provedores de assinatura digital.");
	provider = selecionarProvider();
	return provider != undefined;
}

// Inicia a operação de assinatura para todos os documentos referenciados na
// pagina
//
function AssinarDocumentos(copia, politica, juntar, tramitar, exibirNoProtocolo) {
	if (gAssinando)
		return;
	gAssinando = true;
	
	if (politica != undefined)
		gPolitica = politica;

	identificarOperacoes();

	var tipo = verificarTipoDeAssinatura();
	
	if (tipo == 0) {
		window.alert("Antes de assinar ou autenticar, é necessário selecionar pelo menos um documento.");
		gAssinando = false;
		return;
	}

	if (tipo == 1 || tipo == 3) {
		if (!TestarAssinaturaDigital()) {
			gAssinando = false;
			return;
		}
	}

	if (tipo == 1) {
		if ("OK" == provider.inicializar(function() {
			ExecutarAssinarDocumentos(copia, juntar, tramitar, exibirNoProtocolo);
		})) {
			ExecutarAssinarDocumentos(copia, juntar, tramitar, exibirNoProtocolo);
		}
	}

	if (tipo == 2) {
		provider = providerPassword;
		providerPassword.inicializar(function() {
			ExecutarAssinarDocumentos(copia, juntar, tramitar, exibirNoProtocolo);
		});
	}

	if (tipo == 3) {
		providerPassword.inicializar(function() {
			if ("OK" == provider.inicializar(function() {
				ExecutarAssinarDocumentos(copia, juntar, tramitar, exibirNoProtocolo);
			})) {
				ExecutarAssinarDocumentos(copia, juntar, tramitar, exibirNoProtocolo);
			}
		});
	}
	
	if (tipo == 4) {
		provider = providerPIN;
		provider.inicializar(function() {
			ExecutarAssinarDocumentos(copia, juntar, tramitar);
		});
	}
}

//
// Provider: AssijusPopup
//
var providerAssijusPopup = {
	nome : 'AssijusPopup',
	list : [],
	errormsg : [],

	testar : function() {
		if (window.produzirAssinaturaDigital === undefined)
			return false;
		// please note,
		// that IE11 now returns undefined again for window.chrome
		// and new Opera 30 outputs true for window.chrome
		// and new IE Edge outputs to true now for window.chrome
		// and if not iOS Chrome check
		// so use the below updated condition
		var isChromium = window.chrome, winNav = window.navigator, vendorName = winNav.vendor, isOpera = winNav.userAgent
				.indexOf("OPR") > -1, isIEedge = winNav.userAgent
				.indexOf("Edge") > -1, isIOSChrome = winNav.userAgent
				.match("CriOS");

		if (isIOSChrome) {
			// is Google Chrome on IOS
		} else if (isChromium !== null && isChromium !== undefined
				&& vendorName === "Google Inc." && isOpera == false
				&& isIEedge == false) {
			// is Google Chrome
			return true;
		} else {
			// not Google Chrome
		}
		return false;
	},

	inicializar : function() {
		this.list = [];
		return "OK";
	},

	assinar : function(signable) {
		var item = {
			id : signable.id,
			system : "sigadocsigner",
			extra : signable.extra,
			code : signable.code,
			descr : signable.descr,
			kind : signable.kind,
			origin : "Siga-Doc"
		}
		this.list.push(item);
		return;
	},

	concluir : function(urlRedirect) {
		var parent = this;
		window.produzirAssinaturaDigital({
			ui: 'bootstrap-3',

			docs: this.list,
			
			errormsg: [],

			hashCallback: function(id, cont) {
				console.log(id, 'hash')
				var errormsg = this.errormsg;
				$.ajax({
					url : "/sigaex/app/assinador-popup/doc/" + id + "/hash",
					type : "POST",
					async : false,
					success : function(xhr) {
						console.log(xhr)
						cont({sha1: xhr.sha1, sha256: xhr.sha256});
					},
					error : function(xhr) {
						errormsg.push(id + " - Erro calculando hash de documento: " + xhr.responseJSON.errormsg);
						cont({});
					}
				});
			},
			
			saveCallback: function(id, sign, cont) {
				console.log(id, 'sign')
				console.log(sign)
				var errormsg = this.errormsg;
				$.ajax({
					url : "/sigaex/app/assinador-popup/doc/" + id + "/sign",
					type : "PUT",
					contentType: "application/json",
					data : JSON.stringify(sign),
					async : false,
					success : function(xhr) {
						console.log(xhr)
						cont({success: true});
					},
					error : function(xhr) {
						console.log(xhr);
						errormsg.push(id + " - Erro na gravação da assinatura: " + xhr.responseJSON.errormsg);
						cont({});
					}
				});
			},
			
			errorCallback: function(id, err, cont) {
				errormsg.push(id + " - Erro na gravação da assinatura. " + err);
				cont();
			},
			
			endCallback: function() {
				gAssinando = false;
				if (this.errormsg.length > 0)
					window.alert(this.errormsg.join(', '));
				else
					window.location.href = urlRedirect;
			}
		});
	}
}

//
// Provider: Assijus
//
var providerAssijus = {
	nome : 'Assijus',
	endpoint : location.hostname == "siga.jfrj.jus.br" ? "https://assijus.jfrj.jus.br/assijus"
			: "/assijus",
	list : [],

	testar : function() {
		// please note,
		// that IE11 now returns undefined again for window.chrome
		// and new Opera 30 outputs true for window.chrome
		// and new IE Edge outputs to true now for window.chrome
		// and if not iOS Chrome check
		// so use the below updated condition
		var isChromium = window.chrome, winNav = window.navigator, vendorName = winNav.vendor, isOpera = winNav.userAgent
				.indexOf("OPR") > -1, isIEedge = winNav.userAgent
				.indexOf("Edge") > -1, isIOSChrome = winNav.userAgent
				.match("CriOS");

		if (isIOSChrome) {
			// is Google Chrome on IOS
		} else if (isChromium !== null && isChromium !== undefined
				&& vendorName === "Google Inc." && isOpera == false
				&& isIEedge == false) {
			// is Google Chrome
			return true;
		} else {
			// not Google Chrome
		}
		return false;
	},

	inicializar : function() {
		this.list = [];
		return "OK";
	},

	assinar : function(signable) {
		var item = {
			id : signable.id,
			system : "sigadocsigner",
			extra : signable.extra,
			code : signable.code,
			descr : signable.descr,
			kind : signable.kind,
			origin : "Siga-Doc"
		}
		this.list.push(item);
		return;
	},

	concluir : function(urlRedirect) {
		var parent = this;
		$.ajax({
			url : this.endpoint + "/api/v1/store",
			type : "POST",
			data : {
				payload : JSON.stringify({
					list : this.list
				})
			},
			async : false,
			success : function(xhr) {
				window.location.href = parent.endpoint
						+ "/?endpointautostart=true&endpointlistkey=" + xhr.key
						+ "&endpointcallback=" + encodeURI(urlRedirect);
			},
			error : function(xhr) {
				gAssinando = false;
				result = "Erro na gravação da assinatura. " + xhr.responseText;
			}
		});
	}
}

//
// Provider: Ittru ActiveX
//
var providerIttruAx = {
	nome : 'Ittru ActiveX',

	testar : function() {
		try {
			// return false;
			if (ittruSignAx == undefined) {
				ittruSignAx = new ActiveXObject("ittru");
			}
			// console.log("IttruAx: OK!");
			return true;
		} catch (err) {
			// console.log("IttruAx:" + err.message);
		}
		return false;
	},

	inicializar : function() {
		try {
			gCertificadoB64 = ittruSignAx.getCertificate('Assinatura Digital',
					'Escolha o certificado que será utilizado na assinatura.',
					'ICP-Brasil', '');
			return "OK";

		} catch (Err) {
			return Err.description;
		}
	},

	assinar : function(conteudo) {
		var ret = {};
		try {
			// alert(conteudo);
			// gAssinatura.Content = gUtil.Base64Decode(conteudo)
			if (gPolitica) {
				if (ittruSignAx.getKeySize() >= 2048) {
					ret.assinaturaB64 = ittruSignAx.sign("sha256", conteudo);
				} else {
					ret.assinaturaB64 = ittruSignAx.sign("sha1", conteudo);
				}
			} else {
				ret.assinaturaB64 = ittruSignAx.sign("PKCS7", conteudo);
			}
			// alert(ret.assinaturaB64);
			ret.assinante = ittruSignAx.getSubject();

			var re = /CN=([^,]+),/gi;
			var m;
			if ((m = re.exec(ret.assinante)) != null) {
				ret.assinante = m[1];
			}
			ret.status = "OK"
			return ret;
		} catch (err) {
			Erro(err);
			ret.status = err.message;
			return ret;
		}
	}
}

//
// Provider: Ittru CAPI
//
var providerIttruCAPI = {
	nome : 'Ittru CAPI',

	testar : function() {
		return false;
		try {
			var to = typeof (document.getElementById("signerCAPI"));
			if (to == 'function' || to == 'object') {
				if (document.signerCAPI.isActive()) {
					ittruSignApplet = document.signerCAPI;
					// console.log("IttruCAPI: OK!");
					return true;
				} else {
					// console.log("IttruCAPI:" + document.signerCAPI.getMsg());
				}
			}
		} catch (err) {
			// console.log("IttruCAPI:" + err.message);
		}
		return false;
	},

	inicializar : function() {
		try {
			gCertificadoB64 = ittruSignApplet.getCertificate('Certificados',
					'Selecione para assinatura', '', '');
			return "OK";

		} catch (Err) {
			return Err.description;
		}
	},

	assinar : function(conteudo) {
		var ret = {};
		try {
			// alert(conteudo);
			// gAssinatura.Content = gUtil.Base64Decode(conteudo)

			var keySize = ittruSignApplet.getKeySize();

			if (gPolitica) {
				var alg = 'sha1';

				if (keySize < 2048) {
					// alert("assinando sha1");
					ret.assinaturaB64 = ittruSignApplet.sign(0, conteudo);
				} else {
					alg = 'sha256';
					// alert("assinando sha256");
					ret.assinaturaB64 = ittruSignApplet.sign(2, conteudo);
				}
			} else {
				// alert("assinando pkcs7");
				ret.assinaturaB64 = ittruSignApplet.sign(99, conteudo);
			}
			// alert(ret.assinaturaB64);

			ret.assinante = ittruSignApplet.getSubject();

			// alert(ret.assinante);

			var re = /CN=([^,]+),/gi;
			var m;
			if ((m = re.exec(ret.assinante)) != null) {
				ret.assinante = m[1];
			}
			ret.status = "OK"
			return ret;
		} catch (err) {
			Erro(err);
			ret.status = err.message;
			return ret;
		}
	}
}

//
// Provider: Ittru PKCS#11
//
var providerIttruP11 = {
	nome : 'Ittru PKCS#11',

	testar : function() {
		try {
			var to = typeof (document.getElementById("signer"));
			if (to == 'function' || to == 'object') {
				if (document.signer.isActive()) {
					ittruSignApplet = document.signer;
					// console.log("IttruP11: OK!");
					return true;
				} else {
					// console.log("IttruP11:" + document.signer.getMsg());
				}
			}
		} catch (err) {
			// console.log("IttruP11:" + err.message);
		}
		return false;
	},

	inicializar : function(cont) {
		try {
			this.dialog = $(
					'<div id="dialog-form-pin" title="Assinar com token"><fieldset><label>PIN</label> <br/><input type="password" name="pin" id="pin" class="text ui-widget-content ui-corner-all" autocomplete="off"/></fieldset><fieldset><div id="certChoice"/></fieldset></div>')
					.dialog(
							{
								title : "Assinatura Digital (" + this.nome
										+ ")",
								width : '50%',
								height : 'auto',
								resizable : false,
								autoOpen : true,
								position : {
									my : "center top+25%",
									at : "center top",
									of : window
								},
								modal : true,
								closeText : "hide",
								buttons : {
									"Assinar" : function() {
										if ("OK" == providerIttruP11
												.escolherCertificado(this, cont)) {
											$(this).dialog('destroy').remove();
											cont();
										}
									},
									"Cancelar" : function() {
										$(this).dialog('destroy').remove();
									}
								},
								close : function() {

								},
								open : function() {
									$("#pin")
											.keyup(
													function(event) {
														if (event.keyCode == 13) {
															$(
																	'.ui-dialog-buttonpane button:eq(0)')
																	.click();
														}
													});
								}
							});

			return "AGUARDE";

		} catch (Err) {
			return Err.description;
		}
	},

	escolherCertificado : function(dlg, cont) {
		var gPIN = $("#pin").val();
		if (gPIN === '') {
			return Erro({
				message : "PIN deve ser informado."
			});
		}

		gCertAlias = $('input[name=cert_alias]:checked').val();

		if (gCertAlias == undefined || gCertAlias === '') {
			data = ittruSignApplet.listCerts(gStore, gPIN);

			var json = $.parseJSON(data);

			if (json.length == 1) {
				gCertAlias = json[0].alias;
			} else if (json.length == 0) {
				return Erro({
					message : "Nenhum certificado digital disponível."
				});
			} else if (json.length >= 1) {
				var html = '<br /><label>Selecione o certificado que deseja usar:</label><br />';
				for (var i = 0; i < json.length; i++) {
					html += "<input type=\"radio\" name=\"cert_alias\" value=\""
							+ json[i].alias
							+ "\""
							+ (i == 0 ? "checked" : "")
							+ "> " + json[i].subject + "<br>";
				}
				$("#certChoice").html(html);

				return "Selecione o certificado para prosseguir.";
			}
		}

		if (gCertAlias === '') {
			return Erro({
				message : "Certificado deve ser selecionado."
			});
		}

		gCertificadoB64 = ittruSignApplet.getCertificate(gCertAlias);
		return "OK";

	},

	assinar : function(conteudo) {
		var ret = {};
		try {
			// alert(conteudo);
			// gAssinatura.Content = gUtil.Base64Decode(conteudo)

			certSel = ittruSignApplet.getCertificate(gCertAlias);
			var keySize = ittruSignApplet.getKeySize(gCertAlias);

			if (gPolitica) {
				var alg = 'sha1';
				if (keySize < 2048) {
					// alert("assinando sha1");
					ret.assinaturaB64 = document.signer.sign(gStore, 0, gPIN,
							gCertAlias, conteudo);
				} else {
					alg = 'sha256';
					// alert("assinando sha256");
					ret.assinaturaB64 = document.signer.sign(gStore, 2, gPIN,
							gCertAlias, conteudo);
				}
			} else {
				// alert("assinando pkcs7");
				ret.assinaturaB64 = document.signer.sign(gStore, 99, gPIN,
						gCertAlias, conteudo);
			}
			// alert(ret.assinaturaB64);

			ret.assinante = ittruSignApplet.getSubject(gCertAlias);

			// alert(ret.assinante);

			var re = /CN=([^,]+),/gi;
			var m;
			if ((m = re.exec(ret.assinante)) != null) {
				ret.assinante = m[1];
			}
			ret.status = "OK"
			return ret;
		} catch (err) {
			Erro(err);
			ret.status = err.message;
			return ret;
		}
	}
}

//
// Provider: Assinador com senha
//
var providerPassword = {
	nome : 'Assinatura com Senha',
	inicializar : function(cont) {
		try {															
			var senhaDialog = $(
					'<div class="modal fade" tabindex="-1" role="dialog" id="senhaDialog"><div class="modal-dialog modal-dialog-centered" role="document"><div class="modal-content">'
					+ sigaModal.obterCabecalhoPadrao('Identificação')
					+ '<div class="modal-body"><fieldset><label>Matrícula</label> <br /> <input id="nomeUsuarioSubscritor" type="text" value="' + $('#siglaUsuarioCadastrante').val() + '" class="text ui-widget-content ui-corner-all" onblur="javascript:converteUsuario(this)" /> <label>(modifique caso necessário)</label><br /> <br /> <label>Senha</label><br /> <input type="password" id="senhaUsuarioSubscritor" class="text ui-widget-content ui-corner-all" autocomplete="off" autofocus /></fieldset></div>'
					+ '<div class="modal-footer"><button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button><button type="button" id="senhaOk" class="btn btn-primary">OK</button></div>'
					+ '</div></div></div>')
					.modal();										
			
			senhaDialog.on('shown.bs.modal', function () {
				$(this).find('[autofocus]').focus();
				
				$('#nomeUsuarioSubscritor, #senhaUsuarioSubscritor').on('keypress', function(e) {
					// se pressionado enter
				    if(e.which == 13) {
				    	$('#senhaOk').click();				    	
				    }
				});
				
				$('#senhaOk').click(function () {
					gLogin = $("#nomeUsuarioSubscritor").val();
					gPassword = $("#senhaUsuarioSubscritor").val();
					gAssinando = false;
					cont();
					senhaDialog.modal('hide');
				});
			});
			
			senhaDialog.on('hidden.bs.modal', function () {
				// gAssinando = flag necessario para reabrir a janela mediante cancelamento ou fechamento da modal
				// pelo usuário por causa da função AssinarDocumentos que é chamada pelo botão assinar
				gAssinando = false;
				$('#senhaDialog').remove();
			});
			
			
			
			$(document).delegate('.ui-dialog', 'keyup', function(e) {
				if((e.which == 13 || e.key === "Enter") && ($("#nomeUsuarioSubscritor").val() === "" || $("#senhaUsuarioSubscritor").val() === "")) {
					return false;
				}
		        var tagName = e.target.tagName.toLowerCase();

		        tagName = (tagName === 'input' && e.target.type === 'button') ? 'button' : tagName;

		        if (e.which === $.ui.keyCode.ENTER && tagName !== 'textarea' && tagName !== 'select' && tagName !== 'button') {
		            $(this).find('.ui-dialog-buttonset button').eq(0).trigger('click');

		            return false;
		        }
		    });
			return "AGUARDE";
		} catch (Err) {
			return Err.description;
		}
	}
}


//Provider: Assinador com PIN
var providerPIN = {
	nome : 'Assinatura com PIN',
	inicializar : function(cont) {
		try {															
			var senhaDialog = $(
					'<div class="modal fade" tabindex="-1" role="dialog" id="senhaDialog"><div class="modal-dialog modal-dialog-centered" role="document"><div class="modal-content">'
					+ sigaModal.obterCabecalhoPadrao('Identificação&nbsp;&nbsp;&nbsp;<br />CPF: ' +$('#cpfUsuarioCadastrante').val())
					+ '<div class="modal-body"><div class="form-group text-center"><input id="nomeUsuarioSubscritor" type="hidden" value="' + $('#siglaUsuarioCadastrante').val() + '" /> '
					+ '<label>Informe seu PIN</label><br /><div class="row"><div class="col-3"></div><div class="col-6"> <input type="password" id="pinUsuarioSubscritor" class="form-control input-lg" style="text-align: center;" aria-describedby="passwordHelp" maxlength="8" autocomplete="off" autofocus /></div></div><small id="pinHelp" class="form-text text-muted" style="color: #dc3545 !important;"></small></div></div>'
					+ '<div class="modal-footer"><button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button><button type="button" id="senhaOk" class="btn btn-primary">Assinar <i class="fa fa-signature"/></button></div>'
					+ '</div></div></div>')
					.modal();										
			
			senhaDialog.on('shown.bs.modal', function () {
				$(this).find('[autofocus]').focus();
				
				$('#pinUsuarioSubscritor').on('keypress', function(e) {
				    if((e.which == 13 || e.key === "Enter")) {
				    	$('#senhaOk').click();				    	
				    }
				});
				
				$('#senhaOk').click(function () {
					$("#pinHelp").html('');
					if($("#pinUsuarioSubscritor").val() === "") {
						$("#pinHelp").html('PIN não informado. Favor inserí-lo.');
						$("#pinUsuarioSubscritor").select();		
						return false;
					} 
					
					if( !isNumeric($("#pinUsuarioSubscritor").val())) {
						$("#pinHelp").html('PIN deve conter apenas dígitos númericos (0-9). Favor corrigir.');
						$("#pinUsuarioSubscritor").select();			
						return false;
					} 
					
					if( $("#pinUsuarioSubscritor").val().length !== 8) {
						$("#pinHelp").html('PIN deve ter 8 dígitos numéricos.');
						$("#pinUsuarioSubscritor").select();			
						return false;
					} 
					gLogin = $("#nomeUsuarioSubscritor").val();
					gPassword = $("#pinUsuarioSubscritor").val();
					gAssinando = false;
					cont();
					senhaDialog.modal('hide');
				});
			});
			
			senhaDialog.on('hidden.bs.modal', function () {
				gAssinando = false;
				$('#senhaDialog').remove();
			});
			
			return "AGUARDE";
		} catch (Err) {
			return Err.description;
		}
	}
}



//
// State manager and progress bar
//
function Conteudo(url) {
	// alert(url);

	// objHTTP = new ActiveXObject("MSXML2.XMLHTTP");
	// objHTTP.open("GET", url, false);
	// objHTTP.send();

	// if(objHTTP.Status == 200){
	var Conteudo, Inicio, Fim, Texto, err;
	// alert("OK, enviado");
	// Conteudo = objHTTP.responseText;

	// Conteudo = objHTTP.responseText;
	$.ajax({
		type : "GET",
		url : url,
		dataType : "text",
		accepts : {
			text : "text/vnd.siga.b64encoded"
		},
		success : function(data, textStatus, XMLHttpRequest) {
			// alert(data);
			// alert(XMLHttpRequest.getResponseHeader('Atributo-Assinavel-Data-Hora'));
			gAtributoAssinavelDataHora = XMLHttpRequest
					.getResponseHeader('Atributo-Assinavel-Data-Hora');
			Conteudo = data;
		},
		error : function(jqXHR, textStatus, errorThrown) {
			err = textStatus + ": " + errorThrown;
		},
		async : false,
		cache : false
	});

	if (err != undefined)
		return err;

	if (Conteudo == undefined) {
		return "Não foi possível obter o conteúdo do documento a ser assinado."
	}

	if (Conteudo.indexOf("gt-error-page-hd") != -1) {
		Inicio = Conteudo.indexOf("<h3>") + 4;
		Fim = Conteudo.indexOf("</h3>", Inicio);
		Texto = Conteudo.substr(Inicio, Fim - Inicio);
		return "Não foi possível obter o conteúdo do documento a ser assinado: "
				+ Texto;
	}

	// alert(Conteudo);
	return Conteudo;
	// }
	return "Não foi possível obter o conteúdo do documento a ser assinado.";
}

var providers = [ providerAssijusPopup, providerAssijus, providerIttruAx, providerIttruCAPI ];

//
// Processamento de assinaturas em lote, com progress bar
//

var process = {
	steps : [],
	index : 0,
	reset : function() {
		this.steps = [];
		this.index = 0;
	},
	push : function(x) {
		this.steps.push(x);
	},
	run : function() {
		window.scrollTo(0, 0);
		
		var progressDialog = $(
				'<div class="modal fade" tabindex="-1" role="dialog" id="progressDialog"><div class="modal-dialog" role="document"><div class="modal-content">'
				+ sigaModal.obterCabecalhoPadrao()				
				+ '<div class="modal-body"><h1 class="siga-modal__titulo  siga-modal__titulo--conteudo">Assinatura Digital (' + provider.nome + ')</h1><p id="vbslog">Iniciando...</p><div id="progressbar-ad"></div></div>'
				+ '<div class="modal-footer"><button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button><button type="button" id="senhaOk" class="btn btn-primary">OK</button></div>'
				+ '</div></div></div>')
				.modal();
		
		var runFunction = this;
		progressDialog.on('shown.bs.modal', function () {
			runFunction.progressbar = $('#progressbar-ad').progressbar();
			runFunction.nextStep();
		});
		
		progressDialog.on('hidden.bs.modal', function () {
			gAssinando = false;
			try {
				progressDialog.modal('dispose');
			} catch (e) {
			}
			try {
				$('#progressDialog').remove();
			} catch (e) {
			}
		});
	},
	finalize : function() {
		$('#progressDialog').modal('hide');
		gAssinando = false;
	},
	nextStep : function() {
		if (typeof this.steps[this.index] == 'string')
			eval(this.steps[this.index++]);
		else {
			var ret = this.steps[this.index++]();
			if ((typeof ret == 'string') && ret != "OK") {
				this.finalize();
				ModalAlert(ret, "Não foi possível completar a operação");
				return;
			}
		}

		this.progressbar.progressbar("value",
				100 * (this.index / this.steps.length));

		if (this.index != this.steps.length) {
			var me = this;
			window.setTimeout(function() {
				me.nextStep();
			}, 100);
		} else {
			this.finalize();
		}
	}
};

function ModalAlert(err, title) {
	var alertDialog = $(
			'<div class="modal fade" tabindex="-1" role="dialog" id="alertDialog"><div class="modal-dialog" role="document"><div class="modal-content">'
			+ sigaModal.obterCabecalhoPadrao()
			+ '<div class="modal-body"><h1 class="siga-modal__titulo  siga-modal__titulo--conteudo">' + title + '</h1><p>' + err + '</p></div>'
			+ '<div class="modal-footer"><button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button></div>'
			+ '</div></div></div>')
			.modal();
	
	alertDialog.on('hidden.bs.modal', function () {
		// alertDialog.modal('dispose');
		$('#alertDialog').remove();
	});
}

function Erro(err) {
	alert("Ocorreu um erro durante o processo de assinatura: " + err.message);
	return "Ocorreu um erro durante o processo de assinatura: " + err.message;
}

function ExecutarAssinarDocumentos(Copia, Juntar, Tramitar, ExibirNoProtocolo) {
	process.reset();

	if (Copia || Copia == "true") {
		Copia = "true";
		process.push(function() {
			Log("Iniciando conferência")
		});
	} else {
		Copia = "false";
		process.push(function() {
			Log("Iniciando assinatura")
		});
	}

	var oUrlPost, oNextUrl, oUrlBase, oUrlPath, oNome, oUrl, oChk;

	oUrlBase = document.getElementsByName("ad_url_base")[0];
	if (oUrlBase == null) {
		alert("element ad_url_base does not exist");
		return;
	}

	oUrlNext = document.getElementsByName("ad_url_next")[0];
	if (oUrlNext == null) {
		alert("element ad_url_next does not exist");
		return;
	}

	for (var i = 0, len = gOperacoes.length; i < len; i++) {
		var o = gOperacoes[i];
		if (!o.enabled)
			continue;

		process.push("Copia=" + Copia + ";");

		if (( ($('#podeAssinarPor').val() == "true" && $('#siglaUsuSubscritor').val() != "") && ($('#siglaUsuarioCadastrante').val() != $('#siglaUsuTitular').val()) )
				&& !$('#siglaUsuCossignatarios').val().includes($('#siglaUsuarioCadastrante').val()) && Copia != 'true') {
			if (!confirm("DESEJA ASSINAR O DOCUMENTO POR \""+ $('#nomeUsuSubscritor').val() + "\" - \"" + $('#siglaUsuSubscritor').val() +"\" OU POR UM DOS COSIGNATARIOS (" + $('#siglaUsuCossignatarios').val() + " )\"")) {
				gAssinando = false;
				$(this).dialog('destroy').remove();				
			}
		}
		
		if (!o.usePassword && !o.usePin) {
			if (provider != providerAssijusPopup && provider != providerAssijus) {
				process
						.push("gNome='"
								+ o.nome
								+ "'; gAutenticar = "
								+ (o.hasOwnProperty('autenticar') ? o.autenticar
										: Copia)
								+ "; gTramitar = "
								+ (o.hasOwnProperty('tramitar') ? o.tramitar
										: Tramitar)
								+ "; gJuntar = "
								+ (o.hasOwnProperty('juntar') ? o.juntar
										: Juntar)
								+ "; gExibirNoProtocolo = "
								+ (o.hasOwnProperty('exibirNoProtocolo') ? o.exibirNoProtocolo
										: ExibirNoProtocolo)
								+ "; gUrlPost = '"
								+ oUrlBase.value
								+ o.urlPost
								+ "'; gUrlDocumento = '"
								+ oUrlBase.value
								+ o.urlPdf
								+ "&semmarcas=1'; if (gPolitica){gUrlDocumento = gUrlDocumento + '&certificadoB64=' + encodeURIComponent(gCertificadoB64);};");
				process.push(function() {
					Log(gNome + ": Buscando no servidor...")
				});
				process.push(function() {
					gDocumento = Conteudo(gUrlDocumento);
					if (typeof gDocumento == "string"
							&& gDocumento.indexOf("Não") == 0)
						return gDocumento;
				});

				var ret;
				process.push(function() {
					Log(gNome + ": Assinando...")
				});
				process.push(function() {
					gRet = provider.assinar(gDocumento);
				});
				process.push(function() {
					Log(gNome + ": Gravando assinatura de " + gRet.assinante)
				});

				process.push(function() {
					var DadosDoPost = "sigla=" + encodeURIComponent(gNome)
							+ "&copia=" + gAutenticar + "&assinaturaB64="
							+ encodeURIComponent(gRet.assinaturaB64)
							+ "&assinante="
							+ encodeURIComponent(gRet.assinante);
					if (gJuntar !== undefined) {
						DadosDoPost = DadosDoPost + "&juntar=" + gJuntar;
					}
					if (gTramitar !== undefined) {
						DadosDoPost = DadosDoPost + "&tramitar=" + gTramitar;
					}
					if (gExibirNoProtocolo !== undefined) {
						DadosDoPost = DadosDoPost + "&exibirNoProtocolo=" + gExibirNoProtocolo;
					}
					if (gPolitica) {
						DadosDoPost = DadosDoPost + "&certificadoB64="
								+ encodeURIComponent(gCertificadoB64);
						DadosDoPost = DadosDoPost
								+ "&atributoAssinavelDataHora="
								+ gAtributoAssinavelDataHora;
					}

					// alert("oNome: " + oNome.value);
					var aNome = gNome.split(":");
					if (aNome.length == 2) {
						// alert("id: " + aNome(1));
						DadosDoPost = "id=" + aNome[1] + "&" + DadosDoPost;
					}

					Status = GravarAssinatura(gUrlPost, DadosDoPost);
					return Status;
				});
			} else {
				// "code": "TRF2-MEM-2016/00144",
				// "descr": "Teste",
				// "id": "489623760__TRF2MEM201600144",
				// "kind": "Memorando",
				// "origin": "Siga-Doc",
				// "system": "sigadocsigner"
				var signable = {
					id : o.id,
					code : o.nome,
					descr : o.descr,
					kind : o.kind,
					extra : ""
				};
				if (o.hasOwnProperty('autenticar') && o.autenticar)
					signable.extra = "autenticar";
				if (Copia == "true")
					signable.extra = "autenticar";
				
				if (o.hasOwnProperty('juntar') || Juntar || Juntar == false)
					signable.extra += (signable.extra.length > 0 ? "," : "") + ((o.juntar || Juntar == "true" || Juntar == true) ? "juntar" : "nao_juntar");

				if (o.hasOwnProperty('tramitar') || Tramitar || Tramitar == false)
					signable.extra += (signable.extra.length > 0 ? "," : "") + ((o.tramitar || Tramitar == "true" || Tramitar == true) ? "tramitar" : "nao_tramitar");
				
				if (o.hasOwnProperty('exibirNoProtocolo') || ExibirNoProtocolo || ExibirNoProtocolo == false)
					signable.extra += (signable.extra.length > 0 ? "," : "") + ((o.exibirNoProtocolo || ExibirNoProtocolo == "true" || ExibirNoProtocolo == true) ? "exibirNoProtocolo" : "nao_exibirNoProtocolo");
				
				provider.assinar(signable);
			}
		} else {
			process.push("gNome='" + o.nome + "'; gAutenticar = "
					+ (o.hasOwnProperty('autenticar') ? o.autenticar : Copia)
					 + "; gTramitar = "
					+ (o.hasOwnProperty('tramitar') ? o.tramitar : Tramitar)
					 + "; gJuntar = "
					+ (o.hasOwnProperty('juntar') ? o.juntar : Juntar)
					 + "; gExibirNoProtocolo = "
					+ (o.hasOwnProperty('exibirNoProtocolo') ? o.exibirNoProtocolo : ExibirNoProtocolo)
					+ "; gUrlPostPassword = '" + oUrlBase.value
					+ o.urlPostPassword + "';");

			process.push(function() {
				Log(gNome + ": Gravando assinatura com senha de " + gLogin)
			});

			process.push(function() {
				var id = gNome ? gNome.split(':')[1] : null;
				var DadosDoPost = "id=" + id + "&sigla=" + gNome
						
						+ "&nomeUsuarioSubscritor=" + gLogin
						+ "&senhaUsuarioSubscritor=" + encodeURIComponent(gPassword) 
						+ "&senhaIsPin=" + o.usePin
						+ "&copia="	+ gAutenticar;
						
				if (gTramitar !== undefined) {
					DadosDoPost = DadosDoPost + "&tramitar=" + gTramitar;
				}
				if (gJuntar !== undefined) {
					DadosDoPost = DadosDoPost + "&juntar=" + gJuntar;
				}
				if (gExibirNoProtocolo !== undefined) {
					DadosDoPost = DadosDoPost + "&exibirNoProtocolo=" + gExibirNoProtocolo;
				}
				Status = GravarAssinatura(gUrlPostPassword, DadosDoPost);
				gRet = Status;
				return Status;
			});
		}
	}

	if (provider != providerAssijusPopup && provider != providerAssijus) {
		process.push(function() {
			gAssinando = false;
			if (gRet == undefined)
				return;
			Log("Concluído, redirecionando...");
		});
		process.push(function() {
			if (gRet == undefined)
				return;
			location.href = oUrlNext.value;
		});
	} else {
		process
				.push(function() {
					var resolve = function(url, base_url) {
						var doc = document, old_base = doc
								.getElementsByTagName('base')[0], old_href = old_base
								&& old_base.href, doc_head = doc.head
								|| doc.getElementsByTagName('head')[0], our_base = old_base
								|| doc_head.appendChild(doc
										.createElement('base')), resolver = doc
								.createElement('a'), resolved_url;
						our_base.href = base_url || '';
						resolver.href = url;
						resolved_url = resolver.href; // browser magic at work
						// here

						if (old_base)
							old_base.href = old_href;
						else
							doc_head.removeChild(our_base);
						return resolved_url;
					}

					gRet = provider.concluir(resolve(oUrlNext.value,
							window.location.href));
				});
	}
	process.run();
}

// 1 = digital, 2 = com senha, 3 = híbrida, 4 = com PIN
function verificarTipoDeAssinatura() {
	var usePassword = false;
	var useToken = false;
	var usePin = false;

	for (var i = 0, len = gOperacoes.length; i < len; i++) {
		if (gOperacoes[i].enabled) {
			if (gOperacoes[i].usePassword)
				usePassword = true;
			else if (gOperacoes[i].usePin)
				usePin = true;
			else
				useToken = true;
		}
	}
	
	if (usePin) 
		return 4;
	
	return (useToken ? 1 : 0) + (usePassword ? 2 : 0);
}

function identificarOperacoes() {
	gOperacoes = [];

	var NodeList = document.getElementsByTagName("INPUT");
	for (var i = 0, len = NodeList.length; i < len; i++) {
		var Elem = NodeList[i];
		if (Elem.name.substr(0, 9) == "ad_descr_") {
			var operacao = {};

			operacao.codigo = Elem.name.substr(9);
			operacao.nome = document.getElementsByName("ad_descr_"	+ operacao.codigo)[0].value;
			operacao.urlPdf = document.getElementsByName("ad_url_pdf_"	+ operacao.codigo)[0].value;
			operacao.urlPost = document.getElementsByName("ad_url_post_" + operacao.codigo)[0].value;
			operacao.urlPostPassword = document.getElementsByName("ad_url_post_password_" + operacao.codigo)[0].value;
			
			operacao.usePassword = false;
			operacao.usePin = false;
			operacao.transfer = false;

			// Assijus
			operacao.id = document.getElementsByName("ad_id_" + operacao.codigo)[0].value;
			operacao.descr = document.getElementsByName("ad_description_" + operacao.codigo)[0].value;
			operacao.kind = document.getElementsByName("ad_kind_" + operacao.codigo)[0].value;

			/* Assinar com Senha */
			var oChkPwd = document.getElementsByName("ad_password_" + operacao.codigo)[0] || document.getElementsByName("ad_password_0")[0] || document.getElementById("ad_password_0");
			if (oChkPwd == null) {
				operacao.usePassword = false;
			} else {
				operacao.usePassword = oChkPwd.checked;
			}
			
			/* Assinar com Senha PIN */
			var oChkPin = document.getElementsByName("ad_pin_" + operacao.codigo)[0] || document.getElementsByName("ad_pin_0")[0] || document.getElementById("ad_pin_0");
			if (oChkPin == null) {
				operacao.usePin = false;
			} else {
				operacao.usePin = oChkPin.checked;
			}

			var oChk = document.getElementsByName("ad_chk_" + operacao.codigo)[0];
			if (oChk == null) {
				operacao.enabled = true;
			} else {
				operacao.enabled = oChk.checked;
			}

			var oChkAut = document.getElementsByName("ad_aut_"
					+ operacao.codigo)[0];
			if (oChkAut != null) {
				operacao.autenticar = oChkAut.checked;
				if (operacao.autenticar)
					operacao.enabled = true;
			}

			var oChkTramitar = document.getElementsByName("ad_tramitar_"
					+ operacao.codigo)[0];
			if (oChkTramitar != null) 
				operacao.tramitar = oChkTramitar.checked;

			var oChkJuntar = document.getElementsByName("ad_juntar_"
					+ operacao.codigo)[0];
			if (oChkJuntar != null) 
				operacao.juntar = oChkJuntar.checked;

			var oChkExibirNoProtocolo = document.getElementsByName("ad_exibirNoProtocolo_"
					+ operacao.codigo)[0];
			if (oChkExibirNoProtocolo != null) 
				operacao.exibirNoProtocolo = oChkExibirNoProtocolo.checked;

			gOperacoes.push(operacao);
		}
	}
}

function GravarAssinatura(url, datatosend) {
	var result = "OK";
	$.ajax({
		url : url,
		type : "POST",
		data : datatosend,
		async : false,
		error : function(xhr) {
			// result = TrataErro(xhr.responseText ? xhr.responseText : xhr,
			// "");
			result = "<div class='alert alert-danger' role='alert'>Erro na gravação da assinatura. <br />" + xhr.responseText+"</div>";
		}
	});
	return result;
}

function TrataErro(Conteudo, retorno) {
	if (Conteudo.indexOf("gt-error-page-hd") != -1
			&& Conteudo.indexOf("function GravarAssinatura") < 0) {
		Inicio = Conteudo.indexOf("<h3>") + 4;
		Fim = Conteudo.indexOf("</h3>", Inicio);
		retorno = Conteudo.substr(Inicio, Fim - Inicio);
	}
	return retorno;
}

// var intID 'a global variable

function Log(Text) {
	var oLog;
	oLog = document.getElementById("vbslog");
	if (oLog != null) {
		oLog.innerHTML = Text;
	}
}

/**
 * 
 * Base64 encode / decode http://www.webtoolkit.info/
 * 
 */
var Base64 = {

	// private property
	_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

	// public method for encoding
	encode : function(input) {
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

			output = output + this._keyStr.charAt(enc1)
					+ this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3)
					+ this._keyStr.charAt(enc4);

		}

		return output;
	},

	// public method for decoding
	decode : function(input) {
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

	// private method for UTF-8 encoding
	_utf8_encode : function(string) {
		string = string.replace(/\r\n/g, "\n");
		var utftext = "";

		for (var n = 0; n < string.length; n++) {

			var c = string.charCodeAt(n);

			if (c < 128) {
				utftext += String.fromCharCode(c);
			} else if ((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			} else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}

		}

		return utftext;
	},

	// private method for UTF-8 decoding
	_utf8_decode : function(utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;

		while (i < utftext.length) {

			c = utftext.charCodeAt(i);

			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			} else if ((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i + 1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			} else {
				c2 = utftext.charCodeAt(i + 1);
				c3 = utftext.charCodeAt(i + 2);
				string += String.fromCharCode(((c & 15) << 12)
						| ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}

		}

		return string;
	}

}

/*
 * Attempt to load the applet up to "X" times with a delay. If it succeeds, then
 * execute the callback function.
 */
function WaitForAppletLoad(applet_id, attempts, delay, onSuccessCallback,
		onFailCallback) {
	// Test
	var to = typeof (document.getElementById(applet_id));
	if (to == 'function' || to == 'object') {
		onSuccessCallback(); // Go do it.
		return true;
	} else {
		if (attempts == 0) {
			onFailCallback();
			return false;
		} else {
			// Put it back in the hopper.
			setTimeout(function() {
				WaitForAppletLoad(applet_id, --attempts, delay,
						onSuccessCallback, onFailCallback);
			}, delay);
		}
	}
}

/* converte para maiúscula a sigla do estado */
function converteUsuario(nomeusuario) {
	re = /^[a-zA-Z]{2}\d{3,6}$/;
	ret2 = /^[a-zA-Z]{1}\d{3,6}$/;
	tmp = nomeusuario.value;
	if (tmp.match(re) || tmp.match(ret2)) {
		nomeusuario.value = tmp.toUpperCase();
	}
}