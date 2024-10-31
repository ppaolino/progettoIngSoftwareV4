import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainProgram{

	private JFrame frame;
	private JPanel pannello;	
    private final Model model;

	public static void main(String[] args) {
		
    	EventQueue.invokeLater(() -> {
            try {
                MainProgram window = new MainProgram();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            });
    }

	public MainProgram() {
		model = new Model();
		frame = new JFrame("Scelta tipologia di utente");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setBounds(100, 100, 431, 275);
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
		
		btnConfig.addActionListener(e -> {
			LoginApp window = new LoginApp(model);
            window.setVisible();
			frame.setVisible(false);
		});
		
		
		btnFruitore.addActionListener(e -> {
			new LoginFruitore(model);
			frame.setVisible(false);
		});
	}
}