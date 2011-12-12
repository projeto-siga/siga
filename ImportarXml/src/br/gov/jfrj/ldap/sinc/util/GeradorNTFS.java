/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.ldap.sinc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.cfg.AnnotationConfiguration;

import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class GeradorNTFS {

	private static List<String> comandosCriacao = new ArrayList<String>();
	private static List<String> comandosPermissao = new ArrayList<String>();

	private final static SincProperties conf = SincProperties.getInstancia();

	private static String servidor = conf.getNtfsServidor();
	private static String usuario = conf.getNtfsUsuario();
	private static String senha = conf.getNtfsSenha();
	private static String pathRaiz = conf.getNtfsPathRaiz();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		AnnotationConfiguration cfg = CpDao.criarHibernateCfg(conf
				.getBdStringConexao(), conf.getBdUsuario(), conf.getBdSenha());
		HibernateUtil.configurarHibernate(cfg, "");

		String sql = "select Lpad(lotacao,Length(lotacao) + LEVEL * 10 - 10,'_') as nome, pessoas from (select count(*) pessoas, sigla_orgao_usu orgao,l.sigla_lotacao lotacao, l.id_lotacao id_lotacao, lp.sigla_lotacao lotacao_pai, lp.id_lotacao id_lotacao_pai  from dp_pessoa p, dp_lotacao l, cp_orgao_usuario o, dp_lotacao lp where p.data_fim_pessoa is null and p.id_lotacao = l.id_lotacao and p.id_orgao_usu = o.id_orgao_usu and l.id_lotacao_pai = lp.id_lotacao (+) and o.id_orgao_usu = 1  and situacao_funcional_pessoa = 1 group by sigla_orgao_usu, l.sigla_lotacao, l.id_lotacao, lp.sigla_lotacao, lp.id_lotacao ORDER BY lotacao) START WITH lotacao_pai = 'SJRJ' CONNECT BY PRIOR id_lotacao = id_lotacao_pai";
		List<Object> resultado = HibernateUtil.getSessao().createSQLQuery(sql)
				.addScalar("nome", Hibernate.STRING).list();

		final int nivelRaiz = pathRaiz.replace("\\", "&").split("&").length + 1;
		String pathRaizAtual = pathRaiz;
		String pastaNova = "";
		String ultimaPasta = "";
		for (Object o : resultado) {
			String nomePastaNova = o.toString().replace("_", "");
			if (!o.toString().contains("__________")) {
				pastaNova = pathRaiz + "\\" + nomePastaNova;
			} else {
				String arrayNivelPastaNova[] = o.toString().split("__________");
				int nivelPastaNova = arrayNivelPastaNova.length - 1;

				String arrayNivelUltimaPasta[] = ultimaPasta.replace("\\", "&")
						.split("&");
				int nivelUltimaPasta = arrayNivelUltimaPasta.length - nivelRaiz;

				if (nivelPastaNova > nivelUltimaPasta) {
					pastaNova = ultimaPasta + "\\" + nomePastaNova;
				}

				if (nivelPastaNova == nivelUltimaPasta) {
					pastaNova = ultimaPasta.substring(0, ultimaPasta
							.lastIndexOf("\\"))
							+ "\\" + nomePastaNova;
				}

				if (nivelPastaNova < nivelUltimaPasta) {
					int diferencaNivel = nivelUltimaPasta - nivelPastaNova;
					String pastaAnterior = ultimaPasta.substring(0, ultimaPasta
							.lastIndexOf("\\"));
					for (int i = 0; i < diferencaNivel - 1; i++) {
						pastaAnterior = ultimaPasta.substring(0, pastaAnterior
								.lastIndexOf("\\"));
					}
					pastaNova = pastaAnterior.substring(0, pastaAnterior
							.lastIndexOf("\\"))
							+ "\\" + nomePastaNova;
				}

			}

			System.out.println(o.toString());
			System.out.println(pastaNova);
			List<Permissao> perms = new ArrayList<Permissao>();

			perms.add(new Permissao(conf.getPfxGrpSegAuto()
					+ nomePastaNova.toLowerCase() + conf.getSfxGrpSegAuto(),
					"C"));
			criarPasta(pastaNova, perms);
			
			CpDao dao = CpDao.getInstance();
			CpOrgaoUsuario oUsu = dao.consultar(Long.valueOf(conf.getIdLocalidade()), CpOrgaoUsuario.class, false);
			DpLotacao lotEx = new DpLotacao();
			lotEx.setSigla(nomePastaNova);
			lotEx.setOrgaoUsuario(oUsu);
			DpLotacao lot = dao.consultarPorSigla(lotEx);
//			if (!pastaNova.contains("\\DIRFO")
//					&& !pastaNova.contains(("\\DIR-"))) {
			if (lot == null){
				throw new AplicacaoException("Problema na lotação (não existe ou está definida como lotação ADMINISTRATIVA e JUDICIAL ao mesmo tempo): " + nomePastaNova);
			}
			if (lot.getCpTipoLotacao()!=null && lot.getCpTipoLotacao().getIdTpLotacao().equals(100L)){
				perms.clear();
				perms.add(new Permissao(conf.getPfxGrpSegAutoGab()
						+ nomePastaNova.toLowerCase()
						+ conf.getSfxGrpSegAutoGab(), "C"));
				criarPasta(pastaNova + "\\Gabinete", perms);

				perms.clear();
				perms.add(new Permissao(conf.getPfxGrpSegAutoSec()
						+ nomePastaNova.toLowerCase()
						+ conf.getSfxGrpSegAutoSec(), "C"));
				criarPasta(pastaNova + "\\Secretaria", perms);

				perms.clear();
				perms.add(new Permissao(conf.getPfxGrpSegAutoJuiz()
						+ nomePastaNova.toLowerCase()
						+ conf.getSfxGrpSegAutoJuiz(), "C"));
				criarPasta(pastaNova + "\\Juizes", perms);

				perms.clear();
				perms.add(new Permissao(
						conf.getPfxGrpSegAuto() + nomePastaNova.toLowerCase()
								+ conf.getSfxGrpSegAuto(), "C"));
//				perms.add(new Permissao(conf.getPfxGrpSegAutoGab()
//						+ nomePastaNova.toLowerCase()
//						+ conf.getSfxGrpSegAutoGab(), "C"));
//				perms.add(new Permissao(conf.getPfxGrpSegAutoSec()
//						+ nomePastaNova.toLowerCase()
//						+ conf.getSfxGrpSegAutoSec(), "C"));
//				perms.add(new Permissao(conf.getPfxGrpSegAutoJuiz()
//						+ nomePastaNova.toLowerCase()
//						+ conf.getSfxGrpSegAutoJuiz(), "C"));
				criarPasta(pastaNova + "\\Publica", perms);
			}

			ultimaPasta = pastaNova;
		}

		File f = new File("upload.bat");
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		for (String c : comandosCriacao) {
			System.out.println(c);
			bw.write(c);
			bw.newLine();
		}

		for (String c : comandosPermissao) {
			System.out.println(c);
			bw.write(c);
			bw.newLine();
		}

		bw.close();

		fw.close();

		if (conf.getNtfsExecutarPsexecNaSaida()){
			executar("psexec.exe \\\\" + servidor + " -d -c -f -u " + usuario
					+ " -p " + senha + " upload.bat");
		}
		
		if (conf.getNtfsExcluirArquivoNaSaida()){
			if (f.exists()) {
				f.delete();
			}
		}
	}

	private static void criarPasta(String path, List<Permissao> permissoes)
			throws InterruptedException {

		String cmdPasta = "cmd /c \"mkdir " + path + "\"";
		comandosCriacao.add(cmdPasta);

		for (Permissao p : permissoes) {
			String cmdPermissao = "cacls " + path + " /E /P " + p.getObjetoAD()
					+ ":" + p.getConcessao();
			comandosPermissao.add(cmdPermissao);
			
			String nomePasta = path.substring(path.lastIndexOf("\\")+1);
			if (!nomePasta.equals("Gabinete") && !nomePasta.equals("Secretaria") && !nomePasta.equals("Juizes") && !nomePasta.equals("Publica")){
				String concessaoCompartilhamento = p.getConcessao().equals("C")?"CHANGE":"READ";
				String cmdCompartilhamento = "net share " + nomePasta + "=" + path + " /GRANT:" + p.getObjetoAD()+"," + concessaoCompartilhamento;
				comandosPermissao.add(cmdCompartilhamento);
			
			}  
		}

	}

	private synchronized static void executar(String command)
			throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(command);
		p.waitFor();
		// Thread.sleep(5000);
		p.getOutputStream().close();
		p.getInputStream().close();
		p.getErrorStream().close();
	}

}

/**
 * 
 * @author kpf
 *Concessões : 	C - Change
 *				R - Read
 */
class Permissao {
	private String o;
	private String c;

	public Permissao(String objetoAD, String concessao) {
		setObjetoAD(objetoAD);
		setConcessao(concessao);
	}

	public void setObjetoAD(String objetoAD) {
		this.o = objetoAD;
	}

	public String getObjetoAD() {
		return o;
	}

	public void setConcessao(String concessao) {
		c = concessao;
	}

	public String getConcessao() {
		return c;
	}
}
