import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public final class LoginFruitore {

    private JFrame window;
    private JPanel pnlPredefinito;
    private JPanel pnlLogin;
    private final Controller controller;

    /**
     * Costruttore di `LoginFruitore`.
     * 
     * Precondizioni:
     * - `controller` non deve essere null.
     * 
     * Postcondizioni:
     * - Crea una nuova finestra di login per il fruitore.
     */
    public LoginFruitore(Controller controller) {
        assert controller != null : "Il controller non può essere null";
        this.controller = controller;
        creaLogin();
    }

    /**
     * Inizializza la finestra di registrazione con i componenti necessari.
     * 
     * Precondizioni:
     * - `comp` non deve essere null.
     * 
     * Postcondizioni:
     * - Mostra una finestra di registrazione con campi per username, password e email.
     */
    @SuppressWarnings("unused")
    private void initialize(Comprensorio comp) {
        assert comp != null : "Comprensorio non può essere null";

        pnlPredefinito = new JPanel();
        pnlPredefinito.setLayout(null);
        window.getContentPane().add(pnlPredefinito);

        // Etichetta per l'username
        JLabel lblText = new JLabel("Inserisci username da usare:");
        lblText.setBounds(31, 10, 207, 20);
        lblText.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlPredefinito.add(lblText);

        JTextField userText = new JTextField(20);
        userText.setBounds(31, 40, 233, 30);
        pnlPredefinito.add(userText);

        JLabel lblPassword = new JLabel("Inserisci una password:");
        lblPassword.setBounds(31, 80, 194, 16);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlPredefinito.add(lblPassword);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(31, 106, 233, 30);
        pnlPredefinito.add(passwordText);

        JLabel lblMail = new JLabel("Indirizzo di posta elettronica:");
        lblMail.setBounds(31, 146, 207, 16);
        lblMail.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlPredefinito.add(lblMail);

        JTextField txtMail = new JTextField();
        txtMail.setBounds(31, 178, 232, 30);
        pnlPredefinito.add(txtMail);
        txtMail.setColumns(10);

        JLabel messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
        messageLabel.setBounds(31, 220, 376, 30);
        pnlPredefinito.add(messageLabel);

        JButton btnRegister = new JButton("REGISTER");
        btnRegister.setBounds(286, 87, 120, 75);
        btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 15));
        pnlPredefinito.add(btnRegister);

        btnRegister.addActionListener(e -> {
            String username = userText.getText().trim();
            String password = new String(passwordText.getPassword()).trim();
            String mail = txtMail.getText().trim();

            // Validazione dei dati inseriti
            if(username.equals("") || password.equals("") || mail.equals("")){
                messageLabel.setText("Compilare tutti i campi");
            }
            else if (!controller.inUso(username) && isValidEmail(mail)) {
                window.setVisible(false);
                controller.creaFruitore(username, password, mail, comp);
                new App(controller); // Entra nell'app
            } else {
                messageLabel.setText("Dati errati! Riprova.");
                passwordText.setText(null);
                userText.setText(null);
                txtMail.setText(null);
            }
        });
    }

    /**
     * Verifica se l'indirizzo email è valido.
     * 
     * Precondizioni:
     * - `email` può essere null.
     * 
     * Postcondizioni:
     * - Restituisce true se l'email è valida, altrimenti false.
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Mostra la finestra di login.
     */
    @SuppressWarnings("unused")
    public void login() {
        pnlLogin = new JPanel();
        pnlLogin.setBounds(0, 10, 450, 280);
        pnlLogin.setLayout(null);
        window.getContentPane().add(pnlLogin);

        JLabel lblCredenziali = new JLabel("Inserisci le tue credenziali:");
        lblCredenziali.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblCredenziali.setBounds(29, 10, 264, 37);
        pnlLogin.add(lblCredenziali);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(29, 99, 245, 27);
        pnlLogin.add(txtUser);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(29, 167, 245, 26);
        pnlLogin.add(txtPass);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 19));
        btnLogin.setBounds(295, 99, 109, 94);
        pnlLogin.add(btnLogin);

        JLabel lblExit = new JLabel("");
        lblExit.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblExit.setBounds(52, 220, 325, 23);
        pnlLogin.add(lblExit);

        btnLogin.addActionListener(e -> {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword()).trim();
            if(username.equals("") || password.equals("")){
                lblExit.setText("Compilare tutti i campi");
            }
            else if (controller.listaContainsF(username, password)) {
                window.setVisible(false);
                new App(controller); // Entra nell'app
            } else {
                lblExit.setText("Credenziali errate!");
                txtPass.setText(null);
                txtUser.setText(null);
            }
        });
    }

    /**
     * Crea la finestra principale con opzioni di login e registrazione.
     */
    @SuppressWarnings("unused")
    public void creaLogin() {
        window = new JFrame("Registrazione Fruitore");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(450, 280);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        JPanel pnl = new JPanel();
        window.getContentPane().add(pnl);
        pnl.setLayout(null);

        JButton btnAccedi = new JButton("ACCEDI");
        btnAccedi.setBounds(53, 76, 148, 73);
        pnl.add(btnAccedi);

        JButton btnRegistrati = new JButton("NUOVO FRUITORE");
        btnRegistrati.setBounds(228, 76, 148, 73);
        pnl.add(btnRegistrati);

        btnRegistrati.addActionListener(e -> {
            ArrayList<String> opt = new ArrayList<>();
            if (controller.getComprensori() != null) {
                for (Comprensorio rigaC : controller.getComprensori()) {
                    opt.add(rigaC.getNome());
                }
            }

            JList<String> list = new JList<>(opt.toArray(new String[0]));
            JScrollPane scrollPane = new JScrollPane(list);
            int option = JOptionPane.showConfirmDialog(null, scrollPane, "Seleziona un comprensorio", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION && list.getSelectedValue() != null) {
                Comprensorio comp = controller.getCompbyName(list.getSelectedValue());
                initialize(comp);
                pnl.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(scrollPane, "Devi scegliere un comprensorio");
            }
        });

        btnAccedi.addActionListener(e -> {
            login();
            pnl.setVisible(false);
        });
    }
}
