
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;

public final class LoginFruitore {
    
    private JFrame window;
    private JPanel pnlPredefinito;
    private JPanel pnlLogin; 
    private final Model model;
 																	  	      
     public LoginFruitore(Model model) {	
    	 this.model = model;
    	 creaLogin();    	 
	}
        
    // Metodo per posizionare i componenti nella finestra
    private void initialize(Comprensorio comp) {
    	
         // Pannello principale per il contenuto
         pnlPredefinito = new JPanel();
         window.getContentPane().add(pnlPredefinito);
         pnlPredefinito.setLayout(null);
                            
         // Etichetta per il nome utente
         JLabel lblText = new JLabel("Inserisci username da usare:");
         lblText.setBounds(31, 10, 207, 20);
         lblText.setFont(new Font("Tahoma", Font.PLAIN, 15));
         pnlPredefinito.add(lblText);
                           
         // Campo di testo per l'inserimento dell'username
         JTextField userText = new JTextField(20);
         userText.setBounds(31, 40, 233, 30);
         pnlPredefinito.add(userText);
                  
         // Bottone di login
         JButton btnRegister = new JButton("REGISTER");
         btnRegister.setBounds(286, 87, 120, 75);
         btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 15));
         pnlPredefinito.add(btnRegister);
         
         // Etichetta per la password
         JLabel lblPassword = new JLabel("Inserisci una password:");
         lblPassword.setBounds(31, 80, 194, 16);
         lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
         pnlPredefinito.add(lblPassword);
         
         // Campo di testo per l'inserimento della password (con cifratura dei caratteri)
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
         
         // Etichetta per mostrare eventuali messaggi (successo/errore)
         JLabel messageLabel = new JLabel("");
         messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
         messageLabel.setBounds(31, 206, 376, 30);
         pnlPredefinito.add(messageLabel);
          
                           
         // Aggiungiamo il listener per il bottone di login
         btnRegister.addActionListener((ActionEvent e) -> {
             String username = userText.getText().trim();
             String password = new String(passwordText.getPassword()).trim();
             
             String mail = txtMail.getText().trim();
             
             
             
             if(!model.inUsoFruitore(username) && isValidEmail(mail)){
                 
                 window.setVisible(false);
                 
                 model.creaFruitore(username, password, mail, comp);
                 
                 //entra nell'app
                 new App(model);
                 
             }else {
                 messageLabel.setText("Dati errati! Riprova.");
                 passwordText.setText(null);
                 userText.setText(null);
                 txtMail.setText(null);
             }
         });        
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
    
    
    public void login() {
    	
    	pnlLogin = new JPanel();
        pnlLogin.setBounds(0, 10, 450, 280);
        pnlLogin.setLayout(null);
        window.getContentPane().add(pnlLogin);
    	
    	JLabel lblCredenziali = new JLabel("Inserisci le tue credenziali:");
        lblCredenziali.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblCredenziali.setBounds(29, 10, 264, 37);
        pnlLogin.add(lblCredenziali);
        
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblUser.setBounds(29, 68, 88, 21);
        pnlLogin.add(lblUser);
        
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblPass.setBounds(29, 136, 88, 21);
        pnlLogin.add(lblPass);
    	
    	JTextField txtUser = new JTextField();
    	txtUser.setBounds(29, 99, 245, 27);
    	pnlLogin.add(txtUser);
        txtUser.setColumns(10);
    	
    	JPasswordField txtPass = new JPasswordField();
    	txtPass.setBounds(29, 167, 245, 26);
    	pnlLogin.add(txtPass);
    	txtPass.setColumns(10);
    	
    	JButton btnLogin = new JButton("LOGIN");
    	btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 19));
    	btnLogin.setBounds(295, 99, 109, 94);
    	pnlLogin.add(btnLogin);
    	
    	JLabel lblExit = new JLabel("");
    	lblExit.setHorizontalAlignment(SwingConstants.CENTER);
    	lblExit.setFont(new Font("Tahoma", Font.BOLD, 15));
    	lblExit.setBounds(52, 220, 325, 23);
    	pnlLogin.add(lblExit);
    	
      	btnLogin.addActionListener((ActionEvent e) -> {
            String username = txtUser.getText().trim();
            String password = txtPass.getText().trim();
            
            
            if(model.listaContainsF(username, password)) {
                window.setVisible(false);
                
                //entra nell'app
                new App(model);
                
            }else {
                lblExit.setText("Credenziali errate!");
                txtPass.setText(null);
                txtUser.setText(null);
            }
            });
    }
    
    
        
    
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
     		
     		 if (model.getComprensori() != null) {
     	            for (Comprensorio rigaC : model.getComprensori()) {
     	            	opt.add(rigaC.getNome());
     	            }	        				
     		 }

     		 // Creazione di una JList con le opzioni
     		 JList<String> list = new JList<>(opt.toArray(new String[0]));
     		 list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

     		 JScrollPane scrollPane = new JScrollPane(list);
     		 scrollPane.setPreferredSize(new Dimension(200, 100));

     		 // Mostra il JOptionPane con il pannello
     		 int option = JOptionPane.showConfirmDialog(null, scrollPane, "Seleziona un'opzione", JOptionPane.OK_CANCEL_OPTION);

     		 if(option == JOptionPane.OK_OPTION) {    			 
     			 String selectedValue = list.getSelectedValue();  
     			 if(!(selectedValue == null)) {
     				 
     				 Comprensorio comp = model.getCompbyName(selectedValue);
     				 initialize((comp));        	
     				 pnl.setVisible(false);
     			 } else 
     			 JOptionPane.showMessageDialog(scrollPane, "Devi scegliere un comprensorio");
     		 }
        });
        
        btnAccedi.addActionListener(e -> {
        	login();
        	pnl.setVisible(false);
        });
        
    }
}
