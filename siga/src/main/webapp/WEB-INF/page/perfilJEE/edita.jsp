<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<script type="text/javascript" language="Javascript1.1">
	function gravarGestorGrupo() {
		var t_strUrl = 'gravarGestorGrupo';
		document.formulario.action =  t_strUrl;
		document.formulario.submit();
	}
	function gravarGrupo() {
		var t_strIdConfiguracaoNova = '${idConfiguracaoNova}';
		var t_strUrl = 'gravar';
		if (t_strUrl) { 
			t_strUrl = t_strUrl.split('?')[0];
				var t_arr1StrIdConfigGravadas  = obterIdsConfiguracoesGravadas();
			for (var t_strIdIdx in t_arr1StrIdConfigGravadas) {
				var t_strId = t_arr1StrIdConfigGravadas[t_strIdIdx];
				atualizarConteudo(t_strId);
			}
			for (var t_strIdIdx in t_arr1StrIdConfigGravadas) {
				var t_strId = t_arr1StrIdConfigGravadas[t_strIdIdx];
				if (seNaoHaConteudo(t_strId)) {
					return;
				}
			}
			for (var t_strIdIdx in t_arr1StrIdConfigGravadas) {
				var t_strId = t_arr1StrIdConfigGravadas[t_strIdIdx];
				if (seEmailIncorreto(t_strId)) {
					return;
				}
			}
			// nova configuração
			atualizarConteudo(t_strIdConfiguracaoNova);
			if (seNaoHaConteudo(t_strIdConfiguracaoNova)) {
				return;
			}
			if (seEmailIncorreto(t_strIdConfiguracaoNova)) {
				return;
			}
			// verifica se há repeticoes
			for (var t_strIdIdx in t_arr1StrIdConfigGravadas) {
				var t_strId = t_arr1StrIdConfigGravadas[t_strIdIdx];
				if (seHaRepeticaoConfiguracoes(t_strId)) {
					return;
				}	
			}
			if (seHaRepeticaoConfiguracoes(t_strIdConfiguracaoNova)) {
				return;
			}
			document.formulario.action =  t_strUrl;
			document.formulario.submit();
		}
	}
	function trim (p_strString) {
		return p_strString.replace(/^\s+|\s+$/g,"");
	}
	function seNaoHaConteudo(p_strId) {
		var t_strIdConteudo = "conteudo_" + p_strId;
		var t_nodConteudo = document.getElementById(t_strIdConteudo) ;
		if (t_nodConteudo) {
			t_strConteudo = t_nodConteudo.value;
			if (trim(t_strConteudo) == "" ) {
				var t_nodSelecao = document.getElementById("tipoConfiguracao_" + p_strId) ;
 				if (t_nodSelecao) {
					if (t_nodSelecao.selectedIndex > 0) { 
						alert("Por favor, digite um conteúdo para a configuração!");
						t_nodSelecao.focus();
						return true;
					}
				} 
			}
		}
		return false;
	}
	function seEmailIncorreto(p_strId){
		var t_nodSelecao = document.getElementById("tipoConfiguracao_" + p_strId) ;
		if (t_nodSelecao) {
			if (t_nodSelecao.selectedIndex == 5) { // Email
				var t_strIdConteudo = "conteudo_" + p_strId;
				var t_nodConteudo = document.getElementById(t_strIdConteudo) ;
				if (t_nodConteudo) {
					t_strConteudo = t_nodConteudo.value;
					var t_arr1StrSepArroba = t_strConteudo.split('@');
					if (t_arr1StrSepArroba.length == 2) {
						var t_strAposArroba = t_arr1StrSepArroba[1];
						var t_arr1StrSepPontos = t_strAposArroba.split('.');
						if (t_arr1StrSepPontos.length < 2) {
							alert("Por favor, digite um conteúdo de e-mail no formato aaa@bbb.ccc!");
							t_nodSelecao.focus();
							return true;
						}
					} else {
						alert("Por favor, digite um conteúdo de e-mail no formato aaa@bbb.ccc!");
						t_nodSelecao.focus();
						return true;
					}
				}
			}
		}
		return false;
	}
	function seHaRepeticaoConfiguracoes(p_strId) {
		var t_nodSelecaoVerificar = document.getElementById("tipoConfiguracao_" + p_strId) ;
		if (t_nodSelecaoVerificar) {
			var t_nodConteudoVerificar = document.getElementById( "conteudo_" + p_strId);
			if (t_nodConteudoVerificar) {
				var t_arr1StrIdConfigGravadas  = obterIdsConfiguracoesGravadas();
				for (var t_strIdGravadoIdx in t_arr1StrIdConfigGravadas) {
					var t_strIdGravado = t_arr1StrIdConfigGravadas[t_strIdGravadoIdx];
					if (t_strIdGravado != p_strId) {
						var t_nodSelecaoGravado = document.getElementById("tipoConfiguracao_" + t_strIdGravado) ;
						if (t_nodSelecaoGravado) {
							var t_nodConteudoGravado = document.getElementById( "conteudo_" + t_strIdGravado);
							if (t_nodConteudoGravado) {
								if (t_nodSelecaoGravado.selectedIndex != 0 
										&& t_nodSelecaoVerificar.selectedIndex != 0) {
									if ((t_nodSelecaoGravado.selectedIndex == t_nodSelecaoVerificar.selectedIndex)
										&& (t_nodConteudoGravado.value == t_nodConteudoVerificar.value)) {
										alert("Por favor, retire o conteúdo repetido para a configuração!");
										t_nodSelecaoVerificar.focus();
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	function obterIdsConfiguracoesGravadas() {
		var t_arr1StrIdResult = new Array();
		var t_arr1NodInput = document.getElementsByName("idConfiguracao");
		for (var t_nodInputIdx = 0; t_nodInputIdx < t_arr1NodInput.length; t_nodInputIdx++ ) {
			var t_nodInput = t_arr1NodInput[t_nodInputIdx];
			if (t_nodInput) {
				t_arr1StrIdResult.push(t_nodInput.value);
			}
		}
		return t_arr1StrIdResult;
	}
	function excluirGrupo() {
		var resp = confirm("Deseja realmente excluir o grupo?")
		if (resp){
			var t_strUrl = 'excluir';
			if (t_strUrl) {
				t_strUrl = t_strUrl.split('?')[0];
				document.formulario.action =  t_strUrl;
				document.formulario.submit();
			}
		}
	}
	function sair() {
		var resp = confirm("Deseja realmente sair?\nCaso positivo, toda alteração realizada desde a última gravação será ignorada.")
		if (resp){
			var t_strUrl = 'listar'
			if (t_strUrl) {
				var t_nodTipoGrupoForm = document.formulario.idCpTipoGrupo;
				if (t_nodTipoGrupoForm) {
					t_strUrl = t_strUrl.split('?')[0];
					location.href = t_strUrl + '?idCpTipoGrupo=' + t_nodTipoGrupoForm.value ;
				} else {
					history.back();
				}
			}
		}
	}
	
	function limpar(id) {
		var t_nodLotacaoSelId = document.getElementById(id);
		if (t_nodLotacaoSelId) {
			t_nodLotacaoSelId.value="";
		}
	}
	
	function esconder(id) {
		var t_nod = document.getElementById(id);
		if (t_nod) {
			t_nod.style.display="none";
		}
	}
	
	function exibir(id) {
		var t_nod = document.getElementById(id);
		if (t_nod) {
			t_nod.style.display="inline";
		}
	}
	
	function solicitarInformacao(tarefa){
		var objSelecionado = document.getElementById('tipoConfiguracao_' + tarefa);
		limpar('formulario_lotacao_' + tarefa +'_lotacaoSel_id');
		limpar('formulario_matricula_' + tarefa + '_pessoaSel_id');
		limpar('formulario_cargo_' + tarefa + '_cargoSel_id');
		limpar('formulario_funcao_' + tarefa + '_funcaoSel_id');
		limpar('formulario_lotacao_' + tarefa + '_lotacaoSel_sigla');
		limpar('formulario_matricula_' + tarefa + '_pessoaSel_sigla');
		limpar('formulario_cargo_' + tarefa + '_cargoSel_sigla');
		limpar('formulario_funcao_' + tarefa + '_funcaoSel_sigla');
		limpar('formulario_lotacao_' + tarefa + '_lotacaoSel_descricao');
		limpar('formulario_matricula_' + tarefa + '_pessoaSel_descricao');
		limpar('formulario_cargo_' + tarefa + '_cargoSel_descricao');
		limpar('formulario_funcao_' + tarefa + '_funcaoSel_descricao');
		limpar('formulario_texto_' + tarefa);
		limpar('formulario_area_' + tarefa);
		limpar('conteudo_' + tarefa);
		if (objSelecionado) {
			switch (objSelecionado.selectedIndex){
				//Não Definido
				case 0:
					break;
				//Matrícula
				case 1:
					var tipo = "matricula";
					break;
				//Lotação	
				case 2:
					var tipo = "lotacao";
					break;
				//Cargo
				case 3:
					var tipo = "cargo";
					break;
				//Funcao Confianca	
				case 4:
					var tipo = "funcao";
					break;
				//Email	
				case 5:
					var tipo = "texto";
					break;
				//Formula	
				case 6:
					var tipo = "area";
					break; 
			}
			esconder('matricula_' + tarefa );
			esconder('lotacao_' + tarefa );
			esconder('cargo_' + tarefa );
			esconder('funcao_' + tarefa );
			esconder('texto_' + tarefa );
			esconder('area_' + tarefa );
			exibir(tipo + '_' + tarefa );
		}
	}
	function atualizarConteudo(tarefa) {
		var nodConteudo = document.getElementById("conteudo_" + tarefa) ;
		if (nodConteudo) {
			var objSelecionado = document.getElementById('tipoConfiguracao_' + tarefa);
			if (objSelecionado) {
				switch (objSelecionado.selectedIndex){
					//Não Definido
					case 0:
						break;
					//Matrícula
					case 1:
						var tipo = "matricula"; 
						var nodMatricula = document.getElementById('formulario_matricula_' + tarefa + '_pessoaSel_id');
						if (nodMatricula) {
							nodConteudo.value = nodMatricula.value;
						}
						break;
					//Lotação	
					case 2:
						var tipo = "lotacao";
						var nodLotacao = document.getElementById('formulario_lotacao_' + tarefa + '_lotacaoSel_id');
						if (nodLotacao) {
							nodConteudo.value = nodLotacao.value;
						}
						break;
					//Cargo	
					case 3:
						var tipo = "cargo";
						var nodCargo = document.getElementById('formulario_cargo_' + tarefa + '_cargoSel_id');
						if (nodCargo) {
							nodConteudo.value = nodCargo.value;
						}
						break;
					//funcao	
					case 4:
						var tipo = "funcao";
						var nodfuncao = document.getElementById('formulario_funcao_' + tarefa + '_funcaoSel_id');
						if (nodfuncao) {
							nodConteudo.value = nodfuncao.value;
						}
						break;
					// email
					case 5:
						var tipo = "texto";
						var nodTexto = document.getElementById('formulario_texto_' + tarefa);
						if (nodTexto) {
							nodConteudo.value = nodTexto.value;
						}
						break; 
					// formula
					case 6:
						var tipo = "area";
						var nodTexto = document.getElementById('formulario_area_' + tarefa);
						if (nodTexto) {
							nodConteudo.value = nodTexto.value;
						}
						break; 
				}
			}
		}
	} 
</script>
<siga:pagina titulo="Edição de ${cpTipoGrupo.dscTpGrupo}">
	<div class="container-fluid">
		<form name="formulario" id="formulario" method="post" cssClass="form">
			<div class="card bg-light mb-3" >
				<div class="card-header"><h5>Dados de ${cpTipoGrupo.dscTpGrupo}</h5></div>
					<input type="hidden" name="idCpTipoGrupo" value="${idCpTipoGrupo}" />
					<input type="hidden" name="idCpGrupo" value="${idCpGrupo}" />
					<div class="card-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<c:choose>
										<c:when test="${cpTipoGrupo.idTpGrupo != 2 or (cpTipoGrupo.idTpGrupo == 2 and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;INC:Incluir'))}">
											<label name="lblsiglaGrupo">Sigla</label>
											<input type="text" name="siglaGrupo" size="20" value="${siglaGrupo}" class="form-control"/>
										</c:when>
										<c:otherwise>
											<label name="lblsiglaGrupo">${siglaGrupo}</label>
											<input type="hidden" name="siglaGrupo" value="${siglaGrupo}" class="form-control"/>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label name="lblDscGrupo">Descrição</label>
									<input type="text" name="dscGrupo" size="40" value="${dscGrupo}" class="form-control"/>
								</div>
							</div>
						</div>
					</div>
				</div>		
				<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;INC:Incluir') and not empty idCpGrupo and cpTipoGrupo.idTpGrupo == 2}">
					<div class="card bg-light mb-3" >
						<table class="gt-form-table">
							<tr class="header" >
								<td colspan="2">Gestores do Grupo</td>
							</tr>
							<tr>
								<td colspan="2"><siga:selecao titulo="Nova Lotação Gestora:" propriedade="lotacaoGestora" modulo="siga"/></td>
							</tr>
							<c:forEach var="conf" items="${confGestores}">
								<tr>
									<td>${conf.lotacao.sigla} <span class="gt-cancel"><a href="excluirGestorGrupo.action?idConfGestor=${conf.id}&idCpGrupo=${idCpGrupo}">(excluir)</a></span></td>
								</tr>
							</c:forEach>
							<tr>
								<td><input type="button" id="btnGravarGestor" value="Incluir" onclick="javascript:gravarGestorGrupo()" class="gt-btn-medium gt-btn-left"/></td>
							</tr>
						</table>
					</div>
				</c:if>
				<c:if test="${cpTipoGrupo.idTpGrupo == 1}">
					<siga:selecao titulo="Pai:" propriedade="grupoPai" modulo="siga"/>
				</c:if>
			
				<div class="card bg-light mb-3" >
					<div class="card-header"><h5>Configurações de ${cpTipoGrupo.dscTpGrupo}</h5></div>
						<div class="card-body">
						<c:forEach var="configuracaoGrupo" items="${configuracoesGrupo}">
							<div class="row">
								<div class="col-sm-2">
									<div class="form-group">
										<select id="tipoConfiguracao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}" name="codigoTipoConfiguracaoSelecionada" onchange="javascript:solicitarInformacao('${configuracaoGrupo.cpConfiguracao.idConfiguracao}');" class="form-control">
									  		<option value="-1">[Remover]</option>									              
							          		<c:forEach items="${tiposConfiguracaoGrupoParaTipoDeGrupo}" var="item">
							           			<option value="${item.codigo}" ${item.codigo == configuracaoGrupo.tipo.codigo ? 'selected' : ''}>
							            			${item.descricao}
							           			</option>  
							          		</c:forEach>
							        	</select>
							         
										<input type="hidden" name="conteudoConfiguracaoSelecionada"
											id="conteudo_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											value="" /> 
									
										<input type="hidden" name="idConfiguracao"
											id="id_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											value="${configuracaoGrupo.cpConfiguracao.idConfiguracao}" />
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
									<!-- MATRÍCULA -->
									<div
										style="display:
										<c:choose> 
											<c:when test="${configuracaoGrupo.tipo.codigo == 0}">inline</c:when>
											<c:otherwise>none</c:otherwise>
										</c:choose>;"
										id="matricula_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<siga:selecao tipo="pessoa" tema="simple"
											propriedade="matricula_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											siglaInicial="${configuracaoGrupo.siglaConteudoConfiguracao}"
											idInicial="${configuracaoGrupo.idConteudoConfiguracao}"
											descricaoInicial="${configuracaoGrupo.descricaoConteudoConfiguracao}"
                                            modulo="siga"/>
									</div> <!-- LOTACAO -->
									<div
										style="display: 
										<c:choose> 
											<c:when test="${configuracaoGrupo.tipo.codigo == 1}">inline</c:when>
											<c:otherwise>none</c:otherwise>
										</c:choose>;"
										id="lotacao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
										class="">
										<siga:selecao tipo="lotacao" tema="simple"
											propriedade="lotacao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											siglaInicial="${configuracaoGrupo.siglaConteudoConfiguracao}"
											idInicial="${configuracaoGrupo.idConteudoConfiguracao}"
											descricaoInicial="${configuracaoGrupo.descricaoConteudoConfiguracao}"
                                            modulo="siga"/>
									</div> <!-- CARGO -->
									<div
										style="display: 
										<c:choose> 
											<c:when test="${configuracaoGrupo.tipo.codigo == 2}">inline</c:when>
											<c:otherwise>none</c:otherwise>
										</c:choose>;"
										id="cargo_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<siga:selecao tipo="cargo" tema="simple"
											propriedade="cargo_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											siglaInicial="${configuracaoGrupo.siglaConteudoConfiguracao}"
											idInicial="${configuracaoGrupo.idConteudoConfiguracao}"
											descricaoInicial="${configuracaoGrupo.descricaoConteudoConfiguracao}"
                                            modulo="siga"/>
									</div> <!-- funcao -->
									<div
										style="display: 
										<c:choose> 
											<c:when test="${configuracaoGrupo.tipo.codigo == 3}">inline</c:when>
											<c:otherwise>none</c:otherwise>
										</c:choose>;"
										id="funcao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<siga:selecao tipo="funcao" tema="simple"
											propriedade="funcao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											siglaInicial="${configuracaoGrupo.siglaConteudoConfiguracao}"
											idInicial="${configuracaoGrupo.idConteudoConfiguracao}"
											descricaoInicial="${configuracaoGrupo.descricaoConteudoConfiguracao}"
                                            modulo="siga"/>
									</div> <!-- EMAIL -->
									<div
										style="display: 
										<c:choose> 
											<c:when test="${configuracaoGrupo.tipo.codigo == 4}">inline</c:when>
											<c:otherwise>none</c:otherwise>
										</c:choose>;"
										id="texto_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<input type="text" size="64" maxlength="64"
											id="formulario_texto_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											name="formulario_texto_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											value="${configuracaoGrupo.conteudoConfiguracao}" />
									</div> <!-- FORMULA -->
									<div
										style="display: 
										<c:choose> 
											<c:when test="${configuracaoGrupo.tipo.codigo == 5}">inline</c:when>
											<c:otherwise>none</c:otherwise>
										</c:choose>;"
										id="area_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<textarea rows="3" cols="64"
											id="formulario_area_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											name="formulario_area_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">${configuracaoGrupo.conteudoConfiguracao}</textarea>
										</div>
									</div>
								</div>
							</div>
						
						</c:forEach>
					</div>
				</div>
				<div class="card bg-light mb-3" >
					<div class="card-header"><h5>Nova configuração</h5></div>
						<div class="card-body">
							<div class="row">
								<div class="col-sm-2">
									<div class="form-group">
							
										<select id="tipoConfiguracao_${idConfiguracaoNova}" name="codigoTipoConfiguracaoNova" onchange="javascript:solicitarInformacao('${idConfiguracaoNova}');" class="form-control">
								   			<option value="-1" selected >[Nenhuma]</option>              
					          				<c:forEach items="${tiposConfiguracaoGrupoParaTipoDeGrupo}" var="item">
						           				<option value="${item.codigo}">
						           					 ${item.descricao}
						          				</option>  
						         			</c:forEach>
						        		</select>
						        	</div>
						        </div>
						        <div class="col">
							     	<div style="display: none;" id="matricula_${idConfiguracaoNova}">
										<siga:selecao tipo="pessoa" tema="simple"
												propriedade="matricula_${idConfiguracaoNova}" modulo="siga"/>
									</div>
									<div style="display: none;" id="lotacao_${idConfiguracaoNova}">
										<siga:selecao tipo="lotacao" tema="simple"
											propriedade="lotacao_${idConfiguracaoNova}" modulo="siga"/>
									</div>
									<div style="display: none;" id="cargo_${idConfiguracaoNova}">
										<siga:selecao tipo="cargo" tema="simple"
											propriedade="cargo_${idConfiguracaoNova}" modulo="siga"/>
									</div>
									<div style="display: none;" id="funcao_${idConfiguracaoNova}">
										<siga:selecao tipo="funcao" tema="simple"
											propriedade="funcao_${idConfiguracaoNova}" modulo="siga"/>
									</div>
									<div style="display: none;" id="texto_${idConfiguracaoNova}">
										<input type="text" size="64" maxlength="64"
											name="formulario_texto_${idConfiguracaoNova}"
											id="formulario_texto_${idConfiguracaoNova}" />
									</div>
									<div style="display: none;" id="area_${idConfiguracaoNova}">
										<textarea rows="3" cols="64"
											name="formulario_area_${idConfiguracaoNova}"
											id="formulario_area_${idConfiguracaoNova}"></textarea>
									</div> <input type="hidden" name="conteudoConfiguracaoNova"
									id="conteudo_${idConfiguracaoNova}" value="" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<input type="button" id="btnGravar" value="Gravar" onclick="javascript:gravarGrupo()" class="btn btn-primary"/>
								<c:if test="${cpTipoGrupo.idTpGrupo != 2 or (cpTipoGrupo.idTpGrupo == 2 and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;EXC:Excluir'))}">
									<input type="button" id="btnExcluir" value="Excluir" onclick="javascript:excluirGrupo()" class="btn btn-primary"/>
								</c:if>
								<input type="button" id="btnSair" value="Sair" onclick="javascript:sair()" class="btn btn-primary"/>
							</div>
						</div>
					</div>
				</div>
					
			</div>
		</form>	
	</div>	
</siga:pagina>