<%@tag import="com.ckeditor.CKEditorInsertTag"%>
<%@tag import="com.ckeditor.CKEditorTag"%>
<%@tag import="com.ckeditor.CKEditorConfig"%>
<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://ckeditor.com" prefix="FCK"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ attribute name="titulo"%>
<%@ attribute name="var"%>
<%@ attribute name="entrevista"%>
<%@ attribute name="modeloPrenchido"%>
<%@ attribute name="nmArqMod"%>
<%@ attribute name="conteudo"%>
<%@ attribute name="semBotaoSalvar"%>


<c:set var="v" value="${param[var]}" />
<c:if test="${empty v}">
	<c:set var="v" value="${requestScope[var]}" />
</c:if>
<c:if test="${empty v}">
	<c:set var="v" value="${conteudo}" />
</c:if>

<input type="hidden" name="vars" value="${var}" />
<input type="hidden" id="desconsiderarExtensao" name="desconsiderarExtensao" value="${par.desconsiderarExtensao[0]}" />

<div><c:if test="${not empty titulo}">
	<b>${titulo}</b>
</c:if> <c:choose>
	<c:when test="${param.entrevista == 1}">
	
		<c:if test="${empty v}">
							<c:set var="v" value='<p style="TEXT-INDENT: 2cm" align="justify">&nbsp;</p>'/>
					</c:if>
	
		<%
			String s = (String) jspContext.getAttribute("v");
					jspContext.setAttribute("v",
							br.gov.jfrj.siga.ex.bl.Ex.getInstance().getBL().canonicalizarHtml(s, false, true, false,
											true));
					String var1 = (String) jspContext.getAttribute("var");
					request.setAttribute(var1, jspContext.getAttribute("v"));
		%>

		<div><c:choose>
			<c:when test="${f:podeUtilizarExtensaoEditor(lotaTitular, idMod) && !par.desconsiderarExtensao[0]}">
				<input type="hidden" id="${var}" name="${var}" value="<c:out value="${v}" escapeXml="true" />" >
				${f:obterExtensaoEditor(lotaTitular.orgaoUsuario, var, v, par.serverAndPort[0])}
			</c:when>
			<c:otherwise>
			<tags:fixeditor var="${var}">
					<c:if test="${empty v}">
							<c:set var="v" value='<p style="TEXT-INDENT: 2cm" align="justify"></p>'/>
					</c:if>
					<textarea cols="100" id="xxxeditorxxx" name="xxxeditorxxx" rows="20" class="editor">${v}</textarea>
					
					
					 	<script type="text/javascript">

								CKEDITOR.config.disableNativeSpellChecker = false;
								CKEDITOR.config.scayt_autoStartup = false;
								CKEDITOR.config.scayt_sLang = 'pt_BR';
								CKEDITOR.config.stylesSet = 'siga_ckeditor_styles';
								
								CKEDITOR.stylesSet.add('siga_ckeditor_styles', [{
								        name: 'Título',
								        element: 'h1',
								        styles: {
								            'text-align': 'justify',
								            'text-indent': '2cm'
								        }
								    },
								    {
								        name: 'Subtítulo',
								        element: 'h2',
								        styles: {
								            'text-align': 'justify',
								            'text-indent': '2cm'
								        }
								    },
								    {
								        name: 'Com recuo',
								        element: 'p',
								        styles: {
								            'text-align': 'justify',
								            'text-indent': '2cm'
								        }
								    },
								    {
								        name: 'Marcador',
								        element: 'span',
								        styles: {
								        	'background-color' : '#FFFF00'
								        }
								    },
								    {
								        name: 'Normal',
								        element: 'span'
								    }
								]);
								CKEDITOR.config.toolbar = 'SigaToolbar';
								
								CKEDITOR.config.toolbar_SigaToolbar = [{
								        name: 'styles',
								        items: ['Styles']
								    },
								    {
								        name: 'clipboard',
								        items: ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo']
								    },
								    {
								        name: 'editing',
								        items: ['Find', 'Replace', '-', 'SelectAll']
								    },
								    '/',
								    {
								        name: 'basicstyles',
								        items: ['Bold', 'Italic', 'Subscript', 'Underline', 'Strike', '-', 'RemoveFormat']
								    },
								    {
								        name: 'paragraph',
								        items: ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyBlock', 'JustifyRight']
								    },
								    {
								        name: 'insert',
								        items: ['Table' , 'Footnotes', '-', 'SpecialChar', '-', 'PageBreak']
								    },
								    {
								        name: 'document',
								        items: ['Source']
								    }
								];
								CKEDITOR.config.extraPlugins = 'footnotes';
								window.onload = function() {
								    $("textarea.editor").each(function(index) {
								        CKEDITOR.replace(this, {
								            toolbar: 'SigaToolbar'
								        });
								    });
								}

                            </script>
                            
                            
					<!-- <FCK:replace replace="xxxeditorxxx" basePath="/ckeditor/ckeditor" config="${ckconfig}"></FCK:replace> -->
					</td>
				</tags:fixeditor>
			</c:otherwise>
		</c:choose></div>
	</c:when>
	<c:otherwise>
		<br>
		${v}
		<br>
		<br>
	</c:otherwise>
</c:choose></div>



