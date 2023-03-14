<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Restringir Acesso">

	<c:if test="${not mob.doc.eletronico}">
		<script type="text/javascript">$("html").addClass("fisico");$("body").addClass("fisico");</script>
	</c:if>
<style>
	.usuario-selecionado:nth-child(odd) {
		background-color: #E1E1E1;	
	}
	
	.usuario-selecionado:hover {
		background-color: rgba(0, 123, 255, 0.18);	
	}
	
	.botao {
        width: 35px;
        height: 30px;
        border: none;            
        border-radius: 10%;
        cursor: pointer;                        
        color: #FFFFFF;            
        margin-top: 4px;        
        font-size: 12px;        
      }
          
      .botao.botao-adicionar {
        background-color: #28a745;
        transition: .3s;                      
      }

      .botao.botao-adicionar:hover {
        background-color: #218838;
      }
      
      .botao.botao-adicionar:focus {  
        outline: 0;                          
        box-shadow: 0 0 0 0.2rem rgba(40,167,69,.5);           
      } 

      .botao.botao-remover {
        background-color: #dc3545;
        transition: .3s;  
        float: right;
        margin: 4px;        
      }

      .botao.botao-remover:hover {
        background-color: #bd2130;                    
        transition: .3s;   
      }       

      .botao.botao-remover:focus {  
        outline: 0;                          
        box-shadow: 0 0 0 0.2rem rgba(220,53,69,.5);
      }       
</style>
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>
					Restringir Acesso - ${mob.siglaEDescricaoCompleta}
				</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="restringir_acesso_gravar" namespace="/expediente/mov" theme="simple" method="post">
					<input type="hidden" name="postback" value="1" />
					<input type="hidden" name="sigla" value="${sigla}"/>
       				<input type="hidden" name="usu" id="usu" value=""/>
       				<input type="hidden" id="resetarRestricaoAcesso" name="resetarRestricaoAcesso" value="false"/>
       				<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label>Nível de Acesso</label>
								<select class="form-control" name="nivelAcesso" theme="simple" value="1">
							      <c:forEach var="item" items="${listaNivelAcesso}">
							      	<c:if test="${(siga_cliente ne 'GOVSP') || ( (item.idNivelAcesso eq 5) )}">
								        <option value="${item.idNivelAcesso}"  <c:if test="${item.idNivelAcesso == nivelAcesso}">selected</c:if> >
								          <c:out value="${item.nmNivelAcesso}" />
								        </option>
							        </c:if>
							      </c:forEach>
							    </select>
							</div>
						</div>
						
						<c:if test="${not empty listaAcessoRestrito}">
							<div class="col-sm-4">
								<div class="form-group">
									<div class="form-check form-check-inline mt-4">
										<input type="checkbox" id="chkResetDefault" name="chkResetDefault" class="form-check-input ml-3"  />
										<label class="form-check-label" for="chkResetDefault">Retornar ao Nível de Acesso Padrão</label>
									</div>
								</div>
							</div>
						</c:if>
						
					</div>	
					<div class="row">
						<div class="col-sm-12">
							<label>Indique os usuários que terão acesso ao documento</label>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<siga:selecao tipo="pessoa" propriedade="pessoaObjeto"
									tema="simple" modulo="siga" />
						</div>
						<div class="col-sm-6 p-0">
							<button type="button" class="botao botao-adicionar" id="adicionar" title="adicionar usuário"><i class="fas fa-plus"></i></button>                			                			
						</div>
					</div>	
					<div class='row bg-light' id="divUsuarios">
						<!-- Pessoas selecionadas -->		
									
					</div>				

					<div class="row">
						<div class="col-sm mt-3">
							<input type="button" value="Ok" id="ok" class="btn btn-primary"/> 
							<input type="button" value="Voltar" onclick="javascript:location.href='${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}';" class="btn btn-cancel ml-2" />
						</div>
					</div>
				</form>
			</div>
		</div>
		<c:if test="${not empty listaAcessoRestrito}">
			<h4 class="gt-table-head">Lista de Restrição de Acesso</h4>	
			<div class="table-responsive">
				<table border="0" class="table table-sm table-striped">
					<thead class="thead-dark">
						<tr>
							<th align="left" width="10%">Matrícula</th>
							<th align="left">Nome</th>						
							<th align="left"><fmt:message key="usuario.lotacao"/></th>
							<th align="left">Função</th>
							<th align="left" width="5%">Excluir</th>					
						</tr>
					</thead>
					<tbody>
						<c:forEach var="mov" items="${listaAcessoRestrito}">
							<tr>
								<td>${mov.subscritor }</td>
								<td>${mov.subscritor.nomePessoa }</td>
								<td>${mov.subscritor.lotacao.nomeLotacao }</td>
								<td>${mov.subscritor.funcaoString }</td>
								<td><input type="button" value="Excluir" 
									onclick="javascript:excluirRestricao(${mov.idMov});" class="btn btn-danger"/>					
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>
	</div>
	
	<siga:siga-modal id="confirmacaoModal" exibirRodape="false" tituloADireita="Confirmação">
		<div id="msg" class="modal-body">
       		Deseja concluir a operação realizada? 
     	</div>
     	<div class="modal-footer">
     	    <a href="#" class="btn btn-success siga-modal__btn-acao" role="button" aria-pressed="true" onclick="">Sim</a>
       		<button type="button" class="btn btn-danger" data-dismiss="modal" onclick="cancelar();">Não</button>		        
		</div>
	</siga:siga-modal>
