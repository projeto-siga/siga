import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("definirAcessoModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("definirAcesso", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalAcesso.show(documentos, cont, "definirAcesso", "Definir Acesso");
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Definição de Acesso",
            "definicao-de-acesso",
            "definicao-de-acesso",
            undefined, "sigaex/api/v1/documentos/{sigladoc}/definir-acesso",
            documentos, cont, params
        );
    },
}