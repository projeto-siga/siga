/**
 * Configura o comportamento da página da Mesa virtual, usando a API do Vue.js
 */
"use strict";

setTimeout(function() {
	$('#bem-vindo').fadeTo(1000, 0, function() {
		$('#row-bem-vindo').slideUp(1000);
	});
	$("#ultima-atualizacao").removeClass("text-danger");
	$("#ultima-atualizacao").addClass("text-secondary");
}, 5000);

if (getParmUser('trazerAnotacoes') == null)
	setParmUser('trazerAnotacoes', true)
var appMesa = new Vue({
	el: "#app",

	mounted: function() {
		this.errormsg = undefined;
		var self = this
		self.exibeLota = getParmUser('exibeLota');
		self.carregarMesa();
	},
	data: function() {
		return {
			mesa: undefined,
			filtro: undefined,
			resp: undefined,
			grupos: [],
			todos: {},
			carregando: false,
			primeiraCarga: true,
			exibeLota: undefined,
			acessos: [],
			errormsg: undefined,
			toggleConfig: '',
			selQtd: undefined,
			selQtdPag: (getParmUser('qtdPag') == null ? 15 : getParmUser('qtdPag')),
			trazerAnotacoes: true,
			trazerComposto: false,
			trazerArquivados: false,
			trazerCancelados: false,
			ordemCrescenteData: false,
			usuarioPosse: false,
			dtDMA: false
		};
	},
	computed: {
		filtrados: function() {
			var grps = JSON.parse(JSON.stringify(this.grupos));
			for (let g in grps) {
				if (grps[g].grupoDocs != undefined) {
					var a = grps[g].grupoDocs;
					var grupo;
					var odd = false;
					a = this.filtrarPorSubstring(
						a,
						this.filtro,
						"grupoNome,tempoRelativo,sigla,codigo,descr,origem,situacao,errormsg,list.nome,dataDevolucao".split(
							","
						)
					);
					a = a.filter(function(item) {
						return item.grupo !== "NENHUM";
					});
					grps[g].grupoDocs = a;
				}
			}
			this.$nextTick(function() { 
				initPopovers();
			}); 

			return grps;
		},
		filtradosTemAlgumErro: function() {
			if (!this.filtrados || this.filtrados.length === 0) return false;
			for (var i = 0; i < this.filtrados.length; i++) {
				if (this.filtrados[i].errormsg) return true;
			}
			return false;
		}
	},
	watch: {
		selQtdPag: function(qtdPag) {
			var parms = JSON.parse(getParmUser('gruposMesa'));
			if (parms != null) {
				for (var g in parms) {
					setValueGrupo(g, 'qtdPag', qtdPag);
					if (parms[g].collapsed == false) {
						setValueGrupo(g, 'qtd', qtdPag);
					}
				}
			}
			setParmUser('qtdPag', qtdPag);
			this.recarregarMesa();
		},
		trazerAnotacoes: function() {
			setParmUser('trazerAnotacoes', this.trazerAnotacoes);
			this.recarregarMesa();
		},
		trazerComposto: function() {
			setParmUser('trazerComposto', this.trazerComposto);
			this.recarregarMesa();
		},
		trazerArquivados: function() {
			setParmUser('trazerArquivados', this.trazerArquivados);
			setValueGrupo('Aguardando Ação de Temporalidade', 'hide', !this.trazerArquivados);
			this.recarregarMesa();
		},
		trazerCancelados: function() {
			setParmUser('trazerCancelados', this.trazerCancelados);
			this.recarregarMesa();
		},
		ordemCrescenteData: function() {
			setParmUser('ordemCrescenteData', this.ordemCrescenteData);
			this.recarregarMesa();
		},
		usuarioPosse: function() {
			setParmUser('usuarioPosse', this.usuarioPosse);
			this.recarregarMesa();
		},
		dtDMA: function() {
			setParmUser('dtDMA', this.dtDMA);
			this.recarregarMesa();
		}
		
	},
	methods: {
		carregarMesa: function(grpNome, qtdPagina) {
			var self = this
			this.trazerAnotacoes = (getParmUser('trazerAnotacoes') == null ? false : getParmUser('trazerAnotacoes'));
			this.trazerComposto = (getParmUser('trazerComposto') == null ? false : getParmUser('trazerComposto'));
			this.trazerArquivados = (getParmUser('trazerArquivados') == null ? false : getParmUser('trazerArquivados'));
			this.trazerCancelados = (getParmUser('trazerCancelados') == null ? false : getParmUser('trazerCancelados'));
			this.ordemCrescenteData = (getParmUser('ordemCrescenteData') == null ? false : getParmUser('ordemCrescenteData'));
			this.usuarioPosse = (getParmUser('usuarioPosse') == null ? false : getParmUser('usuarioPosse'));
			this.dtDMA = (getParmUser('dtDMA') == null ? false : getParmUser('dtDMA'));
			this.qtdPag = (getParmUser('qtdPag') == null ? 15 : getParmUser('qtdPag'));
			setValueGrupo('Aguardando Ação de Temporalidade', 'hide', !this.trazerArquivados);
			
			/* clean toast container before reload notification */
			$('#toastContainer').empty();

			var timeout = Math.abs(new Date() -
				new Date(sessionStorage.getItem('timeout' + getUser())));
			if (timeout < 120000 && grpNome == null) {
				if (sessionStorage.getItem('mesa' + getUser()) != undefined) {
					carregaFromJson(sessionStorage.getItem('mesa' + getUser()), self);
					resetCacheLotacaoPessoaAtual();
					return;
				}
			}
			if (this.carregando || timeout < 120000)
				return;
			this.carregando = true;
			var erros = {};
			if (this.grupos && this.grupos.length > 0) {
				for (var i = 0; i < this.grupos.length; i++) {
					erros[this.grupos[i].codigo] = this.grupos[i].errormsg;
				}
			}
			var parmGrupos = JSON.parse(getParmUser('gruposMesa'));
			var parms = {};
			if (grpNome != null) {
				parms[grpNome] = {
					'grupoOrdem': parmGrupos[grpNome].ordem,
					'grupoQtd': parseInt(parmGrupos[grpNome].qtd) + parseInt(this.qtdPag),
					'grupoQtdPag': parmGrupos[grpNome].qtdPag, 'grupoCollapsed': parmGrupos[grpNome].collapsed,
					'grupoHide': parmGrupos[grpNome].hide
				};
				setValueGrupo(grpNome, 'qtd', parseInt(parmGrupos[grpNome].qtd) + parseInt(this.qtdPag));
				
			} else {
				var i = 0;
				for (let p in parmGrupos) {
					parms[p] = {
						'grupoOrdem': parmGrupos[p].ordem, 'grupoQtd': parseInt(parmGrupos[p].qtd),
						'grupoQtdPag': parmGrupos[p].qtdPag, 'grupoCollapsed': parmGrupos[p].collapsed,
						'grupoHide': parmGrupos[p].hide
					};
				}
			}
			$.ajax({
				type: 'POST',
				url: 'mesa2.json',
				timeout: 120000,
				data: {
					parms: JSON.stringify(parms),
					exibeLotacao: this.exibeLota,
					trazerAnotacoes: this.trazerAnotacoes,
					trazerComposto: this.trazerComposto,
					trazerArquivados: this.trazerArquivados,
					trazerCancelados: this.trazerCancelados,
					ordemCrescenteData: this.ordemCrescenteData,
					usuarioPosse: this.usuarioPosse,
					dtDMA: this.dtDMA,
					idVisualizacao: ID_VISUALIZACAO
				},
				complete: function(response, status, request) {
					let cType = response.getResponseHeader('Content-Type');
					self.errormsg = undefined;
					if (cType && cType.indexOf('text/plain') !== -1 && response.status == 200) {
						self.errormsg = response.responseText;
						self.carregando = false;
					} else {
						if (response.status > 300) {
							if (cType != null && cType.indexOf('text/html') !== -1) {
								document.write(response.responseText);
							} else {
								self.errormsg = response.responseText & " - " & response.status;
							}
						} else {
							carregaFromJson(response.responseText, self);
							resetCacheLotacaoPessoaAtual();
							sessionStorage.setItem(
								'timeout' + getUser(), new Date());
						}
					}
				},
				failure: function(response, status) {
					self.carregando = false;
					self.showError(response.responseText, self);
				},
				success: function(){		
					$.ajax({
				        url: "/siga/api/v1/notificacoes",
				        contentType: "application/json",
				        dataType: 'json',
				        success: function(result){
							if (result.list.length > 0) {
								toaster(result.list);
							}
				        },
						error: function(result){	
				        	console.log(result.errormsg);
				        },
				   });
			   }
			})
		},
		resetaStorage: function() {
			sessionStorage.removeItem('mesa' + getUser());
			localStorage.removeItem('gruposMesa' + getUser());
			this.trazerAnotacoes = false;
			this.trazerComposto = false;
			this.trazerArquivados = false;
			localStorage.removeItem('trazerAnotacoes' + getUser());
			localStorage.removeItem('trazerComposto' + getUser());
			localStorage.removeItem('trazerArquivados' + getUser());
			localStorage.removeItem('trazerCancelados' + getUser());
			localStorage.removeItem('ordemCrescenteData' + getUser());
			localStorage.removeItem('usuarioPosse' + getUser());
			localStorage.removeItem('dtDMA' + getUser());
			localStorage.removeItem('qtdPag' + getUser());
			this.recarregarMesa();
			this.selQtdPag = 15;
			
			resetCacheLotacaoPessoaAtual();
		},
		recarregarMesa: function() {
			this.grupos = [];
			sessionStorage.removeItem('timeout' + getUser());
			this.carregarMesa();
		},
		carregarMesaUser: function() {
			setParmUser('exibeLota', false);
			this.exibeLota = false;
			this.recarregarMesa();
		},
		carregarMesaLota: function() {
			setParmUser('exibeLota', true)
			this.exibeLota = true;
			this.recarregarMesa();
		},
		setQtdPag: function(grupoNome, event) {
			var parmGrupos = JSON.parse(getParmUser('gruposMesa'));
			setValueGrupo(grupoNome, 'qtdPag', parseInt(event.target.value));
		},
		pageDownGrupo: function(grupoNome) {
			var parmGrupos = JSON.parse(getParmUser('gruposMesa'));
			if (parmGrupos[grupoNome].qtd === 0) {
				setValueGrupo(grupoNome, 'qtd', parmGrupos[grupoNome].qtdPag)
			} else {
				setValueGrupo(grupoNome, 'qtd', parseInt(parmGrupos[grupoNome].qtd));
			}
			sessionStorage.removeItem('timeout' + getUser());
			this.carregarMesa(grupoNome, parseInt(parmGrupos[grupoNome].qtdPag));
		},
		collapseGrupo: function(grupoOrdem, grupoNome) {
			// Abre ou fecha o grupo, salvando info no local/session storage e refletindo no vue
			var parmGrupos = JSON.parse(getParmUser('gruposMesa'));
			var collapsibleElemHeader = document.getElementById('collapse-header-' + grupoOrdem);
			if (collapsibleElemHeader.classList.contains('collapsed')) {
				setValueGrupoSessionStorage(grupoNome, 'grupoCollapsed', false);
				setValueGrupo(grupoNome, 'collapsed', false);
				setValueGrupo(grupoNome, 'qtd', parseInt(parmGrupos[grupoNome].qtd));
				if ($('#collapsetab-' + grupoOrdem + ' tr').length < 2) {
					sessionStorage.removeItem('timeout' + getUser());
					this.carregarMesa(grupoNome, 0);
				}
			} else {
				setValueGrupoSessionStorage(grupoNome, 'grupoCollapsed', true);
				setValueGrupo(grupoNome, 'collapsed', true);
			}
		},
		fecharGrupo: function(grupoOrdem, grupoNome) {
			$('#collapsetab-' + grupoOrdem).collapse('hide');
			setValueGrupo(grupoNome, 'collapsed', true);
			setValueGrupoVue(grupoNome, 'grupoCollapsed', true);
		},
		getLastRefreshTime: function() {
			if (sessionStorage.getItem('timeout' + getUser()) != null) {
				var dt = new Date(sessionStorage.getItem('timeout' + getUser()));
				return ("0" + dt.getDate()).slice(-2) + "/" + ("0" + (dt.getMonth() + 1)).slice(-2) + " "
					+ ("0" + dt.getHours()).slice(-2) + ":" + ("0" + dt.getMinutes()).slice(-2);
				
			}
			return "Atualizando...";
		},
		toggleMenuConfig: function() {
			if (this.toggleConfig === 'show-config') {
				this.toggleConfig = 'hide-config';
			} else {
				this.toggleConfig = 'show-config';
			}
		},
		fixItem: function(item) {
			this.applyDefauts(item, {
				checked: false,
				disabled: false,
				grupo: undefined,
				grupoNome: undefined,
				datahora: undefined,
				datahoraFormatada: undefined,
				sigla: undefined,
				codigo: undefined,
				descr: undefined,
				origem: undefined,
				situacao: undefined,
				errormsg: undefined,
				odd: undefined
			});
			if (item.datahora !== undefined) {
				item.datahoraFormatada = this.formatJSDDMMYYYYHHMM(item.datahora);
			}
			return item;
		},
		// De UtilsBL
		applyDefauts: function(obj, defaults) {
			for (var k in defaults) {
				if (!defaults.hasOwnProperty(k)) continue;
				if (obj.hasOwnProperty(k)) continue;
				obj[k] = defaults[k];
			}
		},
		showError: function(error, component) {
			component.errormsg = undefined;
			try {
				component.errormsg = error.data.errormsg;
			} catch (e) { }
			if (
				component.errormsg === undefined &&
				error.statusText &&
				error.statusText !== ""
			) {
				component.errormsg = error.statusText;
			}
			if (component.errormsg === undefined) {
				component.errormsg = "Erro desconhecido!";
			}
		},
		// Filtra itens por uma string qualquer. Se desejar restringir o filtro apenas a algumas das propriedades,
		// informar o parâmetro propriedades na forma: ['propriedade','outraPropriedade','lista.propriedade']
		filtrarPorSubstring: function(a, s, propriedades) {
			var re = new RegExp(s, "i");
			return a.filter(function filterItem(item, idx, arr, context) {
				for (var key in item) {
					if (!item.hasOwnProperty(key)) continue;
					var prop = context;
					prop = context === undefined ? key : context + "." + key;
					if (Array.isArray(item[key])) {
						for (var i = 0; i < item[key].length; i++) {
							if (filterItem(item[key][i], i, item[key], prop)) return true;
						}
					}
					if (propriedades && propriedades.indexOf(prop) === -1) continue;
					if (typeof item[key] === "string" && re.test(item[key])) {
						return true;
					}
					if (
						typeof item[key] === "object" &&
						filterItem(item[key], 0, arr, prop)
					) {
						return true;
					}
				}
				return false;
			});
		},
		formatJSDDMMYYYYHHMM: function(s) {
			if (!s) return;
			var r =
				s.substring(8, 10) +
				"/" +
				s.substring(5, 7) +
				"/" +
				s.substring(0, 4) +
				"&nbsp;" +
				s.substring(11, 13) +
				":" +
				s.substring(14, 16);
			return r;
		},
		formatJSDDMMYYYY_AS_HHMM: function(s) {
			if (s === undefined) return

			var r = this.formatJSDDMMYYYYHHMM(s)
			r = r.replace('&nbsp;', ' às ')
			return r
		},
		formatJSDDMMYYYY: function(s) {
			/* A partir da data em string formato DD/MM/YYYY HH:MM ou DD-MM-YYYYTHH:MM:SS...,
			 * devolve somente a data em formato DD/MM/YYYY
			 */
			if (s === undefined) return;

			return s.substring(0,10).replace('-', '/');
		}
	}
});

