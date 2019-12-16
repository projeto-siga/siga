var newwindow = '';

function testpdf(x) {
	padrao = /\.pdf/;
	a = x.arquivo.value;
	if(a.length > 3) {
		a = a.substr(0, a.length - 3) + a.substring(a.length - 3, a.length).toLowerCase();
	}
	OK = padrao.exec(a);
	if (a != '' && !OK) {
		window.alert("Somente é permitido anexar arquivo PDF!");
		x.arquivo.value = '';
		x.arquivo.focus();
	}
}

/*function popitup(url) {
	if (!newwindow.closed && newwindow.location) {
		console.log("teste 1"); 
	} else {

	var popW = 600;
	var popH = 400;
	var winleft = (screen.width - popW) / 2;
	var winUp = (screen.height - popH) / 2;
	winProp = 'width=' + popW + ',height=' + popH + ',left=' + winleft
	+ ',top=' + winUp + ',scrollbars=yes,resizable'
	//newwindow = window.open(url, 'mov', winProp);
	newwindow = window.open(url,'_blank', 'mov');
	// newwindow.name='mov';
	// newwindow.navigate(url);
	}

	if (window.focus) {
		newwindow.focus()
	}
}*/

function popitup(url) {
	var popW = screen.width * 0.8;
	var popH = screen.height * 0.8;
	var winleft = (screen.width - popW) / 2;
	var winUp = (screen.height - popH) / 2;
	
	var nameWindow =  url.replace(/[^a-zA-Z 0-9]+/g,'');
	
	//Redimensionamento da janela pop up apenas para a opção despachar/transferir
	if(nameWindow.indexOf("transferir") > -1){
		popW = 900;
		popH = 600;
		winleft = 100;
		winUp = 100;
	}
	
	winProp = 'width=' + popW + ',height=' + popH + ',left=' + winleft
	+ ',top=' + winUp + ',scrollbars=yes,resizable';
	
	newwindow = window.open(url, nameWindow, winProp);

	if (window.focus) {
		newwindow.focus()
	}
}

function NewWindow(mypage, myname, w, h, scroll, size) {
	if (b_versao >= 4) {
		LeftPosition = (screen.width) ? (screen.width - w) / 2 : 0;
		TopPosition = (screen.height) ? (screen.height - h) / 2 : 0;
	} else {
		LeftPosition = 100;
		TopPosition = 100;
	}
	settings = 'height=' + h + ',width=' + w + ',top=' + TopPosition + ',left='
	+ LeftPosition + ',scrollbars=' + scroll + ',resizable=' + size
	+ ''
	win = window.open(mypage, myname, settings)
	if (win.window.focus) {
		win.window.focus();
	}
}

function tokenize(url) {
	return url + '&webwork.token='
	+ document.getElementsByName('webwork.token')[0].value
	+ '&webwork.token.name='
	+ document.getElementsByName('webwork.token.name')[0].value;
}

//Abre janela com o resultado das pesquisas
function abreEnquete(clicou_voto, form, num_opcao) {
	window
	.open(
			"",
			"enquete",
	"directories=no,height=276,width=520,hotkeys=no,location=no,menubar=no,resizable=no,scrollbars=yes,status=no,toolbar=no,copyhistory=no");

	// Se receber o nome do formulário por parâmetro, utiliza-o;
	// caso contrário, usa o nome do formulário padrão (enquete)
	// esta possibilidade está aberta para o caso de termos mais
	// de uma pesquisa na mesma página ao mesmo tempo

	form.voto.value = clicou_voto;
	form.opcao.value = num_opcao;
	form.submit();
}

function displayTitular(thisElement) {
	var thatElement = document.getElementById('tr_titular');
	if (thisElement.checked)
		thatElement.style.display = '';
	else
		thatElement.style.display = 'none';
}

function avisoColorido(msg, cor, tempo) {
	var quadroAviso = document.getElementById('quadroAviso');
	quadroAviso.style.backgroundColor = cor;
	quadroAviso.innerHTML = msg;
	quadroAviso.style.left = 0;
	var posicionaQuadro = function() {
		quadroAviso.style.top = document.body.scrollTop;
	};
	posicionaQuadro();
	window.onscroll = posicionaQuadro;
	quadroAviso.style.visibility = 'visible';
	setTimeout('escondeAvisoColorido()', (tempo ? tempo : 5000));
}

function escondeAvisoColorido() {
	document.getElementById("quadroAviso").style.visibility = 'hidden';
	window.onscroll = null;
}

function avisoVerde(msg, tempo) {
	avisoColorido(msg, 'green', tempo);
}

function avisoVermelho(msg, tempo) {
	avisoColorido(msg, 'red', tempo);
}

function avisoAzul(msg, tempo) {
	avisoColorido(msg, 'blue', tempo);
}

var carregando = false;
function isCarregando() {
	return carregando;
}
function carrega() {
	carregando = true;
}
function descarrega() {
	carregando = false;
}

function verifica_data(data, naoObriga) {
	mydata = new String(data.value);
	var mySplit;
	var msg = "";
	if (mydata.length > 0) {
		mySplit = mydata.split("/");
		if (mySplit[0] == mydata) {
			mydata = mydata.substring(0, 2) + "/" + mydata.substring(2, 4)
			+ "/" + mydata.substring(4, mydata.length);
			mySplit = mydata.split("/");
		}

		dia = mySplit[0];
		mes = mySplit[1];
		ano = mySplit[2];

		if ((dia == null) || (mes == null) || (ano == null) || (dia == "")
				|| (mes == "") || (ano == "")) {
			msg = msg
			+ "A data deve estar num dos seguintes formatos: DD/MM/AAAA ou DDMMAAAA\n";
			data.style.color = "red";
			alert(msg);
			return;
		}

		if (isNaN(dia) || isNaN(mes) || isNaN(ano)) {
			msg = msg + "A data só pode conter caracteres numéricos\n";
			data.style.color = "red";
			alert(msg);
			return;
		}

		// verifica o dia valido para cada mes
		if (((dia < 1 || dia > 31) || (dia > 30)
				&& (mes == 4 || mes == 6 || mes == 9 || mes == 11))
				|| (mes == 2 && (dia > 29 || (dia > 28 && (parseInt(ano / 4) != ano / 4))))) {
			msg = msg + "Dia inválido\n";
		}

		// verifica se o mes e valido
		if (mes < 1 || mes > 12) {
			msg = msg + "Mês inválido\n";
		}

		if (msg.length > 0) {
			data.style.color = "red";
			alert(msg);
			return;
		}
		if (dia.length < 2) {
			dia = "00" + dia;
			dia = dia.substring(dia.length - 2, dia.length);
		}
		if (mes.length < 2) {
			mes = "00" + mes;
			mes = mes.substring(mes.length - 2, mes.length);
		}
		if (ano.length == 2 && ano >= 80) {
			ano = "19" + ano;
		}
		if (ano.length < 4) {
			ano = "00" + ano;
			ano = "2" + ano.substring(ano.length - 3, ano.length);
		}
		data.value = dia + "/" + mes + "/" + ano;
		data.style.color = "black";
	}
}

