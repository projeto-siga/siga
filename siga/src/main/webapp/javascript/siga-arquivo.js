var xhr;
var inputArquivo;
var arquivo;
var url;
var isCancelado = false;
var jaAvisou = false;

function uploadArq(urlBase, inputArq, tamMaximo){
	url = urlBase;
	inputArquivo = inputArq;
	arquivo = inputArq.files[0];

	if (arquivo) { 
		if((parseInt(arquivo.size)) > tamMaximo) { 
			alert('Tamanho do arquivo excede o máximo permitido de ' + tamMaximo / 1024 * 1024 + ' MB');
			return;
		}
	} else {
		alert('Arquivo não informado. Escolha o arquivo para upload.');
		return;
    }
	inputArq.disabled = true;
	isCancelado = false;
	jaAvisou = false;
	document.getElementById("barraProgresso").classList.remove('d-none');
  	document.getElementById('msgProgressBar').textContent = "Calculando o hash do arquivo...";

	calculaHashEEnvia(
		arquivo,
		prog => setarProgressBar(Math.round(prog * 30))
	).then(
		res => enviarArq(arquivo, res, arquivo.size),
		err => console.error(err)
	);
}

function calculaHashEEnvia(arq, cbProgress) {
	return new Promise((resolve, reject) => {
		var alg = CryptoJS.algo.SHA256.create();
		readChunked(arq, (chunk, offs, total) => {
			if (isCancelado) {
				abortarUpload();
				reject(new Error('Cancelado'));				
			}
			
			alg.update(CryptoJS.enc.Latin1.parse(chunk)); 
			if (cbProgress) {
				cbProgress(offs / total);
			}
		}, err => {
			if (err) {
				reject(err);
			} else {
				var hash = alg.finalize();
				var hashHex = hash.toString(CryptoJS.enc.Hex);
				resolve(hashHex);
			}
		});
	});
}

function readChunked(file, chunkCallback, endCallback) {
	if (isCancelado) 
		return;

	var fileSize   = file.size;
	var chunkSize  = 10 * 1024 * 1024;
	var offset     = 0;
	var reader = new FileReader();
	reader.onload = function() {
		if (reader.error) {
		 	endCallback(reader.error || {});
			return;
		}
		offset += reader.result.length;
		chunkCallback(reader.result, offset, fileSize); 
		if (offset >= fileSize) {
			endCallback(null);
			return;
		}
		readNext();
	};
	reader.onerror = function(err) {
		endCallback(err || {});
	};

	function readNext() {
		if (isCancelado) 
			return;
		var fileSlice = file.slice(offset, offset + chunkSize);
	    reader.readAsBinaryString(fileSlice);
	}
	readNext();
}

function enviarArq (arquivo, hsh, tam) {
	let parms = JSON.stringify({ arquivoNome: arquivo.name, tamanho: tam, hash: hsh});
	xhr = new XMLHttpRequest();
	xhr.open("POST", url + "upload");
	xhr.upload.addEventListener("progress", setarProgressoEnvio, false);
	xhr.setRequestHeader("parms", parms);
	xhr.onload = function(e) {
	  if (this.readyState == 4 && this.status == 200) {
    	  setarProgressBar(100);
		  let parmsObj = JSON.parse(this.responseText);
    	  document.getElementById("tokenArquivo").value = parmsObj.token;
    	  document.getElementById('linkArquivo').innerHTML = '<i class="far fa-file-pdf mr-2"></i>' + parmsObj.arquivoNome;
    	  document.getElementById('linkArquivo').parentNode.parentNode.parentNode.classList.remove('d-none');
    	  inputArquivo.parentNode.classList.add('d-none');
    	  alert("Upload realizado com sucesso. Salve o documento para efetivar os dados.")
    	  document.getElementById("barraProgresso").classList.add('d-none');
	  } else {
	      if (this.readyState == 4 && this.status == 403) {
	    	  window.location.href = "/siga/public/app/login" 
	      } else {
	    	  if (this.status >= 300) { 
		    	  alert(this.response);
		    	  resetarArquivoUpload();
		  	  }
	      }
	  }
	};
	xhr.onerror = function(e){
		alert("Servidor de upload não está disponível. Por favor tente mais tarde.");
		resetarArquivoUpload();
	}
	xhr.withCredentials = true;
	xhr.send(arquivo);
}

function setarProgressoEnvio(event) {
	setarProgressBar(Math.round((event.loaded / event.total) * 70) + 30);
	if (event.loaded === event.total) {
	  	document.getElementById('msgProgressBar').textContent = "Arquivo enviado, gravando no servidor...";
	} else { 
	  	document.getElementById('msgProgressBar').textContent = "Enviou "  + event.loaded + " de um total de " + event.total + " bytes...";
	}
}

function setarProgressBar(percentualProgresso) {
	let bar = document.querySelector("#barraProgresso .progress-bar");
	if (bar) {
		bar.style.width = percentualProgresso + "%";
		bar.textContent = percentualProgresso + "%";
	}
}

function removerArq () {
	tokenArquivo = document.getElementById("tokenArquivo").value;
	if (!tokenArquivo || tokenArquivo === "")
		error("Token não informado para exclusão do arquivo");
	resetarArquivoUpload();
	xhr = new XMLHttpRequest();
	xhr.open("DELETE", url + "remover");
	xhr.setRequestHeader("tokenArquivo", tokenArquivo);
	xhr.withCredentials = true;
	xhr.send(arquivo);
}

function abortarUpload() {
	if (jaAvisou)
		return;
	if (xhr)
		xhr.abort();
	alert("Upload cancelado.")
	resetarArquivoUpload();
}

function resetarArquivoUpload() {
	jaAvisou = true;
	document.getElementById("barraProgresso").classList.add('d-none');
	setarProgressBar(0);
	document.getElementById('msgProgressBar').textContent = "";
	isCancelado = true;
	inputArquivo.parentNode.classList.remove('d-none');
	inputArquivo.disabled = false;
	inputArquivo.value = null;
    document.getElementById('linkArquivo').parentNode.parentNode.parentNode.classList.add('d-none');
    document.getElementById('linkArquivo').value = null;
}