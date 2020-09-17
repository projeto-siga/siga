package br.gov.jfrj.siga.ex.api.v1;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarSpPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarSpPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaTramitarSpPost;

public class DocSiglaTramitarSpPost implements IDocSiglaTramitarSpPost {

	@Override
	public String getContext() {
		return "tramitar documento (SP sem Papel)";
	}

	@Override
	public void run(DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws Exception {
		System.out.println(ToStringBuilder.reflectionToString(req, ToStringStyle.SHORT_PREFIX_STYLE));
	}

}
