import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Classe CreaGerarchia per gestire la creazione e visualizzazione interattiva di una gerarchia.
 * Implementa ActionListener per gestire gli eventi di interazione utente.
 */
public class CreaGerarchia implements ActionListener {
    private final Model model;           // Modello dei dati per la gerarchia
    private JPanel panel;                // Pannello principale per visualizzare gli elementi della GUI
    private JFrame software;             // Finestra principale dell'applicazione
    private JFrame infoRequest;          // Finestra per richieste di informazione
    private JFrame infoRequest1;         // Finestra per selezione di opzioni
    private JTextField insertName;       // Campo di testo per l'inserimento del nome della gerarchia
    private JLabel labelTestoIni;        // Etichetta per il testo iniziale
    private JButton inserimento;         // Pulsante per inviare il nome della gerarchia
    private JButton conferma;            // Pulsante per confermare l'inserimento
    private JPanel treePanel;            // Pannello per visualizzare l'albero della gerarchia
    private Nonfoglia nf;                // Nodo principale (non foglia) della gerarchia

    /**
     * Costruttore di CreaGerarchia.
     * Precondizione: `model` non deve essere null.
     * Postcondizione: L'istanza viene inizializzata con `swInitialize()`.
     *
     * @param model Modello da usare per la gerarchia.
     */
    public CreaGerarchia(Model model) {
        this.model = model;       // Inizializza il modello dei dati
        swInitialize();           // Chiama il metodo per l'inizializzazione della GUI
    }

    /**
     * Inizializza l'interfaccia grafica dell'applicazione.
     * Precondizione: Nessuna.
     * Postcondizione: L'interfaccia viene visualizzata con i componenti necessari per la creazione di una gerarchia.
     */
    private void swInitialize() {
        // Crea la finestra principale per la creazione delle gerarchie
        software = new JFrame("Crea Gerarchie");
        software.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Imposta l'azione di chiusura
        software.setBounds(100, 100, 800, 600);                    // Dimensioni della finestra
        software.setLocationRelativeTo(null);                      // Centra la finestra

        software.setLayout(new BorderLayout()); // Imposta il layout BorderLayout per la finestra principale

        // Pannello principale
        panel = new JPanel();
        panel.setBackground(Color.WHITE);       // Colore di sfondo del pannello
        labelTestoIni = new JLabel("inserisci il nome della gerarchia: ",  SwingConstants.CENTER);
        panel.add(labelTestoIni); // Testo introduttivo

        // Campo di testo per l'inserimento del nome della gerarchia
        insertName = new JTextField(20);
        panel.add(insertName);

        // Bottone di invio
        inserimento = new JButton("Invia");
        inserimento.setPreferredSize(new Dimension(inserimento.getPreferredSize().width + 10, insertName.getPreferredSize().height));
        inserimento.setActionCommand("lancia"); // Comando d'azione per il bottone
        inserimento.addActionListener(this);    // Aggiunge il listener per gli eventi
        panel.add(inserimento);

        software.add(panel, BorderLayout.CENTER); // Aggiunge il pannello principale alla finestra
        software.setVisible(true);               // Rende visibile la finestra
    }