function atualizaGrupos(grupos) {
	var parms = JSON.parse(getParmUser('gruposMesa'));
	if (parms == null) {
		for (var g in grupos) {
			setGrupo(grupos[g].grupoNome, grupos[g].grupoOrdem, grupos[g].grupoQtd, grupos[g].grupoQtdPag,
				grupos[g].grupoCollapsed, grupos[g].hide);
		}
	}
	for (var i in parms) {
		if (parms[i].qtdPag > 0) {
			var grpSelect = document.getElementById('selQtdItens-' + parms[i].ordem);
			if (grpSelect != null)
				grpSelect.value = parms[i].qtdPag;
		}
	}
}

function cloneArray(arr) {
	var newArray = [];
	for (var i = 0; i < arr.length; i++) {
		if (arr[i].isArray) {
			cloneArray(arr[i])
		} else {
			newArray.push(arr[i])
		}
	}
	return newArray;
}

function setGrupo(grupoNome, ordem, qtd, qtdPag, collapsed, hide) {
	// Seta um parametro com dados de selecao do grupo no JSON de parametros e armazena na local storage. 
	// Se a quantidade for "" ou false, deleta o parametro
	var parms = JSON.parse(getParmUser('gruposMesa'));
	if (parms == null) {
		parms = { "key": "" };
		parms[grupoNome] = { 'ordem': ordem, 'qtd': qtd, 'qtdPag': qtdPag, 'collapsed': collapsed, 'hide': hide };
		delete parms["key"];
	} else {
		if (qtd === "" || qtd === false) {
			delete parms[grupoNome];
		} else {
			parms[grupoNome] = { 'ordem': ordem, 'qtd': qtd, 'qtdPag': qtdPag, 'collapsed': collapsed, 'hide': hide };
		}
	}
	setParmUser('gruposMesa', JSON.stringify(parms));
}

