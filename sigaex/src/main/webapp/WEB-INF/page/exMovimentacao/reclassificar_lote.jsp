<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>

<siga:pagina titulo="Reclassifica&ccedil;&atilde;o em Lote">

    <div class="container-fluid">
        <div class="card bg-light mb-3">
            <div class="card-header">
                <h5>Reclassifica&ccedil;&atilde;o em Lote</h5>
            </div>
            <div class="card-body">
                <form name="frm" id="frm" class="form" method="post" action="reclassificar_lote_gravar" theme="simple">
                    <div class="row">
                        <div class="col-sm-2">
                            <div class="form-group">
                                <label>Data
                                    <input type="text" name="dtMovString" onblur="verifica_data(this,0);"
                                           class="form-control"/>
                                </label>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label>Respons&aacute;vel</label>
                                <siga:selecao tema="simple" propriedade="subscritor" modulo="siga"/>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div class="form-group">
                                <div class="form-check form-check-inline mt-4">
                                    <label class="form-check-label" for="substituto">Substituto</label>
                                    <input type="checkbox" id="substituto" name="substituto"
                                           onclick="displayTitular(this);"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row" id="tr_titular" style="display: ${exDocumentoDTO.substituicao ? '' : 'none'};">
                        <div class="col-12">
                            <input type="hidden" name="campos" value="titularSel.id"/>
                            <div class="form-group">
                                <label>Titular</label>
                                <input type="hidden" name="campos" value="titularSel.id"/>
                                <siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="form-group">
                                <siga:selecao titulo="Classifica&ccedil;&atilde;o Atual"
                                              propriedade="classificacaoAtual"
                                              modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar"/>
                                <a href="javascript: listarDocumentosParaReclassificarEmLote()"
                                   class="btn btn-cancel btn-primary">Buscar Documentos</a>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="form-group">
                                <siga:selecao titulo="Nova Classifica&ccedil;&atilde;o" propriedade="classificacaoNova"
                                              modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar"/>
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="form-group">
                                <label>Motivo
                                    <input type="text" id="motivo" name="motivo" maxLength="128" class="form-control"/>
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="form-group">
                                <button type="button" id="btnOk" class="btn btn-primary" onclick="validar();">
                                    Ok
                                </button>
                                <button type="button" class="btn btn-primary" onclick="history.back();">
                                    Cancela
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
                Todos os documentos ser&atilde;o Reclassificados. Deseja, confirmar?
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
        function listarDocumentosParaReclassificarEmLote(offset) {
            sigaSpinner.mostrar();

            offset = offset == null ? 0 : offset;

            let siglaClassificacaoAtual = document.getElementById('formulario_classificacaoAtualSel_sigla').value;
            let url = '/sigaex/app/expediente/doc/listar_docs_para_reclassificar_lote?siglaClassificacao='
                + siglaClassificacaoAtual + '&offset=' + offset;

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
            let classificacaoAtualSelSpan = document.getElementById('classificacaoAtualSelSpan');
            if (classificacaoAtualSelSpan.textContent.trim() === '') {
                sigaModal.alerta('Selecione classificação atual');
                return;
            }

            let classificacaoNovaSelSpan = document.getElementById('classificacaoNovaSelSpan');
            if (classificacaoNovaSelSpan.textContent.trim() === '') {
                sigaModal.alerta('Selecione a nova classificação');
                return;
            }

            let siglaClassificacaoAtual = document.getElementById('formulario_classificacaoAtualSel_sigla');
            let siglaClassificacaoNova = document.getElementById('formulario_classificacaoNovaSel_sigla');
            if (siglaClassificacaoAtual.value === siglaClassificacaoNova.value) {
                sigaModal.alerta('Nova classificação selecionada para reclassificar é a mesma da atual. ' +
                    'Selecione valores diferentes para a reclassificação');
                return;
            }

            let motivo = document.getElementById('motivo');
            if (motivo.value.trim() === '') {
                sigaModal.alerta('É necessário informar o motivo da reclassificação');
                return;
            }

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