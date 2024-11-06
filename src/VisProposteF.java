import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class VisProposteF {
    private JPanel pane;
    private JFrame window;
    private JComboBox<String> comboBox;
    private JPanel proposalPanel;

    private final Model model;
    private int counter = 0;
    private final Fruitore f;
    private ArrayList<Proposta> listaP;
    private Map<String, ArrayList<Proposta>> propostaMap;
     ArrayList<String> originalString= new ArrayList<>();

    public VisProposteF(Model model) {
        this.f = model.getFruitore();
        this.model = model;
        initialize();
    }

    private String getInfo(Proposta p) {
        return "Richiesta: " + p.getRichiesta().getNome() +
               " - Ore richieste: " + p.getOreRichieste() +
               "  -->  Offerta: " + p.getOfferta().getNome() +
               " - Ore offerte: " + p.getOreOfferte() +
               " - Stato: " + p.getStato() +
               " - Data: " + p.getTime();
    }

    private void initializeDataMap() {
        propostaMap = new HashMap<>();
        for (Proposta proposta : listaP) {
            if (proposta.getIdFruitore() == f.getId()) {
                String key =""+proposta.getId();
                propostaMap.computeIfAbsent(key, k -> new ArrayList<>()).add(proposta);
            }
        }
    }

    private void initialize() {
        this.listaP = model.getProposte();
        initializeDataMap();
        window = new JFrame("Visualizza tutte le proposte");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setBounds(100, 100, 814, 450);

        pane = new JPanel();
        window.setContentPane(pane);
        pane.setLayout(new BorderLayout());

        comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Tahoma", Font.BOLD, 18));
        comboBox.setBackground(Color.WHITE);
        pane.add(comboBox, BorderLayout.NORTH);

        if (!propostaMap.keySet().isEmpty()) {
            ArrayList<Proposta> visualizza = new ArrayList<>();
        
            // Flatten all lists into a single ArrayList
            for (String propostaList : propostaMap.keySet()) {
                visualizza.add(propostaMap.get(propostaList).get(0));
            }
            // Loop through each Proposta in the visualizza ArrayList
            for (int i=0; i<visualizza.size(); i++) {
                originalString.add(""+visualizza.get(i).getId());
                comboBox.addItem(visualizza.get(i).getRichiesta().getNome() + " --> " + visualizza.get(i).getOfferta().getNome());
            }
        } else {
            comboBox.addItem("Nessuna proposta creata");
        }
        

        proposalPanel = new JPanel();
        proposalPanel.setLayout(new BoxLayout(proposalPanel, BoxLayout.PAGE_AXIS)); // Stack each proposal row
        JScrollPane scroll = new JScrollPane(proposalPanel);
        pane.add(scroll, BorderLayout.CENTER);

        comboBox.addActionListener(e -> updateSelectedProposalInfo(comboBox.getSelectedIndex()));

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                pane.revalidate();
                pane.repaint();
            }
        });

        window.setVisible(true);
    }

    

    private void updateSelectedProposalInfo(int id) {
        String selectedKey = originalString.get(id);
        proposalPanel.removeAll(); // Clear previous proposals

        if (selectedKey != null && propostaMap.containsKey(selectedKey)) {
            ArrayList<Proposta> selectedProposte = propostaMap.get(selectedKey);

            for (Proposta proposta : selectedProposte) {
                if (proposta.getIdFruitore() == f.getId()) {
                    counter++;
                }
            }

            for (Proposta proposta : selectedProposte) {
                if (proposta.getIdFruitore() == f.getId()) {
                    JPanel proposalRow = new JPanel(); // Align components closely to each other

                    JTextArea proposalText = new JTextArea(getInfo(proposta));
                    proposalText.setColumns(40); // Fixed width for text display
                    proposalText.setRows(1); // Set only one row to keep it as a single line
                    proposalText.setLineWrap(false); // Disable line wrapping to keep one line per proposal
                    proposalText.setWrapStyleWord(false);
                    proposalText.setEditable(false);
                    proposalText.setFont(new Font("Serif", Font.PLAIN, 14));
                    proposalRow.add(proposalText);

                    // Show button only if text fits in one line
                    if ("aperta".equalsIgnoreCase(proposta.getStato()) && counter==1) {
                        JButton actionButton = new JButton("Ritira la proposta");
                        actionButton.setPreferredSize(new Dimension(150, proposalText.getPreferredSize().height)); // Small button height
                        actionButton.addActionListener(e -> performActionOnProposal(proposta));
                        proposalRow.add(actionButton);
                    }

                    proposalPanel.add(proposalRow);
                }
            }
            counter=0;
        }

        proposalPanel.revalidate();
        proposalPanel.repaint();
    }

    private void performActionOnProposal(Proposta proposta) {

        model.aggiornaStato(proposta.getId(), "ritirata");
        JOptionPane.showMessageDialog(window, "La proposta " + proposta.getRichiesta().getNome() + " --> " + proposta.getOfferta().getNome() + " è stata ritirata");
        window.removeAll();
        window.dispose();
        initialize();
    }
}
