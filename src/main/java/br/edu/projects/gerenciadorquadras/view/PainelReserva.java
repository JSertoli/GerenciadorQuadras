/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.projects.gerenciadorquadras.view;

import br.edu.projects.gerenciadorquadras.CourtsManager;
import br.edu.projects.gerenciadorquadras.controller.PersistenciaJPA;
import br.edu.projects.gerenciadorquadras.model.Jogador;
import br.edu.projects.gerenciadorquadras.model.MetodoPagamento;
import br.edu.projects.gerenciadorquadras.model.NivelJogador;
import br.edu.projects.gerenciadorquadras.model.Pagamento;
import br.edu.projects.gerenciadorquadras.model.Quadra;
import br.edu.projects.gerenciadorquadras.model.Reserva;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Joao Sertoli
 */
public class PainelReserva extends JPanel {
    private JComboBox<String> cmbJogadores;
    private JComboBox<String> cmbQuadras;
    private JDateChooser chooserData;
    private JComboBox<String> cmbHora;
    private JSpinner spinDuracao;
    private JCheckBox chkConfirmada;
    private JCheckBox chkPagamento;
    private JTextField txtValor;
    private JComboBox<String> cmbMetodo;

    private PersistenciaJPA persistencia = new PersistenciaJPA();

    public PainelReserva() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2));

        cmbJogadores = new JComboBox<>();
        cmbQuadras = new JComboBox<>();
        chooserData = new JDateChooser();
        cmbHora = new JComboBox<>(gerarHorarios());
        spinDuracao = new JSpinner(new SpinnerNumberModel(30, 10, 240, 10));
        chkConfirmada = new JCheckBox("Confirmada");
        chkPagamento = new JCheckBox("Pagamento Realizado");
        txtValor = new JTextField();
        cmbMetodo = new JComboBox<>(new String[]{"PIX", "CARTAO", "DINHEIRO"});

        atualizarJogadores();
        atualizarQuadras();

        form.add(new JLabel("Jogador:")); form.add(cmbJogadores);
        form.add(new JLabel("Quadra:")); form.add(cmbQuadras);
        form.add(new JLabel("Data da Reserva:")); form.add(chooserData);
        form.add(new JLabel("Horário:")); form.add(cmbHora);
        form.add(new JLabel("Duração (min):")); form.add(spinDuracao);
        form.add(new JLabel("Confirmada:")); form.add(chkConfirmada);
        form.add(new JLabel("Pagamento realizado?")); form.add(chkPagamento);
        form.add(new JLabel("Valor:")); form.add(txtValor);
        form.add(new JLabel("Método de Pagamento:")); form.add(cmbMetodo);

        JButton btnReservar = new JButton("Reservar Quadra");
        btnReservar.addActionListener(this::realizarReserva);

        add(form, BorderLayout.CENTER);
        add(btnReservar, BorderLayout.SOUTH);
    }

    public void atualizarJogadores() {
        String[] nomes = buscarJogadores();
        cmbJogadores.removeAllItems();
        for (String nome : nomes) {
            cmbJogadores.addItem(nome);
        }
        CourtsManager.enableComboBoxFilter(cmbJogadores, Arrays.asList(nomes));
    }

    public void atualizarQuadras() {
        String[] nomes = buscarQuadrasDisponiveis();
        cmbQuadras.removeAllItems();
        for (String nome : nomes) {
            cmbQuadras.addItem(nome);
        }
        CourtsManager.enableComboBoxFilter(cmbQuadras, Arrays.asList(nomes));
    }

    private String[] buscarJogadores() {
        try {
            List<Jogador> jogadores = persistencia.getEntityManager()
                .createQuery("SELECT j FROM Jogador j", Jogador.class)
                .getResultList();
            return jogadores.stream().map(Jogador::getNome).toArray(String[]::new);
        } catch (Exception e) {
            return new String[]{"Nenhum jogador"};
        }
    }

    private String[] buscarQuadrasDisponiveis() {
        try {
            List<Quadra> quadras = persistencia.getEntityManager()
                .createQuery("SELECT q FROM Quadra q", Quadra.class)
                .getResultList();
            return quadras.stream().map(Quadra::getNome).toArray(String[]::new);
        } catch (Exception e) {
            return new String[]{"Nenhuma quadra cadastrada"};
        }
    }

    private String[] gerarHorarios() {
        String[] horarios = new String[24];
        for (int i = 0; i < 24; i++) {
            horarios[i] = String.format("%02d:00", i);
        }
        return horarios;
    }

    private void realizarReserva(ActionEvent e) {
        EntityManager em = persistencia.getEntityManager();

        try {
            Jogador jogador = buscarJogadorPorNome((String) cmbJogadores.getSelectedItem());
            Quadra quadra = buscarQuadraPorNome((String) cmbQuadras.getSelectedItem());
            quadra.setDisponivel(false);

            em.getTransaction().begin();

            em.merge(quadra);

            Reserva reserva = new Reserva();
            reserva.setJogador(jogador);
            reserva.setQuadra(quadra);
            reserva.setData(new java.sql.Date(chooserData.getDate().getTime()));
            reserva.setHorario((String) cmbHora.getSelectedItem());
            reserva.setDuracao((int) spinDuracao.getValue());
            reserva.setConfirmada(chkConfirmada.isSelected());

            em.persist(reserva);

            if (chkPagamento.isSelected()) {
                Pagamento pagamento = new Pagamento();
                pagamento.setReserva(reserva);
                pagamento.setConfirmado(true);
                pagamento.setDataPagamento(new Date());
                pagamento.setMetodo(MetodoPagamento.valueOf((String) cmbMetodo.getSelectedItem()));
                pagamento.setValor(Double.parseDouble(txtValor.getText()));
                em.persist(pagamento);
            }

            em.getTransaction().commit();

            atualizarJogadores();
            atualizarQuadras();

            JOptionPane.showMessageDialog(this, "Reserva realizada com sucesso!");

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao registrar reserva: " + ex.getMessage());
        }
    }


    private Jogador buscarJogadorPorNome(String nomeSelecionado) throws Exception {
        List<Jogador> jogadores = persistencia.getEntityManager()
            .createQuery("SELECT j FROM Jogador j WHERE j.nome = :nome", Jogador.class)
            .setParameter("nome", nomeSelecionado)
            .getResultList();

        if (jogadores.isEmpty()) {
            throw new Exception("Jogador não encontrado.");
        } else if (jogadores.size() > 1) {
            throw new Exception("Mais de um jogador com esse nome. Cadastre nomes únicos ou altere o código para usar ID.");
        }

        return jogadores.get(0);
    }

    private Quadra buscarQuadraPorNome(String nomeSelecionado) throws Exception {
        List<Quadra> quadras = persistencia.getEntityManager()
            .createQuery("SELECT q FROM Quadra q WHERE q.nome = :nome", Quadra.class)
            .setParameter("nome", nomeSelecionado)
            .getResultList();

        if (quadras.isEmpty()) {
            throw new Exception("Quadra não encontrada.");
        } else if (quadras.size() > 1) {
            throw new Exception("Mais de uma quadra com esse nome. Cadastre nomes únicos ou altere o código para usar ID.");
        }

        return quadras.get(0);
    }
}