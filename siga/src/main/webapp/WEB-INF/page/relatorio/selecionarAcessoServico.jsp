<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
	
<siga:pagina titulo="Relatório de Acesso à Utilização de Serviço">
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />

	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header">
				<h5>Relatório de Permissão de Usuários</h5>
			</div>
			<div class="card-body">
			<form id="frm" method="post" action="#">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
							<label>Serviço</label>
							<select name="idServico" id="idServico"	onchange="javascript: criarSituacoesServico()" class="form-control  siga-select2">
								<c:forEach var="serv" items="${cpServicos}">
									<option value="${serv.idServico}">
										${serv.siglaServico} - ${serv.dscServico}</option>
								</c:forEach>
							</select>
						</div>
					</div>				
					<div class="col-sm-4">
						<div class="form-group">
							<label>Situações</label>
							<input type="hidden" id="situacoesSelecionadas" name="situacoesSelecionadas" />
							<table id="situacoesServico"></table>
						</div>
					</div>				
				</div>
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<label>Pessoas</label>
							<select name="idOrgaoUsuario" id="idOrgaoUsuario" class="form-control  siga-select2">
								<option value="-1">Todos</option>
								<c:forEach var="ousu" items="${cpOrgaosUsuario}">
									<option value="${ousu.idOrgaoUsu}">
										${ousu.nmOrgaoUsu}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<div class="form-group">
							<button class="btn btn-primary" onClick="javascript:gerar(); return false;">Gerar...</button>
						</div>
					</div>				
				</div>
			</form>
			</div>
		</div>
	</div>
	<div id="div-tempo" style="display: none; position: absolute; text-align: center; filter: alpha(opacity =     60); opacity: 0.4; background-color: #dcdcdc; vertical-align: middle; border-width: 2px; border-color: darkblue; border-style: solid;">
		<div style="position: absolute; font-family: sans-serif50 %; left: 40%; top: 50%; font-size: medium; large; font-weight: bolder; color: purple;">Aguarde...</div>
	</div>
	
