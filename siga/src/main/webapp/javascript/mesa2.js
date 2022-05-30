/**
 * Configura o comportamento da página da Mesa virtual, usando a API do Vue.js
 */
"use strict";
const qtdMax = 20;
setTimeout(function() {
	$('#bem-vindo').fadeTo(1000, 0, function() {
		$('#row-bem-vindo').slideUp(1000);
	});
	$("#ultima-atualizacao").removeClass("text-danger");
	$("#ultima-atualizacao").addClass("text-secondary");
}, 5000);

if (!getParmUser('trazerAnotacoes'))
	setParmUser('trazerAnotacoes', true)
if (getParmUser('gruposMesa'))
	localStorage.removeItem('gruposMesa' + getUser());
	
var appMesa = new Vue({
	el: "#app",

	mounted: function() {
		this.errormsg = undefined;
		var self = this;
		self.exibeLota = getParmUser('exibeLota');
		self.getItensGrupo();
		self.contar = false;
		// Carrega todas linhas não preenchidas que estiverem na tela
		var timeoutId;
		window.addEventListener('scroll', function ( event ) {
			window.clearTimeout( timeoutId );
			timeoutId = setTimeout(function() {
				if (!self.carregando) {
					// Se o usuario rolar a pagina, espera ele parar de rolar por alguns décimos de seg.
					self.carregaLinhasExibidasNaTela();
					window.clearTimeout( timeoutId );
				}
			}, 400);
		}, false);
	},
	data: function() {
		return {
			mesa: undefined,
			filtro: undefined,
			grupos: [],
			carregando: false,
			contar: true,
			exibeLota: undefined,
			acessos: [],
			errormsg: undefined,
			toggleConfig: '',
			qtd: qtdMax,
			selQtd: undefined,
			qtdPag: (getParmUser('qtdPag') ? parseInt(getParmUser('qtdPag')) : 15),
			trazerAnotacoes: true,
			trazerComposto: false,
			trazerArquivados: false,
			trazerCancelados: false,
			ordemCrescenteData: false,
			usuarioPosse: false,
			dtDMA: false
		};
	},
	watch: {
		qtdPag: function() {
			setParmUser('qtdPag', parseInt(this.qtdPag));
			setParmGrupos('grupoQtd', parseInt(this.qtdPag));
			setParmGrupos('grupoQtdLota', parseInt(this.qtdPag));
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
			setValueGrupo('Aguardando Ação de Temporalidade', 'grupoHide', !this.trazerArquivados);
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
		getItensGrupo: function(grpNome, offset, qtd) {
			var self = this
			this.setParmsDefault();
			let contar = true;

			/* clean toast container before reload notification */
			$('#toastContainer').empty();

			let timeout = Math.abs(new Date() -
				new Date(sessionStorage.getItem('timeout' + getUser())));
			if (timeout < 120000 && grpNome) {
				contar = false;
			}
			if (this.carregando)
				return;
			this.carregando = true;
			let q = (!qtd || qtd > qtdMax? qtdMax : qtd)
			let parmGrupos = JSON.parse(getParmUser('grupos'));
			let parms = {};
			let gNome = grpNome;
			if (gNome) {
				parms[gNome] = {
					'grupoOrdem': parmGrupos[gNome].grupoOrdem,
					'grupoQtd': parseInt(parmGrupos[gNome].grupoQtd),
					'grupoQtdLota': parseInt(parmGrupos[gNome].grupoQtdLota),
					'grupoCollapsed': parmGrupos[gNome].grupoCollapsed,
					'grupoHide': parmGrupos[gNome].grupoHide};
				this.mostraSpinner(getGrupoVue(gNome), offset, q);
			} else {
				for (let p in parmGrupos) {
					parms[p] = {
						'grupoOrdem': parmGrupos[p].grupoOrdem, 
						'grupoQtd': (parseInt(parmGrupos[p].grupoQtd) > 0? parseInt(parmGrupos[p].grupoQtd) : q),
						'grupoQtdLota': (parseInt(parmGrupos[p].grupoQtdLota) > 0? parseInt(parmGrupos[p].grupoQtdLota) : q),
						'grupoCollapsed': parmGrupos[p].grupoCollapsed,
						'grupoHide': parmGrupos[p].grupoHide
					};
				}
			}
			$.ajax({
				type: 'GET',
				url: 'mesa2.json',
				timeout: 120000,
				data: {
					parms: JSON.stringify(parms),
					qtd: q,
					offset: (offset? offset : 0),
					contar: contar,
					exibeLotacao: this.exibeLota,
					trazerAnotacoes: this.trazerAnotacoes,
					trazerComposto: this.trazerComposto,
					trazerArquivados: this.trazerArquivados,
					trazerCancelados: this.trazerCancelados,
					ordemCrescenteData: this.ordemCrescenteData,
					usuarioPosse: this.usuarioPosse,
					dtDMA: this.dtDMA,
					filtro: this.filtro,
					idVisualizacao: ID_VISUALIZACAO
				},
				complete: function(response, status, request) {
					let cType = response.getResponseHeader('Content-Type');
					self.errormsg = undefined;
					if (status == 'timeout') {
						self.errormsg = "O servidor demorou para responder. Por favor tente novamente ou use a pesquisa.";
					} else if (cType && cType.indexOf('text/plain') !== -1 && response.status == 200) {
						self.errormsg = response.responseText;
					} else if (response.status == 401 || response.status == 403) { 
						window.location.href = "/siga/public/app/login" 
					} else if (response.status > 300) {
						if (cType != null && cType.indexOf('text/html') !== -1) {
							document.write(response.responseText);
						} else {
							self.errormsg = response.responseText & " - " & response.status;
						}
					} else {
						carregaFromJson(response.responseText, self);
						for (const el of document.getElementsByClassName('spinner-more')) 
						    el.remove();
						resetCacheLotacaoPessoaAtual();
						if (contar)
							sessionStorage.setItem(
								'timeout' + getUser(), new Date());
					}
					self.carregando = false;
				},
				failure: function(response, status) {
					alert("Ocorreu um erro no servidor. Por favor recarregue a mesa ou faça o login novamente.");
					self.carregando = false;
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
		mostraSpinner: function(grp, offset, qtd) {
			if (grp.grupoDocs) {
				for (i=offset; i - offset < qtd; i++) {
					if (grp.grupoDocs[i] && grp.grupoDocs[i].datahora == '')
						grp.grupoDocs[i].datahora = '.';
				}
			}
			Vue.set(grp, 'grupoDocs', JSON.parse(JSON.stringify(grp.grupoDocs)));
		},
		criaLinhasFantasmas: function(grp, offset, qtd) {
			if (!grp.grupoDocs)
				Vue.set(grp, 'grupoDocs', []);
			for (let i=0; i < qtd && i < grp.grupoCounterAtivo; i++) {
				grp.grupoDocs.push({offset: i.toString(), grupoOrdem: grp.grupoOrdem, codigo: "", dataDevolucao: "ocultar", datahora: ".", datahoraDDMMYYYHHMM: "", 
					descr: "", list: [], lotaCadastrante: "", lotaPosse: "", nomePessoaPosse: "", origem: "", sigla: "", tempoRelativo: "", tipo: "Documento", tipoDoc: ""});
			}
			Vue.set(grp, 'grupoDocs', JSON.parse(JSON.stringify(grp.grupoDocs)));
		},
		carregaLinhasExibidasNaTela: function() {
			// Se encontrar uma linha de referencia (nao preenchida) 
			// que está exibida na tela, vai carregar mais desse grupo e retorna true.
			// Se não encontrar, retorna false.
			let linhaRef = getPrimeiroExibido('.linha-ref');
			if (linhaRef) {
				let grp = linhaRef.getAttribute('data-grupo');
				let offset = parseInt(linhaRef.getAttribute('data-numitem'));
				let listaItens = document.querySelectorAll("[data-grupo='" + grp + "']")
				// Conta quantas linhas vazias tem até prox. linha preenchida
				let i = offset;
				while (i < listaItens.length && listaItens[i].classList.contains('linha-ref')) 
					i++;
				let q = i - offset;
				if (q < qtdMax) {
					// Se não atingiu qtd max a trazer, volta o offset até 
					// atingir ou encontrar linha preenchida
					for (let o=offset - 1; o >= 0 && q < qtdMax 
							&& listaItens[o].classList.contains('linha-ref'); o--) {
						q = q + 1;
						offset = offset - 1;
					}
				}
				this.getItensGrupo(grp, offset, (q > 0? q : qtdMax));
				return true;
			}
			return false;
		},
		isVisible: function(elem) {
			if (!elem)
				return false
			var rect = elem.getBoundingClientRect();
			var viewHeight = Math.max(document.documentElement.clientHeight, window.innerHeight);
			return !(rect.bottom < 0 || rect.top - viewHeight >= 0);
		},	
		resetaStorage: function() {
			localStorage.removeItem('grupos' + getUser());
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
			sessionStorage.removeItem('listaNotificacaoSilenciada' + getUser());
			this.qtdPag = 15;
			
			resetCacheLotacaoPessoaAtual();
		},
		setParmsDefault: function() {
			this.trazerAnotacoes = (getParmUser('trazerAnotacoes') == null ? false : getParmUser('trazerAnotacoes'));
			this.trazerComposto = (getParmUser('trazerComposto') == null ? false : getParmUser('trazerComposto'));
			this.trazerArquivados = (getParmUser('trazerArquivados') == null ? false : getParmUser('trazerArquivados'));
			this.trazerCancelados = (getParmUser('trazerCancelados') == null ? false : getParmUser('trazerCancelados'));
			this.ordemCrescenteData = (getParmUser('ordemCrescenteData') == null ? false : getParmUser('ordemCrescenteData'));
			this.usuarioPosse = (getParmUser('usuarioPosse') == null ? false : getParmUser('usuarioPosse'));
			this.dtDMA = (getParmUser('dtDMA') == null ? false : getParmUser('dtDMA'));
			setValueGrupo('Aguardando Ação de Temporalidade', 'grupoHide', !this.trazerArquivados);
		},
		
		recarregarMesa: function() {
			this.grupos = [];
			sessionStorage.removeItem('timeout' + getUser());
			setParmGrupos();
			this.getItensGrupo(null, 0, this.qtdPag);
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
		pageDownGrupo: function(grupoNome) {
			let listaLinhas = document.querySelectorAll("[data-grupo='" + grupoNome + "']");
			let offset = 0;
			if (listaLinhas.length > 0)
				offset = parseInt(listaLinhas[listaLinhas.length - 1]
						.getAttribute('data-numitem')) + 1;
			sessionStorage.removeItem('timeout' + getUser());
			if (this.exibeLota)
				setValueGrupo(grupoNome, 'grupoQtdLota', listaLinhas.length + parseInt(this.qtdPag));
			else
				setValueGrupo(grupoNome, 'grupoQtd', listaLinhas.length + parseInt(this.qtdPag));
			this.criaLinhasFantasmas(getGrupoVue(grupoNome), offset, this.qtdPag);
			this.getItensGrupo(grupoNome, offset, this.qtdPag);
		},
		collapseGrupo: function(grupoOrdem, grupoNome) {
			// Abre ou fecha o grupo, salvando info no local storage e refletindo no vue
			var parmGrupos = JSON.parse(getParmUser('grupos'));
			var collapsibleElemHeader = document.getElementById('collapse-header-' + grupoOrdem);
			if (collapsibleElemHeader.classList.contains('collapsed')) {
				setValueGrupo(grupoNome, 'grupoCollapsed', false);
				setValueGrupoVue(grupoNome, 'grupoCollapsed', false);
				if ($('#collapsetab-' + grupoOrdem + ' tr').length < 2) {
					this.pageDownGrupo(grupoNome);
				}
			} else {
				setValueGrupo(grupoNome, 'grupoCollapsed', true);
				setValueGrupoVue(grupoNome, 'grupoCollapsed', true);
			}
		},
		fecharGrupo: function(grupoOrdem, grupoNome) {
			$('#collapsetab-' + grupoOrdem).collapse('hide');
			setValueGrupo(grupoNome, 'grupoCollapsed', true);
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
	for (var g in grupos) {
		setGrupo(grupos[g].grupoNome, grupos[g].grupoOrdem, grupos[g].grupoQtd, 
				grupos[g].grupoQtdLota, grupos[g].grupoCollapsed, grupos[g].grupoHide);
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

function setGrupo(grupoNome, ordem, qtd, qtdLota, collapsed, hide) {
	// Seta um parametro com dados de selecao do grupo no JSON de parametros e armazena na local storage. 
	// Se a quantidade for "" ou false, deleta o parametro
	var parms = JSON.parse(getParmUser('grupos'));
	if (parms == null) {
		parms = { "key": "" };
		parms[grupoNome] = { 'grupoOrdem': ordem, 'grupoQtd': qtd, 'grupoQtdLota': qtdLota, 'grupoCollapsed': collapsed, 'grupoHide': hide };
		delete parms["key"];
	} else {
		if ((qtd === "" || qtd === false) && (qtdLota === "" || qtdLota === false)) {
			delete parms[grupoNome];
		} else {
			parms[grupoNome] = { 'grupoOrdem': ordem, 'grupoQtd': qtd, 'grupoQtdLota': qtdLota, 'grupoCollapsed': collapsed, 'grupoHide': hide };
		}
	}
	setParmUser('grupos', JSON.stringify(parms));
}
function getValueGrupo(grupoNome, key) {
	var parms = JSON.parse(getParmUser('grupos'));
	if (parms != null) {
		return parms[grupoNome][key];
	}
}

function setValueGrupo(grupoNome, key, value) {
	var parms = JSON.parse(getParmUser('grupos'));
	if (parms != null) {
		parms[grupoNome][key] = value;
		setParmUser('grupos', JSON.stringify(parms));
	}
}

function setValueGrupoVue(grupoNome, key, value) {
	for (var g in appMesa.grupos) {
		if (appMesa.grupos[g].grupoNome == grupoNome)
			Vue.set(appMesa.grupos[g], key, value);
	}
}
function getGrupoVue(grupoNome) {
	for (var g in appMesa.grupos) {
		if (appMesa.grupos[g].grupoNome == grupoNome)
			return appMesa.grupos[g];
	}
}
function setParmGrupos(nomeParm, value) {
	var parms = JSON.parse(getParmUser('grupos'));
	for (var g in parms) {
		setValueGrupo(g, nomeParm, value);
	}
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
	let grp = JSON.parse(json);
	if (appMesa.filtro && grp.length == 0)
		appMesa.errormsg = "Não foram encontrados documentos para esta pesquisa.";
		
	if (grp.length > 1) {
		preencheLinhasFantasmas(grp);
		appMesa.grupos = grp;
	} else {
		if (grp[0].grupoDocs && grp[0].grupoDocs.length > 0) {
			for (let g in appMesa.grupos) {
				if (appMesa.grupos[g].grupoOrdem == grp[0].grupoOrdem) {
					let grDocs = appMesa.grupos[g].grupoDocs;
					if (grDocs) {
						for (i=grDocs.length - 1; i >= 0; i--) {
							if (grDocs[i].codigo === '')
								grDocs.splice(i, 1);
						}
						grp[0].grupoDocs = grDocs.concat(grp[0].grupoDocs);
					}
					if (!grp[0].grupoCounterAtivo) {
						grp[0].grupoCounterAtivo = appMesa.grupos[g].grupoCounterAtivo;
						grp[0].grupoCounterLota = appMesa.grupos[g].grupoCounterLota;
						grp[0].grupoCounterUser = appMesa.grupos[g].grupoCounterUser;
					}
					removeLinhasDuplicadas(grp[0].grupoDocs);
					preencheLinhasFantasmas(grp);
					Vue.set(appMesa.grupos, g, grp[0]);
				}
			}
		} else {
			for (let g in appMesa.grupos) {
				if (appMesa.grupos[g].grupoOrdem == grp[0].grupoOrdem) 
					appMesa.grupos[g].grupoDocs = [];
			}
		}
	}
	
	appMesa.grupos = removerGruposDuplicados(appMesa.grupos);
	atualizaGrupos(appMesa.grupos);
	appMesa.$nextTick(function() { 
		initPopovers();
	}); 
}

function removeLinhasDuplicadas(grp) {
	for (var i = 1; i < grp.length; i++) {
		let siglaDoc = grp[i].codigo;  
		let ix = grp.findIndex(e => e.codigo === siglaDoc && e != grp[i]);
		if (ix >= 0)
			grp.splice(ix, 1);
	}
}
function preencheLinhasFantasmas(grupos) {
	// De acordo com os contadores de cada grupo e as linhas já baixadas, gera linhas sem dados
	// Estas linhas servirão de referência para obter mais linhas do server quando o usuário rolar página
	for (let j=0; j < grupos.length; j++) {
		let g = grupos[j];
		if (!g.grupoCollapsed && g.grupoCounterAtivo > 0) {
			if (!g.grupoDocs)
				g.grupoDocs = [];
			let docsFinal = []
			let qt = getGrupoQtd(g);
			for (let h=0; h < qt && h < g.grupoCounterAtivo; h++) {
				let ix = g.grupoDocs.findIndex(e => e.offset === h.toString());
				if (ix < 0) {
					docsFinal.push({offset: h.toString(), grupoOrdem: g.grupoOrdem, codigo: "", dataDevolucao: "ocultar", datahora: "", datahoraDDMMYYYHHMM: "", 
						descr: "", list: [], lotaCadastrante: "", lotaPosse: "", nomePessoaPosse: "", origem: "", sigla: "", tempoRelativo: "", tipo: "Documento", tipoDoc: ""});
				} else {
					docsFinal.push(g.grupoDocs[ix]);
					g.grupoDocs.splice(ix, 1);
				}
			}
			g.grupoDocs = docsFinal;
		}
	}
}
function removerGruposDuplicados(values) {
	let concatArray = values.map(eachValue => {
		return Object.values(eachValue).join('')
	});
	let filterValues = values.filter((value, index) => {
		return concatArray.indexOf(concatArray[index]) === index

	});
	return filterValues;
}

function getGrupoQtd(grupo) {
	if (appMesa.exibeLota)
		return grupo.grupoQtdLota;
	else
		return grupo.grupoQtd;
}

function getUser() {
	return document.getElementById('cadastrante').title + (ID_VISUALIZACAO === 0 ? "" : ID_VISUALIZACAO); // ${ idVisualizacao == 0 ? '""' : idVisualizacao };
}

function getPrimeiroExibido(sel) {
	let todos = document.querySelectorAll(sel);
	if (todos.length > 0)
		for(i=0; i < todos.length; i++) {
			if (todos[i].getBoundingClientRect().top > 0 
					&& todos[i].getBoundingClientRect().bottom < window.innerHeight)
				return todos[i];
		}
	return null;
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
	if (_notificacoes != null)
		_notificacoes.forEach(createToast);


	function createToast(item) {
		var id = item.idNotificacao;
		
		if (id === undefined)
			return;
			
		var icone = item.icone;
		var titulo = item.titulo;
		var conteudo = item.conteudo;
		var silenciarNotificacao = item.sempreMostrar;
		
		let listaNotificacaoSilenciada = sessionStorage.getItem('listaNotificacaoSilenciada' + getUser());
		
		if (listaNotificacaoSilenciada == null || !listaNotificacaoSilenciada.includes(id)) {
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
			  if (!silenciarNotificacao) {
			  	let sessionArray = sessionStorage.getItem('listaNotificacaoSilenciada' + getUser());
			  	if (sessionArray == null)
			  		sessionArray = [];
			  	sessionArray.push(id);
			  	sessionStorage.setItem('listaNotificacaoSilenciada' + getUser(), sessionArray);	
			  }
			})
		}
		
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