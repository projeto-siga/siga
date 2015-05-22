package br.gov.jfrj.siga.sr.notifiers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.freemarker.Freemarker;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.view.LinkToHandler;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrGestorItem;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import freemarker.template.TemplateException;

@Component
@RequestScoped
public class Correio {
    private static final String MOVIMENTACAO_DA_SOLICITACAO = "Movimentação da solicitação ";
    private static final String EMAIL_ADMINISTRADOR_DO_SIGA = "Administrador do Siga<sigadocs@jfrj.jus.br>";

    private Freemarker freemarker;
    private LinkToHandler linkTo;

    public Correio(Freemarker freemarker, LinkToHandler linkTo) {
        this.freemarker = freemarker;
        this.linkTo = linkTo;
    }

    private void enviar(String remetente, String assunto, String conteudo, String... destinatarios) throws Exception {
        br.gov.jfrj.siga.base.Correio.enviar(remetente, destinatarios, assunto, conteudo);
    }

    private void enviar(String remetente, String assunto, String conteudo, Object[] destinatarios) throws Exception {
        br.gov.jfrj.siga.base.Correio.enviar(remetente, (String[]) destinatarios, assunto, conteudo);
    }

    public void notificarAbertura(SrSolicitacao sol) throws Exception {
        DpPessoa pessoaAtual = sol.getSolicitante().getPessoaAtual();
        if (pessoaAtual == null || pessoaAtual.getEmailPessoa() == null)
            return;

        String assunto = "";
        if (sol.isFilha())
            assunto = "Escalonamento da solicitação " + sol.getSolicitacaoPai().getCodigo();
        else
            assunto = "Abertura da solicitação " + sol.getCodigo();

        String conteudo = getConteudoComSolicitacao(sol);

        enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, pessoaAtual.getEmailPessoa());
    }

    public void notificarMovimentacao(SrMovimentacao movimentacao) throws Exception {
        SrSolicitacao sol = movimentacao.getSolicitacao().getSolicitacaoAtual();
        DpPessoa pessoaAtual = sol.getSolicitante().getPessoaAtual();
        if (pessoaAtual == null || pessoaAtual.getEmailPessoa() == null)
            return;
        String assunto = MOVIMENTACAO_DA_SOLICITACAO + sol.getCodigo();
        String conteudo = getConteudoComSolicitacaoEMovimentacao(movimentacao, sol);

        enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, pessoaAtual.getEmailPessoa());
    }

    public void notificarAtendente(SrMovimentacao movimentacao) throws Exception {
        SrSolicitacao sol = movimentacao.getSolicitacao().getSolicitacaoAtual();
        String assunto = MOVIMENTACAO_DA_SOLICITACAO + sol.getCodigo();
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

        if (!recipients.isEmpty()) {
            String conteudo = getConteudoComSolicitacaoEMovimentacao(movimentacao, sol);
            enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, recipients.toArray());
        }
    }

    public void notificarReplanejamentoMovimentacao(SrMovimentacao movimentacao) throws Exception {
        SrSolicitacao sol = movimentacao.getSolicitacao().getSolicitacaoAtual();
        List<String> recipients = new ArrayList<String>();
        String assunto = MOVIMENTACAO_DA_SOLICITACAO + sol.getCodigo();
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

        if (!recipients.isEmpty()) {
            String conteudo = getConteudoComSolicitacaoEMovimentacao(movimentacao, sol);
            enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, recipients.toArray());
        }
    }

    public void notificarCancelamentoMovimentacao(SrMovimentacao movimentacao) throws Exception {
        SrSolicitacao sol = movimentacao.getSolicitacao().getSolicitacaoAtual();
        DpPessoa solicitanteAtual = sol.getSolicitante().getPessoaAtual();
        if (solicitanteAtual.getEmailPessoa() == null)
            return;
        String assunto = MOVIMENTACAO_DA_SOLICITACAO + sol.getCodigo();
        String conteudo = getConteudoComSolicitacaoEMovimentacao(movimentacao, sol);
        enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, solicitanteAtual.getEmailPessoa());
    }

    public void pesquisaSatisfacao(SrSolicitacao sol) throws Exception {
        SrSolicitacao solAtual = sol.getSolicitacaoAtual();
        DpPessoa solicitanteAtual = solAtual.getSolicitante().getPessoaAtual();
        if (solicitanteAtual.getEmailPessoa() == null)
            return;
        String assunto = MOVIMENTACAO_DA_SOLICITACAO + sol.getCodigo();
        String conteudo = getConteudoComSolicitacao(sol);
        enviar(EMAIL_ADMINISTRADOR_DO_SIGA, assunto, conteudo, solicitanteAtual.getEmailPessoa());
    }

    private String getConteudoComSolicitacao(SrSolicitacao sol) throws IOException, TemplateException {
        return freemarker.use("notificar").with("sol", sol).with("linkTo", linkTo).getContent();
    }

    private String getConteudoComSolicitacaoEMovimentacao(SrMovimentacao movimentacao, SrSolicitacao sol) throws IOException, TemplateException {
        return freemarker.use("notificar").with("sol", sol).with("movimentacao", movimentacao).with("linkTo", linkTo).getContent();
    }
}
