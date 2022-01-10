<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<c:set var="pesquisaLimitadaPorData" scope="session" 
	value="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar;DTLIMITADA: Pesquisar somente com data limitada ')}" />
<c:set var="podePesquisarDescricao" scope="session" 
	value="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar;PESQDESCR:Pesquisar descrição')}" />
<c:set var="podePesquisarDescricaoLimitada" scope="session" 
	value="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar;PESQDESCR:Pesquisar descrição;LIMITADA:Pesquisar descrição só se informar outros filtros')}" />
<c:set var="limiteDias" scope="session" value="${f:resource('/siga.pesquisa.limite.dias')}" />
<input type="hidden" id="limiteDias" name="limiteDias" value="${f:resource('/siga.pesquisa.limite.dias')}" />
<script type="text/javascript" language="Javascript1.1">
	window.onload = function() {
		var dt = document.getElementById('dtDocFinalString');
		if (dt == null) 
			montaTabAnos();
		else
			montaTabAnos(dt.value.substring(dt.value.length - 4))
		montaTabMeses();
		
		alteraOrigem();
		${idFormaDoc != null && idFormaDoc != 0? 'alteraTipoDaForma(false);' : ''}
		${idMod != null && idMod != 0? 'alteraForma(false);' : ''}
		podeDescricao(false);
		$(document.body).on("change","#idFormaDoc",function(){ podeDescricao(false);});	
		$(document.body).on("change","#idMod",function(){ podeDescricao(false);});
		if ($('a[data-toggle="tooltip"]'))
			$('a[data-toggle="tooltip"]').tooltip({
			    placement: 'bottom',
			    trigger: 'click'
			});
		validarFiltrosPesquisa();
	};
	
	function podeDescricao(limpaDescricao) {
		if ("${podePesquisarDescricao}" != "true") { 
			desabilitaDescricao();
			return;
		}

		if ("${podePesquisarDescricaoLimitada}" == "true") {
			if (document.getElementById('orgaoUsu').value != 0 
					&& (document.getElementById('idFormaDoc').value != "0" 
						|| !$('#idFormaDoc-spinner').hasClass('d-none'))
					&& document.getElementById('anoEmissaoString').value != 0) {
				habilitaDescricao();
				document.getElementById('tabAnos').value = document.getElementById('anoEmissaoString').value;
			} else {
				desabilitaDescricao();
				if (limpaDescricao)
					$('#descrDocumento').val("");
			}
		} else {
			habilitaDescricao();
		}
		validarFiltrosPesquisa();
	}

	function habilitaDescricao() {
		$('#descrDocumento').attr('placeholder', "Clique aqui para pesquisar pela descrição");
		$('#descrDocumento').removeAttr('readonly');
	}
	
	function desabilitaDescricao() {
		$('#descrDocumento').attr('placeholder', "Não é possível realizar a pesquisa pela descrição");
		$('#descrDocumento').attr('readonly', true);
	}
	
	function montaTabMeses() {
		if (!${pesquisaLimitadaPorData})
			return;
		// Cria as tabs com os meses no topo da pagina de acordo com o ano escolhido em tabAnos. 
		// Se o ultimo ano for o ano atual, gera com os ultimos 12 meses.
		var monthName = new Array("Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez");
		var d = new Date();
		mesAtual = d.getMonth();
		anoAtual = d.getFullYear();
		anoSel = document.getElementById('tabAnos').value;
		d.setYear((anoSel != 'ultAno'? anoSel : anoAtual));
		if (anoAtual != d.getFullYear())
			d.setMonth(11);
		d.setDate(1);
		var tabMes = document.querySelectorAll('#tabMeses>li>a'); 
		
		for (i=11; i>=0; i--) {
			if (i == 11 && d.getFullYear() === anoAtual) {
				tabMes[i].innerText = 'Últ.${limiteDias} dias';
			} else {
				var separador = "/";
				var anoStr = (d.getFullYear().toString()).substr(2);
				if (self.innerWidth < 1000) {
					separador = "  ";
					anoStr = d.getFullYear().toString();
				}
				tabMes[i].innerText = monthName[d.getMonth()] + separador + anoStr;
			}
			tabMes[i].setAttribute('onclick', 'setDtDoc(' + d.getMonth() + ',' + d.getFullYear().toString() + ');submitBusca();');
			tabMes[i].setAttribute('data-ano', d.getFullYear().toString());
			tabMes[i].setAttribute('data-mes', d.getMonth());
		    d.setMonth(d.getMonth() - 1);
		}
		setTabMesesFromDtDoc()
	}
	
	function setTabMesesFromDtDoc() {	
		// A partir das data de inicio e fim no form, ativa o mes/ano correspondente na tab. de meses
		// Se as datas não estiverem preenchidas, assume a data de hoje e o limite de dias permitido
		dtDocStr = document.getElementById("dtDocString");
		dtDocFinalStr = document.getElementById("dtDocFinalString");
		limDias = document.getElementById("limiteDias").value;
		
		if (dtDocFinalStr != null && dtDocFinalStr.value == "") 
			dtDocFinalStr.value = (new Date()).toLocaleDateString("pt-BR");
		if (dtDocStr != null && dtDocStr.value == "") 
			dtDocStr.value = new Date(new Date().getTime() - limDias *24*60*60*1000).toLocaleDateString("pt-BR");
		
		var ano = (dtDocFinalStr != null ? dtDocFinalStr.value.substring(dtDocFinalStr.value.length - 4) : '1');
		setTabMeses((dtDocFinalStr != null ? dtDocFinalStr.value.split('/') [1] : 11), ano);
	}
		
	function setTabMeses(mes, ano) {
		// Torna ativo um mes/ano na tabela de meses
		var jsel = $(".nav .nav-link[data-ano=" + ano + "][data-mes=" + (mes - 1) + "]");
		$(".nav .nav-link.active").removeClass('active');
		if (jsel.data('ano') == null) 
			$('.nav .nav-link:last').addClass('active');
		else 
			jsel.addClass('active');
	}
	
	function setDtDoc(mes, ano) {
		var dtIni = new Date();
		var dtFim = dtIni;
		limDias = document.getElementById('limiteDias').value;
		
		if (mes == dtIni.getMonth() && ano == dtIni.getFullYear()) {
			dtIni = new Date(dtIni.getTime() - limDias *24*60*60*1000);
		} else {
			diaFim = new Date(ano, +mes + +1, 0).getDate(); 
			dtIni = new Date(ano, mes, 1);
			dtFim = new Date(ano, mes, diaFim);
		}
		document.getElementById('dtDocString').value = dtIni.toLocaleDateString("pt-BR");   
		document.getElementById('dtDocFinalString').value = dtFim.toLocaleDateString("pt-BR");
	}
		
	function setDtDocAno(ano) {
		// Preenche data inicial e data final com o período de um ano inteiro
		var dtIni = new Date();
		var dtFim = dtIni;
		if (ano == dtIni.getFullYear()) {
			dtIni = new Date(dtIni.getTime() - 366*24*60*60*1000);
		} else {
			dtIni = new Date(ano, 0, 1);
			dtFim = new Date(ano, 11, 31);
		}
		document.getElementById('dtDocString').value = dtIni.toLocaleDateString("pt-BR");   
		document.getElementById('dtDocFinalString').value = dtFim.toLocaleDateString("pt-BR");
	}
	
	function montaTabAnos(anoInicial) {
		if (!${pesquisaLimitadaPorData})
			return;
		
		var tbAnos = document.getElementById('tabAnos');
		var anoAtual = new Date().getFullYear();
		for (i = anoAtual; i > 2010; i--) {
		   var opt = document.createElement("option");
		   opt.value= i;
		   opt.innerHTML = (i == anoAtual ? 'Últ.Ano' : i);
		   tbAnos.appendChild(opt);
		}
		tbAnos.value = (anoInicial == null || anoInicial == "" ? anoAtual : anoInicial);
	}

	function setDtDocAnoInteiro(ano) {
		var dt = new Date();
		var mes = '11';
		if (dt.getFullYear() == ano)
			mes = dt.getMonth();
		setDtDoc(mes, ano)
	}

	function desabilitaTabMeses() {
		$('#tabMeses li.nav-item a.nav-link').addClass('disabled');
		montaTabMeses();
		document.getElementById('dtDocString').value = null;   
		document.getElementById('dtDocFinalString').value = null;
	}

	function habilitaTabMeses() {
		$('#tabMeses li.nav-item a.nav-link').removeClass('disabled');
		montaTabMeses();
	}

	function alteraAno(ano) {
		var dt = new Date();
		var mes = '11';
		if (dt.getFullYear() == ano)
			mes = dt.getMonth();
		setDtDoc(mes, ano)
		montaTabMeses();
	}
	
	function validarFiltrosPesquisa() {
		// Se preencher órgão, ano de emissão e espécie, poderá pesquisar por descrição
		if (document.getElementById('numExpediente').value != ''
			|| (document.getElementById('orgaoUsu').value != 0 
					&& (document.getElementById('idFormaDoc').value != "0" 
						|| !$('#idFormaDoc-spinner').hasClass('d-none'))
					&& document.getElementById('anoEmissaoString').value != 0)) { 
			document.getElementById('limiteDias').value = 366;
			desabilitaTabMeses();
			if (document.getElementById('numExpediente').value == '')
				document.getElementById('tabAnos').disabled = true;
			setDtDocAno(document.getElementById('tabAnos').value);
			return;
		}

		// Se usuário não tem limitação por data, não precisa validar regras abaixo
		if (!${pesquisaLimitadaPorData})
			return;
		
		// Se pesquisa dos documentos de uma pessoa/lotação, não tem limite de data
		if ((document.getElementById('formulario_ultMovLotaRespSel_id').value != 0
				|| document.getElementById('formulario_ultMovRespSel_id').value != 0)
				&& document.getElementById('ultMovIdEstadoDoc').value != 0) { 
			desabilitaTabMeses();
			return;
		}
		
		document.getElementById('limiteDias').value = ${limiteDias};
		habilitaTabMeses();
		document.getElementById('tabAnos').disabled = false;
		setDtDoc($(".nav .nav-link.active").data('mes'), $(".nav .nav-link.active").data('ano'));
	}
	
	function montaDescricao(id, via, descrDoc) {
		var popW = 700;
		var popH = 500;
		var winleft = (screen.width - popW) / 2;
		var winUp = (screen.height - popH) / 2;
		var winProp = '\'width=' + popW + ',height=' + popH + ',left='
				+ winleft + ',top=' + winUp + ',scrollbars=yes,resizable\'';
		var url = '\'<c:url value="/app/expediente/doc/exibir"/>?popup=true&id='
				+ id + '&via=' + via + '\'';

		var onclick = ' onclick="javascript:window.open(' + url
				+ ',\'documento\',' + winProp + ')"';
		var href = ' href="javascript:void(0)"';

		var a = '<a'+href+onclick+'>' + descrDoc + '</a>';
		return a;
	}
	
	function alteraOrigem() {
		var objSelecionado = document.getElementById('idTpDoc');
		if (objSelecionado != null) {
			switch (parseInt(objSelecionado.value)) {
			case 0:
				document.getElementById('trNumOrigDoc').style.display = 'none';
				document.getElementById('trNumDocSistAntigo').style.display = 'none';
				document.getElementById('trOrgExterno').style.display = 'none';
				document.getElementById('trTipo').style.display = '';
	
				if (document.getElementById('idFormaDoc')) document.getElementById('idFormaDoc').value = '0';
				break;
			case 1: // Interno Produzido
				document.getElementById('trNumOrigDoc').style.display = 'none';
				document.getElementById('trNumDocSistAntigo').style.display = 'none';
				document.getElementById('trOrgExterno').style.display = 'none';
				document.getElementById('trTipo').style.display = '';
	
				if (document.getElementById('idFormaDoc')) document.getElementById('idFormaDoc').value = '0';
				break;
			case 2: // Interno Folha de Rosto
				document.getElementById('trNumOrigDoc').style.display = '';
				document.getElementById('trNumDocSistAntigo').style.display = '';
				document.getElementById('trOrgExterno').style.display = 'none';
				document.getElementById('trTipo').style.display = '';
	
				if (document.getElementById('idFormaDoc')) document.getElementById('idFormaDoc').value = '0';
				break;
			case 3: // Externo Folha de Rosto
				document.getElementById('trNumOrigDoc').style.display = '';
				document.getElementById('trNumDocSistAntigo').style.display = '';
				document.getElementById('trOrgExterno').style.display = '';
				document.getElementById('trTipo').style.display = 'none';
	
				if (document.getElementById('idFormaDoc')) document.getElementById('idFormaDoc').value = '5';
				break;
			case 4: // Externo Capturado
				document.getElementById('trNumOrigDoc').style.display = '';
				document.getElementById('trNumDocSistAntigo').style.display = 'none';
				document.getElementById('trOrgExterno').style.display = '';
				document.getElementById('trTipo').style.display = '';
	
				if (document.getElementById('idFormaDoc')) document.getElementById('idFormaDoc').value = '0';
				break;
			case 5: // Interno Capturado
				document.getElementById('trNumOrigDoc').style.display = 'none';
				document.getElementById('trNumDocSistAntigo').style.display = 'none';
				document.getElementById('trOrgExterno').style.display = 'none';
				document.getElementById('trTipo').style.display = '';
	
				if (document.getElementById('idFormaDoc')) document.getElementById('idFormaDoc').value = '0';
				break;
			}
		}
	}

	function alteraAtendente() {
		var objSelecionado = document.getElementById('ultMovTipoResp');

		switch (parseInt(objSelecionado.value)) {
		case 1:
			document.getElementById('divUltMovResp').style.display = '';
			document.getElementById('divUltMovLotaResp').style.display = 'none';
			break;
		case 2:
			document.getElementById('divUltMovResp').style.display = 'none';
			document.getElementById('divUltMovLotaResp').style.display = '';
			break;
		}
	}

	function alteraCadastranteDocumento() {
		var objSelecionado = document.getElementById('tipoCadastrante');

		switch (parseInt(objSelecionado.value)) {
		case 1:
			document.getElementById('divCadastrante').style.display = '';
			document.getElementById('divLotaCadastrante').style.display = 'none';
			break;
		case 2:
			document.getElementById('divCadastrante').style.display = 'none';
			document.getElementById('divLotaCadastrante').style.display = '';
			break;
		}
	}

	function alteraDestinatarioDocumento() {
		var objSelecionado = document.getElementById('tipoDestinatario');

		switch (parseInt(objSelecionado.value)) {
		case 1:
			document.getElementById('divDestinatario').style.display = '';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		case 2:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = '';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		case 3:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = '';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		case 4:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = '';
			break;
		}
	}

	function limpaCampos() {
		var ultMovTipoResp = document.getElementById('ultMovTipoResp');

		switch (parseInt(ultMovTipoResp.value)) {
		case 1:
			document.getElementById('formulario_ultMovLotaRespSel_id').value = '';
			document.getElementById('formulario_ultMovLotaRespSel_descricao').value = '';
			document.getElementById('formulario_ultMovLotaRespSel_buscar').value = '';
			document.getElementById('formulario_ultMovLotaRespSel_sigla').value = '';
			document.getElementById('ultMovLotaRespSelSpan').innerHTML = '';
			break;
		case 2:
			document.getElementById('formulario_ultMovRespSel_id').value = '';
			document.getElementById('formulario_ultMovRespSel_descricao').value = '';
			document.getElementById('formulario_ultMovRespSel_buscar').value = '';
			document.getElementById('formulario_ultMovRespSel_sigla').value = '';
			document.getElementById('ultMovRespSelSpan').innerHTML = '';
			break;
		}

		var tipoCadastrante = document.getElementById('tipoCadastrante');

		switch (parseInt(tipoCadastrante.value)) {
		case 1:
			document.getElementById('formulario_lotaCadastranteSel_id').value = '';
			document.getElementById('formulario_lotaCadastranteSel_descricao').value = '';
			document.getElementById('formulario_lotaCadastranteSel_buscar').value = '';
			document.getElementById('formulario_lotaCadastranteSel_sigla').value = '';
			document.getElementById('lotaCadastranteSelSpan').innerHTML = '';
			break;
		case 2:
			document.getElementById('formulario_cadastranteSel_id').value = '';
			document.getElementById('formulario_cadastranteSel_descricao').value = '';
			document.getElementById('formulario_cadastranteSel_buscar').value = '';
			document.getElementById('formulario_cadastranteSel_sigla').value = '';
			document.getElementById('cadastranteSelSpan').innerHTML = '';
			break;
		}

		var tipoDestinatario = document.getElementById('tipoDestinatario');

		switch (parseInt(tipoDestinatario.value)) {
		case 1:
			document.getElementById('formulario_lotacaoDestinatarioSel_id').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqlotacaoDestinatarioSel').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_sigla').value = '';
			document.getElementById('lotacaoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_id').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqorgaoExternoDestinatarioSel').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_sigla').value = '';
			document.getElementById('orgaoExternoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('nmDestinatario').value = '';

			break;
		case 2:
			document.getElementById('formulario_destinatarioSel_id').value = '';
			document.getElementById('formulario_destinatarioSel_descricao').value = '';
			document.getElementById('formulario_destinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqdestinatarioSel').value = '';
			document.getElementById('formulario_destinatarioSel_sigla').value = '';
			document.getElementById('destinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_id').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqorgaoExternoDestinatarioSel').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_sigla').value = '';
			document.getElementById('orgaoExternoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('nmDestinatario').value = '';

			break;
		case 3:
			document.getElementById('formulario_destinatarioSel_id').value = '';
			document.getElementById('formulario_destinatarioSel_descricao').value = '';
			document.getElementById('formulario_destinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqdestinatarioSel').value = '';
			document.getElementById('formulario_destinatarioSel_sigla').value = '';
			document.getElementById('destinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_id').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqlotacaoDestinatarioSel').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_sigla').value = '';
			document.getElementById('lotacaoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('nmDestinatario').value = '';

			break;
		case 4:
			document.getElementById('formulario_destinatarioSel_id').value = '';
			document.getElementById('formulario_destinatarioSel_descricao').value = '';
			document.getElementById('formulario_destinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqdestinatarioSel').value = '';
			document.getElementById('formulario_destinatarioSel_sigla').value = '';
			document.getElementById('destinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_id').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqlotacaoDestinatarioSel').value = '';
			document.getElementById('formulario_lotacaoDestinatarioSel_sigla').value = '';
			document.getElementById('lotacaoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_id').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_descricao').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_buscar').value = '';
			document.getElementById('formulario_reqorgaoExternoDestinatarioSel').value = '';
			document.getElementById('formulario_orgaoExternoDestinatarioSel_sigla').value = '';
			document.getElementById('orgaoExternoDestinatarioSelSpan').innerHTML = '';

			break;
		}

		var listar_idTpDoc = document.getElementById('idTpDoc');

		switch (parseInt(listar_idTpDoc.value)) {
		case 0:
			document.getElementById('numExtDoc').value = '';

			document.getElementById('formulario_cpOrgaoSel_id').value = '';
			document.getElementById('formulario_cpOrgaoSel_descricao').value = '';
			document.getElementById('formulario_cpOrgaoSel_buscar').value = '';
			document.getElementById('formulario_cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			document.getElementById('numAntigoDoc').value = '';

			break;
		case 1:
			document.getElementById('numExtDoc').value = '';
			document.getElementById('formulario_cpOrgaoSel_id').value = '';
			document.getElementById('formulario_cpOrgaoSel_descricao').value = '';
			document.getElementById('formulario_cpOrgaoSel_buscar').value = '';
			document.getElementById('formulario_cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			document.getElementById('numAntigoDoc').value = '';

			break;
		case 2:
			document.getElementById('formulario_cpOrgaoSel_id').value = '';
			document.getElementById('formulario_cpOrgaoSel_descricao').value = '';
			document.getElementById('formulario_cpOrgaoSel_buscar').value = '';
			document.getElementById('formulario_cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';

			break;
		case 3:
			document.getElementById('idFormaDoc').value = '5';

			break;
		}
	
		var count = 0;

		if (document.getElementById('idTpDoc').value != 0)
			count++;	
		
		if (document.getElementById('dtDocString').value != "")
			count++;
			
		if (document.getElementById('dtDocFinalString').value != "")
			count++;
		
		if (document.getElementById('idTipoFormaDoc').value != 0)
			count++;
		
		if (document.getElementById('idMod') != null && document.getElementById('idMod').value != 0)
			count++;				
			
		if (document.getElementById('idFormaDoc').value != 0)
			count++;	
		
		if (document.getElementById('anoEmissaoString').value != "")
			count++;	
			
		if (document.getElementById('numExpediente').value != "")
			count++;		
		
		if (document.getElementById('subscritorSel_id').value != "")
			count++;	
		
		if (document.getElementById('cadastranteSel_id').value != "")
			count++;	
			
		if (document.getElementById('lotaCadastranteSel_id').value != "")
			count++;	
			
		if (document.getElementById('destinatarioSel_id').value != "")
			count++;	

		if (document.getElementById('lotacaoDestinatarioSel_id').value != "")
			count++;	

		if (document.getElementById('orgaoExternoDestinatarioSel_id').value != "")
			count++;	

		if (document.getElementById('nmDestinatario').value != "")
			count++;	

		if (document.getElementById('classificacaoSel_id').value != "")
			count++;	

		if (document.getElementById('descrDocumento').value != "")
			count++;	
			
		if (document.getElementById('fullText').value != "")
			count++;

		if (document.getElementById('ultMovIdEstadoDoc').value != 0)
			count++;	

		if (document.getElementById('ultMovRespSel_id').value != "")
			count++;	

		if (document.getElementById('ultMovLotaRespSel_id').value != "")
			count++;	

		if (count < 2 && ${formOrigem eq 'busca'}) {
			alert('Esta pesquisa retornará muitos resultados. Favor restringi-la um pouco mais.');
			descarrega();
			return false;
		}
		
		document.getElementById("btnBuscar").disabled = false;
		$('#buscandoSpinner').addClass('d-none');

		return true;
	}
</script>
<c:if test="${pesquisaLimitadaPorData}">
	<div id="pesqHeader" class="card-header sticky-top pb-0 pt-1 bg-light">
<!-- 	<a href="#collapsedocDados" class="card-toggle text-dark" data-toggle="collapse" data-target="#pesqFiltros" aria-expanded="true"></a> -->
		<style>
			#tabMeses .nav-link {padding: 0.3rem; white-space:pre-wrap;  ${formOrigem eq 'busca'?'max-width: 3rem;':''}}
			#tabMeses .nav-link.active {font-weight: bold;}
			#ui-datepicker {z-index:900 !important;}			
		</style>
		<ul id="tabMeses" class="nav nav-tabs ml-0 small">
			<li class="nav-item ml-0 d-flex align-items-center">
				<div class="input-group input-group-sm">
					<select id="tabAnos" class="form-control form-select input-group-sm p-0" aria-label="Default select example"
						onchange="alteraAno(this.value);">
					</select>
				</div>
			</li>
		    <li class="nav-item ml-1"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link" data-toggle="tab" href="#pesqResult"></a></li>
		    <li class="nav-item ml-0"><a class="nav-link active" data-toggle="tab" href="#pesqResult"></a></li>
		    <li>
		    	<a class="fas fa-info-circle h6 text-secondary m-2" data-toggle="tooltip" data-trigger="click" data-placement="bottom" 
		    		title='A pesquisa será "anual" somente para busca dos campos Número do Documento e Descrição (habilitado para pesquisa após o preenchimento dos campos "Órgão", "Espécie" e "Ano de Emissão").'></a>
		    </li>
		</ul>
	</div>
</c:if>
<c:if test="${!pesquisaLimitadaPorData}">
	<div id="pesqHeader" class="card-header bg-light">
		<h5 id="pesqTitle">Pesquisar Documentos</h5>
	</div>
</c:if>	
