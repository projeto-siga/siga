import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("cancelarMovimentacao", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Desfazendo Movimentação",
            "cancelamento",
            "movimentacao",
            undefined, "sigaex/api/v1/documentos/{siglamob}/movimentacoes/" + params.idMov + "/cancelar",
            documentos, cont
        );
    },
}