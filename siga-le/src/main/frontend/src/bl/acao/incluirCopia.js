import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("incluirCopiaModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("incluirCopia", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalMob.show(documentos, cont, "incluirCopia", "Incluir Cópia", "Documento");
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Apensamento",
            "apensamento",
            "apensamento",
            undefined, "sigaex/api/v1/documentos/{siglamob}/incluir-copia?siglacopia=" + params.mob,
            documentos, cont
        );
    },
}