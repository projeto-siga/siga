import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("refazer", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Refazendo",
            "refazer",
            "refazer",
            null, "sigaex/api/v1/documentos/{siglamob}/refazer",
            documentos, cont
        );
    },
}