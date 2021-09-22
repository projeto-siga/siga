<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>
<%@ taglib prefix="tptags" uri="/WEB-INF/tpTags.tld"%>

<script src="/sigatp/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<jsp:include page="../tags/calendario.jsp" />
<sigatp:decimal />
<br />
<form id="formMissoes" method="post" enctype="multipart/form-data">
<sigatp:erros />
		<input type="hidden" id="missaoId" name="missao" value="${missao.id}" />

	<h3>Informa&ccedil;&otilde;es B&aacute;sicas</h3>
	<div id ="infbasicas" class="gt-content-box gt-for-table">
	 	<table id="htmlgridInformacoesBasicas" class="gt-table" >
	 		<tr>
	        	<th width="14%">Estado</span></th>
	        	<td>${missao.estadoMissao}</td>
	        </tr>
	        <tr>
	        	<th width="14%">Dist&acirc;ncia Percorrida</th>
	        	<td>
	        		<input type="text" readonly="readonly" name="missao.distanciaPercorridaEmKm" value="${missao.distanciaPercorridaEmKm}" size="12" class="decimal" />
	        	</td>
	        </tr>
	        <tr>
	        	<th width="14%">Tempo</th>
	        	<td>
	        		<input type="text" readonly="readonly" name="missao.tempoBruto" value="${missao.tempoBruto}" size="12" />
	        	</td>
	        </tr>
	        <tr>
	        	<th width="14%">Consumo (l)</th>
	        	<td>
	        		<input type="text" name="missao.consumoEmLitros" value="${missao.consumoEmLitros}" size="12" class="decimal" />
	        	</td>
	        </tr>
		</table>
	</div>
	<br>
	<h3> Requisi&ccedil;&atilde;o(&otilde;es)</h3>

		<div id ="gridRequisicoes" class="gt-content-box gt-for-table">
		 	<table id="htmlgridRequisicoes" class="gt-table" >
		    	<thead>
		    	<tr style="font-weight: bold;">
		    		<th>Sa&iacute;da prevista</th>
		    		<th>Retorno previsto</th>
		    		<th>Dados da Requisi&ccedil;&atilde;o</th>
		    		<th width="8%"></th>
				</tr>
				</thead>
				<tbody id="tbody">
					<c:if test="${null != missao.requisicoesTransporte && !missao.requisicoesTransporte.isEmpty()}">
						<c:forEach items="${missao.requisicoesTransporte}" var="requisicaoTransporte" varStatus="loop">
                			<tr id="row_${requisicaoTransporte.id}">
			   	    			<input type="hidden" name='requisicoesVsEstados[${loop.index}].idRequisicaoTransporte' readonly="readonly" value="${requisicaoTransporte.id}" class="requisicoes" />
			   	   				<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${requisicaoTransporte.dataHoraSaidaPrevista.time}" /></td>
		    					<td>
		    						<c:choose>
		    							<c:when test="${requisicaoTransporte.dataHoraRetornoPrevisto != null}">
		    								<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${requisicaoTransporte.dataHoraRetornoPrevisto.time}" />
		    							</c:when>
		    							<c:otherwise>
		    								<fmt:message key="no" />
		    							</c:otherwise>
		    						</c:choose>
		    					</td>
			   	    			<td>
			   	    				<tptags:link texto="${requisicaoTransporte.descricaoCompleta}"
			   	    							 parteTextoLink="${requisicaoTransporte.buscarSequence()}"
			   	    							 comando="${linkTo[RequisicaoController].buscarPelaSequence(popUp,sequence)}?popUp=true&sequence=${requisicaoTransporte.buscarSequence()}">
			   	    				</tptags:link>
								</td>
			   	    			<td width="8%" >
			   	    				<select name="requisicoesVsEstados[${loop.index}].estado">
			   	    					<c:forEach items="${estadosRequisicao}" var="estado">
			   	    						<option value="${estado}" ${estado == requisicoesVsEstados[i].estado ? 'selected' : ''}>${estado.descricao}</option>
			   	    					</c:forEach>
			   	    				</select>
					   	    	</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	<br />

	<h3>Sa&iacute;da</h3>
	<div id ="infSaida" class="gt-content-box gt-for-table">
	 	<table id="htmlgridSaida" class="gt-table" >
		    <tr>
        		<th  width="14%" class="obrigatorio">Data/Hora</th>
        		<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${missao.dataHoraSaida.time}" /></td>

        		<th width="14%" class="obrigatorio">Ve&iacute;culo</th>
				<td>${missao.veiculo.dadosParaExibicao}</td>

        		<th width="14%" class="obrigatorio">Condutor</th>
				<td colspan="3">${missao.condutor.dadosParaExibicao}</td>
	        </tr>
	        <tr>
        		<th width="14%">Od&ocirc;metro</th>
	        	<td>
	        		<input id="odometroSaidaEmKm" type="text" name="missao.odometroSaidaEmKm" value="${missao.odometroSaidaEmKm}" size="12" class="decimal" />
	        	</td>
	        	<th width="14%" class="obrigatorio">Estepe</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.estepe">
						<c:forEach items="${estepes}" var="estepe">
							<option value="${estepe}" ${estepe == missao.estepe ? 'selected' : ''}>${estepe.descricao}</option>
						</c:forEach>
					</select>
				</td>

	        	<th width="14%" class="obrigatorio">Avarias Aparentes</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.avariasAparentesSaida">
						<c:forEach items="${avariasAparentesSaida}" var="avariaAparente">
							<option value="${avariaAparente}" ${avariaAparente == missao.avariasAparentesSaida ? 'selected' : ''}>${avariaAparente.descricao}</option>
						</c:forEach>
					</select>
				</td>

				<th width="14%" class="obrigatorio">Limpeza</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.limpeza">
						<c:forEach items="${limpeza}" var="limpo">
							<option value="${limpo}" ${limpo == missao.limpeza}>${limpo.descricao}</option>
						</c:forEach>
					</select>
				</td>
        	</tr>
	        <tr>
        		<th width="14%" class="obrigatorio">N&iacute;vel Combust&iacute;vel</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.nivelCombustivelSaida">
						<c:forEach items="${niveisCombustivelSaida}" var="nivel">
							<option value="${nivel}" ${nivel == missao.nivelCombustivelSaida ? 'selected' : ''}>${nivel.descricao}</option>
						</c:forEach>
					</select>
				</td>

	        	<th width="14%" class="obrigatorio">Tri&acirc;ngulo</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.triangulos">
						<c:forEach items="${triangulos}" var="triangulo">
							<option value="${triangulo}" ${triangulo == missao.triangulos ? 'selected' : ''}>${triangulo.descricao}</option>
						</c:forEach>
					</select>
				</td>

	        	<th width="14%" class="obrigatorio">Extintor</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.extintor">
						<c:forEach items="${extintores}" var="extintor">
							<option value="${extintor}" ${extintor == missao.extintor ? 'selected' : ''}>${extintor.descricao}</option>
						</c:forEach>
					</select>
				</td>

				<th width="14%" class="obrigatorio">Ferramentas</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.ferramentas">
						<c:forEach items="${ferramentas}" var="ferramenta">
							<option value="${ferramenta}" ${ferramenta == missao.ferramentas ? 'selected' : ''}>${ferramenta.descricao}</option>
						</c:forEach>
					</select>
				</td>
	        </tr>
	        <tr>
	        	<th width="14%" class="obrigatorio">Licen&ccedil;a</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.licenca">
						<c:forEach items="${licencas}" var="licenca">
							<option value="${licenca}" ${licenca == missao.licenca ? 'selected' : ''}>${licenca.descricao}</option>
						</c:forEach>
					</select>
				</td>

	        	<th width="14%" class="obrigatorio">Cart&atilde;o Seguro</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.cartaoSeguro">
						<c:forEach items="${cartoesSeguro}" var="seguro">
							<option value="${seguro}" ${seguro == missao.cartaoSeguro ? 'selected' : ''}>${seguro.descricao}</option>
						</c:forEach>
					</select>
				</td>

	        	<th width="14%" class="obrigatorio">Cart&atilde;o Abastecimento</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.cartaoAbastecimento">
						<c:forEach items="${cartoesAbastecimento}" var="abastecimento">
							<option value="${abastecimento}" ${abastecimento == missao.cartaoAbastecimento ? 'selected' : ''}>${abastecimento.descricao}</option>
						</c:forEach>
					</select>
				</td>

	        	<th width="14%" class="obrigatorio">Cart&atilde;o Sa&iacute;da</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.cartaoSaida">
						<c:forEach items="${cartoesSaida}" var="saida">
							<option value="${saida}" ${saida == missao.cartaoSaida ? 'selected' : '' }>${saida.descricao}</option>
						</c:forEach>
					</select>
				</td>
	        </tr>
		</table>
	</div>
	<br>

	<h3>Retorno</h3>
	<div id ="infRetorno" class="gt-content-box gt-for-table">
	 	<table id="htmlgridRetorno" class="gt-table" >
		    <tr>
	        	<th width="14%" class="obrigatorio">Data/Hora</th>
	        	<td>
	        		<input type="text" id="inputdataHoraRetorno" name="missao.dataHoraRetorno" value="<fmt:formatDate pattern='dd/MM/yyyy HH:mm' value="${missao.dataHoraRetorno.time}" />" size="12" class="dataHora" />
	        	</td>
	        	<th colspan ="6"/>
	        </tr>
	        <tr>
	        	<th width="14%" class="obrigatorio">Od&ocirc;metro</th>
	        	<td>
	        		<input type="text" name="missao.odometroRetornoEmKm" value="${missao.odometroRetornoEmKm}" size="12" class="decimal" />
	        	</td>

	        	<th width="14%" class="obrigatorio">Combustivel</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.nivelCombustivelRetorno">
						<c:forEach items="${nivelCombustivelRetorno}" var="nivel">
							<option value="${nivel}" ${nivel == missao.nivelCombustivelRetorno ? 'selected' : ''}>${nivel.descricao}</option>
						</c:forEach>
					</select>
				</td>

	        	<th width="14%" class="obrigatorio">Avarias Aparentes</th>
				<td align="left" style="padding: 7px 10px;">
					<select name="missao.avariasAparentesRetorno">
						<c:forEach items="${avariasAparentesRetorno}" var="avariaAparente">
							<option value="${avariaAparente}" ${avariaAparente == missao.avariasAparentesRetorno ? 'selected' : ''}>${avariaAparente.descricao}</option>
						</c:forEach>
					</select>
				</td>
				<th width="14%"></th>
					<td></td>
				<tr>
		        	<th><label for="missao.ocorrencias">Ocorr&ecirc;ncias</label> </th>
		        	<td colspan="7">
		        		<sigatp:controleCaracteresTextArea nomeTextArea="missao.ocorrencias" rows="7" cols="80" valorTextArea="${null != missao ? missao.ocorrencias : ''}" />
		        	</td>
		        </tr>
		       	<tr>
		        	<th><label for="missao.itinerarioCompleto">Itiner&aacute;rio Completo</label></th>
		        	<td colspan="7">
		        		<sigatp:controleCaracteresTextArea nomeTextArea="missao.itinerarioCompleto" rows="7" cols="80" valorTextArea="${null != missao ? missao.itinerarioCompleto : ''}" />
                    </td>
		        </tr>
		</table>
	</div>
	<br/><span style="color: red; font-weight: bolder; font-size: smaller;"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>

	<script>
		 function submitForm(acao) {
	        var formulario = document.getElementById("formMissoes");
	        formulario.setAttribute("action",acao);
			var x = 0;
			var inputsRequisicoes = document.getElementsByName("requisicoesSelecionadas");
			for (var i = 0; i < inputsRequisicoes.length; i++) {
				inputsRequisicoes[i].setAttribute("name", "missao.requisicoesTransporte[" + x + "].id");
				i--;
				x++;
			}

			formulario.submit();
		}
	</script>

	<div id="btnAcoes" class="gt-table-buttons">
		<input type="button" id="btnFinalizar" value="<fmt:message key='views.botoes.finalizar' />" onClick="submitForm('${linkTo[MissaoController].finalizarMissao}')" class="gt-btn-medium gt-btn-left" />
		<input type="button" id="btnVoltar"  value="<fmt:message key='views.botoes.voltar' />" onClick="javascript:location.href='${linkTo[MissaoController].buscarPelaSequence(false,missao.sequence)}'" class="gt-btn-medium gt-btn-left" />
	</div>
</form>