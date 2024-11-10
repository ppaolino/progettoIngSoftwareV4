import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Classe VisComprensorioApp
 * 
 * Applicazione grafica per visualizzare i comprensori geografici e i loro comuni.
 * L'interfaccia include un JComboBox per selezionare un comprensorio e un JTextArea per mostrare i comuni associati.
 */
public class VisComprensorioApp {

    private JPanel pane; // Pannello principale
    private JFrame window; // Finestra principale
    private JComboBox<String> comboBox; // Menù a tendina per i comprensori
    private JTextArea txtArea; // Area di testo per visualizzare i comuni

    private final Model model; // Modello dei dati

    /**
     * Costruttore per VisComprensorioApp
     * 
     * @param model il modello dei dati contenente i comprensori geografici
     */
    public VisComprensorioApp(Model model) {
        this.model = model; // Inizializza il modello
        initialize(); // Inizializza l'interfaccia grafica
    }

    /**
     * Inizializza l'interfaccia grafica e i suoi componenti.
     * 
     * Precondizione: il modello deve contenere una lista di comprensori valida.
     * Postcondizione: la finestra è configurata e visibile con i comprensori caricati nella comboBox.
     */
    @SuppressWarnings("unused")
    private void initialize() {
        // Configura la finestra principale
        window = new JFrame("Visualizza tutti i comprensori geografici");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setBounds(100, 100, 614, 417);

        pane = new JPanel(); // Pannello di contenuto principale
        window.setContentPane(pane);
        pane.setLayout(new BorderLayout());

        // Inizializza la JComboBox per la selezione dei comprensori
        comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Tahoma", Font.BOLD, 18));
        comboBox.setBackground(Color.WHITE);
        pane.add(comboBox, BorderLayout.NORTH);

        // Inizializza l'area di testo per mostrare i comuni dei comprensori
        txtArea = new JTextArea();
        txtArea.setEditable(false); // Rende l'area di testo non modificabile
        JScrollPane scroll = new JScrollPane(txtArea);
        pane.add(scroll, BorderLayout.CENTER);

        // Carica i nomi dei comprensori nel JComboBox
        ArrayList<Comprensorio> listaC = model.getComprensori();
        for (Comprensorio elem : listaC) {
            comboBox.addItem(elem.getNome());
        }

        // Aggiungi un ActionListener per gestire il cambio di selezione nella JComboBox
        comboBox.addActionListener(e -> {
            int selectedIndex = comboBox.getSelectedIndex();
            txtArea.setText(""); // Ripulisce l'area di testo

            // Ottiene i comuni del comprensorio selezionato e li aggiunge all'area di testo
            ArrayList<String> wordsArray = listaC.get(selectedIndex).getComuni();
            for (String comune : wordsArray) {
                txtArea.append(comune + "\n");
            }

            window.revalidate(); // Ricostruisce il layout
            window.repaint(); // Ridisegna la finestra
        });

        // Aggiungi un listener per gestire il ridimensionamento della finestra
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                pane.revalidate(); // Ricostruisce il layout del pannello principale
                pane.repaint(); // Ridisegna il pannello
            }
        });

        window.setVisible(true); // Rende visibile la finestra principale
    }
}
