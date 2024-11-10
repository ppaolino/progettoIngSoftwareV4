import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Classe NewPropostaApp
 * 
 * Rappresenta una applicazione grafica per la compilazione di proposte.
 * Utilizza un modello (Model) per la gestione dei dati e Swing per l'interfaccia utente.
 */
public class NewPropostaApp {

    private JFrame software; // Finestra principale
    private JPanel panel; // Pannello principale
    private final Model model; // Modello dei dati
    private JPanel treePanel; // Pannello per visualizzare l'albero
    private JButton conferma;            // Pulsante per confermare l'inserimento
    private final JFrame infoFrame = new JFrame();
    private JComboBox<String> comboBox;
    private Foglia richiesta;
    private int number = 0;

    /**
     * Costruttore per NewPropostaApp
     * 
     * @param model il modello dei dati
     */
    public NewPropostaApp(Model model) {
        this.model = model; // Inizializza il modello
        swInitialize(); // Chiama il metodo per inizializzare l'interfaccia grafica
    }

    /**
     * Inizializza l'interfaccia grafica e i suoi componenti.
     * 
     * Precondizione: Il modello deve essere inizializzato correttamente.
     */
    @SuppressWarnings("unused")
    private void swInitialize() {
        assert model != null : "modello deve essere inizializzato";

        software = new JFrame("Crea proposta"); // Crea la finestra principale
        software.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiudi l'app quando la finestra è chiusa
        software.setBounds(100, 100, 800, 600); // Dimensioni della finestra
        software.setLocationRelativeTo(null); // Centra la finestra sullo schermo
        software.setLayout(new BorderLayout()); // Usa BorderLayout per la finestra principale

        // Crea il pannello principale con BorderLayout
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE); // Imposta il colore di sfondo
       
        ArrayList<Nonfoglia> listaGer = model.getListaGerarchie(); // Ottieni la lista delle gerarchie
        HashMap<String, Integer> gerarchie = new HashMap<>(); // Mappa per associare nomi a ID

        // Popola la mappa con i nomi e gli ID
        for (Nonfoglia elem : listaGer) {
            gerarchie.put(elem.getNome(), elem.getId());
        }

        // Crea un JComboBox con i nomi delle gerarchie
        comboBox = new JComboBox<>(gerarchie.keySet().toArray(new String[0]));

        // Aggiungi un ActionListener al JComboBox
        comboBox.addActionListener((ActionEvent e) -> {

            conferma = new JButton("abortisci");
            conferma.setBounds(
                panel.getWidth()/2 - ((conferma.getPreferredSize().width + 10)/2), 
                panel.getHeight() - conferma.getPreferredSize().height, 
                conferma.getPreferredSize().width + 10, 
                conferma.getPreferredSize().height);
    
            conferma.addActionListener((ActionEvent ez) -> {
                software.dispose();
            });
            panel.add(conferma);  // Aggiungi il bottone al frame

            int id = gerarchie.get((String) comboBox.getSelectedItem());
            try {
                Nonfoglia radice = model.getGerarchia(id);
                if (treePanel != null) {
                    panel.remove(treePanel);
                }

                // Crea il nuovo pannello per visualizzare l'albero
                treePanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.ORANGE);
                        int p = calcolaProfondita(radice);
                        int yOffset = (getHeight() - 35 - conferma.getPreferredSize().height - 30 * p) / (p - 1);
                        drawTree(radice, p, 0, getWidth(), 25, yOffset, g, this);
                    }
                };
                treePanel.setBackground(Color.WHITE);
                panel.setLayout(new BorderLayout());
                panel.add(treePanel, BorderLayout.CENTER);

