package br.gov.jfrj.siga.matrix.sinc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.gov.jfrj.siga.cp.util.SigaCpSinc;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sinc.lib.Item;
import br.gov.jfrj.siga.sinc.lib.Item.Operacao;
import br.gov.jfrj.siga.sinc.lib.OperadorSemHistorico;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;

public class SigaCpSincExtMatrix extends SigaCpSinc {
    private static boolean LOG_SIMULACAO = false;

    private Logger log = Logger.getLogger(" br.gov.jfrj.siga.matrix.sinc");
    private boolean modoLog = true;

    private static final String ACC_LVL_SEM_ACESSO = "NO ACC";
    private static final String ACC_LVL_SERVIDOR_SJRJ = "SJRJ-SERVI";
    private static final String ACC_LVL_ESTAGIARIO_SJRJ = "SJRJ-ESTAG";
    private static final String ACC_LVL_SERVIDOR_TRF2 = "TRF2-SERVI";
    private static final String ACC_LVL_ESTAGIARIO_TRF2 = "TRF2-ESTAG";

    private static final char CARD_TYPE_USUARIO = 'U';
    private static final char CARD_TYPE_TEMPORARIO = 'T';
    String[] args;

    private Set<String> cardNumbersGeralSemAdvogados = new HashSet<String>(); // utilizado para resolver conflitos de
                                                                              // card number de advogados

    private static String fix(String s) {
        if (s == null)
            return null;
        return s.trim();
    }

    public void matrix() throws Exception {
        // try {
        log("Importando matrix...");
        carregarCards();
        Map<String, String> idsDepartCorp = carregarDepartment();
        Map<String, String> idsJobTitleCorp = carregarJobTitle();

        log("Importando corporativo...");

        List<Sincronizavel> sincs = new ArrayList<Sincronizavel>();
        sincs.addAll(importarTabela());

        carregarLotacoes(idsDepartCorp, sincs);
        carregarCargos(idsJobTitleCorp, sincs);
        carregarPessoas(idsDepartCorp, idsJobTitleCorp, sincs);

        log("Gravando alterações...");
        List<Item> list = gravarAlteracoes();
        log("Total de alterações: " + list.size());
    }

    private void carregarPessoas(Map<String, String> idsDepartCorp,
            Map<String, String> idsJobTitleCorp, List<Sincronizavel> sincs) throws Exception {
        List<Cards> cardsCorp = new ArrayList<Cards>();
        SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMdd");
        String startingDate = formatador.format(new Date());

        for (Sincronizavel s : sincs) {
            if (s instanceof DpPessoa) {
                DpPessoa p = (DpPessoa) s;

                // apenas servidores e estagiarios do trf2
                if (p.getOrgaoUsuario().getId() == 3 && (!isServidorTRF2(p) && !isEstagiarioTRF2(p))) {
                    continue;
                }
                // apenas servidores e estagi�rios da sjrj
                if (p.getOrgaoUsuario().getId() == 1 && (!p.getCpTipoPessoa().getDscTpPessoa().equals("Servidor")
                        && !p.getCpTipoPessoa().getDscTpPessoa().equals("Estagi�rio"))) {
                    continue;
                }

                if (!"1".equals(p.getSituacaoFuncionalPessoa()))
                    continue;
                Cards c = new Cards();
                c.setStaffNumber(String.format("%1$-12s", p.getSigla()));
                c.setCardNumber(String.format("%010d", c.getId()));
                String prefixo_Card = definirPrefixoCard(p);

                // Aqui deve ser substituido por uma pesquisa nas informacoes que vem do matrix
                // e, se estiver preechido, copiar de l� a informa�ao.
                c.setCardNumber(prefixo_Card
                        + c.getCardNumber().substring(prefixo_Card.length()));

                c.setName(p.getNomePessoaAI());
                c.setPin("0000");
                c.setStartingDate(startingDate);
                c.setExpiryDate("20501231");

                // os setStaffNumber a seguir foram comentados de acordo com o pedido
                // do Fabricio na reuniao de 27/08/2019 (info em ata)

                if (cpOrgaoUsuario.getAcronimoOrgaoUsu().equals("TRF2")) {
                    if (isEstagiarioTRF2(p)) {
                        c.setAccessLevel(ACC_LVL_ESTAGIARIO_TRF2);
                        String estag = "T2E" + c.getId(); // aqui entra T2E + Matricula
                        // c.setStaffNumber(String.format("%1$-12s", estag));
                    } else {
                        c.setAccessLevel(ACC_LVL_SERVIDOR_TRF2);
                        // c.setStaffNumber(String.format("%1$-12s", p.getSigla()));
                    }
                }
                if (cpOrgaoUsuario.getAcronimoOrgaoUsu().equals("JFRJ")) {
                    if (p.getCpTipoPessoa().getDscTpPessoa().equals("Estagi�rio")) {
                        c.setAccessLevel(ACC_LVL_ESTAGIARIO_SJRJ);
                        String estag = "RJE" + c.getId(); // aqui entra RJE + Matricula
                        // c.setStaffNumber(String.format("%1$-12s", estag));
                    }
                    if (p.getCpTipoPessoa().getDscTpPessoa().equals("Servidor")) {
                        c.setAccessLevel(ACC_LVL_SERVIDOR_SJRJ);
                        // c.setStaffNumber(String.format("%1$-12s", p.getSigla()));
                    }
                }

                c.setFloorAccess(ACC_LVL_SEM_ACESSO);
                c.setPhoto(c.getId() + ".jpg");
                if (p.getLotacao() != null)
                    c.setDepartCode(idsDepartCorp.get(geraDepartDesc(p
                            .getLotacao())));
                if (c.getDepartCode() == null)
                    c.setDepartCode("NA");
                if (p.getCargo() != null)
                    c.setPositionCode(idsJobTitleCorp.get(geraPositionDesc(p
                            .getCargo())));
                if (c.getPositionCode() == null)
                    c.setPositionCode("NA");
                c.setCardType(CARD_TYPE_USUARIO);
                if (c.getPositionCode() == null)
                    log("null position code: " + c.getDescricaoExterna());
                c.setUser1(String.format("%011d", p.getCpfPessoa()));
                c.setUser2("SIGA-" + cpOrgaoUsuario.getSiglaOrgaoUsu());
                c.setUser4(p.getEmailPessoaAtual());
                cardsCorp.add(c);
            }
        }
        setNovo.addAll(cardsCorp);
    }

