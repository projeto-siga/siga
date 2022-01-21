/**
 * PRODESP - Cia de Processamento de Dados do Estado de S�o Paulo  <br>--------------------------------------<br>Servi�o para comunica��o com sistema SIAFEM/SIAFISICO, utilizando mensagens XML;<br><br>10/02/2020 v8.01.015(g)- MWAS - Inclus�o mensagens: SIAFNLRETIR, SIAFNLDEVOLVIR, SIAFNLTRANSFIR, SIAFEMALTERACAOPROCESSONL<br>20/02/2020 v8.01.016(a)- MWAS - Inclus�o mensagens: SIAFLISTAOBNCUMP, SIAFINCDOMCREDWEB12/03/2020 v8.02.001(a)- MWAS - Migra��o do codigo para novo repositorio, simplificando a manuten��o do servi�o.02/07/2020 v8.02.002(a)- MWAS - Altera��o acesso servidor com SSL, devido problema certificado SEFAZ.31/07/2020 v8.02.003(d )- MWAS - Inclusao de LOG de Acesso e lista negra de IP
 * 
 */
@javax.xml.bind.annotation.XmlSchema(namespace = "https://www.bec.sp.gov.br/SIAFISICO/RecebeMSG/", elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)
package br.gov.jfrj.siga.integracao.ws.siafem;
