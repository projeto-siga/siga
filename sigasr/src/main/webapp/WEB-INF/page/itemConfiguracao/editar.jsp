<div class="gt-form gt-content-box" style="width: 800px !important; max-width: 800px !important;">
	<form id="formLista" method="post" enctype="multipart/form-data">
		<input type="hidden" id="idLista" name="idLista" value="${lista.idLista}">
		<input type="hidden" id="id" name="id" value="${lista.idLista}">
		<input type="hidden" id="hisIdIni" name="hisIdIni" value="${lista.hisIdIni}">
		<div class="gt-form-row gt-width-66">
			<label>Nome <span>*<span></label> 
			<input type="text" id="nomeLista" name="nomeLista" value="${nomeLista}" size="98" maxlength="255" required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Abrangência</label>
			<textarea cols="98" rows="5" name="descrAbrangencia"
				id="descrAbrangencia" maxlength="8192">${descrAbrangencia}</textarea>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Justificativa</label>
			<textarea cols="98" rows="5" name="descrJustificativa"
				id="descrJustificativa" maxlength="8192">${descrJustificativa}</textarea>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Priorização</label>
			<textarea cols="98" rows="5" name="descrPriorizacao"
				id="descrPriorizacao" maxlength="8192">${descrPriorizacao}</textarea>
		</div>
		<div class="container">
			<div class="title-table">
				<h3 style="padding-top: 25px;">Permissões</h3>
			</div>
		</div>
		<div class="gt-content-box gt-for-table dataTables_div">
	        <div class="gt-form-row dataTables_length">
	            <label>
	            	<siga:checkbox name="mostrarAssocDesativada" value="${mostrarAssocDesativada}"></siga:checkbox>
	            	<b>Incluir Inativas</b>
	            </label>
	        </div>        
			<table id="permissoes_table" border="0" class="gt-table display">
				<thead>
					<tr>
						<th>ID Orgão</th>
						<th>Orgão</th>
						<th>ID Local</th>
						<th>Local</th>
						<th>ID Lotação</th>
						<th>Nome Lotação</th>
						<th>Lotação</th>
						<th>ID Pessoa</th>
						<th>Nome Pessoa</th>
						<th>Pessoa</th>
						<th>ID Cargo</th>
						<th>Cargo</th>
						<th>Cargo</th>
						<th>ID Função</th>
						<th>Função</th>
						<th>Função</th>
						<th>Tipo Permissão JSON</th>
						<th>ID Tipo Permissao</th>
						<th>Tipo Permissão</th>	
						<th>Tipo Permissão</th>	
						<th></th>
					</tr>
				</thead>

				<tbody>
				</tbody>
			</table>
		</div>
	</form>
</div>
