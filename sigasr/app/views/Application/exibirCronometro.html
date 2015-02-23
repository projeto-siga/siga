#{set cron:solicitacao.cronometro /}


<script type="text/javascript">

	$(document).ready(function() {
		#{if cron.decorrido != null}
		atualizaDecorrido(${cron.decorrido});
		#{/if}

		#{if cron.restante != null}	
		atualizaRestante(${cron.restante});
		#{/if}
	});
	
	function atualizaDecorrido(tempo){
		$('#valorDecorrido').html(toString(tempo));
		#{if cron.ligado}
	   	setTimeout(function() {
	   		atualizaDecorrido(tempo + 1000);
	   	}, 1000);
	   	#{/if}
	}
	
	function atualizaRestante(tempo) { 
	   	$('#valorRestante').html(toString(tempo));

		if (tempo > 0){
	   		$('#descrRestante').html('Restante: ');
		} else {
			$('#descrRestante').html('Atraso: ').css('color', 'red');
			$('#valorRestante').css('color', 'red');
		}
	   	
		#{if cron.ligado}
	   	setTimeout(function() {
	   		atualizaRestante(tempo - 1000);
	   	}, 1000);
	   	#{/if}
	}

	function toString(tempo) {

		if (tempo < 0)
			tempo *= -1;
		
		var tempoStr = '', dias = 0, horas = 0, minutos = 0, segundos = 0;

		while (tempo >= 86400000){
			dias++;
			tempo -= 86400000;
		}
		while (tempo >= 3600000){
			horas++;
			tempo -= 3600000;
		}
		while (tempo >= 60000){
			minutos++;
			tempo -= 60000;
		}
		while (tempo >= 1000){
			segundos++;
			tempo -= 1000;
		}

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
</script>

<div class="gt-sidebar">
	<div class="gt-sidebar-content">
		<h3>
			<img src="/siga/css/famfamfam/icons/clock.png" width="15px;"
				style="vertical-align: bottom;">&nbsp;${cron.descricao}
		</h3>
		#{if cron.inicio}
		<p><b>In&iacute;cio: </b>${cron.inicioString}</p>
		#{/if}
		#{if cron.fim}
		<p><b>Fim: </b>${cron.fimString}</p>
		#{/if}
		#{if cron.decorrido != null}
		<p><span style="font-weight: bold; color: black;" id="descrDecorrido">Decorrido:&nbsp;</span><span id="valorDecorrido" style="color: black;" ></span></p>
		#{/if}
		#{if cron.restante != null}
		<p><span style="font-weight: bold; color: black;" id="descrRestante">Restante:&nbsp;</span><span id="valorRestante" style="color: black;" ></span></p>
		#{/if}
	</div>
</div>
