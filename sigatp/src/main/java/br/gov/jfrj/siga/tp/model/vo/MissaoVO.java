package br.gov.jfrj.siga.tp.model.vo;

import java.util.ArrayList;
import java.util.List;

public class MissaoVO {
    public List<ItemVO> veiculos = new ArrayList<>();
    public List<ItemVO> condutores = new ArrayList<>();
    public boolean disabled;
}