    /**
     * Metodo per gestire gli eventi ActionEvent, inclusi pulsanti e comandi.
     * Precondizione: Nessuna.
     * Postcondizione: Le azioni sono eseguite in base all'ActionCommand ricevuto.
     *
     * @param e L'evento ActionEvent generato da un'interazione dell'utente.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("lancia".equals(e.getActionCommand())) { // Azione per il comando 'lancia'
            String name = insertName.getText();
            if (!"".equals(name)) {   // Controllo del nome non vuoto
                if (model.isNameClear(name)) {  // Controlla se il nome è disponibile
                    software.setTitle("Creazione della gerarchia " + name);
                    //labelTestoIni.setForeground(Color.BLACK);
                    nf = creaNonfoglia(name, true, "inutile", -1);

                    // Rimuove i componenti obsoleti dal pannello
                    panel.remove(inserimento);
                    panel.remove(insertName);
                    labelTestoIni.setText("Seleziona una categoria per compilarla");
                    labelTestoIni.setBounds(5, 5, panel.getWidth() -5, labelTestoIni.getHeight());
                    conferma = new JButton("conferma");
                    conferma.setBounds(
                        panel.getWidth()/2 - ((conferma.getPreferredSize().width + 10)/2), 
                        panel.getHeight() - conferma.getPreferredSize().height, 
                        conferma.getPreferredSize().width + 10, 
                        conferma.getPreferredSize().height);
                    conferma.setActionCommand("conferma");
                    
                    conferma.addActionListener(this);
                    panel.add(conferma);  // Aggiungi il bottone al frame

                    // Configura il pannello per visualizzare l'albero
                    treePanel = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.setColor(Color.ORANGE);
                            int p = calcolaProfondita(nf);
                            int yOffset = (getHeight() - 25 - conferma.getPreferredSize().height - 30 * p) / (p - 1);
                            drawTree(nf, p, 0, getWidth(), 25, yOffset, g, this);
                        }
                    };
                    treePanel.setBackground(Color.WHITE);
                    panel.setLayout(new BorderLayout());
                    panel.add(treePanel, BorderLayout.CENTER);

                    // Handle window resize events
                    software.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentResized(ComponentEvent e) {
                            aggiornaTree();
                        }
                    });

                    // Add the panel to the main frame
                    software.add(panel, BorderLayout.CENTER);
                    software.revalidate();
                    software.repaint();
                } else {
                    JOptionPane.showMessageDialog(infoRequest, "Nome già occupato, riprova.", "Errore", JOptionPane.ERROR_MESSAGE);
                    labelTestoIni.setForeground(Color.RED);
                }
            } else {
                labelTestoIni.setForeground(Color.RED);
            }
        } else if (e.getActionCommand().startsWith("crea figlio:")) { // Azione per creare un nuovo nodo figlio
            String[] parts = e.getActionCommand().substring("crea figlio:".length()).split(":");
            if (parts.length != 2) throw new IllegalArgumentException("Formato errato: usare 'crea figlio:tipo:id'.");

            String tipoFiglio = parts[0].trim();
            if (!tipoFiglio.equals("foglie") && !tipoFiglio.equals("non foglie") && !tipoFiglio.equals("tipo foglia"))
                throw new IllegalArgumentException("Tipo figlio errato: deve essere 'foglia', 'non foglia', o 'tipo foglia'.");

            int id;
            try {
                id = Integer.parseInt(parts[1].trim());
                if (id < 0 && !tipoFiglio.equals("tipo foglia"))
                    throw new IllegalArgumentException("ID errato: deve essere un numero maggiore di 0.");
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("ID errato: deve essere un numero valido.");
            }

            Object[] possibilities = model.cercaNodo(nf, id).getDominiDisp().toArray();
            String s = "";
            while ((s == null) || (s.length() < 1)) {
                s = (String) JOptionPane.showInputDialog(
                    infoRequest1, "Scegli il dominio della sotto-categoria:\n", "Scelta posizione",
                    JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[0]);
                if (s != null && s.length() > 0)
                    s = s.substring(0, s.indexOf(":"));
            }

            // Crea il nodo in base al tipo specificato
            if (tipoFiglio.equals("non foglie") && id >= 0) creaNonfoglia("", false, s, id);
            else if (tipoFiglio.equals("foglie")) creaFoglia(s, id);
        } else if ("conferma".equals(e.getActionCommand())) { // Azione per confermare la gerarchia
            if (model.controllaFigliDefiniti(nf)) {
                model.salvaGerarchie();
                software.dispose();
            } else {
                JOptionPane.showMessageDialog(infoRequest, "Completa l'inserimento prima di continuare.");
            }
        }
    }

        /**
     * Metodo per aggiornare l'albero di visualizzazione quando viene modificato o ridimensionato.
     * Precondizione: Nessuna.
     * Postcondizione: Il pannello `treePanel` viene ridisegnato per riflettere le modifiche o l'aggiornamento della gerarchia.
     */
    private void aggiornaTree(){
        
        if (treePanel != null) {
            panel.revalidate();
            panel.repaint();
            labelTestoIni.setBounds(5, 5, panel.getWidth() -5, labelTestoIni.getHeight());
            conferma.setBounds(
                panel.getWidth()/2 - ((conferma.getPreferredSize().width + 10)/2), 
                panel.getHeight() - conferma.getPreferredSize().height, 
                conferma.getPreferredSize().width + 10, 
                conferma.getPreferredSize().height);
            treePanel.removeAll();
            treePanel.revalidate();
            treePanel.repaint(); // Ensure treePanel is redrawn when the window is resized
        }
    }

