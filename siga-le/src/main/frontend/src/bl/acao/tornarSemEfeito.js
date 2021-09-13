import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("tornarSemEfeitoModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("tornarSemEfeito", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalTexto.show(documentos, cont, "tornarSemEfeito", "Cancelar", "Motivo");
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Tornando sem Efeito",
            "cancelamento",
            "tornandoSemEfeito",
            undefined, "sigaex/api/v1/documentos/{siglamob}/tornar-sem-efeito?motivo=" + params.texto,
            documentos, cont
        );
    },
}