</siga:pagina>


<script>
	var temUsuarioSelecionado = false;

	$("#adicionar").click(function(){
		var id = document.getElementById('formulario_pessoaObjeto_pessoaSel_id').value;
		var matricula = document.getElementById('formulario_pessoaObjeto_pessoaSel_sigla').value;
		var nome = document.getElementById('formulario_pessoaObjeto_pessoaSel_descricao').value;
		var usu = document.getElementById("usu").value;					
		
		if(id != "") {								
			if (!temUsuarioSelecionado) 
				$("#divUsuarios").append("<div class='col-sm-12 mt-3 mb-1'><label><b>Pessoas Selecionadas</b></label></div>");
			
	    	$("#divUsuarios").append("<div class='col-sm-12 mb-1 usuario-selecionado' id='div"+id+"'><span style='position: absolute; padding-top: 6px'>" + nome  + " (" + matricula + ")</span> <button type='button' class='botao botao-remover' onclick='remover("+id+");' title='remover "+nome+"'><i class='fas fa-minus'></i></button></div>");	    	
	    	if(usu == "") {
				usu = ";";
			}
			
	    	temUsuarioSelecionado = true;
	    	document.getElementById("usu").value = usu + id + ";";
	    	
	    	//Limpa usuário adicionado
			document.getElementById('formulario_pessoaObjeto_pessoaSel_sigla').value = "";
			ajax_pessoaObjeto_pessoa();
		}
		
	});

	$("#ok").click(function(){
		var usuarios = document.getElementById("usu").value;
		if ((usuarios.length > 1) || (document.getElementById("chkResetDefault") !== null && document.getElementById("chkResetDefault").checked) ) {
			mostrarModalConfirmacao('javascript:confirmarAlteracao();');
		} else {
			alert("Selecione pelo menos uma pessoa.");
		}	
	});
	
	function mostrarModalConfirmacao(acaoBotaoConfirmacao) {
		document.getElementById("ok").disabled = true;
		sigaModal.alterarLinkBotaoDeAcao('confirmacaoModal',acaoBotaoConfirmacao);
		sigaModal.abrir('confirmacaoModal'); 
		sigaModal.reabilitarBotaoAposFecharModal('confirmacaoModal','ok');
	}
	
    function confirmarAlteracao() {
    	sigaSpinner.mostrar();
    	//reset restrição acesso
    	if (document.getElementById("chkResetDefault") !== null && document.getElementById("chkResetDefault").checked) {
	        document.getElementById("resetarRestricaoAcesso").value = true;
    	} 
        sigaModal.fechar('confirmacaoModal');
        document.getElementById("ok").disabled = false;
        frm.submit();
    }
    
    function excluirRestricao(idMov) {
    	mostrarModalConfirmacao('${pageContext.request.contextPath}/app/expediente/mov/cancelar_restricao_acesso?idMovRestricao='+ idMov +'&redirectURL=/app/expediente/mov/restringir_acesso?sigla=${sigla}');
    }
    
    function cancelar() {
    	document.getElementById("ok").disabled = false;
        sigaModal.fechar('confirmacaoModal');
    }

	function remover(id) {
		$('#div' + id).remove();
		document.getElementById("usu").value = document.getElementById("usu").value.replace(";"+id+";",";"); 
	}
</script>
