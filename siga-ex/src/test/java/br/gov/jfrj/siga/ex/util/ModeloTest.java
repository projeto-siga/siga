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
package br.gov.jfrj.siga.ex.util;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;
import br.gov.jfrj.siga.cp.CpPapel;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.util.FuncoesEL;
import br.gov.jfrj.siga.ex.util.ProcessadorModeloFreemarker;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Diagram;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class ModeloTest extends TestCase {

	private Configuration cfg;

	public ModeloTest() throws Exception {
		cfg = new Configuration();
		String s = cfg.getVersionNumber();
		// Specify the data source where the template files come from.
		// Here I set a file directory for it:
		cfg.setDirectoryForTemplateLoading(new File(
				"src/main/resources/br/gov/jfrj/siga/ex/util/test"));
		// Specify how templates will see the data-model. This is an
		// advanced
		// topic...
		// but just use this:
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setWhitespaceStripping(true);
	}

	public void testGeraModelo() throws Exception {
		if (true)
			return;
		ProcessadorModeloFreemarker p = new ProcessadorModeloFreemarker();

		Map<String, Object> attrs = new TreeMap<String, Object>();
		Map<String, Object> params = new TreeMap<String, Object>();

		ExDocumento doc = new ExDocumento();
		doc.setOrgaoUsuario(ExDao.getInstance().consultar(1L,
				CpOrgaoUsuario.class, false));
		ExMovimentacao mov = new ExMovimentacao();
		attrs.put("doc", doc);
		attrs.put("mov", mov);
		attrs.put("mob", mov.getExMobil());
		attrs.put("template", "teste freemarker: ${param.doc!}.");

		params.put("processar_modelo", "1");
		if (doc != null && doc.getIdDoc() != null)
			params.put("idDoc", doc.getIdDoc().toString());
		if (mov != null && mov.getIdMov() != null) {
			params.put("id", mov.getIdMov().toString());
		}

		String s = p.processarModelo(doc.getOrgaoUsuario(), attrs, params);
		System.out.println(s);
	}

	public void testGeraModeloAntigo() throws Exception {
		if (true)
			return;
		// Create the root hash
		Map root = new HashMap();
		Map param = new HashMap();
		root.put("root", root);
		root.put("param", param);
		root.put("entrevista", false);
		root.put("formulario", true);
		root.put("documento", false);
		root.put("assinatura", false);
		root.put("finalizacao", false);
		root.put("func", new FuncoesEL());

		Template temp = cfg.getTemplate("memorando.ftl");

		try (Writer out = new OutputStreamWriter(System.out)) {
			temp.process(root, out);
			out.flush();
		}
	}

	public void testGeraDesenhoCp() throws Exception {
		Diagram d = new Diagram();
		d.setfMergeWithAbstractClass(true);
		boolean fI = true;

		d.addClass(DpPessoa.class, fI);
		d.addClass(DpLotacao.class, fI);
		d.addClass(DpFuncaoConfianca.class, fI);
		d.addClass(DpCargo.class, fI);
		d.addClass(CpPapel.class, fI);
		d.addClass(CpOrgaoUsuario.class, fI);

		d.addClass(br.gov.jfrj.siga.dp.CpOrgao.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.DpSubstituicao.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpFeriado.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpOcorrenciaFeriado.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpAplicacaoFeriado.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpLocalidade.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpUF.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpPersonalizacao.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpConfiguracao.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpServico.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpTipoGrupo.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpGrupo.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpTipoLotacao.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpTipoPapel.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpPapel.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpTipoServico.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpTipoIdentidade.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpIdentidade.class, fI);

		d.createGraphML("target/siga.graphml", true, false);
	}

	public void testGeraDesenhoEx() throws Exception {
		Diagram d = new Diagram();
		d.setfMergeWithAbstractClass(true);
		boolean fI = true;

		d.addClass(DpPessoa.class, fI);
		d.addClass(DpLotacao.class, fI);
		d.addClass(DpFuncaoConfianca.class, fI);
		d.addClass(DpCargo.class, fI);
		d.addClass(CpPapel.class, fI);
		d.addClass(CpOrgaoUsuario.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpOrgao.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.DpSubstituicao.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpFeriado.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpOcorrenciaFeriado.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpAplicacaoFeriado.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpLocalidade.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpUF.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpPersonalizacao.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpConfiguracao.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpServico.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpTipoGrupo.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpGrupo.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpTipoLotacao.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpTipoPapel.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpPapel.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpTipoServico.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpTipoIdentidade.class, fI);
		d.addClass(br.gov.jfrj.siga.cp.CpIdentidade.class, fI);

		d.addClass(br.gov.jfrj.siga.ex.ExMobil.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExDocumento.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExFormaDocumento.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExConfiguracao.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExClassificacao.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExModelo.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExTemporalidade.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExTipoDespacho.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExTipoDestinacao.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExTipoDocumento.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExNivelAcesso.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExEstadoDoc.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExPreenchimento.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExTipoFormaDoc.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExVia.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExMovimentacao.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExTpDocPublicacao.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExTipoMobil.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.BIE.ExBoletimDoc.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExPapel.class, fI);
		d.addClass(br.gov.jfrj.siga.ex.ExEmailNotificacao.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpMarcador.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpTipoMarca.class, fI);

		d.addClass(br.gov.jfrj.siga.ex.ExMarca.class, fI);
		d.addClass(br.gov.jfrj.siga.dp.CpMarca.class, fI);

		d.createGraphML("target/siga-ex.graphml", false, false);
	}

}