function setValueGrupo(grupoNome, key, value) {
	var parms = JSON.parse(getParmUser('gruposMesa'));
	if (parms != null) {
		parms[grupoNome][key] = value;
		setParmUser('gruposMesa', JSON.stringify(parms));
	}
}

function setValueGrupoVue(grupoNome, key, value) {
	for (var g in appMesa.grupos) {
		if (appMesa.grupos[g].grupoNome == grupoNome)
			Vue.set(appMesa.grupos[g], key, value);
	}
}

function setValueGrupoSessionStorage(grupoNome, key, value) {
	var grp = JSON.parse(sessionStorage.getItem('mesa' + getUser()));
	for (var g in grp) {
		if (grp[g].grupoNome == grupoNome) {
			grp[g][key] = value;
		}
	}
	sessionStorage.setItem(
		'mesa' + getUser(), JSON.stringify(grp));
}

function setParmUser(nomeParm, value) {
	window.localStorage.setItem(nomeParm + getUser(), value)
}

function getParmUser(nomeParm) {
	let val = window.localStorage.getItem(nomeParm + getUser())
	if (val === 'true')
		val = true;
	if (val === 'false')
		val = false;
	return val
}

function slideDown(id) {
	setInterval(function() {
		document.getElementById("#" + id).slideDown();
	}, 20);
}

