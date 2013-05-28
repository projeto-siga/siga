<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<ww:url id="urlGravar" action="gravar" />
<ww:url id="urlExcluir" action="excluir" />
<ww:url id="urlBuscar" action="listar" />
<script type="text/javascript" language="Javascript1.1">
	function gravarGrupo() {
		var t_strIdConfiguracaoNova = '${idConfiguracaoNova}';
		var t_strUrl = '${urlGravar}';
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
		var resp = confirm("Deseja realmente exluir o grupo?")
		if (resp){
			var t_strUrl = '${urlExcluir}';
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
			var t_strUrl = '${urlBuscar}'
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
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>${cpTipoGrupo.dscTpGrupo}</h2>
			<div class="gt-content-box gt-for-table">
				<ww:form name="formulario" id="formulario" method="POST"
					cssClass="form">
					<table class="gt-form-table">
						<tr class="header">
							<td colspan=2>Dados de ${cpTipoGrupo.dscTpGrupo}</td>
						</tr>
						<input type="hidden" name="idCpTipoGrupo" value="${idCpTipoGrupo}" />
						<input type="hidden" name="idCpGrupo" value="${idCpGrupo}" />
						<ww:if
							test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;INC:Incluir')}">
							<ww:textfield label="Sigla" name="siglaGrupo" size="20" />
						</ww:if>
						<ww:else>
							<ww:label label="Sigla" name="siglaGrupo" value="${siglaGrupo}" />
							<ww:hidden name="siglaGrupo" value="${siglaGrupo}"></ww:hidden>
						</ww:else>

						<ww:textfield label="Descrição" name="dscGrupo" size="40" />
						<siga:selecao titulo="Lotação Gestora:" propriedade="lotacaoGestora" />
						
						
						<c:if test="${cpTipoGrupo.idTpGrupo == 1}">
							<siga:selecao titulo="Pai:" propriedade="grupoPai" />
						</c:if>
					</table>

					<br />

					<table class="gt-form-table">
						<tr class="header">
							<td>Configurações de ${cpTipoGrupo.dscTpGrupo}</td>
						</tr>
						<c:forEach var="configuracaoGrupo" items="${configuracoesGrupo}">
							<tr class="">
								<td valign="top"><ww:select
										id="tipoConfiguracao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
										name="codigoTipoConfiguracao"
										list="tiposConfiguracaoGrupoParaTipoDeGrupo" theme="simple"
										listValue="descricao" listKey="codigo" headerValue="[Remover]"
										headerKey="-1" value="${configuracaoGrupo.tipo.codigo}"
										onchange="javascript:solicitarInformacao('${configuracaoGrupo.cpConfiguracao.idConfiguracao}');" />
									<input type="hidden" name="conteudoConfiguracao"
									id="conteudo_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
									value="" /> <input type="hidden" name="idConfiguracao"
									id="id_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
									value="${configuracaoGrupo.cpConfiguracao.idConfiguracao}" />



									<!-- MATRÍCULA -->
									<div
										style="display: <ww:if test="${configuracaoGrupo.tipo.codigo == 0}">inline</ww:if><ww:else>none</ww:else>;"
										id="matricula_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<siga:selecao tipo="pessoa" tema="simple"
											propriedade="matricula_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											siglaInicial="${configuracaoGrupo.siglaConteudoConfiguracao}"
											idInicial="${configuracaoGrupo.idConteudoConfiguracao}"
											descricaoInicial="${configuracaoGrupo.descricaoConteudoConfiguracao}" />
									</div> <!-- LOTACAO -->
									<div
										style="display: <ww:if test="${configuracaoGrupo.tipo.codigo == 1}">inline</ww:if><ww:else>none</ww:else>;"
										id="lotacao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
										class="">
										<siga:selecao tipo="lotacao" tema="simple"
											propriedade="lotacao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											siglaInicial="${configuracaoGrupo.siglaConteudoConfiguracao}"
											idInicial="${configuracaoGrupo.idConteudoConfiguracao}"
											descricaoInicial="${configuracaoGrupo.descricaoConteudoConfiguracao}" />
									</div> <!-- CARGO -->
									<div
										style="display: <ww:if test="${configuracaoGrupo.tipo.codigo == 2}">inline</ww:if><ww:else>none</ww:else>;"
										id="cargo_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<siga:selecao tipo="cargo" tema="simple"
											propriedade="cargo_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											siglaInicial="${configuracaoGrupo.siglaConteudoConfiguracao}"
											idInicial="${configuracaoGrupo.idConteudoConfiguracao}"
											descricaoInicial="${configuracaoGrupo.descricaoConteudoConfiguracao}" />
									</div> <!-- funcao -->
									<div
										style="display: <ww:if test="${configuracaoGrupo.tipo.codigo == 3}">inline</ww:if><ww:else>none</ww:else>;"
										id="funcao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<siga:selecao tipo="funcao" tema="simple"
											propriedade="funcao_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											siglaInicial="${configuracaoGrupo.siglaConteudoConfiguracao}"
											idInicial="${configuracaoGrupo.idConteudoConfiguracao}"
											descricaoInicial="${configuracaoGrupo.descricaoConteudoConfiguracao}" />
									</div> <!-- EMAIL -->
									<div
										style="display: <ww:if test="${configuracaoGrupo.tipo.codigo == 4}">inline</ww:if><ww:else>none</ww:else>;"
										id="texto_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<input type="text" size="64" maxlength="64"
											id="formulario_texto_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											name="formulario_texto_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											value="${configuracaoGrupo.conteudoConfiguracao}" />
									</div> <!-- FORMULA -->
									<div
										style="display: <ww:if test="${configuracaoGrupo.tipo.codigo == 5}">inline</ww:if><ww:else>none</ww:else>;"
										id="area_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">
										<textarea rows="3" cols="64"
											id="formulario_area_${configuracaoGrupo.cpConfiguracao.idConfiguracao}"
											name="formulario_area_${configuracaoGrupo.cpConfiguracao.idConfiguracao}">${configuracaoGrupo.conteudoConfiguracao}</textarea>
									</div>
								</td>
							</tr>
						</c:forEach>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr class="header">
							<td>Nova configuração</td>
						</tr>
						<tr class="">
							<td><ww:select id="tipoConfiguracao_${idConfiguracaoNova}"
									name="codigoTipoConfiguracaoNova"
									list="tiposConfiguracaoGrupoParaTipoDeGrupo"
									listValue="descricao" listKey="codigo" headerValue="[Nenhuma]"
									headerKey="-1" theme="simple"
									onchange="javascript:solicitarInformacao('${idConfiguracaoNova}');" />
								<div style="display: none;" id="matricula_${idConfiguracaoNova}">
									<siga:selecao tipo="pessoa" tema="simple"
										propriedade="matricula_${idConfiguracaoNova}" />
								</div>
								<div style="display: none;" id="lotacao_${idConfiguracaoNova}">
									<siga:selecao tipo="lotacao" tema="simple"
										propriedade="lotacao_${idConfiguracaoNova}" />
								</div>
								<div style="display: none;" id="cargo_${idConfiguracaoNova}">
									<siga:selecao tipo="cargo" tema="simple"
										propriedade="cargo_${idConfiguracaoNova}" />
								</div>
								<div style="display: none;" id="funcao_${idConfiguracaoNova}">
									<siga:selecao tipo="funcao" tema="simple"
										propriedade="funcao_${idConfiguracaoNova}" />
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
								id="conteudo_${idConfiguracaoNova}" value="" /></td>
						</tr>
						<tr>
							<td>
								<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;ALT:Alterar')}">
									<input type="button" id="btnGravar" value="Gravar" onclick="javascript:gravarGrupo()" class="gt-btn-medium gt-btn-left"/>
								</c:if>
								<c:if test="${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;GI;GDISTR;EXC:Excluir')}">
									<input type="button" id="btnExcluir" value="Excluir" onclick="javascript:excluirGrupo()" class="gt-btn-medium gt-btn-left"/>
								</c:if>
								<input type="button" id="btnSair" value="Sair" onclick="javascript:sair()" class="gt-btn-medium gt-btn-left"/>
							</td>
						</tr>
					</table>
				</ww:form>
			</div>
		</div>
	</div>
</siga:pagina>