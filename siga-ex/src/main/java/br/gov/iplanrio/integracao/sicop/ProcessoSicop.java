package br.gov.iplanrio.integracao.sicop;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebParam;

import org.apache.commons.lang3.StringUtils;

public class ProcessoSicop {
	
	private String rotina;
	private String opcao;
	private String numprocesso;
	private String dataproc;
	private String orgaoorigem;
	private String tipodoc;
	private String numdoc = "";
	private String orgaodoc;
	private String tipoident;
	private String nrdocidentificador;
	private String codassunto;
	private String nomrequerente;
	private String autoinfracao="";
	private String placaveiculo="";
	private String tppessoa="";
	private String numcpfcgc="";
	private String codlogradouro="";
	private String endereco="";
	private String num="";
	private String complemento="";
	private String cep="";
	private String codcep="";
	private String bairro="";
	private String ddi="";
	private String ddd="";
	private String telefone="";
	private String ramal="";
	private String email="";
	private String infcompl1="";
	private String infcompl2="";
	private String infcompl3="";
	private String nomnomeparte="";
	private String enderecoparte="";
	private String numparte="";
	private String complementoparte="";
	private String bairroparte="";
	private String cepparte="";
	private String codlograparte="";
	private String telefoneparte="";
	private String qlfparte="";
	private String dataprocuracaoparte="";
	private String tipoidentparte="";
	private String nrdocidentificadorparte="";
	private String matrrecebdor;
	private String impressora="";
	private String indconfirma;
    

	@Deprecated
	public ProcessoSicop() {
		
		this.opcao = "1";
     	this.orgaoorigem = "99000000";
		this.tipodoc = "2";// REQUERIMENTO
		this.numdoc = "";
		this.orgaodoc = "00000000";
		this.tipoident = "10";
		this.nrdocidentificador = "9";
		this.nomrequerente = "ProcessoRio-Integracao";
		this.indconfirma="1";
		this.codassunto="05028";
	}

	public ProcessoSicop( String numeroProcesso,   String observacao, String matrrecebdor, String codassunto ) {

		this();
		
		this.numprocesso=  numeroProcesso;

		this.dataproc =  new SimpleDateFormat("ddMMyyyy").format(new Date());
		
		this.matrrecebdor = matrrecebdor;
		
		this.infcompl1 = observacao;
		
		this.codassunto = codassunto;
	}

	public String getRotina() {
		return rotina;
	}

	public String getOpcao() {
		return opcao;
	}

	public String getNumprocesso() {
		return numprocesso;
	}

	public String getDataproc() {
		return dataproc;
	}

	public String getOrgaoorigem() {
		return orgaoorigem;
	}

	public String getTipodoc() {
		return tipodoc;
	}

	public String getNumdoc() {
		return numdoc;
	}

	public String getOrgaodoc() {
		return orgaodoc;
	}

	public String getTipoident() {
		return tipoident;
	}

	public String getNrdocidentificador() {
		return nrdocidentificador;
	}

	public String getCodassunto() {
		return codassunto;
	}

	public String getNomrequerente() {
		return nomrequerente;
	}

	public String getAutoinfracao() {
		return autoinfracao;
	}

	public String getPlacaveiculo() {
		return placaveiculo;
	}

	public String getTppessoa() {
		return tppessoa;
	}

	public String getNumcpfcgc() {
		return numcpfcgc;
	}

	public String getCodlogradouro() {
		return codlogradouro;
	}

	public String getEndereco() {
		return endereco;
	}

	public String getNum() {
		return num;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getCep() {
		return cep;
	}

	public String getCodcep() {
		return codcep;
	}

	public String getBairro() {
		return bairro;
	}

	public String getDdi() {
		return ddi;
	}

	public String getDdd() {
		return ddd;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getRamal() {
		return ramal;
	}

	public String getEmail() {
		return email;
	}

	public String getInfcompl1() {
		return infcompl1;
	}

	public String getInfcompl2() {
		return infcompl2;
	}

	public String getInfcompl3() {
		return infcompl3;
	}

	public String getNomnomeparte() {
		return nomnomeparte;
	}

	public String getEnderecoparte() {
		return enderecoparte;
	}

	public String getNumparte() {
		return numparte;
	}

	public String getComplementoparte() {
		return complementoparte;
	}

	public String getBairroparte() {
		return bairroparte;
	}

	public String getCepparte() {
		return cepparte;
	}

	public String getCodlograparte() {
		return codlograparte;
	}

	public String getTelefoneparte() {
		return telefoneparte;
	}

	public String getQlfparte() {
		return qlfparte;
	}

	public String getDataprocuracaoparte() {
		return dataprocuracaoparte;
	}

	public String getTipoidentparte() {
		return tipoidentparte;
	}

	public String getNrdocidentificadorparte() {
		return nrdocidentificadorparte;
	}

	public String getMatrrecebdor() {
		return matrrecebdor;
	}

	public String getImpressora() {
		return impressora;
	}

	public String getIndconfirma() {
		return indconfirma;
	}

	 
	
}
