package br.jus.tjrr.siga.assinador;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class DialogoSenha extends JDialog implements ActionListener {


    private JFrame frame = new JFrame("Autenticando");
    private JPasswordField password = new JPasswordField();
    private JButton ok =  new JButton("OK");
    private JButton cancel = new JButton("Cancelar");
    private JLabel prompt = new JLabel("Entre com a senha do token:");
    JPanel passPanel = new JPanel();
    JPanel labelInput = new JPanel();
    JPanel buttons = new JPanel();
    private char[] result;

    DialogoSenha() {
        setTitle("Senha token");
        passPanel.setLayout(new BorderLayout());
        labelInput.setLayout(new GridLayout(2,1));
        buttons.setLayout(new BorderLayout());

        password.setEchoChar('*');

        ok.addActionListener(this);
        cancel.addActionListener(this);

        labelInput.add(prompt);
        labelInput.add(password);
        buttons.add(ok, BorderLayout.WEST);
        buttons.add(cancel, BorderLayout.EAST);
        passPanel.setPreferredSize(new Dimension(200, 100));
        passPanel.add(labelInput, BorderLayout.NORTH);
        passPanel.add(buttons, BorderLayout.SOUTH);

        add(passPanel);
        pack();

        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        char[] tempPassArray = password.getPassword();

        if(e.getSource() == ok){
            result = tempPassArray;
        } else {
            result = null;
        }
        dispose();
    }

    public char[] getResult(){
        return result;
    }

}
