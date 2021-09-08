<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina
	titulo="Configuração de Pessoas a ${dscTpConfiguracao}">	
		
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
			<div class="card-header"><h5>Configuração de Pessoas a ${dscTpConfiguracao}</h5></div>
			<div class="card-body">		
				<table border="0" class="table table-sm table-striped">
					<thead class="${thead_color}">
					<th>Matrícula</th>
					<th>Nome</th>
					<c:forEach var="servico" items="${cpServicosDisponiveis}">						
						<th><a href="#" alt="${servico.descricao}"
							title="${servico.descricao}">									
							<c:choose>
								<c:when test="${not empty servico.labelServico}">${servico.labelServico}</c:when>
								<c:otherwise>${servico.siglaServico}</c:otherwise>
							</c:choose>
							</a>
						</th>
					</c:forEach> </thead>
					<c:forEach var="pessoa" items="${dpPessoasDaLotacao}">
						<tr class="">
							<td>${pessoa.sesbPessoa}${pessoa.matricula}</td>
							<td>${pessoa.nomePessoa}</td>
							<c:forEach var="servico" items="${cpServicosDisponiveis}">								
								<c:forEach var="config" items="${cpConfiguracoesAdotadas}">
									<%--${config.dpPessoa.matricula} --- ${pessoa.matricula} :::: ${config.cpServico.idServico} --- ${servico.idServico}<br/> --%>
									<c:if
										test="${(config.dpPessoa.matricula == pessoa.matricula) && (config.cpServico.idServico == servico.idServico) }">
										<td><select name="configuracao_pessoa_servico"
											id="configuracao_${pessoa.idPessoa}_${servico.idServico}"
											valorSalvo="${config.cpSituacaoConfiguracao.id}"
											onchange="javascript:alterar(this, ${pessoa.idPessoa},${servico.idServico},${idTpConfUtilizarSvc});">
												<%--<c:forEach var="sit" items="${cpSituacoesPossiveis}"> --%>
												<c:forEach var="sit"
													items="${servico.cpTipoServico.cpSituacoesConfiguracaoSet}">
													<c:if
														test="${sit.id == config.cpSituacaoConfiguracao.id }">
														<option value="${sit.id}"
															selected="selected">${sit.descr}</option>
													</c:if>
													<c:if
														test="${sit.id != config.cpSituacaoConfiguracao.id }">
															<option value="${sit.id}">
																${sit.descr}</option>
														</c:if>
													</c:forEach>
											</select></td>
										</c:if>
									</c:forEach>
							</c:forEach>
						</tr>
					</c:forEach>
					<c:if test="${fn:length(pessoasGrupoSegManual) > 0}">
						<tr class="header">
							<td colspan="${2 + fn:length(cpServicosDisponiveis)}">Pessoas
								extras</td>
						</tr>
					</c:if>
					<c:forEach var="pessoa" items="${pessoasGrupoSegManual}">
						<tr class="">
							<td>	
								${pessoa.sesbPessoa}${pessoa.matricula}
								<input type="button" value="Excluir"
													class="btn btn-primary" onclick="javascript:location.href='excluir-pessoa-extra/${pessoa.id}'"/>
							</td>												
							<td>${pessoa.nomePessoa}</td>
							<c:forEach var="servico" items="${cpServicosDisponiveis}">
								<td>													
									<c:set  value="${ configuracaoConfManual.executar(pessoa.id, servico.id) }" 
										var="idSitConf" />										
									<div >
									<select	name="configuracao_pessoa_servico"
										id="configuracao_${pessoa.idPessoa}_${servico.idServico}"
										valorSalvo="${idSitConf}"
										onchange="javascript:alterar(this, ${pessoa.idPessoa},${servico.idServico},${idTpConfUtilizarSvcOutraLot});">
										<%--<c:forEach var="sit" items="${cpSituacoesPossiveis}"> --%>
										<c:forEach var="sit"
											items="${servico.cpTipoServico.cpSituacoesConfiguracaoSet}">
											<c:if test="${sit.id == idSitConf }">
												<option value="${sit.id}" selected="selected">
													${sit.descr}</option>
											</c:if>
											<c:if test="${sit.id != idSitConf }">
												<option value="${sit.id}">
													${sit.descr}</option>
											</c:if>
										</c:forEach>
								</select></td>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div class="card bg-light mb-3" >
			
				<form id="frmPessoaExtra"  action="inserirPessoaExtra" method="post">
					
					<div class="card-header"><h5>Inserir Pessoa Extra</h5></div>
					<div class="card-body">
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
									<label>Pessoa</label>
									<siga:selecao tipo="pessoa"
										tema="simple" propriedade="pessoaExtra" modulo="siga"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
									<button type="submit" class="btn btn-primary">
										Inserir Pessoa
									</button>
								</div>
							</div>
						</div>
					</div>
				</form>
		</div>
	</div>
</siga:pagina>

