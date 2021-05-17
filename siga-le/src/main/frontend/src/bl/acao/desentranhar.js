import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("desentranharModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("desentranhar", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalTexto.show(documentos, cont, "desentranhar", "Cancelar", "Motivo");
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Desentranhando",
            "desentranhamento",
            "desentranhamento",
            undefined, "sigaex/api/v1/documentos/{siglamob}/desentranhar?motivo=" + params.texto,
            documentos, cont
        );
    },
}