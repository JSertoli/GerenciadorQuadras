/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.projects.gerenciadorquadras.view;

import br.edu.projects.gerenciadorquadras.controller.PersistenciaJPA;
import br.edu.projects.gerenciadorquadras.model.Quadra;
import br.edu.projects.gerenciadorquadras.model.TipoQuadra;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Joao Sertoli
 */
public class PainelQuadra extends JPanel {
    private JTextField txtNome, txtLocalizacao;
    private JComboBox<String> cmbTipo;
    private JCheckBox chkDisponivel;
    private PersistenciaJPA persistencia = new PersistenciaJPA();
    
    private PainelReserva painelReserva;


    public PainelQuadra(PainelReserva painelReserva) {
        setLayout(new GridLayout(0, 2));

        this.painelReserva = painelReserva;
        txtNome = new JTextField();
        txtLocalizacao = new JTextField();
        cmbTipo = new JComboBox<>(new String[]{"ABERTA", "COBERTA"});
        chkDisponivel = new JCheckBox("Disponível", true);

        add(new JLabel("Nome:")); add(txtNome);
        add(new JLabel("Localização:")); add(txtLocalizacao);
        add(new JLabel("Tipo:")); add(cmbTipo);
        add(new JLabel("Status:")); add(chkDisponivel);

        JButton btnSalvar = new JButton("Cadastrar Quadra");
        btnSalvar.addActionListener(e -> salvarQuadra());
        add(btnSalvar);
    }

    private void salvarQuadra() {
        try {
            Quadra q = new Quadra();
            q.setNome(txtNome.getText());
            q.setLocalizacao(txtLocalizacao.getText());
            q.setTipo(TipoQuadra.valueOf((String) cmbTipo.getSelectedItem()));
            q.setDisponivel(chkDisponivel.isSelected());
            persistencia.persist(q);
            painelReserva.atualizarQuadras();
            JOptionPane.showMessageDialog(this, "Quadra salva com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar quadra.");
        }
    }
}
