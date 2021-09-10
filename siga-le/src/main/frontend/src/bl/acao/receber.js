import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("receber", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Recebendo",
            "recebimento",
            "recebimento",
            undefined, "sigaex/api/v1/documentos/{siglamob}/receber",
            documentos, cont
        );
    },
}