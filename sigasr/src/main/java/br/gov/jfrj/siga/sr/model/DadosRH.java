package br.gov.jfrj.siga.sr.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "dados_rh", schema = "sigarh")
public class DadosRH  extends Objeto {
    
    public static final ActiveRecord<DadosRH> AR = new ActiveRecord<>(DadosRH.class);
	private static final long serialVersionUID = -3610233962047379185L;
	
	@Id
    private Long pessoa_id;
	private String pessoa_cpf;
	private String pessoa_nome;
	private String pessoa_sexo;
	private Date pessoa_data_nascimento;
	private String pessoa_rua;
	private String pessoa_bairro;
	private String pessoa_cidade;
	private String pessoa_uf;
	private String pessoa_cep;
	private Long pessoa_matricula;
	private Date pessoa_data_inicio_exercicio;
	private String pessoa_ato_nomeacao;
	private Date pessoa_data_nomeacao;
	private Date pessoa_dt_publ_nomeacao;
	private Date pessoa_data_posse;
	private String pessoa_padrao_referencia;
	private Integer pessoa_situacao;
	private String pessoa_rg;
	private String pessoa_rg_orgao;
	private String pessoa_rg_uf;
	private Date pessoa_data_expedicao_rg;
	private Integer pessoa_estado_civil;
	private String pessoa_grau_de_instrucao;
	private String pessoa_sigla;
	private String pessoa_email;
	private String tipo_rh;
	private String pessoa_tp_sanguineo;
	private String pessoa_naturalidade;
	private String pessoa_nacionalidade;
	private Long cargo_id;
	private String cargo_nome;
	private String cargo_sigla;
	private Long funcao_id;
	private String funcao_nome;
	private String funcao_sigla;
	@Id
    private Long lotacao_id;
	private String lotacao_nome;
	private String lotacao_sigla;
	private Long lotacao_id_pai;
	private String lotacao_tipo;
	private String lotacao_tipo_papel;
	private Long papel_id;
//	public Long orgao_usu_id;

	public class Pessoa {
		private Long pessoa_id;
		private String pessoa_cpf;
		private String pessoa_nome;
		private String pessoa_sexo;
		private Date pessoa_data_nascimento;
		private String pessoa_rua;
		private String pessoa_bairro;
		private String pessoa_cidade;
		private String pessoa_uf;
		private String pessoa_cep;
		private Long pessoa_matricula;
		private Date pessoa_data_inicio_exercicio;
		private String pessoa_ato_nomeacao;
		private Date pessoa_data_nomeacao;
		private Date pessoa_dt_publ_nomeacao;
		private Date pessoa_data_posse;
		private String pessoa_padrao_referencia;
		private Integer pessoa_situacao;
		private String pessoa_rg;
		private String pessoa_rg_orgao;
		private String pessoa_rg_uf;
		private Date pessoa_data_expedicao_rg;
		private Integer pessoa_estado_civil;
		private String pessoa_grau_de_instrucao;
		private String pessoa_sigla;
		private String pessoa_email;
		private String tipo_rh;
		private String pessoa_tp_sanguineo;
		private String pessoa_naturalidade;
		private String pessoa_nacionalidade;
		private Long cargo_id;
		private Long funcao_id;
		private Long lotacao_id;
		private String lotacao_tipo_papel;
		
