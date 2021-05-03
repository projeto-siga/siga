import Vue from 'vue'
import UtilsBL from "./utils.js";
import {
    Bus
} from "./bus.js";

import ArquivarCorrente from "./acao/arquivarCorrente";
import DesarquivarCorrente from "./acao/desarquivarCorrente";
import Sobrestar from "./acao/sobrestar";
import Dessobrestar from "./acao/dessobrestar";
import Juntar from "./acao/juntar";
import Vincular from "./acao/vincular";
import Apensar from "./acao/apensar";
import Desapensar from "./acao/desapensar";
import TornarSemEfeito from "./acao/tornarSemEfeito";
import IncluirCopia from "./acao/incluirCopia";
import CancelarMovimentacao from "./acao/cancelarMovimentacao";
import DefinirMarcador from "./acao/definirMarcador";
import AssinarComSenha from "./acao/assinarComSenha";
import AutenticarComSenha from "./acao/autenticarComSenha";
import CriarVia from "./acao/criarVia";
import Finalizar from "./acao/finalizar";
import Duplicar from "./acao/duplicar";
import DefinirPerfil from "./acao/definirPerfil";
import DefinirAcesso from "./acao/definirAcesso";

export default {
    bus: Bus,

    registrar(modais) {
        this.modais = modais;
        ArquivarCorrente.registrar();
        DesarquivarCorrente.registrar();
        Sobrestar.registrar();
        Dessobrestar.registrar();
        Juntar.registrar();
        Vincular.registrar();
        Apensar.registrar();
        Desapensar.registrar();
        TornarSemEfeito.registrar();
        IncluirCopia.registrar();
        CancelarMovimentacao.registrar();
        DefinirMarcador.registrar();
        AssinarComSenha.registrar();
        AutenticarComSenha.registrar();
        CriarVia.registrar();
        Finalizar.registrar();
        Duplicar.registrar();
        DefinirPerfil.registrar();
        DefinirAcesso.registrar();
    },

    lotePadrao: function (gerundio, evento1, evento2, evento3, url, documentos, cont, params) {
        Bus.$emit(
            "prgStart",
            gerundio,
            documentos.length,
            (i) => this.acaoPadrao(gerundio,
                evento1,
                evento2,
                evento3,
                url.replace("{siglamob}", documentos[i].codigo).replace("{sigladoc}", documentos[i].sigla.replace(/[^a-zA-Z0-9]/gi, "")),
                documentos[i], documentos.length !== 1, params),
            cont
        );
    },

    acaoPadrao: function (gerundio, evento1, evento2, evento3, url, d, lote, params) {
        this.errormsg = undefined;
        Bus.$emit("prgCaption", " " + d.sigla);

        if (!params)
            params = {};

        Vue.http
            .post(
                url, params, {
                    block: !lote
                }
            )
            .then(
                (result) => {
                    d.errormsg = undefined;
                    UtilsBL.logEvento(
                        evento1,
                        evento2,
                        evento3
                    );
                    Bus.$emit("prgNext", result);
                },
                (error) => {
                    if (lote) d.errormsg = error.data.errormsg;
                    else Bus.$emit("message", "Erro", error.data.errormsg);
                    Bus.$emit("prgNext");
                }
            );
    },
}