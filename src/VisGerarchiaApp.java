import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Classe VisGerarchiaApp
 * 
 * Rappresenta una applicazione grafica per la visualizzazione di gerarchie di dati.
 * Utilizza un controllerlo (controller) per la gestione dei dati e Swing per l'interfaccia utente.
 */
public class VisGerarchiaApp {

    private JFrame software; // Finestra principale
    private JPanel panel2; // Pannello principale
    private final Controller controller; // controllerlo dei dati
    private JPanel treePanel; // Pannello per visualizzare l'albero

    /**
     * Costruttore per VisGerarchiaApp
     * 
     * @param controller il controllerlo dei dati per la gerarchia da visualizzare
     */
    public VisGerarchiaApp(Controller controller) {
        this.controller = controller; // Inizializza il controllerlo
        swInitialize(); // Chiama il metodo per inizializzare l'interfaccia grafica
    }

    /**
     * Inizializza l'interfaccia grafica e i suoi componenti.
     * 
     * Precondizione: Il controllerlo deve essere inizializzato correttamente.
     */
    private void swInitialize() {
        software = new JFrame("Visualizzazione gerarchie"); // Crea la finestra principale
        software.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiudi l'app quando la finestra è chiusa
        software.setBounds(100, 100, 800, 600); // Dimensioni della finestra
        software.setLocationRelativeTo(null); // Centra la finestra sullo schermo
        software.setLayout(new BorderLayout()); // Usa BorderLayout per la finestra principale

        // Crea il pannello principale con BorderLayout
        panel2 = new JPanel(new BorderLayout());
        panel2.setBackground(Color.WHITE); // Imposta il colore di sfondo

        // Crea uno JScrollPane per il pannello principale
        JScrollPane scrollPane = new JScrollPane(panel2);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        software.add(scrollPane, BorderLayout.CENTER); // Aggiungi lo JScrollPane alla finestra principale

        List<Nonfoglia> listaGer = controller.getListaGerarchie(); // Ottieni la lista delle gerarchie
        HashMap<String, Integer> gerarchie = new HashMap<>(); // Mappa per associare nomi a ID

        // Popola la mappa con i nomi e gli ID
        for (Nonfoglia elem : listaGer) {
            gerarchie.put(elem.getNome(), elem.getId());
        }

        // Crea un JComboBox con i nomi delle gerarchie
        JComboBox<String> comboBox = new JComboBox<>(gerarchie.keySet().toArray(new String[0]));

        // Aggiungi un ActionListener al JComboBox
        comboBox.addActionListener((@SuppressWarnings("unused") ActionEvent e) -> {
            int id = gerarchie.get((String) comboBox.getSelectedItem());
            try {
                Nonfoglia radice = controller.getGerarchia(id);
                if (treePanel != null) {
                    panel2.remove(treePanel);
                }

                // Crea il nuovo pannello per visualizzare l'albero
                treePanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.ORANGE);
                        setLayout(null); // Layout personalizzato

                        int p = calcolaProfondita(radice);
                        int yOffset = (panel2.getHeight() - 25 - 30 * p) / (p - 1);
                        drawTree(radice, p, 0, getWidth(), 25, yOffset, g, this); // Disegna l'albero
                    }
                };
                treePanel.setBackground(Color.WHITE);

                // Aggiungi il nuovo treePanel al pannello principale
                panel2.add(treePanel, BorderLayout.CENTER);
                panel2.revalidate();
                panel2.repaint();
            } catch (Exception ex) {
                System.err.println("Errore: " + ex.getMessage());
            }
        });

        // Aggiungi JComboBox in cima al pannello
        panel2.add(comboBox, BorderLayout.NORTH);

        // Aggiungi un listener per il ridimensionamento della finestra
        software.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (treePanel != null) {
                    panel2.revalidate();
                    panel2.repaint();
                    treePanel.removeAll();
                    treePanel.revalidate();
                    treePanel.repaint();
                }
            }
        });

        software.setVisible(true); // Rendi visibile la finestra
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

        // Crea l'etichetta per il nodo attuale
        JLabel label = new JLabel(node.getNome(), SwingConstants.CENTER);

        FontMetrics metrics = label.getFontMetrics(label.getFont());
        int textWidth = metrics.stringWidth(label.getText());
        int buttonWidth = Math.min((xspaceF - xspaceS), textWidth + 20);
        int buttonHeight = 30;

        int xpos = xspaceS + (xspaceF - xspaceS) / 2 - (buttonWidth / 2);
        label.setBounds(xpos, y, buttonWidth, buttonHeight);

        LineBorder border = (node instanceof Nonfoglia) ? new LineBorder(Color.ORANGE, 3) : new LineBorder(Color.GREEN, 3);
        label.setBorder(border);

        // Configura finestre di hover per informazioni aggiuntive
        JFrame hoverWindow = new JFrame();
        hoverWindow.setSize(250, 150);
        hoverWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JFrame hoverWindow2 = new JFrame();
        hoverWindow2.setBounds(120, 120, 1000, 300); // Dimensioni della finestra
        hoverWindow2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        // Aggiungi un MouseListener per l'evento hover
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel hoverText = new JLabel(info(node), SwingConstants.CENTER);
                hoverWindow.setSize(hoverWindow.getWidth(), hoverText.getPreferredSize().height + 100);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Point labelLocation = label.getLocationOnScreen();
                int hoverX = labelLocation.x + label.getWidth();
                int hoverY = labelLocation.y;

                if (hoverX + hoverWindow.getWidth() > screenSize.width) {
                    hoverX = labelLocation.x - hoverWindow.getWidth();
                }
                if (hoverY + hoverWindow.getHeight() > screenSize.height) {
                    hoverY = labelLocation.y - hoverWindow.getHeight();
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
        else if(controller.getFruitore() == null){ //se il nodo è una foglia e l'utente è un cconfiguratore mostra tutte le proposte che coinvolgono la foglia
            label.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Imposta il cursore come una mano

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Azione da eseguire quando si clicca la label
                    
                    JTextArea hoverText2 = new JTextArea(info2(node.getId()));
                    hoverText2.setEditable(false);
                    JScrollPane scroll = new JScrollPane(hoverText2);
                    hoverWindow2.add(scroll, BorderLayout.CENTER);
                    hoverWindow2.setVisible(true);
                }
            });
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
        assert nodo != null : "il nodo passato non deve essere null";

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
        assert c != null : "la categoria non deve essere null";

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
            ArrayList<Float> fattori = fattore.getFattoriCategoria(f.getId());
            s.append("<html><body>");
            for (int i = 0; i < fattori.size(); i++) {
                if (i != f.getId()) {
                    Foglia ff = controller.getFoglia(i);
                    s.append(ff.getNome()).append("<br>").append(String.format("%.2f", fattori.get(i))).append("<br>");
                }
            }   
        }
        s.append("</body></html>");
        return s.toString();
    }

    /**
     * Genera una lista di tutte le proposte che coinvolgono una foglia.
     *
     * @param id l'id della foglia di cui genera le informazioni
     * @return la stringa  co le informazioni sulla foglia
     */
    private String info2 (int id){
        ArrayList<String> info = controller.caricaProposteById(id);
        StringBuilder s = new StringBuilder();
        if(info.isEmpty()) s.append("nessuna proposta contiene questa attività!");
        for (String elem : info) {
            String[]elems = elem.split(";");

            s.append("La proposta ")
                .append(elems[0].split(":")[1].trim())
                .append(" dell'utente ")
                .append(controller.getFruitoreName(Integer.parseInt(elems[6].split(":")[1].trim())))
                .append(": ")
                .append(elems[2].split(":")[1].trim())
                .append(" ore di ")
                .append(controller.getFoglia(Integer.parseInt(elems[1].split(":")[1].trim())).getNome())
                .append(" in cambio di ")
                .append(elems[4].split(":")[1].trim())
                .append(" ore di ")
                .append(controller.getFoglia(Integer.parseInt(elems[3].split(":")[1].trim())).getNome())
                .append(", è stata ")
                .append(elems[5].split(":")[1].trim())
                .append(" in ")
                .append(elems[7])
                .append("\n\n");
            
        }

        return s.toString();
    }
}
