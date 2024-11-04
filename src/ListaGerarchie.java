import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe singleton per la gestione di gerarchie di elementi di tipo Nonfoglia e Foglia.
 * Permette il caricamento da file e la gestione della lista di gerarchie radici.
 * 
 * Invariante: `instance` rappresenta sempre un'unica istanza della classe. 
 * `radici` contiene solo oggetti `Nonfoglia` e `foglie` solo oggetti `Foglia` caricati dai rispettivi file.
 */
public final class ListaGerarchie {
    
    private static final String FOGLIE_FILE_NAME = "foglie.txt"; // Nome del file contenente le foglie
    private static final String FILE_NAME = "nonfoglie.txt"; // Nome del file contenente le non-foglie
    private static final ListaGerarchie instance = new ListaGerarchie(); // Istanza singleton della classe
    private static ArrayList<Nonfoglia> radici; // Lista delle radici (non-foglie)

    /**
     * Costruttore privato per inizializzare la lista delle radici e caricare i dati dai file.
     * Precondizione: i file di dati devono esistere, altrimenti viene segnalato errore in console.
     * Postcondizione: i dati di radici e foglie vengono caricati se i file esistono.
     */
    private ListaGerarchie() {
        ListaGerarchie.radici = new ArrayList<>(); 
        caricaDaFile(); // Carica le informazioni dai file
    }

    /**
     * Metodo per caricare una foglia in base all'ID specificato.
     * Precondizione: il file delle foglie deve esistere e contenere dati formattati correttamente.
     * Postcondizione: se esiste, restituisce una Foglia con ID corrispondente e la aggiunge alla lista foglie.
     * 
     * @param id ID della foglia da cercare
     * @return Oggetto Foglia corrispondente all'ID o null se non trovato
     */
    public Foglia caricaFoglia(int id) {
        File fileFoglie = new File(FOGLIE_FILE_NAME);

        if (!fileFoglie.exists()) {
            System.err.println(FOGLIE_FILE_NAME + " does not exist. Cannot load data.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileFoglie))) {
            String line;
            Foglia aggiunto;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 4) continue;

                String[] ind = parts[0].trim().split(":");
                int index = Integer.parseInt(ind[1].trim());
                if (index == id) {
                    String[] nomi = parts[1].trim().split(":");
                    String nome = nomi[1].trim();
                    String fattori[] = parts[2].trim().split(":");
                    float fattore = Float.parseFloat(fattori[1].trim());
                    String[] idf = parts[3].trim().split(":");
                    int idfattore = Integer.parseInt(ind[1].trim());
                    aggiunto = new Foglia(nome, index, fattore, idfattore);
                    return aggiunto;
                }  
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura da file");
        }
        return null;
    }

    /**
     * Metodo per caricare una non-foglia in base all'ID specificato.
     * Precondizione: il file delle non-foglie deve esistere e contenere dati formattati correttamente.
     * Postcondizione: se esiste, restituisce una Nonfoglia con ID corrispondente con tutti i figli caricati.
     * 
     * @param id ID della non-foglia da cercare
     * @return Oggetto Nonfoglia corrispondente all'ID o null se non trovato
     */
    public Nonfoglia caricaNonfoglia(int id) {
        File filenFoglie = new File(FILE_NAME);

        if (!filenFoglie.exists()) {
            System.err.println(FILE_NAME + " does not exist. Cannot load data.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filenFoglie))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 6) continue;

                String[] ind = parts[0].trim().split(":");
                int index = Integer.parseInt(ind[1].trim());
                if (index == id) {
                    String[] nome = parts[1].trim().split(":");
                    String name = nome[1].trim();
                    String[] radice = parts[2].trim().split(":");
                    boolean isRadice = Boolean.parseBoolean(radice[1].trim());
                    String[] typeson = parts[3].trim().split(":");
                    String tipofigli = typeson[1].trim();
                    String[] type = parts[4].trim().split(":");
                    String tipo = type[1].trim();
                    String[] finale = parts[5].trim().split(":");
                    String data = finale[1].trim();
                    String[] parts1 = data.split("\\)\\(");
                    parts1[0] = parts1[0].replace("(", "");
                    parts1[parts1.length - 1] = parts1[parts1.length - 1].replace(")", "");

                    String[] domini = new String[parts1.length];
                    String[] descrizioni = new String[parts1.length];
                    int[] ind_figli_associati = new int[parts1.length];

                    for (int i = 0; i < parts1.length; i++) {
                        String[] elements = parts1[i].split(" - ");
                        domini[i] = elements[0].trim();
                        descrizioni[i] = elements[1].trim();
                        ind_figli_associati[i] = Integer.parseInt(elements[2].trim());
                    }

                    Nonfoglia f = new Nonfoglia(index, name, tipo, domini, descrizioni, isRadice, tipofigli);
                    if (tipofigli.equals("foglie")) {
                        for (int i = 0; i < ind_figli_associati.length; i++) 
                            f.aggiungiFiglio(caricaFoglia(ind_figli_associati[i]), domini[i]);
                    } else {
                        for (int i = 0; i < ind_figli_associati.length; i++) 
                            f.aggiungiFiglio(caricaNonfoglia(ind_figli_associati[i]), domini[i]);
                    }
                    return f;
                }
            }
        } catch (IOException e) {
            System.err.println("errore nella lettura del file");
        }

        return null;
    }