        public Long getPessoa_id() {
            return pessoa_id;
        }
        public void setPessoa_id(Long pessoa_id) {
            this.pessoa_id = pessoa_id;
        }
        public String getPessoa_cpf() {
            return pessoa_cpf;
        }
        public void setPessoa_cpf(String pessoa_cpf) {
            this.pessoa_cpf = pessoa_cpf;
        }
        public String getPessoa_nome() {
            return pessoa_nome;
        }
        public void setPessoa_nome(String pessoa_nome) {
            this.pessoa_nome = pessoa_nome;
        }
        public String getPessoa_sexo() {
            return pessoa_sexo;
        }
        public void setPessoa_sexo(String pessoa_sexo) {
            this.pessoa_sexo = pessoa_sexo;
        }
        public Date getPessoa_data_nascimento() {
            return pessoa_data_nascimento;
        }
        public void setPessoa_data_nascimento(Date pessoa_data_nascimento) {
            this.pessoa_data_nascimento = pessoa_data_nascimento;
        }
        public String getPessoa_rua() {
            return pessoa_rua;
        }
        public void setPessoa_rua(String pessoa_rua) {
            this.pessoa_rua = pessoa_rua;
        }
        public String getPessoa_bairro() {
            return pessoa_bairro;
        }
        public void setPessoa_bairro(String pessoa_bairro) {
            this.pessoa_bairro = pessoa_bairro;
        }
        public String getPessoa_cidade() {
            return pessoa_cidade;
        }
        public void setPessoa_cidade(String pessoa_cidade) {
            this.pessoa_cidade = pessoa_cidade;
        }
        public String getPessoa_uf() {
            return pessoa_uf;
        }
        public void setPessoa_uf(String pessoa_uf) {
            this.pessoa_uf = pessoa_uf;
        }
        public String getPessoa_cep() {
            return pessoa_cep;
        }
        public void setPessoa_cep(String pessoa_cep) {
            this.pessoa_cep = pessoa_cep;
        }
        public Long getPessoa_matricula() {
            return pessoa_matricula;
        }
        public void setPessoa_matricula(Long pessoa_matricula) {
            this.pessoa_matricula = pessoa_matricula;
        }
        public Date getPessoa_data_inicio_exercicio() {
            return pessoa_data_inicio_exercicio;
        }
        public void setPessoa_data_inicio_exercicio(Date pessoa_data_inicio_exercicio) {
            this.pessoa_data_inicio_exercicio = pessoa_data_inicio_exercicio;
        }
        public String getPessoa_ato_nomeacao() {
            return pessoa_ato_nomeacao;
        }
        public void setPessoa_ato_nomeacao(String pessoa_ato_nomeacao) {
            this.pessoa_ato_nomeacao = pessoa_ato_nomeacao;
        }
        public Date getPessoa_data_nomeacao() {
            return pessoa_data_nomeacao;
        }
        public void setPessoa_data_nomeacao(Date pessoa_data_nomeacao) {
            this.pessoa_data_nomeacao = pessoa_data_nomeacao;
        }
        public Date getPessoa_dt_publ_nomeacao() {
            return pessoa_dt_publ_nomeacao;
        }
        public void setPessoa_dt_publ_nomeacao(Date pessoa_dt_publ_nomeacao) {
            this.pessoa_dt_publ_nomeacao = pessoa_dt_publ_nomeacao;
        }
        public Date getPessoa_data_posse() {
            return pessoa_data_posse;
        }
        public void setPessoa_data_posse(Date pessoa_data_posse) {
            this.pessoa_data_posse = pessoa_data_posse;
        }
        public String getPessoa_padrao_referencia() {
            return pessoa_padrao_referencia;
        }
        public void setPessoa_padrao_referencia(String pessoa_padrao_referencia) {
            this.pessoa_padrao_referencia = pessoa_padrao_referencia;
        }
        public Integer getPessoa_situacao() {
            return pessoa_situacao;
        }
        public void setPessoa_situacao(Integer pessoa_situacao) {
            this.pessoa_situacao = pessoa_situacao;
        }
        public String getPessoa_rg() {
            return pessoa_rg;
        }
        public void setPessoa_rg(String pessoa_rg) {
            this.pessoa_rg = pessoa_rg;
        }
        public String getPessoa_rg_orgao() {
            return pessoa_rg_orgao;
        }
        public void setPessoa_rg_orgao(String pessoa_rg_orgao) {
            this.pessoa_rg_orgao = pessoa_rg_orgao;
        }
        public String getPessoa_rg_uf() {
            return pessoa_rg_uf;
        }
        public void setPessoa_rg_uf(String pessoa_rg_uf) {
            this.pessoa_rg_uf = pessoa_rg_uf;
        }
        public Date getPessoa_data_expedicao_rg() {
            return pessoa_data_expedicao_rg;
        }
        public void setPessoa_data_expedicao_rg(Date pessoa_data_expedicao_rg) {
            this.pessoa_data_expedicao_rg = pessoa_data_expedicao_rg;
        }
        public Integer getPessoa_estado_civil() {
            return pessoa_estado_civil;
        }
        public void setPessoa_estado_civil(Integer pessoa_estado_civil) {
            this.pessoa_estado_civil = pessoa_estado_civil;
        }
        public String getPessoa_grau_de_instrucao() {
            return pessoa_grau_de_instrucao;
        }
        public void setPessoa_grau_de_instrucao(String pessoa_grau_de_instrucao) {
            this.pessoa_grau_de_instrucao = pessoa_grau_de_instrucao;
        }
        public String getPessoa_sigla() {
            return pessoa_sigla;
        }
        public void setPessoa_sigla(String pessoa_sigla) {
            this.pessoa_sigla = pessoa_sigla;
        }
        public String getPessoa_email() {
            return pessoa_email;
        }
        public void setPessoa_email(String pessoa_email) {
            this.pessoa_email = pessoa_email;
        }
        public String getTipo_rh() {
            return tipo_rh;
        }
        public void setTipo_rh(String tipo_rh) {
            this.tipo_rh = tipo_rh;
        }
        public String getPessoa_tp_sanguineo() {
            return pessoa_tp_sanguineo;
        }
        public void setPessoa_tp_sanguineo(String pessoa_tp_sanguineo) {
            this.pessoa_tp_sanguineo = pessoa_tp_sanguineo;
        }
        public String getPessoa_naturalidade() {
            return pessoa_naturalidade;
        }
        public void setPessoa_naturalidade(String pessoa_naturalidade) {
            this.pessoa_naturalidade = pessoa_naturalidade;
        }
        public String getPessoa_nacionalidade() {
            return pessoa_nacionalidade;
        }
        public void setPessoa_nacionalidade(String pessoa_nacionalidade) {
            this.pessoa_nacionalidade = pessoa_nacionalidade;
        }
        public Long getCargo_id() {
            return cargo_id;
        }
        public void setCargo_id(Long cargo_id) {
            this.cargo_id = cargo_id;
        }
        public Long getFuncao_id() {
            return funcao_id;
        }
        public void setFuncao_id(Long funcao_id) {
            this.funcao_id = funcao_id;
        }
        public Long getLotacao_id() {
            return lotacao_id;
        }
        public void setLotacao_id(Long lotacao_id) {
            this.lotacao_id = lotacao_id;
        }
        public String getLotacao_tipo_papel() {
            return lotacao_tipo_papel;
        }
        public void setLotacao_tipo_papel(String lotacao_tipo_papel) {
            this.lotacao_tipo_papel = lotacao_tipo_papel;
        }
	}

