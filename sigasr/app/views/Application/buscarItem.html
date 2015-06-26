#{extends 'main.html' /} 
<script language="javascript">
function sbmt(nivel){
	document.getElementById('alterou').value=nivel;
	frm.submit();
}
</script>
<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
		<div class="gt-content-box gt-for-table">
			<form action="@{Application.buscarItem}" id="frm" enctype="multipart/form-data">
				<input type="hidden" name="popup" value="true" />
				<table class="gt-form-table">
					<tr class="header">
						<td align="center" valign="top" colspan="4">Dados do item</td>
					</tr>
					<tr>
						<td>Código:</td>
						<td><input type="text" name="filtro.siglaItemConfiguracao"
							value="${filtro?.siglaItemConfiguracao}" />
						</td>
					</tr>
					<tr>
						<td>Título</td>
						<td><input type="text" name="filtro.tituloItemConfiguracao"
							value="${filtro?.tituloItemConfiguracao}" /></td>
					</tr>
					<tr>
						<td><input type="hidden" name="nome" value="${nome}" />
							<input type="hidden" name="sol.solicitante" value="${sol.solicitante?.idPessoa}" />
							<input type="hidden" name="sol.titular" value="${sol.titular?.idPessoa}" />
							<input type="hidden" name="sol.lotaTitular" value="${sol.lotaTitular?.idLotacao}" />
							<input type="hidden" name="sol.local" value="${sol.local?.idComplexo}" />
							<input
							type="submit" class="gt-btn-small gt-btn-left" value="Pesquisar" />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>

	<br />
	
	<div class="gt-content-box gt-for-table">
		<table class="gt-table gt-table-nowrap display" id="tabela">
			<col width="40%">
	  		<col width="10%">
	  		<col width="25%">
	  		<col width="25%">
	  		<thead>
				<tr id="plim">
					<th>Título
					</td>
					<th>Código
					</td>
					<th>Descrição</th>
					<th>Itens Similares</th>
				</tr>
			</thead>
			<tbody>
				#{list items:itens, as:'item'}
				<tr name="${item.nivel}" >
					<td class="gt-celula-nowrap" style="padding-left: ${item.nivel*13}px;">
						#{if !item.especifico}
							<a href="" onclick="clica(this); return false;" style="text-decoration: none; font-size: 14pt" name="sinal">-</a>
						#{/if}
						#{else}
							&nbsp;&nbsp;
						#{/else}
						#{if item.especifico || (!sol.solicitante && !sol.local)}<a
						href="javascript:opener.retorna_item${nome}('${item.id}','${item.sigla}','${item.descricao}');window.close()">${item.tituloItemConfiguracao}</a>
						#{/if}
						#{else}<span>${item.tituloItemConfiguracao}</span>#{/else}
					</td>
					<td class="gt-celula-nowrap">${item.siglaItemConfiguracao}</td>
					<td class="gt-celula-nowrap">#{selecionado sigla:item.descrItemConfiguracao,descricao:item.descrItemConfiguracao /}</td>
					<td class="gt-celula-nowrap">#{selecionado sigla:item.descricaoSimilaridade,descricao:item.descricaoSimilaridade /}</td>
				</tr>
				#{/list}
			</tbody>
		</table>
	</div>
</div>	

<script language="javascript">

#{if !filtro.tituloItemConfiguracao}
$(document).ready(function(){
	var sinais = $("[name='sinal']"); 
	for(i=0; i<sinais.length; i++ ){
        clica(sinais[i]);
     }
});
#{/if}

function clica(obj){
	var ope = obj.innerHTML; 
	obj.innerHTML = (ope == '+' ? '-' : '+');
	var tr = $(obj).parent().parent().next();
	var nivelAtual = parseInt(tr.attr('name'));
	var nivelMinimo = nivelMaximo = nivelAtual;
	while (nivelAtual >= nivelMinimo){
		if (ope == '-')
			tr.hide();
		else {
			if (nivelAtual <= nivelMaximo){
				tr.show();
				var span = tr.find('td:first').find("[name='sinal']")[0];
				var sinal = span ? span.innerHTML : '';
				nivelMaximo = nivelAtual + (sinal == '-' ? 1 : 0)
			}
		}
		tr = tr.next();
		nivelAtual = parseInt(tr.attr('name'));
	}
}


</script>