<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<siga:pagina titulo="Reclassifica&ccedil;&atilde;o em Lote">
    <link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css"
          media="screen, projection"/>

    <c:set var="thead_color" value="${thead_color}" scope="session"/>

    <div class="container-fluid">
        <div class="card bg-light mb-3">
            <div class="card-header">
                <h5>Reclassifica&ccedil;&atilde;o em Lote</h5>
            </div>
            <div class="card-body">
                <form name="frm" id="frm" class="form" method="post" action="listar_docs_reclassificados" theme="simple">
                    <input type="hidden" name="siglasDocumentosReclassificados" value=""/>
                    <input type="hidden" name="errosDocumentosNaoReclassificadosJson" value=""/>
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
                                <br/>
                                <div class="form-check form-check-inline">
                                    <input type="checkbox" id="substituto" name="substituto"
                                           class="form-check-input"
                                           onclick="displayTitular(this);"/>
                                    <label class="form-check-label" for="substituto">Substituto</label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row" id="tr_titular" style="display: ${exDocumentoDTO.substituicao ? '' : 'none'};">
                        <div class="col-md-8">
                            <input type="hidden" name="campos" value="titularSel.id"/>
                            <div class="form-group">
                                <label>Titular</label>
                                <input type="hidden" name="campos" value="titularSel.id"/>
                                <siga:selecao propriedade="titular" tema="simple" modulo="siga"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-8">
                            <div class="form-group">
                                <label for="selectLotacao"><fmt:message key="usuario.lotacao"/></label>
                                <select class="form-control siga-select2" style="width: 100%"
                                        id="selectLotacao" name="selectLotacao">
                                    <option value="">Selecione uma <fmt:message key="usuario.lotacao"/></option>
                                    <c:forEach items="${listaLotacao}" var="item">
                                        <option value="${item.idLotacao}" ${item.idLotacao == selectLotacao ? 'selected' : ''}>
                                                ${item.orgaoUsuario.iniciais}-${item.siglaLotacao} / ${item.nomeLotacao}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-8">
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
                        <div class="col-sm-8">
                            <div class="form-group">
                                <siga:selecao titulo="Nova Classifica&ccedil;&atilde;o" propriedade="classificacaoNova"
                                              modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-8">
                            <div class="form-group">
                                <label for="motivo">Motivo</label>
                                <input type="text" id="motivo" name="motivo" maxLength="128" class="form-control"/>
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
                Os documentos selecionados ser&atilde;o reclassificados de
                <span id="classificacaoAtualSelecionada"></span> para <span id="classificacaoNovaSelecionada"></span>.
                Deseja, confirmar?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">N&atilde;o</button>
                <a href="#" class="btn btn-success btn-confirmacao" role="button" aria-pressed="true"
                   onclick="confirmar();">
                    Sim</a>
            </div>
        </siga:siga-modal>
        <siga:siga-modal id="progressModal" exibirRodape="false" centralizar="true" tamanhoGrande="true"
                         tituloADireita="Reclassifica&ccedil;&atilde;o em lote" linkBotaoDeAcao="#"
                         botaoFecharNoCabecalho="false">
            <div class="modal-body">
                <div id="progressbar-ad"></div>
            </div>
        </siga:siga-modal>
    </div>
    <script type="text/javascript">
        function listarDocumentosParaReclassificarEmLote(offset) {
            sigaSpinner.mostrar();

            let selectIdLotacao = document.getElementById('selectLotacao').value;
            let siglaClassificacaoAtual = document.getElementById('formulario_classificacaoAtualSel_sigla').value;
            offset = offset == null ? 0 : offset;

            let url = '/sigaex/app/expediente/doc/listar_docs_para_reclassificar_lote?siglaClassificacao='
                + siglaClassificacaoAtual + '&dpLotacaoSelecao=' + selectIdLotacao + '&offset=' + offset;

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
            let subscritorSelSpan = document.getElementById('subscritorSelSpan');
            if (subscritorSelSpan.textContent.trim() === '') {
                sigaModal.alerta('Selecione responsável');
                return;
            }

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
                updateClassificacoesSelecionadas(siglaClassificacaoAtual.value, siglaClassificacaoNova.value);
                sigaModal.abrir('confirmacaoModal');
            }
        }

        function updateClassificacoesSelecionadas(classificacaoAtualSelecionada, classificacaoNovaSelecionada) {
            document.getElementById('classificacaoAtualSelecionada').innerHTML = classificacaoAtualSelecionada;
            document.getElementById('classificacaoNovaSelecionada').innerHTML = classificacaoNovaSelecionada;
        }

        function confirmar() {
            sigaSpinner.mostrar();
            document.getElementById("btnOk").disabled = true;
            sigaModal.fechar('confirmacaoModal');
            document.frm.submit();
        }

        let siglasDocumentosReclassificados = [];
        let errosDocumentosNaoReclassificadosMap = new Map();

        function enviarParaReclassificarLote() {

            process.push(function () {
                $('#progressModal').modal({
                    backdrop: 'static',
                    keyboard: false
                });
            });

            Array.from($(".chkMobil:checkbox").filter(":checked")).forEach(
                chk => {
                    process.push(function () {
                        return reclassificarPost(chk.value);
                    });
                    process.push(function () {
                        chk.checked = false;
                    });
                }
            );

            process.push(function () {
                sigaModal.fechar('progressModal');
                sigaSpinner.mostrar();
                enviarParaListagemDocumentosReclassificados();
            });

            process.run();
        }

        function reclassificarPost(documentoSelSigla) {
            $.ajax({
                url: '/sigaex/api/v1/documentos/' + documentoSelSigla + '/reclassificar',
                type: 'POST',
                async: false,
                data: { sigla: documentoSelSigla },
                dataType: 'json',
                success: function () {
                    siglasDocumentosReclassificados.push(documentoSelSigla);
                },
                error: function (response) {
                    errosDocumentosNaoReclassificadosMap.set(documentoSelSigla, response.responseJSON.errormsg);
                }
            });
        }

        function enviarParaListagemDocumentosReclassificados() {
            document.getElementsByName('siglasDocumentosReclassificados')[0].value = siglasDocumentosReclassificados;

            let errosDocumentosNaoReclassificadosJson = JSON.stringify(Object.fromEntries(errosDocumentosNaoReclassificadosMap));
            document.getElementsByName('errosDocumentosNaoReclassificadosJson')[0].value = errosDocumentosNaoReclassificadosJson;

            document.frm.submit();
        }

        let process = {
            steps: [],
            index: 0,
            title: "Executando o arquivar em lote dos documentos selecionados",
            errormsg: "Não foi possível completar a operação",
            urlRedirect: null,
            reset: function () {
                this.steps = [];
                this.index = 0;
            },
            push: function (x) {
                this.steps.push(x);
            },
            run: function () {
                this.progressbar = $('#progressbar-ad').progressbar();
                this.nextStep();
            },
            finalize: function () {
            },
            nextStep: function () {
                if (typeof this.steps[this.index] == 'string')
                    eval(this.steps[this.index++]);
                else {
                    let ret = this.steps[this.index++]();
                    if ((typeof ret == 'string') && ret != "OK") {
                        this.finalize();
                        return;
                    }
                }

                this.progressbar.progressbar("value",
                    100 * (this.index / this.steps.length));

                if (this.index != this.steps.length) {
                    let me = this;
                    window.setTimeout(function () {
                        me.nextStep();
                    }, 100);
                } else {
                    this.finalize();
                }
            }
        };

    </script>
    <script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
    <script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
    <script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
</siga:pagina>