	public class Cargo {
		private Long cargo_id;
		private String cargo_nome;
		private String cargo_sigla;
        public Long getCargo_id() {
            return cargo_id;
        }
        public void setCargo_id(Long cargo_id) {
            this.cargo_id = cargo_id;
        }
        public String getCargo_nome() {
            return cargo_nome;
        }
        public void setCargo_nome(String cargo_nome) {
            this.cargo_nome = cargo_nome;
        }
        public String getCargo_sigla() {
            return cargo_sigla;
        }
        public void setCargo_sigla(String cargo_sigla) {
            this.cargo_sigla = cargo_sigla;
        }
	}

	public class Funcao {
		private Long funcao_id;
		private String funcao_nome;
		private String funcao_sigla;
        public Long getFuncao_id() {
            return funcao_id;
        }
        public void setFuncao_id(Long funcao_id) {
            this.funcao_id = funcao_id;
        }
        public String getFuncao_nome() {
            return funcao_nome;
        }
        public void setFuncao_nome(String funcao_nome) {
            this.funcao_nome = funcao_nome;
        }
        public String getFuncao_sigla() {
            return funcao_sigla;
        }
        public void setFuncao_sigla(String funcao_sigla) {
            this.funcao_sigla = funcao_sigla;
        }
	}

	public class Lotacao {
		private Long lotacao_id;
		private String lotacao_nome;
		private String lotacao_sigla;
		private Long lotacao_id_pai;
		private String lotacao_tipo;
		private String lotacao_tipo_papel;
        public String getLotacao_nome() {
            return lotacao_nome;
        }
        public void setLotacao_nome(String lotacao_nome) {
            this.lotacao_nome = lotacao_nome;
        }
        public Long getLotacao_id() {
            return lotacao_id;
        }
        public void setLotacao_id(Long lotacao_id) {
            this.lotacao_id = lotacao_id;
        }
        public String getLotacao_sigla() {
            return lotacao_sigla;
        }
        public void setLotacao_sigla(String lotacao_sigla) {
            this.lotacao_sigla = lotacao_sigla;
        }
        public Long getLotacao_id_pai() {
            return lotacao_id_pai;
        }
        public void setLotacao_id_pai(Long lotacao_id_pai) {
            this.lotacao_id_pai = lotacao_id_pai;
        }
        public String getLotacao_tipo() {
            return lotacao_tipo;
        }
        public void setLotacao_tipo(String lotacao_tipo) {
            this.lotacao_tipo = lotacao_tipo;
        }
        public String getLotacao_tipo_papel() {
            return lotacao_tipo_papel;
        }
        public void setLotacao_tipo_papel(String lotacao_tipo_papel) {
            this.lotacao_tipo_papel = lotacao_tipo_papel;
        }
	}
	
