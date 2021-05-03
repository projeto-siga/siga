import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("vincularModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("vincular", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalMob.show(documentos, cont, "vincular", "Vincular", "Documento para Ver Também");
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Vinculando",
            "vínculo",
            "vínculo",
            undefined, "sigaex/api/v1/documentos/{siglamob}/vincular?siglavertambem=" + params.mob,
            documentos, cont
        );
    },
}