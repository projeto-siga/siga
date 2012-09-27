<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	try {
		String req_uri = request.getAttribute(
				"javax.servlet.forward.request_uri").toString();
		if (req_uri != null && req_uri.length() != 0) {
			String req_qs = (String)request.getAttribute(
					"javax.servlet.forward.query_string");
			String req_redirect = req_uri
					+ (req_qs != null ? "?" + req_qs.toString() : "");
			response.sendRedirect("/siga/redirect.action?uri="
					+ java.net.URLEncoder.encode(req_redirect, "UTF-8"));
		}else{
			response.sendRedirect("/siga");
		}

		} catch (Exception e) {
			response.sendRedirect("/siga");
		}
%>
