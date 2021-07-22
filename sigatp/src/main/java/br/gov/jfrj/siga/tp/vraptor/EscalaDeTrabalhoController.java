package br.gov.jfrj.siga.tp.vraptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.DiaDaSemana;
import br.gov.jfrj.siga.tp.model.DiaDeTrabalho;
import br.gov.jfrj.siga.tp.model.EscalaDeTrabalho;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/escalaDeTrabalho")
public class EscalaDeTrabalhoController extends TpController {

	private static final String ESCALAS = "escalas";
    private static final String ESCALA_STR = "escala";
    private static final String DIAS_DE_TRABALHO = "diasDeTrabalho";
    private static final String DIA_SEMANA = "diaSemana";
    private static final String MODO = "modo";
	private static final String LABEL_EDITAR = "views.label.editar";
	private static final String LABEL_INCLUIR = "views.label.incluir";
	private static final String PATTERN_DDMMYYYYHHMM = "dd/MM/yyyy HH:mm";
	private static final String PATTERN_DDMMYYYYHHMM_MYSQL = "yyyy-MM-dd HH:mm";

	private MissaoController missaoController;

	/**
	 * @deprecated CDI eyes only
	 */
	public EscalaDeTrabalhoController() {
		super();
	}
	
	@Inject
	public EscalaDeTrabalhoController(HttpServletRequest request, Result result,   Validator validator, SigaObjects so,  EntityManager em, MissaoController missaoController) {
		super(request, result, TpDao.getInstance(), validator, so, em);

		this.missaoController = missaoController;
	}

	@Path("/listar")
	public void listar() {
    	result.include(ESCALAS, EscalaDeTrabalho.buscarTodasVigentes());
    }

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/incluir/{idCondutor}")
    public void incluir(Long idCondutor) throws Exception {
		MenuMontador.instance(result).recuperarMenuCondutores(idCondutor, ItemMenu.ESCALASDETRABALHO);
    	Condutor condutor = Condutor.AR.findById(idCondutor);

    	EscalaDeTrabalho escala = new EscalaDeTrabalho();
    	escala.setCondutor(condutor);

    	DiaDaSemana diaSemana = DiaDaSemana.SEGUNDA;
    	DiaDeTrabalho diaTrabalho = new DiaDeTrabalho();
    	escala.getDiasDeTrabalho().add(diaTrabalho);

    	result.include(MODO, LABEL_INCLUIR);
    	result.include(ESCALA_STR, escala);
    	result.include(DIA_SEMANA, diaSemana);

    	result.use(Results.page()).of(EscalaDeTrabalhoController.class).editar(null);
	}

	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/editar/{id}")
    public void editar(final Long id) throws Exception {
		EscalaDeTrabalho escala = EscalaDeTrabalho.AR.findById(id);
    	MenuMontador.instance(result).recuperarMenuCondutores(escala.getCondutor().getId(), ItemMenu.ESCALASDETRABALHO);
    	DiaDaSemana diaSemana = DiaDaSemana.SEGUNDA;

    	result.include(MODO, LABEL_EDITAR);
    	result.include(ESCALA_STR, escala);
    	result.include(DIA_SEMANA, diaSemana);
    }

	@Path("/listarPorCondutor/{id}")
	public void listarPorCondutor(Long id) throws Exception {
    	MenuMontador.instance(result).recuperarMenuCondutores(id, ItemMenu.ESCALASDETRABALHO);
        Condutor condutor = Condutor.AR.findById(id);

        List<EscalaDeTrabalho> escalas = EscalaDeTrabalho.buscarTodosPorCondutor(condutor);

        EscalaDeTrabalho escala = new EscalaDeTrabalho();
        escala.setCondutor(condutor);
    	DiaDaSemana diaSemana = DiaDaSemana.SEGUNDA;
    	escala.setDataVigenciaInicio(Calendar.getInstance());
    	escala.getDataVigenciaInicio().set(Calendar.HOUR_OF_DAY, 0);
    	escala.getDataVigenciaInicio().set(Calendar.MINUTE, 0);
    	escala.getDataVigenciaInicio().set(Calendar.SECOND, 0);
    	DiaDeTrabalho diaTrabalho = new DiaDeTrabalho();
    	escala.getDiasDeTrabalho().add(diaTrabalho);                    
    	
    	for(EscalaDeTrabalho escalaVigente : escalas){
	    	if (!escalas.isEmpty() && (escalaVigente.getDataVigenciaFim() == null )) {
	        	escala = escalaVigente;	            		
	        }
	    }      
           	
        result.include(ESCALAS, escalas);
        result.include("condutor", condutor);
        result.include(ESCALA_STR, escala);
        result.include(DIA_SEMANA, diaSemana);
     }