    private void carregarCargos(Map<String, String> idsJobTitleCorp,
            List<Sincronizavel> sincs) {
        List<JobTitle> jobTitleCorp = new ArrayList<JobTitle>();
        for (Sincronizavel s : sincs) {
            if (s instanceof DpCargo) {
                DpCargo l = (DpCargo) s;
                JobTitle d = new JobTitle();
                d.setPositionDesc(geraPositionDesc(l));
                if (idsJobTitleCorp.containsKey(d.getPositionDesc()))
                    d.setPositionCode(idsJobTitleCorp.get(d.getPositionDesc()));
                else {
                    String positionCode;
                    while (true) {
                        positionCode = geraSenha(4, true, false, true);
                        if (!idsJobTitleCorp.containsValue(positionCode))
                            break;
                    }
                    d.setPositionCode(positionCode);
                    idsJobTitleCorp.put(d.getPositionDesc(),
                            d.getPositionCode());
                }
                jobTitleCorp.add(d);
            }
        }
        setNovo.addAll(jobTitleCorp);
    }

    private void carregarLotacoes(Map<String, String> idsDepartCorp,
            List<Sincronizavel> sincs) {
        List<Department> departCorp = new ArrayList<Department>();
        for (Sincronizavel s : sincs) {
            if (s instanceof DpLotacao) {
                DpLotacao l = (DpLotacao) s;
                Department d = new Department();
                d.setDepartDesc(geraDepartDesc(l));
                if (idsDepartCorp.containsKey(d.getDepartDesc()))
                    d.setDepartCode(idsDepartCorp.get(d.getDepartDesc()));
                else {
                    String departCode;
                    while (true) {
                        departCode = geraSenha(4, true, false, true);
                        if (!idsDepartCorp.containsValue(departCode))
                            break;
                    }
                    d.setDepartCode(departCode);
                    idsDepartCorp.put(d.getDepartDesc(), d.getDepartCode());
                }
                departCorp.add(d);
            }
        }
        setNovo.addAll(departCorp);
    }

    private void ajustarCards(final Criteria crit) {
        @SuppressWarnings("unchecked")
        List<Cards> cardsMatrix = crit.list();
        for (Cards c : cardsMatrix) {
            c.setStaffNumber(c.getStaffNumber());
            c.setCardNumber(fix(c.getCardNumber()));
            c.setName(fix(c.getName()));
            c.setPin(fix(c.getPin()));
            c.setStartingDate(fix(c.getStartingDate()));
            c.setExpiryDate(fix(c.getExpiryDate()));
            c.setAccessLevel(fix(c.getAccessLevel()));
            c.setFloorAccess(fix(c.getFloorAccess()));
            c.setPhoto(fix(c.getPhoto()));
            c.setDepartCode(fix(c.getDepartCode()));
            c.setPositionCode(fix(c.getPositionCode()));
            c.setUser1(fix(c.getUser1()));
            c.setUser2(fix(c.getUser2()));
            c.setUser3(fix(c.getUser3()));
            c.setUser4(fix(c.getUser4()));
            c.setCardType(c.getCardType());
        }
        setAntigo.addAll(cardsMatrix);
    }

