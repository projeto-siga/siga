<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         buffer="64kb" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://localhost/libstag" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="/siga/bootstrap/css/bootstrap.min.css" type="text/css" media="screen, projection, print"/>

<siga:pagina titulo="Enviar para Visualiza&ccedil;&atilde;o Externa" popup="true">
    <style type="text/css">
        @media print {
            #btn-form {
                display: none;
            }

            #bg {
                -webkit-print-color-adjust: exact;
            }
        }
        
        .link-reduzido {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            display: inline-block;
            max-width: 40%;
        }
    </style>
    <!-- main content bootstrap -->
    <div class="card-body">
        <div class="row">

            <div class="col-sm-12">
                <div class="text-center">
                    <img src="${pageContext.request.contextPath}/imagens/${f:resource('/siga.relat.brasao')}"
                         class="rounded float-left" width="80px"/>
                    <h4><b>${f:resource('/siga.relat.titulo')}</b></h4>
                    <h5>${doc.orgaoUsuario.descricao}</h5>
                    <h5>${doc.lotacao.descricao }</h5>
                </div>
            </div>
        </div>
        <br>

        <div style="font-size: 26px">
            <div class="row">
                <div class="col-sm-12">
                    <div class="p-3 mb-2 bg-dark text-white text-center" id="bg"><h4><b>Código de Visualiza&ccedil;&atilde;o Externa do Documento</b></h4></div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group text-center">
                        <label>N&uacute;mero do Documento: <b>${sigla}</b></label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group text-center">
                        <p><label for="cod">C&oacute;digo de acesso ao documento:</label></p>
                        <textarea id="cod"
                                  style="width: 200px; height: 170px;padding: 20px 20px;box-sizing: border-box;border: 2px solid #ccc;border-radius: 4px;background-color: #f8f8f8;font-size: 10px;resize: none;"
                                  disabled rows="8" cols="30">${cod}</textarea>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group text-center">
                        <label>Data/Hora: ${dataHora}</label>
                    </div>
                </div>
            </div>
            <br>
            <br>
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group text-center">
                        <label>${mensagem}</label>
                        <br/>
                            ${descrMov}
                        <br/>
                            ${dest}
                    </div>
                </div>
            </div>
        </div>
    </div>

</siga:pagina>