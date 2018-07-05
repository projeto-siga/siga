package br.gov.jfrj.siga.tp.dto;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.Plantao;

public class PlantaoDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantaoDTO.class);

    private Long id;

    private Condutor condutor;

    private Calendar dataHoraInicio;

    private Calendar dataHoraFim;

    private String referencia;

    public Plantao buscarPlantao() {
        if (this.id == null || this.id == 0L) {
            return novoPlantao();
        } else {
            try {
                Plantao plantao = Plantao.AR.findById(this.id);
                preencherDadosComuns(plantao);
                return plantao;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                return null;
            }
        }
    }

    private Plantao novoPlantao() {
        Plantao plantao = new Plantao();
        plantao.setId(id);
        preencherDadosComuns(plantao);
        return plantao;
    }

    private void preencherDadosComuns(Plantao plantao) {
        plantao.setDataHoraFim(dataHoraFim);
        plantao.setDataHoraInicio(dataHoraInicio);
        plantao.setReferencia(referencia);
        plantao.setCondutor(recuperarCondutor());
    }

    private Condutor recuperarCondutor() {
        if (this.condutor == null || this.condutor.getId() == null || this.condutor.getId() == 0L) {
            return null;
        } else {
            try {
                return Condutor.AR.findById(this.condutor.getId());
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                return null;
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Condutor getCondutor() {
        return condutor;
    }

    public void setCondutor(Condutor condutor) {
        this.condutor = condutor;
    }

    public Calendar getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(Calendar dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public Calendar getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Calendar dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

}