    private boolean isServidorTRF2(DpPessoa p) {
        return p.getMatricula() >= 10000L && p.getMatricula() < 16000L;
    }

    private boolean isEstagiarioTRF2(DpPessoa p) {
        return p.getMatricula() >= 50000L && p.getMatricula() < 80000L;
    }

    private String definirPrefixoCard(DpPessoa p) throws Exception {
        String prefixoCard = "";
        if (ehTRF2(cpOrgaoUsuario)) {
            if (isEstagiarioTRF2(p)) {
                prefixoCard = "00000";
            } else {
                prefixoCard = "00001";
            }
        } else if (ehJFES(cpOrgaoUsuario)) {
            prefixoCard = "00003";
        } else if (ehJFRJ(cpOrgaoUsuario)) {
            if (ehEstagiarioJFRJ(p)) {
                prefixoCard = "00555";
            } else {
                prefixoCard = "00002";
            }
        }
        return prefixoCard;
    }

    private boolean ehEstagiarioJFRJ(DpPessoa pessoa) {
        return pessoa.getCpTipoPessoa().getDscTpPessoa().equals("Estagiário");
    }

    private boolean ehJFES(CpOrgaoUsuario orgao) {
        return orgao.getAcronimoOrgaoUsu().equals("JFES");
    }

    private boolean ehTRF2(CpOrgaoUsuario orgao) {
        return orgao.getAcronimoOrgaoUsu().equals("TRF2");
    }

    private boolean ehJFRJ(CpOrgaoUsuario orgao) {
        return orgao.getAcronimoOrgaoUsu().equals("JFRJ");
    }

    protected String geraDepartDesc(DpLotacao l) {
        return cpOrgaoUsuario.getSiglaOrgaoUsu() + ": " + l.getSigla();
    }

    protected String geraPositionDesc(DpCargo l) {
        return cpOrgaoUsuario.getSiglaOrgaoUsu() + ": " + l.getDescricao();
    }

    public static String geraSenha(int tamanho, boolean fMaiusculas,
            boolean fMinusculas, boolean fNumeros) {
        final StringBuilder caracteres = new StringBuilder();

        for (int i = 0; i < 26; i++) {

            if (fMinusculas) {
                final char minuscula = (char) ('a' + i);
                // Exclus�o das letras o e O na gera��o de senha
                if (minuscula != 'o')
                    caracteres.append(minuscula);
            }
            if (fMaiusculas) {
                final char maiuscula = (char) ('A' + i);
                // Exclus�o das letras o e O na gera��o de senha
                if (maiuscula != 'O')
                    caracteres.append(maiuscula);
            }

        }

        // exclus�o do n�mero zero na gera��o de senha
        for (int i = 0; i < 10 - 1; i++) {
            caracteres.append('1' + i);
        }

        boolean contemMinusculas;
        boolean contemMaiusculas;
        boolean contemNumeros;

        final Random random = new Random();
        StringBuilder novaSenha;

        do {
            novaSenha = new StringBuilder();
            contemMinusculas = false;
            contemMaiusculas = false;
            contemNumeros = false;
            for (int i = 0; i < tamanho; i++) {
                novaSenha.append(caracteres.charAt(random.nextInt(caracteres
                        .length())));
            }

            for (int i = 0; i < novaSenha.length(); i++) {
                if (Character.isLowerCase(novaSenha.charAt(i))) {
                    contemMinusculas = true;
                }
                if (Character.isUpperCase(novaSenha.charAt(i))) {
                    contemMaiusculas = true;
                }
                if (Character.isDigit(novaSenha.charAt(i))) {
                    contemNumeros = true;
                }

            }

        } while ((fMinusculas && !contemMinusculas)
                || (fMaiusculas && !contemMaiusculas)
                || (fNumeros && !contemNumeros));

        return novaSenha.toString();
    }

    //
    // Operações com o banco de dados do Matrix
    //
    private Map<String, String> carregarJobTitle() {
        final Criteria crit3 = (emMatrix.unwrap(Session.class)).createCriteria(JobTitle.class);
        List<JobTitle> jobTitleMatrix = crit3.list();
        List<JobTitle> jobTitleSelectedMatrix = new ArrayList<JobTitle>();
        Map<String, String> idsJobTitleCorp = new HashMap<String, String>();
        for (JobTitle j : jobTitleMatrix) {
            // c.setPositionCode(c.getPositionCode());
            j.setPositionDesc(fix(j.getPositionDesc()));
            idsJobTitleCorp.put(j.getPositionDesc(), j.getPositionCode());
            if (j.getPositionDesc().startsWith(
                    cpOrgaoUsuario.getSiglaOrgaoUsu() + ": "))
                jobTitleSelectedMatrix.add(j);
        }
        setAntigo.addAll(jobTitleSelectedMatrix);
        return idsJobTitleCorp;
    }

