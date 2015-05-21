package br.gov.jfrj.siga.sr.notifiers;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrGestorItem;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;

public class Correio {

    private static final String MOVIMENTAÇÃO_DA_SOLICITAÇÃO = "Movimentação da solicitação ";
    private static final String EMAIL_ADMINISTRADOR_DO_SIGA = "Administrador do Siga<sigadocs@jfrj.jus.br>";

    private static void enviar(String remetente, String assunto, String conteudo, String... destinatarios) throws Exception {
        br.gov.jfrj.siga.base.Correio.enviar(remetente, destinatarios, assunto, conteudo);
    }

    private static void enviar(String remetente, String assunto, String conteudo, Object[] destinatarios) throws Exception {
        br.gov.jfrj.siga.base.Correio.enviar(remetente, (String[]) destinatarios, assunto, conteudo);
    }

    public static void notificarAbertura(SrSolicitacao sol) throws Exception {
        DpPessoa pessoaAtual = sol.getSolicitante().getPessoaAtual();
        if (pessoaAtual == null || pessoaAtual.getEmailPessoa() == null)
            return;

        String assunto = "";
        if (sol.isFilha())
            assunto = "Escalonamento da solicitação " + sol.getSolicitacaoPai().getCodigo();
        else
            assunto = "Abertura da solicitação " + sol.getCodigo();

        String conteudo = "";

        enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, pessoaAtual.getEmailPessoa());
        // send(sol);
    }

    public static void notificarMovimentacao(SrMovimentacao movimentacao) throws Exception {
        SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
        DpPessoa pessoaAtual = sol.getSolicitante().getPessoaAtual();
        if (pessoaAtual == null || pessoaAtual.getEmailPessoa() == null)
            return;
        String assunto = MOVIMENTAÇÃO_DA_SOLICITAÇÃO + sol.getCodigo();
        String conteudo = "";

        enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, pessoaAtual.getEmailPessoa());
        // send(movimentacao, sol);
    }

    public static void notificarAtendente(SrMovimentacao movimentacao) {
        SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
        String assunto = MOVIMENTAÇÃO_DA_SOLICITAÇÃO + sol.getCodigo();
        List<String> recipients = new ArrayList<String>();
        String email = null;

        DpPessoa atendenteSolPai = sol.getSolicitacaoPai().getAtendente();
        if (atendenteSolPai != null) {
            email = atendenteSolPai.getPessoaAtual().getEmailPessoa();
            if (email != null)
                recipients.add(email);
        } else {
            DpLotacao lotaAtendenteSolPai = sol.getSolicitacaoPai().getLotaAtendente();
            if (lotaAtendenteSolPai != null)
                for (DpPessoa pessoaDaLotacao : lotaAtendenteSolPai.getDpPessoaLotadosSet())
                    if (pessoaDaLotacao.getDataFim() == null) {
                        email = pessoaDaLotacao.getPessoaAtual().getEmailPessoa();
                        if (email != null)
                            recipients.add(email);
                    }
        }

        if (recipients.size() > 0)
            try {
                String conteudo = "";
                enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, recipients.toArray());
                // send(movimentacao, sol);
            } catch (Exception e) {
                // Logger.error(e, "Nao foi possivel notificar o atendente");
            }
    }

    public static void notificarReplanejamentoMovimentacao(SrMovimentacao movimentacao) throws Exception {
        SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
        List<String> recipients = new ArrayList<String>();
        String assunto = MOVIMENTAÇÃO_DA_SOLICITAÇÃO + sol.getCodigo();
        for (SrGestorItem gestor : sol.getItemConfiguracao().getGestorSet()) {
            DpPessoa pessoaGestorAtual = gestor.getDpPessoa().getPessoaAtual();
            if (pessoaGestorAtual != null && pessoaGestorAtual.getDataFim() == null)
                if (pessoaGestorAtual.getEmailPessoa() != null)
                    recipients.add(pessoaGestorAtual.getEmailPessoa());

            if (gestor.getDpLotacao() != null)
                for (DpPessoa gestorPessoa : gestor.getDpLotacao().getDpPessoaLotadosSet())
                    if (gestorPessoa.getPessoaAtual().getDataFim() == null)
                        if (gestorPessoa.getPessoaAtual().getEmailPessoa() != null)
                            recipients.add(gestorPessoa.getPessoaAtual().getEmailPessoa());
        }
        recipients.add(sol.getSolicitante().getEmailPessoa());
        if (recipients.size() > 0) {
            String conteudo = "";
            enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, recipients.toArray());
//            send(movimentacao, sol);
        }
    }

    public static void notificarCancelamentoMovimentacao(SrMovimentacao movimentacao) throws Exception {
        SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
        DpPessoa solicitanteAtual = sol.getSolicitante().getPessoaAtual();
        if (solicitanteAtual.getEmailPessoa() == null)
            return;
        String assunto = MOVIMENTAÇÃO_DA_SOLICITAÇÃO + sol.getCodigo();
        String conteudo = "";
        enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, solicitanteAtual.getEmailPessoa());
//        send(movimentacao, sol);
    }

    public static void pesquisaSatisfacao(SrSolicitacao sol) throws Exception {
        SrSolicitacao solAtual = sol.getSolicitacaoAtual();
        DpPessoa solicitanteAtual = solAtual.getSolicitante().getPessoaAtual();
        if (solicitanteAtual.getEmailPessoa() == null)
            return;
        String assunto = MOVIMENTAÇÃO_DA_SOLICITAÇÃO + sol.getCodigo();
        String conteudo = "";
        enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo , solicitanteAtual.getEmailPessoa());
//        send(sol);
    }
}
