package br.gov.jfrj.siga.sr.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.util.Catalogs;
import play.db.jpa.GenericModel;

@Entity
@Table(name = "DADOS_RH", schema = Catalogs.SIGASR)
public class DadosRH  extends GenericModel {
	private static final long serialVersionUID = -3610233962047379185L;
	
	@Id
	public Long pessoa_id;
	public String pessoa_cpf;
	public String pessoa_nome;
	public String pessoa_sexo;
	public Date pessoa_data_nascimento;
	public String pessoa_rua;
	public String pessoa_bairro;
	public String pessoa_cidade;
	public String pessoa_uf;
	public String pessoa_cep;
	public Long pessoa_matricula;
	public Date pessoa_data_inicio_exercicio;
	public String pessoa_ato_nomeacao;
	public Date pessoa_data_nomeacao;
	public Date pessoa_dt_publ_nomeacao;
	public Date pessoa_data_posse;
	public String pessoa_padrao_referencia;
	public Integer pessoa_situacao;
	public String pessoa_rg;
	public String pessoa_rg_orgao;
	public String pessoa_rg_uf;
	public Date pessoa_data_expedicao_rg;
	public Integer pessoa_estado_civil;
	private String pessoa_grau_de_instrucao;
	public String pessoa_sigla;
	public String pessoa_email;
	public String tipo_rh;
	public String pessoa_tp_sanguineo;
	public String pessoa_naturalidade;
	public String pessoa_nacionalidade;
	public Long cargo_id;
	public String cargo_nome;
	public String cargo_sigla;
	public Long funcao_id;
	public String funcao_nome;
	public String funcao_sigla;
	@Id
	public Long lotacao_id;
	public String lotacao_nome;
	public String lotacao_sigla;
	public Long lotacao_id_pai;
	public String lotacao_tipo;
	public String lotacao_tipo_papel;
	public Long papel_id;
//	public Long orgao_usu_id;

	public class Pessoa {
		public Long pessoa_id;
		public String pessoa_cpf;
		public String pessoa_nome;
		public String pessoa_sexo;
		public Date pessoa_data_nascimento;
		public String pessoa_rua;
		public String pessoa_bairro;
		public String pessoa_cidade;
		public String pessoa_uf;
		public String pessoa_cep;
		public Long pessoa_matricula;
		public Date pessoa_data_inicio_exercicio;
		public String pessoa_ato_nomeacao;
		public Date pessoa_data_nomeacao;
		public Date pessoa_dt_publ_nomeacao;
		public Date pessoa_data_posse;
		public String pessoa_padrao_referencia;
		public Integer pessoa_situacao;
		public String pessoa_rg;
		public String pessoa_rg_orgao;
		public String pessoa_rg_uf;
		public Date pessoa_data_expedicao_rg;
		public Integer pessoa_estado_civil;
		public String pessoa_grau_de_instrucao;
		public String pessoa_sigla;
		public String pessoa_email;
		public String tipo_rh;
		public String pessoa_tp_sanguineo;
		public String pessoa_naturalidade;
		public String pessoa_nacionalidade;
		public Long cargo_id;
		public Long funcao_id;
		public Long lotacao_id;
		public String lotacao_tipo_papel;
	}

	public class Cargo {
		public Long cargo_id;
		public String cargo_nome;
		public String cargo_sigla;
	}

	public class Funcao {
		public Long funcao_id;
		public String funcao_nome;
		public String funcao_sigla;
	}

	public class Lotacao {
		public Long lotacao_id;
		public String lotacao_nome;
		public String lotacao_sigla;
		public Long lotacao_id_pai;
		public String lotacao_tipo;
		public String lotacao_tipo_papel;
	}
	
	public class Papel {
		public Long papel_id;
		public Long papel_lotacao_id;
		public Long papel_cargo_id;
		public Long papel_funcao_id;
		public String papel_lotacao_tipo;
		public Long papel_pessoa_id;
	}
	