    /**
     * Metodo per caricare tutte le non-foglie radice dal file.
     * Precondizione: il file delle non-foglie deve esistere e contenere dati corretti.
     * Postcondizione: carica tutti gli oggetti Nonfoglia radice nella lista radici.
     */
    public void caricaDaFile() {
        File filenFoglie = new File(FILE_NAME);

        if (!filenFoglie.exists()) {
            try {
                filenFoglie.createNewFile();
            } catch (IOException e) {
                System.err.println("errore nella creazione del file");
            }
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filenFoglie))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 6) continue;

                String[] radice = parts[2].trim().split(":");
                boolean isRadice = Boolean.parseBoolean(radice[1].trim());
                if (!isRadice) continue;

                String[] ind = parts[0].trim().split(":");
                int index = Integer.parseInt(ind[1].trim());
                String[] nome = parts[1].trim().split(":");
                String name = nome[1].trim();
                String[] typeson = parts[3].trim().split(":");
                String tipofigli = typeson[1].trim();
                String[] type = parts[4].trim().split(":");
                String tipo = type[1].trim();
                String[] finale = parts[5].trim().split(":");
                String data = finale[1].trim();
                String[] parts1 = data.split("\\)\\(");
                parts1[0] = parts1[0].replace("(", "");
                parts1[parts1.length - 1] = parts1[parts1.length - 1].replace(")", "");

                String[] domini = new String[parts1.length];
                String[] descrizioni = new String[parts1.length];
                int[] ind_figli_associati = new int[parts1.length];

                for (int i = 0; i < parts1.length; i++) {
                    String[] elements = parts1[i].split(" - ");
                    domini[i] = elements[0].trim();
                    descrizioni[i] = elements[1].trim();
                    ind_figli_associati[i] = Integer.parseInt(elements[2].trim());
                }

                Nonfoglia f = new Nonfoglia(index, name, tipo, domini, descrizioni, isRadice, tipofigli);
                if (tipofigli.equals("foglie")) {
                    for (int i = 0; i < ind_figli_associati.length; i++) 
                        f.aggiungiFiglio(caricaFoglia(ind_figli_associati[i]), domini[i]);
                } else {
                    for (int i = 0; i < ind_figli_associati.length; i++) 
                        f.aggiungiFiglio(caricaNonfoglia(ind_figli_associati[i]), domini[i]);
                }
                radici.add(f);
            }
        } catch (IOException e) {
            System.err.println("errore nella lettura del file");
        }
    }

    /**
     * Metodo statico per ottenere l'istanza singleton della classe ListaGerarchie.
     * Precondizione: Nessuna.
     * Postcondizione: Viene restituita l'unica istanza di ListaGerarchie.
     * Invariante: `instance` rappresenta sempre la stessa istanza.
     * 
     * @return Istanza singleton di ListaGerarchie.
     */
    public static ListaGerarchie getInstance() {
        return instance;
    }

    /**
     * Metodo per ottenere una Foglia specifica in base all'ID.
     * Precondizione: Il file foglie.txt deve esistere ed essere accessibile.
     * Postcondizione: Viene restituito un oggetto Foglia corrispondente all'ID, se esiste.
     * 
     * @param id ID della foglia da cercare.
     * @return Oggetto Foglia con ID corrispondente o null se non trovato.
     */
    public Foglia getFoglia(int id){
        return caricaFoglia(id);
    }

    /**
     * Metodo per aggiungere una nuova gerarchia radice alla lista.
     * Precondizione: L'oggetto Nonfoglia `radice` deve essere valido e non nullo.
     * Postcondizione: La radice viene aggiunta alla lista delle gerarchie.
     * Invariante: `radici` contiene solo oggetti `Nonfoglia`.
     * 
     * @param radice Oggetto Nonfoglia da aggiungere come nuova gerarchia radice.
     */
    public void aggiungiGerarchia(Nonfoglia radice) {
        radici.add(radice);
    }

    /**
     * Metodo per ottenere una gerarchia radice basata su un ID specifico.
     * Precondizione: `radici` deve contenere almeno un elemento.
     * Postcondizione: Viene restituita la Nonfoglia con ID corrispondente, se esiste.
     * 
     * @param id ID della gerarchia radice da ottenere.
     * @return Oggetto Nonfoglia con ID corrispondente o null se non trovato.
     */
    public Nonfoglia getGerarchiaID (int id){
        for (Nonfoglia elem : radici) {
            if (elem.getId() == id)
                return elem;
        }
        return null;
    }

    /**
     * Metodo per salvare tutte le gerarchie radice presenti nella lista.
     * Precondizione: `radici` deve contenere almeno un elemento.
     * Postcondizione: Tutti gli oggetti Nonfoglia nella lista delle radici vengono salvati.
     * Invariante: `radici` contiene solo oggetti `Nonfoglia`.
     */
    public void save() {
        for (Nonfoglia elem : radici) {
            elem.save(); // Chiama il metodo save su ogni non-foglia
        }
    }

    /**
     * Metodo per verificare se un nome è unico nella lista delle radici.
     * Precondizione: `name` non deve essere null o vuoto.
     * Postcondizione: Ritorna true se `name` non esiste nella lista, false altrimenti.
     * 
     * @param name Nome da controllare per l'unicità.
     * @return true se il nome non è presente in `radici`, false altrimenti.
     */
    public boolean isNameClear(String name) {
        for (Nonfoglia elem : radici) {
            if (elem.getNome().equals(name)) return false;
        }
        return true;
    }

    /**
     * Metodo per ottenere la lista completa delle gerarchie radice.
     * Precondizione: Nessuna.
     * Postcondizione: Ritorna la lista `radici`.
     * Invariante: `radici` contiene solo oggetti `Nonfoglia`.
     * 
     * @return Lista di Nonfoglia contenente tutte le radici.
     */
    public ArrayList<Nonfoglia> getGerarchie() {
        return radici;
    }

}
