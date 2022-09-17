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

import org.hibernate.cfg.Configuration;

import br.gov.jfrj.ldap.AdGrupoDeDistribuicao;
import br.gov.jfrj.ldap.AdGrupoDeSeguranca;
import br.gov.jfrj.ldap.AdModelo;
import br.gov.jfrj.ldap.AdObjeto;
import br.gov.jfrj.ldap.AdUsuario;
import br.gov.jfrj.ldap.sinc.LdapBL;
import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class InjetorLDAP {

	private static LdapBL ldap;
	private static boolean injetarGruposDeSeguranca = true;
	private static boolean injetarGruposDeDistribuicao = false;
	private static boolean injetarUsuarios = false;

	private static SincProperties conf = SincProperties.getInstancia();
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		
		ldap = LdapBL.getInstance(conf);

		CpAmbienteEnumBL ambiente = CpAmbienteEnumBL.DESENVOLVIMENTO;
		Cp.getInstance().getProp().setPrefixo(ambiente.getSigla());
		Configuration cfg = CpDao.criarHibernateCfg(ambiente);

		HibernateUtil.configurarHibernate(cfg);

		AdModelo adModelo = AdModelo.getInstance(conf);
		List<AdObjeto> lista = adModelo.gerarModelo(consultarPessoas(),null,false);

		// nomes de usuï¿½rios com iniciais maiï¿½sculas e sem acentos
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

	private static List<DpPessoa> consultarPessoas() {
		CpDao dao = CpDao.getInstance();
		DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		flt.setNome("");
		flt.setSigla("");
		flt.setLotacao(null);
		flt.setSituacaoFuncionalPessoa(conf.getIdSituacaoFuncionalAtivo()
				.toString());
		flt.setBuscarFechadas(false);
		flt.setIdOrgaoUsu(Long.valueOf(conf.getIdLocalidade()));

		List<DpPessoa> list = dao.consultarPorFiltro(flt);
		return list;
	}
}
