package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import io.restassured.response.ValidatableResponse;

public class IncluirDocumento extends DocTest {

    public static String incluirDocumentoTemporario(Pessoa pessoa, String siglaPai) {
        return incluirDocumentoTemporario(pessoa, siglaPai, pessoa);
    }

    public static String incluirDocumentoTemporario(Pessoa pessoa, String siglaPai, Pessoa pessoaSubscritor) {
        ValidatableResponse resp = givenFor(pessoa)

                .param("siglamobilpai", siglaPai)
                .param("modelo", "Despacho")
                .param("subscritor", pessoaSubscritor.name())
                .param("texto_padrao", "Autorizo.")
                .param("classificacao", "00.01.01.01")

                .when().post("/sigaex/api/v1/documentos").then();

        assertStatusCode200(resp);

        return resp.body("sigladoc", notNullValue())

                .extract()
                .path("sigladoc");
    }

    public static String incluirDespacho(Pessoa pessoa, String siglaPai) {
        String siglaTmp = incluirDocumentoTemporario(pessoa, siglaPai);
        String sigla = AssinarComSenha.assinarComSenha(pessoa, siglaTmp);
        sigla += "A";
        return sigla;
    }

    public static String incluirDespachoAssinadoPorOutraPessoa(Pessoa pessoa, String siglaPai,
            Pessoa pessoaSubscritor) {
        String siglaTmp = incluirDocumentoTemporario(pessoa, siglaPai, pessoaSubscritor);
        String sigla = AssinarComSenha.assinarComSenha(pessoaSubscritor, siglaTmp);
        sigla += "A";
        return sigla;
    }

    public static String compactarSigla(String sigla) {
        if (sigla == null)
            return null;
        return sigla.replace("-", "").replace("/", "");
    }

    @Test
    public void test_IncluirDespachoTemporario_OK() {
        String siglaPai = Criar.criarMemorando(Pessoa.ZZ99999);
        String sigla = incluirDocumentoTemporario(Pessoa.ZZ99999, siglaPai);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.EM_ELABORACAO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemMarca(CpMarcadorEnum.PENDENTE_DE_ASSINATURA, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemMarca(CpMarcadorEnum.COMO_SUBSCRITOR, Pessoa.ZZ99999);
        contemAcao("assinar", true);
        contemAcao("transferir", false);
    }

    @Test
    public void test_IncluirDespacho_OK() {
        String siglaPai = Criar.criarMemorando(Pessoa.ZZ99999);
        String sigla = incluirDespacho(Pessoa.ZZ99999, siglaPai);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.JUNTADO);
        contemAcao("assinar", false);
        contemAcao("cancelar_juntada", true);
    }

    @Test
    public void test_IncluirDespachoAssinadoPorOutraPessoa_OK() {
        String siglaPai = Criar.criarMemorando(Pessoa.ZZ99999);
        String sigla = incluirDespachoAssinadoPorOutraPessoa(Pessoa.ZZ99999, siglaPai, Pessoa.ZZ99998);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.JUNTADO);
        contemAcao("assinar", false);
        contemAcao("cancelar_juntada", true);
        contemMovimentacao(ExTipoDeMovimentacao.JUNTADA, Pessoa.ZZ99998, Lotacao.ZZLTEST2);
    }

}
