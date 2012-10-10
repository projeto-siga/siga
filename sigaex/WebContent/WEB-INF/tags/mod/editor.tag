<%@tag import="com.ckeditor.CKEditorInsertTag"%>
<%@tag import="com.ckeditor.CKEditorTag"%>
<%@tag import="com.ckeditor.CKEditorConfig"%>
<%@ tag body-content="empty"%>
<%@ taglib prefix="ww" uri="/webwork"%>
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
<div><c:if test="${not empty titulo}">
	<b>${titulo}</b>
</c:if> <c:choose>
	<c:when test="${param.entrevista == 1}">
		<%
			String s = (String) jspContext.getAttribute("v");
					jspContext.setAttribute("v",
							br.gov.jfrj.siga.ex.bl.Ex.getInstance().getBL().canonicalizarHtml(s, false, true, false,
											true));
					String var1 = (String) jspContext.getAttribute("var");
					request.setAttribute(var1, jspContext.getAttribute("v"));
		%>

		<div><c:choose>
			<c:when test="${f:podeUtilizarExtensaoEditor(lotaTitular, idMod) && !desconsiderarExtensao}">
				${f:obterExtensaoEditor(lotaTitular.orgaoUsuario, var, v, par.serverAndPort[0])}
			</c:when>
			<c:otherwise>
			<% 
				com.ckeditor.CKEditorConfig ckconfig = new com.ckeditor.CKEditorConfig();
				ckconfig.addConfigValue("height","300");

				java.util.List<Object> mainList = new java.util.ArrayList<Object>();				
				
				// barra de estilos
				java.util.HashMap<String, Object> toolbarSectionMap = new java.util.HashMap<String, Object>();
				java.util.List<String> subList = new java.util.ArrayList<String>();
				subList.add("Styles");
				
				toolbarSectionMap.put("name", "styles");	
				toolbarSectionMap.put("items", subList);	
				
				mainList.add(toolbarSectionMap);		
				
				//barra de área de transferência
				toolbarSectionMap = new java.util.HashMap<String, Object>();
				subList = new java.util.ArrayList<String>();
				subList.add("Cut");
				subList.add("Copy");
				subList.add("Paste");
				subList.add("PasteText");
				subList.add("PasteFromWord");
				subList.add("-");
				subList.add("Undo");
				subList.add("Redo");
				
				toolbarSectionMap.put("name", "clipboard");	
				toolbarSectionMap.put("items", subList);	
				
				mainList.add(toolbarSectionMap);		
				
				//barra de edição
				toolbarSectionMap = new java.util.HashMap<String, Object>();
				subList = new java.util.ArrayList<String>();
				subList.add("Find");
				subList.add("Replace");
				subList.add("-");
				subList.add("SelectAll");
				subList.add("-");
				subList.add("Scayt");
				
				toolbarSectionMap.put("name", "editing");	
				toolbarSectionMap.put("items", subList);	
				
				mainList.add(toolbarSectionMap);
				mainList.add("/");
				
				//barra de estilos básicos
				toolbarSectionMap = new java.util.HashMap<String, Object>();
				subList = new java.util.ArrayList<String>();
				subList.add("Bold");
				subList.add("Italic");
				subList.add("Strike");
				subList.add("-");
				subList.add("RemoveFormat");
				
				toolbarSectionMap.put("name", "basicstyles");	
				toolbarSectionMap.put("items", subList);	
				
				mainList.add(toolbarSectionMap);		
				
				//barra de parágrafos
				toolbarSectionMap = new java.util.HashMap<String, Object>();
				subList = new java.util.ArrayList<String>();
				subList.add("NumberedList");
				subList.add("BulletedList");
				subList.add("-");
				subList.add("Outdent");
				subList.add("Indent");
				subList.add("-");
				subList.add("JustifyLeft");
				subList.add("JustifyBlock");
				subList.add("JustifyRight");
				
				toolbarSectionMap.put("name", "paragraph");	
				toolbarSectionMap.put("items", subList);	
				
				mainList.add(toolbarSectionMap);		
				
				//barra de inserções
				toolbarSectionMap = new java.util.HashMap<String, Object>();
				subList = new java.util.ArrayList<String>();
				subList.add("Table");
				subList.add("-");
				subList.add("SpecialChar");
				subList.add("-");
				subList.add("PageBreak");
				
				toolbarSectionMap.put("name", "insert");	
				toolbarSectionMap.put("items", subList);	
				
				mainList.add(toolbarSectionMap);		
				
				//barra de documento
				toolbarSectionMap = new java.util.HashMap<String, Object>();
				subList = new java.util.ArrayList<String>();
				subList.add("Source");
				
				toolbarSectionMap.put("name", "document");	
				toolbarSectionMap.put("items", subList);	
				
				mainList.add(toolbarSectionMap);		
				
				ckconfig.addConfigValue("toolbar", mainList);
				
				
				java.util.Map<String, String> attrTxtArea = new java.util.HashMap<String, String>();
				attrTxtArea.put("rows", "20");
				attrTxtArea.put("cols", "100");
				
				ckconfig.addConfigValue("stylesSet","siga_ckeditor_styles:/sigaex/sigalibs/siga_ckeditor_styles.js");
								
				request.setAttribute("ckconfig", ckconfig);
				request.setAttribute("attrTxtArea", attrTxtArea);
				
				
				
			%>
			
			<tags:fixeditor var="${var}">
					<FCK:editor editor="xxxeditorxxx"
						basePath="/ckeditor/ckeditor" config="${ckconfig}" textareaAttributes="${attrTxtArea}" value="${v}">
					</FCK:editor>
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



