package br.gov.jfrj.siga.api.v1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IArmazenamentoIdMigrarPost;
import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.CpArquivoTipoArmazenamentoEnum;
import br.gov.jfrj.siga.cp.arquivo.Armazenamento;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoFabrica;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoTemporalidadeEnum;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class ArmazenamentoIdMigrarPost implements IArmazenamentoIdMigrarPost {

    @Override
    public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
        CpArquivoTipoArmazenamentoEnum origem = CpArquivoTipoArmazenamentoEnum.valueOf(req.origem);
        CpArquivoTipoArmazenamentoEnum destino = CpArquivoTipoArmazenamentoEnum.valueOf(req.destino);
        CpArquivo arq = CpDao.getInstance().consultar(Long.parseLong(req.id), CpArquivo.class, false);
        byte[] conteudo = arq.getConteudo();

        String caminho = null;

        if (conteudo != null) {
            Armazenamento a = ArmazenamentoFabrica.getInstance(destino);
            caminho = a.gerarCaminho(null, arq.identificarTipoDeConteudo(),
                    ArmazenamentoTemporalidadeEnum.MANTER_POR_30_ANOS);
            a.salvar(caminho, arq.getConteudoTpArq(), conteudo);
            resp.memoria = (double) conteudo.length;
        } else {
            resp.memoria = 0d;
//            throw new NullPointerException("Conteúdo nulo para arquivo " + req.id);
        }

        Connection con = getConnection();
        con.setAutoCommit(true);

        PreparedStatement ps = con.prepareStatement(
                "update corporativo.cp_arquivo set caminho = ?, tp_armazenamento = ? where id_arq = ?");
        ps.setString(1, caminho);
        ps.setString(2, destino.name());
        ps.setLong(3, arq.getIdArq());
        ps.execute();

        if (con != null)
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    public Connection getConnection() throws Exception {
        String dsName = "java:jboss/datasources/SigaCpDS";
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:");
        DataSource ds = (DataSource) envContext.lookup(dsName);
        Connection connection = ds.getConnection();
        if (connection == null)
            throw new Exception("Can't open connection to database.");
        return connection;
    }

    @Override
    public String getContext() {
        return "obter quadro quantitativo";
    }

}
