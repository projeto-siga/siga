/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.ex.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import br.gov.jfrj.siga.Remote;

@WebService(targetNamespace = "http://impl.service.ex.siga.jfrj.gov.br/")
public interface ExService extends Remote {

    @WebMethod
	public Boolean transferir(String codigoDocumentoVia, String siglaDestino,
			String siglaCadastrante, Boolean forcarTransferencia) throws Exception;

    @WebMethod
	public Boolean isAssinado(String codigoDocumento, String siglaCadastrante)
			throws Exception;

    @WebMethod
	public Boolean isSemEfeito(String codigoDocumento)
			throws Exception;


    @WebMethod
	public Boolean arquivarCorrente(String codigoDocumentoVia,
			String siglaDestino, String siglaCadastrante) throws Exception;

    @WebMethod
	public Boolean podeMovimentar(String codigoDocumento,
			String siglaCadastrante) throws Exception;

    @WebMethod
	public Boolean podeTransferir(String codigoDocumento,
			String siglaCadastrante, Boolean forcarTransferencia) throws Exception;

    @WebMethod
	public Boolean isAtendente(String codigoDocumento, String siglaCadastrante)
			throws Exception;

    @WebMethod
	public String getAtendente(String codigoDocumento, String siglaTitular)
			throws Exception;

    @WebMethod
	public byte[] obterPdfPorNumeroAssinatura(String num) throws Exception;

    @WebMethod
	public String buscarPorCodigo(String codigo) throws Exception;

    @WebMethod
	public String criarVia(String codigoDocumento, String siglaCadastrante) throws Exception;

    @WebMethod
	public String form(String codigoDocumento, String variavel) throws Exception;

    @WebMethod
	public String toJSON(String codigo) throws Exception;

    @WebMethod
	public Boolean exigirAnexo(String codigoDocumentoVia, String siglaCadastrante,
			String descricaoDoAnexo) throws Exception;
	
	public String criarDocumento(String cadastranteStr, String subscritorStr, String destinatarioStr, String destinatarioCampoExtraStr, String descricaoTipoDeDocumento, String nomeForma ,String nomeModelo, String classificacaoStr, 
			String descricaoStr, Boolean eletronico, String nomeNivelDeAcesso, String conteudo, String siglaMobilPai, Boolean finalizar) throws Exception;
	
    @WebMethod
	public String obterNumeracaoExpediente(Long idOrgaoUsu, Long idFormaDoc, Long anoEmissao) throws Exception;
    
    @WebMethod
	public String obterSequencia(Integer tipoSequencia, Long anoEmissao, String zerarInicioAnno) throws Exception;
    
    @WebMethod
    public String obterSiglaMobilPorIdDoc(Long idDoc) throws Exception;
    
    @WebMethod
    public String obterMetadadosDocumento(String siglaDocumento, String token) throws Exception;
    
    @WebMethod
    public String obterMarcadores(String token) throws Exception;
    
    @WebMethod
    public String publicarDocumentoPortal(String siglaDocumento, String cadastranteStr, String marcadoresStr, String token) throws Exception;


    @WebMethod
	public String cadastrante(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String titular(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String subscritor(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String destinatario(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String gestor(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String fiscalTecnico(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String fiscalAdministrativo(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String interessado(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String autorizador(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String revisor(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public String liquidante(String codigoDocumentoVia) throws Exception;

    @WebMethod
	public Boolean isModeloIncluso(String codigoDocumentoVia, Long idModelo) throws Exception;
}