    private Map<String, String> carregarDepartment() {
        final Criteria crit2 = (emMatrix.unwrap(Session.class)).createCriteria(Department.class);
        List<Department> departMatrix = crit2.list();
        List<Department> departSelectedMatrix = new ArrayList<Department>();
        Map<String, String> idsDepartCorp = new HashMap<String, String>();
        for (Department d : departMatrix) {
            // c.setDepartCode(c.getDepartCode());
            d.setDepartDesc(fix(d.getDepartDesc()));
            idsDepartCorp.put(d.getDepartDesc(), d.getDepartCode());
            if (d.getDepartDesc().startsWith(
                    cpOrgaoUsuario.getSiglaOrgaoUsu() + ": "))
                departSelectedMatrix.add(d);
        }
        setAntigo.addAll(departSelectedMatrix);
        return idsDepartCorp;
    }

    private void carregarCards() {
        final Criteria crit = (emMatrix.unwrap(Session.class)).createCriteria(Cards.class);
        crit.add(Restrictions.like("user2",
                "SIGA-" + cpOrgaoUsuario.getSiglaOrgaoUsu() + "%"));
        crit.add(Restrictions.eq("cardType", CARD_TYPE_USUARIO));
        ajustarCards(crit);
    }

    private List<Item> gravarAlteracoes() throws Exception {

        List<Item> list;
        Sincronizador sinc = new Sincronizador();
        try {
            sinc.setSetNovo(setNovo);
            sinc.setSetAntigo(setAntigo);
            list = sinc.getOperacoes(dt);
        } catch (Exception e) {
            log("Transa��o abortada por erro: " + e.getMessage());
            throw new Exception("Erro na grava��o", e);
        }
        EntityTransaction tr = emMatrix.getTransaction();
        tr.begin();
        try {
            OperadorSemHistorico o = new OperadorSemHistorico() {

                @Override
                public Sincronizavel incluir(Sincronizavel novo) {
                    emMatrix.persist(novo);
                    return (Sincronizavel) novo;
                }

                @Override
                public Sincronizavel excluir(Sincronizavel antigo) {
                    // exclui apenas se j� for NO_ACC
                    if (antigo instanceof Cards) {
                        Cards c = (Cards) antigo;
                        if (!c.getAccessLevel().equals(ACC_LVL_SEM_ACESSO)) {
                            c.setAccessLevel(ACC_LVL_SEM_ACESSO);
                            emMatrix.persist(c);
                            return c;
                        }
                    }

                    emMatrix.remove(antigo);
                    return antigo;
                }

                @Override
                public Sincronizavel alterar(Sincronizavel antigo,
                        Sincronizavel novo) {
                    if (novo instanceof Cards) {
                        ((Cards) novo).setCardNumber(((Cards) antigo).getCardNumber());

                        ((Cards) antigo).copiarPropriedades((Cards) novo);
                    } else if (novo instanceof Department)
                        ((Department) antigo)
                                .copiarPropriedades((Department) novo);
                    // else if (novo instanceof JobTitle)
                    // ((JobTitle) antigo)
                    // .copiarPropriedades((JobTitle) novo);
                    emMatrix.merge(antigo);
                    return antigo;
                }
            };

            for (Item opr : list) {
                String log = opr.getDescricao();

                /*
                 * Para depurar o que será escrito no banco de dados, descomente as linhas
                 * abaixo
                 * 
                 */

                if ((opr.getNovo() instanceof Cards) && (opr.getAntigo() instanceof Cards)
                        && (opr.getOperacao() == Operacao.alterar)) {
                    Cards novo = (Cards) opr.getNovo();
                    Cards antigo = (Cards) opr.getAntigo();
                    log += " sera gravado: ----> AccessLevel: " +
                            " de: " + antigo.getAccessLevel() +
                            " para: " + novo.getAccessLevel() +
                            " Lotação: " +
                            " de: " + antigo.getDepartCode() +
                            " para: " + novo.getDepartCode();
                }

                log(log);
                sinc.gravar(opr, o, true);
                emMatrix.flush();
            }

            if (modoLog) {
                log("Transa��o em modoLog=true n�o confirmada");
                tr.rollback();
            } else {
                tr.commit();
                log("Transa��o confirmada");
            }

        } catch (Exception e) {
            tr.rollback();
            log("Transa��o abortada por erro: " + e.getMessage());
            throw new Exception("Erro na grava��o", e);
        }

        // emMatrix.flush();
        return list;
    }

}
