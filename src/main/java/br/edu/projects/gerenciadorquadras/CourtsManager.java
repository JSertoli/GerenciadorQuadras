/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.edu.projects.gerenciadorquadras;

import br.edu.projects.gerenciadorquadras.view.PainelQuadra;
import br.edu.projects.gerenciadorquadras.view.PainelJogador;
import br.edu.projects.gerenciadorquadras.view.PainelReserva;
import br.edu.projects.gerenciadorquadras.view.TabelaReservas;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Joao Sertoli
 */
public class CourtsManager {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gerenciador de Quadras Poliesportivas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            
            TabelaReservas tabelaReservas = new TabelaReservas();
            PainelReserva painelReserva = new PainelReserva(tabelaReservas);

            JTabbedPane abas = new JTabbedPane();
            abas.addTab("Reservas", painelReserva);
            abas.addTab("Jogadores", new PainelJogador(painelReserva));
            abas.addTab("Quadras", new PainelQuadra(painelReserva));
            abas.addTab("Lista de Quadras", tabelaReservas);

            frame.getContentPane().add(abas);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
    public static void enableComboBoxFilter(JComboBox<String> comboBox, List<String> items) {
        comboBox.setEditable(true);
        comboBox.setModel(new DefaultComboBoxModel<>(new Vector<>(items)));

        JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = editor.getText();
                List<String> filtered = items.stream()
                    .filter(s -> s.toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());

                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new Vector<>(filtered));
                comboBox.setModel(model);
                comboBox.setSelectedItem(text);
                comboBox.showPopup();
            }
        });
    }
}
