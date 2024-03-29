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

import org.hibernate.criterion.Restrictions;

import br.gov.jfrj.siga.base.AplicacaoException;
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
    JdbcSimpleOrm orm;

    private static final String ACC_LVL_SEM_ACESSO = "NO ACC";
    private static final String ACC_LVL_SERVIDOR_SJRJ = "SJRJ-SERVI";
    private static final String ACC_LVL_ESTAGIARIO_SJRJ = "SJRJ-ESTAG";
    private static final String ACC_LVL_SERVIDOR_TRF2 = "TRF2-SERVI";
    private static final String ACC_LVL_ESTAGIARIO_TRF2 = "TRF2-ESTAG";

    private static final String CARD_TYPE_USUARIO = "U";
    private static final String CARD_TYPE_TEMPORARIO = "T";

    private static String fix(String s) {
        if (s == null)
            return null;
        return s.trim();
    }

    public String matrix(String sigla, int maxSinc, boolean modoLog) throws Exception {
        try {
            dt = new Date();
            log("--- Processando " + dt + " ---");

            this.maxSinc = maxSinc;
            this.modoLog = modoLog;
            if (modoLog) {
                log("*** MODO LOG: use -modoLog=false para sair do modo LOG e escrever as alterações");
            }
            log("MAX SINC = " + maxSinc);

            long inicio = System.currentTimeMillis();
            log("Importando: XML");
            cpOrgaoUsuario = obterOrgaoUsuario(sigla);
            if (cpOrgaoUsuario == null)
                throw new AplicacaoException(
                        "Sigla de órgão usuário '" + sigla + "' do órgão Usuário não encontrado no banco de dados");
            setOrgaoUsuario(cpOrgaoUsuario.getIdOrgaoUsu());

            try (JdbcSimpleOrm jdbcSimpleOrm = new JdbcSimpleOrm()) {
                orm = jdbcSimpleOrm;

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
            long total = (System.currentTimeMillis() - inicio) / 1000;
            log("Tempo total de execução: " + total + " segundos (" + total / 60 + " min)");
            log(" ---- Fim do Processamento --- ");
        } catch (Exception e) {
            if (orm != null)
                orm.rollback();
            log("Transação abortada por erro: " + e.getMessage(), e);
        }
        return logEnd();
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
                        && !p.getCpTipoPessoa().getDscTpPessoa().equals("Estagiário"))) {
                    continue;
                }

                if (!"1".equals(p.getSituacaoFuncionalPessoa()))
                    continue;
                Cards c = new Cards();
                c.setStaffNumber(String.format("%1$-12s", p.getSigla()));
                c.setCardNumber(String.format("%010d", c.getId()));
                String prefixo_Card = definirPrefixoCard(p);

                // Aqui deve ser substituido por uma pesquisa nas informações que vem do matrix
                // e, se estiver preechido, copiar de lá a informação.
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
                    if (p.getCpTipoPessoa().getDscTpPessoa().equals("Estagiário")) {
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
                String email = p.getEmailPessoaAtual();
                if (email != null && email.length() <= 40)
                    c.setUser4(email);
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

    private void ajustarCards(final List<Cards> cardsMatrix) {
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
                // Exclus�o das letras o e O na geração de senha
                if (minuscula != 'o')
                    caracteres.append(minuscula);
            }
            if (fMaiusculas) {
                final char maiuscula = (char) ('A' + i);
                // Exclus�o das letras o e O na geração de senha
                if (maiuscula != 'O')
                    caracteres.append(maiuscula);
            }

        }

        // exclus�o do n�mero zero na geração de senha
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

    private Map<String, String> carregarJobTitle() throws Exception {
        List<JobTitle> jobTitleMatrix = orm.loadAll(JobTitle.class, null);
        List<JobTitle> jobTitleSelectedMatrix = new ArrayList<>();
        Map<String, String> idsJobTitleCorp = new HashMap<String, String>();
        for (JobTitle j : jobTitleMatrix) {
            j.setPositionDesc(fix(j.getPositionDesc()));
            idsJobTitleCorp.put(j.getPositionDesc(), j.getPositionCode());
            if (j.getPositionDesc().startsWith(
                    cpOrgaoUsuario.getSiglaOrgaoUsu() + ": "))
                jobTitleSelectedMatrix.add(j);
        }
        setAntigo.addAll(jobTitleSelectedMatrix);
        return idsJobTitleCorp;
    }

    private Map<String, String> carregarDepartment() throws Exception {
        List<Department> departMatrix = orm.loadAll(Department.class, null);
        List<Department> departSelectedMatrix = new ArrayList<Department>();
        Map<String, String> idsDepartCorp = new HashMap<String, String>();
        for (Department d : departMatrix) {
            d.setDepartDesc(fix(d.getDepartDesc()));
            idsDepartCorp.put(d.getDepartDesc(), d.getDepartCode());
            if (d.getDepartDesc().startsWith(
                    cpOrgaoUsuario.getSiglaOrgaoUsu() + ": "))
                departSelectedMatrix.add(d);
        }
        setAntigo.addAll(departSelectedMatrix);
        return idsDepartCorp;
    }

    private void carregarCards() throws Exception {
        List<Cards> cardsMatrix = orm.loadAll(Cards.class, " WHERE USER2 like ? and CARD_TYPE = ?",
                "SIGA-" + cpOrgaoUsuario.getSiglaOrgaoUsu() + "%", CARD_TYPE_USUARIO);
        ajustarCards(cardsMatrix);
    }

    private List<Item> gravarAlteracoes() throws Exception {

        List<Item> list;
        Sincronizador sinc = new Sincronizador();
        try {
            sinc.setSetNovo(setNovo);
            sinc.setSetAntigo(setAntigo);
            list = sinc.getOperacoes(dt);
        } catch (Exception e) {
            log("Comparação abortada por erro: " + e.getMessage());
            throw new Exception("Erro na gravação", e);
        }
        try {
            OperadorSemHistorico o = new OperadorSemHistorico() {

                @Override
                public Sincronizavel incluir(Sincronizavel novo) {
                    try {
                        orm.insert(novo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return (Sincronizavel) novo;
                }

                @Override
                public Sincronizavel excluir(Sincronizavel antigo) {
                    // exclui apenas se já for NO_ACC
                    if (antigo instanceof Cards) {
                        Cards c = (Cards) antigo;
                        if (!c.getAccessLevel().equals(ACC_LVL_SEM_ACESSO)) {
                            c.setAccessLevel(ACC_LVL_SEM_ACESSO);
                            try {
                                orm.update(c);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            return c;
                        }
                    }

                    try {
                        orm.remove(antigo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return antigo;
                }

                @Override
                public Sincronizavel alterar(Sincronizavel antigo,
                        Sincronizavel novo) {
                    if (novo instanceof Cards) {
                        // Desabilitei isso porque se o objetivo é manter o cardNumber, basta
                        // não copiá-lo no copiarPropriedades() e desprezá-lo no semelhante().
                        //
                        // ((Cards) novo).setCardNumber(((Cards) antigo).getCardNumber());

                        ((Cards) antigo).copiarPropriedades((Cards) novo);
                    } else if (novo instanceof Department)
                        ((Department) antigo)
                                .copiarPropriedades((Department) novo);
                    // else if (novo instanceof JobTitle)
                    // ((JobTitle) antigo)
                    // .copiarPropriedades((JobTitle) novo);
                    try {
                        orm.update(antigo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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
                            " para: " + novo.getDepartCode() +
                            " Cargo: " +
                            " de: " + antigo.getPositionCode() +
                            " para: " + novo.getPositionCode();
                }

                log(log);
                sinc.gravar(opr, o, true);
            }

            if (modoLog) {
                log("Transação em modoLog=true não confirmada");
                orm.rollback();
            } else {
                orm.commit();
                log("Transação confirmada");
            }
            orm = null;
        } catch (Exception e) {
            if (orm != null)
                orm.rollback();
            log("Gravação abortada por erro: " + e.getMessage());
            throw new Exception("Erro na gravação", e);
        }
        return list;
    }

}
