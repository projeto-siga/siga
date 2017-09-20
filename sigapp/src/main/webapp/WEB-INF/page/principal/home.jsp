<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<siga:pagina titulo="Home Page SigaPP">
	<div class="ui-widget" style="position: absolute;left:2%;right:2%">
		Hor&aacute;rios:<br>
		<c:forEach items="${lstLocais}" var="av">
			${av.local} ${av.dias} ${av.hora_ini} ${av.hora_fim} ${av.endereco} <br> <br>
		</c:forEach>
		<br><br><br>
		<div id="div_mural">
			<c:forEach items="${vetorForuns}" var="texto">
				${texto}	
				<br>
			</c:forEach>
		</div>
		<br>
		<br>
		<table border="2px" cellspacing="1px" cellpadding="2px" style="font-size:100%;" >
			<tr>
				<th>
					ATEN&Ccedil;&Atilde;O:
					A fim de evitar que os periciandos aguardem excessivamente por atendimento no espa&ccedil;o de espera das salas de per&iacute;cia nas depend&ecirc;ncias do Foro, &eacute; importante que o hor&aacute;rio para seu comparecimento &agrave; per&iacute;cia, constante do mandado de intima&ccedil;&atilde;o, seja igual ao hor&aacute;rio efetivamente agendado no sistema SigaPP. 
					Veja mais informa&ccedil;&otilde;es no Of&iacute;cio Circular  <a href="http://portalintranet/documentos/grupo_172/oficios_circulares/2012/rjocd201200063a.pdf">RJ-OCD-2012/00063</a>. 
				</th>
			</tr>
		</table>
	</div> 
</siga:pagina>