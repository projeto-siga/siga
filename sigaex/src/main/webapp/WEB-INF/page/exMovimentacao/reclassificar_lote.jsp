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
                <form name="frm" id="frm" class="form" theme="simple">
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
                                <script type="text/javascript">
                                    const classificacaoAtualSelElement = document.getElementById('formulario_classificacaoAtualSel_sigla');
                                    classificacaoAtualSelElement.addEventListener('change', () => {
                                        listarDocumentosParaReclassificarEmLote();
                                    });
                                </script>
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
                                <label for="motivo">Motivo</label>
                                <input type="text" id="motivo" maxLength="128" class="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary">Ok</button>
                                <button type="button" onclick="history.back();" class="btn btn-primary">
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
    </div>
    <script type="text/javascript">
        function listarDocumentosParaReclassificarEmLote() {
            let siglaClassificacao = document.getElementById('formulario_classificacaoAtualSel_sigla').value;
            let offset = 0;
            let url = '/sigaex/app/expediente/doc/listar_docs_para_reclassificar_lote?siglaClassificacao='
                + siglaClassificacao + '&offset=' + offset;

            $.ajax({
                url: url,
                success: function (data) {
                    $('#documentos').html(data);
                }
            });

        }
    </script>
</siga:pagina>