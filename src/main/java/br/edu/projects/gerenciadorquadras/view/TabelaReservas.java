/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.projects.gerenciadorquadras.view;

import br.edu.projects.gerenciadorquadras.controller.PersistenciaJPA;
import br.edu.projects.gerenciadorquadras.model.Quadra;
import br.edu.projects.gerenciadorquadras.model.TipoQuadra;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Joao Sertoli
 */
public class TabelaReservas extends JPanel {
   private JTable tabela;
    private DefaultTableModel modelo;
    private PersistenciaJPA persistencia = new PersistenciaJPA();

    public TabelaReservas() {
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Nome da Quadra", "Local", "Tipo", "Disponível"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton btnEditar = new JButton("Editar Quadra");
        JButton btnExcluir = new JButton("Excluir Quadra");

        btnEditar.addActionListener(e -> editarQuadra());
        btnExcluir.addActionListener(e -> excluirQuadra());

        botoes.add(btnEditar);
        botoes.add(btnExcluir);

        add(botoes, BorderLayout.SOUTH);

        carregarQuadras();
    }

    private void carregarQuadras() {
        modelo.setRowCount(0);
        List<Quadra> quadras = persistencia.getEntityManager()
            .createQuery("SELECT q FROM Quadra q", Quadra.class)
            .getResultList();
        for (Quadra q : quadras) {
            modelo.addRow(new Object[]{q.getId(), q.getNome(), q.getLocalizacao(), q.getTipo().name(), q.isDisponivel() ? "Sim" : "Não"});
        }
    }

    private void excluirQuadra() {
        int row = tabela.getSelectedRow();
        if (row >= 0) {
            Integer id = Integer.valueOf(tabela.getValueAt(row, 0).toString());
            try {
                Quadra q = persistencia.getEntityManager().find(Quadra.class, id);
                persistencia.remover(q);
                carregarQuadras();
                JOptionPane.showMessageDialog(this, "Quadra excluída.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir quadra: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma quadra para excluir.");
        }
    }

    private void editarQuadra() {
        int row = tabela.getSelectedRow();
        if (row >= 0) {
            Integer id = Integer.valueOf(tabela.getValueAt(row, 0).toString());
            try {
                Quadra q = persistencia.getEntityManager().find(Quadra.class, id);

                JTextField nomeField = new JTextField(q.getNome());
                JTextField localField = new JTextField(q.getLocalizacao());
                JComboBox<TipoQuadra> tipoBox = new JComboBox<>(TipoQuadra.values());
                tipoBox.setSelectedItem(q.getTipo());
                JCheckBox disponivelCheck = new JCheckBox("Disponível", q.isDisponivel());

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Nome da Quadra:"));
                panel.add(nomeField);
                panel.add(new JLabel("Localização:"));
                panel.add(localField);
                panel.add(new JLabel("Tipo de Quadra:"));
                panel.add(tipoBox);
                panel.add(disponivelCheck);

                int result = JOptionPane.showConfirmDialog(this, panel, "Editar Quadra",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    q.setNome(nomeField.getText().trim());
                    q.setLocalizacao(localField.getText().trim());
                    q.setTipo((TipoQuadra) tipoBox.getSelectedItem());
                    q.setDisponivel(disponivelCheck.isSelected());

                    persistencia.persist(q);
                    carregarQuadras();
                    JOptionPane.showMessageDialog(this, "Quadra editada com sucesso.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao editar quadra: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma quadra para editar.");
        }
    }
}
