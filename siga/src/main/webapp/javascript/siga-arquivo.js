var xhr;
var inputArquivo;
var arquivo;
var url;
var isCancelado = false;

function uploadArq(urlBase, inputArq, tamMaximo){
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
	document.getElementById("barraProgresso").classList.remove('d-none');

    url = urlBase;
	let ht = calculaSHA256(arquivo)
		.then(hs => {
				enviarArq(arquivo, hs, arquivo.size)
			})
			.catch(err => console.log(err))
}

function enviarArq (arquivo, hsh, tam) {
	let parms = JSON.stringify({ arquivoNome: arquivo.name, tamanho: tam, hash: hsh});
	xhr = new XMLHttpRequest();
	xhr.open("POST", url + "upload");
	xhr.upload.addEventListener("progress", setarProgresso, false);
	xhr.setRequestHeader("parms", parms);
	xhr.onload = function(e) {
	  if (this.readyState == 4 && this.status == 200) {
    	  setarProgressBar(100);
		  let parmsObj = JSON.parse(this.responseText);
    	  document.getElementById("tokenArquivo").value = parmsObj.token;
    	  let urlbase = window.location;
    	  document.getElementById("urlUpload").value = urlbase.protocol + "//" + urlbase.host;
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
		    	  alert('Ocorreu um erro no envio do arquivo (HTTP error ' + this.status + '). Por favor tente mais tarde.');
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

function setarProgresso(event) {
	setarProgressBar(Math.round((event.loaded / event.total) * 100));
	if (event.loaded === event.total) {
	  	document.getElementById('msgProgressBar').textContent = "Arquivo recebido, gravando no servidor...";
	} else { 
	  	document.getElementById('msgProgressBar').textContent = "Recebeu "  + event.loaded + " de um total de " + event.total + " bytes...";
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
	xhr.abort();
	alert("Upload cancelado.")
	resetarArquivoUpload();
}

function resetarArquivoUpload() {
	document.getElementById("barraProgresso").classList.add('d-none');
	isCancelado = true;
	inputArquivo.parentNode.classList.remove('d-none');
	inputArquivo.disabled = false;
	inputArquivo.value = null;
    document.getElementById('linkArquivo').parentNode.parentNode.parentNode.classList.add('d-none');
    document.getElementById('linkArquivo').value = null;
}

function setarProgressBar(percentualProgresso) {
	let bar = document.querySelector("#barraProgresso .progress-bar");
	if (bar) {
		bar.style.width = percentualProgresso + "%";
		bar.textContent = percentualProgresso + "%";
	}
}

async function calculaSHA256(arq) {
	let bf = await arq.arrayBuffer();
	let uint8Array = await new Uint8Array(bf);
	let hs = await window.crypto.subtle.digest('SHA-256', uint8Array );
	const hsArray = Array.from(new Uint8Array(hs))
    return hsArray.map(b => b.toString(16).padStart(2, '0')).join('');
}