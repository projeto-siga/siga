package br.gov.jfrj.siga.wf.util;

import java.util.Map;

import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.base.AcaoVO;

public class WfAcaoVO extends AcaoVO {

	public static WfAcaoVO.Builder builder() {
		return new WfAcaoVO.Builder();
	}

	public static class Builder {
		private String icone;
		private String nome;
		private String nameSpace;
		private String acao;
		private boolean pode;
		private String msgConfirmacao;
		private String pre;
		private String pos;
		private String classe;
		private String modal;
		private Map<String, String> params;
		private String explicacao;
		private boolean post;

		public Builder icone(String icone) {
			this.icone = icone;
			return this;
		}

		public Builder nome(String nome) {
			this.nome = nome;
			return this;
		}

		public Builder nameSpace(String nameSpace) {
			this.nameSpace = nameSpace;
			return this;
		}

		public Builder acao(String acao) {
			this.acao = acao;
			return this;
		}

		public Builder pode(boolean pode) {
			this.pode = pode;
			return this;
		}

		public Builder msgConfirmacao(String msgConfirmacao) {
			this.msgConfirmacao = msgConfirmacao;
			return this;
		}

		public Builder pre(String pre) {
			this.pre = pre;
			return this;
		}

		public Builder pos(String pos) {
			this.pos = pos;
			return this;
		}

		public Builder classe(String classe) {
			this.classe = classe;
			return this;
		}

		public Builder modal(String modal) {
			this.modal = modal;
			return this;
		}

		public Builder params(Map<String, String> params) {
			this.params = params;
			return this;
		}

		public Builder explicacao(String explicacao) {
			this.explicacao = explicacao;
			return this;
		}

		public Builder post(boolean post) {
			this.post = post;
			return this;
		}

		public Builder exp(Expression exp) {
			this.pode = exp.eval();
			this.explicacao = WfAcaoHelper.formatarExplicacao(exp, this.pode);
			return this;
		}

		public AcaoVO build() {
			AcaoVO acaoVO = new AcaoVO();
			acaoVO.setIcone(icone);
			acaoVO.setNome(nome);
			acaoVO.setNameSpace(nameSpace);
			acaoVO.setAcao(acao);
			acaoVO.setPode(pode);
			acaoVO.setMsgConfirmacao(msgConfirmacao);
			acaoVO.setPre(pre);
			acaoVO.setPos(pos);
			acaoVO.setClasse(classe);
			acaoVO.setModal(modal);
			acaoVO.setParams(params);
			acaoVO.setExplicacao(explicacao);
			acaoVO.setPost(post);
			return acaoVO;
		}
	}
}
