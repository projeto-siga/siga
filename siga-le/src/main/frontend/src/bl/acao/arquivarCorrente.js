import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("arquivarCorrente", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Arquivando no Corrente",
            "arquivamento",
            "arquivado",
            "arquivado corrente", "sigaex/api/v1/documentos/{siglamob}/arquivar-corrente",
            documentos, cont
        );
    },
}