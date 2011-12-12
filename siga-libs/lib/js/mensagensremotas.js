/*-----------------------------------------------------------------------------
 *     MensagensRemotas.js - Função para exibição de mensagens vindas do servidor
 *     Luiz Antonio Garcia Simões (LAGS) - 18/03/2010
 *
 *	Uso :     	1 -  incorporar script na página  :   
 *				<script type="text/javascript" src="MensagensRemotas.js">
 *			2 - chamar na carga inicial da página :
 *					<body onload="javascript: exibirMensagensRemotas('mensagem_remota.xml','id-do-tag-a-exibir','style-exibir')">
 *				ou na carga de um scrip : 
 *					<script> exibirMensagensRemotas('mensagem_remota.xml','id-do-tag-a-exibir','style-esconder','style-exibir');</script>
 *				onde : a)  mensagem_remota.xml	- é a URL onde está o <arquivo xml> que contém as mensagens.
 *					   b)  id-do-tag-a-exibir 		- é o nome do atributo id do TAG onde as mensagens serão exibidas.
 *					   c)  style-exibir 	  		- string a ser atribuída ao style quando a mensagem  estiver sendo exibida.
 *				
 *
 *	Leiaute do <arquivo xml> : 
 *	-------------------------------
 * <?xml version='1.0' encoding='UTF-8'?>
 * <!-- Mensagens vindas do servidor - Exemplo  -->
 * <mensagens>
 *	<!-- Primeira mensagem  -->
 *	<mensagem>
 *		<serial>123</serial> 				<!-- Numero de série da mensagem  -->
 *		<inicio>2010,2,12,19,0,0</inicio>	<!-- data e hora de início da mensagem Atenção : mês - janeiro começa em zero  -->
 *		<fim>2010,2,16,19,0,0</fim>			<!-- data e hora de fim da mensagem Atenção : mês - janeiro começa em zero  -->
 *		<texto>O sistema SIGA XXXXXX ficará fora do ar a partir das 19:00h.</texto> <!-- Texto da mensagem  -->
 *	</mensagem>
 *	<!-- Segunda mensagem  -->
 *	<mensagem>
 *		<serial>234</serial>				<!-- Numero de série da mensagem  -->
 *		<inicio>2010,2,12,19,0,0</inicio>	<!-- data e hora de início da mensagem Atenção : mês - janeiro começa em zero  -->
 *		<fim>2010,2,16,20,0,0</fim>			<!-- data e hora de fim da mensagem Atenção : mês - janeiro começa em zero  -->
 *		<texto>Manutenção programada em duas horas !</texto> <!-- Texto da mensagem  -->
 *	</mensagem>
 *</mensagens>
 *
 *
 *-----------------------------------------------------------------------------
 */
