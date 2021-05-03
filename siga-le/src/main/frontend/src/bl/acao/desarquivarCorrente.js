import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("desarquivarCorrente", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Desarquivando no Corrente",
            "arquivamento",
            "desarquivado",
            "desarquivado corrente", "sigaex/api/v1/documentos/{siglamob}/desarquivar-corrente",
            documentos, cont
        );
    },
}