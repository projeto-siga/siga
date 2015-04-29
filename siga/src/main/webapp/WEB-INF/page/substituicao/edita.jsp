<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cadastro de substituição">
	<!-- main content -->
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">


			<script>
				function hideShowSel(combo) {
					var sel1Span = document.getElementById('span'
							+ combo.name.substring(4));
					var sel2Span = document.getElementById('spanLota'
							+ combo.name.substring(4));
					if (combo.selectedIndex == 0) {
						sel1Span.style.display = "";
						sel2Span.style.display = "none";
					} else {
						sel1Span.style.display = "none";
						sel2Span.style.display = "";
					}
				}

				function verificaData() {
					var dataFim = document.getElementById("dtFimSubst").value;
					var dataInicio = document.getElementById("dtIniSubst").value;
					var mensagem = "Data de fim vazia. A substituição ficará valendo até que seja manualmente cancelada.";
					var atencao = " Atenção!";
					if (!dataFim == "" || !dataFim == null) {
						document.getElementById("atencao").innerHTML = "";
						document.getElementById("atencao").value = "";
						document.getElementById("dataFim").innerHTML = "";
						document.getElementById("dataFim").value = "";
					} else {
						document.getElementById("dataFim").innerHTML = mensagem;
						document.getElementById("dataFim").value = mensagem;
						document.getElementById("atencao").innerHTML = atencao;
						document.getElementById("atencao").value = atencao;
					}
					document.getElementById("dtFimSubst").focus;
					return false;
				}

				function aviso() {
					var dataFim = document.getElementById("dtFimSubst").value;
					var dataInicio = document.getElementById("dtIniSubst").value;
					var mensagem = "Caso a 'Data de Início' não seja informada, será assumida a data atual.<br/>Caso a 'Data de Fim' não seja informada, a substituição ficará valendo até que seja manualmente cancelada.";
					var atencao = "Importante";
					document.getElementById("dataFim").innerHTML = mensagem;
					document.getElementById("dataFim").value = mensagem;
					document.getElementById("atencao").innerHTML = atencao;
					document.getElementById("atencao").value = atencao;
					document.getElementById("dtFimSubst").focus;
				}
			</script>   
			<h2 class="gt-table-head">Cadastrar substituição</h2>
			<div class="gt-content-box gt-for-table">
				<form action="gravar" onsubmit="verificaData()" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="substituicao.idSubstituicao" value="${substituicao.idSubstituicao}"/>
					<c:set var="dataFim" value="" />
					<table class="gt-form-table" width="100%">
						<tr class="header">
							<td colspan="2">Dados da substituição</td>
						</tr>

						<tr>
							<td>Titular:</td>

							<td>														

								<select  name="tipoTitular" onchange="javascript:hideShowSel(this);">
									<c:forEach items="${listaTipoTitular}" var="item">
										<option value="${item.key}" ${item.key == tipoTitular ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select>									
									
								
								<c:choose>
									<c:when test="${tipoTitular == 1}">
										<c:set var="titularStyle" value="" />
										<c:set var="lotaTitularStyle" value="display:none" />
									</c:when>
									<c:when test="${tipoTitular == 2}">
										<c:set var="titularStyle" value="display:none" />
										<c:set var="lotaTitularStyle" value="" />
									</c:when>
								</c:choose> 
								
								<span id="spanTitular" style="${titularStyle}"> 
									<siga:selecao modulo="siga" propriedade="titular" tema="simple" /> 
								</span> 
								<span id="spanLotaTitular" style="${lotaTitularStyle}"> 
									<siga:selecao modulo="siga" propriedade="lotaTitular" tema="simple" paramList="${strBuscarFechadas}"/> 
								</span>
							</td>
						</tr>

						<tr>
							<td>Substituto:</td>

							<td>
								
								<select  name="tipoSubstituto" onchange="javascript:hideShowSel(this);">
									<c:forEach items="${listaTipoSubstituto}" var="item">
										<option value="${item.key}" ${item.key == tipoSubstituto ? 'selected' : ''}>
											${item.value}
										</option>  
									</c:forEach>
								</select>									
									
								<c:choose>
									<c:when test="${tipoSubstituto == 1}">
										<c:set var="substitutoStyle" value="" />
										<c:set var="lotaSubstitutoStyle" value="display:none" />
									</c:when>
									<c:when test="${tipoSubstituto == 2}">
										<c:set var="substitutoStyle" value="display:none" />
										<c:set var="lotaSubstitutoStyle" value="" />
									</c:when>
								</c:choose> 
								
								<span id="spanSubstituto" style="${substitutoStyle}"> 
									<siga:selecao modulo="siga" propriedade="substituto" tema="simple"/> 
								</span> 
								<span id="spanLotaSubstituto" style="${lotaSubstitutoStyle}"> 
									<siga:selecao  modulo="siga" propriedade="lotaSubstituto" tema="simple" /> 
								</span>
							</td>
						</tr>

						<tr>
							<td>Data de Início</td>
							<td>
								<input type="text" id="dtIniSubst" name="dtIniSubst" label="Data de Início" value="${dtIniSubst}"
									onblur="javascript:verifica_data(this, true);" theme="simple" />
								(opcional)
							</td>
						</tr>

						<tr>
							<td>Data de Fim</td>
							<td>
								<input type="text" id="dtFimSubst" name="dtFimSubst" label="Data de Fim" value="${dtFimSubst}"
									onblur="javascript:verifica_data(this, true);" theme="simple" />
								(opcional)
							</td>
						</tr>

						<!-- Incluido para retorno de mensagem de campo nao preenchido -->
						<c:if test="${ empty dataFim }">
							<tr>
								<td align="right"><b><span id="atencao">
								</b></span></td>
								<td><span id="dataFim"></span></td>
							</tr>
						</c:if>

						<c:set var="atencao" value="" />
						<c:set var="dataFim" value="" />

						<tr class="button">
							<td colspan="2">
							<input type="submit" value="Ok"
								class="gt-btn-medium gt-btn-left" /> 
								
							<input type="button" value="Cancela" onclick="javascript:history.back();" 
								class="gt-btn-medium gt-btn-left" />
						</tr>
					</table>
				</form>
			</div>

			<script>
				aviso();
			</script>
		</div>
	</div>
</siga:pagina>