//*-------------------------------------------------------------*//
/*
 * Funcao para consistir horas digitadas.
 * Data da Criacao : 18/03/2011 
 * chamada : onblur="javascript:verifica_hora(this)
 */
//*-------------------------------------------------------------*//
function verifica_hora(hora,naoObriga){ 
	myhora = new String(hora.value); 
	var mySplit;
	var msg="";
	if (myhora.length==0 && naoObriga==null) {
		msg=msg + "O campo hora deve ser preenchido\n";
		alert(msg);
		return;
	}
	if (myhora.length>0) {
		mySplit = myhora.split(":"); 
		if (mySplit[0]==myhora) {
			myhora=myhora.substring(0,2)+":"+myhora.substring(2,4);
			mySplit = myhora.split(":");	
		}

		hrs = mySplit[0];
		min = mySplit[1];

		if ((hrs==null) || (min==null) ||(hrs=="") || (min=="")) {
			msg=msg + "A hora deve estar num dos seguintes formatos: HH:MM ou HHMM\n";
			alert(msg);
			return;
		}

		if (isNaN(hrs) || isNaN(min)) {
			msg=msg + "A hora sÃ³ pode conter caracteres numÃ©ricos\n";
			alert(msg);
			return;
		}

		// verifica hrs e min 
		if ((hrs < 00 ) || (hrs > 23) || ( min < 00) ||( min > 59)){ 
			msg=msg + "Hora invÃ¡lida!";
			alert(msg);
			return; 
		}
		if (msg.length>0) { 
			alert(msg); 
			return;
		}
		if (hrs.length<2) {
			hrs="00"+hrs;
			hrs=hrs.substring(hrs.length-2,hrs.length);
		}
		if (min.length<2) {
			min="00"+min;
			min=min.substring(min.length-2,min.length);
		}
		hora.value=hrs+":"+min;
	}
} 

//*-------------------------------------------------------------*//
/*
 * Funcao para negar a digitacao de caracteres nao numericos Exceto o "." Data
 * da Criacao : 02/01/2007 chamada : onkeypress="return sonumeroMesmo(event)"
 */
//*-------------------------------------------------------------*//
function sonumeroMesmo(e) {
	var key = '';
	var strCheck = '0123456789';
	var whichCode = (window.Event) ? e.which : e.keyCode;
	if (whichCode == 13)
		return true; // Enter
	if (whichCode == 8)
		return true; // BackSpace
	if (whichCode == 0)
		return true; // Del
	key = String.fromCharCode(whichCode); // Get key value from key code
	if (strCheck.indexOf(key) == -1)
		return false; // Not a valid key
}

function handleEnter(field, event) {
	var keyCode = event.keyCode ? event.keyCode : event.which ? event.which
			: event.charCode;
	if (keyCode == 13) {
		var i;
		for (i = 0; i < field.form.elements.length; i++)
			if (field == field.form.elements[i])
				break;
		i = (i + 1) % field.form.elements.length;
		field.form.elements[i].focus();
		return false;
	} else
		return true;
}
//*-------------------------------------------------------------*//
/*
 * Funcao para Formatar numeros Data da Criacao : 07/01/2007 chamada :
 * onkeypress="return formataReais(this,'.',',',event)"
 */
//*-------------------------------------------------------------*//
function formataReais(fld, milSep, decSep, e) {
	var sep = 0;
	var key = '';
	var i = j = 0;
	var len = len2 = 0;
	var strCheck = '0123456789';
	var aux = aux2 = '';
	var whichCode = (window.Event) ? e.which : e.keyCode;
	if (whichCode == 13)
		return true;
	key = String.fromCharCode(whichCode);// Valor para o código da Chave
	if (strCheck.indexOf(key) == -1)
		return false; // Chave inválida
	len = fld.value.length;
	for (i = 0; i < len; i++)
		if ((fld.value.charAt(i) != '0') && (fld.value.charAt(i) != decSep))
			break;
	aux = '';
	for (; i < len; i++)
		if (strCheck.indexOf(fld.value.charAt(i)) != -1)
			aux += fld.value.charAt(i);
	aux += key;
	len = aux.length;
	if (len == 0)
		fld.value = '';
	if (len == 1)
		fld.value = '0' + decSep + '0' + aux;
	if (len == 2)
		fld.value = '0' + decSep + aux;
	if (len > 2) {
		aux2 = '';
		for (j = 0, i = len - 3; i >= 0; i--) {
			if (j == 3) {
				aux2 += milSep;
				j = 0;
			}
			aux2 += aux.charAt(i);
			j++;
		}
		fld.value = '';
		len2 = aux2.length;
		for (i = len2 - 1; i >= 0; i--)
			fld.value += aux2.charAt(i);
		fld.value += decSep + aux.substr(len - 2, len);
	}
	return false;
}
//*-------------------------------------------------------------*//
/*
 * Funcao para retornar o valor por extenso de um numero Data da Criacao :
 * 07/01/2007 chamada : onBlur="extenso(${var}); "
 */
