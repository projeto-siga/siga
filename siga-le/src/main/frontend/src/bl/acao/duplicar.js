import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("duplicar", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Duplicando",
            "duplicacao",
            "duplicacao",
            null, "sigaex/api/v1/documentos/{siglamob}/duplicar",
            documentos, cont
        );
    },
}