<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%-- 
#{ifErrors}
    <div id="divErros" class="gt-error">
    %{
         i = 0 
	}%
    #{errors} 
    #{if error.message() == 'Required'}
         %{
             i = i + 1
             if (i == 1) { 
		 }%
             <li>&{'views.erro.campoObrigatorio'}</li>
         %{
             } 
		 }%
    #{/if}  
    
    #{if error.key.contains('LinkErro')}
		#{tp.tags.errolink error.message(),
		      			   comando: @Missoes.buscarPelaSequence('parse',true),
		      			   classe: error.key.replace('LinkErro','')}
		#{/tp.tags.errolink} 
    #{/if}
    
    #{if error.key.contains('LinkGenericoErro')}
		#{tp.tags.erroGenericolink error.message(), comando: '<a href="#" onclick="javascript:window.history.back();">Voltar</a>'}
		#{/tp.tags.erroGenericolink} 
    #{/if}
            
    #{if error.message() != 'Validation failed' && error.message() != 'Required'  && !error.key.contains('LinkErro') && !error.key.contains('LinkGenericoErro')}
 		  <li>${error.raw()}</li>
    #{/if}
    #{/errors}
    </div>
 #{/ifErrors}
 </br>
 --%>