//*-------------------------------------------------------------*//
function extenso(label, valor) {
	var m = valor.value;
	var c = m.replace(".", "");
	if (c == "" || c < 0 || c >= 10000000) {
		return (-1);
	} else {
		if (c == 0) {
			return (" ZERO ");
		} else {
			aUnid = new Array();
			aDezena = new Array();
			aCentena = new Array();
			aGrupo = new Array();
			aTexto = new Array();

			aUnid[1] = "Um ";
			aUnid[2] = "Dois ";
			aUnid[3] = "Tres ";
			aUnid[4] = "Quatro ";
			aUnid[5] = "Cinco ";
			aUnid[6] = "Seis ";
			aUnid[7] = "Sete ";
			aUnid[8] = "Oito ";
			aUnid[9] = "Nove ";
			aUnid[10] = "Dez ";
			aUnid[11] = "Onze ";
			aUnid[12] = "Doze ";
			aUnid[13] = "Treze ";
			aUnid[14] = "Quatorze ";
			aUnid[15] = "Quinze ";
			aUnid[16] = "Dezesseis ";
			aUnid[17] = "Dezesete ";
			aUnid[18] = "Dezoito ";
			aUnid[19] = "Dezenove ";

			aDezena[1] = "Dez ";
			aDezena[2] = "Vinte ";
			aDezena[3] = "Trinta ";
			aDezena[4] = "Quarenta ";
			aDezena[5] = "Cinquenta ";
			aDezena[6] = "Sessenta ";
			aDezena[7] = "Setenta ";
			aDezena[8] = "Oitenta ";
			aDezena[9] = "Noventa ";

			aCentena[1] = "Cento ";
			aCentena[2] = "Duzentos ";
			aCentena[3] = "Trezentos ";
			aCentena[4] = "Quatrocentos ";
			aCentena[5] = "Quinhentos ";
			aCentena[6] = "Seiscentos ";
			aCentena[7] = "Setecentos ";
			aCentena[8] = "Oitocentos ";
			aCentena[9] = "Novecentos ";

			if (c.indexOf(".") != -1) {
				aGrupo[4] = c.substring(c.indexOf(".") + 1, c.length);
				aGrupo[4] = aGrupo[4].substring(0, 2);
				ct = c.substring(0, c.indexOf("."));
			} else {
				if (c.indexOf(",") != -1) {
					aGrupo[4] = c.substring(c.indexOf(",") + 1, c.length);
					aGrupo[4] = aGrupo[4].substring(0, 2);
					ct = c.substring(0, c.indexOf(","));
				} else {
					aGrupo[4] = "00";
					ct = c;
				}
				tt = "";
				for (f = 0; f < (10 - ct.length); f++) {
					tt += "0";
				}
				tt += ct;
			}
			aGrupo[1] = tt.substring(1, 4);
			aGrupo[2] = tt.substring(4, 7);
			aGrupo[3] = tt.substring(7, 10);
			aGrupo[4] = "0" + aGrupo[4];

			for (f = 1; f < 5; f++) {
				cParte = aGrupo[f];
				if (parseFloat(cParte) < 10) {
					nTamanho = 1;
				} else {
					if (parseFloat(cParte) < 100) {
						nTamanho = 2;
					} else {
						if (parseFloat(cParte) < 1000) {
							nTamanho = 3;
						} else {
							nTamanho = 0;
						}
					}
				}
				aTexto[f] = "";
				if (nTamanho == 3) {
					if (cParte.substring(1, 3) != "00") {
						aTexto[f] += aCentena[cParte.substring(0, 1)] + "e ";
						nTamanho = 2;
					} else {
						if (cParte.substring(0, 1) == "1") {
							aTexto[f] += "Cem ";
						} else {
							aTexto[f] += aCentena[cParte.substring(0, 1)];
						}
					}
				}
				if (nTamanho == 2) {
					if (parseFloat(cParte.substring(1, 3)) < 10) {
						aTexto[f] += aUnid[cParte.substring(2, 3)];
					} else {
						if (parseFloat(cParte.substring(1, 3)) < 20) {
							aTexto[f] += aUnid[cParte.substring(1, 3)];
						} else {
							aTexto[f] += aDezena[cParte.substring(1, 2)];
							if (cParte.substring(2, 3) != "0") {
								aTexto[f] += "e ";
								nTamanho = 1;
							}
						}
					}
				}
				if (nTamanho == 1) {
					aTexto[f] += aUnid[cParte.substring(2, 3)];
				}
			}
			if (parseFloat(aGrupo[1] + aGrupo[2] + aGrupo[3]) == 0
					&& parseFloat(aGrupo[4]) != 0) {
				cFinal = aTexto[4];
				if (parseFloat(aGrupo[4]) == 1) {
					cFinal += "Centavo";
				} else {
					cFinal += "Centavos";
				}
			} else {
				if (parseFloat(aGrupo[1]) != 0) {
					cFinal = aTexto[1];
					if (parseFloat(aGrupo[1]) > 1) {
						cFinal += "Milhões ";
					} else {
						cFinal += "Milhão ";
					}
					if (parseFloat(aGrupo[2] + aGrupo[3]) == 0) {
						cFinal += "de ";
					} else {
						cFinal += "e ";
					}
				} else {
					cFinal = "";
				}
				if (parseFloat(aGrupo[2]) != 0) {
					cFinal += aTexto[2] + "Mil ";
					if (parseFloat(aGrupo[3]) != 0) {
						cFinal += "e ";
					}
				}
				if (parseFloat(aGrupo[3]) != 0) {
					cFinal += aTexto[3];
				}
				if (parseFloat(aGrupo[1] + aGrupo[2] + aGrupo[3]) == 1) {
					cFinal += "Real";
				} else {
					cFinal += "Reais";
				}
				if (parseFloat(aGrupo[4]) != 0) {
					cFinal += " e " + aTexto[4];
					if (parseFloat(aGrupo[4]) == 1) {
						cFinal += "Centavo";
					} else {
						cFinal += "Centavos";
					}
				}
			}
			document.getElementById(label + "valorextenso").innerHTML = cFinal;
			document.getElementById(label + "vrextenso").value = cFinal;

		}
	}
}
//*-------------------------------------------------------------*//
/*
 * Funcao que nao permite digitacao de letras em campos numericos Data da
 * Criacao : 07/01/2007 chamada : onKeypress="return verificaNumero(event);"
 */
//*-------------------------------------------------------------*//
function verificaNumero(e) {
	var unicode = e.charCode ? e.charCode : e.keyCode
			if (unicode != 8) { // if the key isn't the backspace key (which we should
				// allow)

				if (unicode < 48 || unicode > 57) // if not a number
					return false // disable key press
			}
}
//*-------------------------------------------------------------*//
/*
 * Funcao que verifica se "#### TODOS ####" os campos do form foram preenchidos.
 * Data da Criacao : 11/01/2007 chamada : onchange="verificaCampos();"
 */
//*-------------------------------------------------------------*//
function verificaCampos() {
	var i;
	// Verifica se todos os campos foram preenchidos
	for (i = 0; i < 18; i++) {
		if (document.form3.elements[i].value == ""
			|| document.form3.elements[i].value == " ") {
			document.form3.elements[i].style.backgroundColor = "#FF333A";
			document.form3.elements[i].focus();
			alert("Preencha o campo '" + document.form3.elements[i].name
					+ "'.\nTodos os campos precisam ser preenchidos");
			return false;
		} else {
			document.form3.elements[i].style.backgroundColor = "transparent";
		}
	}
	alert("Formulário Válido");
	return false;
}