function carregaFromJson(json, appMesa) {
	var grp = JSON.parse(json);
	if (grp.length > 1) {
		appMesa.grupos = grp;
	} else {
		for (var g in appMesa.grupos) {
			if (appMesa.grupos[g].grupoOrdem === grp[0].grupoOrdem) {
				Vue.set(appMesa.grupos, g, grp[0]);
				if (appMesa.grupos[g].grupoDocs == undefined)
					appMesa.grupos[g].grupoDocs = [];
				var q = Math.round((appMesa.grupos[g].grupoDocs.length / appMesa.grupos[g].grupoQtdPag) - 0.51)
					* appMesa.grupos[g].grupoQtdPag + appMesa.grupos[g].grupoQtdPag;
				if (q > 0)
					setValueGrupo(appMesa.grupos[g].grupoNome, 'qtd', q);
			}
		}
	}
	sessionStorage.setItem(
		'mesa' + getUser(), JSON.stringify(appMesa.grupos));
	atualizaGrupos(appMesa.grupos);
	appMesa.primeiraCarga = false;
	appMesa.carregando = false;
}

function getUser() {
	return document.getElementById('cadastrante').title + (ID_VISUALIZACAO === 0 ? "" : ID_VISUALIZACAO); // ${ idVisualizacao == 0 ? '""' : idVisualizacao };
}

