import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("criarVia", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Criando Via",
            "criacao",
            "criacao-de-via",
            undefined, "sigaex/api/v1/documentos/{siglamob}/criar-via",
            documentos, cont
        );
    },
}