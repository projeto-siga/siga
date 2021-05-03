import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("juntarModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("juntar", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalMob.show(documentos, cont, "juntar", "Juntar", "Documento Pai");
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Juntando",
            "juntada",
            "juntada",
            undefined, "sigaex/api/v1/documentos/{siglamob}/juntar?siglapai=" + params.mob,
            documentos, cont
        );
    },
}