/**
 * Substitui as ocorrencias de &quot; na descrição por aspas duplas (") e ainda 
 * corta a string se for necessário. Na base de dados as aspas duplas são 
 * armazenadas como <code>&quot;</code> e é isso que o Vue vai exibir. Por isso se faz 
 * necessário esse tratamento. 
 *
 * @param {string} descr A String original
 * @param {number} [limit=null] O tamanho da string a ser retornada. Se não for passado valor, 
 * será usado Number.MAX_SAFE_INTEGER
 * @return A String original com as aspas duplas no lugar de &quot; e com o 
 * tamanho máximo solicitado. Se a string original for maior que o limite 
 * solicitado, serão adicionadas '...''
 */
function processDescription(descr, limit = null) {
	let _limit = limit ? (limit + 1) : Number.MAX_SAFE_INTEGER;
	let result = descr.replace(/&quot;/g, '"');

	return (result.length > _limit) ? result.substring(0, _limit).concat('...') : result;
}

function initPopovers() {
	var timeOut;
	if (!$('.popover-dismiss').popover) { 
		return; 
	} 

	$('.popover-dismiss').popover({
		html: true,
		animation: false,
		trigger: 'manual'
	}).on("mouseenter", function() {
		var _this = this;
		timeOut = setTimeout( function() {
			if ($(_this).attr('data-pessoa') !== undefined) {
				mountPopoverMarcaPessoa(_this);
			} else if ($(_this).attr('data-lotacao') !== undefined) {
				mountPopoverMarcaLotacao(_this);
			}
			
			$(_this).popover("show");
			$(".popover").on("mouseleave", function() {
				$(_this).popover('hide');
			});
		}, 700);
	}).on("click", function() {
		var _this = this;
		/*** Implementar function***/
		$(this).popover("show");
		$(".popover").on("mouseleave", function() {
			$(_this).popover('hide');
		});
	}).on("mouseleave", function() {
		var _this = this;
		setTimeout(function() {
			if (!$(".popover:hover").length) {
				$(_this).popover("hide");
			}
		}, 100);
		clearTimeout(timeOut);
	})
	
	
}



