<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

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
							<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2"/> 
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
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
		}
		
	});

	$("#ok").click(function(){
		var usuarios = document.getElementById("usu").value;
		if(usuarios.length > 1) {
			frm.submit();
		} else {
			alert("Selecione pelo menos uma pessoa.");
		}
	});

	function remover(id) {
		$('#div' + id).remove();
		document.getElementById("usu").value = document.getElementById("usu").value.replace(";"+id+";",";"); 
	}
</script>
