import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainProgram {

    private JFrame frame;
    private final JPanel pannello;
    private final Controller controller;

    /**
     * Metodo principale che avvia l'applicazione.
     * 
     * Precondizioni:
     * - Nessuna.
     * 
     * Postcondizioni:
     * - Avvia il programma creando una finestra principale.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainProgram window = new MainProgram();
                window.frame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Errore nell'avvio dell'applicazione");
            }
        });
    }

    /**
     * Costruttore della classe `MainProgram`.
     * 
     * Precondizioni:
     * - Nessuna.
     * 
     * Postcondizioni:
     * - Inizializza il controllerlo e la finestra principale con i relativi componenti.
     * - Crea due pulsanti "FRUITORE" e "CONFIGURATORE" con le relative funzionalità.
     */
    @SuppressWarnings("unused")
    public MainProgram() {
        controller = new Controller();

        frame = new JFrame("Scelta tipologia di utente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(347, 203);
        frame.setLocationRelativeTo(null);

        pannello = new JPanel();
        pannello.setLayout(new GridLayout(0, 1, 0, 0));
        frame.setContentPane(pannello);

        JButton btnFruitore = new JButton("FRUITORE");
        btnFruitore.setFont(new Font("Tahoma", Font.PLAIN, 25));
        pannello.add(btnFruitore);

        JButton btnConfig = new JButton("CONFIGURATORE");
        btnConfig.setFont(new Font("Tahoma", Font.PLAIN, 25));
        pannello.add(btnConfig);

        /**
         * Listener per il pulsante "CONFIGURATORE".
         * 
         * Precondizioni:
         * - `controller` non deve essere null.
         * 
         * Postcondizioni:
         * - Avvia la finestra di configurazione (`LoginApp`) e nasconde la finestra principale.
         */
        btnConfig.addActionListener(e -> {
            assert controller != null : "Il controller non può essere null";
            LoginApp window = new LoginApp(controller);
            window.setVisible();
            frame.setVisible(false);
        });

        /**
         * Listener per il pulsante "FRUITORE".
         * 
         * Precondizioni:
         * - `controller` non deve essere null.
         * 
         * Postcondizioni:
         * - Avvia la finestra di login per il fruitore (`LoginFruitore`) e nasconde la finestra principale.
         */
        btnFruitore.addActionListener(e -> {
            assert controller != null : "Il controller non può essere null";
            new LoginFruitore(controller);
            frame.setVisible(false);
        });
    }
}