function mountPopoverMarcaPessoa(_this) {
	
	if ($(_this).attr('data-content') === undefined) { 
		$(_this).attr('data-content', "<div class='spinner-border' role='status'>");
		var cache = sessionStorage.getItem("pessoa."+$(_this).attr('data-pessoa'));
		if (cache == null) {
			$.ajax({
		        url: "/siga/api/v1/pessoas?idPessoaIni="+ $(_this).attr('data-pessoa'),
		        contentType: "application/json",
		        dataType: 'json',
		        success: function(result){
		        	sessionStorage.setItem("pessoa."+$(_this).attr('data-pessoa'), JSON.stringify(result.list[0]));
		        	$(_this).attr('data-content', '<b>'+ result.list[0].lotacao.sigla +'</b>  '+result.list[0].nome);
		        	$(_this).popover("show");
		        }
		    });
		} else {
			var pessoaAtualParsed = JSON.parse(cache);
        	$(_this).attr('data-content', '<b>'+ pessoaAtualParsed.lotacao.sigla +'</b>  '+pessoaAtualParsed.nome);
        	$(_this).popover("show");
		}
	}
}


function mountPopoverMarcaLotacao(_this) {
	
	if ($(_this).attr('data-content') === undefined) { 
		$(_this).attr('data-content', "<div class='spinner-border' role='status'>");
		var cache = sessionStorage.getItem("lotacao."+$(_this).attr('data-lotacao'));
		if (cache == null) {
			$.ajax({
		        url: "/siga/api/v1/lotacoes?idLotacaoIni="+ $(_this).attr('data-lotacao'),
		        contentType: "application/json",
		        dataType: 'json',
		        success: function(result){
		        	sessionStorage.setItem("lotacao."+$(_this).attr('data-lotacao'), JSON.stringify(result.list[0]));
		        	$(_this).attr('data-content', '<b>'+ result.list[0].sigla +'</b>  '+result.list[0].nome);
		        	$(_this).popover("show");
		        }
		    });
		} else {
			var lotacaoAtualParsed = JSON.parse(cache);
        	$(_this).attr('data-content', '<b>'+ lotacaoAtualParsed.sigla +'</b>  '+lotacaoAtualParsed.nome);
        	$(_this).popover("show");
		}
	}
}


