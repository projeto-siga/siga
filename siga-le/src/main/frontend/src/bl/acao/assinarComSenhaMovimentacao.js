import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("assinarComSenhaMovimentacao", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Assinar Movimentação",
            "assinatura em lote",
            "assinando movimentacao",
            "assinando com senha", "sigaex/api/v1/documentos/{siglamob}/movimentacoes/" + params.idMov + "/assinar-com-senha",
            documentos, cont
        );
    },
}