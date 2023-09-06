package br.gov.jfrj.siga.ex.bl;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.ex.util.ExMovimentacaoRecebimentoComparator;

public class ExTramiteBL {

    public static class Pendencias {
        // Trâmite serial, paralelo e notificações
        public Set<ExMovimentacao> tramitesPendentes = new TreeSet<ExMovimentacao>();
        // Trâmite serial, paralelo e notificações recebidos e ainda não concluídos
        public Set<ExMovimentacao> recebimentosPendentes = new TreeSet<ExMovimentacao>();
        // Somente notificações
        public Set<ExMovimentacao> tramitesDeNotificacoesPendentes = new TreeSet<ExMovimentacao>();
        // Somente notificações recebidas e ainda não concluídos
        public Set<ExMovimentacao> recebimentosDeNotificacoesPendentes = new TreeSet<ExMovimentacao>();
        // Indica se o cadastrante do documento deve ser incluído na lista de atendentes
        public boolean fIncluirCadastrante = true;

        public SortedSet<ExMovimentacao> getRecebimentosPendentesSemNotificacoes() {
            SortedSet<ExMovimentacao> recebimentos = new TreeSet<>(new ExMovimentacaoRecebimentoComparator());
            // Tenta selecionar um recebimento da lotação, que não seja de notificação, que
            // será mantido
            for (ExMovimentacao r : recebimentosPendentes)
                if (!recebimentosDeNotificacoesPendentes.contains(r)) {
                    recebimentos.add(r);
                }
            return recebimentos;
        }
    }

