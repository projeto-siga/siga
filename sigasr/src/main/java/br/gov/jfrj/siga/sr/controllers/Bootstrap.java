package br.gov.jfrj.siga.sr.controllers;

//@OnApplicationStart
public class Bootstrap {

    public void doJob() throws Exception {
        javassist.runtime.Desc.useContextClassLoader = true;

        System.out.println("\n\n\n*********************** INICIALIZANDO CLASSES *****************************");
        //
        // System.out.println(Play.classes
        // .hasClass("br.gov.jfrj.siga.cp.CpServico"));
        //
        // Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.CpOrgaoUsuario");
        // Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpCargo");
        // Play.classes
        // .getApplicationClass("br.gov.jfrj.siga.dp.DpFuncaoConfianca");
        // Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpLotacao");
        // Play.classes.getApplicationClass("br.gov.jfrj.siga.dp.DpPessoa");
        // Play.classes.getApplicationClass("br.gov.jfrj.siga.cp.CpServico");
        //
        // System.out.println(Play.classes
        // .hasClass("br.gov.jfrj.siga.cp.CpServico"));
        System.out.println("classes carregadas...");
        // }

    }
}