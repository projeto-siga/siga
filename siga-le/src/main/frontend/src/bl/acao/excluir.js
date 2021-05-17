import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("excluir", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Excluindo",
            "exclusao",
            "exclusao",
            null, "sigaex/api/v1/documentos/{siglamob}/excluir",
            documentos, cont
        );
    },
}