    @Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/finalizar")
    public void finalizar(EscalaDeTrabalho escalaDeTrabalho) throws Exception {
		escalaDeTrabalho.setDataVigenciaFim(Calendar.getInstance());
    	escalaDeTrabalho.getDataVigenciaFim().set(Calendar.HOUR_OF_DAY, 23);
    	escalaDeTrabalho.getDataVigenciaFim().set(Calendar.MINUTE, 59);
    	escalaDeTrabalho.getDataVigenciaFim().set(Calendar.SECOND, 59);
		escalaDeTrabalho.save();

		result.redirectTo(this).listarPorCondutor(escalaDeTrabalho.getCondutor().getId());
	}

    @Transactional
    @SuppressWarnings("static-access")
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/salvar")
	public void salvar(EscalaDeTrabalho escalaDeTrabalho, List<DiaDeTrabalho> diasDeTrabalho) throws Exception {
        		
		if(!validator.hasErrors()) {
        	error(diasDeTrabalho == null || diasDeTrabalho.isEmpty(), DIAS_DE_TRABALHO, "escalasDeTrabalho.diaDaSemana.validation");
        }

        EscalaDeTrabalho escalaAntiga = new EscalaDeTrabalho();
        escalaAntiga.setDiasDeTrabalho(new ArrayList<DiaDeTrabalho>());
        escalaAntiga.getDiasDeTrabalho().addAll(escalaDeTrabalho.getDiasDeTrabalho());
        escalaAntiga.setId(escalaDeTrabalho.getId());
        

        if(!validator.hasErrors()) {
	        escalaDeTrabalho.setDiasDeTrabalho(new ArrayList<DiaDeTrabalho>());
	        escalaDeTrabalho.getDiasDeTrabalho().addAll(diasDeTrabalho);
        }

        if(!validator.hasErrors()) {
        	validarEscala(escalaDeTrabalho.getDiasDeTrabalho());
        }

        if(!validator.hasErrors()) {
        	validarMissoesParaNovaEscala(escalaDeTrabalho);
        }

        if(validator.hasErrors()) {
        	MenuMontador.instance(result).recuperarMenuCondutores(escalaDeTrabalho.getCondutor().getId(), ItemMenu.ESCALASDETRABALHO);

            Condutor condutor = escalaDeTrabalho.getCondutor();

        	List<EscalaDeTrabalho> escalas = EscalaDeTrabalho.buscarTodosPorCondutor(condutor);

        	for (EscalaDeTrabalho escala : escalas) {
        		if (isMesmaEscala(escalaDeTrabalho, escala) && isMesmosDiasDeTrabalho(escalaAntiga.getDiasDeTrabalho(), diasDeTrabalho)) {
        			escala.setDiasDeTrabalho(escalaAntiga.getDiasDeTrabalho());
        		}
			}

        	result.include(ESCALAS, escalas);
            result.include("condutor", condutor);
            result.include(ESCALA_STR, escalaDeTrabalho);
            result.include(DIA_SEMANA, DiaDaSemana.SEGUNDA);

            validator.onErrorUse(Results.logic()).forwardTo(EscalaDeTrabalhoController.class).listarPorCondutor(condutor.getId());
        }
      
        if (ehMesmoDia(escalaDeTrabalho.getDataVigenciaInicio(), Calendar.getInstance())) {
        	if (escalaDeTrabalho.getDataVigenciaFim() == null) {
        		if (escalaAntiga.getId() > 0) {
	        		Object[] escalas = new Object[1];
	        		escalas[0] = escalaAntiga;
	        		DiaDeTrabalho.AR.delete("escalaDeTrabalho", escalas);	        	
        		}	        	
        		            	
        		escalaDeTrabalho.save();            	        		
        		
        		for (DiaDeTrabalho diaDeTrabalho : escalaDeTrabalho.getDiasDeTrabalho()) {
		                DiaDeTrabalho diaDeTrabalhoNovo = new DiaDeTrabalho();
		                diaDeTrabalhoNovo.setDiaEntrada(diaDeTrabalho.getDiaEntrada());
		                diaDeTrabalhoNovo.setDiaSaida(diaDeTrabalho.getDiaSaida());
		                diaDeTrabalhoNovo.setHoraEntrada(diaDeTrabalho.getHoraEntrada());
		                diaDeTrabalhoNovo.setHoraSaida(diaDeTrabalho.getHoraSaida());
		                diaDeTrabalhoNovo.setEscalaDeTrabalho(escalaDeTrabalho);
	
		                diaDeTrabalhoNovo.save();
	    		}        		
        	 } 
        } else {
        	escalaDeTrabalho.setDataVigenciaFim(Calendar.getInstance());
        	escalaDeTrabalho.getDataVigenciaFim().add(Calendar.DATE,-1);
        	escalaDeTrabalho.getDataVigenciaFim().set(Calendar.HOUR_OF_DAY, 23);
        	escalaDeTrabalho.getDataVigenciaFim().set(Calendar.MINUTE, 59);
        	escalaDeTrabalho.getDataVigenciaFim().set(Calendar.SECOND, 59);
        	           	        	
        	EscalaDeTrabalho.AR.em().detach(escalaDeTrabalho);        	
        	
        	escalaDeTrabalho.AR.em().merge(escalaDeTrabalho);
            
        	EscalaDeTrabalho novaEscala = new EscalaDeTrabalho();
        	novaEscala.setCondutor(escalaDeTrabalho.getCondutor());
        	novaEscala.setDataVigenciaInicio(Calendar.getInstance());
        	novaEscala.getDataVigenciaInicio().set(Calendar.HOUR_OF_DAY, 0);
        	novaEscala.getDataVigenciaInicio().set(Calendar.MINUTE, 0);
        	novaEscala.getDataVigenciaInicio().set(Calendar.SECOND, 0);
        	novaEscala.setDiasDeTrabalho(escalaDeTrabalho.getDiasDeTrabalho());
        	novaEscala.save();

    		for (DiaDeTrabalho diaDeTrabalho : diasDeTrabalho) {
                DiaDeTrabalho diaDeTrabalhoNovo = new DiaDeTrabalho();
                diaDeTrabalhoNovo.setDiaEntrada(diaDeTrabalho.getDiaEntrada());
                diaDeTrabalhoNovo.setDiaSaida(diaDeTrabalho.getDiaSaida());
                diaDeTrabalhoNovo.setHoraEntrada(diaDeTrabalho.getHoraEntrada());
                diaDeTrabalhoNovo.setHoraSaida(diaDeTrabalho.getHoraSaida());
                diaDeTrabalhoNovo.setEscalaDeTrabalho(novaEscala);

                diaDeTrabalhoNovo.save();
    		}
         }

       result.redirectTo(CondutorController.class).listar();       
       
    }

