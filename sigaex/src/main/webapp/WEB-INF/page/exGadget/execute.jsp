<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>

<script type="text/javascript">
	submitOk = function() {
		var id = document.getElementById('editar_documentoViaSel_id').value;
		if (id == null || id == "") {
			return;
		}
		frmSigaEx.action = frmSigaEx.docAcao.value;
		frmSigaEx.submit();
	}
</script>

<c:set var="descr" value="${listEstado[4].descricao}" />

<c:set var="situacao"><fmt:message key="quadro.situacao" /></c:set>

<c:forEach var="idTpFormaDoc" items="${listIdTpFormaDoc}">

	<div class="card bg-light mb-3">
		<div class="card-header"><a href="/sigaex/app/expediente/doc/listar?primeiraVez=sim&idTipoFormaDoc=${idTpFormaDoc}">${idTpFormaDoc == 1 ? "Expedientes" : "Processos Administrativos"} </a></div>
		<div class="card-body">
	
			<c:if test="${idTpFormaDoc == 1}">
				<div id="sigaex"></div>
			</c:if>
			<c:if test="${idTpFormaDoc == 2}">
				<div id="processos"></div>
			</c:if>
		
		
			<table class="table-hover" style="width: 100%">
				<c:forEach var="listEstado" items="${listEstados}">
					<c:if test="${listEstado[8] == idTpFormaDoc}">
						<c:if test="${listEstado[4].grupo.nome != descr}">
							<thead>
								<tr>
									<th width="50%" style="text-align: left; padding-top: 0.5em;">
									    <c:choose>
										    <c:when test="${!fn:startsWith(situacao,'???')}">
										        ${situacao}
										    </c:when>    
										
										    <c:otherwise>
										         	 ${listEstado[4].descricao}
										    </c:otherwise>
										</c:choose>
									</th>
				
				 
									<th width="25%" style="text-align: right"><c:if
											test="${empty descr}">
											<fmt:message key="quadro.atendente" />
										</c:if></th>
									<th width="25%" style="text-align: right"><c:if
											test="${empty descr}">
											<fmt:message key="usuario.lotacao" />
										</c:if></th>
								</tr>
							</thead>
						</c:if>
						<c:set var="descr" value="${listEstado[4].grupo.nome}" />
						 
							<c:set var="titulo1" value="" />
							<c:set var="titulo2" value="" />
							<c:set var="titulo3" value="" />
							<c:set var="ordem" value="0" />
							<c:set var="visualizacao" value="0" />
							<c:choose>
								<c:when test="${listEstado[0]==1}">
									<c:set var="titulo1"
										value="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados." />
									<c:set var="titulo2"
										value="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados." />
									<c:set var="titulo3" value="${titulo1}" />
								</c:when>
								<c:when test="${listEstado[0]==2}">
									<c:set var="titulo1"
										value="Documentos que já foram assinados ou tiveram a assinatura manual registrada. Também contém os documentos que já foram recebidos pela lotação ${lotaTitular.nomeLotacao} ou pelo usuário ${titular.nomePessoa}." />
									<c:set var="titulo2"
										value="Documentos que já foram assinados ou tiveram a assinatura manual registrada. Também contém os documentos que já foram recebidos pelo usuário ${titular.nomePessoa}." />
									<c:set var="titulo3" value="${titulo1}" />
									<c:set var="ordem" value="1" />
									<c:set var="visualizacao" value="1" />
								</c:when>
								<c:when test="${listEstado[0]==3}">
									<c:set var="titulo1"
										value="Documentos não eletrônicos transferidos para a ${lotaTitular.nomeLotacao} ou para o usuário ${titular.nomePessoa} que estão aguardando recebimento." />
									<c:set var="titulo2"
										value="Documentos não eletrônicos transferidos para o usuário ${titular.nomePessoa} que estão aguardando recebimento." />
									<c:set var="titulo3" value="${titulo1}" />
									<c:set var="ordem" value="1" />
								</c:when>
								<c:when test="${listEstado[0]==14}">
									<c:set var="titulo1"
										value="Documentos eletrônicos transferidos para a ${lotaTitular.nomeLotacao} ou para o usuário ${titular.nomePessoa} que estão aguardando recebimento." />
									<c:set var="titulo2"
										value="Documentos eletrônicos transferidos para o usuário ${titular.nomePessoa} que estão aguardando recebimento." />
									<c:set var="titulo3" value="${titulo1}" />
									<c:set var="ordem" value="1" />
								</c:when>
								<c:when test="${listEstado[0]==15}">
									<c:set var="titulo1"
										value="Documentos que foram finalizados mas ainda não foram assinados." />
									<c:set var="titulo2"
										value="Documentos que foram finalizados mas ainda não foram assinados." />
									<c:set var="titulo3" value="${titulo1}" />
									<c:set var="ordem" value="1" />
								</c:when>
								<c:when test="${listEstado[0]==23}">
									<c:set var="titulo1"
										value="Documentos não eletrônicos que foram transferidos, pela lotação ${lotaTitular.nomeLotacao} ou pelo usuário ${titular.nomePessoa}, para outra lotação/pessoa mas ainda não foram recebidos." />
									<c:set var="titulo2"
										value="Documentos não eletrônicos que foram transferidos, pelo usuário ${titular.nomePessoa}, para outra lotação/pessoa mas ainda não foram recebidos." />
									<c:set var="titulo3" value="${titulo1}" />
									<c:set var="ordem" value="1" />
								</c:when>
								<c:when test="${listEstado[0]==24}">
									<c:set var="titulo1"
										value="Documentos eletrônicos que foram transferidos, pela lotação ${lotaTitular.nomeLotacao} ou pelo usuário ${titular.nomePessoa}, para outra lotação/pessoa mas ainda não foram recebidos." />
									<c:set var="titulo2"
										value="Documentos eletrônicos que foram transferidos, pelo usuário ${titular.nomePessoa}, para outra lotação/pessoa mas ainda não foram recebidos." />
									<c:set var="titulo3" value="${titulo1}" />
									<c:set var="ordem" value="1" />
								</c:when>
								<c:when test="${listEstado[0]==25}">
									<c:set var="titulo1"
										value="Documentos pendentes de assinatura cujo subscritor é o usuário ${titular.nomePessoa}." />
									<c:set var="titulo2" value="${titulo1}" />
									<c:set var="titulo3" value="" />
									<c:set var="ordem" value="1" />
								</c:when>
								<c:when test="${listEstado[0]==31}">
									<c:set var="visualizacao" value="1" />
								</c:when>
							</c:choose>
							<tr>
								<td
									style="text-align: left; padding-left: 1em; color: ${'#'}${listEstado[6].descricao}">
									<c:if test="${not empty listEstado[7]}">
										<span class="mr-1"><i
											class="${listEstado[7].codigoFontAwesome}"></i></span>
									</c:if>${listEstado[1]}
								
								<td align="right" class="count">
										<c:choose>
											<c:when test="${listEstado[2]>0}">
												<siga:monolink titulo="${titulo2}" texto="${listEstado[2]}"
												href="${pageContext.request.contextPath}/app/expediente/doc/listar?ultMovIdEstadoDoc=${listEstado[0]}&ultMovRespSel.id=${titular.idPessoa}&orgaoUsu=0&idTipoFormaDoc=${idTpFormaDoc}&ordem=${ordem}&visualizacao=${visualizacao}" />
											</c:when>
											<c:otherwise>
												 ${listEstado[2]} 
											</c:otherwise>
										</c:choose>
								<td align="right" class="count">
										<c:choose>
											<c:when test="${listEstado[3]>0}">
												<siga:monolink titulo="${titulo3}" texto="${listEstado[3]}"
												href="${pageContext.request.contextPath}/app/expediente/doc/listar?ultMovIdEstadoDoc=${listEstado[0]}&ultMovLotaRespSel.id=${lotaTitular.idLotacao}&orgaoUsu=0&idTipoFormaDoc=${idTpFormaDoc}&ordem=${ordem}&visualizacao=${visualizacao}" />
											</c:when>
											<c:otherwise>
												 ${listEstado[3]} 
											</c:otherwise>
										</c:choose>
								 </td>

								
							</tr>
 					</c:if>
				</c:forEach>
			</table>
		</div>
	</div>
	
	<c:set var="descr" value="${listEstado[4].descricao}" />
</c:forEach>