	public class Papel {
		private Long papel_id;
		private Long papel_lotacao_id;
		private Long papel_cargo_id;
		private Long papel_funcao_id;
		private String papel_lotacao_tipo;
		private Long papel_pessoa_id;
        public Long getPapel_id() {
            return papel_id;
        }
        public void setPapel_id(Long papel_id) {
            this.papel_id = papel_id;
        }
        public Long getPapel_lotacao_id() {
            return papel_lotacao_id;
        }
        public void setPapel_lotacao_id(Long papel_lotacao_id) {
            this.papel_lotacao_id = papel_lotacao_id;
        }
        public Long getPapel_cargo_id() {
            return papel_cargo_id;
        }
        public void setPapel_cargo_id(Long papel_cargo_id) {
            this.papel_cargo_id = papel_cargo_id;
        }
        public Long getPapel_funcao_id() {
            return papel_funcao_id;
        }
        public void setPapel_funcao_id(Long papel_funcao_id) {
            this.papel_funcao_id = papel_funcao_id;
        }
        public String getPapel_lotacao_tipo() {
            return papel_lotacao_tipo;
        }
        public void setPapel_lotacao_tipo(String papel_lotacao_tipo) {
            this.papel_lotacao_tipo = papel_lotacao_tipo;
        }
        public Long getPapel_pessoa_id() {
            return papel_pessoa_id;
        }
        public void setPapel_pessoa_id(Long papel_pessoa_id) {
            this.papel_pessoa_id = papel_pessoa_id;
        }
	}
	
	public class Orgao {
		private Long orgao_usu_id;

        public Long getOrgao_usu_id() {
            return orgao_usu_id;
        }

        public void setOrgao_usu_id(Long orgao_usu_id) {
            this.orgao_usu_id = orgao_usu_id;
        }
	}

