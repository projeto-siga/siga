const tamanhoChunk = 5242880;
const LINK_DOWNLOAD = "/sigaex/app/arquivo/downloadFormatoLivre?tokenArquivo=";

var formData = new FormData();
var inputArquivo;
var blob;
var url;
var tamanhoTotal;
var dataArquivo; 
var totalChunks; 
var counter = 0;
var uploadId = 0;
var arqNome;
var partETags = [];
var sequenciasErro = [];
var isCancelado = false;
var jaAvisou = false;
var qtdRequestsPendentes;
var msgProgressBar;
var hashArquivo;
/*
 * 	Funções para upload de arquivos grandes em partes
 */
function uploadArq(urlBase, inputArq, tamMaximo){
	inputArquivo = inputArq;
	blob = inputArq.files[0];
	tamanhoTotal = blob.size;
	if (blob) { 
		dataArquivo = new Date(blob.lastModified) 
		if((parseInt(tamanhoTotal)) > tamMaximo) { 
			alert('Tamanho do arquivo excede o máximo permitido de ' + tamMaximo / 1024 * 1024 + ' MB');
			return;
		}
	} else {
		alert('Arquivo não informado. Escolha o arquivo para upload.');
    }
	inputArq.disabled = true;
	isCancelado = false;
	jaAvisou = false;
//	insertProgressBar(true);

    url = urlBase;
	formData.set("arquivo", blob);
	let parms = JSON.stringify({ arquivoNome: blob.name, tamanho: tamanhoTotal, sequencia: 1 });
	formData.set("parms", parms);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", url + "upload");
	xhr.onload = function(e) {
	  if (this.readyState == 4 && this.status == 200) {
		  let respobj = JSON.parse(this.responseText);
	      uploadId = respobj.uploadId;
	      atualizaBarraProgresso(uploadId)
	  } else {
	      if (this.readyState == 4 && this.status == 403) {
	    	  window.location.href = "/siga/public/app/login" 
	      } else {
	    	  if (this.status >= 300) 
		    	  alert('Ocorreu um erro no envio do arquivo (HTTP error ' + this.status + 
		    			  '). Por favor tente mais tarde.');
	      }
	  }
	};
	xhr.onerror = function(e){
		inputArq.disabled = true;
		alertaServidorIndisponivel();
	}
	xhr.withCredentials = true;
	xhr.send(formData);
}

function atualizaBarraProgresso(uploadId) {
	insertProgressBar(true);
	var qtdVezes = 0;
	var intervalId = setInterval(function () {
		qtdVezes++;
        if (isCancelado) {
        	clearInterval(intervalId);
        	return;
        }
        verProgressoUpload(uploadId)
        
		if (qtdVezes > 200) { // Demorou, aborta upload
			abortarUpload();
			clearInterval(intervalId);
		}
	}, 2000);
}

function verProgressoUpload (uploadId) {
	let parms = JSON.stringify({ arquivoNome: blob.name, sequencia: 1, uploadId: uploadId });
	formData.delete("arquivo");
	formData.set("parms", parms);
	var xhr = new XMLHttpRequest();
	xhr.open("GET", url + "verProgressoUpload");
	xhr.onload = function(e) {
	  if (this.readyState == 4 && this.status == 200) {
	      bytesTransferidos = this.responseText;
		  setaProgressBar(Math.round(100 / tamanhoTotal * bytesTransferidos));
		  msgProgressBar.textContent = "Recebeu parte " + seq + "/" + totalChunks + "(qtd pendentes a receber : " + qtdRequestsPendentes
	  } else {
	      if (this.readyState == 4 && this.status == 403) {
	    	  window.location.href = "/siga/public/app/login" 
	      } else {
	    	  if (this.status >= 300) {
	    	  	if (!jaAvisou) { 
	    	  		isCancelado = true;
	    	  		jaAvisou = true;
		    		alert('Ocorreu um erro no envio do arquivo (HTTP error ' + this.status + 
		    			  '). Por favor tente mais tarde.');
		   		}
		   	  }
	      }
	  }
	};
	xhr.onerror = function(e){
		alertaServidorIndisponivel();
	}
	xhr.withCredentials = true;
	xhr.send(formData);
}

