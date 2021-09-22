<%@ include file="/WEB-INF/page/include.jsp"%>

<siga:pagina titulo="Edição de Procedimento">

	<style type="text/css">
		table {
			border-collapse: collapse;
			margin-top: 10px;
			width: 100%;
			font-family: 'Bitstream Vera Sans Mono', Courier, monospace;
			border: 1px solid #CCC;
		}
		
		thead tr {
			font-weight: normal;
			text-aling: left;
			height: 2em;
			border-bottom: 1px solid #DDD;
			background-color: #ECECEC;
			background: -webkit-gradient(linear, left top, left bottom, from(#FAFAFA),
				to(#ECECEC));
			background: -moz-linear-gradient(top, #FAFAFA, #ECECEC);
			filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#FAFAFA',
				endColorstr='#ECECEC');
		}
		
		td {
			padding: 1px 2px;
			white-space: pre;
		}
		
		td.bredecode {
			width: 100%;
			padding-left: 4px;
		}
		
		td.codekolom {
			text-align: right;
			min-width: 3em;
			background-color: #ECECEC;
			border-right: 1px solid #DDD;
			color: #AAA;
		}
		
		tr.add {
			background: #DFD;
		}
		
		tr.del {
			background: #FDD;
		}
	</style>

	<div class="gt-bd gt-cols clearfix" ng-app="wfDesignApp"
		ng-controller="wfDesignCtrl" ng-init="carregar('${procedimento}')">
		<div class="gt-content clearfix" ng-hide="exibirXml">

			<h2 class="gt-form-head">Definição de Procedimento</h2>



			<div class="gt-form gt-content-box"
				style="margin-bottom: 10px !important;">

				<div class="gt-form-row gt-width-100">
					<div class="gt-left-col gt-width-33">
						<label>Nome do procedimento</label> <input type="text"
							ng-model="pd._name" class="gt-form-text">
					</div>

				</div>
			</div>
			<div class="gt-form-row">
				<input type="button" class="gt-btn-medium gt-btn-left"
					value="Salvar" ng-click="salvar('${procedimento}')" /> <input
					type="button" class="gt-btn-medium" value="Editar XML"
					ng-click="editarXml('${procedimento}')" />
			</div>

			<%--

			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Xml</h2>
			<pre>{{xmlOriginal}}</pre>

			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Xml</h2>
			<pre>{{xml}}</pre>
 --%>

			<!-- Raias -->
			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Raias</h2>

			<div class="gt-form gt-content-box"
				style="margin-bottom: 10px !important;"
				ng-repeat="raia in pd.swimlane">
				<div ng-hide="editando === raia">
					<label style="float: right; margin-left: 1em;"><a
						ng-click="editar(raia)"><img
							src="/siga/css/famfamfam/icons/pencil.png"
							style="margin-right: 5px;"><span
							style="vertical-align: top;">Editar</span></a></label>

					<p>{{pd.swimlane[$index]._name}}</p>
				</div>
				<div class="gt-form-row gt-width-100" ng-show="editando === raia">
					<div class="gt-left-col gt-width-33">
						<label>Nome da raia {{raia.nome}}</label> <input type="text"
							ng-model="pd.swimlane[$index]._name" class="gt-form-text" />
					</div>
					<div class="gt-right-col gt-width-33">
						<label style="float: right;"><a ng-click="delRaia($index)">
								<img src="/siga/css/famfamfam/icons/delete.png"
								style="margin-right: 5px;"><span
								style="vertical-align: top;">Raia</span>
						</a></label>
					</div>
				</div>
			</div>
			<div class="gt-form-row">
				<input type="button" ng-click="addRaia()" class="gt-btn-medium"
					value="Adicionar Raia" />
			</div>


			<!-- Start State -->
			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Início</h2>

			<div id="start-state-container" class="gt-form gt-content-box"
				style="margin-bottom: 10px !important;"
				ng-repeat="node in pd.start_state">{{node._name}}</div>


			<!-- Tarefas -->
			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Tarefas</h2>

			<div id="task-node-container" class="gt-form gt-content-box"
				style="margin-bottom: 10px !important;"
				ng-repeat="node in pd.task_node">
				<div ng-hide="editando === node">
					<label style="float: right; margin-left: 1em;"><a
						ng-click="editar(node)"><img
							src="/siga/css/famfamfam/icons/pencil.png"
							style="margin-right: 5px;"><span
							style="vertical-align: top;">Editar</span></a></label>

					<p>{{node._name}}</p>
				</div>
				<div ng-show="editando === node">
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-25">
							<label>Nome</label> <input type="text" ng-model="node._name"
								class="gt-form-text">
						</div>
						<div class="gt-left-col gt-width-25">
							<label>Raia</label> <select ng-model="node.task.swimlane"
								ng-options="raia._name for raia in pd.swimlane">
								<option value="">[Nenhuma]</option>
							</select>
						</div>
						<div class="gt-left-col gt-width-25">
							<label>Descrição</label> <input type="text"
								ng-model="node.task.description" class="gt-form-text">
						</div>
						<div class="gt-right-col gt-width-25">
							<label style="float: right; margin-left: 1em;"><a
								ng-click="delTarefa($index)"><img
									src="/siga/css/famfamfam/icons/delete.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Tarefa</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addTransicao(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Transição</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addEvento(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Evento</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addVariavel(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Variável</span></a></label>
						</div>
					</div>

					<!-- Variáveis -->
					<div class="gt-form-row gt-width-100"
						ng-show="node.task.controller.variable != null && node.task.controller.variable.length > 0">
						<div class="gt-left-col gt-width-25">
							<label>Variáveis</label>
						</div>
						<div class="gt-right-col gt-width-75">
							<div
								style="border-bottom: 1px dotted #000000; margin-bottom: 10px;"
								ng-repeat="variavel in node.task.controller.variable">
								<div class="gt-form-row gt-width-100">
									<div class="gt-left-col gt-width-25">
										<label ng-show="$first">Nome</label> <input type="text"
											ng-model="node.task.controller.variable[$index]._name"
											class="gt-form-text">
									</div>
									<div class="gt-left-col gt-width-25">
										<label ng-show="$first">Identificador</label> <input
											type="text"
											ng-model="node.task.controller.variable[$index]._mapped_name"
											class="gt-form-text">
									</div>
									<div class="gt-left-col gt-width-25">
										<label ng-show="$first">Tipo</label> <input type="text"
											ng-model="node.task.controller.variable[$index]._access"
											class="gt-form-text">
									</div>
									<div class="gt-left-col gt-width-25">
										<label style="float: right; margin-left: 1em;"><a
											ng-click="delVariavel(node, $index)"><img
												src="/siga/css/famfamfam/icons/delete.png"
												style="margin-right: 5px;"><span
												style="vertical-align: top;">Variável</span></a></label>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Eventos -->
					<div id="event-container" class="gt-form-row gt-width-100"
						ng-show="node.event != null && node.event.length > 0">
						<div class="gt-left-col gt-width-25">
							<label>Eventos</label>
						</div>
						<div class="gt-right-col gt-width-75">
							<div ng-repeat="evento in node.event">
								<div class="gt-form-row gt-width-100">
									<div class="gt-left-col gt-width-25">
										<label>Tipo</label> <select ng-model="evento._type"
											ng-options="et.label for et in event_type">
											<option value="">[Nenhuma]</option>
										</select>
									</div>
									<div class="gt-right-col gt-width-25">
										<label style="float: right; margin-left: 1em;"><a
											ng-click="delEvento(node, $index)"><img
												src="/siga/css/famfamfam/icons/delete.png"
												style="margin-right: 5px;"><span
												style="vertical-align: top;">Evento</span></a></label>
									</div>
									<div class="gt-left-col gt-width-100">
										<label>Action</label> <input type="text"
											ng-model="evento.action._name" class="gt-form-text">
										<input type="text" ng-model="evento.action._class"
											class="gt-form-text">
									</div>
									<div class="gt-left-col gt-width-100">
										<label>Script</label> <input type="text"
											ng-model="evento.script._name" class="gt-form-text">
										<textarea ng-model="evento.script.__text" rows="5"
											class="gt-form-text"></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Transições -->
					<div id="transition-container" class="gt-form-row gt-width-100"
						ng-show="node.transition != null && node.transition.length > 0">
						<div class="gt-left-col gt-width-25">
							<label>Transições</label>
						</div>
						<div class="gt-right-col gt-width-75">
							<div ng-repeat="transicao in node.transition">
								<div class="gt-form-row gt-width-100">
									<div class="gt-left-col gt-width-25">
										<label>Nome</label> <input type="text"
											ng-model="node.transition[$index]._name" class="gt-form-text">
									</div>
									<div class="gt-left-col gt-width-25">
										<label>Destino</label> <select
											ng-model="node.transition[$index]._to"
											ng-options="node._name for node in nodes()" required="true"></select>
									</div>
									<div class="gt-left-col gt-width-25">
										<label>Condição</label> <input type="text"
											ng-model="node.transition[$index]._condition"
											class="gt-form-text">
									</div>
									<div class="gt-right-col gt-width-25">
										<label style="float: right; margin-left: 1em;"><a
											ng-click="delTransicao(node, $index)"><img
												src="/siga/css/famfamfam/icons/delete.png"
												style="margin-right: 5px;"><span
												style="vertical-align: top;">Transição</span></a></label> <label
											style="float: right; margin-left: 1em;"><a
											ng-click="addTransicaoEmail(transicao)"><img
												src="/siga/css/famfamfam/icons/add.png"
												style="margin-right: 5px;"><span
												style="vertical-align: top;">Email</span></a></label>
									</div>
								</div>

								<!-- Transição - Emails -->
								<div class="gt-form-row gt-width-100"
									ng-show="transicao.mail != null && transicao.mail.length > 0">
									<div class="gt-left-col gt-width-25">
										<label>Emails</label>
									</div>
									<div class="gt-right-col gt-width-75">
										<div
											style="border-bottom: 1px dotted #000000; margin-bottom: 10px;"
											ng-repeat="email in transicao.mail">
											<div class="gt-form-row gt-width-100">
												<div class="gt-left-col gt-width-25">
													<label>Assunto</label> <input
														ng-model="transicao.mail[$index]._subject"
														class="gt-form-text">
												</div>
												<div class="gt-left-col gt-width-25">
													<label>Destinatário(s)</label> <input
														ng-model="transicao.mail[$index]._actors"
														class="gt-form-text">
												</div>
												<div class="gt-right-col gt-width-25">
													<label style="float: right; margin-left: 1em;"><a
														ng-click="delTransicaoEmail(transicao, $index)"><img
															src="/siga/css/famfamfam/icons/delete.png"
															style="margin-right: 5px;"><span
															style="vertical-align: top;">Email</span></a></label>
												</div>
											</div>
											<div class="gt-form-row gt-width-100">
												<div class="gt-left-col gt-width-100">
													<label>Texto</label>
													<textarea ng-model="transicao.mail[$index]._text"
														class="gt-form-text"></textarea>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="gt-form-row">
				<input type="button" ng-click="addTarefa()" class="gt-btn-medium"
					value="Adicionar Tarefa" />
			</div>


			<!-- Node -->
			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Nós</h2>

			<div id="node-container" class="gt-form gt-content-box"
				style="margin-bottom: 10px !important;" ng-repeat="node in pd.node">
				<div ng-hide="editando === node">
					<label style="float: right; margin-left: 1em;"><a
						ng-click="editar(node)"><img
							src="/siga/css/famfamfam/icons/pencil.png"
							style="margin-right: 5px;"><span
							style="vertical-align: top;">Editar</span></a></label>
					<p>{{node._name}}</p>
				</div>
				<div ng-show="editando === node">
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-25">
							<label>Nome</label> <input type="text" ng-model="node._name"
								class="gt-form-text">
						</div>
						<div class="gt-right-col gt-width-75">
							<label style="float: right; margin-left: 1em;"><a
								ng-click="delNode($index)"><img
									src="/siga/css/famfamfam/icons/delete.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Nó</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addEvento(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Evento</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addTransicao(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Transição</span></a></label>
						</div>
					</div>

					<div id="node-event-container" class="gt-form-row gt-width-100"
						ng-show="node.event != null && node.event.length > 0"></div>
					<div id="node-transition-container"
						class="gt-form-row gt-width-100"
						ng-show="node.transition != null && node.transition.length > 0"></div>
				</div>

			</div>

			<div class="gt-form-row">
				<input type="button" ng-click="addNode()" class="gt-btn-medium"
					value="Adicionar Nó" />
			</div>


			<!-- Mail Node -->
			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Email</h2>

			<div id="mail-node-container" class="gt-form gt-content-box"
				style="margin-bottom: 10px !important;"
				ng-repeat="node in pd.mail_node">
				<div ng-hide="editando === node">
					<label style="float: right; margin-left: 1em;"><a
						ng-click="editar(node)"><img
							src="/siga/css/famfamfam/icons/pencil.png"
							style="margin-right: 5px;"><span
							style="vertical-align: top;">Editar</span></a></label>
					<p>{{node._name}}</p>
				</div>
				<div ng-show="editando === node">
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-25">
							<label>Nome</label> <input type="text" ng-model="node._name"
								class="gt-form-text">
						</div>
						<div class="gt-right-col gt-width-75">
							<label style="float: right; margin-left: 1em;"><a
								ng-click="delMailNode($index)"><img
									src="/siga/css/famfamfam/icons/delete.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Nó</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addEvento(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Evento</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addTransicao(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Transição</span></a></label>
						</div>
					</div>

					<div id="mail-node-event-container"
						class="gt-form-row gt-width-100"
						ng-show="node.event != null && node.event.length > 0"></div>
					<div id="mail-node-transition-container"
						class="gt-form-row gt-width-100"
						ng-show="node.transition != null && node.transition.length > 0"></div>
				</div>

			</div>

			<div class="gt-form-row">
				<input type="button" ng-click="addMailNode()" class="gt-btn-medium"
					value="Adicionar Email" />
			</div>


			<!-- Fork -->
			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Forks</h2>

			<div id="fork-container" class="gt-form gt-content-box"
				style="margin-bottom: 10px !important;" ng-repeat="node in pd.fork">
				<div ng-hide="editando === node">
					<label style="float: right; margin-left: 1em;"><a
						ng-click="editar(node)"><img
							src="/siga/css/famfamfam/icons/pencil.png"
							style="margin-right: 5px;"><span
							style="vertical-align: top;">Editar</span></a></label>
					<p>{{node._name}}</p>
				</div>
				<div ng-show="editando === node">
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-25">
							<label>Nome</label> <input type="text" ng-model="node._name"
								class="gt-form-text">
						</div>
						<div class="gt-right-col gt-width-75">
							<label style="float: right; margin-left: 1em;"><a
								ng-click="delFork($index)"><img
									src="/siga/css/famfamfam/icons/delete.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Nó</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addEvento(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Evento</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addTransicao(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Transição</span></a></label>
						</div>
					</div>

					<div id="fork-event-container" class="gt-form-row gt-width-100"
						ng-show="node.event != null && node.event.length > 0"></div>
					<div id="fork-transition-container"
						class="gt-form-row gt-width-100"
						ng-show="node.transition != null && node.transition.length > 0"></div>
				</div>

			</div>

			<div class="gt-form-row">
				<input type="button" ng-click="addFork()" class="gt-btn-medium"
					value="Adicionar Fork" />
			</div>


			<!-- Join -->
			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Joins</h2>

			<div id="join-container" class="gt-form gt-content-box"
				style="margin-bottom: 10px !important;" ng-repeat="node in pd.join">
				<div ng-hide="editando === node">
					<label style="float: right; margin-left: 1em;"><a
						ng-click="editar(node)"><img
							src="/siga/css/famfamfam/icons/pencil.png"
							style="margin-right: 5px;"><span
							style="vertical-align: top;">Editar</span></a></label>
					<p>{{node._name}}</p>
				</div>
				<div ng-show="editando === node">
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-25">
							<label>Nome</label> <input type="text" ng-model="node._name"
								class="gt-form-text">
						</div>
						<div class="gt-right-col gt-width-75">
							<label style="float: right; margin-left: 1em;"><a
								ng-click="delJoin($index)"><img
									src="/siga/css/famfamfam/icons/delete.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Nó</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addEvento(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Evento</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addTransicao(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Transição</span></a></label>
						</div>
					</div>

					<div id="join-event-container" class="gt-form-row gt-width-100"
						ng-show="node.event != null && node.event.length > 0"></div>
					<div id="join-transition-container"
						class="gt-form-row gt-width-100"
						ng-show="node.transition != null && node.transition.length > 0"></div>
				</div>

			</div>

			<div class="gt-form-row">
				<input type="button" ng-click="addNode()" class="gt-btn-medium"
					value="Adicionar Join" />
			</div>


			<!-- End State -->
			<h2 class="gt-form-head"
				style="margin-top: 20px !important; clear: both">Fins</h2>

			<div class="gt-form gt-content-box"
				style="margin-bottom: 10px !important;"
				ng-repeat="node in pd.end_state">
				<div ng-hide="editando === node">
					<label style="float: right; margin-left: 1em;"><a
						ng-click="editar(node)"><img
							src="/siga/css/famfamfam/icons/pencil.png"
							style="margin-right: 5px;"><span
							style="vertical-align: top;">Editar</span></a></label>
					<p>{{node._name}}</p>
				</div>
				<div ng-show="editando === node">
					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-25">
							<label>Nome do fim</label> <input type="text"
								ng-model="node._name" class="gt-form-text" />
						</div>
						<div class="gt-right-col gt-width-75">
							<label style="float: right; margin-left: 1em;"><a
								ng-click="delEndState($index)"> <img
									src="/siga/css/famfamfam/icons/delete.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Fim</span></a></label> <label
								style="float: right; margin-left: 1em;"><a
								ng-click="addEvento(node)"><img
									src="/siga/css/famfamfam/icons/add.png"
									style="margin-right: 5px;"><span
									style="vertical-align: top;">Evento</span></a></label>
						</div>
					</div>

					<div id="end-state-event-container"
						class="gt-form-row gt-width-100"
						ng-show="node.event != null && node.event.length > 0"></div>
				</div>
			</div>
			<div class="gt-form-row">
				<input type="button" ng-click="addEndState()" class="gt-btn-medium"
					value="Adicionar Fim" />
			</div>

			<div>
				<pre>{{getSmallGraphSource()}}</pre>
			</div>
		</div>

		<div class="gt-content clearfix" ng-show="exibirXml">

			<h2 class="gt-form-head">Editar XML</h2>

			<form action="/sigawf/app/edicao/xml_gravar" method="post">
				<div class="gt-form gt-content-box"
					style="margin-bottom: 10px !important;">

					<div class="gt-form-row gt-width-100">
						<div class="gt-left-col gt-width-100" style="display: none;">
							<label>Procedimento original</label>
							<textarea id="een" name="xmlOriginal" class="gt-form-text" rows="20"
								ng-bind="xmlOriginal"></textarea>
						</div>
						<div class="gt-left-col gt-width-100">
							<label>Procedimento alterado</label>
							<textarea id="twee" name="xml" class="gt-form-text" rows="20"
								ng-bind="xml"></textarea>
						</div>
					</div>
					<div class="gt-form-row">
						<input type="submit" class="gt-btn-medium gt-btn-left"
							value="Salvar" /> <input type="button" id="diffButton"
							class="gt-btn-medium gt-btn-left" value="Diff" />
					</div>
				</div>

			</form>

			<h2>Diff</h2>
			<div class="gt-sidebar-list-content" id="diff" ng-bind-html="diff">
				<span ng-bind-html="diff"></span>
			</div>
			<div class="gt-sidebar-list-content">
				<table>
					<thead>
						<tr>
							<th colspan="3">Output</th>
						</tr>
					</thead>
					<tbody id="res"></tbody>
				</table>
			</div>
		</div>

		<div class="gt-sidebar">

			<!-- Sidebar List -->
			<div class="gt-sidebar-list">
				<h3>
					Mapa do Procedimento <label style="float: right; margin-left: 1em;"><a
						ng-click="atualizarSmallGraph()"><img
							src="/siga/css/famfamfam/icons/add.png"
							style="margin-right: 5px;"><span
							style="vertical-align: top;">Atualizar</span></a></label>

				</h3>

				<div style="display: none" id="input"></div>
				<a
					onclick="javascript: document.getElementById('page').style.display='none'; document.getElementById('svg').style.display='block'; bigmap();"
					title="Zoom" href="#">
					<div class="gt-sidebar-list-content" id="output"
						ng-bind-html="smallgraph"></div>

				</a>
			</div>

			<!-- /Sidebar List -->

			<div class="gt-sidebar-content" id="gc"></div>

		</div>
		<!-- / sidebar -->
	</div>

	<script>
		document.getElementById("start-state-container").innerHTML = document
				.getElementById("task-node-container").innerHTML;
		document.getElementById("node-event-container").innerHTML = document
				.getElementById("event-container").innerHTML;
		document.getElementById("node-transition-container").innerHTML = document
				.getElementById("transition-container").innerHTML;
		document.getElementById("end-state-event-container").innerHTML = document
				.getElementById("event-container").innerHTML;
		document.getElementById("mail-node-event-container").innerHTML = document
				.getElementById("event-container").innerHTML;
		document.getElementById("mail-node-transition-container").innerHTML = document
				.getElementById("transition-container").innerHTML;
		document.getElementById("fork-event-container").innerHTML = document
				.getElementById("event-container").innerHTML;
		document.getElementById("fork-transition-container").innerHTML = document
				.getElementById("transition-container").innerHTML;
		document.getElementById("join-event-container").innerHTML = document
				.getElementById("event-container").innerHTML;
		document.getElementById("join-transition-container").innerHTML = document
				.getElementById("transition-container").innerHTML;
	</script>

	<script	src="/siga/javascript/angularjs/1.8.2/angular.min.js"></script>
	<script src="../../../js/xmlutils.js" type="text/javascript"
		language="javascript"></script>
	<script src="../../../js/wf-design.js"></script>

	<script src="../../../js/diff.js"></script>

	<script src="/siga/javascript/viz.js" language="JavaScript1.1"
		type="text/javascript"></script>

	<script>
		$(document).ready(function() {
			updateContainer();
			$(window).resize(function() {
				updateContainer();
			});
		});
		function updateContainer() {
			var smallwidth = $('#output').width();
			var smallsvg = $('#output :first-child').first();
			var smallviewbox = smallsvg.attr('viewBox');

			if (typeof smallviewbox != 'undefined') {
				var a = smallviewbox.split(' ');

				// set attrs and 'resume' force
				smallsvg.attr('width', smallwidth);
				smallsvg.attr('height', smallwidth * a[3] / a[2]);
			}

		}
	</script>
</siga:pagina>


