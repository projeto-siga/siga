import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("assinarComSenha", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Assinando",
            "assinatura em lote",
            "assinado",
            "assinado com senha", "sigaex/api/v1/documentos/{siglamob}/assinar-com-senha",
            documentos, cont
        );
    },
}