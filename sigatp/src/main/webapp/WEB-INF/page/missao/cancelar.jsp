<%@ taglib prefix="siga" uri="http://localhost/jeetags"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tptags" uri="/WEB-INF/tpTags.tld"%>

<style type="text/css">
	.container { 
		border:2px solid #ccc;
		width:500px; 
		height: 100px; 
		overflow-y: scroll; 
	}
</style>

<siga:pagina titulo="SIGA:Transportes">
	<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
	<script src="/sigatp/public/javascripts/validacao.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnCancelarMissao").click(function(event) {
				if(!event) { //IE
					event = window.event;
				} 
				event.preventDefault();
				
				if ($(":checkbox:checked").length > 0) {
					if(confirm('Tem certeza que deseja excluir as requisi\u00E7\u00F5es selecionadas?')) {
						verificarRequisicoes();
						$("#formCancelarMissao").attr("action",$("#action").val());
						$("#formCancelarMissao").submit();
					} else {
						$("#btnCancelarMissao").removeAttr("disabled");
						$("#btnCancelarMissao").val("Cancelar Miss\u00E3o");
						$("input:button[name!='btnCancelarMissao']").removeAttr("disabled");
					}
				}
				else {
					$("#formCancelarMissao").attr("action",$("#action").val());
					$("#formCancelarMissao").submit();
				}
			});
	
			if ($(".container input[type=checkbox]").length > 0) {
				$("#trRequisicoes").show();
			}
			else {
				$("#trRequisicoes").hide();
			}

			return false;
		});

		function verificarRequisicoes() {
			var x = 0;	
			$(":checkbox:checked").each(function() {
				var checkbox = $(this);
				if(checkbox.is(':checked')) {
					var _id = 'requisicoes[' + x + '].id';
					var _valor = checkbox.attr("id").replace('req_','');
					var input = $('<input type="hidden">').attr('id',_id).attr('name',_id).val(_valor).appendTo('#formCancelarMissao') ;
					x ++;
				}
			});
		}
	</script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Cancelar Miss&atilde;o: ${missao.sequence}</h2>

			<sigatp:erros />

			<form method="post" id="formCancelarMissao" enctype="multipart/form-data">
				<div class="gt-content-box gt-form clearfix">
					<div id ="infCancelamento" class="coluna margemDireitaG">
						<label for="missao.justificativa" class="obrigatorio">Justificativa</label>
						<sigatp:controleCaracteresTextArea nomeTextArea="missao.justificativa" rows="7" cols="80"/>

						<div id="trRequisicoes" class="clearfix">
							<label for="lstRequisicoes">Requisi&ccedil;&otilde;es associadas</label>
							
							<div class="container" id="lstRequisicoes">
								<c:choose>
									<c:when test="${requisicoesParaCancelar.size() > 0}">
										<c:forEach items="${requisicoesParaCancelar}" var="req">
											<input type="checkbox" name="chk" id="req_${req.id}"/> 
											Cancelar tamb&eacute;m a requisi&ccedil;&atilde;o
											<tptags:link  texto="${req.buscarSequence()}" parteTextoLink="${req.buscarSequence()}" 
												   comando="${linkTo[RequisicaoController].buscarPelaSequence(popUp,sequence)}?popUp=true&sequence=${req.buscarSequence()}">
												  ehEditavel="false">
											</tptags:link> 
											<br/>
										</c:forEach>
									</c:when>
								</c:choose>								
							</div>
						</div>

						<input type="hidden" name="missao" value="${missao.id}"/>
					</div>
				</div>
				<span style="color: red; font-weight: bolder; font-size: smaller;">
					<fmt:message key="views.erro.preenchimentoObrigatorio" />
				</span>
				<div class="gt-table-buttons">
					<input type="button" id="btnCancelarMissao" value="<fmt:message key="views.botoes.cancelarMissao"  />" class="gt-btn-medium gt-btn-left" />
					<input type="button" value="<fmt:message key="views.botoes.voltar" />" onclick="javascript:location.href = '${linkTo[MissaoController].buscarPelaSequence(false,missao.sequence)}'" class="gt-btn-medium gt-btn-left"/>
				</div>
				
				<input type="hidden" id="action" value="${linkTo[MissaoController].cancelarMissao}"/>
			</form>
		</div>
	</div>
</siga:pagina>