function resetCacheLotacaoPessoaAtual() {
	for (var obj in sessionStorage) {
	      if (sessionStorage.hasOwnProperty(obj) && (obj.includes("pessoa.") || obj.includes("lotacao."))) {
	    	  sessionStorage.removeItem(obj);
	      }
	}
}

function toaster(_notificacoes) {
	
	var toastContainer = $('#toastContainer');
	
	/* clean toast container before reload notifications */
	toastContainer.empty();
	
	/* Create Toast*/
	_notificacoes.forEach(createToast);


	function createToast(item) {
		var id = item.idNotificacao;
		var icone = item.icone;
		var titulo = item.titulo;
		var conteudo = item.conteudo;
		
		
		$('<div>', {
		    id: 'toastNotificacao_'+id,
		    class: 'toast',
		    role: 'alert',
			'aria-live': 'assertive',
			'aria-atomic': 'true',
			'data-autohide': 'false'
		}).appendTo(toastContainer);
		
		/* Create Toast Header*/
		$('<div>', {
		    id: 'toastNotificacaoHeader_'+id,
		    class: 'toast-header '
		}).appendTo('#toastNotificacao_'+id);
		

		
		$('#toastNotificacaoHeader_'+id).html(mountToastHeader(icone,titulo));
			

		$('<button>', {
		    id: 'toastNotificacaoHeaderButton_'+id,
			type: 'button',
		    class: 'ml-2 mb-1 close',
			'data-dismiss': 'toast',
			'aria-label':'Close'
		}).appendTo('toastNotificacaoHeader_'+id);
		
		$('<div>', {
		    id: 'toastNotificacaoBody_'+id,
		    class: 'toast-body'
		}).appendTo('#toastNotificacao_'+id);
		
		
		$('#toastNotificacaoBody_'+id).html(conteudo);
		
		$('#toastNotificacao_'+id).on('shown.bs.toast', function () {
		  /* TODO: mostrado notificação */
		})
		
		$('#toastNotificacao_'+id).on('hidden.bs.toast', function () {
		  /* TODO: dispensado notificacao */
		})
		
	}

	function mountToastHeader(icone,titulo) {
		var header ="";
		header = '<span class="mr-auto font-weight-bold">';
		
		if (icone != null || icone != "") { //Icone da Notificação
			header = header +'<i class="'+icone+'"></i>&nbsp;';	
		}
		if (titulo != null || titulo != "") { //Título da Notificação
			header = header + titulo;	
		}
		
		header = header + '</span>';
		header = header + '<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
		return header;	
	}
	
	$(document).ready(function() {
        $(".toast").toast('show');
    });

}

