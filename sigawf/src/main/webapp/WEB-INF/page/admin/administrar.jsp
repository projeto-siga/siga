<%@ include file="/WEB-INF/page/include.jsp"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina popup="false" titulo="Administração do SIGA WF">
<div class="container-fluid">
	<div class="card bg-light mb-3" >
		<div class="card-header">
			<h5>Administração do SIGA WF</h5>
		</div>
		<div class="card-body">	
		<form action="endProcessInstance">
			<div class="row">
				<div class="col-sm-2">
					<div class="form-group">
						<label for="idTI">ID do Task Instance</label>
						<input type="text" name="idTI" class="form-control" />
					</div>
				</div>
				<div class="col-sm-2">
					<div class="form-group">
						<label for="dtFim">Data de fim do Processo</label>
						<input type="text" name="dtFim" onblur="javascript:verifica_data(this, true);comparaData(dataInicial,dataFinal);" maxlength="10" class="form-control" />
						<small class="form-text text-muted">(Opcional, use para não distorcer a estatística</small>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-1">
					<button type="submit" class="btn btn-primary">OK</button>
				</div>
			</div>
		</form>		
		</div>
	</div>
</div>	
</siga:pagina>