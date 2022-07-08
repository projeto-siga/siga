<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         buffer="64kb" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://localhost/libstag" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="/siga/bootstrap/css/bootstrap.min.css" type="text/css" media="screen, projection, print"/>

<script type="text/javascript">

    function validarNome(campo) {
        campo.value = campo.value.replace(/[^a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9.\-\/]/g,'');
    }

    function validarEmail(campo) {
        if(campo.value !== "") {
            const RegExp = /\b[\w]+@[\w-]+\.[\w]+/;

            if (campo.value.search(RegExp) === -1) {
                sigaModal.alerta("E-mail inválido!");
                habilitarBotaoOk();
                document.getElementById('email').focus();
                return false;
            } else if (campo.value.search(',') > 0) {
                sigaModal.alerta("E-mail inválido! Não pode conter vírgula ( , )");
                habilitarBotaoOk();
                document.getElementById('email').focus();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
</script>

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
            <br>
            <br>
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
                        <label>Data/Hora: ${dataHora}</label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group text-center">
                        <label><b>Aten&ccedil;&atilde;o: </b>Para encaminhar o documento para um usu&aacute;rio n&atilde;o
                            cadastrado no sistema, preencha os seguintes campos </label>
                        <br/>
                    </div>
                </div>
            </div>
            <form name="frm" action="${pageContext.request.contextPath}/app/expediente/mov/enviar_para_visualizacao_externa_gravar"
                  method="POST">
                <input type="hidden" name="sigla" value="${sigla}" />
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label for="nmPessoa">Nome: </label>
                            <input type="text" id="nmPessoa" name="nmPessoa" value="${nmPessoa}" maxlength="60"
                                   class="form-control" onkeyup="validarNome(this)"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label for="email">E-mail: </label>
                            <input type="text" id="email" name="email" value="${email}" maxlength="60"
                                   onchange="validarEmail(this)" onkeyup="this.value = this.value.toLowerCase().trim()"
                                   class="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <button type="submit" class="btn btn-lg btn-primary btn-block"><i class="fas fa-envelope"></i>
                            Enviar
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

</siga:pagina>