function uploadArquivo(urlBase, inputArq, tamMaximo){
	if (trataUploadPendente(urlBase, inputArq))
		return
    inputArquivo = inputArq;
    partETags = [];
	blob = inputArq.files[0];
	tamanhoTotal = blob.size;
	if (blob) { 
		dataArquivo = new Date(blob.lastModified) 
		if((parseInt(tamanhoTotal)) > tamMaximo) { 
			alert('Tamanho do arquivo excede o máximo permitido de ' + tamMaximo / 1024 * 1024 + ' MB');
			return;
		}
	} else {
		alert('Arquivo não informado. Escolha o arquivo para upload.');
    }
	inputArquivo.disabled = true;
	insertProgressBar(true);
	isCancelado = false;
	jaAvisou = false;

    url = urlBase;
    totalChunks = Math.max(Math.ceil(tamanhoTotal / tamanhoChunk), 1);
    let chunk = blob.slice(0, tamanhoChunk);
	let ht = calculaSHA256(blob)
		.then(ht => {
			let hs = calculaSHA256(chunk)
				.then(hash => {
					postPrimeiroChunk(chunk, hash, ht)
				})
				.catch(err => console.log(err))
		})
		.catch(err => console.log(err))
}

function postPrimeiroChunk (chunk, hash, hashTotal) {
	hashArquivo = hashTotal;
	formData.set("arquivo", chunk, blob.name);
	let parms = JSON.stringify({ arquivoNome: blob.name, sequencia: 1, hash: hash });
	formData.set("parms", parms);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", url + "uploadIniciar");
	xhr.onload = function(e) {
	  if (this.readyState == 4 && this.status == 200) {
		  let respobj = JSON.parse(this.responseText);
	      uploadId = respobj.uploadId;
	      arqNome = respobj.arquivoNomeS3;
	      if (!existeEmPartETags(respobj.partETag.partNumber))
	    	  partETags.push(respobj.partETag);
	      enviaRestante();
	  } else {
	      if (this.readyState == 4 && this.status == 403) {
	    	  window.location.href = "/siga/public/app/login" 
	      } else {
	    	  if (this.status >= 300) 
		    	  alert('Ocorreu um erro no envio do arquivo (HTTP error ' + this.status + 
		    			  '). Por favor tente mais tarde.');
	      }
	  }
	};
	xhr.onerror = function(e){
		alertaServidorIndisponivel();
	}
	xhr.withCredentials = true;
	xhr.send(formData);
}

function alertaServidorIndisponivel() {
	if (!jaAvisou) {
		alert("Servidor de upload não está disponível. Por favor tente mais tarde.");
		resetaArquivoUpload();
		jaAvisou = true;
	}
}

function trataUploadPendente(urlBase, inputArq) {
	blob = inputArq.files[0];
	let temUploadPendente = false
	let jsonUpload = JSON.parse(window.localStorage.getItem(getCadastrante() + "upload"));
	if (jsonUpload) {
		if (blob && jsonUpload.arqNome.endsWith(blob.name)) {
			if (confirm("Há um upload pendente do arquivo " + jsonUpload.arqNome + ". Deseja continuar?")) {
				continuaUpload(urlBase, inputArq, jsonUpload);
				temUploadPendente = true
			} else {
				window.localStorage.removeItem(getCadastrante() + "upload")
			}
		}
	}
	return temUploadPendente;
}

function continuaUpload(urlBase, inputArq, jsonUpload){
    inputArquivo = inputArq;
    partETags = jsonUpload.partETags;
    sequenciasErro = jsonUpload.sequenciasErro;
    uploadId = jsonUpload.uploadId;
    arqNome = jsonUpload.arqNome;
	tamanhoTotal = blob.size;
	if (blob) { 
		dataArquivo = new Date(blob.lastModified) 
	} else {
		alert('Arquivo não informado. Escolha o arquivo para upload.');
    }
	inputArquivo.disabled = true;
	insertProgressBar(true);
	isCancelado = false;
	jaAvisou = false;
    url = urlBase;
    totalChunks = Math.max(Math.ceil(tamanhoTotal / tamanhoChunk), 1);
    msgProgressBar.textContent = "Continuando o upload do arquivo " + arqNome
    enviaRestante();
}


function enviaRestante() {
    setaProgressBar(Math.round(100 / totalChunks * partETags.length));
    let seq = 1;
    qtdRequestsPendentes = 0;
    
	// Solicita o envio das outras partes
	while( seq < totalChunks ) {
        if (isCancelado)
        	return;
		if (qtdRequestsPendentes < 20) { // Limita a qtd de requests concorrentes para nao sobrecarregar memoria
	        seq++;
	       	enviaChunk(seq)
		}
	}
	enviouTodasPartes();
}

function existeEmPartETags(seq) { // Verifica se ja enviou o chunk
	for(let i = 0; i < partETags.length; i++) {
	    if(partETags[i].partNumber === seq)
	    	return true;
	}
	return false;
}


