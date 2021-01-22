<%@ include file="/WEB-INF/page/include.jsp"%><!--  -->





<siga:pagina titulo="Edição de Definição de Procedimento">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Edição de Definição de Procedimento: ${nome}</h2>

			<h4>
				Tarefas <span class="gt-table-action-list"><a href=""><img
						src="/siga/css/famfamfam/icons/add.png" alt="Adicionar"></a></span>
			</h4>
			<div class="gt-content-box gt-form">
				<div class="gt-form-row gt-width-100">
					<div class="gt-left-col" style="width: 10%;">
						<label>Tipo</label> <input type="text" name="nome" value=""
							class="gt-form-text" />
					</div>
					<div class="gt-left-col" style="width: 70%; padding-left: 2%;">
						<label>Nome</label> <input type="text" name="nome" value=""
							class="gt-form-text" />
					</div>
					<div class="gt-left-col" style="width: 16%; padding-left: 2%;">
						<label>Raia</label> <select name="nome" class="gt-form-text"></select>
					</div>
				</div>
				<div class="gt-form-row gt-width-100">
					<label>Variáveis <span class="gt-table-action-list"><a
							href=""><img src="/siga/css/famfamfam/icons/add.png"
								alt="Adicionar"></a></span></label>
					<table class="gt-table gt-form" style="table-layout: fixed;">
						<tr class="header">
							<td>Nome</td>
							<td>Título</td>
							<td style="width: 6em;">Acesso</td>
							<td style="width: 3em; text-align: center;">Ações</td>
						</tr>
						<tr>
							<td><input type="text" name="raia_nome" value=""
								class="gt-form-text gt-width-100" />${dTarefa.raia}</td>
							<td><input type="text" name="raia_nome" value=""
								class="gt-form-text gt-width-100" />${dTarefa.raia}</td>
							<td><select name="raia_nome"
								class="gt-form-text gt-width-100" />${dTarefa.raia}</td>
							<td
								style="width: 3em; text-align: center; vertical-align: middle;"><p
									class="gt-table-action-list">
									<a href=""><img src="/siga/css/famfamfam/icons/cancel.png"
										alt="Remover"></a>
								</p></td>
						</tr>
						<tr>
							<td colspan="4"></td>
						</tr>
					</table>
				</div>
				<div class="gt-form-row gt-width-100">
					<label>Transições <span class="gt-table-action-list"><a
							href=""><img src="/siga/css/famfamfam/icons/add.png"
								alt="Adicionar"></a></span></label>
					<table class="gt-table gt-form" style="table-layout: fixed;">
						<tr class="header">
							<td>Nome</td>
							<td>Título</td>
							<td>Condição</td>
							<td style="width: 3em; text-align: center;">Ações</td>
						</tr>
						<tr>
							<td><select name="raia_nome"
								class="gt-form-text gt-width-100" />${dTarefa.raia}</td>
							<td><input type="text" name="raia_nome" value=""
								class="gt-form-text gt-width-100" />${dTarefa.raia}</td>
							<td><input type="text" name="raia_nome" value=""
								class="gt-form-text gt-width-100" />${dTarefa.raia}</td>
							<td
								style="width: 3em; text-align: center; vertical-align: middle;"><p
									class="gt-table-action-list">
									<a href=""><img src="/siga/css/famfamfam/icons/cancel.png"
										alt="Remover"></a>
								</p></td>
						</tr>
						<tr>
							<td colspan="4"></td>
						</tr>
					</table>
				</div>
				<p class="gt-table-action-list">
					<a href=""><img src="/siga/css/famfamfam/icons/cancel.png">
						Remover</a>
				</p>
			</div>

			<%--
	<mail-node name="Indeferimento" actors="#{UO}">
		<subject>Indeferimento SEC</subject>
		<text>
A solicitação eletrônica de contratação número #{doc_a} foi indeferida pelo seguinte motivo: #{motivo_indef}.
		</text>
		<transition to="Fim 2"></transition>
	</mail-node>

	<transition to="Retifica SEC" name="Retificar SEC">
			<script>
				<expression>
					tiposituacao=3;				
	   </expression>
				<variable name='situacao' access='read,write' mapped-name='tiposituacao' />
			</script>
		</transition>
		
		<transition to="Cientifica SCM" name="Autorizar">
			<condition expression="#{sel_autoriza == 'Não'}"></condition>
		</transition>
					
 --%>
			<h4>
				Raias <span class="gt-table-action-list"><a href=""><img
						src="/siga/css/famfamfam/icons/add.png" alt="Adicionar"></a></span>
			</h4>
			<div class="gt-content-box gt-for-table">
				<table class="gt-table gt-form" style="table-layout: fixed;">
					<tr class="header">
						<td>Nome</td>
						<td style="width: 3em; text-align: center;">Ações</td>
					</tr>
					<tr>
						<td><input type="text" name="raia_nome"
							value="Nome da primeira raia" class="gt-form-text gt-width-100" />${dTarefa.raia}</td>
						<td
							style="width: 3em; text-align: center; vertical-align: middle;"><p
								class="gt-table-action-list">
								<a href=""><img src="/siga/css/famfamfam/icons/cancel.png"
									alt="Remover"></a>
							</p></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</siga:pagina>


