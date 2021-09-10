import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("apensarModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("apensar", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalMob.show(documentos, cont, "apensar", "Apensar", "Documento Mestre");
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Apensamento",
            "apensamento",
            "apensamento",
            undefined, "sigaex/api/v1/documentos/{siglamob}/apensar?siglamestre=" + params.mob,
            documentos, cont
        );
    },
}