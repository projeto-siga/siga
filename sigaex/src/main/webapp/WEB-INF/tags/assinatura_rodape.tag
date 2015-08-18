<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<Applet Code="bluecrystal.applet.sign.SignApplet.class"
	archive="/sigacd/bluc_p11_1.jar" width=1 Height=1 id="signer">
	<PARAM name="module"
		value="aetpkss1.dll;eTPKCS11.dll;asepkcs.dll;libaetpkss.dylib;libeTPkcs11.dylib" />
	<PARAM name="otherPath" value="/usr/local/lib" />
</Applet>

<Applet Code="bluecrystal.applet.capi.SignApplet.class"
		archive="/sigacd/bluc_capi_2.jar,/sigacd/jna-platform-4.1.0_2.jar,/sigacd/jna-4.1.0_2.jar" width=1
		Height=1 id="signerCAPI"> </Applet>
<%--
 --%>
<script src="/sigaex/javascript/assinatura-digital.js"></script>

