package br.gov.jfrj.siga.cp.arquivo;

import java.util.Calendar;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.TipoConteudo;

public interface Armazenamento {
    public static final int MAX_LEN_NOME_SUGERIDO = 30;

    public void salvar(String caminho, String tipoDeConteudo, byte[] conteudo);

    public void apagar(String caminho);

    public byte[] recuperar(String caminho);

    default String separador() {
        return "-";
    }

    default String barra() {
        return "/";
    }

    default String ponto() {
        return ".";
    }

    default String gerarCaminho(String nome, TipoConteudo t, ArmazenamentoTemporalidadeEnum tempo) {
        String extensao;

        if (t != null)
            extensao = t.getExtensao();
        else
            extensao = TipoConteudo.ZIP.getExtensao();

        Calendar c = Calendar.getInstance();
        String diretorio = c.get(Calendar.YEAR) + barra() + (c.get(Calendar.MONTH) + 1) + barra() + c.get(Calendar.DATE)
                + barra()
                + c.get(Calendar.HOUR_OF_DAY) + barra() + c.get(Calendar.MINUTE) + barra();

        String nomeDeArquivo = ((nome != null && !nome.trim().isEmpty())
                ? Texto.slugify(StringUtils.left(nome.trim(), MAX_LEN_NOME_SUGERIDO), true, false) + separador()
                : "")
                + UUID.randomUUID().toString()
                + (tempo.getIdentificador() != null ? separador() + tempo.getIdentificador() : "") + ponto()
                + extensao;

        return diretorio + nomeDeArquivo;
    }

    default ArmazenamentoTemporalidadeEnum obterTemporalidadePorCaminho(String caminho) {
        if (caminho == null)
            return ArmazenamentoTemporalidadeEnum.DEFAULT;
        String split[] = caminho.split("\\Q" + ponto() + "\\E");
        if (split.length == 1)
            return ArmazenamentoTemporalidadeEnum.DEFAULT;
        String split2[] = split[split.length - 2].split("\\Q" + separador() + "\\E");
        if (split2.length == 1)
            return ArmazenamentoTemporalidadeEnum.DEFAULT;
        return ArmazenamentoTemporalidadeEnum.localizarPorIdentificador(split2[split2.length - 1]);
    }

}
