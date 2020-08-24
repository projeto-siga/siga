<%@ tag body-content="scriptless"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ attribute name="idModal" required="true" 
	description="Identificador único do modal"%>

<style>
	.container-senha-alterada-sucesso {
  		background-color: white;
	   	width: 100%;
	   	height: 100%;
	   	position: absolute;
	   	margin: 0;
	   	padding: 0;    
	   	top: 0;
		right: 0;
		visibility: hidden;
		opacity: 0;
	   	z-index: 99999;
	   	transition: opacity .5s;
  	}
  					
	.icone-senha-alterada-sucesso {
  		text-align: center;
   		color: #28a745;
   		font-size: 5em;    
   		margin: 20px 0;
		padding-top: 5%;
	}
  					
  	.titulo-senha-alterada-sucesso {
  		text-align: center;
		font-size: 1.2em;
  	}
  	 
	.texto-senha-alterada-sucesso {
  		text-align: center;
  		font-size: 1em;
  	}  					
  						
  	.troca-senha-usuario-btn {
  		width: 80px;
  		height: 38px;
  	}  	
  						
	.titulo-modal-troca-senha {
  		font-size: 1em;        						
   		margin-top: 15px;    						
  	}					
</style>
<siga:siga-modal id="${idModal}" centralizar="true" botaoFecharNoCabecalho="false" tituloADireita="<i class='fas fa-key'></i> Troca de senha">
	<div class="modal-body">                            
        <div class="alert  alert-danger  alert-dismissible  hidden  js-mensagem-erro" role="alert">
            <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span></button>
            <div class="js-mensagem-erro-textos">
                <div><i class="fa  fa-exclamation-circle"></i> Ocorreu um erro aqui</div>
            </div>  
        </div>                  

        <div class="container-senha-alterada-sucesso">
            <div>
                <p class="icone-senha-alterada-sucesso"><i class="fas fa-check-circle"></i></p>
                <h1 class="titulo-senha-alterada-sucesso">Senha alterada com sucesso!</h1>
                <h2 class="texto-senha-alterada-sucesso">Você será redirecionado para a página inicial...</h2>
            </div>                      
        </div>

        <form class="form-troca-senha" action="/siga/public/app/login/novaSenha">
            <div class="form-group">                                                    
                <div class="input-group">
                    <div class="input-group-prepend">
                        <div class="input-group-text" id="icon-user"><i class="fas fa-user"></i></div>
                    </div>
                    <input id="loginUsuario" type="hidden" value="${loginUsuario}"/>
                    <input id="cont" type="hidden" value="${cont}"/>
                    <input id="cpfUsuario" type="text" class="form-control" autofocus="autofocus" placeholder="CPF" maxlength="14"/>
                    <div class="invalid-feedback">
                        Favor informar um CPF válido
                    </div>                              
                </div>
            </div>
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <div class="input-group-text" id="icon-user"><i class="fas fa-lock"></i></div>
                    </div>
                    <input id="senhaAtualUsuario" type="password" class="form-control" placeholder="Senha atual" maxlength="40"/>
                    <div class="invalid-feedback">
                        Senha atual incorreta
                    </div>                                                            
                </div>                          
            </div>
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <div class="input-group-text" id="icon-user"><i class="fas fa-lock"></i></div>
                    </div>
                    <input id="novaSenhaUsuario" type="password" class="form-control" placeholder="Nova senha" maxlength="40"/>
                    <div class="invalid-feedback">
                        Nova senha incorreta
                    </div>                                                            
                </div>                              
            </div>
            <div class="form-group">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <div class="input-group-text" id="icon-user"><i class="fas fa-lock"></i></div>
                    </div>
                    <input id="confirmacaoNovaSenhaUsuario" type="password" class="form-control" placeholder="Confirmação da senha" maxlength="40"/>
                    <div class="invalid-feedback">
                        Confirmação da senha incorreta
                    </div>                                                            
                </div>                              
            </div>
            <c:if test="${not siga_cliente eq 'GOVSP'}">    
                <div class="form-group">
                    <div class="form-check">
                        <input type="checkbox" checked="checked" id="trocarSenhaRedeUsuario"
                            name="usuario.trocarSenhaRede" class="form-check-input"></input>                                        
                        <label class="form-check-label" for="trocarSenhaRedeUsuario">
                            Trocar também a senha do computador, da rede e do e-mail 
                        </label>
                    </div>  
                </div>
            </c:if>            
            <button type="submit" class="btn  btn-primary  troca-senha-usuario-btn  js-troca-senha-usuario-btn" disabled>                       
                Salvar                      
            </button>
            <a href="#" class="btn  btn-default  js-senha-usuario-cancelar" data-dismiss="modal">Cancelar</a>                       
        </form>                                
    </div>   			
</siga:siga-modal>