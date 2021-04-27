import Vue from 'vue'
import UtilsBL from "./utils.js";
import {
    Bus
} from "./bus.js";
import ArquivarCorrente from "./acao/arquivarCorrente";
import DesarquivarCorrente from "./acao/desarquivarCorrente";
import Sobrestar from "./acao/sobrestar";
import Dessobrestar from "./acao/dessobrestar";

export default {
    bus: Bus,

    registrar() {
        ArquivarCorrente.registrar();
        DesarquivarCorrente.registrar();
        Sobrestar.registrar();
        Dessobrestar.registrar();
    },

    lotePadrao: function (gerundio, evento1, evento2, evento3, url, documentos, cont) {
        Bus.$emit(
            "prgStart",
            gerundio,
            documentos.length,
            (i) => this.acaoPadrao(gerundio,
                evento1,
                evento2,
                evento3,
                url.replace("{sigla}", documentos[i].codigo),
                documentos[i], documentos.length !== 1),
            cont
        );
    },

    acaoPadrao: function (gerundio, evento1, evento2, evento3, url, d, lote) {
        this.errormsg = undefined;
        Bus.$emit("prgCaption", " " + d.sigla);

        Vue.http
            .post(
                url, {}, {
                    block: !lote
                }
            )
            .then(
                () => {
                    d.errormsg = undefined;
                    UtilsBL.logEvento(
                        evento1,
                        evento2,
                        evento3
                    );
                    Bus.$emit("prgNext");
                },
                (error) => {
                    if (lote) d.errormsg = error.data.errormsg;
                    else Bus.$emit("message", "Erro", error.data.errormsg);
                    Bus.$emit("prgNext");
                }
            );
    },
}