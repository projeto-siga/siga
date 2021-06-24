package br.gov.jfrj.siga.ex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;

public class ExConfiguracaoDTO {
	
	private Long[] modelos, orgaos, unidades, cargos, funcoes, pessoas;
	private Integer tipoConfiguracao;
	private Long movimentacao;
	private ExConfiguracaoDestinatarios destinatarios;
	private ExConfiguracaoVisibilidade visibilidade;
		
	public Long[] getModelos() {
		return modelos;
	}

	public void setModelos(Long[] modelos) {
		this.modelos = modelos;
	}

	public Long[] getOrgaos() {
		return orgaos;
	}

	public void setOrgaos(Long[] orgaos) {
		this.orgaos = orgaos;
	}

	public Long[] getUnidades() {
		return unidades;
	}

	public void setUnidades(Long[] unidades) {
		this.unidades = unidades;
	}

	public Long[] getCargos() {
		return cargos;
	}

	public void setCargos(Long[] cargos) {
		this.cargos = cargos;
	}

	public Long[] getFuncoes() {
		return funcoes;
	}

	public void setFuncoes(Long[] funcoes) {
		this.funcoes = funcoes;
	}

	public Long[] getPessoas() {
		return pessoas;
	}

	public void setPessoas(Long[] pessoas) {
		this.pessoas = pessoas;
	}

	public Integer getTipoConfiguracao() {
		return tipoConfiguracao;
	}

	public void setTipoConfiguracao(Integer tipoConfiguracao) {
		this.tipoConfiguracao = tipoConfiguracao;
	}

	public Long getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Long movimentacao) {
		this.movimentacao = movimentacao;
	}

	public ExConfiguracaoDestinatarios getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(ExConfiguracaoDestinatarios destinatarios) {
		this.destinatarios = destinatarios;
	}

	public ExConfiguracaoVisibilidade getVisibilidade() {
		return visibilidade;
	}

	public void setVisibilidade(ExConfiguracaoVisibilidade visibilidade) {
		this.visibilidade = visibilidade;
	}	
	
	public List<Long> getIdModelos() {
		List<Long> idModelos = new ArrayList<>();
		Stream<Long> streamModelos = Arrays.stream(modelos);		
		streamModelos.forEach(m -> idModelos.add(m));
		return idModelos;		
	}
	
	public List<Long> getIdOrgaos() {
		List<Long> idOrgaos = new ArrayList<>();
		Stream<Long> streamOrgaos = Arrays.stream(orgaos);		
		streamOrgaos.forEach(o -> idOrgaos.add(o));
		return idOrgaos;		
	}
	
	public List<Long> getIdUnidades() {
		List<Long> idUnidades = new ArrayList<>();
		Stream<Long> streamUnidades = Arrays.stream(unidades);		
		streamUnidades.forEach(u -> idUnidades.add(u));
		return idUnidades;		
	}
	
	public List<Long> getIdCargos() {
		List<Long> idCargos = new ArrayList<>();
		Stream<Long> streamCargos = Arrays.stream(cargos);		
		streamCargos.forEach(c -> idCargos.add(c));
		return idCargos;		
	}
	
	public List<Long> getIdFuncoes() {
		List<Long> idFuncoes = new ArrayList<>();
		Stream<Long> streamFuncoes = Arrays.stream(funcoes);		
		streamFuncoes.forEach(f -> idFuncoes.add(f));
		return idFuncoes;		
	}
	
	public List<Long> getIdPessoas() {
		List<Long> idPessoas = new ArrayList<>();
		Stream<Long> streamPessoas = Arrays.stream(pessoas);		
		streamPessoas.forEach(p -> idPessoas.add(p));
		return idPessoas;		
	}
	
	public int getIdSituacao() {
		return visibilidade.obterIdSituacao();
	}
	
	public DpLotacaoSelecao getLotacaoSelecao(Long idUnidade) {
		DpLotacaoSelecao lotacaoSelecao = new DpLotacaoSelecao();
		lotacaoSelecao.setId(idUnidade);
		return lotacaoSelecao;
	}
	
	public DpCargoSelecao getCargoSelecao(Long idCargo) {
		DpCargoSelecao cargoSelecao = new DpCargoSelecao();
		cargoSelecao.setId(idCargo);
		return cargoSelecao;
	}
	
	public DpFuncaoConfiancaSelecao getFuncaoSelecao(Long idFuncao) {
		DpFuncaoConfiancaSelecao funcaoSelecao = new DpFuncaoConfiancaSelecao();
		funcaoSelecao.setId(idFuncao);
		return funcaoSelecao;
	}
	
	
	public DpPessoaSelecao getPessoaSel(Long idPessoa) {
		DpPessoaSelecao pessoaSelecao = new DpPessoaSelecao();
		pessoaSelecao.setId(idPessoa);
		return pessoaSelecao;
	}

}
