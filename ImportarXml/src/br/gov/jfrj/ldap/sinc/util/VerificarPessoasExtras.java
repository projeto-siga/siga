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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.hibernate.cfg.AnnotationConfiguration;

import br.gov.jfrj.importar.AdGrupoDeSeguranca;
import br.gov.jfrj.importar.AdObjeto;
import br.gov.jfrj.importar.AdUsuario;
import br.gov.jfrj.ldap.sinc.LdapDaoSinc;
import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class VerificarPessoasExtras {

	/**
	 * @param args
	 * @throws AplicacaoException
	 */
	public static void main(String[] args) throws AplicacaoException {
		List<String> resultado = new ArrayList<String>();
		Logger log = Logger.getLogger(VerificarPessoasExtras.class.getName());
		SincProperties conf = SincProperties.getInstancia("sjrj.prod");
		LdapDaoSinc ldap = LdapDaoSinc.getInstance(conf);
		AnnotationConfiguration cfg;
		try {
			cfg = CpDao.criarHibernateCfg(conf.getBdStringConexao(), conf
					.getBdUsuario(), conf.getBdSenha());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AplicacaoException(
					"Não foi possível configurar o hibernate!");
		}
		HibernateUtil.configurarHibernate(cfg, "");
		CpDao dao = CpDao.getInstance();

		List<AdObjeto> lista;
		try {
			lista = ldap.pesquisarObjeto(
					"OU=Usuarios,DC=corp,DC=jfrj,DC=gov,DC=br",
					"OU=Grupos,DC=corp,DC=jfrj,DC=gov,DC=br");
		} catch (NamingException e) {
			e.printStackTrace();
			throw new AplicacaoException(
					"Não foi possível ler a lista de usuários do LDAP no DN "
							+ conf.getDnGestaoIdentidade());
		}
		// nomes de usuários com iniciais maiúsculas e sem acentos
		for (AdObjeto o : lista) {
			if (o instanceof AdGrupoDeSeguranca) {
				AdGrupoDeSeguranca gs = (AdGrupoDeSeguranca) o;

				String siglaLotacao = gs.getNome().replace("_ler", "").replace(
						"-pub", "").replace("-gab", "").replace("-juiz", "")
						.replace("_pub", "").replace("_sec", "").replace(
								"_gab", "").replace("_juiz", "").replace(
								"juiz_", "").replace("_fs", "").replace("_gs",
								"").replace("sec_", "").replace("gab_", "");
				if (siglaLotacao.startsWith("ts")) {
					siglaLotacao = siglaLotacao.substring(2);
				}
				resultado.add("Checando " + siglaLotacao + "("
						+ gs.getNomeCompleto() + ") \n");

				for (AdObjeto m : gs.getMembros()) {
					if (m instanceof AdUsuario) {
						AdUsuario membro = (AdUsuario) m;
						List<DpPessoa> listaPessoa = HibernateUtil.getSessao()
								.createQuery(
										"from DpPessoa p where data_fim_pessoa is null and ID_ORGAO_USU = "
												+ conf.getIdLocalidade()
												+ " AND sigla_pessoa like '"
												+ membro.getSigla()
														.toUpperCase() + "'")
								.list();
						for (DpPessoa p : listaPessoa) {
							if (!siglaLotacao.equalsIgnoreCase(p.getLotacao()
									.getSigla())) {
								resultado
										.add("\t"
												+ p.getSigla()
												+ " "
												+ p.getNomePessoa()
												+ " (lotado em: "
												+ (p.getLotacao() == null ? "sem lotação"
														: p.getLotacao()
																.getSigla()
																+ ")\n"));
							}
						}
					}
				}
			}

		}

		for (String r : resultado) {
			System.out.println(r);
		}

	}

}
