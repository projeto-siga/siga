import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("anexarModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("anexar", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalAnexo.show(documentos, cont, "anexar", "Anexar", "Descrição do Anexo");
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Anexação",
            "anexo",
            "anexo",
            undefined, "sigaex/api/v1/documentos/{siglamob}/anexar",
            documentos, cont, params
        );
    },
}