	public Pessoa getPessoa() {
		if (getPessoa_id() == null)
			return null;
		Pessoa o = new Pessoa();
		o.setPessoa_id(getPessoa_id());
		o.setPessoa_nome(getPessoa_nome());
		o.setPessoa_cpf(getPessoa_cpf());
		o.setPessoa_sexo(getPessoa_sexo());
		o.setPessoa_data_nascimento(getPessoa_data_nascimento());
		o.setPessoa_rua(getPessoa_rua());
		o.setPessoa_bairro(getPessoa_bairro());
		o.setPessoa_cidade(getPessoa_cidade());
		o.setPessoa_uf(getPessoa_uf());
		o.setPessoa_cep(getPessoa_cep());
		o.setPessoa_matricula(getPessoa_matricula());
		o.setPessoa_data_inicio_exercicio(getPessoa_data_inicio_exercicio());
		o.setPessoa_ato_nomeacao(getPessoa_ato_nomeacao());
		o.setPessoa_data_nomeacao(getPessoa_data_nomeacao());
		o.setPessoa_dt_publ_nomeacao(getPessoa_dt_publ_nomeacao());
		o.setPessoa_data_posse(getPessoa_data_posse());
		o.setPessoa_padrao_referencia(getPessoa_padrao_referencia());
		o.setPessoa_situacao(getPessoa_situacao());
		o.setPessoa_rg(getPessoa_rg());
		o.setPessoa_rg_orgao(getPessoa_rg_orgao());
		o.setPessoa_rg_uf(getPessoa_rg_uf());
		o.setPessoa_data_expedicao_rg(getPessoa_data_expedicao_rg());
		o.setPessoa_estado_civil(getPessoa_estado_civil());
		o.setPessoa_grau_de_instrucao(getPessoa_grau_de_instrucao());
		o.setPessoa_sigla(getPessoa_sigla());
		o.setPessoa_email(getPessoa_email());
		o.setTipo_rh(getTipo_rh());
		o.setPessoa_tp_sanguineo(getPessoa_tp_sanguineo());
		o.setPessoa_naturalidade(getPessoa_naturalidade());
		o.setPessoa_nacionalidade(getPessoa_nacionalidade());
		o.setCargo_id(getCargo_id());
		o.setFuncao_id(getFuncao_id());
		o.setLotacao_id(getLotacao_id());
		o.setLotacao_tipo_papel(getLotacao_tipo_papel());
		return o;
	}

	public Cargo getCargo() {
		if (getCargo_id() == null)
			return null;
		Cargo o = new Cargo();
		o.setCargo_id(getCargo_id());
		o.setCargo_nome(getCargo_nome());
		o.setCargo_sigla(getCargo_sigla());
		return o;
	}

	public Funcao getFuncao() {
		if (getFuncao_id() == null)
			return null;
		Funcao o = new Funcao();
		o.setFuncao_id(getFuncao_id());
		o.setFuncao_nome(getFuncao_nome());
		o.setFuncao_sigla(getFuncao_sigla());
		return o;
	}

	public Lotacao getLotacao() {
		if (getLotacao_id() == null)
			return null;
		Lotacao o = new Lotacao();
		o.setLotacao_id(getLotacao_id());
		o.setLotacao_nome(getLotacao_nome());
		o.setLotacao_sigla(getLotacao_sigla());
		o.setLotacao_id_pai(getLotacao_id_pai());
		o.setLotacao_tipo(getLotacao_tipo());
		o.setLotacao_tipo_papel(getLotacao_tipo_papel());
		return o;
	}
	
	public Papel getPapel() {
		if (getPapel_id() == null)
			return null;
		Papel o = new Papel();
		o.setPapel_pessoa_id(getPessoa_id());
		o.setPapel_id(getPapel_id());
		o.setPapel_lotacao_id(getLotacao_id());
		o.setPapel_cargo_id(getCargo_id());
		o.setPapel_funcao_id(getFuncao_id());
		o.setPapel_lotacao_tipo(getLotacao_tipo_papel());
		return o;
	}
	
	public Orgao getOrgao() {
		Orgao org = new Orgao();
//		org.orgao_usu_id = orgao_usu_id;
		return org;
	}

    protected Long getId() {
        return this.getPessoa_id();
    }

    public Long getPessoa_id() {
        return pessoa_id;
    }

    public void setPessoa_id(Long pessoa_id) {
        this.pessoa_id = pessoa_id;
    }

    public String getPessoa_cpf() {
        return pessoa_cpf;
    }

    public void setPessoa_cpf(String pessoa_cpf) {
        this.pessoa_cpf = pessoa_cpf;
    }

    public String getPessoa_nome() {
        return pessoa_nome;
    }

    public void setPessoa_nome(String pessoa_nome) {
        this.pessoa_nome = pessoa_nome;
    }

    public String getPessoa_sexo() {
        return pessoa_sexo;
    }

    public void setPessoa_sexo(String pessoa_sexo) {
        this.pessoa_sexo = pessoa_sexo;
    }

    public Date getPessoa_data_nascimento() {
        return pessoa_data_nascimento;
    }

    public void setPessoa_data_nascimento(Date pessoa_data_nascimento) {
        this.pessoa_data_nascimento = pessoa_data_nascimento;
    }

