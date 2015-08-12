<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<Applet Code="bluecrystal.applet.sign.SignApplet.class"
	archive="/sigacd/bluc_p11.jar" width=1 Height=1 id="signer">
	<PARAM name="module"
		value="aetpkss1.dll;eTPKCS11.dll;asepkcs.dll;libaetpkss.dylib;libeTPkcs11.dylib" />
	<PARAM name="otherPath" value="/usr/local/lib" />
</Applet>

<script src="/sigaex/javascript/assinatura-digital.js"></script>

