package br.jus.tjrr.siga.assinador;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class DialogoAlias extends JDialog implements ActionListener {


    private JComboBox comboBox = null;
    private JButton ok =  new JButton("OK");
    private JButton cancel = new JButton("Cancelar");
    private JLabel prompt = new JLabel("Selecione o alias:");
    JPanel passPanel = new JPanel();
    JPanel labelInput = new JPanel();
    JPanel buttons = new JPanel();
    private String result;

    DialogoAlias(List<String> aliases) {
        setTitle("Seleção alias");
        passPanel.setLayout(new BorderLayout());
        labelInput.setLayout(new GridLayout(2,1));
        buttons.setLayout(new BorderLayout());

        comboBox = new JComboBox(aliases.toArray());

        ok.addActionListener(this);
        cancel.addActionListener(this);

        labelInput.add(prompt);
        labelInput.add(comboBox);
        buttons.add(ok, BorderLayout.WEST);
        buttons.add(cancel, BorderLayout.EAST);
        passPanel.setPreferredSize(new Dimension(350, 100));
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
        if(e.getSource() == ok){
            result = (String) comboBox.getSelectedItem();
        } else {
            result = null;
        }
        dispose();
    }

    public String getResult(){
        return result;
    }

}