    /**
     * Crea una foglia all'interno della gerarchia.
     * Precondizione: `dom` e `idPadre` devono essere validi e `model` deve essere inizializzato.
     * Postcondizione: Una nuova foglia è aggiunta alla gerarchia con i dettagli specificati dall'utente.
     * 
     * @param dom Dominio o categoria a cui appartiene la foglia.
     * @param idPadre ID del nodo padre nella gerarchia.
     */
    private void creaFoglia(String dom, int idPadre) {
        // Input dell'utente per il nome della foglia
        String name = "";
        while (name.equals("")) {
            name = JOptionPane.showInputDialog(
                infoRequest,
                "Inserisci il nome della categoria:",
                "Inserimento nome",
                JOptionPane.PLAIN_MESSAGE
            );
        }

        // Variabili per gestire il fattore di conversione e i parametri di selezione
        float fattore = 0;
        int idfattore = -1;
        int idsel = 0;
        boolean isOk = false;

        // Ottiene le opzioni disponibili per le foglie
        ArrayList<String> possibilities = model.getAllLeaves();

        if (!possibilities.isEmpty()) {
            // Crea un pannello con pulsanti radio per la selezione
            JPanel panelInfo = new JPanel();
            panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));

            ButtonGroup group = new ButtonGroup();
            JRadioButton[] radioButtons = new JRadioButton[possibilities.size()];

            // Crea e aggiunge ogni pulsante radio al gruppo e al pannello
            for (int i = 0; i < possibilities.size(); i++) {
                String[] parts = possibilities.get(i).split(":");
                radioButtons[i] = new JRadioButton(parts[0] + ", " + parts[1]);
                group.add(radioButtons[i]);
                panelInfo.add(radioButtons[i]);
            }

            // Pre-seleziona il primo pulsante radio
            radioButtons[0].setSelected(true);

            // Scroll pane per gestire un gran numero di opzioni
            JScrollPane scrollPane = new JScrollPane(panelInfo);
            scrollPane.setPreferredSize(new Dimension(300, 200)); // Imposta una dimensione fissa per evitare overflow

