import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Nonfoglia implements Categoria {
    // Contatore statico per assegnare ID univoci a ciascuna istanza di Nonfoglia
    private static int idCounter = 0;  
    private final int id;                // Identificatore univoco della Nonfoglia
    private final boolean isRadice;      // Booleano che indica se la Nonfoglia è una radice
    private final String nome;           // Nome della Nonfoglia
    private final String campo;           // Tipo di Nonfoglia
    private final String[] dominio;      // Array che rappresenta il dominio
    private final String[] descrizione;  // Array che rappresenta le descrizioni associate al dominio
    private final Categoria[] figli;     // Array di figli (possono essere Foglia o Nonfoglia)
    private String tipoFigli;      // Indica se i figli sono "foglie" o "non foglie"
    private static final String FILE_NAME = "nonfoglie.txt"; // Nome del file per memorizzare le istanze di Nonfoglia

    /**
     * Costruttore che crea una Nonfoglia con un ID generato automaticamente.
     * 
     * Precondizioni: il dominio e la descrizione devono avere la stessa lunghezza.
     * Postcondizioni: viene creata un'istanza Nonfoglia con ID unico e figli inizializzati come null.
     */
    public Nonfoglia(String nome, String campo, String[] dominio, String[] descrizione, boolean isRadice) {
        

        this.id = idCounter++;  // Assegna un ID univoco e lo incrementa
        this.nome = nome;
        this.campo = campo;
        for (String elem : dominio) 
            elem = elem.replace(":", "");
        this.dominio = dominio;
        for (String elem : descrizione) 
            elem = elem.replace(":", "");
        
        this.descrizione = descrizione;
        this.isRadice = isRadice;
        this.figli = new Categoria[dominio.length]; // Inizializza l'array dei figli con la dimensione del dominio
    }

    /**
     * Costruttore alternativo che permette di assegnare un ID specifico alla Nonfoglia.
     * 
     * Precondizioni: il dominio e la descrizione devono avere la stessa lunghezza.
     * Postcondizioni: viene creata una Nonfoglia con ID specifico e tipoFigli impostato.
     */
    public Nonfoglia(int id, String nome, String campo, String[] dominio, String[] descrizione, boolean isRadice, String tipoFigli) {
        this.id = id;
        idCounter = id + 1; // Imposta il contatore per il prossimo ID
        this.nome = nome;
        this.campo = campo;
        for (String elem : dominio) 
            elem = elem.replace(":", "");
        this.dominio = dominio;
        for (String elem : descrizione) 
            elem = elem.replace(":", "");
        this.descrizione = descrizione;
        this.isRadice = isRadice;
        this.figli = new Categoria[dominio.length];
        this.tipoFigli = tipoFigli; // Imposta il tipo di figli (foglie o non foglie)
    }

    /**
     * Aggiunge un figlio Nonfoglia associato a un valore di dominio.
     * 
     * Precondizioni: dom deve essere un valore valido del dominio e il figlio non deve eccedere la capacità.
     * Postcondizioni: il figlio viene aggiunto in posizione associata a dom nel dominio.
     * 
     * @param figlio la Nonfoglia da aggiungere
     * @param dom il valore di dominio associato
     * @return l'ID del figlio se aggiunto correttamente, -1 altrimenti
     */
    public int aggiungiFiglio(Nonfoglia figlio, String dom) {
        int nFigli = contaElementi(figli); // Conta quanti figli sono già stati aggiunti
        if (nFigli == 0 && tipoFigli == null) {
            tipoFigli = "non foglie";  // Imposta il tipoFigli se è nullo
        }
        if (nFigli == dominio.length) {
            return -1;
        }
        if (tipoFigli.equals("foglie")) {
            return -1;    
        }

        int indice = getIndexDominio(dom); // Ottiene l'indice del dominio
        if (indice < 0) {
            return -1;
        }

        figli[indice] = figlio; // Aggiunge il figlio nel posto corretto
        return figlio.getId();
    }

     /**
     * Aggiunge un figlio Foglia associato a un valore di dominio.
     * 
     * Precondizioni: dom deve essere un valore valido del dominio e il figlio non deve eccedere la capacità.
     * Postcondizioni: il figlio viene aggiunto in posizione associata a dom nel dominio.
     * 
     * @param figlio la Foglia da aggiungere
     * @param dom il valore di dominio associato
     * @return l'ID del figlio se aggiunto correttamente, -1 altrimenti
     */
    public int aggiungiFiglio(Foglia figlio, String dom) {
        int nFigli = contaElementi(figli); // Conta quanti figli sono già stati aggiunti
        if (nFigli == 0 && tipoFigli == null) {
            tipoFigli = "foglie"; // Imposta il tipoFigli se è nullo
        }
        if (nFigli == dominio.length) {
            return -1;
        }
        if (tipoFigli.equals("non foglie")) {
            return -1;
        }

        int indice = getIndexDominio(dom); // Ottiene l'indice del dominio
        if (indice < 0) {
            return -1;
        }

        figli[indice] = figlio; // Aggiunge il figlio nel posto corretto
        return figlio.getId();
    }

    /**
     * Restituisce il numero di elementi non null in un array.
     * 
     * @param array l'array da analizzare
     * @return il numero di elementi non null presenti nell'array
     */
    private static int contaElementi(Object[] array) {
        int count = 0;
        for (Object elemento : array) {
            if (elemento != null) {
                count++; // Incrementa il conteggio se l'elemento non è null
            }
        }
        return count;
    }

    // Metodo per ottenere l'array di figli
    public Categoria[] getFigli() {
        return figli;
    }

    // Metodo per ottenere l'ID della Nonfoglia
    @Override
    public int getId() {
        return id;
    }

    // Metodo per ottenere il nome della Nonfoglia
    @Override
    public String getNome() {
        return nome;
    }

    public String getTipoFigli() {
        return tipoFigli;
    }

    public void setTipoFigli(String s) {
        if(s.equals("foglie")) tipoFigli = s;
        else if(s.equals("non foglie")) tipoFigli = s;
    }

    // Metodo per verificare se la Nonfoglia è una radice
    public boolean infoRadice() {
        return isRadice;
    }

    // Metodo per ottenere il tipo di Nonfoglia
    public String getCampo() {
        return campo;
    }

    // Metodo per ottenere il dominio della Nonfoglia
    public String[] getDominio() {
        return dominio;
    }

    /**
     * Metodo per ottenere l'indice di un valore nel dominio.
     * 
     * @param valDom il valore di dominio da cercare
     * @return l'indice se trovato, -1 altrimenti
     */
    public int getIndexDominio(String valDom) {
        for (int idx = 0; idx < dominio.length; idx++) {
            if (dominio[idx].equals(valDom)) {
                return idx;
            }
        }
        return -1; // Ritorna -1 se il valore non è trovato
    }

    // Metodo per ottenere le descrizioni associate alla Nonfoglia
    public String[] getDescrizione() {
        return descrizione;
    }

    public ArrayList<String> getDominiDisp(){
        ArrayList<String> lista = new ArrayList<>();
        for (int i = 0; i < figli.length; i++) {
            if(figli[i] == null) lista.add(dominio[i] + ":" + descrizione[i]);
        } 
        return lista;
    }

    /**
     * Serializza l'istanza di Nonfoglia in una stringa.
     * 
     * @return la stringa serializzata
     */
    public String serialize() {
        StringBuilder str = new StringBuilder();
        String desc = "ID: " + id + "; Nome: " + nome + "; isRadice: " + isRadice + "; tipo dei figli: " + tipoFigli + "; Tipo: " + campo + "; dominio-descrizione-id figlio associato: ";
        str.append(desc);
        for (int idx = 0; idx < figli.length; idx++) {
            if (figli[idx] instanceof Nonfoglia f) {
                str.append("(").append(dominio[idx]).append(" - ").append(descrizione[idx]).append(" - ").append(f.getId()).append(")");
            }
            if (figli[idx] instanceof Foglia f) {
                str.append("(").append(dominio[idx]).append(" - ").append(descrizione[idx]).append(" - ").append(f.getId()).append(")");
            }
        }
        return str.toString(); // Ritorna la stringa serializzata
    }

    /**
     * Salva o aggiorna l'istanza di Nonfoglia nel file.
     * 
     * Precondizioni: il file deve essere accessibile in lettura e scrittura.
     * Postcondizioni: l'istanza viene scritta o aggiornata nel file.
     */
     public void save() {
        // Controlla se il file esiste, se non esiste lo crea
        File file = new File(FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile(); // Crea il file se non esiste
            }
        } catch (IOException e) {
            System.err.println("errore nella creazione del file");
            return; // Esce dal metodo se la creazione del file fallisce
        }

        List<String> fileContent = new ArrayList<>(); // Lista per memorizzare il contenuto del file

        // Legge il contenuto dell'intero file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean updated = false;

            // Legge ogni riga
            while ((line = reader.readLine()) != null) {
                // Se la riga corrisponde a questa istanza di Nonfoglia, la sostituisce
                if (line.startsWith("ID: " + id + ";")) {
                    fileContent.add(serialize()); // Sostituisce con i dati aggiornati
                    updated = true;
                } else {
                    fileContent.add(line); // Mantiene le altre righe invariate
                }
            }

            // Se l'ID non è stato trovato, aggiunge la nuova voce alla fine
            if (!updated) {
                fileContent.add(serialize());
            }

        } catch (IOException e) {
            System.err.println("errore nella lettura da file");
        }

        // Scrive il contenuto aggiornato nel file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : fileContent) {
                writer.write(line);  // Scrive ogni riga nel file
                writer.newLine();    // Aggiunge una nuova linea dopo ogni riga
            }
        } catch (IOException e) {
            System.err.println("errore nella scrittura da file");
        }

        // Salva anche i figli
        for (Categoria figli1 : figli) {
            if (figli1 instanceof Nonfoglia f) {
                f.save();
            }
            if (figli1 instanceof Foglia f) {
                f.save();
            }
        }
    }
}