                panel.revalidate();
                panel.repaint();

            } catch (Exception ex) {
                System.err.println("Errore: " + ex.getMessage());
            }
        });

        

        // Aggiungi un listener per il ridimensionamento della finestra
        software.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateTree();
            }
        });

        // Aggiungi JComboBox in cima al pannello
        panel.add(comboBox, BorderLayout.NORTH);
        software.add(panel, BorderLayout.CENTER);
        software.setVisible(true); // Rendi visibile la finestra
        JOptionPane.showMessageDialog(infoFrame, "Seleziona la categoria richiesta");

    }

    //metodo per aggiornare la grafica
    public void updateTree(){
        if (treePanel != null) {
            panel.revalidate();
            panel.repaint();
            comboBox.setSize(panel.getWidth(), comboBox.getHeight());
            conferma.setBounds(
                panel.getWidth()/2 - ((conferma.getPreferredSize().width + 10)/2), 
                panel.getHeight() - conferma.getPreferredSize().height, 
                conferma.getPreferredSize().width + 10, 
                conferma.getPreferredSize().height);

            treePanel.removeAll();
            treePanel.revalidate();
            treePanel.repaint();
        }
    }

    /**
     * Metodo ricorsivo per disegnare l'albero.
     *
     * @param node      il nodo corrente
     * @param p         la profondità dell'albero
     * @param xspaceS   lo spazio iniziale in x
     * @param xspaceF   lo spazio finale in x
     * @param y         la coordinata y del nodo
     * @param yOffset   l'offset verticale per il posizionamento
     * @param g         l'oggetto Graphics
     * @param panel     il pannello su cui disegnare
     * @return          l'offset verticale utilizzato
     * 
     * Postcondizione: tutti i nodi e le connessioni dell'albero sono disegnati sul pannello.
     */
    private int drawTree(Categoria node, int p, int xspaceS, int xspaceF, int y, int yOffset, Graphics g, JPanel panel) {

        JButton label = new JButton(node.getNome());
        label.setBackground(Color.WHITE);
        if(node instanceof Nonfoglia) label.setEnabled(false);
        if(richiesta != null)
            if(node.getId() == richiesta.getId())
                label.setEnabled(false);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        int textWidth = metrics.stringWidth(label.getText());
        int buttonWidth = Math.min((xspaceF - xspaceS), textWidth + 20);
        int buttonHeight = 30;

        int xpos = xspaceS + (xspaceF - xspaceS) / 2 - (buttonWidth / 2);
        label.setBounds(xpos, y, buttonWidth, buttonHeight);

        LineBorder border = (node instanceof Nonfoglia) ? new LineBorder(Color.ORANGE, 2) : new LineBorder(Color.GREEN, 2);
        label.setBorder(border);

        // Configura finestra di hover per informazioni aggiuntive
        JFrame hoverWindow = new JFrame();
        hoverWindow.setSize(250, 150);
        hoverWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Aggiungi un MouseListener per l'evento hover
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel hoverText;
                if(richiesta == null || node instanceof Nonfoglia)
                    hoverText = new JLabel(info(node), SwingConstants.CENTER);
                else if(node.getId() != richiesta.getId())
                    hoverText = new JLabel("<html><body>ore necessarie per soddisfare la richiesta:<br>" + model.getOreConvertite(richiesta.getId(), number).get(node.getId()) + "</body></html>");
                else
                    hoverText = new JLabel("categoria richiesta");
                
                hoverWindow.setSize(hoverWindow.getWidth(), hoverText.getPreferredSize().height + 100);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Point labelLocation = label.getLocationOnScreen();
                int hoverX = labelLocation.x + label.getWidth();
                int hoverY = labelLocation.y;

                if (hoverX + hoverWindow.getWidth() + 100 > screenSize.width) {
                    hoverX = labelLocation.x - hoverWindow.getWidth();
                }
                if (hoverY + hoverWindow.getHeight() > screenSize.height) {
                    hoverY = labelLocation.y - hoverWindow.getHeight() + 100;
                }

                hoverWindow.setLocation(hoverX, hoverY);
                hoverWindow.add(hoverText);
                hoverWindow.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverWindow.getContentPane().removeAll();
                hoverWindow.setVisible(false);
            }
        });
        label.setActionCommand("" + node.getId());
        label.addActionListener(e -> {
            if(richiesta == null){
                int id = Integer.parseInt(e.getActionCommand());
                Foglia ric = model.getFoglia(id);
                if(ric != null) richiesta = ric;

                boolean validInput = false;
                String input;
                

                // Ciclo per ottenere un numero valido di figli
                while (!validInput) {
                    input = JOptionPane.showInputDialog(
                        infoFrame,
                        "Inserisci il numero di ore richieste:",
                        "Numero di figli",
                        JOptionPane.PLAIN_MESSAGE
                    );

                    // Se l'utente chiude il dialogo, termina l'applicazione
                    if (input == null) {
                        System.exit(0);
                    }

                    try {
                        number = Integer.parseInt(input);
                        // Invariante: il numero di figli deve essere maggiore di zero
                        if (number > 0) {
                            validInput = true; // Input valido
                        } else {
                            JOptionPane.showMessageDialog(infoFrame, 
                                "Per favore, inserisci un numero maggiore di zero.", 
                                "Errore", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException es) {
                        JOptionPane.showMessageDialog(infoFrame, 
                            "Input non valido. Inserisci un numero.", 
                            "Errore", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }

                JOptionPane.showMessageDialog(infoFrame, 
                    "Seleziona la foglia offerta.", 
                    "Errore", 
                    JOptionPane.PLAIN_MESSAGE);

                hoverWindow.getContentPane().removeAll();
                hoverWindow.setVisible(false);
                updateTree();

            }
            else if(richiesta != null){
                int id = Integer.parseInt(e.getActionCommand());
                Foglia offerta = model.getFoglia(id);
                if(offerta != null) model.creaProposta(richiesta, number, offerta);
                hoverWindow.getContentPane().removeAll();
                hoverWindow.setVisible(false);
                hoverWindow.dispose();
                software.dispose();
            }
        	
        });

        panel.add(label);

        // Disegna ricorsivamente i figli
        if (node instanceof Nonfoglia nonfoglia) {
            Categoria[] figli = nonfoglia.getFigli();
            int numChildren = figli.length;
            y += yOffset;

            if (numChildren > 0) {
                int larghezzaXfiglio = (xspaceF - xspaceS) / numChildren;

                for (int i = 0; i < numChildren; i++) {
                    Categoria child = figli[i];
                    int startX = xspaceS + larghezzaXfiglio * i;
                    int finishX = xspaceS + larghezzaXfiglio * i + larghezzaXfiglio;

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(3));
                    g.drawLine(xpos + buttonWidth / 2, y - yOffset + buttonHeight, (startX + (finishX - startX) / 2), y);

                    yOffset = drawTree(child, p, startX, finishX, y, yOffset, g, panel);
                }
            }
        }
        return yOffset;
    }

    

    /**
     * Calcola la profondità massima dell'albero.
     *
     * @param nodo la radice dell'albero
     * @return la profondità massima
     */
    public static int calcolaProfondita(Nonfoglia nodo) {
        if ("foglie".equals(nodo.getTipoFigli())) {
            return 2;
        }

        int profonditaMassima = 1;
        for (Categoria figlio : nodo.getFigli()) {
            profonditaMassima = Math.max(profonditaMassima, calcolaProfondita((Nonfoglia) figlio));
        }
        return profonditaMassima + 1;
    }

    /**
     * Genera una descrizione HTML per un nodo Categoria.
     *
     * @param c il nodo Categoria di cui generare le informazioni
     * @return la stringa HTML con le informazioni sul nodo
     */
    private String info(Categoria c) {
        Fattorediconversione fattore = Fattorediconversione.getInstance();
        StringBuilder s = new StringBuilder();

        if (c instanceof Nonfoglia nonfoglia) {
            s.append("<html><body>").append(nonfoglia.getCampo()).append("<br>");
            for (int i = 0; i < nonfoglia.getDescrizione().length; i++) {
                s.append(nonfoglia.getDominio()[i]);
                s.append(", ").append(nonfoglia.getDescrizione()[i]).append("<br>");
            }
        } else {
            Foglia f = (Foglia) c;
            if(f!= null){
                ArrayList<Float> fattori = fattore.getFattoriCategoria(f.getId());
                s.append("<html><body>");
                for (int i = 0; i < fattori.size(); i++) {
                    if (i != f.getId()) {
                        Foglia ff = model.getFoglia(i);
                        s.append(ff.getNome()).append("<br>").append(String.format("%.2f", fattori.get(i))).append("<br>");
                    }
                }
            }
            
        }
        s.append("</body></html>");
        return s.toString();
    }
}
