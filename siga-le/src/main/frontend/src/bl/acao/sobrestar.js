import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("sobrestar", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Sobrestando",
            "sobrestamento",
            "sobrestado",
            undefined, "sigaex/api/v1/documentos/{siglamob}/sobrestar",
            documentos, cont
        );
    },
}