<script type="text/javascript">


	alterar = function (obj, p_strIdPessoa,p_strIdServico,p_strIdTpConf) {
		var t_strIdSituacao = obj.value;
		var t_smrRequisicao = new SimpleMethodRequestRPCGet();
		var t_strUrl = 'gravar';
		t_smrRequisicao.setUrl(t_strUrl);
		t_smrRequisicao.addUrlParam("idPessoaConfiguracao", p_strIdPessoa);
		t_smrRequisicao.addUrlParam("idServicoConfiguracao", p_strIdServico);
		t_smrRequisicao.addUrlParam("idSituacaoConfiguracao", t_strIdSituacao);
		t_smrRequisicao.addUrlParam("idTipoConfiguracao", p_strIdTpConf);
		try {
			var t_objResult = t_smrRequisicao.submeterSincrono();
			if (t_objResult.idpessoa != p_strIdPessoa) {
				throw new Error("Pessoa gravada (" 
						+ t_objResult.idpessoa
						+ ") diferente da enviado (" 
						+ p_strIdPessoa
						+  "). \nAlteração não confirmada ! ");
			}
			if (t_objResult.idservico != p_strIdServico) {
				throw new Error("Serviço gravado (" 
								+ t_objResult.idservico 
								+ ") diferente do enviado (" 
								+ p_strIdServico 
								+  "). \nAlteração não confirmada ! ");
			}
			if (t_objResult.idsituacao != t_strIdSituacao) {
				throw new Error("Situação gravada (" 
						+ t_objResult.idsituacao
						+ ") diferente da enviada (" 
						+ t_strIdSituacao 
						+  "). \nAlteração não confirmada ! ");
			}
			confirmarValor(obj,t_strIdSituacao);
			hover(obj.parentNode,"[OK]");
			setTimeout(
				function () {
					delhover(obj.parentNode);
				}
			, 2000);
		} catch (e) {
			if (e.description) {
				alert(e.description);
			} else {
				alert (e.message);
			}
			restaurarValor(obj);
			return;
		}
	}
	/**
	*  O valor confirmado da opção selecionada 
	*  fica gravado no atributo valorSalvo
	*  quando a ação não é confirmada a opção selecionada
	*   recebe o conteúdo deste atributo   
	*/
	function restaurarValor(p_domSituacao) {
		t_strValorSalvo = p_domSituacao.getAttribute("valorSalvo");
		var t_idxSelecionado = -1;
		for(var t_intConta = 0; t_intConta < p_domSituacao.length; t_intConta++){
          if( p_domSituacao.options[t_intConta].value == t_strValorSalvo ){
             t_idxSelecionado = t_intConta;
           }
        }
        if (t_idxSelecionado >= 0) {
        	p_domSituacao.selectedIndex = t_idxSelecionado;
        }
	}
	/**
	*  O valor confirmado da opção selecionada 
	*  fica gravado no atributo valorSalvo
	*  quando a ação é confirmada o conteúdo
	*  do valor salvo é atribuido para a opção selecionada   
	*/
	function confirmarValor(p_domSituacao,p_strValor) {
		p_domSituacao.setAttribute("valorSalvo",p_strValor);
	}
	function obterConteudoCampoDomResposta(p_domResposta, p_strNomeNode) {
		var t_nodTextoMensagem = p_domResposta.getElementsByTagName(p_strNomeNode)[0];
		if (t_nodTextoMensagem) {
			var t_txnTextoMensagem = t_nodTextoMensagem.firstChild;
			if (t_txnTextoMensagem) {
				return t_txnTextoMensagem.nodeValue ;
			} 
		} 
		return null;
	}
	function hover(p_obj,p_strDescricao) {
		setTimeout( function () {
			try {
				var t2_obj = p_obj;
				var t2_strDescricao = p_strDescricao; 
				if (p_obj) {
					if (!document.getElementById("hoverdiv")) {
						p_objHover = document.createElement("div");
						t2_obj.appendChild(p_objHover);
						p_objHover.setAttribute("id","hoverdiv");
						p_objHover.style.position = "absolute";
						p_objHover.style.display = "block";
						p_objHover.style.zindex = "100";
						p_objHover.style.width = "25px";
						p_objHover.style.height = "15px";
						p_objHover.style.top = (posY(p_obj) + 5 ) + "px";
						p_objHover.style.left = (posX(p_obj) - 25) + "px";
						p_objHover.style.overflow = "visible";
						p_objHover.style.backgroundColor = "#c0c0c0";
						p_objHover.style.color = "black";
						p_objHover.innerHTML = t2_strDescricao;
					}
				}
			} catch (e) {
				// não faz nada
			}
		}, 50);
	}
	function delhover(p_obj) {
		if (p_obj) {
			setTimeout( function() {
				try {
					var t2_obj = p_obj;
					var t_objFilho = document.getElementById("hoverdiv");
					if (t_objFilho) {
						t2_obj.removeChild(t_objFilho);
					}
				} catch (e) {
					// não faz nada
				}
			}, 50); 
		}
	}
	 function posX(p_obj)
	  {
		var t_obj = p_obj;
	    var t_intCurLeft = 0;
	    if(p_obj.offsetParent)
	        while(1) 
	        {
	        	t_intCurLeft += t_obj.offsetLeft;
	          if(!t_obj.offsetParent)
	            break;
	          t_obj = t_obj.offsetParent;
	        }
	    else if(t_obj.x)
	    	t_intCurLeft += t_obj.x;
	    return t_intCurLeft;
	  }

	  function posY(p_obj)
	  {
		var t_obj = p_obj;
	    var t_intCurTop = 0;
	    if(t_obj.offsetParent)
	        while(1)
	        {
	        	t_intCurTop += t_obj.offsetTop;
	          if(!t_obj.offsetParent)
	            break;
	          t_obj = t_obj.offsetParent;
	        }
	    else if(t_obj.y)
	    	t_intCurTop += t_obj.y;
	    return t_intCurTop;
	  }
	  
	   /* function inserirPessoaExtra(){
	  	var frm = document.getElementById("frmPessoaExtra");
	  	frm.action = "inserirPessoaExtra";
	  	frm.submit();
	  }  */
		
</script>