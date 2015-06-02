<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:if test="${locaisDisponiveis.size() > 1}">
	<div class="gt-form-row gt-width-66">
		<label>Local</label>
		<siga:select id="local" 
		             name="solicitacao.local" 
		             list="locaisDisponiveis" 
		             listValue="nomeComplexo" 
		             listKey="idComplexo" 
		             value="${solicitacao.local.idComplexo}" />
	</div>
	<script>
		$("#local").change(function() {
			carregarItem();
		});
	</script>
</c:if>

<div class="gt-form-row gt-width-66">
	<label>Telefone</label> 
	<input type="text" name="solicitacao.telPrincipal" id="telPrincipal" value="${solicitacao.telPrincipal}" maxlength="255" />
</div>

<div id="meioComunicacao">
	<div class="gt-form-row gt-width-66">
		<label>Origem da Demanda</label>
	
		<siga:select id="meioComunicacao" 
                     name="solicitacao.meioComunicacao" 
                     list="meiosComunicadaoList" 
                     listValue="descrMeioComunicacao" 
                     listKey="idTipoContato"
                     value="${solicitacao.meioComunicacao}"
                     isEnum="true"/>
	</div>
	<div class="gt-form-row box-wrapper">
		<div class="box box-left gt-width-25" style="margin-left: 15px;">
			<label>Contato inicial</label> 
			<label>Data</label> 
			<input type="text" name="calendario" id="calendarioComunicacao" value="${solicitacao.dtOrigemDDMMYYYY}" /> 
			<siga:error name="calendario"/>
		</div>
		<div class="box gt-width-33" style="padding-top: 21px;">
			<label>Hora</label> 
		    <input type="text" name="horario" id="horarioComunicacao" value="${solicitacao.dtOrigemHHMM}" />
		    <siga:error name="horario"/>
		</div>
	</div>
	<input type="hidden" name="solicitacao.dtOrigemString" id="stringDtMeioContato" value="${solicitacao.dtOrigemDDMMYYYYHHMM}" >
</div>
<script>
	//validarCadastranteSolicitante();
</script>