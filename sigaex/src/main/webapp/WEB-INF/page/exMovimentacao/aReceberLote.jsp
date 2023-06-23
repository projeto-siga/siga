<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Recebimento em Lote"> 

	<c:set var="thead_color" value="${thead_color}" scope="session"/>

	<div class="container-fluid">
	
	<div class="card bg-light mb-3">
            <div class="card-header">
                <h5>Receber em Lote</h5>
            </div>
            <div class="card-body">
                <form name="frm" id="frm" class="form" method="POST" action="listar_docs_recebidos" theme="simple">
                
                    <input type="hidden" name="siglasDocumentosRecebidosEmLote" value=""/>
                    <input type="hidden" name="errosDocumentosNaoRecebidosJson" value=""/>
                    
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label>Atendente</label>
                                <select class="form-control siga-select2" id="selectAtendente">
                                    <option value="pessoa" selected>${cadastrante.nomePessoa}</option>
                                    <option value="lotacao">
                                            ${cadastrante.lotacao.siglaCompleta} / ${cadastrante.lotacao.nomeLotacao}
                                    </option>
                                </select>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <br/>
                                <a href="javascript: listarDocumentosParaReceberEmLote()"
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
                                    Receber
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
                Os documentos selecionados ser&atilde;o recebidos. Deseja, confirmar?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">N&atilde;o</button>
                <a href="#" class="btn btn-success btn-confirmacao" role="button" aria-pressed="true"
                   onclick="confirmar();">
                    Sim</a>
            </div>
        </siga:siga-modal>
        <siga:siga-modal id="progressModal" exibirRodape="false" centralizar="true" tamanhoGrande="true"
                         tituloADireita="Recebimento em lote" linkBotaoDeAcao="#" botaoFecharNoCabecalho="false">
            <div class="modal-body">
                <div id="progressbar-ad"></div>
            </div>
        </siga:siga-modal>
        
	</div>

<script type="text/javascript">
	
	let siglasDocumentosRecebidosEmLote = [];
	let errosDocumentosNaoRecebidosMap = new Map();
	
	function listarDocumentosParaReceberEmLote(offset) {
	    sigaSpinner.mostrar();
	
	    offset = offset == null ? 0 : offset;
	
	    let selectAtendenteElement = document.getElementById('selectAtendente');
	    let selectAtendenteValue = selectAtendenteElement.value;
	
	    let url = '/sigaex/app/expediente/mov/listar_docs_para_receber_em_lote'
	        + '?atendente=' + selectAtendenteValue
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
            sigaModal.alerta('Selecione pelo menos um documento');
        } else {
            sigaModal.abrir('confirmacaoModal');
        }
    }
	
	function confirmar() {
        document.getElementById("btnOk").disabled = true;
        sigaModal.fechar('confirmacaoModal');
        enviarParaRecebimentoEmLote();
    }
	
	function enviarParaRecebimentoEmLote() {

        process.push(function () {
            $('#progressModal').modal({
                backdrop: 'static',
                keyboard: false
            });
        });

        Array.from($(".chkMobil:checkbox").filter(":checked")).forEach(
            chk => {
                process.push(function () {
                    return receberPost(chk.value);
                });
                process.push(function () {
                    chk.checked = false;
                });
            }
        );

        process.push(function () {
            sigaModal.fechar('progressModal');
            sigaSpinner.mostrar();
            enviarParaListagemDocumentosRecebidos();
        });

        process.run();
    }
	
	function receberPost(documentoSelSigla) {
        $.ajax({
            url: '/sigaex/api/v1/documentos/' + documentoSelSigla.replace('/', '') + '/receber',
            type: 'POST',
            async: false,
            dataType: 'json',
            success: function () {
                siglasDocumentosRecebidosEmLote.push(documentoSelSigla);
            },
            error: function (response) {
            	errosDocumentosNaoRecebidosMap.set(documentoSelSigla, response.responseJSON.errormsg);
            }
        });
    }
	
	function enviarParaListagemDocumentosRecebidos() {
        document.getElementsByName('siglasDocumentosRecebidosEmLote')[0].value = siglasDocumentosRecebidosEmLote;

        let errosDocumentosNaoRecebidosJson = JSON.stringify(Object.fromEntries(errosDocumentosNaoRecebidosMap));
        document.getElementsByName('errosDocumentosNaoRecebidosJson')[0].value = errosDocumentosNaoRecebidosJson;

        document.frm.submit();
    }
	
	let process = {
            steps: [],
            index: 0,
            title: "Executando o recebimento em lote dos documentos selecionados",
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