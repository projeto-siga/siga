<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="conteudo" required="true"%>
<%@ attribute name="nome" required="true"%>

<script type="text/javascript" language="Javascript1.1">

	var insertingTable = false;
	
	function onSave() {
		document.getElementById('${nome}').EscapeUnicode = false;
		document.getElementById('${nome}').value = document.getElementById('xstandard').value;
	}
	
	function xsDialogPropertiesActivated(id, qpath, element, attributes, metadata) {
		alert(metadata + '  -  ' + attributes)
		if (qpath == '' && element == 'table'){
			document.getElementById('xstandard').SetDialogProperties("<attributes><attr><name>summary</name><value>Tabela</value></attr><attr><name>bordercolor</name><value>#000000</value></attr><attr><name>style</name><value>border-width:1px;border-style:solid;border-collapse:collapse</value></attr></attributes>", false, false);
		}
	}
	
</script>

<input type="hidden" id="${nome}" name="${nome}" />
<object classid="clsid:0EED7206-1661-11D7-84A3-00606744831D"
	codebase="/sigaex/XStandard/XStandard.cab#Version=3,0,0,0"
	type="application/x-xstandard" id="xstandard" width="800" height="400">

	<!-- 
		<param name="License" value="%AppPath%\XStandard\license.txt" />
		<param name="CSS" value="%AppPath%\XStandard\format.css" />
		<param name="Styles" value="%AppPath%\XStandard\styles.xml" />
		 <param name="SpellCheckerLangFilter"
			value="da,de,en-ca,en-us,en-gb,es,fr,it,nl,no,pt,sv" />
		 <param name="ToolbarWysiwyg"
			value="ordered-list,unordered-list,definition-list,,draw-layout-table,draw-data-table,image,separator,hyperlink,attachment,directory,spellchecker,,wysiwyg,source,preview,screen-reader,help" /> 			
			 -->

	<param nams="ImageLibraryURL"
		value="http://soap.xstandard.com/imagelibrary.aspx" />
	<param name="AttachmentLibraryURL"
		value="http://soap.xstandard.com/attachmentlibrary.aspx" />
	<param name="SpellCheckerURL"
		value="http://soap.xstandard.com/spellchecker.aspx" />
	<param name="DirectoryURL"
		value="http://soap.xstandard.com/directory.aspx" />
	<param name="SubdocumentURL"
		value="http://soap.xstandard.com/subdocument.aspx" />
	<param name="EscapeUnicode" value="false" />

	<c:set var="conteudo">
		<c:out value="${conteudo}" escapeXml="true" />
	</c:set>
	<param name="Value" value="${conteudo}" />
	<param name="SpellCheckerLangFilter" value="pt" />
	<param name="SpellCheckerLang" value="pt" />
	<param name="License"
		value="http://${nomeServidorComPorta}/sigaex/XStandard/license.txt" />
	<param name="CSS"
		value="http://${nomeServidorComPorta}/sigaex/XStandard/format.css" />
	<param name="Styles"
		value="http://${nomeServidorComPorta}/sigaex/XStandard/styles-pt.xml" />
	<param name="Buttons"
		value="http://${nomeServidorComPorta}/sigaex/XStandard/buttons-pt.xml" />
	<param name="Icons"
		value="http://${nomeServidorComPorta}/sigaex/XStandard/icons.xml" />
	<!-- Ver como coloca português -->
	<param name="Lang" value="pt" />
	<param name="Localization"
		value="http://${nomeServidorComPorta}/sigaex/XStandard/localization-pt.xml" />
	<param name="EnablePasteMarkup" value="yes" />
	<param name="ToolbarWysiwyg"
		value="cut,copy,paste,undo,redo,find-replace,,strong,em,underline,,align-left,align-center,align-right,justify,,undo-blockquote,blockquote,,undo-indent-first,indent-first,,ordered-list,unordered-list,,draw-data-table,,separator,pagebreak,,spellchecker,,source,,help" />
	<param name="BackgroundColor" value="white" />
	<param name="BorderColor" value="#888888" />
	<!-- <param name="Base" value="http://soap.xstandard.com/library/" /> -->
	<param name="LatestVersion" value="2.0.5.0" />
	<param name="ToolbarEffect" value="linear-gradient" />
	<param name="ShowStyles" value="yes" />
	<param name="ShowToolbar" value="yes" />
	<param name="Mode" value="wysiwyg" />
	<param name="Options" value="0" />
	<param name="IndentOutput" value="yes" />
	<param name="ProxySetting" value="platform" />
	<param name="Debug" value="yes" />

	<!-- Tem duas opções que talvez sejam úteis: PreviewXSLT e ScreenReaderXSLT -->
	<!-- A opção icons é pros ícones das operações principais. O Placeholders é pros ícones das tags customizadas -->
	<!-- Ver qual a utilidade desse aqui: param name = EditorCSS --> <!-- Essas abaixo definem os botões em outros modos de visualização 
		<param name="ToolbarSource" value="" />
		<param name="ToolbarPreview" value="" />
		<param name="ToolbarScreenReader" value="" /> 
		Talvez CustomInlineElements, CustomBlockElements e CustomEmptyElements sirvam pras tabelas
		Depois, ver se as integration settings servem pra alguma coisa
		VER HeartbeatURL e Heartbeat Interval. Parecem ser úteis pra verificar sessão
		Talvez algumas subs sejam úteis para mudar os contexts menus. Ver na seção Hooks & Extensions
		Funções TagList, Path e QPath e TagListXML são interessantes
		--> </object>