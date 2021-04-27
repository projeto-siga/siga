import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("sobrestar", (documentos, cont) => {
            this.lote(documentos, cont);
        });
    },

    lote: function (documentos, cont) {
        AcaoBL.lotePadrao("Criando Via",
            "ação",
            "criar via",
            undefined, "sigaex/api/v1/documentos/{sigla}/criar-via",
            documentos, cont
        );
    },
}