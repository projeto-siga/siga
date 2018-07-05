package br.gov.jfrj.siga.tp.model;

import java.util.Calendar;
import java.util.List;

import javax.validation.constraints.NotNull;

public class RelatorioRanking {
	@NotNull
	private Calendar dataInicio;

	private Calendar dataFim;

	@NotNull
	private int quantidadeDadosRetorno;

	public int getQuantidadeDadosRetorno() {
		return quantidadeDadosRetorno;
	}

	public Calendar getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Calendar dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Calendar getDataFim() {
		return dataFim;
	}

	public void setDataFim(Calendar dataFim) {
		this.dataFim = dataFim;
	}

	public void setQuantidadeDadosRetorno(int quantidadeDadosRetorno) {
		this.quantidadeDadosRetorno = quantidadeDadosRetorno;
	}

	public class RankingCondutorRequisicao implements Comparable<RankingCondutorRequisicao> {
		private Condutor condutor;
		private List<Missao> missoes;
		private List<RequisicaoTransporte> requisicoes;

		@Override
		public int compareTo(RankingCondutorRequisicao o) {
			if (!this.getCondutor().equals(o.getCondutor())) {
				if (this.getMissoes().size() == o.getMissoes().size()) {
					return (this.getRequisicoes().size() > o.getRequisicoes().size()) ? -1 : 1;
				} else {
					return (this.getMissoes().size() > o.getMissoes().size()) ? -1 : 1;
				}
			}
			return 0;
		}

        public Condutor getCondutor() {
            return condutor;
        }

        public void setCondutor(Condutor condutor) {
            this.condutor = condutor;
        }

        public List<Missao> getMissoes() {
            return missoes;
        }

        public void setMissoes(List<Missao> missoes) {
            this.missoes = missoes;
        }

        public List<RequisicaoTransporte> getRequisicoes() {
            return requisicoes;
        }

        public void setRequisicoes(List<RequisicaoTransporte> requisicoes) {
            this.requisicoes = requisicoes;
        }
	}

	public class RankingVeiculoRequisicao implements Comparable<RankingVeiculoRequisicao> {
		private Veiculo veiculo;

		private List<RequisicaoTransporte> requisicoes;

		@Override
		public int compareTo(RankingVeiculoRequisicao o) {
			if (!this.getVeiculo().equals(o.getVeiculo())) {
				return (this.getRequisicoes().size() > o.getRequisicoes().size()) ? -1 : 1;
			}
			return 0;
		}

        public Veiculo getVeiculo() {
            return veiculo;
        }

        public void setVeiculo(Veiculo veiculo) {
            this.veiculo = veiculo;
        }

        public List<RequisicaoTransporte> getRequisicoes() {
            return requisicoes;
        }

        public void setRequisicoes(List<RequisicaoTransporte> requisicoes) {
            this.requisicoes = requisicoes;
        }
	}

	public class RankingFinalidadeRequisicao  {
		private FinalidadeRequisicao finalidade;
		private int totalFinalidade;

        public FinalidadeRequisicao getFinalidade() {
            return finalidade;
        }

        public void setFinalidade(FinalidadeRequisicao finalidade) {
            this.finalidade = finalidade;
        }

        public int getTotalFinalidade() {
            return totalFinalidade;
        }

        public void setTotalFinalidade(int totalFinalidade) {
            this.totalFinalidade = totalFinalidade;
        }
	}

	public class RankingTipoPassageiroRequisicao implements Comparable<RankingTipoPassageiroRequisicao> {
		private String tipoPassageiro;
		private int totalTipoPassageiros;

		@Override
		public int compareTo(RankingTipoPassageiroRequisicao o)  {
			if (!this.getTipoPassageiro().equals(o.getTipoPassageiro())) {
				return (this.getTotalTipoPassageiros() > o.getTotalTipoPassageiros()) ? -1 : 1;
			}
			return 0;
		}

        public String getTipoPassageiro() {
            return tipoPassageiro;
        }

        public void setTipoPassageiro(String tipoPassageiro) {
            this.tipoPassageiro = tipoPassageiro;
        }

        public int getTotalTipoPassageiros() {
            return totalTipoPassageiros;
        }

        public void setTotalTipoPassageiros(int totalTipoPassageiros) {
            this.totalTipoPassageiros = totalTipoPassageiros;
        }
	}
}