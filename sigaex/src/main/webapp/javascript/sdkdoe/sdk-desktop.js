/* SDK Desktop Javascript API  */

/*
 Theme: sweetAlert
 Description: Javascript API for SDK Desktop
 Version: 1.2.0 -> tailored for sdk-desktop 1.0.15
 Author: dazevedo
 Requires: Jquery 3.1.1 (low-level functions); base64js, text-encoder-lite (signPureText); sweetAlert (User-interaction functions)
 */

/* ------------------------------------------ */
/*             TABLE OF CONTENTS
 /* ------------------------------------------ */
/*   00 - Variables and Constants    - 34  */
/*   01 - High Level Functions       - 49  */
/*   1.1 - Generic Functions         - 51  */
/*   1.2 - Challenge-Response        - 140 */
/*   1.3 - Signing & Encrypting      - 205 */
/*   1.4 - Token Services            - 433 */
/*   02 - Low Level Functions        - 551 */
/*   03 - User Interaction Functions - 668 */

/* ------------------------------------------ */
/*                 IMPORTANT
 /* ------------------------------------------ */

/* Since many problems now occur on the client side,
 * it is recommended to send the error back to the server,
 * to keep a error log.
 *
 * The function sdkDesktop.sendErrorToServer (Line 683, Low Level Functions)
 * should be modified to send it to the server. */

/*   00 - Variables and Constants     */
/**
 * @exports sdkDesktop
 */
