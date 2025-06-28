/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.projects.gerenciadorquadras.view;

import br.edu.projects.gerenciadorquadras.controller.PersistenciaJPA;
import br.edu.projects.gerenciadorquadras.model.Jogador;
import br.edu.projects.gerenciadorquadras.model.NivelJogador;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Joao Sertoli
 */
public class PainelJogador extends JPanel {
    private JTextField txtNome, txtEmail, txtTelefone;
    private JComboBox<String> cmbNivel;
    private PersistenciaJPA persistencia = new PersistenciaJPA();
    
    private PainelReserva painelReserva;

    public PainelJogador(PainelReserva painelReserva) {
        setLayout(new GridLayout(0, 2));
        
        this.painelReserva = painelReserva;
        txtNome = new JTextField();
        txtEmail = new JTextField();
        txtTelefone = new JTextField();
        cmbNivel = new JComboBox<>(new String[]{"INICIANTE", "INTERMEDIARIO", "AVANCADO"});

        add(new JLabel("Nome:")); add(txtNome);
        add(new JLabel("Email:")); add(txtEmail);
        add(new JLabel("Telefone:")); add(txtTelefone);
        add(new JLabel("Nível:")); add(cmbNivel);

        JButton btnSalvar = new JButton("Cadastrar Jogador");
        btnSalvar.addActionListener(e -> salvarJogador());
        add(btnSalvar);
    }

    private void salvarJogador() {
        try {
            Jogador j = new Jogador();
            j.setNome(txtNome.getText());
            j.setEmail(txtEmail.getText());
            j.setTelefone(txtTelefone.getText());
            j.setNivel(NivelJogador.valueOf((String) cmbNivel.getSelectedItem()));
            persistencia.persist(j);
            painelReserva.atualizarJogadores();

            JOptionPane.showMessageDialog(this, "Jogador salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar jogador.");
        }
    }
}