    public String getPessoa_rua() {
        return pessoa_rua;
    }

    public void setPessoa_rua(String pessoa_rua) {
        this.pessoa_rua = pessoa_rua;
    }

    public String getPessoa_bairro() {
        return pessoa_bairro;
    }

    public void setPessoa_bairro(String pessoa_bairro) {
        this.pessoa_bairro = pessoa_bairro;
    }

    public String getPessoa_cidade() {
        return pessoa_cidade;
    }

    public void setPessoa_cidade(String pessoa_cidade) {
        this.pessoa_cidade = pessoa_cidade;
    }

    public String getPessoa_uf() {
        return pessoa_uf;
    }

    public void setPessoa_uf(String pessoa_uf) {
        this.pessoa_uf = pessoa_uf;
    }

    public String getPessoa_cep() {
        return pessoa_cep;
    }

    public void setPessoa_cep(String pessoa_cep) {
        this.pessoa_cep = pessoa_cep;
    }

    public Long getPessoa_matricula() {
        return pessoa_matricula;
    }

    public void setPessoa_matricula(Long pessoa_matricula) {
        this.pessoa_matricula = pessoa_matricula;
    }

    public Date getPessoa_data_inicio_exercicio() {
        return pessoa_data_inicio_exercicio;
    }

    public void setPessoa_data_inicio_exercicio(Date pessoa_data_inicio_exercicio) {
        this.pessoa_data_inicio_exercicio = pessoa_data_inicio_exercicio;
    }

    public String getPessoa_ato_nomeacao() {
        return pessoa_ato_nomeacao;
    }

    public void setPessoa_ato_nomeacao(String pessoa_ato_nomeacao) {
        this.pessoa_ato_nomeacao = pessoa_ato_nomeacao;
    }

    public Date getPessoa_data_nomeacao() {
        return pessoa_data_nomeacao;
    }

    public void setPessoa_data_nomeacao(Date pessoa_data_nomeacao) {
        this.pessoa_data_nomeacao = pessoa_data_nomeacao;
    }

    public Date getPessoa_dt_publ_nomeacao() {
        return pessoa_dt_publ_nomeacao;
    }

    public void setPessoa_dt_publ_nomeacao(Date pessoa_dt_publ_nomeacao) {
        this.pessoa_dt_publ_nomeacao = pessoa_dt_publ_nomeacao;
    }

    public Date getPessoa_data_posse() {
        return pessoa_data_posse;
    }

    public void setPessoa_data_posse(Date pessoa_data_posse) {
        this.pessoa_data_posse = pessoa_data_posse;
    }

    public String getPessoa_padrao_referencia() {
        return pessoa_padrao_referencia;
    }

    public void setPessoa_padrao_referencia(String pessoa_padrao_referencia) {
        this.pessoa_padrao_referencia = pessoa_padrao_referencia;
    }

    public Integer getPessoa_situacao() {
        return pessoa_situacao;
    }

    public void setPessoa_situacao(Integer pessoa_situacao) {
        this.pessoa_situacao = pessoa_situacao;
    }

    public String getPessoa_rg() {
        return pessoa_rg;
    }

    public void setPessoa_rg(String pessoa_rg) {
        this.pessoa_rg = pessoa_rg;
    }

    public String getPessoa_rg_orgao() {
        return pessoa_rg_orgao;
    }

    public void setPessoa_rg_orgao(String pessoa_rg_orgao) {
        this.pessoa_rg_orgao = pessoa_rg_orgao;
    }

    public String getPessoa_rg_uf() {
        return pessoa_rg_uf;
    }

    public void setPessoa_rg_uf(String pessoa_rg_uf) {
        this.pessoa_rg_uf = pessoa_rg_uf;
    }

    public Date getPessoa_data_expedicao_rg() {
        return pessoa_data_expedicao_rg;
    }

    public void setPessoa_data_expedicao_rg(Date pessoa_data_expedicao_rg) {
        this.pessoa_data_expedicao_rg = pessoa_data_expedicao_rg;
    }

    public Integer getPessoa_estado_civil() {
        return pessoa_estado_civil;
    }

