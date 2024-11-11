import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Classe principale per l'applicazione di gestione del comprensorio.
 * 
 * Invariante: La lista di comuni selezionati non può contenere comuni duplicati.
 * Invariante: Il nome del comprensorio deve essere unico e non nullo.
 */
public class ComprensorioApp {

    private JFrame software;
    private JPanel panel3;
    private JTextField txtNomeC; // Campo di testo per l'inserimento del nome del comprensorio
    private JTextField searchField; // Campo di testo per la ricerca dei comuni
    private JTextArea txtArea; // Area di testo per mostrare i comuni selezionati
    private JPanel optionsPanel; // Pannello per visualizzare le opzioni filtrate

    private List<String> allOptions; // Lista di tutte le opzioni disponibili
    private List<JCheckBox> checkBoxes; // JCheckBox per ogni opzione filtrata
    private List<String> selectedOptions; // Lista delle opzioni selezionate

    private final Controller controller;
    private final ComuniInfo info = ComuniInfo.getInstance();
    private Comprensorio comp;

    // Costanti per dimensioni e posizioni
    private static final int FRAME_WIDTH = 660;
    private static final int FRAME_HEIGHT = 458;
    private static final int LABEL_HEIGHT = 46;
    private static final int BUTTON_HEIGHT = 46;

    public ComprensorioApp(Controller controller) {
        this.controller = controller;
        initialize();
    }

