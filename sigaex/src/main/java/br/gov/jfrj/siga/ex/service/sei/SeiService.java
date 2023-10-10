/**
 * SeiService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public interface SeiService extends javax.xml.rpc.Service {
    public java.lang.String getSeiPortServiceAddress();

    public br.gov.jfrj.siga.ex.service.sei.SeiPortType getSeiPortService() throws javax.xml.rpc.ServiceException;

    public br.gov.jfrj.siga.ex.service.sei.SeiPortType getSeiPortService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
