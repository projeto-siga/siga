package br.gov.jfrj.siga.tp.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.gov.jfrj.siga.tp.exceptions.RequisicaoTransporteException;
import br.gov.jfrj.siga.tp.model.FinalidadeRequisicao;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.model.TipoDePassageiro;

public class RequisicaoTransporteRest {

    private static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
    private RequisicaoTransporte requisicaoTransporte;

    public Long getId() {
        return getRequisicaoTransporte().getId();
    }

    public String getSequence() {
        return getRequisicaoTransporte().buscarSequence();
    }

    public String getDataHoraSaidaPrevista() {
        SimpleDateFormat formatar = new SimpleDateFormat(DD_MM_YYYY_HH_MM);
        return formatar.format(getRequisicaoTransporte().getDataHoraSaidaPrevista().getTime());
    }

    public String getDataHoraRetornoPrevisto() {
        if (getRequisicaoTransporte().getDataHoraRetornoPrevisto() == null) {
            return null;
        }
        SimpleDateFormat formatar = new SimpleDateFormat(DD_MM_YYYY_HH_MM);
        return formatar.format(getRequisicaoTransporte().getDataHoraRetornoPrevisto().getTime());
    }

    public String getFinalidade() {
        return getRequisicaoTransporte().getTipoFinalidade().getDescricao();
    }

    public static RequisicaoTransporteRest recuperar(Long id) throws RequisicaoTransporteException {
        try {
            RequisicaoTransporteRest retorno = new RequisicaoTransporteRest();
            retorno.setRequisicaoTransporte(RequisicaoTransporte.AR.findById(id));
            return retorno;
        } catch (Exception e) {
            throw new RequisicaoTransporteException(e);
        }
    }

    public static RequisicaoTransporteRest recuperarPelaSequence(String sequence) throws RequisicaoTransporteException {
        try {
            RequisicaoTransporteRest retorno = new RequisicaoTransporteRest();
            retorno.setRequisicaoTransporte(RequisicaoTransporte.buscar(sequence));
            return retorno;
        } catch (Exception e) {
            throw new RequisicaoTransporteException(e);
        }
    }

    public static RequisicaoTransporteRest recuperar(String sequence) throws RequisicaoTransporteException {
        try {
            RequisicaoTransporteRest retorno = new RequisicaoTransporteRest();
            retorno.setRequisicaoTransporte(RequisicaoTransporte.buscar(sequence));
            return retorno;
        } catch (Exception e) {
            throw new RequisicaoTransporteException(e);
        }
    }

    public String getDetalhesFinalidade() {
        return getRequisicaoTransporte().getFinalidade();
    }

    public String getPassageiros() {
        return getRequisicaoTransporte().getPassageiros();
    }

    public String[] getTiposDePassageiros() {
        List<String> retorno = new ArrayList<String>();
        for (Iterator<TipoDePassageiro> it = getRequisicaoTransporte().getTiposDePassageiro().iterator(); it.hasNext();) {
            TipoDePassageiro tp = it.next();
            retorno.add(tp.name());
        }
        return retorno.toArray(new String[retorno.size()]);
    }

    public Integer getNumeroDePassageiros() {
        return getRequisicaoTransporte().getNumeroDePassageiros();
    }

    public String getUltimoAndamento() {
        return getRequisicaoTransporte().getUltimoAndamento().getEstadoRequisicao().getDescricao();
    }

    public Long getIdInicialDpPessoaSolicitante() {
        return getRequisicaoTransporte().getSolicitante().getIdInicial();
    }

    // fim proxy propriedades

    // inicio procedimentos

    public static void converterParaRequisicao(RequisicaoTransporte requisicaoAPreencher, Map<String, String> map) throws RequisicaoTransporteException {
        try {
            requisicaoAPreencher.setDataHoraSaidaPrevista(converterParaData(map.get("dataHoraSaidaPrevista")));
            requisicaoAPreencher.setDataHoraRetornoPrevisto(converterParaData(map.get("dataHoraRetornoPrevisto")));
            requisicaoAPreencher.setTipoFinalidade(FinalidadeRequisicao.buscar(map.get("finalidade")));
            requisicaoAPreencher.setFinalidade(map.get("detalhesFinalidade"));
            requisicaoAPreencher.setItinerarios(map.get("itinerarios"));
            requisicaoAPreencher.setTiposDePassageiro(converterParaTiposPassageiros(tratarTiposPassageiros(map.get("tiposDePassageiros"))));
            requisicaoAPreencher.setNumeroDePassageiros(Integer.parseInt(map.get("numeroDePassageiros")));
            requisicaoAPreencher.setPassageiros(map.get("passageiros"));

            if (requisicaoAPreencher.getId().equals(Long.valueOf(0))) {
                requisicaoAPreencher.setIdSolicitante(Long.parseLong(map.get("idInicialDpPessoaSolicitante")));
            }

            requisicaoAPreencher.setOrigemExterna(true);
        } catch (Exception e) {
            throw new RequisicaoTransporteException(e);
        }
    }

    private static List<TipoDePassageiro> converterParaTiposPassageiros(String[] tipos) {
        List<TipoDePassageiro> retorno = new ArrayList<TipoDePassageiro>();

        for (int i = 0; i < tipos.length; i++) {
            retorno.add(TipoDePassageiro.valueOf(tipos[i]));
        }

        return retorno;
    }

    private static Calendar converterParaData(String dataTexto) throws ParseException {
        if ((dataTexto == null) || ("null".equals(dataTexto))) {
            return null;
        }
        Calendar retorno = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat(DD_MM_YYYY_HH_MM);
        retorno.setTime(formato.parse(dataTexto));

        return retorno;
    }

    private static String[] tratarTiposPassageiros(String tipos) {
        return tipos.replace("[", "").replace("]", "").replace(" ", "").replace("\"", "").split(",");
    }

    public static RequisicaoTransporteRest recuperarEConverter(long idABuscar, Map<String, String> map) throws RequisicaoTransporteException {
        try {
            RequisicaoTransporte requisicaoARecuperar = recuperar(idABuscar).getRequisicaoTransporte();
            converterParaRequisicao(requisicaoARecuperar, map);
            RequisicaoTransporteRest retorno = new RequisicaoTransporteRest();
            retorno.setRequisicaoTransporte(requisicaoARecuperar);
            return retorno;
        } catch (Exception e) {
            throw new RequisicaoTransporteException(e);
        }
    }

    public static RequisicaoTransporte recuperarRequisicao(RequisicaoTransporteRest entrada) {
        return entrada.getRequisicaoTransporte();
    }

    public RequisicaoTransporte getRequisicaoTransporte() {
        return requisicaoTransporte;
    }

    public void setRequisicaoTransporte(RequisicaoTransporte requisicaoTransporte) {
        this.requisicaoTransporte = requisicaoTransporte;
    }

}
