<%
	br.gov.jfrj.siga.ex.ExDocumento doc = (br.gov.jfrj.siga.ex.ExDocumento)request.getAttribute("doc");
	response.reset();
	//response.setContentType("application/x-zip");
	//response.addHeader("Content-Disposition", "filename=" + doc.getCodigo() + ".zip");
	response.setContentType(doc.getConteudoTpDoc());
	response.addHeader("Content-Disposition", "filename=" + doc.getNmArqDoc());
	response.getOutputStream().write(doc.getConteudoBlobDoc2());
	response.flushBuffer();
%>