<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>	
</siga:pagina>
<script type="text/javascript">
 	 function gerar() {
 		atualizarSituacoesSelecionadas();
 	 	 if (seTemSituacaoSelecionada()) {
	 	 	 var t_nodForm = document.getElementById('frm');
	 	 	 if (t_nodForm) {
		 	 	 t_nodForm.setAttribute("action","emitir_acesso_servico");
	 	 	 	 t_nodForm.submit();
	 	 	 	 exibirAguarde();
	 	 	 	 return true;
	 	 	 }
 	 	 } else {
 	 	 	 alert("Por favor, selecione pelo menos uma situação!");
 	 	 }
 	 	 return false;
 	 }
 	 function seTemSituacaoSelecionada() {
 		var t_nodSits = document.getElementById('situacoesSelecionadas');
 		if (t_nodSits) {
 	 	 	return t_nodSits.value;
 		}
 		return false;
 	 }
 	 function atualizarSituacoesSelecionadas() {
 		var t_arr1NodInput = document.getElementsByTagName('input');
 		var t_arr1Situacoes = new Array();
 		for (var t_intConta = 0; t_intConta < t_arr1NodInput.length; t_intConta++) {
 	 		var t_nodSit = t_arr1NodInput[t_intConta];
 	 		var t_strNome = t_nodSit.getAttribute("name");
 			if (t_strNome.indexOf( 'situacao_') >= 0 ) {
 	 			if (t_nodSit.checked) {
 	 				t_strIdSit = t_strNome.split("_")[1];
 	 				t_arr1Situacoes.push(t_strIdSit);
 	 			}
 			}
 		}
 		var t_nodSits = document.getElementById('situacoesSelecionadas');
		var t_strSit = new String();
 		if (t_nodSits) {
 	 		for (var t_intConta = 0; t_intConta < t_arr1Situacoes.length; t_intConta++) {
 	 	 		if (t_intConta == 0) {
 	 	 			t_strSit = t_strSit + t_arr1Situacoes[t_intConta];
 	 	 		} else {
 	 	 			t_strSit = t_strSit + "," +  t_arr1Situacoes[t_intConta];
 	 	 		}
 	 		}
 	 		t_nodSits.value = t_strSit;
 		}
 	 }
	 function criarSituacoesServico() {
		 var t_nodSelecServico = document.getElementById('idServico');
		 if (t_nodSelecServico) {
			 var t_strIdServico = t_nodSelecServico.options[t_nodSelecServico.selectedIndex].value;
			 var t_arr1Situacoes = obterSituacoesServico(t_strIdServico);
			 if (t_arr1Situacoes) {
				 var t_nodTabServicos = document.getElementById('situacoesServico');
				 if (t_nodTabServicos) {
					 removerNodesFilhos(t_nodTabServicos);
					 criarNodesFilhos( t_nodTabServicos, t_arr1Situacoes);
				 }
			 }
		 }
	 }
	 function criarNodesFilhos( p_nodTabServicos, p_arr1Situacoes) {
		 var t_nodTbody = document.createElement('tbody');
		 for (var t_idxSituacao = 0; t_idxSituacao < p_arr1Situacoes.length; t_idxSituacao++ ) {
			 var t_nodTr = document.createElement('tr'); 
			 var t_nodTdLabel = document.createElement('td');  
			 t_nodTr.appendChild(t_nodTdLabel);
			 var t_nodLabel = document.createElement('label');
			 var t_nodDesc = document.createTextNode(p_arr1Situacoes[t_idxSituacao]["descricao"]);
			 t_nodLabel.appendChild(t_nodDesc);
			 t_nodTdLabel.appendChild(t_nodLabel);
			 var t_nodTdCheck = document.createElement('td'); 
			 t_nodTr.appendChild(t_nodTdCheck);
			 var t_nodCheck = document.createElement('input');
			 t_nodCheck.setAttribute('type','checkbox');
			 t_nodCheck.setAttribute('id','check_sit_' + t_idxSituacao);
			 t_nodCheck.setAttribute('name', 'situacao_' + p_arr1Situacoes[t_idxSituacao]["codigo"]);
			 t_nodTdCheck.appendChild(t_nodCheck);
			 
 		     t_nodTbody.appendChild(t_nodTr) ;
		 }
		 p_nodTabServicos.appendChild(t_nodTbody);
		 // tratamento do BUG do IE
		 setTimeout(
				 function () {
					 var t2_arr1Situacoes = p_arr1Situacoes;
					 for (var t_idxSituacao = 0; t_idxSituacao < p_arr1Situacoes.length; t_idxSituacao++ ) {
						 var t_nodCheck = document.getElementById('check_sit_' + t_idxSituacao);
						 if (t_nodCheck) {
							 if (p_arr1Situacoes[t_idxSituacao]["seDefault"] == "false") {
									var t_nodCheck = t_nodCheck;
								 	t_nodCheck.checked = "checked" ;
							 }
						 }
					}
				 }
		 , 100 );
	 }
	 function removerNodesFilhos(p_nodObj) {
		 var t_arr1NodItens = p_nodObj.childNodes;
		 var t_intQuantoFilhos = t_arr1NodItens.length;
		 for (var t_intConta = t_intQuantoFilhos - 1; t_intConta >= 0; t_intConta--) {
			 p_nodObj.removeChild(t_arr1NodItens[t_intConta]);
		 }
	 }
	 function obterSituacoesServico(p_strIdServico) {
		 var t_smrRequisicao = new SimpleMethodRequestRPCGet ();
			var t_strUrl = 'obter_situacoes_servico';
			t_smrRequisicao.setUrl(t_strUrl);
			t_smrRequisicao.addUrlParam("idServico", p_strIdServico);
			try {
				exibirAguarde();
				var t_objResult = t_smrRequisicao.submeterSincrono();
				if (t_objResult.idservico != p_strIdServico) {
					throw new Error("Situações não obtidas ! ");
				}
				var t_strQuantasSituacoes =  t_objResult.quantas;
				var t_intQuantasSituacoes = parseInt(t_strQuantasSituacoes);
				if (t_intQuantasSituacoes) {
					var t_arr1Situacoes = new Array();
					for (var t_intConta = 0; t_intConta < t_intQuantasSituacoes; t_intConta++) {
						t_arr1Situacoes [t_intConta] = {
							codigo: t_objResult["idsituacao_" + t_intConta],	
							descricao: t_objResult["descsituacao_" + t_intConta],
							seDefault: t_objResult["sedefault_" + t_intConta]} ;
					}
					voltarDoAguarde();
					return t_arr1Situacoes;
				}
			} catch (e) {
				voltarDoAguarde();
				if (e.description) {
					alert(e.description);
				} else {
					alert(e.message);
				}
				return null;
			}
	 }
 </script>
