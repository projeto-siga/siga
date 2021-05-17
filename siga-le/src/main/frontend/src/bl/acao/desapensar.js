import AcaoBL from "../acao"

export default {

    registrar() {
        AcaoBL.bus.$on("desapensar", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Desapensando",
            "apensamento",
            "desapensamento",
            undefined, "sigaex/api/v1/documentos/{siglamob}/desapensar",
            documentos, cont
        );
    },
}