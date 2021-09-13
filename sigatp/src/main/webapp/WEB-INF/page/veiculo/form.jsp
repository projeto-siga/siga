<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<script type="text/javascript" src="/sigatp/public/javascripts/validacao.js"></script>

<sigatp:calendario/>
<sigatp:decimal/>
<jsp:include page="menu.jsp" />
<sigatp:erros/>

<form id="formVeiculos" action="${linkTo[VeiculoController].salvar(veiculo)}" method="post" enctype="multipart/form-data">
	<input type="hidden" name="veiculo" value="${veiculo.id}">
	
	<h3>Dados de Identifica&ccedil;&atilde;o</h3>
  	<div class="gt-form gt-content-box clearfix" id="infbasicas">
		<div class="clearfix">
			<div class="coluna margemDireitaG">
		       	<label for= "veiculo.placa" class= "obrigatorio">Placa</label>
				<input type="text" name="veiculo.placa" value="${veiculo.placa}"/>
		       	<label for= "veiculo.patrimonio" class= "obrigatorio">Patrim&ocirc;nio</label>
				<input type="text" name="veiculo.patrimonio" value="${veiculo.patrimonio}" maxlength="15"/>
			</div>
			<div class="coluna margemDireitaG">
		       	<label for= "veiculo.situacao" class= "obrigatorio"> Situa&ccedil;&atilde;o</label>
		       	<select name="veiculo.situacao">
		       		<c:forEach items="${situacoes}" var="situacao">
		       			<option value="${situacao}" ${veiculo.situacao == situacao ? 'selected' : ''}>${situacao}</option>
		       		</c:forEach>
		       	</select>
		       	
		       	<label for= "veiculo.usoComum" class= "obrigatorio"> Uso comum</label>
     		    <select name="veiculo.usoComum">
		       		<c:forEach items="${respostasSimNao}" var="respostaSimNao">
		       			<option value="${respostaSimNao}" ${veiculo.usoComum == respostaSimNao ? 'selected' : ''}>${respostaSimNao.descricao}</option>
		       		</c:forEach>
		       	</select>
			</div>
			<div class="coluna margemDireitaG">
		       	<label for= "veiculo.grupo.id" class= "obrigatorio">Grupo</label>
		       	<siga:select name="veiculo.grupo.id" list="grupos" listKey="id" listValue="dadosParaExibicao" value="${veiculo.grupo.id}"/>
		       	
		       	<label for= "veiculo.lotacaoAtual" class="obrigatorio"> Lota&ccedil;&atilde;o</label>
		       	<siga:selecao propriedade="lotacaoAtual" tema="simple" modulo="siga" onchange="mostrarCampoOdometro()"/>
			</div>
		</div>
       	<div id="grupoOdometro" class="coluna">
	       	<label for= "veiculo.odometroEmKmAtual" class="obrigatorio">Od&ocirc;metro atual</label>
			<input type="text" name="veiculo.odometroEmKmAtual" class="decimal" value="${veiculo.odometroEmKmAtual}" maxlength="15"/>
		</div>
	</div>

	<h3>Caracter&iacute;sticas</h3>
  	<div class="gt-form gt-content-box clearfix">
		<div class="coluna">
			<div class="coluna">
		       	<label for= "veiculo.anoFabricacao" >Ano(Fab)</label>
				<input type="text" id="anoFabricacao" name="veiculo.anoFabricacao" value="${veiculo.anoFabricacao}"/></b>
		       	<label for= "veiculo.motor" >Motor</label>
				<input type="text" name="veiculo.motor" value="${veiculo.motor}"/>
		       	<label for= "veiculo.pneuMedida"> Medida pneus</label>
				<input type="text" name="veiculo.pneuMedida" value="${veiculo.pneuMedida}"/>
			</div>
			<div class="coluna">
		       	<label for= "veiculo.anoModelo"> Ano(Mod)</label>
				<input type="text" id="anoModelo" name="veiculo.anoModelo" value="${veiculo.anoModelo}"/>
		       	<label for= "veiculo.potencia"> Pot&ecirc;ncia</label>
				<input type="text" name="veiculo.potencia" value="${veiculo.potencia}"/>
		       	<label for= "veiculo.pneuPressaoDianteira"> Press&atilde;o diant.</label>
				<input type="text" name="veiculo.pneuPressaoDianteira" value="${veiculo.pneuPressaoDianteira}"/>
			</div>		
		</div>		
		<div class="coluna">
			<div class="coluna">
		       	<label for= "veiculo.marca" class= "obrigatorio">Marca</label>
				<input type="text" name="veiculo.marca" value="${veiculo.marca}"/>
		       	<label for= "veiculo.direcao">Dire&ccedil;&atilde;o</label>
				<input type="text" name="veiculo.direcao" value="${veiculo.direcao}"/>
		       	<label for= "veiculo.pneuPressaoTraseira">Press&atilde;o tras.</label>
				<input type="text" name="veiculo.pneuPressaoTraseira" value="${veiculo.pneuPressaoTraseira}"/>
			</div>
			<div class="coluna">
		       	<label for= "veiculo.modelo" class= "obrigatorio">Modelo</label>
				<input type="text" name="veiculo.modelo" value="${veiculo.modelo}"/>
		       	<label for= "veiculo.transmissao">Transmiss&atilde;o</label>
				<input type="text" name="veiculo.transmissao" value="${veiculo.transmissao}"/>
		       	<label for= "veiculo.renavam" class= "obrigatorio">Renavam</label>
				<input type="text" id="renavam" name="veiculo.renavam" value="${veiculo.renavam}"/>
			</div>
		</div>
		<div class="coluna">    
			<div class="coluna">
		       	<label for= "veiculo.cor.id">Cor</label>
		       	<siga:select name="veiculo.cor.id" list="cores" listKey="id" listValue="nome" value="${veiculo.cor.id}"/>
		       		
		       	<label for= "veiculo.tipoDeBlindagem">Tipo de blindagem</label>
				<input type="text" name="veiculo.tipoDeBlindagem" value="${veiculo.tipoDeBlindagem}"/>
		       	<label for= "veiculo.chassi" class= "obrigatorio">Chassi</label>
				<input type="text" name="veiculo.chassi" value="${veiculo.chassi}"/>
			</div>
			<div class="coluna">
		       	<label for= "veiculo.tanque">Tanque</label>
				<input class="clearfix" type="text" name="veiculo.tanque" value="${veiculo.tanque}"/>
				<div>
			       	<label for= "veiculo." class= "obrigatorio"> Tipo combust&iacute;vel</label>
			       	<select name="veiculo.tipoDeCombustivel">
			       		<c:forEach items="${tiposDeCombustivel}" var="tipoDeCombustivel">
			       			<option value="${tipoDeCombustivel}" ${veiculo.tipoDeCombustivel == tipoDeCombustivel ? 'selected' : ''}>${tipoDeCombustivel.descricao}</option>
			       		</c:forEach>
			       	</select>
				</div>
				<div class="coluna margemDireitaP">
					<label for= "veiculo.licenciamentoAnual" class= "obrigatorio">Lic.anual</label>
					<select name="veiculo.licenciamentoAnual">
			       		<c:forEach items="${respostasSimNao}" var="respostaSimNao">
			       			<option value="${respostaSimNao}" ${veiculo.licenciamentoAnual == respostaSimNao ? 'selected' : ''}>${respostaSimNao.descricao}</option>
			       		</c:forEach>
			       	</select>
			    </div>
			    <div class="coluna margemDireitaP">
					<label for= "veiculo.dpvat" class= "obrigatorio">Dpvat</label>
					<select name="veiculo.dpvat">
			       		<c:forEach items="${respostasSimNao}" var="respostaSimNao">
			       			<option value="${respostaSimNao}" ${veiculo.dpvat == respostaSimNao ? 'selected' : ''}>${respostaSimNao.descricao}</option>
			       		</c:forEach>
			       	</select>
			    </div>
			    <div class="coluna">
					<label for= "veiculo.categoriaCNH" class= "obrigatorio">Cat.cnh</label>
				    <select name="veiculo.categoriaCNH">
			       		<c:forEach items="${categoriasCNH}" var="categoriaCNH">
			       			<option value="${categoriaCNH}" ${veiculo.categoriaCNH == categoriaCNH ? 'selected' : ''}>${categoriaCNH.descricao}</option>
			       		</c:forEach>
			       	</select>
			    </div>				 					       	
			</div>
		</div>
	</div>

	<h3>Acess&oacute;rios</h3>
  	<div class="gt-form gt-content-box paddingTop clearfix">
		<div class="coluna">    
			<div class="coluna">
				<input type="checkbox" id="veiculo_temAIRBAG" name="veiculo.temAIRBAG" value="true" ${veiculo.temAIRBAG ? 'checked':''}/>
				<label for="veiculo.temAIRBAG">Air bag</label>
			    <input type="checkbox" id="veiculo_temABS" name="veiculo.temABS" value="true" ${veiculo.temABS ? 'checked':''}/>
				<label for="veiculo.temABS">Abs</label>
			    <input type="checkbox" id="veiculo_temEBD" name="veiculo.temEBD" value="true" ${veiculo.temEBD ? 'checked':''}/>
				<label for="veiculo.temEBD">Ebd</label>
			    <input type="checkbox" id="veiculo_temSENSORDEMARCHARE" name="veiculo.temSENSORDEMARCHARE" value="true" ${veiculo.temSENSORDEMARCHARE ? 'checked':''}/>
				<label for="veiculo.temSENSORDEMARCHARE">Sensor de marcha a r&eacute;</label>
		    </div>
			<div class="coluna">
			    <input type="checkbox" id="veiculo_temGPS" name="veiculo.temGPS" value="true" ${veiculo.temGPS ? 'checked':''}/>
				<label for="veiculo.temGPS">Gps</label>
			    <input type="checkbox" id="veiculo_temCDPLAYER" name="veiculo.temCDPLAYER" value="true" ${veiculo.temCDPLAYER ? 'checked':''}/>
				<label for="veiculo.temCDPLAYER">Cd player</label>
			    <input type="checkbox" id="veiculo_temDVDPLAYER" name="veiculo.temDVDPLAYER" value="true" ${veiculo.temDVDPLAYER ? 'checked':''}/>
				<label for="veiculo.temDVDPLAYER">Dvd player</label>
			    <input type="checkbox" id="veiculo_temCAMERADEMARCHARE" name="veiculo.temCAMERADEMARCHARE" value="true" ${veiculo.temCAMERADEMARCHARE ? 'checked':''}/>
				<label for="veiculo.temCAMERADEMARCHARE">C&acirc;mera de marcha a r&eacute;</label>
		    </div>
	    </div>
		<div class="coluna">    
			<div class="coluna">
			    <input type="checkbox" id="veiculo_temPILOTOAUTOMATICO" name="veiculo.temPILOTOAUTOMATICO" value="true" ${veiculo.temPILOTOAUTOMATICO ? 'checked':''}/>
				<label for="veiculo.temPILOTOAUTOMATICO">Piloto autom&aacute;tico</label>
			    <input type="checkbox" id="veiculo_temBANCOSEMCOURO" name="veiculo.temBANCOSEMCOURO" value="true" ${veiculo.temBANCOSEMCOURO ? 'checked':''}/>
				<label for="veiculo.temBANCOSEMCOURO">Bancos de couro</label>
			    <input type="checkbox" id="veiculo_temTELALCDPAPOIOCABECA" name="veiculo.temTELALCDPAPOIOCABECA" value="true" ${veiculo.temTELALCDPAPOIOCABECA ? 'checked':''}/>
				<label for="veiculo.temTELALCDPAPOIOCABECA">Tela lcd p/apoio cabe&ccedil;a</label>
			    <input type="checkbox"  id="veiculo_temARCONDICIONADO" name="veiculo.temARCONDICIONADO" value="true" ${veiculo.temARCONDICIONADO ? 'checked':''}/>
				<label for="veiculo.temARCONDICIONADO">Ar Condicionado</label>
		    </div>
			<div class="coluna">
			    <input type="checkbox" id="veiculo_temCONTROLEDETRACAO" name="veiculo.temCONTROLEDETRACAO" value="true" ${veiculo.temCONTROLEDETRACAO ? 'checked':''}/>
				<label for="veiculo.temCONTROLEDETRACAO">Controle de tra&ccedil;&atilde;o</label>
			    <input type="checkbox" id="veiculo_temRODADELIGALEVE" name="veiculo.temRODADELIGALEVE" value="true" ${veiculo.temRODADELIGALEVE ? 'checked':''}/>
				<label for="veiculo.temRODADELIGALEVE">Roda de liga leve</label>
			    <input type="checkbox" id="veiculo_temFREIOADISCONASQUATRORODAS" name="veiculo.temFREIOADISCONASQUATRORODAS" value="true" ${veiculo.temFREIOADISCONASQUATRORODAS ? 'checked':''}/>
				<label for="veiculo.temFREIOADISCONASQUATRORODAS">Freio a disco nas 4 rodas</label>
			    <input type="checkbox" id="veiculo_temOUTROS" name="veiculo.temOUTROS" value="true" ${veiculo.temOUTROS ? 'checked':''}/>
				<label for="veiculo.temOUTROS" class="inline">Outros</label>
				<input type="text" class="inline" name="veiculo.outros" value="${veiculo.outros}"/>
		    </div>
		</div>
		<input type="hidden" name="veiculo.temAIRBAG" value="false" />
		<input type="hidden" name="veiculo.temGPS" value="false" />
		<input type="hidden" name="veiculo.temPILOTOAUTOMATICO" value="false" />
		<input type="hidden" name="veiculo.temCONTROLEDETRACAO" value="false" />
		<input type="hidden" name="veiculo.temABS" value="false" />
		<input type="hidden" name="veiculo.temCDPLAYER" value="false" />
		<input type="hidden" name="veiculo.temBANCOSEMCOURO" value="false" />
		<input type="hidden" name="veiculo.temRODADELIGALEVE" value="false" />
		<input type="hidden" name="veiculo.temEBD" value="false" />
		<input type="hidden" name="veiculo.temDVDPLAYER" value="false" />
		<input type="hidden" name="veiculo.temTELALCDPAPOIOCABECA" value="false" />
		<input type="hidden" name="veiculo.temFREIOADISCONASQUATRORODAS" value="false" /> 				
		<input type="hidden" name="veiculo.temSENSORDEMARCHARE" value="false" />
		<input type="hidden" name="veiculo.temCAMERADEMARCHARE" value="false" />
		<input type="hidden" name="veiculo.temARCONDICIONADO" value="false" />
		<input type="hidden" name="veiculo.temOUTROS" value="false" />
	</div>	

	<h3>Aquisi&ccedil;&atilde;o</h3>
  	<div class="gt-form gt-content-box clearfix">
		<div class="coluna">
			<label for="veiculo.fornecedor.id">Fornecedor</label>
	       	<siga:select name="veiculo.fornecedor.id" list="fornecedores" listKey="id" listValue="razaoSocialECNPJ" value="${veiculo.fornecedor.id}"/>
		</div>
		<div class="coluna">
			<div class="coluna">
				<label for="veiculo.valorAquisicao">Valor</label>
				R$ <input type="text" name="veiculo.valorAquisicao" id="valorFormatado" class="decimal"  value="${veiculo.valorAquisicao}" size="8"/>
			</div>
			<div class="coluna">
				<label for="veiculo.dataAquisicao">Data</label>
				<input type="text" name="veiculo.dataAquisicao" class="datePicker" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${veiculo.dataAquisicao.time}" />" size="8"/>
			</div>
			<div class="coluna">
				<label for="veiculo.dataGarantia">Dt.garantia</label>
				<input type="text" name="veiculo.dataGarantia" class="datePicker" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${veiculo.dataGarantia.time}" />" size="8"/>
			</div>
		</div>
	</div>

	<div class="gt-form gt-content-box paddingTop clearfix"> 
		<div class="coluna margemDireitaG"> 
	     	<h3>Cart&atilde;o de abastecimento</h3>
			<div class="coluna">
		  		<label>N&uacute;mero</label>
				<input type="text" name="veiculo.numeroCartaoAbastecimento" value="${veiculo.numeroCartaoAbastecimento}"/>
			</div>
			<div class="coluna">
				<label> Validade</label>
				<input type="text" name="veiculo.validadeCartaoAbastecimento" class="datePicker" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${veiculo.validadeCartaoAbastecimento.time}"/>" size="8"/>
			</div>
		</div>
		<div class="coluna margemDireitaG"> 
	     	<h3>Cart&atilde;o de seguro</h3>
	     	<div class="coluna">
		  		<label> N&uacute;mero</label>							
				<input type="text" name="veiculo.numeroCartaoSeguro" value="${veiculo.numeroCartaoSeguro}"/>
			</div>
	     	<div class="coluna">
				<label> Validade</label>							
				<input type="text" name="veiculo.validadeCartaoSeguro"  class="datePicker" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${veiculo.validadeCartaoSeguro.time}"/>" size="8"/>
			</div>
		</div> 
		<div class="coluna"> 
	     	<h3>Aliena&ccedil;&atilde;o</h3>
	     	<div class="coluna">	
				<label> Termo</label>							
				<input type="text" name="veiculo.termoAlienacao" value="${veiculo.termoAlienacao}"/>
			</div>
	     	<div class="coluna">
				<label> Processo</label>							
				<input type="text" name="veiculo.processoAlienacao" value="${veiculo.processoAlienacao}"/>
			</div>
	     	<div class="coluna">
				<label> Data</label>							
				<input type="text" name="veiculo.dataAlienacao" class="datePicker"  value="<fmt:formatDate pattern="dd/MM/yyyy" value="${veiculo.dataAlienacao.time}"/>" size="8"/>
			</div>
		</div> <!-- fim alienacao -->
	</div>
	
	<c:if test="${!esconderBotoes}">
		<span class="alerta menor">
			<fmt:message key="views.erro.preenchimentoObrigatorio"/>
		</span>
		<div class="gt-form-row">
	    	<input type="submit" value="<fmt:message key="views.botoes.salvar"/>" class="gt-btn-medium gt-btn-left" />
	    	<input type="button" value="<fmt:message key="views.botoes.cancelar"/>" class="gt-btn-medium gt-btn-left" onclick="javascript:window.location = '${linkTo[VeiculoController].listar}'" /> 
		</div>
	</c:if>
</form>

<script>
	$(function() {
		$('#renavam,#anoFabricacao,#anoModelo').keypress(function(e) {
	        if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
	        	return false;
	     	}
	     	return true;
		});

		if($("#lersomente").length != 0) {
			protegeDocumento();
		}
	});
	
	function protegeDocumento() { 
		$('div').find('input, textarea, button, select').attr('disabled','disabled');
	}
	
	
	function mostrarCampoOdometro() {
	    $('#grupoOdometro').show();
	}
	
	function esconderCampoOdometro() {
	    $('#grupoOdometro').hide();
	}
	
	<c:choose>
	    <c:when test="${mostrarCampoOdometro || veiculo.id == 0}">
	    	mostrarCampoOdometro();
	    </c:when>
	    <c:otherwise>
	    	esconderCampoOdometro();
	    </c:otherwise>
	</c:choose>
</script>