<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/customtag" prefix="tags" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://localhost/functiontag" prefix="f" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>

<fmt:message key="documento.transferencia.lote" var="titulo"/>
<siga:pagina titulo="${titulo}">
    <script type="text/javascript" src="/sigaex/javascript/sequential-ajax-calls.js"></script>
    <div class="container-fluid">
        <div class="card bg-light mb-3">
            <div class="card-header">
                <h5>${titulo}</h5>
            </div>
            <div class="card-body">
                <form name="frm" id="frm">
                    <input type="hidden" name="postback" value="1"/>
                    <input type="hidden" name="paramoffset" value="0"/>
                    <input type="hidden" name="p.offset" value="0"/>

                    <div class="row campo-orgao-externo" style="display: none;">
                        <div class="col-sm">
                            <div class="form-group">
                                <span style="color: red"><fmt:message key="tela.tramitar.atencao"/></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <div class="form-group">
                                <label for="tipoResponsavel">
                                    <fmt:message key="tela.tramitarLote.tipoResponsavel"/>
                                </label>
                                <select class="custom-select" id="tipoResponsavel" name="tipoResponsavel"
                                        value="${tipoResponsavel}" onchange="updateTipoResponsavel()">
                                    <c:forEach var="item" items="${listaTipoResp}">
                                        <option value="${item.key}">${item.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label>&nbsp;</label>
                                <span id="lotaResponsavel">
									<siga:selecao propriedade="lotaResponsavel" tema="simple" modulo="siga"
                                                  onchange="updateResponsavelSelecionado('formulario_lotaResponsavelSel_sigla')"/>
								</span>
                                <span id="responsavel" style="display: none;">
                                    <siga:selecao propriedade="responsavel" tema="simple" modulo="siga"
                                                  onchange="updateResponsavelSelecionado('formulario_responsavelSel_sigla')"/>
								</span>
                                <span id="cpOrgao" style="display: none;">
                                    <siga:selecao propriedade="cpOrgao" tema="simple" modulo="siga"
                                                  onchange="updateResponsavelSelecionado('formulario_cpOrgaoSel_sigla')"/>
								</span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <div class="form-group">
                                <label>Data da devolução</label>
                                <input type="text" name="dtDevolucaoMovString"
                                       onblur="verifica_data(this,0)" value="${dtDevolucaoMovString}"
                                       class="form-control"/>
                                <small class="form-text text-muted">Atenção: somente preencher
                                    a data de devolução se a intenção for, realmente, que o
                                    documento seja devolvido até esta data.</small>
                            </div>
                        </div>
                        <div class="col-sm-3">
                            <div class="form-group campo-orgao-externo" style="display: none;">
                                <div class="form-check form-check-inline mt-4 ">
                                    <input class="form-check-input" type="checkbox" name="protocolo"
                                           id="protocolo" value="mostrar"
                                           <c:if test="${protocolo}">checked</c:if> />
                                    <label class="form-check-label" for="protocolo">
                                        <fmt:message key="tela.tramitar.checkbox"/>
                                    </label>
                                </div>
                            </div>

                        </div>
                        <div class="col-sm-3">
                            <div class="form-group campo-orgao-externo" style="display: none;">
                                <label>Observação</label>
                                <input type="text" size="30" name="obsOrgao" id="obsOrgao" class="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-1">
                            <button type="button" id="btnOk" class="btn btn-primary" onclick="validar();">
                                <fmt:message key="documento.transferir"/>
                            </button>
                        </div>
                    </div>

                    <div class="gt-content-box gt-for-table">
                        <br/>
                        <h5>Destinatário: <span id="responsavelSelecionado"></span></h5>
                        <div>

                            <table class="table table-hover table-striped">
                                <thead class="${thead_color} align-middle text-center">
                                <tr>
                                    <th rowspan="2" align="center">
                                        <input type="checkbox" id="checkall" onclick="checkUncheckAll(this)"/>
                                    </th>
                                    <th rowspan="2" class="text-right">Número</th>
                                    <th colspan="3">Documento</th>
                                    <th colspan="2">Última Movimentação</th>
                                    <th rowspan="2">Descrição</th>
                                    <th rowspan="2" class="col-5 d-none">Despacho <c:if test="${secao==0}"/></th>
                                </tr>
                                <tr>
                                    <th class="text-center">Data</th>
                                    <th class="text-center"><fmt:message key="usuario.lotacao"/></th>
                                    <th class="text-center"><fmt:message key="usuario.pessoa2"/></th>
                                    <th class="text-center">Data</th>
                                    <th class="text-center"><fmt:message key="usuario.pessoa2"/></th>
                                </tr>
                                </thead>
                                <tbody class="table-bordered">

                                <siga:paginador maxItens="${maxItems}" maxIndices="10"
                                                totalItens="${tamanho}" itens="${itens}" var="documento">

                                    <c:set var="chk" scope="request">process_chk_${documento.id}</c:set>

                                    <tr>
                                        <td align="center" class="align-middle text-center">
                                            <input type="checkbox" name="documentosSelecionados"
                                                   value="${documento.codigoCompacto}" id="${chk}" class="chkDocumento"
                                                   onclick="displaySel()"/></td>
                                        <td class="text-right">
                                            <c:choose>
                                                <c:when test='${param.popup!="true"}'>
                                                    <a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento.sigla}">
                                                            ${documento.sigla}
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="javascript:opener.retorna_${param.propriedade}('${documento.id}','${documento.sigla}','');">
                                                            ${documento.sigla}
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <c:if test="${not documento.geral}">
                                            <td class="text-center">${documento.doc.dtDocDDMMYY}</td>
                                            <td class="text-center">
                                                <siga:selecionado isVraptor="true"
                                                                  sigla="${documento.doc.lotaSubscritor.sigla}"
                                                                  descricao="${documento.doc.lotaSubscritor.descricao}"/>
                                            </td>
                                            <td class="text-center">
                                                <siga:selecionado isVraptor="true"
                                                                  sigla="${documento.doc.subscritor.iniciais}"
                                                                  descricao="${documento.doc.subscritor.descricao}"/></td>
                                            <td class="text-center">${documento.ultimaMovimentacaoNaoCancelada.dtMovDDMMYY}</td>
                                            <td class="text-center">
                                                <siga:selecionado isVraptor="true"
                                                                  sigla="${documento.ultimaMovimentacaoNaoCancelada.resp.iniciais}"
                                                                  descricao="${documento.ultimaMovimentacaoNaoCancelada.resp.descricao}"/>
                                            </td>
                                        </c:if>
                                        <c:if test="${documento.geral}">
                                            <td class="text-center">${documento.doc.dtDocDDMMYY}</td>
                                            <td class="text-center">
                                                <siga:selecionado isVraptor="true"
                                                                  sigla="${documento.doc.subscritor.iniciais}"
                                                                  descricao="${documento.doc.subscritor.descricao}"/></td>
                                            <td class="text-center">
                                                <siga:selecionado isVraptor="true"
                                                                  sigla="${documento.doc.lotaSubscritor.sigla}"
                                                                  descricao="${documento.doc.lotaSubscritor.descricao}"/></td>
                                            <td class="text-center"></td>
                                            <td class="text-center"></td>
                                            <td class="text-center"></td>
                                            <td class="text-center"></td>
                                        </c:if>
                                        <td>
                                            <c:choose>
                                                <c:when test="${siga_cliente == 'GOVSP'}">
                                                    ${documento.doc.descrDocumento}
                                                </c:when>
                                                <c:otherwise>
                                                    ${f:descricaoConfidencial(documento.doc, lotaTitular)}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </siga:paginador>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <siga:siga-modal id="confirmacaoModal" exibirRodape="false"
                         tituloADireita="Confirma&ccedil;&atilde;o" linkBotaoDeAcao="#">
            <div class="modal-body">
                Todos os documentos selecionados ser&atilde;o Tramitados. Deseja, confirmar?
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
        function sbmt(offset) {
            if (offset == null) {
                offset = 0;
            }

            let form = document.forms['frm'];
            form ["paramoffset"].value = offset;
            form.action = "transferir_lote";
            form.method = "GET";
            form ["p.offset"].value = offset;

            form.submit();
        }

        function checkUncheckAll(theElement) {
            let isChecked = theElement.checked;
            Array.from(document.getElementsByClassName('chkDocumento')).forEach(chk => chk.checked = isChecked);
        }

        function displaySel() {
            document.getElementById('checkall').checked =
                Array.from(document.getElementsByClassName('chkDocumento')).every(chk => chk.checked);
        }

        function responsavelSelecionado() {
            let tipoResponsavelSelecionado = document.getElementById('tipoResponsavel');

            let elementoResponsavelSelecionado = document.getElementById('lotaResponsavel');
            switch (parseInt(tipoResponsavelSelecionado.value)) {
                case 2:
                    elementoResponsavelSelecionado = document.getElementById('responsavel');
                    break;
                case 3:
                    elementoResponsavelSelecionado = document.getElementById('cpOrgao');
                    break;
            }

            return elementoResponsavelSelecionado;
        }

        function updateTipoResponsavel() {
            document.getElementById('lotaResponsavel').style.display = 'none';
            document.getElementById('responsavel').style.display = 'none';
            document.getElementById('cpOrgao').style.display = 'none';
            Array.from(document.getElementsByClassName('campo-orgao-externo')).forEach(el => el.style.display = 'none');

            limparSelecao();

            let elementoResponsavelSelecionado = responsavelSelecionado();
            elementoResponsavelSelecionado.style.display = '';

            if (elementoResponsavelSelecionado.id === 'cpOrgao') {
                Array.from(document.getElementsByClassName('campo-orgao-externo')).forEach(el => el.style.display = '');
            }
        }

        function updateResponsavelSelecionado(id) {
            document.getElementById('responsavelSelecionado').innerHTML = document.getElementById(id).value;
        }

        function limparSelecao() {
            document.getElementById('formulario_lotaResponsavelSel_id').value = "";
            document.getElementById('formulario_lotaResponsavelSel_sigla').value = "";
            document.getElementById('formulario_lotaResponsavelSel_descricao').value = "";
            document.getElementById('lotaResponsavelSelSpan').innerHTML = "";

            document.getElementById('formulario_responsavelSel_id').value = "";
            document.getElementById('formulario_responsavelSel_sigla').value = "";
            document.getElementById('formulario_responsavelSel_descricao').value = "";
            document.getElementById('responsavelSelSpan').innerHTML = "";

            document.getElementById('formulario_cpOrgaoSel_id').value = "";
            document.getElementById('formulario_cpOrgaoSel_sigla').value = "";
            document.getElementById('formulario_cpOrgaoSel_descricao').value = "";
            document.getElementById('cpOrgaoSelSpan').innerHTML = "";

            document.getElementById('responsavelSelecionado').innerHTML = "";
        }

        function limparCampos() {
            limparSelecao();

            document.getElementById('checkall').checked = false;

            document.getElementsByName('dtDevolucaoMovString')[0].value = "";
            document.getElementById('obsOrgao').value = "";
        }

        function validar() {
            let lotaResponsavelSelSpan = document.getElementById('lotaResponsavelSelSpan');
            let responsavelSelSpan = document.getElementById('responsavelSelSpan');
            let cpOrgaoSelSpan = document.getElementById('cpOrgaoSelSpan');

            if (cpOrgaoSelSpan.textContent.trim() === ''
                && responsavelSelSpan.textContent.trim() === ''
                && lotaResponsavelSelSpan.textContent.trim() === '') {

                sigaModal.alerta('Selecione o destinatário da transferência');
                return;
            }

            let checkedElements = $("input[name='documentosSelecionados']:checked");
            if (checkedElements.length === 0) {
                sigaModal.alerta('Selecione pelo menos um documento');
            } else {
                sigaModal.abrir('confirmacaoModal');
            }
        }

        let siglasDocumentosTransferidos = [];
        let siglasDocumentosNaoTransferidos = [];

        function confirmar() {
            document.getElementById('btnOk').disabled = true;
            sigaModal.fechar('confirmacaoModal');
            enviarParaTramitacaoLote();
        }

        function enviarParaTramitacaoLote() {

            let lotacaoDestinoSelSigla = document.getElementById('formulario_lotaResponsavelSel_sigla').value;
            lotacaoDestinoSelSigla = lotacaoDestinoSelSigla.replaceAll('-', '');

            let usuarioDestinoSelSigla = document.getElementById('formulario_responsavelSel_sigla').value;
            let orgaoDestinoSelSigla = document.getElementById('formulario_cpOrgaoSel_sigla').value;

            let dtDevolucaoMovString = document.getElementsByName('dtDevolucaoMovString')[0].value;
            let obsOrgao = document.getElementById('obsOrgao').value;


            process.reset();

            process.push(function () {
                Log("Executando a tramitação em lote dos documentos selecionados")
            });

            Array.from($(".chkDocumento:checkbox").filter(":checked")).forEach(
                chk => {
                    process.push(function () {
                        return ExecutarPost(chk.value, lotacaoDestinoSelSigla, usuarioDestinoSelSigla,
                            orgaoDestinoSelSigla, dtDevolucaoMovString, obsOrgao);

                    });
                    process.push(function () {
                        chk.checked = false;
                    });
                }
            );

            process.push(function () {
                sigaSpinner.mostrar();
                limparCampos();

                let url = '${pageContext.request.contextPath}/app/expediente/mov/listar_docs_transferidos';
                location.href = url + '?siglasDocumentosTransferidos=' + siglasDocumentosTransferidos
                    + '&siglasDocumentosNaoTransferidos=' + siglasDocumentosNaoTransferidos;
            });

            process.run();

        }

        function ExecutarPost(documentoSelSigla, lotacaoDestinoSelSigla, usuarioDestinoSelSigla,
                              orgaoDestinoSelSigla, dtDevolucaoMovString, obsOrgao) {
            $.ajax({
                url: '/sigaex/api/v1/documentos/' + documentoSelSigla + '/tramitar',
                type: 'POST',
                data: {
                    sigla: documentoSelSigla,
                    lotacao: lotacaoDestinoSelSigla,
                    matricula: usuarioDestinoSelSigla,
                    orgao: orgaoDestinoSelSigla,
                    observacao: obsOrgao,
                    dataDevolucao: dtDevolucaoMovString
                },
                success: function () {
                    siglasDocumentosTransferidos.push(documentoSelSigla);
                },
                error: function (textStatus, errorThrown) {
                    console.log(textStatus + errorThrown)
                    siglasDocumentosNaoTransferidos.push(documentoSelSigla);
                }
            });
        }


    </script>
</siga:pagina>