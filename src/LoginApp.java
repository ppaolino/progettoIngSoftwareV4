import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * Classe LoginApp per gestire la GUI di un'applicazione di login e registrazione utente.
 */
public class LoginApp {

    private JFrame frameLogin;           // Finestra principale dell'applicazione
    private JPanel pnlPredefinito;       // Pannello per il login predefinito
    private JPanel pnlLogin;             // Pannello per il login standard
    private final Model model;           // Modello dell'applicazione
    
    private boolean primoAccesso = false; // Flag per il primo accesso

    /**
     * Costruttore per la classe LoginApp.
     * Inizializza il modello e crea la GUI per la registrazione.
     */
    public LoginApp(Model model) {    
        this.model = model;
        creaLogin();
    }

    /**
     * Metodo per inizializzare la finestra di login predefinito.
     * 
     * @pre Il frame principale e il modello devono essere inizializzati.
     * @post La finestra di login predefinito è creata e visibile.
     */
    private void initialize() {
        pnlPredefinito = new JPanel();
        pnlPredefinito.setLayout(null);
        frameLogin.getContentPane().add(pnlPredefinito);

        // Etichetta per il nome utente predefinito
        JLabel lblText = new JLabel("Username predefinito:");
        lblText.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblText.setBounds(32, 10, 145, 25);
        pnlPredefinito.add(lblText);

        // Campo di testo per l'username
        JTextField userText = new JTextField(20);
        userText.setBounds(32, 41, 165, 25);
        pnlPredefinito.add(userText);

        // Etichetta per la password predefinita
        JLabel lblPassword = new JLabel("Password predefinita:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblPassword.setBounds(31, 65, 146, 25);
        pnlPredefinito.add(lblPassword);

        // Campo per la password con mascheramento
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(32, 93, 165, 25);
        pnlPredefinito.add(passwordText);

        // Bottone di registrazione
        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btnRegister.setBounds(212, 51, 98, 49);
        pnlPredefinito.add(btnRegister);

        // Etichetta per eventuali messaggi di successo o errore
        JLabel messageLabel = new JLabel("");
        messageLabel.setBounds(10, 120, 300, 25);
        pnlPredefinito.add(messageLabel);

        // Listener per il bottone di registrazione
        btnRegister.addActionListener((@SuppressWarnings("unused") ActionEvent e) -> {
            String username = userText.getText().trim();
            String password = new String(passwordText.getPassword()).trim();

            if (model.validateLogin(username, password)) {
                pnlPredefinito.setVisible(false);
                login();
            } else {
                messageLabel.setText("Credenziali non valide. Riprova.");
                passwordText.setText(null);
                userText.setText(null);
            }
        });
    }

    public void setVisible(){
        frameLogin.setVisible(true);
    }

    /**
     * Metodo per posizionare i componenti della finestra di login standard.
     * 
     * @pre Il frame principale e il modello devono essere inizializzati.
     * @post La finestra di login è creata e visibile.
     */
    public void login() {
        pnlLogin = new JPanel();
        pnlLogin.setBounds(0, 10, 336, 163);
        pnlLogin.setLayout(null);
        frameLogin.getContentPane().add(pnlLogin);

        // Etichetta principale
        JLabel lblCredenziali = new JLabel("Inserisci le credenziali:");
        lblCredenziali.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblCredenziali.setBounds(29, 10, 264, 37);
        pnlLogin.add(lblCredenziali);

        // Etichetta e campo per l'username
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblUser.setBounds(29, 57, 68, 13);
        pnlLogin.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(123, 49, 189, 27);
        pnlLogin.add(txtUser);
        txtUser.setColumns(10);

        // Etichetta e campo per la password
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblPass.setBounds(33, 91, 64, 13);
        pnlLogin.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(123, 86, 189, 26);
        pnlLogin.add(txtPass);
        txtPass.setColumns(10);

        // Bottone per il login
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(215, 122, 97, 34);
        pnlLogin.add(btnLogin);

        // Etichetta per eventuali messaggi di errore
        JLabel lblExit = new JLabel("");
        lblExit.setFont(new Font("Tahoma", Font.BOLD, 10));
        lblExit.setBounds(10, 128, 195, 23);
        pnlLogin.add(lblExit);

        // Listener per il bottone di login
        btnLogin.addActionListener((@SuppressWarnings("unused") ActionEvent e) -> {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword()).trim();
            if(username.equals("") || password.equals("")){
                lblExit.setText("Compilare tutti i campi");
                txtPass.setText(null);
                txtUser.setText(null);
            }
            else if (!primoAccesso) {
                if (model.listaContains(username, password)) {
                    frameLogin.setVisible(false);
                    System.out.println("Credenziali corrette! Benvenuto...");
                    model.creaConfiguratore(username, password);
                    new App(model);
                } else {
                    lblExit.setText("Credenziali errate!");
                    txtPass.setText(null);
                    txtUser.setText(null);
                }
            } else {
                if (!model.inUso(username)) {
                    frameLogin.setVisible(false);
                    model.salvaConfig(username, password);
                    System.out.println("Nuovo utente creato! Benvenuto...");
                    model.creaConfiguratore(username, password);
                    new App(model);
                } else {
                    lblExit.setText("Username già usato");
                    txtPass.setText(null);
                    txtUser.setText(null);
                }
            }
        });
    }

    /**
     * Crea il menu di accesso, permettendo la scelta tra login e primo accesso.
     * 
     * @pre Il frame principale e il modello devono essere inizializzati.
     * @post Il menu di accesso è creato e visibile.
     */
    @SuppressWarnings("unused")
    private void creaLogin() {
        frameLogin = new JFrame("Registrazione Configuratore");
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogin.setSize(347, 203);
        frameLogin.setLocationRelativeTo(null);

        JPanel pnlInit = new JPanel();
        frameLogin.getContentPane().add(pnlInit);
        pnlInit.setLayout(null);

        // Bottone per accedere
        JButton btnAccedi = new JButton("ACCEDI");
        btnAccedi.setBounds(30, 44, 124, 61);
        pnlInit.add(btnAccedi);

        // Bottone per la registrazione
        JButton btnRegistrati = new JButton("NUOVO CONFIG.");
        btnRegistrati.setBounds(164, 44, 130, 61);
        pnlInit.add(btnRegistrati);

        // Listener per il bottone di registrazione
        btnRegistrati.addActionListener(e -> {
            initialize();
            primoAccesso = true;
            pnlInit.setVisible(false);
        });

        // Listener per il bottone di accesso
        btnAccedi.addActionListener(e -> {
            login();
            pnlInit.setVisible(false);
        });
    }
} // Fine classe LoginApp