    public static Pendencias calcularTramitesPendentes(ExMobil mobil) {
        SortedSet<ExMovimentacao> movs = new TreeSet<>();
        if (mobil.isVolume()) {
            ExMobil mob = mobil;

            // Se o volume acabou de ser criado e ainda não tem nenhum tramite,
            // buscar as informações no volume anterior
            if (mob.isUltimoVolume() && mob.getNumSequencia() > 1 && !mob.contemAlgumTramite())
                mob = mob.doc().getVolume(mob.getNumSequencia() - 1);

            // Primeiro localiza o último volume do apenso
            while (mob.isApensadoAVolumeDoMesmoProcesso())
                mob = mob.getMestre();

            // Obtem a lista completa de mobils, incluindo o grande mestre
            SortedSet<ExMobil> mobs = mob.getApensos(true, true);
            mobs.add(mob);

            // Acumula todas as movimentações de todos os volumes deste processo
            for (ExMobil m : mobs) {
                // Despreza móbiles que não sejam desse processo
                if (!m.doc().equals(mobil.doc()))
                    continue;
                movs.addAll(m.getExMovimentacaoSet());
            }
        } else {
            movs.addAll(mobil.getExMovimentacaoSet());
        }

        // Elimina recebimentos duplicados
        ExMovimentacao movAnt = null;
        SortedSet<ExMovimentacao> movsAExcluir = new TreeSet<>();
        for (ExMovimentacao mov : movs) {
            if (mov.isCancelada())
                continue;
            if (movAnt != null
                    && (ExTipoDeMovimentacao.hasTransferencia(mov.getExTipoMovimentacao())
                            || ExTipoDeMovimentacao.hasRecebimento(mov.getExTipoMovimentacao()))
                    && Utils.igual(mov.getExTipoMovimentacao(), movAnt.getExTipoMovimentacao())
                    && Utils.igual(mov.getExMobil(), movAnt.getExMobil())
                    && Utils.igual(mov.getExMovimentacaoRef(), movAnt.getExMovimentacaoRef())
                    && Utils.igual(mov.getResp(), movAnt.getResp())
                    && Utils.igual(mov.getLotaResp(), movAnt.getLotaResp()))
                movsAExcluir.add(mov);
            else
                movAnt = mov;
        }
        movs.removeAll(movsAExcluir);

        Pendencias p = new Pendencias();
        for (ExMovimentacao mov : movs) {
            if (mov.isCancelada())
                continue;
            ITipoDeMovimentacao t = mov.getExTipoMovimentacao();
            if ((t == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
                    || t == ExTipoDeMovimentacao.TRANSFERENCIA
                    || t == ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA
                    || t == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA
                    || t == ExTipoDeMovimentacao.TRAMITE_PARALELO
                    || t == ExTipoDeMovimentacao.NOTIFICACAO)) {
                // Trâmite sem movRef limpa todos os pendentes até agora
                if (t == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA || t == ExTipoDeMovimentacao.TRANSFERENCIA
                        || t == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA
                        || t == ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA) {
                    if (mov.getExMovimentacaoRef() == null
                            || !p.recebimentosPendentes.contains(mov.getExMovimentacaoRef()))
                        p.recebimentosPendentes.clear();
                    else
                        p.recebimentosPendentes.remove(mov.getExMovimentacaoRef());
                }
                p.tramitesPendentes.add(mov);
            }
            if (t == ExTipoDeMovimentacao.RECEBIMENTO || t == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA) {
                // Recebimento sem movRef limpa todos os pendentes até agora
                if (mov.getExMovimentacaoRef() == null || !p.tramitesPendentes.contains(mov.getExMovimentacaoRef()))
                    p.tramitesPendentes.clear();
                else
                    p.tramitesPendentes.remove(mov.getExMovimentacaoRef());
                p.recebimentosPendentes.add(mov);
            }
            // A juntada deve desativar os trâmites que não sejam de notificação
            if (t == ExTipoDeMovimentacao.JUNTADA) {
                p.tramitesPendentes.removeIf(mv -> mv.getExTipoMovimentacao() != ExTipoDeMovimentacao.NOTIFICACAO);
                p.recebimentosPendentes.removeIf(mv -> mv.getExTipoMovimentacao() != ExTipoDeMovimentacao.NOTIFICACAO);
            }
            if (mov.getExMovimentacaoRef() != null) {
                if (t == ExTipoDeMovimentacao.CONCLUSAO) {
                    // Existe a conclusão direta, que cancela um trâmite pendente, ou a conclusão
                    // normal que cancela um recebimento pendente
                    if (p.tramitesPendentes.contains(mov.getExMovimentacaoRef())
                            || p.recebimentosPendentes.contains(mov.getExMovimentacaoRef())) {
                        if (p.tramitesPendentes.contains(mov.getExMovimentacaoRef()))
                            p.tramitesPendentes.remove(mov.getExMovimentacaoRef());
                        if (p.recebimentosPendentes.contains(mov.getExMovimentacaoRef()))
                            p.recebimentosPendentes.remove(mov.getExMovimentacaoRef());
                    } else {
                        // Caso a movimentação não seja localizada, remover todas as pendências
                        // pois se trata de uma situação de erro
                        p.tramitesPendentes.clear();
                        p.recebimentosPendentes.clear();
                    }
                }
            } else {
                if (t == ExTipoDeMovimentacao.CONCLUSAO)
                    p.fIncluirCadastrante = false;
            }
            if (((t == ExTipoDeMovimentacao.TRANSFERENCIA || t == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
                    || t == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA
                    || t == ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA)
                    && (Utils.equivaleENaoENulo(mov.getCadastrante(), mobil.doc().getCadastrante())
                            || Utils.equivaleENaoENulo(mov.getLotaCadastrante(), mobil.doc().getLotaCadastrante())
                            || Utils.equivaleENaoENulo(mov.getTitular(), mobil.doc().getCadastrante())
                            || Utils.equivaleENaoENulo(mov.getLotaTitular(), mobil.doc().getLotaCadastrante())
                            || Utils.equivaleENaoENulo(mov.getCadastrante(), mobil.getTitular())
                            || Utils.equivaleENaoENulo(mov.getLotaCadastrante(), mobil.getLotaTitular())
                            || Utils.equivaleENaoENulo(mov.getTitular(), mobil.getTitular())
                            || Utils.equivaleENaoENulo(mov.getLotaTitular(), mobil.getLotaTitular())))
                    || t == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA)
                p.fIncluirCadastrante = false;
        }

        for (ExMovimentacao mov : p.tramitesPendentes) {
            if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.NOTIFICACAO)
                p.tramitesDeNotificacoesPendentes.add(mov);
        }
        for (ExMovimentacao mov : p.recebimentosPendentes) {
            if (mov.getExMovimentacaoRef() == null)
                continue;
            if (mov.getExMovimentacaoRef().getExTipoMovimentacao() == ExTipoDeMovimentacao.NOTIFICACAO)
                p.recebimentosDeNotificacoesPendentes.add(mov);
        }

        return p;
    }

}