var intQuantasVerMensagens = 0;
var TOTAL_QUANTAS_VER_MENSAGENS = 2880; //24h ;    máximo  2147483647 (maior integer)
var INTERVALO_CICLO_MENSAGENS = 30000; // 60s
function exibirMensagensRemotas(p_strURLMensagensRemotas, p_strIdPosicao, p_strStyleExibir ) {
	carregarMensagensRemotas(p_strURLMensagensRemotas, p_strIdPosicao, p_strStyleExibir ) ;
}
function carregarMensagensRemotas(p_strURLMensagensRemotas, p_strIdPosicao ,  p_strStyleExibir  ) {
	try {
		intQuantasVerMensagens++;
		if (intQuantasVerMensagens < TOTAL_QUANTAS_VER_MENSAGENS ) {
			var arr1TextosMensagensAtuais = new Array();
			var intContaAtuais = 0 ;
			var domResposta = carregarXMLMensagensRemotas(p_strURLMensagensRemotas);
			if (domResposta) {
				var arr1Mensagens = domResposta.getElementsByTagName("mensagem");
				for (var intContaMsg = 0; intContaMsg < arr1Mensagens.length; intContaMsg++) {
					var intNumSerie = obterNumSerie(domResposta,intContaMsg);
					if (intNumSerie) {
						var dthDataHoraInicio = obterDataHoraInicio(domResposta,intContaMsg);
						if (dthDataHoraInicio) {
							var dthDataHoraFim = obterDataHoraFim(domResposta,intContaMsg);
							if (dthDataHoraFim) {
								var dthAgora = new Date();
								if ( (dthAgora.getTime() > dthDataHoraInicio.getTime()) && (dthAgora.getTime() < dthDataHoraFim.getTime()) ) {
									arr1TextosMensagensAtuais[intContaAtuais] = obterTextoMensagem(domResposta,intContaMsg);
									intContaAtuais++ ;
								}
							}
						}
					} 
				}
			}
			var intQuantasAtuais = arr1TextosMensagensAtuais.length;
			var intIntervalo = Math.floor(INTERVALO_CICLO_MENSAGENS / intQuantasAtuais )  ;
			for (var intContaExib = 0; intContaExib < intQuantasAtuais; intContaExib++ ) {
				var intQuandoExibir = Math.floor((intContaExib + 1)  * intIntervalo)  ;
				exibirMensagensRemotasRotator(arr1TextosMensagensAtuais[intContaExib], 
												p_strIdPosicao,
												intQuandoExibir, 
												intIntervalo , 
												p_strStyleExibir);
			}
			recarregarMensagensRemotas( p_strURLMensagensRemotas, 
										p_strIdPosicao, 
										p_strStyleExibir);
			return;
		} else {
			return;
		}
	} catch (e) {
		return ;
	}
}
function exibirMensagensRemotasRotator(	p_strTextoMensagem, 
										p_strIdPosicao, 
										p_intQuandoExibir, 
										p_intIntervalo,
										p_strStyleExibir) {
	var intIdNumber=Math.floor(Math.random()*100001);
	var strIdTxt = "msgremtxt-" + intIdNumber;
	setTimeout("exibirMensagemRemotaRotatorTimer('" 
				+ p_strIdPosicao 
				+ "','" 
				+ p_strTextoMensagem 
				+ "','" 
				+ strIdTxt
				+ "','" 
				+ p_strStyleExibir
				+ "')" , p_intQuandoExibir);
	setTimeout("excluirMensagemRemotaRotatorTimer('" 
				+ p_strIdPosicao 
				+  "','" 
				+ strIdTxt 
				+ "')" , p_intQuandoExibir + Math.floor( p_intIntervalo / 2 ));
}
function obterPosicaoTopo() {
	var intTop = document.body.scrollTop ;
	if (intTop) {
		return intTop ;
	} else {
		intTop = window.pageYOffset ;
	}
	if (intTop) {
		return intTop ;
	} else {
	     intTop =  (document.body.parentElement
	           ? document.body.parentElement.scrollTop
	           : 0
	           );
	}
	 return intTop;   

}
function exibirMensagemRemotaRotatorTimer(	p_strIdPosicao,
											p_strTextoTempMensagem, 
											p_strIdTxt,
											p_strStyleExibir) {
	var nodPosicao = document.getElementById(p_strIdPosicao);
	if (nodPosicao) {
		var nodPaiTxt = document.createElement('span');
		nodPaiTxt.setAttribute("id", p_strIdTxt );
		nodPaiTxt.style.cssText =  p_strStyleExibir ;
		if (! nodPaiTxt.style.top) {
			nodPaiTxt.style.top = obterPosicaoTopo() + "px";
		}
		
		nodPosicao.appendChild(nodPaiTxt);
		nodText = document.createTextNode(p_strTextoTempMensagem);
		nodPaiTxt.appendChild(nodText);
		var oSelects = document.getElementsByTagName("select");
   		for (var i=0;i<oSelects.length;i++) {
      		var strDisplay = oSelects[i].style.display;
      		oSelects[i].style.display = 'none';
      		oSelects[i].style.display = strDisplay;
        }
		nodPaiTxt.onclick =	function () {
			var t2_nodPaiTxt = nodPaiTxt;
			var t2_nodPosicao = nodPosicao;
			t2_nodPosicao.removeChild(t2_nodPaiTxt);
		};
	}

	
}
function excluirMensagemRemotaRotatorTimer(	p_strIdPosicao,
											p_strIdTxt) {
	var nodPosicao = document.getElementById(p_strIdPosicao);
	if (nodPosicao) {
		var nodText =  document.getElementById(p_strIdTxt);
		if (nodText) {
		   nodPosicao.removeChild(nodText);
		}
	}
}
function obterNumSerie(p_domResposta,p_intContaMsg) {
	var intNumSerie = null;
	var nodNumSerieLida = p_domResposta.getElementsByTagName("serial")[p_intContaMsg];
	if (nodNumSerieLida ) {
		var txnNumSerieLida = nodNumSerieLida.firstChild;
		if (txnNumSerieLida) {
			var strNumSerieLida = txnNumSerieLida.nodeValue ;
			intNumSerie = parseInt(strNumSerieLida);
		}
	}
	return intNumSerie;
}
function obterDataHoraInicio(p_domResposta,p_intContaMsg) {
	var dthDataHoraInicio = null;
	var nodDataHoraInicio = p_domResposta.getElementsByTagName("inicio")[p_intContaMsg];
	if (nodDataHoraInicio ) {
		var txnDataHoraInicio = nodDataHoraInicio.firstChild;
		if (txnDataHoraInicio) {
			var strDataHoraInicio = txnDataHoraInicio.nodeValue ;
			dthDataHoraInicio = eval("new Date(" + strDataHoraInicio + ");");
		}
	}
	return dthDataHoraInicio;
}
function obterDataHoraFim(p_domResposta,p_intContaMsg) {
	var dthDataHoraFim = null;
	var nodDataHoraFim = p_domResposta.getElementsByTagName("fim")[p_intContaMsg];
	if (nodDataHoraFim ) {
		var txnDataHoraFim = nodDataHoraFim.firstChild;
		if (txnDataHoraFim) {
			var strDataHoraFim = txnDataHoraFim.nodeValue ;
			dthDataHoraFim = eval("new Date(" + strDataHoraFim + ");");
		}
	}
	return dthDataHoraFim;
}
function obterTextoMensagem(p_domResposta,p_intContaMsg) {
	var strTextoMensagem = null;
	var nodTextoMensagem = p_domResposta.getElementsByTagName("texto")[p_intContaMsg];
	if (nodTextoMensagem ) {
		var txnTextoMensagem = nodTextoMensagem.firstChild;
		if (txnTextoMensagem) {
			strTextoMensagem = txnTextoMensagem.nodeValue ;
		}
	}
	return strTextoMensagem;
}
function recarregarMensagensRemotas(p_strURLMensagensRemotas, 
									p_strIdPosicao,
									p_strStyleExibir)  {
	var func = setTimeout("carregarMensagensRemotas('" 
						+ p_strURLMensagensRemotas 
						+ "','" 
						+ p_strIdPosicao 
						+ "','" 
						+ p_strStyleExibir 
						+ "' )", INTERVALO_CICLO_MENSAGENS);
}
function carregarXMLMensagensRemotas(p_strUrl) {
	try {
		if (window.XMLHttpRequest)	{
			// IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		} else 	{
			// IE6, IE5
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		var strUrl = p_strUrl ;
		var intAgora = new Date().getTime();
		if ( strUrl.indexOf("?") == -1) {
			strUrl = strUrl + "?rmtmsgagora=" + intAgora ;
		} else {
			strUrl = strUrl + "&rmtmsgagora=" + intAgora ;
		}
		xmlhttp.open("GET",strUrl,false);
		xmlhttp.setRequestHeader("If-Modified-Since", "Thu, 1 Jan 1970 00:00:00 GMT");
		xmlhttp.setRequestHeader("Cache-Control", "no-cache");
		xmlhttp.send(null);
		return xmlhttp.responseXML;
	} catch (e) {
		return null;
	}
}
