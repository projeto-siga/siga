import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("autenticarComSenhaMovimentacao", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Autenticando Movimentação",
            "assinatura em lote",
            "autenticando movimentacao",
            "autenticando com senha", "sigaex/api/v1/documentos/{siglamob}/movimentacoes/" + params.idMov + "/autenticar-com-senha",
            documentos, cont
        );
    },
}