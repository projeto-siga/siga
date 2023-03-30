<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<%@ taglib uri="http://localhost/functiontag" prefix="f" %>

<siga:pagina titulo="Arquivar em Lote">
    <c:set var="thead_color" value="${thead_color}" scope="session"/>
    <div class="container-fluid">
        <div class="card bg-light mb-3">
            <div class="card-header">
                <h5>Arquivar em Lote</h5>
            </div>
            <div class="card-body">
                <form name="frm" id="frm" class="form" method="post" action="listar_docs_arquivados_corrente" theme="simple">
                    <input type="hidden" name="siglasDocumentosArquivadosCorrente" value=""/>
                    <input type="hidden" name="errosDocumentosNaoArquivadosCorrenteJson" value=""/>
                    
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label>Atendente</label>
                                <select class="form-control siga-select2" id="selectAtendente">
                                    <option value="pessoa" data-id="${cadastrante.id}"
                                            selected>${cadastrante.nomePessoa}</option>
                                    <option value="lotacao" data-id="${cadastrante.lotacao.id}">
                                            ${cadastrante.lotacao.siglaCompleta} / ${cadastrante.lotacao.nomeLotacao}
                                    </option>
                                </select>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <br/>
                                <a href="javascript: listarDocumentosParaArquivarCorrenteEmLote()"
                                   class="btn btn-primary"><i class="fas fa-search"></i> Pesquisar Documentos</a>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-2">
                            <div class="form-group">
                                <br/>
                                <br/>
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
                Deseja arquivar em lote os documentos selecionados?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">N&atilde;o</button>
                <a href="#" class="btn btn-success btn-confirmacao" role="button" aria-pressed="true"
                   onclick="confirmar();">
                    Sim</a>
            </div>
        </siga:siga-modal>
        <siga:siga-modal id="progressModal" exibirRodape="false" centralizar="true" tamanhoGrande="true"
                         tituloADireita="Arquivamento em lote" linkBotaoDeAcao="#" botaoFecharNoCabecalho="false">
            <div class="modal-body">
                <div id="progressbar-ad"></div>
            </div>
        </siga:siga-modal>
    </div>
    <script type="text/javascript">

        function listarDocumentosParaArquivarCorrenteEmLote(offset) {
            sigaSpinner.mostrar();

            offset = offset == null ? 0 : offset;

            let selectAtendenteElement = document.getElementById('selectAtendente');
            let selectAtendenteValue = selectAtendenteElement.value;
            let selectAtendenteIndex = selectAtendenteElement.selectedIndex;
            let selectAtendenteId = selectAtendenteElement.options[selectAtendenteIndex].dataset.id;

            let url = '/sigaex/app/expediente/doc/listar_docs_para_arquivar_corrente_lote'
                + '?atendente=' + selectAtendenteValue
                + '&atendenteId=' + selectAtendenteId
                + '&offset=' + offset;

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
                sigaModal.alerta('Selecione pelo menos um documento para arquivar.');
            } else {
                sigaModal.abrir('confirmacaoModal');
            }
        }

        let siglasDocumentosArquivadosCorrente = [];
        let errosDocumentosNaoArquivadosCorrenteMap = new Map();

        function confirmar() {
            document.getElementById("btnOk").disabled = true;
            sigaModal.fechar('confirmacaoModal');
            enviarParaArquivarCorrenteLote();
        }

        function enviarParaArquivarCorrenteLote() {

            process.push(function () {
                $('#progressModal').modal({
                    backdrop: 'static',
                    keyboard: false
                });
            });

            Array.from($(".chkMobil:checkbox").filter(":checked")).forEach(
                chk => {
                    process.push(function () {
                        return arquivarCorrentePost(chk.value);
                    });
                    process.push(function () {
                        chk.checked = false;
                    });
                }
            );

            process.push(function () {
                sigaModal.fechar('progressModal');
                sigaSpinner.mostrar();
                enviarParaListagemDocumentosArquivadosCorrente();
            });

            process.run();
        }

        function arquivarCorrentePost(documentoSelSigla) {
            $.ajax({
                url: '/sigaex/api/v1/documentos/' + documentoSelSigla + '/arquivar-corrente',
                type: 'POST',
                async: false,
                data: {sigla: documentoSelSigla},
                dataType: 'json',
                success: function () {
                    siglasDocumentosArquivadosCorrente.push(documentoSelSigla);
                },
                error: function (response) {
                    errosDocumentosNaoArquivadosCorrenteMap.set(documentoSelSigla, response.responseJSON.errormsg);
                }
            });
        }

        function enviarParaListagemDocumentosArquivadosCorrente() {
            document.getElementsByName('siglasDocumentosArquivadosCorrente')[0].value = siglasDocumentosArquivadosCorrente;

            let errosDocumentosNaoArquivadosCorrenteJson = JSON.stringify(Object.fromEntries(errosDocumentosNaoArquivadosCorrenteMap));
            document.getElementsByName('errosDocumentosNaoArquivadosCorrenteJson')[0].value = errosDocumentosNaoArquivadosCorrenteJson;

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
</siga:pagina>