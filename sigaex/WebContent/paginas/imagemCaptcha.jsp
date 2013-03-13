<%
String contentType = (String)request.getAttribute("contentType");
String fileName = (String)request.getAttribute("fileName");
byte[] bytes = (byte[])request.getAttribute("bytes");
	response.reset();
	response.setContentType(contentType);
	response.setContentLength((int) bytes.length);
	response.addHeader("Content-Disposition", "filename=" + fileName);
	response.getOutputStream().write(bytes);
	response.flushBuffer();
%>
