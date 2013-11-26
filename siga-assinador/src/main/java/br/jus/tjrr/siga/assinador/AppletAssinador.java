package br.jus.tjrr.siga.assinador;

import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyStore;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import netscape.javascript.JSObject;

public class AppletAssinador extends Applet {

    private Logger logger = Logger.getLogger(AppletAssinador.class.getName());

    private KeyStore keyStore = null;

    private Signature signature = null;

    private JButton btnAssinar;

    private JSObject jsObject;

    @Override
    public void init() {
        super.init();
        jsObject = (JSObject) JSObject.getWindow(AppletAssinador.this);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Falha ao carregar o layout.", e);
        }

        btnAssinar = new JButton();
        btnAssinar.setLocation(0, 0);
        btnAssinar.setText("Assinar");
        btnAssinar.setSize(130,20);
        btnAssinar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                assinar();
            }
        });

        add(btnAssinar);
        setLayout(null);
    }


    public void assinar() {
        Assinador assinador = new Assinador();
        Configuracao configuracao = new Configuracao();
        char[] password = null;

        configuracao.setHabilitaJanelaParaSelecaoCertificado(true);

        if (!assinador.isWindows()) {
            //perguntar senha do keystore
            DialogoSenha dialogoSenha = new DialogoSenha();
            password = dialogoSenha.getResult();
        }

        try {
            keyStore = assinador.carregarKeyStore(password);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Falha em obter o KeyStore.", e);
        }

        List<String> aliases = assinador != null ? assinador.listAliases() : null;

        String aliasEscolhido = null;
        if (aliases == null || aliases.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum certificado válido encontrado.");
        } else {
            if (aliases.size() == 1) {
               //não precisa perguntar pelo alias válido
                aliasEscolhido = aliases.get(0);
            } else {
               //exibe janela para selecionar o alias válido
                DialogoAlias dialogoAlias = new DialogoAlias(aliases);
                aliasEscolhido = dialogoAlias.getResult();
            }
        }

    }

    private Object chamarMetodoJS(String metodo) {
        return jsObject.eval(metodo);
    }

    private Object chamarMetodoJS(String metodo, Object[] parametros) {
        return jsObject.call(metodo, parametros);
    }

}