	public class Orgao {
		public Long orgao_usu_id;
	}

	public Pessoa getPessoa() {
		if (pessoa_id == null)
			return null;
		Pessoa o = new Pessoa();
		o.pessoa_id = pessoa_id;
		o.pessoa_nome = pessoa_nome;
		o.pessoa_cpf = pessoa_cpf;
		o.pessoa_sexo = pessoa_sexo;
		o.pessoa_data_nascimento = pessoa_data_nascimento;
		o.pessoa_rua = pessoa_rua;
		o.pessoa_bairro = pessoa_bairro;
		o.pessoa_cidade = pessoa_cidade;
		o.pessoa_uf = pessoa_uf;
		o.pessoa_cep = pessoa_cep;
		o.pessoa_matricula = pessoa_matricula;
		o.pessoa_data_inicio_exercicio = pessoa_data_inicio_exercicio;
		o.pessoa_ato_nomeacao = pessoa_ato_nomeacao;
		o.pessoa_data_nomeacao = pessoa_data_nomeacao;
		o.pessoa_dt_publ_nomeacao = pessoa_dt_publ_nomeacao;
		o.pessoa_data_posse = pessoa_data_posse;
		o.pessoa_padrao_referencia = pessoa_padrao_referencia;
		o.pessoa_situacao = pessoa_situacao;
		o.pessoa_rg = pessoa_rg;
		o.pessoa_rg_orgao = pessoa_rg_orgao;
		o.pessoa_rg_uf = pessoa_rg_uf;
		o.pessoa_data_expedicao_rg = pessoa_data_expedicao_rg;
		o.pessoa_estado_civil = pessoa_estado_civil;
		o.pessoa_grau_de_instrucao = pessoa_grau_de_instrucao;
		o.pessoa_sigla = pessoa_sigla;
		o.pessoa_email = pessoa_email;
		o.tipo_rh = tipo_rh;
		o.pessoa_tp_sanguineo = pessoa_tp_sanguineo;
		o.pessoa_naturalidade = pessoa_naturalidade;
		o.pessoa_nacionalidade = pessoa_nacionalidade;
		o.cargo_id = cargo_id;
		o.funcao_id = funcao_id;
		o.lotacao_id = lotacao_id;
		o.lotacao_tipo_papel = lotacao_tipo_papel;
		return o;
	}

	public Cargo getCargo() {
		if (cargo_id == null)
			return null;
		Cargo o = new Cargo();
		o.cargo_id = cargo_id;
		o.cargo_nome = cargo_nome;
		o.cargo_sigla = cargo_sigla;
		return o;
	}

	public Funcao getFuncao() {
		if (funcao_id == null)
			return null;
		Funcao o = new Funcao();
		o.funcao_id = funcao_id;
		o.funcao_nome = funcao_nome;
		o.funcao_sigla = funcao_sigla;
		return o;
	}

	public Lotacao getLotacao() {
		if (lotacao_id == null)
			return null;
		Lotacao o = new Lotacao();
		o.lotacao_id = lotacao_id;
		o.lotacao_nome = lotacao_nome;
		o.lotacao_sigla = lotacao_sigla;
		o.lotacao_id_pai = lotacao_id_pai;
		o.lotacao_tipo = lotacao_tipo;
		o.lotacao_tipo_papel = lotacao_tipo_papel;
		return o;
	}
	
	public Papel getPapel() {
		if (papel_id == null)
			return null;
		Papel o = new Papel();
		o.papel_pessoa_id = pessoa_id;
		o.papel_id = papel_id;
		o.papel_lotacao_id = lotacao_id;
		o.papel_cargo_id = cargo_id;
		o.papel_funcao_id = funcao_id;
		o.papel_lotacao_tipo = lotacao_tipo_papel;
		return o;
	}
	
	public Orgao getOrgao() {
		Orgao org = new Orgao();
//		org.orgao_usu_id = orgao_usu_id;
		return org;
	}

}
