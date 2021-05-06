import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("incluirCossignatarioModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("incluirCossignatario", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalCossignatario.show(documentos, cont, "incluirCossignatario", "Incluir Cossignatário");
    },

    lote: function (documentos, cont, params) {
        console.log('teste')
        AcaoBL.lotePadrao("Incluindo Cossignatário em",
            "inclusaoDeCossignatario",
            "inclusaoDeCossignatario",
            undefined, "sigaex/api/v1/documentos/{siglamob}/incluir-cossignatario",
            documentos, cont, params
        );
    },
}