function retryPartesComErro() {
    // Tenta enviar novamente as partes que deram erro no upload
    counterRetry = 0;	
    for (let counterRetry = 0; counterRetry < 5; counterRetry++ ) {
        if (isCancelado)
        	return;
        msgProgressBar.textContent = "Solicitando novamente partes que deram erro..."
   		if (qtdRequestsPendentes < 20) { // Limita a qtd de requests concorrentes para nao sobrecarregar memoria
        	enviaChunk(sequenciasErro[i]);
        	i++;
   		}
    }
	if (partETags.length < totalChunks) {
		alert('Ocorreu um erro no envio do arquivo. Por favor tente mais tarde.');
		return;
	}
	enviouTodasPartes();
}

function enviouTodasPartes() {
	var qtdVezes = 0;
	var intervalId = setInterval(function () {
		qtdVezes++;
        if (isCancelado) {
        	clearInterval(intervalId);
        	return;
        }
        	 
		if (partETags.length >= totalChunks) { // Aguarda recebimento de todos chunks para finalizar
			finalizaUpload();
			clearInterval(intervalId);
		}
		if (qtdVezes > 200) { // Demorou, aborta upload
			abortarUpload();
			clearInterval(intervalId);
		}
	}, 2000);
}

function enviaChunk(seq) {
    if (existeEmPartETags(seq)) 
    	return;
	inicioChunk = (seq - 1) * tamanhoChunk;
	let fimChunk = inicioChunk + tamanhoChunk;
	if (fimChunk > tamanhoTotal)
		fimChunk = tamanhoTotal;
    let chunk = blob.slice(inicioChunk, fimChunk);
    
	let hs = calculaSHA256(chunk)
		.then(hash => {
		    postChunk(seq, chunk, hash);
		})
		.catch(err => console.log(err))
}

function postChunk(seq, chunk, hash) {
    formData.set("arquivo", chunk, blob.name);
	let parms = JSON.stringify({ arquivoNome: blob.name, arquivoNomeS3: arqNome, sequencia: seq, hash: hash, uploadId: uploadId });
	formData.set("parms", parms);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url  + "uploadEnviarParte");
    xhr.onload = function(e) {
    	// Trata cada retorno de upload e salva info de part (sequencia)/ETag em um array 
    	// para enviar na finalização do upload
        if (isCancelado)
        	return;
    	if (this.readyState == 4 && this.status == 200) {
			let respobj = JSON.parse(this.responseText);
	        if (!existeEmPartETags(respobj.partETag.partNumber))
	        	partETags.push(respobj.partETag);
			setaProgressBar(Math.round(100 / totalChunks * partETags.length));
			window.localStorage.setItem(getCadastrante() + "upload", 
					JSON.stringify({arqNome:arqNome,uploadId:uploadId,partETags:partETags,sequenciasErro:sequenciasErro}));
		    msgProgressBar.textContent = "Recebeu parte " + seq + "/" + totalChunks + "(qtd pendentes a receber : " + qtdRequestsPendentes
			qtdRequestsPendentes--
        } else {
            if (this.readyState == 4 && this.status == 403) {
            	window.location.href = "/siga/public/app/login"
            	qtdRequestsPendentes--
        	} else {
		    	if (this.status >= 300) 
	        		// Ocorreu um erro no envio, guarda a sequencia para tentar novamente no final
	        		sequenciasErro.push(seq);
		    		qtdRequestsPendentes--
        	}
       }
    };
    xhr.onerror = function(e){
		alertaServidorIndisponivel();
    }
    xhr.withCredentials = true;
    msgProgressBar.textContent = "Enviando parte " + seq + "/" + totalChunks
    xhr.send(formData);
    qtdRequestsPendentes++;
}

