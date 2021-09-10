import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("autenticarComSenha", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Autenticando",
            "assinatura em lote",
            "autenticando",
            "autenticando com senha", "sigaex/api/v1/documentos/{siglamob}/autenticar-com-senha",
            documentos, cont
        );
    },
}