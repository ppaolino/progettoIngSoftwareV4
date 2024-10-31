import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un comprensorio geografico con un nome e una lista di comuni associati.
 * Consente di aggiungere comuni, visualizzare la lista e salvare i dati su file.
 * 
 * @invariant nome != null && !nome.isEmpty(); // Assicura che il nome del comprensorio non sia nullo o vuoto
 */
public class Comprensorio {
    private final String nome; // Nome del comprensorio
    private final ArrayList<String> comuni; // Lista di comuni associati al comprensorio
    private static final ComuniInfo comuniInfo = ComuniInfo.getInstance(); // Singleton per gestione dei comuni
    private static final String FILE_NAME = "comprensorio.txt"; // Nome del file per salvare i dati del comprensorio

    /**
     * Costruttore per creare un nuovo comprensorio con il nome specificato.
     *
     * @param nome il nome del comprensorio
     * @pre nome != null && !nome.isEmpty(); // Assicurati che il nome non sia nullo o vuoto
     * @post this.nome.equals(nome); // Assicurati che il nome sia stato impostato correttamente
     */
    public Comprensorio(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Il nome del comprensorio non può essere nullo o vuoto.");
        }

        this.nome = nome;
        this.comuni = new ArrayList<>(); // Inizializza la lista dei comuni associati
    }

    public Comprensorio(String nome, ArrayList<String> limitrofi) {			//NUOVO COSTRUTTORE---------------------------------------------
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Il nome del comprensorio non può essere nullo o vuoto.");
        }
        
        this.nome = nome;  // Assegna il nome del comprensorio
        this.comuni = limitrofi; // Inizializza la lista dei comuni
    } 

    /**
     * Aggiunge un comune alla lista del comprensorio, se esiste e rispetta i vincoli di vicinanza.
     *
     * @param comune il nome del comune da aggiungere
     * @return true se il comune è stato aggiunto, false altrimenti
     * @pre comune != null && !comune.isEmpty(); // Assicurati che il comune non sia nullo o vuoto
     * @post comuni.contains(comune) == true; // Il comune deve essere presente nella lista se aggiunto con successo
     */
    public boolean aggiungiComune(String comune) {
        if (comune == null || comune.isEmpty()) {
            throw new IllegalArgumentException("Il comune non può essere nullo o vuoto.");
        }
        
        if (!comuniInfo.esisteComune(comune)) { // Verifica se il comune esiste
            return false;
        }
        
        if (comuni.isEmpty()) {
            comuni.add(comune); // Aggiunge il primo comune senza controlli di vicinanza
        } else {
            for (String com : comuni) {
                if (comuniInfo.sonoLimitrofi(com, comune) && !comuni.contains(comune)) {
                    comuni.add(comune); // Aggiunge il comune se è limitrofo e non già presente
                    return true; // Il comune è stato aggiunto
                }
            }
        }
        return comuni.contains(comune); // Conferma se il comune è stato aggiunto
    }

    /**
     * Restituisce la lista dei comuni associati al comprensorio.
     *
     * @return la lista di comuni
     * @post result != null; // La lista di comuni non deve essere nulla
     */
    public ArrayList<String> getComuni() {
        return comuni;
    }

    /**
     * Restituisce il nome del comprensorio.
     *
     * @return il nome del comprensorio
     * @post result != null && !result.isEmpty(); // Il nome del comprensorio deve essere valido
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce una rappresentazione testuale del comprensorio.
     *
     * @return una stringa con il nome e la lista dei comuni
     * @post result != null; // La rappresentazione testuale non deve essere nulla
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(nome).append("; lista: ");
        
        for (String c : comuni) {
            sb.append(c).append(", "); // Aggiunge ogni comune con una virgola
        }
        
        return sb.toString().replaceAll(", $", ""); // Rimuove l'ultima virgola
    }

    /**
     * Salva o aggiorna le informazioni del comprensorio nel file specificato.
     * 
     * @post Se il comprensorio è stato aggiunto o aggiornato, il file conterrà le informazioni corrette.
     */
    public void save() {
        File file = new File(FILE_NAME);

        try {
            if (!file.exists() && !file.createNewFile()) { // Crea il file se non esiste
                System.err.println("Errore nella creazione del file");
                return;
            }
        } catch (IOException e) {
            System.err.println("Errore nella creazione del file");
            return;
        }

        List<String> fileContent = new ArrayList<>(); // Contenuto del file aggiornato

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean updated = false;

            // Lettura delle righe del file e aggiornamento se esiste il comprensorio
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Nome: " + this.nome + ";")) {
                    fileContent.add(toString());
                    updated = true;
                } else {
                    fileContent.add(line);
                }
            }

            // Se il comprensorio non era presente, lo aggiunge alla fine
            if (!updated) {
                fileContent.add(toString());
            }

        } catch (IOException e) {
            System.err.println("Errore nella lettura del file");
        }

        // Scrittura del contenuto aggiornato nel file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Errore nella scrittura nel file");
        }
    }
}