function finalizaUpload() {
	formData.delete("arquivo");
	let parms = JSON.stringify({ arquivoNome: blob.name, arquivoNomeS3: arqNome, sequencia: totalChunks, uploadId: uploadId, tamanho: tamanhoTotal, hash: hashArquivo, partETags: partETags });
	formData.set("parms", parms);
	var xhr = new XMLHttpRequest();
    xhr.open("POST", url  + "uploadFinalizar");
    xhr.onload = function(e) {
      if (this.readyState == 4 && this.status == 200) {
    	  inputArquivo.parentNode.insertAdjacentHTML('afterend', "<input type='hidden' name='vars' class='uploadclass' value='tokenArquivo'>" +
    	  		"<input type='hidden' id='tokenArquivo' class='uploadclass' " +
    	  		"name='tokenArquivo' value='" + this.responseText + "' >");
    	  setaProgressBar(100);
    	  alert("Upload realizado com sucesso. Salve o documento para efetivar os dados.")
    	  document.getElementById("barraProgresso").innerHTML = "";
    	  inputArquivo.parentNode.insertAdjacentHTML('afterend', 
    			"<input type='hidden' name='vars' class='uploadclass' value='nomeArquivo'>" +
    			"<input type='hidden' id='nomeArquivo' class='uploadclass' " +
        	  	"name='nomeArquivo' value='" + arqNome.split("/")[1] + "' >");
    	  inputArquivo.parentNode.insertAdjacentHTML('afterend', 
    			"<input type='hidden' name='vars' class='uploadclass' value='tamanhoArquivo'>" +
    	  		"<input type='hidden' id='tamanhoArquivo' class='uploadclass' " +
      	  		"name='tamanhoArquivo' value='" + tamanhoTotal + "' >");
    	  inputArquivo.parentNode.insertAdjacentHTML('afterend', 
    			"<input type='hidden' name='vars' class='uploadclass' value='dataArquivo'>" +
    	  		"<input type='hidden' id='dataArquivo' class='uploadclass' " +
        	  		"name='dataArquivo' value='" + dataArquivo + "' >");
    	  inputArquivo.parentNode.insertAdjacentHTML('afterend', 
    			"<input type='hidden' name='vars' class='uploadclass' value='hashArquivo'>" +
    	  		"<input type='hidden' id='hashArquivo' class='uploadclass' " +
        	  		"name='hashArquivo' value='" + hashArquivo + "' >");
    	  let urlbase = window.location;
    	  inputArquivo.parentNode.insertAdjacentHTML('afterend', 
    			"<input type='hidden' name='vars' class='uploadclass' value='urlUpload'>" +
    	  		"<input type='hidden' id='urlUpload' class='uploadclass' " +
      	  		"name='urlUpload' value='" + urlbase.protocol + "//" + urlbase.host + "' >");
    	  document.getElementById('linkArquivo').innerHTML = '<i class="far fa-file-pdf mr-2"></i>' + arqNome.split("/")[1];
    	  document.getElementById('linkArquivo').parentNode.parentNode.parentNode.classList.remove('d-none');
    	  inputArquivo.parentNode.classList.add('d-none');
    	  window.localStorage.removeItem(getCadastrante() + "upload");
	  } else {
		  if (this.readyState == 4 && this.status == 403) {
			  window.location.href = "/siga/public/app/login" 
		  } else {
			  if (this.status >= 300) {
				  alert('Ocorreu um erro ao tentar finalizar o upload (HTTP error ' + 
						  this.status + '). Por favor tente mais tarde.');
				  resetaArquivoUpload();
			  }
		  }
	  }
    };
    xhr.onerror = function(e){
		alertaServidorIndisponivel();
    }
    xhr.withCredentials = true;
    msgProgressBar.textContent = "Solicitando finalização do upload..."
    xhr.send(formData);
}

function abortarUpload() {
	formData.delete("arquivo");
	let parms = JSON.stringify({ arquivoNome: blob.name, arquivoNomeS3: arqNome, uploadId: uploadId });
	formData.set("parms", parms);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url  + "uploadAbortar");
    xhr.onload = function(e) {
		alert("Upload cancelado.")
		resetaArquivoUpload();
    };
    xhr.onerror = function(e){
    	alertaServidorIndisponivel();
    }
    xhr.withCredentials = true;
    msgProgressBar.textContent = "Solicitando cancelamento do upload..."
    xhr.send(formData);
}

function resetaArquivoUpload() {
	if (document.getElementById("barraProgresso"))
		document.getElementById("barraProgresso").innerHTML = "";
	isCancelado = true;
	document.querySelectorAll(".uploadclass").forEach(el => el.remove());
	inputArquivo.parentNode.classList.remove('d-none');
	inputArquivo.disabled = false;
	inputArquivo.value = null;
    document.getElementById('linkArquivo').parentNode.parentNode.parentNode.classList.add('d-none');
}

function setaProgressBar(percentualProgresso) {
	let bar = document.querySelector("#barraProgresso .progress-bar");
	if (bar) {
		bar.style.width = percentualProgresso + "%";
		bar.textContent = percentualProgresso + "%";
	}
}

function insertProgressBar(isUpload) {
	inputArquivo.parentNode.insertAdjacentHTML('afterend', '<div id="barraProgresso" name="barraProgresso" class="mt-2">'
			+ '<small id="msgProgressBar" class="text-muted"></small><div class="progress"><div class="progress-bar" ' 
			+ 'role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div></div>'
			+ '<button type="button" class="btn btn-sm btn-primary mt-1" onclick="abortar'
			+ (isUpload ? 'Upload' : 'Download')
			+ '();">Cancelar</button></div>');
	msgProgressBar = document.getElementById('msgProgressBar');
}

async function calculaSHA256(arq) {
	let bf = await arq.arrayBuffer();
	let uint8Array = await new Uint8Array(bf);
	let hs = await window.crypto.subtle.digest('SHA-256', uint8Array );
	const hsArray = Array.from(new Uint8Array(hs))
    return hsArray.map(b => b.toString(16).padStart(2, '0')).join('');
}