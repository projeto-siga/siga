package br.gov.jfrj.siga.cp.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;

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
        String problemas = "";
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
                	problemas += "Linha " + linha +": Quantidade de registros da linha inválida\n";
                }
                
                //NOME DA LOTACAO
                campo = parts[0];
                problemas += validarNomeLotacao(nomes, campo.trim(), orgaoUsuario, linha, lotacao, 100);
				
				//SIGLA DA LOTACAO
				campo = parts[1];
				problemas += validarSiglaLotacao(siglas, campo.trim(), orgaoUsuario, linha, lotacao, 20);
				
				//LOCALIDADE DA LOTACAO
				if(parts.length < 3) {
                	problemas += "Linha " + linha +": Localidade em branco\n";
                	continue;
                }
				campo = parts[2];
				localidade.setUF(uf);
				localidade.setNmLocalidade(campo);
				problemas += validarLocalidadeLotacao(localidades, linha, localidade);
							
				if(problemas == null || "".equals(problemas)) {
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
            if(problemas == null || "".equals(problemas)) {
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
		if(problemas == null || "".equals(problemas)) {
			return null;
		}
    	return new ByteArrayInputStream(problemas.getBytes());
	}
	
	public String validarNomeLotacao(List<String> nomes, String nomeLotacao, CpOrgaoUsuario orgaoUsuario, Integer linha, DpLotacao lotacao, Integer tamanho) {
		if("".equals(nomeLotacao)) {
			return "Linha " + linha +": Nome em branco\n";
		} 
		
		if(nomeLotacao.length() > tamanho){
			return "Linha " + linha +": Nome com mais de 100 caracteres\n";
		}
		lotacao.setOrgaoUsuario(orgaoUsuario);
		lotacao.setNomeLotacao(Texto.removeAcento(Texto.removerEspacosExtra(nomeLotacao).trim()));
		lotacao = CpDao.getInstance().consultarPorNomeOrgao(lotacao);
		if(lotacao != null) {
			return "Linha " + linha +": Nome já cadastrado\n";
		}
		if(!validarCaracterEspecial(nomeLotacao)) {
			return "Linha " + linha +": Nome com caracteres especiais\n";
		}
		if(nomes.contains(nomeLotacao)) {
			return "Linha " + linha +": Nome repetido em outra linha do arquivo\n";
		} else {
			nomes.add(nomeLotacao);	
		}
		return "";
	}
	
	public String validarSiglaLotacao(List<String>siglas, String siglaLotacao, CpOrgaoUsuario orgaoUsuario, Integer linha, DpLotacao lotacao, Integer tamanho) {
		if("".equals(siglaLotacao)) {
			return "Linha " + linha +": Sigla em branco\n";			
		} 
		
		if(siglaLotacao.length() > tamanho) {
			return "Linha " + linha +": Sigla com mais de 20 caracteres\n";
		}
		lotacao = new DpLotacao();
		lotacao.setOrgaoUsuario(orgaoUsuario);
		lotacao.setSigla(Texto.removeAcento(Texto.removerEspacosExtra(siglaLotacao).trim()));
		lotacao = CpDao.getInstance().consultarPorSigla(lotacao);
		if(lotacao != null) {
			return "Linha " + linha +": Sigla já cadastrada\n";
		}
		if(!validarCaracterEspecial(siglaLotacao) || siglaLotacao.contains(" ")) {
			return "Linha " + linha +": Sigla com caracteres especiais\n";
		} 
		
		if(siglas.contains(siglaLotacao)) {
			return "Linha " + linha +": Sigla repetida em outra linha do arquivo\n";
		} else {
			siglas.add(siglaLotacao);	
		}
		return "";
	}
    
	public String validarLocalidadeLotacao(List<CpLocalidade> localidades, Integer linha, CpLocalidade loc) {
		if("".equals(loc.getNmLocalidade())) {
			return "Linha " + linha +": Localidade em branco\n";
		} else if(loc.getNmLocalidade() == null){
			return "Linha " + linha +": Localidade com mais de 256 caracteres\n";
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
				return "Linha " + linha +": Localidade não cadastrada\n";
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
				
				if(problemas == null || "".equals(problemas)) {
					lot.setNomeLotacao(celula.trim());
				}

				//SIGLA DA LOTACAO
				celula = retornaConteudo(row.getCell(1, Row.CREATE_NULL_AS_BLANK));
				problemas += validarSiglaLotacao(siglas, celula.trim(), orgaoUsuario, linha, lotacao, 20);
				
				if(problemas == null || "".equals(problemas)) {
					lot.setSiglaLotacao(celula.toUpperCase().trim());
				}
				
				//LOCALIDADE DA LOTACAO
				celula = retornaConteudo(row.getCell(2, Row.CREATE_NULL_AS_BLANK));
				loc.setUF(uf);
				loc.setNmLocalidade(celula);
				problemas += validarLocalidadeLotacao(localidades, linha, loc);
				
				if(problemas == null || "".equals(problemas)) {
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
			if(problemas == null || "".equals(problemas)) {
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
		} catch (Exception ioe) {
            ioe.printStackTrace();
        }
    	if(problemas == null || "".equals(problemas)) {
    		return null;
    	}
    	inputStream = new ByteArrayInputStream(problemas.getBytes());
    	return inputStream;
    }
    
    public Boolean validarCaracterEspecial(String celula) {
    	Boolean retorno = Boolean.TRUE;
    	if(!celula.matches("[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ 0-9]+")) {
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
				retorno = String.valueOf((int)cell.getNumericCellValue());
				break; 
			case Cell.CELL_TYPE_BLANK:
				retorno = "";
		} 
    	return retorno;
    }
}