            // Dialogo per confermare la scelta
            int result = JOptionPane.showConfirmDialog(null, scrollPane,
                "Scegli la foglia con cui inserire il fattore di conversione",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // Determina quale pulsante radio è selezionato
            if (result == JOptionPane.OK_OPTION) {
                for (int i = 0; i < radioButtons.length; i++) {
                    if (radioButtons[i].isSelected()) {
                        System.out.println("Selezionato: " + possibilities.get(i));
                        idsel = i;
                        break;
                    }
                }
            }

            // Loop per gestire l'inserimento del fattore di conversione
            while (!isOk) {
                if (result == JOptionPane.OK_OPTION) {
                    for (JRadioButton rb : radioButtons) {
                        if (rb.isSelected()) {
                            String input = possibilities.get(idsel);
                            System.err.println(input);
                            String[] parts = input.split(":");

                            // Controllo di validità per il formato
                            if (parts.length == 3) {
                                idfattore = Integer.parseInt(parts[2]);
                                System.err.println(idfattore);

                                // Input dell'utente per il fattore di conversione
                                input = JOptionPane.showInputDialog(
                                    infoRequest,
                                    "Inserisci il fattore con la foglia " + parts[1] + " (0.5 <= fattore <= 2):",
                                    "Fattore di conversione",
                                    JOptionPane.PLAIN_MESSAGE
                                );

                                if (input == null) {
                                    // L'utente ha chiuso il dialogo
                                    System.exit(0);
                                }

                                try {
                                    fattore = Float.parseFloat(input);
                                    if (fattore >= 0.5 && fattore <= 2) {
                                        isOk = true; // Input valido
                                    } else {
                                        JOptionPane.showMessageDialog(infoRequest, "Per favore, inserisci un numero compreso tra 0.5 e 2.", "Errore", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (NumberFormatException e2) {
                                    JOptionPane.showMessageDialog(infoRequest1, "Input non valido. Inserisci un numero.", "Errore", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Crea la foglia con i parametri specificati
        model.creaFoglia(name, fattore, idfattore, idPadre, dom);
        aggiornaTree(); // Aggiorna l'albero di visualizzazione
    }


    /**
     * Metodo per creare un oggetto Nonfoglia.
     * 
     * Precondizioni:
     * - Il parametro name non deve essere nullo.
     * - Il parametro isRadice deve essere un boolean valido.
     * - Il parametro dom non deve essere nullo.
     * - Il parametro IdPadre deve essere un intero positivo o 0 se non ci sono padri.
     * 
     * Postcondizioni:
     * - Viene creato e restituito un oggetto Nonfoglia con i dati forniti dall'utente.
     * 
     * Invarianti:
     * - Il numero di figli deve essere maggiore di zero.
     * - I valori di dominio devono essere unici.
     */
    private Nonfoglia creaNonfoglia(String name, boolean isRadice, String dom, int IdPadre) {
        assert name != null : "name deve essere un valore valido";
        assert dom != null : "dom deve essere un valore valido";
        assert IdPadre >= 0 : "id deve essere un valore valido";

        // Precondizione: verifico che il nome non sia vuoto
        while (name.equals("")) {
            // Richiede all'utente di inserire un nome valido
            name = JOptionPane.showInputDialog(
                infoRequest,
                "Inserisci il nome della categoria:",
                "inserimento nome",
                JOptionPane.PLAIN_MESSAGE);
        }

        // Selezione della tipologia dei figli: sotto-categorie o foglie
        Object[] options = {"Sotto-categorie", "Foglie"};
        int n = JOptionPane.showOptionDialog(infoRequest,
            "I figli della categoria in costruzione, sono sotto-categorie o foglie?",
            "Tipologia dei figli",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     // non utilizzo una icona personalizzata
            options,  // titoli dei bottoni
            options[0] // titolo del bottone di default
        );

        int number = 0;            // Numero di figli della foglia
        boolean validInput = false; // Invariante: controlla la validità dell'input del numero di figli
        boolean validInput2;        // Variabile per validare gli altri input
        String input = "";          // Variabile temporanea per l'input dell'utente
        String campo;               // Campo da assegnare alla Nonfoglia

        // Ciclo per ottenere un numero valido di figli
        while (!validInput) {
            input = JOptionPane.showInputDialog(
                infoRequest,
                "Inserisci il numero di figli della foglia (>0):",
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
                    JOptionPane.showMessageDialog(infoRequest, 
                        "Per favore, inserisci un numero maggiore di zero.", 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException es) {
                JOptionPane.showMessageDialog(infoRequest1, 
                    "Input non valido. Inserisci un numero.", 
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        // Array per memorizzare i valori di dominio e le descrizioni
        String dominio[] = new String[number];
        String desc[] = new String[number];

        // Richiede all'utente di inserire il nome del campo
        campo = JOptionPane.showInputDialog(
            infoRequest,
            "Inserisci il nome del campo:",
            "inserimento campo",
            JOptionPane.PLAIN_MESSAGE);

        // Ciclo per popolare i valori di dominio e le descrizioni, garantendo l'unicità dei valori di dominio
        for (int i = 0; i < number; i++) {
            validInput2 = false;

            // Ciclo per inserire valori unici di dominio
            while (!validInput2) {
                input = JOptionPane.showInputDialog(
                    infoRequest,
                    "Inserisci il valore del dominio:",
                    "inserimento dominio",
                    JOptionPane.PLAIN_MESSAGE);

                validInput2 = true;

                // Controlla che il valore del dominio sia unico
                for (int j = 0; j < i; j++) {
                    if (dominio[j] == null ? input == null : dominio[j].equals(input))
                        validInput2 = false;
                }
            }
            dominio[i] = input; // Assegna il valore di dominio

            // Ciclo per ottenere una descrizione valida e non vuota
            validInput2 = false;
            while (!validInput2) {
                input = JOptionPane.showInputDialog(
                    infoRequest,
                    "Inserisci una descrizione per il dominio " + dominio[i] + ":",
                    "inserimento descrizione",
                    JOptionPane.PLAIN_MESSAGE);

                // Invariante: la descrizione non deve essere vuota
                if (!"".equals(input)) validInput2 = true;
            }

            desc[i] = input; // Assegna la descrizione al dominio
        }

        // Determina il tipo di figli (non foglie o foglie) in base alla scelta dell'utente
        String tipoFigli = n == 0 ? "non foglie" : "foglie";

        // Crea l'oggetto Nonfoglia utilizzando il metodo del modello
        Nonfoglia nnf = model.creaNonFoglia(name, campo, dominio, desc, isRadice, IdPadre, dom, tipoFigli);

        // Serializza e stampa l'oggetto Nonfoglia per il debug
        System.err.println(nnf.serialize());

        // Aggiorna la struttura ad albero
        aggiornaTree();

        // Postcondizione: restituisce l'oggetto Nonfoglia creato
        return nnf;
    }

   /**
     * Metodo per calcolare la profondità massima di un nodo Nonfoglia.
     * 
     * Precondizioni:
     * - Il parametro nodo non deve essere nullo.
     * - Il parametro nodo deve essere di tipo Nonfoglia.
     * 
     * Postcondizioni:
     * - Restituisce la profondità massima dell'albero a partire dal nodo dato.
     * 
     * Invarianti:
     * - Ogni nodo non foglia deve avere una lista di figli.
     * - La profondità deve essere almeno 1 per ogni nodo non foglia.
     * 
     * @param nodo Il nodo di tipo Nonfoglia da cui calcolare la profondità.
     * @return La profondità massima dell'albero.
     */
    public static int calcolaProfondita(Nonfoglia nodo) {
        assert nodo != null : "nodo deve esistere";

        // Precondizione: verifica che il nodo non sia nullo
        if (nodo == null) {
            throw new IllegalArgumentException("Il nodo non può essere nullo");
        }

        // Controlla se il nodo è di tipo "foglia"
        if ("foglie".equals(nodo.getTipoFigli())) {
            return 2;  // Se i figli sono foglie, la profondità è 2
        }

        // Inizializza la profondità massima come 1 (livello attuale)
        int profonditaMassima = 1;

        // Itera su tutti i figli del nodo
        for (Categoria figlio : nodo.getFigli()) {
            if (figlio != null) {
                // Invariante: la profondità di ogni figlio deve essere calcolata ricorsivamente
                profonditaMassima = Math.max(profonditaMassima, calcolaProfondita((Nonfoglia) figlio));
            }
        }

        // Postcondizione: restituisce la profondità massima trovata + 1 per il livello attuale
        return profonditaMassima + 1;
    }
    
    /**
     * Metodo per disegnare ricorsivamente un albero di nodi di tipo Categoria su un JPanel.
     * 
     * Precondizioni:
     * - Il parametro node non deve essere nullo.
     * - Il JPanel panel e l'oggetto Graphics g devono essere validi e inizializzati.
     * 
     * Postcondizioni:
     * - Viene disegnato un bottone per ogni nodo dell'albero.
     * - I nodi vengono collegati graficamente ai loro figli.
     * 
     * Invarianti:
     * - Ogni nodo ha una posizione valida.
     * - I nodi di tipo Nonfoglia possono avere figli, mentre i nodi foglia no.
     * 
     * @param node Il nodo corrente da disegnare.
     * @param p Un parametro extra (non specificato nel codice attuale).
     * @param xspaceS Lo spazio iniziale orizzontale disponibile per il nodo.
     * @param xspaceF Lo spazio finale orizzontale disponibile per il nodo.
     * @param y La posizione verticale attuale.
     * @param yOffset L'offset verticale tra i nodi.
     * @param g L'oggetto Graphics utilizzato per disegnare.
     * @param panel Il pannello su cui vengono aggiunti i bottoni per i nodi.
     * @return L'offset verticale aggiornato dopo aver disegnato i figli.
     */
    private int drawTree(Categoria node, int p, int xspaceS, int xspaceF, int y, int yOffset, Graphics g, JPanel panel) {
        // Precondizione: il nodo non può essere nullo
        if (node == null) {
            throw new IllegalArgumentException("Il nodo non può essere nullo");
        }

        // Dimensioni fisse per i bottoni
        // Crea il bottone per il nodo attuale
        JButton button = new JButton(node.getNome()); // Crea un JButton invece di JLabel
        button.setHorizontalAlignment(SwingConstants.CENTER); // Centra il testo

        // Prepara il messaggio per il comando del bottone
        String message = "crea figlio:";
        if (node instanceof Nonfoglia nonfoglia) {
            message += nonfoglia.getTipoFigli() + ":" + nonfoglia.getId();
        } else {
            message += "tipo foglia:-1";
        }
        button.setActionCommand(message);
        button.addActionListener(this);

        // Misura la larghezza del testo nel bottone
        FontMetrics metrics = button.getFontMetrics(button.getFont());
        int textWidth = metrics.stringWidth(button.getText());

        // Definisce la larghezza e altezza del bottone, limitando la larghezza massima
        int buttonWidth = Math.min((xspaceF - xspaceS), textWidth + 40); 
        int buttonHeight = 30;

        // Calcola la posizione orizzontale (centrale) per il bottone
        int xpos = xspaceS + (xspaceF - xspaceS) / 2 - (buttonWidth / 2);
        button.setBounds(xpos, y, buttonWidth, buttonHeight); // Posiziona il bottone centrato

        // Imposta il colore del bottone in base al tipo di nodo
        if (node instanceof Nonfoglia) {
            button.setBackground(Color.ORANGE); // Nodo non foglia
        } else {
            button.setEnabled(false); // Nodo foglia, disabilitato
            button.setBackground(Color.GREEN);  // Colore verde per le foglie
        }

        panel.add(button); // Aggiunge il bottone al pannello

        // Se il nodo è di tipo Nonfoglia, disegna ricorsivamente i figli
        if (node instanceof Nonfoglia nonfoglia) {
            Categoria[] figli = nonfoglia.getFigli(); // Ottiene i figli del nodo
            int numChildren = 0;

            // Conta il numero di figli non nulli
            for (Categoria figlio : figli) {
                if (figlio != null) {
                    numChildren++;
                }
            }

            // Sposta la posizione verticale verso il basso per i figli
            y = y + yOffset;

            if (numChildren > 0) {
                // Calcola lo spazio orizzontale disponibile per ogni figlio
                int larghezzaXfiglio = (xspaceF - xspaceS) / numChildren;

                // Disegna ogni figlio ricorsivamente
                for (int i = 0; i < numChildren; i++) {
                    Categoria child = figli[i]; // Ottiene il figlio corrente

                    int startX = xspaceS + larghezzaXfiglio * i;
                    int finishX = xspaceS + larghezzaXfiglio * i + larghezzaXfiglio;

                    // Disegna una linea tra il nodo corrente e il figlio
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(3)); // Imposta lo spessore della linea
                    g2.drawLine(
                        xpos + buttonWidth / 2, y - yOffset + buttonHeight, 
                        startX + (finishX - startX) / 2, y); // Linea centrata

                    // Disegna ricorsivamente il figlio e aggiorna l'offset verticale
                    yOffset = drawTree(child, p, startX, finishX, y, yOffset, g, panel); 
                }
            }

            // Disabilita il bottone se tutti i figli sono stati processati
            if (numChildren == figli.length) {
                button.setEnabled(false);
            }
        }

        // Postcondizione: restituisce l'offset verticale aggiornato
        return yOffset;
    }
}
