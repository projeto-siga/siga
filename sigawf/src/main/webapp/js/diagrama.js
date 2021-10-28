var app = angular.module('app', ['angucomplete-alt']);

// Create an AngularJS service called debounce
app.factory('debounce', ['$timeout', '$q', function($timeout, $q) {
	// The service is actually this function, which we call with the func
	// that should be debounced and how long to wait in between calls
	return function debounce(func, wait, immediate) {
		var timeout;
		// Create a deferred object that will be resolved when we need to
		// actually call the func
		var deferred = $q.defer();
		return function() {
			var context = this,
				args = arguments;
			var later = function() {
				timeout = null;
				if (!immediate) {
					deferred.resolve(func.apply(context, args));
					deferred = $q.defer();
				}
			};
			var callNow = immediate && !timeout;
			if (timeout) {
				$timeout.cancel(timeout);
			}
			timeout = $timeout(later, wait);
			if (callNow) {
				deferred.resolve(func.apply(context, args));
				deferred = $q.defer();
			}
			return deferred.promise;
		};
	};
}]);

app
	.controller(
		'ctrl',
		function($scope, $http, debounce) {
			$scope.data = {};

			$scope.gravar = function() {
				var fd = formdata({
					id: $scope.id,
					pd: $scope.encode($scope.data.workflow)
				});
				$http({
					url: '/sigawf/app/diagrama/gravar',
					method: "POST",
					data: fd,
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				}).then(function(response) {
					window.location = '/sigawf/app/diagrama/listar';
				}, function(response) {
					alert(response.data.errormsg)
				});

			}

			$scope.desativar = function() {
				$http({
					url: '/sigawf/app/diagrama/desativar?id=' + $scope.id,
					method: "POST",
					data: {},
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				}).then(function(response) {
					window.location = '/sigawf/app/diagrama/listar'
				}, function(response) {
					alert(response.data.errormsg)
				});

			}

			$scope.encode = function(d) {
				var pd = {
					hisIde: undefined,
					nome: d.nome,
					descr: d.descricao,
					acessoDeEdicao: d.acessoDeEdicao,
					acessoDeInicializacao: d.acessoDeInicializacao,
					tipoDePrincipal: d.tipoDePrincipal,
					tipoDeVinculoComPrincipal: d.tipoDeVinculoComPrincipal,
					definicaoDeTarefa: []
				};
				if (d.responsavel && d.responsavel.originalObject && d.responsavel.originalObject.key)
					pd.responsavelId = d.responsavel.originalObject.key;
				if (d.lotaResponsavel && d.lotaResponsavel.originalObject && d.lotaResponsavel.originalObject.key)
					pd.lotaResponsavelId = d.lotaResponsavel.originalObject.key;
				if (d.tarefa) {
					for (var i = 0; i < d.tarefa.length; i++) {
						var t = d.tarefa[i];
						var td = {};
						td.hisIde = t.id;
						td.nome = t.titulo;
						td.assunto = t.assunto;
						td.conteudo = t.conteudo;
						td.tipoDeTarefa = t.tipo;
						td.tipoDeResponsavel = t.tipoResponsavel;
						// td.definicaoDeResponsavel =
						// t.definicaoDeResponsavel;
						td.seguinteIde = t.depois === "fim" ? undefined : t.depois;
						td.ultimo = t.depois === "fim";
						td.ordem = i;
						td.definicaoDeVariavel = [];
						td.definicaoDeDesvio = [];

						if (t.tipoResponsavel == 'LOTACAO' && t.refUnidadeResponsavel && t.refUnidadeResponsavel.originalObject && t.refUnidadeResponsavel.originalObject.key)
							td.lotacaoId = t.refUnidadeResponsavel.originalObject.key;

						if (t.tipoResponsavel == 'PESSOA' && t.refPessoaResponsavel && t.refPessoaResponsavel.originalObject && t.refPessoaResponsavel.originalObject.key)
							td.pessoaId = t.refPessoaResponsavel.originalObject.key;

						if (t.tipoResponsavel == 'RESPONSAVEL')
							td.definicaoDeResponsavelId = t.refResponsavel;

						if (t.tipo == 'INCLUIR_DOCUMENTO' && t.ref && t.ref.originalObject && t.ref.originalObject.key) {
							td.refId = t.ref.originalObject.key;
							td.refSigla = t.ref.originalObject.firstLine;
						}

						pd.definicaoDeTarefa.push(td);

						if (d.tarefa[i].variavel) {
							for (var j = 0; j < d.tarefa[i].variavel.length; j++) {
								var variavel = d.tarefa[i].variavel[j];
								var dv = {};
								dv.nome = variavel.titulo;
								dv.identificador = variavel.identificador;
								dv.tipo = variavel.tipo;
								dv.acesso = variavel.tipoDeEdicao;
								dv.ordem = j;
								td.definicaoDeVariavel.push(dv);
							}
						}

						if (d.tarefa[i].desvio) {
							for (var j = 0; j < d.tarefa[i].desvio.length; j++) {
								var desvio = d.tarefa[i].desvio[j];
								var dd = {};
								dd.nome = desvio.titulo;
								dd.seguinteIde = desvio.tarefa === "fim" ? undefined : desvio.tarefa;
								dd.ultimo = desvio.tarefa === "fim";
								dd.condicao = desvio.condicao;
								dd.ordem = j;
								td.definicaoDeDesvio.push(dd);
							}
						}
					}
				}
				return pd;
			}

			$scope.decode = function(d) {
				var pd = {
					hisIde: undefined,
					nome: d.nome,
					descricao: d.descr,
					acessoDeEdicao: d.acessoDeEdicao,
					acessoDeInicializacao: d.acessoDeInicializacao,
					tipoDePrincipal: d.tipoDePrincipal,
					tipoDeVinculoComPrincipal: d.tipoDeVinculoComPrincipal,
					tarefa: []
				};
				if (d.responsavel)
					pd.responsavel = {
						originalObject: d.responsavel
					}
				if (d.lotaResponsavel)
					pd.lotaResponsavel = {
						originalObject: d.lotaResponsavel
					}
				if (d.definicaoDeTarefa) {
					for (var i = 0; i < d.definicaoDeTarefa.length; i++) {
						var t = d.definicaoDeTarefa[i];
						var td = {};
						td.id = t.hisIde;
						td.titulo = t.nome;
						td.assunto = t.assunto;
						td.conteudo = t.conteudo;
						td.tipo = t.tipoDeTarefa;
						td.tipoResponsavel = t.tipoDeResponsavel;
						// td.definicaoDeResponsavel =
						// t.definicaoDeResponsavel;
						// td.depois = t.seguinteIde;
						// td.ultimo = t.ultimo;
						td.depois = t.ultimo ? "fim" : t.seguinteIde;
						td.ordem = i;
						td.variavel = [];
						td.desvio = [];

						if (t.lotacao)
							td.refUnidadeResponsavel = {
								originalObject: t.lotacao
							}
						if (t.pessoa)
							td.refPessoaResponsavel = {
								originalObject: t.pessoa
							}

						if (t.definicaoDeResponsavel)
							td.refResponsavel = t.definicaoDeResponsavel;

						if (t.refId) {
							td.ref = {
								originalObject: {
									key: t.refId,
									firstLine: t.refSigla
								}
							}
						}

						pd.tarefa.push(td);

						if (d.definicaoDeTarefa[i].definicaoDeVariavel) {
							for (var j = 0; j < d.definicaoDeTarefa[i].definicaoDeVariavel.length; j++) {
								var variavel = d.definicaoDeTarefa[i].definicaoDeVariavel[j];
								var dv = {};
								dv.titulo = variavel.nome;
								dv.identificador = variavel.identificador;
								dv.tipo = variavel.tipo;
								dv.tipoDeEdicao = variavel.acesso;
								dv.ordem = j;
								td.variavel.push(dv);
							}
						}

						if (d.definicaoDeTarefa[i].definicaoDeDesvio) {
							for (var j = 0; j < d.definicaoDeTarefa[i].definicaoDeDesvio.length; j++) {
								var desvio = d.definicaoDeTarefa[i].definicaoDeDesvio[j];
								var dd = {};
								dd.titulo = desvio.nome;
								dd.tarefa = desvio.ultimo ? "fim" : desvio.seguinteIde;
								dd.condicao = desvio.condicao;
								dd.ordem = j;
								td.desvio.push(dd);
							}
						}
					}
				}
				return pd;
			}

			$scope.carregar = function(id) {
				$http({
						url: '/sigawf/app/diagrama/' + id + '/carregar',
						method: "GET"
					})
					.then(
						function(response) {
							var duplicar = new URLSearchParams(
									window.location.search)
								.get('duplicar') == 'true';
							if (duplicar) {
								delete $scope.id;
								var d = response.data;
								delete d.id;
								if (d.definicaoDeTarefa) {
									for (var i = 0; i < d.definicaoDeTarefa.length; i++) {
										delete d.definicaoDeTarefa[i].id;
									}
								}
							}
							$scope.data.workflow = $scope
								.decode(response.data);
						},
						function(response) {});

			}

			$scope.carregarRecursos = function(cont) {
				$http({
						url: '/sigawf/app/diagrama/acesso-de-inicializacao/carregar',
						method: "GET"
					})
					.then(
						function(response) {
							$scope.acessosDeInicializacao = response.data.list;
							$http({
									url: '/sigawf/app/diagrama/acesso-de-edicao/carregar',
									method: "GET"
								})
								.then(
									function(response) {
										$scope.acessosDeEdicao = response.data.list;
										$http({
												url: '/sigawf/app/diagrama/tipo-de-principal/carregar',
												method: "GET"
											})
											.then(
												function(response) {
													$scope.tiposDePrincipal = response.data.list;
													$http({
															url: '/sigawf/app/diagrama/tipo-de-vinculo-com-principal/carregar',
															method: "GET"
														})
														.then(
															function(response) {
																$scope.tiposDeVinculoComPrincipal = response.data.list;
																$http({
																		url: '/sigawf/app/responsavel/carregar',
																		method: "GET"
																	})
																	.then(
																		function(
																			response) {
																			$scope.responsaveis = response.data.list;
																			if (cont)
																				cont();
																		});
															});
												});
									});
						});
			}

			$scope.selectedObject = function(p1, p2) {
				p2.context[p2.variable] = p1;
				if (p2.full)
					$scope
					.loadRef(p2.variable,
						p2.context[p2.variable]);
			}

			$scope.insert = function() {
				$scope.data.item = ($scope.data.item || []);
				$scope.data.item.push({});
			}

			$scope
				.$watch(
					'data',
					function() {
						if (!$scope.data || !$scope.data.workflow || !$scope.data.workflow.tarefa) {
							if ($scope.data && $scope.data.workflow)
								$scope.data.workflow.dot = undefined;
							document
								.getElementById('graph-workflow').innerHTML = "";
							$scope.tarefas = undefined;
							return;
						}
						var tarefas = [];

						for (var i = 0; i < $scope.data.workflow.tarefa.length; i++) {
							var t = $scope.data.workflow.tarefa[i];
							if (!t.id)
								t.id = uuidv4();
							t.nome = i + 1 + ") " + (t.titulo ? t.titulo : t.tipo);
							tarefas.push({
								id: t.id,
								nome: t.nome
							})
						}
						tarefas.push({
							id: "fim",
							nome: "[Fim]"
						})

						$scope.tarefas = tarefas;
						console.log('graphDrawDebounced')
						$scope.graphDrawDebounced();
					}, true);

			$scope.getResponsavelNome = function(s) {
				if (!$scope.responsaveis)
					return;
				for (var i = 0; i < $scope.responsaveis.length; i++) {
					if ($scope.responsaveis[i].id === s)
						return $scope.responsaveis[i].nome;
				}
				return s;
			}

			$scope.getPrincipalNome = function() {
				if (!$scope.tiposDePrincipal)
					return;
				for (var i = 0; i < $scope.tiposDePrincipal.length; i++) {
					if ($scope.tiposDePrincipal[i].id === $scope.data.workflow.tipoDePrincipal)
						return $scope.tiposDePrincipal[i].descr;
				}
				return s;
			} 

			$scope.graphDraw = function() {
				console.log('graphDraw')
				$scope.dot = $scope.getDot($scope.data.workflow);
				$
					.ajax({
						url: "/siga/public/app/graphviz/svg",
						data: $scope.dot,
						type: 'POST',
						processData: false,
						contentType: 'text/vnd.graphviz',
						contents: window.String,
						success: function(data) {
							document
								.getElementById('graph-workflow').innerHTML = data;
							window.updateContainer();
						},
						error: function(data) {
							$scope.showError(data);
						}
					});

			}

			$scope.graphDrawDebounced = debounce($scope.graphDraw,
				1000, false);

			function graphElement(shape, n, nextn) {
				var resp;
				switch (n.tipoResponsavel) {
					case 'LOTACAO':
						if (n.refUnidadeResponsavel && n.refUnidadeResponsavel.originalObject && n.refUnidadeResponsavel.originalObject.firstLine)
							resp = n.refUnidadeResponsavel.originalObject.firstLine
						else
							resp = n.tipoResponsavel
						break;
					case 'PESSOA':
						if (n.refPessoaResponsavel && n.refPessoaResponsavel.originalObject && n.refPessoaResponsavel.originalObject.firstLine)
							resp = n.refPessoaResponsavel.originalObject.firstLine
						else
							resp = n.tipoResponsavel
						break;
					case 'RESPONSAVEL':
						if (n.refResponsavel)
							resp = $scope
							.getResponsavelNome(n.refResponsavel)
						else
							resp = n.tipoResponsavel
						break;
					case 'PRINCIPAL_CADASTRANTE':
						resp = 'Cadastrante';
						break;
					case 'PRINCIPAL_LOTA_CADASTRANTE':
						resp = 'Lota. Cadastrante';
						break;
					case 'PRINCIPAL_TITULAR':
						resp = 'Titular';
						break;
					case 'PRINCIPAL_LOTA_TITULAR':
						resp = 'Lota. Titular';
						break;
					case 'PRINCIPAL_SUBSCRITOR':
						resp = 'Subscritor';
						break;
					case 'PRINCIPAL_LOTA_SUBSCRITOR':
						resp = 'Lota. Subscritor';
						break;
					case 'PRINCIPAL_DESTINATARIO':
						resp = 'Destinatário';
						break;
					case 'PRINCIPAL_LOTA_DESTINATARIO':
						resp = 'Lota. Destinatário';
						break;
					case 'PRINCIPAL_GESTOR':
						resp = 'Gestor';
						break;
					case 'PRINCIPAL_LOTA_GESTOR':
						resp = 'Lota. Gestor';
						break;
					case 'PRINCIPAL_FISCAL_TECNICO':
						resp = 'Fiscal Técnico';
						break;
					case 'PRINCIPAL_LOTA_FISCAL_TECNICO':
						resp = 'Lota. Fiscal Técnico';
						break;
					case 'PRINCIPAL_FISCAL_ADMINISTRATIVO':
						resp = 'Fiscal Adm.';
						break;
					case 'PRINCIPAL_LOTA_FISCAL_ADMINISTRATIVO':
						resp = 'Lota. Fiscal Adm.';
						break;
					case 'PRINCIPAL_INTERESSADO':
						resp = 'Interessado';
						break;
					case 'PRINCIPAL_LOTA_INTERESSADO':
						resp = 'Lota. Interessado';
						break;
					case 'PRINCIPAL_AUTORIZADOR':
						resp = 'Autorizador';
						break;
					case 'PRINCIPAL_LOTA_AUTORIZADOR':
						resp = 'Lota. Autorizador';
						break;
					case 'PRINCIPAL_REVISOR':
						resp = 'Revisor';
						break;
					case 'PRINCIPAL_LOTA_REVISOR':
						resp = 'Lota. Revisor';
						break;
					case 'PRINCIPAL_LIQUIDANTE':
						resp = 'Liquidante';
						break;
					case 'PRINCIPAL_LOTA_LIQUIDANTE':
						resp = 'Lota. Liquidante';
						break;
					case 'PROCEDIMENTO_TITULAR':
						resp = 'Proc. Titular';
						break;
					case 'PROCEDIMENTO_LOTA_TITULAR':
						resp = 'Proc. Lota. Titular';
						break;
					default:
						resp = n.tipoResponsavel
				}
				var s = '"' + n.id + '"[shape="' + shape + '"][label=<' + n.titulo + (resp ? "<br/><font point-size=\"10pt\">" + resp + "</font>" : "") + '>];';
				if (n.tipo !== "FIM") {
					if (n.desvio && n.desvio.length > 0) {
						for (var x in n.desvio) {
							var tr = n.desvio[x];
							if (tr.tarefa)
								s += '"' + n.id + '"->"' + tr.tarefa + '"';
							else if (nextn)
								s += '"' + n.id + '"->"' + nextn.id + '"';
							else
								s += '"' + n.id + '"->"fim"';
							if (tr.titulo && tr.titulo !== '')
								s += ' [label="' + tr.titulo + '"]';
							s += ';';
						}
					} else if (n.depois) {
						s += '"' + n.id + '"->"' + n.depois + '";';
					} else if (nextn) {
						s += '"' + n.id + '"->"' + nextn.id + '";';
					} else {
						s += '"' + n.id + '"->"fim";';
					}
				}
				return s;
			}

			$scope.getDot = function(wf) {
				var tipos = {
					INCLUIR_DOCUMENTO: 'note',
					FORMULARIO: 'rectangle',
					DECISAO: 'diamond',
					EMAIL: 'folder',
				};
				var s = 'digraph G { graph[size="3,3"];';
				if (wf.tarefa.length > 0) {
					s += graphElement("oval", {
						id: "ini",
						titulo: "Início",
						desvio: [{
							tarefa: wf.tarefa[0].id
						}]
					});
					s += graphElement("oval", {
						id: "fim",
						titulo: "Fim",
						tipo: "FIM"
					});
					for (var i = 0; i < wf.tarefa.length; i++) {
						var n = wf.tarefa[i];
						s += graphElement(
							tipos[n.tipo],
							n,
							i < wf.tarefa.length - 1 ? wf.tarefa[i + 1] : undefined);
					}
				}
				s += '}';
				return s;
			}

			$scope.getParameterByName = function(name, url) {
				if (!url)
					url = window.location.href;
				name = name.replace(/[\[\]]/g, '\\$&');
				var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
					results = regex
					.exec(url);
				if (!results)
					return;
				if (!results[2])
					return;
				return decodeURIComponent(results[2]
					.replace(/\+/g, ' '));
			}

			$scope.carregarRecursos(function() {
				$scope.id = $scope.getParameterByName('id');
				if ($scope.id) {
					$scope.carregar($scope.id);
				} else {
					$http({
						url: '/sigawf/app/diagrama/vazio',
						method: "GET"
					}).then(
						function(response) {
							$scope.data.workflow = $scope
								.decode(response.data);
						},
						function(response) {});
				}
			})
		});

var uuidv4 = function() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = Math.random() * 16 | 0,
			v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	});
};

var formdata = function(obj) {
	var s = "";
	var f = function(prefix, jsonObj) {
		if (typeof jsonObj == "object" && jsonObj !== null) {
			$.each(jsonObj, function(k, v) {
				if (k == "$$hashKey" || v === null || v === undefined)
					return;
				if (typeof v == "object" && v !== null) {
					var nextprefix = prefix;
					if (typeof k == "number") {
						if (prefix.endsWith(".")) {
							nextprefix = nextprefix.substring(0,
								nextprefix.length - 1);
						}
						nextprefix += "[" + k + "].";
					} else
						nextprefix += k + ".";
					f(nextprefix, v);
				} else if (typeof k == "number") {
					if (prefix.endsWith(".")) {
						nextprefix = prefix.substring(0, prefix.length - 1);
					}
					if (s != "")
						s = s + "&";
					s += nextprefix + "[" + k + "]=" + v;
				} else {
					if (s != "")
						s = s + "&";
					s = s + prefix + k + "=" + encodeURIComponent(v);
				}
			});
		}
	}

	f("", obj);

	// alert(s);

	return s;
}