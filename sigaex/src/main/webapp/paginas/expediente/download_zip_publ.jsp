<%
	br.gov.jfrj.siga.ex.ExMovimentacao mov = (br.gov.jfrj.siga.ex.ExMovimentacao)request.getAttribute("mov");
	response.reset();
	response.setContentType("application/zip");
	response.setContentType(mov.getConteudoTpMov());
	response.addHeader("Content-Disposition", "filename=" + mov.getNmArqMov());
	response.getOutputStream().write(mov.getConteudoBlobMov2());
	response.flushBuffer();
%>
