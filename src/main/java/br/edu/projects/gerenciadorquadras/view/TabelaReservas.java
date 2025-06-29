/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.projects.gerenciadorquadras.view;

import br.edu.projects.gerenciadorquadras.controller.PersistenciaJPA;
import br.edu.projects.gerenciadorquadras.model.Quadra;
import br.edu.projects.gerenciadorquadras.model.Reserva;
import br.edu.projects.gerenciadorquadras.model.TipoQuadra;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Joao Sertoli
 */
public class TabelaReservas extends JPanel {
    public static TabelaReservas instanciaAtual;
    private JTable tabela;
    private DefaultTableModel modelo;
    private PersistenciaJPA persistencia = new PersistenciaJPA();

    public TabelaReservas() {
        instanciaAtual = this;
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[][]{}, new String[]{
            "ID", "Jogador", "Quadra", "Data", "Horário", "Duração", "Confirmada", "Pagamento", "Método"
        }) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton btnEditar = new JButton("Editar Reserva");
        JButton btnExcluir = new JButton("Excluir Reserva");

        btnEditar.addActionListener(e -> editarReserva());
        btnExcluir.addActionListener(e -> excluirReserva());

        botoes.add(btnEditar);
        botoes.add(btnExcluir);

        add(botoes, BorderLayout.SOUTH);

        carregarReservas();
    }

    public void carregarReservas() {
        modelo.setRowCount(0);
        List<Reserva> reservas = persistencia.getEntityManager()
            .createQuery("SELECT r FROM Reserva r", Reserva.class)
            .getResultList();

        for (Reserva r : reservas) {
            String pagamentoInfo = (r.getPagamento() != null && r.getPagamento().isConfirmado()) ? "Sim" : "Não";
            String metodo = r.getPagamento() != null ? r.getPagamento().getMetodo().toString() : "-";
            modelo.addRow(new Object[]{
                r.getId(),
                r.getJogador().getNome(),
                r.getQuadra().getNome(),
                r.getData(),
                r.getHorario(),
                r.getDuracao(),
                r.isConfirmada() ? "Sim" : "Não",
                pagamentoInfo,
                metodo
            });
        }
    }

    private void excluirReserva() {
        int row = tabela.getSelectedRow();
        if (row >= 0) {
            Integer id = Integer.valueOf(tabela.getValueAt(row, 0).toString());
            try {
                Reserva r = persistencia.getEntityManager().find(Reserva.class, id);
                persistencia.remover(r);
                carregarReservas();
                JOptionPane.showMessageDialog(this, "Reserva excluída.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir reserva: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva para excluir.");
        }
    }

    private void editarReserva() {
        int row = tabela.getSelectedRow();
        if (row >= 0) {
            Integer id = Integer.valueOf(tabela.getValueAt(row, 0).toString());
            try {
                Reserva r = persistencia.getEntityManager().find(Reserva.class, id);

                JTextField horarioField = new JTextField(r.getHorario());
                JSpinner duracaoField = new JSpinner(new SpinnerNumberModel(r.getDuracao(), 10, 240, 10));
                JCheckBox confirmadaCheck = new JCheckBox("Confirmada", r.isConfirmada());
                JTextField dataField = new JTextField(r.getData().toString());

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Data (AAAA-MM-DD):"));
                panel.add(dataField);
                panel.add(new JLabel("Horário:"));
                panel.add(horarioField);
                panel.add(new JLabel("Duração (min):"));
                panel.add(duracaoField);
                panel.add(confirmadaCheck);

                int result = JOptionPane.showConfirmDialog(this, panel, "Editar Reserva",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    r.setHorario(horarioField.getText().trim());
                    r.setDuracao((int) duracaoField.getValue());
                    r.setConfirmada(confirmadaCheck.isSelected());
                    r.setData(Date.valueOf(dataField.getText().trim()));

                    persistencia.persist(r);
                    carregarReservas();
                    JOptionPane.showMessageDialog(this, "Reserva editada com sucesso.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao editar reserva: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva para editar.");
        }
    }
}