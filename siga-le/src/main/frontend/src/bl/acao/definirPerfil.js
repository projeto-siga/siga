import AcaoBL from "../acao"

export default {
    registrar() {
        AcaoBL.bus.$on("definirPerfilModal", (documentos, cont) => {
            this.modal(documentos, cont);
        });

        AcaoBL.bus.$on("definirPerfil", (documentos, cont, params) => {
            this.lote(documentos, cont, params);
        });
    },

    modal: function (documentos, cont) {
        AcaoBL.modais.modalPerfil.show(documentos, cont, "definirPerfil", "Definir Perfil");
    },

    lote: function (documentos, cont, params) {
        AcaoBL.lotePadrao("Definição de Perfil",
            "definicao-de-perfil",
            "definicao-de-perfil",
            undefined, "sigaex/api/v1/documentos/{sigladoc}/definir-perfil",
            documentos, cont, params
        );
    },
}