package br.gov.jfrj.siga.ex.util;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.Prop.IPropertyProvider;
import junit.framework.TestCase;

public class MascaraClassificacaoTest extends TestCase {

    private static final String MASK_IN_1 = "([0-9]{0,2})\\.?([0-9]{0,2})?\\.?([0-9]{0,2})?\\.?([0-9]{0,2})?-?([A-Z])?";
    private static final String MASK_OUT_1 = "%1$02d.%2$02d.%3$02d.%4$02d";
    private static final String MASK_OUT_1_FORMATADA = "(r=\"\"; m=0; i=0; for (g:grupos) {i++; if (g!=0) m=i;}; r = r + grupos[0] + grupos[1] + grupos[2]; if (m>3) r = r + \".\" + grupos[3]; if (m>4) r = r + grupos[4]; if (m>5) r = r + grupos[5]; return r;)";

    private static final String MASK_IN_2 = "([0-9]{0,2})\\.?([0-9]{0,3})?\\.?([0-9]{0,2})?";
    private static final String MASK_OUT_2 = "%1$02d.%2$03d.%3$02d";

    private static final String MASK_IN_3 = "([0-9]{0,1})\\-?([0-9]{0,2})?\\-?([0-9]{0,3})?";
    private static final String MASK_OUT_3 = "%1$01d-%2$02d-%3$03d";

    private static final String MASK_IN_4 = "([0-9]{0,1})\\-?([0-9]{0,5})?";
    private static final String MASK_OUT_4 = "%1$01d-%2$05d";

    private static final String MASK_IN_5 = "([0-9]?)([0-9]?)([0-9]?)\\.?([0-9])?([0-9])?([0-9])?";
    private static final String MASK_OUT_5 = "%1$01d%2$01d%3$01d.%4$01d%5$01d%6$01d";