<script type="text/javascript">
	 /*
		 ***** Exibição do  Aguarde **** 
	*/
	function exibirAguarde() {
		var nodDivConteudo = document.getElementById("frm");
		if (nodDivConteudo) {
			var nodDivTempo = document.getElementById("div-tempo");
			if (nodDivTempo) {
				nodDivTempo.style.display = "block"
				var intScrollYSize = getScrollingYSize(); 
				var intScrollXSize = getScrollingXSize(); 
				var intScrollYPos = getScrollingYPos() ; 
				var intScrollXPos = getScrollingXPos() ; 
				nodDivTempo.style.zIndex = '3';
				nodDivTempo.style.left = intScrollXPos.toString()     + 'px' ;
				nodDivTempo.style.top =  intScrollYPos.toString()     + 'px' ;
				nodDivTempo.style.width =  intScrollXSize.toString() + 'px' ;
				nodDivTempo.style.height =  intScrollYSize.toString() + 'px' ;
				// correção do bug do IE
				var arr1NodSelect = document.getElementsByTagName('select');
				if (arr1NodSelect) {
					for (var intConta = 0; intConta < arr1NodSelect.length; intConta++) {
						var nodSelect = arr1NodSelect[intConta];
						nodSelect.disabled=true;
					}
				}
			}
		} 
	}
	function voltarDoAguarde() {
	 var nodDivConteudo = document.getElementById("frm");
		if (nodDivConteudo) {
			var nodDivTempo = document.getElementById("div-tempo");
			if (nodDivTempo) {
				nodDivTempo.style.display = "none"
				var arr1NodSelect = document.getElementsByTagName('select');
				if (arr1NodSelect) {
					for (var intConta = 0; intConta < arr1NodSelect.length; intConta++) {
						var nodSelect = arr1NodSelect[intConta];
						nodSelect.disabled=false;
					}
				}	
			}
		} 
	}
	function getScrollingXPos() {
		if (navigator.appName == "Microsoft Internet Explorer"){
			return document.body.scrollLeft ;
		} else {
			return  window.pageXOffset ;
		}
	}
	
	function getScrollingYPos() {
		if (navigator.appName == "Microsoft Internet Explorer"){
			return document.body.scrollTop ;
		} else {
			return  window.pageYOffset ;
		}
	}
	function getScrollingXSize() {
		if (navigator.appName == "Microsoft Internet Explorer"){
			return document.body.clientWidth ;
		} else { 
			//return  window.screen.width ;
			return  window.innerWidth  ;
		}
	}
	
	function getScrollingYSize() {
		if (navigator.appName == "Microsoft Internet Explorer"){
			return document.body.clientHeight ;
		} else {
			//return  window.screen.height ;
			return  window.innerHeight ;
		}
	}
 </script>
<script type="text/javascript">
 // inicialização
 	setTimeout(
 		 	function() {
		 		criarSituacoesServico(); 
		 	}
	, 50 ); 	
</script>
<script type="text/javascript" src="/siga/javascript/siga.bloqueiaF5.js"></script>