//*-------------------------------------------------------------*//
/*
 * Funcao que compara Datas. Data da Criacao : 11/01/2007 chamada :
 * onchange="dataMaiorIgual(datafim,datainicio);"
 */
//*-------------------------------------------------------------*//
//Verifica se a data1 é maior ou igual que a data 2
function dataMaiorIgual(dt1, dt2) {
	alert("Entrou na funcao data Maior");
	var hoje = new Date();
	var ano = hoje.getYear();

	if (ano >= 50 && ano <= 99)
		ano = 1900 + ano
		else
			ano = 2000 + ano;

	var pos1 = dt1.indexOf("/", 0)
	var dd = dt1.substring(0, pos1)
	pos2 = dt1.indexOf("/", pos1 + 1)
	var mm = dt1.substring(pos1 + 1, pos2)
	var aa = dt1.substring(pos2 + 1, 10)

	if (aa.length < 4)
		if (ano > 1999)
			aa = (2000 + parseInt(aa, 10))
			else
				aa = (1900 + parseInt(aa, 10));

	var data1 = new Date(parseInt(aa, 10), parseInt(mm, 10) - 1, parseInt(dd,
			10));
	var pos1 = dt2.indexOf("/", 0)
	var dd = dt2.substring(0, pos1)
	pos2 = dt2.indexOf("/", pos1 + 1)
	var mm = dt2.substring(pos1 + 1, pos2)
	var aa = dt2.substring(pos2 + 1, 10)
	if (aa.length < 4)
		if (ano > 80 && ano <= 99)
			aa = (1900 + parseInt(aa, 10))
			else
				aa = (2000 + parseInt(aa, 10));
	var data2 = new Date(parseInt(aa, 10), parseInt(mm, 10) - 1, parseInt(dd,
			10));

	if (data1 >= data2)
		return true;
	else
		return false;
}
//*-------------------------------------------------------------*//
/*
 * Funcao para retornar o valor literal de um numero Data da Criacao :
 * 22/01/2007 chamada : onBlur="numeroExtenso(${var}); "
 */
//*-------------------------------------------------------------*//
function numeroExtenso(label, valor) {
	var m = valor.value;
	var c = m.replace(".", "");
	if (c == "" || c < 0 || c >= 10000000) {
		return (-1);
	} else {
		if (c == 0) {
			return (" ZERO ");
		} else {
			aUnid = new Array();
			aDezena = new Array();
			aCentena = new Array();
			aGrupo = new Array();
			aTexto = new Array();

			aUnid[1] = "Um ";
			aUnid[2] = "Dois ";
			aUnid[3] = "Tres ";
			aUnid[4] = "Quatro ";
			aUnid[5] = "Cinco ";
			aUnid[6] = "Seis ";
			aUnid[7] = "Sete ";
			aUnid[8] = "Oito ";
			aUnid[9] = "Nove ";
			aUnid[10] = "Dez ";
			aUnid[11] = "Onze ";
			aUnid[12] = "Doze ";
			aUnid[13] = "Treze ";
			aUnid[14] = "Quatorze ";
			aUnid[15] = "Quinze ";
			aUnid[16] = "Dezesseis ";
			aUnid[17] = "Dezesete ";
			aUnid[18] = "Dezoito ";
			aUnid[19] = "Dezenove ";

			aDezena[1] = "Dez ";
			aDezena[2] = "Vinte ";
			aDezena[3] = "Trinta ";
			aDezena[4] = "Quarenta ";
			aDezena[5] = "Cinquenta ";
			aDezena[6] = "Sessenta ";
			aDezena[7] = "Setenta ";
			aDezena[8] = "Oitenta ";
			aDezena[9] = "Noventa ";

			aCentena[1] = "Cento ";
			aCentena[2] = "Duzentos ";
			aCentena[3] = "Trezentos ";
			aCentena[4] = "Quatrocentos ";
			aCentena[5] = "Quinhentos ";
			aCentena[6] = "Seiscentos ";
			aCentena[7] = "Setecentos ";
			aCentena[8] = "Oitocentos ";
			aCentena[9] = "Novecentos ";

			if (c.indexOf(".") != -1) {
				aGrupo[4] = c.substring(c.indexOf(".") + 1, c.length);
				aGrupo[4] = aGrupo[4].substring(0, 2);
				ct = c.substring(0, c.indexOf("."));
			} else {
				if (c.indexOf(",") != -1) {
					aGrupo[4] = c.substring(c.indexOf(",") + 1, c.length);
					aGrupo[4] = aGrupo[4].substring(0, 2);
					ct = c.substring(0, c.indexOf(","));
				} else {
					aGrupo[4] = "00";
					ct = c;
				}
				tt = "";
				for (f = 0; f < (10 - ct.length); f++) {
					tt += "0";
				}
				tt += ct;
			}

			aGrupo[1] = tt.substring(1, 4);
			aGrupo[2] = tt.substring(4, 7);
			aGrupo[3] = tt.substring(7, 10);
			aGrupo[4] = "0" + aGrupo[4];

			for (f = 1; f < 5; f++) {
				cParte = aGrupo[f];
				if (parseFloat(cParte) < 10) {
					nTamanho = 1;
				} else {
					if (parseFloat(cParte) < 100) {
						nTamanho = 2;
					} else {
						if (parseFloat(cParte) < 1000) {
							nTamanho = 3;
						} else {
							nTamanho = 0;
						}
					}
				}
				aTexto[f] = "";
				if (nTamanho == 3) {
					if (cParte.substring(1, 3) != "00") {
						aTexto[f] += aCentena[cParte.substring(0, 1)] + "e ";
						nTamanho = 2;
					} else {
						if (cParte.substring(0, 1) == "1") {
							aTexto[f] += "Cem ";
						} else {
							aTexto[f] += aCentena[cParte.substring(0, 1)];
						}
					}
				}
				if (nTamanho == 2) {
					if (parseFloat(cParte.substring(1, 3)) < 10) {
						aTexto[f] += aUnid[cParte.substring(2, 3)];
					} else {
						if (parseFloat(cParte.substring(1, 3)) < 20) {
							aTexto[f] += aUnid[cParte.substring(1, 3)];
						} else {
							aTexto[f] += aDezena[cParte.substring(1, 2)];
							if (cParte.substring(2, 3) != "0") {
								aTexto[f] += "e ";
								nTamanho = 1;
							}
						}
					}
				} // fim do for

				if (nTamanho == 1) {
					aTexto[f] += aUnid[cParte.substring(2, 3)];
				}
			}
			if (parseFloat(aGrupo[1] + aGrupo[2] + aGrupo[3]) == 0
					&& parseFloat(aGrupo[4]) != 0) {
				cFinal = aTexto[4];
				if (parseFloat(aGrupo[4]) == 1) {
					cFinal += "Centavo";
				} else {
					cFinal += "Centavos";
				}
			} else {
				if (parseFloat(aGrupo[1]) != 0) {
					cFinal = aTexto[1];
					if (parseFloat(aGrupo[1]) > 1) {
						cFinal += "Milhões ";
					} else {
						cFinal += "Milhão ";
					}
					if (parseFloat(aGrupo[2] + aGrupo[3]) == 0) {
						cFinal += "de ";
					} else {
						cFinal += "e ";
					}
				} else {
					cFinal = "";
				}
				if (parseFloat(aGrupo[2]) != 0) {
					cFinal += aTexto[2] + "Mil ";
					if (parseFloat(aGrupo[3]) != 0) {
						cFinal += "e ";
					}
				}
				if (parseFloat(aGrupo[3]) != 0) {
					cFinal += aTexto[3];
				}
			}
		}
		document.getElementById(label + "numeroextenso").innerHTML = cFinal;
		document.getElementById(label + "numextenso").value = cFinal;
	}
}

