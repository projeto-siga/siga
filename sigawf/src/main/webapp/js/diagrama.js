var app = angular.module('app', [ 'angucomplete-alt' ]);

// Create an AngularJS service called debounce
app.factory('debounce', [ '$timeout', '$q', function($timeout, $q) {
	// The service is actually this function, which we call with the func
	// that should be debounced and how long to wait in between calls
	return function debounce(func, wait, immediate) {
		var timeout;
		// Create a deferred object that will be resolved when we need to
		// actually call the func
		var deferred = $q.defer();
		return function() {
			var context = this, args = arguments;
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
} ]);

app
		.controller(
				'ctrl',
				function($scope, $http, debounce) {
					$scope.data = {};

					$scope.encode = function(d) {
						var pd = {
							hisIde : undefined,
							nome : d.nome,
							descr : d.descricao,
							definicaoDeTarefa : []
						};
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
								td.seguinteIde = t.depois;
								td.ultimo = t.ultimo;
								td.ordem = i;
								td.definicaoDeVariavel = [];
								td.definicaoDeDesvio = [];

								if (t.tipoResponsavel == 'LOTACAO'
										&& t.refUnidadeResponsavel
										&& t.refUnidadeResponsavel.originalObject
										&& t.refUnidadeResponsavel.originalObject.key)
									td.lotacaoId = t.refUnidadeResponsavel.originalObject.key;

								if (t.tipoResponsavel == 'PESSOA'
										&& t.refPessoaResponsavel
										&& t.refPessoaResponsavel.originalObject
										&& t.refPessoaResponsavel.originalObject.key)
									td.pessoaId = t.refPessoaResponsavel.originalObject.key;

								if (t.tipoResponsavel == 'RESPONSAVEL')
									td.definicaoDeResponsavelId = t.refResponsavel;

								if (t.tipo == 'INCLUIR_DOCUMENTO' && t.ref
										&& t.ref.originalObject
										&& t.ref.originalObject.key) {
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
										dd.seguinteIde = desvio.tarefa === "fim" ? undefined
												: desvio.tarefa;
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

					$scope.gravar = function() {
						var fd = formdata({
							id : $scope.id,
							pd : $scope.encode($scope.data.workflow)
						});
						$http(
								{
									url : '/sigawf/app/diagrama/gravar',
									method : "POST",
									data : fd,
									headers : {
										'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
									}
								}).then(function(response) {
							window.location = '/sigawf/app/diagrama/listar'
						}, function(response) {
						});

					}

					$scope.decode = function(d) {
						var pd = {
							hisIde : undefined,
							nome : d.nome,
							descricao : d.descr,
							tarefa : []
						};
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
								td.depois = t.seguinteIde;
								td.ultimo = t.ultimo;
								td.ordem = i;
								td.variavel = [];
								td.desvio = [];

								if (t.lotacao)
									td.refUnidadeResponsavel = {
										originalObject : t.lotacao
									}
								if (t.pessoa)
									td.refPessoaResponsavel = {
										originalObject : t.pessoa
									}

								if (t.definicaoDeResponsavel)
									td.refResponsavel = t.definicaoDeResponsavel;

								if (t.refId) {
									td.ref = {
										originalObject : {
											key : t.refId,
											firstLine : t.refSigla
										}
									}
								}

								console.log("TESTE!@#", td);

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
										dd.tarefa = desvio.ultimo ? "fim"
												: desvio.seguinteIde;
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
							url : '/sigawf/app/diagrama/' + id + '/carregar',
							method : "GET"
						}).then(
								function(response) {
									$scope.data.workflow = $scope
											.decode(response.data);
								}, function(response) {
								});

					}

					$scope.carregarResponsaveis = function(cont) {
						$http({
							url : '/sigawf/app/responsavel/carregar',
							method : "GET"
						}).then(function(response) {
							$scope.responsaveis = response.data.list;
							if (cont)
								cont();
						}, function(response) {
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
										if (!$scope.data
												|| !$scope.data.workflow
												|| !$scope.data.workflow.tarefa) {
											if ($scope.data
													&& $scope.data.workflow)
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
											t.nome = i
													+ 1
													+ ") "
													+ (t.titulo ? t.titulo
															: t.tipo);
											tarefas.push({
												id : t.id,
												nome : t.nome
											})
										}
										tarefas.push({
											id : "fim",
											nome : "[Fim]"
										})

										$scope.tarefas = tarefas;
										console.log('graphDrawDebounced')
										$scope.graphDrawDebounced();
									}, true);

					$scope.graphDraw = function() {
						console.log('graphDraw')
						$scope.dot = $scope.getDot($scope.data.workflow);
						$http(
								{
									url : 'https://hml.xrp.com.br/app/graphviz?graph='
											+ $scope.dot,
									method : "GET"
								})
								.then(
										function(response) {
											document
													.getElementById('graph-workflow').innerHTML = response.data;
										}, function(response) {
											$scope.showError(response);
										});
					}

					$scope.graphDrawDebounced = debounce($scope.graphDraw,
							1000, false);

					function graphElement(shape, n, nextn) {
						var s = '"' + n.id + '"[shape="' + shape
								+ '"][label=<<font>' + n.titulo + '</font>>];';
						if (n.tipo !== "FIM") {
							if (n.desvio && n.desvio.length > 0) {
								for ( var x in n.desvio) {
									var tr = n.desvio[x];
									if (tr.tarefa)
										s += '"' + n.id + '"->"' + tr.tarefa
												+ '"';
									else if (nextn)
										s += '"' + n.id + '"->"' + nextn.id
												+ '"';
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
							INCLUIR_DOCUMENTO : 'note',
							FORMULARIO : 'rectangle',
							DECISAO : 'diamond',
							EMAIL : 'folder',
						};
						var s = 'digraph G { graph[size="3,3"];';
						if (wf.tarefa.length > 0) {
							s += graphElement("oval", {
								id : "ini",
								titulo : "Início",
								desvio : [ {
									tarefa : wf.tarefa[0].id
								} ]
							});
							s += graphElement("oval", {
								id : "fim",
								titulo : "Fim",
								tipo : "FIM"
							});
							for (var i = 0; i < wf.tarefa.length; i++) {
								var n = wf.tarefa[i];
								s += graphElement(
										tipos[n.tipo],
										n,
										i < wf.tarefa.length - 1 ? wf.tarefa[i + 1]
												: undefined);
							}
						}
						s += '}';
						return s;
					}

					$scope.getParameterByName = function(name, url) {
						if (!url)
							url = window.location.href;
						name = name.replace(/[\[\]]/g, '\\$&');
						var regex = new RegExp('[?&]' + name
								+ '(=([^&#]*)|&|#|$)'), results = regex
								.exec(url);
						if (!results)
							return;
						if (!results[2])
							return;
						return decodeURIComponent(results[2]
								.replace(/\+/g, ' '));
					}

					$scope.carregarResponsaveis(function() {
						$scope.id = $scope.getParameterByName('id');
						if ($scope.id) {
							$scope.carregar($scope.id);
						}
					})
				});

var uuidv4 = function() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
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