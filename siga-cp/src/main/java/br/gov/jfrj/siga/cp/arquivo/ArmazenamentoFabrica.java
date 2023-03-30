package br.gov.jfrj.siga.cp.arquivo;

import br.gov.jfrj.siga.cp.CpArquivoTipoArmazenamentoEnum;

public class ArmazenamentoFabrica {
    public static Armazenamento getInstance(CpArquivoTipoArmazenamentoEnum tipo) {
        switch (tipo) {
            case HCP:
                return ArmazenamentoHCP.getInstance();
            case S3:
                return ArmazenamentoS3REST.getInstance();
            default:
                return null;
        }
    }
}