//*-------------------------------------------------------------*//
/*
 * Funcao replaceAll Data da Criacao : 02/03/2010 chamada : s = replaceAll(s,
 * "a", "b");
 */
//*-------------------------------------------------------------*//
function replaceAll(string, token, newtoken) {
	while (string.indexOf(token) != -1) {
		string = string.replace(token, newtoken);
	}
	return string;
}

//*-------------------------------------------------------------*//
/*
 * Funcao que substitui caracteres por suas entities correspondentes Data da
 * Criacao : 02/03/2010 chamada : s = toEntities(s);
 */
//*-------------------------------------------------------------*//
function toEntities(s) {
	s = replaceAll(s, " ", "&nbsp;");
	s = replaceAll(s, String.fromCharCode(160), "&nbsp;");
	s = replaceAll(s, "º", "&ordm;");
	s = replaceAll(s, "ª", "&ordf;");
	s = replaceAll(s, "À", "&Agrave;");
	s = replaceAll(s, "Á", "&Aacute;");
	s = replaceAll(s, "Â", "&Acirc;");
	s = replaceAll(s, "Ã", "&Atilde;");
	s = replaceAll(s, "Ç", "&Ccedil;");
	s = replaceAll(s, "É", "&Eacute;");
	s = replaceAll(s, "Í", "&Iacute;");
	s = replaceAll(s, "Ñ", "&Ntilde;");
	s = replaceAll(s, "Ó", "&Oacute;");
	s = replaceAll(s, "Ô", "&Ocirc;");
	s = replaceAll(s, "Õ", "&Otilde;");
	s = replaceAll(s, "Ú", "&Uacute;");
	s = replaceAll(s, "à", "&agrave;");
	s = replaceAll(s, "á", "&aacute;");
	s = replaceAll(s, "â", "&acirc;");
	s = replaceAll(s, "ã", "&atilde;");
	s = replaceAll(s, "ç", "&ccedil;");
	s = replaceAll(s, "é", "&eacute;");
	s = replaceAll(s, "ê", "&ecirc;");
	s = replaceAll(s, "í", "&iacute;");
	s = replaceAll(s, "ñ", "&ntilde;");
	s = replaceAll(s, "ó", "&oacute;");
	s = replaceAll(s, "ô", "&ocirc;");
	s = replaceAll(s, "õ", "&otilde;");
	s = replaceAll(s, "ú", "&uacute;");
	return s;
}

function setDisabled(el, val) {
	if (document.layers)
		return;
	if (document.getElementById)
		elm = document.getElementById(el).getElementsByTagName("INPUT");
	if (document.all)
		elm = document.getElementById(el).getElementsByTagName("INPUT");
	for (a = 0; a < elm.length; a++) {
		elm[a].disabled = val;
	}
}
/** =======================================================================
 *
 *    Estrutura para submissão Ajax e tratamento de retorno RPC  - LAGS - 08/10/2010
 *
 *   ======================================================================
 *     //Exemplo de solicitação síncrona :
 *
 * var t_smrRequisicao = new SimpleMethodRequestRPCGet ();
 * var t_strUrl = '/siga/servico/gravar!aGravar.action';
 * t_smrRequisicao.setUrl(t_strUrl);
 * t_smrRequisicao.addUrlParam("idPessoaConfiguracao", p_strIdPessoa);
 * t_smrRequisicao.addUrlParam("idServicoConfiguracao", p_strIdServico);
 * t_smrRequisicao.addUrlParam("idSituacaoConfiguracao", t_strIdSituacao);
 * try {
 *     var t_objResult = t_smrRequisicao.submeterSincrono();
 *     // os atributos  de retorno  SimpleMethod...
 *     // têm os parâmtros de retorno inseridos no objeto de retorno
 *     alert( "o valor de xxx é: " t_objResult.xxx); 
 * } catch (e) {
 *     // a exceção também ocorre em caso de erros vindos do servidor.
 *     alert(e.description);
 * }	
 */
/**
 * Função (não classe) que decodifica (transforma em conteúdo Javascript)
 * o node dom que é o primeiro nó <value> em um documento no formato RPC
 */
