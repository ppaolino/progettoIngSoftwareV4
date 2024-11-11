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
    private final Controller controller; // controllerlo di dati usato per gestire le operazioni

    /**
     * Costruttore della classe App che inizializza il controllerlo e l'interfaccia se il configuratore è disponibile.
     * 
     * @param controller il controllerlo di dati da utilizzare
     * @pre controller != null; // Assicurati che il controllerlo non sia nullo
     */
    public App(Controller controller) {
        // Precondizione
        if (controller == null) {
            throw new IllegalArgumentException("Il controllerlo non può essere nullo.");
        }
        
        this.controller = controller;
        
        // Verifica se il configuratore esiste prima di avviare l'interfaccia
        if (controller.getConfiguratore() != null) {
            utente = true;
        }
        else if(controller.getFruitore() != null){
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
     * @pre controller.getConfiguratore() != null; // Assicurati che il configuratore esista
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
            lblRichiesta = new JLabel("Benvenuto " + controller.getConfiguratore().getUsername() + ", cosa desideri fare?", SwingConstants.CENTER);
        else
            lblRichiesta = new JLabel("Benvenuto " + controller.getFruitore().getUsername() + ", cosa desideri fare?", SwingConstants.CENTER);
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
            comboBox.addItem("Visualizza gli insiemi chiusi");

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
        btnScelta.addActionListener((@SuppressWarnings("unused") ActionEvent e) -> {
            int scelta = comboBox.getSelectedIndex();
            
            // Switch per determinare l'azione in base all'opzione selezionata
            switch (scelta) {
                case 0 -> controller.visualizzaGerarchia();
                case 1 -> 
                {
                    if(utente)
                        controller.visualizzaComprensorio(); 
                    else
                        controller.nuovaProposta();
                }
                
                case 2 -> {
                    if(utente){
                        controller.creaGerarchia();
                    // Postcondizione: una nuova gerarchia deve essere stata creata
                    assert !controller.getListaGerarchie().isEmpty() : "Nessuna gerarchia creata.";
                    }
                    else{
                        controller.visualizzaProposteF();
                    }
                    
                }
                case 3 -> {
                    controller.aggiungiComprensorio();
                    // Postcondizione: un nuovo comprensorio deve essere stato aggiunto
                    assert !controller.getComprensori().isEmpty() : "Nessun comprensorio aggiunto.";
                }
                case 4 -> {
                    controller.visualizzaInsieme();
                }
                default -> System.out.println("Nessuna azione disponibile.");
            }
        });
    }
}
