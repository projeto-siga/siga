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

import java.util.List;

import org.hibernate.cfg.AnnotationConfiguration;

import br.gov.jfrj.importar.AdGrupoDeDistribuicao;
import br.gov.jfrj.importar.AdGrupoDeSeguranca;
import br.gov.jfrj.importar.AdModelo;
import br.gov.jfrj.importar.AdObjeto;
import br.gov.jfrj.importar.AdUsuario;
import br.gov.jfrj.ldap.sinc.LdapDaoSinc;
import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class InjetorLDAP {

	private static LdapDaoSinc ldap;
	private static boolean injetarGruposDeSeguranca = true;
	private static boolean injetarGruposDeDistribuicao = false;
	private static boolean injetarUsuarios = false;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		SincProperties conf = SincProperties.getInstancia();
		ldap = LdapDaoSinc.getInstance(conf);

		AnnotationConfiguration cfg = CpDao.criarHibernateCfg(
				"jdbc:oracle:thin:@mclaren:1521:mcl", "corporativo",
				"corporativo");

		HibernateUtil.configurarHibernate(cfg, "");

		AdModelo adModelo = AdModelo.getInstance(conf);
		List<AdObjeto> lista = adModelo.gerarModelo();

		// nomes de usuários com iniciais maiúsculas e sem acentos
		for (AdObjeto o : lista) {
			if (injetarGruposDeSeguranca && o instanceof AdGrupoDeSeguranca) {
				ldap.criarGrupo((AdGrupoDeSeguranca) o, false);
			}
			if (injetarGruposDeDistribuicao
					&& o instanceof AdGrupoDeDistribuicao) {
				ldap.criarGrupo((AdGrupoDeDistribuicao) o, false);
			}
			if (injetarUsuarios && o instanceof AdUsuario) {
				ldap.criarUsuario((AdUsuario) o);
			}
		}
	}

}
