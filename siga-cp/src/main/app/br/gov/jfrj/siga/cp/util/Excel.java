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
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpTipoIdentidade;
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

public class Excel {
	
	public InputStream uploadLotacao(File file, CpOrgaoUsuario orgaoUsuario, String extensao) {
		InputStream retorno = null;
		if(".txt".equalsIgnoreCase(extensao) || ".csv".equalsIgnoreCase(extensao)) {
			retorno = uploadCVS(file, orgaoUsuario);
		} else if(".xlsx".equals(extensao)){
			retorno = uploadExcelLotacao(file, orgaoUsuario);
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
                	problemas.append("Linha " + linha +": Quantidade de registros da linha inválida" + System.getProperty("line.separator"));
                }
                
                //NOME DA LOTACAO
                campo = parts[0];
                problemas.append(validarNomeLotacao(nomes, campo.trim(), orgaoUsuario, linha, lotacao, 100));
				
				//SIGLA DA LOTACAO
				campo = parts[1];
				problemas.append(validarSiglaLotacao(siglas, campo.trim(), orgaoUsuario, linha, lotacao, 20));
				
				//LOCALIDADE DA LOTACAO
				if(parts.length < 3) {
                	problemas.append("Linha " + linha +": LOCALIDADE em branco" + System.getProperty("line.separator"));
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
			return "Linha " + linha +": NOME em branco" + System.getProperty("line.separator");
		} 
		
		if(nomeLotacao.length() > tamanho){
			return "Linha " + linha +": NOME com mais de 100 caracteres" + System.getProperty("line.separator");
		}

		if(nomeLotacao != null && !nomeLotacao.matches("[a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9.,/-]+")) {
			return "Linha " + linha +": NOME com caracteres não permitidos" + System.getProperty("line.separator");
		}
		nomes.add(Texto.removeAcento(Texto.removerEspacosExtra(nomeLotacao).trim().toUpperCase()));	

		return "";
	}
	
	public String validarSiglaLotacao(List<String>siglas, String siglaLotacao, CpOrgaoUsuario orgaoUsuario, Integer linha, DpLotacao lotacao, Integer tamanho) {
		if("".equals(siglaLotacao)) {
			return "Linha " + linha +": SIGLA em branco" + System.getProperty("line.separator");			
		} 
		
		if(siglaLotacao.length() > tamanho) {
			return "Linha " + linha +": SIGLA com mais de 20 caracteres" + System.getProperty("line.separator");
		}
		lotacao = new DpLotacao();
		lotacao.setOrgaoUsuario(orgaoUsuario);
		lotacao.setSigla(Texto.removeAcento(Texto.removerEspacosExtra(siglaLotacao).trim()));
		lotacao = CpDao.getInstance().consultarPorSigla(lotacao);
		if(lotacao != null) {
			return "Linha " + linha +": SIGLA já cadastrada" + System.getProperty("line.separator");
		}
		if(siglaLotacao != null && !siglaLotacao.matches("[a-zA-ZçÇ0-9,/-]+")) {
			return "Linha " + linha +": SIGLA com caracteres não permitidos" + System.getProperty("line.separator");
		} 
		
		if(siglas.contains(Texto.removeAcento(Texto.removerEspacosExtra(siglaLotacao).trim().toUpperCase()))) {
			return "Linha " + linha +": SIGLA repetida em outra linha do arquivo" + System.getProperty("line.separator");
		} else {
			siglas.add(Texto.removeAcento(Texto.removerEspacosExtra(siglaLotacao).trim().toUpperCase()));	
		}
		return "";
	}
    
	public String validarLocalidadeLotacao(List<CpLocalidade> localidades, Integer linha, CpLocalidade loc) {
		if("".equals(loc.getNmLocalidade())) {
			return "Linha " + linha +": LOCALIDADE em branco" + System.getProperty("line.separator");
		} else if(loc.getNmLocalidade() == null){
			return "Linha " + linha +": LOCALIDADE com mais de 256 caracteres" + System.getProperty("line.separator");
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
				return "Linha " + linha +": LOCALIDADE não cadastrada" + System.getProperty("line.separator");
			} else {
				localidades.add(localidade);
				loc = localidade;
			}
		}
		