function decodeMainValueRPC(p_nodValueRaiz) {
	/** seleciona do tipo do value RPC e direciona para decodificacao
	 *   correta do noh filho
	 */
	var decodeNodeValue = function(p_nodValor) {
		var t_nodTipo = p_nodValor.firstChild;
		if (t_nodTipo) {
			var t_strNomeNodTipo = t_nodTipo.nodeName;
			if (t_strNomeNodTipo == "struct") {
				return decodeNodeStruct(t_nodTipo );
			} else if (t_strNomeNodTipo == "array") {
				return decodeNodeArray(t_nodTipo);
			} else if (t_strNomeNodTipo == "string") {
				return decodeNodeString(t_nodTipo );
			} else {
				return null;
			}
		}
		return null;
	};
	/** decodifica um filho de value RPC 
	 *   do tipo String (retorna uma String com o conteuhdo ou null)
	 */
	var decodeNodeString = function(p_nodTipo ) {
		if (p_nodTipo){
			if (p_nodTipo.nodeName == "string") {
				var t_nodTxt = p_nodTipo.firstChild;
				if (t_nodTxt) {
					return t_nodTxt.nodeValue;
				}
			}
		}
		return null;
	};
	/** decodifica um filho de value RPC 
	 *   do tipo Array (retorna um Array com o conteuhdo ou null)
	 */
	var decodeNodeArray = function(p_nodTipo ){
		if (p_nodTipo){
			if (p_nodTipo.nodeName == "array") {
				var t_nodData = p_nodTipo.firstChild;
				if (t_nodData) {
					var t_strNomeNodData = t_nodData.nodeName;
					if (t_strNomeNodData == "data") {
						var t_arr1DataChild = t_nodData.childNodes;
						var t_arr1Resultado = new Array();
						for (var t_intConta = 0; t_intConta < t_arr1DataChild.length; t_intConta++) {
							var t_nodValor = t_arr1DataChild[t_intConta];
							if (t_nodValor.nodeName == "value") {
								t_arr1Resultado.push(decodeNodeValue(t_nodValor));
							}
						}
						return t_arr1Resultado;
					}
				}
			}
		}
		return null;
	};
	/** decodifica um filho de value RPC 
	 *   do tipo Struct (retorna um Objeto com os atrubutos 
	 *   de nomes/conteudos iguai ao node/value ou null)
	 */
	var decodeNodeStruct = function(p_nodTipo) {
		if (p_nodTipo) {
			if (p_nodTipo.nodeName == "struct") {
				var t_arr1TipoChild = p_nodTipo.childNodes;
				var t_objStruct = new Object();
				for (var t_intContaMember = 0; t_intContaMember < t_arr1TipoChild.length; t_intContaMember++) {
					var t_nodMember = t_arr1TipoChild[t_intContaMember];
					var t_strMemberName = null;
					var t_objMemberValue = null;
					if (t_nodMember.nodeName == "member") {
						var t_arr1Atribs =  t_nodMember.childNodes;
						for (var t_intContaAttrib = 0; t_intContaAttrib < t_arr1Atribs.length; t_intContaAttrib++) {
							t_nodAttrib = t_arr1Atribs[t_intContaAttrib ];
							if (t_nodAttrib.nodeName == "name") {
								var t_nodTxt = t_nodAttrib.firstChild;
								if (t_nodTxt) {
									t_strMemberName = t_nodTxt.nodeValue;
								}
							} else if (t_nodAttrib.nodeName == "value") {
								t_objMemberValue = decodeNodeValue(t_nodAttrib);
							} else {
								// somente deve haver os nós do tipo <name> e <value>
							}
						}
					}
					if (t_strMemberName && t_objMemberValue ) {
						t_objStruct[t_strMemberName] =  t_objMemberValue;
					}
				}
				return t_objStruct ;
			}
		}
		return null;
	};
	// Rotina principal da função
	if (!p_nodValueRaiz) {
		throw new Error( "decodeMainValueRPC(p_nodValueRaiz)>>p_nodValueRaiz inexistente ou vazio!");
	}
	if (p_nodValueRaiz.nodeName != "value") {
		throw new Error( "decodeMainValueRPC(p_nodValueRaiz)>>p_nodValueRaiz não é nó de nome 'value'!");
	}
	return decodeNodeValue(p_nodValueRaiz);
}
/**
 * Função (não classe) que interpreta o DOM
 * recebido com parâmetro e identifica e
 * redireciona para a decodificação específica 
 * (Simple,Table ou Fault)
 */
function decodeMethodResponseRPC( p_domDocument) {
	var t2_funDecodeMainValueRPC = decodeMainValueRPC; 
	if (!p_domDocument) { 
		throw new Error("decodeMethodResponseRPC( p_domDocument)>> p_domDocument sem conteúdo"  );
	}
	if (p_domDocument.getElementsByTagName("methodResponse").length > 0) {
		var t_arr1NodFaults = p_domDocument.getElementsByTagName("fault");
		if (t_arr1NodFaults.length > 0) {
			// Fault
			var t_nodFault = t_arr1NodFaults[0];
			if (t_nodFault && t_nodFault.nodeName == "fault") { 
				var t_nodValueFault = t_nodFault.firstChild;
				if (t_nodValueFault && t_nodValueFault.nodeName == "value" ) {
					var t_objErro = t2_funDecodeMainValueRPC(t_nodValueFault);
					throw new Error("Retorno do servidor: (" + t_objErro.faultCode + ") " + t_objErro.faultString );
				}
			}
		} else {
			var t_arr1NodParams = p_domDocument.getElementsByTagName("params");
			if (t_arr1NodParams.length > 0) {
				var t_nodParams = t_arr1NodParams[0];
				if (t_nodParams) {
					var t_nodParam = t_nodParams.firstChild;
					if (t_nodParam && t_nodParam.nodeName == "param") {
						var t_nodValue = t_nodParam.firstChild;
						if (t_nodValue && t_nodValue.nodeName == "value") {
							// Tanto o SimpleMethodResponseRPC quanto o TableMethodResponseRPC têm a mesma estrutura inicial
							return t2_funDecodeMainValueRPC(t_nodValue);
						} 
					}
				}
			}
		}
	}
	return null;
}
/**
 * Classe que submete uma requisição RPC (Ajax)
 * com o método GET
 * O retorno é no formato Simples (Pode ser simples ou tabela)
 * no caso de erro é retornado no formato 'RPC Fault'
 */
