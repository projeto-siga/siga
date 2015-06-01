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
package br.gov.jfrj.siga;

import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;

public class AxisClientAlternativo {

	public static QName QNAME_TIPO_BINARIO_64 = new QName(
			"http://www.w3.org/2001/XMLSchema", "base64Binary");

	private String endpoint;

	private String namespace;

	private String methodname;

	private QName returnType;

	private Object[] param;

	private Call call;

	public Object[] getParam() {
		return param;
	}

	public void setParam(Object[] param) {
		this.param = param;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public QName getReturnType() {
		return returnType;
	}

	public void setReturnType(QName returnType) {
		this.returnType = returnType;
	}

	public AxisClientAlternativo(String endpoint, String namespace,
			String methodName, boolean sendBinary64) throws Exception {
		setEndpoint(endpoint);
		setNamespace(namespace);
		setMethodname(methodName);
		setup(sendBinary64);
	}

	public AxisClientAlternativo(String endpoint, String methodName,
			boolean sendBinary64) throws Throwable {
		this(endpoint, "http://tempuri.org/", methodName, sendBinary64);
	}

	public AxisClientAlternativo(String endpoint, String methodName)
			throws Throwable {
		this(endpoint, methodName, false);
	}

	private void setup(boolean sendBinary64) throws Exception {
		Service service = new Service();

		try {
			System.out.print("DJE: criando ClientAlternativo");
			OperationDesc oper = new OperationDesc();
			oper.setName(getMethodname());

			if (sendBinary64) {
				ParameterDesc param = new ParameterDesc(new QName(
						getNamespace(), "_arrBytArquivoZip"), ParameterDesc.IN,
						QNAME_TIPO_BINARIO_64, byte[].class, false, false);
				param.setOmittable(true);
				oper.addParameter(param);
				oper.setReturnType(QNAME_TIPO_BINARIO_64);
			}

			// define o tipo de retorno
			// oper.setReturnClass(byte[].class);
			// oper.setReturnQName(new QName(getNamespace(),
			// "RecebeDocumentosResult"));
			oper.setStyle(Style.WRAPPED);
			oper.setUse(Use.LITERAL);

			call = (Call) service.createCall();
			call.setOperation(oper);
			call.setTargetEndpointAddress(new URL(getEndpoint()));
			call.setOperationName(new QName(getNamespace(), getMethodname()));
			call.setProperty(Call.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
			call.setProperty(Call.SOAPACTION_URI_PROPERTY, getNamespace()
					+ getMethodname());
		} catch (Exception e) {
			throw new Exception("Ocorreu um erro ao criar a chamada", e);
		}
	}

	public AxisClientAlternativo() throws Exception {
		this(null, null, null, false);
	}

	public Object call() throws Exception {
		try {
			return call.invoke(getParam());
		} catch (Throwable e) {
			System.out.println("DJE: erro " + e.getMessage());
			throw new Exception("Ocorreu um erro ao executar a chamada: " + e.getMessage(), e);
		}
	}

	/**
	 * @param zipData
	 */
	/*
	 * public static void testar(byte[] zipData) { try { Service service = new
	 * Service(); // operação------------------------------------------------
	 * 
	 * OperationDesc oper; ParameterDesc param; oper = new OperationDesc();
	 * oper.setName("RecebeDocumentos"); // define o parâmetro que o webservice
	 * espera receber param = new ParameterDesc(new QName("http://tempuri.org/",
	 * "_arrBytArquivoZip"), ParameterDesc.IN, new QName(
	 * "http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false,
	 * false); param.setOmittable(true); // adiciona o parâmetro na operação
	 * oper.addParameter(param); oper.setReturnType(new
	 * QName("http://www.w3.org/2001/XMLSchema", "base64Binary")); // define o
	 * tipo de retorno oper.setReturnClass(byte[].class);
	 * oper.setReturnQName(new QName("http://tempuri.org/",
	 * "RecebeDocumentosResult")); oper.setStyle(Style.WRAPPED);
	 * oper.setUse(Use.LITERAL); //
	 * call-------------------------------------------------- Call call = (Call)
	 * service.createCall(); call.setOperation(oper); call
	 * .setTargetEndpointAddress(new URL(
	 * "http://djewebtrf.jf.trf2.gov.br:80/DJE.WebServices/WsPrimeiraInstancia.asmx"));
	 * call.setOperationName(new QName("http://tempuri.org/",
	 * "RecebeDocumentos")); call.setProperty(Call.SOAPACTION_USE_PROPERTY,
	 * Boolean.TRUE); call.setProperty(Call.SOAPACTION_URI_PROPERTY,
	 * "http://tempuri.org/RecebeDocumentos"); Object o = call.invoke(new
	 * Object[] { zipData }); byte[] b = (byte[]) o; String s = new String(b); }
	 * catch (MalformedURLException e) { 
	 * e.printStackTrace(); } catch (RemoteException e) { 
	 * catch block e.printStackTrace(); } catch (ServiceException e) { 
	 * Auto-generated catch block e.printStackTrace(); } }
	 */
}
