<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ attribute name="atributoAssociados" required="true" type="java.util.List"%>
<%@ attribute name="atributoSolicitacaoMap" required="true" type="java.util.Map"%>
<%@ attribute name="entidade" required="true" type="java.lang.String"%>

<c:if test="${not empty atributoAssociados}">
	<c:set var="atributoSolicitacaoMap" value="${atributoSolicitacaoMap}"/>
	<c:forEach items="${atributoAssociados}" var="atributo" varStatus="loop">
		<div class="form-group">
			<label>
				${atributo.nomeAtributo} 
				<c:if test="${atributo.descrAtributo != null && atributo.descrAtributo != ''}">
					(${atributo.descrAtributo})
				</c:if>
			</label>
			<c:if test="${atributo.tipoAtributo != null}">
				<input type="hidden" name="${entidade}.atributoSolicitacaoList[${loop.index}].idAtributoSolicitacao" value="${atributoSolicitacaoMap[atributo.idAtributo].idAtributoSolicitacao}" class="${atributo.idAtributo}"/>
				<input type="hidden" name="${entidade}.atributoSolicitacaoList[${loop.index}].idAtributo" value="${atributo.idAtributo}" class="${atributo.idAtributo}"/>
				<c:choose>
					<c:when test="${atributo.tipoAtributo.name() == 'TEXTO'}">
						<input type="text" 
								name="${entidade}.atributoSolicitacaoList[${loop.index}].valorAtributo" 
								value="${atributoSolicitacaoMap[atributo.idAtributo].valorAtributo}" 
								class="${atributo.idAtributo} form-control" size="70" maxlength="255" />
					</c:when>
					<c:when test="${atributo.tipoAtributo.name() == 'TEXT_AREA'}">
						<textarea rows="10" name="${entidade}.atributoSolicitacaoList[${loop.index}].valorAtributo" class="${atributo.idAtributo} form-control" maxlength="255">
							${atributoSolicitacaoMap[atributo.idAtributo].valorAtributo}
						</textarea>
					</c:when>
					<c:when test="${atributo.tipoAtributo.name() == 'DATA'}">
						<siga:dataCalendar nome="${entidade}.atributoSolicitacaoList[${loop.index}].valorAtributo" 
								id="calendarioAtributo${atributo.idAtributo}"
								value="${atributoSolicitacaoMap[atributo.idAtributo].valorAtributo}" 
								cssClass="${atributo.idAtributo}"/>
					</c:when>
					<c:when test="${atributo.tipoAtributo.name() == 'NUM_INTEIRO'}">
						<input type="text" class="${atributo.idAtributo} form-control"
								onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }"
								name="${entidade}.atributoSolicitacaoList[${loop.index}].valorAtributo" value="${atributoSolicitacaoMap[atributo.idAtributo].valorAtributo}" maxlength="9"/>
					</c:when>
					<c:when test="${atributo.tipoAtributo.name() == 'NUM_DECIMAL'}">
						<input type="text" name="${entidade}.atributoSolicitacaoList[${loop.index}].valorAtributo" 
								value="${atributoSolicitacaoMap[atributo.idAtributo].valorAtributo}" 
								id="numDecimal" pattern="^\d*(\,\d{2}$)?" title="Somente número e com duas casas decimais EX: 222,22" 
								class="${atributo.idAtributo} form-control" maxlength="9"/>
					</c:when>
					<c:when test="${atributo.tipoAtributo.name() == 'HORA'}">
						<input type="text" 
								name="${entidade}.atributoSolicitacaoList[${loop.index}].valorAtributo" 
								value="${atributoSolicitacaoMap[atributo.idAtributo].valorAtributo}" 
								id="horarioAtributo${atributo.idAtributo}" 
								class="${atributo.idAtributo} form-control" />
						<span style="color: red; display: none;" id="erroHoraAtributo${atributo.idAtributo}">Hor&aacute;rio inv&aacute;lido</span>
					</c:when>
					<c:when test="${atributo.tipoAtributo.name() == 'VL_PRE_DEFINIDO'}" >
						<select name="${entidade}.atributoSolicitacaoList[${loop.index}].valorAtributo" 
								value="${atributoSolicitacaoMap[atributo.idAtributo].valorAtributo}" 
								class="${atributo.idAtributo} form-control" >
							<c:forEach items="${atributo.preDefinidoSet}" var="valorAtributoSolicitacao">
								<option value="${valorAtributoSolicitacao}" <c:if test="${atributoSolicitacaoMap[atributo.idAtributo].valorAtributo == valorAtributoSolicitacao}">selected</c:if> >
									${valorAtributoSolicitacao}
								</option>
							</c:forEach>
						</select>
					</c:when>
				</c:choose>
				<siga:error name="${entidade}.atributoSolicitacaoList[${loop.index}].valorAtributo"/>
			</c:if>
		</div>
	</c:forEach>
</c:if>
<script>
	$(function() {
		$("#horarioAtributo${atributo.idAtributo}").mask("99:99");
		$("#horarioAtributo${atributo.idAtributo}").blur(function() {
			var hora = this.value;
			var array = hora.split(':');
			if (array[0] > 23 || array[1] > 59) {
				$('#erroHoraAtributo${atributo.idAtributo}').show(); 
				return;
			}
			$('#erroHoraAtributo${atributo.idAtributo}').hide();    
		});
	});
</script>