		return "";
	}
	
    public InputStream uploadExcelLotacao(File file, CpOrgaoUsuario orgaoUsuario) {
    	InputStream inputStream = null;
    	String problemas = "";
        CpUF uf = new CpUF();
        uf.setIdUF(26L);
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
				
				//LOCALIDADE DA LOTACAO
				celula = retornaConteudo(row.getCell(2, Row.CREATE_NULL_AS_BLANK));
				loc.setUF(uf);
				loc.setNmLocalidade(celula);
				problemas += validarLocalidadeLotacao(localidades, linha, loc);
				
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
				try {
	            	for (DpLotacao dpLotacao : lista) {
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
			if(problemas == null || "".equals(problemas.toString())) {
	    		return null;
	    	}
	    	inputStream = new ByteArrayInputStream(problemas.getBytes("ISO-8859-1"));
		} catch (Exception ioe) {
            ioe.printStackTrace();
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
    
    public InputStream uploadFuncao(File file, CpOrgaoUsuario orgaoUsuario, String extensao) {
		InputStream retorno = null;
		retorno = uploadExcelFuncao(file, orgaoUsuario);

		return retorno;
	}
    
    public InputStream uploadExcelFuncao(File file, CpOrgaoUsuario orgaoUsuario) {
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
		            	CpDao.getInstance().iniciarTransacao();
		    			CpDao.getInstance().gravar(dpFuncaoConfianca);
		    			
	    				if(dpFuncaoConfianca.getIdFuncaoIni() == null && dpFuncaoConfianca.getId() != null) {
	    					dpFuncaoConfianca.setIdFuncaoIni(dpFuncaoConfianca.getId());
	    					dpFuncaoConfianca.setIdeFuncao(dpFuncaoConfianca.getId().toString());
	    					CpDao.getInstance().gravar(dpFuncaoConfianca);
	        			}
					}
	    			CpDao.getInstance().commitTransacao();			
	    		} catch (final Exception e) {
	    			CpDao.getInstance().rollbackTransacao();
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
			return "Linha " + linha +": NOME em branco" + System.getProperty("line.separator");
		} 
		
		if(nomeFuncao.length() > tamanho){
			return "Linha " + linha +": NOME com mais de 100 caracteres" + System.getProperty("line.separator");
		}
		funcao.setOrgaoUsuario(orgaoUsuario);
		funcao.setNomeFuncao(nomeFuncao);
		funcao = CpDao.getInstance().consultarPorNomeOrgao(funcao);
		if(funcao != null) {
			return "Linha " + linha +": NOME já cadastrado" + System.getProperty("line.separator");
		}
		if(!validarCaracterEspecial(nomeFuncao)) {
			return "Linha " + linha +": NOME com caracteres não permitidos" + System.getProperty("line.separator");
		}
		if(nomes.contains(Texto.removeAcento(Texto.removerEspacosExtra(nomeFuncao).trim().toUpperCase()))) {
			return "Linha " + linha +": NOME repetido em outra linha do arquivo" + System.getProperty("line.separator");
		} else {
			nomes.add(Texto.removeAcento(Texto.removerEspacosExtra(nomeFuncao).trim().toUpperCase()));	
		}
		return "";
	}
    
    public InputStream uploadCargo(File file, CpOrgaoUsuario orgaoUsuario, String extensao) {
		InputStream retorno = null;
		retorno = uploadExcelCargo(file, orgaoUsuario);

		return retorno;
	}
    
    public InputStream uploadExcelCargo(File file, CpOrgaoUsuario orgaoUsuario) {
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
		            	CpDao.getInstance().iniciarTransacao();
		    			CpDao.getInstance().gravar(dpCargo);
		    			
	    				if(dpCargo.getIdCargoIni() == null && dpCargo.getId() != null) {
	    					dpCargo.setIdCargoIni(dpCargo.getId());
	    					dpCargo.setIdeCargo(dpCargo.getId().toString());
	        				CpDao.getInstance().gravar(dpCargo);
	        			}
					}
	    			CpDao.getInstance().commitTransacao();			
	    		} catch (final Exception e) {
	    			CpDao.getInstance().rollbackTransacao();
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
			return "Linha " + linha +": NOME em branco" + System.getProperty("line.separator");
		} 
		
		if(nomeCargo.length() > tamanho){
			return "Linha " + linha +": NOME com mais de 100 caracteres" + System.getProperty("line.separator");
		}
		cargo.setOrgaoUsuario(orgaoUsuario);
		cargo.setNomeCargo(nomeCargo);
		cargo = CpDao.getInstance().consultarPorNomeOrgao(cargo);
		if(cargo != null) {
			return "Linha " + linha +": NOME já cadastrado" + System.getProperty("line.separator");
		}
		if(!validarCaracterEspecial(nomeCargo)) {
			return "Linha " + linha +": NOME com caracteres não permitidos" + System.getProperty("line.separator");
		}
		if(nomes.contains(Texto.removeAcento(Texto.removerEspacosExtra(nomeCargo).trim()).toUpperCase())) {
			return "Linha " + linha +": NOME repetido em outra linha do arquivo" + System.getProperty("line.separator");
		} else {
			nomes.add(Texto.removeAcento(Texto.removerEspacosExtra(nomeCargo).trim().toUpperCase()));	
		}
		return "";
	}
    
    public InputStream uploadPessoa(File file, CpOrgaoUsuario orgaoUsuario, String extensao, CpIdentidade identidade) {
		InputStream retorno = null;
		retorno = uploadExcelPessoa(file, orgaoUsuario, identidade);

		return retorno;
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
			SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy");
			Date date = null;
			
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
				
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;
				
				//SIGLA DO ORGAO				
				celula = retornaConteudo(row.getCell(0, Row.CREATE_NULL_AS_BLANK));
				if("".equals(celula.trim())) {
					problemas.append("Linha " + linha +": ÓRGÃO em branco" + System.getProperty("line.separator"));
				} else {
					if(!celula.equalsIgnoreCase(orgaoUsuario.getSiglaOrgaoUsu())) {
						problemas.append("Linha " + linha +": ÓRGÃO inválido" + System.getProperty("line.separator"));
					}
				}

				//NOME DO CARGO
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
						problemas.append("Linha " + linha +": CARGO não cadastrado" + System.getProperty("line.separator"));
					} else {
						listaCargo.add(cargo);
					}
					if(cargo != null && cargo.getDataFimCargo() != null) {
						problemas.append("Linha " + linha +": CARGO inativo" + System.getProperty("line.separator"));
					}
				} else {
					problemas.append("Linha " + linha +": CARGO em branco" + System.getProperty("line.separator"));
				}
				
				//NOME FUNCAO DE CONFIANCA
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
						problemas.append("Linha " + linha +": FUNÇÃO DE CONFIANÇA não cadastrada" + System.getProperty("line.separator"));
					} else {
						listaFuncao.add(funcao);
					}
					
					if(funcao != null && funcao.getDataFimFuncao() != null) {
						problemas.append("Linha " + linha +": FUNÇÃO DE CONFIANÇA inativa" + System.getProperty("line.separator"));
					}
				} else {
					funcao = null;
				}
				
				//NOME DA LOTACAO
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
						problemas.append("Linha " + linha +": LOTAÇÃO não cadastrado" + System.getProperty("line.separator"));
					} else {
						listaLotacao.add(lotacao);
					}
					
					if(lotacao != null && lotacao.getDataFimLotacao() != null) {
						problemas.append("Linha " + linha +": LOTAÇÃO inativo" + System.getProperty("line.separator"));
					}
				} else {
					problemas.append("Linha " + linha +": LOTAÇÃO em branco" + System.getProperty("line.separator"));
				}
				
				//NOME DA PESSOA
				celula = retornaConteudo(row.getCell(4, Row.CREATE_NULL_AS_BLANK));
				problemas.append(validarNomePessoa(celula.trim(), linha, 60));
				
				if(problemas == null || "".equals(problemas.toString())) {
					pe.setNomePessoa(celula.trim());
				}
				
				//DATA DE NASCIMENTO
				if(retornaConteudo(row.getCell(5, Row.CREATE_NULL_AS_BLANK)) != "") {
					if(row.getCell(5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(row.getCell(5, Row.CREATE_NULL_AS_BLANK))){
						date = row.getCell(5).getDateCellValue();
						
						if(date.compareTo(new Date()) > 0) {
			    			problemas.append( "Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.getProperty("line.separator"));
			    		}
					} else if(row.getCell(5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						problemas.append(validarData(String.valueOf(((Double)row.getCell(5, Row.CREATE_NULL_AS_BLANK).getNumericCellValue()).longValue()), linha));
						if(problemas != null && problemas.toString().equals("")) {
							dataString = String.valueOf(((Double)row.getCell(5).getNumericCellValue()).longValue()).replaceAll("[^0-9]", "");
							date = formato.parse(dataString);	
							
							if(date.compareTo(new Date()) > 0) {
				    			problemas.append( "Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.getProperty("line.separator"));
				    		}
						}
						
					} else if(row.getCell(5).getCellType() == HSSFCell.CELL_TYPE_STRING) {
						problemas.append(validarData(row.getCell(5, Row.CREATE_NULL_AS_BLANK).getStringCellValue(), linha));
						if(problemas != null && problemas.toString().equals("")) {
							dataString = row.getCell(5, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
							date = formato.parse(dataString.replace("-", "").replace("/", "").trim());	
							
							if(date.compareTo(new Date()) > 0) {
				    			problemas.append( "Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.getProperty("line.separator"));
				    		}
						}
						
					} else {
						problemas.append("Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.getProperty("line.separator"));
					}
				}
				//CPF
				celula = retornaConteudo(row.getCell(6, Row.CREATE_NULL_AS_BLANK));
				cpf = celula.replaceAll("[^0-9]", "");
				cpf = StringUtils.leftPad(cpf, 11, "0");
				if(!"".equals(cpf.trim())) {
					if(!validarCPF(cpf)) {
						problemas.append("Linha " + linha +": CPF inválido" + System.getProperty("line.separator"));
					}
				} else {
					problemas.append("Linha " + linha +": CPF em branco" + System.getProperty("line.separator"));
				}
				
				//EMAIL
				celula = retornaConteudo(row.getCell(7, Row.CREATE_NULL_AS_BLANK)).trim();
				
				if(!"".equals(celula.trim())) {
					
					if(celula.length() > 60) {
						problemas.append("Linha " + linha +": E-MAIL com mais de 60 caracteres" + System.getProperty("line.separator"));
					}
					
					if(celula.contains(" ")) {
						problemas.append("Linha " + linha +": E-MAIL com espaço em branco" + System.getProperty("line.separator"));
					} else if(!celula.matches("[a-zA-Z0-9._-][a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")) {
						problemas.append("Linha " + linha +": E-MAIL inválidos ou com caracteres inválidos" + System.getProperty("line.separator"));
					}
				} else {
					problemas.append("Linha " + linha +": E-MAIL em branco" + System.getProperty("line.separator"));
				}
				
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
						problemas.append("Linha " + linha +": Usuário já cadastrado com estes dados: Órgão, Cargo, Função, Unidade e CPF" + System.getProperty("line.separator"));
					}
				}
				if(!lista.isEmpty()) {
					for (DpPessoa p : lista) { //repetido em outra linha do arquivo
						if(p.getCpfPessoa().equals(Long.valueOf(cpf)) && p.getCargo().equals(cargo) && p.getLotacao().equals(lotacao) &&
								((p.getFuncaoConfianca() == null && funcao == null) || (p.getFuncaoConfianca() != null && p.getFuncaoConfianca().equals(funcao)))) {
							problemas.append("Linha " + linha +": Usuário repetido em outra linha do arquivo com estes dados: Órgão, Cargo, Função, Unidade e CPF" + System.getProperty("line.separator"));
						}
					}
				}
				
				if(problemas == null || "".equals(problemas.toString())) {
					pe.setEmailPessoa(celula);
					pe.setDataNascimento(date);
					pe.setCpfPessoa(Long.valueOf(cpf));
					pe.setOrgaoUsuario(orgaoUsuario);
					pe.setLotacao(lotacao);
					pe.setCargo(cargo);
					pe.setFuncaoConfianca(funcao);
					pe.setEmailPessoa(celula);
					pe.setDataInicio(data);
					pe.setSesbPessoa(orgaoUsuario.getSigla());
					pe.setMatricula(Long.valueOf(0));
					pe.setSituacaoFuncionalPessoa(SituacaoFuncionalEnum.APENAS_ATIVOS.getValor()[0]);
					lista.add(pe);
				}
			}
			if(problemas == null || "".equals(problemas.toString())) {
				try {
					CpDao.getInstance().iniciarTransacao();
					CpIdentidade usu = null;
					CpIdentidade usuarioExiste = null;
					List<CpIdentidade> lista1 = new ArrayList<CpIdentidade>();
	            	for (DpPessoa dpPessoa : lista) {
		    			CpDao.getInstance().gravar(dpPessoa);

	    				if(dpPessoa.getIdPessoaIni() == null && dpPessoa.getId() != null) {
	    					dpPessoa.setIdPessoaIni(dpPessoa.getId());
	    					dpPessoa.setIdePessoa(dpPessoa.getId().toString());
	    					dpPessoa.setMatricula(10000 + dpPessoa.getId());	
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
					}
	    			CpDao.getInstance().commitTransacao();			
	    		} catch (final Exception e) {
	    			CpDao.getInstance().rollbackTransacao();
	    			throw new AplicacaoException("Erro na gravação", 0, e);
	    		}
			}
		} catch (Exception ioe) {
            ioe.printStackTrace();
        }
    	if(problemas == null || "".equals(problemas.toString())) {
    		return null;
    	}
    	inputStream = new ByteArrayInputStream(problemas.toString().getBytes());
    	return inputStream;
    }
    
    public String validarData(String date, Integer linha) {
    	date = (date == null ? "" : date).replaceAll("[^0-9]*", "");  
    	  
        if (date.length() == 8) {  
            Integer dia = Integer.valueOf(date.substring(0, 2));  
            Integer mes = Integer.valueOf(date.substring(2, 4)) - 1;  
            Integer ano = Integer.valueOf(date.substring(4, 8));  
  
            if (mes < 0 || mes > 11) {  
            	return "Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.getProperty("line.separator");
            }  
  
            GregorianCalendar calendar = new GregorianCalendar();  
            calendar.set(ano, mes, 1);  
  
            if (dia <= 0 || dia > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {  
            	return "Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.getProperty("line.separator");
            }  
 
            return "";  
        }  
  
        return "Linha " + linha + ": DATA DE NASCIMENTO inválida" + System.getProperty("line.separator");
    }
    
    public String validarNomePessoa(String nomePessoa, Integer linha, Integer tamanho) {
    	
		if("".equals(nomePessoa)) {
			return "Linha " + linha +": NOME em branco" + System.getProperty("line.separator");
		} 
		
		if(nomePessoa.length() > tamanho){
			return "Linha " + linha +": NOME com mais de 60 caracteres" + System.getProperty("line.separator");
		}

		if(nomePessoa != null && !nomePessoa.matches("[a-zA-ZáâãéêíóôõúçÁÂÃÉÊÍÓÔÕÚÇ'' ]+")) {
			return "Linha " + linha +": NOME caracteres não permitidos" + System.getProperty("line.separator");
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