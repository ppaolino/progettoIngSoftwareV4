import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Classe principale dell'applicazione che gestisce l'interfaccia utente
 * per il configuratore.
 */
public class App {

    private JFrame software; // Finestra principale dell'applicazione
    private JPanel panel2; // Pannello principale per i componenti della UI
    private boolean utente; //true = configuratore, false = fruitore
    private final Model model; // Modello di dati usato per gestire le operazioni

    /**
     * Costruttore della classe App che inizializza il modello e l'interfaccia se il configuratore è disponibile.
     * 
     * @param model il modello di dati da utilizzare
     * @pre model != null; // Assicurati che il modello non sia nullo
     */
    public App(Model model) {
        // Precondizione
        if (model == null) {
            throw new IllegalArgumentException("Il modello non può essere nullo.");
        }
        
        this.model = model;
        
        // Verifica se il configuratore esiste prima di avviare l'interfaccia
        if (model.getConfiguratore() != null) {
            utente = true;
        }
        else if(model.getFruitore() != null){
            utente = false;
        }
        else{
            throw new IllegalArgumentException("Accesso non effettuato.");
        }

        swInitialize();
    }

    /**
     * Metodo per l'inizializzazione dell'interfaccia utente.
     * Imposta la finestra principale, il pannello e i componenti UI.
     * 
     * @pre model.getConfiguratore() != null; // Assicurati che il configuratore esista
     * @post software != null; // Assicurati che la finestra sia inizializzata
     */
    private void swInitialize() {
        // Precondizione
        if(utente)  
            software = new JFrame("Menu Configuratore");
        else
            software = new JFrame("Menu Fruitore");

        software.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        software.setBounds(100, 100, 679, 484);
        software.setLocationRelativeTo(null);
        software.setVisible(true);
        
        panel2 = new JPanel();
        panel2.setLayout(null);
        software.getContentPane().add(panel2);

        // Etichetta di benvenuto con il nome dell'utente
        JLabel lblRichiesta;
        if(utente)
            lblRichiesta = new JLabel("Benvenuto " + model.getConfiguratore().getUsername() + ", cosa desideri fare?", SwingConstants.CENTER);
        else
            lblRichiesta = new JLabel("Benvenuto " + model.getFruitore().getUsername() + ", cosa desideri fare?", SwingConstants.CENTER);
        FontMetrics metrics = lblRichiesta.getFontMetrics(lblRichiesta.getFont());
        int textWidth = metrics.stringWidth(lblRichiesta.getText());
        lblRichiesta.setFont(new Font("Tahoma", Font.PLAIN, 22));
        lblRichiesta.setBounds(software.getWidth()/2 - (textWidth + 200)/2, 46, textWidth + 200, 32);
        panel2.add(lblRichiesta);

        // ComboBox per le opzioni disponibili
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 22));
        comboBox.setBounds(104, 88, 458, 70);
        panel2.add(comboBox);
        
        if(utente){
            comboBox.addItem("Visualizza una Gerarchia");
            comboBox.addItem("Visualizza un Comprensorio geografico");
            comboBox.addItem("Crea una nuova Gerarchia");
            comboBox.addItem("Crea un nuovo Comprensorio geografico");
            
        }
        else {
            comboBox.addItem("Visualizza una Gerarchia");
            comboBox.addItem("Crea proposta");
            comboBox.addItem("Visualizza proposte");
        }

        comboBox.setSelectedIndex(0); // Selezione predefinita

        // Pulsante per confermare la scelta
        JButton btnScelta = new JButton("ESEGUI");
        btnScelta.setFont(new Font("Tahoma", Font.PLAIN, 17));
        btnScelta.setBounds(262, 332, 119, 60);
        panel2.add(btnScelta);

        // Listener per il pulsante che esegue l'azione selezionata
        btnScelta.addActionListener((ActionEvent e) -> {
            int scelta = comboBox.getSelectedIndex();
            
            // Switch per determinare l'azione in base all'opzione selezionata
            switch (scelta) {
                case 0 -> model.visualizzaGerarchia();
                case 1 -> 
                {
                    if(utente)
                        model.visualizzaComprensorio(); 
                    else
                        model.nuovaProposta();
                }
                
                case 2 -> {
                    if(utente){
                        model.creaGerarchia();
                    // Postcondizione: una nuova gerarchia deve essere stata creata
                    assert !model.getListaGerarchie().isEmpty() : "Nessuna gerarchia creata.";
                    }
                    else{
                        model.visualizzaProposteF();
                    }
                    
                }
                case 3 -> {
                    model.aggiungiComprensorio();
                    // Postcondizione: un nuovo comprensorio deve essere stato aggiunto
                    assert !model.getComprensori().isEmpty() : "Nessun comprensorio aggiunto.";
                }
                default -> System.out.println("Nessuna azione disponibile.");
            }
        });
    }
}
