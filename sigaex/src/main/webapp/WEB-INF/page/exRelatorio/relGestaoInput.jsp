<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

						<div class="form-group col-md-2">
							<label>Data Inicial</label>
							<input class="form-control" type="text" name="dataInicial"
								id="dataInicial" value="${dataInicial}" onblur="javascript:verifica_data(this,0);" />
					    </div>
						<div class="form-group col-md-2">
							<label>Data Final</label>
							<input class="form-control" type="text" name="dataFinal"
								id="dataFinal" value="${dataFinal}" onblur="javascript:verifica_data(this,0);" />
						</div>
						<div class="form-group col-md-4">
							<label><fmt:message key="usuario.lotacao"/></label>
							<siga:selecao propriedade="lotacao" siglaInicial="${lotacao}" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
						</div>
						<div class="form-group col-md-4">
							<label><fmt:message key="usuario.matricula"/></label>
							<siga:selecao propriedade="usuario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
						</div>