	private boolean isMesmaEscala(EscalaDeTrabalho escalaDeTrabalho, EscalaDeTrabalho escala) {
		return escalaDeTrabalho.getId().equals(escala.getId());
	}

	private boolean isMesmosDiasDeTrabalho(List<DiaDeTrabalho> diasDeTrabalhoAntigo, List<DiaDeTrabalho> diasDeTrabalhoNovo) {
		return diasDeTrabalhoAntigo.equals(diasDeTrabalhoNovo);
	}

    @Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/excluir/{id}")
	public void excluir(final Long id) throws Exception {
    	EscalaDeTrabalho escala = EscalaDeTrabalho.AR.findById(id);
    	Long idCondutor = escala.getCondutor().getId();
    	escala.delete();

    	result.redirectTo(this).listarPorCondutor(idCondutor);
    }

	private boolean  ehMesmoDia(Calendar cal1, Calendar cal2) {
	    if (cal1 == null || cal2 == null)
	        return false;
	    return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
	            && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
	            && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}

	private boolean validarEscala(List<DiaDeTrabalho> diasDeTrabalho) {
		for (DiaDeTrabalho diaDeTrabalho : diasDeTrabalho) {
			if(null == diaDeTrabalho.getHoraEntrada()) {
				validator.add(new I18nMessage(DIAS_DE_TRABALHO, "escalasDeTrabalho.horaEntrada.vazia.validation"));
				return false;
			}

			if(null == diaDeTrabalho.getHoraSaida()) {
				validator.add(new I18nMessage(DIAS_DE_TRABALHO, "escalasDeTrabalho.horaSaida.vazia.validation"));
				return false;
			}
		}

		Collections.sort(diasDeTrabalho);
		List<String> periodosDeTrabalho = new ArrayList<String>();
		for (DiaDeTrabalho diaDeTrabalho : diasDeTrabalho) {

			if (diaDeTrabalho.getDiaEntrada().getOrdem() > diaDeTrabalho.getDiaSaida().getOrdem()) {
				validator.add(new I18nMessage(DIAS_DE_TRABALHO, "escalasDeTrabalho.diaEntrada.validation"));
				return false;
			}

			if (diaDeTrabalho.getHoraEntrada().after(diaDeTrabalho.getHoraSaida()) &&
				diaDeTrabalho.getDiaEntrada().getOrdem().equals(diaDeTrabalho.getDiaSaida().getOrdem())) {
				validator.add(new I18nMessage(DIAS_DE_TRABALHO, "escalasDeTrabalho.horaEntrada.validation"));
				return false;
			}

			periodosDeTrabalho.add(diaDeTrabalho.getDiaEntrada().getOrdem() + diaDeTrabalho.getHoraEntradaFormatada("HHmm"));
			periodosDeTrabalho.add(diaDeTrabalho.getDiaSaida().getOrdem() + diaDeTrabalho.getHoraSaidaFormatada("HHmm"));
		}

		for (int i = 0; i < periodosDeTrabalho.size(); i++) {
			if (i + 1 != periodosDeTrabalho.size())  {
				int intervalo1 = Integer.parseInt(periodosDeTrabalho.get(i));
				int intervalo2 = Integer.parseInt(periodosDeTrabalho.get(i+1));
				if (intervalo1 > intervalo2) {
					validator.add(new I18nMessage(DIAS_DE_TRABALHO, "escalasDeTrabalho.periodosDeTrabalho.validation"));
					return false;
				}
			}
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean validarMissoesParaNovaEscala(EscalaDeTrabalho escala) throws Exception {
		missaoController.buscarPorCondutoreseEscala(escala);
		List<Missao> missoes = (List<Missao>) getRequest().getAttribute("missoesPorCondutoreEscala");
		SimpleDateFormat formatar = new SimpleDateFormat(FormatarDataHora.recuperaFormato(PATTERN_DDMMYYYYHHMM,PATTERN_DDMMYYYYHHMM_MYSQL));
		boolean valido = true;
		StringBuilder listaMissoes = new StringBuilder();
		String delimitador="";

		if (missoes != null && !missoes.isEmpty()) {
			for (Missao missao : missoes) {
				String dataMissao = formatar.format(missao.getDataHoraSaida().getTime());
				String dataFormatadaOracle = dataMissao;
		//		String dataFormatadaOracle = "to_date('" + dataMissao + "', 'DD/MM/YYYY HH24:mi')";
				if (! missao.getCondutor().estaEscalado(dataMissao) &&
					! missao.getCondutor().estaDePlantao(dataFormatadaOracle)	) {
					listaMissoes.append(delimitador).append(missao.getSequence());
					delimitador=",";
					valido = false;
				}
			}
			if (! valido) {
				validator.add(new I18nMessage("LinkErroEscalaDeTrabalho", listaMissoes.toString()));
			}
		}
		return valido;
	}
}