    public void setPessoa_estado_civil(Integer pessoa_estado_civil) {
        this.pessoa_estado_civil = pessoa_estado_civil;
    }

    public String getPessoa_grau_de_instrucao() {
        return pessoa_grau_de_instrucao;
    }

    public void setPessoa_grau_de_instrucao(String pessoa_grau_de_instrucao) {
        this.pessoa_grau_de_instrucao = pessoa_grau_de_instrucao;
    }

    public String getPessoa_sigla() {
        return pessoa_sigla;
    }

    public void setPessoa_sigla(String pessoa_sigla) {
        this.pessoa_sigla = pessoa_sigla;
    }

    public String getPessoa_email() {
        return pessoa_email;
    }

    public void setPessoa_email(String pessoa_email) {
        this.pessoa_email = pessoa_email;
    }

    public String getTipo_rh() {
        return tipo_rh;
    }

    public void setTipo_rh(String tipo_rh) {
        this.tipo_rh = tipo_rh;
    }

    public String getPessoa_tp_sanguineo() {
        return pessoa_tp_sanguineo;
    }

    public void setPessoa_tp_sanguineo(String pessoa_tp_sanguineo) {
        this.pessoa_tp_sanguineo = pessoa_tp_sanguineo;
    }

    public String getPessoa_naturalidade() {
        return pessoa_naturalidade;
    }

    public void setPessoa_naturalidade(String pessoa_naturalidade) {
        this.pessoa_naturalidade = pessoa_naturalidade;
    }

    public String getPessoa_nacionalidade() {
        return pessoa_nacionalidade;
    }

    public void setPessoa_nacionalidade(String pessoa_nacionalidade) {
        this.pessoa_nacionalidade = pessoa_nacionalidade;
    }

    public Long getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Long cargo_id) {
        this.cargo_id = cargo_id;
    }

    public String getCargo_nome() {
        return cargo_nome;
    }

    public void setCargo_nome(String cargo_nome) {
        this.cargo_nome = cargo_nome;
    }

    public String getCargo_sigla() {
        return cargo_sigla;
    }

    public void setCargo_sigla(String cargo_sigla) {
        this.cargo_sigla = cargo_sigla;
    }

    public Long getFuncao_id() {
        return funcao_id;
    }

    public void setFuncao_id(Long funcao_id) {
        this.funcao_id = funcao_id;
    }

    public String getFuncao_nome() {
        return funcao_nome;
    }

    public void setFuncao_nome(String funcao_nome) {
        this.funcao_nome = funcao_nome;
    }

    public String getFuncao_sigla() {
        return funcao_sigla;
    }

    public void setFuncao_sigla(String funcao_sigla) {
        this.funcao_sigla = funcao_sigla;
    }

    public Long getLotacao_id() {
        return lotacao_id;
    }

    public void setLotacao_id(Long lotacao_id) {
        this.lotacao_id = lotacao_id;
    }

    public String getLotacao_nome() {
        return lotacao_nome;
    }

    public void setLotacao_nome(String lotacao_nome) {
        this.lotacao_nome = lotacao_nome;
    }

    public String getLotacao_sigla() {
        return lotacao_sigla;
    }

    public void setLotacao_sigla(String lotacao_sigla) {
        this.lotacao_sigla = lotacao_sigla;
    }

    public Long getLotacao_id_pai() {
        return lotacao_id_pai;
    }

    public void setLotacao_id_pai(Long lotacao_id_pai) {
        this.lotacao_id_pai = lotacao_id_pai;
    }

    public String getLotacao_tipo() {
        return lotacao_tipo;
    }

    public void setLotacao_tipo(String lotacao_tipo) {
        this.lotacao_tipo = lotacao_tipo;
    }

    public String getLotacao_tipo_papel() {
        return lotacao_tipo_papel;
    }

    public void setLotacao_tipo_papel(String lotacao_tipo_papel) {
        this.lotacao_tipo_papel = lotacao_tipo_papel;
    }

    public Long getPapel_id() {
        return papel_id;
    }

    public void setPapel_id(Long papel_id) {
        this.papel_id = papel_id;
    }

}
