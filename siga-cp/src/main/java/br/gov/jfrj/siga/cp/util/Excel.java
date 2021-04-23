package br.gov.jfrj.siga.cp.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaConstraintViolationException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpTipoIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.SituacaoFuncionalEnum;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.model.dao.DaoFiltro;

public class Excel {
	
	public InputStream uploadLotacao(File file, CpOrgaoUsuario orgaoUsuario, String extensao, CpIdentidade cadastrante) {
		InputStream retorno = null;
		if(".txt".equalsIgnoreCase(extensao) || ".csv".equalsIgnoreCase(extensao)) {
			retorno = uploadCVS(file, orgaoUsuario);
		} else if(".xlsx".equalsIgnoreCase(extensao)){
			retorno = uploadExcelLotacao(file, orgaoUsuario, cadastrante);
		}
		return retorno;
	}
	
	public InputStream uploadCVS(File file, CpOrgaoUsuario orgaoUsuario) {
		Scanner sc = null;
        Integer linha=0;
        String campo = "";
        StringBuffer problemas = new StringBuffer();
        List<String> nomes = new ArrayList<String>();
        List<String> siglas = new ArrayList<String>();
        List<CpLocalidade> localidades = new ArrayList<CpLocalidade>();
        DpLotacao lotacao = new DpLotacao();
        CpLocalidade localidade = new CpLocalidade();
        CpUF uf = new CpUF();
        uf.setIdUF(26L);
        List<DpLotacao> listaLotacaoGravar = new ArrayList<DpLotacao>();
        Date data = new Date(System.currentTimeMillis());
		try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
            	localidade = new CpLocalidade();
            	lotacao = new DpLotacao();
            	linha++;
                String aux = sc.nextLine();
                String[] parts = aux.split(";");
                
                if(parts.length > 3) {
                	problemas.append("Linha " + linha +": Quantidade de registros da linha inválida" + System.lineSeparator());
                }
                
                //NOME DA LOTACAO
                campo = parts[0];
                problemas.append(validarNomeLotacao(nomes, campo.trim(), orgaoUsuario, linha, lotacao, 100));
				
				//SIGLA DA LOTACAO
				campo = parts[1];
				problemas.append(validarSiglaLotacao(siglas, campo.trim(), orgaoUsuario, linha, lotacao, 20));
				
				//LOCALIDADE DA LOTACAO
				if(parts.length < 3) {
                	problemas.append("Linha " + linha +": LOCALIDADE em branco" + System.lineSeparator());
                	continue;
                }
				campo = parts[2];
				localidade.setUF(uf);
				localidade.setNmLocalidade(campo);
				problemas.append(validarLocalidadeLotacao(localidades, linha, localidade));
							
				if(problemas == null || "".equals(problemas.toString())) {
					DpLotacao lot = new DpLotacao();
					lot.setNomeLotacao(parts[0].trim());
					lot.setSiglaLotacao(parts[1].toUpperCase().trim());
					
					for (CpLocalidade lo : localidades) {
						if(lo.getNmLocalidade().equalsIgnoreCase(localidade.getNmLocalidade())) {
							lot.setLocalidade(lo);		
						}
					}
					
					if(lot.getLocalidade() == null || lot.getLocalidade().getIdLocalidade() == null) {
						lot.setLocalidade(CpDao.getInstance().consultarLocalidadesPorNomeUF(localidade));
					}
					
					lot.setDataInicio(data);
					lot.setOrgaoUsuario(orgaoUsuario);
					listaLotacaoGravar.add(lot);
					
				}
            }
            if(problemas == null || "".equals(problemas.toString())) {
	            try {
	            	for (DpLotacao dpLotacao : listaLotacaoGravar) {
		            	CpDao.getInstance().iniciarTransacao();
		    			CpDao.getInstance().gravar(dpLotacao);
		    			
	    				if(dpLotacao.getIdLotacaoIni() == null && dpLotacao.getId() != null) {
	    					dpLotacao.setIdLotacaoIni(dpLotacao.getId());
	    					dpLotacao.setIdeLotacao(dpLotacao.getId().toString());
	        				CpDao.getInstance().gravar(dpLotacao);
	        			}
					}
	    			CpDao.getInstance().commitTransacao();			
	    		} catch (final Exception e) {
	    			CpDao.getInstance().rollbackTransacao();
	    			throw new AplicacaoException("Erro na gravação", 0, e);
	    		}
            }
        } catch (FileNotFoundException e) {
            System.err.println("Não foi possível econtrar arquivo: " + e.getMessage());
        } catch (Exception e) {
			System.err.println(e.getMessage());
			throw new AplicacaoException("Erro na gravação", 0, e);
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
		if(problemas == null || "".equals(problemas.toString())) {
			return null;
		}
    	return new ByteArrayInputStream(problemas.toString().getBytes());
	}
	
	public String validarNomeLotacao(List<String> nomes, String nomeLotacao, CpOrgaoUsuario orgaoUsuario, Integer linha, DpLotacao lotacao, Integer tamanho) {
		if("".equals(nomeLotacao)) {
			return "Linha " + linha +": NOME em branco" + System.lineSeparator();
		} 
		
		if(nomeLotacao.length() > tamanho){
			return "Linha " + linha +": NOME com mais de 100 caracteres" + System.lineSeparator();
		}

		if(nomeLotacao != null && !nomeLotacao.matches("[\'a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9.,/-]+")) {
			return "Linha " + linha +": NOME com caracteres não permitidos" + System.lineSeparator();
		}
		nomes.add(Texto.removeAcento(Texto.removerEspacosExtra(nomeLotacao).trim().toUpperCase()));	

		return "";
	}
	
	public String validarSiglaLotacao(List<String>siglas, String siglaLotacao, CpOrgaoUsuario orgaoUsuario, Integer linha, DpLotacao lotacao, Integer tamanho) {
		if("".equals(siglaLotacao)) {
			return "Linha " + linha +": SIGLA em branco" + System.lineSeparator();			
		} 
		
		if(siglaLotacao.length() > tamanho) {
			return "Linha " + linha +": SIGLA com mais de 20 caracteres" + System.lineSeparator();
		}
		lotacao = new DpLotacao();
		lotacao.setOrgaoUsuario(orgaoUsuario);
		lotacao.setSigla(Texto.removeAcento(Texto.removerEspacosExtra(siglaLotacao).trim()));
		lotacao = CpDao.getInstance().consultarPorSigla(lotacao);
		if(lotacao != null) {
			return "Linha " + linha +": SIGLA já cadastrada" + System.lineSeparator();
		}
		if(siglaLotacao != null && !siglaLotacao.matches("[a-zA-ZçÇ0-9,/-]+")) {
			return "Linha " + linha +": SIGLA com caracteres não permitidos" + System.lineSeparator();
		} 
		
		if(siglas.contains(Texto.removeAcento(Texto.removerEspacosExtra(siglaLotacao).trim().toUpperCase()))) {
			return "Linha " + linha +": SIGLA repetida em outra linha do arquivo" + System.lineSeparator();
		} else {
			siglas.add(Texto.removeAcento(Texto.removerEspacosExtra(siglaLotacao).trim().toUpperCase()));	
		}
		return "";
	}
	
	public String validarIsExternaLotacao(String isLotacaoExterna, Integer linha) {			
		if (montarIsExternaLotacao(isLotacaoExterna) == null) {
			return "Linha " + linha +": LOTAÇÃO EXTERNA com valor diferente do esperado (aceito apenas SIM ou NÃO)" + System.lineSeparator();
		}			
				
		return "";		
	}
	
	public Integer montarIsExternaLotacao(String isLotacaoExterna) {
		if("".equals(isLotacaoExterna)) {
			return 0;			
		} 
		
		Integer valor = null;
		
		switch(isLotacaoExterna.toUpperCase()) {
		case "SIM":
			valor = 1;
			break;
		case "NÃO":
		case "NAO":
			valor = 0;
			break;					
		}			
				
		return valor;		
	}
    
	public String validarLocalidadeLotacao(List<CpLocalidade> localidades, Integer linha, CpLocalidade loc) {
		if("".equals(loc.getNmLocalidade())) {
			return "Linha " + linha +": LOCALIDADE em branco" + System.lineSeparator();
		} else if(loc.getNmLocalidade() == null){
			return "Linha " + linha +": LOCALIDADE com mais de 256 caracteres" + System.lineSeparator();
		}
		for (CpLocalidade cpLocalidade : localidades) {
			if(cpLocalidade.getNmLocalidade().equalsIgnoreCase(loc.getNmLocalidade())) {
				loc = cpLocalidade;
				break;
			}
		}
		if(loc == null || loc.getIdLocalidade() == null) {
			loc.setNmLocalidade(Texto.removeAcento(Texto.removerEspacosExtra(loc.getNmLocalidade().replace("'", " ")).trim()));
			CpLocalidade localidade = CpDao.getInstance().consultarLocalidadesPorNomeUF(loc);
			if(localidade == null) {
				return "Linha " + linha +": UF/LOCALIDADE não cadastrada" + System.lineSeparator();
			} else {
				localidades.add(localidade);
				loc = localidade;
			}
		}
		
		return "";
	}
	
    public InputStream uploadExcelLotacao(File file, CpOrgaoUsuario orgaoUsuario, CpIdentidade cadastrante) {
    	InputStream inputStream = null;
    	String problemas = "";
        CpUF uf = null;

    	try {
			FileInputStream fis = new FileInputStream(file); 
			XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); 
			XSSFSheet mySheet = myWorkBook.getSheetAt(0); 
			Iterator<Row> rowIterator = mySheet.iterator(); 
			String celula;
			Integer linha = 0;
			List<CpLocalidade> localidades = new ArrayList();
			List<String> nomes = new ArrayList();
			List<String> siglas = new ArrayList();
			List<DpLotacao> lista = new ArrayList(); 			
			CpLocalidade loc = new CpLocalidade();
			Date data = new Date(System.currentTimeMillis());
			
			while (rowIterator.hasNext()) {
				linha++;
				DpLotacao lotacao = new DpLotacao();
				DpLotacao lot = new DpLotacao();
				Row row = rowIterator.next(); //linha
				
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;
				
				//NOME DA LOTACAO				
				celula = retornaConteudo(row.getCell(0, Row.CREATE_NULL_AS_BLANK));
				problemas += validarNomeLotacao(nomes, celula.trim(), orgaoUsuario, linha, lotacao, 100);
				
				if(problemas == null || "".equals(problemas.toString())) {
					lot.setNomeLotacao(celula.trim());
				}

				//SIGLA DA LOTACAO
				celula = retornaConteudo(row.getCell(1, Row.CREATE_NULL_AS_BLANK));
				problemas += validarSiglaLotacao(siglas, celula.trim(), orgaoUsuario, linha, lotacao, 20);
				
				if(problemas == null || "".equals(problemas.toString())) {
					lot.setSiglaLotacao(celula.toUpperCase().trim());
				}
				
				//ESTADO
				celula = retornaConteudo(row.getCell(2, Row.CREATE_NULL_AS_BLANK));
				
				if(uf == null || !uf.getSigla().equalsIgnoreCase(celula)) {
					uf = CpDao.getInstance().consultaSiglaUF(celula.toUpperCase());	
				}
				
				if(uf == null) {
					problemas += "Linha " + linha +": UF não encontrada" + System.getProperty("line.separator");
				} else {
					//LOCALIDADE DA LOTACAO
					celula = retornaConteudo(row.getCell(3, Row.CREATE_NULL_AS_BLANK));
					loc.setUF(uf);
					loc.setNmLocalidade(celula);
					problemas += validarLocalidadeLotacao(localidades, linha, loc);		
				}
				

				/*
				 * Alteracao 24/04/2020
				 */
				//Lotacao Pai
				//celula = retornaConteudo(row.getCell(3, Row.CREATE_NULL_AS_BLANK));
				//String lotacaopaidescricao = celula;
				celula = retornaConteudo(row.getCell(4, Row.CREATE_NULL_AS_BLANK));
				String lotacaopaisigla = celula;
				if(!celula.equals("")) {
					DpLotacao lo = CpDao.getInstance().consultarLotacaoPorOrgaoEId(orgaoUsuario, lotacaopaisigla);
					if(lo!=null) {
						lot.setLotacaoPai(lo);
					} else {
						problemas += "Linha " + linha +": SIGLA de LOTAÇÃO PAI não encontrada, se você deseja informar " 
								+ lotacaopaisigla + " como unidade pai, é necessário cadastrá-lo antes de importar essa planilha" + System.getProperty("line.separator");
					}
				}
				
				//LOTACAO EXTERNA
				celula = retornaConteudo(row.getCell(5, Row.CREATE_NULL_AS_BLANK));
				problemas += validarIsExternaLotacao(celula.trim(), linha);
				
				if(problemas == null || "".equals(problemas.toString())) {
					lot.setIsExternaLotacao(montarIsExternaLotacao(celula.trim()));
				}
					
				
				if(problemas == null || "".equals(problemas.toString())) {
					for (CpLocalidade lo : localidades) {
						if(lo.getNmLocalidade().equalsIgnoreCase(loc.getNmLocalidade())) {
							lot.setLocalidade(lo);		
						}
					}
					
					if(lot.getLocalidade() == null || lot.getLocalidade().getIdLocalidade() == null) {
						lot.setLocalidade(CpDao.getInstance().consultarLocalidadesPorNomeUF(loc));
					}
				
					lot.setDataInicio(data);
					lot.setOrgaoUsuario(orgaoUsuario);
					lista.add(lot);
				}
			}
			if(problemas == null || "".equals(problemas.toString())) {
            	for (DpLotacao dpLotacao : lista) {
	            	CpDao.getInstance().iniciarTransacao();
	    			CpDao.getInstance().gravarComHistorico(dpLotacao, cadastrante);
	    			
    				if(dpLotacao.getIdLotacaoIni() == null && dpLotacao.getId() != null) {
    					dpLotacao.setIdLotacaoIni(dpLotacao.getId());
    					dpLotacao.setIdeLotacao(dpLotacao.getId().toString());
        				CpDao.getInstance().gravar(dpLotacao);	        				
        			}
				}
            	CpDao.getInstance().em().getTransaction().commit();
			}
			if(problemas == null || "".equals(problemas.toString())) {
	    		return null;
	    	}
	    	inputStream = new ByteArrayInputStream(problemas.getBytes("ISO-8859-1"));
		} catch (Exception e) {								
			if (CpDao.getInstance().em().getTransaction().isActive()) {
				CpDao.getInstance().em().getTransaction().rollback();
			}			
			if (e.getCause() != null && e.getCause().toString().contains("ConstraintViolationException")) {
				throw new SigaConstraintViolationException("Identificado uma violação de integridade no banco de dados." 
						+ " Isso pode ocorrer ao gravar um registro que já exista.<br/>" 
						+ " Por segurança, todo o processo foi cancelado e nenhum registro foi gravado.<br/>"							
						+ " Favor analisar a planilha e se certificar de que na mesma,"  
						+ " não exista nenhuma SIGLA que já esteja cadastrada no sistema e tente novamente.");
			}			
			throw new AplicacaoException("Erro na gravação", 0, e);					
        }
    	return inputStream;
    }
    
    public Boolean validarCaracterEspecial(String celula) {
    	Boolean retorno = Boolean.TRUE;
    	if(!celula.matches("[a-zA-ZáâãéêíóôõúçÁÂÃÉÊÍÓÔÕÚÇ 0-9.]+")) {
    		retorno = Boolean.FALSE;
    	}
    	return retorno;
    }
    
    public Boolean validarCaracter(String celula) {
    	Boolean retorno = Boolean.TRUE;
    	if(!celula.matches("[a-zA-ZáâãéêíóôõúçÁÂÃÉÊÍÓÔÕÚÇ ]+")) {
    		retorno = Boolean.FALSE;
    	}
    	return retorno;
    }
    
    public String retornaConteudo(Cell cell) {
    	String retorno = null;
    	
    	switch (cell.getCellType()) { 
			case Cell.CELL_TYPE_STRING: 
				retorno = cell.getStringCellValue();
				break; 
			case Cell.CELL_TYPE_NUMERIC:
				retorno = String.valueOf(((Double)cell.getNumericCellValue()).longValue());
				break; 
			case Cell.CELL_TYPE_BLANK:
				retorno = "";
		} 
    	return retorno;
    }
    
    public InputStream uploadFuncao(File file, CpOrgaoUsuario orgaoUsuario, String extensao, final CpIdentidade identidadeCadastrante) {
		InputStream retorno = null;
		retorno = uploadExcelFuncao(file, orgaoUsuario,identidadeCadastrante);

		return retorno;
	}
    
    public InputStream uploadExcelFuncao(File file, CpOrgaoUsuario orgaoUsuario,final CpIdentidade identidadeCadastrante) {
    	InputStream inputStream = null;
    	StringBuffer problemas = new StringBuffer();

    	try {
			FileInputStream fis = new FileInputStream(file); 
			XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); 
			XSSFSheet mySheet = myWorkBook.getSheetAt(0); 
			Iterator<Row> rowIterator = mySheet.iterator(); 
			String celula;
			Integer linha = 0;
			List<String> nomes = new ArrayList();
			List<DpFuncaoConfianca> lista = new ArrayList(); 
			Date data = new Date(System.currentTimeMillis());
			
			while (rowIterator.hasNext()) {
				linha++;
				DpFuncaoConfianca funcao = new DpFuncaoConfianca();
				DpFuncaoConfianca func = new DpFuncaoConfianca();
				Row row = rowIterator.next(); //linha
				
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;
				
				//NOME DA FUNCAO				
				celula = retornaConteudo(row.getCell(0, Row.CREATE_NULL_AS_BLANK));
				problemas.append(validarNomeFuncao(nomes, celula.trim(), orgaoUsuario, linha, funcao, 100));
				
				if(problemas == null || "".equals(problemas.toString())) {
					func.setNomeFuncao(celula.trim());
				}

				if(problemas == null || "".equals(problemas.toString())) {
					func.setDataInicio(data);
					func.setOrgaoUsuario(orgaoUsuario);
					lista.add(func);
				}
			}
			
			if(problemas == null || "".equals(problemas.toString())) {
				try {
	            	for (DpFuncaoConfianca dpFuncaoConfianca : lista) {
	            		Cp.getInstance().getBL().gravarFuncaoConfianca(identidadeCadastrante, null, dpFuncaoConfianca.getNomeFuncao(), dpFuncaoConfianca.getOrgaoUsuario().getIdOrgaoUsu(),Boolean.TRUE);
					}		
	    		} catch (final Exception e) {
	    			throw new AplicacaoException("Erro na gravação", 0, e);
	    		}
			}
			if(problemas == null || "".equals(problemas.toString())) {
				try {
	            	for (DpFuncaoConfianca dpFuncaoConfianca : lista) {
	            		Cp.getInstance().getBL().gravarFuncaoConfianca(identidadeCadastrante, null, dpFuncaoConfianca.getNomeFuncao(), dpFuncaoConfianca.getOrgaoUsuario().getIdOrgaoUsu(),Boolean.TRUE);
					}		
	    		} catch (final Exception e) {
	    			throw new AplicacaoException("Erro na gravação", 0, e);
	    		}
			}
			if(problemas == null || "".equals(problemas.toString())) {
	    		return null;
	    	}
	    	inputStream = new ByteArrayInputStream(problemas.toString().getBytes("ISO-8859-1"));
		} catch (Exception ioe) {
            ioe.printStackTrace();
        }
    	
    	return inputStream;
    }
    
    public String validarNomeFuncao(List<String> nomes, String nomeFuncao, CpOrgaoUsuario orgaoUsuario, Integer linha, DpFuncaoConfianca funcao, Integer tamanho) {
    	
		if("".equals(nomeFuncao)) {
			return "Linha " + linha +": NOME em branco" + System.lineSeparator();
		} 
		
		if(nomeFuncao.length() > tamanho){
			return "Linha " + linha +": NOME com mais de 100 caracteres" + System.lineSeparator();
		}
		funcao.setOrgaoUsuario(orgaoUsuario);
		funcao.setNomeFuncao(nomeFuncao);
		funcao = CpDao.getInstance().consultarPorNomeOrgao(funcao);
		if(funcao != null) {
			return "Linha " + linha +": NOME já cadastrado" + System.lineSeparator();
		}
		
		if(!nomeFuncao.matches(Texto.FuncaoConfianca.REGEX_CARACTERES_PERMITIDOS)) {
			return "Linha " + linha +": NOME com caracteres não permitidos" + System.lineSeparator();
    	}
		if(nomes.contains(Texto.removeAcento(Texto.removerEspacosExtra(nomeFuncao).trim().toUpperCase()))) {
			return "Linha " + linha +": NOME repetido em outra linha do arquivo" + System.lineSeparator();
		} else {
			nomes.add(Texto.removeAcento(Texto.removerEspacosExtra(nomeFuncao).trim().toUpperCase()));	
		}
		return "";
	}
    
    public InputStream uploadCargo(File file, CpOrgaoUsuario orgaoUsuario, String extensao,final CpIdentidade identidadeCadastrante) {
		InputStream retorno = null;
		retorno = uploadExcelCargo(file, orgaoUsuario,identidadeCadastrante);

		return retorno;
	}
    
    public InputStream uploadExcelCargo(File file, CpOrgaoUsuario orgaoUsuario, final CpIdentidade identidadeCadastrante) {
    	InputStream inputStream = null;
    	StringBuffer problemas = new StringBuffer();

    	try {
			FileInputStream fis = new FileInputStream(file); 
			XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); 
			XSSFSheet mySheet = myWorkBook.getSheetAt(0); 
			Iterator<Row> rowIterator = mySheet.iterator(); 
			String celula;
			Integer linha = 0;
			List<String> nomes = new ArrayList();
			List<DpCargo> lista = new ArrayList(); 
			Date data = new Date(System.currentTimeMillis());
			
			while (rowIterator.hasNext()) {
				linha++;
				DpCargo cargo = new DpCargo();
				DpCargo ca = new DpCargo();
				Row row = rowIterator.next(); //linha
				
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;
				
				//NOME DA CARGO				
				celula = retornaConteudo(row.getCell(0, Row.CREATE_NULL_AS_BLANK));
				problemas.append(validarNomeCargo(nomes, celula.trim(), orgaoUsuario, linha, cargo, 100));
				
				if(problemas == null || "".equals(problemas.toString())) {
					ca.setNomeCargo(celula.trim());
				}

				if(problemas == null || "".equals(problemas.toString())) {
					ca.setDataInicio(data);
					ca.setOrgaoUsuario(orgaoUsuario);
					lista.add(ca);
				}
			}
			if(problemas == null || "".equals(problemas.toString())) {
				try {
	            	for (DpCargo dpCargo : lista) {
	            		Cp.getInstance().getBL().gravarCargo(identidadeCadastrante, null, dpCargo.getNomeCargo(), dpCargo.getOrgaoUsuario().getIdOrgaoUsu(),Boolean.TRUE);
					}		
	    		} catch (final Exception e) {
	    			throw new AplicacaoException("Erro na gravação", 0, e);
	    		}
			}
			if(problemas == null || "".equals(problemas.toString())) {
	    		return null;
	    	}
	    	inputStream = new ByteArrayInputStream(problemas.toString().getBytes("ISO-8859-1"));
		} catch (Exception ioe) {
            ioe.printStackTrace();
        }
    	
    	return inputStream;
    }
    
    public String validarNomeCargo(List<String> nomes, String nomeCargo, CpOrgaoUsuario orgaoUsuario, Integer linha, DpCargo cargo, Integer tamanho) {
    	
		if("".equals(nomeCargo)) {
			return "Linha " + linha +": NOME em branco" + System.lineSeparator();
		} 
		
		if(nomeCargo.length() > tamanho){
			return "Linha " + linha +": NOME com mais de 100 caracteres" + System.lineSeparator();
		}
		cargo.setOrgaoUsuario(orgaoUsuario);
		cargo.setNomeCargo(nomeCargo);
		cargo = CpDao.getInstance().consultarPorNomeOrgao(cargo);
		if(cargo != null) {
			return "Linha " + linha +": NOME já cadastrado" + System.lineSeparator();
		}
		if(!validarCaracterEspecial(nomeCargo)) {
			return "Linha " + linha +": NOME com caracteres não permitidos" + System.lineSeparator();
		}
		if(nomes.contains(Texto.removeAcento(Texto.removerEspacosExtra(nomeCargo).trim()).toUpperCase())) {
			return "Linha " + linha +": NOME repetido em outra linha do arquivo" + System.lineSeparator();
		} else {
			nomes.add(Texto.removeAcento(Texto.removerEspacosExtra(nomeCargo).trim().toUpperCase()));	
		}
		return "";
	}
    
    public InputStream uploadPessoa(File file, CpOrgaoUsuario orgaoUsuario, String extensao, CpIdentidade identidade) {
		return uploadExcelPessoa(file, orgaoUsuario, identidade);
	}
    
    public InputStream uploadExcelPessoa(File file, CpOrgaoUsuario orgaoUsuario, CpIdentidade identidade) {
    	InputStream inputStream = null;
    	StringBuffer problemas = new StringBuffer();

    	orgaoUsuario = CpDao.getInstance().consultarPorId(orgaoUsuario);
    	try {
			FileInputStream fis = new FileInputStream(file); 
			XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); 
			XSSFSheet mySheet = myWorkBook.getSheetAt(0); 
			Iterator<Row> rowIterator = mySheet.iterator(); 
			String celula;
			Integer linha = 0;
//			List<String> nomes = new ArrayList();
			List<DpPessoa> lista = new ArrayList(); 
			Date data = new Date(System.currentTimeMillis());
			DpCargo cargo = new DpCargo();
			List<DpCargo> listaCargo = new ArrayList<DpCargo>();
			List<DpFuncaoConfianca> listaFuncao = new ArrayList<DpFuncaoConfianca>();
			List<DpLotacao> listaLotacao = new ArrayList<DpLotacao>();
			DpFuncaoConfianca funcao = new DpFuncaoConfianca();
			DpLotacao lotacao = new DpLotacao();
			String cpf = "";
			String dataString;
			String rg = "";
			String orgexp = "";
			String ufexp = "";
			SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
			Date date = null;
			Date dateExp = null;
			int i = 0;
			String email = "";
			String nomeExibicao = null;
			
			DpPessoaDaoFiltro dpPessoaFiltro = new DpPessoaDaoFiltro();
			Integer tamanho = 0;
						
			while (rowIterator.hasNext()) {
				linha++;
				DpPessoa pessoa = new DpPessoa();
				DpPessoa pe = new DpPessoa();
				cargo = new DpCargo();
				funcao = new DpFuncaoConfianca();
				lotacao = new DpLotacao();
				Row row = rowIterator.next(); //linha
				date = null;
				dateExp = null;
				i = 0;
				email = "";
				
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;
				
				/* 6 --------  CPF -----------*/
				celula = retornaConteudo(row.getCell(6, Row.CREATE_NULL_AS_BLANK));
				cpf = celula.replaceAll("[^0-9]", "");
				cpf = StringUtils.leftPad(cpf, 11, "0");
				if(!"".equals(cpf.trim())) {
					if(!validarCPF(cpf)) {
						problemas.append("Linha " + linha +": CPF inválido" + System.lineSeparator());
					}
				} else {
					problemas.append("Linha " + linha +": CPF em branco" + System.lineSeparator());
				}
				/* ----------  END ---------- */
				
				
				/* 0 --------  SIGLA DO ORGAO -----------*/			
				celula = retornaConteudo(row.getCell(0, Row.CREATE_NULL_AS_BLANK));
				if("".equals(celula.trim())) {
					problemas.append("Linha " + linha +": ÓRGÃO em branco" + System.lineSeparator());
				} else {
					if(!celula.equalsIgnoreCase(orgaoUsuario.getSiglaOrgaoUsu())) {
						problemas.append("Linha " + linha +": ÓRGÃO inválido" + System.lineSeparator());
					}
				}
				/* ----------  END ---------- */

				/* 1 --------  NOME DO CARGO -----------*/	
				celula = retornaConteudo(row.getCell(1, Row.CREATE_NULL_AS_BLANK));
				if(!"".equals(celula.trim())) {
					cargo.setOrgaoUsuario(orgaoUsuario);
					cargo.setNomeCargo(Texto.removeAcento(Texto.removerEspacosExtra(celula).trim()));
					
					for(DpCargo c : listaCargo) {
						if(c.getNomeCargoAI().equalsIgnoreCase(cargo.getNomeCargo())) {
							cargo = c;
						}
					}
					if(cargo.getDataInicio() == null) {
						cargo.setNomeCargo(celula.trim());
						cargo = CpDao.getInstance().consultarPorNomeOrgao(cargo);	
					}
										
					if(cargo == null) {
						problemas.append("Linha " + linha +": CARGO não cadastrado" + System.lineSeparator());
					} else {
						listaCargo.add(cargo);
					}
					if(cargo != null && cargo.getDataFimCargo() != null) {
						problemas.append("Linha " + linha +": CARGO inativo" + System.lineSeparator());
					}
				} else {
					problemas.append("Linha " + linha +": CARGO em branco" + System.lineSeparator());
				}
				/* ----------  END ---------- */
							
				/* 2 --------  NOME FUNCAO DE CONFIANCA -----------*/	
				celula = retornaConteudo(row.getCell(2, Row.CREATE_NULL_AS_BLANK));
				if(!"".equals(celula.trim())) {
					funcao.setOrgaoUsuario(orgaoUsuario);
					funcao.setNomeFuncao(Texto.removeAcento(Texto.removerEspacosExtra(celula).trim()));
					
					for(DpFuncaoConfianca f : listaFuncao) {
						if(f.getNmFuncaoConfiancaAI().equalsIgnoreCase(funcao.getNomeFuncao())) {
							funcao = f;
						}
					}
					
					if(funcao.getDataInicio() == null) {
						funcao.setNomeFuncao(celula.trim());
						funcao = CpDao.getInstance().consultarPorNomeOrgao(funcao);	
					}
					
					if(funcao == null) {
						problemas.append("Linha " + linha +": FUNÇÃO DE CONFIANÇA não cadastrada" + System.lineSeparator());
					} else {
						listaFuncao.add(funcao);
					}
					
					if(funcao != null && funcao.getDataFimFuncao() != null) {
						problemas.append("Linha " + linha +": FUNÇÃO DE CONFIANÇA inativa" + System.lineSeparator());
					}
				} else {
					funcao = null;
				}
				/* ----------  END ---------- */
				
				/* 3 --------  NOME DA LOTACAO -----------*/	
				celula = retornaConteudo(row.getCell(3, Row.CREATE_NULL_AS_BLANK));
				if(!"".equals(celula.trim())) {
					lotacao.setOrgaoUsuario(orgaoUsuario);
					lotacao.setSiglaLotacao(Texto.removeAcento(Texto.removerEspacosExtra(celula).trim()));
										
					for(DpLotacao l : listaLotacao) {
						if(l.getSiglaLotacao().equalsIgnoreCase(lotacao.getSiglaLotacao())) {
							lotacao = l;
						}
					}
					
					if(lotacao.getDataInicio() == null) {
						lotacao.setSiglaLotacao(celula.trim());
						lotacao = CpDao.getInstance().consultarPorSigla(lotacao);	
					}					
					
					if(lotacao == null) {
						problemas.append("Linha " + linha +": LOTAÇÃO não cadastrado" + System.lineSeparator());
					} else {
						listaLotacao.add(lotacao);
					}
					
					if(lotacao != null && lotacao.getDataFimLotacao() != null) {
						problemas.append("Linha " + linha +": LOTAÇÃO inativo" + System.lineSeparator());
					}
				} else {
					problemas.append("Linha " + linha +": LOTAÇÃO em branco" + System.lineSeparator());
				}
				/* ----------  END ---------- */
				
				/* 4 --------  NOME DA PESSOA -----------*/
				celula = retornaConteudo(row.getCell(4, Row.CREATE_NULL_AS_BLANK));
				problemas.append(validarNomePessoa(celula.trim(), linha, 60));
				
				
				if(problemas == null || "".equals(problemas.toString())) {
					pe.setNomePessoa(celula.trim());
				}
				
				List<DpPessoa> listaP = new ArrayList<DpPessoa>();
				listaP.addAll(CpDao.getInstance().listarCpfAtivoInativo(Long.valueOf(cpf.replace("-", "").replace(".", ""))));
				
				for (DpPessoa dpPessoa : listaP) {
					if(!dpPessoa.getNomePessoa().equalsIgnoreCase(celula.trim())) {
						problemas.append("Linha " + linha +": Nome diferente para o mesmo CPF já cadastrado.");
						break;
					}
				}
				/* ----------  END ---------- */
				
				
				/* 5 --------  DATA DE NASCIMENTO -----------*/
				if(retornaConteudo(row.getCell(5, Row.CREATE_NULL_AS_BLANK)) != "") {
					if(row.getCell(5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(row.getCell(5, Row.CREATE_NULL_AS_BLANK))){
						date = row.getCell(5).getDateCellValue();
						
						if(date.compareTo(new Date()) > 0) {
			    			problemas.append( "Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.lineSeparator());
			    		}
					} else if(row.getCell(5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						problemas.append(validarData(String.valueOf(((Double)row.getCell(5, Row.CREATE_NULL_AS_BLANK).getNumericCellValue()).longValue()), linha));
						if(problemas != null && problemas.toString().equals("")) {
							dataString = String.valueOf(((Double)row.getCell(5).getNumericCellValue()).longValue()).replaceAll("[^0-9]", "");
							date = formato.parse(dataString);	
							
							if(date.compareTo(new Date()) > 0) {
				    			problemas.append( "Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.lineSeparator());
				    		}
						}
						
					} else if(row.getCell(5).getCellType() == HSSFCell.CELL_TYPE_STRING) {
						problemas.append(validarData(row.getCell(5, Row.CREATE_NULL_AS_BLANK).getStringCellValue(), linha));
						if(problemas != null && problemas.toString().equals("")) {
							dataString = row.getCell(5, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
							date = formato.parse(dataString.replaceAll("[^0-9]", ""));	
							
							if(date.compareTo(new Date()) > 0) {
				    			problemas.append( "Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.lineSeparator());
				    		}
						}
						
					} else {
						problemas.append("Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.lineSeparator());
					}
				}
				/* ----------  END ---------- */
			
				/* 7 --------  EMAIL -----------*/
				celula = retornaConteudo(row.getCell(7, Row.CREATE_NULL_AS_BLANK)).trim().toLowerCase();
				email = celula;
				
				if(!"".equals(celula.trim())) {
					
					if(celula.length() > 60) {
						problemas.append("Linha " + linha +": E-MAIL com mais de 60 caracteres" + System.lineSeparator());
					}
					
					if(celula.contains(" ")) {
						problemas.append("Linha " + linha +": E-MAIL com espaço em branco" + System.lineSeparator());
					} else if(!celula.matches("[a-zA-Z0-9._-][a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")) {
						problemas.append("Linha " + linha +": E-MAIL inválidos ou com caracteres inválidos" + System.lineSeparator());
					}
				} else {
					problemas.append("Linha " + linha +": E-MAIL em branco" + System.lineSeparator());
				}
				
				i = CpDao.getInstance().consultarQtdePorEmailIgualCpfDiferente(Texto.removerEspacosExtra(celula).trim().replace(" ",""), Long.valueOf(cpf), Long.valueOf(0));
				
				if(i > 0) {
					problemas.append("Linha " + linha +": E-MAIL informado está cadastrado para outro CPF" + System.lineSeparator());
				}
				/* ----------  END ---------- */

				/* 8 --------  RG -----------*/
				celula = retornaConteudo(row.getCell(8, Row.CREATE_NULL_AS_BLANK));
				rg = celula;
				/* ----------  END ---------- */
				

				/* 9 --------  RG Orgao Expeditor -----------*/
				celula = retornaConteudo(row.getCell(9, Row.CREATE_NULL_AS_BLANK));
				orgexp = celula;
				/* ----------  END ---------- */
				
				/* 10 --------  UF RG -----------*/
				celula = retornaConteudo(row.getCell(10, Row.CREATE_NULL_AS_BLANK));
				ufexp = celula;
				if(!"".equals(ufexp.trim())) {
					if(CpDao.getInstance().consultaSiglaUF(ufexp)==null)
						problemas.append( "Linha " + linha + ": UF DO RG inválido" + System.lineSeparator());
				}
				/* ----------  END ---------- */
				
				/* 11 --------  RG Data Expedicao -----------*/
				if(retornaConteudo(row.getCell(11, Row.CREATE_NULL_AS_BLANK)) != "") {
					if(row.getCell(11).getCellType() == HSSFCell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(row.getCell(11, Row.CREATE_NULL_AS_BLANK))){
						dateExp = row.getCell(11).getDateCellValue();
						
						if(dateExp.compareTo(new Date()) > 0) {
			    			problemas.append( "Linha " + linha + ": DATA DE EXPEDIÇÃO DO RG inválida" + System.lineSeparator());
			    		}
					} else if(row.getCell(11).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						problemas.append(validarData(String.valueOf(((Double)row.getCell(11, Row.CREATE_NULL_AS_BLANK).getNumericCellValue()).longValue()), linha, "expedição"));
						if(problemas != null && problemas.toString().equals("")) {
							dataString = String.valueOf(((Double)row.getCell(11).getNumericCellValue()).longValue()).replaceAll("[^0-11]", "");
							dateExp = formato.parse(dataString);	
							
							if(dateExp.compareTo(new Date()) > 0) {
				    			problemas.append( "Linha " + linha + ": DATA DE EXPEDIÇÃO DO RG inválida" + System.lineSeparator());
				    		}
						}
						
					} else if(row.getCell(11).getCellType() == HSSFCell.CELL_TYPE_STRING) {
						problemas.append(validarData(row.getCell(11, Row.CREATE_NULL_AS_BLANK).getStringCellValue(), linha, "expedição"));
						if(problemas != null && problemas.toString().equals("")) {
							dataString = row.getCell(11, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
							dateExp = formato.parse(dataString.replace("-", "").replace("/", "").trim());	
							
							if(dateExp.compareTo(new Date()) > 0) {
				    			problemas.append( "Linha " + linha + ": DATA DE EXPEDIÇÃO DO RG inválida" + System.lineSeparator());
				    		}
						}
						
					} else {
						problemas.append("Linha " + linha + ": DATA DE EXPEDIÇÃO DO RG inválida" + System.lineSeparator());
					}
				}
				/* ----------  END ---------- */

				/* 12 --------  Nome Abreviado -----------*/
				celula = retornaConteudo(row.getCell(12, Row.CREATE_NULL_AS_BLANK));
				problemas.append(validarNomeAbreviado(celula.trim(), linha, 40));
				nomeExibicao = celula;
				/* ----------  END ---------- */
				

				
				if(cargo != null && lotacao != null && cpf != null && !"".equals(cpf) && !Long.valueOf(0).equals(Long.valueOf(cpf))) {
					dpPessoaFiltro = new DpPessoaDaoFiltro();
					dpPessoaFiltro.setIdOrgaoUsu(orgaoUsuario.getId());
					dpPessoaFiltro.setCargo(cargo);
					dpPessoaFiltro.setFuncaoConfianca(funcao);
					dpPessoaFiltro.setLotacao(lotacao);
					dpPessoaFiltro.setCpf(Long.valueOf(cpf));
					dpPessoaFiltro.setNome("");
					
					dpPessoaFiltro.setBuscarFechadas(Boolean.FALSE);
					tamanho = CpDao.getInstance().consultarQuantidade(dpPessoaFiltro);
					
					if(tamanho > 0) {
						problemas.append("Linha " + linha +": Usuário já cadastrado com estes dados: Órgão, Cargo, Função, Unidade e CPF" + System.lineSeparator());
					}
				}
				if(!lista.isEmpty()) {
					for (DpPessoa p : lista) { //repetido em outra linha do arquivo
						if(p.getCpfPessoa().equals(Long.valueOf(cpf)) && p.getCargo().equals(cargo) && p.getLotacao().equals(lotacao) &&
								((p.getFuncaoConfianca() == null && funcao == null) || (p.getFuncaoConfianca() != null && p.getFuncaoConfianca().equals(funcao)))) {
							problemas.append("Linha " + linha +": Usuário repetido em outra linha do arquivo com estes dados: Órgão, Cargo, Função, Unidade e CPF" + System.lineSeparator());
						}
						if(email != null && email.equals(p.getEmailPessoa()) && cpf != null && !Long.valueOf(cpf).equals(p.getCpfPessoa())) {
							problemas.append("Linha " + linha +": E-MAIL informado está cadastrado para outro CPF no arquivo" + System.lineSeparator());
						}
						if(!"".equals(cpf.trim()) && p.getCpfPessoa().equals(Long.valueOf(cpf.replace("-", "").replace(".", ""))) &&
								!p.getNomePessoa().equalsIgnoreCase(pe.getNomePessoa())) {
							problemas.append("Linha " + linha +": Nome diferente para o mesmo CPF em outra linha do arquivo." + System.lineSeparator());
							break;
						}
					}
				}
				
				if(problemas == null || "".equals(problemas.toString())) {
					pe.setNomeExibicao(nomeExibicao);
					pe.setDataNascimento(date);
					pe.setCpfPessoa(Long.valueOf(cpf));
					pe.setOrgaoUsuario(orgaoUsuario);
					pe.setLotacao(lotacao);
					pe.setCargo(cargo);
					pe.setFuncaoConfianca(funcao);
					pe.setEmailPessoa(email);
					pe.setDataInicio(data);
					pe.setSesbPessoa(orgaoUsuario.getSigla());					
					
					pe.setIdentidade(rg);
					pe.setOrgaoIdentidade(orgexp);
					pe.setUfIdentidade(ufexp);
					pe.setDataExpedicaoIdentidade(dateExp);
					
					pe.setMatricula(Long.valueOf(0));
					pe.setSituacaoFuncionalPessoa(SituacaoFuncionalEnum.APENAS_ATIVOS.getValor()[0]);
					lista.add(pe);
				}
			}
			if(problemas == null || "".equals(problemas.toString())) {				
				CpDao.getInstance().iniciarTransacao();
				CpIdentidade usu = null;
				CpIdentidade usuarioExiste = null;
				List<CpIdentidade> lista1 = new ArrayList<CpIdentidade>();
            	for (DpPessoa dpPessoa : lista) {
	    			CpDao.getInstance().gravarComHistorico(dpPessoa, identidade);

	    			dpPessoa.setMatricula(10000 + dpPessoa.getId());
					dpPessoa.setIdePessoa(dpPessoa.getId().toString());
					CpDao.getInstance().gravar(dpPessoa);
								
    				lista1.clear();
    				lista1 = CpDao.getInstance().consultaIdentidadesPorCpf(dpPessoa.getCpfPessoa().toString());
    				
    				if(lista1.size() > 0) {
    					usuarioExiste = lista1.get(0);
    					usu = new CpIdentidade();
    					usu.setCpTipoIdentidade(CpDao.getInstance().consultar(1,
    										CpTipoIdentidade.class, false));
    					usu.setDscSenhaIdentidade(usuarioExiste.getDscSenhaIdentidade());
    					usu.setDtCriacaoIdentidade(CpDao.getInstance()
    							.consultarDataEHoraDoServidor());
    					usu.setCpOrgaoUsuario(dpPessoa.getOrgaoUsuario());
    					usu.setHisDtIni(usu.getDtCriacaoIdentidade());
    					usu.setHisAtivo(1);
    					
        				if(usu != null) {
        					usu.setNmLoginIdentidade(dpPessoa.getSesbPessoa() + dpPessoa.getMatricula());
        					usu.setDpPessoa(dpPessoa);
        					CpDao.getInstance().gravarComHistorico(usu, identidade);
        				}
    				}
				}
            	CpDao.getInstance().em().getTransaction().commit();				    		
			}
		} catch (Exception e) {														
			if (CpDao.getInstance().em().getTransaction().isActive()) {
				CpDao.getInstance().em().getTransaction().rollback();
			}			
			if (e.getCause() != null && e.getCause().toString().contains("ConstraintViolationException")) {
				throw new SigaConstraintViolationException("Identificado uma violação de integridade no banco de dados." 
						+ " Isso pode ocorrer ao gravar um registro que já exista.<br/>" 
						+ " Por segurança, todo o processo foi cancelado e nenhum registro foi gravado.<br/>"							
						+ " Favor analisar a planilha e se certificar de que na mesma,"  
						+ " não exista nenhuma Pessoa que já esteja cadastrada no sistema e tente novamente.");
			}			
			throw new AplicacaoException("Erro na gravação", 0, e);						        									        
        }
    	if(problemas == null || "".equals(problemas.toString())) {
    		return null;
    	}
    	inputStream = new ByteArrayInputStream(problemas.toString().getBytes());
    	return inputStream;
    }
    
    public String validarData(String date, Integer linha) {
    	return validarData(date, linha, "");
    }
    
    public String validarData(String date, Integer linha, String nomeCampo) {
    	date = (date == null ? "" : date).replaceAll("[^0-9]*", "");
    	String campo = StringUtils.isEmpty(nomeCampo) ? "NASCIMENTO" : nomeCampo;  
    	  
        if (date.length() == 8) {  
            Integer dia = Integer.valueOf(date.substring(0, 2));  
            Integer mes = Integer.valueOf(date.substring(2, 4)) - 1;  
            Integer ano = Integer.valueOf(date.substring(4, 8));  
  
            if (mes < 0 || mes > 11) {  
            	return "Linha " + linha + ": DATA DE " + campo.toUpperCase() + " inválida" + System.lineSeparator();
            }  
  
            GregorianCalendar calendar = new GregorianCalendar();  
            calendar.set(ano, mes, 1);  
  
            if (dia <= 0 || dia > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {  
            	return "Linha " + linha + ": DATA DE " + campo.toUpperCase() + " inválida" + System.lineSeparator();
            }  
 
            return "";  
        }  
  
        return "Linha " + linha + ": DATA DE " + campo.toUpperCase() + " inválida" + System.lineSeparator();
    }
    
    public String validarNomePessoa(String nomePessoa, Integer linha, Integer tamanho) {
    	
		if("".equals(nomePessoa)) {
			return "Linha " + linha +": NOME em branco" + System.lineSeparator();
		} 
		
		if(nomePessoa.length() > tamanho){
			return "Linha " + linha +": NOME com mais de 60 caracteres" + System.lineSeparator();
		}

		if(nomePessoa != null && !nomePessoa.matches(Texto.DpPessoa.NOME_REGEX_CARACTERES_PERMITIDOS)) {
			return "Linha " + linha +": NOME com caracteres não permitidos" + System.lineSeparator();
		}
		return "";
	}    
    
    public String validarNomeAbreviado(String nomeAbreviado, Integer linha, Integer tamanho) {
    	
    	if(nomeAbreviado.length() > tamanho){
			return "Linha " + linha +": NOME ABREVIADO com mais de 40 caracteres" + System.lineSeparator();
		}
    	
    	if(nomeAbreviado != null && !"".equals(nomeAbreviado.trim()) && !nomeAbreviado.matches(Texto.DpPessoa.NOME_REGEX_CARACTERES_PERMITIDOS)) {
			return "Linha " + linha +": NOME ABREVIADO com caracteres não permitidos" + System.lineSeparator();
		}
    	return "";
    }
    
    public boolean validarCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
            CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") ||
            (CPF.length() != 11))
            return(false);
          
        char dig10, dig11;
        int sm, i, r, num, peso;
          
        try {
        // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {              
	            num = (int)(CPF.charAt(i) - 48); 
	            sm = sm + (num * peso);
	            peso = peso - 1;
            }
          
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico
          
        // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
	            num = (int)(CPF.charAt(i) - 48);
	            sm = sm + (num * peso);
	            peso = peso - 1;
            }
          
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                 dig11 = '0';
            else dig11 = (char)(r + 48);
          
        // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                 return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }
}