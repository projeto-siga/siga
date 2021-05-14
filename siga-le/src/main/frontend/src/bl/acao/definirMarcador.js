import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("definirMarcadorModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("definirMarcador", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalMarcador.show(documentos, cont, "definirMarcador", "Definir Marcador");
    },

    lote: function (documentos, cont, params) {
        console.log('teste')
        AcaoBL.lotePadrao("Definição de Marcador",
            "marcacao",
            "marcacao",
            undefined, "sigaex/api/v1/documentos/{siglamob}/marcar",
            documentos, cont, params
        );
    },
}