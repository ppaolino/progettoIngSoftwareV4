import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class VisProposteF {
    private JPanel pane;
    private JFrame window;
    private JComboBox<String> comboBox;
    private JPanel proposalPanel;

    private final Controller controller;
    private int counter = 0;
    private final Fruitore f;
    private ArrayList<Proposta> listaP;
    private Map<String, ArrayList<Proposta>> propostaMap;
    ArrayList<String> originalString = new ArrayList<>();

    /**
     * Precondizioni:
     * - `controller` non deve essere null.
     * - Il `controller` deve avere un `Fruitore` associato.
     * 
     * Postcondizioni:
     * - L'istanza `VisProposteF` è inizializzata correttamente.
     * - Viene invocato il metodo `initialize()`.
     */
    public VisProposteF(Controller controller) {
        assert controller != null : "il controller deve essere valido";

        this.controller = controller;
        this.f = controller.getFruitore();
        initialize();
    }

    /**
     * Precondizioni:
     * - `p` non deve essere null.
     * 
     * Postcondizioni:
     * - Viene restituita una stringa contenente le informazioni sulla `Proposta`.
     */
    private String getInfo(Proposta p) {
        assert p != null : "la proposta deve essere valida";

        return "Richiesta: " + p.getRichiesta().getNome() +
               " - Ore richieste: " + p.getOreRichieste() +
               "  -->  Offerta: " + p.getOfferta().getNome() +
               " - Ore offerte: " + p.getOreOfferte() +
               " - Stato: " + p.getStato() +
               " - Data: " + p.getTime() + "\n";
    }

    /**
     * Precondizioni:
     * - `listaP` non deve essere null.
     * 
     * Postcondizioni:
     * - Viene popolata la mappa `propostaMap` con tutte le proposte relative al `Fruitore` corrente.
     */
    @SuppressWarnings("unused")
    private void initializeDataMap() {
        propostaMap = new HashMap<>();
        for (Proposta proposta : listaP) {
            if (proposta.getIdFruitore() == f.getId()) {
                String key = "" + proposta.getId();
                propostaMap.computeIfAbsent(key, k -> new ArrayList<>()).add(proposta);
            }
        }
    }

    /**
     * Precondizioni:
     * - Il `controller` deve contenere una lista di `Proposta`.
     * 
     * Postcondizioni:
     * - Viene creata e visualizzata una finestra `JFrame`.
     * - La `comboBox` viene popolata con le proposte disponibili.
     * - Viene aggiunto un listener per gestire la selezione delle proposte.
     */
    @SuppressWarnings("unused")
    private void initialize() {
        this.listaP = controller.getProposte();
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

            // Loop through each `Proposta` in the visualizza ArrayList
            for (int i = 0; i < visualizza.size(); i++) {
                originalString.add("" + visualizza.get(i).getId());
                comboBox.addItem(visualizza.get(i).getRichiesta().getNome() + " --> " + visualizza.get(i).getOfferta().getNome());
            }
        } else {
            comboBox.addItem("Nessuna proposta creata");
            comboBox.setEnabled(false);
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

    /**
     * Precondizioni:
     * - `id` deve essere un indice valido all'interno della lista `originalString`.
     * 
     * Postcondizioni:
     * - Viene aggiornato il pannello con le informazioni sulla `Proposta` selezionata.
     */
    @SuppressWarnings("unused")
    private void updateSelectedProposalInfo(int id) {
        assert id >= 0 : "id deve essere un numero positivo";

        String selectedKey = originalString.get(id);
        proposalPanel.removeAll();

        JPanel proposalRow = new JPanel();
        JTextArea proposalText = new JTextArea();
        proposalText.setColumns(40);
        proposalText.setRows(1);
        proposalText.setLineWrap(false);
        proposalText.setWrapStyleWord(false);
        proposalText.setEditable(false);
        proposalText.setFont(new Font("Serif", Font.PLAIN, 14));

        if (selectedKey != null && propostaMap.containsKey(selectedKey)) {
            ArrayList<Proposta> selectedProposte = propostaMap.get(selectedKey);

            for (Proposta proposta : selectedProposte) {
                if (proposta.getIdFruitore() == f.getId()) {
                    counter++;
                }
            }

            proposalRow.add(proposalText);

            for (Proposta proposta : selectedProposte) {
                if (proposta.getIdFruitore() == f.getId()) {
                    proposalText.append(getInfo(proposta));

                    if ("aperta".equalsIgnoreCase(proposta.getStato()) && counter == 1) {
                        JButton actionButton = new JButton("Ritira la proposta");
                        actionButton.setPreferredSize(new Dimension(150, proposalText.getPreferredSize().height));
                        actionButton.addActionListener(e -> performActionOnProposal(proposta));
                        proposalRow.add(actionButton);
                    }
                }
            }
            counter = 0;
        }

        proposalPanel.add(proposalRow);
        proposalPanel.revalidate();
        proposalPanel.repaint();
    }

    /**
     * Precondizioni:
     * - `proposta` non deve essere null.
     * - Il `controller` deve supportare il metodo `aggiornaStato`.
     * 
     * Postcondizioni:
     * - Lo stato della proposta viene aggiornato a "ritirata".
     * - Viene mostrato un messaggio di conferma.
     * - La finestra viene chiusa e riaperta con i dati aggiornati.
     */
    private void performActionOnProposal(Proposta proposta) {
        assert proposta != null : "proposta deve esistere";
        
        controller.aggiornaStato(proposta.getId(), "ritirata");
        JOptionPane.showMessageDialog(window, "La proposta " + proposta.getRichiesta().getNome() +
                " --> " + proposta.getOfferta().getNome() + " è stata ritirata");
        window.removeAll();
        window.dispose();
        initialize();
    }
}