    /**
     * Inizializza l'interfaccia utente.
     * 
     * Precondizione: controller deve essere un'istanza valida di controller.
     * Postcondizione: L'interfaccia utente è visibile e pronta per l'interazione.
     */
    @SuppressWarnings("unused")
    private void initialize() {
        // Inizializza il frame principale
        software = new JFrame("Gestione Comprensorio");
        software.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        software.setBounds(100, 100, FRAME_WIDTH, FRAME_HEIGHT);
        software.setVisible(true);

        // Pannello principale
        panel3 = new JPanel();
        panel3.setBorder(new EmptyBorder(5, 5, 5, 5));
        software.setContentPane(panel3);
        panel3.setLayout(null);

        // Label per il titolo
        JLabel lblChiedi = new JLabel("Inserisci il nome del Comprensorio da creare:");
        lblChiedi.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblChiedi.setBounds(27, 10, 388, LABEL_HEIGHT);
        panel3.add(lblChiedi);

        // Campo di testo per l'inserimento del nome del comprensorio
        txtNomeC = new JTextField();
        txtNomeC.setBounds(lblChiedi.getX(), lblChiedi.getY() + lblChiedi.getHeight() + 10, 374, BUTTON_HEIGHT);
        panel3.add(txtNomeC);

        // Bottone per verificare il nome del comprensorio
        JButton btnVerifica = new JButton("VERIFICA E CREA");
        btnVerifica.setBounds(txtNomeC.getX() + txtNomeC.getWidth() + 10, txtNomeC.getY(), 157, BUTTON_HEIGHT);
        panel3.add(btnVerifica);

        // Campo di ricerca per i comuni
        searchField = new JTextField(20);
        searchField.setBounds(22, 167, 255, BUTTON_HEIGHT);
        searchField.setVisible(false);
        panel3.add(searchField);

        // Pannello per mostrare le opzioni durante la ricerca
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(optionsPanel);
        scrollPane.setBounds(22, 227, 255, 184);
        scrollPane.setVisible(false);
        panel3.add(scrollPane);

        // Label per messaggi di errore
        JLabel lblRiprova = new JLabel("");
        lblRiprova.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblRiprova.setBounds(22, 123, 374, 22);
        panel3.add(lblRiprova);

        // TextArea per mostrare i comuni selezionati
        txtArea = new JTextArea();
        JScrollPane scrTemp = new JScrollPane(txtArea);
        scrTemp.setVisible(false);
        scrTemp.setBounds(316, 227, 175, 85);
        panel3.add(scrTemp);

        // Bottone per confermare l'aggiunta dei comuni selezionati
        JButton showSelectedButton = new JButton("AGGIUNGI QUESTI COMUNI");
        showSelectedButton.setBounds(316, scrTemp.getY() + scrTemp.getHeight() + 10, 175, 75);
        showSelectedButton.setVisible(false);
        panel3.add(showSelectedButton);

        // Action listener per verificare la disponibilità del nome
        btnVerifica.addActionListener(e -> {
            String nomeComprensorio = txtNomeC.getText().trim();
            // Precondizione: il nome non deve essere nullo o vuoto
            if (nomeComprensorio.isEmpty()) {
                lblRiprova.setText("Il nome del comprensorio non può essere vuoto.");
                return;
            }
            // Verifica se il nome del comprensorio è disponibile
            if (controller.verificaDisp(nomeComprensorio)) {
                comp = controller.creaComprensorio(nomeComprensorio);
                txtNomeC.setEditable(false);
                searchField.setVisible(true);
                scrTemp.setVisible(true);
                scrollPane.setVisible(true);
                showSelectedButton.setVisible(true);
                lblRiprova.setText(null);

                allOptions = info.getTuttiComuni();
                selectedOptions = new ArrayList<>();
                checkBoxes = new ArrayList<>();

                // DocumentListener per aggiornare le opzioni di ricerca
                searchField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) { filterOptions(); }
                    @Override
                    public void removeUpdate(DocumentEvent e) { filterOptions(); }
                    @Override
                    public void changedUpdate(DocumentEvent e) { filterOptions(); }
                });
            } else {
                lblRiprova.setText("È stato già inserito un Comprensorio con questo nome, riprova!");
                txtNomeC.setText(null);
            }
        });

        // Action listener per confermare i comuni selezionati
        showSelectedButton.addActionListener(e -> {
            // Postcondizione: selezionare i comuni non deve modificare lo stato del comprensorio in modo errato
            if (confirmSelectedOptions()) {
                controller.salvaComprensori();
                software.dispose();
            }
        });
    }

    /**
     * Filtra le opzioni di ricerca in base al testo inserito.
     * 
     * Precondizione: allOptions deve essere inizializzato e non nullo.
     * Postcondizione: Le opzioni visualizzate nel pannello delle opzioni sono aggiornate in base al termine di ricerca.
     */
    private void filterOptions() {
        // Invariante: selectedOptions non deve contenere duplicati
        String searchTerm = searchField.getText().toLowerCase();
        List<String> filteredOptions = allOptions.stream()
            .filter(option -> option.toLowerCase().startsWith(searchTerm))
            .collect(Collectors.toList());
        updateCheckboxes(filteredOptions);
    }

    /**
     * Aggiorna le checkbox delle opzioni filtrate.
     * 
     * Precondizione: filteredOptions deve essere una lista valida e non nulla.
     * Postcondizione: Le checkbox nel pannello delle opzioni sono aggiornate in base alle nuove opzioni filtrate.
     */
    @SuppressWarnings("unused")
    private void updateCheckboxes(List<String> filteredOptions) {
        optionsPanel.removeAll();
        checkBoxes.clear();

        // Crea una JCheckBox per ogni opzione filtrata
        for (String option : filteredOptions) {
            JCheckBox checkBox = new JCheckBox(option);
            checkBox.setSelected(selectedOptions.contains(option)); // Mantiene le opzioni selezionate
            checkBox.addActionListener(e -> {
                // Invariante: selectedOptions deve rimanere unica
                if (checkBox.isSelected()) {
                    selectedOptions.add(option);
                    txtArea.append(option + "\n");
                } else {
                    selectedOptions.remove(option);
                    String currentText = txtArea.getText(); // Testo attuale
                    currentText = currentText.replace(option + "\n", ""); // Rimuove il comune non selezionato
                    txtArea.setText(currentText);
                }
                // Assicurati che selectedOptions rimanga unica
                List<String> uniqueSelected = new ArrayList<>(new HashSet<>(selectedOptions));
                selectedOptions.clear();
                selectedOptions.addAll(uniqueSelected);
            });

            checkBoxes.add(checkBox);
            optionsPanel.add(checkBox);
        }

        // Aggiorna il pannello grafico
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    /**
     * Conferma le opzioni selezionate.
     * 
     * Precondizione: selectedOptions non deve essere nullo.
     * Postcondizione: I comuni selezionati vengono aggiunti al comprensorio se validi.
     * @return true se tutte le opzioni sono valide, false altrimenti.
     */
    private boolean confirmSelectedOptions() {
        boolean isValid = true;
        for (String comune : selectedOptions) {
            if (!comp.aggiungiComune(comune)) {
                isValid = false; 
                JOptionPane.showMessageDialog(software, comune + " non è limitrofo a comuni già nel comprensorio, riprova");
            }
        }
        return isValid;
    }
}
