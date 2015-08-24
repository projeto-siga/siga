<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<style>
	.valor {
      display:none;
    }
</style>
<c:choose>
<c:when test="${solicitacao.estaSendoAtendidaPor(titular, lotaTitular) || exibirMenuAdministrar}">
	<div class="gt-sidebar">
		<div class="gt-sidebar-content">
			<h3>Acordos</h3>
			<c:forEach items="${solicitacao.etapas}" var="etapa">
				<p class="acordo"><b>${etapa.descricao}</b>
				<ul class="acordo">
					<li><b>In&iacute;cio:</b> ${etapa.inicioString}</li>
					<c:if test="${not empty etapa.fimPrevisto}">
			 			<li><b>Fim Previsto:</b> ${etapa.fimPrevistoString}</li>
					</c:if>
					<c:if test="${not empty etapa.fim}">
			 			<li><b>Fim:</b> ${etapa.fimString}</li>
					</c:if>
			 		<li><span class="crono decorrido ${etapa.ativo ? 'ligado' : 'desligado'}"><b>Decorrido:</b> <span class="descrValor"></span><span class="valor">${etapa.decorridoEmSegundos}</span></span></li>
					<c:set var="restante" value="${etapa.restanteEmSegundos}" />
					<c:if test="${not empty restante}">
			 			<li><span class="crono restante ${etapa.ativo ? 'ligado' : 'desligado'}"><b><span class="label"></span></b><span class="descrValor"></span><span class="valor">${restante}</span></span></li>
					</c:if>
					<c:if test="${not empty etapa.paramAcordo}">
						<c:set var="situacao" value="${etapa.situacaoAcordo}" />
						<c:choose>
							<c:when test="${situacao == 'NAO_CUMPRIDO'}">
								<c:set var="style" value="color: red" />
								<c:set var="descrSituacao" value="(${situacao.descrSituacaoAtributoAcordo})" />
							</c:when>
							<c:when test="${situacao == 'ALERTA'}">
								<c:set var="style" value="color: yellow" />
								<c:set var="descrSituacao" value="(${situacao.descrSituacaoAtributoAcordo})" />
							</c:when>
						</c:choose>
		    			<li style="${style}"><b>Acordo:</b> ${etapa.paramAcordo.descricao} ${descrSituacao}<span></span></li>
					</c:if>
				</ul>
				</p>
			</c:forEach>
		</div>
	</div>
</c:when>
<c:otherwise>
<div class="gt-sidebar">
	<c:set var="etapa" value="${solicitacao.etapaPrincipal.descricao}" />
	<div class="gt-sidebar-content cronometro ${etapa.ativo ? 'ligado' : 'desligado'}">
		<h3>
			<img src="/siga/css/famfamfam/icons/clock.png" width="15px;"
				style="vertical-align: bottom;">&nbsp;${etapa.descricao}
		</h3>
		<c:if test="${not empty etapa.inicio}">
		    <p><b>In&iacute;cio: </b>${etapa.inicioString}</p>
		</c:if>
		<c:choose>
			<c:when test="${not empty etapa.fim}">
				<p>Fim: ${etapa.fimString}</p>
			</c:when>
			<c:when test="${not empty etapa.fimPrevisto}">
				<p>Previs&atilde;o: ${etapa.fimPrevistoString}
			</c:when>
		</c:choose>
	</div>
</div>
</c:otherwise>
</c:choose>

<script type="text/javascript">

	$(document).ready(function() {
		$(".crono").each(function(){
			rodaCrono($(this));
		});
	});
	
	function rodaCrono(j){
		var spanValor = j.find(".valor");
		var valor = parseInt(spanValor.html());
		var sentido = j.hasClass("decorrido") ? 1 : -1;
		j.find(".descrValor").html(toString(valor));
		if (valor > 0){
			j.find(".label").html('Restante: ');
	   		j.css('color', 'black');
		} else {
			j.find(".label").html('Atraso: ')
			j.css('color', 'red');
		}
		if (j.hasClass("ligado")){
	   		setTimeout(function() {
	   			spanValor.html(valor + sentido);
		   		rodaCrono(j);
	   		}, 1000);
		}
	}

	function toString(tempo) {

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
