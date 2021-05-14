import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("dessobrestar", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Dessobrestando",
            "sobrestamento",
            "dessobrestado",
            undefined, "sigaex/api/v1/documentos/{siglamob}/dessobrestar",
            documentos, cont
        );
    },
}