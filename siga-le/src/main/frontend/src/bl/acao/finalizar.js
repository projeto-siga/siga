import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("finalizar", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Finalizando",
            "finalizacao",
            "finalizado",
            null, "sigaex/api/v1/documentos/{siglamob}/finalizar",
            documentos, cont
        );
    },
}