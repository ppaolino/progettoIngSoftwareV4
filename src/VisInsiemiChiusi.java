
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.*;

public class VisInsiemiChiusi {
    private JPanel pane; // Pannello principale
    private JFrame window; // Finestra principale
    private JTextArea txtArea; // Area di testo per visualizzare i comuni

    private final Controller controller; // controllerlo dei dati

    /**
     * Costruttore per VisInsiemiChiusi
     * 
     * @param controller il controllerlo dei dati contenente gli insiemi chiusi
     */
    public VisInsiemiChiusi(Controller controller) {
        this.controller = controller; // Inizializza il controllerlo
        initialize(); // Inizializza l'interfaccia grafica
    }

    /**
     * Inizializza l'interfaccia grafica e i suoi componenti.
     * 
     * Precondizione: il controllerlo deve contenere una lista di comprensori valida.
     * Postcondizione: la finestra è configurata e visibile con i comprensori caricati nella comboBox.
     */
    private void initialize() {
        // Configura la finestra principale
        window = new JFrame("Visualizza tutti gli insiemi chiusi");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setBounds(100, 100, 614, 417);

        pane = new JPanel(); // Pannello di contenuto principale
        window.setContentPane(pane);
        pane.setLayout(new BorderLayout());

        // Inizializza l'area di testo per mostrare le attività coinvolte
        txtArea = new JTextArea();
        txtArea.setEditable(false); // Rende l'area di testo non modificabile
        txtArea.setFont(new Font("Serif", Font.PLAIN, 14)); // Imposta il font della JTextArea
        JScrollPane scroll = new JScrollPane(txtArea);
        pane.add(scroll, BorderLayout.CENTER);

        // Carica gli insiemi chiusi nel JComboBox
        ArrayList<String> lista = controller.caricaInsiemiChiusi();
        txtArea.setText(""); // Ripulisce l'area di testo
        for (String lista1 : lista) {
            String[] proposte = lista1.split("%");
            StringBuilder out = new StringBuilder();
            for (String prop : proposte) {
                out.setLength(0);
                String[] elem = prop.split(";");

                String val = elem[6].split(":")[1].trim();
                int id = Integer.parseInt(val);
                String nome = controller.getFruitoreName(id);
                String mail = controller.getFruitoreMail(id);
                out.append(nome)
                        .append(" (")
                        .append(mail)
                        .append(") offre ")
                        .append(elem[4].split(":")[1].trim())
                        .append(" ore di ");
                
                nome = controller.getFoglia(Integer.parseInt(elem[3].split(":")[1].trim())).getNome();
                out.append(nome)
                        .append(", in cambio di ")
                        .append(elem[2].split(":")[1].trim())
                        .append(" ore di ");
                nome = controller.getFoglia(Integer.parseInt(elem[1].split(":")[1].trim())).getNome();
                out.append(nome);
                txtArea.append(out.toString() + "\n\n");
            }
            txtArea.append(out.toString() + "\n\n ------------------------------ \n\n");
        }

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
