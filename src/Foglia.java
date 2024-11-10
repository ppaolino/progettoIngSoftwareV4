import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Foglia rappresenta una foglia con attributi univoci e capacità di
 * serializzarsi su file.
 */
public class Foglia implements Categoria {
    // Contatore statico per generare un ID univoco per ogni istanza di Foglia
    private static int idCounter = 0;

    private final int id;                     // Identificatore univoco della foglia
    private final String nome;                // Nome della foglia
    private static final String FILE_NAME = "foglie.txt"; // Nome del file per salvare i dati delle foglie

    private final Fattorediconversione f = Fattorediconversione.getInstance();
    private final float fattore;                    // Valore del fattore di conversione associato
    private final int idfattore;                    // ID del fattore di conversione

    /**
     * Costruttore principale per Foglia.
     * 
     * @param nome      Nome della foglia.
     * @param fattore   Valore del fattore di conversione associato.
     * @param idfattore ID del fattore di conversione.
     * 
     * @pre nome != null && !nome.isEmpty() && fattore >= 0
     * @post La nuova istanza di Foglia è creata con un ID univoco.
     */
    public Foglia(String nome, float fattore, int idfattore) {
        this.id = idCounter++;               // Assegna un ID univoco e incrementa il contatore
        this.nome = nome;                    // Assegna il nome
        this.fattore = fattore;
        this.idfattore = idfattore;
        f.calcolaFattori(this.id, fattore, idfattore);
    }

    /**
     * Costruttore alternativo per creare una Foglia con un ID specifico.
     * 
     * @param nome Nome della foglia.
     * @param id   ID specifico per la foglia.
     * 
     * @pre nome != null && !nome.isEmpty() && id >= 0
     * @post La nuova istanza di Foglia è creata con l'ID specificato.
     */
    public Foglia(String nome, int id, float fattore, int idfattore) {
        this.fattore = fattore;
        this.idfattore = idfattore;
        this.id = id;                       // Assegna l'ID specificato
        this.nome = nome;                   // Assegna il nome
        idCounter = id + 1;                 // Imposta il contatore per il prossimo ID
    }

    /**
     * Ottiene l'ID della foglia.
     * 
     * @return L'ID della foglia.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Ottiene il nome della foglia.
     * 
     * @return Il nome della foglia.
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * Rappresentazione in stringa dell'istanza di Foglia.
     * 
     * @return Una stringa contenente ID, nome, fattore e ID del fattore.
     */
    @Override
    public String toString() {
        return "ID: " + id + "; Nome: " + nome + "; Fattore: " + fattore + "; ID fattore: " + idfattore;
    }

    /**
     * Salva o aggiorna l'istanza della Foglia nel file specificato.
     * 
     * @pre Il file foglie.txt deve essere accessibile in scrittura.
     * @post La rappresentazione della Foglia è aggiunta o aggiornata nel file.
     */
    public void save() {
        File file = new File(FILE_NAME);

        try {
            // Se il file non esiste, lo crea
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Errore nella creazione del file: " + e.getMessage());
            return; // Esce dal metodo se la creazione del file fallisce
        }

        List<String> fileContent = new ArrayList<>(); // Contenuto del file da aggiornare

        // Lettura del contenuto esistente nel file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean updated = false;

            // Legge ogni riga del file per verificare se esiste già l'ID della Foglia
            while ((line = reader.readLine()) != null) {
                // Se la riga corrisponde all'ID della Foglia corrente, aggiorna la riga
                if (line.startsWith("ID: " + id + ";")) {
                    fileContent.add(toString()); // Sostituisce con i dati aggiornati
                    updated = true;
                } else {
                    fileContent.add(line); // Mantiene le righe non corrispondenti
                }
            }

            // Se l'ID della Foglia corrente non è stato trovato, aggiunge una nuova riga
            if (!updated) {
                fileContent.add(toString());
            }

        } catch (IOException e) {
            System.err.println("Errore nella lettura del file: " + e.getMessage());
            return; // Esce se c'è un problema durante la lettura del file
        }

        // Scrittura del contenuto aggiornato nel file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : fileContent) {
                writer.write(line);       // Scrive ogni riga nel file
                writer.newLine();         // Aggiunge una nuova linea dopo ogni riga
            }
        } catch (IOException e) {
            System.err.println("Errore nella scrittura del file: " + e.getMessage());
        }
    }
}
