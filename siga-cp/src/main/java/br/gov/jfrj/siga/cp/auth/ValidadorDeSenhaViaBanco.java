package br.gov.jfrj.siga.cp.auth;

import java.security.NoSuchAlgorithmException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.GeraMessageDigest;
import br.gov.jfrj.siga.cp.CpIdentidade;

public class ValidadorDeSenhaViaBanco implements ValidadorDeSenha {

    @Override
    public boolean validarSenha(CpIdentidade identidade, String senha) {
        String hashAtual;

        if (identidade == null)
            throw new AplicacaoException("Identidade deve ser informada");

        if (identidade.getDscSenhaIdentidade() == null || identidade.getDscSenhaIdentidade().equals(""))
            throw new AplicacaoException("Senha da identidade não pode ser nula");
        hashAtual = gerarMessageDigest(senha);
        return identidade.getDscSenhaIdentidade().equals(hashAtual);

    }

    @Override
    public String gerarMessageDigest(String senha) {
        try {
            return GeraMessageDigest.executaHash(senha.getBytes(), "MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro inicializando hash MD5", e);
        }
    }

}
