<c:set var="cron" value="${solicitacao.cronometro}" />


<script type="text/javascript">

	$(document).ready(function() {
		atualizaRelogio(${cron.remanescente});
	})
	
	function atualizaRelogio(tempo) { 
	   	$('#valorRemanescente').html(intervalo(tempo));
	   
	   	if (tempo >=0){
	   		$('#descrRemanescente').html('Restante: ').css('color', 'black');
	   		$('#valorRemanescente').css('color', 'black');
		} else {
			$('#descrRemanescente').html('Atraso: ').css('color', 'red');
			$('#valorRemanescente').css('color', 'red');
		}
	   	
		<c:if test="${cron.ligado}">
	   	setTimeout(function() {
	   		atualizaRelogio(tempo - 1000);
	   	}, 1000);
	   	</c:if>
	}; 

	function intervalo(tempo) {

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
		<c:if test="${cron.inicio}">
		<p><b>In&iacute;cio: </b>${cron.inicio}</p>
		</c:if>
		<c:if test="${cron.fim}">
		<p><b>Fim #{if cron.ligado}previsto#{/if}: </b>${cron.fim}</p>
		</c:if>
		<c:if test="${cron.remanescente}">
		<p><span style="font-weight: bold" id="descrRemanescente"></span><span id="valorRemanescente"></span></p>
		</c:if>
	</div>
</div>