function SimpleMethodRequestRPCGet () {
	var t2_funDecodeMethodResponseRPC = decodeMethodResponseRPC;
	var _params = new Array();
	var _url = new String();
	this.addUrlParam = function(p_strNome, p_strValor) {
		var t_objParam = new Object();
		t_objParam.nome = p_strNome;
		t_objParam.valor = p_strValor;
		_params.push(t_objParam) ;
	};
	this.setUrl = function (p_strUrl) {
		_url = p_strUrl;
	};
	this.submeterSincrono = function () {
		if (window.XMLHttpRequest)	{
			// IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		} else 	{
			// IE6, IE5
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (!_url) {
			throw new Error("Url inexistente !");
		}
		var t_strParams = new String();
		for  ( var t_intContaPar = 0 ; t_intContaPar < _params.length ; t_intContaPar++ ) {
			var t_objParam = _params[t_intContaPar];
			if (t_intContaPar == 0 ) {
				t_strParams += "?" + t_objParam.nome; 
			} else {
				t_strParams += "&" + t_objParam.nome;
			}
			t_strParams += "=" + t_objParam.valor;
		}
		xmlhttp.open("GET",_url + t_strParams,false);
		xmlhttp.send(null);
		//var t_domResp = xmlhttp.responseXML;
		var t_domResp = null;
		if (!t_domResp) 
			t_domResp = (new DOMParser()).parseFromString(xmlhttp.responseText, 'text/xml');     
		if (t_domResp) {
			return t2_funDecodeMethodResponseRPC(t_domResp);
		} else {
			throw new Error("Retorno da submissão vazio.");
		}
	};
}
SimpleMethodRequestRPCGet.prototype = new Object() ;
SimpleMethodRequestRPCGet.prototype.constructor = SimpleMethodRequestRPCGet;
/* =======================================================================
 *    Fim da strutura para submissão Ajax e tratamento de retorno RPC  
 *  ======================================================================
 */



/* ajax.js */
/**
SAL - Simple Ajax Lib. 23-Sep-2005
by Nigel Liefrink
Email: leafrink@hotmail.com
*/

var debug = false;
var active = 0;

/**
<summary>
Browser Compatability function.
Returns the correct XMLHttpRequest 
  depending on the current browser.
</summary>
*/
function GetXmlHttp() {
  var xmlhttp = false;
  if (window.XMLHttpRequest)
  {
    xmlhttp = new XMLHttpRequest()
  }
  else if (window.ActiveXObject)// code for IE
  {
    try
    {
      xmlhttp = new ActiveXObject("Msxml2.XMLHTTP")
    } catch (e) {
      try
      {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP")
      } catch (E) {
        xmlhttp=false
      }
    }
  }
  return xmlhttp;
}

function mostraCarregando(){
	try {
		var style = document.getElementById("carregando").style;
		var posiciona = function(){
			style.top = document.body.scrollTop;
		};
		posiciona();
		window.onscroll = posiciona;
		style.display = 'block';
	} catch (err) {
	}
}

function ocultaCarregando(){
	try {
		document.getElementById("carregando").style.display = 'none';
		window.onscroll = null;
	} catch (err) {
	}
}

/**
<summary>
Gets the response stream from the passed url, and then calls
   the callbackFuntion passing the response and the div_ids.
</summary>
<param name="url">The url to make the request
            to get the response data.</param>
<param name="callbackFunction">The function to call after 
the response has been recieved.
The response <b>must</b> always 
be the first argument to the function.</param>
<param name="params"> (optional) Any other parameters 
you want to pass to the functions.
(Note: only constants/strings/globals can be passed
       as params, most variables will be out of scope.)
</param>
<example>
    <code>
PassAjaxResponseToFunction('?getsomehtml=1', 
              'FunctionToHandleTheResponse', 
              "\'div1\',\'div2\',\'div3\'');

function FunctionToHandleTheResponse(response, d1, d2, d3){
    var data = response.split(';');
    document.getElementById(d1).innerHTML = data[0];
    document.getElementById(d2).innerHTML = data[1];
    document.getElementById(d3).innerHTML = data[2];
}
    </code>
</example>
*/
function PassAjaxResponseToFunction(url, callbackFunction, params, omitirCarregando, postParams)
{
  if (!omitirCarregando)
  	mostraCarregando();
  var xmlhttp = new GetXmlHttp();
  //now we got the XmlHttpRequest object, send the request.
  if (xmlhttp)
  {
    xmlhttp.onreadystatechange = 
            function ()
            {
              if (xmlhttp && xmlhttp.readyState==4)
              {//we got something back..
                active--;
                if (xmlhttp.status==200)
                {
                  var response = xmlhttp.responseText;
                  var functionToCall = callbackFunction + 
                                 '(response,'+params+')';
                  if(debug)
                  {
                    alert(response);
                    alert(functionToCall);
                  }
                  eval(functionToCall);
                } else if(debug){
                  document.write(xmlhttp.responseText);
                }
                ocultaCarregando();
              }
            }
    var method;
    if (postParams == null)
  		method = "GET";
  	else method = "POST";
    xmlhttp.open(method,url,true);
    if (method == "POST"){
    	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		//xmlhttp.setRequestHeader("Content-length", postParams.length);
		//xmlhttp.setRequestHeader("Connection", "close");
		xmlhttp.send(postParams);
    } else xmlhttp.send(null);
  }
  active++;
}


/**
<summary>
Sets the innerHTML property of obj_id with the response from the passed url.
</summary>
<param name="url">The url to make the request 
to get the response data.</param>
<param name="obj_id">The object or the id of 
the object to set the innerHTML for.</param>
*/
function SetInnerHTMLFromAjaxResponse(url, obj_id)
{
  mostraCarregando();
  active++;
  
  var jqxhr = $.get(url, function(s) {
	if(typeof obj_id == 'object') {
    	obj_id.innerHTML = s;
	} else {
    	document.getElementById(obj_id).innerHTML = s;
	}
  
	// Caso seja necessario acrescentar algum script na pagina, ficou convencionado que
	// o script deverá ser marcado com <script type="text/javascript" > e < /script>
	if (s.indexOf('<script type="text/javascript">') != -1) {
		var j = 0;
		var len = 0;
		do {
			j = s.indexOf('<script type="text/javascript">', j);
			if (j<=0)
				break;
			j = j + 31;
			len = s.indexOf('</script>', j);
			eval(s.substring(j, len));
		} while (true)
	}
  })
  .always(function() {
		ocultaCarregando();
  });
  
  return;

  
  var xmlhttp = new GetXmlHttp();
  //now we got the XmlHttpRequest object, send the request.
  if (xmlhttp)
  {
    xmlhttp.onreadystatechange = 
            function ()
            {
              if (xmlhttp && xmlhttp.readyState==4)
              {//we got something back..
                active--;
                if (xmlhttp.status==200)
                {
                	var s = xmlhttp.responseText;
                	
                	var splitted = s.split('###');
                	if (splitted[0] == 'TRYAGAIN'){
                		//alert('SetInnerHTMLFromAjaxResponse(\''+url+'\',\''+obj_id.id+'\')');
                		var timer=setTimeout('SetInnerHTMLFromAjaxResponse(\''+url+'\',\''+obj_id.id+'\')',1500);
                	} else {
                    	if(debug) {
	                    	alert(s);
                  		}
                  		if(typeof obj_id == 'object') {
	                    	obj_id.innerHTML = s;
                  		} else {
	                    	document.getElementById(obj_id).innerHTML = s;
                  		}
	                  
						// Caso seja necessario acrescentar algum script na pagina, ficou convencionado que
		            	// o script deverá ser marcado com <script type="text/javascript" > e < /script>
	               		if (s.indexOf('<script type="text/javascript">') != -1) {
							var j = 0;
							var len = 0;
							do {
								j = s.indexOf('<script type="text/javascript">', j);
								if (j<=0)
									break;
								j = j + 31;
								len = s.indexOf('</script>', j);
								eval(s.substring(j, len));
							} while (true)
						}
						ocultaCarregando();
					}
                } else if(debug){
                  document.Write(xmlhttp.responseText);
                }
              }
            }
    xmlhttp.open("GET",url,true);
    xmlhttp.send(null);
    active++;
  }
}

function collect(a,f){var n=[];for(var i=0;i<a.length;i++){var v=f(a[i]);if(v!=null)n.push(v)}return n};

ajax={};
ajax.x=function(){try{return new ActiveXObject('Msxml2.XMLHTTP')}catch(e){try{return new ActiveXObject('Microsoft.XMLHTTP')}catch(e){return new XMLHttpRequest()}}};
ajax.serialize=function(f){var g=function(n){return f.getElementsByTagName(n)};

var nv=function(e){
if(e.name)
	return encodeURIComponent(e.name)+'='+encodeURIComponent(e.value);
else
	return ''};
	
var nvs=function(e){
if(e.name) {
	if (e.options[e.selectedIndex].value != "")
		return encodeURIComponent(e.name)+'='+encodeURIComponent(e.options[e.selectedIndex].value);
	else
		return encodeURIComponent(e.name)+'='+encodeURIComponent(e.options[e.selectedIndex].text);
} else
	return ''};
	
var i=collect(g('input'),function(i){if((i.type!='radio'&&i.type!='checkbox')||i.checked)return nv(i)});
var s=collect(g('select'),nvs);
var t=collect(g('textarea'),nv);
return i.concat(s).concat(t).join('&');};
ajax.send=function(u,f,m,a){var x=ajax.x();x.open(m,u,true);x.onreadystatechange=function(){if(x.readyState==4)f(x.responseText)};if(m=='POST')x.setRequestHeader('Content-type','application/x-www-form-urlencoded');x.send(a)};
ajax.get=function(url,func){ajax.send(url,func,'GET')};
ajax.gets=function(url){var x=ajax.x();x.open('GET',url,false);x.send(null);return x.responseText};
ajax.post=function(url,func,args){ajax.send(url,func,'POST',args)};
ajax.update=function(url,elm){var e=document.getElementById(elm);var f=function(r){e.innerHTML=r};ajax.get(url,f)};
ajax.submit=function(url,elm,frm){var e=document.getElementById(elm);var f=function(r){e.innerHTML=r};ajax.post(url,f,ajax.serialize(frm))};


/**
<summary>
Sets the innerHTML property of obj_id with the response from the passed url.
</summary>
<param name="url">The url to make the request 
to get the response data.</param>
<param name="obj_id">The object or the id of 
the object to set the innerHTML for.</param>
*/
function MixWithNewPage(aElements, sPage, sIdObj) {
	var e;
	var depende;
//	var a = [document.getElementsByTagName('div'),document.getElementsByTagName('span')];
//	var d = document.getElementsByTagName('div');
//  for (var d in a) {
    for (e in aElements) {
    
	    if (e != null && e != 'length' && e != 'item' && e != 'namedItem') {
	    
//			if (window.XMLHttpRequest) {
				// Firefox
//				alert('e: ' + e + aElements[e]);
				e = aElements[e];
				try {
					depende = e.attributes.depende.nodeValue;
				} catch (ex) {
					depende = null;
				}
//			} else {
				// InternetExplorer
//				e = document.getElementById(e);
//				depende = e.depende;
//			}
			
	     	if(e != null && typeof depende == 'string') {
//				e = document.getElementById(e);
//alert('depende: ' + e.depende);
				if (depende.indexOf(';'+sIdObj+';') != -1) {
					var i = 0;
					i = sPage.indexOf('<!--ajax:'+e.id+'-->', i);
//alert('<!--ajax:'+e.id+'-->', i);
					if (i > 0) {
						var s = sPage.substring(i,sPage.indexOf('<!--/ajax:'+e.id+'-->', i));
						
						// Insere o HTML
						e.innerHTML = s;
//alert(s);							
		              	// Caso seja necessario acrescentar algum script na pagina, ficou convencionado que
		              	// o script deverá ser marcado com <script type="text/javascript" > e < /script>
		               	if (s.indexOf('<script type="text/javascript">') != -1) {
							var j = 0;
							var len = 0;
							do {
								j = s.indexOf('<script type="text/javascript">', j);
								if (j<=0)
									break;
								j = j + 31;
								len = s.indexOf('</script>', j);
//	alert(j);
//	alert(len);
//alert(s.substring(j, len));
								eval(s.substring(j, len));
							} while (true)
						}
					}
				}
			}
		}
	}					
}

function ReplaceInnerHTMLFromAjaxResponse(url, frm, obj_id, cont) {
	var res = new RespostaAjax();
	mostraCarregando();
	var xmlhttp = new GetXmlHttp();
	//now we got the XmlHttpRequest object, send the request.
	if (xmlhttp) {
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp && xmlhttp.readyState == 4) {//we got something back..
				active--;
				if (xmlhttp.status == 200) {
					if (debug) {
						alert(xmlhttp.responseText);
					}
					if (typeof obj_id == 'object') {
						obj_id.innerHTML = xmlhttp.responseText;
					} else {
						var r = xmlhttp.responseText;
						MixWithNewPage(document.getElementsByTagName('span'), r, obj_id);
						MixWithNewPage(document.getElementsByTagName('div'), r, obj_id);
					}
					res.invokeSuccess(xmlhttp.responseText);
					if (cont) 
						cont();
				} else if (debug) {
					document.Write(xmlhttp.responseText);
					res.invokeError(xmlhttp.responseText);
				}
				ocultaCarregando();
			}
		}
		//	alert(ajax.serialize(frm));
		if (frm == null) {
			xmlhttp.open("GET", url, true);
			xmlhttp.send(null);
		} else {
			xmlhttp.open("POST", url, true);
			xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
			xmlhttp.send(ajax.serialize(frm));
		}
		active++;
	}
	return res;
}

function IsRunningAjaxRequest() {
	//window.alert(document.getElementById("carregando").style.display != 'none');
	return document.getElementById("carregando").style.display != 'none';
}

function RespostaAjax() {
	var successCallback = errorCallback = null;
	
	this.success = function(successCallback0) {
		successCallback = successCallback0;
	}
	
	this.error = function(errorCallback0) {
		errorCallback = errorCallback0;
	}
	
	this.invokeSuccess = function(responseText) {
		if(successCallback) 
			successCallback(responseText);
	}
	
	this.invokeError = function(responseText) {
		if(errorCallback)
			errorCallback(responseText);
	}
}