var sdkDesktop = {

	parameters : {},
	fileParameters : [],
	signatureDestination : "",

	/* 01 - High Level Functions */

	/* 1.1 - Generic functions */

	/**
	 * This function will check if sdk-desktop is running. callback function
	 * will receive a json like this: {alive : true}
	 *
	 * In case of failure, sdkDesktop.alertError will be called.
	 *
	 * @param {function}
	 *            callbackSuccess - callback function in case of success.
	 */
	checkStarted : function(callbackSuccess) {

		if(location.protocol == 'https:'){
			sdkDesktop
			.sdkDesktopGenericCallHttps(
					"alive",
					3000,
					callbackSuccess,
					function() {
						sdkDesktop
								.alertError(messages.errorMessageAliveHttps);
					});
		}else{
			sdkDesktop
			.sdkDesktopGenericCall(
					"alive",
					3000,
					function(){
						sdkDesktop
						.sdkDesktopGenericCallHttps(
								"alive",
								3000,
								callbackSuccess,
								function() {
									sdkDesktop
											.alertError(messages.errorMessageAliveHttps);
								});
					},
					function() {
						sdkDesktop
								.alertError(messages.errorMessageAliveHttp);
					});
		}


	},

	checkStarted : function(callbackSuccess, callbackError) {

		if(location.protocol == 'https:'){
			if (!callbackError) {
				callbackError = function() {
					sdkDesktop
					.alertError(messages.errorMessageAliveHttps);
				};
			}
			sdkDesktop
			.sdkDesktopGenericCallHttps(
					"alive",
					3000,
					callbackSuccess,
					callbackError);
		}else{
			if (!callbackError) {
				callbackError = function() {
					sdkDesktop
					.alertError(messages.errorMessageAliveHttp);
				};
			}
			sdkDesktop
			.sdkDesktopGenericCall(
					"alive",
					3000,
					function(){
						sdkDesktop
						.sdkDesktopGenericCallHttps(
								"alive",
								3000,
								callbackSuccess,
								callbackError);
					},
					callbackError);
		}


	},


	/**
	 * This function will show the configuration screen to the user. Will update
	 * the configuration and call sdkDesktop.alertSuccess.
	 *
	 * In case of failure, will call sdkDesktop.alertError and
	 * sdkDesktop.sendErrorToServer.
	 *
	 */
	showConfig : function(callback) {
		sdkDesktop.sdkDesktopGenericCallHttps(
			"config",
			5000000,
			callback,
			function(err) {
				sdkDesktop
						.alertError(messages.errorMessageConfiguration);
				sdkDesktop.sendErrorToServer("config", null, err);
			}, "showConfig");
	},

	/**
	 * This function shall be used to set-up the parameters used by most
	 * functions.
	 *
	 * the parameters must be a Javascript object like: { "key" : "value",
	 * "booleanKey" : true, "integerKey": 2 [...] }
	 *
	 * Must be set before calling any high-level function.
	 *
	 * @param {object}
	 *            params - parameters passed to sdk-desktop.
	 *
	 */
	setParameters : function(params) {
		sdkDesktop.parameters = params;
	},

	/**
	 * This function shall be used to set-up the file parameters for signature/encryption
	 *
	 * the parameters must be a Javascript array object like: [ { "key" : "value", ...}, {...}, ... ]
	 *
	 * Must be set before calling the high-level functions.
	 *
	 * @param {object}
	 *            params - parameters passed to sdk-desktop.
	 *
	 */
	setFileParameters : function(params) {
		sdkDesktop.fileParameters = params;
	},

	/* 1.2 - Challenge-response */

	/**
	 * This will call sdk-desktop requesting a response to a given challenge
	 * (the challenge must be in the parameters previously set.
	 *
	 * The callback function will receive a JSON with the following: {"response" :
	 * "Base 64 encoded response" "certificate" : "Base 64 encoded signer
	 * certificate"}
	 *
	 * In case of failure, will call sdkDesktop.alertError and
	 * sdkDesktop.sendErrorToServer.
	 *
	 * @param {function}
	 *            callbackSuccess - callback function.
	 *
	 */
	generateResponse : function(callbackSuccess) {
		sdkDesktop
				.sdkDesktopGenericCallHttps(
						"challenge",
						5000000,
						function(response) {
							callbackSuccess(response);
						},
						function(error) {
							sdkDesktop.alertError(messages.errorChallengeResponse(error));
							sdkDesktop.sendErrorToServer("challenge", null, error);
						});
	},

	/**
	 * This will call sdk-desktop requesting the user list of mac addresses.
	 *
	 * The callback function will receive a JSON with the following:
	 * {"macAddressList" : "HEX encoded list of addresses separated by ';' " }
	 *
	 * In case of failure, will call sdkDesktop.alertError and
	 * sdkDesktop.sendErrorToServer.
	 *
	 * @param {function}
	 *            callbackSuccess - callback function.
	 *
	 */
	loadMacAddress : function(callbackSuccess) {
		sdkDesktop
				.sdkDesktopGenericCallHttps(
						"loadMacAdd",
						5000000,
						function(response) {
							callbackSuccess(response);
						},
						function(err) {
							sdkDesktop
									.alertError(messages.errorMessageMacAddress);
							sdkDesktop.sendErrorToServer("loadMacAdd", null,
									err);
						});
	},

	/* 1.3 - Signing & Encrypting functions */

	/**
	 * When the signature is made locally, you must set the location it should
	 * be saved.
	 *
	 * @param {string} destination - location signature should be saved
	 */
	setSignatureDestination : function(destination) {
		sdkDesktop.signatureDestination = destination;
	},

	/**
	 * Calls graphic interface so the user can add files to be signed
	 *
	 * @param {function} callbackSuccess - callback function
	 *
	 */
	addDocument : function(callbackSuccess) {
		sdkDesktop.callSignService("addContentFile", callbackSuccess);
	},

	/**
	 * Signs and saves the file locally.
	 *
	 * Should be called <b>AFTER</b> setSignatureDestination
	 *
	 */
	signAndSave : function(callbackFunction) {

		var numOfFiles = sdkDesktop.parameters.numberOfFilesToSign || 0;
		for(file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked){
				numOfFiles++;
			}
		}

		if(numOfFiles === 0){
			sdkDesktop.alertWarning(messages.warningNoFileTosign);
			return;
		}

		var i= 0;
		for(var file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked === true){

				for(var param in sdkDesktop.fileParameters[file] ){
					if(param !== "checked"){
						sdkDesktop.parameters[param + '.' + i] = sdkDesktop.fileParameters[file][param];
					}
				}
				i++;
			}
		}

		sdkDesktop.callSignService(
				"signAndSave",
				function(resp) {
					if (callbackFunction) {
						callbackFunction(resp);
					} else {
						sdkDesktop.alertSuccess(messages.successSignDocuments);
					}
				});
	},

	/**
	 * Signs some content and returns the signature to the callback function
	 *
	 * WARNING: in the future, only sdk-desktop versions above 1.0.12 will be supported
	 *
	 * @param {String} content - Content to be signed
	 * @param {function} callback - callback function
	 *
	 */
	signPureContent : function(content, callbackFunction){

		sdkDesktop.checkVersion("1.0.12",
			function(){
				if(sdkDesktop.parameters["textEncode"]){
					sdkDesktop.parameters["content"] = content;
				}else{
					sdkDesktop.parameters["textEncode"] = "base64";
					sdkDesktop.parameters["content"] = base64js.fromByteArray(new TextEncoderLite('utf-8').encode(content));
				}
				sdkDesktop.callSignService(
					"pureContent",
					function(resp) {
						if (callbackFunction) {
							callbackFunction(resp);
						} else {
							console.log(resp)
							sdkDesktop.alertWarning(messages.warningNoCallbackFunction);
						}
					});
			},
			function(){
				sdkDesktop.callSignService(
					"pureContent&content=" + content,
					function(resp) {
						if (callbackFunction) {
							callbackFunction(resp);
						} else {
							console.log(resp)
						sdkDesktop.alertWarning(messages.warningNoCallbackFunction);
					}
				});
			});
	},

	/**
	 * Signs only one file and returns the signature on JSON
	 *
	 * @param {number} index - file index on parameters
	 * @param {function} callback - callback function
	 */
	getEncodedContent : function(index, callbackFunction){

		var numOfFiles = sdkDesktop.parameters.numberOfFilesToSign || 0;
		for(file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked){
				numOfFiles++;
			}
		}

		if(numOfFiles === 0){
			sdkDesktop.alertWarning(messages.warningNoFileTosign);
			return;
		}

		var i= 0;
		for(var file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked === true){

				for(var param in sdkDesktop.fileParameters[file] ){
					if(param !== "checked"){
						sdkDesktop.parameters[param + '.' + i] = sdkDesktop.fileParameters[file][param];
					}
				}
				i++;
			}
		}

		sdkDesktop.callSignService(
				"getEncodedContent&index=" + index,
				function(resp) {
					if (callbackFunction) {
						callbackFunction(resp);
					} else {
						console.log(resp)
						sdkDesktop.alertWarning(messages.warningNoCallbackFunction);
					}
				});
	},

	/**
	 * This will check/uncheck a single document to be signed.
	 *
	 * @param {number}
	 *            index - 0-based position of the document to be signed.
	 * @param {boolean}
	 *            checked - true in case of checking the document, false
	 *            otherwise.
	 */
	markDocument : function(index, checked) {
		sdkDesktop.fileParameters[index].checked = checked;
	},

	/**
	 * This will request sdk-desktop to mark all documents to be signed.
	 *
	 * The callback function will receive nothing.
	 *
	 * @param {function}
	 *            callback - callback function
	 *
	 */
	markAllDocuments : function(callback) {
		var i;
		for(i = 0; i < sdkDesktop.fileParameters.length; i++){
			sdkDesktop.markDocument(i, true);
		}
		callback();
	},

	/**
	 * This will request sdk-desktop to mark, sign and send all documents.
	 *
	 * Will call sdkDesktop.alertSuccess when succeeded.
	 *
	 * Will call sdkDesktop.alertError and sdkDesktop.sendErrorToServer in case
	 * of failure.
	 *
	 */
	markSignAndSendAllDocuments : function(callbackSuccess) {
		sdkDesktop.markAllDocuments(function() {
			sdkDesktop.signAndSendDocuments(callbackSuccess);
		});
	},

	/**
	 * This will request sdk-desktop to sign and send all marked documents.
	 *
	 * Will call sdkDesktop.alertSuccess when succeeded, if no callback function is defined.
	 *
	 * Will call sdkDesktop.alertError and sdkDesktop.sendErrorToServer in case
	 * of failure.
	 *
	 */
	signAndSendDocuments : function(callbackFunction) {

		var numOfFiles = sdkDesktop.parameters.numberOfFilesToSign || 0;
		for(file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked){
				numOfFiles++;
			}
		}

		if(numOfFiles === 0){
			sdkDesktop.alertWarning(messages.warningNoFileTosign);
			return;
		}

		var i= 0;
		for(var file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked === true){

				for(var param in sdkDesktop.fileParameters[file] ){
					if(param !== "checked"){
						sdkDesktop.parameters[param + '.' + i] = sdkDesktop.fileParameters[file][param];
					}
				}
				i++;
			}
		}

		sdkDesktop.callSignService(
				"signAndSend",
				function() {
					if (callbackFunction) {
						callbackFunction(resp);
					} else {
						sdkDesktop
								.alertSuccess(messages.successSignSendDocuments);
					}
				});
	},

	/**
	 * This will request sdk-desktop signOnly.
	 *
	 * Will call sdkDesktop.alertSuccess when succeeded, if no callback function is defined.
	 *
	 * Will call sdkDesktop.alertError and sdkDesktop.sendErrorToServer in case
	 * of failure.
	 *
	 */
	signHashOnly : function(callbackFunction) {

		var numOfFiles = sdkDesktop.parameters.numberOfFilesToSign || 0;
		for(file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked){
				numOfFiles++;
			}
		}

		if(numOfFiles === 0){
			sdkDesktop.alertWarning(messages.warningNoFileTosign);
			return;
		}

		var i= 0;
		for(var file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked === true){

				for(var param in sdkDesktop.fileParameters[file] ){
					if(param !== "checked"){
						sdkDesktop.parameters[param + '.' + i] = sdkDesktop.fileParameters[file][param];
					}
				}
				i++;
			}
		}

		sdkDesktop.callSignService(
				"signHashOnly",
				function(resp) {
					if (callbackFunction) {
						callbackFunction(resp);
					} else {
						sdkDesktop
								.alertSuccess(messages.successSignSendDocuments);
					}
				});
	},

	signHashOnly : function(callbackSuccess, callbackError) {

		var numOfFiles = sdkDesktop.parameters.numberOfFilesToSign || 0;
		for(file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked){
				numOfFiles++;
			}
		}

		if(numOfFiles === 0){
			sdkDesktop.alertWarning(messages.warningNoFileTosign);
			return;
		}

		var i= 0;
		for(var file in sdkDesktop.fileParameters){
			if(sdkDesktop.fileParameters[file].checked === true){

				for(var param in sdkDesktop.fileParameters[file] ){
					if(param !== "checked"){
						sdkDesktop.parameters[param + '.' + i] = sdkDesktop.fileParameters[file][param];
					}
				}
				i++;
			}
		}

		sdkDesktop.callSignService(
				"signHashOnly",
				function(resp) {
					if (callbackSuccess) {
						callbackSuccess(resp);
					} else {
						sdkDesktop
								.alertSuccess(messages.successSignSendDocuments);
					}
				},
				callbackError);
	},


	/**
	 * @private
	 * This is a generic function to call signature-related services.
	 *
	 * @param {string}
	 *            service - service parameter in URL.
	 * @param {function}
	 *            callbackFunction - callback function in case of success.
	 *
	 */
	callSignService : function(service, callbackFunction) {
		sdkDesktop
				.sdkDesktopGenericCallHttps(
						"sign",
						5000000,
						callbackFunction,
						function(err) {
							sdkDesktop.alertError(messages.errorCallSignService(service, err));
							sdkDesktop.sendErrorToServer("sign", service, err);
						}, service);
	},

	callSignService : function(service, callbackSuccess, callbackError) {
		sdkDesktop
				.sdkDesktopGenericCallHttps(
						"sign",
						5000000,
						callbackSuccess,
						callbackError, service);
	},

	/* 1.4 - Token Services functions */

	/**
	 * This will request the token ATR to sdk-desktop
	 *
	 * The callback function will receive a JSON response as: {"ATR" : "Token ATR",
	 * "tokenSerialNumber" : "xpto", "tokenModel" : "xpto", "tokenManufacturerID" : "xpto"}
	 *
	 * All may come null, except ATR. If not found, ATR will come as 'ATR nao encontrado' (legacy reasons)
	 *
	 * Will call sdkDesktop.alertError and sdkDesktop.sendErrorToServer in case
	 * of failure.
	 *
	 * @param {function}
	 *            callbackFunction - callback function
	 *
	 */
	getTokenInfo : function(callbackFunction) {
		sdkDesktop
				.sdkDesktopGenericCallHttps(
						"tokenServices",
						5000000,
						callbackFunction,
						function(err) {
							sdkDesktop
									.alertError(messages.errorGetTokenInfo);
							sdkDesktop.sendErrorToServer("token",
									"getTokenInfo", err);
						}, "getTokenInfo");
	},

	/**
	 * This will request sdk-desktop to check if there is a token present
	 *
	 * The callback function will receive a JSON response as: {"isTokenPresent" :
	 * true/false, "isPkcs11" : true/false}
	 *
	 * isTokenPresent returns if there is a token present (dur)
	 * isPkcs11 returns if a token is required on configuration (you may not need one if you use pkcs#12)
	 *
	 * Will call sdkDesktop.alertError and sdkDesktop.sendErrorToServer in case
	 * of failure.
	 *
	 * @param {function}
	 *            callbackFunction - callback function
	 *
	 */
	isTokenPresent : function(callbackFunction) {
		sdkDesktop
				.sdkDesktopGenericCallHttps(
						"tokenServices",
						5000000,
						callbackFunction,
						function(err) {
							sdkDesktop
									.alertError(messages.errorIsTokenPresent);
							sdkDesktop.sendErrorToServer("token",
									"isTokenPresent", err);
						}, "isTokenPresent");
	},

	/**
	 * This will request a new pkcs10 Certificate to sdk-desktop
	 *
	 * The callback function will receive a JSON response as: {"pkcs10" :
	 * "Base-64 encoded certificate", "canceled" : true/false}
	 *
	 * It will receive an aditional pkcs8 string IF 'keyBackup' parameter is set to true
	 *
	 * Will call sdkDesktop.alertError and sdkDesktop.sendErrorToServer in case
	 * of failure.
	 *
	 * @param {function}
	 *            callbackFunction - callback function
	 *
	 */
	createNewCertificate : function(callbackFunction) {
		sdkDesktop
				.sdkDesktopGenericCallHttps(
						"tokenServices",
						5000000,
						callbackFunction,
						function(error) {

							sdkDesktop.alertError(messages.errorCreateNewCertificate(error));
							sdkDesktop.sendErrorToServer("token",
									"createNewCertificate", error);
						}, "createNewCertificate");
	},

	/**
	 * This will request sdk-desktop to install a new certificate.
	 *
	 * The callback function will receive a JSON response as: {"installed" :
	 * true/false, "canceled" : true/false, "pkcs12Path" : "Certificate path (if
	 * not token), "deviceType" : "PKCS11 or PKCS12" "}
	 *
	 * Will call sdkDesktop.alertError and sdkDesktop.sendErrorToServer in case
	 * of failure.
	 *
	 * @param {function}
	 *            callbackFunction - callback function.
	 *
	 */
	installCert : function(callbackFunction) {
		sdkDesktop
				.sdkDesktopGenericCallHttps(
						"tokenServices",
						5000000,
						callbackFunction,
						function(err) {
							sdkDesktop.alertError(messages.errorInstallNewCertificate(err));
							sdkDesktop.sendErrorToServer("token", "installCert", err);
						}, "installCert");
	},

	/* 02 - Low Level Functions */

	/**
	 * @private
	 * Internal Function! Use it at your own risk. No behaviour nor interface guarantees between versions.
	 *
	 * @deprecated Deve se usar somente conexões seguras (HTTPS). Use
	 *             sdkDesktop.sdkDesktopGenericCallHttps.
	 *
	 * This will call sdk-desktop (no handler or service in specific).
	 *
	 * @param {string}
	 *            handler - handler to be called. Ex.: "alive", "challenge"...
	 * @param {number}
	 *            timeInMilis - max timeout to response.
	 * @param {function}
	 *            callbackSuccess - callback function on success.
	 * @param {function}
	 *            callbackError - callback function on failure.
	 * @param {string}
	 *            service - service to be used with handler. Ex.: handler =
	 *            "tokenServices", service = "getATR".
	 *
	 */
	sdkDesktopGenericCall : function(handler, timeInMilis, callbackSuccess,
			callbackError, service) {
		$.ajax({
			url : "http://localhost:8585/" + handler + "/?service=" + service,
			type : "POST",
			cache : false,
			timeout : timeInMilis, /* Timeout in ms */
			data : sdkDesktop.parameters,
			success : function(result) {
				if (callbackSuccess) {
					callbackSuccess(JSON.stringify(result));
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				if (callbackError) {
					if (jqXHR.responseText) {
						callbackError(JSON.parse(JSON
								.stringify(jqXHR.responseText)));
					} else {
						callbackError();
					}
				}
			}
		});
	},

	/**
	 * @private
	 * Internal Function! Use it at your own risk. No behaviour nor interface guarantees between versions.
	 *
	 * This will call sdk-desktop (no handler or service in specific).
	 *
	 * @param {string}
	 *            handler - handler to be called. Ex.: "alive", "challenge"...
	 * @param {number}
	 *            timeInMilis - max timeout to response.
	 * @param {function}
	 *            callbackSuccess - callback function on success.
	 * @param {function}
	 *            callbackError - callback function on failure.
	 * @param {string}
	 *            service - service to be used with handler. Ex.: handler =
	 *            "tokenServices", service = "getATR".
	 *
	 */
	sdkDesktopGenericCallHttps : function(handler, timeInMilis,
			callbackSuccess, callbackError, service) {
		$.ajax({
			url : "https://localhost:8686/" + handler + "/?service=" + service,
			type : "POST",
			cache : false,
			timeout : timeInMilis, /* Timeout in ms */
			data : sdkDesktop.parameters,
			success : function(result) {
				if (callbackSuccess) {
					callbackSuccess(JSON.stringify(result));
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				if (callbackError) {
					if (jqXHR.responseText) {
						callbackError(JSON.parse(JSON
								.stringify(jqXHR.responseText)));
					} else {
						callbackError();
					}
				}
			}
		});
	},

	/**
	 * In case of client-side error, it should be sent to the server for logging
	 * purposes.
	 *
	 * TODO: override this function sending client errors back to server.
	 *
	 * @param {string}
	 *            handler - handler that was called. Ex.: "alive",
	 *            "challenge"...
	 * @param {string}
	 *            service - service that was used with handler. Ex.: handler =
	 *            "tokenServices", service = "getATR".
	 * @param {object}
	 *            error - JSON error response
	 *
	 */
	sendErrorToServer : function(handler, service, error) {
		json = JSON.parse(error);
//		json.Failure.handler = handler
//		json.Failure.service = service;
		console.log(JSON.stringify(json));
		/*
		 * You should send your errors back to the server. $.ajax({ url :
		 * "http://yourwebsite.com/something/error/add/", cache : false, timeout :
		 * 5000, Timeout in ms dataType : "jsonp", data : error });
		 */
	},

	/* 03 - User Interaction Functions */

	/**
	 * Will prompt the user with the given message. Best layout for success
	 * messages. Uses sweetAlert for a better user experience.
	 *
	 * @param {string}
	 *            message - message to be prompted
	 *
	 */
	alertSuccess : function(message) {
		swal({
			title : messages.successAlertHeader,
			text : message,
			type : "success",
			html : true
		});
	},

	/**
	 * Will prompt the user with the given message. Best layout for failure
	 * messages. Uses sweetAlert for a better user experience.
	 *
	 * @param {string}
	 *            message - message to be prompted
	 *
	 */
	alertError : function(message) {
		swal({
			title : messages.errorAlertHeader,
			text : message,
			type : "error",
			html : true
		});
	},

	/**
	 * Will prompt the user with the given message. Best layout for warning
	 * messages. Uses sweetAlert for a better user experience.
	 *
	 * @param {string}
	 *            message - message to be prompted
	 *
	 */
	alertWarning : function(message) {
		swal({
			title : messages.warningAlertHeader,
			text : message,
			type : "warning",
			html : true
		});
	},

	/**
	 * Will prompt the user with the given message. Best layout for info
	 * messages. Uses sweetAlert for a better user experience.
	 *
	 * @param {string}
	 *            message - message to be prompted
	 *
	 */
	alertInfo : function(message) {
		swal({
			title : messages.infoAlertHeader,
			text : message,
			type : "info",
			html : true
		});
	},

	/**
	 * @private
	 * Internal function! Use it at your own risk. No behaviour nor interface guarantees between versions.
	 *
	 * Will execute callbackSuccess if version equals or above "minVersion"
	 * Will execute callbackError otherwise
	 *
	 * @param {string} minVersion - minimum version that supports the operation
	 * @param {function} callbackSuccess - what to do if version is above minimum
	 * @param {function} callbackError - what to do if version is below minimum
	 */
	checkVersion: function(minVersion, callbackSuccess, callbackError){
		sdkDesktop.checkStarted(
				function(resp){
					var json = JSON.parse(resp);
					var version = json.version;
					if(!version){
						version = "1.0.6";
					}
					version    = sdkDesktop.breakVersionNumber(version);
					minVersion = sdkDesktop.breakVersionNumber(minVersion);

					for (i in minVersion) {
						if(version[i] < minVersion[i]){
							if(callbackError){
								return callbackError();
							}else{
								sdkDesktop.alertError(messages.errorUnsuportedSDKVersion);
							}
						}
					}
					if(callbackSuccess){
						return callbackSuccess();
					}
				}
		);
	},

	/**
	 * @private
	 * Internal function! Use it at your own risk. No behaviour nor interface guarantees between versions.
	 *
	 * Receives a version ("1.0.8", for example) and returns an array of numbers ([1, 0, 8])
	 *
	 * @param {string} version to be splitted.
	 */
	breakVersionNumber: function(version){
		var strings = version.split(".");
		return strings.map(Number);
	}



}