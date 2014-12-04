var app = angular.module("wfDesignApp", []);

app.controller("wfDesignCtrl", function ($scope, $http, $sce) {

    $scope.event_type = [
        {label: "Ao entrar", name: "node-enter"},
        {label: "Ao sair", name:"node-leave"},
        {label: "Ao criar tarefa", name:"task-create"},
        {label: "Ao trocar o contexto", name:"context-change"}
    ];

    $scope.editar = function(obj) {
        $scope.editando = obj;
    }

    $scope.addRaia = function () {
        var obj = {name: ""};
        $scope.editar(obj);
        $scope.pd.swimlane.push(obj);
    }

    $scope.delRaia = function (st) {
        $scope.pd.swimlane.splice(st, 1);
    };

    $scope.addTarefa = function () {
        var obj = {
            "name": "",
            "task": {
                "controller": {
                    "variable": [
                    ]
                },
                "name": "",
                "swimlane": null
            },
            "transition": [
            ]
        };
        $scope.editar(obj);
        $scope.pd.task_node.push(obj);
    }

    $scope.delTarefa = function (st) {
        $scope.pd.task_node.splice(st, 1);
    };

    $scope.addNode = function () {
        var obj = {
            "name": "",
            "event": [
            ],
            "transition": [
            ]
        };
        $scope.editar(obj);
        $scope.pd.node.push(obj);
    }

    $scope.delNode = function (st) {
        $scope.pd.node.splice(st, 1);
    };

    $scope.addEndState = function () {
        var obj = {
            "name": "",
            "event": [
            ]
        };
        $scope.editar(obj);
        $scope.pd.end_state.push(obj);
    }

    $scope.delEndState = function (st) {
        $scope.pd.end_state.splice(st, 1);
    };

    $scope.addVariavel = function (tarefa) {
        tarefa.task.controller.variable.push({name: ""});
    }

    $scope.delVariavel = function (tarefa, st) {
        tarefa.task.controller.variable.splice(st, 1);
    };

    $scope.addEvento = function (tarefa) {
        if (typeof tarefa.event === "undefined")
            tarefa.event = [];
        tarefa.event.push({name: ""});
    }

    $scope.delEvento = function (tarefa, st) {
        tarefa.event.splice(st, 1);
    };

    $scope.addTransicao = function (tarefa) {
        tarefa.transition.push({name: ""});
    }

    $scope.delTransicao = function (tarefa, st) {
        tarefa.transition.splice(st, 1);
    };

    $scope.addTransicaoEmail = function (transicao) {
        if (transicao.mail == null)
            transicao.mail = [];
        transicao.mail.push({});
    }

    $scope.delTransicaoEmail = function (transicao, st) {
        transicao.mail.splice(st, 1);
    };
    
    $scope.carregar = function (name) {
	    $http.get('processdefinition/' + name).success(function (xml) {
	        //$scope.pd = $.xml2json(xml);
	        $scope.json = JSON.parse(xml2json(parseXml(xml), " "));
	        $scope.pd = $scope.json['process-definition'];
	        $scope.xmlOriginal = xml;
	        $scope.jsonToShow = $scope.json;
	        
	        posLoad($scope.pd)
	    }).error(function (data, status, headers, config) {
	            $scope.pd = defaultprocessdefinition;
	    });
    }

    $scope.salvar = function(name) {
        var obj = jQuery.extend(true, {}, $scope.json);
        trimStrings(obj);
        restoreSwimlanes(obj);
        restoreEventTypes(obj);
        restoreTransitions(obj);
        removeHashKeys(obj);
        restorePropertyNames(obj);
//        $scope.jsonToShow = obj;
        $scope.xml = json2xml(obj, " ");
        
	    $http.post('processdefinition/' + name, 
	    		$.param({procedimento: name, xml: $scope.xml}), 
	    		{headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function (res) {
	    	window.alert(res);
	    }).error(function (data, status, headers, config) {
	    	window.alert(res);
	    });
    }


    
    //
    // Pre-save
    //
    function restorePropertyNames(obj) {
        for (var x in obj) {
            if (typeof obj[x] == 'object') {
                restorePropertyNames(obj[x]);
            }
            var newx = x.replace(/__text/,"#text").replace(/^_/, "@").replace(/_/, "-");
            replacePropertyName(obj, x, newx);
        }
    }

    function restoreSwimlanes(obj) {
        for (var x in obj) {
            if (typeof obj[x] == 'object' && x == "_swimlane") {
                obj[x] = obj[x]._name;
            }
            if (typeof obj[x] == 'object') {
                restoreSwimlanes(obj[x]);
            }
        }
    }

    function restoreTransitions(obj) {
        var ns = $scope.nodes();
        for (var x in obj) {
            if (typeof obj[x] == 'object' && x == "_to") {
                obj[x] = obj[x]._name;
            } else if (typeof obj[x] == 'object') {
                restoreTransitions(obj[x]);
            }
        }
    }

    function restoreEventTypes(obj) {
        var ns = $scope.event_type;
        for (var x in obj) {
            if (typeof obj[x] == 'object' && x == "_type") {
                obj[x] = obj[x].name;
            } else if (typeof obj[x] == 'object') {
                restoreEventTypes(obj[x]);
            }
        }
    }

    function removeHashKeys(obj) {
        var ns = $scope.event_type;
        for (var x in obj) {
            if (x == "$$hashKey") {
                delete obj[x];
            } else if (typeof obj[x] == 'object') {
                removeHashKeys(obj[x]);
            }
        }
    }


    //
    // Pos-Load
    //
    function posLoad(obj) {
        trimStrings(obj);
        replacePropertyNames(obj);
        assureArrays(obj);
        replaceSwimlanes(obj);
        replaceEventTypes(obj);
        replaceTransitions(obj);
    }

    function replacePropertyName(obj, name, newname) {
        if (name == newname)
            return;
        obj[newname] = obj[name];
        delete obj[name];
    }

    function replacePropertyNames(obj) {
        for (var x in obj) {
            if (typeof obj[x] == 'object') {
                replacePropertyNames(obj[x]);
            }
            var newx = x.replace(/^@/, "_").replace(/-/, "_").replace(/#text/,"__text");
            replacePropertyName(obj, x, newx);
        }
    }

    function trimStrings(obj) {
        for (var x in obj) {
            if (typeof obj[x] == 'string') {
                obj[x] = obj[x].replace(/^\s+|\s+$/g,"");
            }
            if (typeof obj[x] == 'object') {
                trimStrings(obj[x]);
            }
        }
    }



    function assureArrays(obj) {
        for (var x in obj) {
            if (!(obj[x] instanceof Array) && $.inArray(x, ['swimlane', 'transition', 'variable', 'mail', 'node', 'decision', 'mail_node', 'end_state', 'start_state', 'event']) != -1 && typeof obj[x] == 'object')
                obj[x] = [obj[x]];
            if (typeof obj[x] == 'object') {
                assureArrays(obj[x]);
            }
        }
    }

    function replaceSwimlanes(obj) {
        for (var x in obj) {
            if (typeof obj[x] == 'string' && x == "_swimlane") {
                for (var s in $scope.pd.swimlane) {
                    if ($scope.pd.swimlane[s]._name == obj[x]) {
                        obj[x] = $scope.pd.swimlane[s];
                        break;
                    }
                }
            }
            if (typeof obj[x] == 'object') {
                replaceSwimlanes(obj[x]);
            }
        }
    }

    function replaceTransitions(obj) {
        var ns = $scope.nodes();
        for (var x in obj) {
            if (typeof obj[x] == 'string' && x == "_to") {
                var replaced = false;
                for (var s in ns) {
                    if (ns[s]._name == obj[x]) {
                        obj[x] = ns[s];
                        replaced = true;
                        break;
                    }
                }
                if (!replaced)
                    alert("Destino de transição não encontrado: " + obj[x]);
            } else if (typeof obj[x] == 'object') {
                replaceTransitions(obj[x]);
            }
        }
    }

    function replaceEventTypes(obj) {
        var ns = $scope.event_type;
        for (var x in obj) {
            if (typeof obj[x] == 'string' && x == "_type") {
                var replaced = false;
                for (var s in ns) {
                    if (ns[s].name == obj[x]) {
                        obj[x] = ns[s];
                        replaced = true;
                        break;
                    }
                }
                if (!replaced)
                    alert("Tipo de evento não encontrado: " + obj[x]);
            } else if (typeof obj[x] == 'object') {
                replaceEventTypes(obj[x]);
            }
        }
    }


    //
    // Validate
    //
    function validate(pd) {

    }


    //
    // Pre-persist
    //
    function prePersist(pd) {
        copyTaskNodeNamesToTaks(pd);
    }

    function copyTaskNodeNamesToTaks(obj) {
        for (var s in obj.task_node) {
            obj.task_node[s].task.name = obj.task_node[s].name;
        }
    }


    $scope.nodes = function () {
        var ns = [];
        for (var s in $scope.pd.task_node) {
            ns.push($scope.pd.task_node[s]);
        }
        for (var s in $scope.pd.node) {
            ns.push($scope.pd.node[s]);
        }
        for (var s in $scope.pd.mail_node) {
            ns.push($scope.pd.mail_node[s]);
        }
        for (var s in $scope.pd.decision) {
            ns.push($scope.pd.decision[s]);
        }
        for (var s in $scope.pd.fork) {
            ns.push($scope.pd.fork[s]);
        }
        for (var s in $scope.pd.join) {
            ns.push($scope.pd.join[s]);
        }
        for (var s in $scope.pd.end_state) {
            ns.push($scope.pd.end_state[s]);
        }
        return ns;
    }

    //digraph G {
    //    graph[size="100,100", rankdir="LR"];
    //    node[shape=rectangle]; "Início"[shape="oval"][label=<<font>Início</font>>][color="red"]; "Início"->"Precisa Autorizar"; "Dividir Tarefas"[shape="trapezium"][label=<<font>Dividir Tarefas</font>>]; "Dividir Tarefas"->"Reservar Passagens"; "Dividir Tarefas"->"Reservar Diárias"; "Dividir Tarefas"->"Reservar Verba"; "Dividir Tarefas"->"Agendar Veículo"; "Agrupar Tarefas"[shape="invtrapezium"][label=<<font>Agrupar Tarefas</font>>]; "Agrupar Tarefas"->"Aprovar Ausência"; "Agendar Veículo"[shape="rectangle"][label=<<font>Agendar Veículo</font><br/><font point-size="9" color="gray">RJSESIA</font>>]; "Agendar Veículo"->"Agrupar Tarefas"; "Reservar Passagens"[shape="rectangle"][label=<<font>Reservar Passagens</font><br/><font point-size="9" color="gray">RJ13635</font>>]; "Reservar Passagens"->"Agrupar Tarefas"; "Reservar Diárias"[shape="rectangle"][label=<<font>Reservar Diárias</font><br/><font point-size="9" color="gray">RJCSIS</font>>]; "Reservar Diárias"->"Agrupar Tarefas"; "Reservar Verba"[shape="rectangle"][label=<<font>Reservar Verba</font><br/><font point-size="9" color="gray">RJ13635</font>>]; "Reservar Verba"->"Agrupar Tarefas"; "Autorizar Servidor"[shape="rectangle"][label=<<font>Autorizar Servidor</font><br/><font point-size="9" color="gray">RJ13635</font>>]; "Autorizar Servidor"->"Dividir Tarefas"[edgetooltip="Sim"]; "Autorizar Servidor"->"Arquivar"[edgetooltip="Não"]; "Aprovar Ausência"[shape="rectangle"][label=<<font>Aprovar Ausência</font><br/><font point-size="9" color="gray">RJ13635</font>>]; "Aprovar Ausência"->"fork1"[edgetooltip="Sim"]; "Aprovar Ausência"->"fork2"[edgetooltip="Não"]; "fork1"[shape="trapezium"][label=<<font>fork1</font>>]; "fork1"->"Comprar Passagens"; "fork1"->"Pagar Diária"; "fork1"->"Empenhar"; "fork1"->"Notificar RH"; "join1"[shape="invtrapezium"][label=<<font>join1</font>>]; "join1"->"Arquivar"; "Comprar Passagens"[shape="rectangle"][label=<<font>Comprar Passagens</font><br/><font point-size="9" color="gray">RJ13635</font>>]; "Comprar Passagens"->"join1"; "Pagar Diária"[shape="rectangle"][label=<<font>Pagar Diária</font><br/><font point-size="9" color="gray">RJCSIS</font>>]; "Pagar Diária"->"join1"; "Empenhar"[shape="rectangle"][label=<<font>Empenhar</font><br/><font point-size="9" color="gray">RJ13635</font>>]; "Empenhar"->"join1"; "Arquivar"[shape="octagon"][label=<<font>Arquivar</font>>]; "Arquivar"->"Fim"; "fork2"[shape="trapezium"][label=<<font>fork2</font>>]; "fork2"->"Liberar Passagens"; "fork2"->"Liberar Diárias"; "fork2"->"Liberar Verba"; "fork2"->"Liberar Veículo"; "join2"[shape="invtrapezium"][label=<<font>join2</font>>]; "join2"->"Arquivar"; "Liberar Passagens"[shape="rectangle"][label=<<font>Liberar Passagens</font><br/><font point-size="9" color="gray">RJ13635</font>>]; "Liberar Passagens"->"join2"; "Liberar Diárias"[shape="rectangle"][label=<<font>Liberar Diárias</font><br/><font point-size="9" color="gray">RJCSIS</font>>]; "Liberar Diárias"->"join2"; "Liberar Verba"[shape="rectangle"][label=<<font>Liberar Verba</font><br/><font point-size="9" color="gray">RJ13635</font>>]; "Liberar Verba"->"join2"; "Liberar Veículo"[shape="rectangle"][label=<<font>Liberar Veículo</font><br/><font point-size="9" color="gray">RJSESIA</font>>]; "Liberar Veículo"->"join2"; "Precisa Autorizar"[shape="diamond"][label=<<font>Precisa Autorizar</font>>]; "Precisa Autorizar"->"Cargo"[edgetooltip="Sim"]; "Precisa Autorizar"->"Dividir Tarefas"[edgetooltip="Não"]; "Autorizar Magistrado"[shape="rectangle"][label=<<font>Autorizar Magistrado</font><br/><font point-size="9" color="gray">RJ13635</font>>]; "Autorizar Magistrado"->"Dividir Tarefas"[edgetooltip="Sim"]; "Autorizar Magistrado"->"Arquivar"[edgetooltip="Não"]; "Cargo"[shape="diamond"][label=<<font>Cargo</font>>]; "Cargo"->"Autorizar Servidor"[edgetooltip="Servidor"]; "Cargo"->"Autorizar Magistrado"[edgetooltip="Magistrado"]; "Notificar RH"[shape="note"][label=<<font>Notificar RH</font>>]; "Notificar RH"->"join1"; "Fim"[shape="oval"][label=<<font>Fim</font>>];
    //    }

    function graphElement(name, shape, n) {
        var s = '"' + name + '"[shape="' + shape + '"][label=<<font>' + name + '</font>>];';
        for (var x in n.transition) {
            var tr = n.transition[x];
            if (typeof tr._to !== "undefined")
                s += '"' + n._name + '"->"' + tr._to._name + '";';
        }
        return s;
    }

    $scope.getSmallGraphSource = function () {
        if ($scope.pd == null)
            return "";
        var s = 'digraph G { graph[size="3,3"];';
        s += graphElement($scope.pd.start_state._name, "oval", $scope.pd.start_state);
        for (var x in $scope.pd.task_node) {
            var n = $scope.pd.task_node[x];
            s += graphElement(n._name, "rectangle", n);
        }
        for (var x in $scope.pd.fork) {
            var n = $scope.pd.fork[x];
            s += graphElement(n._name, "trapezium", n);
        }
        for (var x in $scope.pd.join) {
            var n = $scope.pd.join[x];
            s += graphElement(n._name, "invtrapezium", n);
        }
        for (var x in $scope.pd.node) {
            var n = $scope.pd.node[x];
            s += graphElement(n._name, "octagon", n);
        }
        for (var x in $scope.pd.decision) {
            var n = $scope.pd.decision[x];
            s += graphElement(n._name, "diamond", n);
        }
        for (var x in $scope.pd.mail_node) {
            var n = $scope.pd.mail_node[x];
            s += graphElement(n._name, "note", n);
        }
        for (var x in $scope.pd.end_state) {
            var n = $scope.pd.end_state[x];
            s += graphElement(n._name, "oval", n);
        }

        s += '}';
        return s;
    }

    $scope.getSmallGraph = function () {
        var input = $scope.getSmallGraphSource();
        var format = "svg";
        var engine = "dot";

        var result = Viz(input, format, engine);

        if (format == "svg") {
            return $sce.trustAsHtml(result);
        } else {
            return "";
        }
    }

    $scope.atualizarSmallGraph = function () {
        $scope.smallgraph = $scope.getSmallGraph();
    }

    $scope.getPD = function () {
        return $sce.trustAsHtml(angular.toJson($scope.pd, true));
    }


})
;