    private static final String MASK_IN_6 = "([0-9]?)([0-9]?)([0-9]?)\\.?([0-9])?([0-9])?([0-9])?";
    private static final String MASK_OUT_6 = "(r=\"\"; m=0; i=0; for (g:grupos) {i++; if (g!=0) m=i;}; r = r + grupos[0] + grupos[1] + grupos[2]; if (m>3) r = r + \".\" + grupos[3]; if (m>4) r = r + grupos[4]; if (m>5) r = r + grupos[5]; return r;)";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Prop.setProvider(new IPropertyProvider() {

            @Override
            public String getProp(String nome) {
                return null;
            }

            @Override
            public void addRestrictedProperty(String name, String defaultValue) {
            }

            @Override
            public void addRestrictedProperty(String name) {
            }

            @Override
            public void addPublicProperty(String name, String defaultValue) {
            }

            @Override
            public void addPublicProperty(String name) {
            }

            @Override
            public void addPrivateProperty(String name, String defaultValue) {
            }

            @Override
            public void addPrivateProperty(String name) {
            }
        });
    }

    public void testFormatar() {
        MascaraUtil m = MascaraUtil.getInstance();

        /* TESTE DE FORMATAÇÃO */
        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);
        assertNull(m.formatar(null));
        assertEquals("11.22.33.44", m.formatar("11223344A"));
        assertEquals("11.22.33.44", m.formatar("11223344"));
        assertEquals("11.22.33.44", m.formatar("11.223344"));
        assertEquals("11.22.33.44", m.formatar("11.22.33.44"));
        assertEquals("11.00.00.00", m.formatar("11"));
        assertEquals("11.22.00.00", m.formatar("1122"));
        assertEquals("11.22.33.00", m.formatar("112233"));
        assertEquals("11.22.03.00", m.formatar("11223"));
        assertEquals("01.02.03.04", m.formatar("1.2.3.4"));
        assertEquals("01.02.03.00", m.formatar("1.2.3"));
        assertEquals("01.20.00.00", m.formatar("1.20"));
        assertEquals("01.21.22.01", m.formatar("1.21.22.1"));
        assertEquals("01.21.22.01", m.formatar("1.21.22.1-A"));
        assertEquals("00.00.00.00", m.formatar("0"));
        assertEquals("01.00.00.00", m.formatar("1"));
        assertEquals("00.00.00.00", m.formatar(""));

        m.setMascaraEntrada(MASK_IN_2);
        m.setMascaraSaida(MASK_OUT_2);
        assertEquals("11.222.33", m.formatar("1122233"));
        assertEquals("11.000.00", m.formatar("11"));
        assertEquals("11.222.00", m.formatar("11222"));
        assertEquals("00.000.00", m.formatar("0"));
        assertEquals("00.000.00", m.formatar(""));

        m.setMascaraEntrada(MASK_IN_3);
        m.setMascaraSaida(MASK_OUT_3);
        assertEquals("1-22-333", m.formatar("122333"));
        assertEquals("1-00-000", m.formatar("1"));
        assertEquals("1-22-000", m.formatar("122"));
        assertEquals("0-00-000", m.formatar("0"));
        assertEquals("0-00-000", m.formatar(""));

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);
        assertEquals("000.000", m.formatar("000"));
        assertEquals("001.000", m.formatar("001"));
        assertEquals("011.000", m.formatar("011"));
        assertEquals("111.000", m.formatar("111"));
        assertEquals("123.400", m.formatar("1234"));
        assertEquals("123.450", m.formatar("12345"));
        assertEquals("123.456", m.formatar("123456"));
        assertEquals("000.000", m.formatar("0"));
        assertEquals("000.000", m.formatar(""));

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);
        assertEquals("000", m.formatar("0"));
        assertEquals("000", m.formatar("00"));
        assertEquals("000", m.formatar("000"));
        assertEquals("001", m.formatar("001"));
        assertEquals("011", m.formatar("011"));
        assertEquals("111", m.formatar("111"));
        assertEquals("123.4", m.formatar("1234"));
        assertEquals("123.45", m.formatar("12345"));
        assertEquals("123.456", m.formatar("123456"));
        assertEquals("000", m.formatar(""));
    }

    public void testCampoDaMascara() {
        MascaraUtil m = MascaraUtil.getInstance();

        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);

        assertNull(m.getCampoDaMascara(0, "11.22.33.44"));
        assertEquals("11", m.getCampoDaMascara(1, "11.22.33.44"));
        assertEquals("22", m.getCampoDaMascara(2, "11.22.33.44"));
        assertEquals("33", m.getCampoDaMascara(3, "11.22.33.44"));
        assertEquals("44", m.getCampoDaMascara(4, "11.22.33.44"));
        assertNull(m.getCampoDaMascara(5, "11.22.33.44"));

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);

        assertNull(m.getCampoDaMascara(0, "123.456"));
        assertEquals("1", m.getCampoDaMascara(1, "123.456"));
        assertEquals("2", m.getCampoDaMascara(2, "123.456"));
        assertEquals("3", m.getCampoDaMascara(3, "123.456"));
        assertEquals("4", m.getCampoDaMascara(4, "123.456"));
        assertEquals("5", m.getCampoDaMascara(5, "123.456"));
        assertEquals("6", m.getCampoDaMascara(6, "123.456"));
        assertNull(m.getCampoDaMascara(7, "123.456"));

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);

        assertNull(m.getCampoDaMascara(0, "123.456"));
        assertEquals("1", m.getCampoDaMascara(1, "123.456"));
        assertEquals("2", m.getCampoDaMascara(2, "123.456"));
        assertEquals("3", m.getCampoDaMascara(3, "123.456"));
        assertEquals("4", m.getCampoDaMascara(4, "123.456"));
        assertEquals("5", m.getCampoDaMascara(5, "123.456"));
        assertEquals("6", m.getCampoDaMascara(6, "123.456"));
        assertNull(m.getCampoDaMascara(7, "123.456"));
    }

    public void testCalcularNivel() {
        MascaraUtil m = MascaraUtil.getInstance();

        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);
        assertEquals(-1, m.calcularNivel(null));
        assertEquals(-1, m.calcularNivel(""));
        assertEquals(1, m.calcularNivel("00.00.00.00"));
        assertEquals(1, m.calcularNivel("11.00.00.00"));
        assertEquals(2, m.calcularNivel("11.22.00.00"));
        assertEquals(3, m.calcularNivel("11.22.33.00"));
        assertEquals(4, m.calcularNivel("11.22.33.44"));

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);
        assertEquals(-1, m.calcularNivel(null));
        assertEquals(-1, m.calcularNivel(""));
        assertEquals(1, m.calcularNivel("000.000"));
        assertEquals(1, m.calcularNivel("100.000"));
        assertEquals(2, m.calcularNivel("110.000"));
        assertEquals(2, m.calcularNivel("010.000"));
        assertEquals(3, m.calcularNivel("111.000"));
        assertEquals(3, m.calcularNivel("011.000"));
        assertEquals(3, m.calcularNivel("001.000"));
        assertEquals(4, m.calcularNivel("123.400"));
        assertEquals(5, m.calcularNivel("123.450"));
        assertEquals(6, m.calcularNivel("123.456"));

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);
        assertEquals(-1, m.calcularNivel(null));
        assertEquals(-1, m.calcularNivel(""));
        assertEquals(1, m.calcularNivel("000"));
        assertEquals(1, m.calcularNivel("100"));
        assertEquals(2, m.calcularNivel("110"));
        assertEquals(2, m.calcularNivel("010"));
        assertEquals(3, m.calcularNivel("111"));
        assertEquals(3, m.calcularNivel("011"));
        assertEquals(3, m.calcularNivel("001"));
        assertEquals(4, m.calcularNivel("123.4"));
        assertEquals(5, m.calcularNivel("123.45"));
        assertEquals(6, m.calcularNivel("123.456"));
    }

    public void testTotalNiveisMascara() {
        MascaraUtil m = MascaraUtil.getInstance();

        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);

        assertEquals(4, m.getTotalDeNiveisDaMascara());

        m.setMascaraEntrada(MASK_IN_2);
        m.setMascaraSaida(MASK_OUT_2);

        assertEquals(3, m.getTotalDeNiveisDaMascara());

        m.setMascaraEntrada(MASK_IN_3);
        m.setMascaraSaida(MASK_OUT_3);

        assertEquals(3, m.getTotalDeNiveisDaMascara());

        m.setMascaraEntrada(MASK_IN_4);
        m.setMascaraSaida(MASK_OUT_4);

        assertEquals(2, m.getTotalDeNiveisDaMascara());

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);

        assertEquals(6, m.getTotalDeNiveisDaMascara());

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);

        assertEquals(6, m.getTotalDeNiveisDaMascara());

    }

    public void testBuscaDeFilhos() {
        MascaraUtil m = MascaraUtil.getInstance();
        /* TESTE MÁSCARA DE BUSCA DE FILHOS */
        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);
        assertNull(m.getMscFilho(null, 0, false));
        assertNull(m.getMscFilho(null, 1, false));
        assertEquals("11.__.00.00", m.getMscFilho("11", 2, false));
        assertEquals("11.__.00.00", m.getMscFilho("11.00.00.00", 2, false));
        assertEquals("11.22.__.00", m.getMscFilho("11.22", 3, false));
        assertEquals("11.22.33.__", m.getMscFilho("11.22.33", 4, false));
        assertEquals("11.22.33.44", m.getMscFilho("11.22.33.44", 5, false));

        m.setMascaraEntrada(MASK_IN_2);
        m.setMascaraSaida(MASK_OUT_2);
        assertEquals("11.___.00", m.getMscFilho("11", 2, false));
        assertEquals("11.___.00", m.getMscFilho("11.000.00", 2, false));
        assertEquals("11.222.__", m.getMscFilho("11.222", 3, false));

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);
        assertEquals("1_0.000", m.getMscFilho("100.000", 2, false));
        assertEquals("10_.000", m.getMscFilho("100.000", 3, false));
        assertEquals("100._00", m.getMscFilho("100.000", 4, false));
        assertEquals("100.0_0", m.getMscFilho("100.000", 5, false));
        assertEquals("100.00_", m.getMscFilho("100.000", 6, false));

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);
        assertEquals("1_0", m.getMscFilho("1", false));
        assertEquals("1_0", m.getMscFilho("100.000", false));
        assertEquals("12_", m.getMscFilho("120.000", false));
        assertEquals("123._", m.getMscFilho("123.000", false));
        assertEquals("123.4_", m.getMscFilho("123.400", false));
        assertEquals("123.45_", m.getMscFilho("123.450", false));
        assertEquals("123.456", m.getMscFilho("123.456", false));
        assertEquals("0_0", m.getMscFilho("000.000", false));

        // TESTE MÁSCARA DE BUSCA DE FILHOS E DESCENDENTES
        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);
        assertEquals("11.__.__.__", m.getMscFilho("11", 2, true));
        assertEquals("11.__.__.__", m.getMscFilho("11.00.00.00", 2, true));
        assertEquals("11.22.__.__", m.getMscFilho("11.22", 3, true));
        assertEquals("11.22.33.__", m.getMscFilho("11.22.33", 4, true));
        assertEquals("11.22.33.44", m.getMscFilho("11.22.33.44", 5, true));

        assertEquals("11.__.__.__", m.getMscFilho("11", 2, true));
        assertEquals("11.22.__.__", m.getMscFilho("11.22", 3, true));
        assertEquals("11.22.33.__", m.getMscFilho("11.22.33", 4, true));
        assertEquals("11.22.33.44", m.getMscFilho("11.22.33.44", 5, true));

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);
        assertEquals("1__.___", m.getMscFilho("1", 2, true));
        assertEquals("12_.___", m.getMscFilho("12", 3, true));
        assertEquals("123.___", m.getMscFilho("123", 4, true));
        assertEquals("123.4__", m.getMscFilho("1234", 5, true));
        assertEquals("123.45_", m.getMscFilho("12345", 6, true));
        assertEquals("123.456", m.getMscFilho("123456", 7, true));

        // TESTE MÁSCARA DE BUSCA DE FILHOS E DESCENDENTES COM DEDUÇÃO DE NÍVEL INCIAL
        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);
        assertEquals("11.22.33.44", m.getMscFilho("11223344A", true));
        assertEquals("11.__.__.__", m.getMscFilho("11", true));
        assertEquals("11.__.__.__", m.getMscFilho("11.00.00.00", true));
        assertEquals("11.22.__.__", m.getMscFilho("11.22", true));
        assertEquals("11.22.33.__", m.getMscFilho("11.22.33", true));
        assertEquals("11.22.33.44", m.getMscFilho("11.22.33.44", true));
        assertEquals("00.__.__.__", m.getMscFilho("00.00.00.00", true));
        assertEquals("00.__.__.__", m.getMscFilho("0", true));

        m.setMascaraEntrada(MASK_IN_2);
        m.setMascaraSaida(MASK_OUT_2);
        assertEquals("11.___.__", m.getMscFilho("11", true));
        assertEquals("11.___.__", m.getMscFilho("11.000.00", true));
        assertEquals("11.222.__", m.getMscFilho("11.222", true));
        assertEquals("11.222.33", m.getMscFilho("11.222.33", true));
        assertEquals("00.___.__", m.getMscFilho("00.000.00", true));

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);
        assertEquals("1__.___", m.getMscFilho("1", true));
        assertEquals("1__.___", m.getMscFilho("100.000", true));
        assertEquals("12_.___", m.getMscFilho("120.000", true));
        assertEquals("123.___", m.getMscFilho("123.000", true));
        assertEquals("123.4__", m.getMscFilho("123.400", true));
        assertEquals("123.45_", m.getMscFilho("123.450", true));
        assertEquals("123.456", m.getMscFilho("123.456", true));
        assertEquals("0__.___", m.getMscFilho("000.000", true));

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);
        assertEquals("1%", m.getMscFilho("1", true));
        assertEquals("1%", m.getMscFilho("100.000", true));
        assertEquals("12%", m.getMscFilho("120.000", true));
        assertEquals("123.%", m.getMscFilho("123.000", true));
        assertEquals("123.4%", m.getMscFilho("123.400", true));
        assertEquals("123.45%", m.getMscFilho("123.450", true));
        assertEquals("123.456", m.getMscFilho("123.456", true));
        assertEquals("0%", m.getMscFilho("000.000", true));
    }

    public void testBuscaDePais() {
        MascaraUtil m = MascaraUtil.getInstance();
        /* TESTE BUSCA DE PAIS */
        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);
        assertNull(m.getPais(null));

        String[] result = m.getPais("11.22.33.44");
        assertEquals(result.length, 3);
        assertEquals("11.00.00.00", result[0]);
        assertEquals("11.22.00.00", result[1]);
        assertEquals("11.22.33.00", result[2]);

        result = m.getPais("11.22.33.00");
        assertEquals(result.length, 2);
        assertEquals("11.00.00.00", result[0]);
        assertEquals("11.22.00.00", result[1]);

        result = m.getPais("11.22.33");
        assertEquals(result.length, 2);
        assertEquals("11.00.00.00", result[0]);
        assertEquals("11.22.00.00", result[1]);

        result = m.getPais("11.22.00.00");
        assertEquals(result.length, 1);
        assertEquals("11.00.00.00", result[0]);

        result = m.getPais("11.22");
        assertEquals(result.length, 1);
        assertEquals("11.00.00.00", result[0]);

        result = m.getPais("11.00.00.00");
        assertNull(result);

        result = m.getPais("11");
        assertNull(result);

        m.setMascaraEntrada(MASK_IN_2);
        m.setMascaraSaida(MASK_OUT_2);
        result = m.getPais("11.222.33");
        assertEquals(result.length, 2);
        assertEquals("11.000.00", result[0]);
        assertEquals("11.222.00", result[1]);

        result = m.getPais("11.222.00");
        assertEquals(result.length, 1);
        assertEquals("11.000.00", result[0]);

        result = m.getPais("11.000.00");
        assertNull(result);

        result = m.getPais("11.222");
        assertEquals(result.length, 1);
        assertEquals("11.000.00", result[0]);

        result = m.getPais("11");
        assertNull(result);

        m.setMascaraEntrada(MASK_IN_3);
        m.setMascaraSaida(MASK_OUT_3);
        result = m.getPais("1-22-333");
        assertEquals(result.length, 2);
        assertEquals("1-00-000", result[0]);
        assertEquals("1-22-000", result[1]);

        result = m.getPais("1-22-000");
        assertEquals(result.length, 1);
        assertEquals("1-00-000", result[0]);

        result = m.getPais("1-00-000");
        assertNull(result);

        result = m.getPais("1-22");
        assertEquals(result.length, 1);
        assertEquals("1-00-000", result[0]);

        result = m.getPais("1");
        assertNull(result);

        m.setMascaraEntrada(MASK_IN_4);
        m.setMascaraSaida(MASK_OUT_4);
        result = m.getPais("1-22222");
        assertEquals(result.length, 1);
        assertEquals("1-00000", result[0]);

        result = m.getPais("1");
        assertNull(result);

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);
        assertNull(m.getPais(null));

        result = m.getPais("123.456");
        assertEquals(result.length, 5);
        assertEquals("100.000", result[0]);
        assertEquals("120.000", result[1]);
        assertEquals("123.000", result[2]);
        assertEquals("123.400", result[3]);
        assertEquals("123.450", result[4]);

        result = m.getPais("123.400");
        assertEquals(result.length, 3);
        assertEquals("100.000", result[0]);
        assertEquals("120.000", result[1]);
        assertEquals("123.000", result[2]);

        result = m.getPais("120.000");
        assertEquals(result.length, 1);
        assertEquals("100.000", result[0]);

        result = m.getPais("100.000");
        assertNull(result);

        result = m.getPais("000.000");
        assertNull(result);

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);
        assertNull(m.getPais(null));

        result = m.getPais("123.456");
        assertEquals(result.length, 5);
        assertEquals("100", result[0]);
        assertEquals("120", result[1]);
        assertEquals("123", result[2]);
        assertEquals("123.4", result[3]);
        assertEquals("123.45", result[4]);

        result = m.getPais("123.4");
        assertEquals(result.length, 3);
        assertEquals("100", result[0]);
        assertEquals("120", result[1]);
        assertEquals("123", result[2]);

        result = m.getPais("120");
        assertEquals(result.length, 1);
        assertEquals("100", result[0]);

        result = m.getPais("100");
        assertNull(result);

        result = m.getPais("000");
        assertNull(result);

    }

    public void testBuscaPorNivel() {
        MascaraUtil m = MascaraUtil.getInstance();
        /* TESTE MÁSCARA DE BUSCA TODOS DE UM DETERMINADO NÍVEL */
        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);
        assertEquals("__.__.__.__", m.getMscTodosDoNivel(0));
        assertEquals("__.00.00.00", m.getMscTodosDoNivel(1));
        assertEquals("__.__.00.00", m.getMscTodosDoNivel(2));
        assertEquals("__.__.__.00", m.getMscTodosDoNivel(3));
        assertEquals("__.__.__.__", m.getMscTodosDoNivel(4));
        assertEquals("__.__.__.__", m.getMscTodosDoNivel(-1));
        assertEquals("__.__.__.__", m.getMscTodosDoNivel(99999));
        assertEquals("__.__.__.__", m.getMscTodosDoMaiorNivel());

        m.setMascaraEntrada(MASK_IN_2);
        m.setMascaraSaida(MASK_OUT_2);
        assertEquals("__.___.__", m.getMscTodosDoNivel(0));
        assertEquals("__.000.00", m.getMscTodosDoNivel(1));
        assertEquals("__.___.00", m.getMscTodosDoNivel(2));
        assertEquals("__.___.__", m.getMscTodosDoNivel(3));
        assertEquals("__.___.__", m.getMscTodosDoNivel(-1));
        assertEquals("__.___.__", m.getMscTodosDoNivel(99999));
        assertEquals("__.___.__", m.getMscTodosDoMaiorNivel());

        m.setMascaraEntrada(MASK_IN_3);
        m.setMascaraSaida(MASK_OUT_3);
        assertEquals("_-__-___", m.getMscTodosDoNivel(0));
        assertEquals("_-00-000", m.getMscTodosDoNivel(1));
        assertEquals("_-__-000", m.getMscTodosDoNivel(2));
        assertEquals("_-__-___", m.getMscTodosDoNivel(3));
        assertEquals("_-__-___", m.getMscTodosDoNivel(-1));
        assertEquals("_-__-___", m.getMscTodosDoNivel(99999));
        assertEquals("_-__-___", m.getMscTodosDoMaiorNivel());

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);
        assertEquals("___.___", m.getMscTodosDoNivel(0));
        assertEquals("_00.000", m.getMscTodosDoNivel(1));
        assertEquals("__0.000", m.getMscTodosDoNivel(2));
        assertEquals("___.000", m.getMscTodosDoNivel(3));
        assertEquals("___._00", m.getMscTodosDoNivel(4));
        assertEquals("___.__0", m.getMscTodosDoNivel(5));
        assertEquals("___.___", m.getMscTodosDoNivel(6));
        assertEquals("___.___", m.getMscTodosDoNivel(-1));
        assertEquals("___.___", m.getMscTodosDoNivel(99999));
        assertEquals("___.___", m.getMscTodosDoMaiorNivel());

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);
        assertEquals("%", m.getMscTodosDoNivel(0));
        assertEquals("_00", m.getMscTodosDoNivel(1));
        assertEquals("__0", m.getMscTodosDoNivel(2));
        assertEquals("___", m.getMscTodosDoNivel(3));
        assertEquals("___._", m.getMscTodosDoNivel(4));
        assertEquals("___.__", m.getMscTodosDoNivel(5));
        assertEquals("___.___", m.getMscTodosDoNivel(6));
        assertEquals("%", m.getMscTodosDoNivel(-1));
        assertEquals("%", m.getMscTodosDoNivel(99999));
        assertEquals("%", m.getMscTodosDoMaiorNivel());

    }

    public void testSubstituicao() {
        MascaraUtil m = MascaraUtil.getInstance();

        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);

        assertEquals("01.02.03.04", m.substituir("01.02.03.04", "__.__.__.__"));
        assertEquals("05.02.03.04", m.substituir("01.02.03.04", "05.__.__.__"));
        assertEquals("05.06.03.04", m.substituir("01.02.03.04", "05.06.__.__"));
        assertEquals("05.06.07.04", m.substituir("01.02.03.04", "05.06.07.__"));
        assertEquals("05.06.07.08", m.substituir("01.02.03.04", "05.06.07.08"));

        m.setMascaraEntrada(MASK_IN_2);
        m.setMascaraSaida(MASK_OUT_2);
        assertEquals("01.002.03", m.substituir("01.002.03", "__.___.__"));
        assertEquals("05.002.03", m.substituir("01.002.03", "05.___.__"));
        assertEquals("05.006.03", m.substituir("01.002.03", "05.006.__"));
        assertEquals("05.006.07", m.substituir("01.002.03", "05.006.07"));

        m.setMascaraEntrada(MASK_IN_3);
        m.setMascaraSaida(MASK_OUT_3);
        assertEquals("1-22-333", m.substituir("1-22-333", "_-__-___"));
        assertEquals("5-22-333", m.substituir("1-22-333", "5-__-___"));
        assertEquals("5-66-333", m.substituir("1-22-333", "5-66-___"));
        assertEquals("5-66-777", m.substituir("1-22-333", "5-66-777"));

        m.setMascaraEntrada(MASK_IN_4);
        m.setMascaraSaida(MASK_OUT_4);
        assertEquals("1-22222", m.substituir("1-22222", "_-_____"));
        assertEquals("5-22222", m.substituir("1-22222", "5-_____"));
        assertEquals("5-66666", m.substituir("1-22222", "5-66666"));

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);

        assertEquals("123.456", m.substituir("123.456", "___.___"));
        assertEquals("723.456", m.substituir("123.456", "7__.___"));
        assertEquals("783.456", m.substituir("123.456", "78_.___"));
        assertEquals("789.456", m.substituir("123.456", "789.___"));
        assertEquals("789.156", m.substituir("123.456", "789.1__"));
        assertEquals("789.126", m.substituir("123.456", "789.12_"));
        assertEquals("789.123", m.substituir("123.456", "789.123"));

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);

        assertEquals("123.456", m.substituir("123.456", "___.___"));
        assertEquals("723.456", m.substituir("123.456", "7__.___"));
        assertEquals("783.456", m.substituir("123.456", "78_.___"));
        assertEquals("789.456", m.substituir("123.456", "789.___"));
        assertEquals("789.156", m.substituir("123.456", "789.1__"));
        assertEquals("789.126", m.substituir("123.456", "789.12_"));
        assertEquals("789.123", m.substituir("123.456", "789.123"));

        // máscaras e valores inválidos
        assertNull(m.substituir("01.02.03.04", "__,__,__,__"));
        assertNull(m.substituir("01.02.03.04", "__.__.__"));
        assertNull(m.substituir("01.02.03.04", "abcde012345"));
        assertNull(m.substituir("01.02.03.04", ""));
        assertNull(m.substituir("01.02.03.04", null));
        assertNull(m.substituir("", "__.__.__.__"));
        assertNull(m.substituir(null, "__.__.__.__"));
        assertNull(m.substituir("1", "__.__.__.__"));
    }

    public void testIsUltimoNivel() {
        MascaraUtil m = MascaraUtil.getInstance();

        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);

        assertTrue(m.isUltimoNivel("00.00.00.01"));
        assertTrue(m.isUltimoNivel("01.02.03.04"));
        assertTrue(m.isUltimoNivel("1.2.3.4"));

        assertFalse(m.isUltimoNivel("00.00.00.00"));
        assertFalse(m.isUltimoNivel("01.02.03.00"));
        assertFalse(m.isUltimoNivel("01.00.03.00"));
        assertFalse(m.isUltimoNivel("1.2"));

        m.setMascaraEntrada(MASK_IN_2);
        m.setMascaraSaida(MASK_OUT_2);

        assertTrue(m.isUltimoNivel("00.000.01"));
        assertTrue(m.isUltimoNivel("01.002.03"));
        assertTrue(m.isUltimoNivel("01.000.03"));
        assertTrue(m.isUltimoNivel("1.2.3"));

        assertFalse(m.isUltimoNivel("00.000.00"));
        assertFalse(m.isUltimoNivel("01.002.00"));
        assertFalse(m.isUltimoNivel("1.2"));

        m.setMascaraEntrada(MASK_IN_3);
        m.setMascaraSaida(MASK_OUT_3);

        assertTrue(m.isUltimoNivel("0-00-001"));
        assertTrue(m.isUltimoNivel("1-02-003"));
        assertTrue(m.isUltimoNivel("1-00-003"));
        assertTrue(m.isUltimoNivel("1-2-3"));

        assertFalse(m.isUltimoNivel("0-00-000"));
        assertFalse(m.isUltimoNivel("1-02-000"));
        assertFalse(m.isUltimoNivel("1-2"));

        m.setMascaraEntrada(MASK_IN_4);
        m.setMascaraSaida(MASK_OUT_4);

        assertTrue(m.isUltimoNivel("0-00001"));
        assertTrue(m.isUltimoNivel("1-00002"));
        assertTrue(m.isUltimoNivel("1-2"));

        assertFalse(m.isUltimoNivel("0-00000"));
        assertFalse(m.isUltimoNivel("1-00000"));
        assertFalse(m.isUltimoNivel("1"));

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);

        assertTrue(m.isUltimoNivel("000.001"));
        assertTrue(m.isUltimoNivel("123.456"));
        assertTrue(m.isUltimoNivel("123456"));

        assertFalse(m.isUltimoNivel("000.000"));
        assertFalse(m.isUltimoNivel("123.450"));
        assertFalse(m.isUltimoNivel("100.050"));
        assertFalse(m.isUltimoNivel("12"));

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);

        assertTrue(m.isUltimoNivel("000.001"));
        assertTrue(m.isUltimoNivel("123.456"));
        assertTrue(m.isUltimoNivel("123456"));

        assertFalse(m.isUltimoNivel("000.000"));
        assertFalse(m.isUltimoNivel("000"));
        assertFalse(m.isUltimoNivel("123.450"));
        assertFalse(m.isUltimoNivel("123.45"));
        assertFalse(m.isUltimoNivel("100.050"));
        assertFalse(m.isUltimoNivel("100.05"));
        assertFalse(m.isUltimoNivel("12"));
    }

    public void testMaiorCodigoPossivel() {
        MascaraUtil m = MascaraUtil.getInstance();
        m.setMascaraEntrada(MASK_IN_1);
        m.setMascaraSaida(MASK_OUT_1);
        assertEquals("99.99.99.99", m.getMaiorCodigoPossivel());

        m.setMascaraEntrada(MASK_IN_2);
        m.setMascaraSaida(MASK_OUT_2);
        assertEquals("99.999.99", m.getMaiorCodigoPossivel());

        m.setMascaraEntrada(MASK_IN_5);
        m.setMascaraSaida(MASK_OUT_5);
        assertEquals("999.999", m.getMaiorCodigoPossivel());

        m.setMascaraEntrada(MASK_IN_6);
        m.setMascaraSaida(MASK_OUT_6);
        assertEquals("999.999", m.getMaiorCodigoPossivel());
    }


}
