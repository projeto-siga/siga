
	$(document).ready(function() {
		$(".crono").each(function(){
			rodaCrono($(this));
		});
	});
	
	function rodaCrono(j){
		var spanValor = j.find(".valor");
		var valor;
		if (spanValor.html())
			valor = parseInt(spanValor.html());
		else return;
		var sentido = j.hasClass("decorrido") ? 1 : -1;
		var resumido = j.hasClass("resumido");
		j.find(".descrValor").html((resumido && valor < 0 ? '-' : '') + toString(valor, resumido));
		if (valor >= 0){
			j.find(".label-cron").html('Restante: ');
	   		j.css('color', 'black');
		} else {
			j.find(".label-cron").html('Atraso: ')
			j.css('color', 'red');
		}
		if (j.hasClass("ligado")){
	   		setTimeout(function() {
	   			spanValor.html(valor + sentido);
		   		rodaCrono(j);
	   		}, 1000);
		}
	}

	function toString(tempo, resumido) {

		if (tempo < 0)
			tempo *= -1;
		
		var tempoStr = '', dias = 0, horas = 0, minutos = 0, segundos = 0;

		while (tempo >= 86400){
			dias++;
			tempo -= 86400;
		}
		while (tempo >= 3600){
			horas++;
			tempo -= 3600;
		}
		while (tempo >= 60){
			minutos++;
			tempo -= 60;
		}
		while (tempo >= 1){
			segundos++;
			tempo -= 1;
		}

		if (resumido){
			if (dias > 0)
				tempoStr += dias + 'd';
			if (horas > 0) 
				tempoStr += horas + 'h';
			if (minutos > 0)
				tempoStr += minutos + 'min';
			if (segundos > 0)
				tempoStr += segundos + 's';
		} else {
			if (dias > 0){
				tempoStr += dias + ' dia';
				tempoStr += plural(dias);
				tempoStr += virgula(tempoStr);
			} if (horas > 0){ 
				tempoStr += horas + ' hora';
				tempoStr += plural(horas);
				tempoStr += virgula(tempoStr);
			} if (minutos > 0){
				tempoStr += minutos + ' minuto';
				tempoStr += plural(minutos);
				tempoStr += virgula(tempoStr);
			} if (segundos > 0){
				tempoStr += segundos + ' segundo';
				tempoStr += plural(segundos);
				tempoStr += virgula(tempoStr);
			}
			tempoStr = substituirUltimaVirgula(tempoStr);
		}

		
		return tempoStr;
	}

	function virgula(str){
		return (str.length > 1) ? ', ' : '';
	}
	function plural(valor){
		return (valor > 1) ? 's' : '';
	}
	function substituirUltimaVirgula(str){
		var pos = str.lastIndexOf(',');
		if (pos > 1)
			str = str.substring(0,pos);
		pos = str.lastIndexOf(',');
		if (pos > 1)
			str = str.substring(0,pos)+' e'+str.substring(pos+1);
		return str;
	}