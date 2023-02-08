<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<%@ taglib uri="http://localhost/functiontag" prefix="f" %>

<siga:pagina titulo="Arquivamento em Lote">
    <div class="container-fluid">
        <div class="card bg-light mb-3">
            <div class="card-header">
                <h5>Arquivamento</h5>
            </div>
            <div class="card-body">
                <form name="frm" action="arquivar_corrente_lote_gravar" method="post" theme="simple">
                    <div class="row">
                        <div class="col-sm-2">
                            <div class="form-group">
                                <button type="button" id="btnOk" class="btn btn-primary" onclick="validar();">
                                    Arquivar
                                </button>
                            </div>
                        </div>
                    </div>
                    <div id="documentos">
                    </div>
                </form>
            </div>
        </div>

        <siga:siga-modal id="confirmacaoModal" exibirRodape="false"
                         tituloADireita="Confirma&ccedil;&atilde;o" linkBotaoDeAcao="#">
            <div class="modal-body">
                Os documentos selecionados ser&atilde;o arquivados. Deseja, confirmar?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">N&atilde;o</button>
                <a href="#" class="btn btn-success btn-confirmacao" role="button" aria-pressed="true"
                   onclick="confirmar();">
                    Sim</a>
            </div>
        </siga:siga-modal>
    </div>
    <script type="text/javascript">
        window.onload = function () {
            listarDocumentosParaArquivarCorrenteEmLote();
        }
        
        function listarDocumentosParaArquivarCorrenteEmLote(offset) {
            sigaSpinner.mostrar();

            offset = offset == null ? 0 : offset;

            let url = '/sigaex/app/expediente/doc/listar_docs_para_arquivar_corrent_lote?&offset=' + offset;

            $.ajax({
                url: url,
                success: function (data) {
                    $('#documentos').html(data);
                    sigaSpinner.ocultar();
                },
                error: function (result) {
                    sigaSpinner.ocultar();
                    console.log(result.errormsg);
                },
            });
        }

        function validar() {
            let checkedElements = $("input[name='documentosSelecionados']:checked");
            if (checkedElements.length == 0) {
                sigaModal.alerta('Selecione pelo menos um documento');
            } else {
                sigaModal.abrir('confirmacaoModal');
            }
        }

        function confirmar() {
            sigaSpinner.mostrar();
            document.getElementById("btnOk").disabled = true;
            sigaModal.fechar('confirmacaoModal');
            document.frm.submit();
        }

    </script>
</siga:pagina>